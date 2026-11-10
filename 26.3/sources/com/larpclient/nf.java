package com.larpclient;

import org.joml.Matrix4d;
import org.joml.Matrix4fc;
import org.joml.Vector3d;

public final class nf {
   public static final double d = 70.0;

   private nf() {
   }

   public static nf.j l(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = Math.clamp(var8, -70.0, 70.0);
      double[] var14 = new nf.j(0.0, 0.0, 0.0, var6, var12).forward();
      return new nf.j(var0 + var14[0] * var10, var2 + var14[1] * var10, var4 + var14[2] * var10, var6, var12);
   }

   public static nf.j D(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      double var18 = Math.toDegrees(Math.atan2(-var12, var16));
      double var20 = Math.toDegrees(Math.atan2(-var14, Math.hypot(var12, var16)));
      return new nf.j(var0, var2, var4, var18, Math.clamp(var20, -70.0, 70.0));
   }

   public static nf.j b(double var0, double var2, double var4, double[] var6, double var7, double var9, double var11) {
      double var13 = var0 + var6[0] * var7;
      double var15 = var2 + var6[1] * var7;
      double var17 = var4 + var6[2] * var7;
      nf.j var19 = D(var13, var15, var17, var0, var2, var4);

      for (int var20 = 0; var20 < 5; var20++) {
         double[] var21 = var19.point(var9, var11);
         var19 = D(var13 - (var21[0] - var19.x), var15 - (var21[1] - var19.y), var17 - (var21[2] - var19.z), var0, var2, var4);
      }

      double[] var22 = var19.point(var9, var11);
      return new nf.j(var13 - (var22[0] - var19.x), var15 - (var22[1] - var19.y), var17 - (var22[2] - var19.z), var19.yaw, var19.pitch);
   }

   public static dx.z X(nf.j var0, double var1, double var3, double var5, double var7, double var9, double var11, Matrix4fc var13, double var14, double var16) {
      double[] var18 = var0.right();
      double[] var19 = var0.up();
      double var20 = var18[0] * var1;
      double var22 = var18[1] * var1;
      double var24 = var18[2] * var1;
      double var26 = -var19[0] * var1;
      double var28 = -var19[1] * var1;
      double var30 = -var19[2] * var1;
      double var32 = var0.x - var7 - var20 * var3 - var26 * var5;
      double var34 = var0.y - var9 - var22 * var3 - var28 * var5;
      double var36 = var0.z - var11 - var24 * var3 - var30 * var5;
      double[] var38 = y(var13, var20, var22, var24, 0.0);
      double[] var39 = y(var13, var26, var28, var30, 0.0);
      double[] var40 = y(var13, var32, var34, var36, 1.0);
      double var41 = var14 / 2.0;
      double var43 = var16 / 2.0;
      return new dx.z(
         var41 * (var38[0] + var38[2]),
         var41 * (var39[0] + var39[2]),
         var41 * (var40[0] + var40[2]),
         var43 * (var38[2] - var38[1]),
         var43 * (var39[2] - var39[1]),
         var43 * (var40[2] - var40[1]),
         var38[2],
         var39[2],
         var40[2]
      );
   }

   private static double[] y(Matrix4fc var0, double var1, double var3, double var5, double var7) {
      return new double[]{
         var0.m00() * var1 + var0.m10() * var3 + var0.m20() * var5 + var0.m30() * var7,
         var0.m01() * var1 + var0.m11() * var3 + var0.m21() * var5 + var0.m31() * var7,
         var0.m03() * var1 + var0.m13() * var3 + var0.m23() * var5 + var0.m33() * var7
      };
   }

   public static double[] N(Matrix4fc var0, double var1, double var3, double var5, double var7) {
      Matrix4d var9 = new Matrix4d(var0);
      if (Math.abs(var9.determinant()) < 1.0E-18) {
         return null;
      }

      var9.invert();
      Vector3d var10 = var9.transformProject(var1 / var5 * 2.0 - 1.0, 1.0 - var3 / var7 * 2.0, 0.5, new Vector3d());
      double var11 = var10.length();
      return var11 > 1.0E-9 && Double.isFinite(var11) ? new double[]{var10.x / var11, var10.y / var11, var10.z / var11} : null;
   }

   public record j(double x, double y, double z, double yaw, double pitch) {
      public double[] forward() {
         double var1 = Math.toRadians(this.yaw);
         double var3 = Math.toRadians(this.pitch);
         return new double[]{-Math.sin(var1) * Math.cos(var3), -Math.sin(var3), Math.cos(var1) * Math.cos(var3)};
      }

      public double[] right() {
         double var1 = Math.toRadians(this.yaw);
         return new double[]{-Math.cos(var1), 0.0, -Math.sin(var1)};
      }

      public double[] up() {
         double[] var1 = this.right();
         double[] var2 = this.forward();
         return new double[]{var1[1] * var2[2] - var1[2] * var2[1], var1[2] * var2[0] - var1[0] * var2[2], var1[0] * var2[1] - var1[1] * var2[0]};
      }

      public double[] point(double var1, double var3) {
         double[] var5 = this.right();
         double[] var6 = this.up();
         return new double[]{this.x + var5[0] * var1 + var6[0] * var3, this.y + var5[1] * var1 + var6[1] * var3, this.z + var5[2] * var1 + var6[2] * var3};
      }

      public boolean faces(double var1, double var3, double var5) {
         double[] var7 = this.forward();
         return var7[0] * (this.x - var1) + var7[1] * (this.y - var3) + var7[2] * (this.z - var5) > 0.0;
      }

      public double distance(double var1, double var3, double var5) {
         return Math.sqrt((this.x - var1) * (this.x - var1) + (this.y - var3) * (this.y - var3) + (this.z - var5) * (this.z - var5));
      }
   }
}
