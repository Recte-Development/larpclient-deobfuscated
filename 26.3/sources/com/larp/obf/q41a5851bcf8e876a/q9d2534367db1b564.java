package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class q9d2534367db1b564 {
   @Inject(method = "getModelTint", at = @At("RETURN"), cancellable = true)
   private void U(LivingEntityRenderState var1, CallbackInfoReturnable<Integer> var2) {
      HitFXModule var3 = HitFXModule.U();
      if (var1.hasRedOverlay && var3 != null && var3.T()) {
         var2.setReturnValue(var3.i());
      }
   }
}
