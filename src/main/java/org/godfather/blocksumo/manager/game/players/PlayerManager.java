package org.godfather.blocksumo.manager.game.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;
import org.godfather.blocksumo.manager.game.items.PlayerNavigator;
import org.godfather.blocksumo.manager.game.items.Shears;
import org.godfather.blocksumo.manager.game.items.Wool;
import org.godfather.blocksumo.utils.Helper;

import java.util.*;

public class PlayerManager {

    private final GameManager gameManager;
    private final Set<UUID> playersInGame = new HashSet<>();
    private final Set<UUID> spectators = new HashSet<>();
    private final Map<Player, GamePlayer> players = new HashMap<>();
    public final Map<Player, Player> damageMap = new HashMap<>();

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Set<UUID> getPlayersInGame() {
        return playersInGame;
    }

    public int getMaxPlayers() {
        return 20;
    }

    public int getRequiredPlayers() {
        return 2;
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

    public void setupGamePlayer(Player p) {
        players.put(p, new GamePlayer(p.getUniqueId()));
    }

    public void removeGamePlayer(Player p) {
        players.remove(p);
    }

    public GamePlayer getProfile(Player p) {
        return players.get(p);
    }

    public void killPlayer(Player p) {
        p.getInventory().clear();
        if (getProfile(p).getLives() >= 1) {
            getProfile(p).removeLife();
            new DeathCountdown(gameManager, p).runTaskTimer(gameManager.getInstance(), 0L, 20L);
        } else {
            playersInGame.remove(p.getUniqueId());
            spectators.add(p.getUniqueId());
            p.getInventory().setItem(0, PlayerNavigator.getItem());
            Helper.sendTitle(p, ChatColor.RED + "" + ChatColor.BOLD + "SEI UNO SPETTATORE", ChatColor.GRAY + "Non puoi più respawnare!", 10, 40, 10);
            p.getWorld().strikeLightningEffect(p.getLocation());

            if (getPlayersInGame().size() == 1)
                gameManager.setPhase(GamePhases.END);
        }
        for (UUID uuid : playersInGame) {
            Player player = Bukkit.getPlayer(uuid);
            player.hidePlayer(p);
        }
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.teleport(gameManager.getMap().getSpectLocation());
    }

    public void respawnPlayer(Player p) {
        Inventory inventory = p.getInventory();
        inventory.setItem(0, Shears.getItem());
        inventory.setItem(3, Wool.getItem());
        for (UUID uuid : playersInGame) {
            Player player = Bukkit.getPlayer(uuid);
            player.showPlayer(p);
        }
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(false);
        p.setFlying(false);
        p.teleport(gameManager.getMap().getSpawnLocation());
    }

    public void restart() {
        playersInGame.clear();
        spectators.clear();
        players.clear();
    }
}
