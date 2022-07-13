package ca.fxco.preview_hijack.utils;

import ca.fxco.preview_hijack.PreviewHijack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class DecoratorHandler {

    public static ChatDecorator getDecorator() {
        return (serverPlayer, component) -> {
            String text = component.getString();
            if (PreviewHijack.CONFIG.conditions != null && PreviewHijack.CONFIG.actions != null) {
                boolean isValid = true;
                for (FullCondition condition : PreviewHijack.CONFIG.conditions)
                    if (!condition.getCheck().checkString(text, condition.getArgs())) {
                        isValid = false;
                        break;
                    }
                if (isValid) {
                    for (FullAction action : PreviewHijack.CONFIG.actions)
                        text = action.getStep().formatString(text, action.getArgs());
                    if (PreviewHijack.CONFIG.hideChangesFromPlayer)
                        PreviewHijack.ignoreNextPlayer = new Pair<>(serverPlayer, component);
                }
            }
            return CompletableFuture.completedFuture(Component.literal(text));
        };
    }
}
