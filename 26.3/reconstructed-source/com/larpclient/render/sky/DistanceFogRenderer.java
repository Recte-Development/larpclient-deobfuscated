package com.larpclient.render.sky;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.FogModule;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.SceneCapture;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
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
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.StartMain;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class DistanceFogRenderer {
   private static final Identifier iD = Identifier.fromNamespaceAndPath("larp-client", "distance_fog");
   private static final RenderPipeline fogPipeline = createPipeline("distance_fog", 7);
   private static final RenderPipeline layerPipeline = createPipeline("distance_fog_layer", 15);
   static final int uNIFORM_SIZE = 128;
   private static final Matrix4f worldProjection = new Matrix4f();
   private static boolean hasWorldProjection;
   private static boolean disabled;
   private static boolean irisLogged;
   private static boolean projectionWarned;
   private static boolean depthCopyFailed;
   private static final GpuBuffer[] parameterBuffers = new GpuBuffer[2];
   private static GpuTexture opaqueDepthTexture;
   private static GpuTextureView opaqueDepthView;
   private static boolean composedBeforeTranslucent;
   private static int resourceGeneration = -1;
   private static String lastSkipReason;
   private static String lastComposeMode;

   private static RenderPipeline createPipeline(String var0, int var1) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/distance_fog"))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("Depth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
               .withUniform("OpaqueDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
               .withUniform("DistanceFog", UniformType.UNIFORM_BUFFER)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), ColorTargetState.DEFAULT.format(), var1))
         .withCull(false)
         .build();
   }

   public static void register() {
      LevelRenderEvents.START_MAIN.register((StartMain)var0 -> composedBeforeTranslucent = (boolean)0);
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.addPhaseOrdering(iD, Event.DEFAULT_PHASE);
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register(iD, DistanceFogRenderer::renderBeforeTranslucent);
      LevelRenderEvents.END_MAIN.addPhaseOrdering(iD, Event.DEFAULT_PHASE);
      LevelRenderEvents.END_MAIN.register(iD, DistanceFogRenderer::renderEndMain);
   }

   public static void captureProjection(Matrix4f var0) {
      worldProjection.set(var0);
      hasWorldProjection = (boolean)1;
   }

   public static boolean getWorldProjection(Matrix4f var0) {
      if (hasWorldProjection) {
         var0.set(worldProjection);
      }

      return hasWorldProjection;
   }

   private static void renderBeforeTranslucent(LevelRenderContext var0) {
      composedBeforeTranslucent = (boolean)0;
      if (!depthCopyFailed && !StreamProofManager.submitFrame()) {
         composedBeforeTranslucent = compose(var0, DistanceFogRenderer.ComposePass.BEFORE_TRANSLUCENT);
      }
   }

   private static void renderEndMain(LevelRenderContext var0) {
      DistanceFogRenderer.ComposePass var1 = composedBeforeTranslucent && opaqueDepthView != null
         ? DistanceFogRenderer.ComposePass.TRANSLUCENT
         : DistanceFogRenderer.ComposePass.END_MAIN;
      composedBeforeTranslucent = (boolean)0;
      if (compose(var0, var1)) {
         String var2 = var1 == DistanceFogRenderer.ComposePass.TRANSLUCENT
            ? "before translucent terrain, then translucent surfaces by their own depth"
            : "once from the final depth ("
               + (
                  StreamProofManager.submitFrame()
                     ? "Stream Proof Hide Everything"
                     : (depthCopyFailed ? "opaque depth copy failed" : "no draw before translucent terrain")
               )
               + ")";
         if (!var2.equals(lastComposeMode)) {
            lastComposeMode = var2;
            LarpLog.info("Fog", "Composed {}", var2);
         }
      }
   }

   private static boolean compose(LevelRenderContext var0, DistanceFogRenderer.ComposePass var1) {
      Minecraft var2 = Minecraft.getInstance();
      FogModule var3 = FogModule.getInstance();
      if (var2.level != null && var3 != null && var3.isFogActive()) {
         CameraRenderState var4 = var0.levelState().cameraRenderState;
         if (var4 == null || var4.pos == null || var4.viewRotationMatrix == null || var4.projectionMatrix == null) {
            compose("camera state not extracted");
            return false;
         }

         if (!isFogTypeSupported(var4.fogType)) {
            compose("camera in " + var4.fogType + " fog");
            return false;
         }

         if (compose()) {
            compose("shader pack active");
            return false;
         }

         float var5 = var3.getSkyVisibility(var2.getDeltaTracker().getGameTimeDeltaPartialTick(false));
         if (var5 <= 0.001F) {
            compose("faded underground");
            return false;
         }

         if (lastSkipReason != null) {
            LarpLog.debug("Fog", "Resumed after {}", lastSkipReason);
            lastSkipReason = null;
         }

         if (resourceGeneration != PostProcessing.reloadCount) {
            releaseResources();
            depthCopyFailed = (boolean)0;
            disabled = (boolean)0;
            resourceGeneration = PostProcessing.reloadCount;
         }

         if (disabled) {
            return false;
         }

         long var6 = PerfProfiler.startTimer();
         String var8 = "resources";

         try {
            if (parameterBuffers[0] == null) {
               ClientPipelines.validatePipeline(fogPipeline);
               ClientPipelines.validatePipeline(layerPipeline);
               LarpLog.info("Fog", "Fog shader loaded; composed before translucent terrain and over translucent surfaces");
            }

            GpuBuffer var9 = parameterBuffers[var1.BUFFERINDEX];
            if (var9 == null) {
               var9 = parameterBuffers[var1.BUFFERINDEX] = RenderSystem.getDevice().createBuffer(() -> "Larp distance fog parameters", 136, 128L);
            }

            RenderTarget var10 = var2.gameRenderer.mainRenderTarget();
            if (var1 == DistanceFogRenderer.ComposePass.BEFORE_TRANSLUCENT) {
               var8 = "opaque depth copy";
               if (!copyDepth(var10)) {
                  return false;
               }
            }

            var8 = "parameters";
            if (!hasWorldProjection && !projectionWarned) {
               projectionWarned = (boolean)1;
               LarpLog.warn("Fog", "World projection was not captured; using the unbobbed camera projection");
            }

            Matrix4f var11 = new Matrix4f(hasWorldProjection ? worldProjection : var4.projectionMatrix).mul(var4.viewRotationMatrix).invert();
            LevelRenderState var12 = var0.levelState();
            float var13 = var12.skyRenderState.sunAngle;
            float var14 = -((float)Math.sin(var13));
            float var15 = (float)Math.cos(var13);
            float var16 = Math.clamp(var12.weatherRenderState.intensity, 0.0F, 1.0F);
            float var17 = smoothstep(-0.08F, 0.14F, var15) * (1.0F - var16 * 0.85F);
            float[] var18 = compose(var3, var4.fogData == null ? null : var4.fogData.color);
            float var19 = var2.level.getSeaLevel();
            float var20 = (float)Math.sin(Math.toRadians(var3.skyHeight.getValue()));
            MemoryStack var21 = MemoryStack.stackPush();

            try {
               ByteBuffer var22 = var21.malloc(128);
               var11.get(var22);
               var22.position(64);
               var22.putFloat((float)var4.pos.y - var19)
                  .putFloat(1.0F / var3.height.getValue().floatValue())
                  .putFloat(RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F)
                  .putFloat(var1.TRANSLUCENTFLAG);
               var22.putFloat(var3.startDistance.getValue().floatValue()).putFloat(var3.getEffectiveEndDistance()).putFloat(1.0F * var5).putFloat(var20);
               var22.putFloat(var18[0]).putFloat(var18[1]).putFloat(var18[2]).putFloat(var3.sunGlow.getValue().floatValue());
               var22.putFloat(var14).putFloat(var15).putFloat(0.0F).putFloat(var17);
               var22.flip();
               RenderSystem.getDevice().createCommandEncoder().writeToBuffer(var9.slice(), var22);
            } catch (Throwable var37) {
               if (var21 != null) {
                  try {
                     var21.close();
                  } catch (Throwable var35) {
                     var37.addSuppressed(var35);
                  }
               }

               throw var37;
            }

            if (var21 != null) {
               var21.close();
            }

            var8 = "compose " + var1.LABEL;
            RenderTarget var40 = StreamProofManager.setHideEverything(var10);
            RenderPass var41 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp distance fog", var40.getColorTextureView(), Optional.empty());

            try {
               RenderPipelineCompat.setPipeline(var41, var40 == var10 ? fogPipeline : layerPipeline);
               var41.setUniform("DistanceFog", var9.slice());
               GpuSampler var23 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               RenderPipelineCompat.setSamplerUniform(var41, "Depth", var10.getDepthTextureView(), var23);
               RenderPipelineCompat.setSamplerUniform(
                  var41, "OpaqueDepth", var1 == DistanceFogRenderer.ComposePass.TRANSLUCENT ? opaqueDepthView : var10.getDepthTextureView(), var23
               );
               var41.draw(3, 1, 0, 0);
            } catch (Throwable var36) {
               if (var41 != null) {
                  try {
                     var41.close();
                  } catch (Throwable var34) {
                     var36.addSuppressed(var34);
                  }
               }

               throw var36;
            }

            if (var41 != null) {
               var41.close();
            }

            SceneCapture.invalidateColor();
            PerfProfiler.addCounter("Distance fog passes", 1L);
            return true;
         } catch (RuntimeException var38) {
            disabled = (boolean)1;

            try {
               releaseResources();
            } catch (RuntimeException var33) {
               var38.addSuppressed(var33);
            }

            LarpLog.error("Fog", "DistanceFogRenderer stage=" + var8 + "; fog disabled until resource reload", var38);
            return false;
         } finally {
            PerfProfiler.endTimer("Distance fog", var6);
         }
      } else {
         lastSkipReason = null;
         return false;
      }
   }

   private static boolean copyDepth(RenderTarget var0) {
      try {
         GpuTexture var1 = var0.getDepthTexture();
         if (var1 == null) {
            return false;
         }

         if (opaqueDepthTexture == null
            || opaqueDepthTexture.getWidth(0) != var1.getWidth(0)
            || opaqueDepthTexture.getHeight(0) != var1.getHeight(0)
            || opaqueDepthTexture.getFormat() != var1.getFormat()) {
            copyDepth();
            opaqueDepthTexture = RenderSystem.getDevice()
               .createTexture("Larp fog depth before translucent terrain", 13, var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
            opaqueDepthView = RenderSystem.getDevice().createTextureView(opaqueDepthTexture);
            LarpLog.debug("Fog", "Opaque depth copy allocated {}x{}", var1.getWidth(0), var1.getHeight(0));
         }

         RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var1, opaqueDepthTexture, 0, 0, 0, 0, 0, var1.getWidth(0), var1.getHeight(0));
         return true;
      } catch (RuntimeException var4) {
         depthCopyFailed = (boolean)1;

         try {
            copyDepth();
         } catch (RuntimeException var3) {
            var4.addSuppressed(var3);
         }

         LarpLog.error("Fog", "Depth copy before translucent terrain failed; fog is composed once from the final depth until resource reload", var4);
         return false;
      }
   }

   private static void copyDepth() {
      GpuTextureView var0 = opaqueDepthView;
      GpuTexture var1 = opaqueDepthTexture;
      opaqueDepthView = null;
      opaqueDepthTexture = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }
   }

   static boolean isFogTypeSupported(FogType var0) {
      return var0 == FogType.NONE || var0 == FogType.ATMOSPHERIC;
   }

   private static void compose(String var0) {
      if (!var0.equals(lastSkipReason)) {
         lastSkipReason = var0;
         LarpLog.debug("Fog", "Pass skipped: {}", var0);
      }
   }

   static float[] compose(FogModule var0, Vector4f var1) {
      if (!var0.colorMode.isMode("Custom") && var1 != null) {
         Integer var3 = StreamProofManager.isHidingInterface() ? getSkyHorizonColor() : null;
         return var3 != null
            ? new float[]{(var3 >> 16 & 0xFF) / 255.0F, (var3 >> 8 & 0xFF) / 255.0F, (var3 & 0xFF) / 255.0F}
            : new float[]{Math.clamp(var1.x, 0.0F, 1.0F), Math.clamp(var1.y, 0.0F, 1.0F), Math.clamp(var1.z, 0.0F, 1.0F)};
      } else {
         int var2 = var0.colorMode.isMode("Custom") ? var0.customColor.getValue() : -4601895;
         return new float[]{(var2 >> 16 & 0xFF) / 255.0F, (var2 >> 8 & 0xFF) / 255.0F, (var2 & 0xFF) / 255.0F};
      }
   }

   private static Integer getSkyHorizonColor() {
      CustomSkyModule var0 = CustomSkyModule.getInstance();
      if (CustomSkyModule.isActive()) {
         return var0.getNebulaColor() ? var0.getGradient().horizon() : null;
      }

      SkyShaderModule var1 = SkyShaderModule.getInstance();
      return var1 != null && var1.shouldRender() ? var1.getThemeColor() : null;
   }

   static float smoothstep(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   private static boolean compose() {
      if (!FabricLoader.getInstance().isModLoaded("iris")) {
         return false;
      }

      try {
         Object var0 = Class.forName("net.irisshaders.iris.api.v0.IrisApi").getMethod("getInstance").invoke(null);
         boolean var1 = (Boolean)var0.getClass().getMethod("isShaderPackInUse").invoke(var0);
         if (var1 && !irisLogged) {
            irisLogged = (boolean)1;
            LarpLog.info("Fog", "Iris shader pack in use; fog paused");
         }

         if (!var1) {
            irisLogged = (boolean)0;
         }

         return var1;
      } catch (ReflectiveOperationException | RuntimeException var2) {
         if (!irisLogged) {
            irisLogged = (boolean)1;
            LarpLog.warn("Fog", "Iris detected without its API; fog paused");
         }

         return true;
      }
   }

   public static void releaseResources() {
      composedBeforeTranslucent = (boolean)0;

      try {
         for (int var0 = 0; var0 < parameterBuffers.length; var0++) {
            GpuBuffer var1 = parameterBuffers[var0];
            parameterBuffers[var0] = null;
            if (var1 != null) {
               var1.close();
            }
         }
      } finally {
         copyDepth();
      }
   }

   private DistanceFogRenderer() {
   }

   enum ComposePass {
      END_MAIN(0, 0, "end of the main pass"),
      BEFORE_TRANSLUCENT(0, 0, "before translucent terrain"),
      TRANSLUCENT(1, 1, "translucent surfaces");

      final int TRANSLUCENTFLAG;
      final int BUFFERINDEX;
      final String LABEL;

      ComposePass(int var3, int var4, String var5) {
         this.TRANSLUCENTFLAG = var3;
         this.BUFFERINDEX = var4;
         this.LABEL = var5;
      }
   }
}
