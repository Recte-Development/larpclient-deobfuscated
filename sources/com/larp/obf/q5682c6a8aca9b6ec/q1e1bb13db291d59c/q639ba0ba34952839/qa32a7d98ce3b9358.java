package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q639ba0ba34952839;

import com.larpclient.RealisticWaterModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class qa32a7d98ce3b9358 {
   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void M(CallbackInfo var0) {
      if (RealisticWaterModule.t()) {
         var0.cancel();
      }
   }
}
