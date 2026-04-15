package me.duchiru.daytimecount.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.multiplayer.ClientLevel;

public class NewDayInvoker {
    public static long lastDays = -1;

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            ClientLevel world = client.level;
            if (world == null) return;

            long days = world.getOverworldClockTime() / 24000;

            if (days > lastDays) {
                NewDayCallback.EVENT.invoker().onNewDay(client, days);
                lastDays = days;
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            lastDays = -1;
        });
    }
}
