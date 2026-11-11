package com.larpclient.mixin;

import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.model.ModelVisualRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin_4 {
   @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
   private void G(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4, CallbackInfoReturnable<RenderType> var5) {
      SelfVisualsModule var6 = SelfVisualsModule.getPrimaryColor();
      if (var6 != null && var6.isEnabled() && !ModelVisualRenderer.isProbing() && SelfVisualsModule.shouldStyleRenderState(var1)) {
         var5.setReturnValue(null);
      }
   }
}
