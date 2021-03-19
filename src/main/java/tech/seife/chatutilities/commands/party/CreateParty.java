package tech.seife.chatutilities.commands.party;

import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.utils.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                player.sendMessage(MessageManager.replacePartyName(MessageManager.getTranslatedMessage(plugin, "partyCreation"), args[0]));
            }
        }
        return true;
    }
}
