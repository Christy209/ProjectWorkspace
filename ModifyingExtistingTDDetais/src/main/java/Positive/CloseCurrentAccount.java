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

public class CloseCurrentAccount extends BaseTest{

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
@Test(dataProvider ="testcase", dataProviderClass = Dataproviders.class)
  @ExcelData(sheetName ="Sheet2",rowIndex= {1,6} )
  public void closing(RowData input,RowData id) throws Exception 
{
	       WindowHandle.handleAlertIfPresent(driver);
	       
	   	WindowHandle.slowDown(4);
  	
  	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),id.getByIndex(1));

  	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
  	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	       try {
	           // Switch to required frames
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	           WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
	           WindowHandle.selectByVisibleText(funCode,id.getByIndex(2));
	           
//	           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	           WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
//
//	           Select select = new Select(dropdown);
//	           select.selectByIndex(2);  // selects the 3rd option (index starts from 0)

	         //  WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))),id.getByIndex(2));
	           WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
	           String AcctID = input.getByHeader("Created_AccountID");
	           ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);

	            WindowHandle.safeClick(driver, wait, By.id("Submit"));
	}catch(Exception e) {
		 WindowHandle.checkForApplicationErrors(driver);
	}



	       WindowHandle.safeClick(driver, wait, By.id("Submit"));
	    	try {
	    			 WindowHandle.ValidationFrame(driver);
//	    		        String  accountId=WindowHandle.getAccountId(driver);
//	    		        if (accountId != null) {
//	    		            System.out.println("✅ Extracted Account ID: " + accountId);
//	    		        } else {
//	    		            System.out.println("❌ Account ID not found!");
//	    		        }

	                 String successLog = WindowHandle.checkForSuccessElements(driver);
	                 if(successLog != null){
	                     System.out.println("✅ Test Success");
	                 }

	    		}catch(Exception e)
	    		{
	    			System.out.println("Error Message"+e);
	    		}
	    		driver.switchTo().defaultContent();
	    		LogOut.performLogout(driver, wait);

	    }

	    }
	

