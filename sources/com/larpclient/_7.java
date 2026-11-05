package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class _7 {
   public static final double S = 3.6;
   public static final double R = 1.56;
   public static final double T = 1.28;
   public static final double u = 1.12;
   public static final Vec3 p = new Vec3(-0.031, 0.08, -0.067);
   public static final Vec3 Y = new Vec3(-0.02649, -0.064244, -0.046604);
   public static final Vec3 N = new Vec3(-0.088521, -0.283297, 0.098837);
   private static final double E = -0.105;
   private static final double H = 1.65;
   private final _y.w G;
   private final float[] g;
   private final float[] c;
   private final float[] e;
   private final double C;
   private final double X;
   private final Vec3 P;
   private final Matrix4f j = new Matrix4f();
   private final Matrix4f f = new Matrix4f();
   private final Vector3f s = new Vector3f();

   public _7(_y.w var1) {
      kz.r var2 = kz.z(var1);
      this.G = var2.mesh();
      this.g = (float[])this.G.vertices().clone();
      this.c = new float[this.G.colors().length];
      this.e = new float[this.c.length];
      this.C = 3.6 / var1.height();

      for (int var3 = 0; var3 < this.c.length; var3++) {
         double var4 = this.g[var3 * 5 + 1];
         this.c[var3] = var2.arm()[var3] ? 1.0F : 0.0F;
         this.e[var3] = (float)Math.max(this.c[var3], ta.k((var4 + 0.17) / 0.1));
      }

      double var10 = -1.5;
      double var5 = 0.0;

      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (var10 + var5) * 0.5;
         if (this.E(var8) > 1.65) {
            var10 = var8;
         } else {
            var5 = var8;
         }
      }

      this.X = (var10 + var5) * 0.5;
      Vec3 var11 = this.R(N, 1.56);
      this.P = new Vec3(-var11.x * this.C, 0.0, -var11.z * this.C);
   }

   private double E(double var1) {
      this.V(var1, b(1.56), 0.0);
      this.s.set((float)N.x, (float)N.y, (float)N.z);
      this.j.transformPosition(this.s);
      this.f.transformPosition(this.s);
      return (this.s.y - this.G.minimumY()) * this.C;
   }

   private void V(double var1, double var3, double var5) {
      this.j
         .identity()
         .translate((float)p.x, (float)p.y, (float)p.z)
         .rotateY((float)var5)
         .rotateX((float)var1)
         .translate((float)(-p.x), (float)(-p.y), (float)(-p.z));
      this.f.identity().translate(0.0F, -0.105F, 0.0F).rotateX((float)var3).translate(0.0F, 0.105F, 0.0F);
   }

   public double w(double var1) {
      if (var1 < 1.28) {
         return -2.65 * ta.k((var1 - 0.36) / 0.7600000000000001);
      } else if (var1 < 1.56) {
         double var3 = Math.clamp((var1 - 1.28) / 0.28, 0.0, 1.0);
         return -2.65 + (this.X + 2.65) * var3 * var3;
      } else if (var1 < 1.6800000000000002) {
         return this.X + 0.13 * ta.k((var1 - 1.56) / 0.12);
      } else {
         return var1 < 2.12 ? this.X + 0.13 - 0.27 * ta.k((var1 - 1.56 - 0.12) / 0.44000000000000006) : (this.X - 0.14) * (1.0 - ta.k((var1 - 2.35) / 0.8));
      }
   }

   private static double z(double var0) {
      return 0.7 * Math.sin(Math.PI * ta.k((var0 - 0.36) / 0.7600000000000001));
   }

   public static double b(double var0) {
      double var2 = -0.055 * ta.k((var0 - 0.36) / 0.7600000000000001);
      if (var0 < 1.28) {
         return var2;
      } else if (var0 < 1.56) {
         return -0.055 + 0.235 * ta.k((var0 - 1.28) / 0.28);
      } else {
         return var0 < 2.12 ? 0.18 - 0.12 * ta.k((var0 - 1.56) / 0.56) : 0.06 * (1.0 - ta.k((var0 - 2.35) / 0.8));
      }
   }

   private Vec3 R(Vec3 var1, double var2) {
      this.V(this.w(var2), b(var2), z(var2));
      this.s.set((float)var1.x, (float)var1.y, (float)var1.z);
      this.j.transformPosition(this.s);
      this.f.transformPosition(this.s);
      return new Vec3(this.s.x, this.s.y, this.s.z);
   }

   private Vec3 K(ta var1, Vec3 var2) {
      Vec3 var3 = var2.add(0.0, -this.G.minimumY(), 0.0).scale(this.C).add(this.P);
      double var4 = Math.cos(var1.v);
      double var6 = Math.sin(var1.v);
      return var1.J.add(var3.x * var4 - var3.z * var6, var3.y, var3.x * var6 + var3.z * var4);
   }

   public Vec3 m(ta var1, double var2) {
      return this.K(var1, this.R(N, var2));
   }

   public Vec3 o(ta var1, double var2) {
      return this.K(var1, this.R(Y, var2));
   }

   public _7.l S(ta var1, double var2, Vec3 var4) {
      this.V(this.w(var2), b(var2), z(var2));
      float[] var5 = this.G.vertices();

      for (int var6 = 0; var6 < this.c.length; var6++) {
         int var7 = var6 * 5;
         float var8 = var5[var7];
         float var9 = var5[var7 + 1];
         float var10 = var5[var7 + 2];
         this.s.set(var8, var9, var10);
         this.j.transformPosition(this.s);
         float var11 = this.c[var6];
         float var12 = this.e[var6];
         float var13 = var8 + (this.s.x - var8) * var11;
         float var14 = var9 + (this.s.y - var9) * var11;
         float var15 = var10 + (this.s.z - var10) * var11;
         this.s.set(var13, var14, var15);
         this.f.transformPosition(this.s);
         this.g[var7] = var13 + (this.s.x - var13) * var12;
         this.g[var7 + 1] = var14 + (this.s.y - var14) * var12;
         this.g[var7 + 2] = var15 + (this.s.z - var15) * var12;
      }

      double var16 = Math.cos(var1.v);
      double var17 = Math.sin(var1.v);
      Vec3 var18 = var1.J.add(this.P.x * var16 - this.P.z * var17, 0.0, this.P.x * var17 + this.P.z * var16);
      Matrix4f var19 = new Matrix4f()
         .translation((float)(var18.x - var4.x), (float)(var18.y - var4.y), (float)(var18.z - var4.z))
         .rotateY((float)(-var1.v))
         .scale((float)this.C)
         .translate(0.0F, -this.G.minimumY(), 0.0F);
      Vec3[] var20 = new Vec3[14];

      for (int var21 = 0; var21 < var20.length; var21++) {
         var20[var21] = this.m(var1, Math.max(1.28, var2 - 0.13 + var21 * 0.13 / (var20.length - 1)));
      }

      double var22 = ta.k(var2 / 0.42) * ta.k((4.6 - var2) / 1.15);
      return new _7.l(this.g, this.G.colors(), var19, var22, var18, this.o(var1, var2), this.m(var1, var2), var20);
   }

   public static ta.y o(ta var0, int var1, double var2) {
      double var4 = ta.k((var2 - 1.56) / 0.28);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = ta.Z(var1, var0.T) / 16.0 * 0.9375;
      double var10 = (24.0 - ta.Q[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.v);
      double var16 = Math.sin(var0.v);
      Vec3 var18 = var0.J.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = ta.k((3.65 - var2) / 0.9);
      double var21 = ta.k((var2 - 2.6) / 1.05);
      return new ta.y(var18, -var6, 0.0, 0.0, 1.0, var19, var21, var4 >= 1.0);
   }

   public record l(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3 feet, Vec3 grip, Vec3 tip, Vec3[] trail) {
   }
}
