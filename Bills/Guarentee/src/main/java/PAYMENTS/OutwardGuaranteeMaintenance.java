package PAYMENTS;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.ErrorCapture;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class OutwardGuaranteeMaintenance {
    private WebDriver driver;
    private WebDriverWait wait;

    public OutwardGuaranteeMaintenance(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
    }

//    public String execute(RowData inputData, RowData verify, String sheetname, int row, String excelpath) throws Exception {
    public Map<String, String> executeWithResultMap(RowData inputData, RowData verify, String sheetname, int row, String excelpath) throws Exception {
        String mainWindowHandle = driver.getWindowHandle();
        Map<String, String> result = new HashMap<>();
        @SuppressWarnings("unused")
		String guaranteeNo = "";
        String errorMsg  = "";
        WindowHandle.slowDown(4);

        WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))), inputData.getByIndex(1));
        WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            String FunCode = inputData.getByIndex(2);
            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("funcCode")), FunCode);

            if (FunCode.equalsIgnoreCase("A - Add")) {
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))), inputData.getByIndex(3));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bgType"))), inputData.getByIndex(5));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))), inputData.getByIndex(6));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ccy"))), inputData.getByIndex(7));
            } else {
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bgSrlNum"))),
                        verify.getByHeader("GuaranteeNo"));
            }

            WebElement Accept = driver.findElement(By.id("Accept"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Accept);
            Accept.click();

           // ErrorCapture.checkForApplicationErrors(driver);
            
            errorMsg = ErrorCapture.checkApplicationErrors(driver);
            if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                result.put("errorMsg", errorMsg);
                Assert.fail("Application error found: " + errorMsg); // Fail immediately
                return result;
            }

            if (FunCode.equalsIgnoreCase("N - Invoke") || FunCode.equalsIgnoreCase("G - Mark Invoke") || FunCode.equalsIgnoreCase("V - Verify") || FunCode.equalsIgnoreCase("Z - Close")) {
                if (FunCode.equalsIgnoreCase("N - Invoke")) {
                    WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loanacid"))), inputData.getByIndex(20));
                } else if (FunCode.equalsIgnoreCase("G - Mark Invoke")) {
                    WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("markInvokeAmt"))), inputData.getByIndex(19));
                } else if (FunCode.equalsIgnoreCase("V - Verify")) {
                    Map<String, String> appData = getApplicationData(verify, driver, wait, sheetname, row, excelpath);
                    Validation.validateData(inputData.getHeaderMap(), appData);
                }else if(FunCode.equalsIgnoreCase("Z - Close")) {
                	System.out.println("🟡 Performing Close operation for Guarantee...");
                }

                Map<String, String> submitResult = submitAndValidateGuarantee(driver, wait, mainWindowHandle, excelpath, sheetname, row, FunCode);
                guaranteeNo = submitResult.get("GuaranteeNo");
                return submitResult;
            }

            // Fill normal Add/Modify form fields
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))), inputData.getByIndex(8));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bgAmt"))), inputData.getByIndex(9));

            // Party Details Tab
            WebElement ogmpartyTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmparty")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ogmpartyTab);
            ogmpartyTab.click();

            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benName"))), inputData.getByIndex(11));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benAddr1"))), inputData.getByIndex(12));

            if(!FunCode.equalsIgnoreCase("N - Invoke")) {
            // Guarantee Details Tab
            WebElement ogmgrnteeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmgrntee")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ogmgrnteeTab);
            ogmgrnteeTab.click();

            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("applcRules")), inputData.getByIndex(13));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("purpOfGrntee"))), inputData.getByIndex(14));
            }
            // Limit Tab
            WebElement LimitTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", LimitTab);
            LimitTab.click();
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("limitIdPrefix"))), inputData.getByIndex(15));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("limitIdSuffix"))), inputData.getByIndex(16));

            if (FunCode.equalsIgnoreCase("A - Add")) {
                WebElement ChargeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ChargeTab);
                ChargeTab.click();
            }

            Map<String, String> submitResult = submitAndValidateGuarantee(driver, wait, mainWindowHandle, excelpath, sheetname, row, FunCode);
            guaranteeNo = submitResult.get("GuaranteeNo");

        } catch (Exception e) {
        	String exceptionMsg = "Exception occurred: " + e.getMessage();
            result.put("errorMsg", errorMsg);
            Assert.fail(exceptionMsg);
        }

        return submitAndValidateGuarantee(driver, wait, mainWindowHandle, excelpath, sheetname, row, inputData.getByIndex(2));
    }

    private static Map<String, String> getApplicationData(RowData inputData, WebDriver driver, WebDriverWait wait, String sheetname, int row, String excelpath) throws Exception {
        String FunCode = inputData.getByIndex(2);
        Map<String, String> appData = new HashMap<>();

        if (FunCode.equalsIgnoreCase("A - Add") || FunCode.equalsIgnoreCase("M - Modify")) {
            appData.put("acctId", ExcelUtils.getTextOrValue(driver, wait, "acctId","id"));
            appData.put("bgAmt", ExcelUtils.getTextOrValue(driver, wait, "bgAmt","id"));

            WebElement PartyetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmparty")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", PartyetailsTab);
            PartyetailsTab.click();

            appData.put("benName", ExcelUtils.getTextOrValue(driver, wait, "benName","id"));
            appData.put("benAddr1", ExcelUtils.getTextOrValue(driver, wait, "benAddr1","id"));

            WebElement LimitTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", LimitTab);
            LimitTab.click();

            appData.put("limitIdPrefix", ExcelUtils.getTextOrValue(driver, wait, "limitIdPrefix","id"));
            appData.put("limitIdSuffix", ExcelUtils.getTextOrValue(driver, wait, "limitIdSuffix","id"));

            if(!FunCode.equalsIgnoreCase("N - Invoke")) {
            WebElement GuaranteeDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmgrntee")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", GuaranteeDetailsTab);
            GuaranteeDetailsTab.click();

            appData.put("applcRules", ExcelUtils.getTextOrValue(driver, wait, "applcRules","id"));
            appData.put("purpOfGrntee", ExcelUtils.getTextOrValue(driver, wait, "purpOfGrntee","id"));
            }
            if(FunCode.equalsIgnoreCase("A - Add")) {
                WebElement ChargeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ChargeTab);
                ChargeTab.click();
            }
        }

        if(FunCode.equalsIgnoreCase("G - Mark Invoke")) {
            WebElement MarkInvokeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmmarkinvoke")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", MarkInvokeTab);
            appData.put("markInvokeAmt", ExcelUtils.getTextOrValue(driver, wait, "markInvokeAmt","id"));
        }

        if(FunCode.equalsIgnoreCase("N - Invoke")) {
            WebElement MarkInvokeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogminvoc")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", MarkInvokeTab);
            appData.put("loanacid", ExcelUtils.getTextOrValue(driver, wait, "loanacid","id"));
        }

        return appData;
    }

    public static Map<String, String> submitAndValidateGuarantee(WebDriver driver, WebDriverWait wait,
            String mainWindow, String excelpath,
            String sheetname, int row, String FunCode) {

        Map<String, String> result = new HashMap<>();
        String guaranteeNo = "";
        String transactionId = "";
        String labelText = "";
        boolean guaranteeCaptured = false;
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {

        	try {
        	    WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
        	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
        	    System.out.println("✅ Submit button clicked successfully.");
        	} catch (Exception e) {
        	    System.err.println("⚠️ Submit button not clickable, but proceeding if GuaranteeNo is captured: " + e.getMessage());
        	}

            // Check system-level error
            try {
                WebElement errorLink = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("anc1")));
                Assert.fail("❌ Test failed due to system error: " + errorLink.getText());
            } catch (TimeoutException e) {
                System.out.println("✅ No system error encountered.");
            }

            // Handle popup if exists
            if (WindowHandle.HandlePopupIfExists(driver)) {
                WindowHandle.slowDown(3);
                WebElement accept = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accept);

                driver.switchTo().window(mainWindow);
                WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WindowHandle.ValidationFrame(driver);

                try {
                    WebElement errorElement = wait2.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//tr[@class='alert']//td[2]")));
                    String errorMsg = errorElement.getText().trim();
                    if (!errorMsg.isEmpty()) {
                        Assert.fail("❌ UI Error Message Displayed: " + errorMsg);
                    } else {
                        System.out.println("✅ No UI Error Message Displayed. Proceeding...");
                    }
                } catch (TimeoutException e) {
                    System.out.println("✅ No UI Error Message Displayed. Proceeding...");
                }
            }

            // Capture label text, Guarantee No and Transaction ID
            WindowHandle.ValidationFrame(driver);
            WebElement label = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
            labelText = label.getText().trim();

            Pattern guaranteePattern = Pattern.compile("Guarantee No\\.\\s*([A-Za-z0-9]+)");
            Matcher guaranteeMatcher = guaranteePattern.matcher(labelText);
            if (guaranteeMatcher.find()) {
                guaranteeNo = guaranteeMatcher.group(1).trim();
                guaranteeCaptured = true;
                ExcelUtils.updateExcel(excelpath, sheetname, row, "GuaranteeNo", guaranteeNo);
                System.out.println("✅ Guarantee Number captured successfully: " + guaranteeNo);
            }

            Pattern txnPattern = Pattern.compile("Invocation Transaction ID\\s*:\\s*([A-Za-z0-9]+)");
            Matcher txnMatcher = txnPattern.matcher(labelText);
            if (txnMatcher.find()) {
                transactionId = txnMatcher.group(1).trim();
                ExcelUtils.updateExcel(excelpath, sheetname, row, "TransactionID", transactionId);
                System.out.println("✅ Invocation Transaction ID captured: " + transactionId);
            } else {
                System.out.println("ℹ️ No Transaction ID found in this case.");
            }

            Assert.assertFalse(guaranteeNo.isEmpty(), "❌ Test Failed: Guarantee No is empty!");

        } catch (Exception e) {
            if (guaranteeCaptured) {
                System.out.println("⚠️ Skipping error since Guarantee No. " + guaranteeNo + " already captured successfully.");
            } else {
                System.out.println("❌ Guarantee NO creation error: " + e.getMessage());
                Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
            }
        }

        result.put("GuaranteeNo", guaranteeNo);
        result.put("TransactionID", transactionId);
        result.put("LabelText", labelText);
        return result;
    }
}
