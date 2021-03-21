package tech.seife.chatutilities.commands.channels;

import org.bukkit.Bukkit;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.channels.Channel;
import tech.seife.chatutilities.enums.ReplaceType;
import tech.seife.chatutilities.utils.MessageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChangeChannels {

    private final ChatUtilities plugin;

    public ChangeChannels(ChatUtilities plugin) {
        this.plugin = plugin;
    }


    public void changePlayerChannel(UUID playerUuid, String channelName) {
        if (plugin.getChannelManager().getChannel(channelName) != null && Bukkit.getPlayer(playerUuid).hasPermission(plugin.getChannelManager().getChannel(channelName).getPermission())) {
            removePlayerFromCurrentChannel(playerUuid);
            addPlayerToChannel(channelName, playerUuid);
        } else {
            plugin.getChannelManager().getChannels()
                    .forEach(channel -> {
                        if (channel.getShortCut().equalsIgnoreCase("/" + channelName) && Bukkit.getPlayer(playerUuid).hasPermission(channel.getPermission())) {
                            removePlayerFromCurrentChannel(playerUuid);
                            addPlayerToChannel(channel.getName(), playerUuid);
                        }
                    });
        }
    }

    private void addPlayerToChannel(String channelName, UUID playerUuid) {
        Channel channel = plugin.getChannelManager().getChannel(channelName);
        channel.getPlayersInChannel().add(playerUuid);

        Map<ReplaceType, String> values = new HashMap<>();
        values.put(ReplaceType.CHANNEL, channelName);

        Bukkit.getPlayer(playerUuid).sendMessage(MessageManager.getTranslatedMessage(plugin, "changedChannel", values));
    }

    private void removePlayerFromCurrentChannel(UUID playerUuid) {
        Channel currentChannel = plugin.getChannelManager().getPlayerCurrentChannel(playerUuid);

        if (currentChannel != null) {
            currentChannel.getPlayersInChannel().remove(playerUuid);
        }
    }

}
