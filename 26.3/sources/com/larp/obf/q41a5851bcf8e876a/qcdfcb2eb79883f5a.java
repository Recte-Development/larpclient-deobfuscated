package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.w7;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class qcdfcb2eb79883f5a {
   @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
   private void G(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4, CallbackInfoReturnable<RenderType> var5) {
      SelfVisualsModule var6 = SelfVisualsModule.v();
      if (var6 != null && var6.isEnabled() && !w7.x() && SelfVisualsModule.u(var1)) {
         var5.setReturnValue(null);
      }
   }
}
