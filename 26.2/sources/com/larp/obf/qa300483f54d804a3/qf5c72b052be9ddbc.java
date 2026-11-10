package com.larp.obf.qa300483f54d804a3;

import com.larpclient.j4;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class qf5c72b052be9ddbc {
   @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
   private void L(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4, CallbackInfoReturnable<RenderType> var5) {
      SelfVisualsModule var6 = SelfVisualsModule.k();
      if (var6 != null && var6.isEnabled() && !j4.X() && SelfVisualsModule.H(var1)) {
         var5.setReturnValue(null);
      }
   }
}
