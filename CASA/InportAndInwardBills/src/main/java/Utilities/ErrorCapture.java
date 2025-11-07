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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorCapture {

    private static final Logger logger = LogManager.getLogger(ErrorCapture.class);
    static WebDriver driver;

    // ------------------- Method 1 -------------------
    public static void checkForError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr.alert td")));
            String errorText = errorElement.getText().trim();
            logger.error("Error Found: " + errorText);
            Assert.fail("Test failed due to application error: " + errorText);

        } catch (Exception e) {
            logger.info("No error message found, proceeding with test.");
        }
    }

    // ------------------- Method 2 -------------------
    @SuppressWarnings("deprecation")
    public static String checkForApplicationErrors(WebDriver driver) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
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
                        logger.error("❌ Error Found: " + errorText);
                        return errorText;
                    }
                } catch (TimeoutException ignore) {}
            }

            try {
                longWait.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.id("Submit")),
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Submit' or @value='Ok']"))
                ));
                logger.info("✅ Navigation successful — next page loaded after action.");
            } catch (TimeoutException e) {
                String additionalError = "";
                try {
                    WebElement msg = driver.findElement(By.xpath("//tr[contains(@class,'alert')]/td | //span[contains(@class,'error') or contains(@id,'error')]"));
                    additionalError = msg.getText().trim();
                    if (additionalError.isEmpty()) {
                        additionalError = msg.getAttribute("innerText").trim();
                    }
                } catch (Exception ignored2) {}

                String failMsg = additionalError.isEmpty() ? "Page did not load properly after action." : additionalError;
                logger.error("❌ " + failMsg);
                return failMsg;
            }

            logger.info("✅ No error message found, proceeding with test.");

        } catch (Exception e) {
            String msg = "⚠️ Unexpected issue while checking for errors: " + e.getMessage();
            logger.error(msg, e);
            return msg;
        }

        return null;
    }

    // ------------------- Method 3 -------------------
    @SuppressWarnings("deprecation")
    public static String checkApplicationErrors(WebDriver driver) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebDriverWait normalWait = new WebDriverWait(driver, Duration.ofSeconds(6));
        String detectedError = null;

        try {
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
                        logger.warn("⚠️ Error message detected: " + errorText);
                        break;
                    }
                } catch (TimeoutException ignore) {}
            }

            boolean pageLoaded = false;
            try {
                normalWait.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.id("Submit")),
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Submit' or @value='Ok']"))
                ));
                pageLoaded = true;
            } catch (TimeoutException e) {
                if (detectedError == null) {
                    logger.info("⏳ Finacle slow — waiting extra 10 seconds...");
                    WebDriverWait extraWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    try {
                        extraWait.until(ExpectedConditions.or(
                                ExpectedConditions.presenceOfElementLocated(By.id("Submit")),
                                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Submit' or @value='Ok']"))
                        ));
                        pageLoaded = true;
                        logger.info("✅ Page loaded after extra delay.");
                    } catch (TimeoutException e2) {
                        pageLoaded = false;
                    }
                }
            }

            if (detectedError != null && !pageLoaded) {
                logger.error("❌ Error found and page stuck — failing.");
                return detectedError;
            } else if (detectedError != null && pageLoaded) {
                logger.warn("⚠️ Error found but page loaded — continuing test.");
                return null;
            } else if (detectedError == null && !pageLoaded) {
                logger.error("❌ No visible error, but page didn't load even after waiting.");
                return "Page stuck or unresponsive — Finacle timeout.";
            } else {
                logger.info("✅ No error and page loaded successfully — continuing fast.");
                return null;
            }

        } catch (Exception e) {
            String msg = "⚠️ Unexpected issue while checking for errors: " + e.getMessage();
            logger.error(msg, e);
            return msg;
        }
    }

    // ------------------- Method 4 -------------------
    @SuppressWarnings("deprecation")
    public static String CheckingUIErrors(WebDriver driver) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebDriverWait normalWait = new WebDriverWait(driver, Duration.ofSeconds(6));
        String detectedError = null;
        boolean pageLoaded = false;

        try {
            List<By> messageLocators = Arrays.asList(
                    By.cssSelector("tr.alert td"),
                    By.xpath("//a[contains(text(), '-') and contains(@onclick,'fnSelectField')]"),
                    By.id("anc1"),
                    By.xpath("//span[contains(@class,'error') or contains(@id,'error') or contains(text(),'Error')]")
            );

            for (By locator : messageLocators) {
                try {
                    WebElement msgElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    String msgText = msgElement.getText().trim();
                    if (msgText.isEmpty()) msgText = msgElement.getAttribute("innerText").trim();
                    if (!msgText.isEmpty()) {
                        detectedError = msgText;
                        logger.info("🔹 Message detected: " + detectedError);
                        break;
                    }
                } catch (TimeoutException ignore) {}
            }

            try {
                normalWait.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.id("Submit")),
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Submit' or @value='Ok']"))
                ));
                pageLoaded = true;
            } catch (TimeoutException e) {
                pageLoaded = false;
            }

            if (detectedError != null && !pageLoaded) {
                logger.error("❌ Error detected and page stuck — failing test.");
                return detectedError;
            } else {
                logger.info("✅ Either no error or page is moving — continuing test.");
                return null;
            }

        } catch (Exception e) {
            String msg = "⚠️ Unexpected issue while checking for errors: " + e.getMessage();
            logger.error(msg, e);
            return msg;
        }
    }

}
