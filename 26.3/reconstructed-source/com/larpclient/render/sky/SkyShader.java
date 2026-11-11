package com.larpclient.render.sky;

import com.larpclient.hud.HudRenderer;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.util.misc.LarpLog;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;

public abstract class SkyShader implements HudRenderer {
   private final RenderPipeline pipeline;
   private boolean loaded;
   protected double time;
   protected double rotation;

   protected SkyShader(String var1, String... var2) {
      this.pipeline = SkyRenderer.createPipeline(var1, var2);
   }

   public final RenderPipeline getPipeline() {
      return this.pipeline;
   }

   @Override
   public final void render(SkyRenderContext var1) throws IOException {
      if (!this.loaded) {
         ClientPipelines.validatePipeline(this.pipeline);
         this.load(var1);
         this.loaded = (boolean)1;
         LarpLog.info("Sky", "Shader compiled and resources ready: {}", RenderPipelineCompat.getFragmentShaderId(this.pipeline));
      }

      this.advance(var1);
      MemoryStack var2 = MemoryStack.stackPush();

      try {
         ByteBuffer var3 = var2.calloc(160);
         this.write(var1, var3);
         var3.position(0).limit(160);
         var1.state.render(var1, this, var3);
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }
   }

   protected void advance(SkyRenderContext var1) {
      this.time = this.time + var1.partialTick * var1.skyModule.getAnimationSpeed();
      this.rotation = (this.rotation + var1.partialTick * Math.toRadians(var1.skyModule.getRotationSpeed())) % (Math.PI * 2);
   }

   protected void load(SkyRenderContext var1) throws IOException {
   }

   public void bindTextures(RenderPass var1) {
   }

   protected abstract void write(SkyRenderContext var1, ByteBuffer var2);

   protected void writeCommonUniforms(SkyRenderContext var1, ByteBuffer var2) {
      this.writeScaledUniforms(var1, var2, 1.0F, 1.0F);
   }

   protected void writeScaledUniforms(SkyRenderContext var1, ByteBuffer var2, float var3, float var4) {
      CustomSkyModule var5 = var1.skyModule;
      getPipeline(var2, var5.getGradient().zenith());
      getPipeline(var2, var5.getGradient().horizon());
      putVec4(var2, (float)this.time, (float)this.rotation, var5.getExposure(), var5.isHorizonBlend() ? 1.0F : 0.0F);
      putVec4(var2, var5.getStarBrightness() * var3, var5.isShowVanillaMoon() * var4, 0.0F, var5.getAnimationSettings());
      getPipeline(var2, var5.getOceanBaseColor());
      getPipeline(var2, var5.getRotationSpeed());
   }

   protected static void putVec4(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   protected static void getPipeline(ByteBuffer var0, int var1) {
      putVec4(var0, (var1 >> 16 & 0xFF) / 255.0F, (var1 >> 8 & 0xFF) / 255.0F, (var1 & 0xFF) / 255.0F, 1.0F);
   }

   @Override
   public final void reload() {
      this.close();
   }

   @Override
   public void close() {
      this.loaded = (boolean)0;
   }
}
