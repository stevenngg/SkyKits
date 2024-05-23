package dev.sky.main;

import dev.sky.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyKits extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // ConfigManager startup logic
        configManager = new ConfigManager(this);
        configManager.loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        configManager.saveConfig();
    }

    public ConfigManager getManager() {
        return configManager;
    }
}
