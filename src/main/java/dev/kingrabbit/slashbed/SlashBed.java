package dev.kingrabbit.slashbed;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.TeleportTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlashBed implements ModInitializer {

	public static final String MOD_ID = "slashbed";
	public static final String VERSION = "1.0";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Config CONFIG = new Config();

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
				context.getSource().sendFeedback(() -> Text.literal(CONFIG.teleportedMessage).formatted(Formatting.GREEN), false);
				return 1;
			}));

			dispatcher.register(CommandManager.literal("slashbed").requires(source -> source.hasPermissionLevel(2)).executes(context -> {
				context.getSource().sendFeedback(() -> Text.literal("Running SlashBed v" + VERSION + " (modid: " + MOD_ID + ")").formatted(Formatting.GREEN), false);
				return 1;
			}).then(CommandManager.literal("reload").executes(context -> {
				CONFIG = Config.loadOrCreateConfig();
				context.getSource().sendFeedback(() -> Text.literal("Reloaded SlashBed's config.").formatted(Formatting.GREEN), false);
                return 1;
            })));
		});

		ServerLifecycleEvents.SERVER_STARTING.register((s) -> CONFIG = Config.loadOrCreateConfig());
	}

}
