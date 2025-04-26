package me.xoq.flux.mixin;

import me.xoq.flux.FluxClient;
import me.xoq.flux.events.KeyEvent;
import me.xoq.flux.utils.input.Input;
import me.xoq.flux.utils.input.KeyAction;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;

        // update modifier flags
        if (action == GLFW.GLFW_PRESS)    modifiers |= Input.getModifier(key);
        else if (action == GLFW.GLFW_RELEASE) modifiers &= ~Input.getModifier(key);

        Input.setKeyState(key, action != GLFW.GLFW_RELEASE);

        // dispatch and possibly cancel
        KeyEvent evt = KeyEvent.get(key, modifiers, KeyAction.get(action));
        if (FluxClient.EVENT_BUS.dispatch(evt).isCancelled()) {
            info.cancel();
        }
    }
}
