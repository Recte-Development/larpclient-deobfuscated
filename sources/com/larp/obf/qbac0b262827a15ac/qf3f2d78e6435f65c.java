package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.PerformanceModule;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.OptionsRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class qf3f2d78e6435f65c {
   @Shadow
   public abstract GameRenderState gameRenderState();

   @Inject(method = "extractOptions()V", at = @At("TAIL"))
   private void a(CallbackInfo var1) {
      OptionsRenderState var2 = this.gameRenderState().optionsRenderState;
      var2.prioritizeChunkUpdates = PerformanceModule.A(var2.prioritizeChunkUpdates);
      var2.cloudStatus = PerformanceModule.f(var2.cloudStatus);
   }
}
