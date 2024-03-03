package com.thombsonadam.antigrief.state;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class DangerPlayerTrigger implements Trigger {
    private final PlayerEntity player;

    public DangerPlayerTrigger(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public double getDistanceToClient() {
        return player.distanceTo(MinecraftClient.getInstance().player);
    }

    @Override
    public String getDescription() {
        return "Player " + player.getGameProfile().getName() + " is danger";
    }
}
