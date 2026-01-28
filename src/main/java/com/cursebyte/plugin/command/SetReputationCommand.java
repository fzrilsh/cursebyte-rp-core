package com.cursebyte.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.reputation.ReputationService;

public class SetReputationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player player)) return true;
        if (!player.hasPermission("government.admin")) return true;

        if (args.length == 0) {
            player.sendMessage("§cUsage: /setreputation <nama|reputasi> <reputasi>");
            return true;
        }

        if (args.length == 1) {
            ReputationService.add(player.getUniqueId(), Double.parseDouble(args[0]));
        }else{
            Player target = Bukkit.getPlayer(args[0]);
            ReputationService.add(target.getUniqueId(), Double.parseDouble(args[1]));
        }

        player.sendMessage("§eReputasi berhasil di kirim.");
        return true;
    }
}