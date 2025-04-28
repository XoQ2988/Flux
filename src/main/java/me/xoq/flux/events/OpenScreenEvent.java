package me.xoq.flux.events;

import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent {
    private final Screen screen;
    private boolean cancelled = false;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void cancel()              { this.cancelled = true; }
    public boolean isCancelled()      { return this.cancelled; }
}
