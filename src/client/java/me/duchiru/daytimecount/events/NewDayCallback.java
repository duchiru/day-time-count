package me.duchiru.daytimecount.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

public interface NewDayCallback {
    Event<NewDayCallback> EVENT = EventFactory.createArrayBacked(NewDayCallback.class, (listeners) -> (client, days) -> {
        for (NewDayCallback listener : listeners) {
            listener.onNewDay(client, days);
        }
    });

    void onNewDay(Minecraft client, long days);
}
