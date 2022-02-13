package org.godfather.blocksumo.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.godfather.blocksumo.manager.game.GameManager;
import org.godfather.blocksumo.manager.game.GamePhases;

public class Restart implements CommandExecutor {

    private GameManager gameManager;

    public Restart(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("blocksumo.restart") && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Non hai accesso a questo comando.");
            return false;
        }

        if(gameManager.getPhase() == GamePhases.LOADING || gameManager.getPhase() == GamePhases.END){
            sender.sendMessage(ChatColor.RED + "La partita è già in riavvio!");
            return false;
        }

        gameManager.setPhase(GamePhases.LOADING);
        return true;
    }
}
