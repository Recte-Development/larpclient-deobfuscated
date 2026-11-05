package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class l9 {
   public static final double w = 3.6;
   public static final double A = 1.56;
   public static final double T = 1.28;
   public static final double U = 1.12;
   public static final Vec3 q = new Vec3(-0.031, 0.08, -0.067);
   public static final Vec3 x = new Vec3(-0.02649, -0.064244, -0.046604);
   public static final Vec3 y = new Vec3(-0.088521, -0.283297, 0.098837);
   private static final double i = -0.105;
   private static final double H = 1.65;
   private final ed.q G;
   private final float[] P;
   private final float[] N;
   private final float[] W;
   private final double C;
   private final double o;
   private final Vec3 R;
   private final Matrix4f u = new Matrix4f();
   private final Matrix4f p = new Matrix4f();
   private final Vector3f k = new Vector3f();

   public l9(ed.q var1) {
      dt.q var2 = dt.q(var1);
      this.G = var2.mesh();
      this.P = (float[])this.G.vertices().clone();
      this.N = new float[this.G.colors().length];
      this.W = new float[this.N.length];
      this.C = 3.6 / var1.height();

      for (int var3 = 0; var3 < this.N.length; var3++) {
         double var4 = this.P[var3 * 5 + 1];
         this.N[var3] = var2.arm()[var3] ? 1.0F : 0.0F;
         this.W[var3] = (float)Math.max(this.N[var3], ez.N((var4 + 0.17) / 0.1));
      }

      double var10 = -1.5;
      double var5 = 0.0;

      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (var10 + var5) * 0.5;
         if (this.B(var8) > 1.65) {
            var10 = var8;
         } else {
            var5 = var8;
         }
      }

      this.o = (var10 + var5) * 0.5;
      Vec3 var11 = this.C(y, 1.56);
      this.R = new Vec3(-var11.x * this.C, 0.0, -var11.z * this.C);
   }

   private double B(double var1) {
      this.U(var1, T(1.56), 0.0);
      this.k.set((float)y.x, (float)y.y, (float)y.z);
      this.u.transformPosition(this.k);
      this.p.transformPosition(this.k);
      return (this.k.y - this.G.minimumY()) * this.C;
   }

   private void U(double var1, double var3, double var5) {
      this.u
         .identity()
         .translate((float)q.x, (float)q.y, (float)q.z)
         .rotateY((float)var5)
         .rotateX((float)var1)
         .translate((float)(-q.x), (float)(-q.y), (float)(-q.z));
      this.p.identity().translate(0.0F, -0.105F, 0.0F).rotateX((float)var3).translate(0.0F, 0.105F, 0.0F);
   }

   public double d(double var1) {
      if (var1 < 1.28) {
         return -2.65 * ez.N((var1 - 0.36) / 0.7600000000000001);
      } else if (var1 < 1.56) {
         double var3 = Math.clamp((var1 - 1.28) / 0.28, 0.0, 1.0);
         return -2.65 + (this.o + 2.65) * var3 * var3;
      } else if (var1 < 1.6800000000000002) {
         return this.o + 0.13 * ez.N((var1 - 1.56) / 0.12);
      } else {
         return var1 < 2.12 ? this.o + 0.13 - 0.27 * ez.N((var1 - 1.56 - 0.12) / 0.44000000000000006) : (this.o - 0.14) * (1.0 - ez.N((var1 - 2.35) / 0.8));
      }
   }

   private static double f(double var0) {
      return 0.7 * Math.sin(Math.PI * ez.N((var0 - 0.36) / 0.7600000000000001));
   }

   public static double T(double var0) {
      double var2 = -0.055 * ez.N((var0 - 0.36) / 0.7600000000000001);
      if (var0 < 1.28) {
         return var2;
      } else if (var0 < 1.56) {
         return -0.055 + 0.235 * ez.N((var0 - 1.28) / 0.28);
      } else {
         return var0 < 2.12 ? 0.18 - 0.12 * ez.N((var0 - 1.56) / 0.56) : 0.06 * (1.0 - ez.N((var0 - 2.35) / 0.8));
      }
   }

   private Vec3 C(Vec3 var1, double var2) {
      this.U(this.d(var2), T(var2), f(var2));
      this.k.set((float)var1.x, (float)var1.y, (float)var1.z);
      this.u.transformPosition(this.k);
      this.p.transformPosition(this.k);
      return new Vec3(this.k.x, this.k.y, this.k.z);
   }

   private Vec3 Q(ez var1, Vec3 var2) {
      Vec3 var3 = var2.add(0.0, -this.G.minimumY(), 0.0).scale(this.C).add(this.R);
      double var4 = Math.cos(var1.A);
      double var6 = Math.sin(var1.A);
      return var1.D.add(var3.x * var4 - var3.z * var6, var3.y, var3.x * var6 + var3.z * var4);
   }

   public Vec3 z(ez var1, double var2) {
      return this.Q(var1, this.C(y, var2));
   }

   public Vec3 R(ez var1, double var2) {
      return this.Q(var1, this.C(x, var2));
   }

   public l9.p Q(ez var1, double var2, Vec3 var4) {
      this.U(this.d(var2), T(var2), f(var2));
      float[] var5 = this.G.vertices();

      for (int var6 = 0; var6 < this.N.length; var6++) {
         int var7 = var6 * 5;
         float var8 = var5[var7];
         float var9 = var5[var7 + 1];
         float var10 = var5[var7 + 2];
         this.k.set(var8, var9, var10);
         this.u.transformPosition(this.k);
         float var11 = this.N[var6];
         float var12 = this.W[var6];
         float var13 = var8 + (this.k.x - var8) * var11;
         float var14 = var9 + (this.k.y - var9) * var11;
         float var15 = var10 + (this.k.z - var10) * var11;
         this.k.set(var13, var14, var15);
         this.p.transformPosition(this.k);
         this.P[var7] = var13 + (this.k.x - var13) * var12;
         this.P[var7 + 1] = var14 + (this.k.y - var14) * var12;
         this.P[var7 + 2] = var15 + (this.k.z - var15) * var12;
      }

      double var16 = Math.cos(var1.A);
      double var17 = Math.sin(var1.A);
      Vec3 var18 = var1.D.add(this.R.x * var16 - this.R.z * var17, 0.0, this.R.x * var17 + this.R.z * var16);
      Matrix4f var19 = new Matrix4f()
         .translation((float)(var18.x - var4.x), (float)(var18.y - var4.y), (float)(var18.z - var4.z))
         .rotateY((float)(-var1.A))
         .scale((float)this.C)
         .translate(0.0F, -this.G.minimumY(), 0.0F);
      Vec3[] var20 = new Vec3[14];

      for (int var21 = 0; var21 < var20.length; var21++) {
         var20[var21] = this.z(var1, Math.max(1.28, var2 - 0.13 + var21 * 0.13 / (var20.length - 1)));
      }

      double var22 = ez.N(var2 / 0.42) * ez.N((4.6 - var2) / 1.15);
      return new l9.p(this.P, this.G.colors(), var19, var22, var18, this.R(var1, var2), this.z(var1, var2), var20);
   }

   public static ez.q Y(ez var0, int var1, double var2) {
      double var4 = ez.N((var2 - 1.56) / 0.28);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = ez.k(var1, var0.d) / 16.0 * 0.9375;
      double var10 = (24.0 - ez.X[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.A);
      double var16 = Math.sin(var0.A);
      Vec3 var18 = var0.D.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = ez.N((3.65 - var2) / 0.9);
      double var21 = ez.N((var2 - 2.6) / 1.05);
      return new ez.q(var18, -var6, 0.0, 0.0, 1.0, var19, var21, var4 >= 1.0);
   }

   public record p(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 grip, Vec3 tip, Vec3[] trail) {
   }
}
