package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestResultLogger {

    private static final String BASE_DIR = System.getProperty("user.dir") + "/TestResults/";
    private static String currentLogFile;

    // Create log file per test case
    public static String createLogFile(String testCaseId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testCaseId + "_" + timestamp + ".txt";
        new File(BASE_DIR).mkdirs();
        return currentLogFile = BASE_DIR + fileName;
    }

    // Log message to the current file
    public static void log(String filePath, String message) {
        if (currentLogFile == null) {
            currentLogFile = filePath;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentLogFile, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing test result: " + e.getMessage());
        }
    }

    // Overloaded method to log directly using currentLogFile
    public static void log(String message) {
        if (currentLogFile == null) {
            System.out.println("❌ Test run not started. Call createLogFile() first.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentLogFile, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing test result: " + e.getMessage());
        }
    }

    public static String getCurrentLogFile() {
        return currentLogFile;
    }
}
