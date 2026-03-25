package me.duchiru.daytimecount;

import me.duchiru.daytimecount.config.ModConfig;
import me.duchiru.daytimecount.contents.DayCountMilestones;
import me.duchiru.daytimecount.events.NewDayCallback;
import me.duchiru.daytimecount.events.NewDayInvoker;
import me.duchiru.daytimecount.contents.HudDayTimeTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;

public class DayTimeCountClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DayTimeCount.LOGGER.info("Initializing client-side...");

        HudElementRegistry.attachElementBefore(VanillaHudElements.MISC_OVERLAYS, Identifier.of(DayTimeCount.MOD_ID, "before_misc"), new HudDayTimeTracker());
        NewDayCallback.EVENT.register(new DayCountMilestones());

        NewDayInvoker.init();
        ModConfig.load();

        DayTimeCount.LOGGER.info("Initialized client-side!");
    }
}