package TestCases;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.ITestResult;

import io.qameta.allure.Allure;

import Base.DriverManager;
import Payments.ImportAndInwardBills;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.TestResultLogger;

public class TC01_LodgeTheBillUsingImportandInwardMenu {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/resources/MIIB.xlsx";
    private static final String SHEET_NAME = "TC01";

    private String logFile;   // 🔥 Make logFile accessible to @AfterMethod

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }

    @Test
    public void runTest() throws Exception {

        logFile = TestResultLogger.createLogFile("TC01_LodgeTheBillUsingImportandInwardMenu");
        Login login = new Login();

        try {

            RowData fwdcno = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            RowData VerifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2);

            login.First();
            ImportAndInwardBills FrwConNo = new ImportAndInwardBills(driver);

            // Step 1 — Lodge
            Map<String, String> result = FrwConNo.execute(fwdcno, fwdcno, SHEET_NAME, 1, EXCEL_PATH);

            if (result.containsKey("errorMsg")) {
                String errorMsg = result.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
                Assert.fail(errorMsg);
            } else {
                TestResultLogger.log(logFile, result.get("labelText"));
            }

            LogOut.performLogout(driver, wait);
            login.Second();

            ExcelUtils.loadExcel(EXCEL_PATH);
            fwdcno = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

            // Step 2 — Verify
            Map<String, String> verifyResult = FrwConNo.execute(VerifyData, fwdcno, SHEET_NAME, 2, EXCEL_PATH);

            if (verifyResult.containsKey("errorMsg")) {
                String errorMsg = verifyResult.get("errorMsg");
                TestResultLogger.log(logFile, "❌ Error: " + errorMsg);
                Assert.fail(errorMsg);
            } else {
                TestResultLogger.log(logFile, verifyResult.get("labelText"));
            }

        } catch (Exception e) {
            TestResultLogger.log(logFile, "❌ Exception: " + e.getMessage());
            Assert.fail(e.getMessage());

        } finally {
            try {
                LogOut.performLogout(driver, wait);
            } catch (Exception ignored) {}
        }
    }

    // -------- Attach TXT file to Allure --------
    @AfterMethod
    public void attachTxtToAllure(ITestResult result) {

        try {
            File file = new File(logFile);
            if (file.exists()) {
                Allure.addAttachment("📄 Execution Log (TXT)", "text/plain", new FileInputStream(file), ".txt");
            }
        } catch (Exception e) {
            System.err.println("Failed to attach TXT log to Allure: " + e.getMessage());
        }
    }
}
