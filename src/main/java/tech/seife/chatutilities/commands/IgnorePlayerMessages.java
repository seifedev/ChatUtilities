package tech.seife.chatutilities.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.enums.ReplaceType;
import tech.seife.chatutilities.ignores.IgnoreManager;
import tech.seife.chatutilities.utils.MessageManager;

import java.util.HashMap;
import java.util.Map;

public final class IgnorePlayerMessages implements CommandExecutor {

    private final ChatUtilities plugin;
    private final IgnoreManager ignoreManager;

    public IgnorePlayerMessages(ChatUtilities plugin, IgnoreManager ignoreManager) {
        this.plugin = plugin;
        this.ignoreManager = ignoreManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length == 2 && args[0] != null && Bukkit.getPlayer(args[0]) != null && args[1] != null && plugin.getChannelManager().getChannel(args[1]) != null) {

            Player ignoredByPlayer = ((Player) sender);
            Player ignoredPlayer = Bukkit.getPlayer(args[0]);

            Map<ReplaceType, String> values = new HashMap<>();
            values.put(ReplaceType.PLAYER_NAME, ignoredByPlayer.getName());

            ignoredPlayer.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "ignored", values));

            values.put(ReplaceType.PLAYER_NAME, ignoredPlayer.getName());

            ignoredByPlayer.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "toIgnored", values));

            ignoreManager.addIgnore(ignoredByPlayer, ignoredPlayer, plugin.getChannelManager().getChannel(args[1]));

        }
        return true;
    }
}
