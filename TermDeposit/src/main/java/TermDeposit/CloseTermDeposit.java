package TermDeposit;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.Alert;
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

public class CloseTermDeposit extends BaseTest{

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
	    @ExcelData(sheetName = "Sheet1", rowIndex = {1,6})
	    public void colsingtdaccount(RowData id,RowData close) {
	        try {
//	            driver.switchTo().defaultContent();
//	            driver.switchTo().frame("loginFrame");

	       	 WindowHandle.slowDown(4);
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),close.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
	      

	            // ✅ Handle alert right after Go click
	            try {
	                WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
	                alertWait.until(ExpectedConditions.alertIsPresent());
	                Alert alert = driver.switchTo().alert();
	                System.out.println("🔔 Alert Text: " + alert.getText());
	                alert.accept();
	                System.out.println("✅ Alert Accepted");
	            } catch (Exception e) {
	                System.out.println("ℹ️ No alert appeared: " + e.getMessage());
	            }

	           
	          try {
	        	  WindowHandle.slowDown(2);	            
	          
	    
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))),close.getByIndex(2));


	            WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
	            String AcctID =id.getByHeader("Created_AccountID");
	            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);
//	            WebElement colsevaluedate = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clsValDate_ui")));
//	            colsevaluedate.getByIndex(3);

	            
	           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("clsValDate_ui"))),close.getByIndex(3));
	          //  WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clsValDate"))),close.("15-03-2025"));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("wthDrwlAmt"))),close.getByIndex(4));
	           
	            WebElement Accept = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Accept")));
	            Accept.click();
	          }catch(Exception e) {
	        	  WindowHandle.checkForApplicationErrors(driver);
	          }

	            try {
	                WebElement caactdclstab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("caactdcls")));
	                caactdclstab.click();

//	                WebElement closeMode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("closeMode")));
//	                closeMode.sendKeys(close.getByIndex(3));

//	                WebElement repymntAcctId = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("repymntAcctId")));
//	                repymntAcctId.sendKeys("");
//
//	                WebElement closureInd = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("closureInd")));
//	                closureInd.sendKeys("");
//
//	                WebElement clsAmt = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clsAmt")));
//	                clsAmt.sendKeys("");

	            } catch (Exception e) {
	                System.out.println("Error (closure details): " + e.getMessage());
	            }

	            try {
	                WebElement caactdexcptab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("caactdexcp")));
	                caactdexcptab.click();

	               // WebElement clsReasonCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clsReasonCode")));
	                //clsReasonCode.sendKeys("");

	                WebElement Submit = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Submit")));
	                Submit.click();
	                
	                @SuppressWarnings("unused")
					String mainWindow = driver.getWindowHandle();
	    	    	WindowHandle.handlePopupIfExists(driver);
	    	    	 WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Accept")));
	    	    	 acceptButton.click();
	    	    	 
	            } catch (Exception e) {
	                System.out.println("Error (exception tab): " + e.getMessage());
	            }

try {
	        	//WebElement Submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	        	//((JavascriptExecutor) driver).executeScript("arguments[0].click();", Submit);
	                  driver.switchTo().defaultContent();
	                driver.switchTo().frame("loginFrame");
	                driver.switchTo().frame("CoreServer");
	                driver.switchTo().frame("FINW");
	               
	                String successLog = WindowHandle.checkForSuccessElements(driver);
	                if(successLog != null){
	                    System.out.println("✅ Test Success");
	                }

	                WebDriverWait resultWait = new WebDriverWait(driver, Duration.ofSeconds(10));

	                // ✅ Step 1: Get closed account number
	                WebElement accIdElement = resultWait.until(ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//td[normalize-space(text())='A/c. ID']/following-sibling::td[1]")));
	                String fullAcctText = accIdElement.getText().trim();
	                String closedAcctNo = fullAcctText.split(" ")[0].trim();

	                Assert.assertFalse(closedAcctNo == null || closedAcctNo.isEmpty(),
	                    "❌ Test Failed: Closed A/c. ID is empty!");
	                System.out.println("✅ TD Account Closed Successfully..! AcctNo: " + closedAcctNo);

	                try {
	                    String excelFilePath = System.getProperty("user.dir") + "/Resource/TD Testcase.xlsx";
	                    String sheetName = "Sheet1";
	                    int rowNum = 10;
	                    String columnName = "Created_AccountID";

	                    if (closedAcctNo != null && !closedAcctNo.trim().isEmpty()) {
	                        ExcelUtils2.updateExcel(excelFilePath, sheetName, rowNum, columnName, closedAcctNo);
	                        System.out.println("📝 Closed Account Number written to Excel: " + closedAcctNo);
	                    } else {
	                        System.out.println("⚠️ Account number is null or empty. Skipping Excel update.");
	                    }
	                } catch (Exception e) {
	                    System.err.println("❌ Failed to write to Excel: " + e.getMessage());
	                    e.printStackTrace();
	                }


	                // ✅ Step 3: Click OK (may close window)
	                try {
	                	
	                	WebElement messageLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("compField")));
	                	String successMessage = messageLabel.getText();
	                	System.out.println("✅ Success Message: " + successMessage);
	                    WebElement okBtn = wait.until(ExpectedConditions.elementToBeClickable(
	                        By.xpath("//input[@type='button' and @value='OK']")));
	                    okBtn.click();
	                    System.out.println("✅ OK button clicked.");
	                } catch (Exception e) {
	                    System.out.println("⚠️ OK button not found or already handled: " + e.getMessage());
	                }

	                // ✅ Step 4: Logout (only if window is still open)
	                try {
	                    if (!driver.getWindowHandles().isEmpty()) {
	                        LogOut.performLogout(driver, wait);
	                        System.out.println("✅ Logged out successfully.");
	                    } else {
	                        System.out.println("⚠️ Window already closed. Logout skipped.");
	                    }
	                } catch (Exception e) {
	                    System.out.println("❌ Logout failed: " + e.getMessage());
	                }

	            } catch (Exception e) {
	                System.out.println("❌ Error during result validation or Excel update: " + e.getMessage());
	                e.printStackTrace();
	            }

	        } catch (Exception e) {
	            System.out.println("❌ Error in closing TD account flow: " + e.getMessage());
	        }
	    }
	}

