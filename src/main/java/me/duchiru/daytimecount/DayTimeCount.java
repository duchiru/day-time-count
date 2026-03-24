package me.duchiru.daytimecount;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DayTimeCount implements ModInitializer {
    public static final String MOD_ID = "daytimecount";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing server-side...");

        // Init code

        LOGGER.info("Initialized server-side!");
    }
}