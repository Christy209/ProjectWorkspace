package Base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

    private static WebDriver driver;
    protected static WebDriverWait wait;

    private DriverManager() {} // Prevent instance creation

    public static WebDriver getDriver() {
        if (driver == null) {

            EdgeOptions options = new EdgeOptions();

            // Detect current user
            String userName = System.getProperty("user.name");
            System.out.println("Current Windows user: " + userName);

            // Set headless only for local users if needed
            boolean runHeadless = false; // change to true if you want headless for local users
            if (!"JenkinsAdmin".equalsIgnoreCase(userName) && runHeadless) {
                options.addArguments("--headless=new");
                options.addArguments("--disable-gpu");
                System.out.println("Running Edge in headless mode (local user)");
            } else {
                System.out.println("Running Edge with GUI (JenkinsAdmin or local user)");
            }

            // Common Edge options
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-background-networking");

            // Setup EdgeDriver using WebDriverManager
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver(options);

            // Maximize window and configure timeouts
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            System.out.println("EdgeDriver initialized successfully");
        }
        return driver;
    }

    public static void login(String userID, String password) {
        WebDriver driver = getDriver();
        driver.get(getProperty("url"));

        try {
            // Handle SSL warning if present
            wait.until(ExpectedConditions.elementToBeClickable(By.id("details-button"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("proceed-link"))).click();
        } catch (Exception e) {
            System.out.println("SSL warning not present, continuing...");
        }

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("loginFrame"));

        driver.findElement(By.id("usertxt")).clear();
        driver.findElement(By.id("usertxt")).sendKeys(userID);
        driver.findElement(By.id("passtxt")).clear();
        driver.findElement(By.id("passtxt")).sendKeys(password);
        driver.findElement(By.id("Submit")).click();

        System.out.println("Logged in as: " + userID);
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

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
