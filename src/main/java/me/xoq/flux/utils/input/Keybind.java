package me.xoq.flux.utils.input;

import me.xoq.flux.utils.misc.Utils;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public final class Keybind {
    private boolean isKey;
    private int code;
    private int modifiers;

    private Keybind(boolean isKey, int code, int modifiers) {
        set(isKey, code, modifiers);
    }

    public static Keybind none() {
        return new Keybind(true, GLFW_KEY_UNKNOWN, 0);
    }


    public boolean isKey() {
        return isKey;
    }

    public int getCode() {
        return code;
    }

    public int getModifiers() {
        return modifiers;
    }

    public boolean isSet() {
        return code != GLFW_KEY_UNKNOWN;
    }

    public boolean hasModifiers() {
        return isKey && modifiers != 0;
    }

    public void set(boolean isKey, int code, int modifiers) {
        this.isKey = isKey;
        this.code = code;
        this.modifiers = modifiers;
    }

    public void set(Keybind value) {
        this.isKey = value.isKey;
        this.code = value.code;
        this.modifiers = value.modifiers;
    }

    public void reset() {
        set(true, GLFW_KEY_UNKNOWN, 0);
    }

    public boolean matches(boolean isKey, int value, int modifiers) {
        if (!this.isSet() || this.isKey != isKey) return false;
        if (!hasModifiers()) return this.code == value;
        return this.code == value && this.modifiers == modifiers;
    }

    public boolean isPressed() {
        return isKey ? modifiersPressed() && Input.isKeyPressed(code) : Input.isButtonPressed(code);
    }

    public boolean canBindTo(boolean isKey, int value, int modifiers) {
        if (isKey) {
            if (modifiers != 0 && isKeyMod(value)) return false;
            return value != GLFW_KEY_UNKNOWN && value != GLFW_KEY_ESCAPE;
        }
        return value != GLFW_MOUSE_BUTTON_LEFT && value != GLFW_MOUSE_BUTTON_RIGHT;
    }

    private boolean isModPressed(int value, int... keys) {
        if ((modifiers & value) == 0) return true;

        for (int key : keys) {
            if (Input.isKeyPressed(key)) return true;
        }

        return false;
    }

    private boolean isKeyMod(int key) {
        return key >= GLFW_KEY_LEFT_SHIFT && key <= GLFW_KEY_RIGHT_SUPER;
    }

    public Keybind copy() {
        return new Keybind(isKey, code, modifiers);
    }

    private boolean modifiersPressed() {
        if (!hasModifiers()) return true;

        if ((modifiers & GLFW_MOD_CONTROL) != 0
                && !Input.isKeyPressed(GLFW_KEY_LEFT_CONTROL)
                && !Input.isKeyPressed(GLFW_KEY_RIGHT_CONTROL)) {
            return false;
        }
        if ((modifiers & GLFW_MOD_SHIFT) != 0
                && !Input.isKeyPressed(GLFW_KEY_LEFT_SHIFT)
                && !Input.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
            return false;
        }
        if ((modifiers & GLFW_MOD_ALT) != 0
                && !Input.isKeyPressed(GLFW_KEY_LEFT_ALT)
                && !Input.isKeyPressed(GLFW_KEY_RIGHT_ALT)) {
            return false;
        }
        return (modifiers & GLFW_MOD_SUPER) == 0
                || Input.isKeyPressed(GLFW_KEY_LEFT_SUPER)
                || Input.isKeyPressed(GLFW_KEY_RIGHT_SUPER);
    }

    @Override
    public String toString() {
        if (!isSet()) {
            return "None";
        }

        StringBuilder sb = new StringBuilder();
        if ((modifiers & GLFW_MOD_CONTROL) != 0) sb.append("Ctrl+");
        if ((modifiers & GLFW_MOD_SHIFT)   != 0) sb.append("Shift+");
        if ((modifiers & GLFW_MOD_ALT)     != 0) sb.append("Alt+");
        if ((modifiers & GLFW_MOD_SUPER)   != 0) sb.append("Cmd+");

        // use Utils to get human-friendly name
        sb.append(Utils.getKeyName(code));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Keybind keybind)) return false;
        return isKey == keybind.isKey && code == keybind.code && modifiers == keybind.modifiers;
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(isKey);
        result = 31 * result + code;
        result = 31 * result + modifiers;
        return result;
    }
}
