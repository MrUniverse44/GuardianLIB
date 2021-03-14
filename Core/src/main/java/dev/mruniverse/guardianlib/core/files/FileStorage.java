package dev.mruniverse.guardianlib.core.files;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class FileStorage {
    private final GuardianLIB main;
    private final File settings;
    private FileConfiguration settingsCng;
    public FileStorage(GuardianLIB main) {
        this.main = main;
        this.settings = new File(main.getDataFolder(),"settings.yml");
        settingsCng = loadConfig("settings");
    }
    /**
     * Creates a config File if it doesn't exists,
     * reloads if specified file exists.
     *
     * @param configName config to create/reload.
     */
    public FileConfiguration loadConfig(String configName) {
        File configFile = new File(main.getDataFolder(), configName + ".yml");

        if (!configFile.exists()) {
            saveConfig(configName);
        }

        FileConfiguration cnf = null;
        try {
            cnf = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            main.getLogs().warn(String.format("A error occurred while loading the settings file. Error: %s", e));
            e.printStackTrace();
        }

        main.getLogs().info(String.format("&7File &e%s.yml &7has been loaded", configName));
        return cnf;
    }
    /**
     * Save config File Changes & Paths
     *
     * @param configName config to save/create.
     */
    public void saveConfig(String configName) {
        File folderDir = main.getDataFolder();
        File file = new File(main.getDataFolder(), configName + ".yml");
        if (!folderDir.exists()) {
            boolean createFile = folderDir.mkdir();
            if(createFile) main.getLogs().info("&7Folder created!");
        }

        if (!file.exists()) {
            try (InputStream in = main.getResource(configName + ".yml")) {
                if(in != null) {
                    Files.copy(in, file.toPath());
                }
            } catch (Throwable throwable) {
                main.getLogs().error(String.format("A error occurred while copying the config %s to the plugin data folder. Error: %s", configName, throwable));
                main.getLogs().error(throwable);
            }
        }
    }

    public void save() {
        try {
            getConfig().save(settings);
        }catch (Throwable throwable) {
            main.getLogs().error(throwable);
        }
    }

    public void reload() {
        try {
            settingsCng = YamlConfiguration.loadConfiguration(settings);
        }catch (Throwable throwable) {
            main.getLogs().error(throwable);
        }
    }

    public FileConfiguration getConfig() {
        if(settingsCng == null) settingsCng = loadConfig("settings");
        return settingsCng;
    }
}

