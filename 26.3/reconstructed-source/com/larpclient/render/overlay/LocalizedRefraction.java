package com.larpclient.render.overlay;

import com.larpclient.core.StreamProofManager;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.misc.SafetyLevel;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class LocalizedRefraction {
   private static final GlowFramebuffers maskTarget = new GlowFramebuffers(GpuFormat.RGBA16_FLOAT);
   private static final Set<String> quarantinedOwners = new HashSet<>();
   private static final RenderPipeline pipeline = RenderPipeline.builder(new Snippet[0])
      .withLocation(id("localized_refraction"))
      .withVertexShader(id("post/fullscreen"))
      .withFragmentShader(id("post/refraction"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("Source", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("Mask", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("SceneDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("Refraction", UniformType.UNIFORM_BUFFER)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static GpuBuffer paramsBuffer;
   private static int generation = -1;
   private static boolean usedThisFrame;
   private static boolean disabled;
   private static int compositeCount;

   public static void beginFrame() {
      if (generation != PostProcessing.reloadCount) {
         generation = PostProcessing.reloadCount;
         quarantinedOwners.clear();
         disabled = (boolean)0;

         try {
            release();
         } catch (RuntimeException var1) {
            disabled = (boolean)1;
            LarpLog.error("Refraction", "Generation cleanup failed; base materials retained until resource reload", var1);
         }
      }

      usedThisFrame = (boolean)0;
      compositeCount = 0;
   }

   public static boolean composite(String var0, Runnable var1, double var2) {
      if (!disabled && compositeCount < 2 && SafetyLevel.isAggressive() && !(var2 <= 0.0) && !quarantinedOwners.contains(var0)) {
         GpuTextureView var4 = OverlayTextures.colorTexture;
         GpuTextureView var5 = OverlayTextures.depthTexture;
         long var6 = PerfProfiler.startTimer();

         try {
            GpuTextureView var8 = SceneCapture.getSceneColor();
            GpuTextureView var9 = SceneCapture.getSceneDepth();
            if (var8 != null && var9 != null) {
               if (paramsBuffer == null) {
                  ClientPipelines.validatePipeline(pipeline);
                  paramsBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp refraction parameters", 136, 16L);
                  LarpLog.info("Refraction", "Localized depth-aware pipeline ready; shared scene samples and mask");
               }

               RenderTarget var10 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
               maskTarget.resize(var10.width, var10.height, 1, GlowFramebuffers.Mode.MASK_NO_DEPTH);
               usedThisFrame = (boolean)1;
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               var11.clearColorTexture(maskTarget.glowMask.getColorTexture(), new Vector4f());
               OverlayTextures.colorTexture = maskTarget.glowMask.getColorTextureView();
               OverlayTextures.depthTexture = var9;
               var1.run();
               OverlayTextures.colorTexture = var4;
               OverlayTextures.depthTexture = var5;
               MemoryStack var12 = MemoryStack.stackPush();

               try {
                  ByteBuffer var13 = var12.malloc(16);
                  var13.putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.width)
                     .putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.height)
                     .putFloat(0.0F)
                     .putFloat(0.0F)
                     .flip();
                  var11.writeToBuffer(paramsBuffer.slice(), var13);
               } catch (Throwable var26) {
                  if (var12 != null) {
                     try {
                        var12.close();
                     } catch (Throwable var24) {
                        var26.addSuppressed(var24);
                     }
                  }

                  throw var26;
               }

               if (var12 != null) {
                  var12.close();
               }

               RenderTarget var29 = StreamProofManager.setHideEverything(var10);
               RenderPass var30 = var11.createRenderPass(() -> "Larp localized refraction " + var0, var29.getColorTextureView(), Optional.empty());

               try {
                  RenderPipelineCompat.setPipeline(var30, pipeline);
                  var30.setUniform("Refraction", paramsBuffer.slice());
                  GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
                  RenderPipelineCompat.setSamplerUniform(var30, "Source", var8, var14);
                  RenderPipelineCompat.setSamplerUniform(var30, "Mask", maskTarget.glowMask.getColorTextureView(), var14);
                  RenderPipelineCompat.setSamplerUniform(var30, "SceneDepth", var9, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                  var30.draw(3, 1, 0, 0);
               } catch (Throwable var25) {
                  if (var30 != null) {
                     try {
                        var30.close();
                     } catch (Throwable var23) {
                        var25.addSuppressed(var23);
                     }
                  }

                  throw var25;
               }

               if (var30 != null) {
                  var30.close();
               }

               compositeCount++;
               SceneCapture.invalidateColor();
               PerfProfiler.addCounter("Localized refraction composites", 1L);
               return true;
            } else {
               return false;
            }
         } catch (RuntimeException var27) {
            quarantinedOwners.add(var0);
            LarpLog.error("Refraction", "Owner=" + var0 + "; mask/composite failed, base material retained until resource reload", var27);
            return false;
         } finally {
            OverlayTextures.colorTexture = var4;
            OverlayTextures.depthTexture = var5;
            PerfProfiler.endTimer(var0 + " refraction CPU", var6);
         }
      } else {
         return false;
      }
   }

   public static void endFrame() {
      if (!usedThisFrame) {
         release();
      }

      PerfProfiler.setCounter("Refraction mask bytes (estimate)", maskTarget.getMemoryBytes());
      PerfProfiler.setCounter("Refraction quarantined owners", quarantinedOwners.size());
   }

   public static void release() {
      try {
         maskTarget.close();
      } finally {
         GpuBuffer var2 = paramsBuffer;
         paramsBuffer = null;
         if (var2 != null) {
            var2.close();
         }
      }
   }

   private static Identifier id(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private LocalizedRefraction() {
   }
}
