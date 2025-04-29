package me.xoq.flux.utils.inventory;

import me.xoq.flux.utils.misc.SlotUtils;
import net.minecraft.item.ItemStack;

import java.util.OptionalInt;
import java.util.function.Predicate;

import static me.xoq.flux.FluxClient.mc;

public final class InventoryFinder {
    private InventoryFinder() {}

    public static OptionalInt findSlot(Predicate<ItemStack> pred, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (pred.test(mc.player.getInventory().getStack(i))) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public static OptionalInt findInHandsThenHotbar(Predicate<ItemStack> pred) {
        if (pred.test(mc.player.getOffHandStack())) return OptionalInt.of(SlotUtils.OFFHAND);
        if (pred.test(mc.player.getMainHandStack())) return OptionalInt.of(mc.player.getInventory().getSelectedSlot());
        return findSlot(pred, SlotUtils.HOTBAR_START, SlotUtils.HOTBAR_END);
    }

    public static OptionalInt findAnywhere(Predicate<ItemStack> pred) {
        if (mc.player == null) return OptionalInt.empty();
        int size = mc.player.getInventory().size();
        return findSlot(pred, 0, size - 1);
    }
}
