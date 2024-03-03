package com.thombsonadam.antigrief;

import com.mojang.logging.LogUtils;
import com.thombsonadam.antigrief.state.State;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;

import java.util.Set;

public class Mod implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<Item> DANGER_ITEMS = Set.of(
            Items.END_CRYSTAL,
            Items.OBSIDIAN,
            Items.TNT_MINECART,
            Items.TNT,
            Items.RESPAWN_ANCHOR
    );

    private static final Set<EntityType<?>> DANGER_ENTITIES = Set.of(
            EntityType.END_CRYSTAL,
            EntityType.TNT,
            EntityType.TNT_MINECART
    );
    public static StringBuilder debugLine = new StringBuilder();

    private static final CheckTask checkTask = new CheckTask();
    private static long lastKickedAt = 0;

    public static void kick(State state) {
        String kickReason = "§l§c[AntiGrief]§r§c mod kicked!§r\n"+state.kickAddiction();
        Mod.LOGGER.warn("KICKED.\nReason = " + kickReason);

        lastKickedAt = System.currentTimeMillis();
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            client.getNetworkHandler().getConnection().disconnect(Text.literal(kickReason));
        } catch (Exception e) {

        }
    }

    public static String getDebugLine() {
        return debugLine.toString();
    }

    public static boolean isDisabled() {
        return System.currentTimeMillis() - lastKickedAt < 1000 * 15;
    }

    public static boolean isTotemExists(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                return true;
            }
        }
        return false;
    }

    public static void blockState(BlockPos pos, BlockState state) {
        if (state.getBlock() == Blocks.RESPAWN_ANCHOR) {
            RespawnAnchors.setAnchor(pos);
        }

        if (state.getBlock() == Blocks.AIR) {
            RespawnAnchors.unsetAnchor(pos);
        }
    }

    @Override
    public void onInitializeClient() {
        Thread thread = new Thread(checkTask);
        thread.setName("AntiGrief-CheckTaskThread");
        thread.start();
    }

    public static Set<Item> getDanderItems() {
        return DANGER_ITEMS;
    }

    public static Set<EntityType<?>> getDanderEntities() {
        return DANGER_ENTITIES;
    }
}
