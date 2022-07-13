package ca.fxco.preview_hijack;

import ca.fxco.preview_hijack.config.ConfigManager;
import ca.fxco.preview_hijack.config.PHConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreviewHijack implements ModInitializer {

    public static final String MODID = "preview_hijack";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Nullable
    public static Pair<ServerPlayer, Component> ignoreNextPlayer;

    private static final ConfigManager configManager = new ConfigManager(MODID);
    public static PHConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = configManager.loadConfig();
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
