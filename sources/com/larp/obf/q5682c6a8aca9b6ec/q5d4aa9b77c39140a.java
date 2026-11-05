package com.larp.obf.q5682c6a8aca9b6ec;

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
public abstract class q5d4aa9b77c39140a {
   @Shadow
   public abstract GameRenderState gameRenderState();

   @Inject(method = "extractOptions()V", at = @At("TAIL"))
   private void B(CallbackInfo var1) {
      OptionsRenderState var2 = this.gameRenderState().optionsRenderState;
      var2.prioritizeChunkUpdates = PerformanceModule.t(var2.prioritizeChunkUpdates);
      var2.cloudStatus = PerformanceModule.B(var2.cloudStatus);
   }
}
