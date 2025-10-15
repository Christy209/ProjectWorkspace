package PAYMENTS;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class OutwardDocumentaryCreditsMaintenance {
    private WebDriver driver;
    private WebDriverWait wait;

    public OutwardDocumentaryCreditsMaintenance(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
    }

    public Map<String, String> execute(RowData inputData, RowData id, String sheetname, int row, String excelpath) throws Exception {
        String mainWindowHandle = driver.getWindowHandle();
        Map<String, String> result = new HashMap<>();
        String labelText = "";
        WindowHandle.slowDown(4);

        WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))), inputData.getByIndex(1));

        WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            String FunCode = inputData.getByIndex(2);
            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("funcCode")), FunCode);

            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))), inputData.getByIndex(3));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("odcmNum"))), inputData.getByIndex(4));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("odcmType"))), inputData.getByIndex(5));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))), inputData.getByIndex(6));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcCurrency"))), inputData.getByIndex(7));

            if(!FunCode.equalsIgnoreCase("S - Issue")) {
                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("odcmNum"))), id.getByHeader("DCNo"));
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
                Assert.fail("Application error found: " + errorMsg); // Fail immediately
                return result;
            }

            if("V - Verify".equalsIgnoreCase(inputData.getByIndex(2))) {
                Map<String, String> appData = getApplicationData(driver, wait);
                Validation.validateData(inputData.getHeaderMap(), appData);
                clickSubmitAndHandlePopup(mainWindowHandle);
                
                try {
                    WindowHandle.ValidationFrame(driver);
                    WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
                    labelText = label.getText();
                    System.out.println(labelText);

                    if (labelText == null || labelText.isEmpty()) {
                        String failMsg = "❌ Test Failed: Documentary Credit No is empty!";
                        result.put("errorMsg", failMsg);
                        Assert.fail(failMsg);
                    }

                    String DCNo = labelText.replaceAll("[^0-9]", "");
                    ExcelUtils.updateExcel(excelpath, sheetname, row, "DCNo", DCNo);
                    result.put("labelText", labelText);

                    switch (FunCode) {
                        case "S - Issue":
                        case "M - Modify":
                        case "V - Verify":
                        case "X - Cancel":
                        case "C - Copy":
                            break;
                        default:
                            Assert.fail("Unsupported FunCode: " + FunCode);
                    }
                } catch (Exception e) {
                    String exceptionMsg = "Exception occurred: " + e.getMessage();
                    result.put("errorMsg", exceptionMsg);
                    Assert.fail(exceptionMsg);
                }

                return result;
            }

            // Continue with other tabs and inputs...
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))), inputData.getByIndex(9));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcOpenValueAmt"))), inputData.getByIndex(10));
            WebElement dcpartyTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("dcparty")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dcpartyTab);
            dcpartyTab.click();

            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benfName"))), inputData.getByIndex(11));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benfAddr1"))), inputData.getByIndex(12));

            WebElement dcDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("odcmdcdetails")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dcDetailsTab);
            dcDetailsTab.click();

            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("applicableRules")), inputData.getByIndex(13));
            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("tenor")), inputData.getByIndex(14));
            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("dueDateInd")), inputData.getByIndex(15));

            String EXDate = inputData.getByIndex(16);
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expDate_ui"))), DateUtils.toDDMMYYYY(EXDate));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plExpDate"))), inputData.getByIndex(17));

            WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
            NextPage.click();

            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("latestDateOfShipment_ui"))), DateUtils.toDDMMYYYY(inputData.getByIndex(18)));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("negotPeriod"))), inputData.getByIndex(19));
            WindowHandle.selectDropdownIfValuePresent(driver, wait, (By.id("chargesBorneBy")), inputData.getByIndex(20));

            String confRequired = inputData.getByIndex(21);
            WebElement confRequiredFlg = driver.findElement(By.xpath("//input[@id='confRequired' and @value='" +
                    (confRequired.equalsIgnoreCase("Confirm") ? "Y" : confRequired.equalsIgnoreCase("Without") ? "N" : "M") + "']"));
            confRequiredFlg.click();

            WebElement ChargeDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ChargeDetailsTab);
            ChargeDetailsTab.click();

            clickSubmitAndHandlePopup(mainWindowHandle);

            try {
                WindowHandle.ValidationFrame(driver);
                WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
                labelText = label.getText();
                System.out.println(labelText);

                if (labelText == null || labelText.isEmpty()) {
                    String failMsg = "❌ Test Failed: Documentary Credit No is empty!";
                    result.put("errorMsg", failMsg);
                    Assert.fail(failMsg);
                }

                String DCNo = labelText.replaceAll("[^0-9]", "");
                ExcelUtils.updateExcel(excelpath, sheetname, row, "DCNo", DCNo);
                result.put("labelText", labelText);

            } catch (Exception e) {
                String exceptionMsg = "Exception occurred: " + e.getMessage();
                result.put("errorMsg", exceptionMsg);
                Assert.fail(exceptionMsg);
            }

        } catch (Exception e) {
            System.out.println("Documentary credit creation Error: " + e.getMessage());
        }

        return result;
    }

    /*** Reusable method for clicking Submit and handling popup 
     * @return ***/
    private Map<String, String> clickSubmitAndHandlePopup(String mainWindowHandle) {
    	Map<String, String> result = new HashMap<>();
        try {
            WebElement Submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Submit);
            Submit.click();

            if (WindowHandle.HandlePopupIfExists(driver)) {
                WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
                accept.click();
                driver.switchTo().window(mainWindowHandle);
            }
            String errorMsg = ErrorCapture.checkForApplicationErrors(driver);
            if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                result.put("errorMsg", errorMsg);
               // Assert.fail("Application error found: " + errorMsg); // Fail immediately
                
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Submit: " + e.getMessage());
        }
        return result;
    }

    private static Map<String, String> getApplicationData(WebDriver driver, WebDriverWait wait) throws Exception {
        Map<String, String> appData = new HashMap<>();

        WebElement docNoElement = driver.findElement(By.xpath("//td[text()='Documentary Credit No.']/following-sibling::td"));    
        String actualDocNo = docNoElement.getText().trim();
        appData.put("odcmNum", actualDocNo.split("\\s+")[0]);

        WebElement DCType = driver.findElement(By.xpath("//td[text()='Documentary Credit Type']/following-sibling::td"));		            
        appData.put("odcmType", DCType.getText().trim().split("\\s+")[0]);

        WebElement amtElement = driver.findElement(By.xpath("//td[text()='Documentary Credit Amt.']/following-sibling::td"));		            
        appData.put("dcOpenValueAmt", amtElement.getText().trim());

        appData.put("acctId", ExcelUtils.getTextOrValue(driver, wait, "acctId","id"));
        appData.put("dcOpenValueAmt", ExcelUtils.getTextOrValue(driver, wait, "dcOpenValueAmt","id"));

        WebElement PartyetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("dcparty")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", PartyetailsTab);
        PartyetailsTab.click();

        appData.put("benfName", ExcelUtils.getTextOrValue(driver, wait, "benfName","id"));
        appData.put("benfAddr1", ExcelUtils.getTextOrValue(driver, wait, "benfAddr1","id"));

        WebElement GuaranteeDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("odcmdcdetails")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", GuaranteeDetailsTab);
        GuaranteeDetailsTab.click();

        appData.put("applicableRules", ExcelUtils.getTextOrValue(driver, wait, "applicableRules","id"));
        appData.put("tenor", ExcelUtils.getTextOrValue(driver, wait, "tenor","id"));
        appData.put("dueDateInd", ExcelUtils.getTextOrValue(driver, wait, "dueDateInd","id"));
        appData.put("expDate_ui", ExcelUtils.getTextOrValue(driver, wait, "expDate_ui","id"));
        appData.put("plExpDate", ExcelUtils.getTextOrValue(driver, wait, "plExpDate","id"));

        WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
        NextPage.click();

        appData.put("latestDateOfShipment_ui", ExcelUtils.getTextOrValue(driver, wait, "latestDateOfShipment_ui","id"));
        appData.put("negotPeriod", ExcelUtils.getTextOrValue(driver, wait, "negotPeriod","id"));
        appData.put("chargesBorneBy", ExcelUtils.getTextOrValue(driver, wait, "chargesBorneBy","id"));

        WebElement chargetab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chargetab);
        chargetab.click();

        return appData;
    }
}
