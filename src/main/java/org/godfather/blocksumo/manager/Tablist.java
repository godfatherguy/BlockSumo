package org.godfather.blocksumo.manager;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.godfather.blocksumo.Main;

import java.lang.reflect.Field;

public class Tablist extends BukkitRunnable {

    @Override
    public void run() {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText(" \n" + ChatColor.AQUA + "" + ChatColor.BOLD + "CORAL" + ChatColor.WHITE + "" + ChatColor.BOLD + "MC" + "\n");
        Object footer = new ChatComponentText(ChatColor.AQUA + "\nSito: " + ChatColor.GRAY + "www.coralmc.it\n"
                + ChatColor.AQUA + "Teamspeak: " + ChatColor.GRAY + "ts.coralmc.it\n"
                + ChatColor.AQUA + "Store: " + ChatColor.GRAY + "ts.coralmc.it");

        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);

            a.set(packet, header);
            b.set(packet, footer);

            if (Bukkit.getOnlinePlayers().size() == 0) return;
            for (Player p : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (IllegalAccessError | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        Tablist task = new Tablist();
        task.runTaskTimer(Main.getInstance(), 0L, 40L);
    }
}
