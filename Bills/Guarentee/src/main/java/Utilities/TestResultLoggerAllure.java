package Utilities;

import io.qameta.allure.Allure;
import java.io.FileInputStream;

public class TestResultLoggerAllure {

    // Attach a single log line dynamically to Allure under "Documents"
    public static void attachLogLine(String logLine) {
        try {
            Allure.addAttachment("Documents", "text/plain", logLine, ".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Attach an entire existing log file to Allure under "Documents"
    public static void attachLogFile(String logFilePath) {
        try (FileInputStream fis = new FileInputStream(logFilePath)) {
            Allure.addAttachment("Documents", "text/plain", fis, ".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
