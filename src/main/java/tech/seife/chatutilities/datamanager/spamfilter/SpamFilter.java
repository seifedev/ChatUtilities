package tech.seife.chatutilities.datamanager.spamfilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpamFilter {

    private UUID player;
    private Map<LocalDateTime, String> messages;

    public SpamFilter(UUID playerUuid, String message) {
        this.player = playerUuid;
        messages = new HashMap<>();

        messages.put(LocalDateTime.now(), message);
    }


    public UUID getPlayer() {
        return player;
    }

    public Map<LocalDateTime, String> getMessages() {
        return messages;
    }
}
