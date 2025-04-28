package me.xoq.flux.events;

import net.minecraft.client.sound.SoundInstance;

public class SoundEvent {
    private final SoundInstance soundInstance;
    private boolean cancelled = false;

    public SoundEvent(SoundInstance soundInstance) {
        this.soundInstance = soundInstance;
    }

    public SoundInstance getSoundInstance() {
        return soundInstance;
    }

    public void cancel()              { this.cancelled = true; }
    public boolean isCancelled()      { return this.cancelled; }
}
