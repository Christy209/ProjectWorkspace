package PaymentTestCases;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import CASA.SBA;
import CASA.VerificationOfSavingsAccount;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;


public class TC01_CreateSavingsAccountwithnomineeandrelatedparty {
    
    private WebDriver driver;
    private WebDriverWait  wait;
    
    @BeforeClass
    public void setup() throws Exception {

        driver = DriverManager.getDriver(); 
        wait = new WebDriverWait(driver, Duration.ofSeconds(3)); // initialize driver from DriverManager
    }
    
    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC01_CreateSavingsAccountwithnomineeandrelatedparty");

        try {
            String excelPath = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
            ExcelUtils.loadExcel(excelPath);

            Login login = new Login();
            RowData id = ExcelUtils.getRowAsRowData("TC01", 1);
            List<String> verifyData = ExcelUtils.getRowAsList("TC01", 2);
            String sheetname = "TC01";

            login.First();
            int row = 1;

            // ✅ Get dynamic account number from SBA.execute
            String accountNumber = new SBA(driver).execute(id, sheetname, row, excelPath);
            String resultMessage = "SB Account Created Successfully..! AcctNo: " + accountNumber;
            TestResultLogger.log(logFile, resultMessage);

            ExcelUtils.loadExcel(excelPath);
            id = ExcelUtils.getRowAsRowData("TC01", 1);

            LogOut.performLogout(driver, wait);
            login.Second();
            
            String VAcctId = new VerificationOfSavingsAccount(driver).execute(id, verifyData);
            String resultMsgOfVerification = "Verified A/c. ID: " + VAcctId;
            TestResultLogger.log(logFile, resultMsgOfVerification);
            
            LogOut.performLogout(driver, wait);

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
