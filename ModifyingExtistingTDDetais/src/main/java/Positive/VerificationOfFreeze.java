package Positive;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.RowData;
import Utilities.WindowHandle;

public class VerificationOfFreeze extends BaseTest {

    private WebDriver driver;
    private WebDriverWait wait;

    String excelpath = System.getProperty("user.dir") + "/resources/TestData.xlsx";
    int row = 1;
    String sheetname = "TC01";

    @BeforeClass
    public void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        String userid = DriverManager.getProperty("userid");
        String password = DriverManager.getProperty("password");
        DriverManager.login(userid, password);
    }

    @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "TC01", rowIndex = {1, 8})
    public void Addfreeze(RowData inputData, RowData id) throws Exception {
        WindowHandle.handleAlertIfPresent(driver);
        WindowHandle.slowDown(2);

        // Navigate to menu
        WebElement menuSelect = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect")));
        WindowHandle.setValueWithJS(driver, menuSelect, id.getByIndex(1));

        WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

        try {
            // Switch to required frames
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            // Select function code
            WebElement funCodeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("afsm.funcCode")));
            String FunCode = id.getByIndex(2);
            WindowHandle.selectByVisibleText(funCodeDropdown, "V - Verify");

            String accountIdFromSheet1 = getCellDataByColumnName(excelpath, "Sheet1", 1, "Created_AccountID");

	           // Log for clarity
	           System.out.println("🔹 Account ID fetched from Sheet1: " + accountIdFromSheet1);

	           // ✅ Enter into field
	           wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys(accountIdFromSheet1);
            // Click Accept
            WebElement go = driver.findElement(By.id("Accept"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);

            // Select checkbox
            WebElement checkBox = driver.findElement(By.xpath("//input[@type='checkbox' and @value='00001']"));
            checkBox.click();

            // Scroll and perform actions
            WindowHandle.action(driver);

            // Extract UI data
            String actualAcctId = WindowHandle.extract(
                driver.findElement(By.xpath("//td[text()='A/c. ID']/following-sibling::td//label")).getText().trim()
            );

            List<WebElement> freezeRadios = driver.findElements(By.xpath("//input[@id='freezeCode' and @type='radio']"));
            String selectedFreezeCode = null;
            for (WebElement radio : freezeRadios) {
                if (radio.isSelected()) {
                    selectedFreezeCode = radio.getAttribute("value");
                    break;
                }
            }

            String freezeReasonCode = driver.findElement(By.xpath("//input[@id='freezeReason' or @name='afsm.freezeReason']"))
                                          .getAttribute("value").trim();

            // Read expected data from Excel
            try (FileInputStream fis = new FileInputStream(excelpath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheet(sheetname);
                int acctRowIndex = 1; // Update as needed
                int frRowIndex = 1;   // Update as needed

                Row row1 = sheet.getRow(acctRowIndex);
                Row row2 = sheet.getRow(frRowIndex);

                // Prepare actual data map
                Map<String, String> actualData = new HashMap<>();
                actualData.put("AccountId", actualAcctId);
                actualData.put("FreezeCode", selectedFreezeCode);
                actualData.put("ReasonCode", freezeReasonCode);

                // Extract FunCode values as list for validation
                List<String> fr = Arrays.asList(
                    id.getByIndex(0), id.getByIndex(1), id.getByIndex(2), id.getByIndex(3), id.getByIndex(4)
                );

                // Validate UI data against Excel
                validateAppData(actualData, row1, row2, fr);
            }

            // Click Submit
            WindowHandle.slowDown(1);
            WebElement submit = driver.findElement(By.xpath("//input[@type='button' and @value='Submit']"));
            submit.click();

            // Validate success message
            WindowHandle.ValidationFrame(driver);
            WebElement msgLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
            String actualMessage = msgLabel.getText().trim();

            String expectedMessage = "Total Freeze on Selected A/cs verified successfully.";
            if ("F - Freeze".equalsIgnoreCase(FunCode)) {
                Assert.assertEquals(actualMessage, expectedMessage, "❌ Freeze message validation failed!");
            } else {
                Assert.assertTrue(actualMessage.contains("Unfreeze on Selected A/cs verified successfully."),
                                  "❌ Expected operation success message was not found! Actual: " + actualMessage);
            }

            System.out.println("✅ Freeze verification message: " + actualMessage);

        } catch (Exception e) {
            System.out.println("❌ Error during freeze verification: " + e.getMessage());
            throw e;
        } finally {
            driver.switchTo().defaultContent(); // Always switch back to default content
        }
    }

    private static void validateAppData(Map<String, String> appData, Row row1, Row row2, List<String> fr) {
        String FunCode = fr.get(2);

        Map<String, String> expectedData = new HashMap<>();
        expectedData.put("AccountId", row1.getCell(74).toString().trim());
        if ("F - Freeze".equalsIgnoreCase(FunCode)) {
            expectedData.put("FreezeCode", row2.getCell(3).toString().trim());
            expectedData.put("ReasonCode", row2.getCell(4).toString().trim());
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
                if (new BigDecimal(normExpected).compareTo(new BigDecimal(normActual)) != 0) {
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
        value = value.trim().replaceAll("\\.0+$", "");
        if (value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}")) {
            return value.replace("-", "/");
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
    @SuppressWarnings("unused")
	 private String getCellDataByColumnName(String filePath, String sheetName, int rowNum, String columnName) {
	     try (FileInputStream fis = new FileInputStream(filePath);
	          XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

	         XSSFSheet sheet = workbook.getSheet(sheetName);
	         if (sheet == null) {
	             System.out.println("❌ Sheet not found: " + sheetName);
	             return "";
	         }

	         Row headerRow = sheet.getRow(0);
	         if (headerRow == null) {
	             System.out.println("❌ Header row missing in sheet: " + sheetName);
	             return "";
	         }

	         int columnIndex = -1;
	         for (int i = 0; i < headerRow.getLastCellNum(); i++) {
	             if (headerRow.getCell(i).getStringCellValue().trim().equalsIgnoreCase(columnName)) {
	                 columnIndex = i;
	                 break;
	             }
	         }

	         if (columnIndex == -1) {
	             System.out.println("❌ Column not found: " + columnName);
	             return "";
	         }

	         Row dataRow = sheet.getRow(rowNum);
	         if (dataRow == null || dataRow.getCell(columnIndex) == null) {
	             System.out.println("⚠️ Data not found at row: " + rowNum);
	             return "";
	         }

	         org.apache.poi.ss.usermodel.Cell cell = dataRow.getCell(columnIndex);
	         String cellValue = "";

	         switch (cell.getCellType()) {
	             case STRING:
	                 cellValue = cell.getStringCellValue().trim();
	                 break;

	             case NUMERIC:
	                 // ✅ Use BigDecimal to preserve full numeric precision (no loss of digits)
	                 java.math.BigDecimal bd = new java.math.BigDecimal(cell.getNumericCellValue());
	                 cellValue = bd.toPlainString().trim();
	                 break;

	             case FORMULA:
	                 try {
	                     cellValue = cell.getStringCellValue().trim();
	                 } catch (Exception e) {
	                     java.math.BigDecimal bd2 = new java.math.BigDecimal(cell.getNumericCellValue());
	                     cellValue = bd2.toPlainString().trim();
	                 }
	                 break;

	             default:
	                 cellValue = cell.toString().trim();
	         }

	         return cellValue;

	     } catch (Exception e) {
	         System.out.println("❌ Error reading Excel cell: " + e.getMessage());
	         return "";
	     }
	 }

}
