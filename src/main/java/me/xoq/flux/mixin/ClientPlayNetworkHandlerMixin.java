package me.xoq.flux.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.xoq.flux.commands.Commands;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {
    protected ClientPlayNetworkHandlerMixin(MinecraftClient client,
                                            ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }


    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (!message.startsWith(".")) return;
        try {
            Commands.dispatch(message.substring(1));
        } catch (CommandSyntaxException e) {
            client.player.sendMessage(Text.of(e.getMessage()), false);
        }

        // add to history so the player can recall it with ↑
        client.inGameHud.getChatHud().addToMessageHistory(message);
        ci.cancel(); // don't send to server
    }
}
