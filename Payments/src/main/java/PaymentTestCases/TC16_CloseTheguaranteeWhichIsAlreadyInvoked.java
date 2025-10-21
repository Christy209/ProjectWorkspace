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

public class TC16_CloseTheguaranteeWhichIsAlreadyInvoked {

	
	  private WebDriver driver;
	    private WebDriverWait wait;
	    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
	    private static final String SHEET_NAME = "TC16";

	    @BeforeClass
	    public void setup() throws Exception {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        ExcelUtils.loadExcel(EXCEL_PATH);
	    }

	    @Test
	    public void runTest() throws Exception {
	        String logFile = TestResultLogger.createLogFile("TC16_CloseTheguaranteeWhichIsAlreadyInvoked");
	        Login login = new Login();

	        try {
	            // 🔹 Load Test Data
	            RowData initialData = ExcelUtils.getRowAsRowData("TC04", 1);
	            RowData modifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
	            RowData verifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2);

	            // ---------------- Step 1: Login as Maker ----------------
	            login.First();
	            OutwardGuaranteeMaintenance guarantee = new OutwardGuaranteeMaintenance(driver);

	            // ---------------- Step 2: Mark Guarantee for Invocation ----------------
	            Map<String, String> markResult = guarantee.executeWithResultMap(modifyData, initialData, SHEET_NAME, 1, EXCEL_PATH);
	            String guaranteeNo = markResult.get("GuaranteeNo");
	            String labelText = markResult.get("LabelText");

	            assertGuaranteeCaptured(guaranteeNo, logFile, "Closed");
	            TestResultLogger.log(logFile,labelText);

	            // ---------------- Step 3: Logout Maker ----------------
	            LogOut.performLogout(driver, wait);

	            // ---------------- Step 4: Login as Checker ----------------
	            login.Second();

	            ExcelUtils.loadExcel(EXCEL_PATH);
	            modifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

	            // ---------------- Step 5: Verify Guarantee ----------------
	            Map<String, String> verifyResult = guarantee.executeWithResultMap(verifyData, modifyData, SHEET_NAME, 2, EXCEL_PATH);
	            String verifyGuaranteeNo = verifyResult.get("GuaranteeNo");
	            String verifyLabelText = verifyResult.get("LabelText");

	            assertGuaranteeCaptured(verifyGuaranteeNo, logFile, "Verify");

	            String verificationMsg =  verifyLabelText;

	            TestResultLogger.log(logFile,verificationMsg);
	            System.out.println("✅ " + verificationMsg);

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
