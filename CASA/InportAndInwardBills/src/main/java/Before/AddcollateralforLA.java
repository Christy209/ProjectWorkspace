package Before;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

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
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class AddcollateralforLA extends BaseTest {
	 protected static WebDriver driver;
	    protected static WebDriverWait wait;
	   
	    String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
	    int row = 1;
	    String sheetname = "Sheet5"; 

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
	    @ExcelData(sheetName = "Sheet5", rowIndex = {1})
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

	            // Loan initiation
	          //  WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempFuncCode"))),rowData.getByIndex(33));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),rowData.getByIndex(2));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("schmCode"))),rowData.getByIndex(3));
	            //WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("glSubHeadCode"))),rowData.getByIndex(3));
	            ((JavascriptExecutor) driver).executeScript("document.body.click();");
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('Submit').click();");
	            }catch(Exception e) {
	            	 WindowHandle.checkForApplicationErrors(driver);
	            }
	            
	            try {
	            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFlg")));
	            WindowHandle.selectDropdownWithJS(driver, dropdown, rowData.getByIndex(4));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqType"))),rowData.getByIndex(5));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqStartDD"))),rowData.getByIndex(6));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqHldyStat"))),rowData.getByIndex(7));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqCalBase"))),rowData.getByIndex(8));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("despatchMode"))),rowData.getByIndex(9));

	           
	            ///////////////Link colletral/////////////////
	            WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('linkcollaterals').click();");
	            WindowHandle.slowDown(1);
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("collateralCode"))),rowData.getByIndex(10));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("entityId"))),rowData.getByIndex(11));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("apportionedAmt"))),rowData.getByIndex(12));
	          
	            ((JavascriptExecutor) driver).executeScript("document.body.click();");
	            driver.findElement(By.id("Validate")).click();
	            
	            /////////////////////// Account Interest///////////////////////////////////
	            
	            WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('laacctinterest').click();");
	            WebElement compRestInd = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("compRestInd")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", compRestInd);
	            ////////////////Loan Details////////////////////////////////////
	            
	            try {
	                WindowHandle.slowDown(2);
	                ((JavascriptExecutor) driver).executeScript("document.getElementById('lasch').click();");
	               WindowHandle.safeSelectOrInput(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loanAmt"))), rowData.getByIndex(13));
	               WindowHandle.safeSelectOrInput(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loanPerdMths"))), rowData.getByIndex(14));
	               WindowHandle.safeSelectOrInput(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loanPerdDays"))), rowData.getByIndex(15));
	               WindowHandle.safeSelectOrInput(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("operacct"))), rowData.getByIndex(16));
	               driver.findElement(By.id("Validate")).click();
	             
	            } catch (Exception e) {
	                System.out.println("⚠️ Loan Details error: " + e.getMessage());
	            }

	            ((JavascriptExecutor) driver).executeScript("document.getElementById('Submit').click();");
	            
	            
	            ///////////////////////////Interest Details//////////////////////////////////
	            
	            WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('laint').click();");

	            ////////////////////////////////Account Limits//////////////////////////////////
	            
	            WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('acctlmt').click();");
	            WindowHandle.slowDown(1);
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("documentDate_ui"))),rowData.getByIndex(17));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("expiryDate_ui"))),rowData.getByIndex(18));
	            WindowHandle.selectDropdownWithJS(driver,wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drawingPowerInd"))),rowData.getByIndex(19));

	            //////////////////////MIS Codes///////////////////////////////////////////////////
	            
	            WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('miscodes').click();");
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectCode"))),rowData.getByIndex(20));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subSectCode"))),rowData.getByIndex(21));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("purpAdv"))),rowData.getByIndex(22));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modeAdv"))),rowData.getByIndex(23));

			///////////////////////////////////Disbursement & Charges/////////////////////////////////////////////
				            
			WindowHandle.slowDown(2);
			((JavascriptExecutor) driver).executeScript("document.getElementById('ladsbsch').click();");
			WindowHandle.slowDown(2);
			((JavascriptExecutor) driver).executeScript("document.getElementById('lachrg').click();");
			
			///////////////////////////////////////Related Party////////////////////////////////////////
			
			WindowHandle.slowDown(2);
			((JavascriptExecutor) driver).executeScript("document.getElementById('relatedpartydetails').click();");
			WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("relnCode"))),rowData.getByIndex(25));
			((JavascriptExecutor) driver).executeScript("document.getElementById('relParty_AddNew').click();");
			WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("relnCode"))),rowData.getByIndex(26));
			WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("relnType"))),rowData.getByIndex(27));
			WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("custTitle"))),rowData.getByIndex(28));
			WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("custName"))),rowData.getByIndex(29));
			WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("custAddrLine1"))),rowData.getByIndex(30));
			
			////////////////////////////////////Document Details/////////////////////////////////////////
			
			WindowHandle.slowDown(2);
			((JavascriptExecutor) driver).executeScript("document.getElementById('documentdetails').click();");
			WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docCode"))),rowData.getByIndex(31));
			WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docScanFlg"))),rowData.getByIndex(32));
			
			////////////////////////////////////Payment Plan///////////////////////////////////////////
			
			WindowHandle.slowDown(3);
			((JavascriptExecutor) driver).executeScript("document.getElementById('laparm').click();");
			WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noOfInstlmnts"))),rowData.getByIndex(33));
			WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("intRestBasis"))),rowData.getByIndex(34));
			 WindowHandle.slowDown(1);
	         WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("intStartDate_ui"))),rowData.getByIndex(35));
	         WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("instlmntStartDate_ui"))),rowData.getByIndex(36));
	         WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eiFreqType"))),rowData.getByIndex(37));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eiFreqStartDate"))),rowData.getByIndex(38));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eiHldyStatus"))),rowData.getByIndex(39));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eiFreqCalBase"))),rowData.getByIndex(40));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("intFreqCalBase"))),rowData.getByIndex(41));
  
			
			////////////////////////////////////Payment Schedule/////////////////////////////
			WindowHandle.slowDown(6);
			((JavascriptExecutor) driver).executeScript("document.getElementById('lamnt').click();");
//			WebElement flowStartDateField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flowStartDate_ui")));
//
//			// Step 1: Clear any existing text
//			flowStartDateField.clear();
//
//			// Step 2: Try setting value using JavaScript
//			WindowHandle.setValueWithJS(driver, flowStartDateField, rowData.getByIndex(42));
//
//			// Step 3: Wait briefly and verify
//			Thread.sleep(500); // gives time for IE DOM to update
//			@SuppressWarnings("deprecation")
//			String enteredValue = flowStartDateField.getAttribute("value");
//
//			// Step 4: Fallback if value not entered
//			if (enteredValue == null || enteredValue.trim().isEmpty()) {
//			    flowStartDateField.clear();
//			    flowStartDateField.sendKeys(rowData.getByIndex(42));
//			}
//
	            }catch(Exception e) {
	            	 WindowHandle.checkForApplicationErrors(driver);
	            }
     /////////////////////////////////////Submit///////////////////////////
            
            try {
                WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);

                try {
                    Thread.sleep(2000);
                    String mainWindow = driver.getWindowHandle();
                    Set<String> allWindows = driver.getWindowHandles();

                    if (allWindows.size() > 1) {
                        for (String handle : allWindows) {
                            if (!handle.equals(mainWindow)) {
                                driver.switchTo().window(handle);
                                break;
                            }
                        }

                        try {
                            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                            WebElement accept = shortWait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
                            accept.click();
                            System.out.println("✅ Accept clicked successfully.");
                        } catch (Exception inner) {
                            System.out.println("⚠ Selenium could not click Accept, trying Robot...");
                            Robot robot = new Robot();
                            robot.setAutoDelay(500);
                            robot.keyPress(KeyEvent.VK_TAB);
                            robot.keyRelease(KeyEvent.VK_TAB);
                            robot.keyPress(KeyEvent.VK_ENTER);
                            robot.keyRelease(KeyEvent.VK_ENTER);
                            System.out.println("✅ Accept clicked via Robot.");
                        }
                    } else {
                        System.out.println("ℹ️ Popup not present, skipping click.");
                    }

                    // Always return to main window (safe placement)
                    driver.switchTo().window(mainWindow);
                    System.out.println("✅ Back to main window.");

                } catch (Exception e) {
                    System.out.println("❌ Error during Submit: " + e.getMessage());
                }

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
            }catch(Exception e) {
           	 WindowHandle.checkForApplicationErrors(driver);
           }


		}
}
