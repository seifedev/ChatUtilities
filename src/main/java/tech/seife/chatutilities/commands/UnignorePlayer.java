package tech.seife.chatutilities.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.ignores.IgnoreManager;

import java.util.UUID;

public final class UnignorePlayer implements CommandExecutor {

    private final ChatUtilities plugin;
    private final IgnoreManager ignoreManager;

    public UnignorePlayer(ChatUtilities plugin, IgnoreManager ignoreManager) {
        this.plugin = plugin;
        this.ignoreManager = ignoreManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length == 2 && args[0] != null && args[1] != null && plugin.getChannelManager().getChannel(args[1]) != null && Bukkit.getPlayer(args[0]) != null) {
            Player ignoredBy = ((Player) sender);

            ignoreManager.removeIgnore(ignoredBy, Bukkit.getPlayer(args[0]), plugin.getChannelManager().getChannel(args[1]));
        }
        return true;
    }
}
