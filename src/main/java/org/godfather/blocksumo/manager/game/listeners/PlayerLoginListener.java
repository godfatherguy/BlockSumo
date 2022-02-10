package org.godfather.blocksumo.manager.game.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;
import org.godfather.blocksumo.utils.Helper;

public class PlayerLoginListener implements Listener {

    private GameManager gameManager;

    public PlayerLoginListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        switch (gameManager.getPhase()) {
            case LOADING:
            case END:
                p.kickPlayer(ChatColor.RED + "Caricamento in corso... Aspetta!");
                break;
            case WAITING:
                gameManager.getPlayerManager().addPlayerToGame(p);
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GRAY + p.getName()
                        + ChatColor.YELLOW + " è entrato (" + ChatColor.AQUA + gameManager.getPlayerManager().getPlayersInGame().size() + ChatColor.YELLOW + "/"
                        + ChatColor.AQUA + gameManager.getPlayerManager().getMaxPlayers() + ChatColor.YELLOW + ")!"));
                if (gameManager.getPlayerManager().getPlayersInGame().size() >= gameManager.getPlayerManager().getRequiredPlayers())
                    gameManager.setPhase(GamePhases.STARTING);
                break;
            case STARTING:
                if (gameManager.getPlayerManager().getPlayersInGame().size() == gameManager.getPlayerManager().getMaxPlayers())
                    p.kickPlayer(ChatColor.RED + "Il gioco è pieno. Aspetta!");
                else {
                    gameManager.getPlayerManager().addPlayerToGame(p);
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GRAY + p.getName()
                            + ChatColor.YELLOW + " è entrato (" + ChatColor.AQUA + gameManager.getPlayerManager().getPlayersInGame().size() + ChatColor.YELLOW + "/"
                            + ChatColor.AQUA + gameManager.getPlayerManager().getMaxPlayers() + ChatColor.YELLOW + ")!"));
                }
                break;
            case INGAME:
                gameManager.getPlayerManager().addSpectator(p);
                p.sendMessage(ChatColor.GRAY + "Sei entrato come spettatore!");
                break;
        }
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        switch (gameManager.getPhase()) {
            case WAITING:
                gameManager.getPlayerManager().removePlayerFromGame(p);
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GRAY + p.getName() + ChatColor.YELLOW + " è uscito!"));
                break;
            case STARTING:
                gameManager.getPlayerManager().removePlayerFromGame(p);
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GRAY + p.getName() + ChatColor.YELLOW + " è uscito!"));
                if (gameManager.getPlayerManager().getPlayersInGame().size() < gameManager.getPlayerManager().getRequiredPlayers()) {
                    gameManager.setPhase(GamePhases.WAITING);
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "Aspettando altri giocatori..."));
                }
                break;
            case INGAME:
            case END:
                if (gameManager.getPlayerManager().getSpectators().contains(p)) {
                    gameManager.getPlayerManager().removeSpectator(p);
                } else if (gameManager.getPlayerManager().getPlayersInGame().contains(p)) {
                    gameManager.getPlayerManager().removePlayerFromGame(p);
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GRAY + p.getName() + ChatColor.YELLOW + " è uscito dalla partita."));
                    Bukkit.getOnlinePlayers().forEach(player -> Helper.sendTitle(player, ChatColor.RED + "Aspettando altri giocatori...", "", 5, 20, 5));
                    if (gameManager.getPlayerManager().getPlayersInGame().size() == 0) {
                        //todo resettare il game;
                    }
                }
                break;
        }
        event.setQuitMessage(null);
    }
}
