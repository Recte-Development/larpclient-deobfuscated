package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class qdbba1540d7fdb48a {
   @Inject(method = "getModelTint", at = @At("RETURN"), cancellable = true)
   private void D(LivingEntityRenderState var1, CallbackInfoReturnable<Integer> var2) {
      HitFXModule var3 = HitFXModule.N();
      if (var1.hasRedOverlay && var3 != null && var3.V()) {
         var2.setReturnValue(var3.b());
      }
   }
}
