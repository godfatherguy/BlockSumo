package org.godfather.blocksumo.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;
import org.godfather.blocksumo.manager.game.players.GamePlayer;
import org.org.godfather.gboard.api.scoreboard.ScoreboardProvider;
import org.org.godfather.gboard.api.scoreboard.board.line.ScoreboardLine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreboardAdapter implements ScoreboardProvider {

    private final GameManager gameManager;

    public ScoreboardAdapter(GameManager gameManager) {
        super();
        this.gameManager = gameManager;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.AQUA + "" + ChatColor.BOLD + "BLOCK SUMO";
    }

    @Override
    public List<ScoreboardLine> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");

        lines.add(ChatColor.GRAY + date.format(new Date()));
        lines.add("              ");

        switch (gameManager.getPhase()) {
            case WAITING:
            case STARTING:
                lines.add(ChatColor.WHITE + "Mappa: " + ChatColor.AQUA + "Prova");
                lines.add(ChatColor.WHITE + "Giocatori: " + ChatColor.AQUA + gameManager.getPlayerManager().getPlayersInGame().size() + "/" + gameManager.getPlayerManager().getMaxPlayers());
                lines.add("     ");
                if(gameManager.getPhase() == GamePhases.WAITING){
                    lines.add(ChatColor.AQUA + "In attesa...");
                } else {
                    lines.add(ChatColor.AQUA + "Inizio in " + ChatColor.WHITE + gameManager.getTimecountdown() + "s");
                }
                lines.add("   ");
                lines.add(ChatColor.WHITE + "Modalità: " + ChatColor.AQUA + "Solo");
                lines.add(ChatColor.WHITE + "Versione: " + ChatColor.AQUA + "1.0");
                break;
            case INGAME:
            case END:
                GamePlayer profile = gameManager.getPlayerManager().getProfile(player);
                lines.add(ChatColor.WHITE + "Vite: " + profile.getLifeColor() + profile.getLives() + ChatColor.DARK_RED + "❤");
                lines.add(ChatColor.WHITE + "Uccisioni: " + ChatColor.AQUA + profile.getKills());
                lines.add("   ");
                lines.add(ChatColor.WHITE + "Giocatori in vita: " + ChatColor.AQUA + gameManager.getPlayerManager().getPlayersInGame().size());
                lines.add("  ");
                lines.add(ChatColor.WHITE + "Mappa: " + ChatColor.AQUA + "Prova");
                break;
        }

        lines.add(" ");
        lines.add(ChatColor.YELLOW + "play.coralmc.it     ");

        return build(player, lines);
    }

    @Override
    public JavaPlugin getPlugin() {
        return gameManager.getInstance();
    }

    @Override
    public org.org.godfather.gboard.api.scoreboard.adapter.ScoreboardAdapter getAdapter() {
        return gameManager.getInstance().getAdapter();
    }
}
