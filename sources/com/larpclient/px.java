package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class px {
   public static final long H = 1500L;
   public static final long G = 150L;
   public static final double C = 0.5;
   public static final double A = 1.3;
   public static final double o = 0.05;

   public static boolean H(double var0, double var2, double var4, double var6) {
      return Double.isFinite(var0) && Double.isFinite(var2) && var0 >= 0.0 && var2 >= 0.0 && var0 <= var4 && var2 <= var6;
   }

   public static <T> T S(List<px.l<T>> var0, T var1, long var2, T var4, double var5, double var7) {
      double var9 = var7 / 2.0;

      for (px.l var12 : var0) {
         if (var12.entity == var1 && var2 >= 0L && var2 <= 1500L && H(var12.distance, var12.angle, var5, var9)) {
            return (T)var1;
         }
      }

      for (px.l var18 : var0) {
         if (var18.entity == var4 && H(var18.distance, var18.angle, var5 + 0.5, var9 * 1.3)) {
            return (T)var4;
         }
      }

      Object var17 = null;
      double var19 = var9;

      for (px.l var15 : var0) {
         if (H(var15.distance, var15.angle, var5, var19)) {
            var17 = var15.entity;
            var19 = var15.angle;
         }
      }

      return (T)var17;
   }

   public static AABB L(AABB var0) {
      return var0.deflate(var0.getXsize() * 0.25, var0.getYsize() * 0.1, var0.getXsize() * 0.25);
   }

   public static Vec3 U(Vec3 var0, AABB var1) {
      return new Vec3(Math.clamp(var0.x, var1.minX, var1.maxX), Math.clamp(var0.y, var1.minY, var1.maxY), Math.clamp(var0.z, var1.minZ, var1.maxZ));
   }

   public static float[] h(float var0, float var1, double var2, double var4) {
      return new float[]{var0 + (float)var2 * 0.15F, Math.clamp(var1 + (float)var4 * 0.15F, -90.0F, 90.0F)};
   }

   public static Vec3 c(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      if (var2.contains(var0)) {
         return var0;
      }

      Optional var5 = var2.clip(var0, var0.add(var1.scale(var3 + 1.0)));
      return var5.orElseGet(() -> U(var0.add(var1.scale(var0.distanceTo(var2.getCenter()))), var2));
   }

   public static float[] k(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      if (!var2.contains(var0) && !var2.clip(var0, var0.add(var1.scale(var3 + 1.0))).isPresent()) {
         Vec3 var5 = c(var0, var1, var2, var3).subtract(var0);
         return new float[]{
            (float)j6.U(Math.toDegrees(Math.atan2(var5.z, var5.x)) - 90.0),
            (float)Math.clamp(-Math.toDegrees(Math.atan2(var5.y, Math.hypot(var5.x, var5.z))), -90.0, 90.0)
         };
      } else {
         return null;
      }
   }

   public static double U(float var0, float var1, float[] var2) {
      return var2 == null ? 0.0 : Math.hypot(j6.U(var2[0] - var0), var2[1] - var1);
   }

   public static double c(double var0, boolean var2, boolean var3) {
      double var4 = var0 * 0.6F + 0.2F;
      return var4 * var4 * var4 * (var2 && var3 ? 1 : 8);
   }

   private px() {
   }

   public static final class f {
      private double E;
      private double H;
      private double L;
      private double Z;
      private long M;
      private long y;
      private long o;
      private boolean s;

      public px.k O(
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
               this.M = var19;
               this.s = (boolean)1;
            }

            if (var1 != 0.0) {
               this.L = Math.signum(var1);
               this.y = var19;
            } else if (this.L != 0.0 && (var19 < this.y || var19 - this.y > 150L)) {
               this.L = 0.0;
               this.E = 0.0;
            }

            if (var3 != 0.0) {
               this.Z = Math.signum(var3);
               this.o = var19;
            } else if (this.Z != 0.0 && (var19 < this.o || var19 - this.o > 150L)) {
               this.Z = 0.0;
               this.H = 0.0;
            }

            if (var9 && this.s && var19 >= this.M && var19 - this.M <= 150L) {
               double var21 = Math.min(var11, 0.05);
               double var23 = var13 * var15 * var21;
               double var25 = -Math.expm1(-12.0 * var21);
               double var27 = (float)var17 * 0.15F;
               double var29 = j6.U(var5);
               if (this.L * var29 < 0.0) {
                  var29 = 0.0;
               }

               double var31 = var10 && this.Z * var7 >= 0.0 ? var7 : 0.0;
               if (var29 == 0.0 || this.E * var29 < 0.0) {
                  this.E = 0.0;
               }

               if (var31 == 0.0 || this.H * var31 < 0.0) {
                  this.H = 0.0;
               }

               this.E = this.E + Math.clamp(var29 * var25, -var23, var23) / var27;
               this.H = this.H + Math.clamp(var31 * var25 * 0.6, -var23, var23) / var27;
               long var33 = (long)this.E;
               long var35 = (long)this.H;
               this.E -= var33;
               this.H -= var35;
               return new px.k(var33 * var17, var35 * var17);
            } else {
               this.h();
               this.L = this.Z = 0.0;
               return px.k.STILL;
            }
         } else {
            this.g();
            return px.k.STILL;
         }
      }

      public void h() {
         this.E = this.H = 0.0;
      }

      public void g() {
         this.h();
         this.L = this.Z = 0.0;
         this.M = 0L;
         this.y = this.o = 0L;
         this.s = (boolean)0;
      }

      public double E() {
         return this.E;
      }

      public double c() {
         return this.H;
      }

      public boolean T(long var1) {
         return this.s && var1 >= this.M && var1 - this.M <= 150L;
      }

      public boolean R(double var1, double var3, boolean var5) {
         int var6 = var1 != 0.0 ? 1 : 0;
         int var7 = var5 && var3 != 0.0 ? 1 : 0;
         return (var6 != 0 || var7 != 0) && (var6 == 0 || !(this.L * var1 >= 0.0)) && (var7 == 0 || !(this.Z * var3 >= 0.0));
      }
   }

   public record k(double yaw, double pitch) {
      public static final px.k STILL = new px.k(0.0, 0.0);
   }

   public record l<T>(T entity, double distance, double angle) {
   }
}
