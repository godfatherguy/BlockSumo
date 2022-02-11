package org.godfather.blocksumo.manager.game.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
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

        Wool wool = (Wool) block.getState();
        gameManager.getBlockManager().setRandomColor(wool);
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

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            event.setCancelled(true);
        else event.setDamage(0.0);
    }
}
