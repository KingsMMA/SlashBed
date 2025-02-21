package dev.kingrabbit.slashbed;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.TeleportTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SlashBed implements ModInitializer {

	public static final String MOD_ID = "slashbed";
	public static final String VERSION = "1.0";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Config CONFIG = new Config();

	public List<TeleportTask> tasks = new ArrayList<>();

	@Override
	public void onInitialize() {
		LOGGER.info("SlashBed initialising");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("bed").executes(context -> {
				ServerCommandSource source = context.getSource();
				if (!source.isExecutedByPlayer()) {
					source.sendError(Text.literal("This command can only be executed by a player."));
					return 1;
				}

				if (CONFIG.delay <= 0) {
					source.getPlayer().teleportTo(source.getPlayer().getRespawnTarget(false, TeleportTarget.NO_OP));
					source.sendFeedback(() -> Text.literal(CONFIG.teleportedMessage).formatted(Formatting.GREEN), false);
					source.getPlayer().playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.BLOCKS, 1.0f, 1.0f);
					return 1;
				}

				ServerPlayerEntity player = source.getPlayer();
				tasks.add(new TeleportTask(player));

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

		ServerTickEvents.END_SERVER_TICK.register(serverWorld -> tasks = tasks
                .stream()
                .filter(TeleportTask::tick)
                .collect(Collectors.toCollection(ArrayList::new)));

		ServerLifecycleEvents.SERVER_STARTING.register((s) -> CONFIG = Config.loadOrCreateConfig());
	}

}
