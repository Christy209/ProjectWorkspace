package PAYMENTS;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.DateUtils;
import Utilities.ErrorCapture;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class InwardDocumentaryCreditsMaintenance {
    private WebDriver driver;
    private WebDriverWait wait;

    public InwardDocumentaryCreditsMaintenance(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public Map<String, String> execute(RowData inputData, RowData id, String sheetname, int row, String excelpath) throws Exception {
        String mainWindowHandle = driver.getWindowHandle();
        String FunCode = inputData.getByIndex(2);
        Map<String, String> result = new HashMap<>();
        String labelText = "";

        WindowHandle.slowDown(2);
        WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))), inputData.getByIndex(1));

        WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("funcCode")), FunCode);
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))), inputData.getByIndex(3));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idcmNum"))), inputData.getByIndex(4));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idcmType"))), inputData.getByIndex(5));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))), inputData.getByIndex(6));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcCurrency"))), inputData.getByIndex(7));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messageId"))), inputData.getByIndex(8));

            if (FunCode.equalsIgnoreCase("V - Verify") || FunCode.equalsIgnoreCase("A - Amendment") || FunCode.equalsIgnoreCase("R - Reinstatement")|| 
            		FunCode.equalsIgnoreCase("C - Copy")) {
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idcmNum"))),
                		id.getByHeader("DCNo"));
            }

            WebElement go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Go")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);

            if(FunCode.equalsIgnoreCase("C - Copy")) {
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcNumCopy"))), inputData.getByIndex(8));
                System.out.println("" + id.getByHeader("DCNo"));

                WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Go")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accept);
                
            }
            String errorMsg = ErrorCapture.checkForApplicationErrors(driver);
            if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                result.put("errorMsg", errorMsg);
                return result;
            }

            if ("V - Verify".equalsIgnoreCase(FunCode)) {
                Map<String, String> appData = getApplicationData(driver, wait);
                Validation.validateData(inputData.getHeaderMap(), appData);

                labelText = clickSubmitAndGetLabelText(mainWindowHandle);

                if (labelText == null || labelText.trim().isEmpty()) {
                    Assert.fail("❌ Documentary Credit No label not found! Stopping test execution.");
                } else {
                    System.out.println("✅ Label found: " + labelText + " — Stopping test execution.");
                    String DCNo = labelText.replaceAll("[^0-9]", "");
                    ExcelUtils.updateExcel(excelpath, sheetname, row, "DCNo", DCNo);
                    result.put("labelText", labelText);

                    // ✅ Stop execution immediately after success
                    return result;
                }
            }

            if ("A - Amendment".equalsIgnoreCase(FunCode)) {
            		
                WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("amendmentStatus")), inputData.getByIndex(26));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("negotPeriod"))), 
                inputData.getByIndex(23));
                
                WebElement submitBtn = driver.findElement(By.id("Submit"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);

                closePopupWindow(driver);
                
                WindowHandle.ValidationFrame(driver);
                WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
                labelText = label.getText().trim();
                
                if (labelText == null || labelText.trim().isEmpty()) {
                    Assert.fail("❌ Documentary Credit No label not found! Stopping test execution.");
                } else {
                    System.out.println("✅ Label found: " + labelText + " — Stopping test execution.");
                    String DCNo = labelText.replaceAll("[^0-9]", "");
                    ExcelUtils.updateExcel(excelpath, sheetname, row, "DCNo", DCNo);
                    result.put("labelText", labelText);

                    // ✅ Stop execution immediately after success
                    return result;
                }
                
                
            }
            if("E - Advise".equalsIgnoreCase(FunCode) || FunCode.equalsIgnoreCase("C - Copy")) {
                // Fill main fields for creation
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))), inputData.getByIndex(9));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcOpenValueAmt"))), inputData.getByIndex(10));

                String issuedDate = inputData.getByIndex(11);
                String IssuedformattedDate = DateUtils.toDDMMYYYY(issuedDate);
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("issueDate_ui"))), IssuedformattedDate);

                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otherBankRef"))), inputData.getByIndex(12));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("country"))), inputData.getByIndex(13));

                // DC Details Tab
                WebElement dcDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("odcmdcdetails")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dcDetailsTab);
                dcDetailsTab.click();

                WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("applicableRules")), inputData.getByIndex(18));
                WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("tenor")), inputData.getByIndex(19));
                WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("dueDateInd")), inputData.getByIndex(20));

                String EXDate = inputData.getByIndex(21);
                String formattedDate = DateUtils.toDDMMYYYY(EXDate);
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expDate_ui"))), formattedDate);

                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plExpDate"))), inputData.getByIndex(22));

                WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
                NextPage.click();

                String LatestDate = inputData.getByIndex(23);
                String LaformattedDate = DateUtils.toDDMMYYYY(LatestDate);
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("latestDateOfShipment_ui"))), LaformattedDate);

                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("negotPeriod"))), inputData.getByIndex(24));
                WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("chargesBorneBy")), inputData.getByIndex(25));

                // Confirm Required
                String confRequired = inputData.getByIndex(26);
                WebElement confRequiredFlg = driver.findElement(By.xpath("//input[@id='confRequired' and @value='" +
                        (confRequired.equalsIgnoreCase("Confirm") ? "Y" : confRequired.equalsIgnoreCase("Without") ? "N" : "M") + "']"));
                confRequiredFlg.click();

                // Related party details
                WebElement dcpartyTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("dcparty")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dcpartyTab);
                dcpartyTab.click();

                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benfCifId"))), inputData.getByIndex(14));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("issueBank"))), inputData.getByIndex(15));
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("issueBranch"))), inputData.getByIndex(16));

                // Charge details tab
                WebElement element = driver.findElement(By.id("tfccharge"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
                labelText = clickSubmitAndGetLabelText(mainWindowHandle);
                if (labelText == null || labelText.trim().isEmpty()) {
                    Assert.fail("❌ Documentary Credit No label not found! Stopping test execution.");
                    result.put("labelText", labelText); // return null or empty string
                } else {
                    String DCNo = labelText.replaceAll("[^0-9]", "");
                    ExcelUtils.updateExcel(excelpath, sheetname, row, "DCNo", DCNo);
                    result.put("labelText", labelText);
                    result.put("labelText", labelText);// return the found value
                }
           //}
        } catch (Exception e) {
            System.out.println("Documentary credit creation Error: " + e.getMessage());
            result.put("errorMsg", e.getMessage());
        }

        return result;
    }

    private String clickSubmitAndGetLabelText(String mainWindowHandle) {
        String labelText = null;
        try {
            WindowHandle.slowDown(2);
            WebElement submitBtn = driver.findElement(By.id("Submit"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);

            WindowHandle.slowDown(2);
            if (WindowHandle.HandlePopupIfExists(driver)) {
                WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
                accept.click();
                driver.switchTo().window(mainWindowHandle);
            }

            WindowHandle.ValidationFrame(driver);
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
            labelText = label.getText().trim();

        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for Submit/Label: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception in submit method: " + e.getMessage());
        }
        return labelText;
    }

    private static Map<String, String> getApplicationData(WebDriver driver, WebDriverWait wait) throws Exception {
        Map<String, String> appData = new HashMap<>();

        WebElement docNoElement = driver.findElement(By.xpath("//td[text()='Documentary Credit No.']/following-sibling::td"));
        String actualDocNo = docNoElement.getText().trim();
        String actualType = actualDocNo.split("\\s+")[0];
        appData.put("odcmNum", actualType);

        WebElement DCType = driver.findElement(By.xpath("//td[text()='Documentary Credit Type']/following-sibling::td"));
        String odcmType = DCType.getText().trim();
        String actualDCType = odcmType.split("\\s+")[0];
        appData.put("odcmType", actualDCType);

        WebElement CifId = driver.findElement(By.xpath("//td[text()='CIF ID']/following-sibling::td"));
        String CIFID = CifId.getText().trim();
        String actualCIFID = CIFID.split("\\s+")[0];
        appData.put("cifId", actualCIFID);

        WebElement amtElement = driver.findElement(By.xpath("//td[text()='Documentary Credit Amt.']/following-sibling::td"));
        String actualAmt = amtElement.getText().trim();
        appData.put("dcOpenValueAmt", actualAmt);

        appData.put("acctId", ExcelUtils.getTextOrValue(driver, wait, "acctId", "id"));
        appData.put("dcOpenValueAmt", ExcelUtils.getTextOrValue(driver, wait, "dcOpenValueAmt", "id"));
        appData.put("issueDate", ExcelUtils.getTextOrValue(driver, wait, "issueDate_ui", "id"));

        WebElement PartyetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("dcparty")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", PartyetailsTab);
        PartyetailsTab.click();

        appData.put("benfCifId", ExcelUtils.getTextOrValue(driver, wait, "benfCifId", "id"));
        appData.put("issueName", ExcelUtils.getTextOrValue(driver, wait, "issueName", "id"));
        appData.put("issueBranch", ExcelUtils.getTextOrValue(driver, wait, "issueBranch", "id"));

        WebElement GuaranteeDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("odcmdcdetails")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", GuaranteeDetailsTab);
        GuaranteeDetailsTab.click();

        appData.put("applicableRules", ExcelUtils.getTextOrValue(driver, wait, "applicableRules", "id"));
        appData.put("tenor", ExcelUtils.getTextOrValue(driver, wait, "tenor", "id"));
        appData.put("dueDateInd", ExcelUtils.getTextOrValue(driver, wait, "dueDateInd", "id"));
        appData.put("expDate_ui", ExcelUtils.getTextOrValue(driver, wait, "expDate_ui", "id"));
        appData.put("plExpDate", ExcelUtils.getTextOrValue(driver, wait, "plExpDate", "id"));

        WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
        NextPage.click();

        appData.put("latestDateOfShipment_ui", ExcelUtils.getTextOrValue(driver, wait, "latestDateOfShipment_ui", "id"));
        appData.put("negotPeriod", ExcelUtils.getTextOrValue(driver, wait, "negotPeriod", "id"));
        appData.put("chargesBorneBy", ExcelUtils.getTextOrValue(driver, wait, "chargesBorneBy", "id"));

        
        return appData;
    }
    public static void closePopupWindow(WebDriver driver) {
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);

                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    WebElement closeButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//img[contains(@title, 'Close') or contains(@alt, 'Close') or contains(@src, 'close')]")
                    ));

                    // Scroll and click via JavaScript
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", closeButton);
                    Thread.sleep(500);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeButton);

                    System.out.println("✅ Popup window closed successfully using JS click.");
                } catch (Exception e) {
                    System.out.println("⚠️ JS click failed, closing window directly: " + e.getMessage());
                    driver.close(); // Fallback
                }

                // Switch back to main window
                driver.switchTo().window(mainWindow);
                break;
            }
        }
    }
}
