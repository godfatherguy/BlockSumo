package org.godfather.blocksumo.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Helper {

    public static void sendTitle(Player p, String titol, String subtitol, int in, int stay, int fade) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + titol + "\"}");
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(in, stay, fade);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);

        IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitol + "\"}");
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
        PacketPlayOutTitle length2 = new PacketPlayOutTitle(in, stay, fade);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length2);
    }

    public static String centerText(String text) {
        int maxWidth = 80,
                spaces = (int) Math.round((maxWidth - 1.4 * ChatColor.stripColor(text).length()) / 2);
        return StringUtils.repeat(" ", spaces) + text;
    }
}
