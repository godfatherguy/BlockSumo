package org.godfather.blocksumo.manager.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.Main;
import org.godfather.blocksumo.manager.game.players.PlayerManager;
import org.godfather.blocksumo.manager.runnables.Countdown;
import org.godfather.blocksumo.utils.Helper;

import java.io.File;

public class GameManager {

    private GamePhases phase;
    private Countdown countdown;
    private PlayerManager playerManager;
    private BlockManager blockManager;

    public GameManager() {
        this.countdown = new Countdown(this);
        this.playerManager = new PlayerManager(this);
        this.blockManager = new BlockManager(this);
        setPhase(GamePhases.LOADING);
    }

    public Countdown getCountdown() {
        return this.countdown;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public GamePhases getPhase() {
        return this.phase;
    }

    public void setPhase(GamePhases phase) {
        this.phase = phase;

        switch (getPhase()) {
            case LOADING:
                getPlayerManager().getPlayersInGame().forEach(uuid -> Bukkit.getPlayer(uuid).kickPlayer(ChatColor.RED + "Riavvio in corso..."));
                getPlayerManager().getSpectators().forEach(uuid -> Bukkit.getPlayer(uuid).kickPlayer(ChatColor.RED + "Riavvio in corso..."));
                resetWorld();

                setPhase(GamePhases.WAITING);
                break;
            case WAITING:
                break;
            case STARTING:
                getCountdown().start();
                break;
            case INGAME:
                getPlayerManager().getPlayersInGame().forEach(uuid -> Helper.sendTitle(Bukkit.getPlayer(uuid), ChatColor.RED + "" + ChatColor.BOLD + "BlockSumo", ChatColor.YELLOW + "Gioco iniziato!", 5, 40, 5));
                getPlayerManager().getPlayersInGame().forEach(uuid -> Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.LEVEL_UP, 1, 2));
                getPlayerManager().getPlayersInGame().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Partita iniziata!"));
                break;
            case END:
                new BukkitRunnable() {
                    @Override
                    public void run() {

                    }
                }.runTaskLater(Main.getInstance(), 100L);
                break;
        }
    }

    public void resetWorld() {
        getBlockManager().clear();
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
    }
}
