package me.duchiru.daytimecount.contents;

import me.duchiru.daytimecount.config.ModConfig;
import me.duchiru.daytimecount.config.TrackerPosition;
import me.duchiru.daytimecount.config.TrackerStyle;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.NonNull;

public class HudDayTimeTracker implements HudElement {
    private static final int PADDING_X = 4;
    private static final int PADDING_Y = 4;
    private static final int LINE_HEIGHT = 9;
    private static final int GAP = 2;
    private static final int HOTBAR_HEIGHT = 46;

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor gui, @NonNull DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.options.hideGui) return;

        ModConfig config = ModConfig.getConfig();

        // 1. Calculate days and time
        long timeOfDay = client.level.getOverworldClockTime() % 24000;
        long days = client.level.getOverworldClockTime() / 24000 + 1; // 1-based days count

        int hours = (int) ((timeOfDay / 1000 + 6) % 24);
        int minutes = (int) ((timeOfDay % 1000) * 60 / 1000);

        // 2. Decide how to display time based on style from config
        TrackerStyle trackerStyle = config.trackerStyle;
        int textColor = config.getTrackerTextColorArgb();
        float textScale = config.getTrackerTextScale();
        switch (trackerStyle) {
            case DEFAULT -> {
                String dayString = String.format("Day %d", days);
                String timeString = String.format("Time %02d:%02d", hours, minutes);
                renderTwoLines(gui, client.font, config.trackerPosition, dayString, timeString, textColor, textScale);
            }

            case COMPACT -> {
                String dayTimeString = String.format("Day %d - %02d:%02d", days, hours, minutes);
                renderOneLine(gui, client.font, config.trackerPosition, dayTimeString, textColor, textScale);
            }

            case DAY_ONLY -> {
                String dayOnlyString = String.format("Day %d", days);
                renderOneLine(gui, client.font, config.trackerPosition, dayOnlyString, textColor, textScale);
            }

            case TIME_ONLY -> {
                String timeOnlyString = String.format("%02d:%02d", hours, minutes);
                renderOneLine(gui, client.font, config.trackerPosition, timeOnlyString, textColor, textScale);
            }
        }
    }

    private void renderOneLine(GuiGraphicsExtractor gui, Font font, TrackerPosition position, String line, int textColor, float textScale) {
        int textWidth = scaleDimension(font.width(line), textScale);
        int textHeight = scaleDimension(LINE_HEIGHT, textScale);

        RenderPosition renderPos = calculateRenderPosition(gui, position, textWidth, textHeight);

        drawScaledText(gui, font, line, renderPos.x, renderPos.y, textColor, textScale);
    }

    private void renderTwoLines(GuiGraphicsExtractor gui, Font font, TrackerPosition position, String line1, String line2, int textColor, float textScale) {
        int textWidth = Math.max(scaleDimension(font.width(line1), textScale), scaleDimension(font.width(line2), textScale));
        int textHeight = scaleDimension(LINE_HEIGHT * 2 + GAP, textScale);

        RenderPosition renderPos = calculateRenderPosition(gui, position, textWidth, textHeight);
        int secondLineOffset = LINE_HEIGHT + GAP;

        drawScaledText(gui, font, line1, renderPos.x, renderPos.y, textColor, textScale);
        drawScaledText(gui, font, line2, renderPos.x, renderPos.y + scaleDimension(secondLineOffset, textScale), textColor, textScale);
    }

    private void drawScaledText(GuiGraphicsExtractor gui, Font font, String text, int x, int y, int color, float scale) {
        gui.pose().pushMatrix();
        gui.pose().translate((float) x, (float) y);
        gui.pose().scale(scale, scale);
        gui.text(font, text, 0, 0, color, true);
        gui.pose().popMatrix();
    }

    private int scaleDimension(int baseValue, float scale) {
        return Math.max(1, Math.round(baseValue * scale));
    }

    private RenderPosition calculateRenderPosition(GuiGraphicsExtractor gui, TrackerPosition position, int textWidth, int textHeight) {
        int screenWidth = gui.guiWidth();
        int screenHeight = gui.guiHeight();

        int rightAlignedX = Math.max(PADDING_X, screenWidth - PADDING_X - textWidth);
        int bottomAlignedY = Math.max(PADDING_Y, screenHeight - PADDING_Y - textHeight);
        int hotbarY = Math.max(PADDING_Y, screenHeight - HOTBAR_HEIGHT - PADDING_Y - textHeight);
        int centeredX = Math.max(PADDING_X, (screenWidth - textWidth) / 2);

        return switch (position) {
            case TOP_LEFT -> new RenderPosition(PADDING_X, PADDING_Y);
            case TOP_RIGHT -> new RenderPosition(rightAlignedX, PADDING_Y);
            case BOTTOM_LEFT -> new RenderPosition(PADDING_X, bottomAlignedY);
            case BOTTOM_RIGHT -> new RenderPosition(rightAlignedX, bottomAlignedY);
            case HOTBAR -> new RenderPosition(centeredX, hotbarY);
        };
    }

    private record RenderPosition(int x, int y) {
    }
}
