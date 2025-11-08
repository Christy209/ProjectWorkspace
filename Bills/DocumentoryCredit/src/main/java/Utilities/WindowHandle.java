package Utilities;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class WindowHandle {
	
    public static void handlePopupIfExists(WebDriver driver) throws Exception {
        //String mainWindow = driver.getWindowHandle(); // Store the main window handle

        String mainWindowHandle = driver.getWindowHandle();
        System.out.println("Main Window: " + mainWindowHandle);

        // Wait for new window to open
        slowDown(2);  // Use explicit wait in real tests

        // Get all window handles
        Set<String> allWindows = driver.getWindowHandles();
        System.out.println("Total windows: " + allWindows.size());

        // Switch to new window
        for (String window : allWindows) {
            if (!window.equals(mainWindowHandle)) {
                driver.switchTo().window(window);
                System.out.println("Switched to new window: " + window);
                break;
            }
        }

        // Verify the current window handle
        System.out.println("Current Window Handle after switch: " + driver.getWindowHandle());

    }
    
    public static void handleAlertIfPresent(WebDriver driver ) {
        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Short wait
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            
            System.out.println("⚠️ Alert Message: " + alert.getText());
            alert.accept(); // Dismiss the alert
        } catch (TimeoutException ignored) {
            // No alert means we can continue
        }
    }
    
	public static void selectByVisibleText(WebElement element, String visibleText) {
	    try {
	        Select dropdown = new Select(element);
	        dropdown.selectByVisibleText(visibleText);
	    } catch (NoSuchElementException e) {
	        System.out.println("Option '" + visibleText + "' not found in dropdown.");
	    } catch (Exception e) {
	        System.out.println("Error selecting from dropdown: " + e.getMessage());
	    }	
	}
	public static void selectDropdownIfValuePresent(WebDriver driver, WebDriverWait wait, By locator, String value) {
		try {
	        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
	        Select select = new Select(dropdown);

	        if (value != null && !value.trim().isEmpty()) {
	            select.selectByVisibleText(value.trim());
	        } else {
	            try {
	                // Try to select default/empty option
	                select.selectByVisibleText("--Select--");
	            } catch (Exception e) {
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("❌ Error handling dropdown " + locator + ": " + e.getMessage());
	    }
	}

    public static void slowDown(int seconds) {
        try {
            Thread.sleep(seconds * 1000L); // Convert to milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Slow down interrupted: " + e.getMessage());
        }
    }
    public static void goToMainFrame(WebDriverWait wait) {
		try { 
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("formDispFrame")));
    	
		} catch (Exception ed) {
			ed.printStackTrace();
		}

	}
    public static String Window(WebDriver driver) throws Exception {
        String mainWindowHandle = driver.getWindowHandle();
        System.out.println("Main Window: " + mainWindowHandle);

        // Wait briefly for popup
        Thread.sleep(2000);  // Replace with WebDriverWait if needed

        Set<String> allWindows = driver.getWindowHandles();
        System.out.println("Total windows: " + allWindows.size());

        for (String window : allWindows) {
            if (!window.equals(mainWindowHandle)) {
                driver.switchTo().window(window);
                System.out.println("Switched to new window: " + window);
                return window;
            }
        }

        // If no new window found, stay on main
        driver.switchTo().window(mainWindowHandle);
        return mainWindowHandle;
    }
    public static void ValidationFrame(WebDriver driver) {
	    driver.switchTo().defaultContent(); // or use driver.switchTo().frame(0); if no name
	    driver.switchTo().frame("loginFrame");
	    driver.switchTo().frame("CoreServer");
	    driver.switchTo().frame("FINW");

	}
    public static void action(WebDriver driver) {
    	Actions actions = new Actions(driver);
        actions.sendKeys(Keys.PAGE_DOWN).perform();

	}
    public static String extract(String fullText) {
        if (fullText == null || fullText.isBlank()) {
            return "";
        }
        // Split based on whitespace, limit = 2 to keep only the first token
        String[] parts = fullText.trim().split("\\s+", 2);
        return parts.length > 0 ? parts[0] : "";
    }
    public static String getValueByLabel(WebDriver driver, String labelText) {
        // Adjust this if the field is inside an iframe
        // driver.switchTo().frame("FINW"); // only if needed

        String xpath = "//td[normalize-space(text())='" + labelText + "']" +
                       "/following-sibling::td//span[contains(@class,'display-text')]";

        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return el.getText().trim();
    }
    public static String getLabelledValue(WebDriver driver, String labelText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // 2️⃣ Build and use a reliable following-sibling XPath
        String xpath = "//td[normalize-space()='" + labelText + "']"
                     + "/following-sibling::td[1]//*[contains(@class,'display-text') or normalize-space(text())]";
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        String text = el.getText().trim();
        
        // 🛑 Switch back to the main document context
        driver.switchTo().defaultContent();
        
        return text;
    }
    public static void UIError(WebDriver driver, WebDriverWait wait) {
 
        // Wait for any spinner or overlay to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-overlay")));

        WindowHandle.ValidationFrame(driver);
        
        String alertMsg = "";
        By err = By.cssSelector("table.errortableborder tr.alert td");
        List<WebElement> elems = driver.findElements(err);
        if (!elems.isEmpty()) {
            String msg = wait.until(ExpectedConditions.visibilityOfElementLocated(err)).getText().trim();
            Assert.fail("❌ UI Error: " + msg);
        }

        SharedContext.alertMessage = alertMsg;
}
    public static void Go(WebDriver driver, WebDriverWait wait) {

    	WindowHandle.ValidationFrame(driver);
    	String alertMsg = "";
        By errorLocator = By.cssSelector("table.errortableborder tr.alert td a[onmouseover]");
        List<WebElement> elems = driver.findElements(errorLocator);
        if (!elems.isEmpty()) {
            String msg = wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator)).getText().trim();
            Assert.fail("❌ UI Error: " + msg);
        }
        SharedContext.alertMessage = alertMsg;
//    	
//    	WindowHandle.ValidationFrame(driver);
//        By locator = By.cssSelector("table.errortableborder tr.alert td a[onmouseover]");
//        List<WebElement> elems = driver.findElements(locator);
//
//        if (!elems.isEmpty()) {
//            String actual = wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
//                                 .getText().trim();
//
//            if (expected != null && !expected.isEmpty() && actual.equalsIgnoreCase(expected)) {
//                System.out.println("✅ Expected UI error occurred: " + actual);
//                return; // Treat as pass
//            }
//
//            Assert.fail("❌ UI Error: '" + actual + "' — expected: '" + expected + "'");
//        } else if (expected != null && !expected.isEmpty()) {
//            Assert.fail("❌ Expected UI error '" + expected + "' did not occur");
//        }
//
}
    public static boolean HandlePopupIfExists(WebDriver driver) {
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String handle : allWindows) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                return true; // popup handled
            }
        }

        return false; // no popup
    }
    
//    public static void setValueWithJS(WebDriver driver, WebElement element, String value) {
//    	String jsScript =
//    	"var el = arguments[0];" +
//    	"el.value = arguments[1];" +
//    	"var evt1 = document.createEvent('HTMLEvents');" +
//    	"evt1.initEvent('input', true, false);" +
//    	"el.dispatchEvent(evt1);" +
//    	"var evt2 = document.createEvent('HTMLEvents');" +
//    	"evt2.initEvent('change', true, false);" +
//    	"el.dispatchEvent(evt2);";
//    	((JavascriptExecutor) driver).executeScript(jsScript, element, value);
//    	}
    
    @SuppressWarnings("deprecation")
	public static void setValueWithJS(WebDriver driver, WebElement element, String value) {
        if (value != null && !value.trim().isEmpty()) {
            String jsScript =
                "var el = arguments[0];" +
                "el.value = arguments[1];" +
                "var evt1 = document.createEvent('HTMLEvents');" +
                "evt1.initEvent('input', true, false);" +
                "el.dispatchEvent(evt1);" +
                "var evt2 = document.createEvent('HTMLEvents');" +
                "evt2.initEvent('change', true, false);" +
                "el.dispatchEvent(evt2);";
            ((JavascriptExecutor) driver).executeScript(jsScript, element, value);
            //System.out.println("Updated field [" + element.getAttribute("id") + "] with Excel value: " + value);
        } else {
            // Skip updating, keep default value
            @SuppressWarnings("unused")
			String defaultValue = element.getAttribute("value");
            //System.out.println("Excel empty → keeping default value in field [" + element.getAttribute("id") + "]: " + defaultValue);
        }
    }
    public static void closeProcessingPopup(WebDriver driver) {
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String handle : allWindows) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                if (driver.getCurrentUrl().contains("fetch.jsp")) {
                    driver.close();
                    System.out.println("Processing popup closed.");
                }
            }
        }
        driver.switchTo().window(mainWindow);
    }
    
}