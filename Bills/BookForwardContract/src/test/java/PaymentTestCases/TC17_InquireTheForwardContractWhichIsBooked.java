package PaymentTestCases;

import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import PAYMENTS.MNTFWC_Verification;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC17_InquireTheForwardContractWhichIsBooked {

	private WebDriver driver;
    private WebDriverWait wait;
    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
    private static final String SHEET_NAME = "TC17";

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }

    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC17_InquireTheForwardContractWhichIsBooked");
        Login login = new Login();

        try {
        	RowData bookno = ExcelUtils.getRowAsRowData("TC05", 1);
            RowData fwdcno = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);


            // ---------------- Step 1: Login ----------------
            login.First();
            MNTFWC_Verification FrwConNo = new MNTFWC_Verification(driver);

            // ---------------- Step 2: Mark Invoke ----------------
            Map<String, String> result = FrwConNo.execute(fwdcno,bookno ,SHEET_NAME, 1, EXCEL_PATH);
            
            if (result.containsKey("errorMsg")) {
                String errorMsg = result.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
                Assert.fail("Test failed: " + errorMsg);
            } else {
                TestResultLogger.log(logFile, "✅ AppData captured at inquiry time:");

                for (Map.Entry<String, String> entry : result.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    TestResultLogger.log(logFile, key + " = " + value);
                }
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
            // 🔹 Always perform logout at the end, even if test failed
            try {
            	LogOut.performLogout(driver, wait);
            } catch (Exception e) {
                System.err.println("⚠️ Logout failed at end: " + e.getMessage());
                TestResultLogger.log(logFile, "⚠️ Logout failed at end: " + e.getMessage());
            }
        }
}

}
