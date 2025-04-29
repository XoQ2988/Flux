package me.xoq.flux.mixin;

import me.xoq.flux.FluxClient;
import me.xoq.flux.events.world.BlockPlaceEvent;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z",
            at = @At("HEAD"), cancellable = true)
    private void onPlaceBlock(ItemPlacementContext placementContext, BlockState blockState,
                              CallbackInfoReturnable<Boolean> callbackInfo) {
        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(placementContext.getBlockPos(), blockState.getBlock());
        FluxClient.EVENT_BUS.dispatch(blockPlaceEvent);
        if (blockPlaceEvent.isCancelled()) {
            callbackInfo.setReturnValue(false);
            callbackInfo.cancel();
        }
    }
}
