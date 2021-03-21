package tech.seife.chatutilities.events;

import net.milkbowl.vault.permission.Permission;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.channels.Channel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import tech.seife.chatutilities.datamanager.spamfilter.SpamFilterManager;

public final class OnPlayerCommandPreprocessEvent extends ChatManager implements Listener {

    private final ChatUtilities plugin;

    public OnPlayerCommandPreprocessEvent(ChatUtilities plugin, SpamFilterManager spamFilterManager, Permission permission) {
        super(plugin, spamFilterManager, permission);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled() || e.getMessage().split(" ")[0] == null || isSpam(e.getPlayer().getUniqueId(), e.getMessage())) {
            return;
        }

        String shortCut = e.getMessage().split(" ")[0];

        for (Channel channel : plugin.getChannelManager().getChannels()) {
            if (channel.getShortCut().equalsIgnoreCase(shortCut)) {
                e.setCancelled(true);

                String message = getChannelName(channel) + getPlayerPrefix(e.getPlayer()) +  getPlayerName(e.getPlayer()) + getPlayerSuffix(e.getPlayer()) +  formatText(channel.getColourCode(), formatMessage(e.getMessage().substring(channel.getShortCut().length())));

                if (e.getPlayer().hasPermission("ChatUtilities.channels.colour")) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                }

                sendMessage(e.getPlayer(), channel, message);
            }
        }
    }
}