package org.godfather.blocksumo.manager.game.players;

import org.bukkit.entity.Player;
import org.godfather.blocksumo.manager.game.GameManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {

    private GameManager gameManager;
    private Set<UUID> playersInGame = new HashSet<>();
    private Set<UUID> spectators = new HashSet<>();
    private int maxPlayers = 20;
    private int requiredPlayers = 2;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Set<UUID> getPlayersInGame() {
        return this.playersInGame;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getRequiredPlayers() {
        return this.requiredPlayers;
    }

    public void addPlayerToGame(Player p) {
        if (!getPlayersInGame().contains(p.getUniqueId()))
            getPlayersInGame().add(p.getUniqueId());
    }

    public void removePlayerFromGame(Player p) {
        if (getPlayersInGame().contains(p.getUniqueId()))
            getPlayersInGame().remove(p.getUniqueId());
    }

    public Set<UUID> getSpectators() {
        return this.spectators;
    }

    public void addSpectator(Player p) {
        if (!getSpectators().contains(p.getUniqueId()))
            getSpectators().add(p.getUniqueId());
    }

    public void removeSpectator(Player p) {
        if (getSpectators().contains(p.getUniqueId()))
            getSpectators().remove(p.getUniqueId());
    }
}
