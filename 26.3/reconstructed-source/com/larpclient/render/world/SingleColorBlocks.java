package com.larpclient.render.world;

import com.larpclient.module.misc.PerformanceModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.StartMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public final class SingleColorBlocks {
   private static final Identifier sHADER_ID = larpId("performance/single_color");
   static final Identifier sODIUM_SHADER_ID = larpId("performance/sodium_single_color");
   private static final String uNIFORM_NAME = "LarpSingleColor";
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> vanillaPipelines = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, SingleColorBlocks.SodiumPipeline> sodiumPipelines = new IdentityHashMap<>();
   private static final List<RenderPipeline> pendingSodiumPipelines = new ArrayList<>();
   private static GpuBuffer colorBuffer;
   private static int lastColor;
   private static int resourceGeneration = -1;
   private static boolean initialized;
   private static boolean sodiumActive;
   private static boolean unsupported;
   private static boolean failed;
   private static boolean ready;

   static void init() {
      if (!initialized) {
         initialized = (boolean)1;
         FabricLoader var0 = FabricLoader.getInstance();
         unsupported = (boolean)(!var0.isModLoaded("iris") && (!var0.isModLoaded("sodium") || SodiumTerrainPipelines.isSupported()) ? 0 : 1);
         sodiumActive = (boolean)(var0.isModLoaded("sodium") && !unsupported ? 1 : 0);
         if (unsupported) {
            LarpLog.info("SingleColor", "Iris or an unsupported Sodium is loaded; Single Color Blocks keeps native terrain");
         }

         LevelRenderEvents.START_MAIN.register((StartMain)var0x -> onLevelRenderStart());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0x -> release());
      }
   }

   public static boolean isAvailable() {
      return !unsupported && !failed;
   }

   static boolean isActive() {
      return ready && !failed && PerformanceModule.isSingleColorBlocks();
   }

   private static void onLevelRenderStart() {
      if (resourceGeneration != PostProcessing.reloadCount) {
         resourceGeneration = PostProcessing.reloadCount;
         vanillaPipelines.clear();
         sodiumPipelines.clear();
         pendingSodiumPipelines.clear();
         if (failed) {
            LarpLog.info("SingleColor", "Resources reloaded; retrying Single Color Blocks");
         }

         failed = (boolean)0;
      }

      if (!unsupported && !failed && PerformanceModule.isSingleColorBlocks()) {
         String var0 = "uniform";

         try {
            if (colorBuffer == null) {
               colorBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp single color blocks", 136, 16L);
               lastColor = ~PerformanceModule.getBlockColor();
               LarpLog.info(
                  "SingleColor", "Single Color Blocks active on {} terrain", sodiumActive ? "Sodium " + SodiumTerrainPipelines.sodiumVersion() : "vanilla"
               );
            }

            int var1 = PerformanceModule.getBlockColor();
            if (var1 != lastColor) {
               MemoryStack var2 = MemoryStack.stackPush();

               try {
                  ByteBuffer var3 = var2.malloc(16);
                  var3.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(1.0F).flip();
                  RenderSystem.getDevice().createCommandEncoder().writeToBuffer(colorBuffer.slice(), var3);
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

               lastColor = var1;
            }

            if (!sodiumActive) {
               var0 = "vanilla-compile";
               createVanillaPipeline(RenderPipelines.SOLID_TERRAIN);
               createVanillaPipeline(RenderPipelines.CUTOUT_TERRAIN);
            } else {
               var0 = "sodium-compile";

               for (RenderPipeline var10 : pendingSodiumPipelines) {
                  SingleColorBlocks.SodiumPipeline var4 = createSodiumPipeline(var10);
                  SodiumTerrainPipelines.compilePipeline(var4.pipeline(), var4.source());
                  sodiumPipelines.put(var10, var4);
                  LarpLog.info("SingleColor", "Sodium pass {} draws through {}", var10.getLocation(), var4.pipeline().getLocation());
               }

               pendingSodiumPipelines.clear();

               for (SingleColorBlocks.SodiumPipeline var11 : sodiumPipelines.values()) {
                  SodiumTerrainPipelines.compilePipeline(var11.pipeline(), var11.source());
               }
            }

            ready = (boolean)1;
         } catch (RuntimeException var7) {
            onLevelRenderStart(var0, var7);
         }
      } else {
         if (colorBuffer != null) {
            release();
         }
      }
   }

   static RenderPipeline getVanillaPipeline(RenderPipeline var0) {
      return !sodiumActive && isActive() ? vanillaPipelines.get(var0) : null;
   }

   static RenderPipeline init(RenderPipeline var0) {
      if (sodiumActive && !failed && !unsupported && PerformanceModule.isSingleColorBlocks() && !SodiumTerrainPipelines.isBlended(var0)) {
         SingleColorBlocks.SodiumPipeline var1 = sodiumPipelines.get(var0);
         if (var1 != null && ready) {
            return var1.pipeline();
         }

         if (!pendingSodiumPipelines.contains(var0)) {
            pendingSodiumPipelines.add(var0);
         }

         return null;
      } else {
         return null;
      }
   }

   static boolean bindUniform(RenderPass var0, RenderPipeline var1) {
      if (colorBuffer != null && isReplacementPipeline(var1)) {
         var0.setUniform("LarpSingleColor", colorBuffer.slice());
         return true;
      } else {
         return false;
      }
   }

   private static boolean isReplacementPipeline(RenderPipeline var0) {
      if (vanillaPipelines.containsValue(var0)) {
         return true;
      }

      for (SingleColorBlocks.SodiumPipeline var2 : sodiumPipelines.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   private static RenderPipeline createVanillaPipeline(RenderPipeline var0) {
      return vanillaPipelines.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = RenderPipeline.builder(new Snippet[0])
               .withLocation(larpId("pipeline/performance/single_color/" + var0x.getLocation().getPath()))
               .withVertexShader(sHADER_ID)
               .withFragmentShader(sHADER_ID)
               .withCull(var0x.isCull())
               .withPolygonMode(var0x.getPolygonMode())
               .withPrimitiveTopology(var0x.getPrimitiveTopology())
               .withDepthStencilState(Optional.ofNullable(var0x.getDepthStencilState()));
            getVanillaPipeline(var0x, var1);
            var0x.getShaderDefines().flags().forEach(var1::withShaderDefine);
            var0x.getShaderDefines().values().forEach((var1x, var2x) -> var1.withShaderDefine(var1x, Float.parseFloat(var2x)));
            RenderPipeline var2 = var1.build();
            ClientPipelines.validatePipeline(var2);
            return var2;
         }
      );
   }

   public static String patchSodiumVertexShader(String var0, String var1) {
      for (String var3 : List.of("_vert_color", "u_LightTex")) {
         if (!var0.contains(var3)) {
            throw new IllegalStateException("Sodium vertex chunk shader no longer provides " + var3);
         }
      }

      return SodiumTerrainPipelines.patchShaderWithFunction(var0, var1, ShaderType.VERTEX, "larp_single_block");
   }

   private static SingleColorBlocks.SodiumPipeline createSodiumPipeline(RenderPipeline var0) {
      ShaderSource var1 = RenderPipelineCompat.getBaseShaderSource();
      String var2 = patchSodiumVertexShader(
         SodiumTerrainPipelines.requireSource(
            var1.getShader(RenderPipelineCompat.getVertexShaderId(var0), ShaderType.VERTEX), RenderPipelineCompat.getVertexShaderId(var0)
         ),
         SodiumTerrainPipelines.requireSource(var1.getShader(sODIUM_SHADER_ID, ShaderType.VERTEX), sODIUM_SHADER_ID)
      );
      String var3 = SodiumTerrainPipelines.patchShaderWithFunction(
         SodiumTerrainPipelines.requireSource(
            var1.getShader(RenderPipelineCompat.getFragmentShaderId(var0), ShaderType.FRAGMENT), RenderPipelineCompat.getFragmentShaderId(var0)
         ),
         SodiumTerrainPipelines.requireSource(var1.getShader(sODIUM_SHADER_ID, ShaderType.FRAGMENT), sODIUM_SHADER_ID),
         ShaderType.FRAGMENT,
         "larp_single_block"
      );
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "single_color/" + var0.getLocation().getPath()))
         .withVertexShader(sODIUM_SHADER_ID)
         .withFragmentShader(sODIUM_SHADER_ID)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));
      getVanillaPipeline(var0, var4);
      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var5 = RenderPipelineCompat.createShaderSource(
         (var3x, var4x) -> !var3x.equals(sODIUM_SHADER_ID) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3)
      );
      return new SingleColorBlocks.SodiumPipeline(var4.build(), var5);
   }

   private static void getVanillaPipeline(RenderPipeline var0, Builder var1) {
      for (BindGroupLayout var3 : var0.getBindGroupLayouts()) {
         var1.withBindGroupLayout(var3);
      }

      var1.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpSingleColor", UniformType.UNIFORM_BUFFER).build());
      List var5 = RenderPipelineCompat.getColorTargetStates(var0);

      for (int var6 = 0; var6 < var5.size(); var6++) {
         var1.withColorTargetState(var6, (ColorTargetState)var5.get(var6));
      }

      List var7 = RenderPipelineCompat.getVertexFormatBindings(var0);

      for (int var4 = 0; var4 < var7.size(); var4++) {
         var1.withVertexBinding(var4, (VertexFormat)var7.get(var4));
      }
   }

   private static void onLevelRenderStart(String var0, RuntimeException var1) {
      failed = (boolean)1;
      ready = (boolean)0;
      LarpLog.error("SingleColor", "stage=" + var0 + "; keeping native terrain until the next resource reload", var1);
   }

   public static void release() {
      ready = (boolean)0;
      if (colorBuffer != null) {
         colorBuffer.close();
         colorBuffer = null;
         LarpLog.info("SingleColor", "Single Color Blocks released; native terrain restored");
      }
   }

   private static Identifier larpId(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private SingleColorBlocks() {
   }

   private record SodiumPipeline(RenderPipeline pipeline, ShaderSource source) {
   }
}
