package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class q3edf5aa297853301 {
   @Shadow
   private long uploadedTime;

   @ModifyReturnValue(method = "getVisibility", at = @At("RETURN"))
   private float U(float var1, long var2) {
      GUIAnimationModule var4 = GUIAnimationModule.a();
      return var4 != null && var4.isEnabled() && !var4.h.getValue() && var4.u() ? (float)var4.R((double)(var2 - this.uploadedTime) / var4.n()) : var1;
   }
}
