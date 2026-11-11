package com.larpclient.util.entity;

import net.minecraft.world.phys.Vec3;

public final class WindChargeBoost {
   public static final double tRACE_STEP = 1.5;
   public static final double tRACE_STEP_ALT = 1.2;
   public static final double bLAST_RADIUS = 2.4;
   public static final double bLAST_POWER = 1.22;
   public static final double bURST_OFFSET = 0.25;
   public static final double jUMP_VELOCITY = 0.42;
   public static final double gRAVITY = 0.08;
   public static final double dRAG = 0.98;
   public static final int mAX_TICKS = 6;
   public static final double mIN_PUSH = 0.3;
   public static final double sCORE_TOLERANCE = 0.9;

   public static Vec3 blastPush(Vec3 var0, double var1, Vec3 var3) {
      double var4 = var0.distanceTo(var3) / 2.4;
      if (var4 > 1.0) {
         return Vec3.ZERO;
      }

      Vec3 var6 = var0.add(0.0, var1, 0.0).subtract(var3);
      return var6.lengthSqr() < 1.0E-12 ? Vec3.ZERO : var6.normalize().scale((1.0 - var4) * 1.22);
   }

   public static WindChargeBoost.AimSolution traceCharge(Vec3 var0, Vec3 var1, Vec3 var2, WindChargeBoost.RaycastProvider var3) {
      Vec3 var4 = var1.normalize().scale(1.5).add(var2);
      Vec3 var5 = var0;

      for (int var6 = 1; var6 <= 6; var6++) {
         Vec3 var7 = var5.add(var4);
         WindChargeBoost.RayHit var8 = var3.raycast(var5, var7);
         if (var8 != null) {
            return new WindChargeBoost.AimSolution(0.0F, 0.0F, var6, var8.point().add(var8.normal().scale(0.25)), Vec3.ZERO);
         }

         var5 = var7;
      }

      return null;
   }

   public static Vec3 predictPosition(Vec3 var0, Vec3 var1, boolean var2, boolean var3, int var4) {
      double var5 = var0.y;
      double var7 = var3 && var2 ? 0.42 : (var2 ? 0.0 : var1.y);

      for (int var9 = 0; var9 < var4; var9++) {
         var5 += var7;
         var7 = (var7 - 0.08) * 0.98;
         if (var2 && !var3) {
            var5 = var0.y;
            var7 = 0.0;
         }
      }

      return new Vec3(var0.x + var1.x * var4, var5, var0.z + var1.z * var4);
   }

   public static WindChargeBoost.AimSolution findBestAim(
      Vec3 var0, double var1, Vec3 var3, boolean var4, boolean var5, float var6, boolean var7, WindChargeBoost.RaycastProvider var8
   ) {
      WindChargeBoost.AimSolution var9 = null;
      double var10 = Double.NEGATIVE_INFINITY;
      float var12 = var7 ? var6 + 180.0F : var6;

      for (int var13 = var7 ? 40 : 90; var13 <= 90; var13++) {
         WindChargeBoost.AimSolution var14 = simulateThrow(var0, var1, var3, var4, var5, var12, var13, var8);
         if (var14 != null) {
            double var15 = scorePush(var14, var6, var7);
            if (var14.push().length() >= 0.3 && var15 > var10) {
               var10 = var15;
               var9 = var14;
            }
         }
      }

      return var9;
   }

   public static WindChargeBoost.AimSolution simulateThrow(
      Vec3 var0, double var1, Vec3 var3, boolean var4, boolean var5, float var6, float var7, WindChargeBoost.RaycastProvider var8
   ) {
      Vec3 var9 = var0.add(0.0, var1, 0.0);
      Vec3 var10 = var4 ? new Vec3(var3.x, 0.0, var3.z) : var3;
      WindChargeBoost.AimSolution var11 = traceCharge(var9, Vec3.directionFromRotation(var7, var6), var10, var8);
      if (var11 == null) {
         return null;
      }

      Vec3 var12 = predictPosition(var0, var3, var4, var5, var11.ticks());
      return new WindChargeBoost.AimSolution(var6, var7, var11.ticks(), var11.burst(), blastPush(var12, var1, var11.burst()));
   }

   public static double scorePush(WindChargeBoost.AimSolution var0, float var1, boolean var2) {
      Vec3 var3 = var0.push();
      Vec3 var4 = Vec3.directionFromRotation(0.0F, var1);
      double var5 = var3.x * var4.x + var3.z * var4.z;
      return var2 ? (var3.y >= 0.15 ? var5 + 0.25 * var3.y : Double.NEGATIVE_INFINITY) : var3.y;
   }

   public static boolean isBetterAim(WindChargeBoost.AimSolution var0, WindChargeBoost.AimSolution var1, float var2, boolean var3) {
      if (var0 != null && var1 != null && !(var1.push().length() < 0.3)) {
         double var4 = scorePush(var0, var2, var3);
         double var6 = scorePush(var1, var2, var3);
         return Double.isFinite(var6) && var6 >= (var4 > 0.0 ? var4 * 0.9 : var4);
      } else {
         return false;
      }
   }

   public static int traceCharge(int var0, int var1, int var2) {
      int var3 = var2 > 0 ? var2 : var0 + 1 + Math.max(0, var1);
      return Math.max(0, var3 - 2);
   }

   public static double fallDistance(double var0) {
      double var2 = 0.0;

      for (int var4 = 0; var4 < 200 && var0 > 0.0; var4++) {
         var2 += var0;
         var0 = (var0 - 0.08) * 0.98;
      }

      return var2;
   }

   private WindChargeBoost() {
   }

   public record AimSolution(float yaw, float pitch, int ticks, Vec3 burst, Vec3 push) {
   }

   public record RayHit(Vec3 point, Vec3 normal) {
   }

   public interface RaycastProvider {
      WindChargeBoost.RayHit raycast(Vec3 var1, Vec3 var2);
   }
}
