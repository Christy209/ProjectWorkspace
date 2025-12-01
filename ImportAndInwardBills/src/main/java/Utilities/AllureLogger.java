package utils;

import io.qameta.allure.Allure;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.testng.ITestResult;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AllureLogger {

    private static final String LOG_FOLDER = "logs/";
    private static ThreadLocal<StringBuilder> logCollector = ThreadLocal.withInitial(StringBuilder::new);

    static {
        File dir = new File(LOG_FOLDER);
        if (!dir.exists()) dir.mkdirs();
    }

    // ---------------------------
    // Add a log message
    // ---------------------------
    public static void log(String message) {
        String timestamp = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] ";
        String finalMsg = timestamp + message;

        // 1. Store in ThreadLocal collector
        logCollector.get().append(finalMsg).append("\n");

        // 2. Send to Allure as a step
        Allure.step(finalMsg);

        System.out.println("LOG: " + finalMsg);
    }

    // ---------------------------
    // Save log to TXT file
    // ---------------------------
    private static Path writeTxt(String testName) {
        try {
            Path txtFile = Paths.get(LOG_FOLDER + testName + ".txt");
            Files.write(txtFile, logCollector.get().toString().getBytes());
            return txtFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------
    // Save log to Word (DOCX) file
    // ---------------------------
    private static Path writeDocx(String testName) {
        try {
            Path docFile = Paths.get(LOG_FOLDER + testName + ".docx");

            XWPFDocument doc = new XWPFDocument();
            String[] lines = logCollector.get().toString().split("\n");

            for (String line : lines) {
                XWPFParagraph p = doc.createParagraph();
                p.createRun().setText(line);
            }

            FileOutputStream fos = new FileOutputStream(docFile.toFile());
            doc.write(fos);
            fos.close();
            return docFile;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------
    // Attachments into Allure
    // ---------------------------
    public static void attachLogsToAllure(ITestResult result) {

        String testName = result.getMethod().getMethodName();

        // Create TXT + DOCX
        Path txtFile = writeTxt(testName);
        Path docFile = writeDocx(testName);

        try {
            // TXT
            Allure.addAttachment("📄 Text Log", "text/plain",
                    Files.newInputStream(txtFile), testName + ".txt");

            // DOCX
            Allure.addAttachment("📄 Word Report",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    Files.newInputStream(docFile),
                    testName + ".docx");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------
    // Clear after each test
    // ---------------------------
    public static void clear() {
        logCollector.remove();
    }
}
