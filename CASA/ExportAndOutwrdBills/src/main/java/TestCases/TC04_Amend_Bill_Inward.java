package TestCases;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Base.DriverManager;
import Payments.ImportAndInwardBills;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC04_Amend_Bill_Inward {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/resources/MIIB.xlsx";
    private static final String SHEET_NAME = "TC04";

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }

    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC04_Amend_Bill_Inward");
        Login login = new Login();

        try {
            // Load test data
            RowData initialData = ExcelUtils.getRowAsRowData("TC01", 1);
            RowData realizeData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            RowData verifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2);

            // ---------------- Step 1: Login ----------------
       //     login.First();

            ImportAndInwardBills frwConNo = new ImportAndInwardBills(driver);

//            // ---------------- Step 2: Mark Invoke ----------------
//            Map<String, String> markResult = frwConNo.execute(realizeData, initialData, SHEET_NAME, 1, EXCEL_PATH);
//            logMap(markResult, logFile);
//
//            String labelText = markResult.get("LabelText");
//            String billId = markResult.get("LABELTEXT_BILLID");
//
//            TestResultLogger.log(logFile, "LabelText: " + (labelText != null ? labelText : "null"));
//            TestResultLogger.log(logFile, "BillID: " + (billId != null ? billId : "null"));
//
//            if (markResult.containsKey("errorMsg")) {
//                String errorMsg = markResult.get("errorMsg");
//                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
//                Assert.fail("Test failed: " + errorMsg);
//            }
//
//            // ---------------- Step 3: Logout safely ----------------
//            try {
//                LogOut.performLogout(driver, wait);
//                // Handle logout confirmation alert if present
//                try {
//                    wait.until(ExpectedConditions.alertIsPresent());
//                    Alert alert = driver.switchTo().alert();
//                    System.out.println("⚠️ Logout confirmation alert appeared: " + alert.getText());
//                    alert.accept();
//                    System.out.println("✅ Logout alert accepted.");
//                    TestResultLogger.log(logFile, "✅ Logout alert accepted.");
//                } catch (Exception e) {
//                    System.out.println("⚠️ No logout alert appeared.");
//                    TestResultLogger.log(logFile, "⚠️ No logout alert appeared.");
//                }
//            } catch (Exception e) {
//                System.err.println("⚠️ Logout failed: " + e.getMessage());
//                TestResultLogger.log(logFile, "⚠️ Logout failed: " + e.getMessage());
//            }

            // ---------------- Step 4: Login again ----------------
          //  LogOut.performLogout(driver, wait);
            login.Second();

            // Reload Excel to get updated data
            ExcelUtils.loadExcel(EXCEL_PATH);
            realizeData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

            // ---------------- Step 5: Verify ----------------
            Map<String, String> verifyResult = frwConNo.execute(verifyData, realizeData, SHEET_NAME, 2, EXCEL_PATH);
            logMap(verifyResult, logFile);

            String verifyLabelText = verifyResult.get("LabelText");
            String verifyBillId = verifyResult.get("LABELTEXT_BILLID");

            TestResultLogger.log(logFile, "Verification LabelText: " + (verifyLabelText != null ? verifyLabelText : "null"));
            TestResultLogger.log(logFile, "Verification BillID: " + (verifyBillId != null ? verifyBillId : "null"));

            if (verifyResult.containsKey("errorMsg")) {
                String errorMsg = verifyResult.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
                Assert.fail("Test failed: " + errorMsg);
            }

        } catch (AssertionError ae) {
            String errorMsg = "❌ Assertion Failure: " + ae.getMessage();
            System.err.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);
            Assert.fail(errorMsg);
        } catch (Exception e) {
            String errorMsg = "❌ Exception Occurred: " + e.getMessage();
            System.err.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);
            Assert.fail(errorMsg);
        } finally {
            // Final logout attempt
            try {
                LogOut.performLogout(driver, wait);
                // Handle logout confirmation alert if present
                try {
                    wait.until(ExpectedConditions.alertIsPresent());
                    Alert alert = driver.switchTo().alert();
                    alert.accept();
                    TestResultLogger.log(logFile, "✅ Final logout alert accepted.");
                } catch (Exception ignored) { }
            } catch (Exception ignored) { }
        }
    }

    // ---------------- Utility Methods ----------------
    private void logMap(Map<String, String> map, String logFile) {
        if (map != null) {
            map.forEach((k, v) -> System.out.println(k + " = " + v));
            map.forEach((k, v) -> TestResultLogger.log(logFile, k + " = " + (v != null ? v : "null")));
        }
    }
}
