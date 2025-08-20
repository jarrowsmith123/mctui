package com.jarrowsmith123.listeners;

import com.jarrowsmith123.MctuiPlugin;
import com.jarrowsmith123.network.HttpManager;
import com.jarrowsmith123.network.PlayerLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class PlayerListener implements Listener {

    private final MctuiPlugin plugin;
    private final HttpManager httpManager;

    public PlayerListener(MctuiPlugin plugin, HttpManager httpManager) {
        this.plugin = plugin;
        this.httpManager = httpManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();
        this.plugin.getPlayerLocations().put(uuid, location);
        getLogger().info("Added " + player.getName() + " to location cache.");
        // TODO send player join event to rust backend
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String world_name = player.getWorld().getName();

        PlayerLocation payload = new PlayerLocation(uuid, player.getName(), world_name, player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        httpManager.sendLocationUpdate(payload);
        this.plugin.getPlayerLocations().remove(uuid);
        getLogger().info("Removed " + player.getName() + " from location cache.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (event.getFrom().getBlockX() != Objects.requireNonNull(event.getTo()).getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            this.plugin.getPlayerLocations().put(event.getPlayer().getUniqueId(), event.getTo());
        }
    }
}
