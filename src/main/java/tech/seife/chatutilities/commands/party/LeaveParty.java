package tech.seife.chatutilities.commands.party;

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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.getPartyManager().isPlayerOwner(player)) {
                plugin.getPartyManager().disbandParty(player);
            } else {
                plugin.getPartyManager().removeMember(plugin.getPartyManager().getPartyFromPlayer(player), player);
            }

            Map<ReplaceType, String> values = new HashMap<>();
            values.put(ReplaceType.PLAYER_NAME, args[0]);

            player.sendMessage(MessageManager.getTranslatedMessage(plugin, "leaveParty", values));
        }
        return true;
    }
}
