package com.BBSSL.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
  		WebDriverManager.edgedriver().setup(); // 👈 Automatically handles driver download & path
  		driver = new EdgeDriver();
//    		WebDriverManager.iedriver().setup();
//    		driver = new InternetExplorerDriver();


    	    driver.manage().window().maximize();
    	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    	    wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    	}
        return driver;
    }

    public static void login(String userID, String password) {

        WebDriver driver = getDriver();
        driver.get(getProperty("url"));
        
        
        try {
//            //WindowHandle.slowDown(5); // Delay if needed to let the SSL warning load
//
//            // Wait for "More information" link to be present and click it
//            WebElement moreInfoLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("More information")));
//            moreInfoLink.click();
//            
//            // Step 2: Click "Go on to the webpage (not recommended)"
//            WebElement continueLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Go on to the webpage (not recommended)")));
//            continueLink.click();
            
          wait.until(ExpectedConditions.elementToBeClickable(By.id("details-button"))).click();
          wait.until(ExpectedConditions.elementToBeClickable(By.id("proceed-link"))).click();
            
        } catch (Exception e) {
            System.out.println("SSL warning not present, continuing...");
        }
        
        
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("loginFrame"));

        // Perform login
        driver.findElement(By.xpath("//input[@id='usertxt']")).clear();
        driver.findElement(By.xpath("//input[@id='usertxt']")).sendKeys(userID);
        driver.findElement(By.xpath("//input[@id='passtxt']")).clear();
        driver.findElement(By.xpath("//input[@id='passtxt']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@id='Submit']")).click();

        System.out.println("Logged in as: " + userID);
    }

    public static String getProperty(String key) {
        try {
        	 String projectPath = System.getProperty("user.dir");  // Gets your project root path
             FileInputStream configFile = new FileInputStream(projectPath + "/Resource/config.properties");
            Properties propertyObj = new Properties();
            propertyObj.load(configFile);
            return propertyObj.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
