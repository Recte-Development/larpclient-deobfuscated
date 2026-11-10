package com.larpclient;

import net.minecraft.class_243;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class g4 {
   public static final double s = 3.6;
   public static final double z = 0.78;
   public static final double F = 1.62;
   public static final double x = 2.16;
   public static final double Z = 1.12;
   public static final double r = 6.640000000000001;
   public static final double J = 7.5;
   public static final double i = 0.42;
   public static final double v = 0.84;
   private static final double M = 0.028;
   private static final double C = -0.062;
   private static final double N = -0.105;
   private static final class_243 b = new class_243(0.028, -0.105, -0.062);
   private static final class_243[] d = new class_243[]{new class_243(-0.031, 0.08, -0.067), new class_243(0.088, 0.08, -0.067)};
   private static final class_243[] X = new class_243[]{new class_243(-0.038, 0.002, -0.068), new class_243(0.096, 0.002, -0.068)};
   private static final class_243[] w = new class_243[]{new class_243(-0.027, -0.077, -0.05), new class_243(0.077, -0.077, -0.05)};
   private static final class_243[] l = new class_243[]{new class_243(0.001, -0.105, -0.062), new class_243(0.055, -0.105, -0.062)};
   private static final class_243[] H = new class_243[]{new class_243(-0.001, -0.194, -0.067), new class_243(0.057, -0.194, -0.067)};
   private static final class_243[] f = new class_243[]{new class_243(-0.003, -0.28, -0.068), new class_243(0.057, -0.28, -0.068)};
   private static final double[] I = new double[]{1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0};
   private static final double[] G = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
   private static final class_243 y = new class_243(0.0, 0.0, 1.0);
   private static final class_243 c = new class_243(0.0, 0.0, -1.0);
   private final tt.r g;
   private final int[] Y;
   private final float[] O;
   private final double k;
   private final Matrix4f j = new Matrix4f();
   private final Matrix4f t = new Matrix4f();
   private final g4.v[] P = new g4.v[]{new g4.v(), new g4.v()};
   private final g4.v[] h = new g4.v[]{new g4.v(), new g4.v()};
   private final Vector3f Q = new Vector3f();
   private final Vector3f V = new Vector3f();

   public g4(tt.r var1) {
      l.i var2 = com.larpclient.l.l(var1);
      this.g = var2.mesh();
      this.Y = var2.joints();
      this.O = (float[])this.g.vertices().clone();
      this.k = 3.6 / var1.height();
   }

   private static double I(double var0) {
      return u8.S(var0);
   }

   private static double J(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public static g4.s k(double var0) {
      double var2 = Math.max(0.0, var0);
      double var4 = Math.clamp((var2 - 0.78) / 0.8400000000000001, 0.0, 1.0);
      double var6 = I(var4);
      double var8 = 4.0 * var4 * (1.0 - var4);
      double var10 = var2 < 0.78 ? I((var2 - 0.32) / 0.34) : 0.0;
      double var12 = 1.0 - I((var2 - 0.78) / 0.14);
      double var14 = I((var2 - 1.62) / 0.085) * (1.0 - I((var2 - 1.62 - 0.085) / 0.31));
      double var16 = I((var2 - 2.16) / 0.22) * (1.0 - I((var2 - 6.640000000000001) / 0.3));
      double var18 = Math.max(0.0, var2 - 2.16) / 1.12;
      double var20 = (var18 - Math.floor(var18)) * 6.0;
      int var22 = (int)var20;
      double var23 = I(var20 - var22);
      double var25 = J(I[var22], I[var22 + 1], var23);
      double var27 = J(G[var22], G[var22 + 1], var23);
      double var29 = -var25 * 0.018 * var16;
      double var31 = 0.008
         + 0.043 * var10
         + 0.043 * (var2 >= 0.78 && var2 < 1.62 ? var12 : 0.0)
         + 0.05 * var14
         + 0.007 * var16 * (0.5 + 0.5 * Math.cos(var18 * Math.PI * 4.0));
      double var33 = 0.18 * var10 + (var2 >= 0.78 && var2 < 1.62 ? 0.18 * var12 - 0.09 * var8 : 0.0) + 0.15 * var14;
      double var35 = var2 >= 0.78 && var2 < 1.62 ? 0.05 * var8 : 0.0;
      class_243[] var37 = new class_243[2];

      for (int var38 = 0; var38 < 2; var38++) {
         double var39 = var38 == 0 ? -1.0 : 1.0;
         double var41 = (1.0 + var39 * var25) * 0.5;
         class_243 var43 = new class_243(0.028 + var25 * 0.102 + var39 * 0.017, -0.02, J(0.025, -0.173, var27 * var41));
         double var44 = var2 >= 0.78 && var2 < 1.62 ? var8 : 0.0;
         double var46 = var2 < 0.78 ? var10 : (var2 < 1.62 ? var12 : 0.0);
         class_243 var48 = w[var38].method_1031(var39 * 0.028 * var44, 0.105 * var44, -0.055 * var46 + 0.095 * var44);
         var37[var38] = var48.method_35590(var43, var16);
      }

      return new g4.s(
         new class_243(0.0, 0.42 * var6 + 1.75 * var8, J(-1.48, 0.84, var6)), var31, var29, var33, -var25 * 0.045 * var16, var35, var37[0], var37[1], var16
      );
   }

   private static class_243 C(Matrix4f var0, class_243 var1) {
      Vector3f var2 = var0.transformPosition(new Vector3f((float)var1.field_1352, (float)var1.field_1351, (float)var1.field_1350));
      return new class_243(var2.x, var2.y, var2.z);
   }

   public g4.j a(u8 var1, double var2, class_243 var4) {
      g4.s var5 = k(var2);
      this.j
         .identity()
         .translate((float)(b.field_1352 + var5.hipShift), (float)(b.field_1351 - var5.compression), (float)b.field_1350)
         .rotateY((float)(var5.roll * 2.2))
         .rotateX((float)var5.lean)
         .rotateZ((float)var5.roll)
         .translate((float)(-b.field_1352), (float)(-b.field_1351), (float)(-b.field_1350));
      this.t
         .set(this.j)
         .translate(0.028F, 0.135F, -0.062F)
         .rotateY((float)(-var5.roll * 1.5))
         .rotateZ((float)(-var5.roll * 0.7))
         .rotateX((float)(-var5.lean * 0.55))
         .translate(-0.028F, -0.135F, 0.062F);

      for (int var6 = 0; var6 < 2; var6++) {
         this.P[var6].U(d[var6], X[var6], w[var6], C(this.j, d[var6]), var6 == 0 ? var5.rightHand : var5.leftHand, c);
         class_243 var7 = f[var6].method_1031(var6 == 0 ? 0.011 : -0.011, var5.tuck, -0.007 + var5.tuck * 0.42);
         this.h[var6].U(l[var6], H[var6], f[var6], C(this.j, l[var6]), var7, y);
      }

      float[] var15 = this.g.vertices();

      for (int var16 = 0; var16 < this.Y.length; var16++) {
         int var8 = var16 * 5;
         int var9 = this.Y[var16];
         float var10 = var15[var8];
         float var11 = var15[var8 + 1];
         float var12 = var15[var8 + 2];
         this.Q.set(var10, var11, var12);
         this.V.set(this.Q);
         if (var9 == 0) {
            this.j.transformPosition(this.Q);
            this.t.transformPosition(this.V);
            this.Q.lerp(this.V, (float)I((var11 - 0.105) / 0.07));
         } else {
            int var13 = var9 >= 3 ? 1 : 0;
            g4.v var14 = (var13 != 0 ? this.h : this.P)[(var9 - 1) % 2];
            var14.z.transformPosition(this.Q);
            var14.A.transformPosition(this.V);
            this.Q.lerp(this.V, (float)I(((var13 != 0 ? -0.194 : 0.002) + 0.013 - var11) / 0.026));
            if (var13 != 0) {
               this.V.set(var10, var11, var12);
               var14.l.transformPosition(this.V);
               this.Q.lerp(this.V, (float)I((-0.257 - var11) / 0.018));
            }
         }

         this.O[var8] = this.Q.x;
         this.O[var8 + 1] = this.Q.y;
         this.O[var8 + 2] = this.Q.z;
      }

      double var17 = Math.cos(var1.y);
      double var18 = Math.sin(var1.y);
      class_243 var19 = var1.z
         .method_1031(
            var5.root.field_1352 * var17 - var5.root.field_1350 * var18, var5.root.field_1351, var5.root.field_1352 * var18 + var5.root.field_1350 * var17
         );
      Matrix4f var20 = new Matrix4f()
         .translation((float)(var19.field_1352 - var4.field_1352), (float)(var19.field_1351 - var4.field_1351), (float)(var19.field_1350 - var4.field_1350))
         .rotateY((float)(-var1.y))
         .scale((float)this.k)
         .translate(-0.028F, -this.g.minimumY(), 0.062F);
      double var21 = I(var2 / 0.3) * I((7.5 - var2) / 0.65);
      return new g4.j(
         this.O,
         this.g.colors(),
         var20,
         var21,
         var19,
         M(var20, this.P[0].n, var4),
         M(var20, this.P[1].n, var4),
         M(var20, this.h[0].n, var4),
         M(var20, this.h[1].n, var4)
      );
   }

   private static class_243 M(Matrix4f var0, class_243 var1, class_243 var2) {
      return C(var0, var1).method_1019(var2);
   }

   public static u8.s a(u8 var0, int var1, double var2) {
      double var4 = I((var2 - 0.22) / 0.48);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = u8.z(var1, var0.D) / 16.0 * 0.9375;
      double var10 = (24.0 - u8.E[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.y);
      double var16 = Math.sin(var0.y);
      class_243 var18 = var0.z.method_1031(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = I((var2 - 6.790000000000001) / 0.65);
      double var21 = I((7.5 - var2) / 0.65);
      return new u8.s(var18, -var6, 0.0, 0.0, 1.0, var21, var19, var4 >= 1.0);
   }

   public record j(
      float[] vertices,
      int[] colors,
      Matrix4f transform,
      double opacity,
      class_243 feet,
      class_243 rightHand,
      class_243 leftHand,
      class_243 rightFoot,
      class_243 leftFoot
   ) {
   }

   public record s(
      class_243 root, double compression, double hipShift, double lean, double roll, double tuck, class_243 rightHand, class_243 leftHand, double dance
   ) {
   }

   private static final class v {
      final Matrix4f z = new Matrix4f();
      final Matrix4f A = new Matrix4f();
      final Matrix4f l = new Matrix4f();
      class_243 n = class_243.field_1353;

      void U(class_243 var1, class_243 var2, class_243 var3, class_243 var4, class_243 var5, class_243 var6) {
         double var7 = var1.method_1022(var2);
         double var9 = var2.method_1022(var3);
         class_243 var11 = var5.method_1020(var4);
         double var12 = Math.clamp(var11.method_1033(), 0.001, var7 + var9 - 1.0E-5);
         class_243 var14 = var11.method_1027() < 1.0E-12 ? new class_243(0.0, -1.0, 0.0) : var11.method_1029();
         this.n = var4.method_1019(var14.method_1021(var12));
         class_243 var15 = var6.method_1020(var14.method_1021(var6.method_1026(var14)));
         if (var15.method_1027() < 1.0E-10) {
            var15 = new class_243(1.0, 0.0, 0.0).method_1036(var14);
         }

         var15 = var15.method_1029();
         double var16 = (var7 * var7 - var9 * var9 + var12 * var12) / (2.0 * var12);
         class_243 var18 = var4.method_1019(var14.method_1021(var16)).method_1019(var15.method_1021(Math.sqrt(Math.max(0.0, var7 * var7 - var16 * var16))));
         m(this.z, var1, var2, var4, var18);
         m(this.A, var2, var3, var18, this.n);
         this.l
            .translation(
               (float)(this.n.field_1352 - var3.field_1352), (float)(this.n.field_1351 - var3.field_1351), (float)(this.n.field_1350 - var3.field_1350)
            );
      }

      private static void m(Matrix4f var0, class_243 var1, class_243 var2, class_243 var3, class_243 var4) {
         Vector3f var5 = new Vector3f(
               (float)(var2.field_1352 - var1.field_1352), (float)(var2.field_1351 - var1.field_1351), (float)(var2.field_1350 - var1.field_1350)
            )
            .normalize();
         Vector3f var6 = new Vector3f(
               (float)(var4.field_1352 - var3.field_1352), (float)(var4.field_1351 - var3.field_1351), (float)(var4.field_1350 - var3.field_1350)
            )
            .normalize();
         var0.translation((float)var3.field_1352, (float)var3.field_1351, (float)var3.field_1350)
            .rotate(new Quaternionf().rotationTo(var5, var6))
            .translate((float)(-var1.field_1352), (float)(-var1.field_1351), (float)(-var1.field_1350));
      }
   }
}
