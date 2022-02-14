package org.godfather.blocksumo;

import org.bukkit.plugin.java.JavaPlugin;
import org.godfather.blocksumo.commands.Restart;
import org.godfather.blocksumo.manager.Tablist;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.items.PlayerNavigator;
import org.godfather.blocksumo.manager.game.listeners.ChatListener;
import org.godfather.blocksumo.manager.game.listeners.PlayerLoginListener;
import org.godfather.blocksumo.manager.game.listeners.PlayerWorldListener;
import org.godfather.blocksumo.manager.runnables.TimeRunnable;

public class Main extends JavaPlugin {

    private GameManager gameManager;

    public void onEnable() {
        saveDefaultConfig();
        gameManager = new GameManager(this);

        new Tablist().runTaskTimer(this, 0L, 10L);
        new TimeRunnable().runTaskTimer(this, 0L, 20L);

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerWorldListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerNavigator(gameManager), this);

        getCommand("restart").setExecutor(new Restart(gameManager));
    }

    public void onDisable() {
        gameManager.resetWorld();
        gameManager.restart();
    }
}
