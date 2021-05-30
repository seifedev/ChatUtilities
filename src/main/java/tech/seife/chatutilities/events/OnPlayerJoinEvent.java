package tech.seife.chatutilities.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.party.Party;

public class OnPlayerJoinEvent implements Listener {

    private final ChatUtilities plugin;

    public OnPlayerJoinEvent(ChatUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getCustomFiles().getChannelsConfig().getString("defaultChannel") != null) {
                plugin.getChannelManager().addPlayerToChannel(plugin.getCustomFiles().getChannelsConfig().getString("defaultChannel"), e.getPlayer().getUniqueId());
            }
        }, 1L);

        if (plugin.getPartyManager().getPartyFromPlayerUuid(e.getPlayer().getUniqueId()) != null) {
            Party party = plugin.getPartyManager().getPartyFromPlayerUuid(e.getPlayer().getUniqueId());
            party.getMembers().remove(e.getPlayer());

            party.getMembers().add(e.getPlayer());

            if (party.getLeader().getUniqueId().equals(e.getPlayer().getUniqueId())) {
                party.setLeader(e.getPlayer());
            }
        }
        plugin.getIgnoreManager().loadIgnores(e.getPlayer().getUniqueId());
    }

}
