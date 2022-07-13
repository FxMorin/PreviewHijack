package ca.fxco.preview_hijack;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class HijackCommand {

    // I wrote this in 10m, enjoy xD
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("hijack")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                        .executes(commandContext -> {
                            commandContext.getSource().sendSuccess(
                                    Component.literal(
                                            "Preview Hijacking is currently: " +
                                                    (PreviewHijack.CONFIG.enabled ? "enabled" : "disabled")
                                    ), false);
                            return 1;
                        })
                        .then(Commands.literal("toggle").executes(commandContext -> {
                            PreviewHijack.CONFIG.enabled = !PreviewHijack.CONFIG.enabled;
                            commandContext.getSource().sendSuccess(
                                    Component.literal(
                                            "Preview Hijacking is now: " +
                                                    (PreviewHijack.CONFIG.enabled ? "enabled" : "disabled")
                                    ), false);
                            PreviewHijack.getConfigManager().saveConfig(PreviewHijack.CONFIG);
                            return 1;
                        }))
                        .then(Commands.literal("hideChangesFromPlayer").executes(commandContext -> {
                            PreviewHijack.CONFIG.hideChangesFromPlayer = !PreviewHijack.CONFIG.hideChangesFromPlayer;
                            commandContext.getSource().sendSuccess(
                                    Component.literal(
                                            "hideChangesFromPlayer is now: " +
                                                    (PreviewHijack.CONFIG.hideChangesFromPlayer ? "enabled":"disabled")
                                    ), false);
                            PreviewHijack.getConfigManager().saveConfig(PreviewHijack.CONFIG);
                            return 1;
                        }))
                        .then(Commands.literal("reload").executes(commandContext -> {
                            PreviewHijack.CONFIG = PreviewHijack.getConfigManager().loadConfig();
                            commandContext.getSource().sendSuccess(Component.literal(
                                    "Config has been reloaded"
                            ), false);
                            return 1;
                        }))
        );
    }
}
