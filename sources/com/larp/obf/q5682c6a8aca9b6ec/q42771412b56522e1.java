package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.uv;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class q42771412b56522e1 {
   @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
   private void f(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4, CallbackInfoReturnable<RenderType> var5) {
      if (!uv.G() && SelfVisualsModule.B(var1)) {
         var5.setReturnValue(null);
      }
   }
}
