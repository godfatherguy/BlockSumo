package org.godfather.blocksumo;

import org.bukkit.plugin.java.JavaPlugin;
import org.godfather.blocksumo.manager.Tablist;
import org.godfather.blocksumo.manager.game.GameManager;

public class Main extends JavaPlugin {

    private static Main plugin;
    private GameManager gameManager;

    public void onEnable() {
        super.onEnable();
        plugin = this;
        this.gameManager = new GameManager();

        Tablist.start();
    }

    public void onDisable() {
        super.onDisable();

    }

    public static Main getInstance() {
        return plugin;
    }
}
