package tech.seife.chatutilities.commands.channels;

import tech.seife.chatutilities.ChatUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetChannel extends ChangeChannels implements CommandExecutor {

    private final ChatUtilities plugin;

    public SetChannel(ChatUtilities plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length == 1 && args[0] != null) {
            changePlayerChannel(player.getUniqueId(), args[0]);
        }
        return true;
    }
}
