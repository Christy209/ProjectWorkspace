package TermDeposit;

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
import Utilities.RowData;
import Utilities.WindowHandle;

public class ModificationOfTermDepositAftersVerification extends BaseTest{
	

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
	    @ExcelData(sheetName = "Sheet1", rowIndex = {1,4})
	    public void ModifyAfterVerification(RowData id,RowData close) {
	        try {
//	            driver.switchTo().defaultContent();
//	            driver.switchTo().frame("loginFrame");

	       	 WindowHandle.slowDown(4);
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),close.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
	            
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mode"))),close.getByIndex(2));

		  		   WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempForacid")));
		  		   String AcctID = id.getByHeader("Created_AccountID");
		  		   ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);



		  		   WebElement acceptButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Accept")));
		  		   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
		  		   System.out.println("✅ Accept button clicked successfully");
		  		   WindowHandle.captureErrors(driver, 5);
		  		   
	      

}catch(Exception e) {
	System.out.println("Error Message"+e);
}
}
}