package me.duchiru.daytimecount.hud;

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

        // 2. Render Text at Top-Left
        context.drawText(client.textRenderer, dayString, PADDING_X, PADDING_Y, 0xFFFFFFFF, true);
        context.drawText(client.textRenderer, timeString, PADDING_X, PADDING_Y + LINE_HEIGHT + GAP, 0xFFFFFFFF, true);
    }
}
