package com.larpclient;

import com.larpclient.module.EmotesModule;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_10799;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import net.minecraft.class_638;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class is {
   private static final String a = "models/emotes/";
   private static final float V = 0.86F;
   private static final float G = 2.25F;
   private static final float e = 0.05F;
   private static final float m = 0.66F;
   private static final int A = 9;
   private static final RenderPipeline L = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
      .withLocation(G("emote_prop"))
      .withFragmentShader(G("particle/sprite"))
      .withVertexFormat(class_290.field_1575, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
      .withDepthWrite(true)
      .withoutBlend()
      .withCull(true)
      .build();
   private static final Map<String, is.m> j = new HashMap<>();
   private static final Map<String, class_1921> w = new HashMap<>();
   private static is.e u;

   private static class_2960 G(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   private is() {
   }

   public static void b() {
      u = null;
   }

   private static is.m V(String var0) {
      is.m var1 = j.get(var0);
      if (var1 != null && var1.q == t_.g) {
         return var1;
      }

      class_3300 var2 = class_310.method_1551().method_1478();
      class_2960 var3 = G("models/emotes/" + var0 + "/albedo.png");

      try (InputStream var4 = var2.getResourceOrThrow(G("models/emotes/" + var0 + "/mesh.bin")).method_14482()) {
         var2.getResourceOrThrow(var3);
         yg.e var5 = yg.k(var4);
         if (var5.parts().size() != 2) {
            throw new IOException("Prop needs a body and a wheel part");
         }

         f2.a(L);
         class_1921 var6 = w.computeIfAbsent(
            var0, var1x -> class_1921.method_75940("larp_emote_prop_" + var1x, class_12247.method_75927(L).method_75934("Sampler0", var3).method_75938())
         );
         var1 = new is.m(var5, var6, t_.g);
         j.put(var0, var1);
         q8.G("Emotes", "Prop {} ready: {} triangles, resource generation {}", var0, var5.triangles(), var1.q);
         return var1;
      } catch (IOException var10) {
         throw new IllegalStateException("Emote prop " + var0 + " failed to load; reload resources to retry", var10);
      }
   }

   public static void P(float var0) {
      u = null;
      EmotesModule.j var1 = EmotesModule.C(var0);
      if (var1 != null) {
         float var2 = var1.track()[6] * var1.fade() * var1.avatarScale();
         float var3 = Math.min(1.0F, var1.age() / 9.0F);
         float var4 = var1.stopping() ? Math.min(1.0F, var1.stopAge() / 4.0F) : var3;
         float var5 = var1.stopping() ? (1.0F - var4) * 0.45F : (var3 < 1.0F ? (1.0F - var3) * (1.0F - var3) * 0.55F : 0.0F);
         if (!(var2 < 0.001F) || !(var5 <= 0.0F)) {
            is.m var6 = V(var1.prop());
            float[] var7 = var1.track();
            Quaternionf var8 = new Quaternionf().rotationY((float)Math.toRadians(180.0F - var1.facing()));
            Quaternionf var9 = new Quaternionf(var8).rotateY(var7[4]);
            Quaternionf var10 = new Quaternionf(var9).rotateZ(var7[5]).rotateX(var7[3]);
            boolean var11 = "crypto_rock".equals(var1.prop());
            float var12 = var11 ? var1.avatarScale() : var2;
            Vector3f var13 = var8.transform(new Vector3f(var7[0] * var12, 0.0F, var7[2] * var12));
            class_243 var14 = var1.feet().method_1031(var13.x, var13.y, var13.z);
            Vector3f var15 = var9.transform(new Vector3f(0.0F, var7[1] * var2, 0.0F));
            class_638 var16 = class_310.method_1551().field_1687;
            int var17 = 15;
            if (var16 != null) {
               class_2338 var18 = class_2338.method_49637(var14.field_1352, var14.field_1351 + 1.2, var14.field_1350);
               var17 = var16.method_22335(var18, var16.method_8594());
            }

            R(var6, var17);
            u = new is.e(
               var6,
               var14,
               var9,
               var10,
               var14.method_1031(var15.x, var15.y, var15.z),
               var2,
               Math.clamp(0.42F - var7[1] * 0.9F, 0.0F, 0.5F) * Math.min(1.0F, var2),
               0.55F + 1.9F * var4,
               var5,
               var11 ? var1.age() + var1.stopAge() : -1.0F,
               var11 && ij.G("Emotes"),
               Math.round(170.0F * e(var17))
            );
         }
      }
   }

   private static float e(int var0) {
      return 0.22F + 0.78F * (float)Math.pow(var0 / 15.0, 1.3);
   }

   private static void R(is.m var0, int var1) {
      if (var0.c != var1) {
         float var2 = e(var1);

         for (int var3 = 0; var3 < var0.y.length; var3++) {
            int[] var4 = var0.H.parts().get(var3).colors();
            int[] var5 = var0.y[var3];

            for (int var6 = 0; var6 < var4.length; var6++) {
               int var7 = Math.round((var4[var6] & 0xFF) * var2);
               var5[var6] = 0xFF000000 | var7 << 16 | var7 << 8 | var7;
            }
         }

         var0.c = var1;
      }
   }

   public static void j(f1 var0, boolean var1) {
      is.e var2 = u;
      if (var2 != null && !var1) {
         if (var2.puffAlpha > 0.0F) {
            var0.f(
               var2.ground.field_1352,
               var2.ground.field_1351 + 0.02,
               var2.ground.field_1350,
               1.1 * var2.puff,
               0.5 + 0.4 * var2.puff,
               ei.D(-1446155, var2.puffAlpha)
            );
            var0.f(var2.ground.field_1352, var2.ground.field_1351 + 0.03, var2.ground.field_1350, 0.6 * var2.puff, 0.35, ei.D(-1, var2.puffAlpha * 0.7));
         }

         if (!(var2.scale < 0.001F)) {
            if (var2.shadow > 0.0F) {
               Q(var0, var2);
            }

            List var3 = var2.prop.H.parts();
            var0.p(var2.prop.f, var2.bodyOrigin, var2.bodyRotation, var2.scale, ((yg.j)var3.get(0)).vertices(), var2.prop.y[0]);
            var0.p(var2.prop.f, var2.ground, var2.carRotation, var2.scale, ((yg.j)var3.get(1)).vertices(), var2.prop.y[1]);
            c5.V("Emote prop triangles", var2.prop.H.triangles());
            if (var2.effectsAge >= 0.0F) {
               u(var0, var2);
            }
         }
      }
   }

   private static void u(f1 var0, is.e var1) {
      float var2 = yz.a(var1.effectsAge);
      if (var2 >= 0.0F && var2 < 3.0F) {
         class_243 var3 = W(var1, -0.3672F, 0.37802F, 0.43596F);
         float var4 = var2 / 3.0F;
         var0.V(var3.field_1352, var3.field_1351, var3.field_1350, 0.34F * var1.scale * (1.0F - var4 * var4), var2 * 0.7F, ei.D(-11654, 1.0F - var4));
      }

      if (var2 >= 0.0F && var2 < 6.0F) {
         float var8 = var2 / 6.0F;
         var0.f(
            var1.ground.field_1352,
            var1.ground.field_1351 + 0.02,
            var1.ground.field_1350,
            (0.72F + 0.5F * var8) * var1.scale,
            0.3F,
            ei.D(-4607064, 0.28F * (1.0F - var8))
         );
      }

      int var9 = var1.chipGray;
      int[] var10 = new int[yz.d.length / 5];
      Arrays.fill(var10, 0xFF000000 | var9 << 16 | var9 << 8 | var9);

      for (yz.w var6 : yz.Z(var1.effectsAge)) {
         if (!(var6.scale() <= 0.0F)) {
            Quaternionf var7 = new Quaternionf(var1.carRotation).rotateXYZ(var6.spin(), var6.spin() * 0.7F, var6.spin() * 1.3F);
            var0.p(var1.prop.f, W(var1, var6.x(), var6.y(), var6.z()), var7, var6.scale() * var1.scale, yz.d, var10);
         }
      }

      if (var1.coins) {
         for (yz.b var12 : yz.b(var1.effectsAge)) {
            if (!(var12.scale() <= 0.0F)) {
               Quaternionf var13 = new Quaternionf(var1.carRotation).rotateY(var12.yaw()).rotateX(var12.pitch());
               ij.U(var0, ij.g.get(var12.kind()), W(var1, var12.x(), var12.y(), var12.z()), var13, var12.scale() * var1.scale);
            }
         }
      }
   }

   private static class_243 W(is.e var0, float var1, float var2, float var3) {
      Vector3f var4 = var0.carRotation.transform(new Vector3f(var1, var2, var3).mul(var0.scale));
      return var0.ground.method_1031(var4.x, var4.y, var4.z);
   }

   private static void Q(f1 var0, is.e var1) {
      int var2 = var1.effectsAge >= 0.0F ? 1 : 0;
      float var3 = 0.32F * var1.scale;
      float var4 = (var2 != 0 ? 0.66F : 0.86F) * var1.scale;
      float var5 = (var2 != 0 ? 0.66F : 2.25F) * var1.scale;
      float var6 = var2 != 0 ? 0.0F : 0.05F * var1.scale;
      int var7 = ei.D(-16777216, var1.shadow);
      byte var8 = 0;
      float[][] var9 = new float[][]{{-var4, var6 - var5}, {var4, var6 - var5}, {var4, var6 + var5}, {-var4, var6 + var5}};
      float[][] var10 = new float[][]{
         {-var4 - var3, var6 - var5 - var3}, {var4 + var3, var6 - var5 - var3}, {var4 + var3, var6 + var5 + var3}, {-var4 - var3, var6 + var5 + var3}
      };
      d(var0, var1, var9[3], var7, var9[2], var7, var9[1], var7, var9[0], var7);

      for (int var11 = 0; var11 < 4; var11++) {
         int var12 = (var11 + 1) % 4;
         d(var0, var1, var10[var11], var8, var10[var12], var8, var9[var12], var7, var9[var11], var7);
      }
   }

   private static void d(f1 var0, is.e var1, float[] var2, int var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
      d(var0, var1, var2, var3);
      d(var0, var1, var4, var5);
      d(var0, var1, var6, var7);
      d(var0, var1, var8, var9);
   }

   private static void d(f1 var0, is.e var1, float[] var2, int var3) {
      Vector3f var4 = var1.carRotation.transform(new Vector3f(var2[0], 0.0F, var2[1]));
      var0.vertex(var1.ground.field_1352 + var4.x, var1.ground.field_1351 + 0.015, var1.ground.field_1350 + var4.z, var3);
   }

   private record e(
      is.m prop,
      class_243 ground,
      Quaternionf carRotation,
      Quaternionf bodyRotation,
      class_243 bodyOrigin,
      float scale,
      float shadow,
      float puff,
      float puffAlpha,
      float effectsAge,
      boolean coins,
      int chipGray
   ) {
   }

   private static final class m {
      final yg.e H;
      final class_1921 f;
      final long q;
      final int[][] y;
      int c = -1;

      m(yg.e var1, class_1921 var2, long var3) {
         this.H = var1;
         this.f = var2;
         this.q = var3;
         this.y = new int[var1.parts().size()][];

         for (int var5 = 0; var5 < this.y.length; var5++) {
            this.y[var5] = new int[var1.parts().get(var5).colors().length];
         }
      }
   }
}
