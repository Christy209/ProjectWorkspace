package Base;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.WindowHandle; // Custom utility for sleep
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class DriverManager {
    private static WebDriver driver;
    protected static WebDriverWait wait;

    private DriverManager() {} // Prevent instance creation

    // ✅ Initialize EdgeDriver
    public static WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.edgedriver().setup();

            EdgeOptions options = new EdgeOptions();
            options.addArguments("--start-maximized"); // Maximize window
            options.setCapability("acceptInsecureCerts", true); // Accept SSL

            driver = new EdgeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        }
        return driver;
    }

    // ✅ Login method
    public static void login(String userID, String password) {
        WebDriver driver = getDriver();
        driver.get(getProperty("url"));

        // Handle SSL warning if present
        try {
            WindowHandle.slowDown(3);

//            WebElement moreInfo = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("More information")));
//            moreInfo.click();
//
//            WebElement continueLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Go on to the webpage (not recommended)")));
//            continueLink.click();

            wait.until(ExpectedConditions.elementToBeClickable(By.id("details-button"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("proceed-link"))).click();
              
        } catch (Exception e) {
            System.out.println("No SSL warning detected, continuing...");
        }

        // Switch to login frame
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("loginFrame"));

        // Fill login fields using JavaScript to avoid flakiness
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("document.getElementById('usertxt').value='" + userID + "';");
//        js.executeScript("document.getElementById('passtxt').value='" + password + "';");
        driver.findElement(By.xpath("//input[@id='usertxt']")).clear();
        driver.findElement(By.xpath("//input[@id='usertxt']")).sendKeys(userID);
        driver.findElement(By.xpath("//input[@id='passtxt']")).clear();
        driver.findElement(By.xpath("//input[@id='passtxt']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@id='Submit']")).click();
        // Click login button
        System.out.println("Logged in as: " + userID);

        System.out.println("✅ Logged in as: " + userID);
    }

    // ✅ Read property from config.properties
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

    // ✅ Quit driver safely
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    // ✅ Reinitialize driver (for Jenkins/CI flaky sessions)
    public static WebDriver reinitializeDriver() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception ignore) {
            System.out.println("⚠ Old driver session already closed.");
        }
        driver = null;
        return getDriver();
    }
}
