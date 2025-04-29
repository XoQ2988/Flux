package me.xoq.flux.modules.impl;

import me.xoq.flux.events.EventHandler;
import me.xoq.flux.events.world.BlockPlaceEvent;
import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.BoolSetting;
import me.xoq.flux.settings.IntSetting;
import me.xoq.flux.settings.Setting;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.xoq.flux.FluxClient.mc;

public class RandomHotbar extends Module {
    public RandomHotbar() {
        super("random-hotbar", "Randomly switch hotbar slot after placing a block");
    }

    private final Setting<Integer> minSlot = settings.add(
            new IntSetting.Builder()
                    .name("min-slot")
                    .description("Minimum hotbar slot (0–8) to switch to")
                    .defaultValue(0)
                    .range(0, 8)
                    .build()
    );

    private final Setting<Integer> maxSlot = settings.add(
            new IntSetting.Builder()
                    .name("max-slot")
                    .description("Maximum hotbar slot (0–8) to switch to")
                    .defaultValue(3)
                    .range(0, 8)
                    .build()
    );

    private final Setting<Boolean> weighted = settings.add(
            new BoolSetting.Builder()
                    .name("weighted")
                    .description("Weight selection by slot index (lower slots more likely)")
                    .defaultValue(false)
                    .build()
    );

    private final Random random = new Random();

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (mc.player == null) return;

        PlayerInventory inv = mc.player.getInventory();
        int min = minSlot.getValue();
        int max = maxSlot.getValue();
        if (max < min) { int t = min; min = max; max = t; }

        // collect valid slots containing a placeable block
        List<Integer> valid = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                valid.add(i);
            }
        }

        int chosenSlot;
        if (!valid.isEmpty() && weighted.getValue()) {
            int totalWeight = 0;
            for (int slot : valid) {
                totalWeight += (max - slot + 1);
            }
            int r = random.nextInt(totalWeight);
            int cum = 0;
            chosenSlot = valid.get(0);
            for (int slot : valid) {
                cum += (max - slot + 1);
                if (r < cum) {
                    chosenSlot = slot;
                    break;
                }
            }
        } else if (!valid.isEmpty()) {
            // uniform random among valid
            chosenSlot = valid.get(random.nextInt(valid.size()));
        } else {
            // fallback: uniform random in full range
            chosenSlot = min + random.nextInt(max - min + 1);
        }

        // switch to the chosen slot
        inv.setSelectedSlot(chosenSlot);
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(chosenSlot));
    }
}