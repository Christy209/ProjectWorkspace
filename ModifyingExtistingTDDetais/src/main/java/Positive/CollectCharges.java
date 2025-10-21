package Positive;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class CollectCharges extends BaseTest {
	   private WebDriver driver;
	    private WebDriverWait wait;
	    
	    @BeforeClass
	    public void setup() throws IOException {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        String userid = DriverManager.getProperty("userid");
	        String password = DriverManager.getProperty("password");
	        DriverManager.login(userid, password);
	    }

	    @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	    @ExcelData(sheetName = "Sheet1", rowIndex = {1,6})
	    public void  FUNDING(RowData id,RowData charge) throws Exception {
        try {
		 	
	        
  	WindowHandle.slowDown(4);
	    	
	    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),charge.getByIndex(1));

	    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

            
        } catch (Exception e) {
            throw new RuntimeException("Menu selection failed: " + e.getMessage(), e);
        }
try {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")))
            .sendKeys(charge.getByIndex(2));

        WebElement acctField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
        acctField.sendKeys(id.getByHeader("Created_AccountID"));
       //cctField.sendKeys("003000500000269");
		
		WebElement drAcctId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drAcctId")));
		drAcctId.sendKeys(id.getByHeader("Created_AccountID"));

        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Go")));
        goButton.click();
}catch(Exception e) {
	System.out.println("Error Message"+e);
	   WindowHandle.checkForApplicationErrors(driver);
}
try {
		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
 		submit.click();
 }catch(Exception e) {
	   WindowHandle.checkForApplicationErrors(driver);
}
 		WindowHandle.ValidationFrame(driver);

        String successLog = WindowHandle.checkForSuccessElements(driver);
        if(successLog != null){
            System.out.println("✅ Test Success");
        }
 		WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label#compField")));	    
 		Assert.assertFalse(label == null ||label.getText().trim().isEmpty(),"Test Failed: Closure Charges message is empty!");
 		Assert.assertTrue(label.getText().contains("Closure Charges Collected"),"Test Failed: Closure Charges message is not as expected!");	    

    
	    driver.switchTo().defaultContent();
		LogOut.performLogout(driver, wait);
}
}