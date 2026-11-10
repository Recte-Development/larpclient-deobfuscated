package com.larpclient;

import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;

public final class k_ {
   static final Identifier q = Identifier.fromNamespaceAndPath("larp-client", "temple/sodium_terrain");
   private static final Pattern n = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void\\s*)?\\)");
   private static final Pattern C = Pattern.compile("fragColor\\s*=\\s*_linearFog\\(\\s*color\\s*,");
   private static final List<String> j = List.of("_vert_position", "u_RegionOffset", "_get_draw_translation", "_draw_id", "_vert_tex_light_coord");
   private static final List<String> x = List.of("v_Color", "v_FragDistance", "u_FogColor", "u_RenderFog", "fragColor");
   private static final int M = 600;
   private static final IdentityHashMap<RenderPipeline, k_.i> Q = new IdentityHashMap<>();
   private static final List<RenderPipeline> B = new ArrayList<>();
   private static boolean o;
   private static int L;

   static boolean r() {
      return !FabricLoader.getInstance().isModLoaded("iris") && E().startsWith("0.9.");
   }

   static String E() {
      return FabricLoader.getInstance().getModContainer("sodium").map(var0 -> var0.getMetadata().getVersion().getFriendlyString()).orElse("");
   }

   static void u() {
      o = (boolean)1;
   }

   static boolean C() {
      return !o && L >= 600;
   }

   static void k() {
      if (!o && ++L == 600) {
         h.g("TerrainMaterials", "Sodium {} drew no terrain through the Wet Ground hook in {} frames; keeping Sodium output", E(), 600);
      }
   }

   static RenderPipeline J(RenderPipeline var0) {
      if (u(var0)) {
         return var0;
      }

      k_.i var1 = Q.get(var0);
      if (var1 != null) {
         return var1.pipeline();
      }

      if (!B.contains(var0)) {
         B.add(var0);
      }

      return var0;
   }

   static boolean k(RenderPipeline var0) {
      for (k_.i var2 : Q.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   static void g() {
      for (RenderPipeline var1 : B) {
         k_.i var2 = f(var1);
         K(var2);
         Q.put(var1, var2);
         h.A("TerrainMaterials", "Wet Ground draws Sodium pass {} through {}", var1.getLocation(), var2.pipeline().getLocation());
      }

      B.clear();

      for (k_.i var4 : Q.values()) {
         K(var4);
      }
   }

   static void r() {
      Q.clear();
      B.clear();
   }

   public static String p(String var0, String var1, ShaderType var2) {
      return L(var0, var1, var2, "larp_wet");
   }

   public static String L(String var0, String var1, ShaderType var2, String var3) {
      for (String var5 : var2 == ShaderType.VERTEX ? j : x) {
         if (!var0.contains(var5)) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader no longer provides " + var5);
         }
      }

      Matcher var9 = n.matcher(var0);
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
               Matcher var8 = C.matcher(var7);
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

   private static k_.i f(RenderPipeline var0) {
      ShaderManager var1 = mx.Q();
      String var2 = p(O(var1.getShader(mx.R(var0), ShaderType.VERTEX), mx.R(var0)), O(var1.getShader(q, ShaderType.VERTEX), q), ShaderType.VERTEX);
      String var3 = p(O(var1.getShader(mx.K(var0), ShaderType.FRAGMENT), mx.K(var0)), O(var1.getShader(q, ShaderType.FRAGMENT), q), ShaderType.FRAGMENT);
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "wet_ground/" + var0.getLocation().getPath()))
         .withVertexShader(q)
         .withFragmentShader(q)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));

      for (BindGroupLayout var6 : var0.getBindGroupLayouts()) {
         var4.withBindGroupLayout(var6);
      }

      var4.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).build());
      List var8 = mx.G(var0);

      for (int var9 = 0; var9 < var8.size(); var9++) {
         var4.withColorTargetState(var9, (ColorTargetState)var8.get(var9));
      }

      List var10 = mx.i(var0);

      for (int var7 = 0; var7 < var10.size(); var7++) {
         var4.withVertexBinding(var7, (VertexFormat)var10.get(var7));
      }

      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var11 = mx.V((var3x, var4x) -> !var3x.equals(q) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new k_.i(var4.build(), var11);
   }

   static String O(String var0, Identifier var1) {
      if (var0 == null) {
         throw new IllegalStateException("Shader source " + var1 + " is not loaded");
      } else {
         return var0;
      }
   }

   private static void K(k_.i var0) {
      n(var0.pipeline(), var0.source());
   }

   static void n(RenderPipeline var0, ShaderSource var1) {
      if (!mx.p(var0, var1)) {
         throw new IllegalStateException("Pipeline " + var0.getLocation() + " failed; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages());
      }
   }

   static boolean u(RenderPipeline var0) {
      for (ColorTargetState var4 : var0.getColorTargetStates()) {
         if (var4 != null && var4.blendFunction().isPresent()) {
            return true;
         }
      }

      return false;
   }

   private k_() {
   }

   private record i(RenderPipeline pipeline, ShaderSource source) {
   }
}
