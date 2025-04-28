package me.xoq.flux.modules.impl;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import me.xoq.flux.events.BlockAttackEvent;
import me.xoq.flux.events.EventHandler;
import me.xoq.flux.events.TickEvent;
import me.xoq.flux.modules.Module;
import me.xoq.flux.settings.BoolSetting;
import me.xoq.flux.settings.IntSetting;
import me.xoq.flux.settings.Setting;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Set;

import static me.xoq.flux.FluxClient.mc;

public class AutoTool extends Module {
    public AutoTool() {
        super("auto-tool", "Automatically select the most effective tool when breaking a block");
    }

    private final Setting<Boolean> switchBack = settings.add(
            new BoolSetting.Builder()
                    .name("switch-back")
                    .description("Whether or not to switch back to the original slot")
                    .defaultValue(true)
                    .build()
    );

    private final Setting<Integer> switchBackDelay = settings.add(
            new IntSetting.Builder()
                    .name("switch-back-delay")
                    .description("How many ticks to wait before switching back (0 = next tick).")
                    .defaultValue(1)
                    .range(1, 20)  // Values under 1 prevents breaking entirely
                    .build()
    );

    private int previousSlot = -1;
    private boolean didSwitch = false;
    private int ticksUntilSwitchBack = -1;


    @EventHandler
    public void onTick(TickEvent event) {
        if (didSwitch && switchBack.getValue() && ticksUntilSwitchBack >= 0) {
            if (ticksUntilSwitchBack-- <= 0) {
                PlayerInventory inv = mc.player.getInventory();

                inv.setSelectedSlot(previousSlot);
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(previousSlot));

                didSwitch = false;
                previousSlot = -1;
                ticksUntilSwitchBack = -1;
            }
        }
    }

    @EventHandler
    private void onStartBreakingBlock(BlockAttackEvent event) {
        if (!isEnabled()) return;

        BlockState blockState = mc.world.getBlockState(event.getBlockPos());
        PlayerInventory inv = mc.player.getInventory();

        int currentSlot = inv.getSelectedSlot();
        int bestSlot = currentSlot;
        float bestSpeed = mc.player.getMainHandStack().getMiningSpeedMultiplier(blockState);

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty()) {
                int effLevel = getEnchantmentLevel(stack, Enchantments.EFFICIENCY);
                float enchantBonus = effLevel > 0 ? 1.0f + (effLevel * effLevel + 1) * 0.2f : 1.0f;
                float speed = stack.getMiningSpeedMultiplier(blockState) * enchantBonus;

                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
        }

        if (bestSlot != currentSlot) {
            inv.setSelectedSlot(bestSlot);
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(bestSlot));

            previousSlot = currentSlot;
            didSwitch = true;
            ticksUntilSwitchBack = switchBackDelay.getValue();
        }
    }

    public static void getEnchantments(ItemStack itemStack, Object2IntMap<RegistryEntry<Enchantment>> enchantments) {
        enchantments.clear();

        if (!itemStack.isEmpty()) {
            Set<Object2IntMap.Entry<RegistryEntry<Enchantment>>> itemEnchantments =
                    itemStack.getItem() == Items.ENCHANTED_BOOK
                    ? itemStack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
                            .getEnchantmentEntries()
                    : itemStack.getEnchantments()
                            .getEnchantmentEntries();

            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantments) {
                enchantments.put(entry.getKey(), entry.getIntValue());
            }
        }
    }

    public static int getEnchantmentLevel(ItemStack itemStack, RegistryKey<Enchantment> enchantment) {
        if (itemStack.isEmpty()) return 0;
        Object2IntMap<RegistryEntry<Enchantment>> itemEnchantments = new Object2IntArrayMap<>();
        getEnchantments(itemStack, itemEnchantments);
        return getEnchantmentLevel(itemEnchantments, enchantment);
    }

    public static int getEnchantmentLevel(Object2IntMap<RegistryEntry<Enchantment>> itemEnchantments,
                                          RegistryKey<Enchantment> enchantment) {
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : Object2IntMaps.fastIterable(itemEnchantments)) {
            if (entry.getKey().matchesKey(enchantment)) return entry.getIntValue();
        }
        return 0;
    }
}