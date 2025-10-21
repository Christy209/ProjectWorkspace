package Positive;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
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
import Utilities.RowData;
import Utilities.WindowHandle;

public class VerifyCharge extends BaseTest {
	
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
	    @ExcelData(sheetName = "Sheet1", rowIndex = {1,9})
	    public void  FUNDING(RowData id,RowData charge) throws Exception {	 	
	    	  try {
	  		 	
	  	        
	    		  	WindowHandle.slowDown(4);
	    			    	
	    			    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),charge.getByIndex(1));

	    			    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	    			    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	    		            
	    		        } catch (Exception e) {
	    		            throw new RuntimeException("Menu selection failed: " + e.getMessage(), e);
	    		        }
	            WindowHandle.ValidationFrame(driver);
	        	try {
	        		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")))
	                .sendKeys(charge.getByIndex(2));

			        WebElement acctField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
			        acctField.sendKeys(id.getByHeader("Created_AccountID"));
			       //cctField.sendKeys("003000500000269");
					
//					WebElement drAcctId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drAcctId")));
//					drAcctId.sendKeys(id.getByHeader("Created_AccountID"));
//					
					 driver.findElement(By.id("Go")).click();
					 driver.findElement(By.id("Go")).click();

//			        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Go")));
//			        js.executeScript("arguments[0].click();", goButton);
//			       
				} catch (Exception e) {
					
				}
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit"))).click();
	        	
	        	WindowHandle.handlePopupIfExists(driver);
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept"))).click();
	        	
	        	//driver.switchTo().window(mainWindow);
	        	
	        	WindowHandle.ValidationFrame(driver);
	        	String AcctID = id.getByHeader("Created_AccountID");
	        	WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label#compField")));
	        	String labelText = label.getText().trim();
	        	Assert.assertFalse(label == null ||labelText.isEmpty(),"Test Failed: Closure Charges message is empty!");
	        	Assert.assertTrue(labelText.contains(AcctID),"Test Failed: Closure Charges message does not contain account IDs!");
	        	String keyPhrase = "Verification of A/c. Closure Charges";
	        	String closingConfirmation = "Done successfully";
	        	Assert.assertTrue(labelText.contains(keyPhrase) && labelText.contains(closingConfirmation),
	        			"Test Failed: Closure Charges message does not reflect successful closure!");       
	    }


}
