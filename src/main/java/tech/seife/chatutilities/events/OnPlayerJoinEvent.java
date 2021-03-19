package tech.seife.chatutilities.events;

import tech.seife.chatutilities.ChatUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        plugin.getIgnoreManager().loadIgnores(e.getPlayer().getUniqueId());
    }

}
