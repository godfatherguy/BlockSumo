package org.godfather.blocksumo.manager.runnables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;
import org.godfather.blocksumo.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Countdown extends BukkitRunnable {

    @Override
    public void run() {
        gameManager.setTimecountdown(time);
        if (gameManager.getPhase() != GamePhases.STARTING) {
            cancel();
            return;
        }
        if (time == 0) {
            cancel();
            gameManager.setPhase(GamePhases.INGAME);
            return;
        } else if (time <= 5 || time == 10 || time % 10 == 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.YELLOW + "La partita inizia tra " + timeColor(time) + time + ChatColor.YELLOW + " secondi!");
                Helper.sendTitle(p, timeColor(time) + String.valueOf(time), "", 0, 21, 0);
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1, 1);
            }
        }
        time--;
    }

    private final GameManager gameManager;

    public Countdown(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private int time = 30;

    private ChatColor timeColor(int time) {
        if (time <= 5) return ChatColor.RED;
        else if (time == 10) return ChatColor.GOLD;
        else return ChatColor.YELLOW;
    }
}
