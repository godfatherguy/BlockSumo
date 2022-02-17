package org.godfather.blocksumo.manager.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.godfather.blocksumo.manager.game.items.Shears;
import org.godfather.blocksumo.manager.game.items.Wool;
import org.godfather.blocksumo.manager.runnables.Countdown;
import org.godfather.blocksumo.manager.runnables.VisibleRunnable;
import org.godfather.blocksumo.utils.Helper;

import java.util.function.Consumer;

public enum GamePhases {

    LOADING(gameManager -> {
        gameManager.getPlayerManager().getPlayersInGame().forEach(uuid -> Bukkit.getPlayer(uuid).kickPlayer(ChatColor.RED + "Riavvio in corso..."));
        gameManager.getPlayerManager().getSpectators().forEach(uuid -> Bukkit.getPlayer(uuid).kickPlayer(ChatColor.RED + "Riavvio in corso..."));
        gameManager.resetWorld();
        gameManager.restart();

    }), WAITING(gameManager -> {

    }), STARTING(gameManager -> {
        new Countdown(gameManager).runTaskTimer(gameManager.getInstance(), 10L, 20L);

    }), INGAME(gameManager -> {
        new VisibleRunnable(gameManager).runTaskTimer(gameManager.getInstance(), 0L, 10L);
        gameManager.getPlayerManager().getPlayersInGame().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            Helper.sendTitle(p, ChatColor.RED + "" + ChatColor.BOLD + "BlockSumo", ChatColor.YELLOW + "Gioco iniziato!", 5, 40, 5);
            p.playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.LEVEL_UP, 1, 2);
            p.sendMessage(ChatColor.GREEN + "Partita iniziata!");
            p.teleport(gameManager.getMap().getSpawnLocation());
            p.getInventory().setItem(0, Shears.getItem());
            p.getInventory().setItem(3, Wool.getItem());
        });

    }), END(GameManager::end);

    public Consumer<GameManager> getConsumer() {
        return consumer;
    }

    private final Consumer<GameManager> consumer;

    GamePhases(Consumer<GameManager> consumer) {
        this.consumer = consumer;
    }
}
