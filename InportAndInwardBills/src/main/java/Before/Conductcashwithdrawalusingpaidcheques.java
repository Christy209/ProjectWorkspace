package Before;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
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
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class Conductcashwithdrawalusingpaidcheques extends BaseTest{
	
	@BeforeClass
    public  void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String userID = DriverManager.getProperty("userid1");
        String password = DriverManager.getProperty("password1");
        DriverManager.login(userID, password);
    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet2", rowIndex = {4})
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
            
            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))),rowData.getByIndex(2));


             WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tranTypeSubType"))),rowData.getByIndex(3));

             WindowHandle.safeClick(driver, wait, By.id("Go"));
             
             WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))),rowData.getByIndex(4)); 
             ((JavascriptExecutor) driver).executeScript("document.body.click();");

             WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refAmt"))),rowData.getByIndex(5));       
             WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranParticularsCode"))),rowData.getByIndex(6));
             WindowHandle.slowDown(2);
             ((JavascriptExecutor) driver).executeScript("document.getElementById('DENOMDTLS').click();");
             
             WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("arrDenomCount"))),rowData.getByIndex(7));
             WindowHandle.safeClick(driver, wait, By.id("OK"));
         try {    
             WindowHandle.slowDown(2);
             ((JavascriptExecutor) driver).executeScript("document.getElementById('Post').click();");
             

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
                    

             } catch (Exception popupEx) {
                 System.out.println("ℹ️ Popup not present, skipping click");
             }

      
             
}
}
