package me.duchiru.daytimecount.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import me.duchiru.daytimecount.DayTimeCount;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {
    private static final String CONFIG_FILE_NAME = String.format("%s.json", DayTimeCount.MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig INSTANCE;

    @SerializedName("tracker_position")
    public TrackerPosition trackerPosition = TrackerPosition.TOP_LEFT;

    public static void save() {
        if (INSTANCE == null) return;

        Path configDir = FabricLoader.getInstance().getConfigDir();
        File configFile = Paths.get(configDir.toString(), CONFIG_FILE_NAME).toFile();

        try {
            Files.createDirectories(configDir);
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (IOException exception) {
            DayTimeCount.LOGGER.error("Failed to save config to {}", configFile, exception);
        }
    }

    public static void load() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        File configFile = Paths.get(configDir.toString(), CONFIG_FILE_NAME).toFile();

        if (!configFile.exists()) {
            INSTANCE = new ModConfig();
            save();
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            INSTANCE = GSON.fromJson(reader, ModConfig.class);
        } catch (IOException exception) {
            DayTimeCount.LOGGER.error("Failed to load config from {}", configFile, exception);
            return;
        }

        if (INSTANCE == null) {
            INSTANCE = new ModConfig();
        }
    }

    public static ModConfig getConfig() {
        return INSTANCE;
    }
}
