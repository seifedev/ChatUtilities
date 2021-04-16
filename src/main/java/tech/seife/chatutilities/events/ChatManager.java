package tech.seife.chatutilities.events;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.channels.Channel;
import tech.seife.chatutilities.datamanager.spamfilter.SpamFilterManager;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatManager {

    private static final String numberRegex = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
    private static final String regex = numberRegex + "\\." + numberRegex + "\\." + numberRegex + "\\." + numberRegex;

    private final ChatUtilities plugin;
    private final SpamFilterManager spamFilterManager;
    private final Permission permission;

    public ChatManager(ChatUtilities plugin, SpamFilterManager spamFilterManager, Permission permission) {
        this.plugin = plugin;
        this.spamFilterManager = spamFilterManager;
        this.permission = permission;
    }

    protected boolean isSpam(UUID playerUuid, String message) {
        if (permission != null && permission.playerHas(Bukkit.getPlayer(playerUuid), "chatUtilities.byPassChatSpamFilter")) {
            return false;
        } else return spamFilterManager.shouldCancelEvent(playerUuid, message, 30);
    }

    protected void sendMessage(Player player, Channel channel, String message) {

        if (plugin.getModeration().getDataManager().isPlayerMutedByUuid(player.getUniqueId(), channel.getName())) {
            player.sendMessage("You are muted in this channel!");
            return;
        }

        if (channel.getRange() == -1 && !channel.isPrivate()) {
            sendMessageToEveryone(player.getUniqueId(), message, channel.getName());
        } else if (channel.getRange() > 0 && channel.isPrivate()) {
            sendMessageToNearbyPlayers(channel, message, player);
        } else if (channel.isPrivate() && channel.getName().equalsIgnoreCase("party")) {
            sendMessageToPartyMembers(player, message, channel.getName());
        } else if (channel.getRange() == -1 && channel.isPrivate()) {
            sendMessageToPrivateChannel(player.getUniqueId(), channel.getPermission(), message, channel.getName());
        }
    }

    protected String formatMessage(String message) {
        message = replaceIp(message);
        message = message.toLowerCase();
        message = message.replaceAll("t", "\\t");
        if (plugin.getCustomFiles().getChannelsConfig() != null) {
            for (String bannedWord : plugin.getCustomFiles().getBannedWordsConfig().getStringList("bannedWords")) {
                message = message.replaceAll(bannedWord, "[REDACTED]");
            }
        }

        return message;
    }

    protected String formatText(String colourCode, String message) {
        message = " >> " + message;
        message = ChatColor.translateAlternateColorCodes('&', "" + colourCode + "" + message) + ChatColor.RESET + "";
        return message;
    }

    protected String getPlayerSuffix(Player player) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerSuffix(player)) + ChatColor.RESET + "";
    }

    protected String getPlayerPrefix(Player player) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(player)) + ChatColor.RESET + "";
    }


    protected String getChannelName(Channel channel) {
        return ChatColor.translateAlternateColorCodes('&', "" + channel.getColourCode() + "" + "[" + channel.getName() + "]") + ChatColor.RESET + "";
    }

    protected String getPlayerName(Player player) {
        if (player.hasPermission("chatUtilities.suffix")) {
            if (plugin.getChat().getPlayerSuffix(player) != null && plugin.getChat().getPlayerSuffix(player).length() >= 2) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerSuffix(player).charAt(0) + "" + plugin.getChat().getPlayerSuffix(player).charAt(1) + player.getName());
            }
        } else if (player.hasPermission("chatUtilities.prefix")) {
            if (plugin.getChat().getPlayerSuffix(player) != null) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(player).charAt(0) + "" + plugin.getChat().getPlayerPrefix(player).charAt(1) + player.getName());
            }
        }
        return player.getName();
    }


    protected void sendMessageToEveryone(UUID sender, String message, String channelName) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (plugin.getIgnoreManager().canAcceptMessage(sender, player.getUniqueId(), channelName)) {
                    player.sendMessage(message);
                }
            }
        });
    }


    protected void sendMessageToNearbyPlayers(Channel channel, String message, Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getIgnoreManager().canAcceptMessage(player.getUniqueId(), player.getUniqueId(), channel.getName())) {
                player.sendMessage(message); // The below loop doesn't include the Player, so we have to explicitly send a message to him/her.
            }
            for (Entity entity : player.getNearbyEntities(channel.getRange() / 2, 255, channel.getRange() / 2)) {
                if (entity instanceof Player) {
                    if (plugin.getIgnoreManager().canAcceptMessage(player.getUniqueId(), player.getUniqueId(), channel.getName())) {
                        entity.sendMessage(message);
                    }
                }
            }
        }, 1L);
    }

    protected void sendMessageToPartyMembers(Player player, String message, String channelName) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.getPartyManager() != null && plugin.getPartyManager().getPartyFromPlayer(player) != null && plugin.getPartyManager().getPartyFromPlayer((player)).getMembers() != null) {
                for (Player p : plugin.getPartyManager().getPartyFromPlayer((player)).getMembers()) {
                    if (plugin.getIgnoreManager().canAcceptMessage(player.getUniqueId(), player.getUniqueId(), channelName)) {
                        p.sendMessage(message);
                    }
                }
            }
        });
    }

    protected void sendMessageToPrivateChannel(UUID sender, String permission, String message, String channelName) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(permission)) {
                if (plugin.getIgnoreManager().canAcceptMessage(sender, player.getUniqueId(), channelName)) {
                    player.sendMessage(message);
                }
            }
        });
    }

    private String replaceIp(String messageToReplace) {
        if (messageToReplace != null) {
            Pattern pattern = Pattern.compile(regex);

            for (String message : messageToReplace.split(" ")) {
                Matcher m = pattern.matcher(message);

                if (message.matches(".*[.].*[.].*")) {
                    if (!message.startsWith("http") || !message.startsWith("www")) {
                        messageToReplace = messageToReplace.replaceAll(message, "[REDACTED]");
                    } else if (m.matches()) {
                        messageToReplace = messageToReplace.replaceAll(message, "[REDACTED]");
                    }
                }
            }
        }
        return messageToReplace;
    }

}
