package org.godfather.blocksumo;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.godfather.blocksumo.manager.Tablist;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.map.GameMap;
import org.godfather.blocksumo.manager.map.LocalGameMap;

import java.io.File;

public class Main extends JavaPlugin {

    private static Main plugin;
    private GameManager gameManager;
    private GameMap map;
    private static World actualWorld;

    public void onEnable() {
        super.onEnable();
        plugin = this;
        saveDefaultConfig();

        getDataFolder().mkdirs();
        File gameMapsFolder = new File(getDataFolder(), "gameMaps");
        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }
        this.gameManager = new GameManager();
        map = new LocalGameMap(gameMapsFolder, "Mappa1", true);
        actualWorld = map.getWorld();

        Tablist.start();
    }

    public void onDisable() {
        super.onDisable();

        for (World world : Bukkit.getWorlds()) {
            File[] files = new File(world.getWorldFolder().getAbsolutePath() + "/playerdata/").listFiles();
            if (files == null) continue;
            for (File file : files) {
                file.delete();
            }
        }
        for (World world : Bukkit.getWorlds()) {
            File[] files = new File(world.getWorldFolder().getAbsolutePath() + "/stats/").listFiles();
            if (files == null) continue;
            for (File file : files) {
                file.delete();
            }
        }

        map.unload();
        actualWorld = null;
    }

    public static Main getInstance() {
        return plugin;
    }

    public static World getActualWorld() {
        return actualWorld;
    }
}
