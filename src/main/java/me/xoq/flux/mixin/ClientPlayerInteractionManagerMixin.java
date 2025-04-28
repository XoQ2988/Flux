package me.xoq.flux.mixin;


import me.xoq.flux.FluxClient;
import me.xoq.flux.events.BlockBreakEvent;
import me.xoq.flux.events.BlockInteractEvent;
import me.xoq.flux.events.BlockAttackEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.xoq.flux.FluxClient.mc;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void interactBlock(ClientPlayerEntity player, Hand hand,
                               BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> callbackInfo) {
        BlockInteractEvent blockInteractEvent = new BlockInteractEvent(mc.world.getBlockState(hitResult.getBlockPos()));
        FluxClient.EVENT_BUS.dispatch(blockInteractEvent);
        if (blockInteractEvent.isCancelled()) {
            callbackInfo.setReturnValue(ActionResult.FAIL);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> callbackInfo) {
        BlockAttackEvent blockAttackEvent = new BlockAttackEvent(blockPos, direction);
        FluxClient.EVENT_BUS.dispatch(blockAttackEvent);
        if (blockAttackEvent.isCancelled()) {
            callbackInfo.setReturnValue(false);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    private void onBreakBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> callbackInfo) {
        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(blockPos);
        FluxClient.EVENT_BUS.dispatch(blockBreakEvent);
        if (blockBreakEvent.isCancelled()) {
            callbackInfo.setReturnValue(false);
            callbackInfo.cancel();
        }
    }
}