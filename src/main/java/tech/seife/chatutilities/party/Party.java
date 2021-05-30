package tech.seife.chatutilities.party;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class Party {

    private Player leader;
    private final String name;
    private final Set<Player> members;

    public Party(Player leader, String name) {
        this.leader = leader;
        this.name = name;
        members = new HashSet<>();
        members.add(leader);
    }

    public Player getLeader() {
        return leader;
    }

    public String getName() {
        return name;
    }

    public Set<Player> getMembers() {
        return members;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }
}
