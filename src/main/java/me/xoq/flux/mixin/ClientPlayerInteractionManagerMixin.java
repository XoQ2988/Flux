package me.xoq.flux.mixin;


import me.xoq.flux.FluxClient;
import me.xoq.flux.events.StartBreakingBlockEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> info) {
        StartBreakingBlockEvent evt = new StartBreakingBlockEvent(blockPos, direction);
        FluxClient.EVENT_BUS.dispatch(evt);
        if (evt.isCancelled()) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
}