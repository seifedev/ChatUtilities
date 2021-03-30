package tech.seife.chatutilities.utils;

import org.bukkit.ChatColor;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.enums.ReplaceType;

import java.util.Map;

public final class MessageManager {

    public static String getTranslatedMessageWithReplace(ChatUtilities plugin, String path, Map<ReplaceType, String> values) {
        if (plugin.getCustomFiles() != null && plugin.getCustomFiles().getTranslationsConfig() != null && plugin.getCustomFiles().getTranslationsConfig().getString(path) != null) {
            String message = plugin.getCustomFiles().getTranslationsConfig().getString(path);

            for (Map.Entry<ReplaceType, String> entry : values.entrySet()) {
                message = message.replaceAll(entry.getKey().getValue(), entry.getValue());
            }
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        return "There isn't a message.";
    }

    public static String getTranslatedMessage(ChatUtilities plugin, String path) {
        if (plugin.getCustomFiles() != null && plugin.getCustomFiles().getTranslationsConfig() != null && plugin.getCustomFiles().getTranslationsConfig().getString(path) != null) {
            return ChatColor.translateAlternateColorCodes('&', plugin.getCustomFiles().getTranslationsConfig().getString(path));
        }
        return null;
    }
}

