package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.GUIAnimationModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class qa6be4b5f97052f32 {
   @Shadow
   private long uploadedTime;

   @Inject(method = "getVisibility", at = @At("RETURN"), cancellable = true)
   private void j(long var1, long var3, CallbackInfoReturnable<Float> var5) {
      GUIAnimationModule var6 = GUIAnimationModule.s();
      if (var6 != null && var6.c()) {
         var5.setReturnValue((float)var6.s((double)(var1 - this.uploadedTime) / var6.T()));
      }
   }
}
