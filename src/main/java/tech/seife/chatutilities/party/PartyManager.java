package tech.seife.chatutilities.party;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class PartyManager {

    private final Set<Party> parties;

    public PartyManager() {
        parties = new HashSet<>();
    }

    public boolean isPlayerOwner(Player player) {
        Party party = getPartyFromPlayer(player);

        if (party != null) {
            return party.getLeader().equals(player);
        }
        return false;
    }

    public void createParty(Player player, String partyName) {
        parties.add(new Party(player, partyName));
    }

    public void disbandParty(Player player) {
        Party party = getPartyFromPlayer(player);

        if (party != null && isPlayerOwner(player)) {
            party.getMembers().clear();
            parties.removeIf(p -> p.equals(party));
        }
    }

    public void addMember(Party party, Player player) {
        party.getMembers().add(player);
    }

    public void removeMember(Party fromParty, Player player) {
        if (fromParty != null) {
            fromParty.getMembers().removeIf(p -> p.equals(player));
        }
    }

    public Party getPartyFromPlayer(Player player) {
        return parties.
                stream()
                .filter(party -> party.getMembers().contains(player))
                .findFirst()
                .orElse(null);
    }

    public Party getPartyFromPlayerUuid(UUID player) {
        for (Party party : parties) {
            for (Player members : party.getMembers()) {
                if (members.getUniqueId().equals(player)) {
                    return party;
                }
            }
        }
        return null;
    }

    public Party getPartyFromName(String partyName) {
        return parties.
                stream()
                .filter(party -> party.getName().equalsIgnoreCase(partyName))
                .findFirst()
                .orElse(null);
    }

}
