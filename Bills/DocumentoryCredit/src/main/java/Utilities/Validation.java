package Utilities;

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

        Set<String> matchedFields = new HashSet<>();
        Set<String> missingFields = new HashSet<>();
        Set<String> mismatchedFields = new HashSet<>();
        Set<String> extraFields = new HashSet<>();

        // 1️⃣ Compare expected vs actual
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = actual.get(key);

            // Skip empty Excel value
            if (expectedValue == null || expectedValue.trim().isEmpty()) {
                continue;
            }

            // Missing value in UI
            if (actualValue == null || actualValue.trim().isEmpty() || actualValue.equalsIgnoreCase("Not Found")) {
                missingCount++;
                missingFields.add(key);
                continue;
            }

            // Normalize values
            String normExpected = normalize(expectedValue);
            String normActual = normalize(actualValue);

            // Date formatting
            if (isDateFormat(normExpected) && isDateFormat(normActual)) {
                normExpected = formatDateToStandard(normExpected);
                normActual = formatDateToStandard(normActual);
            }

            // Numeric comparison
            if (isNumeric(normExpected.replace(",", "")) && isNumeric(normActual.replace(",", ""))) {
                try {
                    BigDecimal excelNum = new BigDecimal(normExpected.replace(",", "")).stripTrailingZeros();
                    BigDecimal appNum = new BigDecimal(normActual.replace(",", "")).stripTrailingZeros();

                    if (excelNum.compareTo(appNum) == 0) {
                        matchedCount++;
                        matchedFields.add(key);
                    } else {
                        mismatchCount++;
                        mismatchedFields.add(key);
                        System.out.printf("❌ Mismatch at key '%s': Expected='%s', Actual='%s'%n", key, excelNum, appNum);
                    }
                } catch (NumberFormatException e) {
                    mismatchCount++;
                    mismatchedFields.add(key);
                    System.out.printf("⚠️ Invalid numeric format at key '%s': Expected='%s', Actual='%s'%n", key, normExpected, normActual);
                }
            } else {
                // String comparison
                if (normExpected.equals(normActual)) {
                    matchedCount++;
                    matchedFields.add(key);
                } else {
                    mismatchCount++;
                    mismatchedFields.add(key);
                    System.out.printf("❌ Mismatch at key '%s': Expected='%s', Actual='%s'%n", key, expectedValue, actualValue);
                }
            }
        }

        // 2️⃣ Check extra keys in actual data
        for (String key : actual.keySet()) {
            if (!expected.containsKey(key)) {
                extraCount++;
                extraFields.add(key);
            }
        }

        // 3️⃣ Print summary
        System.out.println("--------------------------");
        System.out.printf("🔢 Total Excel Fields  : %d%n", expected.size());
        System.out.printf("✅ Matched Fields      : %d%n", matchedCount);
        System.out.printf("❌ Mismatched Fields   : %d%n", mismatchCount);
        System.out.printf("⚠️ Missing Fields      : %d%n", missingCount);
        System.out.printf("⚠️ Extra Fields in UI  : %d%n", extraCount);

        if (!matchedFields.isEmpty()) {
            System.out.println("✅ Matched Keys: " + matchedFields);
        }
        if (!mismatchedFields.isEmpty()) {
            System.out.println("❌ Mismatched Keys: " + mismatchedFields);
        }
        if (!missingFields.isEmpty()) {
            System.out.println("⚠️ Missing Keys: " + missingFields);
        }
        if (!extraFields.isEmpty()) {
            System.out.println("⚠️ Extra Keys: " + extraFields);
        }

        if (mismatchCount == 0) {
            System.out.println("🎉 All data matched successfully!");
        }

        Assert.assertEquals(mismatchCount, 0, "Test failed due to mismatched values.");
    }

    private static String normalize(String value) {
        if (value == null || value.trim().isEmpty()) return "";
        value = value.trim().replaceAll("\\.0+$", "");
        if (value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}")) {
            return value.replace("-", "/");
        }
        return value.toLowerCase();
    }

    private static boolean isNumeric(String str) {
        try {
            new BigDecimal(str.replace(",", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDateFormat(String value) {
        return value != null && (
            value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}") ||     
            value.matches("\\d{2}-[A-Za-z]{3}-\\d{4}")         
        );
    }

    public static String formatDateToStandard(String dateStr) {
        List<SimpleDateFormat> inputFormats = Arrays.asList(
            new SimpleDateFormat("dd/MM/yyyy"),
            new SimpleDateFormat("dd-MM-yyyy"),
            new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        );
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (SimpleDateFormat inputFormat : inputFormats) {
            try {
                Date date = inputFormat.parse(dateStr);
                return outputFormat.format(date);
            } catch (ParseException ignored) {}
        }
        return dateStr;
    }

    public static File getLatestFile(String directoryPath, String filePrefix) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith(filePrefix) && name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            return null;
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return files[0];
    }
}
