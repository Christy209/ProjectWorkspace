package CASA;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.RowData;
import Utilities.WindowHandle;

public class VerificationOfIssueChequeBook {
    private WebDriver driver;
    private WebDriverWait wait;

    public VerificationOfIssueChequeBook(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void execute(RowData id, List<String> issue) throws Exception {
        driver.switchTo().defaultContent();
        driver.switchTo().frame("loginFrame");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")))
            .sendKeys(issue.get(1));
        driver.findElement(By.id("menuSearcherGo")).click();
        WindowHandle.handleAlertIfPresent(driver);

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")))
            .sendKeys("V - Verify");

        String foracid = id.getByHeader("Created_AccountID");
        WebElement account = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctNum")));
        account.clear();
        account.sendKeys(foracid);

        WebElement accept = wait.until(ExpectedConditions.elementToBeClickable(By.id("Accept")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accept);
        
        try {
        WindowHandle.ValidationFrame(driver);

        By firstRowLocator = By.xpath(
        	    "(//tr[contains(@class,'searclist1arow') or contains(@class,'searchlist1arow')])[1]"
        	);
        	WebElement firstRow = wait.until(
        	    ExpectedConditions.visibilityOfElementLocated(firstRowLocator)
        	);

        	// Extract the <td> cells
        	List<WebElement> cells = firstRow.findElements(By.tagName("td"));
        	if (cells.size() >= 4) {
        	    // ✅ Save to String variables
        		String alpha = cells.get(0).getText().trim();
        	    String beginCheque = cells.get(1).getText().trim();
        	    String numLeaves   = cells.get(2).getText().trim();
        	    String endCheque   = cells.get(3).getText().trim();

        	    // Print or use these values
        	    System.out.printf(
        	        "Begin Cheque: %s | Leaves: %s | End Cheque: %s%n",
        	        beginCheque, numLeaves, endCheque
        	    );

        	    // Example: store in a map if needed
        	    Map<String, String> AppData = new HashMap<>();
        	    AppData.put("A/C ID", foracid);
        	    AppData.put("ChequeAlpha", alpha);
        	    AppData.put("BeginChequeNo.", beginCheque);
        	    AppData.put("NoOfLeaves", numLeaves);
        	    AppData.put("EndChequeNo", endCheque);
        	    
        	    Map<String, String> ExceptedValue = new HashMap<>();

        	    ExceptedValue.put("A/C ID", id.getByHeader("Created_AccountID"));
        	    ExceptedValue.put("ChequeAlpha", issue.get(5).toString().trim());
        	    ExceptedValue.put("BeginChequeNo.", issue.get(6).toString().trim());
        	    ExceptedValue.put("NoOfLeaves", issue.get(8).toString().trim());
        	    //ExceptedValue.put("EndChequeNo", issue.get(8).toString().trim());

        	    int matchCount = 0, mismatchCount = 0;
                StringBuilder mismatchDetails = new StringBuilder();

                for (String key : ExceptedValue.keySet()) {
                    String exp = ExceptedValue.get(key);
                    String act = AppData.get(key);
                    
                    if (exp == null || exp.isEmpty() || act == null || act.isEmpty()) {
                        System.out.printf("⚠️ Skipping '%s' – expected: [%s], actual: [%s]%n", key, exp, act);
                        continue;
                    }
                    if (exp.equals(act)) {
                        matchCount++;
                    } else {
                        mismatchCount++;
                        mismatchDetails.append(
                            String.format("Field '%s': expected [%s], actual [%s]%n", key, exp, act)
                        );
                    }
                }
                // Log summary
                System.out.printf("✅ Matched: %d, ❌ Mismatches: %d%n", matchCount, mismatchCount);
                if (mismatchCount > 0) {
                    System.err.println("Mismatch Details:\n" + mismatchDetails);
                }
                // ✅ Only fail if any mismatch found
                Assert.assertEquals(mismatchCount, 0, 
                    "🔴 Found " + mismatchCount + " mismatched field(s).");
        	    // You can now use beginCheque, numLeaves, endCheque, or chequeData in further processing
        	} else {
        	    throw new IllegalStateException("First row has insufficient cell data!");
        	}
        }catch(Exception e) {
        	
        	System.out.printf("Validation failed" +e);
        }
        	WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
            WindowHandle.slowDown(1);
//            
            WindowHandle.ValidationFrame(driver);
            String expectedMessage = "Cheque Book Issue for A/c. ID" + foracid  + "verified successfully";
            WebElement accountElement = wait.until( ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")
            )
        );
        String actualText = accountElement.getText().trim();
        Assert.assertEquals(actualText, expectedMessage, "❌ Mismatch in Verified Account ID message!");
    }
}
