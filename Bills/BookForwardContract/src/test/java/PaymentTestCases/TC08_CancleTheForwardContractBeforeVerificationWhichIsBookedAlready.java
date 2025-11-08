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
import PAYMENTS.VerificationOfForwardContractMaintenance;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC08_CancleTheForwardContractBeforeVerificationWhichIsBookedAlready {
	 	private WebDriver driver;
	    private WebDriverWait wait;
	    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
	    private static final String SHEET_NAME = "TC08";

	    @BeforeClass
	    public void setup() throws Exception {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        ExcelUtils.loadExcel(EXCEL_PATH);
	    }

	    @Test
	    public void runTest() throws Exception {
	        String logFile = TestResultLogger.createLogFile("TC08_CancleTheForwardContractBeforeVerificationWhichIsBookedAlready");
	        Login login = new Login();

	        try {
	            RowData add = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
	            RowData cancel = ExcelUtils.getRowAsRowData(SHEET_NAME, 2);
	            RowData VerifyData = ExcelUtils.getRowAsRowData(SHEET_NAME,3); // Row 2 = Invoke

	            // ---------------- Step 1: Login ----------------
	            login.First();
	            ForwardContractMaintenance FrwConNo = new ForwardContractMaintenance(driver);
	//
	            // ---------------- Step 2: Mark Invoke ----------------
	            Map<String, String> Book = FrwConNo.execute(add,add ,SHEET_NAME, 1, EXCEL_PATH);
	            
	            if (Book.containsKey("errorMsg")) {
	                String errorMsg = Book.get("errorMsg");
	                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
	                Assert.fail("Test failed: " + errorMsg);
	            } else {
	                String labelText = Book.get("labelText");
	                System.out.println(labelText);
	                TestResultLogger.log(logFile,labelText);
	            }// File
	            LogOut.performLogout(driver, wait);
	            login.First();
	            
		        ExcelUtils.loadExcel(EXCEL_PATH);
		        add = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
		        
	            Map<String, String> Cancel = FrwConNo.execute(cancel,add ,SHEET_NAME, 2, EXCEL_PATH);
	            
	            if (Cancel.containsKey("errorMsg")) {
	                String errorMsg = Cancel.get("errorMsg");
	                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
	                Assert.fail("Test failed: " + errorMsg);
	            } else {
	                String labelText = Cancel.get("labelText");
	                System.out.println(labelText);
	                TestResultLogger.log(logFile,labelText);
	            }// File
	            
	            LogOut.performLogout(driver, wait);
	            login.Second();

		        ExcelUtils.loadExcel(EXCEL_PATH);
		        cancel = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
		          
		        VerificationOfForwardContractMaintenance VMNTFWC = new VerificationOfForwardContractMaintenance(driver);
		        Map<String, String> Verifyresult = VMNTFWC.execute(VerifyData,cancel ,SHEET_NAME, 3, EXCEL_PATH);
		           
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
