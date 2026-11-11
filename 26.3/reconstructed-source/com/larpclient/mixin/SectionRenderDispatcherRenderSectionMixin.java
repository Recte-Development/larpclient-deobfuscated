package com.larpclient.mixin;

import com.larpclient.module.render.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class SectionRenderDispatcherRenderSectionMixin {
   @Shadow
   private long uploadedTime;

   @ModifyReturnValue(method = "getVisibility", at = @At("RETURN"))
   private float h(float var1, long var2, long var4) {
      GUIAnimationModule var6 = GUIAnimationModule.getInstance();
      return var6 != null && var6.isEnabled() && !var6.nativeChunks.getValue() && var6.getInstance()
         ? (float)var6.applyChunkEasing((double)(var2 - this.uploadedTime) / var6.getDurationMillis())
         : var1;
   }
}
