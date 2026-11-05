package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class k1 {
   public static final long F = 1500L;
   public static final long a = 150L;
   public static final double Z = 0.5;
   public static final double T = 1.3;
   public static final double u = 0.05;

   public static boolean w(double var0, double var2, double var4, double var6) {
      return Double.isFinite(var0) && Double.isFinite(var2) && var0 >= 0.0 && var2 >= 0.0 && var0 <= var4 && var2 <= var6;
   }

   public static <T> T x(List<k1.w<T>> var0, T var1, long var2, T var4, double var5, double var7) {
      double var9 = var7 / 2.0;

      for (k1.w var12 : var0) {
         if (var12.entity == var1 && var2 >= 0L && var2 <= 1500L && w(var12.distance, var12.angle, var5, var9)) {
            return (T)var1;
         }
      }

      for (k1.w var18 : var0) {
         if (var18.entity == var4 && w(var18.distance, var18.angle, var5 + 0.5, var9 * 1.3)) {
            return (T)var4;
         }
      }

      Object var17 = null;
      double var19 = var9;

      for (k1.w var15 : var0) {
         if (w(var15.distance, var15.angle, var5, var19)) {
            var17 = var15.entity;
            var19 = var15.angle;
         }
      }

      return (T)var17;
   }

   public static AABB u(AABB var0) {
      return var0.deflate(var0.getXsize() * 0.25, var0.getYsize() * 0.1, var0.getXsize() * 0.25);
   }

   public static Vec3 U(Vec3 var0, AABB var1) {
      return new Vec3(Math.clamp(var0.x, var1.minX, var1.maxX), Math.clamp(var0.y, var1.minY, var1.maxY), Math.clamp(var0.z, var1.minZ, var1.maxZ));
   }

   public static float[] S(float var0, float var1, double var2, double var4) {
      return new float[]{var0 + (float)var2 * 0.15F, Math.clamp(var1 + (float)var4 * 0.15F, -90.0F, 90.0F)};
   }

   public static Vec3 x(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      if (var2.contains(var0)) {
         return var0;
      }

      Optional var5 = var2.clip(var0, var0.add(var1.scale(var3 + 1.0)));
      return var5.orElseGet(() -> U(var0.add(var1.scale(var0.distanceTo(var2.getCenter()))), var2));
   }

   public static float[] z(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      if (!var2.contains(var0) && !var2.clip(var0, var0.add(var1.scale(var3 + 1.0))).isPresent()) {
         Vec3 var5 = x(var0, var1, var2, var3).subtract(var0);
         return new float[]{
            (float)ph.N(Math.toDegrees(Math.atan2(var5.z, var5.x)) - 90.0),
            (float)Math.clamp(-Math.toDegrees(Math.atan2(var5.y, Math.hypot(var5.x, var5.z))), -90.0, 90.0)
         };
      } else {
         return null;
      }
   }

   public static double F(float var0, float var1, float[] var2) {
      return var2 == null ? 0.0 : Math.hypot(ph.N(var2[0] - var0), var2[1] - var1);
   }

   public static double R(double var0, boolean var2, boolean var3) {
      double var4 = var0 * 0.6F + 0.2F;
      return var4 * var4 * var4 * (var2 && var3 ? 1 : 8);
   }

   private k1() {
   }

   public static final class j {
      private double U;
      private double N;
      private double e;
      private double s;
      private long H;
      private long t;
      private long z;
      private boolean S;

      public k1.y a(
         double var1, double var3, double var5, double var7, boolean var9, boolean var10, double var11, double var13, double var15, double var17, long var19
      ) {
         if (Double.isFinite(var1)
            && Double.isFinite(var3)
            && Double.isFinite(var11)
            && !(var11 < 0.0)
            && Double.isFinite(var17)
            && !(var17 <= 0.0)
            && Double.isFinite(var5)
            && Double.isFinite(var7)
            && Double.isFinite(var13)
            && !(var13 <= 0.0)
            && Double.isFinite(var15)
            && !(var15 <= 0.0)) {
            if (var1 != 0.0 || var3 != 0.0) {
               this.H = var19;
               this.S = (boolean)1;
            }

            if (var1 != 0.0) {
               this.e = Math.signum(var1);
               this.t = var19;
            } else if (this.e != 0.0 && (var19 < this.t || var19 - this.t > 150L)) {
               this.e = 0.0;
               this.U = 0.0;
            }

            if (var3 != 0.0) {
               this.s = Math.signum(var3);
               this.z = var19;
            } else if (this.s != 0.0 && (var19 < this.z || var19 - this.z > 150L)) {
               this.s = 0.0;
               this.N = 0.0;
            }

            if (var9 && this.S && var19 >= this.H && var19 - this.H <= 150L) {
               double var21 = Math.min(var11, 0.05);
               double var23 = var13 * var15 * var21;
               double var25 = -Math.expm1(-12.0 * var21);
               double var27 = (float)var17 * 0.15F;
               double var29 = ph.N(var5);
               if (this.e * var29 < 0.0) {
                  var29 = 0.0;
               }

               double var31 = var10 && this.s * var7 >= 0.0 ? var7 : 0.0;
               if (var29 == 0.0 || this.U * var29 < 0.0) {
                  this.U = 0.0;
               }

               if (var31 == 0.0 || this.N * var31 < 0.0) {
                  this.N = 0.0;
               }

               this.U = this.U + Math.clamp(var29 * var25, -var23, var23) / var27;
               this.N = this.N + Math.clamp(var31 * var25 * 0.6, -var23, var23) / var27;
               long var33 = (long)this.U;
               long var35 = (long)this.N;
               this.U -= var33;
               this.N -= var35;
               return new k1.y(var33 * var17, var35 * var17);
            } else {
               this.Y();
               this.e = this.s = 0.0;
               return k1.y.STILL;
            }
         } else {
            this.r();
            return k1.y.STILL;
         }
      }

      public void Y() {
         this.U = this.N = 0.0;
      }

      public void r() {
         this.Y();
         this.e = this.s = 0.0;
         this.H = 0L;
         this.t = this.z = 0L;
         this.S = (boolean)0;
      }

      public double L() {
         return this.U;
      }

      public double m() {
         return this.N;
      }

      public boolean R(long var1) {
         return this.S && var1 >= this.H && var1 - this.H <= 150L;
      }

      public boolean L(double var1, double var3, boolean var5) {
         int var6 = var1 != 0.0 ? 1 : 0;
         int var7 = var5 && var3 != 0.0 ? 1 : 0;
         return (var6 != 0 || var7 != 0) && (var6 == 0 || !(this.e * var1 >= 0.0)) && (var7 == 0 || !(this.s * var3 >= 0.0));
      }
   }

   public record w<T>(T entity, double distance, double angle) {
   }

   public record y(double yaw, double pitch) {
      public static final k1.y STILL = new k1.y(0.0, 0.0);
   }
}
