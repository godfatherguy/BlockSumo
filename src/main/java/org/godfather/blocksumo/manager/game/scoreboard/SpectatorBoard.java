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
import org.godfather.blocksumo.manager.game.players.GamePlayer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SpectatorBoard extends BukkitRunnable {

    @Override
    public void run() {
        if (scoreboardManager.getGameManager().getPhase() != GamePhases.INGAME && scoreboardManager.getGameManager().getPhase() != GamePhases.END) {
            cancel();
            return;
        }
        scoreboardManager.getGameManager().getPlayerManager().getSpectators().forEach(uuid -> setScoreboard(Bukkit.getPlayer(uuid)));
    }

    private final ScoreboardManager scoreboardManager;

    public SpectatorBoard(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    private void setScoreboard(Player p) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "dummy");
        obj.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "BLOCK SUMO");
        scoreboardManager.replaceScore(obj, 12, ChatColor.GRAY + format.format(new Date()));
        scoreboardManager.replaceScore(obj, 11, "    ");
        scoreboardManager.replaceScore(obj, 10, ChatColor.GRAY + "Sei uno spettatore.");
        scoreboardManager.replaceScore(obj, 9, "   ");
        scoreboardManager.replaceScore(obj, 8, ChatColor.WHITE + "Giocatori in vita: " + ChatColor.AQUA + scoreboardManager.getGameManager().getPlayerManager().getPlayersInGame().size());
        scoreboardManager.replaceScore(obj, 7, ChatColor.WHITE + "Mappa: " + ChatColor.AQUA + "Java");
        scoreboardManager.replaceScore(obj, 6, " ");
        scoreboardManager.replaceScore(obj, 5, ChatColor.YELLOW + "play.coralmc.it");

        if (obj.getDisplaySlot() != DisplaySlot.SIDEBAR) obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.getUniqueId() == p.getUniqueId()) {
                Team giocatore = board.registerNewTeam("bGiocatore");
                giocatore.setPrefix(ChatColor.GRAY + "" + ChatColor.ITALIC + "");
                giocatore.addEntry(players.getName());

            } else {
                GamePlayer gameprofile = scoreboardManager.getGameManager().getPlayerManager().getProfile(players);
                Team giocatore = board.registerNewTeam("a" + players.getName());
                giocatore.setPrefix(ChatColor.GRAY + "");
                giocatore.setSuffix(" " + gameprofile.getLifeColor() + gameprofile.getLives() + ChatColor.DARK_RED + "❤");
                giocatore.addEntry(players.getName());
            }
        }

        p.setScoreboard(board);
    }
}
