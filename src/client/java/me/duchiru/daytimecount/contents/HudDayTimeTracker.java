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
    private record RenderPosition(int x, int y) {}

    private static final int PADDING_X = 4;
    private static final int PADDING_Y = 4;
    private static final int LINE_HEIGHT = 9;
    private static final int GAP = 2;
    private static final int HOTBAR_HEIGHT = 40;

    @Override
    public void render(@NonNull DrawContext context, @NonNull RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.options.hudHidden) return;

        // 1. Calculate days and time
        long timeOfDay = client.world.getTimeOfDay() % 24000;
        long days = client.world.getTimeOfDay() / 24000 + 1; // 1-based days count

        int hours = (int) ((timeOfDay / 1000 + 6) % 24);
        int minutes = (int) ((timeOfDay % 1000) * 60 / 1000);

        // 2. Decide how to display time based on style from config
        TrackerStyle trackerStyle = ModConfig.getConfig().trackerStyle;
        switch (trackerStyle) {
            case DEFAULT -> {
                String dayString = String.format("Day %d", days);
                String timeString = String.format("Time %02d:%02d", hours, minutes);
                renderTwoLines(context, client.textRenderer, ModConfig.getConfig().trackerPosition, dayString, timeString);
            }

            case COMPACT -> {
                String dayTimeString = String.format("Day %d - %02d:%02d", days, hours, minutes);
                renderOneLine(context, client.textRenderer, ModConfig.getConfig().trackerPosition, dayTimeString);
            }

            case DAY_ONLY -> {
                String dayOnlyString = String.format("Day %d", days);
                renderOneLine(context, client.textRenderer, ModConfig.getConfig().trackerPosition, dayOnlyString);
            }

            case TIME_ONLY -> {
                String timeOnlyString = String.format("%02d:%02d", hours, minutes);
                renderOneLine(context, client.textRenderer, ModConfig.getConfig().trackerPosition, timeOnlyString);
            }
        }
    }

    private void renderOneLine(DrawContext context, TextRenderer textRenderer, TrackerPosition position, String line) {
        int textWidth = textRenderer.getWidth(line);

        RenderPosition renderPos = calculateRenderPosition(context, position, textWidth, LINE_HEIGHT);

        context.drawText(textRenderer, line, renderPos.x, renderPos.y, 0xFFFFFFFF, true);
    }

    private void renderTwoLines(DrawContext context, TextRenderer textRenderer, TrackerPosition position, String line1, String line2) {
        int textWidth = Math.max(textRenderer.getWidth(line1), textRenderer.getWidth(line2));
        int textHeight = LINE_HEIGHT * 2 + GAP;

        RenderPosition renderPos = calculateRenderPosition(context, position, textWidth, textHeight);

        context.drawText(textRenderer, line1, renderPos.x, renderPos.y, 0xFFFFFFFF, true);
        context.drawText(textRenderer, line2, renderPos.x, renderPos.y + LINE_HEIGHT + GAP, 0xFFFFFFFF, true);
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
}
