package tech.seife.chatutilities.dao.spamfilter;

import org.bukkit.Bukkit;
import tech.seife.chatutilities.ChatUtilities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SpamFilterManager {

    private final Set<SpamFilter> spamFilterSet;
    private final ChatUtilities plugin;

    public SpamFilterManager(ChatUtilities plugin) {
        spamFilterSet = new HashSet<>();
        this.plugin = plugin;
    }

    public boolean shouldCancelEvent(UUID playerUuid, String message, long bufferTimeInSeconds) {
        SpamFilter spamFilter = getFilter(playerUuid);
        if (spamFilter == null) {
            addMessage(playerUuid, message);
            return false;
        } else if (spamFilter.getMessages() != null) {
            if (spamFilter.getMessages().containsValue(message)) {
                for (Map.Entry<LocalDateTime, String> entry : spamFilter.getMessages().entrySet()) {
                    if (entry.getValue().equals(message)) {
                        if (entry.getKey().plusSeconds(bufferTimeInSeconds).isBefore(LocalDateTime.now())) {
                            spamFilter.getMessages().remove(entry.getKey());

                            addMessage(playerUuid, message);
                            return false;
                        } else if (entry.getKey().plusSeconds(bufferTimeInSeconds).isAfter(LocalDateTime.now())) {
                            Bukkit.getPlayer(playerUuid).sendMessage("Please don't spam.");
                            return true;
                        }
                    }
                }
            } else {
                addMessage(playerUuid, message);
                return false;
            }
        }
        return false;
    }

    private void addMessage(UUID playerUuid, String message) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            SpamFilter spamFilter = getFilter(playerUuid);

            if (spamFilter != null) {
                spamFilter.getMessages().put(LocalDateTime.now(), message);
            } else {
                spamFilterSet.add(new SpamFilter(playerUuid, message));
            }
        }, 5L);
    }

    private SpamFilter getFilter(UUID playerUuid) {
        return spamFilterSet
                .stream()
                .filter(spamFilter -> spamFilter.getPlayer().equals(playerUuid))
                .findFirst()
                .orElse(null);
    }

}
