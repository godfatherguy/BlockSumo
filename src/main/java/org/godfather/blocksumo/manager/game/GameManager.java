package org.godfather.blocksumo.manager.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.Main;
import org.godfather.blocksumo.manager.game.items.Shears;
import org.godfather.blocksumo.manager.game.items.Wool;
import org.godfather.blocksumo.manager.game.players.PlayerManager;
import org.godfather.blocksumo.manager.game.scoreboard.IngameBoard;
import org.godfather.blocksumo.manager.game.scoreboard.ScoreboardManager;
import org.godfather.blocksumo.manager.game.scoreboard.SpectatorBoard;
import org.godfather.blocksumo.manager.game.scoreboard.WaitingBoard;
import org.godfather.blocksumo.manager.runnables.Countdown;
import org.godfather.blocksumo.utils.Helper;

import java.io.File;
import java.util.*;

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
                new SpectatorBoard(scoreboardManager).runTaskTimer(plugin, 0L, 5L);
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
                end();
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        setPhase(GamePhases.LOADING);
                    }
                }.runTaskLater(plugin, 120L);
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

    public void end() {
        for (UUID uuid : playerManager.getPlayersInGame()) {
            Player p = Bukkit.getPlayer(uuid);
            Helper.sendTitle(p, ChatColor.GOLD + "" + ChatColor.BOLD + "VITTORIA!", "", 10, 60, 10);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);

            Bukkit.getOnlinePlayers().forEach(players -> {
                players.sendMessage(" ");
                players.sendMessage(Helper.centerText(ChatColor.AQUA + "" + ChatColor.BOLD + "BLOCK SUMO"));
                players.sendMessage(" ");
                players.sendMessage(Helper.centerText(ChatColor.GOLD + "Vincitore: " + ChatColor.GRAY + p.getName()));
                players.sendMessage(" ");
                Set<Player> possibilities = new HashSet<>(Bukkit.getOnlinePlayers());
                int counter=0;
                do{
                    int maxkills=0;
                    Player killer=p;
                    for(Player player : possibilities){
                        if(playerManager.getProfile(player).getKills() > maxkills){
                            maxkills=playerManager.getProfile(player).getKills();
                            killer=player;
                        }
                    }
                    players.sendMessage(Helper.centerText(ChatColor.RED + String.valueOf(counter+1) + "° killer: " + ChatColor.GRAY + killer.getName() + ChatColor.RED + "- " + maxkills));
                    possibilities.remove(killer);
                    counter++;
                }while(counter<2);
                players.sendMessage(" ");
            });
        }
        for (UUID uuid : playerManager.getSpectators()) {
            Player p = Bukkit.getPlayer(uuid);
            if (playerManager.getProfile(p).getLives() == 5) {
                Helper.sendTitle(p, ChatColor.BLUE + "" + ChatColor.BOLD + "FINE PARTITA", "", 10, 60, 10);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                Helper.sendTitle(p, ChatColor.RED + "" + ChatColor.BOLD + "SCONFITTA", "", 10, 60, 10);
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 2);
            }
        }
    }
}
