package org.godfather.blocksumo;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;
import org.godfather.blocksumo.commands.Restart;
import org.godfather.blocksumo.manager.Tablist;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.items.PlayerNavigator;
import org.godfather.blocksumo.manager.game.listeners.ChatListener;
import org.godfather.blocksumo.manager.game.listeners.PlayerLoginListener;
import org.godfather.blocksumo.manager.game.listeners.PlayerWorldListener;
import org.godfather.blocksumo.manager.runnables.TimeRunnable;
import org.godfather.blocksumo.utils.ScoreboardAdapter;
import org.org.godfather.gboard.api.scoreboard.ScoreboardManager;
import org.org.godfather.gboard.api.scoreboard.ScoreboardProvider;
import org.org.godfather.gboard.nms.ScoreboardUtils;

public class Main extends JavaPlugin {

    private GameManager gameManager;
    private final org.org.godfather.gboard.api.scoreboard.adapter.ScoreboardAdapter scoreboardAdapter = org.org.godfather.gboard.api.scoreboard.adapter.ScoreboardAdapter.builder(this, new ScoreboardUtils()).build();


    public void onEnable() {
        saveDefaultConfig();
        gameManager = new GameManager(this);
        ScoreboardAdapter scoreboardAdapter = new ScoreboardAdapter(gameManager);

        new Tablist().runTaskTimer(this, 0L, 10L);
        new TimeRunnable().runTaskTimer(this, 0L, 20L);

        new ScoreboardManager(this, (ScoreboardProvider) scoreboardAdapter, 5).start();

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

    public org.org.godfather.gboard.api.scoreboard.adapter.ScoreboardAdapter getAdapter(){
        return scoreboardAdapter;
    }
}
