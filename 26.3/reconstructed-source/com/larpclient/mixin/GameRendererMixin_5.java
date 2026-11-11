package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.widget.TextInputController;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.render.overlay.MotionBlurRenderer;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.sky.DistanceFogRenderer;
import com.larpclient.render.sky.SkyRenderer2;
import com.larpclient.render.world.WorldColorRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.PerfProfiler;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin_5 {
   @Inject(method = "render", at = @At("HEAD"))
   private void l(CallbackInfo var1) {
      StreamProofManager.endWorldLayer();
      PostProcessing.beginFrame();
      SkyRenderer2.tick();
      SkyShaderModule var2 = SkyShaderModule.getInstance();
      if (var2 != null) {
         var2.usesCoreShader();
      }
   }

   @Inject(method = "extract", at = @At("HEAD"))
   private void i(DeltaTracker var1, boolean var2, CallbackInfo var3) {
      StreamProofManager.tick();
      TickTimer.updatePartialTick(var1.getGameTimeDeltaPartialTick(false));
      PostProcessing.resetBlur();
      PerfProfiler.endFrame();
      TextInputController.update();
   }

   @ModifyArg(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;"
      )
   )
   private Matrix4f n(Matrix4f var1) {
      DistanceFogRenderer.captureProjection(var1);
      return var1;
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void j(CallbackInfo var1) {
      MotionBlurRenderer.render();
      WorldColorRenderer.render();
      PostProcessing.applyBlur();
   }

   @Inject(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/CommandEncoder;clearDepthTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;D)V"
      )
   )
   private void v(CallbackInfo var1) {
      StreamProofManager.statusText();
   }

   @Inject(method = "render", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void k(CallbackInfo var1) {
      StreamProofManager.submitFrame();
   }
}
