package com.jarrowsmith123;

import com.jarrowsmith123.listeners.PlayerListener;
import com.jarrowsmith123.network.HttpManager;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MctuiPlugin extends JavaPlugin {
    private final Map<UUID, Location> playerLocations = new HashMap<>();

    @Override
    public void onEnable() {
        HttpManager httpManager = new HttpManager(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this, httpManager), this);
        getLogger().info("Player listeners have been registered."); // Good to add a confirmation
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    public Map<UUID, Location> getPlayerLocations() {
        return this.playerLocations;
    }
}