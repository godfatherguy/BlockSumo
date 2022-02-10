package org.godfather.blocksumo.manager.runnables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.Main;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;
import org.godfather.blocksumo.utils.Helper;

public class Countdown extends BukkitRunnable {

    @Override
    public void run() {
        if (gameManager.getPhase() != GamePhases.STARTING) {
            this.cancel();
            return;
        }
        if (time == 0) {
            this.cancel();
            gameManager.setPhase(GamePhases.INGAME);
            return;
        }
        if (time <= 5) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.YELLOW + "La partita inizia tra " + ChatColor.RED + time + ChatColor.YELLOW + " secondi!");
                Helper.sendTitle(p, ChatColor.RED + String.valueOf(time), "", 1, 20, 1);
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
            }
        } else if (time == 10) {
            Bukkit.getOnlinePlayers().forEach(p -> Helper.sendTitle(p, ChatColor.GOLD + "10", "", 1, 20, 1));
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(ChatColor.YELLOW + "La partita inizia tra " + ChatColor.GOLD + "10" + ChatColor.YELLOW + " secondi!"));
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1));
        } else if (time % 10 == 0) {
            Bukkit.getOnlinePlayers().forEach(p -> Helper.sendTitle(p, ChatColor.YELLOW + String.valueOf(time), "", 1, 20, 1));
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(ChatColor.YELLOW + "La partita inizia tra " + time + " secondi!"));
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1));
        }

        time--;
    }

    private GameManager gameManager;

    public Countdown(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private int time = 30;

    public void start() {
        Countdown task = new Countdown(gameManager);
        task.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}
