package com.thombsonadam.antigrief.mixin;

import com.mojang.authlib.GameProfile;
import com.thombsonadam.antigrief.HasDangerLevel;
import com.thombsonadam.antigrief.PlayerDangerLevel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements HasDangerLevel {
    @Unique
    private PlayerDangerLevel antigrief$playerDangerLevel = PlayerDangerLevel.DEFAULT;
    @Unique
    private long antigrief$lastDanger = 0;

    @Shadow
    public abstract PlayerInventory getInventory();

    @Shadow
    public abstract GameProfile getGameProfile();

    @Unique
    @Override
    public PlayerDangerLevel antigrief$getPlayerDangerLevel() {
        return antigrief$playerDangerLevel;
    }

    @Override
    public void antiGrief$recalculateDangerLevel() {
        if ((System.currentTimeMillis() - antigrief$lastDanger) > 1000 * 60) {
            antigrief$playerDangerLevel = PlayerDangerLevel.check(getInventory());
        }

        if (antigrief$playerDangerLevel.isPVP()) {
            antigrief$lastDanger = System.currentTimeMillis();
        }
    }

    @Override
    public String antigrief$name() {
        return getGameProfile().getName();
    }
}
