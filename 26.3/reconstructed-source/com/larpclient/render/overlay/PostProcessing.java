package com.larpclient.render.overlay;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.render.ChamsModule;
import com.larpclient.module.render.KillFXModule;
import com.larpclient.render.entity.EntityHaloRenderer;
import com.larpclient.render.entity.HandChamsRenderer;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderEffects;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.particle.SoftParticleRenderer;
import com.larpclient.render.world.WorldColorRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.misc.SafetyLevel;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class PostProcessing {
   public static PostProcessing.Quality bloomQuality = PostProcessing.Quality.BALANCED;
   public static PostProcessing.Quality blurQuality = PostProcessing.Quality.BALANCED;
   public static Supplier<PostProcessing.Quality> qualityOverrideSupplier = () -> null;
   private static PostProcessing.Quality qualityOverride;
   public static RenderTarget outputTarget;
   private static final GlowFramebuffers bloomBuffer = new GlowFramebuffers();
   private static final GlowFramebuffers blurBuffer = new GlowFramebuffers();
   private static final GlowFramebuffers bloomBufferMedium = new GlowFramebuffers();
   private static final GlowFramebuffers bloomBufferLarge = new GlowFramebuffers();
   private static boolean disabled;
   private static boolean pipelinesInitialized;
   private static long frameCounter;
   private static long lastBlurFrame = -1L;
   public static int reloadCount;
   private static PostProcessing.Quality lastBloomQuality;
   private static PostProcessing.Quality lastBlurQuality;
   public static float bloomStrength = 0.55F;
   public static float bloomSpread = 1.0F;
   private static GpuBuffer parametersBuffer;
   private static boolean fullscreenBlurRequested;
   private static final List<int[]> blurRegions = new ArrayList<>();
   private static float blurScale = 1.0F;
   private static int extraBlurPasses;
   private static int blurLevel = 2;

   public static PostProcessing.Quality getBloomQuality() {
      PostProcessing.Quality var0 = qualityOverride;
      return var0 != null && var0.ordinal() < bloomQuality.ordinal() ? var0 : bloomQuality;
   }

   private static RenderTarget setBlurLevel() {
      return outputTarget != null ? outputTarget : Minecraft.getInstance().gameRenderer.mainRenderTarget();
   }

   public static int disableForSession(PostProcessing.Quality var0) {
      if (var0 == PostProcessing.Quality.OFF) {
         return 0;
      }

      int var1 = var0.BLURITERATIONS * 2 + 1;
      if (var0.ordinal() >= PostProcessing.Quality.BALANCED.ordinal()) {
         var1 += PostProcessing.Quality.BALANCED.BLURITERATIONS * 2 + 1;
      }

      if (var0.ordinal() >= PostProcessing.Quality.HIGH.ordinal()) {
         var1 += var0.BLURITERATIONS * 2 + 1;
      }

      return var1;
   }

   static int passCount(PostProcessing.Quality var0) {
      return var0 == PostProcessing.Quality.OFF
         ? 0
         : (var0.ordinal() >= PostProcessing.Quality.HIGH.ordinal() ? 3 : (var0 == PostProcessing.Quality.BALANCED ? 2 : 1));
   }

   static float texelOffset(float var0, int var1) {
      if (var1 >= 1 && Float.isFinite(var0) && !(var0 < 0.0F)) {
         return var0 / var1;
      } else {
         throw new IllegalArgumentException("Invalid blur footprint");
      }
   }

   public static void resetBlur() {
      fullscreenBlurRequested = (boolean)0;
      blurRegions.clear();
      setBlurLevel(2);
   }

   public static void setBlurLevel(int var0) {
      blurLevel = Math.clamp(var0, 0, 4);

      blurScale = switch (blurLevel) {
         case 1 -> 0.6F;
         default -> 1.0F;
         case 3 -> 1.4F;
         case 4 -> 1.8F;
      };
      extraBlurPasses = blurLevel >= 3 ? 1 : 0;
   }

   public static boolean isBlurActive() {
      return blurQuality != PostProcessing.Quality.OFF && blurLevel > 0;
   }

   public static void requestFullscreenBlur() {
      if (blurLevel > 0) {
         fullscreenBlurRequested = (boolean)1;
      }
   }

   public static void addBlurRegion(int var0, int var1, int var2, int var3) {
      if (blurLevel > 0 && blurRegions.size() < 32) {
         blurRegions.add(new int[]{var0, var1, var2, var3});
      }
   }

   public static void applyBlur() {
      RoundedShapes.initPipelines();
      SdfText.initialize();
      if (!StreamProofManager.getOverlayGraphics()) {
         drawBlur(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      } else {
         if (fullscreenBlurRequested || !blurRegions.isEmpty()) {
            passCount();
         }
      }
   }

   private static void drawBlur(RenderTarget var0) {
      if (fullscreenBlurRequested) {
         drawFullscreenBlur(var0);
      } else {
         for (int[] var2 : blurRegions) {
            drawBlurRegion(var0, var2[0], var2[1], var2[2], var2[3]);
         }
      }
   }

   public static GpuTextureView getBlurTexture() {
      return passCount() ? blurBuffer.blurPong.getColorTextureView() : null;
   }

   public static boolean resetBlur() {
      return fullscreenBlurRequested;
   }

   public static List<int[]> getBlurRegions() {
      return Collections.unmodifiableList(blurRegions);
   }

   public static void beginFrame() {
      frameCounter++;
      qualityOverride = qualityOverrideSupplier.get();
      SafetyLevel.capLevel(qualityOverride);
      PostProcessing.Quality var0 = getBloomQuality();
      if (lastBloomQuality != var0) {
         bloomBuffer.close();
         bloomBufferMedium.close();
         bloomBufferLarge.close();
         LarpLog.info("Bloom", "Quality changed: {}", var0);
         lastBloomQuality = var0;
      }

      if (lastBlurQuality != blurQuality) {
         if (blurQuality == PostProcessing.Quality.OFF) {
            blurBuffer.close();
         }

         LarpLog.debug("Blur", "Quality: {}", blurQuality);
         lastBlurQuality = blurQuality;
      }
   }

   private static void initPipelines() {
      if (!pipelinesInitialized) {
         for (RenderPipeline var3 : new RenderPipeline[]{
            ClientPipelines.bLUR_HORIZONTAL, ClientPipelines.bLUR_VERTICAL, ClientPipelines.cOPY, ClientPipelines.bLOOM_COMPOSITE
         }) {
            ClientPipelines.validatePipeline(var3);
         }

         pipelinesInitialized = (boolean)1;
         LarpLog.info("Shader", "Post pipelines initialized");
      }
   }

   public static void blit(RenderPipeline var0, RenderTarget var1, RenderTarget var2) {
      runPostPass(var0, var1, var2, 1.0F);
   }

   private static void runPostPass(RenderPipeline var0, RenderTarget var1, RenderTarget var2, float var3) {
      int var4 = var0 != ClientPipelines.cOPY ? 1 : 0;
      if (var4 != 0) {
         if (parametersBuffer == null) {
            parametersBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp post parameters", 136, 16L);
         }

         MemoryStack var5 = MemoryStack.stackPush();

         try {
            ByteBuffer var6 = var5.malloc(16);
            int var7 = Math.max(1, passCount(getBloomQuality()));
            var6.putFloat(bloomStrength / var7).putFloat(var3).putFloat(texelOffset(var3, var2.width)).putFloat(texelOffset(var3, var2.height)).flip();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(parametersBuffer.slice(), var6);
         } catch (Throwable var10) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var5 != null) {
            var5.close();
         }
      }

      RenderPass var12 = RenderSystem.getDevice()
         .createCommandEncoder()
         .createRenderPass(() -> "Larp post " + var0.getLocation(), var2.getColorTextureView(), Optional.empty());

      try {
         RenderPipelineCompat.setPipeline(var12, var0);
         if (var4 != 0) {
            var12.setUniform("PostParameters", parametersBuffer.slice());
         }

         RenderPipelineCompat.setSamplerUniform(var12, "Source", var1.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var12.draw(3, 1, 0, 0);
         PerfProfiler.addCounter("Post fullscreen draws", 1L);
      } catch (Throwable var11) {
         if (var12 != null) {
            try {
               var12.close();
            } catch (Throwable var8) {
               var11.addSuppressed(var8);
            }
         }

         throw var11;
      }

      if (var12 != null) {
         var12.close();
      }
   }

   public static void runPostPass(RenderTarget var0) {
      initPipelines();
      blit(ClientPipelines.cOPY, Minecraft.getInstance().gameRenderer.mainRenderTarget(), var0);
   }

   private static void blurPass(RenderTarget var0, GlowFramebuffers var1, PostProcessing.Quality var2) {
      getBlurTexture(var0, var1, var2, 1.0F);
   }

   private static void getBlurTexture(RenderTarget var0, GlowFramebuffers var1, PostProcessing.Quality var2, float var3) {
      blurPasses(var0, var1, var2, var3, 0);
   }

   private static void blurPasses(RenderTarget var0, GlowFramebuffers var1, PostProcessing.Quality var2, float var3, int var4) {
      runPostPass(ClientPipelines.bLUR_HORIZONTAL, var0, var1.blurPing, var3);
      runPostPass(ClientPipelines.bLUR_VERTICAL, var1.blurPing, var1.blurPong, var3);

      for (int var5 = 1; var5 < var2.BLURITERATIONS + var4; var5++) {
         runPostPass(ClientPipelines.bLUR_HORIZONTAL, var1.blurPong, var1.blurPing, var3);
         runPostPass(ClientPipelines.bLUR_VERTICAL, var1.blurPing, var1.blurPong, var3);
      }
   }

   public static RenderTarget beginBloom() {
      PostProcessing.Quality var0 = getBloomQuality();
      if (!disabled && var0 != PostProcessing.Quality.OFF) {
         try {
            initPipelines();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            bloomBuffer.resize(var1.width, var1.height, var0.DOWNSAMPLE, true);
            bloomBuffer.glowMask.copyDepthFrom(var1);
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(bloomBuffer.glowMask.getColorTexture(), new Vector4f());
            return bloomBuffer.glowMask;
         } catch (RuntimeException var2) {
            disableForSession("Bloom", var2);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void applyBloom() {
      PostProcessing.Quality var0 = getBloomQuality();
      if (!disabled && var0 != PostProcessing.Quality.OFF && bloomBuffer.glowMask != null) {
         long var1 = PerfProfiler.startTimer();

         try {
            getBlurTexture(bloomBuffer.glowMask, bloomBuffer, var0, bloomSpread);
            RenderTarget var3 = setBlurLevel();
            blit(ClientPipelines.bLOOM_COMPOSITE, bloomBuffer.blurPong, var3);
            if (var0.ordinal() >= PostProcessing.Quality.BALANCED.ordinal()) {
               bloomBufferMedium.resize(var3.width, var3.height, Math.max(4, var0.DOWNSAMPLE * 2), false);
               getBlurTexture(bloomBuffer.blurPong, bloomBufferMedium, PostProcessing.Quality.BALANCED, bloomSpread);
               blit(ClientPipelines.bLOOM_COMPOSITE, bloomBufferMedium.blurPong, var3);
            }

            if (var0.ordinal() >= PostProcessing.Quality.HIGH.ordinal()) {
               bloomBufferLarge.resize(var3.width, var3.height, Math.max(8, var0.DOWNSAMPLE * 4), false);
               getBlurTexture(bloomBufferMedium.blurPong, bloomBufferLarge, var0, bloomSpread);
               blit(ClientPipelines.bLOOM_COMPOSITE, bloomBufferLarge.blurPong, var3);
            }
         } catch (RuntimeException var7) {
            disableForSession("Bloom", var7);
         } finally {
            PerfProfiler.endTimer("Bloom", var1);
         }
      }
   }

   private static boolean passCount() {
      if (disabled || blurQuality == PostProcessing.Quality.OFF) {
         return false;
      }

      if (lastBlurFrame == frameCounter) {
         return true;
      }

      long var0 = PerfProfiler.startTimer();

      try {
         initPipelines();
         RenderTarget var2 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         blurBuffer.resize(var2.width, var2.height, blurQuality.DOWNSAMPLE, false);
         blurPasses(var2, blurBuffer, blurQuality, blurScale, extraBlurPasses);
         lastBlurFrame = frameCounter;
         return true;
      } catch (RuntimeException var7) {
         disableForSession("Blur", var7);
         return false;
      } finally {
         PerfProfiler.endTimer("Blur", var0);
      }
   }

   private static void drawFullscreenBlur(RenderTarget var0) {
      if (passCount()) {
         try {
            blit(ClientPipelines.cOPY, blurBuffer.blurPong, var0);
         } catch (RuntimeException var2) {
            disableForSession("Blur", var2);
         }
      }
   }

   private static void drawBlurRegion(RenderTarget var0, int var1, int var2, int var3, int var4) {
      if (passCount()) {
         Minecraft var5 = Minecraft.getInstance();
         RenderTarget var6 = var0;
         int var7 = var5.getWindow().getGuiScale();

         try {
            RenderPass var8 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp HUD blur region", var6.getColorTextureView(), Optional.empty());

            try {
               RenderPipelineCompat.setPipeline(var8, ClientPipelines.cOPY);
               RenderPipelineCompat.setSamplerUniform(
                  var8, "Source", blurBuffer.blurPong.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
               );
               var8.enableScissor(
                  Math.max(0, var1 * var7),
                  Math.max(0, var6.height - (var2 + var4) * var7),
                  Math.max(0, Math.min(var3 * var7, var6.width - var1 * var7)),
                  Math.max(0, Math.min(var4 * var7, var6.height - var2 * var7))
               );
               var8.draw(3, 1, 0, 0);
               PerfProfiler.addCounter("Post fullscreen draws", 1L);
            } catch (Throwable var12) {
               if (var8 != null) {
                  try {
                     var8.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }
               }

               throw var12;
            }

            if (var8 != null) {
               var8.close();
            }
         } catch (RuntimeException var13) {
            disableForSession("Blur", var13);
         }
      }
   }

   private static void disableForSession(String var0, RuntimeException var1) {
      disabled = (boolean)1;
      LarpLog.error(var0, "Post-processing disabled for this session; see shader/compiler diagnostics", var1);
      releaseResources();
   }

   public static void releaseResources() {
      bloomBuffer.close();
      bloomBufferMedium.close();
      bloomBufferLarge.close();
      blurBuffer.close();
      WorldColorRenderer.release();
      MotionBlurRenderer.releaseResources();
      EntityHaloRenderer.close();
      HandChamsRenderer.reset();
      ChamsModule var0 = ChamsModule.getInstance();
      if (var0 != null) {
         var0.reset();
      }

      ModelVisualRenderer.clearCaches();
      if (parametersBuffer != null) {
         parametersBuffer.close();
         parametersBuffer = null;
      }

      pipelinesInitialized = (boolean)0;
      lastBlurFrame = -1L;
   }

   public static void onResourceReload() {
      reloadCount++;
      StreamProofManager.clearLayers();
      RenderEffects.clearAll();
      VisualsRenderer.runIsolated("Scene resource cleanup", SceneCapture::releaseAll);
      VisualsRenderer.runIsolated("Refraction cleanup", LocalizedRefraction::release);
      RoundedShapes.resetFailures();
      SdfText.reset();
      SoftParticleRenderer.releaseBuffer();
      VisualsRenderer.ghostEcho.clear();
      KillFXModule var0 = KillFXModule.getInstance();
      if (var0 != null) {
         var0.reset();
      }

      releaseResources();
      disabled = (boolean)0;
      LarpLog.info("Shader", "Resources reloaded; post pipelines will be checked on next use");
   }

   public enum Quality {
      OFF(0, 0),
      LOW(4, 1),
      BALANCED(2, 1),
      HIGH(2, 2),
      ULTRA(1, 2);

      final int DOWNSAMPLE;
      final int BLURITERATIONS;

      Quality(int var3, int var4) {
         this.DOWNSAMPLE = var3;
         this.BLURITERATIONS = var4;
      }
   }
}
