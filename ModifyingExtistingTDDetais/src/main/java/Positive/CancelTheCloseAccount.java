package Positive;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class CancelTheCloseAccount extends BaseTest  {
	

	 private WebDriver driver;
	 private WebDriverWait wait;
	 
	 String excelpath = System.getProperty("user.dir") + "/resources/TestData.xlsx";
	    int row = 1;
	    String sheetname = "Sheet2"; 
		    
	@BeforeClass
	public void setup() throws IOException {
		driver = DriverManager.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		String userid = DriverManager.getProperty("userid");
		String password = DriverManager.getProperty("password");
		DriverManager.login(userid, password);
			}
	@Test(dataProvider ="testcase", dataProviderClass = Dataproviders.class)
	@ExcelData(sheetName ="Sheet2",rowIndex= {1,7} )
	public void closing(RowData verify, RowData id) throws Exception {

	    String mainWindowHandle = driver.getWindowHandle();
	    WindowHandle.handleAlertIfPresent(driver);
	    WindowHandle.slowDown(4);

	    // Select menu
	    WebElement menu = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect")));
	    WindowHandle.setValueWithJS(driver, menu, id.getByIndex(1));
	    WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	    try {
	        // Switch to frames
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	        // Fill function code and account ID
	        WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
	        WindowHandle.selectByVisibleText(funCode, id.getByIndex(2));

	        WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
	        String AcctID = verify.getByHeader("Created_AccountID");
	        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);

	        // 1️⃣ First submit
	        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
	        Thread.sleep(2000); // allow popup to appear

	        // 2️⃣ Handle popup if it appears
	        Set<String> allWindows = driver.getWindowHandles();
	        if (allWindows.size() > 1) {
	            System.out.println("ℹ️ Popup detected after submit.");
	            for (String handle : allWindows) {
	                if (!handle.equals(mainWindowHandle)) {
	                    driver.switchTo().window(handle);
	                    break;
	                }
	            }

	            try {
	                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
	                WebElement accept = shortWait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accept);
	                System.out.println("✅ Accept clicked successfully (Selenium/JS).");
	            } catch (Exception inner) {
	                System.out.println("⚠ Selenium could not click Accept, using Robot...");
	                Robot robot = new Robot();
	                robot.setAutoDelay(500);
	                robot.keyPress(KeyEvent.VK_TAB);
	                robot.keyRelease(KeyEvent.VK_TAB);
	                robot.keyPress(KeyEvent.VK_ENTER);
	                robot.keyRelease(KeyEvent.VK_ENTER);
	                System.out.println("✅ Accept clicked via Robot.");
	            }

	            // Close popup if still open
	            try { driver.close(); } catch (Exception ignored) {}
	        } else {
	            System.out.println("ℹ️ No popup detected, continuing...");
	        }

	        driver.switchTo().window(mainWindowHandle);
	        System.out.println("✅ Back to main window.");

	        // 3️⃣ Optional second submit (validation frame)
	        try {
	            WindowHandle.ValidationFrame(driver);
	            if (driver.findElements(By.id("Submit")).size() > 0) {
	                WebElement secondSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondSubmit);
	                System.out.println("⚡ Second submit clicked.");
	            }
	        } catch (Exception e) {
	            System.out.println("⚠ No second submit detected: " + e.getMessage());
	        }

	        // 4️⃣ Check for success message
	        try {
	            WindowHandle.ValidationFrame(driver);
	            String successLog = WindowHandle.checkForSuccessElements(driver);
	            if (successLog != null) {
	                System.out.println("✅ Test Success: " + successLog);
	            }
	        } catch (Exception e) {
	            System.out.println("⚠ Could not verify success: " + e.getMessage());
	        }

	        driver.switchTo().defaultContent();
	        LogOut.performLogout(driver, wait);

	    } catch (Exception e) {
	        WindowHandle.checkForApplicationErrors(driver);
	        System.out.println("Error Message: " + e.getMessage());
	    }
	}
}