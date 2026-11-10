package com.larp.obf.qa300483f54d804a3;

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
public abstract class q913149bb8a8c3687 {
   @Shadow
   public abstract GameRenderState gameRenderState();

   @Inject(method = "extractOptions()V", at = @At("TAIL"))
   private void D(CallbackInfo var1) {
      OptionsRenderState var2 = this.gameRenderState().optionsRenderState;
      var2.prioritizeChunkUpdates = PerformanceModule.y(var2.prioritizeChunkUpdates);
      var2.cloudStatus = PerformanceModule.k(var2.cloudStatus);
   }
}
