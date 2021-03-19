package tech.seife.chatutilities.ignores;

import tech.seife.chatutilities.channels.Channel;

import java.util.UUID;

public final class Ignore {

    private final UUID ignoredBy;
    private final UUID ignoredUuid;

    private final String ignoredByName;
    private final String ignoredName;

    private final Channel channel;

    public Ignore(UUID ignoredBy, UUID ignoredUuid, String ignoredByName, String ignoredName, Channel channel) {
        this.ignoredBy = ignoredBy;
        this.ignoredUuid = ignoredUuid;
        this.ignoredByName = ignoredByName;
        this.ignoredName = ignoredName;
        this.channel = channel;
    }

    public UUID getIgnoredBy() {
        return ignoredBy;
    }

    public UUID getIgnoredUuid() {
        return ignoredUuid;
    }

    public String getIgnoredByName() {
        return ignoredByName;
    }

    public String getIgnoredName() {
        return ignoredName;
    }

    public Channel getChannel() {
        return channel;
    }
}
