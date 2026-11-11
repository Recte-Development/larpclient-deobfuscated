package com.larpclient.mixin;

import com.larpclient.module.misc.FakeFPSModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.debug.DebugEntryFps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugEntryFps.class)
public class DebugEntryFpsMixin {
   @ModifyExpressionValue(method = "display", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getFps()I"))
   private int d(int var1) {
      return FakeFPSModule.getDisplayedFps(var1);
   }
}
