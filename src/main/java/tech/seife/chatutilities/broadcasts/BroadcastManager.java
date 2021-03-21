package tech.seife.chatutilities.broadcasts;

import tech.seife.chatutilities.ChatUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class BroadcastManager {

    private final Broadcast broadcast;
    private final ChatUtilities plugin;

    public BroadcastManager(ChatUtilities plugin) {
        this.plugin = plugin;
        List<String> messages = getMessages();
        long delay = getDelay();

        broadcast = new Broadcast(messages, delay);
    }

    public void startBroadcast() {
        AtomicInteger index = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcast.getMessages().get(index.getAndIncrement())));

            if (index.get() == broadcast.getMessages().size()) {
                index.set(0);
            }
        }, 10L, plugin.getCustomFiles().getBroadcastingConfig().getLong("broadcastDelay") * 20L);
    }

    private List<String> getMessages() {
        if (plugin.getCustomFiles().getBroadcastingConfig() != null) {
            return plugin.getCustomFiles().getBroadcastingConfig().getStringList("messages");
        }
        return plugin.getCustomFiles().getTranslationsConfig().getStringList("EmptyBroadcastConfig");
    }

    private long getDelay() {
        return plugin.getCustomFiles().getBroadcastingConfig().getLong("delay") > 0 ? plugin.getCustomFiles().getBroadcastingConfig().getLong("delay") : Long.MAX_VALUE;
    }

}
