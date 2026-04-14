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
    private static final String DEFAULT_TRACKER_TEXT_COLOR = "#FFFFFF";
    private static final float DEFAULT_TRACKER_TEXT_SCALE = 1.0f;
    private static final float MIN_TRACKER_TEXT_SCALE = 0.5f;
    private static final float MAX_TRACKER_TEXT_SCALE = 3.0f;
    private static ModConfig INSTANCE;

    @SerializedName("tracker_style")
    public TrackerStyle trackerStyle;
    @SerializedName("tracker_position")
    public TrackerPosition trackerPosition;
    @SerializedName("tracker_text_color")
    public String trackerTextColor;
    @SerializedName("tracker_text_scale")
    public float trackerTextScale;
    @SerializedName("milestones")
    public Map<Long, Milestone> milestones;

    private static ModConfig getDefault() {
        ModConfig defaultConfig = new ModConfig();

        defaultConfig.trackerStyle = TrackerStyle.DEFAULT;
        defaultConfig.trackerPosition = TrackerPosition.TOP_RIGHT;
        defaultConfig.trackerTextColor = DEFAULT_TRACKER_TEXT_COLOR;
        defaultConfig.trackerTextScale = DEFAULT_TRACKER_TEXT_SCALE;
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
            return;
        }

        sanitize(INSTANCE);
    }

    public static ModConfig getConfig() {
        if (INSTANCE == null) {
            INSTANCE = getDefault();
        }

        return INSTANCE;
    }

    public int getTrackerTextColorArgb() {
        Integer parsedColor = parseHexColor(trackerTextColor);
        return parsedColor != null ? parsedColor : 0xFFFFFFFF;
    }

    public float getTrackerTextScale() {
        return clampScale(trackerTextScale);
    }

    private static void sanitize(ModConfig config) {
        if (config.trackerStyle == null) {
            config.trackerStyle = TrackerStyle.DEFAULT;
        }

        if (config.trackerPosition == null) {
            config.trackerPosition = TrackerPosition.TOP_RIGHT;
        }

        if (config.milestones == null) {
            config.milestones = new TreeMap<>();
        } else if (!(config.milestones instanceof TreeMap)) {
            config.milestones = new TreeMap<>(config.milestones);
        }

        if (parseHexColor(config.trackerTextColor) == null) {
            config.trackerTextColor = DEFAULT_TRACKER_TEXT_COLOR;
        }

        config.trackerTextScale = clampScale(config.trackerTextScale);
    }

    private static float clampScale(float scale) {
        if (Float.isNaN(scale) || Float.isInfinite(scale)) {
            return DEFAULT_TRACKER_TEXT_SCALE;
        }

        return Math.max(MIN_TRACKER_TEXT_SCALE, Math.min(MAX_TRACKER_TEXT_SCALE, scale));
    }

    private static Integer parseHexColor(String colorValue) {
        if (colorValue == null) {
            return null;
        }

        String normalized = colorValue.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }

        if (normalized.length() == 6) {
            normalized = "FF" + normalized;
        }

        if (normalized.length() != 8 || !normalized.matches("(?i)[0-9a-f]{8}")) {
            return null;
        }

        try {
            return (int) Long.parseLong(normalized, 16);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
