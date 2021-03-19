package tech.seife.chatutilities.commands.channels;

import tech.seife.chatutilities.channels.ChannelManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class ListChannels implements CommandExecutor {

    private final ChannelManager channelManager;

    public ListChannels(ChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder sb = new StringBuilder();

        sb.append("Channels: ");

        channelManager.getChannels().forEach(channel -> {
            if (sender.hasPermission(channel.getPermission())) {
                sb.append(channel.getName()).append(", ");
            }
        });

        sb.setCharAt(sb.length() - 2, '.');

        sender.sendMessage(sb.toString());

        return true;
    }
}
