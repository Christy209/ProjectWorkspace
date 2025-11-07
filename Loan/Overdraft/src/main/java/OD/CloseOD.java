package OD;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class CloseOD extends BaseTest{
	  protected static WebDriver driver;
	    protected static WebDriverWait wait;
	   
	    @BeforeClass
	    public  void setup() throws IOException {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	        String userID = DriverManager.getProperty("userid");
	        String password = DriverManager.getProperty("password");

	        DriverManager.login(userID, password);
	        System.out.println("✅ Logged in as: " + userID);
	    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	@ExcelData(sheetName = "Sheet3", rowIndex = {1,6})
	  public void VerifyLoanAccount(RowData createData, RowData verifyData) throws Exception {
		 String mainwindow = driver.getWindowHandle();
       

        try {
            WindowHandle.slowDown(3);
            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),verifyData.getByIndex(1));
            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
            WindowHandle.handleAlertIfPresent(driver);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
try {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
        WindowHandle.slowDown(2);

        WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))),verifyData.getByIndex(2));
        WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
        String AcctID = createData.getByHeader("Created_AccountID");
        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);

        WebElement go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);
    
      //  WindowHandle.captureErrors(driver, 5);
}catch(Exception e) {
	 WindowHandle.checkForApplicationErrors(driver);
}


	WebElement Submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	((JavascriptExecutor) driver).executeScript("arguments[0].click();", Submit);
	try {
			 WindowHandle.ValidationFrame(driver);
			 String successLog = WindowHandle.checkForSuccessElements(driver);
             if(successLog != null){
                 System.out.println("✅ Test Success");
             }

		}catch(Exception e)
		{
			System.out.println("Error Message"+e);
		}
		driver.switchTo().defaultContent();
		driver.switchTo().window(mainwindow);
		LogOut.performLogout(driver, wait);

}

}
