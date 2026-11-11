package com.larpclient.render.entity;

import com.larpclient.config.GradientOutlineSettings;
import com.larpclient.core.StreamProofManager;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.overlay.GlowFramebuffers;
import com.larpclient.render.overlay.OverlayTextures;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.SceneCapture;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

public final class GradientChamsRenderer implements AutoCloseable {
   private static final Vector4fc cLEAR_COLOR = new Vector4f(0.0F);
   private static final RenderPipeline eDGE_PIPELINE = createPostPipeline("chams_edge", false);
   private static final RenderPipeline eNTITY_MASK_PIPELINE = createEntityMaskPipeline();
   private static final RenderPipeline bLUR_PIPELINE = createPostPipeline("chams_blur", false);
   private static final RenderPipeline cOMPOSITE_PIPELINE = createPostPipeline("chams_composite", true);
   private static final RenderPipeline hAND_MASK_PIPELINE = RenderPipeline.builder(new Snippet[0])
      .withLocation(drawPass("chams_hand_mask"))
      .withVertexShader(drawPass("post/fullscreen"))
      .withFragmentShader(drawPass("post/chams_hand_mask"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("Before", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("After", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("HandDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private final String label;
   private final GlowFramebuffers scratchTargets = new GlowFramebuffers();
   private TextureTarget sceneTarget;
   private GpuBuffer parameterBuffer;
   private EffectBatch modelBuffers;
   private int resourceGeneration = -1;
   private boolean disabled;
   private boolean warnedBusy;
   private boolean handCaptured;
   private boolean handPipelineReady;
   private boolean capturingEntities;
   private boolean flagged;
   private static final long iDLE_TIMEOUT_NANOS = 5000000000L;
   private long idleSince;

   public GradientChamsRenderer(String var1) {
      this.label = var1;
   }

   private static RenderPipeline createEntityMaskPipeline() {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(drawPass("chams_entity_mask"))
         .withVertexShader(drawPass("post/fullscreen"))
         .withFragmentShader(drawPass("post/chams_entity_mask"))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("Source", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
               .withUniform("ModelDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
               .withUniform("SceneDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
               .withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static RenderPipeline createPostPipeline(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder()
         .withUniform("Source", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
         .withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER);
      if (var1) {
         var2.withUniform("Glow", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(drawPass(var0))
         .withVertexShader(drawPass("post/fullscreen"))
         .withFragmentShader(drawPass("post/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(BlendFunction.TRANSLUCENT) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private boolean canCapture() {
      int var1 = OverlayTextures.colorTexture == null && OverlayTextures.depthTexture == null ? 1 : 0;
      if (var1 == 0 && !this.warnedBusy) {
         this.warnedBusy = (boolean)1;
         LarpLog.warn(this.label, "Gradient capture skipped while another renderer owns the output attachments");
      }

      return (boolean)var1;
   }

   private boolean ensureResources() {
      if (this.resourceGeneration != PostProcessing.reloadCount) {
         this.close();
         this.resourceGeneration = PostProcessing.reloadCount;
         this.disabled = (boolean)0;
      }

      if (!this.disabled && this.canCapture()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (this.sceneTarget != null && (this.sceneTarget.width != var1.width || this.sceneTarget.height != var1.height)) {
               this.close();
            }

            if (this.sceneTarget == null) {
               ClientPipelines.validatePipeline(eDGE_PIPELINE);
               ClientPipelines.validatePipeline(eNTITY_MASK_PIPELINE);
               ClientPipelines.validatePipeline(bLUR_PIPELINE);
               ClientPipelines.validatePipeline(cOMPOSITE_PIPELINE);
               this.scratchTargets.resize(var1.width, var1.height, 1, GlowFramebuffers.Mode.MASK_AND_BLUR);
               this.sceneTarget = RenderPipelineCompat.createTextureTarget(
                  "Larp " + this.label + " gradient", var1.width, var1.height, false, GpuFormat.RGBA8_UNORM
               );
               this.parameterBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp " + this.label + " gradient parameters", 136, 80L);
               LarpLog.info(this.label, "Gradient pass initialized {}x{}; four full-resolution targets", var1.width, var1.height);
            }

            return true;
         } catch (RuntimeException var2) {
            this.disableOnError("allocate/check", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public void renderEntities(List<GradientChamsRenderer.ChamsEntity> var1, PoseStack var2, GradientOutlineSettings.Snapshot var3, boolean var4) {
      if (var1.isEmpty()) {
         long var22 = System.nanoTime();
         if (this.idleSince == 0L) {
            this.idleSince = var22;
         } else if (var22 - this.idleSince > 5000000000L) {
            this.close();
         }
      } else {
         this.idleSince = 0L;
         if (this.ensureResources()) {
            GpuTextureView var5 = OverlayTextures.colorTexture;
            GpuTextureView var6 = OverlayTextures.depthTexture;

            try {
               this.capturingEntities = (boolean)1;
               this.flagged = var4;
               if (SceneCapture.getSceneDepth() == null) {
                  throw new IllegalStateException("scene-depth capture unavailable");
               }

               if (this.modelBuffers == null) {
                  this.modelBuffers = new EffectBatch();
               }

               CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
               var7.clearColorTexture(this.scratchTargets.glowMask.getColorTexture(), cLEAR_COLOR);
               var7.clearDepthTexture(this.scratchTargets.glowMask.getDepthTexture(), 0.0);
               OverlayTextures.colorTexture = this.scratchTargets.glowMask.getColorTextureView();
               OverlayTextures.depthTexture = this.scratchTargets.glowMask.getDepthTextureView();

               try {
                  ModelVisualRenderer.beginBoundsCapture(this.scratchTargets.glowMask.width, this.scratchTargets.glowMask.height);

                  for (GradientChamsRenderer.ChamsEntity var10 : var1) {
                     ModelVisualRenderer.renderEntityLayers(
                        var10.state, var2, this.modelBuffers, ModelVisualRenderer.Material.CHAMS_SURFACE, var10.color, 1.0, false, false
                     );
                  }

                  this.modelBuffers.flush();
               } finally {
                  int[] var8 = ModelVisualRenderer.endBoundsCapture();
                  OverlayTextures.colorTexture = var5;
                  OverlayTextures.depthTexture = var6;
               }

               Object var23;
               if (var23 == null || ((Object[])var23)[2] > 0) {
                  this.runPasses(var3, (int[])var23);
               }
            } catch (RuntimeException var20) {
               this.disableOnError("entity mask/composite", var20);
            } finally {
               OverlayTextures.colorTexture = var5;
               OverlayTextures.depthTexture = var6;
               if (this.modelBuffers != null) {
                  this.modelBuffers.endFrame();
               }

               this.capturingEntities = (boolean)0;
            }
         }
      }
   }

   public boolean captureHandScene() {
      this.handCaptured = (boolean)0;
      if (!this.ensureResources()) {
         return false;
      }

      try {
         if (!this.handPipelineReady) {
            ClientPipelines.validatePipeline(hAND_MASK_PIPELINE);
            this.handPipelineReady = (boolean)1;
         }

         PostProcessing.blit(
            ClientPipelines.cOPY, StreamProofManager.blitOverlayTarget(Minecraft.getInstance().gameRenderer.mainRenderTarget()), this.sceneTarget
         );
         this.handCaptured = (boolean)1;
         return true;
      } catch (RuntimeException var2) {
         this.disableOnError("hand scene capture", var2);
         return false;
      }
   }

   public void compositeHand(GradientOutlineSettings.Snapshot var1) {
      RenderTarget var2 = this.renderHandMask();
      if (var2 != null) {
         try {
            this.runPasses(var1, null);
         } catch (RuntimeException var4) {
            this.disableOnError("hand mask/composite", var4);
         }
      }
   }

   public void createPostPipeline(List<GradientChamsRenderer.Region> var1) {
      RenderTarget var2 = this.renderHandMask();
      if (var2 != null) {
         long var3 = PerfProfiler.startTimer();

         try {
            for (GradientChamsRenderer.Region var6 : var1) {
               this.drawPass(eDGE_PIPELINE, this.scratchTargets.glowMask, this.sceneTarget, var6.frame(), 0.0F, 0.0F, var6);
               this.drawPass(cOMPOSITE_PIPELINE, this.sceneTarget, var2, var6.frame(), 0.0F, 0.0F, var6);
            }

            SceneCapture.invalidateColor();
         } catch (RuntimeException var10) {
            this.disableOnError("hand region composite", var10);
         } finally {
            PerfProfiler.endTimer(this.label + " gradient post", var3);
         }
      }
   }

   private RenderTarget renderHandMask() {
      if (!this.handCaptured) {
         return null;
      }

      this.handCaptured = (boolean)0;
      if (!this.canCapture()) {
         this.close();
         return null;
      }

      try {
         RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         if (var1.width == this.sceneTarget.width && var1.height == this.sceneTarget.height) {
            CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var3 = var2.createRenderPass(
               () -> "Larp " + this.label + " silhouette", this.scratchTargets.glowMask.getColorTextureView(), Optional.empty()
            );

            try {
               RenderPipelineCompat.setPipeline(var3, hAND_MASK_PIPELINE);
               GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               RenderPipelineCompat.setSamplerUniform(var3, "Before", this.sceneTarget.getColorTextureView(), var4);
               RenderPipelineCompat.setSamplerUniform(var3, "After", StreamProofManager.blitOverlayTarget(var1).getColorTextureView(), var4);
               RenderPipelineCompat.setSamplerUniform(
                  var3, "HandDepth", var1.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST)
               );
               var3.draw(3, 1, 0, 0);
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }

            return var1;
         } else {
            this.close();
            return null;
         }
      } catch (RuntimeException var8) {
         this.disableOnError("hand mask", var8);
         return null;
      }
   }

   public static int[][] computeRegions(int[] var0, float var1, float var2) {
      int var3 = (int)Math.ceil(Math.max(var1, 1.0F));
      int var4 = var2 > 0.0F ? (int)Math.ceil(Math.clamp(var2, 1.0F, 32.0F)) : 0;
      int[] var5 = expandRegion(var0, var3 + var4 + 2);
      int[] var6 = expandRegion(var5, var4 + 1);
      int[] var7 = expandRegion(var6, var4 + 1);
      return new int[][]{expandRegion(var7, var3 + 1), var7, var6, var5};
   }

   private static int[] expandRegion(int[] var0, int var1) {
      return new int[]{var0[0] - var1, var0[1] - var1, var0[2] + 2 * var1, var0[3] + 2 * var1};
   }

   private static GradientChamsRenderer.Region regionAt(int[][] var0, int var1, GradientOutlineSettings.Snapshot var2) {
      if (var0 == null) {
         return null;
      }

      int[] var3 = var0[var1];
      return new GradientChamsRenderer.Region(var3[0], var3[1], var3[2], var3[3], var2);
   }

   private void runPasses(GradientOutlineSettings.Snapshot var1, int[] var2) {
      long var3 = PerfProfiler.startTimer();

      try {
         int[][] var5 = var2 == null ? null : computeRegions(var2, var1.lineWidth(), var1.glowing() ? var1.radius() : 0.0F);
         if (this.capturingEntities) {
            this.drawPass(eNTITY_MASK_PIPELINE, this.scratchTargets.glowMask, this.scratchTargets.blurPing, var1, 0.0F, 0.0F, regionAt(var5, 0, var1));
         }

         this.drawPass(
            eDGE_PIPELINE,
            this.capturingEntities ? this.scratchTargets.blurPing : this.scratchTargets.glowMask,
            this.sceneTarget,
            var1,
            0.0F,
            0.0F,
            regionAt(var5, 1, var1)
         );
         if (var1.glowing()) {
            this.drawPass(bLUR_PIPELINE, this.sceneTarget, this.scratchTargets.blurPing, var1, 1.0F, 0.0F, regionAt(var5, 2, var1));
            this.drawPass(bLUR_PIPELINE, this.scratchTargets.blurPing, this.scratchTargets.blurPong, var1, 0.0F, 1.0F, regionAt(var5, 3, var1));
         }

         this.drawPass(
            cOMPOSITE_PIPELINE,
            this.sceneTarget,
            StreamProofManager.setHideEverything(Minecraft.getInstance().gameRenderer.mainRenderTarget()),
            var1,
            0.0F,
            0.0F,
            regionAt(var5, 3, var1)
         );
         SceneCapture.invalidateColor();
      } finally {
         PerfProfiler.endTimer(this.label + " gradient post", var3);
      }
   }

   private void drawPass(
      RenderPipeline var1,
      RenderTarget var2,
      RenderTarget var3,
      GradientOutlineSettings.Snapshot var4,
      float var5,
      float var6,
      GradientChamsRenderer.Region var7
   ) {
      CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var9 = MemoryStack.stackPush();

      try {
         ByteBuffer var10 = var9.malloc(80);
         var10.putFloat(var4.lineWidth()).putFloat(var4.scale()).putFloat(var4.fill()).putFloat(var4.outline());
         var10.putFloat(var4.entityColor() ? 1.0F : 0.0F).putFloat(var4.phase()).putFloat(var4.radius()).putFloat(var4.glowing() ? var4.intensity() : 0.0F);
         putColor(var10, var4.color1());
         putColor(var10, var4.color2());
         var10.putFloat(var5).putFloat(var6).putFloat(this.flagged ? 1.0F : 0.0F).putFloat(0.0F).flip();
         var8.writeToBuffer(this.parameterBuffer.slice(), var10);
      } catch (Throwable var16) {
         if (var9 != null) {
            try {
               var9.close();
            } catch (Throwable var15) {
               var16.addSuppressed(var15);
            }
         }

         throw var16;
      }

      if (var9 != null) {
         var9.close();
      }

      RenderPass var18 = var8.createRenderPass(() -> "Larp " + this.label + " " + var1.getLocation(), var3.getColorTextureView(), Optional.empty());

      try {
         RenderPipelineCompat.setPipeline(var18, var1);
         if (var7 != null) {
            int var19 = Math.clamp(var7.x2(), 0, var3.width);
            int var11 = Math.clamp(var7.y2(), 0, var3.height);
            int var12 = Math.clamp(var7.x2() + var7.width(), var19, var3.width);
            int var13 = Math.clamp(var7.y2() + var7.height(), var11, var3.height);
            var18.enableScissor(var19, var3.height - var13, var12 - var19, var13 - var11);
         }

         var18.setUniform("ChamsParameters", this.parameterBuffer.slice());
         GpuSampler var20 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         RenderPipelineCompat.setSamplerUniform(var18, "Source", var2.getColorTextureView(), var20);
         if (var1 == eNTITY_MASK_PIPELINE) {
            GpuSampler var21 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            RenderPipelineCompat.setSamplerUniform(var18, "Source", var2.getColorTextureView(), var21);
            RenderPipelineCompat.setSamplerUniform(var18, "ModelDepth", this.scratchTargets.glowMask.getDepthTextureView(), var21);
            RenderPipelineCompat.setSamplerUniform(var18, "SceneDepth", SceneCapture.getSceneDepth(), var21);
         }

         if (var1 == cOMPOSITE_PIPELINE) {
            RenderPipelineCompat.setSamplerUniform(
               var18, "Glow", (var4.glowing() ? this.scratchTargets.blurPong : this.sceneTarget).getColorTextureView(), var20
            );
         }

         var18.draw(3, 1, 0, 0);
         PerfProfiler.addCounter(this.label + " fullscreen draws", 1L);
      } catch (Throwable var17) {
         if (var18 != null) {
            try {
               var18.close();
            } catch (Throwable var14) {
               var17.addSuppressed(var14);
            }
         }

         throw var17;
      }

      if (var18 != null) {
         var18.close();
      }
   }

   private static void putColor(ByteBuffer var0, int var1) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat((var1 >>> 24) / 255.0F);
   }

   private void disableOnError(String var1, RuntimeException var2) {
      this.disabled = (boolean)1;
      this.close();
      LarpLog.error(this.label, "stage=" + var1 + "; gradient pass disabled until resource reload; ordinary models retained", var2);
   }

   @Override
   public void close() {
      this.handCaptured = (boolean)0;
      this.handPipelineReady = (boolean)0;
      this.idleSince = 0L;
      TextureTarget var1 = this.sceneTarget;
      this.sceneTarget = null;
      GpuBuffer var2 = this.parameterBuffer;
      this.parameterBuffer = null;
      EffectBatch var3 = this.modelBuffers;
      this.modelBuffers = null;
      RuntimeException var4 = null;

      for (Runnable var8 : new Runnable[]{this.scratchTargets::close, () -> {
         if (var1 != null) {
            var1.destroyBuffers();
         }
      }, () -> {
         if (var2 != null) {
            var2.close();
         }
      }, () -> {
         if (var3 != null) {
            var3.close();
         }
      }}) {
         try {
            var8.run();
         } catch (RuntimeException var10) {
            if (var4 == null) {
               var4 = var10;
            } else {
               var4.addSuppressed(var10);
            }
         }
      }

      if (var4 != null) {
         LarpLog.error(this.label, "stage=cleanup; all gradient resource releases attempted", var4);
      }
   }

   private static Identifier drawPass(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public record ChamsEntity(EntityRenderState state, int color) {
   }

   public record Region(int x, int y, int width, int height, GradientOutlineSettings.Snapshot frame) {
      private final int x2;
      private final int y2;

      public Region(int x, int y, int width, int height, GradientOutlineSettings.Snapshot frame) {
         this.x2 = x;
         this.y2 = y;
         this.width = width;
         this.height = height;
         this.frame = frame;
      }
   }
}
