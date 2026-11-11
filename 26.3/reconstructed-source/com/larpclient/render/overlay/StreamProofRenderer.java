package com.larpclient.render.overlay;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice.MappedView;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFactor;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.pip.GuiBannerResultRenderer;
import net.minecraft.client.gui.render.pip.GuiBookModelRenderer;
import net.minecraft.client.gui.render.pip.GuiEntityRenderer;
import net.minecraft.client.gui.render.pip.GuiProfilerChartRenderer;
import net.minecraft.client.gui.render.pip.GuiSkinRenderer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState.TraverseRange;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector4f;

final class StreamProofRenderer {
   private static final Vector4f cLEAR_COLOR = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);
   private static final int rEAD_BACK_SLOTS = 3;
   static final RenderPipeline composePipeline = hasFailed("streamproof_pack", "Interface", "Effects");
   static final RenderPipeline interfaceComposePipeline = hasFailed("streamproof_pack_interface", "Interface");
   static final RenderPipeline blurPipeline = hasFailed("streamproof_opaque_blur", "Source");
   static final RenderPipeline handCoverPipeline = compilePipelines("streamproof_hand_cover", overlayBlendState(), "Scene", "Depth");
   static final RenderPipeline skyPipeline = compilePipelines("streamproof_sky", overlayBlendState(), "Sky", "Depth");
   private final GuiRenderState guiRenderState = new GuiRenderState();
   private GuiRenderer guiRenderer;
   private TextureTarget interfaceTarget;
   private TextureTarget effectsTarget;
   private TextureTarget skyTarget;
   private TextureTarget selfDepthTarget;
   private TextureTarget handBackdropTarget;
   private TextureTarget packTarget;
   private boolean redirectEnabled;
   private long selfDepthFrame = -1L;
   private final StreamProofRenderer.ReadBackSlot[] readBackSlots = new StreamProofRenderer.ReadBackSlot[3];
   private boolean pipelinesCompiled;
   private boolean failed;
   private boolean depthCopied;
   private boolean skyCleared;
   private long effectsFrame = -1L;
   private long lastDeliveredFrame = -1L;
   private int layerDepth;

   private static ColorTargetState overlayBlendState() {
      return new ColorTargetState(new BlendFunction(BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE));
   }

   GuiRenderState getGuiRenderState() {
      return this.guiRenderState;
   }

   boolean hasContent(Object var1) {
      return var1 != null && var1 == this.guiRenderer;
   }

   RenderTarget getInterfaceTarget() {
      return this.interfaceTarget;
   }

   boolean hasFailed() {
      return this.failed;
   }

   boolean isInLayer() {
      return this.layerDepth > 0;
   }

   boolean getSkyTarget(long var1) {
      return this.effectsFrame == var1;
   }

   RenderTarget runOutsideLayer() {
      return this.layerDepth > 0 ? this.effectsTarget : null;
   }

   boolean hasContent() {
      boolean[] var1 = new boolean[]{false};
      this.guiRenderState.forEachElement(var1x -> var1[0] = (boolean)1, TraverseRange.ALL);
      if (!var1[0]) {
         this.guiRenderState.forEachText(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.guiRenderState.forEachItem(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.guiRenderState.forEachPictureInPicture(var1x -> var1[0] = (boolean)1);
      }

      return var1[0];
   }

   void beginFrame(long var1) {
      if (this.effectsFrame != var1) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.effectsTarget = captureHandBackdrop(this.effectsTarget, "Larp Stream Proof effects", var3.width, var3.height, true);
         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.clearColorTexture(this.effectsTarget.getColorTexture(), cLEAR_COLOR);
         this.effectsFrame = var1;
         this.depthCopied = (boolean)0;
         this.skyCleared = (boolean)0;
      }
   }

   RenderTarget drawHandCover(long var1) {
      this.beginFrame(var1);
      this.drawBlur();
      return this.effectsTarget;
   }

   private void drawBlur() {
      if (!this.depthCopied) {
         this.effectsTarget.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
         this.depthCopied = (boolean)1;
      }
   }

   RenderTarget getSkyTarget(long var1) {
      this.beginFrame(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.skyTarget = captureHandBackdrop(this.skyTarget, "Larp Stream Proof sky", var3.width, var3.height, true);
      if (!this.skyCleared) {
         RenderSystem.getDevice()
            .createCommandEncoder()
            .clearColorAndDepthTextures(this.skyTarget.getColorTexture(), cLEAR_COLOR, this.skyTarget.getDepthTexture(), 0.0);
         this.skyCleared = (boolean)1;
      }

      return this.skyTarget;
   }

   void composeSky(long var1, boolean var3) {
      if (this.skyCleared && this.effectsFrame == var1 && !this.failed) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.drawBlur();
         Object var5 = var3 ? this.effectsTarget : var4;

         try {
            this.compilePipelines();
            GpuSampler var6 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            RenderPass var7 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp Stream Proof sky", this.effectsTarget.getColorTextureView(), Optional.empty());

            try {
               RenderPipelineCompat.setPipeline(var7, skyPipeline);
               RenderPipelineCompat.setSamplerUniform(var7, "Sky", this.skyTarget.getColorTextureView(), var6);
               RenderPipelineCompat.setSamplerUniform(var7, "Depth", var5.getDepthTextureView(), var6);
               var7.draw(3, 1, 0, 0);
            } catch (Throwable var11) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (var7 != null) {
               var7.close();
            }
         } catch (RuntimeException var12) {
            this.fail("sky", var12);
         }
      }
   }

   void captureHandBackdrop(long var1) {
      if (this.effectsFrame == var1 && !this.failed) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.handBackdropTarget = captureHandBackdrop(this.handBackdropTarget, "Larp Stream Proof hand backdrop", var3.width, var3.height, false);
            PostProcessing.blit(ClientPipelines.cOPY, var3, this.handBackdropTarget);
         } catch (RuntimeException var5) {
            this.fail("hand backdrop", var5);
         }
      }
   }

   void drawHandCover(long var1) {
      if (this.effectsFrame == var1 && !this.failed && this.handBackdropTarget != null) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.compilePipelines();
            GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var6 = var5.createRenderPass(() -> "Larp Stream Proof hand cover", this.effectsTarget.getColorTextureView(), Optional.empty());

            try {
               RenderPipelineCompat.setPipeline(var6, handCoverPipeline);
               RenderPipelineCompat.setSamplerUniform(var6, "Scene", this.handBackdropTarget.getColorTextureView(), var4);
               RenderPipelineCompat.setSamplerUniform(var6, "Depth", var3.getDepthTextureView(), var4);
               var6.draw(3, 1, 0, 0);
            } catch (Throwable var10) {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (var6 != null) {
               var6.close();
            }

            var5.clearDepthTexture(var3.getDepthTexture(), 0.0);
         } catch (RuntimeException var11) {
            this.fail("hand cover", var11);
         }
      }
   }

   void runWithEffectsTarget(long var1, Runnable var3) {
      if (this.effectsFrame == var1 && !this.failed) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         GpuTextureView var5 = OverlayTextures.colorTexture;
         GpuTextureView var6 = OverlayTextures.depthTexture;
         OverlayTextures.colorTexture = this.effectsTarget.getColorTextureView();
         OverlayTextures.depthTexture = var4.getDepthTextureView();

         try {
            var3.run();
         } finally {
            OverlayTextures.colorTexture = var5;
            OverlayTextures.depthTexture = var6;
         }
      } else {
         var3.run();
      }
   }

   void setRedirect(boolean var1) {
      this.redirectEnabled = var1;
   }

   RenderTarget redirectTarget(RenderTarget var1) {
      return (RenderTarget)(this.redirectEnabled && this.effectsTarget != null ? this.effectsTarget : var1);
   }

   void captureSelfDepth(long var1) {
      this.beginFrame(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.selfDepthTarget = captureHandBackdrop(this.selfDepthTarget, "Larp Stream Proof self depth", var3.width, var3.height, true);
      this.selfDepthTarget.copyDepthFrom(var3);
      this.selfDepthFrame = var1;
   }

   void reset(long var1, Runnable var3) {
      if (this.selfDepthFrame == var1 && this.selfDepthTarget != null) {
         this.beginFrame(var1);
         GpuTextureView var4 = OverlayTextures.colorTexture;
         GpuTextureView var5 = OverlayTextures.depthTexture;
         RenderTarget var6 = PostProcessing.outputTarget;
         OverlayTextures.colorTexture = this.effectsTarget.getColorTextureView();
         OverlayTextures.depthTexture = this.selfDepthTarget.getDepthTextureView();
         PostProcessing.outputTarget = this.effectsTarget;
         this.layerDepth++;

         try {
            var3.run();
         } finally {
            this.layerDepth--;
            OverlayTextures.colorTexture = var4;
            OverlayTextures.depthTexture = var5;
            PostProcessing.outputTarget = var6;
         }
      } else {
         this.renderGizmos(var1, var3);
      }
   }

   void renderGizmos(long var1, Runnable var3) {
      this.beginFrame(var1);
      this.drawBlur();
      GpuTextureView var4 = OverlayTextures.colorTexture;
      GpuTextureView var5 = OverlayTextures.depthTexture;
      RenderTarget var6 = PostProcessing.outputTarget;
      OverlayTextures.colorTexture = this.effectsTarget.getColorTextureView();
      OverlayTextures.depthTexture = this.effectsTarget.getDepthTextureView();
      PostProcessing.outputTarget = this.effectsTarget;
      this.layerDepth++;

      try {
         var3.run();
      } finally {
         this.layerDepth--;
         OverlayTextures.colorTexture = var4;
         OverlayTextures.depthTexture = var5;
         PostProcessing.outputTarget = var6;
      }
   }

   void runOutsideLayer(Runnable var1) {
      if (this.layerDepth == 0) {
         var1.run();
      } else {
         GpuTextureView var2 = OverlayTextures.colorTexture;
         GpuTextureView var3 = OverlayTextures.depthTexture;
         RenderTarget var4 = PostProcessing.outputTarget;
         int var5 = this.layerDepth;
         OverlayTextures.colorTexture = null;
         OverlayTextures.depthTexture = null;
         PostProcessing.outputTarget = null;
         this.layerDepth = 0;

         try {
            var1.run();
         } finally {
            this.layerDepth = var5;
            OverlayTextures.colorTexture = var2;
            OverlayTextures.depthTexture = var3;
            PostProcessing.outputTarget = var4;
         }
      }
   }

   boolean composeInterface(long var1, long var3, boolean var5, Consumer<RenderTarget> var6, StreamProofRenderer.ReadBackListener var7) {
      if (this.failed) {
         this.guiRenderState.reset();
         return false;
      }

      RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      String var9 = "resources";
      long var10 = PerfProfiler.startTimer();

      try {
         this.compilePipelines();
         int var12 = var8.width;
         int var13 = var8.height;
         this.interfaceTarget = captureHandBackdrop(this.interfaceTarget, "Larp Stream Proof interface", var12, var13, true);
         this.packTarget = captureHandBackdrop(this.packTarget, "Larp Stream Proof pack", var12, var13, false);
         CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
         var14.clearColorAndDepthTextures(this.interfaceTarget.getColorTexture(), cLEAR_COLOR, this.interfaceTarget.getDepthTexture(), 0.0);
         var9 = "interface blur";
         var6.accept(this.interfaceTarget);
         var9 = "interface GUI";
         this.getGuiRenderer().render();
         this.getGuiRenderer().endFrame();
         var9 = "compose";
         int var15 = var5 && this.effectsFrame == var1 && this.effectsTarget != null && this.effectsTarget.width == var12 && this.effectsTarget.height == var13
            ? 1
            : 0;
         GpuSampler var16 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         RenderPass var17 = var14.createRenderPass(() -> "Larp Stream Proof compose", this.packTarget.getColorTextureView(), Optional.empty());

         try {
            RenderPipelineCompat.setPipeline(var17, var15 != 0 ? composePipeline : interfaceComposePipeline);
            RenderPipelineCompat.setSamplerUniform(var17, "Interface", this.interfaceTarget.getColorTextureView(), var16);
            if (var15 != 0) {
               RenderPipelineCompat.setSamplerUniform(var17, "Effects", this.effectsTarget.getColorTextureView(), var16);
            }

            var17.draw(3, 1, 0, 0);
         } catch (Throwable var26) {
            if (var17 != null) {
               try {
                  var17.close();
               } catch (Throwable var25) {
                  var26.addSuppressed(var25);
               }
            }

            throw var26;
         }

         if (var17 != null) {
            var17.close();
         }

         var9 = "read-back";
         return this.readBack(var3, var12, var13, var7);
      } catch (RuntimeException var27) {
         this.guiRenderState.reset();
         this.fail(var9, var27);
         return false;
      } finally {
         PerfProfiler.endTimer("Stream Proof compose", var10);
      }
   }

   void discardFrame() {
      this.guiRenderState.reset();
   }

   void renderBlur(RenderTarget var1) {
      if (PostProcessing.resetBlur()) {
         this.drawBlur(var1, null);
      } else {
         for (int[] var3 : PostProcessing.getBlurRegions()) {
            this.drawBlur(var1, var3);
         }
      }
   }

   boolean blurInterface() {
      if (this.interfaceTarget != null && !this.failed) {
         this.drawBlur(this.interfaceTarget, null);
         return true;
      } else {
         return false;
      }
   }

   private void drawBlur(RenderTarget var1, int[] var2) {
      GpuTextureView var3 = PostProcessing.getBlurTexture();
      if (var3 != null) {
         this.compilePipelines();
         RenderPass var4 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp Stream Proof blur", var1.getColorTextureView(), Optional.empty());

         try {
            RenderPipelineCompat.setPipeline(var4, blurPipeline);
            RenderPipelineCompat.setSamplerUniform(var4, "Source", var3, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            if (var2 != null) {
               int var5 = Minecraft.getInstance().getWindow().getGuiScale();
               int var6 = var2[0];
               int var7 = var2[1];
               int var8 = var2[2];
               int var9 = var2[3];
               var4.enableScissor(
                  Math.max(0, var6 * var5),
                  Math.max(0, var1.height - (var7 + var9) * var5),
                  Math.max(0, Math.min(var8 * var5, var1.width - var6 * var5)),
                  Math.max(0, Math.min(var9 * var5, var1.height - var7 * var5))
               );
            }

            var4.draw(3, 1, 0, 0);
         } catch (Throwable var11) {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (var4 != null) {
            var4.close();
         }
      }
   }

   void renderGizmos(List<GizmoInstance> var1, CameraRenderState var2, FeatureRenderDispatcher var3) {
      if (!this.failed && !var1.isEmpty()) {
         long var4 = PerfProfiler.startTimer();

         try {
            long var6 = Util.getMillis();
            DrawableGizmoPrimitives var8 = new DrawableGizmoPrimitives();
            DrawableGizmoPrimitives var9 = new DrawableGizmoPrimitives();

            for (GizmoInstance var11 : var1) {
               var11.gizmo().emit(var11.isAlwaysOnTop() ? var9 : var8, var11.getAlphaMultiplier(var6));
            }

            SubmitNodeStorage var29 = new SubmitNodeStorage();
            var8.submit(var29, var2, false);
            var9.submit(var29, var2, true);
            RenderTarget var30 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            PreparedFrame var12 = var3.prepareFrame(var29);

            try {
               RenderPass var13 = RenderSystem.getDevice()
                  .createCommandEncoder()
                  .createRenderPass(
                     () -> "Larp stream proof gizmos", var30.getColorTextureView(), Optional.empty(), var30.getDepthTextureView(), OptionalDouble.empty()
                  );

               try {
                  RenderSystem.bindDefaultUniforms(var13);
                  FeatureRenderDispatcher.renderAllFeatures(var13, var12);
               } catch (Throwable var25) {
                  if (var13 != null) {
                     try {
                        var13.close();
                     } catch (Throwable var24) {
                        var25.addSuppressed(var24);
                     }
                  }

                  throw var25;
               }

               if (var13 != null) {
                  var13.close();
               }
            } catch (Throwable var26) {
               if (var12 != null) {
                  try {
                     var12.close();
                  } catch (Throwable var23) {
                     var26.addSuppressed(var23);
                  }
               }

               throw var26;
            }

            if (var12 != null) {
               var12.close();
            }
         } catch (RuntimeException var27) {
            this.fail("gizmo overlay", var27);
         } finally {
            PerfProfiler.endTimer("Stream Proof gizmos", var4);
         }
      }
   }

   private boolean readBack(long var1, int var3, int var4, StreamProofRenderer.ReadBackListener var5) {
      StreamProofRenderer.ReadBackSlot var6 = null;

      for (int var7 = 0; var7 < 3; var7++) {
         if (this.readBackSlots[var7] == null) {
            this.readBackSlots[var7] = new StreamProofRenderer.ReadBackSlot();
         }

         StreamProofRenderer.ReadBackSlot var8 = this.readBackSlots[var7];
         if (!var8.pending) {
            if (var8.buffer == null || var8.width != var3 || var8.height != var4) {
               if (var8.buffer != null) {
                  var8.buffer.close();
               }

               var8.buffer = RenderSystem.getDevice().createBuffer(() -> "Larp Stream Proof read-back", 9, (long)var3 * var4 * 4L);
               var8.width = var3;
               var8.height = var4;
            }

            var6 = var8;
            break;
         }
      }

      if (var6 == null) {
         PerfProfiler.addCounter("Stream Proof skipped read-backs", 1L);
         return false;
      } else {
         StreamProofRenderer.ReadBackSlot var9 = var6;
         var9.pending = (boolean)1;
         RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(this.packTarget.getColorTexture(), var9.buffer, 0L, () -> {
            try {
               if (!var9.discardRequested && var1 > this.lastDeliveredFrame) {
                  MappedView var7x = var9.buffer.map(true, false);

                  try {
                     var5.onFrame(var1, var3, var4, var7x.data());
                  } catch (Throwable var16) {
                     if (var7x != null) {
                        try {
                           var7x.close();
                        } catch (Throwable var15) {
                           var16.addSuppressed(var15);
                        }
                     }

                     throw var16;
                  }

                  if (var7x != null) {
                     var7x.close();
                  }

                  this.lastDeliveredFrame = var1;
               }
            } catch (RuntimeException var17) {
               LarpLog.errorOnce("StreamProof", "Read-back delivery failed; the overlay keeps its previous frame", var17);
            } finally {
               var9.pending = (boolean)0;
               if (var9.discardRequested && var9.buffer != null) {
                  var9.buffer.close();
                  var9.buffer = null;
               }
            }
         }, 0);
         return true;
      }
   }

   private GuiRenderer getGuiRenderer() {
      if (this.guiRenderer == null) {
         Minecraft var1 = Minecraft.getInstance();
         this.guiRenderer = new GuiRenderer(
            this.guiRenderState,
            var1.gameRenderer.featureRenderDispatcher(),
            List.of(
               new GuiEntityRenderer(var1.getEntityRenderDispatcher()),
               new GuiSkinRenderer(),
               new GuiBookModelRenderer(),
               new GuiBannerResultRenderer(var1.getAtlasManager()),
               new GuiProfilerChartRenderer()
            )
         );
         LarpLog.debug("StreamProof", "Interface GUI renderer created");
      }

      return this.guiRenderer;
   }

   private void compilePipelines() {
      if (!this.pipelinesCompiled) {
         for (RenderPipeline var4 : new RenderPipeline[]{composePipeline, interfaceComposePipeline, blurPipeline, handCoverPipeline, skyPipeline}) {
            ClientPipelines.validatePipeline(var4);
         }

         this.pipelinesCompiled = (boolean)1;
         LarpLog.info("StreamProof", "Layer pipelines compiled");
      }
   }

   private void fail(String var1, RuntimeException var2) {
      this.failed = (boolean)1;
      LarpLog.error("StreamProof", "Layer stage=" + var1 + " failed; Stream Proof falls back to normal drawing until it is re-enabled", var2);
   }

   private static TextureTarget captureHandBackdrop(TextureTarget var0, String var1, int var2, int var3, boolean var4) {
      if (var0 != null && var0.width == var2 && var0.height == var3) {
         return var0;
      }

      if (var0 != null) {
         var0.destroyBuffers();
      }

      return RenderPipelineCompat.createTextureTarget(var1, var2, var3, var4, GpuFormat.RGBA8_UNORM);
   }

   void reset() {
      this.guiRenderState.reset();
      if (this.guiRenderer != null) {
         this.guiRenderer.close();
         this.guiRenderer = null;
      }

      for (TextureTarget var4 : new TextureTarget[]{
         this.interfaceTarget, this.effectsTarget, this.skyTarget, this.selfDepthTarget, this.handBackdropTarget, this.packTarget
      }) {
         if (var4 != null) {
            var4.destroyBuffers();
         }
      }

      this.interfaceTarget = this.effectsTarget = this.skyTarget = this.selfDepthTarget = this.handBackdropTarget = this.packTarget = null;
      this.redirectEnabled = (boolean)0;
      this.selfDepthFrame = -1L;
      this.depthCopied = this.skyCleared = (boolean)0;

      for (int var5 = 0; var5 < 3; var5++) {
         StreamProofRenderer.ReadBackSlot var6 = this.readBackSlots[var5];
         if (var6 != null) {
            if (var6.pending) {
               var6.discardRequested = (boolean)1;
            } else if (var6.buffer != null) {
               var6.buffer.close();
            }

            this.readBackSlots[var5] = null;
         }
      }

      this.effectsFrame = -1L;
      this.lastDeliveredFrame = -1L;
      this.layerDepth = 0;
      this.failed = (boolean)0;
      this.pipelinesCompiled = (boolean)0;
   }

   private static RenderPipeline hasFailed(String var0, String... var1) {
      return compilePipelines(var0, ColorTargetState.DEFAULT, var1);
   }

   private static RenderPipeline compilePipelines(String var0, ColorTargetState var1, String... var2) {
      Builder var3 = BindGroupLayout.builder();

      for (String var7 : var2) {
         var3.withUniform(var7, RenderPipelineCompat.sAMPLER_UNIFORM_TYPE);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/" + var0))
         .withBindGroupLayout(var3.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1)
         .withCull(false)
         .build();
   }

   interface ReadBackListener {
      void onFrame(long var1, int var3, int var4, ByteBuffer var5);
   }

   private static final class ReadBackSlot {
      GpuBuffer buffer;
      int width;
      int height;
      boolean pending;
      boolean discardRequested;
   }
}
