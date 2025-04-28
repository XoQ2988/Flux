package me.xoq.flux.mixin;

import me.xoq.flux.FluxClient;
import me.xoq.flux.events.OpenScreenEvent;
import me.xoq.flux.events.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo info) {
        FluxClient.EVENT_BUS.dispatch(TickEvent.get());
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onSetScreen(Screen screen, CallbackInfo callbackInfo) {
        OpenScreenEvent openScreenEvent = new OpenScreenEvent(screen);
        FluxClient.EVENT_BUS.dispatch(openScreenEvent);

        if (openScreenEvent.isCancelled()) callbackInfo.cancel();
    }
}