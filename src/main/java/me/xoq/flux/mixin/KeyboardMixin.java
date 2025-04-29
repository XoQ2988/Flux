package me.xoq.flux.mixin;

import me.xoq.flux.FluxClient;
import me.xoq.flux.events.misc.KeyEvent;
import me.xoq.flux.utils.input.Input;
import me.xoq.flux.utils.input.KeyAction;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.*;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key == GLFW_KEY_UNKNOWN) return;

        // update modifier flags
        if (action == GLFW_PRESS) modifiers |= Input.getModifierMask(key);
        else if (action == GLFW_RELEASE) modifiers &= ~Input.getModifierMask(key);

        Input.setKeyState(key, action != GLFW_RELEASE);

        // dispatch and possibly cancel
        KeyEvent keyEvent = new KeyEvent(key, modifiers, KeyAction.get(action));
        if (FluxClient.EVENT_BUS.dispatch(keyEvent).isCancelled()) {
            info.cancel();
        }
    }
}
