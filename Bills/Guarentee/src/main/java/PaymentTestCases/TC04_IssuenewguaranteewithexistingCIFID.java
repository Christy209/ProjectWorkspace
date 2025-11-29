package PaymentTestCases;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Base.DriverManager;
import PAYMENTS.OutwardGuaranteeMaintenance;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import io.qameta.allure.Allure;

public class TC04_IssuenewguaranteewithexistingCIFID {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";
    private static final String SHEET_NAME = "TC04";

    // 🔹 A single combined log
    private StringBuilder testLog = new StringBuilder();

    private void logStep(String msg) {
        String time = "[" + LocalDateTime.now() + "] ";
        testLog.append(time).append(msg).append("\n");
    }

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ExcelUtils.loadExcel(EXCEL_PATH);
    }

    @Test
    public void runTest() throws Exception {

        Login login = new Login();

        try {
            RowData initialData = ExcelUtils.getRowAsRowData("TC04", 1);
            RowData addData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            RowData verifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2);

            // ---------------- Step 1: Login as Maker ----------------
            login.First();
            logStep("Login as Maker successful");

            OutwardGuaranteeMaintenance guarantee = new OutwardGuaranteeMaintenance(driver);

            // ---------------- Step 2: Add Guarantee ----------------
            Map<String, String> addResult = guarantee.executeWithResultMap(addData, initialData, SHEET_NAME, 1, EXCEL_PATH);
            String guaranteeNo = addResult.get("GuaranteeNo");

            logStep("Guarantee added - " + guaranteeNo);
            assertGuaranteeCaptured(guaranteeNo, "Add");

            // ---------------- Step 3: Logout Maker ----------------
            LogOut.performLogout(driver, wait);
            logStep("Maker logout successful");

            // ---------------- Step 4: Login as Checker ----------------
            login.Second();
            logStep("Login as Checker successful");

            ExcelUtils.loadExcel(EXCEL_PATH);
            addData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

            // ---------------- Step 5: Verify Guarantee ----------------
            Map<String, String> verifyResult = guarantee.executeWithResultMap(verifyData, addData, SHEET_NAME, 2, EXCEL_PATH);
            String verifyGuaranteeNo = verifyResult.get("GuaranteeNo");

            logStep("Guarantee verified - " + verifyGuaranteeNo);
            assertGuaranteeCaptured(verifyGuaranteeNo, "Verify");

        } catch (Exception e) {
            logStep("❌ Exception Occurred: " + e.getMessage());
            Assert.fail(e.getMessage());

        } finally {

            try {
                LogOut.performLogout(driver, wait);
            } catch (Exception e) {
                logStep("⚠ Logout failed at end: " + e.getMessage());
            }

            // ----------------------------------------------------------------------
            // OPTION B — CREATE DOCUMENT-VIEW FOLDER + FILE AUTOMATICALLY
            // ----------------------------------------------------------------------
            saveDocumentViewCopy();

            // 🔹 Attach consolidated log inside Allure
            Allure.addAttachment(
                "TC04_IssuenewguaranteewithexistingCIFID",
                "text/plain",
                testLog.toString()
            );
        }
    }

    private void assertGuaranteeCaptured(String guaranteeNo, String phase) {
        if (guaranteeNo == null || guaranteeNo.trim().isEmpty()) {
            String errorMsg = "❌ Guarantee No not captured during " + phase;
            logStep(errorMsg);
            Assert.fail(errorMsg);
        }
    }

    // ------------------------------------------------------------------
    // 🔹 OPTION B: SINGLE CUSTOM DOCUMENT TAB (Document View)
    // ------------------------------------------------------------------
    private void saveDocumentViewCopy() {

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String basePath = System.getProperty("user.dir") + "/Bills/Guarentee/allure-results/document-view";

            File folder = new File(basePath + "/TC04_" + timestamp);
            folder.mkdirs();

            File outputFile = new File(folder, "TC04_Document.txt");

            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(testLog.toString());
            }

            Allure.addAttachment("TC04 Document View", Files.newInputStream(outputFile.toPath()));

            logStep("📄 Document saved in: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            logStep("❌ Document saving failed: " + e.getMessage());
        }
    }
}
