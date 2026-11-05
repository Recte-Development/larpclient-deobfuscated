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

public final class w {
   static final Identifier z = Identifier.fromNamespaceAndPath("larp-client", "temple/sodium_terrain");
   private static final Pattern V = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void\\s*)?\\)");
   private static final Pattern F = Pattern.compile("fragColor\\s*=\\s*_linearFog\\(\\s*color\\s*,");
   private static final List<String> a = List.of("_vert_position", "u_RegionOffset", "_get_draw_translation", "_draw_id", "_vert_tex_light_coord");
   private static final List<String> R = List.of("v_Color", "v_FragDistance", "u_FogColor", "u_RenderFog", "fragColor");
   private static final int f = 600;
   private static final IdentityHashMap<RenderPipeline, w.s> x = new IdentityHashMap<>();
   private static final List<RenderPipeline> w = new ArrayList<>();
   private static boolean p;
   private static int A;

   static boolean t() {
      return !FabricLoader.getInstance().isModLoaded("iris") && q().startsWith("0.9.");
   }

   static String q() {
      return FabricLoader.getInstance().getModContainer("sodium").map(var0 -> var0.getMetadata().getVersion().getFriendlyString()).orElse("");
   }

   static void w() {
      p = (boolean)1;
   }

   static boolean p() {
      return !p && A >= 600;
   }

   static void c() {
      if (!p && ++A == 600) {
         mt.T("TerrainMaterials", "Sodium {} drew no terrain through the Wet Ground hook in {} frames; keeping Sodium output", q(), 600);
      }
   }

   static RenderPipeline C(RenderPipeline var0) {
      if (z(var0)) {
         return var0;
      }

      w.s var1 = x.get(var0);
      if (var1 != null) {
         return var1.pipeline();
      }

      if (!w.contains(var0)) {
         w.add(var0);
      }

      return var0;
   }

   static boolean R(RenderPipeline var0) {
      for (w.s var2 : x.values()) {
         if (var2.pipeline() == var0) {
            return true;
         }
      }

      return false;
   }

   static void x() {
      for (RenderPipeline var1 : w) {
         w.s var2 = D(var1);
         L(var2);
         x.put(var1, var2);
         mt.K("TerrainMaterials", "Wet Ground draws Sodium pass {} through {}", var1.getLocation(), var2.pipeline().getLocation());
      }

      w.clear();

      for (w.s var4 : x.values()) {
         L(var4);
      }
   }

   static void E() {
      x.clear();
      w.clear();
   }

   public static String m(String var0, String var1, ShaderType var2) {
      return j(var0, var1, var2, "larp_wet");
   }

   public static String j(String var0, String var1, ShaderType var2, String var3) {
      for (String var5 : var2 == ShaderType.VERTEX ? a : R) {
         if (!var0.contains(var5)) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader no longer provides " + var5);
         }
      }

      Matcher var9 = V.matcher(var0);
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
               Matcher var8 = F.matcher(var7);
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

   private static w.s D(RenderPipeline var0) {
      ShaderSource var1 = hj.k();
      String var2 = m(k(var1.getShader(hj.D(var0), ShaderType.VERTEX), hj.D(var0)), k(var1.getShader(z, ShaderType.VERTEX), z), ShaderType.VERTEX);
      String var3 = m(k(var1.getShader(hj.q(var0), ShaderType.FRAGMENT), hj.q(var0)), k(var1.getShader(z, ShaderType.FRAGMENT), z), ShaderType.FRAGMENT);
      Builder var4 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-sodium", "wet_ground/" + var0.getLocation().getPath()))
         .withVertexShader(z)
         .withFragmentShader(z)
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withPrimitiveTopology(var0.getPrimitiveTopology())
         .withDepthStencilState(Optional.ofNullable(var0.getDepthStencilState()));

      for (BindGroupLayout var6 : var0.getBindGroupLayouts()) {
         var4.withBindGroupLayout(var6);
      }

      var4.withBindGroupLayout(BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).build());
      List var8 = hj.n(var0);

      for (int var9 = 0; var9 < var8.size(); var9++) {
         var4.withColorTargetState(var9, (ColorTargetState)var8.get(var9));
      }

      List var10 = hj.P(var0);

      for (int var7 = 0; var7 < var10.size(); var7++) {
         var4.withVertexBinding(var7, (VertexFormat)var10.get(var7));
      }

      var0.getShaderDefines().flags().forEach(var4::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1x, var2x) -> var4.withShaderDefine(var1x, Float.parseFloat(var2x)));
      ShaderSource var11 = hj.t((var3x, var4x) -> !var3x.equals(z) ? var1.getShader(var3x, var4x) : (var4x == ShaderType.VERTEX ? var2 : var3));
      return new w.s(var4.build(), var11);
   }

   static String k(String var0, Identifier var1) {
      if (var0 == null) {
         throw new IllegalStateException("Shader source " + var1 + " is not loaded");
      } else {
         return var0;
      }
   }

   private static void L(w.s var0) {
      x(var0.pipeline(), var0.source());
   }

   static void x(RenderPipeline var0, ShaderSource var1) {
      if (!hj.O(var0, var1)) {
         throw new IllegalStateException("Pipeline " + var0.getLocation() + " failed; diagnostics=" + RenderSystem.getDevice().getLastDebugMessages());
      }
   }

   static boolean z(RenderPipeline var0) {
      for (ColorTargetState var2 : var0.getColorTargetStates()) {
         if (var2 != null && var2.blendFunction().isPresent()) {
            return true;
         }
      }

      return false;
   }

   private w() {
   }

   private record s(RenderPipeline pipeline, ShaderSource source) {
   }
}
