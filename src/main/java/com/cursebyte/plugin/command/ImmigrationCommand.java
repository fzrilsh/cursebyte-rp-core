package com.cursebyte.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.cursebyte.plugin.CursebyteCore;

public class ImmigrationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (!p.hasPermission("immigration.admin")) return true;

        if (args.length == 0) {
            p.sendMessage("§cUsage: /setupimmigration <spawn|npc>");
            return true;
        }

        if (args[0].equalsIgnoreCase("spawn")) {
            CursebyteCore.getInstance().getConfig().set("immigration.spawn-location", p.getLocation());
            CursebyteCore.getInstance().saveConfig();
            p.sendMessage("§aLokasi spawn imigrasi diatur di sini!");
        
        } else if (args[0].equalsIgnoreCase("npc")) {
            CursebyteCore.getInstance().getConfig().set("immigration.npc.location", p.getLocation());
            CursebyteCore.getInstance().saveConfig();
            p.sendMessage("§aLokasi NPC disimpan! Gunakan command spawn NPC untuk memunculkannya.");
        }

        return true;
    }
}