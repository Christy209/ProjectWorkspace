package PaymentTestCases;

import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import PAYMENTS.ForwardContractMaintenance;
import PAYMENTS.MNTFWC_Verification;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC05_BookForwardContractToTheSavingsAccount {
	 	private WebDriver driver;
	    private WebDriverWait wait;
	    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
	    private static final String SHEET_NAME = "TC05";

	    @BeforeClass
	    public void setup() throws Exception {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        ExcelUtils.loadExcel(EXCEL_PATH);
	    }

	    @Test
	    public void runTest() throws Exception {
	        String logFile = TestResultLogger.createLogFile("TC05_BookForwardContractToTheSavingsAccount");
	        Login login = new Login();

	        try {
	            RowData fwdcno = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
	            RowData inputData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
	            RowData VerifyData = ExcelUtils.getRowAsRowData(SHEET_NAME,2); // Row 2 = Invoke

	            // ---------------- Step 1: Login ----------------
	            login.First();
	            ForwardContractMaintenance FrwConNo = new ForwardContractMaintenance(driver);
//	//
	            // ---------------- Step 2: Mark Invoke ----------------
	            Map<String, String> result = FrwConNo.execute(inputData,fwdcno ,SHEET_NAME, 1, EXCEL_PATH);
	            
	            if (result.containsKey("errorMsg")) {
	                String errorMsg = result.get("errorMsg");
	                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
	                Assert.fail("Test failed: " + errorMsg);
	            } else {
	                String labelText = result.get("labelText");
	                System.out.println(labelText);
	                TestResultLogger.log(logFile,labelText);
	            }// File
	            
	            LogOut.performLogout(driver, wait);
	            login.Second();

		        ExcelUtils.loadExcel(EXCEL_PATH);
		        inputData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
		          
		        MNTFWC_Verification VMNTFWC = new MNTFWC_Verification(driver);
		        Map<String, String> Verifyresult = VMNTFWC.execute(VerifyData,inputData ,SHEET_NAME, 2, EXCEL_PATH);
		           
		            if (Verifyresult.containsKey("errorMsg")) {
		                String errorMsg = Verifyresult.get("errorMsg");
		                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
		                Assert.fail("Test failed: " + errorMsg);
		            } else {
		                String labelText = Verifyresult.get("labelText");
		                System.out.println(labelText);
		                TestResultLogger.log(logFile,labelText);
		            }// File

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
	                //System.out.println("✅ Logout performed successfully at end.");
	            } catch (Exception e) {
	                System.err.println("⚠️ Logout failed at end: " + e.getMessage());
	                TestResultLogger.log(logFile, "⚠️ Logout failed at end: " + e.getMessage());
	            }
	        }
}
}
