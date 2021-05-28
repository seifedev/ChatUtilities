package tech.seife.chatutilities.commands.party;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.enums.ReplaceType;
import tech.seife.chatutilities.utils.MessageManager;

import java.util.HashMap;
import java.util.Map;

public final class LeaveParty implements CommandExecutor {

    private final ChatUtilities plugin;

    public LeaveParty(ChatUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 0) {
            Player player = (Player) sender;
            if (plugin.getPartyManager().isPlayerOwner(player)) {
                plugin.getPartyManager().disbandParty(player);
            } else if (plugin.getPartyManager().getPartyFromPlayer(player) != null){
                plugin.getPartyManager().removeMember(plugin.getPartyManager().getPartyFromPlayer(player), player);
            } else {
                player.sendMessage("You're not in a party.");
            }

            Map<ReplaceType, String> values = new HashMap<>();
            values.put(ReplaceType.PLAYER_NAME, player.getDisplayName());

            player.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "leaveParty", values));
        }
        return true;
    }
}
