package org.godfather.blocksumo.manager.runnables;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeRunnable extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getWorld("world").setTime(6000L);
        Bukkit.getWorld("world").setWeatherDuration(0);
    }
}
