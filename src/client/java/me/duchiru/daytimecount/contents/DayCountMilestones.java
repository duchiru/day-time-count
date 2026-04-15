package me.duchiru.daytimecount.contents;

import me.duchiru.daytimecount.config.Milestone;
import me.duchiru.daytimecount.config.ModConfig;
import me.duchiru.daytimecount.events.NewDayCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class DayCountMilestones implements NewDayCallback {
    private static ChatFormatting parseFormatting(String colorName, ChatFormatting fallback) {
        if (colorName == null) return fallback;

        ChatFormatting formatting = ChatFormatting.getByName(colorName);
        return formatting != null ? formatting : fallback;
    }

    private static SoundEvent parseSound(String soundId, SoundEvent fallback) {
        if (soundId == null || soundId.isBlank()) return fallback;

        Identifier identifier = Identifier.tryParse(soundId);
        if (identifier == null) return fallback;

        SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.getValue(identifier);
        return soundEvent != null ? soundEvent : fallback;
    }

    @Override
    public void onNewDay(Minecraft client, long days) {
        if (client.player == null || ModConfig.getConfig() == null || ModConfig.getConfig().milestones == null) return;

        long triggerDay = days + 1;
        Milestone milestone = ModConfig.getConfig().milestones.get(triggerDay);
        if (milestone == null) return;

        ChatFormatting titleFormatting = parseFormatting(milestone.titleColor, ChatFormatting.WHITE);
        ChatFormatting subtitleFormatting = parseFormatting(milestone.subtitleColor, ChatFormatting.GRAY);
        SoundEvent sound = parseSound(milestone.sound, SoundEvents.EXPERIENCE_ORB_PICKUP);

        client.gui.setTimes(10, 60, 20);
        client.gui.setTitle(Component.literal(milestone.title).withStyle(titleFormatting));
        client.gui.setSubtitle(Component.literal(milestone.subtitle).withStyle(subtitleFormatting));
        client.player.playSound(sound, 1.0f, 1.0f);
    }
}