package Utilities;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.WinDef.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExcelScreenshotUtil {

    public static File captureExcelWindow(String reportName) {
        File file = null;
        try {
            // 1. Find Excel main window
            HWND hwnd = User32.INSTANCE.FindWindow("XLMAIN", null);
            if (hwnd == null) {
                System.err.println("❌ Excel window not found!");
                return null;
            }

            // 2. Maximize Excel and bring to front
            User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_MAXIMIZE);
            User32.INSTANCE.SetForegroundWindow(hwnd);
            Thread.sleep(500); // wait for maximize animation

            // 3. Wait until Excel is actually in foreground
            HWND fg = User32.INSTANCE.GetForegroundWindow();
            int attempts = 0;
            while ((fg == null || !fg.equals(hwnd)) && attempts < 10) {
                Thread.sleep(300);
                User32.INSTANCE.SetForegroundWindow(hwnd);
                fg = User32.INSTANCE.GetForegroundWindow();
                attempts++;
            }
            
            // Let Excel repaint
            Thread.sleep(800);
            
         // 4. Get client area (inside borders, actual sheet area)
            RECT clientRect = new RECT();
            User32.INSTANCE.GetClientRect(hwnd, clientRect);

            POINT topLeft = new POINT();
            topLeft.x = clientRect.left;
            topLeft.y = clientRect.top;
            //User32.INSTANCE.ClientToScreen(hwnd, topLeft);
            User32Extra.INSTANCE.ClientToScreen(hwnd, topLeft);


            int width = clientRect.right - clientRect.left;
            int height = clientRect.bottom - clientRect.top;

            // 5. Capture using BitBlt
            HDC hdcWindow = User32.INSTANCE.GetDC(hwnd);
            HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);
            HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);
            HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);

            GDI32.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height,
                    hdcWindow, topLeft.x, topLeft.y, GDI32.SRCCOPY);
            
            // 3. Get window bounds
            RECT rect = new RECT();
            User32.INSTANCE.GetWindowRect(hwnd, rect);
            int width1 = rect.right - rect.left;
            @SuppressWarnings("unused")
			int height1 = rect.bottom - rect.top;

            // 4. Get device context and memory DC
            HDC hdcWindow1 = User32.INSTANCE.GetDC(hwnd);
            @SuppressWarnings("unused")
			HDC hdcMemDC1 = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow1);
            @SuppressWarnings("unused")
			HBITMAP hBitmap1 = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow1, width1, height);
            @SuppressWarnings("unused")
			HANDLE hOld1 = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);

            // 5. Copy window into memory DC
            GDI32.INSTANCE.BitBlt(hdcMemDC, 0, 0, width1, height,
                    hdcWindow1, 0, 0, GDI32.SRCCOPY);

            // 6. Prepare bitmap info
            WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
            bmi.bmiHeader.biWidth = width1;
            bmi.bmiHeader.biHeight = -height; // top-down
            bmi.bmiHeader.biPlanes = 1;
            bmi.bmiHeader.biBitCount = 32;
            bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

            int imageSize = width1 * height * 4;
            Memory buffer = new Memory(imageSize);

            // 7. Copy bits into buffer
            GDI32.INSTANCE.GetDIBits(hdcWindow1, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

            // 8. Create BufferedImage
            BufferedImage image = new BufferedImage(width1, height, BufferedImage.TYPE_INT_RGB);
            int[] pixels = buffer.getIntArray(0, width1 * height);
            image.setRGB(0, 0, width1, height, pixels, 0, width1);
//
            // 9. Save screenshot file
            File dir = new File(System.getProperty("user.dir") + "/test-output/screenshots/");
            if (!dir.exists()) dir.mkdirs();
            file = new File(dir, reportName + "_Excel.png");
            ImageIO.write(image, "png", file);

            System.out.println("✅ Excel screenshot saved: " + file.getAbsolutePath());

            // 10. Cleanup
            GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
            GDI32.INSTANCE.DeleteObject(hBitmap);
            GDI32.INSTANCE.DeleteDC(hdcMemDC);
            User32.INSTANCE.ReleaseDC(hwnd, hdcWindow1);

        } catch (Exception e) {
            System.err.println("❌ Excel screenshot failed: " + e.getMessage());
            e.printStackTrace();
        }

        return file;
    }
}
