package com.larpclient.render.entity;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.overlay.GlowFramebuffers;
import com.larpclient.render.overlay.OverlayTextures;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class EntityHaloRenderer {
   private static final GlowFramebuffers targets = new GlowFramebuffers();
   private static final RenderPipeline haloPipeline = RenderPipeline.builder(new Snippet[0])
      .withLocation(id("entity_halo"))
      .withVertexShader(id("post/fullscreen"))
      .withFragmentShader(id("post/entity_halo"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("Source", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("Mask", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("Halo", UniformType.UNIFORM_BUFFER)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
      .withCull(false)
      .build();
   private static EffectBatch maskRenderer;
   private static GpuBuffer haloParamsBuffer;
   private static int lastReloadGeneration = -1;
   private static boolean disabled;

   public static void render(PoseStack var0, EntityRenderState var1) {
      SelfVisualsModule var2 = SelfVisualsModule.getPrimaryColor();
      int var3 = var2 != null && var2.isActiveForSelf() && var2.shouldRenderOutline() && var2.outline.getValue() && var1 != null ? 1 : 0;
      if (var3 != 0) {
         if (lastReloadGeneration != PostProcessing.reloadCount) {
            close();
            disabled = (boolean)0;
            lastReloadGeneration = PostProcessing.reloadCount;
         }

         if (!disabled) {
            GpuTextureView var4 = OverlayTextures.colorTexture;
            GpuTextureView var5 = OverlayTextures.depthTexture;

            try {
               if (maskRenderer == null) {
                  maskRenderer = new EffectBatch();
               }

               if (haloParamsBuffer == null) {
                  ClientPipelines.validatePipeline(haloPipeline);
                  haloParamsBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp entity halo parameters", 136, 32L);
                  LarpLog.info("SelfVisuals", "Outline pass initialized; full-resolution silhouette edges");
               }

               Minecraft var6 = Minecraft.getInstance();
               RenderTarget var7 = var6.gameRenderer.mainRenderTarget();
               targets.resize(var7.width, var7.height, 2, var2.outlineBloom.getValue() ? GlowFramebuffers.Mode.MASK_AND_BLUR : GlowFramebuffers.Mode.MASK_ONLY);
               if (var3 != 0) {
                  long var8 = PerfProfiler.startTimer();
                  prepareMaskTarget();
                  ModelVisualRenderer.beginBoundsCapture(var7.width, var7.height);

                  try {
                     ModelVisualRenderer.renderEntity(var1, var0, maskRenderer, ModelVisualRenderer.Material.SILHOUETTE, -1, 1.0, true);
                  } finally {
                     int[] var10 = ModelVisualRenderer.endBoundsCapture();
                  }

                  maskRenderer.flush();
                  restoreTextureViews(var4, var5);
                  PerfProfiler.endTimer("SelfVisuals outline mask", var8);
                  var8 = PerfProfiler.startTimer();
                  Object var23;
                  if (var23 == null || ((Object[])var23)[2] > 0) {
                     compositeHalo(
                        var2.outlineColor.getValue(),
                        var2.outlineThickness.getValue().floatValue(),
                        var2.outlineOpacity.getValue().floatValue(),
                        var2.outlineBloom.getValue() ? 0.2F : 0.0F,
                        (int[])var23
                     );
                  }

                  PerfProfiler.endTimer("SelfVisuals outline composite", var8);
               }
            } catch (RuntimeException var20) {
               disabled = (boolean)1;
               LarpLog.error(
                  "SelfVisuals", "EntityMaskRenderer stage=mask/composite framebuffer=entity mask shader=post/entity_halo; disabled until reload", var20
               );
               close();
            } finally {
               restoreTextureViews(var4, var5);
               if (maskRenderer != null) {
                  maskRenderer.endFrame();
               }
            }
         }
      }
   }

   private static void restoreTextureViews(GpuTextureView var0, GpuTextureView var1) {
      OverlayTextures.colorTexture = var0;
      OverlayTextures.depthTexture = var1;
   }

   private static void prepareMaskTarget() {
      CommandEncoder var0 = RenderSystem.getDevice().createCommandEncoder();
      var0.clearColorTexture(targets.glowMask.getColorTexture(), new Vector4f());
      targets.glowMask.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      OverlayTextures.colorTexture = targets.glowMask.getColorTextureView();
      OverlayTextures.depthTexture = targets.glowMask.getDepthTextureView();
   }

   static int haloPadding(float var0, boolean var1) {
      return (int)Math.ceil(var0) * 2 + (var1 ? 12 : 0) + 2;
   }

   private static void compositeHalo(int var0, float var1, float var2, float var3, int[] var4) {
      if (var3 > 0.0F) {
         PostProcessing.blit(ClientPipelines.bLUR_HORIZONTAL, targets.glowMask, targets.blurPing);
         PostProcessing.blit(ClientPipelines.bLUR_VERTICAL, targets.blurPing, targets.blurPong);
      }

      RenderTarget var5 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      CommandEncoder var6 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var7 = MemoryStack.stackPush();

      try {
         ByteBuffer var8 = var7.malloc(32);
         var8.putFloat((var0 >> 16 & 0xFF) / 255.0F).putFloat((var0 >> 8 & 0xFF) / 255.0F).putFloat((var0 & 0xFF) / 255.0F).putFloat(var2);
         var8.putFloat(var1).putFloat(var3).putFloat(1.0F / var5.width).putFloat(1.0F / var5.height);
         var8.flip();
         var6.writeToBuffer(haloParamsBuffer.slice(), var8);
      } catch (Throwable var15) {
         if (var7 != null) {
            try {
               var7.close();
            } catch (Throwable var14) {
               var15.addSuppressed(var14);
            }
         }

         throw var15;
      }

      if (var7 != null) {
         var7.close();
      }

      RenderPass var17 = var6.createRenderPass(
         () -> "Larp entity outline and halo", StreamProofManager.setHideEverything(var5).getColorTextureView(), Optional.empty()
      );

      try {
         RenderPipelineCompat.setPipeline(var17, haloPipeline);
         if (var4 != null) {
            int var18 = haloPadding(var1, var3 > 0.0F);
            int var9 = Math.clamp(var4[0] - var18, 0, var5.width);
            int var10 = Math.clamp(var4[1] - var18, 0, var5.height);
            int var11 = Math.clamp(var4[0] + var4[2] + var18, var9, var5.width);
            int var12 = Math.clamp(var4[1] + var4[3] + var18, var10, var5.height);
            var17.enableScissor(var9, var5.height - var12, var11 - var9, var12 - var10);
         }

         var17.setUniform("Halo", haloParamsBuffer.slice());
         GpuSampler var19 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         RenderPipelineCompat.setSamplerUniform(var17, "Source", (var3 > 0.0F ? targets.blurPong : targets.glowMask).getColorTextureView(), var19);
         RenderPipelineCompat.setSamplerUniform(var17, "Mask", targets.glowMask.getColorTextureView(), var19);
         var17.draw(3, 1, 0, 0);
      } catch (Throwable var16) {
         if (var17 != null) {
            try {
               var17.close();
            } catch (Throwable var13) {
               var16.addSuppressed(var13);
            }
         }

         throw var16;
      }

      if (var17 != null) {
         var17.close();
      }
   }

   public static void close() {
      targets.close();
      if (haloParamsBuffer != null) {
         haloParamsBuffer.close();
         haloParamsBuffer = null;
      }

      if (maskRenderer != null) {
         maskRenderer.close();
         maskRenderer = null;
      }
   }

   private static Identifier id(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}
