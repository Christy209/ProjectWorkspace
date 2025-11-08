package CASA;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.RowData;
import Utilities.WindowHandle;

public class VerificationOfFreeze {
    private WebDriver driver;
    private WebDriverWait wait;

    public VerificationOfFreeze(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @SuppressWarnings("deprecation")
	public void execute(RowData id, List<String> fr, String sheetname,int acctid,int frid) throws Exception {
    	
    	String FunCode = fr.get(2);
    	
        driver.switchTo().defaultContent();
        driver.switchTo().frame("loginFrame");

        WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
        searchbox.click();
        searchbox.sendKeys(fr.get(1));

        WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
        element4.click();

        WindowHandle.handleAlertIfPresent(driver);

        try {
            // Switch to inner application frames
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("afsm.funcCode")));
            WindowHandle.selectByVisibleText(funCode, "V - Verify");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys(id.getByHeader("Created_AccountID"));
            WebElement go = driver.findElement(By.id("Accept"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);

            WebElement checkBox = driver.findElement(By.xpath("//input[@type='checkbox' and @value='00001']"));
            checkBox.click();

            WindowHandle.action(driver);
            // ✅ Extract data from result table

            String AccountId = driver.findElement(By.xpath("//td[text()='A/c. ID']/following-sibling::td//label")).getText().trim();
            String actualAcctId = WindowHandle.extract(AccountId);
            
            List<WebElement> freezeRadios = driver.findElements(
            	    By.xpath("//input[@id='freezeCode' and @type='radio']"));

            	String selectedFreezeCode = null;
//
            	for (WebElement radio : freezeRadios) {
            	    if (radio.isSelected()) {
            	        selectedFreezeCode = radio.getAttribute("value");
            	        break;
            	    }
            	}
            	WebElement reasonCodeInput = driver.findElement(By.xpath("//input[@id='freezeReason' or @name='afsm.freezeReason']")); 
            	String freezeReasonCode = reasonCodeInput.getAttribute("value").trim();

            // ✅ Read expected data from Excel
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/Resource/TestData.xlsx");
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetname);

            Row row1 = sheet.getRow(acctid); // Contains account id
            Row row = sheet.getRow(frid);  // Contains freeze details

            // ✅ Prepare actual data map and validate
            Map<String, String> actualData = new HashMap<>();
            actualData.put("AccountId", actualAcctId);
            actualData.put("FreezeCode", selectedFreezeCode);
            actualData.put("ReasonCode", freezeReasonCode);

            validateAppData(actualData, row1, row,fr);

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("❌ Error during freeze validation: " + e.getMessage());
            throw e;
        }
        WindowHandle.slowDown(1);
        WebElement submit = driver.findElement(By.xpath("//input[@type='button' and @value='Submit']"));
        submit.click();
        
        WindowHandle.ValidationFrame(driver);
        WebElement msgLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
        String actualMessage = msgLabel.getText().trim();
        String expectedMessage = "Total Freeze on Selected A/cs verified successfully.";
        
        if("F - Freeze".equalsIgnoreCase(FunCode)) {
        Assert.assertEquals(actualMessage, expectedMessage, "❌ Freeze message validation failed!");
        System.out.println("✅ Message validated: " + actualMessage);
        }else {
        	Assert.assertTrue(
        	        actualMessage.contains("Unfreeze on Selected A/cs verified successfully."),
        	        "❌ Expected operation success message was not found! Actual: " + actualMessage);
        }
    }

    private static void validateAppData(Map<String, String> appData, Row row1, Row row,List<String> fr) {
    	
    	String FunCode = fr.get(2);
    	
        Map<String, String> expectedData = new HashMap<>();
        
        expectedData.put("AccountId", row1.getCell(74).toString().trim());
        if("F - Freeze".equalsIgnoreCase(FunCode)) { 
        expectedData.put("FreezeCode", row.getCell(3).toString().trim());
        expectedData.put("ReasonCode", row.getCell(4).toString().trim());
        }
        
        

        int matchedCount = 0;
        int mismatchedCount = 0;

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String key = entry.getKey();
            String expected = entry.getValue();
            String actual = appData.getOrDefault(key, "");

            String normExpected = normalize(expected.replace(",", ""));
            String normActual = normalize(actual.replace(",", ""));

            if (isNumeric(normExpected) && isNumeric(normActual)) {
                BigDecimal expectedNum = new BigDecimal(normExpected);
                BigDecimal actualNum = new BigDecimal(normActual);
                if (expectedNum.compareTo(actualNum) != 0) {
                    System.out.println("❌ Mismatch in " + key + ": expected [" + expected + "] but found [" + actual + "]");
                    mismatchedCount++;
                } else {
                    System.out.println("✅ " + key + " matched: " + actual);
                    matchedCount++;
                }
            } else {
                if (!normExpected.equals(normActual)) {
                    System.out.println("❌ Mismatch in " + key + ": expected [" + expected + "] but found [" + actual + "]");
                    mismatchedCount++;
                } else {
                    System.out.println("✅ " + key + " matched: " + actual);
                    matchedCount++;
                }
            }
        }

        System.out.println("Total Matched: " + matchedCount);
        System.out.println("Total Mismatched: " + mismatchedCount);

        Assert.assertEquals(mismatchedCount, 0, "❌ Test Failed: Mismatches found in data validation!");
    }

    private static String normalize(String value) {
        if (value == null || value.trim().isEmpty()) return "";
        value = value.trim().replaceAll("\\.0+$", ""); // Remove trailing .0
        if (value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}")) {
            return value.replace("-", "/"); // Normalize date separators
        }
        return value.toLowerCase();
    }

    private static boolean isNumeric(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
