package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class sr {
   public static final double U = 3.6;
   public static final double n = 1.56;
   public static final double G = 1.28;
   public static final double e = 1.12;
   public static final Vec3 v = new Vec3(-0.031, 0.08, -0.067);
   public static final Vec3 Z = new Vec3(-0.02649, -0.064244, -0.046604);
   public static final Vec3 L = new Vec3(-0.088521, -0.283297, 0.098837);
   private static final double a = -0.105;
   private static final double m = 1.65;
   private final qt.v b;
   private final float[] M;
   private final float[] d;
   private final float[] p;
   private final double E;
   private final double K;
   private final Vec3 I;
   private final Matrix4f t = new Matrix4f();
   private final Matrix4f l = new Matrix4f();
   private final Vector3f V = new Vector3f();

   public sr(qt.v var1) {
      p1.o var2 = p1.v(var1);
      this.b = var2.mesh();
      this.M = (float[])this.b.vertices().clone();
      this.d = new float[this.b.colors().length];
      this.p = new float[this.d.length];
      this.E = 3.6 / var1.height();

      for (int var3 = 0; var3 < this.d.length; var3++) {
         double var4 = this.M[var3 * 5 + 1];
         this.d[var3] = var2.arm()[var3] ? 1.0F : 0.0F;
         this.p[var3] = (float)Math.max(this.d[var3], n5.g((var4 + 0.17) / 0.1));
      }

      double var10 = -1.5;
      double var5 = 0.0;

      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (var10 + var5) * 0.5;
         if (this.e(var8) > 1.65) {
            var10 = var8;
         } else {
            var5 = var8;
         }
      }

      this.K = (var10 + var5) * 0.5;
      Vec3 var11 = this.D(L, 1.56);
      this.I = new Vec3(-var11.x * this.E, 0.0, -var11.z * this.E);
   }

   private double e(double var1) {
      this.x(var1, Z(1.56), 0.0);
      this.V.set((float)L.x, (float)L.y, (float)L.z);
      this.t.transformPosition(this.V);
      this.l.transformPosition(this.V);
      return (this.V.y - this.b.minimumY()) * this.E;
   }

   private void x(double var1, double var3, double var5) {
      this.t
         .identity()
         .translate((float)v.x, (float)v.y, (float)v.z)
         .rotateY((float)var5)
         .rotateX((float)var1)
         .translate((float)(-v.x), (float)(-v.y), (float)(-v.z));
      this.l.identity().translate(0.0F, -0.105F, 0.0F).rotateX((float)var3).translate(0.0F, 0.105F, 0.0F);
   }

   public double l(double var1) {
      if (var1 < 1.28) {
         return -2.65 * n5.g((var1 - 0.36) / 0.7600000000000001);
      } else if (var1 < 1.56) {
         double var3 = Math.clamp((var1 - 1.28) / 0.28, 0.0, 1.0);
         return -2.65 + (this.K + 2.65) * var3 * var3;
      } else if (var1 < 1.6800000000000002) {
         return this.K + 0.13 * n5.g((var1 - 1.56) / 0.12);
      } else {
         return var1 < 2.12 ? this.K + 0.13 - 0.27 * n5.g((var1 - 1.56 - 0.12) / 0.44000000000000006) : (this.K - 0.14) * (1.0 - n5.g((var1 - 2.35) / 0.8));
      }
   }

   private static double o(double var0) {
      return 0.7 * Math.sin(Math.PI * n5.g((var0 - 0.36) / 0.7600000000000001));
   }

   public static double Z(double var0) {
      double var2 = -0.055 * n5.g((var0 - 0.36) / 0.7600000000000001);
      if (var0 < 1.28) {
         return var2;
      } else if (var0 < 1.56) {
         return -0.055 + 0.235 * n5.g((var0 - 1.28) / 0.28);
      } else {
         return var0 < 2.12 ? 0.18 - 0.12 * n5.g((var0 - 1.56) / 0.56) : 0.06 * (1.0 - n5.g((var0 - 2.35) / 0.8));
      }
   }

   private Vec3 D(Vec3 var1, double var2) {
      this.x(this.l(var2), Z(var2), o(var2));
      this.V.set((float)var1.x, (float)var1.y, (float)var1.z);
      this.t.transformPosition(this.V);
      this.l.transformPosition(this.V);
      return new Vec3(this.V.x, this.V.y, this.V.z);
   }

   private Vec3 D(n5 var1, Vec3 var2) {
      Vec3 var3 = var2.add(0.0, -this.b.minimumY(), 0.0).scale(this.E).add(this.I);
      double var4 = Math.cos(var1.y);
      double var6 = Math.sin(var1.y);
      return var1.r.add(var3.x * var4 - var3.z * var6, var3.y, var3.x * var6 + var3.z * var4);
   }

   public Vec3 Z(n5 var1, double var2) {
      return this.D(var1, this.D(L, var2));
   }

   public Vec3 R(n5 var1, double var2) {
      return this.D(var1, this.D(Z, var2));
   }

   public sr.v w(n5 var1, double var2, Vec3 var4) {
      this.x(this.l(var2), Z(var2), o(var2));
      float[] var5 = this.b.vertices();

      for (int var6 = 0; var6 < this.d.length; var6++) {
         int var7 = var6 * 5;
         float var8 = var5[var7];
         float var9 = var5[var7 + 1];
         float var10 = var5[var7 + 2];
         this.V.set(var8, var9, var10);
         this.t.transformPosition(this.V);
         float var11 = this.d[var6];
         float var12 = this.p[var6];
         float var13 = var8 + (this.V.x - var8) * var11;
         float var14 = var9 + (this.V.y - var9) * var11;
         float var15 = var10 + (this.V.z - var10) * var11;
         this.V.set(var13, var14, var15);
         this.l.transformPosition(this.V);
         this.M[var7] = var13 + (this.V.x - var13) * var12;
         this.M[var7 + 1] = var14 + (this.V.y - var14) * var12;
         this.M[var7 + 2] = var15 + (this.V.z - var15) * var12;
      }

      double var16 = Math.cos(var1.y);
      double var17 = Math.sin(var1.y);
      Vec3 var18 = var1.r.add(this.I.x * var16 - this.I.z * var17, 0.0, this.I.x * var17 + this.I.z * var16);
      Matrix4f var19 = new Matrix4f()
         .translation((float)(var18.x - var4.x), (float)(var18.y - var4.y), (float)(var18.z - var4.z))
         .rotateY((float)(-var1.y))
         .scale((float)this.E)
         .translate(0.0F, -this.b.minimumY(), 0.0F);
      Vec3[] var20 = new Vec3[14];

      for (int var21 = 0; var21 < var20.length; var21++) {
         var20[var21] = this.Z(var1, Math.max(1.28, var2 - 0.13 + var21 * 0.13 / (var20.length - 1)));
      }

      double var22 = n5.g(var2 / 0.42) * n5.g((4.6 - var2) / 1.15);
      return new sr.v(this.M, this.b.colors(), var19, var22, var18, this.R(var1, var2), this.Z(var1, var2), var20);
   }

   public static n5.p V(n5 var0, int var1, double var2) {
      double var4 = n5.g((var2 - 1.56) / 0.28);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = n5.H(var1, var0.Q) / 16.0 * 0.9375;
      double var10 = (24.0 - n5.a[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.y);
      double var16 = Math.sin(var0.y);
      Vec3 var18 = var0.r.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = n5.g((3.65 - var2) / 0.9);
      double var21 = n5.g((var2 - 2.6) / 1.05);
      return new n5.p(var18, -var6, 0.0, 0.0, 1.0, var19, var21, var4 >= 1.0);
   }

   public record v(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 grip, Vec3 tip, Vec3[] trail) {
   }
}
