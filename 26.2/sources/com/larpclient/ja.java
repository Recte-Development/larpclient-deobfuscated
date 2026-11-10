package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ja {
   public static final double m = 3.6;
   public static final double o = 0.78;
   public static final double Q = 1.62;
   public static final double y = 2.16;
   public static final double i = 1.12;
   public static final double U = 6.640000000000001;
   public static final double J = 7.5;
   public static final double n = 0.42;
   public static final double f = 0.84;
   private static final double t = 0.028;
   private static final double v = -0.062;
   private static final double N = -0.105;
   private static final Vec3 r = new Vec3(0.028, -0.105, -0.062);
   private static final Vec3[] K = new Vec3[]{new Vec3(-0.031, 0.08, -0.067), new Vec3(0.088, 0.08, -0.067)};
   private static final Vec3[] z = new Vec3[]{new Vec3(-0.038, 0.002, -0.068), new Vec3(0.096, 0.002, -0.068)};
   private static final Vec3[] E = new Vec3[]{new Vec3(-0.027, -0.077, -0.05), new Vec3(0.077, -0.077, -0.05)};
   private static final Vec3[] Z = new Vec3[]{new Vec3(0.001, -0.105, -0.062), new Vec3(0.055, -0.105, -0.062)};
   private static final Vec3[] w = new Vec3[]{new Vec3(-0.001, -0.194, -0.067), new Vec3(0.057, -0.194, -0.067)};
   private static final Vec3[] X = new Vec3[]{new Vec3(-0.003, -0.28, -0.068), new Vec3(0.057, -0.28, -0.068)};
   private static final double[] b = new double[]{1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0};
   private static final double[] V = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
   private static final Vec3 c = new Vec3(0.0, 0.0, 1.0);
   private static final Vec3 p = new Vec3(0.0, 0.0, -1.0);
   private final ka.u x;
   private final int[] k;
   private final float[] h;
   private final double W;
   private final Matrix4f L = new Matrix4f();
   private final Matrix4f S = new Matrix4f();
   private final ja.p[] s = new ja.p[]{new ja.p(), new ja.p()};
   private final ja.p[] D = new ja.p[]{new ja.p(), new ja.p()};
   private final Vector3f O = new Vector3f();
   private final Vector3f g = new Vector3f();

   public ja(ka.u var1) {
      b2.r var2 = b2.U(var1);
      this.x = var2.mesh();
      this.k = var2.joints();
      this.h = (float[])this.x.vertices().clone();
      this.W = 3.6 / var1.height();
   }

   private static double Y(double var0) {
      return pj.Z(var0);
   }

   private static double v(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public static ja.e e(double var0) {
      double var2 = Math.max(0.0, var0);
      double var4 = Math.clamp((var2 - 0.78) / 0.8400000000000001, 0.0, 1.0);
      double var6 = Y(var4);
      double var8 = 4.0 * var4 * (1.0 - var4);
      double var10 = var2 < 0.78 ? Y((var2 - 0.32) / 0.34) : 0.0;
      double var12 = 1.0 - Y((var2 - 0.78) / 0.14);
      double var14 = Y((var2 - 1.62) / 0.085) * (1.0 - Y((var2 - 1.62 - 0.085) / 0.31));
      double var16 = Y((var2 - 2.16) / 0.22) * (1.0 - Y((var2 - 6.640000000000001) / 0.3));
      double var18 = Math.max(0.0, var2 - 2.16) / 1.12;
      double var20 = (var18 - Math.floor(var18)) * 6.0;
      int var22 = (int)var20;
      double var23 = Y(var20 - var22);
      double var25 = v(b[var22], b[var22 + 1], var23);
      double var27 = v(V[var22], V[var22 + 1], var23);
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
         Vec3 var43 = new Vec3(0.028 + var25 * 0.102 + var39 * 0.017, -0.02, v(0.025, -0.173, var27 * var41));
         double var44 = var2 >= 0.78 && var2 < 1.62 ? var8 : 0.0;
         double var46 = var2 < 0.78 ? var10 : (var2 < 1.62 ? var12 : 0.0);
         Vec3 var48 = E[var38].add(var39 * 0.028 * var44, 0.105 * var44, -0.055 * var46 + 0.095 * var44);
         var37[var38] = var48.lerp(var43, var16);
      }

      return new ja.e(
         new Vec3(0.0, 0.42 * var6 + 1.75 * var8, v(-1.48, 0.84, var6)), var31, var29, var33, -var25 * 0.045 * var16, var35, var37[0], var37[1], var16
      );
   }

   private static Vec3 s(Matrix4f var0, Vec3 var1) {
      Vector3f var2 = var0.transformPosition(new Vector3f((float)var1.x, (float)var1.y, (float)var1.z));
      return new Vec3(var2.x, var2.y, var2.z);
   }

   public ja.w V(pj var1, double var2, Vec3 var4) {
      ja.e var5 = e(var2);
      this.L
         .identity()
         .translate((float)(r.x + var5.hipShift), (float)(r.y - var5.compression), (float)r.z)
         .rotateY((float)(var5.roll * 2.2))
         .rotateX((float)var5.lean)
         .rotateZ((float)var5.roll)
         .translate((float)(-r.x), (float)(-r.y), (float)(-r.z));
      this.S
         .set(this.L)
         .translate(0.028F, 0.135F, -0.062F)
         .rotateY((float)(-var5.roll * 1.5))
         .rotateZ((float)(-var5.roll * 0.7))
         .rotateX((float)(-var5.lean * 0.55))
         .translate(-0.028F, -0.135F, 0.062F);

      for (int var6 = 0; var6 < 2; var6++) {
         this.s[var6].i(K[var6], z[var6], E[var6], s(this.L, K[var6]), var6 == 0 ? var5.rightHand : var5.leftHand, p);
         Vec3 var7 = X[var6].add(var6 == 0 ? 0.011 : -0.011, var5.tuck, -0.007 + var5.tuck * 0.42);
         this.D[var6].i(Z[var6], w[var6], X[var6], s(this.L, Z[var6]), var7, c);
      }

      float[] var15 = this.x.vertices();

      for (int var16 = 0; var16 < this.k.length; var16++) {
         int var8 = var16 * 5;
         int var9 = this.k[var16];
         float var10 = var15[var8];
         float var11 = var15[var8 + 1];
         float var12 = var15[var8 + 2];
         this.O.set(var10, var11, var12);
         this.g.set(this.O);
         if (var9 == 0) {
            this.L.transformPosition(this.O);
            this.S.transformPosition(this.g);
            this.O.lerp(this.g, (float)Y((var11 - 0.105) / 0.07));
         } else {
            int var13 = var9 >= 3 ? 1 : 0;
            ja.p var14 = (var13 != 0 ? this.D : this.s)[(var9 - 1) % 2];
            var14.a.transformPosition(this.O);
            var14.f.transformPosition(this.g);
            this.O.lerp(this.g, (float)Y(((var13 != 0 ? -0.194 : 0.002) + 0.013 - var11) / 0.026));
            if (var13 != 0) {
               this.g.set(var10, var11, var12);
               var14.u.transformPosition(this.g);
               this.O.lerp(this.g, (float)Y((-0.257 - var11) / 0.018));
            }
         }

         this.h[var8] = this.O.x;
         this.h[var8 + 1] = this.O.y;
         this.h[var8 + 2] = this.O.z;
      }

      double var17 = Math.cos(var1.z);
      double var18 = Math.sin(var1.z);
      Vec3 var19 = var1.v.add(var5.root.x * var17 - var5.root.z * var18, var5.root.y, var5.root.x * var18 + var5.root.z * var17);
      Matrix4f var20 = new Matrix4f()
         .translation((float)(var19.x - var4.x), (float)(var19.y - var4.y), (float)(var19.z - var4.z))
         .rotateY((float)(-var1.z))
         .scale((float)this.W)
         .translate(-0.028F, -this.x.minimumY(), 0.062F);
      double var21 = Y(var2 / 0.3) * Y((7.5 - var2) / 0.65);
      return new ja.w(
         this.h,
         this.x.colors(),
         var20,
         var21,
         var19,
         J(var20, this.s[0].L, var4),
         J(var20, this.s[1].L, var4),
         J(var20, this.D[0].L, var4),
         J(var20, this.D[1].L, var4)
      );
   }

   private static Vec3 J(Matrix4f var0, Vec3 var1, Vec3 var2) {
      return s(var0, var1).add(var2);
   }

   public static pj.n k(pj var0, int var1, double var2) {
      double var4 = Y((var2 - 0.22) / 0.48);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = pj.R(var1, var0.l) / 16.0 * 0.9375;
      double var10 = (24.0 - pj.S[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.z);
      double var16 = Math.sin(var0.z);
      Vec3 var18 = var0.v.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = Y((var2 - 6.790000000000001) / 0.65);
      double var21 = Y((7.5 - var2) / 0.65);
      return new pj.n(var18, -var6, 0.0, 0.0, 1.0, var21, var19, var4 >= 1.0);
   }

   public record e(Vec3 root, double compression, double hipShift, double lean, double roll, double tuck, Vec3 rightHand, Vec3 leftHand, double dance) {
   }

   private static final class p {
      final Matrix4f a = new Matrix4f();
      final Matrix4f f = new Matrix4f();
      final Matrix4f u = new Matrix4f();
      Vec3 L = Vec3.ZERO;

      void i(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, Vec3 var6) {
         double var7 = var1.distanceTo(var2);
         double var9 = var2.distanceTo(var3);
         Vec3 var11 = var5.subtract(var4);
         double var12 = Math.clamp(var11.length(), 0.001, var7 + var9 - 1.0E-5);
         Vec3 var14 = var11.lengthSqr() < 1.0E-12 ? new Vec3(0.0, -1.0, 0.0) : var11.normalize();
         this.L = var4.add(var14.scale(var12));
         Vec3 var15 = var6.subtract(var14.scale(var6.dot(var14)));
         if (var15.lengthSqr() < 1.0E-10) {
            var15 = new Vec3(1.0, 0.0, 0.0).cross(var14);
         }

         var15 = var15.normalize();
         double var16 = (var7 * var7 - var9 * var9 + var12 * var12) / (2.0 * var12);
         Vec3 var18 = var4.add(var14.scale(var16)).add(var15.scale(Math.sqrt(Math.max(0.0, var7 * var7 - var16 * var16))));
         Z(this.a, var1, var2, var4, var18);
         Z(this.f, var2, var3, var18, this.L);
         this.u.translation((float)(this.L.x - var3.x), (float)(this.L.y - var3.y), (float)(this.L.z - var3.z));
      }

      private static void Z(Matrix4f var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
         Vector3f var5 = new Vector3f((float)(var2.x - var1.x), (float)(var2.y - var1.y), (float)(var2.z - var1.z)).normalize();
         Vector3f var6 = new Vector3f((float)(var4.x - var3.x), (float)(var4.y - var3.y), (float)(var4.z - var3.z)).normalize();
         var0.translation((float)var3.x, (float)var3.y, (float)var3.z)
            .rotate(new Quaternionf().rotationTo(var5, var6))
            .translate((float)(-var1.x), (float)(-var1.y), (float)(-var1.z));
      }
   }

   public record w(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 rightHand, Vec3 leftHand, Vec3 rightFoot, Vec3 leftFoot) {
   }
}
