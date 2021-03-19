package tech.seife.chatutilities.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class Broadcast implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder sb = new StringBuilder();

        for (String string : args) {
            sb.append(string);
        }

        Bukkit.broadcastMessage(ChatColor.RED + "[Broadcast] " + ChatColor.translateAlternateColorCodes('&', sb.toString()));
        return true;
    }
}
