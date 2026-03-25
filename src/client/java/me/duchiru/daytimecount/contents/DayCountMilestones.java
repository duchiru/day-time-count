package me.duchiru.daytimecount.contents;

import me.duchiru.daytimecount.events.NewDayCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DayCountMilestones implements NewDayCallback {
    @Override
    public void onNewDay(MinecraftClient client, long days) {
        if (client.player == null) return;

        if (days == 0) {
            client.inGameHud.setTitleTicks(10, 100, 20);
            client.inGameHud.setTitle(Text.literal("Welcome to Minecraft!").formatted(Formatting.RED, Formatting.BOLD));
            client.inGameHud.setSubtitle(Text.literal("Happy mining!").formatted(Formatting.WHITE, Formatting.ITALIC));
            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.8f);
            return;
        }

        if (days == 6) { // Day 7 (One Week)
            client.inGameHud.setTitleTicks(10, 70, 20);
            client.inGameHud.setTitle(Text.literal("One Week Survived").formatted(Formatting.GREEN));
            client.inGameHud.setSubtitle(Text.literal("You're getting the hang of this!").formatted(Formatting.GRAY));
            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            return;
        }

        if (days == 9) { // Day 10
            client.inGameHud.setTitleTicks(10, 70, 20);
            client.inGameHud.setTitle(Text.literal("Double Digits!").formatted(Formatting.GOLD));
            client.inGameHud.setSubtitle(Text.literal("Day 10 reached.").formatted(Formatting.YELLOW));
            client.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            return;
        }

        if (days == 99) { // Day 100
            client.inGameHud.setTitleTicks(20, 140, 40); // Stays longer (7 seconds)
            client.inGameHud.setTitle(Text.literal("Wow, 100-Day Challenge!").formatted(Formatting.AQUA, Formatting.BOLD));
            client.inGameHud.setSubtitle(Text.literal("It's time to show off your world!").formatted(Formatting.GREEN));
            client.player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            return;
        }

        if (days == 364) { // Day 365 (One Year)
            client.inGameHud.setTitleTicks(20, 200, 40);
            client.inGameHud.setTitle(Text.literal("HAPPY ANNIVERSARY!").formatted(Formatting.LIGHT_PURPLE, Formatting.OBFUSCATED));
            client.inGameHud.setSubtitle(Text.literal("One entire year in this world.").formatted(Formatting.WHITE));
            client.player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 0.5f);
            return;
        }
    }
}