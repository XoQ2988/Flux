package me.xoq.flux.mixin;


import me.xoq.flux.FluxClient;
import me.xoq.flux.events.render.Render2DEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        FluxClient.EVENT_BUS.dispatch(new Render2DEvent(context,
                context.getScaledWindowWidth(), context.getScaledWindowWidth(),
                tickCounter.getTickProgress(true)));
    }
}
