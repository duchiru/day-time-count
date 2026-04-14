package me.duchiru.daytimecount.contents;

import me.duchiru.daytimecount.config.ModConfig;
import me.duchiru.daytimecount.config.TrackerPosition;
import me.duchiru.daytimecount.config.TrackerStyle;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.jspecify.annotations.NonNull;

public class HudDayTimeTracker implements HudElement {
    private static final int PADDING_X = 4;
    private static final int PADDING_Y = 4;
    private static final int LINE_HEIGHT = 9;
    private static final int GAP = 2;
    private static final int HOTBAR_HEIGHT = 46;

    @Override
    public void render(@NonNull DrawContext context, @NonNull RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.options.hudHidden) return;

        ModConfig config = ModConfig.getConfig();

        // 1. Calculate days and time
        long timeOfDay = client.world.getTimeOfDay() % 24000;
        long days = client.world.getTimeOfDay() / 24000 + 1; // 1-based days count

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
                renderTwoLines(context, client.textRenderer, config.trackerPosition, dayString, timeString, textColor, textScale);
            }

            case COMPACT -> {
                String dayTimeString = String.format("Day %d - %02d:%02d", days, hours, minutes);
                renderOneLine(context, client.textRenderer, config.trackerPosition, dayTimeString, textColor, textScale);
            }

            case DAY_ONLY -> {
                String dayOnlyString = String.format("Day %d", days);
                renderOneLine(context, client.textRenderer, config.trackerPosition, dayOnlyString, textColor, textScale);
            }

            case TIME_ONLY -> {
                String timeOnlyString = String.format("%02d:%02d", hours, minutes);
                renderOneLine(context, client.textRenderer, config.trackerPosition, timeOnlyString, textColor, textScale);
            }
        }
    }

    private void renderOneLine(DrawContext context, TextRenderer textRenderer, TrackerPosition position, String line, int textColor, float textScale) {
        int textWidth = scaleDimension(textRenderer.getWidth(line), textScale);
        int textHeight = scaleDimension(LINE_HEIGHT, textScale);

        RenderPosition renderPos = calculateRenderPosition(context, position, textWidth, textHeight);

        drawScaledText(context, textRenderer, line, renderPos.x, renderPos.y, textColor, textScale);
    }

    private void renderTwoLines(DrawContext context, TextRenderer textRenderer, TrackerPosition position, String line1, String line2, int textColor, float textScale) {
        int textWidth = Math.max(scaleDimension(textRenderer.getWidth(line1), textScale), scaleDimension(textRenderer.getWidth(line2), textScale));
        int textHeight = scaleDimension(LINE_HEIGHT * 2 + GAP, textScale);

        RenderPosition renderPos = calculateRenderPosition(context, position, textWidth, textHeight);
        int secondLineOffset = LINE_HEIGHT + GAP;

        drawScaledText(context, textRenderer, line1, renderPos.x, renderPos.y, textColor, textScale);
        drawScaledText(context, textRenderer, line2, renderPos.x, renderPos.y + scaleDimension(secondLineOffset, textScale), textColor, textScale);
    }

    private void drawScaledText(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color, float scale) {
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float) x, (float) y);
        context.getMatrices().scale(scale, scale);
        context.drawText(textRenderer, text, 0, 0, color, true);
        context.getMatrices().popMatrix();
    }

    private int scaleDimension(int baseValue, float scale) {
        return Math.max(1, Math.round(baseValue * scale));
    }

    private RenderPosition calculateRenderPosition(DrawContext context, TrackerPosition position, int textWidth, int textHeight) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

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
