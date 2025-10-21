package Utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogOut {
    public static void performLogout(WebDriver driver, WebDriverWait wait) {
        try {
            driver.switchTo().defaultContent();
            driver.switchTo().frame("loginFrame"); 
            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, 'logout')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoutButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutButton);
            //logoutButton.click();
            WindowHandle.handleAlertIfPresent(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.name("Submit2"))).click();
            
        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());

        }
    }
}
