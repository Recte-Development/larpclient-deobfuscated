package com.larpclient;

import com.larpclient.module.FlameHandsModule;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;
import net.minecraft.class_10799;
import net.minecraft.class_11659;
import net.minecraft.class_12247;
import net.minecraft.class_1306;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4587.class_4665;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class sg {
   public static final int c = 768;
   private static final class_2960 v = class_2960.method_60655("larp-client", "textures/particles/flame.png");
   private static final class_2960 u = class_2960.method_60655("larp-client", "textures/particles/flame_glow.png");
   private static final sg.g[] D = new sg.g[]{new sg.g(new Random()), new sg.g(new Random())};
   private static Object f;
   private static Object g;
   private static int k = -1;
   private static boolean y;
   private static class_243 m;

   public static void E() {
      for (sg.g var3 : D) {
         var3.v();
      }

      g = null;
      f = null;
      m = null;
      y = (boolean)0;
   }

   public static void N() {
      class_310 var0 = class_310.method_1551();
      if (f != var0.field_1687 || g != var0.field_1724 || k != t_.g) {
         E();
         f = var0.field_1687;
         g = var0.field_1724;
         k = t_.g;
      }

      if (var0.field_1724 != null) {
         class_243 var1 = var0.field_1724.method_73189();
         if (m != null && m.method_1025(var1) > 64.0) {
            for (sg.g var5 : D) {
               var5.v();
            }

            q8.v("FlameHands", "Cleared histories after player relocation");
         }

         m = var1;
      }

      if (var0.field_1724 == null || var0.field_1687 == null || !var0.field_1690.method_31044().method_31034()) {
         for (sg.g var9 : D) {
            var9.v();
         }
      }
   }

   public static void D(class_1306 var0, class_4587 var1, class_11659 var2, boolean var3) {
      FlameHandsModule var4 = FlameHandsModule.L();
      class_310 var5 = class_310.method_1551();
      if (var4 != null && var4.isRuntimeActive() && var5.field_1724 != null && var5.field_1687 != null && var5.field_1690.method_31044().method_31034()) {
         N();
         sg.g var6 = D[var0 == class_1306.field_6183 ? 0 : 1];
         if (!var4.K(var0 == var5.field_1724.method_6068(), var3)) {
            var6.v();
         } else if (!y) {
            try {
               sg.n.y();
               class_4184 var7 = var5.field_1773.method_19418();
               class_243 var13 = var5.field_1724.method_18798();
               Vector3f var14 = new Vector3f((float)var13.field_1352, (float)var13.field_1351, (float)var13.field_1350)
                  .rotate(new Quaternionf(var7.method_23767()).conjugate());
               sg.w var15 = var4.j();
               var6.N(qo.x(), var7.method_19330(), var7.method_19329(), var14.x, var14.y, var14.z, var3, var15);
               double[] var16 = var6.J(var1.method_23760().method_23761(), var15);
               if (var16.length == 0) {
                  return;
               }

               var2.method_73529(2).method_73483(new class_4587(), sg.n.s(var4.T.G("Glow")), (var1x, var2x) -> b(var16, var1x, var2x));
               c5.L("Flame hand particles", var16.length / 6);
            } catch (RuntimeException var12) {
               for (sg.g var11 : D) {
                  var11.v();
               }

               y = (boolean)1;
               q8.u("FlameHands", "Hand particle submission failed; suspended until disable or resource reload", var12);
            }
         }
      }
   }

   private static void b(double[] var0, class_4665 var1, class_4588 var2) {
      for (byte var3 = 0; var3 < var0.length; var3 += 6) {
         float var4 = (float)var0[var3];
         float var5 = (float)var0[var3 + 1];
         float var6 = (float)var0[var3 + 2];
         float var7 = (float)var0[var3 + 3] * 0.5F;
         float var8 = (float)var0[var3 + 4];
         float var9 = (float)Math.cos(var8) * var7;
         float var10 = (float)Math.sin(var8) * var7;
         int var11 = (int)((long)var0[var3 + 5]);
         F(var2, var1, var4 - var9 - var10, var5 + var9 - var10, var6, var11, 0.0F, 1.0F);
         F(var2, var1, var4 + var9 - var10, var5 + var9 + var10, var6, var11, 1.0F, 1.0F);
         F(var2, var1, var4 + var9 + var10, var5 - var9 + var10, var6, var11, 1.0F, 0.0F);
         F(var2, var1, var4 - var9 + var10, var5 - var9 - var10, var6, var11, 0.0F, 0.0F);
      }
   }

   private static void F(class_4588 var0, class_4665 var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      var0.method_56824(var1, var2, var3, var4).method_39415(var5).method_22913(var6, var7);
   }

   public static float W(float var0) {
      float var1 = var0 % 360.0F;
      return var1 >= 180.0F ? var1 - 360.0F : (var1 < -180.0F ? var1 + 360.0F : var1);
   }

   public static int V(double var0, sg.w var2) {
      return switch (var2.colorMode) {
         case "Gradient" -> G(var2.primary, var2.secondary, (float)(Math.sin(var0 * 3.0) * 0.5 + 0.5));
         case "Rainbow" -> Color.HSBtoRGB((float)(var0 % 4.0 / 4.0), 1.0F, 1.0F);
         default -> var2.primary;
      };
   }

   public static int G(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = 0;

      for (byte var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= Math.round((var0 >>> var5 & 0xFF) * (1.0F - var3) + (var1 >>> var5 & 0xFF) * var3) << var5;
      }

      return var4;
   }

   private sg() {
   }

   private static final class c {
      float Y;
      float F;
      float G;
      float K;
      float i;
      float a;
      float P;
      float H;
      float M;
      float Z;
      double o;
      int Q;
   }

   public static final class g {
      private final RandomGenerator C;
      private final ArrayList<sg.c> V = new ArrayList<>();
      private double M = Double.NaN;
      private double A;
      private float s;
      private float o;

      public g(RandomGenerator var1) {
         this.C = var1;
      }

      public int d() {
         return this.V.size();
      }

      public void v() {
         this.V.clear();
         this.M = Double.NaN;
         this.A = 0.0;
      }

      public void N(double var1, float var3, float var4, float var5, float var6, float var7, boolean var8, sg.w var9) {
         if (!Double.isFinite(var1)) {
            this.v();
         } else if (Double.isFinite(this.M) && !(var1 < this.M) && !(var1 - this.M > 0.25)) {
            float var10 = (float)Math.clamp(var1 - this.M, 0.0, 0.05);
            if (!(var10 <= 0.0F)) {
               this.M = var1;
               float var11 = sg.W(var3 - this.s);
               float var12 = var4 - this.o;
               this.s = var3;
               this.o = var4;
               float var13 = var9.inertia;
               float var14 = -var11 * var13 * 0.008F - var5 * var13 * 0.15F;
               float var15 = var12 * var13 * 0.008F - var6 * var13 * 0.15F + var9.buoyancy * 0.012F;
               float var16 = -var7 * var13 * 0.15F;
               float var17 = var10 * 60.0F;
               float var18 = (float)Math.pow(0.86, var17);
               float var19 = var9.turbulence * 0.008F;

               for (int var20 = 0; var20 < this.V.size(); var20++) {
                  sg.c var21 = this.V.get(var20);
                  if (var1 - var21.o >= var21.H) {
                     this.V.remove(var20--);
                  } else {
                     var21.Y = var21.Y + var21.K * var17;
                     var21.F = var21.F + var21.i * var17;
                     var21.G = var21.G + var21.a * var17;
                     var21.K = (var21.K + (var14 + this.z(-var19, var19)) * var17) * var18;
                     var21.i = (var21.i + (var15 + this.z(-var19, var19) * 0.5F) * var17) * var18;
                     var21.a = (var21.a + (var16 + this.z(-var19, var19)) * var17) * var18;
                     var21.M = var21.M + var21.Z * var10;
                  }
               }

               this.A += var10;
               double var26 = 1.0 / var9.density;

               while (this.A + 1.0E-8 >= var26) {
                  this.A = Math.max(0.0, this.A - var26);
                  float var22 = this.C.nextFloat();
                  float var23 = var8 ? 0.45F : 0.15F;
                  float var24 = var8 ? 0.06F : 0.04F;
                  sg.c var25 = new sg.c();
                  var25.Y = this.z(-var24, var24);
                  var25.F = var22 * var23 + this.z(-0.015F, 0.015F);
                  var25.G = -var22 * var23 * 0.35F + this.z(-0.015F, 0.015F);
                  var25.K = this.z(-0.001F, 0.001F);
                  var25.i = this.z(0.003F, 0.008F);
                  var25.a = this.z(-0.001F, 0.001F);
                  var25.P = var9.size * this.z(0.7F, 1.3F) * (1.0F - var22 * 0.4F);
                  var25.H = Math.max(0.1F, var9.lifetime * this.z(0.8F, 1.2F));
                  var25.o = var1;
                  var25.Q = sg.V(var1, var9);
                  var25.M = this.z(0.0F, 360.0F);
                  var25.Z = this.z(-90.0F, 90.0F);
                  if (this.V.size() >= 768) {
                     this.V.removeFirst();
                  }

                  this.V.add(var25);
               }
            }
         } else {
            this.v();
            this.M = var1;
            this.s = var3;
            this.o = var4;
         }
      }

      private float z(float var1, float var2) {
         return var1 + (var2 - var1) * this.C.nextFloat();
      }

      public double[] J(Matrix4f var1, sg.w var2) {
         double[] var3 = new double[this.V.size() * 6];
         int var4 = 0;

         for (sg.c var6 : this.V) {
            Vector3f var7 = new Vector3f(var6.Y, var6.F, var6.G);
            var1.transformPosition(var7);
            float var8 = (float)Math.clamp((this.M - var6.o) / var6.H, 0.0, 1.0);
            int var9 = var2.colorMode.equals("Gradient") ? sg.G(var6.Q, var2.secondary, var8) : var6.Q;
            int var10 = Math.round((var9 >>> 24) * (1.0F - var8));
            var3[var4++] = var7.x;
            var3[var4++] = var7.y;
            var3[var4++] = var7.z;
            var3[var4++] = var6.P;
            var3[var4++] = (float)Math.toRadians(var6.M);
            var3[var4++] = var9 & 16777215 | var10 << 24;
         }

         return var3;
      }
   }

   private static final class n {
      private static final RenderPipeline C = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
         .withLocation(class_2960.method_60655("larp-client", "flame_hands"))
         .withFragmentShader(class_2960.method_60655("larp-client", "particle/sprite"))
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withCull(false)
         .build();
      private static int F = -1;
      private static final class_1921 h = X("bloom", sg.v);
      private static final class_1921 H = X("glow", sg.u);

      private static class_1921 X(String var0, class_2960 var1) {
         return class_1921.method_75940("larp_flame_hand_" + var0, class_12247.method_75927(C).method_75934("Sampler0", var1).method_75938());
      }

      private static class_1921 s(boolean var0) {
         return var0 ? H : h;
      }

      private static void y() {
         if (F != t_.g) {
            f2.a(C);
            F = t_.g;
            q8.v("FlameHands", "Reference additive sprite material validated for resource generation {}", F);
         }
      }
   }

   public record w(float size, float density, float lifetime, float buoyancy, float turbulence, float inertia, int primary, int secondary, String colorMode) {
   }
}
