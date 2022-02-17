package org.godfather.blocksumo.manager.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.Main;
import org.godfather.blocksumo.manager.game.players.PlayerManager;
import org.godfather.blocksumo.utils.Helper;

import java.io.File;
import java.util.*;

public class GameManager {

    private GamePhases phase;
    private final Main plugin;
    private final PlayerManager playerManager;
    private final BlockManager blockManager;
    private final MapManager mapManager;
    private int timecountdown;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        playerManager = new PlayerManager(this);
        blockManager = new BlockManager();
        mapManager = new MapManager();
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

    public int getTimecountdown() {
        return timecountdown;
    }

    public void setTimecountdown(int time) {
        timecountdown = time;
    }

    public MapManager getMap() {
        return mapManager;
    }

    public GamePhases getPhase() {
        return phase;
    }

    public void setPhase(GamePhases phase) {
        this.phase = phase;
        phase.getConsumer().accept(this);
        if (phase == GamePhases.LOADING) {
            setPhase(GamePhases.WAITING);
        } else if (phase == GamePhases.END) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    setPhase(GamePhases.LOADING);
                }
            }.runTaskLater(plugin, 120L);
        }
    }

    public void resetWorld() {
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
                int counter = 0;
                do {
                    int maxkills = 0;
                    Player killer = p;
                    for (Player player : possibilities) {
                        if (playerManager.getProfile(player).getKills() > maxkills) {
                            maxkills = playerManager.getProfile(player).getKills();
                            killer = player;
                        }
                    }
                    players.sendMessage(Helper.centerText(ChatColor.RED + String.valueOf(counter + 1) + "°: " + ChatColor.GRAY + killer.getName() + ChatColor.RED + " - " + ChatColor.GRAY + maxkills + " Uccisioni"));
                    possibilities.remove(killer);
                    counter++;
                } while (counter < 3);
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

    public void restart() {
        playerManager.restart();
        blockManager.clear();
    }
}
