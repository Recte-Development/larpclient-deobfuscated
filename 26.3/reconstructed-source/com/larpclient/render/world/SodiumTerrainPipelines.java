package com.larpclient.render.world;

import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

public final class SodiumTerrainPipelines {
   static final Identifier shaderId = Identifier.fromNamespaceAndPath("larp-client", "temple/sodium_terrain");
   private static final Pattern mainPattern = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void\\s*)?\\)");
   private static final Pattern fogCallPattern = Pattern.compile("fragColor\\s*=\\s*_linearFog\\(\\s*color\\s*,");
   private static final List<String> requiredVertexTokens = List.of(
      "_vert_position", "u_RegionOffset", "_get_draw_translation", "_draw_id", "_vert_tex_light_coord"
   );
   private static final List<String> requiredFragmentTokens = List.of("v_Color", "v_FragDistance", "u_FogColor", "u_RenderFog", "fragColor");
   private static final int frameLimit = 600;
   private static final IdentityHashMap<RenderPipeline, SodiumTerrainPipelines.PipelineReplacement> replacements = new IdentityHashMap<>();
   private static final List<RenderPipeline> pendingPipelines = new ArrayList<>();
   private static boolean terrainDrawn;
   private static int missedFrames;

   static boolean isSupported() {
      return !FabricLoader.getInstance().isModLoaded("iris") && sodiumVersion().startsWith("0.9.");
   }

   static String sodiumVersion() {
      return FabricLoader.getInstance().getModContainer("sodium").map(var0 -> var0.getMetadata().getVersion().getFriendlyString()).orElse("");
   }

   static void markTerrainDrawn() {
      terrainDrawn = (boolean)1;
   }

   static boolean shouldFallBack() {
      return !terrainDrawn && missedFrames >= 600;
   }

   static void shouldFallBack() {
      if (!terrainDrawn && ++missedFrames == 600) {
         LarpLog.warn("TerrainMaterials", "Sodium {} drew no terrain through the Wet Ground hook in {} frames; keeping Sodium output", sodiumVersion(), 600);
      }
   }

   static RenderPipeline resolvePipeline(RenderPipeline var0) {
      if (isBlended(var0)) {
         return var0;
      }

      SodiumTerrainPipelines.PipelineReplacement var1 = replacements.get(var0);
      if (var1 != null) {
         return var1.pipeline();
      }

      if (!pendingPipelines.contains(var0)) {
         pendingPipelines.add(var0);
      }

      return var0;
   }

   static boolean isReplacement(RenderPipeline var0) {
      for (SodiumTerrainPipelines.PipelineReplacement var2 : replacements.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   static void buildPending() {
      for (RenderPipeline var1 : pendingPipelines) {
         SodiumTerrainPipelines.PipelineReplacement var2 = buildReplacement(var1);
         compileReplacement(var2);
         replacements.put(var1, var2);
         LarpLog.info("TerrainMaterials", "Wet Ground draws Sodium pass {} through {}", var1.getLocation(), var2.pipeline().getLocation());
      }

      pendingPipelines.clear();

      for (SodiumTerrainPipelines.PipelineReplacement var4 : replacements.values()) {
         compileReplacement(var4);
      }
   }

   static void clear() {
      replacements.clear();
      pendingPipelines.clear();
   }

   public static String patchShader(String var0, String var1, ShaderType var2) {
      return patchShaderWithFunction(var0, var1, var2, "larp_wet");
   }

   public static String patchShaderWithFunction(String var0, String var1, ShaderType var2, String var3) {
      for (String var5 : var2 == ShaderType.VERTEX ? requiredVertexTokens : requiredFragmentTokens) {
         if (!var0.contains(var5)) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader no longer provides " + var5);
         }
      }

      Matcher var9 = mainPattern.matcher(var0);
      if (!var9.find()) {
         throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader has no main()");
      } else {
         int var10 = var9.start();
         int var6 = var9.end();
         if (var9.find()) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader has more than one main()");
         } else {
            String var7 = var0.substring(0, var10) + var1 + "\nvoid larp_sodium_main()" + var0.substring(var6);
            if (var2 == ShaderType.VERTEX) {
               return var7;
            } else {
               Matcher var8 = fogCallPattern.matcher(var7);
               if (!var8.find()) {
                  throw new IllegalStateException("Sodium fragment chunk shader no longer fogs color through _linearFog");
               } else {
                  var10 = var8.start();
                  var6 = var8.end();
                  if (var8.find()) {
                     throw new IllegalStateException("Sodium fragment chunk shader fogs color more than once");
                  } else {
                     return var7.substring(0, var10) + "fragColor = _linearFog(" + var3 + "(color)," + var7.substring(var6);
                  }
               }
            }
         }
      }
   }

   private static SodiumTerrainPipelines.PipelineReplacement buildReplacement(RenderPipeline var0) {
      ShaderSource var1 = RenderPipelineCompat.getBaseShaderSource();
      String var2 = patchShader(
         requireSource(var1.getShader(RenderPipelineCompat.getVertexShaderId(var0), ShaderType.VERTEX), RenderPipelineCompat.getVertexShaderId(var0)),
         requireSource(var1.getShader(shaderId, ShaderType.VERTEX), shaderId),
         ShaderType.VERTEX
      );
      String var3 = patchShader(
         requireSource(var1.getShader(RenderPipelineCompat.getFragmentShaderId(var0), ShaderType.FRAGMENT), RenderPipelineCompat.getFragmentShaderId(var0)),
         requireSource(var1.getShader(shaderId, ShaderType.FRAGMENT), shaderId),
         ShaderType.FRAGMENT
      );
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "wet_ground/" + var0.getLocation().getPath()))
         .withVertexShader(shaderId)
         .withFragmentShader(shaderId)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));

      for (BindGroupLayout var6 : var0.getBindGroupLayouts()) {
         var4.withBindGroupLayout(var6);
      }

      var4.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).build());
      List var8 = RenderPipelineCompat.getColorTargetStates(var0);

      for (int var9 = 0; var9 < var8.size(); var9++) {
         var4.withColorTargetState(var9, (ColorTargetState)var8.get(var9));
      }

      List var10 = RenderPipelineCompat.getVertexFormatBindings(var0);

      for (int var7 = 0; var7 < var10.size(); var7++) {
         var4.withVertexBinding(var7, (VertexFormat)var10.get(var7));
      }

      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var11 = RenderPipelineCompat.createShaderSource(
         (var3x, var4x) -> !var3x.equals(shaderId) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3)
      );
      return new SodiumTerrainPipelines.PipelineReplacement(var4.build(), var11);
   }

   static String requireSource(String var0, Identifier var1) {
      if (var0 == null) {
         throw new IllegalStateException("Shader source " + var1 + " is not loaded");
      } else {
         return var0;
      }
   }

   private static void compileReplacement(SodiumTerrainPipelines.PipelineReplacement var0) {
      compilePipeline(var0.pipeline(), var0.source());
   }

   static void compilePipeline(RenderPipeline var0, ShaderSource var1) {
      if (!RenderPipelineCompat.compilePipeline(var0, var1)) {
         throw new IllegalStateException("Pipeline " + var0.getLocation() + " failed; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages());
      }
   }

   static boolean isBlended(RenderPipeline var0) {
      for (ColorTargetState var2 : var0.getColorTargetStates()) {
         if (var2 != null && var2.blendFunction().isPresent()) {
            return true;
         }
      }

      return false;
   }

   private SodiumTerrainPipelines() {
   }

   private record PipelineReplacement(RenderPipeline pipeline, ShaderSource source) {
   }
}
