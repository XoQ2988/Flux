package me.xoq.flux.utils.input;

import me.xoq.flux.mixin.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static final boolean[] KEYS = new boolean[512];
    private static final boolean[] BUTTONS = new boolean[16];

    private Input() { }

    public static void setKeyState(int keyCode, boolean pressed) {
        if (keyCode >= 0 && keyCode < KEYS.length) {
            KEYS[keyCode] = pressed;
        }
    }

    public static void setKeyState(KeyBinding bind, boolean pressed) {
        setKeyState(getKey(bind), pressed);
    }

    public static void setButtonState(int button, boolean pressed) {
        if (button >= 0 && button < BUTTONS.length) {
            BUTTONS[button] = pressed;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyCode >= 0 && keyCode < KEYS.length && KEYS[keyCode];
    }

    public static boolean isButtonPressed(int button) {
        return button >= 0 && button < BUTTONS.length && BUTTONS[button];
    }

    public static int getKey(KeyBinding binding) {
        return ((KeyBindingAccessor) binding).getKey().getCode();
    }

    public static int getModifierMask(int keyCode) {
        return switch (keyCode) {
            case GLFW_KEY_LEFT_SHIFT,
                 GLFW_KEY_RIGHT_SHIFT  ->  GLFW_MOD_SHIFT;
            case GLFW_KEY_LEFT_CONTROL,
                 GLFW_KEY_RIGHT_CONTROL -> GLFW_MOD_CONTROL;
            case GLFW_KEY_LEFT_ALT,
                 GLFW_KEY_RIGHT_ALT    ->  GLFW_MOD_ALT;
            case GLFW_KEY_LEFT_SUPER,
                 GLFW_KEY_RIGHT_SUPER  ->  GLFW_MOD_SUPER;
            default -> 0;
        };
    }
}