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

public final class CreateParty implements CommandExecutor {

    private final ChatUtilities plugin;

    public CreateParty(ChatUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1 && args[0] != null) {
            Player player = (Player) sender;

            if (plugin.getPartyManager().getPartyFromPlayer(player) == null) {
                plugin.getPartyManager().createParty(player, args[0]);

                Map<ReplaceType, String> values = new HashMap<>();
                values.put(ReplaceType.PARTY_NAME, args[0]);

                player.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "partyCreation", values));
            }
        }
        return true;
    }
}
