package Loan;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.LogOut;
import Utilities.PopupUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class LoanDisbustment extends BaseTest {
	
	 protected static WebDriver driver;
	    protected static WebDriverWait wait;

	    @BeforeClass
	    public void setup() throws IOException {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	        String userID = DriverManager.getProperty("userid1");
	        String password = DriverManager.getProperty("password1");
	        DriverManager.login(userID, password);
	       
	    }
		@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	    @ExcelData(sheetName = "Sheet1", rowIndex = {1, 6})
	    public void VerifyLoanAccount(RowData createData, RowData disburData) throws Exception {
	        String mainwindow = driver.getWindowHandle();
	        System.out.println("MainWindow :" + mainwindow);

	            WindowHandle.slowDown(3);
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),disburData.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
	            WindowHandle.handleAlertIfPresent(driver);	          
	      try {
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
	        WindowHandle.slowDown(2);
	        WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))),disburData.getByIndex(2));
	    	WindowHandle.slowDown(2);
	    	
			//WindowHandleJS wh = new WindowHandleJS(driver);
			
//			 WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctNum")));
//		        String AcctID = createData.getByHeader("Created_AccountID");
//		        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);
//		        ((JavascriptExecutor) driver).executeScript("document.body.click();");

			WebElement acctNum =wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctNum")));
			//wh.setValue(acctNum, createData.getByIndex(5));
            String AcctID = createData.getByHeader("Created_AccountID");
            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctNum, AcctID);
           // ((JavascriptExecutor) driver).executeScript("document.body.click();");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.querySelector('body').click(); document.querySelector('body').click();");
//	        WebElement body = driver.findElement(By.tagName("body"));
//            Actions actions = new Actions(driver);
//           actions.moveToElement(body, 0, 0).click().perform();
//            
//           WebElement body1 = driver.findElement(By.tagName("body"));
//           Actions actions1 = new Actions(driver);
//          actions1.moveToElement(body1, 0, 0).click().perform();
          
           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("disbAmt"))),disburData.getByIndex(4));  
        // Example usage: handle alerts around a button click
           PopupUtils.handleRepeatedAlerts(driver, 10, 10, () -> {
               driver.findElement(By.id("Accept")).click();
           });

           clickWithRetry(By.id("accept"), "First Accept");
	      }catch(Exception e) {
	    	  WindowHandle.checkForApplicationErrors(driver);
	      }

           // 🔹 STEP 2 → Click GO				
	      
           try {
               WebElement goBtn = driver.findElement(By.xpath("//input[@value='Go' or @name='BTN_GO']"));
               goBtn.click();
           } catch (Exception e) {
               System.out.println("⚠️ Selenium couldn't click Go, trying Robot...");
               Robot robot = new Robot();
               Thread.sleep(1000);
               for (int i = 0; i < 4; i++) {  
                   robot.keyPress(KeyEvent.VK_TAB);
                   robot.keyRelease(KeyEvent.VK_TAB);
                   Thread.sleep(300);
               }
               robot.keyPress(KeyEvent.VK_ENTER);
               robot.keyRelease(KeyEvent.VK_ENTER);
           }

           // 🔹 STEP 3 → Click ACCEPT again
           clickWithRetry(By.id("accept"), "Second Accept");

           By finalAcceptLocator = By.xpath("//input[@id='accept' or @name='BTN_ACCEPT' or @value='Accept']");
           clickWithRetry(finalAcceptLocator, "Final Accept");
           try {
           WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
        		    By.xpath("//input[@id='Submit' or @name='Submit' or @value='Submit']")
        		));
        		((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
        		
        		
        		 try {
                     Thread.sleep(2000);
                     String mainWindow = driver.getWindowHandle();
                     for (String handle : driver.getWindowHandles()) {
                         if (!handle.equals(mainWindow)) {
                             driver.switchTo().window(handle);
                             break;
                         }
                     }

                     try {
                         WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                         WebElement accept = shortWait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
                         accept.click();
                        
                     } catch (Exception inner) {
                         
                         System.out.println("⚠ Selenium could not click Accept, trying Robot...");

                         Robot robot = new Robot();
                         robot.setAutoDelay(500);

                         // Press Tab key to focus "Accept" button
                         robot.keyPress(KeyEvent.VK_TAB);
                         robot.keyRelease(KeyEvent.VK_TAB);

                         // Press Enter to confirm
                         robot.keyPress(KeyEvent.VK_ENTER);
                         robot.keyRelease(KeyEvent.VK_ENTER);
                     }

                     // Switch back to main window
                     driver.switchTo().window(mainWindow);

                 } catch (Exception popupEx) {
                     System.out.println("ℹ️ Popup not present, skipping click");
                 }

             } catch (Exception e) {
                 System.out.println("❌ Error during Submit: " + e.getMessage());
                 WindowHandle.checkForApplicationErrors(driver);
             }
           
           try {
        	   
        	   WindowHandle.ValidationFrame(driver);
        	   String successLog = WindowHandle.checkForSuccessElements(driver);
               if(successLog != null){
                   System.out.println("✅ Test Success");
               }
           driver.switchTo().window(mainwindow);
   		driver.switchTo().defaultContent();
   		LogOut.performLogout(driver, wait);
		}catch(Exception e) {
			 WindowHandle.checkForApplicationErrors(driver);
		}
           }
		private void clickWithRetry(By locator, String stepName) {
		    try {
		        WebElement element = wait.until(ExpectedConditions.refreshed(
		                ExpectedConditions.elementToBeClickable(locator)));
		        element.click();
		        System.out.println("✅ Clicked: " + stepName);
		    } catch (StaleElementReferenceException e) {
		        System.out.println("⚠️ StaleElement at " + stepName + ", retrying...");
		        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		        element.click();
		    } catch (Exception e) {
		        System.out.println("⚠️ Normal click failed for " + stepName + ", using JS...");
		        WebElement element = driver.findElement(locator);
		        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		    }
		}

}
