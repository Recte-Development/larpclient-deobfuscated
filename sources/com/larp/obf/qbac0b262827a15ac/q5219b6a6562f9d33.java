package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tj;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class q5219b6a6562f9d33 {
   @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
   private void I(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4, CallbackInfoReturnable<RenderType> var5) {
      if (!tj.r() && SelfVisualsModule.R(var1)) {
         var5.setReturnValue(null);
      }
   }
}
