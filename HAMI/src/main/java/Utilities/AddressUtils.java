package Utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import CRM.PageObject;
import java.time.Duration;

public class AddressUtils {

    public static void handleLookupCityOrState(WebDriver driver,
                                               WebDriverWait wait,
                                               String buttonName,
                                               String valueToSelect, String[] getData) throws Exception {

        Actions actions = new Actions(driver);
        String mainWindow = driver.getWindowHandle();

        WebElement lookupBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("btnone_CorporateBO.Address.state")));
        lookupBtn.click();
        System.out.println("✅ Clicked on State Lookup");

       
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
        System.out.println("✅ Switched into iframe: IFrmtab0");

        WebElement body = driver.findElement(By.tagName("body"));
        
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        PageObject.waitAndFill(wait1, "name", "FilterParam1", getData[51]);
        actions.moveToElement(body, 0, 0).click().perform();
        
        WebElement saveButton = driver.findElement(By.name("SAVE"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", saveButton);


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@class='fntColData']")));
        for (WebElement row : driver.findElements(By.xpath("//font[@class='fntColData']"))) {
            if (row.getText().toUpperCase().contains(valueToSelect.trim().toUpperCase())) {
                actions.moveToElement(row).doubleClick().perform();
                System.out.println("✅ Selected row: " + row.getText());
                break;
            }
        }

        // 6. Switch back to main window
        driver.switchTo().window(mainWindow);
        System.out.println("🔙 Switched back to main window: " + driver.getTitle());
    }

}
