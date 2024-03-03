package com.thombsonadam.antigrief.mixin;

import com.thombsonadam.antigrief.Mod;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {

    @Inject(method = "getLeftText", at = @At("RETURN"))
    private void d(CallbackInfoReturnable<List<String>> cir) {
        List<String> l = cir.getReturnValue();

        l.add("[AntiGrief] " + Mod.getDebugLine());
    }
}
