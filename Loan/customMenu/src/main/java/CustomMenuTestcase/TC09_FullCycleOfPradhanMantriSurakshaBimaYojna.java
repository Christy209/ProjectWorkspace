package CustomMenuTestcase;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Base.DriverManager;
import CustomMenu.PMSBY;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;

public class TC09_FullCycleOfPradhanMantriSurakshaBimaYojna {

	  private WebDriver driver;
	    private WebDriverWait wait;
	    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/CustomData.xlsx";
	    private static final String SHEET = "TC01";

	    @BeforeClass
	    public void setup() {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // increased timeout
	    }

	    @Test
	    public void runTest() throws Throwable {
	        ExcelUtils.loadExcel(EXCEL_PATH);
	        Login login = new Login();

	        // 1) Add
	        login.First();
	        executeStep("Add", 1);

	        // 2) Cancel
	        executeStep("Cancel", 2);

	        // 3) Add again
	        executeStep("AddAgain", 1);  // reusing row 1

	        // 4) Verify
	        LogOut.performLogout(driver, wait);
	        login.Second();
	        executeStep("Verify", 3);

	        // 5) Modify
	        executeStep("Modify", 4);

	        // 6) Inquire after modify
	        executeStep("InquireMV", 5);

	        // 7) Delete
	        executeStep("Delete", 6);

	        // 8) Final Inquire
	        executeStep("Inquire", 7);

	    }
	    /**
	     * Helper to run PMSBY test steps, logging both pass/fail and ensuring
	     * that logout is performed per-step on failure.
	     */
	    private void executeStep(String action, int row) throws Throwable {
	        List<String> data = ExcelUtils.getRowAsList(SHEET, row);
	        try {
	            new PMSBY(driver).execute(data, EXCEL_PATH, SHEET, row);
	            Reporter.log(action + "' passed", true);
	        } catch (Throwable t) {
	            Reporter.log(action + "' failed : " + t.getMessage(), true);
	            LogOut.performLogout(driver, wait);
	            throw t;
	        }
	    }

	    @AfterMethod(alwaysRun = true)
	    public void afterMethod(ITestResult result) {
	        String status = switch (result.getStatus()) {
	            case ITestResult.SUCCESS -> "PASS";
	            case ITestResult.FAILURE -> "FAIL";
	            case ITestResult.SKIP    -> "SKIP";
	            default                  -> "UNKNOWN";
	        };
	        Reporter.log("Test '" + result.getMethod().getMethodName() + "' completed with status: " + status, true);
	    }

	    @AfterClass(alwaysRun = true)
	    public void teardown() {
	        try {
	            LogOut.performLogout(driver, wait);
	            Reporter.log("✅ Final logout completed", true);
	        } catch (Exception e) {
	            Reporter.log("⚠ Final logout failed: " + e.getMessage(), true);
	        } finally {
	            if (driver != null) {
	                driver.quit();
	                Reporter.log("✅ Browser session terminated", true);
	            }
	        }
	    }
}
