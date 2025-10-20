package NegativeTestCase;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class HLARA extends BaseTest {

	@BeforeClass
    public  void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String userID = DriverManager.getProperty("userid");
        String password = DriverManager.getProperty("password");
        DriverManager.login(userID, password);
    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet2", rowIndex = {9})
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
            
            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))),rowData.getByIndex(2));
            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rescheduleMethod"))),rowData.getByIndex(3));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctNum"))),rowData.getByIndex(4));
            WindowHandle.safeClick(driver, wait, By.id("Accept"));
            }catch(Exception e) {
               	 WindowHandle.checkForApplicationErrors(driver);
               }
         
            try {
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("noOfInstlmnts"))),rowData.getByIndex(5));
//            WebElement yesRadio = driver.findElement(By.cssSelector("input[name='lareprm.applyIntUptoDate'][value='Y']"));
//            yesRadio.click();                     
            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hldyPerdIntFlg"))),rowData.getByIndex(6));
            WindowHandle.safeClick(driver, wait, By.id("Validate"));
            }catch(Exception e) {
              	 WindowHandle.checkForApplicationErrors(driver);
              }
            
            //////////////Schedule History//////////////////
        	WindowHandle.slowDown(2);
			WebElement larehist = wait.until(ExpectedConditions.elementToBeClickable(By.id("larehist")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", larehist);
			larehist.click();
			
			
			
			  //////////////Advance Payement Parameters//////////////////
        	WindowHandle.slowDown(2);
			WebElement laadvparm = wait.until(ExpectedConditions.elementToBeClickable(By.id("laadvparm")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", laadvparm);
			laadvparm.click();
			
			  //////////////Payement Parameteres//////////////////
			WindowHandle.slowDown(2);
			WebElement lareprm = wait.until(ExpectedConditions.elementToBeClickable(By.id("lareprm")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", lareprm);
			lareprm.click();
			try {
			    Thread.sleep(2000);
			    String mainWindow = driver.getWindowHandle();
			    Set<String> allWindows = driver.getWindowHandles();

			    if (allWindows.size() > 1) {
			        // Popup detected
			        for (String handle : allWindows) {
			            if (!handle.equals(mainWindow)) {
			                driver.switchTo().window(handle);
			                System.out.println("ℹ️ Popup detected, switched to popup window.");
			                break;
			            }
			        }

			        try {
			            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
			            WebElement noBtn = null;

			            try {
			                noBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
			                    By.xpath("//input[@value='No' or @id='No' or contains(@onclick,'No')]")));
			            } catch (Exception e1) {
			                noBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
			                    By.xpath("//button[text()='No' or contains(@value,'No')]")));
			            }

			            if (noBtn != null) {
			                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noBtn);
			                System.out.println("✅ Clicked 'No' successfully via JavaScript.");
			            } else {
			                System.out.println("⚠️ 'No' button not found via Selenium, trying Robot...");
			                Robot robot = new Robot();
			                robot.setAutoDelay(500);
			                for (int i = 0; i < 2; i++) {
			                    robot.keyPress(KeyEvent.VK_TAB);
			                    robot.keyRelease(KeyEvent.VK_TAB);
			                }
			                robot.keyPress(KeyEvent.VK_ENTER);
			                robot.keyRelease(KeyEvent.VK_ENTER);
			                System.out.println("✅ Clicked 'No' using Robot simulation.");
			            }

			        } catch (Exception inner) {
			            System.out.println("⚠️ Could not handle popup properly: " + inner.getMessage());
			        }

			        // Always return to main window after handling popup
			        driver.switchTo().window(mainWindow);
			        System.out.println("🔁 Returned to main window successfully.");
			    } else {
			        // No popup detected
			        System.out.println("ℹ️ No popup detected, continuing without 'No' click.");
			    }

			} catch (Exception e) {
			    System.out.println("❌ Error while checking for popup: " + e.getMessage());
			}

			
			// ✅ After popup is closed and we’ve switched to the main window:
			try {
			    WindowHandle.ValidationFrame(driver);
 
			    WindowHandle.slowDown(2);
			    WebElement laprepay = wait.until(ExpectedConditions.elementToBeClickable(By.id("laprepay")));
			    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", laprepay);
			    laprepay.click();
	
			    WindowHandle.safeClick(driver, wait, By.id("Submit"));
			    }catch(Exception e) {
			    	WindowHandle.checkForApplicationErrors(driver);
			    }
//			} catch (Exception e) {
//			    System.out.println("❌ Error after popup while locating laprepay: " + e.getMessage());
//			    throw e; // Let TestNG mark failure properly
//			}
			
//			 WebElement row = driver.findElement(By.cssSelector("tr.textfielddisplaylabel1"));
//	            System.out.println("Row text: " + row.getText());
//	            @SuppressWarnings("unused")
//				String rowText = row.getText();
			
			 WindowHandle.ValidationFrame(driver);
      	   String successLog = WindowHandle.checkForSuccessElements(driver);
             if(successLog != null){
                 System.out.println("✅ Test Success");
             }
	            driver.switchTo().defaultContent();
			    LogOut.performLogout(driver, wait);
			       

}
}