package com.jarrowsmith123.listeners;

import com.jarrowsmith123.MctuiPlugin;
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

    public PlayerListener(MctuiPlugin plugin) { this.plugin = plugin; }

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
        // TODO send location to rust backend
        this.plugin.getPlayerLocations().remove(uuid);
        getLogger().info("Removed " + player.getName() + " from location cache.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (event.getFrom().getBlockX() != Objects.requireNonNull(event.getTo()).getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            this.plugin.getPlayerLocations().put(event.getPlayer().getUniqueId(), event.getTo());
            getLogger().info("Moved to " + event.getTo().toString());
        }
    }
}
