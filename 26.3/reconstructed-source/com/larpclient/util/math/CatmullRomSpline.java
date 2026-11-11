package com.larpclient.util.math;

import com.larpclient.util.render.ColorUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class CatmullRomSpline {
   private CatmullRomSpline() {
   }

   public static Vec3 interpolate(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4) {
      if (var4 <= 0.0) {
         return var1;
      } else {
         return var4 >= 1.0
            ? var2
            : interpolateSegment(
               var0, var1, var2, var3, var4, Math.sqrt(var0.distanceTo(var1)), Math.sqrt(var1.distanceTo(var2)), Math.sqrt(var2.distanceTo(var3))
            );
      }
   }

   private static Vec3 interpolateSegment(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, double var6, double var8, double var10) {
      if (!(var6 < 1.0E-6) && !(var8 < 1.0E-6) && !(var10 < 1.0E-6)) {
         double var12 = var6 + var4 * var8;
         double var14 = var12 / var6;
         double var16 = (var12 - var6) / var8;
         double var18 = (var12 - var6 - var8) / var10;
         double var20 = var12 / (var6 + var8);
         double var22 = (var12 - var6) / (var8 + var10);
         return new Vec3(
            interpolateAxis(var0.x, var1.x, var2.x, var3.x, var14, var16, var18, var20, var22),
            interpolateAxis(var0.y, var1.y, var2.y, var3.y, var14, var16, var18, var20, var22),
            interpolateAxis(var0.z, var1.z, var2.z, var3.z, var14, var16, var18, var20, var22)
         );
      } else {
         return var1.lerp(var2, var4);
      }
   }

   private static double interpolateAxis(
      double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16
   ) {
      double var18 = Mth.lerp(var8, var0, var2);
      double var20 = Mth.lerp(var10, var2, var4);
      double var22 = Mth.lerp(var12, var4, var6);
      double var24 = Mth.lerp(var14, var18, var20);
      double var26 = Mth.lerp(var16, var20, var22);
      return Mth.lerp(var10, var24, var26);
   }

   public static final class Ribbon {
      final Vec3[] points;
      final Vec3[] offsets;
      final double[] widths;
      final int[] colors;
      int size;
      double maxDistanceSq = Double.POSITIVE_INFINITY;

      public Ribbon(int var1) {
         this.points = new Vec3[var1];
         this.offsets = new Vec3[var1];
         this.widths = new double[var1];
         this.colors = new int[var1];
      }

      public void clear() {
         this.size = 0;
      }

      public int getSize() {
         return this.size;
      }

      public void addPoint(Vec3 var1, double var2, int var4) {
         if (this.size == this.points.length) {
            throw new IllegalStateException("Ribbon capacity exceeded");
         }

         this.points[this.size] = var1;
         this.widths[this.size] = var2;
         this.colors[this.size++] = var4;
      }

      public void fadeInTail() {
         if (this.size >= 2) {
            double var1 = 0.0;

            for (int var3 = 1; var3 < this.size; var3++) {
               var1 += this.points[var3 - 1].distanceTo(this.points[var3]);
            }

            if (!(var1 < 1.0E-8)) {
               double var10 = var1 * 0.18;
               double var5 = 0.0;
               this.widths[0] = 0.0;
               this.colors[0] = this.colors[0] & 16777215;

               for (int var7 = 1; var7 < this.size; var7++) {
                  var5 += this.points[var7 - 1].distanceTo(this.points[var7]);
                  if (var5 >= var10) {
                     break;
                  }

                  double var8 = Easing.smoothstep(var5 / var10);
                  this.widths[var7] = this.widths[var7] * var8;
                  this.colors[var7] = ColorUtil.multiplyAlpha(this.colors[var7], var8);
               }
            }
         }
      }

      public void computeOffsets(Vec3 var1) {
         if (this.size >= 2) {
            Vec3 var2 = computeNormal(this.points[0], this.points[1], var1, new Vec3(1.0, 0.0, 0.0));
            this.offsets[0] = var2.scale(this.widths[0] * 0.5);

            for (int var3 = 1; var3 < this.size; var3++) {
               Vec3 var4 = var3 + 1 < this.size ? computeNormal(this.points[var3], this.points[var3 + 1], var1, var2) : var2;
               if (var4.dot(var2) < 0.0) {
                  var4 = var4.scale(-1.0);
               }

               Vec3 var5 = var2.add(var4);
               Vec3 var6 = var5.lengthSqr() < 1.0E-12 ? var2 : var5.normalize();
               double var7 = Math.min(2.0, 1.0 / Math.max(0.5, Math.abs(var6.dot(var4))));
               this.offsets[var3] = var6.scale(this.widths[var3] * 0.5 * var7);
               var2 = var4;
            }
         }
      }

      public Vec3 addPoint(int var1) {
         return this.offsets[var1];
      }

      private static Vec3 computeNormal(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3) {
         Vec3 var4 = var1.subtract(var0);
         Vec3 var5 = var4.cross(var2.subtract(var0));
         if (var5.lengthSqr() >= 1.0E-12) {
            return var5.normalize();
         }

         if (var4.lengthSqr() < 1.0E-12) {
            return var3;
         }

         Vec3 var6 = var4.normalize();
         Vec3 var7 = var3.subtract(var6.scale(var3.dot(var6)));
         if (var7.lengthSqr() >= 1.0E-12) {
            return var7.normalize();
         }

         Vec3 var8 = Math.abs(var6.y) < 0.9 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(0.0, 0.0, 1.0);
         return var6.cross(var8).normalize();
      }
   }

   static final class SplineSegment {
      private Vec3 p0;
      private Vec3 p1;
      private Vec3 p2;
      private Vec3 p3;
      private double dist01;
      private double dist12;
      private double dist23;

      void setControlPoints(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
         this.p0 = var1;
         this.p1 = var2;
         this.p2 = var3;
         this.p3 = var4;
         this.dist01 = Math.sqrt(var1.distanceTo(var2));
         this.dist12 = Math.sqrt(var2.distanceTo(var3));
         this.dist23 = Math.sqrt(var3.distanceTo(var4));
      }

      Vec3 sample(double var1) {
         if (var1 <= 0.0) {
            return this.p1;
         } else {
            return var1 >= 1.0 ? this.p2 : CatmullRomSpline.interpolateSegment(this.p0, this.p1, this.p2, this.p3, var1, this.dist01, this.dist12, this.dist23);
         }
      }
   }
}
