package tech.seife.chatutilities.utils;

import tech.seife.chatutilities.ChatUtilities;

public final class MessageManager {

    public static String getTranslatedMessage(ChatUtilities plugin, String name) {
        if (plugin.getCustomFiles() != null && plugin.getCustomFiles().getTranslationsConfig() != null && plugin.getCustomFiles().getTranslationsConfig().getString(name) != null) {
            return plugin.getCustomFiles().getTranslationsConfig().getString(name);
        }
        return "none";
    }

    public static String replacePlayerName(String message, String playerName) {
        return message = message.replaceAll("%player%", playerName);
    }

    public static String replacePartyName(String message, String partyName) {
        return message = message.replaceAll("%partyName%", partyName);
    }

    public static String replaceChannelName(String message, String channelName) {
        return  message = message.replaceAll("%channelName%", channelName);
    }
}
