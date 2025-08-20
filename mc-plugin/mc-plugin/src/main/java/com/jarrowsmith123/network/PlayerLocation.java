package com.jarrowsmith123.network;

import java.util.UUID;

public class PlayerLocation {

    private UUID player_uuid;
    private String player_name;
    private String world_name;
    private int x;
    private int y;
    private int z;

    public PlayerLocation(UUID player_uuid, String player_name, String world_name, int x, int y, int z) {
        this.player_uuid = player_uuid;
        this.player_name = player_name;
        this.world_name = world_name;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
