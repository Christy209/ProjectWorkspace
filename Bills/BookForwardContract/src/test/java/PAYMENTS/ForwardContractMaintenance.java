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
import Utilities.DateUtils;
import Utilities.ErrorCapture;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class ForwardContractMaintenance {

	private WebDriver driver;
	private WebDriverWait wait;

	    public ForwardContractMaintenance(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public Map<String, String> execute(RowData inputData, RowData no, String sheetname, int row, String excelpath) throws Exception {
	        String mainWindowHandle = driver.getWindowHandle();
	        WindowHandle.slowDown(4);

	        Map<String, String> result = new HashMap<>();
	        String labelText = "";

	        try {
	            // ---- Navigate and enter initial values ----
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))), inputData.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	            String FunCode = inputData.getByIndex(2);
	            WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("funcCode"), FunCode);

	            // ---- FunCode-specific field inputs ----
	            if (FunCode.equalsIgnoreCase("A - Booking of Contract")) {
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))), inputData.getByIndex(3));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctType"))), inputData.getByIndex(5));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))), inputData.getByIndex(6));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ccy"))), inputData.getByIndex(7));
	            } else{
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctNo"))),
	                		no.getByHeader("FwdCntrctNo"));
	            }

	            // ---- Click Accept/Go ----
	            WebElement goButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Accept")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", goButton);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", goButton);

	            String errorMsg = ErrorCapture.checkForApplicationErrors(driver);
	            if (errorMsg != null && !errorMsg.trim().isEmpty()) {
	                result.put("errorMsg", errorMsg);
	                return result;
	            }

	            // ---- Extension of Contract tab ----
	            if (FunCode.equalsIgnoreCase("E - Extension of Contract")) {
	                String validFrmDate = inputData.getByIndex(16);
	                if (validFrmDate != null && !validFrmDate.trim().isEmpty()) {
	                    WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validityFrmDate_ui"))),
	                            DateUtils.toDDMMYYYY(validFrmDate));
	                }

	                String validToDate = inputData.getByIndex(17);
	                if (validToDate != null && !validToDate.trim().isEmpty()) {
	                    WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validityToDate_ui"))),
	                            DateUtils.toDDMMYYYY(validToDate));
		                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newRateCode"))), inputData.getByIndex(18));

		                driver.findElement(By.tagName("body")).click();

		                WebElement limitTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
		                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", limitTab);
		                limitTab.click();
	                    
	                    WindowHandle.slowDown(5);
	                    WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	    	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

	    	            // Handle popup if exists
	    	            if (WindowHandle.HandlePopupIfExists(driver)) {
	    	                WindowHandle.slowDown(5);
	    	                WebElement acceptBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
	    	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptBtn);
	    	                
	    	               // WindowHandle.ValidationFrame(driver);
	    	            }
	    	            driver.switchTo().window(mainWindowHandle);
	                    WindowHandle.ValidationFrame(driver);
	                    
	                    WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
	                    labelText = label.getText().trim();
	                    
		                Pattern pattern = Pattern.compile("^[A-Z0-9]+"); // match letters+digits at the start
		                Matcher matcher = pattern.matcher(labelText);

		                 String FwdCntrctNo = "";
		                if (matcher.find()) {
		                    FwdCntrctNo = matcher.group(); // ✅ This will be "003FXTODP2500010"
		                } else {
		                    System.out.println("⚠️ Could not extract Forward Contract No from label: " + labelText);
		                }

		                // Update Excel
		                Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:Forward Contract No is empty!"); 

		                ExcelUtils.updateExcel(excelpath, sheetname, row, "FwdCntrctNo", FwdCntrctNo);
		                result.put("labelText", labelText);
		                System.out.println("✅ Excel updated: FwdCntrctNo = " + FwdCntrctNo);

	                return result;
	            }
	            }
	            // ---- General tab inputs ----
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))), inputData.getByIndex(8));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctAmt"))), inputData.getByIndex(9));

	            String validFrmDate = inputData.getByIndex(10);
	            if (validFrmDate != null && !validFrmDate.trim().isEmpty()) {
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validfrmDate_ui"))),
	                        DateUtils.toDDMMYYYY(validFrmDate));
	            }

	            String validToDate = inputData.getByIndex(11);
	            if (validToDate != null && !validToDate.trim().isEmpty()) {
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validtoDate_ui"))),
	                        DateUtils.toDDMMYYYY(validToDate));
	            }

	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Toccy"))), inputData.getByIndex(12));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rateCode"))), inputData.getByIndex(13));

	            // ---- Limit tab ----
	            WebElement limitTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", limitTab);
	            limitTab.click();
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("limitIdPrefix"))), inputData.getByIndex(14));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("limitIdSuffix"))), inputData.getByIndex(15));

	            // ---- Charge tab ----
	            WebElement chargeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chargeTab);
	            chargeTab.click();

	            // ---- Submit and capture post-submit errors ----
	            WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

	            // Handle popup if exists
	            if (WindowHandle.HandlePopupIfExists(driver)) {
	                WindowHandle.slowDown(5);
	                WebElement acceptBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptBtn);
	                driver.switchTo().window(mainWindowHandle);
	                WindowHandle.ValidationFrame(driver);
	            }

	            // Check for UI errors after submit
	            try {
	                WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@class='alert']//td[2]")));
	                String uiErrorMsg = errorElement.getText().trim();
	                if (!uiErrorMsg.isEmpty()) {
	                    Assert.fail("❌ UI Error Message Displayed: " + uiErrorMsg);
	                }
	            } catch (TimeoutException ignored) {
	                System.out.println("✅ No UI Error Message Displayed. Proceeding...");
	            }

	            // ---- Capture Forward Contract Number ----
	            try {
	                WindowHandle.ValidationFrame(driver);
	                WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
	                labelText = label.getText().trim(); // e.g., "003FXTODP2500010 added successfully."

	                // Extract only the Forward Contract No (alphanumeric at start)
	                Pattern pattern = Pattern.compile("^[A-Z0-9]+"); // match letters+digits at the start
	                Matcher matcher = pattern.matcher(labelText);

	                 String FwdCntrctNo = "";
	                if (matcher.find()) {
	                    FwdCntrctNo = matcher.group(); // ✅ This will be "003FXTODP2500010"
	                } else {
	                    System.out.println("⚠️ Could not extract Forward Contract No from label: " + labelText);
	                }

	                // Update Excel
	                Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:Forward Contract No is empty!"); 

	                ExcelUtils.updateExcel(excelpath, sheetname, row, "FwdCntrctNo", FwdCntrctNo);

	                System.out.println("✅ Excel updated: FwdCntrctNo = " + FwdCntrctNo);
	                result.put("labelText", labelText);
	                
	            } catch (Exception e) {
	                System.out.println("⚠️ Could not capture Forward Contract No: " + e.getMessage());
	                result.put("errorMsg", e.getMessage());
	            }

	        } catch (Exception e) {
	            System.out.println("MNTFWC Exception: " + e);
	        }

	        return result;
	    }
	    
}


	    

