package org.godfather.blocksumo.manager.game.players;

import org.bukkit.entity.Player;
import org.godfather.blocksumo.manager.game.GameManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {

    private GameManager gameManager;
    private Set<UUID> playersInGame = new HashSet<>();
    private Set<UUID> spectators = new HashSet<>();
    private int maxPlayers = 20;
    private int requiredPlayers = 2;
    public HashMap<UUID, Integer> lives = new HashMap<>();
    public HashMap<UUID, Integer> kills = new HashMap<>();
    public HashMap<UUID, UUID> damageMap = new HashMap<>();

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Set<UUID> getPlayersInGame() {
        return playersInGame;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getRequiredPlayers() {
        return requiredPlayers;
    }

    public void addPlayerToGame(Player p) {
        getPlayersInGame().add(p.getUniqueId());
    }

    public void removePlayerFromGame(Player p) {
        getPlayersInGame().remove(p.getUniqueId());
    }

    public Set<UUID> getSpectators() {
        return spectators;
    }

    public void addSpectator(Player p) {
        getSpectators().add(p.getUniqueId());
    }

    public void removeSpectator(Player p) {
        getSpectators().remove(p.getUniqueId());
    }

    public void setupStats() {
        for (UUID uuid : getPlayersInGame()) {
            lives.put(uuid, 5);
            kills.put(uuid, 0);
        }
    }

    public int getLives(Player p) {
        return lives.get(p.getUniqueId());
    }

    public void setLives(Player p, int life) {
        lives.remove(p.getUniqueId());
        lives.put(p.getUniqueId(), life);
    }

    public int getKills(Player p) {
        return kills.get(p.getUniqueId());
    }

    public void setKills(Player p, int kill) {
        kills.remove(p.getUniqueId());
        kills.put(p.getUniqueId(), kill);
    }

    public void killPlayer(Player p) {
        p.getInventory().clear();
        removePlayerFromGame(p);
        addSpectator(p);
        if (getLives(p) >= 1) {
            setLives(p, getLives(p) - 1);
            DeathCountdown task = new DeathCountdown(gameManager, p);
            task.runTaskTimer(gameManager.getInstance(), 0L, 20L);
        } else {
            //todo aggiungere item dello spettatore.
        }
    }

    public void respawnPlayer(Player p) {
        p.getInventory().clear();
        //todo dare item del giocatore
        removeSpectator(p);
        addPlayerToGame(p);
        //todo teletrasportare al punto di spawn
    }
}
