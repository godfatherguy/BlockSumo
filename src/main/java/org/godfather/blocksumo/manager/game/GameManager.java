package org.godfather.blocksumo.manager.game;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.godfather.blocksumo.manager.game.players.PlayerManager;
import org.godfather.blocksumo.manager.runnables.Countdown;

import java.io.File;

public class GameManager {

    private GamePhases phase;
    private Countdown countdown;
    private PlayerManager playerManager;

    public GameManager() {
        this.countdown = new Countdown(this);
        this.playerManager = new PlayerManager(this);
        setPhase(GamePhases.LOADING);
    }

    public Countdown getCountdown() {
        return this.countdown;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public GamePhases getPhase() {
        return this.phase;
    }

    public void setPhase(GamePhases phase) {
        this.phase = phase;

        switch (getPhase()) {
            case LOADING:
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
                setPhase(GamePhases.WAITING);
                break;
            case WAITING:
                break;
            case STARTING:
                getCountdown().start();
                break;
            case INGAME:
                break;
            case END:
                break;
        }
    }
}
