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

public class TC01_Payments_LodgeOutwardBill {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/resources/FBTCDATA.xlsx";
    private static final String SHEET_NAME = "TC01";

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }


    @Test
    public void runTest() throws Exception {
        String logFile = TestResultLogger.createLogFile("TC01_Payments_LodgeOutwardBill");
        Login login = new Login();

        try {
            RowData fwdcno = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            RowData VerifyData = ExcelUtils.getRowAsRowData(SHEET_NAME,2);

            // ---------------- Step 1: Login ----------------//
            login.First();
            ExportAndOutwardBill FrwConNo = new ExportAndOutwardBill(driver);

            // ---------------- Step 2: Mark Invoke ----------------//
            Map<String, String> result = FrwConNo.execute(fwdcno,fwdcno ,SHEET_NAME, 1, EXCEL_PATH);
            //TestResultLogger.log(logFile, "Lodge");
            
            if (result.containsKey("errorMsg")) {
                String errorMsg = result.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
               Assert.fail("Test failed: " + errorMsg);
            } else {
                String labelText = result.get("labelText");
                System.out.println(labelText);
                TestResultLogger.log(logFile,labelText);
            }
            
            LogOut.performLogout(driver, wait);
            login.Second();

	        ExcelUtils.loadExcel(EXCEL_PATH);
	        fwdcno = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
	          
	       
	        Map<String, String> Verifyresult = FrwConNo.execute(VerifyData,fwdcno,SHEET_NAME, 2, EXCEL_PATH);
	       // TestResultLogger.log(logFile, "Verify");
	        
	            if (Verifyresult.containsKey("errorMsg")) {
	                String errorMsg = Verifyresult.get("errorMsg");
	               TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
	               Assert.fail("Test failed: " + errorMsg);
	            } else {
	                String labelText = Verifyresult.get("labelText");
	                System.out.println(labelText);
	                TestResultLogger.log(logFile,labelText);
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

