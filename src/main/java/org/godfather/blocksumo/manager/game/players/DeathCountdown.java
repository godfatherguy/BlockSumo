package org.godfather.blocksumo.manager.game.players;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;
import org.godfather.blocksumo.utils.Helper;

public class DeathCountdown extends BukkitRunnable {

    @Override
    public void run() {
        if (gameManager.getPhase() != GamePhases.INGAME) {
            cancel();
            return;
        }
        if (time == 0) {
            cancel();
            gameManager.getPlayerManager().respawnPlayer(player);
            return;
        }
        Helper.sendTitle(player, ChatColor.RED + "SEI MORTO!", ChatColor.YELLOW + "Respawnerai in " + ChatColor.RED + String.valueOf(time) + ChatColor.YELLOW + " secondi.", 1, 20, 1);
        time--;
    }

    private final Player player;
    private int time = 5;

    private final GameManager gameManager;

    public DeathCountdown(GameManager gameManager, Player p) {
        this.gameManager = gameManager;
        player = p;
    }
}
