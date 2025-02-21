package dev.kingrabbit.slashbed;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlashBed implements ModInitializer {

	public static final String MOD_ID = "slashbed";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("SlashBed initialising");
	}

}
