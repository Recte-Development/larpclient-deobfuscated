package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.FakeFPSModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.debug.DebugEntryFps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugEntryFps.class)
public class q8e11dfcc97c204df {
   @ModifyExpressionValue(method = "display", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getFps()I"))
   private int N(int var1) {
      return FakeFPSModule.p(var1);
   }
}
