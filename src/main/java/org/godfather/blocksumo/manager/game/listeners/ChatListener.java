package org.godfather.blocksumo.manager.game.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;

public class ChatListener implements Listener {

    private final GameManager gameManager;

    public ChatListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();

        if (gameManager.getPlayerManager().getPlayersInGame().contains(p.getUniqueId()) || gameManager.getPhase() == GamePhases.END) {
            event.setFormat(ChatColor.GRAY + p.getName() + ": " + event.getMessage());
        } else if (gameManager.getPlayerManager().getSpectators().contains(p.getUniqueId())) {
            event.setCancelled(true);
            gameManager.getPlayerManager().getSpectators().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "[SPETTATORE] " + p.getName() + ": " + event.getMessage()));
        } else {
            event.setFormat(ChatColor.GRAY + p.getName() + ": " + event.getMessage());
        }
    }
}
