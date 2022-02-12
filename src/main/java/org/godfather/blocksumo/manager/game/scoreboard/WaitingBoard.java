package org.godfather.blocksumo.manager.game.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.godfather.blocksumo.manager.game.GamePhases;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaitingBoard extends BukkitRunnable {

    @Override
    public void run() {
        if (scoreboardManager.getGameManager().getPhase() != GamePhases.WAITING) {
            cancel();
            return;
        }
        Bukkit.getOnlinePlayers().forEach(this::setScoreboard);
    }

    private final ScoreboardManager scoreboardManager;

    public WaitingBoard(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    private void setScoreboard(Player p) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "dummy");
        obj.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "BLOCK SUMO");
        scoreboardManager.replaceScore(obj, 15, ChatColor.GRAY + format.format(new Date()));
        scoreboardManager.replaceScore(obj, 14, "    ");
        scoreboardManager.replaceScore(obj, 13, ChatColor.WHITE + "Mappa: " + ChatColor.AQUA + "Java");
        scoreboardManager.replaceScore(obj, 12, ChatColor.WHITE + "Giocatori: " + ChatColor.AQUA + scoreboardManager.getGameManager().getPlayerManager().getPlayersInGame().size() + "/" + scoreboardManager.getGameManager().getPlayerManager().getMaxPlayers());
        scoreboardManager.replaceScore(obj, 11, "   ");
        scoreboardManager.replaceScore(obj, 10, ChatColor.AQUA + "In attesa...");
        scoreboardManager.replaceScore(obj, 9, "  ");
        scoreboardManager.replaceScore(obj, 8, ChatColor.WHITE + "Modalità: " + ChatColor.AQUA + "Sumo SOLO");
        scoreboardManager.replaceScore(obj, 7, ChatColor.WHITE + "Versione: " + ChatColor.AQUA + "1.0");
        scoreboardManager.replaceScore(obj, 6, " ");
        scoreboardManager.replaceScore(obj, 5, ChatColor.YELLOW + "play.coralmc.it");

        if (obj.getDisplaySlot() != DisplaySlot.SIDEBAR) obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team giocatore = board.registerNewTeam("giocatore");
        giocatore.setPrefix(ChatColor.GRAY + "");
        for (Player players : Bukkit.getOnlinePlayers()) {
            giocatore.addEntry(players.getName());
        }

        p.setScoreboard(board);
    }
}
