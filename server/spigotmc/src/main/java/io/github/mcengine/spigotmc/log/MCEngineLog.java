package io.github.mcengine.spigotmc.log;

import io.github.mcengine.api.MCEngineApiUtil;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the MCEngineLog plugin.
 * This class extends JavaPlugin and manages the plugin lifecycle.
 */
public class MCEngineLog extends JavaPlugin {

    private final MCEngineApiUtil mcengineApiUtil = new MCEngineApiUtil();

    /**
     * Called when the plugin is enabled.
     * This method is executed when the server starts or when the plugin is reloaded.
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        mcengineApiUtil.saveResourceIfNotExists(this, "logs.db");
    }

    /**
     * Called when the plugin is disabled.
     * This method is executed when the server stops or when the plugin is reloaded.
     */
    @Override
    public void onDisable() {}
}