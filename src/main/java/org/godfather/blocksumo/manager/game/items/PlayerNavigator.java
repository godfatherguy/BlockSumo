package org.godfather.blocksumo.manager.game.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.godfather.blocksumo.manager.game.GameManager;

import java.util.UUID;

public class PlayerNavigator implements Listener {

    private GameManager gameManager;

    public PlayerNavigator(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = p.getInventory().getItemInHand();

        if (item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null || !item.getItemMeta().getDisplayName().equals(getItem().getItemMeta().getDisplayName()))
            return;

        p.openInventory(getGui());
        //todo mettere update automatico dell'inventario
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player p = (Player) event.getWhoClicked();

        if (!event.getInventory().getName().equals(getGui().getName())) return;
        ItemStack item = event.getCurrentItem();

        if (item == null) return;
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        p.teleport(Bukkit.getPlayerExact(meta.getOwner()));
        p.closeInventory();
    }

    public static ItemStack getItem() {

        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Cerca un giocatore");
        item.setItemMeta(meta);
        return item;
    }

    private Inventory getGui() {

        Inventory gui = Bukkit.createInventory(null, 27, "Cerca un giocatore");
        int counter = 0;
        for (UUID uuid : gameManager.getPlayerManager().getPlayersInGame()) {
            Player p = Bukkit.getPlayer(uuid);

            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(p.getName());
            meta.setDisplayName(ChatColor.RED + p.getName());
            item.setItemMeta(meta);

            gui.setItem(counter, item);
            counter++;
        }
        return gui;
    }
}
