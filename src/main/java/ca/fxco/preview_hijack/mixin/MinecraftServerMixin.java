package ca.fxco.preview_hijack.mixin;

import ca.fxco.preview_hijack.PreviewHijack;
import ca.fxco.preview_hijack.utils.DecoratorHandler;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftServer.class, priority = 2200)
public class MinecraftServerMixin {


    @Inject(
            method = "getChatDecorator",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getChatDecorator(CallbackInfoReturnable<ChatDecorator> cir) {
        if (PreviewHijack.CONFIG.enabled) cir.setReturnValue(DecoratorHandler.getDecorator());
    }
}
