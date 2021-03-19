package tech.seife.chatutilities.dao;

import tech.seife.chatutilities.party.Party;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class DataHolder {

    private final Map<Party, Set<UUID>> invitedPlayers;

    public DataHolder() {
        invitedPlayers = new HashMap<>();
    }

    public Map<Party, Set<UUID>> getInvitedPlayers() {
        return invitedPlayers;
    }
}
