package tech.seife.chatutilities.broadcasts;

import java.util.List;
import java.util.Set;

public final class Broadcast {

    private final List<String> messages;
    private final long delay;

    public Broadcast(List<String> messages, long delay) {
        this.messages = messages;
        this.delay = delay;
    }

    public List<String> getMessages() {
        return messages;
    }

    public long getDelay() {
        return delay;
    }
}
