package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.class_10799;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class j_ {
   public static final String Y = "Tung Tung Tung Sahur";
   private static final class_2960 e = class_2960.method_60655("larp-client", "models/pets/sahur/mesh.bin");
   private static final class_1921 i = D(false);
   private static final class_1921 I = D(true);
   private static tt.r M;
   private static long a = -1L;
   private static long K = -1L;

   private static class_1921 D(boolean var0) {
      Builder var1 = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
         .withLocation(class_2960.method_60655("larp-client", var0 ? "sahur_strike" : "sahur_pet"))
         .withFragmentShader(class_2960.method_60655("larp-client", "particle/sprite"))
         .withVertexFormat(class_290.field_1575, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(true)
         .withoutBlend()
         .withCull(true);
      if (var0) {
         var1.withShaderDefine("DITHER_FADE");
      }

      return class_1921.method_75940(
         var0 ? "larp_sahur_strike" : "larp_sahur_pet",
         class_12247.method_75927(var1.build()).method_75934("Sampler0", class_2960.method_60655("larp-client", "models/pets/sahur/albedo.png")).method_75938()
      );
   }

   public static boolean x() {
      return M != null && a == t_.g;
   }

   public static tt.r S() {
      if (!x()) {
         throw new IllegalStateException("Sahur asset is not prepared");
      } else {
         return M;
      }
   }

   public static void H() {
      if (M == null || a != t_.g) {
         M = null;

         try (InputStream var0 = class_310.method_1551().method_1478().getResourceOrThrow(e).method_14482()) {
            tt.r var1 = tt.q(var0);
            f2.a(i.method_73243());
            M = var1;
            a = t_.g;
            q8.v("Sahur", "Static mesh ready: {} triangles, resource generation {}", M.triangles(), a);
         } catch (IOException var5) {
            throw new IllegalStateException("Sahur asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void g() {
      H();
      if (K != t_.g) {
         f2.a(I.method_73243());
         K = t_.g;
      }
   }

   public static void d(f1 var0, class_243 var1, Quaternionfc var2, float var3) {
      if (x()) {
         var0.p(i, var1, var2, var3, M.vertices(), M.colors());
         c5.V("Sahur triangles", M.triangles());
      }
   }

   public static void K(class_4587 var0, cw var1, Matrix4f var2) {
      if (x()) {
         Matrix4f var3 = new Matrix4f(var0.method_23760().method_23761()).mul(var2);
         f1.h(var1, i, var3, M.vertices(), M.colors());
         c5.V("Sahur triangles", M.triangles());
      }
   }

   public static void a(class_4587 var0, cw var1, Matrix4f var2, float[] var3, int[] var4) {
      if (x()) {
         Matrix4f var5 = new Matrix4f(var0.method_23760().method_23761()).mul(var2);
         f1.h(var1, i, var5, var3, var4);
         c5.V("Sahur triangles", var4.length / 3);
      }
   }

   public static void L(class_4587 var0, cw var1, Matrix4f var2, float[] var3, int[] var4, double var5) {
      if (x() && !(var5 <= 0.001)) {
         Matrix4f var7 = new Matrix4f(var0.method_23760().method_23761()).mul(var2);
         class_4588 var8 = var1.N(I);

         for (int var9 = 0; var9 < var4.length; var9++) {
            int var10 = var9 * 5;
            var8.method_22918(var7, var3[var10], var3[var10 + 1], var3[var10 + 2])
               .method_22913(var3[var10 + 3], var3[var10 + 4])
               .method_39415(ei.R(var4[var9], var5));
         }

         c5.V("Sahur triangles", var4.length / 3);
      }
   }

   private j_() {
   }
}
