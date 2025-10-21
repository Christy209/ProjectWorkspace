package Positive;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class AddTransaction extends BaseTest{
	 private WebDriver driver;
	 private WebDriverWait wait;
	 
	 String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
	    int row = 1;
	    String sheetname = "Sheet9"; 
	    
@BeforeClass
public void setup() throws IOException {
	driver = DriverManager.getDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	String userid = DriverManager.getProperty("userid");
	String password = DriverManager.getProperty("password");
	DriverManager.login(userid, password);
		}
 @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName ="Sheet9",rowIndex= {1})
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

	           WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
	           WindowHandle.selectByVisibleText(funCode,id.getByIndex(2));
	         
	           WebElement tranTypeSubType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tranTypeSubType")));
	           WindowHandle.selectByVisibleText(tranTypeSubType,id.getByIndex(3));
	         
	           driver.findElement(By.id("Go")).click();
	           
	       }catch(Exception e) {
	    	   WindowHandle.checkForApplicationErrors(driver);
	       }
	         
	     try {
	    	 
	    	// Wait for the frame where the radio buttons exist
	    	// WindowHandle.ValidationFrame(driver);  // your Finacle frame switch method

	    	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    	 // Locate the "Credit" radio button using name and value
	    	 WebElement creditRadio = wait.until(ExpectedConditions.presenceOfElementLocated(
	    	     By.xpath("//input[@name='tm.pTranType' and @value='C']")
	    	 ));

	    	 // Scroll and click using JavaScript (works even if normal click fails)
	    	 ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", creditRadio);
	    	 ((JavascriptExecutor) driver).executeScript("arguments[0].click();", creditRadio);

	    	 System.out.println("✅ Clicked the 'Credit' radio button successfully");

	    	 
	       WebElement acctID = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId")));
	           String AcctID = id.getByIndex(4);
	           ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);
	           
	           WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refCrncy"))),id.getByIndex(5));        
	           WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refAmt"))),id.getByIndex(6));       
	             WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranParticularsCode"))),id.getByIndex(7));
	             WindowHandle.slowDown(2);
	             WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranParticular"))),id.getByIndex(8)); 
	             driver.findElement(By.id("Post")).click();
	           //  ((JavascriptExecutor) driver).executeScript("document.getElementById('DENOMDTLS').click();");
	             
	             driver.findElement(By.id("Go")).click();
	             String mainWindow = driver.getWindowHandle();
	             driver.switchTo().window(mainWindow);
                 try {
                     WindowHandle.ValidationFrame(driver);
                     WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='tranId']")));
            	      String labelText = label.getText(); // Get the text of the label
            	      Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
            	      System.out.println("Transaction successfully with id: " + labelText);
            	      
            	  
            		 driver.switchTo().defaultContent();
            		 LogOut.performLogout(driver, wait);


            } catch (Exception popupEx) {
                System.out.println("ℹ️ Popup not present, skipping click");
            }
	     }catch(Exception e) {
	    	   WindowHandle.checkForApplicationErrors(driver);
	       }
 }
}

	             
