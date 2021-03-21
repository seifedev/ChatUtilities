package tech.seife.chatutilities.ignores;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.channels.Channel;

import java.util.*;

public final class IgnoreManager {

    private final Set<Ignore> ignoreSet;
    private final ChatUtilities plugin;
    private final Gson gson;

    public IgnoreManager(ChatUtilities plugin) {
        this.plugin = plugin;
        ignoreSet = new HashSet<>();
        gson = new Gson();
    }

    public boolean canAcceptMessage(UUID sender, UUID receiver, String channelName) {
        return ignoreSet
                .stream()
                .noneMatch(ignore -> ignore.getIgnoredBy().equals(sender) && ignore.getIgnoredUuid().equals(receiver) && ignore.getChannel().getName().equalsIgnoreCase(channelName));
    }

    public void loadIgnores(UUID ignoredBy) {
        JsonObject ignores = gson.fromJson(gson.toJson(plugin.getCustomFiles().getIgnoredFile()), JsonObject.class);

        if (ignores.get(ignoredBy.toString()) == null) return;
        String ignoredByName = ignores.get(ignoredBy.toString()).getAsJsonObject().get("ignoredByUsername").getAsString();

        JsonObject ignoresSubSection = ignores.getAsJsonObject(ignoredBy.toString()).getAsJsonObject("ignores");

        for (Map.Entry<String, JsonElement> entry : ignoresSubSection.entrySet()) {
            JsonObject ignoredUuidSection = ignoresSubSection.getAsJsonObject(entry.getKey());
            List<String> strings = gson.fromJson(ignoredUuidSection.get("channels").getAsJsonArray(), List.class);

            for (int i = 0; i < strings.size(); i++) {
                ignoreSet.add(new Ignore(ignoredBy, UUID.fromString(entry.getKey()), ignoredByName, ignoredUuidSection.get("ignoredUsername").getAsString(), plugin.getChannelManager().getChannel(strings.get(i))));
            }
        }

    }

    public void addIgnore(Player ignoredBy, Player ignored, Channel channel) {
        JsonObject ignores = gson.fromJson(gson.toJson(plugin.getCustomFiles().getIgnoredFile()), JsonObject.class);

        JsonObject ignoredByUuidSection = new JsonObject();

        JsonArray channelsArraySection = new JsonArray();

        JsonObject ignoresSubSection = new JsonObject();

        JsonObject ignoredUuidSection = new JsonObject();

        if (isIgnoringOtherPlayers(ignoredBy, ignores)) {
            ignoresSubSection = ignores.get(ignoredBy.getUniqueId().toString()).getAsJsonObject().get("ignores").getAsJsonObject();

            ignoredUuidSection.addProperty("ignoredUsername", ignored.getName());
            ignoredByUuidSection.addProperty("ignoredByUsername", ignoredBy.getName());

            if (!isPlayerIgnored(ignored, ignoresSubSection)) {
                channelsArraySection = new JsonArray();
            } else {
                channelsArraySection = ignoresSubSection.get(ignored.getUniqueId().toString()).getAsJsonObject().get("channels").getAsJsonArray();
            }

            channelsArraySection.add(channel.getName());

            ignoredUuidSection.add("channels", channelsArraySection);

            ignoresSubSection.add(ignored.getUniqueId().toString(), ignoredUuidSection);

            ignoredByUuidSection.add("ignores", ignoresSubSection);

            ignores.add(ignoredBy.getUniqueId().toString(), ignoredByUuidSection);

        } else {
            ignoredByUuidSection.addProperty("ignoredByUsername", ignoredBy.getName());
            channelsArraySection.add(channel.getName());

            ignoredUuidSection.addProperty("ignoredUsername", ignored.getName());

            ignoredUuidSection.add("channels", channelsArraySection);

            ignoresSubSection.add(ignored.getUniqueId().toString(), ignoredUuidSection);

            ignoredByUuidSection.add("ignores", ignoresSubSection);

            ignores.add(ignoredBy.getUniqueId().toString(), ignoredByUuidSection);

            ignoreSet.add(new Ignore(ignoredBy.getUniqueId(), ignored.getUniqueId(), ignoredBy.getName(), ignored.getName(), channel));

        }
        ignoreSet.add(new Ignore(ignoredBy.getUniqueId(), ignored.getUniqueId(), ignoredBy.getName(), ignored.getName(), channel));

        plugin.getCustomFiles().saveJson(gson.fromJson(ignores, Map.class));
    }

    public void removeIgnore(Player ignoredBy, Player ignoredPlayer, Channel channel) {
        JsonObject ignores = gson.fromJson(gson.toJson(plugin.getCustomFiles().getIgnoredFile()), JsonObject.class);

        JsonObject ignoredByUuidSection = ignores.getAsJsonObject(ignoredBy.getUniqueId().toString());

        if (ignoredByUuidSection != null) {
            JsonObject ignoresSubSectionUuid = ignoredByUuidSection.get("ignores").getAsJsonObject().getAsJsonObject(ignoredPlayer.getUniqueId().toString());

            if (ignoresSubSectionUuid != null) {

                Set<String> channelSet = gson.fromJson(ignoresSubSectionUuid.getAsJsonArray("channels"), Set.class);

                if (channelSet.contains(channel.getName())) {
                    channelSet.remove(channel.getName());

                    ignoresSubSectionUuid.remove(ignoredPlayer.getUniqueId().toString());


                    JsonObject uuid = new JsonObject();
                    uuid.addProperty("ignoredUsername", ignoredPlayer.getName());
                    uuid.add("channels", gson.toJsonTree(channelSet));

                    ignores.get(ignoredBy.getUniqueId().toString()).getAsJsonObject().getAsJsonObject("ignores").add(ignoredPlayer.getUniqueId().toString(), uuid);

                    plugin.getCustomFiles().saveJson(gson.fromJson(ignores, Map.class));

                    ignoreSet.remove(getIgnoredViaIgnoredName(ignoredBy.getUniqueId(), ignoredPlayer.getName(), channel.getName()));
                }
            }
        }

    }


    private boolean isPlayerIgnored(Player ignored, JsonObject ignoresSubSections) {
        return ignoresSubSections.get(ignored.getUniqueId().toString()) != null;
    }

    private boolean isIgnoringOtherPlayers(Player ignoredBy, JsonObject ignores) {
        return ignores.get(ignoredBy.getUniqueId().toString()) != null;
    }

    private Ignore getIgnoredViaIgnoredName(UUID ignoredByUuid, String ignored, String channelName) {
        return ignoreSet
                .stream()
                .filter(ignore -> ignore.getIgnoredBy().equals(ignoredByUuid) && ignore.getIgnoredName().equalsIgnoreCase(ignored) && ignore.getChannel().getName().equalsIgnoreCase(channelName))
                .findFirst()
                .orElse(null);
    }
}
