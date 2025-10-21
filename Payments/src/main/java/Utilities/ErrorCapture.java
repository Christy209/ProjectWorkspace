package Utilities;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ErrorCapture {
    static WebDriver driver;

    public static void checkForError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // Wait for error message if it appears
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr.alert td")));

            String errorText = errorElement.getText().trim();
            System.out.println("Error Found: " + errorText);

            // Fail the test if error message is displayed
            Assert.fail("Test failed due to application error: " + errorText);

        } catch (Exception e) {
            // No error found → continue test
            System.out.println("No error message found, proceeding with test.");
        }
    }

    @SuppressWarnings("deprecation")
	public static String checkForApplicationErrors(WebDriver driver) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // 🔹 Common Finacle error locators
            List<By> errorLocators = Arrays.asList(
                By.cssSelector("tr.alert td"),
                By.xpath("//a[contains(text(), '-') and contains(@onclick,'fnSelectField')]"),
                By.id("anc1"),
                By.xpath("//span[contains(@class,'error') or contains(@id,'error') or contains(text(),'Error')]")
            );

            // 🔹 Step 1: Check for visible error messages
            for (By locator : errorLocators) {
                try {
                    WebElement errorElement = shortWait.until(
                        ExpectedConditions.visibilityOfElementLocated(locator)
                    );

                    String errorText = errorElement.getText().trim();
                    if (errorText.isEmpty()) {
                        // Sometimes Finacle injects via innerText or script
                        errorText = errorElement.getAttribute("innerText").trim();
                    }

                    if (!errorText.isEmpty()) {
                        System.out.println("❌ Error Found: " + errorText);
                        return errorText; // 🔴 Return error text to be logged in TC02 log file
                    }

                } catch (TimeoutException ignore) {
                    // Continue checking next locator
                }
            }

            // 🔹 Step 2: Verify navigation success (page should have Submit or OK button)
            try {
                longWait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.id("Submit")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Submit' or @value='Ok']"))
                ));
                System.out.println("✅ Navigation successful — next page loaded after action.");
            } catch (TimeoutException e) {
                // Page didn’t load properly → try to capture additional error text
                String additionalError = "";
                try {
                    WebElement msg = driver.findElement(By.xpath(
                        "//tr[contains(@class,'alert')]/td | //span[contains(@class,'error') or contains(@id,'error')]"
                    ));
                    additionalError = msg.getText().trim();
                    if (additionalError.isEmpty()) {
                        additionalError = msg.getAttribute("innerText").trim();
                    }
                } catch (Exception ignored2) {}

                String failMsg = additionalError.isEmpty()
                        ? "Page did not load properly after action."
                        : additionalError;

                System.out.println("❌ " + failMsg);
                return failMsg; // 🔴 Return error text to be logged
            }

            System.out.println("✅ No error message found, proceeding with test.");
        } catch (Exception e) {
            String msg = "⚠️ Unexpected issue while checking for errors: " + e.getMessage();
            System.err.println(msg);
            return msg; // 🔴 Return so it gets logged in testcase log
        }

        return null; // ✅ No errors found
    }

    @SuppressWarnings("deprecation")
	public static String checkApplicationErrors(WebDriver driver) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));   // quick error check
        WebDriverWait normalWait = new WebDriverWait(driver, Duration.ofSeconds(6));  // normal load
        String detectedError = null;

        try {
            // 🔹 Step 1: Check for visible error messages
            List<By> errorLocators = Arrays.asList(
                By.cssSelector("tr.alert td"),
                By.xpath("//a[contains(text(), '-') and contains(@onclick,'fnSelectField')]"),
                By.id("anc1"),
                By.xpath("//span[contains(@class,'error') or contains(@id,'error') or contains(text(),'Error')]")
            );

            for (By locator : errorLocators) {
                try {
                    WebElement errorElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    String errorText = errorElement.getText().trim();
                    if (errorText.isEmpty()) {
                        errorText = errorElement.getAttribute("innerText").trim();
                    }

                    if (!errorText.isEmpty()) {
                        detectedError = errorText;
                        System.out.println("⚠️ Error message detected: " + errorText);
                        break;
                    }
                } catch (TimeoutException ignore) {
                    // No visible error for this locator, continue checking
                }
            }

            // 🔹 Step 2: Check if next page loaded
            boolean pageLoaded = false;
            try {
                normalWait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.id("Submit")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Submit' or @value='Ok']"))
                ));
                pageLoaded = true;
            } catch (TimeoutException e) {
                // Finacle might be slow — wait extra time only if no visible error
                if (detectedError == null) {
                    System.out.println("⏳ Finacle slow — waiting extra 10 seconds...");
                    WebDriverWait extraWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    try {
                        extraWait.until(ExpectedConditions.or(
                            ExpectedConditions.presenceOfElementLocated(By.id("Submit")),
                            ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Submit' or @value='Ok']"))
                        ));
                        pageLoaded = true;
                        System.out.println("✅ Page loaded after extra delay.");
                    } catch (TimeoutException e2) {
                        pageLoaded = false;
                    }
                }
            }

            // 🔹 Step 3: Apply Finacle-smart logic
            if (detectedError != null && !pageLoaded) {
                System.out.println("❌ Error found and page stuck — failing.");
                return detectedError; // Fail
            } else if (detectedError != null && pageLoaded) {
                System.out.println("⚠️ Error found but page loaded — continuing test.");
                return null; // Continue (don’t fail)
            } else if (detectedError == null && !pageLoaded) {
                System.out.println("❌ No visible error, but page didn't load even after waiting.");
                return "Page stuck or unresponsive — Finacle timeout.";
            } else {
                System.out.println("✅ No error and page loaded successfully — continuing fast.");
                return null; // No issues
            }

        } catch (Exception e) {
            String msg = "⚠️ Unexpected issue while checking for errors: " + e.getMessage();
            System.err.println(msg);
            return msg;
        }
    }

}