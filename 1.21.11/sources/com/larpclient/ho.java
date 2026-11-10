package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_243;
import org.joml.Matrix4f;

public final class ho {
   public static final float j = 0.12F;
   public static final int z = 73;
   private static final int[] L = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
   private static final float[] D = new float[]{0.27F, 0.2F, 0.2F, 0.1F};
   public static final List<ho.d> X = b();
   public static final List<ho.e> P;
   public static final List<ho.w> d = List.of(
      new ho.w(0, 1, 0.028F, true),
      new ho.w(1, 2, 0.024F, true),
      new ho.w(2, 3, 0.019F, true),
      new ho.w(3, 4, 0.012F, false),
      new ho.w(2, 19, 0.013F, false),
      new ho.w(19, 7, 0.008F, false),
      new ho.w(2, 22, 0.012F, false),
      new ho.w(22, 10, 0.007F, false),
      new ho.w(2, 25, 0.01F, false),
      new ho.w(25, 13, 0.005F, false)
   );
   public static final float C = 1.38F;
   public static final float O = 0.16F;
   public static final float F = 2.0F;

   private static List<ho.d> b() {
      ArrayList var0 = new ArrayList<>(
         List.of(
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(0.21F, 0.29F, -0.015F),
            new ho.d(0.48F, 0.47F, 0.045F),
            new ho.d(1.14F, 0.49F, 0.05F),
            new ho.d(1.52F, 0.12F, 0.08F),
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(1.18F, -0.45F, 0.06F),
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(0.68F, -0.55F, 0.03F),
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(0.1F, -0.7F, 0.005F),
            new ho.d(0.0F, 0.0F, 0.0F),
            new ho.d(0.0F, 0.0F, 0.0F)
         )
      );

      for (int var1 = 0; var1 < 4; var1++) {
         ho.d var2 = (ho.d)var0.get(L[var1 * 3]);
         ho.d var3 = (ho.d)var0.get(L[var1 * 3 + 3]);
         ho.d var4 = M(M(var2, var3, 0.5F), (ho.d)var0.get(2), D[var1] * 2.0F);

         for (int var5 = 1; var5 < 3; var5++) {
            float var6 = var5 / 3.0F;
            var0.set(L[var1 * 3 + var5], M(M(var2, var4, var6), M(var4, var3, var6), var6));
         }
      }

      return List.copyOf(var0);
   }

   public static ho.z n(boolean var0, boolean var1, float var2, float var3) {
      return O(var0 ? 1.0F : 0.0F, var1 ? 1.0F : 0.0F, var2, var3);
   }

   public static ho.z O(float var0, float var1, float var2, float var3) {
      float var4 = Math.clamp(var0, 0.0F, 1.0F);
      float var5 = Math.clamp(var1, 0.0F, 1.0F);
      float var6 = Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) * (-90.0F - var3);
      return new ho.z(
         0.34F * var5,
         0.46F * var5,
         (1.38F - 0.42F * var4) * (1.0F - var5),
         0.16F * (1.0F - var5),
         18.0F * var4 * (1.0F - var5) + var6 * var5,
         1.0F - 0.24F * var5,
         1.0F - 0.08F * var5,
         7.0F * (1.0F - var5) + 2.0F * var5,
         11.0F * (1.0F - var5) + 1.5F * var5,
         0.09F,
         0.025F,
         -6.0F - 3.0F * var4,
         -3.0F,
         0.12F
      );
   }

   public static Matrix4f o(ho.z var0, float var1, float var2, float var3, float var4, int var5) {
      return q(
         new Matrix4f().rotateY(R(180.0F - var1)).translate(0.0F, var0.preY, var0.preZ).rotateX(R(var0.pitch)).translate(0.0F, var0.anchorY, var0.anchorZ),
         var0,
         var2,
         var3,
         var4,
         var5
      );
   }

   public static Matrix4f d(ho.z var0, float var1, float var2, float var3, int var4, boolean var5) {
      return q(
         new Matrix4f()
            .translate(0.0F, var5 ? 0.125F : 0.0F, 0.0F)
            .translate(0.0F, 1.501F, 0.0F)
            .scale(1.0666667F)
            .scale(-1.0F, -1.0F, 1.0F)
            .translate(0.0F, 1.38F, 0.16F),
         var0,
         var1,
         var2,
         var3,
         var4
      );
   }

   private static Matrix4f q(Matrix4f var0, ho.z var1, float var2, float var3, float var4, int var5) {
      float var6 = var2 * var1.frequency;
      float var7 = (float)(Math.sin(var6) + 0.16 * Math.sin(var6 * 2.0F)) * var1.amplitude;
      float var8 = (18.0F + var7 + Math.clamp(var3, 0.0F, 1.0F) * var1.walkSpread) * var1.openScale;
      return var0.scale(var4 * var1.scale)
         .translate(var5 * var1.hingeX, 0.0F, var1.hingeZ)
         .rotateY(R(-var5 * var8))
         .rotateZ(R(var5 * (var1.splay + 3.5F * (float)Math.cos(var6))))
         .rotateX(R(var1.wingPitch));
   }

   public static ho.x M(float var0, float var1, float var2, float var3) {
      ArrayList var4 = new ArrayList<>(X);
      ho.d var5 = (ho.d)var4.get(2);
      float var6 = var0 * 0.12F;
      float var7 = 0.075F + 0.018F * (float)Math.sin(var6 - 0.7F);

      for (int var8 = 0; var8 < 13; var8++) {
         ho.d var9 = M(var5, (ho.d)var4.get(L[var8]), 0.5F);
         var4.add(new ho.d(var9.x, var9.y, var9.z + (var8 % 3 != 0 ? var7 : 0.008F)));
      }

      float var21 = R(8.0F + 7.0F * (float)Math.sin(var6 - 0.65F) + 5.0F * Math.clamp(var1, 0.0F, 1.0F) + 7.0F * var2);
      float var22 = R(-4.0F * var2 + 2.0F * (float)Math.sin(var6 - 0.95F));
      float var10 = 1.0F - 0.7F * Math.clamp(var3, 0.0F, 1.0F);

      for (int var11 = 0; var11 < var4.size(); var11++) {
         if (var11 != 0 && var11 != 1 && var11 != 2) {
            ho.d var12 = (ho.d)var4.get(var11);
            float var13 = Math.clamp(var12.x / 0.48F, 0.0F, 1.0F);
            float var14 = var21 * var13 * var10;
            float var15 = var22 * var13 * var10;
            float var16 = var12.x - var5.x;
            float var17 = var12.y - var5.y;
            float var18 = var12.z - var5.z;
            float var19 = var16 * (float)Math.cos(var14) + var18 * (float)Math.sin(var14);
            float var20 = -var16 * (float)Math.sin(var14) + var18 * (float)Math.cos(var14);
            var4.set(
               var11,
               new ho.d(
                  var5.x + var19 * (float)Math.cos(var15) - var17 * (float)Math.sin(var15),
                  var5.y + var19 * (float)Math.sin(var15) + var17 * (float)Math.cos(var15),
                  var5.z + var20
               )
            );
         }
      }

      return new ho.x(List.copyOf(var4), 0.88F + 0.12F * (float)Math.sin(var6 - 0.25F));
   }

   public static class_243 C(class_243 var0, Matrix4f var1, int var2, ho.d var3) {
      float var4 = var2 * var3.x;
      float var5 = var3.y;
      float var6 = var3.z;
      return var0.method_1031(
         var1.m00() * var4 + var1.m10() * var5 + var1.m20() * var6 + var1.m30(),
         var1.m01() * var4 + var1.m11() * var5 + var1.m21() * var6 + var1.m31(),
         var1.m02() * var4 + var1.m12() * var5 + var1.m22() * var6 + var1.m32()
      );
   }

   public static ho.o e(class_243 var0, Matrix4f var1, int var2, ho.x var3, int var4, int var5, float var6) {
      return new ho.o(
         var3.points.stream().map(var3x -> C(var0, var1, var2, var3x)).toList(),
         new class_243(var1.m20(), var1.m21(), var1.m22()).method_1029(),
         var4,
         var5,
         var6,
         var3.pulse
      );
   }

   public static int H(ho.o var0, int var1) {
      float var2 = var1 < X.size() ? X.get(var1).x / 1.52F : 0.4F;
      int var3 = ei.W(var0.primary, var0.secondary, Math.clamp(var2, 0.0F, 1.0F));
      int var4 = var1 >= X.size() && (var1 - X.size()) % 3 != 0 ? 1 : 0;
      double var5 = var4 != 0 ? 0.9 : (var1 >= X.size() ? 0.56 : (var1 < 3 ? 0.3 : 0.62));
      int var7 = var1 >= X.size() ? 212 : (var1 < 3 ? 225 : 145);
      return c(ei.W(-15526620, var3, var5), Math.round(var7 * (var3 >>> 24) / 255.0F));
   }

   public static int A(ho.o var0, int var1, boolean var2) {
      double var3 = var1 >= X.size() ? 0.4 : Math.clamp(X.get(var1).x / 1.52F, 0.0F, 1.0F);
      int var5 = ei.W(var0.primary, var0.secondary, var3);
      return c(ei.W(var5, -1, var2 ? 0.22 : 0.38), Math.round((var2 ? 88 : 215) * var0.pulse * (var5 >>> 24) / 255.0F));
   }

   public static boolean v(ho.o var0) {
      return var0.primary >>> 24 > 0 || var0.secondary >>> 24 > 0;
   }

   public static void B(gq var0, ho.o var1, ho.t var2) {
      List var3 = var1.points;
      if (var2 != ho.t.w) {
         var0.blend(ti.d.l);
         double var9 = (var2 == ho.t.b ? 0.047 : 0.008) * var1.size;

         for (int var11 = 0; var11 < X.size(); var11++) {
            var0.ribbon(
               (class_243)var3.get(var11),
               (class_243)var3.get((var11 + 1) % X.size()),
               var9,
               A(var1, var11, var2 == ho.t.b),
               A(var1, (var11 + 1) % X.size(), var2 == ho.t.b)
            );
         }
      } else {
         var0.blend(ti.d.G);

         for (ho.e var5 : P) {
            f(var0, (class_243)var3.get(var5.a), H(var1, var5.a));
            f(var0, (class_243)var3.get(var5.b), H(var1, var5.b));
            f(var0, (class_243)var3.get(var5.c), H(var1, var5.c));
            f(var0, (class_243)var3.get(var5.d), H(var1, var5.d));
         }

         for (ho.w var10 : d) {
            int var6 = A(var1, var10.a, false);
            int var7 = A(var1, var10.b, false);
            var0.ribbon((class_243)var3.get(var10.a), (class_243)var3.get(var10.b), var10.width * var1.size, var6, var7);
            if (var10.ridge) {
               P(var0, (class_243)var3.get(var10.a), (class_243)var3.get(var10.b), var1.normal, var10.width * var1.size, var6, var7);
            }
         }

         j(var0, (class_243)var3.get(1), (class_243)var3.get(0), (class_243)var3.get(2), var1.normal, 0.045 * var1.size, A(var1, 1, false));
         j(var0, (class_243)var3.get(2), (class_243)var3.get(1), (class_243)var3.get(3), var1.normal, 0.036 * var1.size, A(var1, 2, false));
      }
   }

   public static void X(f1 var0, ho.o var1) {
      if (v(var1)) {
         var0.blend(ti.d.l);
         List var2 = var1.points;

         for (int var3 = 0; var3 < X.size(); var3++) {
            var0.ribbon(
               (class_243)var2.get(var3),
               (class_243)var2.get((var3 + 1) % X.size()),
               0.016 * var1.size,
               A(var1, var3, false),
               A(var1, (var3 + 1) % X.size(), false)
            );
         }

         for (ho.w var4 : d) {
            if (var4.a == 2) {
               var0.ribbon((class_243)var2.get(var4.a), (class_243)var2.get(var4.b), 0.01 * var1.size, A(var1, var4.a, false), A(var1, var4.b, false));
            }
         }
      }
   }

   private static void P(gq var0, class_243 var1, class_243 var2, class_243 var3, double var4, int var6, int var7) {
      class_243 var8 = var3.method_1021(var4 * 0.4);
      f(var0, var1.method_1019(var8), ei.R(var6, 0.72));
      f(var0, var2.method_1019(var8), ei.R(var7, 0.72));
      f(var0, var2.method_1020(var8), ei.R(var7, 0.45));
      f(var0, var1.method_1020(var8), ei.R(var6, 0.45));
   }

   private static void j(gq var0, class_243 var1, class_243 var2, class_243 var3, class_243 var4, double var5, int var7) {
      class_243 var8 = var3.method_1020(var2).method_1029().method_1021(var5);
      class_243 var9 = var8.method_1036(var4).method_1029().method_1021(var5);
      f(var0, var1.method_1019(var8), var7);
      f(var0, var1.method_1019(var9), ei.R(var7, 0.75));
      f(var0, var1.method_1020(var8), ei.R(var7, 0.55));
      f(var0, var1.method_1020(var9), var7);
   }

   private static ho.d M(ho.d var0, ho.d var1, float var2) {
      return new ho.d(var0.x + (var1.x - var0.x) * var2, var0.y + (var1.y - var0.y) * var2, var0.z + (var1.z - var0.z) * var2);
   }

   private static void f(gq var0, class_243 var1, int var2) {
      var0.vertex(var1.field_1352, var1.field_1351, var1.field_1350, var2);
   }

   private static int c(int var0, int var1) {
      return var1 << 24 | var0 & 16777215;
   }

   private static float R(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private ho() {
   }

   static {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 12; var1++) {
         var0.add(new ho.e(2, 16 + var1, 17 + var1, 2));
         var0.add(new ho.e(16 + var1, L[var1], L[var1 + 1], 17 + var1));
      }

      var0.add(new ho.e(0, 1, 2, 28));
      var0.add(new ho.e(2, 3, 4, 16));
      P = List.copyOf(var0);
   }

   public static final class b {
      private boolean F;
      private float s;
      private float C;
      private float q;
      private float T;
      private float M;
      private float A;
      private float Z;
      private class_243 r;

      public void p(float var1, boolean var2, float var3, boolean var4, class_243 var5) {
         this.R(var1, var2, var3, var4, false, var5);
      }

      public void R(float var1, boolean var2, float var3, boolean var4, boolean var5, class_243 var6) {
         float var7 = Math.clamp(var3, 0.0F, 1.0F);
         int var8 = this.F && !(var1 < this.s) && !(var1 - this.s > 10.0F) && (this.r == null || !(var6.method_1025(this.r) > 64.0)) ? 0 : 1;
         if (var8 != 0) {
            this.C = var2 ? 1.0F : 0.0F;
            this.q = var7;
            this.T = var4 ? 1.0F : 0.0F;
            this.M = var5 ? 1.0F : 0.0F;
            this.A = 0.0F;
         } else {
            float var9 = Math.clamp((var1 - this.s) * 0.05F, 0.0F, 0.1F);
            float var10 = 1.0F - (float)Math.exp(-var9 / 0.13F);
            float var11 = 1.0F - (float)Math.exp(-var9 / 0.19F);
            this.C = this.C + ((var2 ? 1 : 0) - this.C) * var10;
            this.T = this.T + ((var4 ? 1 : 0) - this.T) * var10;
            this.M = this.M + ((var5 ? 1 : 0) - this.M) * var10;
            this.q = this.q + (var7 - this.q) * var11;
            this.A = var9 * 20.0F;
         }

         this.s = var1;
         this.r = var6;
         this.F = (boolean)1;
      }

      public void i(float var1) {
         this.Z = (float)((this.Z + this.A * var1) % (Math.PI * 2));
      }

      public float x() {
         return this.C;
      }

      public float N() {
         return this.q;
      }

      public float k() {
         return this.T;
      }

      public float T() {
         return this.M;
      }

      public float V() {
         return this.Z;
      }
   }

   public record d(float x, float y, float z) {
   }

   public record e(int a, int b, int c, int d) {
   }

   public record o(List<class_243> points, class_243 normal, int primary, int secondary, float size, float pulse) {
   }

   public enum t {
      b,
      w,
      G;
   }

   public record w(int a, int b, float width, boolean ridge) {
   }

   public record x(List<ho.d> points, float pulse) {
   }

   public record z(
      float preY,
      float preZ,
      float anchorY,
      float anchorZ,
      float pitch,
      float openScale,
      float scale,
      float walkSpread,
      float amplitude,
      float hingeX,
      float hingeZ,
      float splay,
      float wingPitch,
      float frequency
   ) {
   }
}
