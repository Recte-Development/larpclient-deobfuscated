package com.larpclient;

import net.minecraft.class_243;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class jo {
   public static final double W = 3.6;
   public static final double S = 1.56;
   public static final double U = 1.28;
   public static final double c = 1.12;
   public static final class_243 D = new class_243(-0.031, 0.08, -0.067);
   public static final class_243 A = new class_243(-0.02649, -0.064244, -0.046604);
   public static final class_243 M = new class_243(-0.088521, -0.283297, 0.098837);
   private static final double k = -0.105;
   private static final double r = 1.65;
   private final tt.r J;
   private final float[] T;
   private final float[] n;
   private final float[] z;
   private final double d;
   private final double N;
   private final class_243 E;
   private final Matrix4f I = new Matrix4f();
   private final Matrix4f q = new Matrix4f();
   private final Vector3f V = new Vector3f();

   public jo(tt.r var1) {
      l.u var2 = l.P(var1);
      this.J = var2.mesh();
      this.T = (float[])this.J.vertices().clone();
      this.n = new float[this.J.colors().length];
      this.z = new float[this.n.length];
      this.d = 3.6 / var1.height();

      for (int var3 = 0; var3 < this.n.length; var3++) {
         double var4 = this.T[var3 * 5 + 1];
         this.n[var3] = var2.arm()[var3] ? 1.0F : 0.0F;
         this.z[var3] = (float)Math.max(this.n[var3], u8.S((var4 + 0.17) / 0.1));
      }

      double var10 = -1.5;
      double var5 = 0.0;

      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (var10 + var5) * 0.5;
         if (this.C(var8) > 1.65) {
            var10 = var8;
         } else {
            var5 = var8;
         }
      }

      this.N = (var10 + var5) * 0.5;
      class_243 var11 = this.N(M, 1.56);
      this.E = new class_243(-var11.field_1352 * this.d, 0.0, -var11.field_1350 * this.d);
   }

   private double C(double var1) {
      this.H(var1, g(1.56), 0.0);
      this.V.set((float)M.field_1352, (float)M.field_1351, (float)M.field_1350);
      this.I.transformPosition(this.V);
      this.q.transformPosition(this.V);
      return (this.V.y - this.J.minimumY()) * this.d;
   }

   private void H(double var1, double var3, double var5) {
      this.I
         .identity()
         .translate((float)D.field_1352, (float)D.field_1351, (float)D.field_1350)
         .rotateY((float)var5)
         .rotateX((float)var1)
         .translate((float)(-D.field_1352), (float)(-D.field_1351), (float)(-D.field_1350));
      this.q.identity().translate(0.0F, -0.105F, 0.0F).rotateX((float)var3).translate(0.0F, 0.105F, 0.0F);
   }

   public double G(double var1) {
      if (var1 < 1.28) {
         return -2.65 * u8.S((var1 - 0.36) / 0.7600000000000001);
      } else if (var1 < 1.56) {
         double var3 = Math.clamp((var1 - 1.28) / 0.28, 0.0, 1.0);
         return -2.65 + (this.N + 2.65) * var3 * var3;
      } else if (var1 < 1.6800000000000002) {
         return this.N + 0.13 * u8.S((var1 - 1.56) / 0.12);
      } else {
         return var1 < 2.12 ? this.N + 0.13 - 0.27 * u8.S((var1 - 1.56 - 0.12) / 0.44000000000000006) : (this.N - 0.14) * (1.0 - u8.S((var1 - 2.35) / 0.8));
      }
   }

   private static double N(double var0) {
      return 0.7 * Math.sin(Math.PI * u8.S((var0 - 0.36) / 0.7600000000000001));
   }

   public static double g(double var0) {
      double var2 = -0.055 * u8.S((var0 - 0.36) / 0.7600000000000001);
      if (var0 < 1.28) {
         return var2;
      } else if (var0 < 1.56) {
         return -0.055 + 0.235 * u8.S((var0 - 1.28) / 0.28);
      } else {
         return var0 < 2.12 ? 0.18 - 0.12 * u8.S((var0 - 1.56) / 0.56) : 0.06 * (1.0 - u8.S((var0 - 2.35) / 0.8));
      }
   }

   private class_243 N(class_243 var1, double var2) {
      this.H(this.G(var2), g(var2), N(var2));
      this.V.set((float)var1.field_1352, (float)var1.field_1351, (float)var1.field_1350);
      this.I.transformPosition(this.V);
      this.q.transformPosition(this.V);
      return new class_243(this.V.x, this.V.y, this.V.z);
   }

   private class_243 p(u8 var1, class_243 var2) {
      class_243 var3 = var2.method_1031(0.0, -this.J.minimumY(), 0.0).method_1021(this.d).method_1019(this.E);
      double var4 = Math.cos(var1.y);
      double var6 = Math.sin(var1.y);
      return var1.z.method_1031(var3.field_1352 * var4 - var3.field_1350 * var6, var3.field_1351, var3.field_1352 * var6 + var3.field_1350 * var4);
   }

   public class_243 T(u8 var1, double var2) {
      return this.p(var1, this.N(M, var2));
   }

   public class_243 F(u8 var1, double var2) {
      return this.p(var1, this.N(A, var2));
   }

   public jo.c g(u8 var1, double var2, class_243 var4) {
      this.H(this.G(var2), g(var2), N(var2));
      float[] var5 = this.J.vertices();

      for (int var6 = 0; var6 < this.n.length; var6++) {
         int var7 = var6 * 5;
         float var8 = var5[var7];
         float var9 = var5[var7 + 1];
         float var10 = var5[var7 + 2];
         this.V.set(var8, var9, var10);
         this.I.transformPosition(this.V);
         float var11 = this.n[var6];
         float var12 = this.z[var6];
         float var13 = var8 + (this.V.x - var8) * var11;
         float var14 = var9 + (this.V.y - var9) * var11;
         float var15 = var10 + (this.V.z - var10) * var11;
         this.V.set(var13, var14, var15);
         this.q.transformPosition(this.V);
         this.T[var7] = var13 + (this.V.x - var13) * var12;
         this.T[var7 + 1] = var14 + (this.V.y - var14) * var12;
         this.T[var7 + 2] = var15 + (this.V.z - var15) * var12;
      }

      double var16 = Math.cos(var1.y);
      double var17 = Math.sin(var1.y);
      class_243 var18 = var1.z.method_1031(this.E.field_1352 * var16 - this.E.field_1350 * var17, 0.0, this.E.field_1352 * var17 + this.E.field_1350 * var16);
      Matrix4f var19 = new Matrix4f()
         .translation((float)(var18.field_1352 - var4.field_1352), (float)(var18.field_1351 - var4.field_1351), (float)(var18.field_1350 - var4.field_1350))
         .rotateY((float)(-var1.y))
         .scale((float)this.d)
         .translate(0.0F, -this.J.minimumY(), 0.0F);
      class_243[] var20 = new class_243[14];

      for (int var21 = 0; var21 < var20.length; var21++) {
         var20[var21] = this.T(var1, Math.max(1.28, var2 - 0.13 + var21 * 0.13 / (var20.length - 1)));
      }

      double var22 = u8.S(var2 / 0.42) * u8.S((4.6 - var2) / 1.15);
      return new jo.c(this.T, this.J.colors(), var19, var22, var18, this.F(var1, var2), this.T(var1, var2), var20);
   }

   public static u8.s f(u8 var0, int var1, double var2) {
      double var4 = u8.S((var2 - 1.56) / 0.28);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = u8.z(var1, var0.D) / 16.0 * 0.9375;
      double var10 = (24.0 - u8.E[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.y);
      double var16 = Math.sin(var0.y);
      class_243 var18 = var0.z.method_1031(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = u8.S((3.65 - var2) / 0.9);
      double var21 = u8.S((var2 - 2.6) / 1.05);
      return new u8.s(var18, -var6, 0.0, 0.0, 1.0, var19, var21, var4 >= 1.0);
   }

   public record c(float[] vertices, int[] colors, Matrix4f transform, double opacity, class_243 feet, class_243 grip, class_243 tip, class_243[] trail) {
   }
}
