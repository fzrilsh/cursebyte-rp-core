package com.cursebyte.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.ui.core.MenuRouter;

public class AppCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player p)) return true;

        MenuRouter.open(p, "main");
        return true;
    }
}
