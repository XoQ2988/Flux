package me.xoq.flux.modules.impl;

import me.xoq.flux.events.EventHandler;
import me.xoq.flux.events.SoundEvent;
import me.xoq.flux.events.TickEvent;
import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.IntSetting;
import me.xoq.flux.settings.Setting;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static me.xoq.flux.FluxClient.mc;

public class AutoFish extends Module {
    public AutoFish() {
        super("auto-fish", "Automatically reels and recasts your fishing rod");
    }

    private final Setting<Integer> detectRange = settings.add(
            new IntSetting.Builder()
                    .name("detect-range")
                    .description("Max blocks away to respond to fish-bite sounds")
                    .defaultValue(6)
                    .range(1, 64)
                    .build()
    );

    private final Setting<Integer> minDelay = settings.add(
            new IntSetting.Builder()
                    .name("min-delay")
                    .description("Minimum ticks to wait before reeling & recasting")
                    .defaultValue(20)
                    .range(0, 100)
                    .build()
    );
    private final Setting<Integer> maxDelay = settings.add(
            new IntSetting.Builder()
                    .name("max-delay")
                    .description("Maximum ticks to wait before reeling & recasting")
                    .defaultValue(40)
                    .range(0, 200)
                    .build()
    );

    private int ticksWaiting;
    private boolean cast, shouldReel;
    private final Random rand = new Random();

    @Override
    protected void onEnabled() {
        cast = false;
        shouldReel = false;
        scheduleNext();
    }

    @EventHandler
    protected void onTick(TickEvent event) {
        if (mc.player == null) return;

        ItemStack mainHandStack = mc.player.getMainHandStack();
        if (mainHandStack.getItem() != Items.FISHING_ROD) return;

        // If detected a bite, reel in
        if (shouldReel) {
            mc.interactionManager.interactItem(mc.player, net.minecraft.util.Hand.MAIN_HAND);
            cast = false;
            shouldReel = false;
            scheduleNext();
            return;
        }

        // Humanized delay
        if (ticksWaiting > 0) {
            ticksWaiting--;
            return;
        }

        FishingBobberEntity bobber = mc.player.fishHook;
        if (!cast) {
            // initial or new cast
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            cast = true;
            scheduleNext();
        } else if (bobber == null) {
            // bobber gone :( cast again
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            scheduleNext();
        }
    }

    @EventHandler
    protected void onSound(SoundEvent event) {
        if (mc.player == null) return;

        SoundInstance soundInstance = event.getSoundInstance();

        // check if fish
        Identifier identifier = soundInstance.getId();
        if (!identifier.equals(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH.id())) return;

        // distance check (squared)
        Vec3d playerPos = mc.player.getPos();
        double dx = playerPos.x - soundInstance.getX();
        double dy = playerPos.y - soundInstance.getY();
        double dz = playerPos.z - soundInstance.getZ();
        double maxDist = detectRange.getDefault();

        if (dx*dx + dy*dy + dz*dz > maxDist*maxDist) return;

        shouldReel = true;
    }

    private void scheduleNext() {
        int min = minDelay.getValue();
        int max = maxDelay.getValue();
        if (max < min) max = min;
        ticksWaiting = min + rand.nextInt(max - min + 1);
    }
}