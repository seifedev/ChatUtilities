package tech.seife.chatutilities.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.seife.chatutilities.ChatUtilities;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class CustomFiles {

    private final ChatUtilities plugin;

    private final File channelsFiles, broadcastingFile, bannedWordsFile, translationsFile, ignoredFile;
    private final FileConfiguration channelsConfig, broadcastingConfig, bannedWordsConfig, translationsConfig;

    private final Gson gson;

    public CustomFiles(ChatUtilities plugin) {
        this.plugin = plugin;

        gson = new GsonBuilder().setPrettyPrinting().create();
        ignoredFile = new File(plugin.getDataFolder(), "ignored.json");

        if (!ignoredFile.exists()) {
            plugin.saveResource(ignoredFile.getName(), false);
        }

        channelsFiles = new File(plugin.getDataFolder(), "channels.yml");
        channelsConfig = new YamlConfiguration();
        createFile(channelsFiles, channelsConfig, "channels.yml");

        broadcastingFile = new File(plugin.getDataFolder(), "broadcasts.yml");
        broadcastingConfig = new YamlConfiguration();
        createFile(broadcastingFile, broadcastingConfig, "broadcasts.yml");

        bannedWordsFile = new File(plugin.getDataFolder(), "bannedWords.yml");
        bannedWordsConfig = new YamlConfiguration();
        createFile(bannedWordsFile, bannedWordsConfig, "bannedWords.yml");

        translationsFile = new File(plugin.getDataFolder(), "translations.yml");
        translationsConfig = new YamlConfiguration();
        createFile(translationsFile, translationsConfig, "translations.yml");
    }

    private void createFile(File file, FileConfiguration config, String resourceName) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(resourceName, false);
        }

        try {
            config.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to create file: " + file.getName() + "\nError message: " + e.getMessage());
        }
    }

    public FileConfiguration getChannelsConfig() {
        return channelsConfig;
    }

    public FileConfiguration getBroadcastingConfig() {
        return broadcastingConfig;
    }

    public FileConfiguration getBannedWordsConfig() {
        return bannedWordsConfig;
    }

    public FileConfiguration getTranslationsConfig() {
        return translationsConfig;
    }

    public Map getIgnoredFile() {
        try {
            return gson.fromJson(new FileReader(ignoredFile), new HashMap<String, Object>().getClass());
        } catch (FileNotFoundException e) {
            plugin.getLogger().log(Level.WARNING, "didn't find ignored.json!\nError message: " + e.getMessage());
        }
        return null;
    }

    public void saveJson(Map map) {
        String json = gson.toJson(map);
        ignoredFile.delete();
        try {
            Files.write(ignoredFile.toPath(), json.getBytes());
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save json!\nErrorMessage: " + e.getMessage());
        }
    }
}

