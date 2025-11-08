package Utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TestResultlog {

	private static final String ALL_REPORT = System.getProperty("user.dir") + "/TestResults/AllTestReport.txt";

    public static void log(String testCaseName, String message) {
        String formatted = "[" + testCaseName + "] " + message;
        System.out.println(formatted); // Console
        appendToFile(formatted);
    }

    private static void appendToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ALL_REPORT, true))) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearReport() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ALL_REPORT))) {
            writer.write(""); // Clear the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
