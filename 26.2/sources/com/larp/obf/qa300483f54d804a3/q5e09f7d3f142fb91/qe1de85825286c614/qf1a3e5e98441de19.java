package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.qe1de85825286c614;

import com.larpclient.RealisticWaterModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class qf1a3e5e98441de19 {
   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void o(CallbackInfo var0) {
      if (RealisticWaterModule.c()) {
         var0.cancel();
      }
   }
}
