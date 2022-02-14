package org.godfather.blocksumo.manager.game.scoreboard;

import org.bukkit.scoreboard.Objective;
import org.godfather.blocksumo.manager.game.GameManager;

public class ScoreboardManager {

    private final GameManager gameManager;

    public ScoreboardManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public String getEntryFromScore(Objective o, int score) {
        if (o == null) return null;
        if (!hasScoreTaken(o, score)) return null;
        for (String s : o.getScoreboard().getEntries()) {
            if (o.getScore(s).getScore() == score) return o.getScore(s).getEntry();
        }
        return null;
    }

    public boolean hasScoreTaken(Objective o, int score) {
        for (String s : o.getScoreboard().getEntries()) {
            if (o.getScore(s).getScore() == score) return true;
        }
        return false;
    }

    public void replaceScore(Objective o, int score, String name) {
        if (hasScoreTaken(o, score)) {
            if (getEntryFromScore(o, score).equalsIgnoreCase(name)) return;
            if (!(getEntryFromScore(o, score).equalsIgnoreCase(name)))
                o.getScoreboard().resetScores(getEntryFromScore(o, score));
        }
        o.getScore(name).setScore(score);
    }
}
