package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.GUIAnimationModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class q7aa08a744ac0ce4d {
   @Shadow
   private long uploadedTime;

   @Inject(method = "getVisibility", at = @At("RETURN"), cancellable = true)
   private void u(long var1, long var3, CallbackInfoReturnable<Float> var5) {
      GUIAnimationModule var6 = GUIAnimationModule.F();
      if (var6 != null && var6.t()) {
         var5.setReturnValue((float)var6.b((double)(var1 - this.uploadedTime) / var6.f()));
      }
   }
}
