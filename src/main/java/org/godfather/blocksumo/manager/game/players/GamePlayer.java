package org.godfather.blocksumo.manager.game.players;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.manager.game.GameManager;

import java.util.UUID;

public class GamePlayer {

    private GameManager gameManager;
    private UUID uuid;
    private int kills = 0;
    private int lives = 5;
    private Player hit; //ritorna chi ha attaccato il player
    private Player attacked; //ritorna chi il player ha attaccato

    public GamePlayer(GameManager gameManager, UUID uuid) {
        this.gameManager = gameManager;
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

    public Player getHit() {
        return hit;
    }

    public void setWhoHit(Player hito) {
        hit = hito;

        new BukkitRunnable() {
            @Override
            public void run() {
                hit = null;
            }
        }.runTaskLater(gameManager.getInstance(), 120L);
    }

    public Player getAttacked() {
        return attacked;
    }

    public void setWhoAttacked(Player attack) {
        attacked = attack;

        new BukkitRunnable() {
            @Override
            public void run() {
                attacked = null;
            }
        }.runTaskLater(gameManager.getInstance(), 120L);
    }
}
