package Before;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class HLADGEN extends BaseTest{
	
	@BeforeClass
    public  void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String userID = DriverManager.getProperty("userid");
        String password = DriverManager.getProperty("password");
        DriverManager.login(userID, password);
    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet2", rowIndex = {8})
    public void CreateLoan(RowData rowData) throws Exception {

            @SuppressWarnings("unused")
			String mainWindowHandle = driver.getWindowHandle();
            WindowHandle.slowDown(4);

            // Menu selection
            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),rowData.getByIndex(1));
            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
            
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("report_to"))),rowData.getByIndex(2));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acct_from"))),rowData.getByIndex(3));
            ((JavascriptExecutor) driver).executeScript("document.body.click();");
            
            WindowHandle.safeClick(driver, wait, By.id("Submit"));
		}catch(Exception e) {
		  	 WindowHandle.checkForApplicationErrors(driver);
		  }
				try {
		    WindowHandle.ValidationFrame(driver);
		
		    String successLog = WindowHandle.checkForSuccessElements(driver);
		    if(successLog != null){
		        System.out.println("✅ Test Success");
		    }
		            driver.switchTo().defaultContent();
		            LogOut.performLogout(driver, wait);
		
				}catch(Exception e) {
			System.out.println("error MEssage"+e);
		}
		        

}
}