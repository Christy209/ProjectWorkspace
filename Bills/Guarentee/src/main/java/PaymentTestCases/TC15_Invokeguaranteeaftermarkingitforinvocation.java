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

public class TC15_Invokeguaranteeaftermarkingitforinvocation {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
    private static final String SHEET_NAME = "TC15";

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }

    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC15_Invokeguaranteeaftermarkingitforinvocation");
        Login login = new Login();

        try {
            // 🔹 Load Test Data
            RowData initialData = ExcelUtils.getRowAsRowData("TC04", 1);
            RowData markInvokeData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1); // Row 1 = Mark Invoke
            RowData verifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2);     // Row 2 = Invoke

            // ---------------- Step 1: Login ----------------
            login.First();
           OutwardGuaranteeMaintenance guarantee = new OutwardGuaranteeMaintenance(driver);

            // ---------------- Step 2: Mark Invoke ----------------
            Map<String, String> markInvokeResult = guarantee.executeWithResultMap(markInvokeData, initialData, SHEET_NAME, 1, EXCEL_PATH);
            String markInvokeGuaranteeNo = markInvokeResult.get("GuaranteeNo");
            assertGuaranteeCaptured(markInvokeGuaranteeNo, logFile, "Mark Invoke");
            TestResultLogger.log(logFile, markInvokeGuaranteeNo);

            // ---------------- Step 3: Logout after Mark Invoke ----------------
            LogOut.performLogout(driver, wait);

            // ---------------- Step 4: Login for Verification ----------------
            login.Second();

	          ExcelUtils.loadExcel(EXCEL_PATH);
	          markInvokeData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            // ---------------- Step 5: Invoke Verification ----------------
            Map<String, String> result = guarantee.executeWithResultMap(verifyData, markInvokeData, SHEET_NAME, 2, EXCEL_PATH);
            String guaranteeNo = result.get("GuaranteeNo");
            String labelText = result.get("LabelText");

            assertGuaranteeCaptured(guaranteeNo, logFile, "Verify");

            String verificationMsg = labelText;

            TestResultLogger.log(logFile, verificationMsg);
            System.out.println(verificationMsg);

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
            // 🔹 Always perform logout at the end, even if test failed
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
