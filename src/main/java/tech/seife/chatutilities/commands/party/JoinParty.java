package tech.seife.chatutilities.commands.party;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.enums.ReplaceType;
import tech.seife.chatutilities.party.Party;
import tech.seife.chatutilities.utils.MessageManager;

import java.util.HashMap;
import java.util.Map;

public final class JoinParty implements CommandExecutor {

    private final ChatUtilities plugin;

    public JoinParty(ChatUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player || args.length != 1 || args[0] == null || Bukkit.getPlayer(args[0]) == null)
            return true;

        Party party = plugin.getPartyManager().getPartyFromName(args[0]);

        Player player = ((Player) sender);

        if (plugin.getDataHolder().getInvitedPlayers().get(party).contains(player.getUniqueId())) {
            plugin.getPartyManager().addMember(party, Bukkit.getPlayer(player.getUniqueId()));

            Map<ReplaceType, String> values = new HashMap<>();
            values.put(ReplaceType.PARTY_NAME, party.getName());

            player.sendMessage(MessageManager.getTranslatedMessage(plugin, "joinedTheParty", values));
        }
        return true;
    }
}

