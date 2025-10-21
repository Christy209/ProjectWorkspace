package Positive;

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

public class HCCFM extends BaseTest {
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
    @ExcelData(sheetName ="Sheet15",rowIndex= {1})
    public void Addfreeze(RowData id) throws Exception {
	       WindowHandle.handleAlertIfPresent(driver);
	    
	   	WindowHandle.slowDown(4);
    	
    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),id.getByIndex(1));

    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	       try {
	           // Switch to required frames
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	           WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("func")));
	           WindowHandle.selectByVisibleText(funCode,id.getByIndex(2));
	           
		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),id.getByIndex(3));
		          // WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("beginChqSrlNumTo"))),id.getByIndex(5));
	           driver.findElement(By.id("Accept")).click();
	           
	          ///////////////////////////////////General Details///////////////////////////////
	           driver.findElement(By.id("Validate")).click();
	           
	           ////////////////////////////Buyer seller details/////////////////////////////////////
	           try {
	        	   WindowHandle.slowDown(2);
	                ((JavascriptExecutor) driver).executeScript("document.getElementById('ccfmbslimit').click();");
	                driver.findElement(By.id("Validate")).click();
	           }catch(Exception e) {
	        	   WindowHandle.checkForApplicationErrors(driver);
	           }
	           driver.findElement(By.id("Submit")).click();
	        //   driver.findElement(By.id("Submit")).click();
	           try {
	        	   WindowHandle.ValidationFrame(driver);

	                String successLog = WindowHandle.checkForSuccessElements(driver);
	                if(successLog != null){
	                    System.out.println("✅ Test Success");
	                }
	           }catch(Exception e) {
	        	   System.out.println("error message"+e);
	           }
	      	 driver.switchTo().defaultContent();
    		 LogOut.performLogout(driver, wait);
	           
	       }catch(Exception e) {
	    	   WindowHandle.checkForApplicationErrors(driver);
	       }
	         
 }
}

