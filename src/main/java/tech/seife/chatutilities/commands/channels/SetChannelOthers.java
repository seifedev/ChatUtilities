package tech.seife.chatutilities.commands.channels;

import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetChannelOthers extends ChangeChannels implements CommandExecutor {

    private final ChatUtilities plugin;

    public SetChannelOthers(ChatUtilities plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2 && args[0] != null && args[1] != null) {

            Player player = Bukkit.getPlayer(args[0]);

            if (player != null && args[0] != null) {
                changePlayerChannel(player.getUniqueId(), args[1]);
                sender.sendMessage(MessageManager.getTranslatedMessage(plugin, "forcedChannelChangeSender"));
            }
        }
        return true;
    }
}
