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
	import Utilities.ExcelUtils;
	import Utilities.LogOut;
	import Utilities.Login;
	import Utilities.RowData;
	import Utilities.TestResultLogger;
	
	public class TC19_CloseForwardContractWhichIsHavingBalance {
		private WebDriver driver;
	    private WebDriverWait wait;
	    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
	    private static final String SHEET_NAME = "TC19";
	
	    @BeforeClass
	    public void setup() throws Exception {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        ExcelUtils.loadExcel(EXCEL_PATH);
	    }
	
	    @Test
	    public void runTest() throws Exception {
	        String logFile = TestResultLogger.createLogFile("TC19_CloseForwardContractWhichIsHavingBalance");
	        Login login = new Login();

	        try {
	            RowData fwdcno = ExcelUtils.getRowAsRowData("TC05", 1);
	            RowData Close = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

	            // ---------------- Step 1: Login ----------------
	            login.First();
	            ForwardContractMaintenance FrwConNo = new ForwardContractMaintenance(driver);

	            // ---------------- Step 2: Execute Test ----------------
	            Map<String, String> result = FrwConNo.execute(Close, fwdcno, SHEET_NAME, 1, EXCEL_PATH);

	            if (result.containsKey("errorMsg")) {
	                String errorMsg = result.get("errorMsg");
	                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
	                System.out.println("Error from UI: " + errorMsg);

	                // ✅ If this is the expected validation message, mark test as PASSED
	                if (errorMsg.contains("E4221 - A forward contract with current balance more than zero cannot be closed")) {
	                    TestResultLogger.log(logFile, "✅ Expected validation message received. Test Passed.");
	                    Assert.assertTrue(true, "Expected error displayed correctly");
	                } else {
	                    // ❌ Unexpected error — mark test as failed
	                    Assert.fail("Test failed with unexpected error: " + errorMsg);
	                }
	            } else {
	                // ✅ If no error, log normal result
	                String labelText = result.get("labelText");
	                System.out.println(labelText);
	                TestResultLogger.log(logFile, labelText);
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
	            // 🔹 Always perform logout at the end
	            try {
	                LogOut.performLogout(driver, wait);
	            } catch (Exception e) {
	                System.err.println("⚠️ Logout failed at end: " + e.getMessage());
	                TestResultLogger.log(logFile, "⚠️ Logout failed at end: " + e.getMessage());
	            }
	        }
	    }
	}
