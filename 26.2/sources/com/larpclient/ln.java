package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ln {
   public static final double c = 3.6;
   public static final double l = 1.56;
   public static final double S = 1.28;
   public static final double d = 1.12;
   public static final Vec3 E = new Vec3(-0.031, 0.08, -0.067);
   public static final Vec3 o = new Vec3(-0.02649, -0.064244, -0.046604);
   public static final Vec3 y = new Vec3(-0.088521, -0.283297, 0.098837);
   private static final double b = -0.105;
   private static final double W = 1.65;
   private final ka.u X;
   private final float[] k;
   private final float[] v;
   private final float[] P;
   private final double G;
   private final double t;
   private final Vec3 R;
   private final Matrix4f F = new Matrix4f();
   private final Matrix4f Z = new Matrix4f();
   private final Vector3f T = new Vector3f();

   public ln(ka.u var1) {
      b2.h var2 = b2.v(var1);
      this.X = var2.mesh();
      this.k = (float[])this.X.vertices().clone();
      this.v = new float[this.X.colors().length];
      this.P = new float[this.v.length];
      this.G = 3.6 / var1.height();

      for (int var3 = 0; var3 < this.v.length; var3++) {
         double var4 = this.k[var3 * 5 + 1];
         this.v[var3] = var2.arm()[var3] ? 1.0F : 0.0F;
         this.P[var3] = (float)Math.max(this.v[var3], pj.Z((var4 + 0.17) / 0.1));
      }

      double var10 = -1.5;
      double var5 = 0.0;

      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (var10 + var5) * 0.5;
         if (this.g(var8) > 1.65) {
            var10 = var8;
         } else {
            var5 = var8;
         }
      }

      this.t = (var10 + var5) * 0.5;
      Vec3 var11 = this.l(y, 1.56);
      this.R = new Vec3(-var11.x * this.G, 0.0, -var11.z * this.G);
   }

   private double g(double var1) {
      this.K(var1, K(1.56), 0.0);
      this.T.set((float)y.x, (float)y.y, (float)y.z);
      this.F.transformPosition(this.T);
      this.Z.transformPosition(this.T);
      return (this.T.y - this.X.minimumY()) * this.G;
   }

   private void K(double var1, double var3, double var5) {
      this.F
         .identity()
         .translate((float)E.x, (float)E.y, (float)E.z)
         .rotateY((float)var5)
         .rotateX((float)var1)
         .translate((float)(-E.x), (float)(-E.y), (float)(-E.z));
      this.Z.identity().translate(0.0F, -0.105F, 0.0F).rotateX((float)var3).translate(0.0F, 0.105F, 0.0F);
   }

   public double t(double var1) {
      if (var1 < 1.28) {
         return -2.65 * pj.Z((var1 - 0.36) / 0.7600000000000001);
      } else if (var1 < 1.56) {
         double var3 = Math.clamp((var1 - 1.28) / 0.28, 0.0, 1.0);
         return -2.65 + (this.t + 2.65) * var3 * var3;
      } else if (var1 < 1.6800000000000002) {
         return this.t + 0.13 * pj.Z((var1 - 1.56) / 0.12);
      } else {
         return var1 < 2.12 ? this.t + 0.13 - 0.27 * pj.Z((var1 - 1.56 - 0.12) / 0.44000000000000006) : (this.t - 0.14) * (1.0 - pj.Z((var1 - 2.35) / 0.8));
      }
   }

   private static double d(double var0) {
      return 0.7 * Math.sin(Math.PI * pj.Z((var0 - 0.36) / 0.7600000000000001));
   }

   public static double K(double var0) {
      double var2 = -0.055 * pj.Z((var0 - 0.36) / 0.7600000000000001);
      if (var0 < 1.28) {
         return var2;
      } else if (var0 < 1.56) {
         return -0.055 + 0.235 * pj.Z((var0 - 1.28) / 0.28);
      } else {
         return var0 < 2.12 ? 0.18 - 0.12 * pj.Z((var0 - 1.56) / 0.56) : 0.06 * (1.0 - pj.Z((var0 - 2.35) / 0.8));
      }
   }

   private Vec3 l(Vec3 var1, double var2) {
      this.K(this.t(var2), K(var2), d(var2));
      this.T.set((float)var1.x, (float)var1.y, (float)var1.z);
      this.F.transformPosition(this.T);
      this.Z.transformPosition(this.T);
      return new Vec3(this.T.x, this.T.y, this.T.z);
   }

   private Vec3 f(pj var1, Vec3 var2) {
      Vec3 var3 = var2.add(0.0, -this.X.minimumY(), 0.0).scale(this.G).add(this.R);
      double var4 = Math.cos(var1.z);
      double var6 = Math.sin(var1.z);
      return var1.v.add(var3.x * var4 - var3.z * var6, var3.y, var3.x * var6 + var3.z * var4);
   }

   public Vec3 f(pj var1, double var2) {
      return this.f(var1, this.l(y, var2));
   }

   public Vec3 v(pj var1, double var2) {
      return this.f(var1, this.l(o, var2));
   }

   public ln.v j(pj var1, double var2, Vec3 var4) {
      this.K(this.t(var2), K(var2), d(var2));
      float[] var5 = this.X.vertices();

      for (int var6 = 0; var6 < this.v.length; var6++) {
         int var7 = var6 * 5;
         float var8 = var5[var7];
         float var9 = var5[var7 + 1];
         float var10 = var5[var7 + 2];
         this.T.set(var8, var9, var10);
         this.F.transformPosition(this.T);
         float var11 = this.v[var6];
         float var12 = this.P[var6];
         float var13 = var8 + (this.T.x - var8) * var11;
         float var14 = var9 + (this.T.y - var9) * var11;
         float var15 = var10 + (this.T.z - var10) * var11;
         this.T.set(var13, var14, var15);
         this.Z.transformPosition(this.T);
         this.k[var7] = var13 + (this.T.x - var13) * var12;
         this.k[var7 + 1] = var14 + (this.T.y - var14) * var12;
         this.k[var7 + 2] = var15 + (this.T.z - var15) * var12;
      }

      double var16 = Math.cos(var1.z);
      double var17 = Math.sin(var1.z);
      Vec3 var18 = var1.v.add(this.R.x * var16 - this.R.z * var17, 0.0, this.R.x * var17 + this.R.z * var16);
      Matrix4f var19 = new Matrix4f()
         .translation((float)(var18.x - var4.x), (float)(var18.y - var4.y), (float)(var18.z - var4.z))
         .rotateY((float)(-var1.z))
         .scale((float)this.G)
         .translate(0.0F, -this.X.minimumY(), 0.0F);
      Vec3[] var20 = new Vec3[14];

      for (int var21 = 0; var21 < var20.length; var21++) {
         var20[var21] = this.f(var1, Math.max(1.28, var2 - 0.13 + var21 * 0.13 / (var20.length - 1)));
      }

      double var22 = pj.Z(var2 / 0.42) * pj.Z((4.6 - var2) / 1.15);
      return new ln.v(this.k, this.X.colors(), var19, var22, var18, this.v(var1, var2), this.f(var1, var2), var20);
   }

   public static pj.n a(pj var0, int var1, double var2) {
      double var4 = pj.Z((var2 - 1.56) / 0.28);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = pj.R(var1, var0.l) / 16.0 * 0.9375;
      double var10 = (24.0 - pj.S[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.z);
      double var16 = Math.sin(var0.z);
      Vec3 var18 = var0.v.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = pj.Z((3.65 - var2) / 0.9);
      double var21 = pj.Z((var2 - 2.6) / 1.05);
      return new pj.n(var18, -var6, 0.0, 0.0, 1.0, var19, var21, var4 >= 1.0);
   }

   public record v(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 grip, Vec3 tip, Vec3[] trail) {
   }
}
