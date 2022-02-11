package org.godfather.blocksumo.manager.game.players;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.Main;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;
import org.godfather.blocksumo.utils.Helper;

public class DeathCountdown extends BukkitRunnable {

    @Override
    public void run() {
        if (gameManager.getPhase() != GamePhases.INGAME) {
            this.cancel();
            return;
        }
        if (time == 0) {
            this.cancel();
            gameManager.getPlayerManager().respawnPlayer(player);
            return;
        }
        Helper.sendTitle(player, ChatColor.RED + "SEI MORTO!", ChatColor.YELLOW + "Respawnerai in " + ChatColor.RED + String.valueOf(time) + ChatColor.YELLOW + " secondi.", 1, 20, 1);
        time--;
    }

    private Player player;
    private int time = 5;

    private GameManager gameManager;

    public DeathCountdown(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void start(Player p) {
        this.player = p;
        DeathCountdown task = new DeathCountdown(gameManager);
        task.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}
