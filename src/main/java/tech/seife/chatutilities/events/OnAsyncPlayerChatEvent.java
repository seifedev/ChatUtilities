package tech.seife.chatutilities.events;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.channels.Channel;
import tech.seife.chatutilities.datamanager.spamfilter.SpamFilterManager;

public final class OnAsyncPlayerChatEvent extends ChatManager implements Listener {

    private final ChatUtilities plugin;

    public OnAsyncPlayerChatEvent(ChatUtilities plugin, SpamFilterManager spamFilterManager, Permission permission) {
        super(plugin, spamFilterManager, permission);
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Channel channel = plugin.getChannelManager().getPlayerCurrentChannel(e.getPlayer().getUniqueId());
        if (!e.isCancelled()) {
            e.setCancelled(true);
            if (channel != null && !isSpam(e.getPlayer().getUniqueId(), e.getMessage())) {
                String message = getChannelName(channel) + getPlayerPrefix(e.getPlayer()) + getPlayerName(e.getPlayer()) + getPlayerSuffix(e.getPlayer()) + formatText(channel.getColourCode(), formatMessage(e.getMessage()));

                if (e.getPlayer().hasPermission("ChatUtilities.channels.colour")) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                }
                sendMessage(e.getPlayer(), channel, message);
            }
        }
    }
}
