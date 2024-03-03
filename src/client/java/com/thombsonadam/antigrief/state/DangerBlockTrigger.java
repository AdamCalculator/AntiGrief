package com.thombsonadam.antigrief.state;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class DangerBlockTrigger implements Trigger {
    private final BlockPos pos;

    public DangerBlockTrigger(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public double getDistanceToClient() {
        return Math.sqrt(pos.getSquaredDistance(MinecraftClient.getInstance().player.getPos()));
    }

    @Override
    public String getDescription() {
        String block = "???";
        try {
            block = MinecraftClient.getInstance().world.getBlockState(pos).getBlock().getName().asTruncatedString(99);
        } catch (Exception ignored) {
            // empty
        }
        return String.format("Block at %s (%s)", pos.toShortString(), block);
    }


}
