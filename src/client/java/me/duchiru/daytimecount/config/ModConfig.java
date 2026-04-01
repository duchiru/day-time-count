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
import java.util.Map;
import java.util.TreeMap;

public class ModConfig {
    private static final String CONFIG_FILE_NAME = String.format("%s.json", DayTimeCount.MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig INSTANCE;

    @SerializedName("tracker_style")
    public TrackerStyle trackerStyle;
    @SerializedName("tracker_position")
    public TrackerPosition trackerPosition;
    @SerializedName("milestones")
    public Map<Long, Milestone> milestones;

    private static ModConfig getDefault() {
        ModConfig defaultConfig = new ModConfig();

        defaultConfig.trackerStyle = TrackerStyle.DEFAULT;
        defaultConfig.trackerPosition = TrackerPosition.TOP_RIGHT;
        defaultConfig.milestones = new TreeMap<>();

        defaultConfig.milestones.put(1L, new Milestone("Welcome to Minecraft!", "Happy mining!", "RED", "WHITE", "entity.experience_orb.pickup"));
        defaultConfig.milestones.put(7L, new Milestone("One Week Survived", "You're getting the hang of this!", "GREEN", "GRAY", "entity.experience_orb.pickup"));
        defaultConfig.milestones.put(10L, new Milestone("Double Digits!", "Day 10 reached.", "GOLD", "YELLOW", "entity.player.levelup"));
        defaultConfig.milestones.put(100L, new Milestone("Wow, 100-Day Challenge!", "It's time to show off your world!", "AQUA", "GREEN", "ui.toast.challenge_complete"));
        defaultConfig.milestones.put(365L, new Milestone("HAPPY ANNIVERSARY!", "One entire year in this world.", "LIGHT_PURPLE", "WHITE", "ui.toast.challenge_complete"));

        return defaultConfig;
    }

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
            INSTANCE = getDefault();
            save();
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            INSTANCE = GSON.fromJson(reader, ModConfig.class);
        } catch (IOException exception) {
            DayTimeCount.LOGGER.error("Failed to load config from {}", configFile, exception);
        }

        if (INSTANCE == null) {
            INSTANCE = getDefault();
        }
    }

    public static ModConfig getConfig() {
        return INSTANCE;
    }
}
