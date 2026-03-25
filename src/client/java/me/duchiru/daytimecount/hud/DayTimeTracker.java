package me.duchiru.daytimecount.hud;

import me.duchiru.daytimecount.config.ModConfig;
import me.duchiru.daytimecount.config.TrackerPosition;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.jspecify.annotations.NonNull;

public class DayTimeTracker implements HudElement {
    private static final int PADDING_X = 4;
    private static final int PADDING_Y = 4;
    private static final int LINE_HEIGHT = 9;
    private static final int GAP = 2;
    private static final int HOTBAR_HEIGHT = 36;

    @Override
    public void render(@NonNull DrawContext context, @NonNull RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.options.hudHidden) return;

        // 1. Calculate Time
        long timeOfDay = client.world.getTimeOfDay() % 24000;
        long days = client.world.getTimeOfDay() / 24000 + 1; // 1-based days count

        int hours = (int) ((timeOfDay / 1000 + 6) % 24);
        int minutes = (int) ((timeOfDay % 1000) * 60 / 1000);
        String dayString = String.format("Day %d", days);
        String timeString = String.format("Time %02d:%02d", hours, minutes);

        // 2. Render text based on position from config
        TrackerPosition trackerPosition = ModConfig.getConfig().trackerPosition;

        int textWidth = Math.max(client.textRenderer.getWidth(dayString), client.textRenderer.getWidth(timeString));
        int textHeight = LINE_HEIGHT * 2 + GAP;
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        int rightAlignedX = Math.max(PADDING_X, screenWidth - PADDING_X - textWidth);
        int bottomAlignedY = Math.max(PADDING_Y, screenHeight - PADDING_Y - textHeight);
        int hotbarY = Math.max(PADDING_Y, screenHeight - HOTBAR_HEIGHT - PADDING_Y - textHeight);
        int centeredX = Math.max(PADDING_X, (screenWidth - textWidth) / 2);

        int textX;
        int textY;
        switch (trackerPosition) {
            case TOP_RIGHT -> {
                textX = rightAlignedX;
                textY = PADDING_Y;
            }
            case BOTTOM_LEFT -> {
                textX = PADDING_X;
                textY = bottomAlignedY;
            }
            case BOTTOM_RIGHT -> {
                textX = rightAlignedX;
                textY = bottomAlignedY;
            }
            case HOTBAR -> {
                textX = centeredX;
                textY = hotbarY;
            }
            default -> {
                textX = PADDING_X;
                textY = PADDING_Y;
            }
        }

        context.drawText(client.textRenderer, dayString, textX, textY, 0xFFFFFFFF, true);
        context.drawText(client.textRenderer, timeString, textX, textY + LINE_HEIGHT + GAP, 0xFFFFFFFF, true);
    }
}
