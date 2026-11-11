package com.larpclient.mixin;

import com.larpclient.module.misc.PerformanceModule;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.OptionsRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
   @Shadow
   public abstract GameRenderState gameRenderState();

   @Inject(method = "extractOptions()V", at = @At("TAIL"))
   private void z(CallbackInfo var1) {
      OptionsRenderState var2 = this.gameRenderState().optionsRenderState;
      var2.prioritizeChunkUpdates = PerformanceModule.adjustChunkUpdates(var2.prioritizeChunkUpdates);
      var2.cloudStatus = PerformanceModule.adjustCloudStatus(var2.cloudStatus);
   }
}
