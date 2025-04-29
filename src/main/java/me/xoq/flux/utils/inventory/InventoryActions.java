package me.xoq.flux.utils.inventory;

import me.xoq.flux.utils.misc.SlotUtils;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import static me.xoq.flux.FluxClient.mc;

public final class InventoryActions {
    private static final ClientPlayerInteractionManager IM = mc.interactionManager;

    private final int syncId;

    private InventoryActions(int syncId) {
        this.syncId = syncId;
    }

    public static InventoryActions with(ScreenHandler handler) {
        return new InventoryActions(handler.syncId);
    }

    public InventoryActions pickupId(int slotId) {
        IM.clickSlot(syncId, slotId, 0, SlotActionType.PICKUP, mc.player);
        return this;
    }

    public InventoryActions pickupHotbar(int hotbarIdx) {
        return pickupId(SlotUtils.indexToId(hotbarIdx));
    }

    public InventoryActions swapHotbarOffhand(int hotbarIdx) {
        IM.clickSlot(syncId, SlotUtils.indexToId(hotbarIdx), 0, SlotActionType.SWAP, mc.player);
        return this;
    }

    public InventoryActions quickMoveId(int slotId) {
        IM.clickSlot(syncId, slotId, 0, SlotActionType.QUICK_MOVE, mc.player);
        return this;
    }

    public InventoryActions dropId(int slotId) {
        IM.clickSlot(syncId, slotId, 1, SlotActionType.THROW, mc.player);
        return this;
    }
}
