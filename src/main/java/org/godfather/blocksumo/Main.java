package org.godfather.blocksumo;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    public void onEnable(){
        super.onEnable();
        plugin = this;

    }

    public void onDisable(){
        super.onDisable();

    }

    public static Main getInstance(){
        return plugin;
    }
}
