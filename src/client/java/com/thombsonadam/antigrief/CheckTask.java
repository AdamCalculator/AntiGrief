package com.thombsonadam.antigrief;

import com.thombsonadam.antigrief.state.DangerBlockTrigger;
import com.thombsonadam.antigrief.state.DangerEntityTrigger;
import com.thombsonadam.antigrief.state.DangerPlayerTrigger;
import com.thombsonadam.antigrief.state.State;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CheckTask implements Runnable {
    private MinecraftClient client;
    private final State state = new State();
    private boolean isRunning = true;

    @Override
    public void run() {
        client = MinecraftClient.getInstance();
        while (isRunning) {
            if (client.world == null || state.isParkTask()) {
                // ~~deep sleep in menu
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Mod.debugLine = new StringBuilder("CheckTask is parked");
                continue;
            }


            try {
                Thread.sleep(state.calcDelay());
                run0();
            } catch (Exception e) {
                Mod.LOGGER.info("Error: " + e);
            }
        }

    }

    private void run0() {
        if (client.world == null || client.player == null) {
            return;
        }

        state.updateTotemState(Mod.isTotemExists(client.player));

        state.resetStart();
        for (Entity entity : client.world.getEntities()) {
            // skip client-player
            if (entity instanceof ClientPlayerEntity) {
                continue;
            }

            final double distanceSquared = entity.squaredDistanceTo(client.player);
            final EntityType<?> type = entity.getType();

            if (Mod.getDanderEntities().contains(type)) {
                state.addTrigger(new DangerEntityTrigger(entity));
            }

            if (((Object)entity) instanceof HasDangerLevel dangerLevel) {
                if (distanceSquared < 10*10) {
                    state.playersInDangerZone = true;
                }
                dangerLevel.antiGrief$recalculateDangerLevel(); // recalc

                final PlayerDangerLevel pdl = dangerLevel.antigrief$getPlayerDangerLevel();

                if (pdl.isNotNormal()) {
                    if (dangerLevel instanceof PlayerEntity player) {
                        state.addTrigger(new DangerPlayerTrigger(player));
                    }
                }
            }
        }

        BlockPos closestBlock = RespawnAnchors.getClosestBlock(client.player.getPos());
        if (closestBlock != null) {
            double sq = closestBlock.getSquaredDistance(client.player.getPos());
            if (sq < 400) {
                state.addTrigger(new DangerBlockTrigger(closestBlock));
            }
        }

        state.resetEnd();

        state.updateTotemState(Mod.isTotemExists(client.player));
    }
}
