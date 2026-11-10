package com.larpclient;

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

public final class p {
   static final Identifier W = Identifier.fromNamespaceAndPath("larp-client", "temple/sodium_terrain");
   private static final Pattern u = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void\\s*)?\\)");
   private static final Pattern R = Pattern.compile("fragColor\\s*=\\s*_linearFog\\(\\s*color\\s*,");
   private static final List<String> r = List.of("_vert_position", "u_RegionOffset", "_get_draw_translation", "_draw_id", "_vert_tex_light_coord");
   private static final List<String> T = List.of("v_Color", "v_FragDistance", "u_FogColor", "u_RenderFog", "fragColor");
   private static final int B = 600;
   private static final IdentityHashMap<RenderPipeline, p.f> g = new IdentityHashMap<>();
   private static final List<RenderPipeline> q = new ArrayList<>();
   private static boolean K;
   private static int E;

   static boolean T() {
      return !FabricLoader.getInstance().isModLoaded("iris") && S().startsWith("0.9.");
   }

   static String S() {
      return FabricLoader.getInstance().getModContainer("sodium").map(var0 -> var0.getMetadata().getVersion().getFriendlyString()).orElse("");
   }

   static void u() {
      K = (boolean)1;
   }

   static boolean h() {
      return !K && E >= 600;
   }

   static void h() {
      if (!K && ++E == 600) {
         _q.R("TerrainMaterials", "Sodium {} drew no terrain through the Wet Ground hook in {} frames; keeping Sodium output", S(), 600);
      }
   }

   static RenderPipeline n(RenderPipeline var0) {
      if (o(var0)) {
         return var0;
      }

      p.f var1 = g.get(var0);
      if (var1 != null) {
         return var1.pipeline();
      }

      if (!q.contains(var0)) {
         q.add(var0);
      }

      return var0;
   }

   static boolean s(RenderPipeline var0) {
      for (p.f var2 : g.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   static void N() {
      for (RenderPipeline var1 : q) {
         p.f var2 = V(var1);
         M(var2);
         g.put(var1, var2);
         _q.z("TerrainMaterials", "Wet Ground draws Sodium pass {} through {}", var1.getLocation(), var2.pipeline().getLocation());
      }

      q.clear();

      for (p.f var4 : g.values()) {
         M(var4);
      }
   }

   static void l() {
      g.clear();
      q.clear();
   }

   public static String D(String var0, String var1, ShaderType var2) {
      return W(var0, var1, var2, "larp_wet");
   }

   public static String W(String var0, String var1, ShaderType var2, String var3) {
      for (String var5 : var2 == ShaderType.VERTEX ? r : T) {
         if (!var0.contains(var5)) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader no longer provides " + var5);
         }
      }

      Matcher var9 = u.matcher(var0);
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
               Matcher var8 = R.matcher(var7);
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

   private static p.f V(RenderPipeline var0) {
      ShaderSource var1 = p7.o();
      String var2 = D(g(var1.getShader(p7.X(var0), ShaderType.VERTEX), p7.X(var0)), g(var1.getShader(W, ShaderType.VERTEX), W), ShaderType.VERTEX);
      String var3 = D(g(var1.getShader(p7.a(var0), ShaderType.FRAGMENT), p7.a(var0)), g(var1.getShader(W, ShaderType.FRAGMENT), W), ShaderType.FRAGMENT);
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "wet_ground/" + var0.getLocation().getPath()))
         .withVertexShader(W)
         .withFragmentShader(W)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));

      for (BindGroupLayout var6 : var0.getBindGroupLayouts()) {
         var4.withBindGroupLayout(var6);
      }

      var4.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).build());
      List var8 = p7.i(var0);

      for (int var9 = 0; var9 < var8.size(); var9++) {
         var4.withColorTargetState(var9, (ColorTargetState)var8.get(var9));
      }

      List var10 = p7.A(var0);

      for (int var7 = 0; var7 < var10.size(); var7++) {
         var4.withVertexBinding(var7, (VertexFormat)var10.get(var7));
      }

      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var11 = p7.w((var3x, var4x) -> !var3x.equals(W) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new p.f(var4.build(), var11);
   }

   static String g(String var0, Identifier var1) {
      if (var0 == null) {
         throw new IllegalStateException("Shader source " + var1 + " is not loaded");
      } else {
         return var0;
      }
   }

   private static void M(p.f var0) {
      L(var0.pipeline(), var0.source());
   }

   static void L(RenderPipeline var0, ShaderSource var1) {
      if (!p7.L(var0, var1)) {
         throw new IllegalStateException("Pipeline " + var0.getLocation() + " failed; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages());
      }
   }

   static boolean o(RenderPipeline var0) {
      for (ColorTargetState var2 : var0.getColorTargetStates()) {
         if (var2 != null && var2.blendFunction().isPresent()) {
            return true;
         }
      }

      return false;
   }

   private p() {
   }

   private record f(RenderPipeline pipeline, ShaderSource source) {
   }
}
