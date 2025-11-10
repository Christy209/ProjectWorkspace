package Base;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.WindowHandle;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class DriverManager {
    private static WebDriver driver;
    protected static WebDriverWait wait;

    private DriverManager() {} // Prevent instance creation

    public static WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.edgedriver().setup();

            EdgeOptions options = new EdgeOptions();
            options.addArguments("--start-maximized"); // maximize window
            options.setCapability("acceptInsecureCerts", true); // handle SSL if needed

            driver = new EdgeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        }
        return driver;
    }

    public static void login(String userID, String password) {
        WebDriver driver = getDriver();
        driver.get(getProperty("url"));

        // ✅ Handle SSL warning if shown (Edge should handle most)
        try {
            WindowHandle.slowDown(5);

            WebElement moreInfoLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("More information")));
            moreInfoLink.click();

            WebElement continueLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Go on to the webpage (not recommended)")));
            continueLink.click();

        } catch (Exception e) {
            System.out.println("No SSL warning detected, continuing...");
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Switch into login frame
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("loginFrame"));

        // ✅ Use JavaScriptExecutor for instant input
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('usertxt').value='" + userID + "';");
        js.executeScript("document.getElementById('passtxt').value='" + password + "';");

        // ✅ Click login button normally
        driver.findElement(By.xpath("//input[@id='Submit']")).click();

        System.out.println("✅ Logged in as: " + userID);
    }

    public static String getProperty(String key) {
        try {
            String projectPath = System.getProperty("user.dir");
            FileInputStream configFile = new FileInputStream(projectPath + "/Resource/config.properties");
            Properties propertyObj = new Properties();
            propertyObj.load(configFile);
            return propertyObj.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void quitDriver() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//        }
//    }

    public static WebDriver reinitializeDriver() throws IOException {
        try {
            if (driver != null) {
                driver.quit(); // Force close old session
            }
        } catch (Exception ignore) {
            System.out.println("⚠ Old driver session already dead.");
        }
        driver = null; // Reset reference completely
        driver = getDriver(); // Reinitialize Edge
        return driver;
    }
}
