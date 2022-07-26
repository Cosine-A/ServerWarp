package com.cosine.serverwarp.bungee;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
// Copyright 2022. 코사인(Cosine_A) all rights reserved.
public class CustomConfig {

    public String fileName;
    private final JavaPlugin plugin;
    public File file;
    private FileConfiguration config;

    public CustomConfig(JavaPlugin plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null) {
            throw new IllegalStateException();
        }
        this.file = new File(dataFolder.toString() + File.separatorChar + this.fileName);
    }
    public void reloadConfig() {
        if (config != null) {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Reader defConfigStream = new InputStreamReader(this.plugin.getResource(this.fileName));
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                this.config.setDefaults(defConfig);
            }
        }
    }
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return this.config;
    }

    public void saveConfig() {
        if ((this.config == null) || (this.file == null)) {
            return;
        }
        try {
            getConfig().save(this.file);
        } catch (IOException ex) {
            this.plugin.getLogger().info("Could not save config to " + this.file);
        }
    }

    public void saveDefaultConfig() {
        if (!this.file.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
    }
}
