package PaymentTestCases;

import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import PAYMENTS.OutwardGuaranteeMaintenance;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC04_IssuenewguaranteewithexistingCIFID {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
    private static final String SHEET_NAME = "TC04";

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }

    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC04_IssuenewguaranteewithexistingCIFID");
        Login login = new Login();

        try {
            // 🔹 Load Test Data
            RowData initialData = ExcelUtils.getRowAsRowData("TC04", 1); // Initial row
            RowData addData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1); // Row 1 = Add
            RowData verifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2); // Row 2 = Verify

            // ---------------- Step 1: Login as Maker ----------------
            login.First();
            OutwardGuaranteeMaintenance guarantee = new OutwardGuaranteeMaintenance(driver);

            // ---------------- Step 2: Add Guarantee ----------------
            Map<String, String> addResult = guarantee.executeWithResultMap(addData, initialData, SHEET_NAME, 1, EXCEL_PATH);
            String guaranteeNo = addResult.get("GuaranteeNo");
            String labelText = addResult.get("LabelText");

            // Validate Guarantee Number captured
            assertGuaranteeCaptured(guaranteeNo, logFile, "Add");
            TestResultLogger.log(logFile, labelText);

            // ---------------- Step 3: Logout Maker ----------------
            LogOut.performLogout(driver, wait);

            // ---------------- Step 4: Login as Checker ----------------
            login.Second();

            ExcelUtils.loadExcel(EXCEL_PATH);
            addData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

            // ---------------- Step 5: Verify Guarantee ----------------
            Map<String, String> verifyResult = guarantee.executeWithResultMap(verifyData, addData, SHEET_NAME, 2, EXCEL_PATH);
            String verifyGuaranteeNo = verifyResult.get("GuaranteeNo");
            String verifyLabelText = verifyResult.get("LabelText");

            assertGuaranteeCaptured(verifyGuaranteeNo, logFile, "Verify");

            String verificationMsg = verifyLabelText;

            TestResultLogger.log(logFile, "✅ " + verificationMsg);
            System.out.println("✅ " + verificationMsg);

        } catch (Exception e) {
            String errorMsg = "❌ Exception Occurred: " + e.getMessage();
            System.err.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);
            Assert.fail(errorMsg);
        } finally {
            // 🔹 Always perform logout at the end
            try {
               LogOut.performLogout(driver, wait);
            } catch (Exception e) {
                System.err.println("⚠️ Logout failed at end: " + e.getMessage());
                TestResultLogger.log(logFile, "⚠️ Logout failed at end: " + e.getMessage());
            }
        }
    }

    private void assertGuaranteeCaptured(String guaranteeNo, String logFile, String phase) throws Exception {
        if (guaranteeNo == null || guaranteeNo.trim().isEmpty()) {
            String errorMsg = "❌ Error: Guarantee No not captured from UI during " + phase + " phase.";
            System.err.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);
            Assert.fail(errorMsg);
        } else {
            System.out.println("✅ Guarantee No Captured during " + phase + ": " + guaranteeNo);
        }
    }
}
