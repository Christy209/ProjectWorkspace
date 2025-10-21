package PaymentTestCases;

import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.BBSSL.Utilities.LogOut;
import Base.DriverManager;
import PAYMENTS.ForwardContractMaintenance;
import PAYMENTS.VerificationOfForwardContractMaintenance;
import Utilities.ExcelUtils;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC06_ModifyForwardContractToTheAmountOf15000 {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC06_ModifyForwardContractToTheAmountOf15000");
        try {
            // Load Excel test data
            String excelPath = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
            ExcelUtils.loadExcel(excelPath);

            Login login = new Login();
            RowData fwdcno = ExcelUtils.getRowAsRowData("TC05", 1);
            RowData inputData = ExcelUtils.getRowAsRowData("TC06", 1);
            RowData VerifyData = ExcelUtils.getRowAsRowData("TC06",2);

            login.First();

            Map<String, String> result =  new ForwardContractMaintenance(driver).execute(inputData,fwdcno, "TC06", 1, excelPath);
            
            if (result.containsKey("errorMsg")) {
                String errorMsg = result.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
                Assert.fail("Test failed: " + errorMsg);
            } else {
                String labelText = result.get("labelText");
                System.out.println(labelText);
                TestResultLogger.log(logFile,labelText);
            }// File
            
            ExcelUtils.loadExcel(excelPath); 
            inputData = ExcelUtils.getRowAsRowData("TC06", 1);
            
            LogOut.performLogout(driver, wait);
            login.Second();
            
            //String VForwardContractNo =  new VerificationOfForwardContractMaintenance(driver).execute(fwdcno,VerifyData, "TC06", 2, excelPath);
            VerificationOfForwardContractMaintenance VMNTFWC = new VerificationOfForwardContractMaintenance(driver);
	        Map<String, String> Verifyresult = VMNTFWC.execute(VerifyData ,inputData,"TC06", 2, excelPath);
	           
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