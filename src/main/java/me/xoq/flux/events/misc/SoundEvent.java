package me.xoq.flux.events.misc;

import me.xoq.flux.events.CancellableEvent;
import net.minecraft.client.sound.SoundInstance;

public class SoundEvent extends CancellableEvent {
    private final SoundInstance soundInstance;

    public SoundEvent(SoundInstance soundInstance) {
        this.soundInstance = soundInstance;
    }

    public SoundInstance getSoundInstance() {
        return soundInstance;
    }
}