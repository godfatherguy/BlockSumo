package org.godfather.blocksumo.manager.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MapManager {

    public Location getLobbyLocation() {
        return new Location(Bukkit.getWorld("world"), 0.5, 51, 0.5, (float) 0.0, (float) 0.0);
    }

    public Location getSpawnLocation() {
        return new Location(Bukkit.getWorld("world"), 0.5, 14, 0.5, (float) 90.0, (float) 0.0);
    }

    public Location getSpectLocation() {
        return new Location(Bukkit.getWorld("world"), 0.5, 40, 0.5, (float) -180.0, (float) 0.0);
    }

    public Location getBuildZone1() {
        return new Location(Bukkit.getWorld("world"), -30.0, 0.0, -30);
    }

    public Location getBuildZone2() {
        return new Location(Bukkit.getWorld("world"), 30, 41.0, 30);
    }

    public Location getSafeZone1() {
        return new Location(Bukkit.getWorld("world"), -1.5, 14, -1.5);
    }

    public Location getSafeZone2() {
        return new Location(Bukkit.getWorld("world"), 2.5, 41.0, 2.5);
    }
}
