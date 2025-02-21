package dev.kingrabbit.slashbed;

import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.TeleportTarget;

public class TeleportTask {

    public final ServerPlayerEntity player;
    public int tick = 0;
    public int secondsRemaining;

    public TeleportTask(ServerPlayerEntity player) {
        this.player = player;
        this.secondsRemaining = SlashBed.CONFIG.delay;

        player.networkHandler.sendPacket(new TitleFadeS2CPacket(0, 10, 5));
    }

    /**
     * Tick the teleport task
     * @return If the task should run again next tick
     */
    public boolean tick() {
        tick++;

        if (tick != 1) {
            if (tick >= 20) tick = 0;
            return true;
        }

        if (secondsRemaining <= 0) {
            player.teleportTo(player.getRespawnTarget(false, TeleportTarget.NO_OP));
            player.sendMessage(Text.literal(SlashBed.CONFIG.teleportedMessage).formatted(Formatting.GREEN));
            player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.BLOCKS, 1.0f, 1.0f);
            return false;
        }

        player.playSoundToPlayer(SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.BLOCKS, 1.0f, 1.0f);
        player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(SlashBed.CONFIG.teleportingTitle).formatted(Formatting.GREEN)));
        player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal(secondsRemaining + "").formatted(Formatting.GREEN)));

        secondsRemaining--;
        return true;
    }

}
