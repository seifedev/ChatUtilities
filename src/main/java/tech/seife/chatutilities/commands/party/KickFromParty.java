package tech.seife.chatutilities.commands.party;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.enums.ReplaceType;
import tech.seife.chatutilities.party.Party;
import tech.seife.chatutilities.party.PartyManager;
import tech.seife.chatutilities.utils.MessageManager;

import java.util.HashMap;
import java.util.Map;

public final class KickFromParty implements CommandExecutor {

    private final ChatUtilities plugin;

    public KickFromParty(ChatUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1 && args[0] != null && Bukkit.getPlayer(args[0]) != null) {
            Player owner = (Player) sender;
            Player playerToKick = Bukkit.getPlayer(args[0]);

            PartyManager partyManager = plugin.getPartyManager();

            if (partyManager.isPlayerOwner(owner)) {
                partyManager.removeMember(plugin.getPartyManager().getPartyFromPlayer(owner), playerToKick);


                Map<ReplaceType, String> values = new HashMap<>();
                values.put(ReplaceType.PLAYER_NAME, args[0]);
                values.put(ReplaceType.PARTY_NAME, partyManager.getPartyFromPlayer(owner).getName());

                owner.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "successfullyKicked", values));
                playerToKick.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "kickedFromTheParty", values));
            }
        }
        return true;
    }
}
