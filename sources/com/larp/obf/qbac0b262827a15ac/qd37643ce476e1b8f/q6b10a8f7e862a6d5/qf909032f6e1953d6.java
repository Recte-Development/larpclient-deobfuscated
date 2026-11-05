package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q6b10a8f7e862a6d5;

import com.larpclient.RealisticWaterModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class qf909032f6e1953d6 {
   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void w(CallbackInfo var0) {
      if (RealisticWaterModule.X()) {
         var0.cancel();
      }
   }
}
