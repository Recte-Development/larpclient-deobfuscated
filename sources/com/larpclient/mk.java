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

public final class mk {
   static final Identifier T = Identifier.fromNamespaceAndPath("larp-client", "temple/sodium_terrain");
   private static final Pattern w = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void\\s*)?\\)");
   private static final Pattern G = Pattern.compile("fragColor\\s*=\\s*_linearFog\\(\\s*color\\s*,");
   private static final List<String> z = List.of("_vert_position", "u_RegionOffset", "_get_draw_translation", "_draw_id", "_vert_tex_light_coord");
   private static final List<String> q = List.of("v_Color", "v_FragDistance", "u_FogColor", "u_RenderFog", "fragColor");
   private static final int r = 600;
   private static final IdentityHashMap<RenderPipeline, mk.k> W = new IdentityHashMap<>();
   private static final List<RenderPipeline> a = new ArrayList<>();
   private static boolean t;
   private static int c;

   static boolean o() {
      return !FabricLoader.getInstance().isModLoaded("iris") && Z().startsWith("0.9.");
   }

   static String Z() {
      return FabricLoader.getInstance().getModContainer("sodium").map(var0 -> var0.getMetadata().getVersion().getFriendlyString()).orElse("");
   }

   static void t() {
      t = (boolean)1;
   }

   static boolean v() {
      return !t && c >= 600;
   }

   static void V() {
      if (!t && ++c == 600) {
         r4.m("TerrainMaterials", "Sodium {} drew no terrain through the Wet Ground hook in {} frames; keeping Sodium output", Z(), 600);
      }
   }

   static RenderPipeline h(RenderPipeline var0) {
      if (v(var0)) {
         return var0;
      }

      mk.k var1 = W.get(var0);
      if (var1 != null) {
         return var1.pipeline();
      }

      if (!a.contains(var0)) {
         a.add(var0);
      }

      return var0;
   }

   static boolean U(RenderPipeline var0) {
      for (mk.k var2 : W.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   static void s() {
      for (RenderPipeline var1 : a) {
         mk.k var2 = y(var1);
         p(var2);
         W.put(var1, var2);
         r4.V("TerrainMaterials", "Wet Ground draws Sodium pass {} through {}", var1.getLocation(), var2.pipeline().getLocation());
      }

      a.clear();

      for (mk.k var4 : W.values()) {
         p(var4);
      }
   }

   static void b() {
      W.clear();
      a.clear();
   }

   public static String G(String var0, String var1, ShaderType var2) {
      return V(var0, var1, var2, "larp_wet");
   }

   public static String V(String var0, String var1, ShaderType var2, String var3) {
      for (String var5 : var2 == ShaderType.VERTEX ? z : q) {
         if (!var0.contains(var5)) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader no longer provides " + var5);
         }
      }

      Matcher var9 = w.matcher(var0);
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
               Matcher var8 = G.matcher(var7);
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

   private static mk.k y(RenderPipeline var0) {
      ShaderSource var1 = of.v();
      String var2 = G(y(var1.getShader(of.C(var0), ShaderType.VERTEX), of.C(var0)), y(var1.getShader(T, ShaderType.VERTEX), T), ShaderType.VERTEX);
      String var3 = G(y(var1.getShader(of.b(var0), ShaderType.FRAGMENT), of.b(var0)), y(var1.getShader(T, ShaderType.FRAGMENT), T), ShaderType.FRAGMENT);
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "wet_ground/" + var0.getLocation().getPath()))
         .withVertexShader(T)
         .withFragmentShader(T)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));

      for (BindGroupLayout var6 : var0.getBindGroupLayouts()) {
         var4.withBindGroupLayout(var6);
      }

      var4.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).build());
      List var8 = of.J(var0);

      for (int var9 = 0; var9 < var8.size(); var9++) {
         var4.withColorTargetState(var9, (ColorTargetState)var8.get(var9));
      }

      List var10 = of.m(var0);

      for (int var7 = 0; var7 < var10.size(); var7++) {
         var4.withVertexBinding(var7, (VertexFormat)var10.get(var7));
      }

      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var11 = of.A((var3x, var4x) -> !var3x.equals(T) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new mk.k(var4.build(), var11);
   }

   static String y(String var0, Identifier var1) {
      if (var0 == null) {
         throw new IllegalStateException("Shader source " + var1 + " is not loaded");
      } else {
         return var0;
      }
   }

   private static void p(mk.k var0) {
      t(var0.pipeline(), var0.source());
   }

   static void t(RenderPipeline var0, ShaderSource var1) {
      if (!of.k(var0, var1)) {
         throw new IllegalStateException("Pipeline " + var0.getLocation() + " failed; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages());
      }
   }

   static boolean v(RenderPipeline var0) {
      for (ColorTargetState var2 : var0.getColorTargetStates()) {
         if (var2 != null && var2.blendFunction().isPresent()) {
            return true;
         }
      }

      return false;
   }

   private mk() {
   }

   private record k(RenderPipeline pipeline, ShaderSource source) {
   }
}
