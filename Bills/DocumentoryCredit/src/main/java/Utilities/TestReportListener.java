package Utilities;

import org.testng.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.poi.xwpf.usermodel.*;
import java.nio.file.*;

public class TestReportListener implements ITestListener {

    private XWPFDocument document;
    private String outputFolder = "test-output/TestReports";
    private String individualResultFolder = "TestResults";
    private String latestCopyFolder = "test-output/LatestTxtCopy";  // ✅ new folder for latest txt copies
    private String fileName;
    private int passedCount = 0;
    private int failedCount = 0;
    private int skippedCount = 0;
    private long suiteStartTime;
    private int totalTestCount = 0;

    @Override
    public void onStart(ITestContext context) {
        try {
            new File(outputFolder).mkdirs();
            new File(latestCopyFolder).mkdirs(); // ✅ create new folder
            suiteStartTime = System.currentTimeMillis();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            fileName = outputFolder + "/Functionality_TestResult_" + timestamp + ".docx";
            document = new XWPFDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        totalTestCount++;
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        addIndividualTestResult(result, true);
        passedCount++;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        addIndividualTestResult(result, false);
        failedCount++;
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        XWPFParagraph p = document.createParagraph();
        XWPFRun r = p.createRun();
        r.setText("⚠️ SKIPPED: " + result.getMethod().getMethodName());
        r.setFontSize(11);
        r.setColor("FFA500"); // Orange for skipped
        skippedCount++;
        totalTestCount++;
    }

//    private void addIndividualTestResult(ITestResult result, boolean passed) {
//        try {
//            String testCaseName = result.getTestClass().getRealClass().getSimpleName();
//            String methodName = result.getMethod().getMethodName();
//
//            // ✅ Copy the latest txt file for this test case name
//            copyLatestTxtFileForTest(testCaseName);
//
//            // Header: Test case
//            XWPFParagraph header = document.createParagraph();
//            XWPFRun title = header.createRun();
//            title.setText("📄 Test Case: " + testCaseName);
//            title.setBold(true);
//            title.setFontSize(12);
//
//            // ✅/❌ Status immediately after header
//            XWPFParagraph statusPara = document.createParagraph();
//            XWPFRun statusRun = statusPara.createRun();
//            statusRun.setFontSize(11);
//            statusRun.setBold(true);
//            if (passed) {
//                statusRun.setText("✅ PASSED:");
//                statusRun.setColor("008000");
//            } else {
//                statusRun.setText("❌ FAILED:");
//                statusRun.setColor("FF0000");
//            }
//
//            // Search for corresponding txt file
//            File folder = new File(individualResultFolder);
//            File[] files = folder.listFiles((dir, name) ->
//                    name.toLowerCase().contains(testCaseName.toLowerCase()) ||
//                    name.toLowerCase().contains(methodName.toLowerCase()));
//
//            boolean txtFound = false;
//
//            if (files != null && files.length > 0) {
//                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
//                File latestFile = files[0];
//                txtFound = true;
//
//                try (BufferedReader reader = new BufferedReader(new FileReader(latestFile))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        if (line.trim().isEmpty()) continue;
//
//                        XWPFParagraph para = document.createParagraph();
//                        XWPFRun run = para.createRun();
//                        run.setFontSize(10);
//
//                        // Color coding
//                        if (line.contains("❌")) {
//                            run.setColor("FF0000");
//                            run.setBold(true);
//                        } else if (line.contains("✅")) {
//                            run.setColor("008000");
//                            run.setBold(true);
//                        } else {
//                            run.setColor("000000"); // black for normal messages
//                        }
//
//                        run.setText(line);
//                    }
//                }
//            }
//
//            // If txt not found, fallback
//            if (!txtFound) {
//                XWPFParagraph fallback = document.createParagraph();
//                XWPFRun r = fallback.createRun();
//                r.setFontSize(10);
//                r.setColor("808080"); // gray
//                r.setText("[No detailed logs found]");
//            }
//
//            // If failed, add reason at the end
//            if (!passed && result.getThrowable() != null) {
//                XWPFParagraph reasonPara = document.createParagraph();
//                XWPFRun reasonRun = reasonPara.createRun();
//                reasonRun.setFontSize(10);
//                reasonRun.setColor("FF0000");
//                reasonRun.setBold(true);
//                reasonRun.setText("↳ Reason: ❌ " + result.getThrowable().getMessage());
//            }
//
//            // Blank line after each test
//            document.createParagraph().createRun().addBreak();
//
//        } catch (Exception e) {
//            XWPFParagraph errorPara = document.createParagraph();
//            XWPFRun r = errorPara.createRun();
//            r.setText("❌ Error writing result for " + result.getName() + ": " + e.getMessage());
//            r.setColor("FF0000");
//            r.setFontSize(10);
//        }
//    }
    private void addIndividualTestResult(ITestResult result, boolean passed) {
        try {
            String testCaseName = result.getTestClass().getRealClass().getSimpleName();

            // 🔹 Header
            XWPFParagraph header = document.createParagraph();
            XWPFRun title = header.createRun();
            title.setText("📄 Test Case: " + testCaseName);
            title.setBold(true);
            title.setFontSize(12);

            // 🔹 Determine status
            XWPFParagraph statusPara = document.createParagraph();
            XWPFRun statusRun = statusPara.createRun();
            statusRun.setFontSize(11);
            statusRun.setBold(true);

            Throwable throwable = result.getThrowable();
            boolean isSkippedAfterSuccess = (throwable instanceof SkipException);

            if (isSkippedAfterSuccess) {
                statusRun.setText("✅ SKIPPED AFTER SUCCESS:");
                statusRun.setColor("008000");
            } else if (passed) {
                statusRun.setText("✅ PASSED:");
                statusRun.setColor("008000");
            } else {
                statusRun.setText("❌ FAILED:");
                statusRun.setColor("FF0000");
            }

            // 🔹 Get the latest .txt log file for this test
            File folder = new File(individualResultFolder);
            File[] txtFiles = folder.listFiles((dir, name) ->
                    name.toLowerCase().startsWith(testCaseName.toLowerCase()) && name.endsWith(".txt"));

            if (txtFiles != null && txtFiles.length > 0) {
                Arrays.sort(txtFiles, Comparator.comparingLong(File::lastModified).reversed());
                File latestTxt = txtFiles[0]; // most recent log file

                try (BufferedReader br = new BufferedReader(new FileReader(latestTxt))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;

                        XWPFParagraph para = document.createParagraph();
                        XWPFRun run = para.createRun();
                        run.setFontSize(10);

                        // 🎨 Color based on content
                        if (line.contains("❌") || line.toLowerCase().contains("error") ||
                            line.toLowerCase().contains("failed") || line.toLowerCase().contains("exception")) {
                            run.setColor("FF0000");
                            run.setBold(true);
                        } else if (line.contains("✅") || line.toLowerCase().contains("success")) {
                            run.setColor("008000");
                            run.setBold(true);
                        } else {
                            run.setColor("000000");
                        }

                        run.setText(line);
                    }
                }
            } else {
                // No txt found
                XWPFParagraph para = document.createParagraph();
                XWPFRun run = para.createRun();
                run.setFontSize(10);
                run.setColor("808080");
                run.setText("[No log file found for " + testCaseName + "]");
            }

            // 🔹 Add failure / skip reason
            if (throwable != null) {
                XWPFParagraph reasonPara = document.createParagraph();
                XWPFRun reasonRun = reasonPara.createRun();
                reasonRun.setFontSize(10);

                if (isSkippedAfterSuccess) {
                    reasonRun.setColor("008000");
                    reasonRun.setText("↳ Note: ✅ " + throwable.getMessage());
                } else {
                    reasonRun.setColor("FF0000");
                    reasonRun.setBold(true);
                    reasonRun.setText("↳ Reason: ❌ " + throwable.getMessage());
                }
            }

            // Blank line after each test
            document.createParagraph().createRun().addBreak();

        } catch (Exception e) {
            XWPFParagraph errorPara = document.createParagraph();
            XWPFRun r = errorPara.createRun();
            r.setText("❌ Error writing result for " + result.getName() + ": " + e.getMessage());
            r.setColor("FF0000");
            r.setFontSize(10);
        }
    }

    // ✅ Utility method: Copy the latest .txt file for the current test case
    private void copyLatestTxtFileForTest(String testCaseName) {
        try {
            File folder = new File(individualResultFolder);
            File[] files = folder.listFiles((dir, name) ->
                    name.toLowerCase().contains(testCaseName.toLowerCase()) && name.endsWith(".txt"));

            if (files != null && files.length > 0) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                File latestFile = files[0];

                Path source = latestFile.toPath();
                Path destination = Paths.get(latestCopyFolder, latestFile.getName());

                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("📁 Copied latest txt for " + testCaseName + ": " + latestFile.getName());
            } else {
                System.out.println("⚠️ No matching .txt file found for " + testCaseName);
            }
        } catch (Exception e) {
            System.out.println("❌ Error copying latest txt for " + testCaseName + ": " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            long suiteEndTime = System.currentTimeMillis();
            long totalDurationMs = suiteEndTime - suiteStartTime;
            long minutes = (totalDurationMs / 1000) / 60;
            long seconds = (totalDurationMs / 1000) % 60;

            // Separator before summary
            XWPFParagraph separator1 = document.createParagraph();
            XWPFRun sepRun1 = separator1.createRun();
            sepRun1.setText("============================================================");

            // Suite Summary title
            XWPFParagraph summaryHeader = document.createParagraph();
            XWPFRun headerRun = summaryHeader.createRun();
            headerRun.setText("📊 Suite Summary: " + context.getName());
            headerRun.setBold(true);
            headerRun.addCarriageReturn();

            // TestNG-style summary block
            XWPFParagraph summaryBlock = document.createParagraph();
            XWPFRun blockRun = summaryBlock.createRun();
            blockRun.setText("===============================================");
            blockRun.addCarriageReturn();
            blockRun.setText(context.getName());
            blockRun.addCarriageReturn();
            blockRun.setText(String.format("Total tests run: %d, Passes: %d, Failures: %d, Skips: %d",
                    totalTestCount, passedCount, failedCount, skippedCount));
            blockRun.addCarriageReturn();
            blockRun.setText("===============================================");
            blockRun.addCarriageReturn();

            // Duration
            XWPFParagraph durationPara = document.createParagraph();
            XWPFRun durRun = durationPara.createRun();
            durRun.setText(String.format("⏱️ Total Duration: %02d min %02d sec", minutes, seconds));

            // Save document
            FileOutputStream out = new FileOutputStream(fileName);
            document.write(out);
            out.close();
            document.close();

            // Keep only 10 latest reports
            maintainLatestFiles(outputFolder, 10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void maintainLatestFiles(String folderPath, int maxFiles) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".docx"));
        if (files != null && files.length > maxFiles) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            for (int i = 0; i < files.length - maxFiles; i++) {
                files[i].delete();
            }
        }
    }

    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onTestFailedWithTimeout(ITestResult result) {}
}
