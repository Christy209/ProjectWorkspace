package com.BBSSL.Utilities;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.testng.Assert;

public class Validation {

    public static void validateData(Map<String, String> expected, Map<String, String> actual) {
        System.out.println("\n🔍 Validating Excel vs. Application Data...\n");
        int matchedCount = 0;
        int mismatchCount = 0;
        int missingCount = 0;
        int extraCount = 0;
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = actual.get(key);

            // Skip empty expected values
            if (expectedValue == null || expectedValue.trim().isEmpty()) {
                //System.out.printf("⏭️ Skipping validation for key '%s' (Excel empty)%n", key);
                continue;
            }

            // Handle missing actual value
            if (actualValue == null || actualValue.trim().isEmpty() || actualValue.equalsIgnoreCase("Not Found")) {
                missingCount++;
                //System.out.printf("⚠️ Missing actual value for key '%s'%n", key);
                continue;
            }

            // Normalize both values
            String normExpected = normalize(expectedValue);
            String normActual = normalize(actualValue);

            // Handle date comparison
            if (isDateFormat(normExpected) && isDateFormat(normActual)) {
                normExpected = formatDateToStandard(normExpected);
                normActual = formatDateToStandard(normActual);
            }

            // Handle numeric comparison
            if (isNumeric(normExpected) && isNumeric(normActual)) {
                BigDecimal excelNum = new BigDecimal(normExpected).stripTrailingZeros();
                BigDecimal appNum = new BigDecimal(normActual).stripTrailingZeros();
                try {
                    Assert.assertEquals(excelNum, appNum, "Mismatch on: " + key);
                    matchedCount++;
                } catch (AssertionError e) {
                    mismatchCount++;
                    System.out.printf("❌ Mismatch at key '%s': Expected='%s', Actual='%s'%n", key, excelNum, appNum);
                }
            } else { // String comparison
                try {
                    Assert.assertEquals(normExpected, normActual, "Mismatch on: " + key);
                    matchedCount++;
                } catch (AssertionError e) {
                    mismatchCount++;
                    System.out.printf("❌ Mismatch at key '%s': Expected='%s', Actual='%s'%n", key, expectedValue, actualValue);
                }
            }
        }

        // Handle extra keys in actual
        for (String key : actual.keySet()) {
            if (!expected.containsKey(key)) {
                extraCount++;
                //System.out.printf("⚠️ Extra key in actual data not in expected data: '%s'%n", key);
            }
        }

        System.out.println("--------------------------");
        System.out.printf("✅ Matched Count      : %d%n", matchedCount);
        System.out.printf("❌ Mismatched Count   : %d%n", mismatchCount);
        System.out.printf("⚠️ Missing Count      : %d%n", missingCount);
        System.out.printf("⚠️ Extra Key Count    : %d%n", extraCount);
        System.out.printf("🔢 Total Expected Keys: %d%n", expected.size());
        
        Assert.assertEquals(mismatchCount, 0, "Test failed due to mismatched values.");
    }

    private static String normalize(String value) {
        if (value == null || value.trim().isEmpty()) return "";
        value = value.trim().replaceAll("\\.0+$", ""); // Remove trailing .0
        if (value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}")) {
            return value.replace("-", "/"); // Normalize date separators
        }
        return value.toLowerCase();
    }

    private static boolean isNumeric(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDateFormat(String value) {
        return value != null && (
            value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}") ||     // dd-MM-yyyy or dd/MM/yyyy
            value.matches("\\d{2}-[A-Za-z]{3}-\\d{4}")         // dd-MMM-yyyy
        );
    }

    public static String formatDateToStandard(String dateStr) {
        List<SimpleDateFormat> inputFormats = Arrays.asList(
            new SimpleDateFormat("dd/MM/yyyy"),
            new SimpleDateFormat("dd-MM-yyyy"),
            new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)  // for "15-Mar-2023"
        );

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        for (SimpleDateFormat inputFormat : inputFormats) {
            try {
                Date date = inputFormat.parse(dateStr);
                return outputFormat.format(date);
            } catch (ParseException ignored) {
            }
        }

        return dateStr; // return original if parsing fails
    }
    // ✅ Retrieves the latest text file with the given prefix
    public static File getLatestFile(String directoryPath, String filePrefix) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith(filePrefix) && name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            return null; // No matching files found
        }

        // Sort files by last modified time (newest first)
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return files[0];
    }
}
