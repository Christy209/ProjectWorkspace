package Before;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class Unscheduledpaymentofmigratedaccounts extends BaseTest {
	 String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
	    int row = 14;
	    String sheetname = "Sheet2"; 
	    
	@BeforeClass
 public  void setup() throws IOException {
     driver = DriverManager.getDriver();
     wait = new WebDriverWait(driver, Duration.ofSeconds(20));
     String userID = DriverManager.getProperty("userid");
     String password = DriverManager.getProperty("password");
     DriverManager.login(userID, password);
 }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
 @ExcelData(sheetName = "Sheet2", rowIndex = {14})
 public void CreateLoan(RowData rowData) throws Exception {

         @SuppressWarnings("unused")
			String mainWindowHandle = driver.getWindowHandle();
         WindowHandle.slowDown(4);

         // Menu selection
         WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),rowData.getByIndex(1));
         WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));  
         try {
         WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pymtType"))),rowData.getByIndex(2));
         WindowHandle.safeClick(driver, wait, By.id("Accept"));
         }catch(Exception e) {           
         WindowHandle.checkForApplicationErrors(driver);
         }
         try {
         WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loanAcctId"))),rowData.getByIndex(3));
         WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("crAmt"))),rowData.getByIndex(4));            
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
        	                System.out.println("ℹ️ Popup detected, attempting to click OK...");

        	                try {
        	                    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        	                    // OK button usually has value='OK' and type='button'
        	                    WebElement okButton = shortWait.until(ExpectedConditions.elementToBeClickable(
        	                        By.xpath("//input[@type='button' and translate(normalize-space(@value),'ok','OK')='OK']")
        	                    ));
        	                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okButton);
        	                    System.out.println("✅ OK clicked via JavaScript.");
        	                } catch (Exception seleniumFail) {
        	                    System.out.println("⚠ Selenium click failed, trying Robot keypress...");
        	                    Robot robot = new Robot();
        	                    robot.setAutoDelay(500);
        	                    // Tab may not be needed if OK is focused, but keep for safety
        	                    robot.keyPress(KeyEvent.VK_TAB);
        	                    robot.keyRelease(KeyEvent.VK_TAB);
        	                    robot.keyPress(KeyEvent.VK_ENTER);
        	                    robot.keyRelease(KeyEvent.VK_ENTER);
        	                    System.out.println("✅ OK clicked via Robot.");
        	                }

        	                // Close popup if it remains open
        	                try {
        	                    driver.close();
        	                } catch (Exception ignored) {}

        	                break; // handled popup
        	            }
        	        }
        	    } else {
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
         WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='tranId']")));
	      String labelText = label.getText(); // Get the text of the label

	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:  label text is empty!");
	      
       String excelfilePath = excelpath;
       String sheetName = sheetname;
       int rowNum = row;
       String columnName = "Tran_Id";

       ExcelUtils2.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);
         }
               
         }catch(Exception e) {
         	System.out.println("Unable to find Tranid:"+e);
         }
         
         driver.switchTo().defaultContent();
		 LogOut.performLogout(driver, wait);


         }
	}
        


