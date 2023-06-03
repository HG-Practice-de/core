package de.hg_practice.core;

import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    private static Core instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // plugin startup logic
    }

    @Override
    public void onDisable() {
        // plugin shutdown logic
    }

    public static Core getInstance() {
        return instance;
    }
}
