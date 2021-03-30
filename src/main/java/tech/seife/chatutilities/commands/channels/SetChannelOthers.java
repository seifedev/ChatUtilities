package tech.seife.chatutilities.commands.channels;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.enums.ReplaceType;
import tech.seife.chatutilities.utils.MessageManager;

import java.util.HashMap;
import java.util.Map;

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

                Map<ReplaceType, String> values = new HashMap<>();
                values.put(ReplaceType.CHANNEL, args[1]);
                values.put(ReplaceType.PLAYER_NAME, player.getName());

                sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "forcedChannelChangeSender", values));
            }
        }
        return true;
    }
}
