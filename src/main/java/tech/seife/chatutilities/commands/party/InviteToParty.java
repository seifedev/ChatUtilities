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

import java.util.*;

public final class InviteToParty implements CommandExecutor {

    private final ChatUtilities plugin;


    public InviteToParty(ChatUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println(0);
        if (!(sender instanceof Player) || args.length != 1) return true;
        if (Bukkit.getPlayer(args[0]) == null) return true;

        System.out.println(1);

        Player inviter = (Player) sender;
        Player invited = Bukkit.getPlayer(args[0]);

        System.out.println("inviter: " + inviter.getDisplayName());
        System.out.println("invited: " + invited.getDisplayName());

        if (plugin.getPartyManager().isPlayerOwner(inviter)) {
            System.out.println(2);
            Party party = plugin.getPartyManager().getPartyFromPlayer(inviter);

            Set<UUID> invitedPlayers;
            if (plugin.getDataHolder().getInvitedPlayers().containsKey(party)) {
                System.out.println(3);
                invitedPlayers = plugin.getDataHolder().getInvitedPlayers().get(party);
            } else {
                System.out.println(4);
                invitedPlayers = new HashSet<>();
            }

            System.out.println(5);
            invitedPlayers.add(invited.getUniqueId());

            invitedPlayers.forEach(System.out::println);

            plugin.getDataHolder().getInvitedPlayers().put(party, invitedPlayers);


            sendInvitationsMessages(inviter, invited, party.getName());
        }
        return true;
    }

    private void sendInvitationsMessages(Player inviter, Player invited, String partyName) {
        System.out.println(6);
        Map<ReplaceType, String> values = new HashMap<>();

        values.put(ReplaceType.PARTY_NAME, partyName);
        values.put(ReplaceType.PLAYER_NAME, invited.getDisplayName());

        System.out.println(7);
        inviter.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "invitedToPartyReceiver", values));

        values.put(ReplaceType.PLAYER_NAME, inviter.getDisplayName());
        invited.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "invitedToPartySender", values));

    }
}
