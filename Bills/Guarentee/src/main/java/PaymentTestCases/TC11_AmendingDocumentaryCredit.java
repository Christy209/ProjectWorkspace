package PaymentTestCases;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Base.DriverManager;
import PAYMENTS.InwardDocumentaryCreditsMaintenance;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC11_AmendingDocumentaryCredit {

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
	        String logFile = TestResultLogger.createLogFile("TC11_AmendingDocumentaryCredit");

	        try {
	            String excelPath = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
	            ExcelUtils.loadExcel(excelPath);

	            Login login = new Login();
	            RowData dcno = ExcelUtils.getRowAsRowData("TC10", 1);
	            RowData id = ExcelUtils.getRowAsRowData("TC11", 1);
	            RowData verifyData = ExcelUtils.getRowAsRowData("TC11", 2);

	            login.First();

	            Map<String, String> result = new InwardDocumentaryCreditsMaintenance(driver).execute(id,dcno, "TC11", 1, excelPath);  
	            if (result.containsKey("errorMsg")) {
	                String errorMsg = result.get("errorMsg");
	                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
	                Assert.fail("Test failed: " + errorMsg);
	            } else {
	                String labelText = result.get("labelText");
	                System.out.println(labelText);
	                TestResultLogger.log(logFile,labelText);
	            }           // File           // File

	            LogOut.performLogout(driver, wait);
	            login.Second();
	            ExcelUtils.loadExcel(excelPath);
	            dcno = ExcelUtils.getRowAsRowData("TC10", 1);

	            Map<String, String> Verifyresult  = new InwardDocumentaryCreditsMaintenance(driver).execute(verifyData,dcno, "TC11", 2, excelPath);
	            
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
	                System.out.println("✅ Logout performed successfully at end.");
	            } catch (Exception e) {
	                System.err.println("⚠️ Logout failed at end: " + e.getMessage());
	                TestResultLogger.log(logFile, "⚠️ Logout failed at end: " + e.getMessage());
	            }
	        }
	        }
}
