package com.cursebyte.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.economy.EconomyService;

public class SetMoneyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player player)) return true;
        if (!player.hasPermission("economy.admin")) return true;

        if (args.length == 0) {
            player.sendMessage("§cUsage: /setmoney <nama|uang> <uang>");
            return true;
        }

        if (args.length == 1) {
            EconomyService.add(player.getUniqueId(), Double.parseDouble(args[0]));
        }else{
            Player target = Bukkit.getPlayer(args[0]);
            EconomyService.add(target.getUniqueId(), Double.parseDouble(args[1]));
        }

        player.sendMessage("§eUang berhasil di kirim.");
        return true;
    }
}
