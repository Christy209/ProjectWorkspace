package NegativeTestCase;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
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

public class Verifyunscheduledpaymentofmigratedaccounts extends BaseTest {
	
	
		 String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
		    int row = 15;
		    String sheetname = "Sheet2";
		    
			@BeforeClass
			 public  void setup() throws IOException {
			     driver = DriverManager.getDriver();
			     wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			     String userID = DriverManager.getProperty("userid1");
			     String password = DriverManager.getProperty("password1");
			     DriverManager.login(userID, password);
			 }


		@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	    @ExcelData(sheetName = "Sheet2", rowIndex = {14,15})
	    public void CreateLoan(RowData rowData,RowData id) throws Exception {

	            @SuppressWarnings("unused")
				String mainWindowHandle = driver.getWindowHandle();
	            WindowHandle.slowDown(4);

	            // Menu selection
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),id.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
	try {
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));            
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pymtType"))),id.getByIndex(2));        
	            WebElement tranId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tranId")));
	            String tranId1 = rowData.getByHeader("Tran_Id");
	            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", tranId, tranId1);            
	            WindowHandle.safeClick(driver, wait, By.id("Accept"));
	            
	         }catch(Exception e) {
	            WindowHandle.checkForApplicationErrors(driver);
		}


	try {
	    // Click Submit safely
	    WindowHandle.safeClick(driver, wait, By.id("Submit"));

	    Thread.sleep(2000); // wait for potential popup

	    String mainWindow = driver.getWindowHandle();

	    // Check if a new window (popup) exists
	    Set<String> allWindows = driver.getWindowHandles();
	    if (allWindows.size() > 1) {
	        // Popup exists
	        for (String handle : allWindows) {
	            if (!handle.equals(mainWindow)) {
	                driver.switchTo().window(handle);
	                System.out.println("ℹ️ Popup detected, attempting to click Accept...");

	                try {
	                    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
	                    WebElement accept = shortWait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
	                    accept.click();
	                    System.out.println("✅ Accept clicked via Selenium.");
	                } catch (Exception seleniumFail) {
	                    System.out.println("⚠ Selenium click failed, trying Robot...");
	                    Robot robot = new Robot();
	                    robot.setAutoDelay(500);
	                    robot.keyPress(KeyEvent.VK_TAB);
	                    robot.keyRelease(KeyEvent.VK_TAB);
	                    robot.keyPress(KeyEvent.VK_ENTER);
	                    robot.keyRelease(KeyEvent.VK_ENTER);
	                    System.out.println("✅ Accept clicked via Robot.");
	                }

	                break; // handled popup, exit
	            }
	        }
	    } else {
	        // No popup
	        System.out.println("ℹ️ No popup detected, continuing...");
	    }

	    // Switch back to main window
	    driver.switchTo().window(mainWindow);
	    System.out.println("✅ Back to main window.");
			
			} catch (Exception e) {
			    System.err.println("⚠ Error after Submit: " + e.getMessage());
			    WindowHandle.checkForApplicationErrors(driver);
			}

		            try {
		            	WindowHandle.ValidationFrame(driver);

		   			 String successLog = WindowHandle.checkForSuccessElements(driver);
		   	            if(successLog != null){
		   	                System.out.println("✅ Test Success");
		   	            }
		            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='tranId']")));
		  	      String labelText = label.getText(); // Get the text of the label

		  	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:  label text is empty!");
		  	      
		          String excelfilePath = excelpath;
		          String sheetName = sheetname;
		          int rowNum = row;
		          String columnName = "Tran_Id";

		          ExcelUtils2.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);
		            }catch(Exception e) {
		            	System.out.println("Unable to find Tranid:"+e);
		            }
		            
		            driver.switchTo().defaultContent();
					 LogOut.performLogout(driver, wait);

	}
	}


