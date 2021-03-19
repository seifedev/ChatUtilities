package tech.seife.chatutilities.channels;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Channel {

    private final String name;

    private final String prefix;

    private final int range;

    private final String shortCut;

    private final String permission;

    private final Set<UUID> playersInChannel;
    private final String colourCode;
    private boolean isPrivate;

    public Channel(String name, String prefix, int range, String shortCut, String permission, boolean isPrivate, String colourCode) {
        this.name = name;
        this.prefix = prefix;
        this.range = range;
        this.shortCut = shortCut;
        this.permission = permission;
        this.isPrivate = isPrivate;
        this.colourCode = colourCode;
        playersInChannel = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getRange() {
        return range;
    }

    public String getShortCut() {
        return shortCut;
    }

    public String getPermission() {
        return permission;
    }

    public Set<UUID> getPlayersInChannel() {
        return playersInChannel;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getColourCode() {
        return colourCode;
    }
}
