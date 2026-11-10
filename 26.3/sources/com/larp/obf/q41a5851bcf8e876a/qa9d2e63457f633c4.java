package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class qa9d2e63457f633c4 {
   @Shadow
   private long uploadedTime;

   @ModifyReturnValue(method = "getVisibility", at = @At("RETURN"))
   private float h(float var1, long var2, long var4) {
      GUIAnimationModule var6 = GUIAnimationModule.O();
      return var6 != null && var6.isEnabled() && !var6.J.getValue() && var6.O() ? (float)var6.z((double)(var2 - this.uploadedTime) / var6.B()) : var1;
   }
}
