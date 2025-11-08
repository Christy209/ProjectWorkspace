package CASA;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.RowData;
import Utilities.WindowHandle;

public class VerificationOfClosure {
	  private WebDriver driver;
	    private WebDriverWait wait;

	    public VerificationOfClosure(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
    public void execute(RowData id, RowData cl) throws IOException, Exception {
    	
    	String mainWindow = driver.getWindowHandle();
        String expClearBal = cl.getByHeader("Created_AccountID").trim();  // e.g. "0"
        String expLienAmt = "0";
        String expAcctStatus = "Active";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Navigate UI to retrieve values

        WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
        searchbox.click();
        searchbox.sendKeys(cl.getByIndex(1));
        driver.findElement(By.id("menuSearcherGo")).click();
        
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
        
        WebElement funCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
        funCode.sendKeys("V - Verify");
        
        driver.findElement(By.id("acctId")).sendKeys(id.getByHeader("Created_AccountID"));

        WebElement go = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
        js.executeScript("arguments[0].click();", go);

        WindowHandle.slowDown(1);
        String rawClearBal = driver.findElement(By.xpath("//td[text()='Clear Balance']/following-sibling::td[1]"))
                                 .getText().trim();
        WindowHandle.slowDown(1);
        String rawLienAmt = driver.findElement(By.xpath("//td[text()='Lien Amt.']/following-sibling::td[1]"))
                                .getText().trim();
        WindowHandle.slowDown(1);
        String rawAcctStatus = driver.findElement(By.xpath("//td[text()='A/c. Status']/following-sibling::td[1]"))
                                 .getText().trim();

        // Normalize and convert to BigDecimal
        BigDecimal uiClear = new BigDecimal(cleanNumeric(rawClearBal));
        BigDecimal uiLien = new BigDecimal(cleanNumeric(rawLienAmt));
        BigDecimal expClear = new BigDecimal(expClearBal);
        BigDecimal expLien = new BigDecimal(expLienAmt);

        // Use compareTo() for numeric equality (ignores scale) :contentReference[oaicite:0]{index=0}
        assertTrue(uiClear.compareTo(expClear) == 0,
            String.format("Clear Balance mismatch: expected %s but found %s", expClear, uiClear));
        assertTrue(uiLien.compareTo(expLien) == 0,
            String.format("Lien Amount mismatch: expected %s but found %s", expLien, uiLien));
        assertEquals(rawAcctStatus, expAcctStatus, "Account Status mismatch");

        WebElement closure = wait.until(ExpectedConditions.elementToBeClickable(By.id("caaccls")));
        js.executeScript("arguments[0].click();", closure);
        
        wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit"))).click();
        
        WindowHandle.handlePopupIfExists(driver);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("accept"))).click();
        driver.switchTo().window(mainWindow);
        

        
        WindowHandle.ValidationFrame(driver);
        String AcctID = id.getByHeader("Created_AccountID");
        WebElement confirmationMsg = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(),'verified successfully.')]")));
        String confirmationText = confirmationMsg.getText().trim();
        String expectedMessage = "A/c. ID " + AcctID + " verified successfully.";
        Assert.assertTrue(confirmationText.contains(expectedMessage),"Confirmation message does not match!");

    }

    /**
     * Cleans input string to only valid numeric content:
     * 1. Removes non-digit/dot/minus characters.
     * 2. Ensures only one decimal point is retained.
     */
    private String cleanNumeric(String input) {
        String filtered = input.replaceAll("[^0-9.\\-]", "");
        int idx = filtered.indexOf('.');
        if (idx >= 0) {
            String before = filtered.substring(0, idx + 1);
            String after = filtered.substring(idx + 1).replace(".", "");
            return before + after;
        }
        return filtered;
    }
    
    
}
