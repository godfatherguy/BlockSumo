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
        Bukkit.getOnlinePlayers().forEach(p -> setScoreboard(p, time));
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

    private void setScoreboard(Player p, int time) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "dummy");
        obj.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "BLOCK SUMO");
        gameManager.getScoreboard().replaceScore(obj, 15, ChatColor.GRAY + format.format(new Date()));
        gameManager.getScoreboard().replaceScore(obj, 14, "    ");
        gameManager.getScoreboard().replaceScore(obj, 13, ChatColor.WHITE + "Mappa: " + ChatColor.AQUA + "Java");
        gameManager.getScoreboard().replaceScore(obj, 12, ChatColor.WHITE + "Giocatori: " + ChatColor.AQUA + gameManager.getScoreboard().getGameManager().getPlayerManager().getPlayersInGame().size() + "/" + gameManager.getScoreboard().getGameManager().getPlayerManager().getMaxPlayers());
        gameManager.getScoreboard().replaceScore(obj, 11, "   ");
        gameManager.getScoreboard().replaceScore(obj, 10, ChatColor.AQUA + "Inizio in " + ChatColor.WHITE + time + "s");
        gameManager.getScoreboard().replaceScore(obj, 9, "  ");
        gameManager.getScoreboard().replaceScore(obj, 8, ChatColor.WHITE + "Modalità: " + ChatColor.AQUA + "Sumo SOLO");
        gameManager.getScoreboard().replaceScore(obj, 7, ChatColor.WHITE + "Versione: " + ChatColor.AQUA + "1.0");
        gameManager.getScoreboard().replaceScore(obj, 6, " ");
        gameManager.getScoreboard().replaceScore(obj, 5, ChatColor.YELLOW + "play.coralmc.it");

        if (obj.getDisplaySlot() != DisplaySlot.SIDEBAR) obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team giocatore = board.registerNewTeam("giocatore");
        giocatore.setPrefix(ChatColor.GRAY + "");
        for (Player players : Bukkit.getOnlinePlayers()) {
            giocatore.addEntry(players.getName());
        }

        p.setScoreboard(board);
    }
}
