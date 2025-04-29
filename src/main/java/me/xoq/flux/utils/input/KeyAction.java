package me.xoq.flux.utils.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public enum KeyAction {
    Press,
    Repeat,
    Release;

    public static KeyAction get(int action) {
        return switch (action) {
            case GLFW_PRESS -> Press;
            case GLFW_RELEASE -> Release;
            default -> Repeat;
        };
    }
}