package org.godfather.blocksumo;

import org.bukkit.plugin.java.JavaPlugin;
import org.godfather.blocksumo.manager.Tablist;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.listeners.PlayerLoginListener;
import org.godfather.blocksumo.manager.game.listeners.PlayerWorldListener;

public class Main extends JavaPlugin {

    private GameManager gameManager;

    public void onEnable() {
        saveDefaultConfig();
        gameManager = new GameManager(this);

        Tablist tablist = new Tablist();
        tablist.runTaskTimer(this, 0L, 10L);

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerWorldListener(gameManager), this);
    }
}
