package me.xoq.flux.utils.misc;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Utils {
    public static String nameToTitle(String name) {
        return Arrays.stream(name.split("-")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    public static String titleToName(String title) {
        return title.replace(" ", "-").toLowerCase(Locale.ROOT);
    }
    public static String getKeyName(int key) {
        // Printable keys: use GLFW’s mapping, then title-case
        String raw = GLFW.glfwGetKeyName(key, 0);
        if (raw != null) {
            raw = raw.toLowerCase(Locale.ROOT);
            return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
        }

        // Non-printable keys
        return switch (key) {
            case GLFW.GLFW_KEY_SPACE         -> "Space";
            case GLFW.GLFW_KEY_ESCAPE        -> "Escape";
            case GLFW.GLFW_KEY_ENTER         -> "Enter";
            case GLFW.GLFW_KEY_TAB           -> "Tab";
            case GLFW.GLFW_KEY_BACKSPACE     -> "Backspace";
            case GLFW.GLFW_KEY_INSERT        -> "Insert";
            case GLFW.GLFW_KEY_DELETE        -> "Delete";
            case GLFW.GLFW_KEY_RIGHT         -> "Right Arrow";
            case GLFW.GLFW_KEY_LEFT          -> "Left Arrow";
            case GLFW.GLFW_KEY_DOWN          -> "Down Arrow";
            case GLFW.GLFW_KEY_UP            -> "Up Arrow";
            case GLFW.GLFW_KEY_PAGE_UP       -> "Page Up";
            case GLFW.GLFW_KEY_PAGE_DOWN     -> "Page Down";
            case GLFW.GLFW_KEY_HOME          -> "Home";
            case GLFW.GLFW_KEY_END           -> "End";
            case GLFW.GLFW_KEY_CAPS_LOCK     -> "Caps Lock";
            case GLFW.GLFW_KEY_SCROLL_LOCK   -> "Scroll Lock";
            case GLFW.GLFW_KEY_NUM_LOCK      -> "Num Lock";
            case GLFW.GLFW_KEY_PRINT_SCREEN  -> "Print Screen";
            case GLFW.GLFW_KEY_PAUSE         -> "Pause";
            // Function keys
            case GLFW.GLFW_KEY_F1            -> "F1";
            case GLFW.GLFW_KEY_F2            -> "F2";
            case GLFW.GLFW_KEY_F3            -> "F3";
            case GLFW.GLFW_KEY_F4            -> "F4";
            case GLFW.GLFW_KEY_F5            -> "F5";
            case GLFW.GLFW_KEY_F6            -> "F6";
            case GLFW.GLFW_KEY_F7            -> "F7";
            case GLFW.GLFW_KEY_F8            -> "F8";
            case GLFW.GLFW_KEY_F9            -> "F9";
            case GLFW.GLFW_KEY_F10           -> "F10";
            case GLFW.GLFW_KEY_F11           -> "F11";
            case GLFW.GLFW_KEY_F12           -> "F12";
            // Modifier keys
            case GLFW.GLFW_KEY_LEFT_SHIFT    -> "Left Shift";
            case GLFW.GLFW_KEY_RIGHT_SHIFT   -> "Right Shift";
            case GLFW.GLFW_KEY_LEFT_CONTROL  -> "Left Control";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "Right Control";
            case GLFW.GLFW_KEY_LEFT_ALT      -> "Left Alt";
            case GLFW.GLFW_KEY_RIGHT_ALT     -> "Right Alt";
            case GLFW.GLFW_KEY_LEFT_SUPER    -> "Left Super";
            case GLFW.GLFW_KEY_RIGHT_SUPER   -> "Right Super";
            default                          -> "Unknown(" + key + ")";
        };
    }
}
