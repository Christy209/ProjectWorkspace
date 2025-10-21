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

public class HCUACC extends BaseTest {
	 private WebDriver driver;
	 private WebDriverWait wait;

	    
@BeforeClass
public void setup() throws IOException {
	driver = DriverManager.getDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	String userid = DriverManager.getProperty("userid1");
	String password = DriverManager.getProperty("password1");
	DriverManager.login(userid, password);
		}
@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
  @ExcelData(sheetName ="Sheet16",rowIndex= {1})
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

	           
		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),id.getByIndex(2));
		          // WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("beginChqSrlNumTo"))),id.getByIndex(5));
	           driver.findElement(By.id("Accept")).click();

	          // driver.findElement(By.id("OK")).click();
	          
	          try { 
	           driver.findElement(By.id("Submit")).click();
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
	        	   System.out.println("error message"+e);
	           }
	
	       }catch(Exception e) {
	    	   WindowHandle.checkForApplicationErrors(driver);
	       }
	         
}
}

