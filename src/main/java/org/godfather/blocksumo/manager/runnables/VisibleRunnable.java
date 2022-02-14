package org.godfather.blocksumo.manager.runnables;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;

import java.util.stream.Collectors;

public class VisibleRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (gameManager.getPhase() != GamePhases.INGAME) {
            cancel();
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {

            for (Player player : Bukkit.getOnlinePlayers().stream().filter(player -> p.getUniqueId() != player.getUniqueId()).collect(Collectors.toList())) {

                if (gameManager.getPlayerManager().getSpectators().contains(player.getUniqueId()) || player.getGameMode() == GameMode.ADVENTURE)
                    p.hidePlayer(player);
                else p.showPlayer(player);
            }
        }
    }

    private final GameManager gameManager;

    public VisibleRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
