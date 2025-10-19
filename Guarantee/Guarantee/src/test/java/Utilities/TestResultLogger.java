package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestResultLogger {

    private static final String BASE_DIR = System.getProperty("user.dir") + "/TestResults/";

    // Create a unique file per test run using TC ID and timestamp
    public static String createLogFile(String testCaseId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testCaseId + "_" + timestamp + ".txt";

        // Ensure directory exists
        new File(BASE_DIR).mkdirs();

        return BASE_DIR + fileName;
    }

    // Log a message to the specified file
    public static void log(String filePath, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing test result: " + e.getMessage());
        }
    }
}
