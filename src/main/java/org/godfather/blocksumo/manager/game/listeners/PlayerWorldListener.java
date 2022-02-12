package org.godfather.blocksumo.manager.game.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;

public class PlayerWorldListener implements Listener {

    private GameManager gameManager;

    public PlayerWorldListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (gameManager.getPhase() != GamePhases.INGAME) {
            event.setCancelled(true);
            return;
        }

        if (block.getType() != Material.WOOL) {
            event.setCancelled(true);
            return;
        }
        gameManager.getBlockManager().addBlock(block);


    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        event.setCancelled(true);

        if (gameManager.getPhase() != GamePhases.INGAME) return;

        if (!gameManager.getBlockManager().getPlacedBlocks().contains(block)) return;

        gameManager.getBlockManager().removeBlock(block);
        p.getInventory().addItem(new ItemStack(Material.WOOL));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPick(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();

        if (gameManager.getPhase() != GamePhases.INGAME) {
            event.setCancelled(true);
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            event.setCancelled(true);
        else event.setDamage(0.0);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (gameManager.getPhase() != GamePhases.INGAME) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        Player victim = (Player) event.getEntity();

        if (!(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        Player damager = (Player) event.getDamager();

        event.setDamage(0.0);
    }

    @EventHandler
    public void onFall(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (p.getLocation().getY() > 0.0) return;
        if (p.getGameMode() != GameMode.SURVIVAL) return;
        if (!gameManager.getPlayerManager().getPlayersInGame().contains(p.getUniqueId())) return;

        if (gameManager.getPhase() != GamePhases.INGAME) return;

        gameManager.getPlayerManager().killPlayer(p);
        Player damager = (Player) p.getLastDamageCause().getEntity();

        if (damager.isOnline() && damager != null) {
            gameManager.getPlayerManager().setKills(damager, gameManager.getPlayerManager().getKills(damager) + 1);
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GREEN + p.getName() + ChatColor.GRAY + " è stato spinto da " + ChatColor.RED + damager.getName() + ChatColor.GRAY + "!"));
        } else
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GREEN + p.getName() + ChatColor.GRAY + " è caduto nel vuoto."));
    }
}
