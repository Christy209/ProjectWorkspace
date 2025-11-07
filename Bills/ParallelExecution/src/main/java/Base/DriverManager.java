package Base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.WindowHandle;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> wait1 = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() { return driver.get(); }
    public static WebDriverWait getWait() { return wait1.get(); }

    public static WebDriver initDriver(String browser) throws Exception {
        String hubURL = "http://192.168.84.247:4444/wd/hub";
        WebDriver drv;
        
        

        if (browser.equalsIgnoreCase("chrome")) {
            drv = new RemoteWebDriver(new java.net.URL(hubURL), new ChromeOptions());
        } else if (browser.equalsIgnoreCase("edge")) {
            drv = new RemoteWebDriver(new java.net.URL(hubURL), new EdgeOptions());
        } else if (browser.equalsIgnoreCase("ie")) {
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.ignoreZoomSettings();
            options.introduceFlakinessByIgnoringSecurityDomains();
            drv = new RemoteWebDriver(new java.net.URL(hubURL), options);
        } else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        driver.set(drv);
        wait1.set(new WebDriverWait(drv, Duration.ofSeconds(10)));

        drv.manage().window().maximize();
        drv.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        return drv;
    }

    public static void login(String userID, String password) {
        WebDriver drv = getDriver();
        WebDriverWait wait = getWait();

        drv.get(getProperty("url"));

        try {
            WindowHandle.slowDown(3); // optional pause for page load

            // Chrome HTTPS warning
            if (isElementPresent(By.id("details-button"))) {
                wait.until(ExpectedConditions.elementToBeClickable(By.id("details-button"))).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.id("proceed-link"))).click();
            }
            // Edge IE mode warning
            else if (isElementPresent(By.linkText("More information"))) {
                wait.until(ExpectedConditions.elementToBeClickable(By.linkText("More information"))).click();
                if (isElementPresent(By.id("overridelink"))) {
                    wait.until(ExpectedConditions.elementToBeClickable(By.id("overridelink"))).click();
                }
            }
        } catch (Exception e) {
            System.out.println("No security warning page detected, continuing normally.");
        }

        // Switch to login frame
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("loginFrame"));

        // Perform login
        drv.findElement(By.id("usertxt")).clear();
        drv.findElement(By.id("usertxt")).sendKeys(userID);
        drv.findElement(By.id("passtxt")).clear();
        drv.findElement(By.id("passtxt")).sendKeys(password);
        drv.findElement(By.id("Submit")).click();

        System.out.println("Logged in as: " + userID);
    }

    private static boolean isElementPresent(By locator) {
        try {
            getDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static String getProperty(String key) {
        try {
            String projectPath = System.getProperty("user.dir");
            FileInputStream configFile = new FileInputStream(projectPath + "/resources/config.properties");
            Properties propertyObj = new Properties();
            propertyObj.load(configFile);
            return propertyObj.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            wait1.remove();
        }
    }
}
