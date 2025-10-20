package Utilities;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.openqa.selenium.WebDriver;

public class WindowHandle2 {

    private static final User32 USER32 = User32.INSTANCE;

    public static void handleExcelPopups(WebDriver driver, String reportName) {
        try {
            boolean brought = bringWindowToFrontByTitlePart("excel", 6);
            System.out.println("bringWindowToFront result: " + brought);

            Thread.sleep(600);

            boolean fmtHandled = attemptFormatMismatchHandling(2);
            System.out.println("formatMismatch handled: " + fmtHandled);

            Thread.sleep(600);

            boolean secHandled = attemptSecurityPopupHandling(4);
            System.out.println("security popup handled: " + secHandled);

            Thread.sleep(800);

        } catch (Exception e) {
            System.err.println("❌ Error in handleExcelPopupsAndClose: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean bringWindowToFrontByTitlePart(String titlePart, int timeoutSeconds) {
        String part = titlePart == null ? "" : titlePart.toLowerCase();
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < end) {
            HWND hwnd = findMainExcelWindow(part);
            if (hwnd != null) {
                try {
                    USER32.ShowWindow(hwnd, WinUser.SW_RESTORE);
                    USER32.SetForegroundWindow(hwnd);
                    USER32.SetFocus(hwnd);
                    return true;
                } catch (Exception e) {
                    System.out.println("⚠ bringWindowToFront: SetForegroundWindow failed: " + e.getMessage());
                }
            }
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
        return false;
    }

    public static HWND findMainExcelWindow(String titlePartLower) {
        final List<HWND> result = new ArrayList<>();
        USER32.EnumWindows((hwnd, data) -> {
            if (!USER32.IsWindowVisible(hwnd)) return true;

            char[] buffer = new char[512];
            USER32.GetWindowText(hwnd, buffer, 512);
            String wText = Native.toString(buffer).trim();

            if (wText.length() > 0 && wText.toLowerCase().contains(titlePartLower)) {
                String lowerTitle = wText.toLowerCase();
                if (lowerTitle.endsWith(".xls") || lowerTitle.endsWith(".xlsx")) {
                    result.add(hwnd);
                    return false;
                }
                result.add(hwnd);
                return false;
            }
            return true;
        }, Pointer.NULL);

        return result.isEmpty() ? null : result.get(0);
    }

    public static boolean attemptFormatMismatchHandling(int maxAttempts) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(200);
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                System.out.println("Attempt formatMismatch: " + attempt);
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_Y);
                robot.keyRelease(KeyEvent.VK_Y);
                robot.keyRelease(KeyEvent.VK_ALT);
                Thread.sleep(500);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(700);
            }
            return true;
        } catch (Exception e) {
            System.out.println("⚠ attemptFormatMismatchHandling exception: " + e.getMessage());
            return false;
        }
    }

    public static boolean attemptSecurityPopupHandling(int maxAttempts) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(200);
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                System.out.println("Attempt securityPopup: " + attempt);
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_ALT);
                Thread.sleep(600);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                Thread.sleep(200);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(600);
            }
            return true;
        } catch (Exception e) {
            System.out.println("⚠ attemptSecurityPopupHandling exception: " + e.getMessage());
            return false;
        }
    }

    public static void ensureExcelClosed() {
        try {
            Process p = new ProcessBuilder("taskkill", "/IM", "EXCEL.EXE", "/F").start();
            p.waitFor();
            Thread.sleep(800);
        } catch (Exception e) {
            System.out.println("⚠ ensureExcelClosed: " + e.getMessage());
        }
    }

    public static boolean waitForExcelToOpen(int timeoutSeconds) {
        for (int i = 0; i < timeoutSeconds; i++) {
            if (isExcelRunning()) {
                return true;
            }
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        return false;
    }

    public static boolean isExcelRunning() {
        try {
            HWND hwnd = User32.INSTANCE.FindWindow("XLMAIN", null);
            User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_MAXIMIZE);
            User32.INSTANCE.SetForegroundWindow(hwnd);
            Thread.sleep(1000);
            return ProcessHandle.allProcesses()
                    .anyMatch(p -> p.info().command().isPresent() &&
                            p.info().command().get().toLowerCase().contains("excel"));
        } catch (Exception e) {
            return false;
        }
    }

    /** Capture Excel screenshot (no comment handling) */
    public static File captureExcelScreenshot(String reportName) {
        File screenshotFile = null;
        try {
            HWND hwnd = findMainExcelWindow("excel");
            if (hwnd == null) {
                System.out.println("⚠ Excel window not found");
                return null;
            }

            USER32.ShowWindow(hwnd, WinUser.SW_RESTORE);
            USER32.SetForegroundWindow(hwnd);
            Thread.sleep(800);

            Robot robot = new Robot();
            robot.setAutoDelay(150);

            // Just dismiss any open popups
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            Thread.sleep(200);

            com.sun.jna.platform.win32.WinDef.RECT rect = new com.sun.jna.platform.win32.WinDef.RECT();
            USER32.GetWindowRect(hwnd, rect);
            Rectangle excelRect = new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);

            BufferedImage excelImage = robot.createScreenCapture(excelRect);
            screenshotFile = File.createTempFile("ExcelShot_" + reportName, ".png");
            ImageIO.write(excelImage, "png", screenshotFile);

            System.out.println("✅ Excel screenshot captured: " + screenshotFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("❌ Failed to capture Excel screenshot: " + e.getMessage());
            e.printStackTrace();
        }
        return screenshotFile;
    }

    public static boolean waitForExcelPopupToDisappear(int timeoutSeconds) {
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < end) {
            if (!isExcelPopupVisible()) {
                return true;
            }
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
        return false;
    }

    public static boolean isExcelPopupVisible() {
        final boolean[] popupFound = {false};
        USER32.EnumWindows((hwnd, data) -> {
            if (!USER32.IsWindowVisible(hwnd)) return true;
            char[] buffer = new char[512];
            USER32.GetWindowText(hwnd, buffer, 512);
            String wText = Native.toString(buffer).trim().toLowerCase();
            if (wText.contains("security") || wText.contains("download") || wText.contains("open")) {
                popupFound[0] = true;
                return false;
            }
            return true;
        }, Pointer.NULL);
        return popupFound[0];
    }
}  