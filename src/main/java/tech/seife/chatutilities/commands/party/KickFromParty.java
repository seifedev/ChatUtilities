package tech.seife.chatutilities.commands.party;

import tech.seife.chatutilities.ChatUtilities;
import tech.seife.chatutilities.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

            if (plugin.getPartyManager().isPlayerOwner(owner)) {
                plugin.getPartyManager().removeMember(plugin.getPartyManager().getPartyFromPlayer(owner), playerToKick);

                owner.sendMessage(MessageManager.getTranslatedMessage(plugin, "successfullyKicked"));
                playerToKick.sendMessage(MessageManager.getTranslatedMessage(plugin, "kickedFromTheParty"));
            }
        }
        return true;
    }
}
