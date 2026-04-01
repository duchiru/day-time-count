package me.duchiru.daytimecount.contents;

import me.duchiru.daytimecount.config.Milestone;
import me.duchiru.daytimecount.config.ModConfig;
import me.duchiru.daytimecount.events.NewDayCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class DayCountMilestones implements NewDayCallback {
    private static Formatting parseFormatting(String colorName, Formatting fallback) {
        if (colorName == null) return fallback;

        Formatting formatting = Formatting.byName(colorName);
        return formatting != null ? formatting : fallback;
    }

    private static SoundEvent parseSound(String soundId, SoundEvent fallback) {
        if (soundId == null || soundId.isBlank()) return fallback;

        Identifier identifier = Identifier.tryParse(soundId);
        if (identifier == null) return fallback;

        SoundEvent soundEvent = Registries.SOUND_EVENT.get(identifier);
        return soundEvent != null ? soundEvent : fallback;
    }

    @Override
    public void onNewDay(MinecraftClient client, long days) {
        if (client.player == null || ModConfig.getConfig() == null || ModConfig.getConfig().milestones == null) return;

        long triggerDay = days + 1;
        Milestone milestone = ModConfig.getConfig().milestones.get(triggerDay);
        if (milestone == null) return;

        Formatting titleFormatting = parseFormatting(milestone.titleColor, Formatting.WHITE);
        Formatting subtitleFormatting = parseFormatting(milestone.subtitleColor, Formatting.GRAY);
        SoundEvent sound = parseSound(milestone.sound, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

        client.inGameHud.setTitleTicks(10, 60, 20);
        client.inGameHud.setTitle(Text.literal(milestone.title).formatted(titleFormatting));
        client.inGameHud.setSubtitle(Text.literal(milestone.subtitle).formatted(subtitleFormatting));
        client.player.playSound(sound, 1.0f, 1.0f);
    }
}