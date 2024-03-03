package com.thombsonadam.antigrief;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;

import java.util.HashSet;
import java.util.Set;

public class RespawnAnchors {

    private static final Set<BlockPos> knownAnchors = new HashSet<>();

    public static void setAnchor(BlockPos pos) {
        knownAnchors.add(pos);
    }

    public static void unsetAnchor(BlockPos pos) {
        knownAnchors.remove(pos);
    }

    public boolean isAnchor(BlockPos pos) {
        return knownAnchors.contains(pos);
    }

    public static BlockPos getClosestBlock(Position position) {
        if (knownAnchors.isEmpty()) {
            return null;
        }

        double minDistance = Double.MAX_VALUE;

        BlockPos ret = null;
        for (BlockPos knownAnchor : knownAnchors) {
            double squaredDistance = knownAnchor.getSquaredDistance(position);

            if (squaredDistance < minDistance) {
                minDistance = squaredDistance;
                ret = knownAnchor;
            }
        }

        return ret;
    }
}
