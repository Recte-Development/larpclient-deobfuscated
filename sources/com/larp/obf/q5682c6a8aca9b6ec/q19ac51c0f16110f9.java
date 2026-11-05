package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class q19ac51c0f16110f9 {
   @Inject(method = "getModelTint", at = @At("RETURN"), cancellable = true)
   private void T(LivingEntityRenderState var1, CallbackInfoReturnable<Integer> var2) {
      HitFXModule var3 = HitFXModule.y();
      if (var1.hasRedOverlay && var3 != null && var3.b()) {
         var2.setReturnValue(var3.p());
      }
   }
}
