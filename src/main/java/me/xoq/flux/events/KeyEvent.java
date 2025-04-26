package me.xoq.flux.events;

import me.xoq.flux.utils.input.KeyAction;

public class KeyEvent {
    private static final KeyEvent INSTANCE = new KeyEvent();

    public int key, modifiers;
    public boolean cancelled;
    public KeyAction action;

    public static KeyEvent get(int key, int modifiers, KeyAction action) {
        INSTANCE.setCancelled(false);
        INSTANCE.key = key;
        INSTANCE.modifiers = modifiers;
        INSTANCE.action = action;

        return INSTANCE;
    }

    public void cancel() {
        setCancelled(true);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
