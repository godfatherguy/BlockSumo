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

public class IngameBoard extends BukkitRunnable {

    @Override
    public void run() {
        if (scoreboardManager.getGameManager().getPhase() != GamePhases.INGAME && scoreboardManager.getGameManager().getPhase() != GamePhases.END) {
            cancel();
            return;
        }
        scoreboardManager.getGameManager().getPlayerManager().getPlayersInGame().forEach(uuid -> setScoreboard(Bukkit.getPlayer(uuid)));
    }

    private final ScoreboardManager scoreboardManager;

    public IngameBoard(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    private void setScoreboard(Player p) {
        GamePlayer profile = scoreboardManager.getGameManager().getPlayerManager().getProfile(p);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "dummy");
        obj.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "BLOCK SUMO");
        scoreboardManager.replaceScore(obj, 14, ChatColor.GRAY + format.format(new Date()));
        scoreboardManager.replaceScore(obj, 13, "    ");
        scoreboardManager.replaceScore(obj, 12, ChatColor.WHITE + "Vite: " + profile.getLifeColor() + profile.getLives() + ChatColor.DARK_RED + "❤");
        scoreboardManager.replaceScore(obj, 11, ChatColor.WHITE + "Uccisioni: " + ChatColor.AQUA + profile.getKills());
        scoreboardManager.replaceScore(obj, 10, "   ");
        scoreboardManager.replaceScore(obj, 9, ChatColor.WHITE + "Giocatori in vita: " + ChatColor.AQUA + scoreboardManager.getGameManager().getPlayerManager().getPlayersInGame().size());
        scoreboardManager.replaceScore(obj, 8, "  ");
        scoreboardManager.replaceScore(obj, 7, ChatColor.WHITE + "Mappa: " + ChatColor.AQUA + "Java");
        scoreboardManager.replaceScore(obj, 6, " ");
        scoreboardManager.replaceScore(obj, 5, ChatColor.YELLOW + "play.coralmc.it");

        if (obj.getDisplaySlot() != DisplaySlot.SIDEBAR) obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (Player players : Bukkit.getOnlinePlayers()) {
            GamePlayer gameprofile = scoreboardManager.getGameManager().getPlayerManager().getProfile(players);
            Team giocatore = board.registerNewTeam(players.getName());
            giocatore.setPrefix(ChatColor.GRAY + "");
            giocatore.setSuffix(gameprofile.getLifeColor() + String.valueOf(gameprofile.getLives()) + ChatColor.DARK_RED + "❤");
            giocatore.addEntry(players.getName());
        }

        p.setScoreboard(board);
    }
}
