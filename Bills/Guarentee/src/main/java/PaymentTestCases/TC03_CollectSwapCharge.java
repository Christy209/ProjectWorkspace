package PaymentTestCases;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import PAYMENTS.SwapChargeCollection;
import Utilities.ExcelUtils;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC03_CollectSwapCharge {

	private WebDriver driver;
    @SuppressWarnings("unused")
	private WebDriverWait  wait;
    
    @BeforeClass
    public void setup() throws Exception {

        driver = DriverManager.getDriver(); 
        wait = new WebDriverWait(driver, Duration.ofSeconds(3)); // initialize driver from DriverManager
    }
    
    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC03");

        try {
            String excelPath = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
            ExcelUtils.loadExcel(excelPath);

            Login login = new Login();
            RowData id = ExcelUtils.getRowAsRowData("TC03", 1);
            //List<String> verifyData = ExcelUtils.getRowAsList("TC01", 2);
            String sheetname = "TC03";

            login.First();
            int row = 1;

            // ✅ Get dynamic account number from SBA.execute
            String accountNumber = new SwapChargeCollection(driver).execute(id, sheetname, row, excelPath);
            String resultMessage = "SB Account Created Successfully..! AcctNo: " + accountNumber;
            TestResultLogger.log(logFile, resultMessage);
            
            
        } catch (AssertionError ae) {
            // Capture assertion errors from ErrorCapture.checkForError()
            String errorMsg = "❌ Error Found: " + ae.getMessage();
            System.out.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);

            // Fail TestNG properly
            throw ae;
        } catch (Exception e) {
            // Capture unexpected exceptions
            String errorMsg = "❌ Exception Occurred: " + e.getMessage();
            System.out.println(errorMsg);
            TestResultLogger.log(logFile, errorMsg);

            throw new RuntimeException(errorMsg, e);
        }
    }
}