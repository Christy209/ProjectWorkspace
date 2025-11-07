package CashCreditAccount;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;
import Utilities.WindowHandleJS;

public class CreateCashCreditAccount extends BaseTest{
	String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
    int row = 1;
    String sheetname = "Sheet4"; 


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
	@ExcelData(sheetName = "Sheet4", rowIndex = 1)

	public void createTDAccount(RowData inputData) throws Exception {
		
		
		
			 WindowHandle.slowDown(4);
         WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));
         WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
try {
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
   
			 	WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(2));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("schmCode"))),inputData.getByIndex(3));
	            WebElement body = driver.findElement(By.tagName("body"));
	            Actions actions = new Actions(driver);
	            actions.moveToElement(body, 0, 1).click().perform();
	            
	            WindowHandle.safeClick(driver, wait, By.id("Accept"));
}catch(Exception e) {
  	 WindowHandle.checkForApplicationErrors(driver);
  }

	 			////////////////// General Details ///////////////////
	            
	            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFlg")));
	            WindowHandle.selectDropdownWithJS(driver, dropdown, inputData.getByIndex(5));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqType"))),inputData.getByIndex(6));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqStartDD"))),inputData.getByIndex(7));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqHldyStat"))),inputData.getByIndex(8));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqCalBase"))),inputData.getByIndex(9));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("despatchMode"))),inputData.getByIndex(10));
	            
	            
	            //////////////////////MIS Codes///////////////////////////////////////////////////
	            
	            WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('miscodes').click();");
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectCode"))),inputData.getByIndex(21));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subSectCode"))),inputData.getByIndex(22));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("purpAdv"))),inputData.getByIndex(23));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modeAdv"))),inputData.getByIndex(24));

	 	    			 
	 	    			 ////////////////////////////////////Document Details/////////////////////////////////////////
	 	    	            
	 	    	            WindowHandle.slowDown(2);
	 	    	            ((JavascriptExecutor) driver).executeScript("document.getElementById('documentdetails').click();");
	 	    	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docCode"))),inputData.getByIndex(11));
	 	    	            WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docScanFlg"))),inputData.getByIndex(12));

	 	    	            /////////////////////////////// Related Party Tab///////////////////////////////////////////////////
	 	    	            try {
	 	    	            	WindowHandle.slowDown(2);
	 	    	            	
	 	    	    			WebElement relatedPartyTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relatedpartydetails")));
	 	    	    			JavascriptExecutor js = (JavascriptExecutor) driver;
	 	    	    			js.executeScript("arguments[0].click();", relatedPartyTab);
	 	    	    			

	 	    	    			WindowHandle.slowDown(2);
	 	    	    			WebElement relationCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relnCode']")));
	 	    	    			JavascriptExecutor js1 = (JavascriptExecutor) driver;
	 	    	    			js1.executeScript("arguments[0].value='" + inputData.getByIndex(13) + "';", relationCode);

	 	    	    			
	 	    	    			////////////////////////////////// Second Related Party Tab////////////////////////////////////
	 	    	    			try {

	 	    	    				if (inputData.getByIndex(14).equalsIgnoreCase("Y")) {

	 	    	    					WebElement add = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relParty_AddNew']")));
	 	    	    					((JavascriptExecutor) driver).executeScript("arguments[0].click();", add);
	 	    	    					
	 	    	    					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	 	    	    		            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("relnCode"))),inputData.getByIndex(16));
	 	    	    		            
	 	    	    		           WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("relnType"))),inputData.getByIndex(15));
	 	    	    		           Thread.sleep(500);
	 	    	    					WindowHandleJS wh = new WindowHandleJS(driver);
	 	    	    					 WebElement cifId = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='cifId']")));
	 	    	    					 wh.setValue(cifId, inputData.getByIndex(17));
	 	    	    					((JavascriptExecutor) driver).executeScript("arguments[0].click();", cifId);

	 	    	    					 
//	 	    	    					 act.moveToElement(element).click().perform();

//	 	    	    					WebElement Title = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custTitle']")));
//	 	    	    					Title.sendKeys(inputData.getByIndex(48));


	 	    	    					 Thread.sleep(100);
	 	    	    					WebElement addressLine = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custAddrLine1']")));
	 	    	    					addressLine.sendKeys(inputData.getByIndex(19));

	 	    	    				} else {
	 	    	    					System.out.println("⏩ Skipping 'Add' button click as SecondRelatedFlg is not Y.");
	 	    	    				}
	 	    	    			} catch (Exception e) {
	 	    	    				System.out.println("Error in second Related process:: " + e.getMessage());
	 	    	    			}

	 	    	    		} catch (Exception e) {
	 	    	    			System.out.println("Relationship tab Error : " + e.getMessage());
	 	    	    		}

	 	    	            ///////////////////////////Interest Details//////////////////////////////////
	 	    	            
	 	    	            WindowHandle.slowDown(2);
	 	    	            ((JavascriptExecutor) driver).executeScript("document.getElementById('generaldetails2').click();");
	 	    	            
	 	    	           WebElement dateField = wait.until(ExpectedConditions
	 	    	        	        .presenceOfElementLocated(By.id("nextIntDrCalcDt")));

	 	    	        	// Get the value from Excel by index
	 	    	        	String dateValue = inputData.getByIndex(50);

	 	    	        	// Inject the value into the field with JS
	 	    	        	((JavascriptExecutor) driver).executeScript(
	 	    	        	        "arguments[0].value=arguments[1];", dateField, dateValue);
	 	    	            	/////////////////////////////// Scheme Tab ///////////////////////////////////////////////////
	    		             
	    			 WindowHandle.slowDown(2);
	    			 WebElement tdsch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sbschemedetails")));
	    			 ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tdsch);
	    			 
	    			 WebElement acctHlthCode = wait.until(ExpectedConditions
	    	        	        .presenceOfElementLocated(By.id("acctHlthCode")));

	    	        	// Get the value from Excel by index
	    	        	String acctHlthCodeq = inputData.getByIndex(51);

	    	        	// Inject the value into the field with JS
	    	        	((JavascriptExecutor) driver).executeScript(
	    	        	        "arguments[0].value=arguments[1];", acctHlthCode, acctHlthCodeq);

	    			 WindowHandle.safeClick(driver, wait, By.id("Validate"));
	    			 
//	    			 WindowHandle.slowDown(2);
//	    	            driver.findElement(By.id("linttmacct")).click();
//	    	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenorOfSlabInMnths"))),inputData.getByIndex(48));
//	    	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenorOfSlabInDays"))),inputData.getByIndex(49));
//	    			 
	    			    ////////////////////////////////Account Limits//////////////////////////////////
	 	            
	 	            WindowHandle.slowDown(2);
	 	            ((JavascriptExecutor) driver).executeScript("document.getElementById('acctlmt').click();");
	 	            WindowHandle.slowDown(1);
	 	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("documentDate_ui"))),inputData.getByIndex(26));
	 	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("expiryDate_ui"))),inputData.getByIndex(27));
	 	            WindowHandle.selectDropdownWithJS(driver,wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drawingPowerInd"))),inputData.getByIndex(28));

	 	    		
	 	    	            
	    			 
	 	           try {
	 	                WebElement submit1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	 	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit1);
	 	                
	 	               try {
	 	                  WindowHandle.ValidationFrame(driver);

	 	                 String successLog = WindowHandle.checkForSuccessElements(driver);
	 	                 if(successLog != null){
	 	                     System.out.println("✅ Test Success");
	 	                 }

	 	                  WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='AcctNum']")));
	 	  	    	      String labelText = label.getText(); // Get the text of the label

	 	  	    	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
	 	  	    	      
	 	                  String excelfilePath = excelpath;
	 	                  String sheetName = sheetname;
	 	                  int rowNum = row;
	 	                  String columnName = "Created_AccountID";

	 	                  ExcelUtils2.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);

	 	              } catch (Exception e) {
	 	                  System.out.println("❌ Account id creation Error: " + e.getMessage());
	 	                  Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
	 	              }

	 	              
	 	   			 driver.switchTo().defaultContent();
	 	   				LogOut.performLogout(driver, wait);
	 	   			 
	 	  	
	 	  		} catch (Exception e) {
	 	  		    System.out.println("❌ Error: " + e.getMessage());
	 	  		}
	 	      }


}
