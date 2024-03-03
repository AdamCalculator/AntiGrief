package com.thombsonadam.antigrief;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class PlayerDangerLevel {
    public static final PlayerDangerLevel DEFAULT = new PlayerDangerLevel(true, false);

    private final boolean isNormal;
    private final boolean is100PercentPvp;

    public PlayerDangerLevel(boolean isNormal, boolean is100PercentPvp) {
        this.isNormal = isNormal;
        this.is100PercentPvp = is100PercentPvp;
    }

    public boolean isNotNormal() {
        return !isNormal;
    }

    public boolean isPVP() {
        return is100PercentPvp;
    }

    public static PlayerDangerLevel check(PlayerInventory inventory) {
        ItemStack stack = inventory.getMainHandStack();
        if (Mod.getDanderItems().contains(stack.getItem())) {
            if (stack.isOf(Items.OBSIDIAN)) {
                return new PlayerDangerLevel(false, false);
            }

            return new PlayerDangerLevel(false, true);
        }
        return DEFAULT;
    };
}
