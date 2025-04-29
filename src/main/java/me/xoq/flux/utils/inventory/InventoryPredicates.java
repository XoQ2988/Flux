package me.xoq.flux.utils.inventory;

import me.xoq.flux.utils.misc.SlotUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

import static me.xoq.flux.FluxClient.mc;

public final class InventoryPredicates {
    private InventoryPredicates() {}

    public static boolean inMainHand(Predicate<ItemStack> pred) {
        return pred.test(mc.player.getMainHandStack());
    }

    public static boolean mainHandIs(Item... items) {
        return inMainHand(s -> {
            for (Item i : items) if (s.isOf(i)) return true;
            return false;
        });
    }

    public static boolean inOffHand(Predicate<ItemStack> pred) {
        return pred.test(mc.player.getOffHandStack());
    }

    public static boolean offHandIs(Item... items) {
        return inOffHand(s -> {
            for (Item i : items) if (s.isOf(i)) return true;
            return false;
        });
    }

    public static boolean inHands(Predicate<ItemStack> pred) {
        return inMainHand(pred) || inOffHand(pred);
    }

    public static boolean handsContain(Item... items) {
        return mainHandIs(items) || offHandIs(items);
    }

    public static boolean inHotbar(Predicate<ItemStack> pred) {
        if (inHands(pred)) return true;
        for (int i = SlotUtils.HOTBAR_START; i <= SlotUtils.HOTBAR_END; i++) {
            if (pred.test(mc.player.getInventory().getStack(i))) return true;
        }
        return false;
    }

    public static boolean hotbarContains(Item... items) {
        return inHotbar(s -> {
            for (Item i : items) if (s.isOf(i)) return true;
            return false;
        });
    }
}