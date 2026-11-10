package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.FakeFPSModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.debug.DebugEntryFps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugEntryFps.class)
public class q2c358d06e3350036 {
   @ModifyExpressionValue(method = "display", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getFps()I"))
   private int d(int var1) {
      return FakeFPSModule.E(var1);
   }
}
