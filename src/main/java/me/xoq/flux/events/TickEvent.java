package me.xoq.flux.events;

public class TickEvent {
    private static final TickEvent INSTANCE = new TickEvent();

    public static TickEvent get() {
        return INSTANCE;
    }
}