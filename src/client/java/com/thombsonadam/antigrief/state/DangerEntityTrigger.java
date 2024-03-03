package com.thombsonadam.antigrief.state;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;

public class DangerEntityTrigger implements Trigger {
    private final Entity entity;

    public DangerEntityTrigger(Entity entity) {
        this.entity = entity;
    }

    @Override
    public double getDistanceToClient() {
        return entity.distanceTo(MinecraftClient.getInstance().player);
    }

    @Override
    public String getDescription() {
        return String.format("Entity %s at %s", Registries.ENTITY_TYPE.getId(entity.getType()), entity.getBlockPos().toShortString());
    }
}
