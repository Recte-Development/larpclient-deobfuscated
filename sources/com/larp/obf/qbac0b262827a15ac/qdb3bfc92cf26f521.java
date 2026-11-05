package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class qdb3bfc92cf26f521 {
   @Inject(method = "getModelTint", at = @At("RETURN"), cancellable = true)
   private void W(LivingEntityRenderState var1, CallbackInfoReturnable<Integer> var2) {
      HitFXModule var3 = HitFXModule.q();
      if (var1.hasRedOverlay && var3 != null && var3.q()) {
         var2.setReturnValue(var3.h());
      }
   }
}
