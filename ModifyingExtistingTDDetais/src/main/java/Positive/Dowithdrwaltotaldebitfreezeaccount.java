package Positive;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class Dowithdrwaltotaldebitfreezeaccount extends BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;

    private String excelpath = System.getProperty("user.dir") + "/resources/TestData.xlsx";
    private int row = 7;
    private String sheetname = "Sheet1";

    @BeforeClass
    public void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        String userid = DriverManager.getProperty("userid");
        String password = DriverManager.getProperty("password");
        DriverManager.login(userid, password);
    }

    @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet1", rowIndex = {1, 7})
    public void Addfreeze(RowData inputData, RowData id) throws Exception {
        WindowHandle.handleAlertIfPresent(driver);
        WindowHandle.slowDown(4);

        // Navigate to menu
        WebElement menuSelect = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect")));
        WindowHandle.setValueWithJS(driver, menuSelect, id.getByIndex(1));

        WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

        try {
            // Switch to required frames
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            // Fill function code and transaction type
            WebElement funCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
            funCode.sendKeys(id.getByIndex(2));

            WebElement transactionType = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranTypeSubType")));
            transactionType.sendKeys(id.getByIndex(3));

            // Click Go
            driver.findElement(By.id("Go")).click();
        } catch (Exception e) {
            System.out.println("Error in navigation frame: " + e.getMessage());
        }

        try {
            WindowHandle.slowDown(1);

            WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
            String AcctID = inputData.getByHeader("Created_AccountID");
            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);
            
//            String accountIdFromSheet1 = getCellDataByColumnName(excelpath, "Sheet1", 1, "Created_AccountID");
//
//	           // Log for clarity
//	           System.out.println("🔹 Account ID fetched from Sheet1: " + accountIdFromSheet1);
//
//	           // ✅ Enter into field
//	           wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys(accountIdFromSheet1);

            
            WebElement currency = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refCrncy")));
            currency.sendKeys(id.getByIndex(4));

            WindowHandle.slowDown(1);

            WebElement amount = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refAmt")));
            amount.sendKeys(id.getByIndex(5));

            WebElement tranParticularCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranParticularsCode")));
            tranParticularCode.sendKeys(id.getByIndex(6));

            WebElement denomButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("DENOMDTLS")));
            denomButton.click();

            WindowHandle.slowDown(1);

            WebElement denomCount = wait.until(ExpectedConditions.elementToBeClickable(By.id("arrDenomCount")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", denomCount);
            denomCount.clear();
            denomCount.sendKeys(id.getByIndex(8));

            WebElement okButton = driver.findElement(By.xpath("//input[@type='button' and @id='OK']"));
            okButton.click();

            WindowHandle.slowDown(1);

            WebElement post = driver.findElement(By.id("Post"));
            post.click();

            String mainWindow = driver.getWindowHandle();
            WindowHandle.handlePopupIfExists(driver);

            // Handle Accept popup if exists
            try {
                WebElement acceptButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Accept")));
                if (acceptButton.isDisplayed() && acceptButton.isEnabled()) {
                    acceptButton.click();
                    System.out.println("Clicked Accept button.");
                }
            } catch (Exception e) {
                System.out.println("Accept button not present, may have been auto-handled.");
            }

            driver.switchTo().window(mainWindow);
            WindowHandle.slowDown(1);

            // Capture result label
            WindowHandle.ValidationFrame(driver);
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody//tr//label[1]")));
            String labelText = label.getText();

            if (labelText == null || labelText.isEmpty()) {
                System.out.println("Error: Failed to create transaction.");
            } else {
                System.out.println("TranID Created Successfully..! TranID: " + labelText);
            }

            // Update Excel
            ExcelUtils.updateExcel(excelpath, sheetname, row, "Created_AccountID", labelText);

        } catch (Exception e) {
            System.out.println("CashWithdrawal error: " + e.getMessage());
            throw e;
        } finally {
            driver.switchTo().defaultContent();
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
