package org.godfather.blocksumo.manager.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.godfather.blocksumo.Main;
import org.godfather.blocksumo.manager.game.items.Shears;
import org.godfather.blocksumo.manager.game.items.Wool;
import org.godfather.blocksumo.manager.game.players.PlayerManager;
import org.godfather.blocksumo.manager.game.scoreboard.IngameBoard;
import org.godfather.blocksumo.manager.game.scoreboard.ScoreboardManager;
import org.godfather.blocksumo.manager.game.scoreboard.WaitingBoard;
import org.godfather.blocksumo.manager.runnables.Countdown;
import org.godfather.blocksumo.utils.Helper;

import java.io.File;

public class GameManager {

    private GamePhases phase;
    private final Main plugin;
    private final PlayerManager playerManager;
    private final BlockManager blockManager;
    private final MapManager mapManager;
    private final ScoreboardManager scoreboardManager;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        playerManager = new PlayerManager(this);
        blockManager = new BlockManager(this);
        mapManager = new MapManager();
        this.scoreboardManager = new ScoreboardManager(this);
        setPhase(GamePhases.LOADING);
    }

    public Main getInstance() {
        return plugin;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public ScoreboardManager getScoreboard() {
        return scoreboardManager;
    }

    public MapManager getMap() {
        return mapManager;
    }

    public GamePhases getPhase() {
        return phase;
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
                new WaitingBoard(scoreboardManager).runTaskTimer(plugin, 0L, 5L);
                break;
            case STARTING:
                new Countdown(this).runTaskTimer(plugin, 10L, 20L);
                break;
            case INGAME:
                new IngameBoard(scoreboardManager).runTaskTimer(plugin, 0L, 5L);
                getPlayerManager().getPlayersInGame().forEach(uuid -> {
                    Player p = Bukkit.getPlayer(uuid);
                    Helper.sendTitle(p, ChatColor.RED + "" + ChatColor.BOLD + "BlockSumo", ChatColor.YELLOW + "Gioco iniziato!", 5, 40, 5);
                    p.playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.LEVEL_UP, 1, 2);
                    p.sendMessage(ChatColor.GREEN + "Partita iniziata!");
                    p.teleport(mapManager.getSpawnLocation());
                    p.getInventory().setItem(0, Shears.getItem());
                    p.getInventory().setItem(3, Wool.getItem());

                });
                break;
            case END:
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
