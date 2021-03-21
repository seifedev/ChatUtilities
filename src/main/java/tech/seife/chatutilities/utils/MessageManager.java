package tech.seife.chatutilities.utils;

import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.enums.ReplaceType;

import java.util.Map;

public final class MessageManager {

    public static String getTranslatedMessage(ChatUtilities plugin, String path, Map<ReplaceType, String> values) {
        if (plugin.getCustomFiles() != null && plugin.getCustomFiles().getTranslationsConfig() != null && plugin.getCustomFiles().getTranslationsConfig().getString(path) != null) {
            String message = null;

            for (Map.Entry<ReplaceType, String> entry : values.entrySet()) {
                message = message.replaceAll(entry.getKey().getValue(), entry.getValue());
            }
            return message;
        }
        return "none";
    }
}

