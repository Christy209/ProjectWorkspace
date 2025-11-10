package Utilities;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;


/**
 * Extends JNA User32 to include missing APIs like ClientToScreen.
 */
public interface User32Extra extends User32 {
    User32Extra INSTANCE = Native.load("user32", User32Extra.class);

    boolean ClientToScreen(HWND hWnd, POINT lpPoint);
}
