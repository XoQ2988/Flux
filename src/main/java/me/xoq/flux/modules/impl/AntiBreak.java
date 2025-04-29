package me.xoq.flux.modules.impl;

import me.xoq.flux.events.world.BlockAttackEvent;
import me.xoq.flux.events.EventHandler;
import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.BoolSetting;
import me.xoq.flux.settings.IntSetting;
import me.xoq.flux.settings.Setting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.tag.ItemTags;

import static me.xoq.flux.FluxClient.mc;

public class AntiBreak extends Module {
    public AntiBreak() {
        super("anti-break", "Prevents you from destroying your tools");
    }

    private final Setting<Boolean> shouldWarn = settings.add(
            new BoolSetting.Builder()
                    .name("should-warn")
                    .description("Warn if durability is at or below a certain point")
                    .defaultValue(true)
                    .build()
    );

    private final Setting<Integer> warnThreshold = settings.add(
            new IntSetting.Builder()
                    .name("warn-threshold")
                    .description("Durability percent to warn at")
                    .defaultValue(15)
                    .range(1, 100)
                    .build()
    );

    private final Setting<Boolean> shouldPrevent = settings.add(
            new BoolSetting.Builder()
                    .name("should-prevent")
                    .description("Prevent using tool if durability is at or below a certain point")
                    .defaultValue(false)
                    .build()
    );

    private final Setting<Integer> preventThreshold = settings.add(
            new IntSetting.Builder()
                    .name("prevent-threshold")
                    .description("Durability percent to prevent at")
                    .defaultValue(5)
                    .range(1, 100)
                    .build()
    );

    private boolean warned, prevented;

    @Override
    protected void onEnabled() {
        warned = false;
        prevented = false;
    }

    @EventHandler
    private void onBlockAttack(BlockAttackEvent event) {
        ItemStack handStack = mc.player.getMainHandStack();
        if (handStack.isEmpty() || !isTool(handStack)) return;

        int maxDamage = handStack.getMaxDamage();
        int damage = handStack.getDamage();
        int remaining = maxDamage - damage;
        int remainingPercent = remaining * 100 / maxDamage;

        // Prevention logic
        if (shouldPrevent.getValue() && remainingPercent <= preventThreshold.getValue()) {
            if (!prevented) {
                error(formatToolName(handStack) + " at " + remainingPercent + "% durability");
                prevented = true;
            }

            event.cancel();
            return;
        } else prevented = false;

        // Warning logic
        if (shouldWarn.getValue() && remainingPercent <= warnThreshold.getValue()) {
            if (!warned) {
                warn(formatToolName(handStack) + " at " + remainingPercent + "% durability");
                warned = true;
            }
        } else warned = false;
    }

    public static boolean isTool(Item item) {
        return isTool(item.getDefaultStack());
    }
    public static boolean isTool(ItemStack itemStack) {
        return itemStack.isIn(ItemTags.AXES) || itemStack.isIn(ItemTags.HOES) || itemStack.isIn(ItemTags.PICKAXES) || itemStack.isIn(ItemTags.SHOVELS) || itemStack.getItem() instanceof ShearsItem;
    }

    private String formatToolName(ItemStack stack) {
        return stack.getItem().getName(stack).getString();
    }
}