package ca.fxco.preview_hijack.mixin;

import ca.fxco.preview_hijack.HijackCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class CommandsMixin {

    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;


    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void onInit(Commands.CommandSelection commandSelection,
                        CommandBuildContext commandBuildContext, CallbackInfo ci) {
        HijackCommand.register(this.dispatcher);
    }
}
