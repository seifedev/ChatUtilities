package tech.seife.chatutilities.channels;

import tech.seife.chatutilities.ChatUtilities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ChannelManager {

    private final ChatUtilities plugin;

    private final Set<Channel> channels;


    public ChannelManager(ChatUtilities plugin) {
        this.plugin = plugin;
        channels = new HashSet<>();
        loadChannels();
    }

    public Channel getPlayerCurrentChannel(UUID playerUuid) {
        return channels.
                stream()
                .filter(channel -> channel.getPlayersInChannel().contains(playerUuid))
                .findFirst()
                .orElse(null);
    }

    public void addPlayerToChannel(String channelName, UUID playerUuid) {
        Channel channel = getChannel(channelName);

        if (channel != null) {
            channel.getPlayersInChannel().add(playerUuid);
        }
    }

    public Channel getChannel(String name) {
        return channels
                .stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private void loadChannels() {
        if (plugin.getCustomFiles().getChannelsConfig() != null) {
            for (String channelName : plugin.getCustomFiles().getChannelsConfig().getKeys(false)) {
                if (plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName) != null) {
                    int range = getChannelRange(channelName);
                    String prefix = getChannelPrefix(channelName);
                    String shortCut = getChannelShortcut(channelName);
                    String permissionNode = getChannelPermissionNode(channelName);
                    boolean isCustomChat = isPrivateChannel(channelName);
                    String colourCode = getChannelColourCode(channelName);

                    addChannel(channelName, prefix, range, shortCut, permissionNode, isCustomChat, colourCode);
                }
            }
        }
    }

    private int getChannelRange(String channelName) {
        return plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getInt("range");
    }

    private String getChannelPrefix(String channelName) {
        if (plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("prefix") != null) {
            return plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("prefix");
        }
        return "[" + channelName + "]";
    }

    private String getChannelShortcut(String channelName) {
        if (plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("channelShortcut") != null) {
            return plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("channelShortcut");
        } else {
            return "/" + channelName.charAt(0);
        }
    }

    private String getChannelPermissionNode(String channelName) {
        if (plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("permission") != null) {
            return plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("permission");
        } else {
            return "ChatUtilities.channel." + channelName;
        }
    }

    private void addChannel(String name, String prefix, int range, String shortCut, String permission, boolean isCustomChat, String colourCode) {
        if (!doesChannelExist(name)) {
            channels.add(new Channel(name, prefix, range, shortCut, permission, isCustomChat, colourCode));
        }
    }

    private boolean doesChannelExist(String name) {
        return channels
                .stream()
                .anyMatch(channel -> channel.getName().equalsIgnoreCase(name));
    }

    private boolean isPrivateChannel(String channelName) {
        return plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getBoolean("isPrivate");
    }

    private String getChannelColourCode(String channelName) {
        if (plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("textColour") != null) {
            return plugin.getCustomFiles().getChannelsConfig().getConfigurationSection(channelName).getString("textColour");
        } else {
            return "&f";
        }

    }

    public Set<Channel> getChannels() {
        return channels;
    }
}
