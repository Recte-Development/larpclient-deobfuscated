package com.larpclient.render.world;

import com.larpclient.module.misc.Module;
import com.larpclient.module.render.RealisticWaterModule;
import com.larpclient.module.render.SnowGroundModule;
import com.larpclient.module.render.WavingFoliageModule;
import com.larpclient.module.render.WetGroundModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.overlay.DropletSurfaceRenderer;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.AfterSolidFeatures;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.AfterTranslucentTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeTranslucentTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.StartMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryStack;

public final class TerrainMaterials {
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> materialPipelines = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> decodePipelines = new IdentityHashMap<>();
   private static final RenderPipeline wetReflectionPipeline = isNativeHookUnavailable("wet_reflection");
   private static final RenderPipeline underwaterPipeline = isNativeHookUnavailable("underwater");
   public static final int uNIFORM_BUFFER_SIZE = 1088;
   private static final TerrainMaterials.SceneCapture sceneCapture = new TerrainMaterials.SceneCapture();
   private static GpuBuffer materialBuffer;
   private static boolean initialized;
   private static boolean failed;
   private static boolean prepared;
   private static boolean sceneCaptured;
   private static boolean sodiumOrIrisLoaded;
   private static boolean sodiumWetGroundActive;
   private static int shaderGeneration = -1;
   private static Object lastLevel;
   private static LevelRenderState levelState;
   private static double lastTime;
   private static double foliageTime;
   private static boolean chunksRebuiltForMaterials;

   public static void initialize() {
      if (!initialized) {
         initialized = (boolean)1;
         sodiumOrIrisLoaded = (boolean)(!FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("iris") ? 0 : 1);
         sodiumWetGroundActive = (boolean)(sodiumOrIrisLoaded && SodiumTerrainPipelines.isSupported() ? 1 : 0);
         SingleColorBlocks.init();
         if (sodiumWetGroundActive) {
            LarpLog.info(
               "TerrainMaterials",
               "Sodium {} detected; Wet Ground draws through Sodium's chunk shader, other terrain materials keep Sodium output",
               SodiumTerrainPipelines.sodiumVersion()
            );
         } else if (sodiumOrIrisLoaded) {
            LarpLog.warn(
               "TerrainMaterials", "Sodium/Iris detected; native terrain material hooks are unavailable, preserving renderer output and saved module choices"
            );
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0 -> isNativeHookUnavailable(var0.levelState()));
         LevelRenderEvents.AFTER_SOLID_FEATURES.register((AfterSolidFeatures)var0 -> renderWetReflection());
         LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> captureWaterScene());
         LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register((AfterTranslucentTerrain)var0 -> {
            if (isMaterialPassActive()) {
               try {
                  DropletSurfaceRenderer.render();
               } catch (RuntimeException var2) {
                  bindMaterial("volumetric-surface", var2);
               }
            }

            isHookDisabled();
         });
         ClientTickEvents.END_CLIENT_TICK.register(RealisticWater::tick);
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> reset());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> reset());
      }
   }

   public static boolean isHookDisabled() {
      return failed || sodiumOrIrisLoaded;
   }

   public static boolean isMaterialPassActive() {
      return prepared && !isHookDisabled();
   }

   public static boolean isNativeHookUnavailable() {
      return failed || sodiumOrIrisLoaded && (!sodiumWetGroundActive || SodiumTerrainPipelines.shouldFallBack());
   }

   private static boolean isMaterialPassReady() {
      return prepared && !isNativeHookUnavailable();
   }

   private static boolean shouldPrepare() {
      return sodiumOrIrisLoaded ? !isNativeHookUnavailable() && isModuleActive(WetGroundModule.getInstance()) : !failed;
   }

   private static boolean isModuleActive(Module var0) {
      return var0 != null && var0.isRuntimeActive();
   }

   private static boolean isAnyMaterialActive() {
      return isModuleActive(WavingFoliageModule.getInstance())
         || isModuleActive(WetGroundModule.getInstance())
         || isModuleActive(SnowGroundModule.getQualityLevel())
         || isModuleActive(RealisticWaterModule.getInstance());
   }

   public static void onNativeActivated() {
      LarpLog.debug("TerrainMaterials", "Native terrain material activated; generations follow shader reloads");
   }

   public static void refreshChunks() {
      BlockRenderTags.refreshModuleStates();
      Minecraft var0 = Minecraft.getInstance();
      int var1 = !isModuleActive(WavingFoliageModule.getInstance())
            && !isModuleActive(RealisticWaterModule.getInstance())
            && !isModuleActive(SnowGroundModule.getQualityLevel())
         ? 0
         : 1;
      if (var0 != null && (var1 != 0 || chunksRebuiltForMaterials)) {
         chunksRebuiltForMaterials = (boolean)var1;
         var0.execute(() -> {
            if (var0.level != null) {
               var0.levelExtractor.allChanged();
            }
         });
      }
   }

   public static void isNativeHookUnavailable() {
      if (!isAnyMaterialActive()) {
         releaseResources();
      }
   }

   private static void isNativeHookUnavailable(LevelRenderState var0) {
      levelState = var0;
      sceneCaptured = (boolean)0;
      Minecraft var1 = Minecraft.getInstance();
      if (lastLevel != var1.level) {
         releaseResources();
         lastLevel = var1.level;
         lastTime = TickTimer.getTime();
         foliageTime = 0.0;
         BlockRenderTags.refreshModuleStates();
      }

      if (shaderGeneration != PostProcessing.reloadCount) {
         releaseResources();
         failed = (boolean)0;
         shaderGeneration = PostProcessing.reloadCount;
         materialPipelines.clear();
         decodePipelines.clear();
         SodiumTerrainPipelines.clear();
         refreshChunks();
      }

      if (isAnyMaterialActive()
         && shouldPrepare()
         && var0.cameraRenderState.pos != null
         && var0.cameraRenderState.projectionMatrix != null
         && var0.cameraRenderState.viewRotationMatrix != null) {
         long var2 = PerfProfiler.startTimer();

         try {
            if (materialBuffer == null) {
               materialBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp terrain material parameters", 136, 1088L);
               if (sodiumWetGroundActive) {
                  ClientPipelines.validatePipeline(wetReflectionPipeline);
                  LarpLog.info("TerrainMaterials", "Sodium Wet Ground material and wet reflection shaders ready");
               } else {
                  createDecodePipeline(RenderPipelines.SOLID_TERRAIN);
                  createDecodePipeline(RenderPipelines.CUTOUT_TERRAIN);
                  createDecodePipeline(RenderPipelines.TRANSLUCENT_TERRAIN);
                  remapTerrainPipeline(RenderPipelines.SOLID_TERRAIN);
                  remapTerrainPipeline(RenderPipelines.CUTOUT_TERRAIN);
                  remapTerrainPipeline(RenderPipelines.TRANSLUCENT_TERRAIN);
                  ClientPipelines.validatePipeline(wetReflectionPipeline);
                  ClientPipelines.validatePipeline(underwaterPipeline);
                  LarpLog.info("TerrainMaterials", "Terrain, water, foliage, wet reflection, snow and underwater shaders ready");
               }
            }

            initialize(var0);
            if (sodiumWetGroundActive) {
               SodiumTerrainPipelines.buildPending();
               SodiumTerrainPipelines.shouldFallBack();
            }

            prepared = (boolean)1;
         } catch (RuntimeException var8) {
            bindMaterial(sodiumWetGroundActive ? "sodium-prepare" : "prepare", var8);
         } finally {
            PerfProfiler.endTimer("TerrainMaterials", var2);
         }
      }
   }

   public static RenderPipeline remapSodiumPipeline(RenderPipeline var0) {
      SodiumTerrainPipelines.markTerrainDrawn();
      RenderPipeline var1 = SingleColorBlocks.init(var0);
      if (var1 != null) {
         return var1;
      } else {
         return sodiumWetGroundActive && isMaterialPassReady() && isModuleActive(WetGroundModule.getInstance())
            ? SodiumTerrainPipelines.resolvePipeline(var0)
            : var0;
      }
   }

   public static RenderPipeline remapTerrainPipeline(RenderPipeline var0) {
      if (sodiumOrIrisLoaded) {
         return var0;
      } else {
         RenderPipeline var1 = SingleColorBlocks.getVanillaPipeline(var0);
         if (var1 != null) {
            return var1;
         } else if (!failed && isAnyMaterialActive() && (var0 != RenderPipelines.TRANSLUCENT_TERRAIN || isModuleActive(RealisticWaterModule.getInstance()))) {
            return var0 != RenderPipelines.SOLID_TERRAIN && var0 != RenderPipelines.CUTOUT_TERRAIN && var0 != RenderPipelines.TRANSLUCENT_TERRAIN
               ? var0
               : materialPipelines.computeIfAbsent(
                  var0,
                  var0x -> {
                     int var1x = var0x == RenderPipelines.TRANSLUCENT_TERRAIN ? 1 : 0;
                     Builder var2 = RenderPipeline.builder(new Snippet[0])
                        .withLocation(identifier("pipeline/temple/" + var0x.getLocation().getPath()))
                        .withVertexShader(identifier("temple/terrain"))
                        .withFragmentShader(identifier("temple/terrain"))
                        .withCull(var0x.isCull())
                        .withPolygonMode(var0x.getPolygonMode())
                        .withPrimitiveTopology(var0x.getPrimitiveTopology())
                        .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

                     for (BindGroupLayout var4 : var0x.getBindGroupLayouts()) {
                        var2.withBindGroupLayout(var4);
                     }

                     com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder var7 = BindGroupLayout.builder()
                        .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER);
                     if (var1x != 0) {
                        var7.withUniform("SceneColor", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
                           .withUniform("SceneDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE);
                     }

                     var2.withBindGroupLayout(var7.build());
                     List var8 = RenderPipelineCompat.getColorTargetStates(var0x);

                     for (int var5 = 0; var5 < var8.size(); var5++) {
                        var2.withColorTargetState(var5, (ColorTargetState)var8.get(var5));
                     }

                     List var9 = RenderPipelineCompat.getVertexFormatBindings(var0x);

                     for (int var6 = 0; var6 < var9.size(); var6++) {
                        var2.withVertexBinding(var6, (VertexFormat)var9.get(var6));
                     }

                     var0x.getShaderDefines().flags().forEach(var2::withShaderDefine);
                     var0x.getShaderDefines().values().forEach((var1xx, var2x) -> var2.withShaderDefine(var1xx, Float.parseFloat(var2x)));
                     if (var1x != 0) {
                        var2.withShaderDefine("LARP_WATER");
                     }

                     RenderPipeline var10 = var2.build();
                     ClientPipelines.validatePipeline(var10);
                     return var10;
                  }
               );
         } else {
            return decodePipelines.getOrDefault(var0, var0);
         }
      }
   }

   private static RenderPipeline createDecodePipeline(RenderPipeline var0) {
      return decodePipelines.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(identifier("pipeline/temple/decode/" + var0x.getLocation().getPath()))
               .withVertexShader(identifier("temple/terrain_decode"))
               .withFragmentShader(RenderPipelineCompat.getFragmentShaderId(var0x))
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));

            for (BindGroupLayout var3 : var0x.getBindGroupLayouts()) {
               var1.withBindGroupLayout(var3);
            }

            List var5 = RenderPipelineCompat.getColorTargetStates(var0x);

            for (int var6 = 0; var6 < var5.size(); var6++) {
               var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
            }

            List var7 = RenderPipelineCompat.getVertexFormatBindings(var0x);

            for (int var4 = 0; var4 < var7.size(); var4++) {
               var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
            }

            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2) -> var1.withShaderDefine(var1x, Float.parseFloat(var2)));
            RenderPipeline var8 = var1.build();
            ClientPipelines.validatePipeline(var8);
            return var8;
         }
      );
   }

   public static void bindMaterial(RenderPass var0, RenderPipeline var1) {
      if (!SingleColorBlocks.bindUniform(var0, var1)) {
         if (materialBuffer != null && !failed && (materialPipelines.containsValue(var1) || SodiumTerrainPipelines.isReplacement(var1))) {
            var0.setUniform("LarpMaterial", materialBuffer.slice());
            if (var1.getShaderDefines().flags().contains("LARP_WATER") && sceneCapture.colorView != null) {
               RenderPipelineCompat.setSamplerUniform(
                  var0, "SceneColor", sceneCapture.colorView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
               );
               RenderPipelineCompat.setSamplerUniform(
                  var0, "SceneDepth", sceneCapture.depthView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST)
               );
            }
         }
      }
   }

   private static void initialize(LevelRenderState var0) {
      WavingFoliageModule var1 = WavingFoliageModule.getInstance();
      RealisticWaterModule var2 = RealisticWaterModule.getInstance();
      WetGroundModule var3 = WetGroundModule.getInstance();
      SnowGroundModule var4 = SnowGroundModule.getQualityLevel();
      boolean var5 = isModuleActive(var4);
      boolean var6 = isModuleActive(var1);
      boolean var7 = isModuleActive(var2);
      boolean var8 = isModuleActive(var3);
      double var9 = TickTimer.getTime();
      foliageTime = foliageTime + Math.clamp(var9 - lastTime, 0.0, 0.1) * (var6 ? var1.speed.getValue() : 1.0);
      lastTime = var9;
      float var11 = Math.clamp(var0.weatherRenderState.intensity, 0.0F, 1.0F);
      float var12 = var8 ? var3.puddles.getValue().floatValue() / 100.0F : 0.0F;
      float var13 = var8 ? var3.shine.getValue().floatValue() / 100.0F : 0.0F;
      float var14 = var8 ? var3.wetness.getValue().floatValue() / 100.0F : 0.0F;
      if (var8 && var3.wetterInRain.getValue()) {
         var12 = Math.min(1.0F, var12 + var11 * 0.35F);
         var13 = Math.min(1.0F, var13 + var11 * 0.25F);
         var14 = Math.min(1.0F, var14 + var11 * 0.25F);
      }

      CameraRenderState var15 = var0.cameraRenderState;
      Matrix4f var16 = new Matrix4f(var15.projectionMatrix).mul(var15.viewRotationMatrix);
      Matrix4f var17 = new Matrix4f(var16).invert();
      float var18 = var0.skyRenderState.sunAngle;
      float var19 = -((float)Math.sin(var18));
      float var20 = (float)Math.cos(var18);
      float var21 = Math.clamp(var20 * 1.8F + 0.15F, 0.0F, 1.0F) * (1.0F - var11 * 0.9F);
      Vector3fc var22 = var0.skyRenderState.skyColor;
      int var23 = var22 == null ? 0 : ARGB.colorFromFloat(1.0F, var22.x(), var22.y(), var22.z());
      MemoryStack var24 = MemoryStack.stackPush();

      try {
         ByteBuffer var25 = var24.malloc(1088);
         isHookDisabled(
            var25,
            (float)foliageTime,
            var6 ? var1.strength.getValue().floatValue() : 0.0F,
            var6 ? var1.gusts.getValue().floatValue() / 100.0F : 0.0F,
            (float)var9
         );
         isHookDisabled(
            var25,
            var7 ? var2.intensity.getValue().floatValue() / 100.0F : 0.0F,
            var7 ? var2.waves.getValue().floatValue() / 100.0F : 0.0F,
            var7 && var2.worldReflections.getValue() ? 1.0F : 0.0F,
            var7 ? 1.0F : 0.0F
         );
         isHookDisabled(
            var25,
            var7 && var2.foam.getValue() ? 1.0F : 0.0F,
            var7 && var2.caustics.getValue() ? 1.0F : 0.0F,
            var7 && var2.underwater.getValue() ? 1.0F : 0.0F,
            RenderSystem.getDevice().getDeviceInfo().isZZeroToOne() ? 1.0F : 0.0F
         );
         isHookDisabled(var25, var12, var13, var8 && var3.raindrops.getValue() ? 1.0F + var11 : 0.0F, var8 && var3.style.isMode("Flooded") ? 1.0F : 0.0F);
         isHookDisabled(
            var25,
            var8 ? 1.0F : 0.0F,
            var8 && var3.worldReflections.getValue() ? 1.0F : 0.0F,
            sceneCaptured ? 1.0F : 0.0F,
            var15.fogType == FogType.WATER ? 1.0F : 0.0F
         );
         isHookDisabled(var25, var19, var20, 0.0F, var21);
         isHookDisabled(var25, (var23 >> 16 & 0xFF) / 255.0F, (var23 >> 8 & 0xFF) / 255.0F, (var23 & 0xFF) / 255.0F, var11);
         isHookDisabled(var25, (float)(var15.pos.x % 8192.0), (float)var15.pos.y, (float)(var15.pos.z % 8192.0), var14);
         isHookDisabled(
            var25,
            var5 ? var4.coverage.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.depth.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.sparkle.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.getQualityLevel() : 0.0F
         );
         var16.get(var25);
         var25.position(var25.position() + 64);
         var17.get(var25);
         var25.position(var25.position() + 64);
         if (var4 != null) {
            var4.writeTrail(var25, var15.pos, var9, var5);
         } else {
            for (int var26 = 0; var26 < 100; var26++) {
               var25.putLong(0L);
            }
         }

         isHookDisabled(var25, (float)var15.pos.x, (float)var15.pos.y, (float)var15.pos.z, 0.0F);
         var25.flip();
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(materialBuffer.slice(), var25);
      } catch (Throwable var28) {
         if (var24 != null) {
            try {
               var24.close();
            } catch (Throwable var27) {
               var28.addSuppressed(var27);
            }
         }

         throw var28;
      }

      if (var24 != null) {
         var24.close();
      }
   }

   private static void captureWaterScene() {
      if (isMaterialPassActive() && isModuleActive(RealisticWaterModule.getInstance())) {
         try {
            sceneCapture.capture(Minecraft.getInstance().gameRenderer.mainRenderTarget());
            sceneCaptured = (boolean)1;
            initialize(levelState);
         } catch (RuntimeException var1) {
            bindMaterial("capture-water-scene", var1);
         }
      }
   }

   private static void renderWetReflection() {
      WetGroundModule var0 = WetGroundModule.getInstance();
      if (isMaterialPassReady() && isModuleActive(var0) && var0.worldReflections.getValue()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            sceneCapture.capture(var1);
            sceneCaptured = (boolean)1;
            initialize(levelState);
            drawFullscreenPass(wetReflectionPipeline, var1);
         } catch (RuntimeException var2) {
            bindMaterial("wet-reflection", var2);
         }
      }
   }

   private static void isHookDisabled() {
      RealisticWaterModule var0 = RealisticWaterModule.getInstance();
      if (isMaterialPassActive() && isModuleActive(var0) && var0.underwater.getValue() && levelState.cameraRenderState.fogType == FogType.WATER) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            sceneCapture.capture(var1);
            sceneCaptured = (boolean)1;
            initialize(levelState);
            drawFullscreenPass(underwaterPipeline, var1);
         } catch (RuntimeException var2) {
            bindMaterial("underwater", var2);
         }
      }
   }

   private static void drawFullscreenPass(RenderPipeline var0, RenderTarget var1) {
      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var3 = var2.createRenderPass(() -> "Larp " + var0.getLocation(), var1.getColorTextureView(), Optional.empty());

      try {
         RenderPipelineCompat.setPipeline(var3, var0);
         var3.setUniform("LarpMaterial", materialBuffer.slice());
         RenderPipelineCompat.setSamplerUniform(var3, "SceneColor", sceneCapture.colorView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         RenderPipelineCompat.setSamplerUniform(var3, "SceneDepth", sceneCapture.depthView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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
   }

   static GpuBuffer releaseResources() {
      return materialBuffer;
   }

   static GpuTextureView getSceneColorView() {
      return sceneCapture.colorView;
   }

   static GpuTextureView bindMaterial() {
      return sceneCapture.depthView;
   }

   static LevelRenderState getLevelState() {
      return levelState;
   }

   static void captureScene() {
      sceneCapture.capture(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      sceneCaptured = (boolean)1;
      initialize(levelState);
   }

   private static RenderPipeline isNativeHookUnavailable(String var0) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(identifier("pipeline/temple/" + var0))
         .withVertexShader(identifier("post/fullscreen"))
         .withFragmentShader(identifier("temple/" + var0))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
               .withUniform("SceneColor", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
               .withUniform("SceneDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withCull(false)
         .build();
   }

   private static void isHookDisabled(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   private static Identifier identifier(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static void bindMaterial(String var0, RuntimeException var1) {
      failed = (boolean)1;
      releaseResources();
      refreshChunks();
      LarpLog.error("TerrainMaterials", "stage=" + var0 + "; restored vanilla terrain; retry after resource reload", var1);
   }

   public static void releaseResources() {
      sceneCaptured = (boolean)0;
      prepared = (boolean)0;
      if (materialBuffer != null) {
         materialBuffer.close();
         materialBuffer = null;
      }

      sceneCapture.release();
      DropletSurfaceRenderer.releaseResources();
   }

   public static void reset() {
      RealisticWater.reset();
      releaseResources();
      levelState = null;
      lastLevel = null;
      foliageTime = 0.0;
      lastTime = TickTimer.getTime();
   }

   private TerrainMaterials() {
   }

   private static final class SceneCapture {
      private GpuTexture colorTexture;
      private GpuTexture depthTexture;
      private GpuTextureView colorView;
      private GpuTextureView depthView;

      private void capture(RenderTarget var1) {
         if (var1.getDepthTexture() == null) {
            throw new IllegalStateException("Terrain target has no depth texture");
         }

         if (this.colorTexture == null
            || this.colorTexture.getWidth(0) != var1.width
            || this.colorTexture.getHeight(0) != var1.height
            || this.colorTexture.getFormat() != var1.getColorTexture().getFormat()
            || this.depthTexture.getFormat() != var1.getDepthTexture().getFormat()) {
            this.release();
            GpuDevice var2 = RenderSystem.getDevice();
            byte var3 = 5;
            this.colorTexture = var2.createTexture("Larp material scene", var3, var1.getColorTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.depthTexture = var2.createTexture("Larp material depth", var3, var1.getDepthTexture().getFormat(), var1.width, var1.height, 1, 1);
            this.colorView = var2.createTextureView(this.colorTexture);
            this.depthView = var2.createTextureView(this.depthTexture);
         }

         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.copyTextureToTexture(var1.getColorTexture(), this.colorTexture, 0, 0, 0, 0, 0, var1.width, var1.height);
         var4.copyTextureToTexture(var1.getDepthTexture(), this.depthTexture, 0, 0, 0, 0, 0, var1.width, var1.height);
      }

      private void release() {
         if (this.colorView != null) {
            this.colorView.close();
            this.colorView = null;
         }

         if (this.depthView != null) {
            this.depthView.close();
            this.depthView = null;
         }

         if (this.colorTexture != null) {
            this.colorTexture.close();
            this.colorTexture = null;
         }

         if (this.depthTexture != null) {
            this.depthTexture.close();
            this.depthTexture = null;
         }
      }
   }
}
