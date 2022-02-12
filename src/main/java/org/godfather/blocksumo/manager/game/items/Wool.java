package org.godfather.blocksumo.manager.game.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Wool {

    public static ItemStack getItem() {

        ItemStack item = new ItemStack(Material.WOOL, 64);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Lana");
        item.setItemMeta(meta);
        return item;
    }
}
