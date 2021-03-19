package tech.seife.chatutilities.commands.party;

import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.party.Party;
import tech.seife.chatutilities.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class InviteToParty implements CommandExecutor {

    private final ChatUtilities plugin;


    public InviteToParty(ChatUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || args.length != 1) return true;
        if (Bukkit.getPlayer(args[0]) == null) return true;

        Player inviter = (Player) sender;
        Player invited = Bukkit.getPlayer(args[0]);

        if (plugin.getPartyManager().isPlayerOwner(inviter)) {
            Party party = plugin.getPartyManager().getPartyFromPlayer(inviter);

            Set<UUID> invitedPlayers;
            if (plugin.getDataHolder().getInvitedPlayers().containsKey(party)) {
                invitedPlayers = plugin.getDataHolder().getInvitedPlayers().get(party);
            } else {
                invitedPlayers = new HashSet<>();
            }
            invitedPlayers.add(invited.getUniqueId());

            plugin.getDataHolder().getInvitedPlayers().put(party, invitedPlayers);

            sendInvitationsMessages(inviter, invited, party.getName());
        }
        return true;
    }

    private void sendInvitationsMessages(Player inviter, Player invited, String partyName) {
        invited.sendMessage("You have been invited by: " + inviter.getName() + " to join the party: " + partyName);
        inviter.sendMessage(MessageManager.getTranslatedMessage(plugin, "invitedToPartySender"));
    }
}
