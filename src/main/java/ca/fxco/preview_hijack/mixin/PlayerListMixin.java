package ca.fxco.preview_hijack.mixin;

import ca.fxco.preview_hijack.PreviewHijack;
import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public class PlayerListMixin {


    @Redirect(
            method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;" +
                    "Ljava/util/function/Function;Lnet/minecraft/network/chat/ChatSender;" +
                    "Lnet/minecraft/resources/ResourceKey;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;sendChatMessage(" +
                            "Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/network/chat/ChatSender;" +
                            "Lnet/minecraft/resources/ResourceKey;)V"
            )
    )
    public void broadcastChatMessage(ServerPlayer serverPlayer, PlayerChatMessage playerChatMessage, 
                                     ChatSender chatSender, ResourceKey<ChatType> resourceKey) {
        if (
                PreviewHijack.CONFIG.hideChangesFromPlayer &&
                PreviewHijack.ignoreNextPlayer != null &&
                PreviewHijack.ignoreNextPlayer.getFirst() == serverPlayer &&
                serverPlayer.getUUID() == chatSender.profileId()
        ) {
            PlayerChatMessage newPlayerChatMessage = PlayerChatMessage.unsigned(PreviewHijack.ignoreNextPlayer.getSecond());
            serverPlayer.sendChatMessage(newPlayerChatMessage, chatSender, resourceKey);
            PreviewHijack.ignoreNextPlayer = null;
        } else {
            serverPlayer.sendChatMessage(playerChatMessage, chatSender, resourceKey);
        }
    }
}
