package TestCases;

import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import Payments.ExportAndOutwardBill;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TCO4_Outward_FreeDelivery {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/resources/FBTCDATA.xlsx";
    private static final String SHEET_NAME = "TC07";

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }

    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TCO4_Outward_FreeDelivery");
        Login login = new Login();

        try {
            RowData initialData = ExcelUtils.getRowAsRowData("TC01", 1);
            RowData Realize = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            RowData VerifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2); // Row 2 = Invoke

            login.First();
            ExportAndOutwardBill FrwConNo = new ExportAndOutwardBill(driver);

            // ---------------- Step 2: Mark Invoke ----------------
            Map<String, String> markResult = FrwConNo.execute(Realize, initialData, SHEET_NAME, 1, EXCEL_PATH);
            String labelText = markResult.get("labelText"); // ✅ Correct key

            if (labelText == null || labelText.isEmpty()) {
                labelText = markResult.get("LABELTEXT_BILLID"); // fallback
            }

            if (markResult.containsKey("errorMsg")) {
                String errorMsg = markResult.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
            } else {
                System.out.println("✅ Bill ID / Label: " + labelText);
                TestResultLogger.log(logFile, "✅ Bill ID / Label: " + labelText);
            }

            LogOut.performLogout(driver, wait);
            login.Second();

            ExcelUtils.loadExcel(EXCEL_PATH);
            Realize = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

            // ---------------- Step 3: Verify ----------------
            Map<String, String> Verifyresult = FrwConNo.execute(VerifyData, Realize, SHEET_NAME, 2, EXCEL_PATH);
            String verifyLabelText = Verifyresult.get("labelText"); // ✅ Correct key
            String verifyBillID = Verifyresult.get("LABELTEXT_BILLID");

            if (verifyLabelText == null || verifyLabelText.isEmpty()) {
                verifyLabelText = verifyBillID; // fallback
            }

            if (Verifyresult.containsKey("errorMsg")) {
                String errorMsg = Verifyresult.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
            } else {
                System.out.println("✅ Verified Bill ID / Label: " + verifyLabelText);
                TestResultLogger.log(logFile, "✅ Verified Bill ID / Label: " + verifyLabelText);
            }

        } catch (AssertionError ae) {
            String errorMsg = "❌ Assertion Failure: " + ae.getMessage();
            System.err.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);
        } catch (Exception e) {
            String errorMsg = "❌ Exception Occurred: " + e.getMessage();
            System.err.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);
        } finally {
            try {
                LogOut.performLogout(driver, wait);
                System.out.println("✅ Logout performed successfully at end.");
            } catch (Exception e) {
                System.err.println("⚠️ Logout failed at end: " + e.getMessage());
                TestResultLogger.log(logFile, "⚠️ Logout failed at end: " + e.getMessage());
            }
        }
    }
}
