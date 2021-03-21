package tech.seife.chatutilities;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import tech.seife.chatutilities.broadcasts.BroadcastManager;
import tech.seife.chatutilities.channels.ChannelManager;
import tech.seife.chatutilities.commands.Broadcast;
import tech.seife.chatutilities.commands.IgnorePlayerMessages;
import tech.seife.chatutilities.commands.channels.ListChannels;
import tech.seife.chatutilities.commands.UnignorePlayer;
import tech.seife.chatutilities.commands.channels.SetChannel;
import tech.seife.chatutilities.commands.channels.SetChannelOthers;
import tech.seife.chatutilities.commands.party.*;
import tech.seife.chatutilities.datamanager.CustomFiles;
import tech.seife.chatutilities.datamanager.DataHolder;
import tech.seife.chatutilities.datamanager.spamfilter.SpamFilterManager;
import tech.seife.chatutilities.events.OnAsyncPlayerChatEvent;
import tech.seife.chatutilities.events.OnPlayerCommandPreprocessEvent;
import tech.seife.chatutilities.events.OnPlayerJoinEvent;
import tech.seife.chatutilities.ignores.IgnoreManager;
import tech.seife.chatutilities.party.PartyManager;

public final class ChatUtilities extends JavaPlugin {

    private Chat chat;
    private CustomFiles customFiles;
    private DataHolder dataHolder;
    private PartyManager partyManager;
    private BroadcastManager broadcastManager;
    private SpamFilterManager spamFilterManager;
    private IgnoreManager ignoreManager;

    private ChannelManager channelManager;
    private Permission permission;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setupChat();
        setUpPermissions();
        initialize();
        registerEvents();
        registerCommands();

        customFiles.transformBannedWords();
    }

    private void initialize() {
        dataHolder = new DataHolder();
        customFiles = new CustomFiles(this);
        channelManager = new ChannelManager(this);
        partyManager = new PartyManager();
        ignoreManager = new IgnoreManager(this);
        spamFilterManager = new SpamFilterManager(this);

        broadcastManager = new BroadcastManager(this);
        broadcastManager.startBroadcast();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new OnAsyncPlayerChatEvent(this, spamFilterManager, permission), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerCommandPreprocessEvent(this, spamFilterManager, permission), this);
    }

    private boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();

        return chat != null;
    }

    private boolean setUpPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();

        return permission != null;
    }

    private void registerCommands() {
        getCommand("cc").setExecutor(new SetChannel(this));
        getCommand("setChannel").setExecutor(new SetChannelOthers(this));
        getCommand("createParty").setExecutor(new CreateParty(this));
        getCommand("inviteToParty").setExecutor(new InviteToParty(this));
        getCommand("joinParty").setExecutor(new JoinParty(this));
        getCommand("kickFromParty").setExecutor(new KickFromParty(this));
        getCommand("leaveParty").setExecutor(new LeaveParty(this));
        getCommand("unIgnore").setExecutor(new UnignorePlayer(this, ignoreManager));
        getCommand("ignore").setExecutor(new IgnorePlayerMessages(this, ignoreManager));
        getCommand("broadcast").setExecutor(new Broadcast());
        getCommand("ccList").setExecutor(new ListChannels(channelManager));
    }

    public CustomFiles getCustomFiles() {
        return customFiles;
    }

    public Chat getChat() {
        return chat;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    public IgnoreManager getIgnoreManager() {
        return ignoreManager;
    }
}
