package com.thombsonadam.antigrief.mixin;

import com.thombsonadam.antigrief.Mod;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(at = @At("HEAD"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z")
    private void d1(BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Mod.blockState(pos, state);
    }


    @Inject(at = @At("HEAD"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z")
    private void d2(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
        Mod.blockState(pos, state);
    }

    @Inject(at = @At("HEAD"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z")
    private void d3(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        Mod.blockState(pos, state);
    }
}
