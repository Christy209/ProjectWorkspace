package CASA;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Base.BaseTest;
import Utilities.ExcelUtils;
import Utilities.RowData;

public class VerificationofCashDeposit extends BaseTest {

    protected  WebDriver driver;
    protected  WebDriverWait wait;
	@SuppressWarnings("unused")
	private static Row sheetRow;
	
    public VerificationofCashDeposit (WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
    }
    public void execute(RowData id,RowData tranId,List<String> vr,String sheetName,int idrow,int carow ,String excelpath) throws Exception {
        WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
        searchbox.click();
        searchbox.sendKeys(vr.get(1));
        WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
        element4.click();

        // Switch to required frames
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

        WebElement FunCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
        FunCode.sendKeys("V - Verify");

        WebElement TranId = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("tm.tranId")));
        String tranIdValue = tranId.getByHeader("Created_AccountID");
        TranId.sendKeys(tranIdValue);

        driver.findElement(By.id("Go")).click();
        
        FileInputStream fis = new FileInputStream(excelpath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        // Index 3 corresponds to Excel's 4th row, which contains the "Cash Deposit" data
        Row row1 = sheet.getRow(idrow);//1
        Row row = sheet.getRow(carow); // 0-based index 3

        Map<String, String> appData = getApplicationData(driver, wait, row1, row);
        validateAppData(appData, row1, row);

        workbook.close();
        fis.close();

        try {
            WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Submit")));
            submit.click();
        } catch (Exception e) {
            System.out.println("Error clicking submit: " + e.getMessage());
            // optionally: Assert.fail("Submit button click failed");
        }

        WebElement accountElement = driver.findElement(By.xpath("//td[text()='Transaction ID']/following-sibling::td/label"));
        String actualText = accountElement.getText().trim();
        String expectedMessage = tranIdValue;  // Use actual tranId string
        Assert.assertEquals(actualText, expectedMessage, "Test Failed: TranID verification message is incorrect!");
    }
    
    private static Map<String, String> getApplicationData(WebDriver driver, WebDriverWait wait,Row row1, Row row) throws Exception {
        Map<String, String> appData = new HashMap<>();
        
        appData.put("PartTransactionType", getPartTransactionType(driver));
        System.out.println("PartTransactionType: " + appData.get("PartTransactionType"));
        appData.put("RefCrncy", ExcelUtils.getTextOrValue(driver, wait, "refCrncy", "id"));
        appData.put("RefAmt", ExcelUtils.getTextOrValue(driver, wait, "refAmt", "id"));
        appData.put("AccountId", ExcelUtils.getTextOrValue(driver, wait, "acctId", "id"));
        appData.put("TranParticularcode", ExcelUtils.getTextOrValue(driver, wait, "tranParticularsCode", "id"));

        return appData;
    }

    public static WebElement getTextOrValue(WebDriver driver, WebDriverWait wait, String locator, String byType) {
        if (byType.equalsIgnoreCase("id")) {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locator)));
        } else if (byType.equalsIgnoreCase("name")) {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.name(locator)));
        } else if (byType.equalsIgnoreCase("xpath")) {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
        } else if (byType.equalsIgnoreCase("css")) {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
        }

        // If no matching type found, throw an exception
        throw new IllegalArgumentException("Unsupported locator type: " + byType);
    }
    private static void validateAppData(Map<String, String> appData,Row row1, Row row) {
        Map<String, String> expectedData = new HashMap<>();
        
        expectedData.put("RefCrncy", row.getCell(4).toString().trim());
        expectedData.put("RefAmt", row.getCell(5).toString().trim());
        expectedData.put("AccountId",row1.getCell(74).toString().trim());//////accountid
        expectedData.put("TranParticularcode",row.getCell(6).toString().trim());
        int matchedCount = 0;
        int mismatchedCount = 0;

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String key = entry.getKey();
            String expected = entry.getValue();
            String actual = appData.get(key);

            String normExpected = normalize(expected.replace(",", ""));
            String normActual = normalize((actual != null ? actual : "").replace(",", ""));

            if (isNumeric(normExpected) && isNumeric(normActual)) {
                // Compare numerically
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
                // Compare as normalized strings
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

        // Fail the test if any mismatches are found
        Assert.assertEquals(mismatchedCount, 0, "Test Failed: Mismatches found in data validation!");
   
        }

    public static String getPartTransactionType(WebDriver driver) {
    	
        WebElement debitRadio = driver.findElement(By.xpath("//input[@name='tm.pTranType' and @value='D']"));
        WebElement creditRadio = driver.findElement(By.xpath("//input[@name='tm.pTranType' and @value='C']"));

        if (debitRadio.isSelected()) {
            return "Debit";
        } else if (creditRadio.isSelected()) {
            return "Credit";
        } else {
            return "Unknown";
        }
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

    public static boolean isDateFormat(String value) {
        return value != null && value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}");
    }

    public static String formatDateToStandard(String dateStr) {
        SimpleDateFormat inputFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat inputFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date;
            if (dateStr.contains("/")) {
                date = inputFormat1.parse(dateStr);
            } else {
                date = inputFormat2.parse(dateStr);
            }
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr; // Return original value if parsing fails
        }
    }

}
