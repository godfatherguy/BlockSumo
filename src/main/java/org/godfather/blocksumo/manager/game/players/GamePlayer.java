package org.godfather.blocksumo.manager.game.players;

import org.bukkit.ChatColor;

import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;
    private int kills = 0;
    private int lives = 5;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        kills++;
    }

    public void removeKill() {
        kills--;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addLife() {
        lives++;
    }

    public void removeLife() {
        lives--;
    }

    public ChatColor getLifeColor() {
        if (getLives() >= 4) return ChatColor.GREEN;
        else if (getLives() >= 2) return ChatColor.GOLD;
        else return ChatColor.RED;
    }
}
