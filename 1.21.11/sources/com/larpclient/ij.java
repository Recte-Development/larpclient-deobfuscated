package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class ij {
   public static final String W = "Bitcoin";
   public static final double Y = 0.6;
   public static final double k = 1.6;
   public static final int d = -552166;
   private static final String E = "models/pets/";
   private static final RenderPipeline G = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
      .withLocation(z("bitcoin_pet"))
      .withFragmentShader(z("particle/sprite"))
      .withVertexFormat(class_290.field_1575, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
      .withDepthWrite(true)
      .withoutBlend()
      .withCull(true)
      .build();
   public static final List<ij.r> g = List.of(
      G("Bitcoin", "bitcoin", 0.6, -552166),
      G("Solana", "solana", 0.6, -6732289),
      G("Ethereum", "ethereum", 0.6, -10322198),
      G("Litecoin", "litecoin", 0.6, -13345379),
      G("Monero", "monero", 0.6, -39424)
   );
   private static b3.f q;
   private static long l = -1L;
   private static long t = -1L;

   private static class_2960 z(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   private static ij.r G(String var0, String var1, double var2, int var4) {
      return new ij.r(
         var0,
         var1,
         var2,
         var4,
         class_1921.method_75940(
            "larp_" + var1 + "_pet", class_12247.method_75927(G).method_75934("Sampler0", z("models/pets/" + var1 + "/albedo.png")).method_75938()
         ),
         var0 + " triangles"
      );
   }

   public static ij.r j(String var0) {
      for (ij.r var2 : g) {
         if (var2.style().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static boolean w() {
      return q != null && l == t_.g;
   }

   public static void y() {
      if (!w()) {
         q = null;
         class_3300 var0 = class_310.method_1551().method_1478();

         try (InputStream var1 = var0.getResourceOrThrow(z("models/pets/bitcoin/mesh.bin")).method_14482()) {
            for (ij.r var3 : g) {
               var0.getResourceOrThrow(z("models/pets/" + var3.folder() + "/albedo.png"));
            }

            b3.f var7 = b3.w(var1);
            f2.a(G);
            q = var7;
            l = t_.g;
            q8.v("Pets", "Crypto coins ready: {} faces, {} triangles, resource generation {}", g.size(), q.triangles(), l);
         } catch (IOException var6) {
            throw new IllegalStateException("Crypto coin asset load failed; reload resources to retry", var6);
         }
      }
   }

   public static boolean G(String var0) {
      if (w()) {
         return true;
      }

      if (t == t_.g) {
         return false;
      }

      try {
         y();
         return true;
      } catch (RuntimeException var2) {
         t = t_.g;
         q8.u(var0, "Crypto coins unavailable; using fallback until resource reload", var2);
         return false;
      }
   }

   public static b3.f l() {
      return w() ? q : null;
   }

   public static void O(f1 var0, ij.r var1, Matrix4f var2) {
      if (w()) {
         var0.F(var1.type(), var2, q.vertices(), q.colors());
         c5.V(var1.profile(), q.triangles());
      }
   }

   public static void U(f1 var0, ij.r var1, class_243 var2, Quaternionfc var3, float var4) {
      if (w()) {
         var0.p(var1.type(), var2, var3, var4, q.vertices(), q.colors());
         c5.V(var1.profile(), q.triangles());
      }
   }

   private ij() {
   }

   public record r(String style, String folder, double scale, int spark, class_1921 type, String profile) {
   }
}
