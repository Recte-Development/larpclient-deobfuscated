package com.larpclient;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class wt {
   public static wt.k r(Vec3 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1);
      Vec3 var5 = new Vec3(-Math.sin(var3), 0.0, Math.cos(var3));
      Vec3 var6 = new Vec3(var5.x, Math.clamp(-Math.tan(Math.toRadians(var2)), -5.0, 5.0), var5.z);
      return new wt.k(var0.add(var5.scale(0.3)), var6.scale(0.6 / var6.length() + 0.5));
   }

   public static wt.u c(Vec3 var0, Vec3 var1, int var2) {
      double var3 = var1.x - var0.x;
      double var5 = var1.z - var0.z;
      double var7 = Math.hypot(var3, var5);
      if (var7 > 0.5 && Double.isFinite(var1.y)) {
         float var9 = (float)Math.toDegrees(Math.atan2(var5, var3)) - 90.0F;
         float var10 = -35.0F;
         float var11 = 35.0F;
         if (K(var0, var9, var10, var7, var1.y, var2) < 0.0) {
            return null;
         }

         if (K(var0, var9, var11, var7, var1.y, var2) > 0.0) {
            return null;
         }

         for (int var12 = 0; var12 < 32; var12++) {
            float var13 = (var10 + var11) / 2.0F;
            if (K(var0, var9, var13, var7, var1.y, var2) > 0.0) {
               var10 = var13;
            } else {
               var11 = var13;
            }
         }

         float var14 = (var10 + var11) / 2.0F;
         int var15 = g(var0, var9, var14, var7, var2);
         return var15 < 0 ? null : new wt.u(var9, var14, var15);
      } else {
         return null;
      }
   }

   static double K(Vec3 var0, float var1, float var2, double var3, double var5, int var7) {
      wt.k var8 = r(var0, var1, var2);

      for (int var9 = 0; var9 < var7; var9++) {
         Vec3 var10 = var8.position();
         Vec3 var11 = var8.collisionEnd();
         double var12 = Math.hypot(var10.x - var0.x, var10.z - var0.z);
         double var14 = Math.hypot(var11.x - var0.x, var11.z - var0.z);
         if (var14 >= var3) {
            double var16 = var14 - var12 < 1.0E-9 ? 1.0 : Math.clamp((var3 - var12) / (var14 - var12), 0.0, 1.0);
            return var10.y + (var11.y - var10.y) * var16 - var5;
         }

         var8 = var8.next();
      }

      return Double.NEGATIVE_INFINITY;
   }

   private static int g(Vec3 var0, float var1, float var2, double var3, int var5) {
      wt.k var6 = r(var0, var1, var2);

      for (int var7 = 0; var7 < var5; var7++) {
         Vec3 var8 = var6.collisionEnd();
         if (Math.hypot(var8.x - var0.x, var8.z - var0.z) >= var3) {
            return var7 + 1;
         }

         var6 = var6.next();
      }

      return -1;
   }

   public static double Z(Vec3 var0, Vec3 var1, AABB var2) {
      return var2.contains(var0) ? 0.0 : var2.clip(var0, var1).<Double>map(var0::distanceToSqr).orElse(Double.POSITIVE_INFINITY);
   }

   public static double W(int var0) {
      return Math.clamp((var0 - 1) / 20.0, 0.0, 0.3);
   }

   public static Vec3 M(Vec3 var0, int var1) {
      double var2 = var0.length();
      return var0.scale((var2 > 0.6 ? 0.6 / var2 : 1.0) * Math.min(var1, 6));
   }

   private wt() {
   }

   public record k(Vec3 position, Vec3 velocity) {
      public Vec3 collisionEnd() {
         return this.position.add(this.velocity);
      }

      public wt.k next() {
         Vec3 var1 = this.velocity.add(0.0, -0.03, 0.0);
         return new wt.k(this.position.add(var1), var1.scale(0.92));
      }
   }

   public record u(float yaw, float pitch, int ticks) {
   }
}
