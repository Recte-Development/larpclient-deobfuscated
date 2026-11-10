package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ae {
   public static final double F = 3.6;
   public static final double Q = 0.78;
   public static final double i = 1.62;
   public static final double K = 2.16;
   public static final double D = 1.12;
   public static final double O = 6.640000000000001;
   public static final double m = 7.5;
   public static final double S = 0.42;
   public static final double k = 0.84;
   private static final double J = 0.028;
   private static final double Z = -0.062;
   private static final double e = -0.105;
   private static final Vec3 U = new Vec3(0.028, -0.105, -0.062);
   private static final Vec3[] c = new Vec3[]{new Vec3(-0.031, 0.08, -0.067), new Vec3(0.088, 0.08, -0.067)};
   private static final Vec3[] u = new Vec3[]{new Vec3(-0.038, 0.002, -0.068), new Vec3(0.096, 0.002, -0.068)};
   private static final Vec3[] h = new Vec3[]{new Vec3(-0.027, -0.077, -0.05), new Vec3(0.077, -0.077, -0.05)};
   private static final Vec3[] M = new Vec3[]{new Vec3(0.001, -0.105, -0.062), new Vec3(0.055, -0.105, -0.062)};
   private static final Vec3[] n = new Vec3[]{new Vec3(-0.001, -0.194, -0.067), new Vec3(0.057, -0.194, -0.067)};
   private static final Vec3[] s = new Vec3[]{new Vec3(-0.003, -0.28, -0.068), new Vec3(0.057, -0.28, -0.068)};
   private static final double[] E = new double[]{1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0};
   private static final double[] L = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
   private static final Vec3 j = new Vec3(0.0, 0.0, 1.0);
   private static final Vec3 b = new Vec3(0.0, 0.0, -1.0);
   private final qt.v X;
   private final int[] R;
   private final float[] r;
   private final double y;
   private final Matrix4f v = new Matrix4f();
   private final Matrix4f z = new Matrix4f();
   private final ae.u[] Y = new ae.u[]{new ae.u(), new ae.u()};
   private final ae.u[] w = new ae.u[]{new ae.u(), new ae.u()};
   private final Vector3f l = new Vector3f();
   private final Vector3f T = new Vector3f();

   public ae(qt.v var1) {
      p1.c var2 = p1.q(var1);
      this.X = var2.mesh();
      this.R = var2.joints();
      this.r = (float[])this.X.vertices().clone();
      this.y = 3.6 / var1.height();
   }

   private static double g(double var0) {
      return n5.g(var0);
   }

   private static double o(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public static ae.o k(double var0) {
      double var2 = Math.max(0.0, var0);
      double var4 = Math.clamp((var2 - 0.78) / 0.8400000000000001, 0.0, 1.0);
      double var6 = g(var4);
      double var8 = 4.0 * var4 * (1.0 - var4);
      double var10 = var2 < 0.78 ? g((var2 - 0.32) / 0.34) : 0.0;
      double var12 = 1.0 - g((var2 - 0.78) / 0.14);
      double var14 = g((var2 - 1.62) / 0.085) * (1.0 - g((var2 - 1.62 - 0.085) / 0.31));
      double var16 = g((var2 - 2.16) / 0.22) * (1.0 - g((var2 - 6.640000000000001) / 0.3));
      double var18 = Math.max(0.0, var2 - 2.16) / 1.12;
      double var20 = (var18 - Math.floor(var18)) * 6.0;
      int var22 = (int)var20;
      double var23 = g(var20 - var22);
      double var25 = o(E[var22], E[var22 + 1], var23);
      double var27 = o(L[var22], L[var22 + 1], var23);
      double var29 = -var25 * 0.018 * var16;
      double var31 = 0.008
         + 0.043 * var10
         + 0.043 * (var2 >= 0.78 && var2 < 1.62 ? var12 : 0.0)
         + 0.05 * var14
         + 0.007 * var16 * (0.5 + 0.5 * Math.cos(var18 * Math.PI * 4.0));
      double var33 = 0.18 * var10 + (var2 >= 0.78 && var2 < 1.62 ? 0.18 * var12 - 0.09 * var8 : 0.0) + 0.15 * var14;
      double var35 = var2 >= 0.78 && var2 < 1.62 ? 0.05 * var8 : 0.0;
      Vec3[] var37 = new Vec3[2];

      for (int var38 = 0; var38 < 2; var38++) {
         double var39 = var38 == 0 ? -1.0 : 1.0;
         double var41 = (1.0 + var39 * var25) * 0.5;
         Vec3 var43 = new Vec3(0.028 + var25 * 0.102 + var39 * 0.017, -0.02, o(0.025, -0.173, var27 * var41));
         double var44 = var2 >= 0.78 && var2 < 1.62 ? var8 : 0.0;
         double var46 = var2 < 0.78 ? var10 : (var2 < 1.62 ? var12 : 0.0);
         Vec3 var48 = h[var38].add(var39 * 0.028 * var44, 0.105 * var44, -0.055 * var46 + 0.095 * var44);
         var37[var38] = var48.lerp(var43, var16);
      }

      return new ae.o(
         new Vec3(0.0, 0.42 * var6 + 1.75 * var8, o(-1.48, 0.84, var6)), var31, var29, var33, -var25 * 0.045 * var16, var35, var37[0], var37[1], var16
      );
   }

   private static Vec3 u(Matrix4f var0, Vec3 var1) {
      Vector3f var2 = var0.transformPosition(new Vector3f((float)var1.x, (float)var1.y, (float)var1.z));
      return new Vec3(var2.x, var2.y, var2.z);
   }

   public ae.f n(n5 var1, double var2, Vec3 var4) {
      ae.o var5 = k(var2);
      this.v
         .identity()
         .translate((float)(U.x + var5.hipShift), (float)(U.y - var5.compression), (float)U.z)
         .rotateY((float)(var5.roll * 2.2))
         .rotateX((float)var5.lean)
         .rotateZ((float)var5.roll)
         .translate((float)(-U.x), (float)(-U.y), (float)(-U.z));
      this.z
         .set(this.v)
         .translate(0.028F, 0.135F, -0.062F)
         .rotateY((float)(-var5.roll * 1.5))
         .rotateZ((float)(-var5.roll * 0.7))
         .rotateX((float)(-var5.lean * 0.55))
         .translate(-0.028F, -0.135F, 0.062F);

      for (int var6 = 0; var6 < 2; var6++) {
         this.Y[var6].f(c[var6], u[var6], h[var6], u(this.v, c[var6]), var6 == 0 ? var5.rightHand : var5.leftHand, b);
         Vec3 var7 = s[var6].add(var6 == 0 ? 0.011 : -0.011, var5.tuck, -0.007 + var5.tuck * 0.42);
         this.w[var6].f(M[var6], n[var6], s[var6], u(this.v, M[var6]), var7, j);
      }

      float[] var15 = this.X.vertices();

      for (int var16 = 0; var16 < this.R.length; var16++) {
         int var8 = var16 * 5;
         int var9 = this.R[var16];
         float var10 = var15[var8];
         float var11 = var15[var8 + 1];
         float var12 = var15[var8 + 2];
         this.l.set(var10, var11, var12);
         this.T.set(this.l);
         if (var9 == 0) {
            this.v.transformPosition(this.l);
            this.z.transformPosition(this.T);
            this.l.lerp(this.T, (float)g((var11 - 0.105) / 0.07));
         } else {
            int var13 = var9 >= 3 ? 1 : 0;
            ae.u var14 = (var13 != 0 ? this.w : this.Y)[(var9 - 1) % 2];
            var14.n.transformPosition(this.l);
            var14.Q.transformPosition(this.T);
            this.l.lerp(this.T, (float)g(((var13 != 0 ? -0.194 : 0.002) + 0.013 - var11) / 0.026));
            if (var13 != 0) {
               this.T.set(var10, var11, var12);
               var14.A.transformPosition(this.T);
               this.l.lerp(this.T, (float)g((-0.257 - var11) / 0.018));
            }
         }

         this.r[var8] = this.l.x;
         this.r[var8 + 1] = this.l.y;
         this.r[var8 + 2] = this.l.z;
      }

      double var17 = Math.cos(var1.y);
      double var18 = Math.sin(var1.y);
      Vec3 var19 = var1.r.add(var5.root.x * var17 - var5.root.z * var18, var5.root.y, var5.root.x * var18 + var5.root.z * var17);
      Matrix4f var20 = new Matrix4f()
         .translation((float)(var19.x - var4.x), (float)(var19.y - var4.y), (float)(var19.z - var4.z))
         .rotateY((float)(-var1.y))
         .scale((float)this.y)
         .translate(-0.028F, -this.X.minimumY(), 0.062F);
      double var21 = g(var2 / 0.3) * g((7.5 - var2) / 0.65);
      return new ae.f(
         this.r,
         this.X.colors(),
         var20,
         var21,
         var19,
         f(var20, this.Y[0].S, var4),
         f(var20, this.Y[1].S, var4),
         f(var20, this.w[0].S, var4),
         f(var20, this.w[1].S, var4)
      );
   }

   private static Vec3 f(Matrix4f var0, Vec3 var1, Vec3 var2) {
      return u(var0, var1).add(var2);
   }

   public static n5.p B(n5 var0, int var1, double var2) {
      double var4 = g((var2 - 0.22) / 0.48);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = n5.H(var1, var0.Q) / 16.0 * 0.9375;
      double var10 = (24.0 - n5.a[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.y);
      double var16 = Math.sin(var0.y);
      Vec3 var18 = var0.r.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = g((var2 - 6.790000000000001) / 0.65);
      double var21 = g((7.5 - var2) / 0.65);
      return new n5.p(var18, -var6, 0.0, 0.0, 1.0, var21, var19, var4 >= 1.0);
   }

   public record f(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 rightHand, Vec3 leftHand, Vec3 rightFoot, Vec3 leftFoot) {
   }

   public record o(Vec3 root, double compression, double hipShift, double lean, double roll, double tuck, Vec3 rightHand, Vec3 leftHand, double dance) {
   }

   private static final class u {
      final Matrix4f n = new Matrix4f();
      final Matrix4f Q = new Matrix4f();
      final Matrix4f A = new Matrix4f();
      Vec3 S = Vec3.ZERO;

      void f(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, Vec3 var6) {
         double var7 = var1.distanceTo(var2);
         double var9 = var2.distanceTo(var3);
         Vec3 var11 = var5.subtract(var4);
         double var12 = Math.clamp(var11.length(), 0.001, var7 + var9 - 1.0E-5);
         Vec3 var14 = var11.lengthSqr() < 1.0E-12 ? new Vec3(0.0, -1.0, 0.0) : var11.normalize();
         this.S = var4.add(var14.scale(var12));
         Vec3 var15 = var6.subtract(var14.scale(var6.dot(var14)));
         if (var15.lengthSqr() < 1.0E-10) {
            var15 = new Vec3(1.0, 0.0, 0.0).cross(var14);
         }

         var15 = var15.normalize();
         double var16 = (var7 * var7 - var9 * var9 + var12 * var12) / (2.0 * var12);
         Vec3 var18 = var4.add(var14.scale(var16)).add(var15.scale(Math.sqrt(Math.max(0.0, var7 * var7 - var16 * var16))));
         N(this.n, var1, var2, var4, var18);
         N(this.Q, var2, var3, var18, this.S);
         this.A.translation((float)(this.S.x - var3.x), (float)(this.S.y - var3.y), (float)(this.S.z - var3.z));
      }

      private static void N(Matrix4f var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
         Vector3f var5 = new Vector3f((float)(var2.x - var1.x), (float)(var2.y - var1.y), (float)(var2.z - var1.z)).normalize();
         Vector3f var6 = new Vector3f((float)(var4.x - var3.x), (float)(var4.y - var3.y), (float)(var4.z - var3.z)).normalize();
         var0.translation((float)var3.x, (float)var3.y, (float)var3.z)
            .rotate(new Quaternionf().rotationTo(var5, var6))
            .translate((float)(-var1.x), (float)(-var1.y), (float)(-var1.z));
      }
   }
}
