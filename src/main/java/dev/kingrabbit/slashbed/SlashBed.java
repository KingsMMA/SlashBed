package dev.kingrabbit.slashbed;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.world.TeleportTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlashBed implements ModInitializer {

	public static final String MOD_ID = "slashbed";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("SlashBed initialising");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("bed").executes(context -> {
				if (!context.getSource().isExecutedByPlayer()) {
					context.getSource().sendError(Text.literal("This command can only be executed by a player."));
					return 1;
				}

				context.getSource().getPlayer().teleportTo(context.getSource().getPlayer().getRespawnTarget(false, TeleportTarget.NO_OP));
				context.getSource().sendFeedback(() -> Text.literal("Teleported to bed spawn location."), false);
				return 1;
			}));
		});
	}

}
