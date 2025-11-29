package PaymentTestCases;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
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

    // 🔹 Combined test log
    private StringBuilder testLog = new StringBuilder();
    private int totalTests = 0, passedTests = 0, failedTests = 0, skippedTests = 0;

    private void logStep(String msg) {
        String time = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] ";
        testLog.append(time).append(msg).append("\n");
    }

    @BeforeClass
    public void setup() throws Exception {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            ExcelUtils.loadExcel(EXCEL_PATH);
            logStep("Excel loaded successfully: " + EXCEL_PATH);
        } catch (Exception e) {
            logStep("❌ Failed to load Excel: " + e.getMessage());
            Assert.fail("Cannot load Excel: " + EXCEL_PATH);
        }
    }

    @Test
    public void runTest() throws Exception {

        Login login = new Login();
        LocalDateTime startTime = LocalDateTime.now();

        try {
            // Load Excel rows
            RowData initialData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            RowData addData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);
            RowData verifyData = ExcelUtils.getRowAsRowData(SHEET_NAME, 2);

            // ---------------- Step 1: Login as Maker ----------------
            totalTests++;
            login.First();
            logStep("Login as Maker successful");
            passedTests++;

            OutwardGuaranteeMaintenance guarantee = new OutwardGuaranteeMaintenance(driver);

            // ---------------- Step 2: Add Guarantee ----------------
            totalTests++;
            Map<String, String> addResult = guarantee.executeWithResultMap(addData, initialData, SHEET_NAME, 1, EXCEL_PATH);
            String guaranteeNo = addResult.get("GuaranteeNo");
            logStep("Guarantee added - " + guaranteeNo);
            assertGuaranteeCaptured(guaranteeNo, "Add");
            passedTests++;

            // ---------------- Step 3: Logout Maker ----------------
            totalTests++;
            LogOut.performLogout(driver, wait);
            logStep("Maker logout successful");
            passedTests++;

            // ---------------- Step 4: Login as Checker ----------------
            totalTests++;
            login.Second();
            logStep("Login as Checker successful");
            passedTests++;

            // Reload Excel to refresh any dynamic data
            ExcelUtils.loadExcel(EXCEL_PATH);
            addData = ExcelUtils.getRowAsRowData(SHEET_NAME, 1);

            // ---------------- Step 5: Verify Guarantee ----------------
            totalTests++;
            Map<String, String> verifyResult = guarantee.executeWithResultMap(verifyData, addData, SHEET_NAME, 2, EXCEL_PATH);
            String verifyGuaranteeNo = verifyResult.get("GuaranteeNo");
            logStep("Guarantee verified - " + verifyGuaranteeNo);
            assertGuaranteeCaptured(verifyGuaranteeNo, "Verify");
            passedTests++;

        } catch (Exception e) {
            failedTests++;
            logStep("❌ Exception Occurred: " + e.getMessage());
            Assert.fail(e.getMessage());

        } finally {

            try {
                LogOut.performLogout(driver, wait);
            } catch (Exception e) {
                logStep("⚠ Logout failed at end: " + e.getMessage());
            }

            // 🔹 Save styled HTML document and attach to Allure
            File docFile = saveDocumentViewCopy(startTime, LocalDateTime.now());

            // 🔹 Make clickable link in log
            if (docFile != null) {
                String fileUrl = docFile.toURI().toString();
                logStep("📄 Document saved and attached: <a href='" + fileUrl + "' target='_blank'>Open Document</a>");
                System.out.println("📄 Document clickable link: " + fileUrl);
            }

            // 🔹 Attach plain log as well
            Allure.addAttachment(
                "TC04 Consolidated Log",
                "text/plain",
                testLog.toString()
            );
        }
    }

    private void assertGuaranteeCaptured(String guaranteeNo, String phase) {
        if (guaranteeNo == null || guaranteeNo.trim().isEmpty()) {
            String errorMsg = "❌ Guarantee No not captured during " + phase;
            logStep(errorMsg);
            failedTests++;
            Assert.fail(errorMsg);
        }
    }

    // ------------------------------------------------------------------
    // 🔹 Save all logs as single HTML test-case card for Allure
    // ------------------------------------------------------------------
    private File saveDocumentViewCopy(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String basePath = System.getProperty("user.dir") + "/allure-results/document-view";

            File folder = new File(basePath);
            folder.mkdirs();

            File outputFile = new File(folder, "TC04_Document_" + timestamp + ".html");

            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
                writer.write("<title>TC04 Document View</title>");
                writer.write("<style>");
                writer.write("body { font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px; }");
                writer.write(".test-case { background:#fff; border-radius:8px; padding:20px; margin-bottom:15px; box-shadow:0 2px 5px rgba(0,0,0,0.1);} ");
                writer.write(".test-case h2 { margin:0 0 10px 0; font-size:20px; color:#333;} ");
                writer.write(".log { font-family: monospace; background:#f0f0f0; padding:10px; border-radius:5px; white-space: pre-wrap;} ");
                writer.write(".passed { color: green; font-weight:bold; font-size:16px;} ");
                writer.write(".failed { color: red; font-weight:bold; font-size:16px;} ");
                writer.write(".summary { background:#d9edf7; padding:10px; border-radius:5px; margin-top:20px; font-weight:bold;} ");
                writer.write("</style></head><body>");

                writer.write("<div class='test-case'>");
                writer.write("<h2>📄 Test Case: TC04_IssuenewguaranteewithexistingCIFID</h2>");

                if (failedTests == 0) {
                    writer.write("<p class='passed'>✅ PASSED</p>");
                } else {
                    writer.write("<p class='failed'>❌ FAILED</p>");
                }

                writer.write("<div class='log'>" + testLog.toString().trim().replaceAll("\n", "<br>") + "</div>");
                writer.write("</div>");

                Duration duration = Duration.between(startTime, endTime);
                long mins = duration.toMinutes();
                long secs = duration.minusMinutes(mins).getSeconds();

                writer.write("<div class='summary'>");
                writer.write("📊 Suite Summary: TC04_IssuenewguaranteewithexistingCIFID<br>");
                writer.write("Total tests run: " + totalTests + ", Passes: " + passedTests + ", Failures: " + failedTests + ", Skips: " + skippedTests + "<br>");
                writer.write("⏱️ Total Duration: " + mins + " min " + secs + " sec");
                writer.write("</div>");

                writer.write("</body></html>");
            }

            // Attach HTML to Allure
            try (InputStream is = Files.newInputStream(outputFile.toPath())) {
                Allure.addAttachment("TC04 Document View", "text/html", is, ".html");
            }

            return outputFile;

        } catch (Exception e) {
            logStep("❌ Document saving failed: " + e.getMessage());
            return null;
        }
    }
}
