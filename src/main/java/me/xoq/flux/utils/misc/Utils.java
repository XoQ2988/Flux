package me.xoq.flux.utils.misc;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.*;

public class Utils {
    public static String nameToTitle(String name) {
        return Arrays.stream(name.split("-")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    public static String titleToName(String title) {
        return title.replace(" ", "-").toLowerCase(Locale.ROOT);
    }

    public static int parseColor(String input) {
        String hex = input.startsWith("0x")
                ? input.substring(2)
                : input.startsWith("#")
                ? input.substring(1)
                : input;
        if (hex.length() > 8) {
            hex = hex.substring(hex.length() - 8); // drop extra alpha if provided
        }
        return (int) Long.parseLong(hex, 16);
    }

    public static String getKeyName(int key) {
        // Printable keys: use GLFW’s mapping, then title-case
        String raw = glfwGetKeyName(key, 0);
        if (raw != null) {
            raw = raw.toLowerCase(Locale.ROOT);
            return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
        }

        // Non-printable keys
        return switch (key) {
            case GLFW_KEY_SPACE         -> "Space";
            case GLFW_KEY_ESCAPE        -> "Escape";
            case GLFW_KEY_ENTER         -> "Enter";
            case GLFW_KEY_TAB           -> "Tab";
            case GLFW_KEY_BACKSPACE     -> "Backspace";
            case GLFW_KEY_INSERT        -> "Insert";
            case GLFW_KEY_DELETE        -> "Delete";
            case GLFW_KEY_RIGHT         -> "Right Arrow";
            case GLFW_KEY_LEFT          -> "Left Arrow";
            case GLFW_KEY_DOWN          -> "Down Arrow";
            case GLFW_KEY_UP            -> "Up Arrow";
            case GLFW_KEY_PAGE_UP       -> "Page Up";
            case GLFW_KEY_PAGE_DOWN     -> "Page Down";
            case GLFW_KEY_HOME          -> "Home";
            case GLFW_KEY_END           -> "End";
            case GLFW_KEY_CAPS_LOCK     -> "Caps Lock";
            case GLFW_KEY_SCROLL_LOCK   -> "Scroll Lock";
            case GLFW_KEY_NUM_LOCK      -> "Num Lock";
            case GLFW_KEY_PRINT_SCREEN  -> "Print Screen";
            case GLFW_KEY_PAUSE         -> "Pause";
            // Function keys
            case GLFW_KEY_F1            -> "F1";
            case GLFW_KEY_F2            -> "F2";
            case GLFW_KEY_F3            -> "F3";
            case GLFW_KEY_F4            -> "F4";
            case GLFW_KEY_F5            -> "F5";
            case GLFW_KEY_F6            -> "F6";
            case GLFW_KEY_F7            -> "F7";
            case GLFW_KEY_F8            -> "F8";
            case GLFW_KEY_F9            -> "F9";
            case GLFW_KEY_F10           -> "F10";
            case GLFW_KEY_F11           -> "F11";
            case GLFW_KEY_F12           -> "F12";
            // Modifier keys
            case GLFW_KEY_LEFT_SHIFT    -> "Left Shift";
            case GLFW_KEY_RIGHT_SHIFT   -> "Right Shift";
            case GLFW_KEY_LEFT_CONTROL  -> "Left Control";
            case GLFW_KEY_RIGHT_CONTROL -> "Right Control";
            case GLFW_KEY_LEFT_ALT      -> "Left Alt";
            case GLFW_KEY_RIGHT_ALT     -> "Right Alt";
            case GLFW_KEY_LEFT_SUPER    -> "Left Super";
            case GLFW_KEY_RIGHT_SUPER   -> "Right Super";
            default                          -> "Unknown(" + key + ")";
        };
    }
}
