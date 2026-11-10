package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q6780db403bf6e5d8;

import com.larpclient.RealisticWaterModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class q2347103d80e1733c {
   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void Q(CallbackInfo var0) {
      if (RealisticWaterModule.Z()) {
         var0.cancel();
      }
   }
}
