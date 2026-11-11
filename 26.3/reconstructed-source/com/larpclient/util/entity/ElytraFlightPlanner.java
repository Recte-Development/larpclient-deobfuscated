package com.larpclient.util.entity;

import net.minecraft.util.Mth;

public final class ElytraFlightPlanner {
   public static final double cLIMB_START_DISTANCE = 45.0;
   public static final double bASE_MARGIN = 14.0;
   public static final double cLIMB_SLOPE = 0.095;
   static final int sIMULATION_STEPS = 60;
   static final int aPPROACH_STEPS = 90;
   static final float mAX_YAW_RATE = 12.0F;
   static final float mAX_PITCH_RATE = 9.0F;
   private static final float[] cRUISE_YAW_OFFSETS = new float[]{0.0F, -12.0F, 12.0F};
   private static final float[] wIDE_YAW_OFFSETS = new float[]{-30.0F, 30.0F, -55.0F, 55.0F, -90.0F, 90.0F, -140.0F, 140.0F};
   private static final float[] cRUISE_PITCHES = new float[]{-62.0F, -46.0F, -33.0F, -23.0F, -15.0F, -9.0F, -4.0F, 1.0F, 6.0F, 12.0F, 19.0F, 27.0F, 36.0F};
   private static final float[] aPPROACH_YAW_OFFSETS = new float[]{0.0F, -18.0F, 18.0F, -36.0F, 36.0F, -60.0F, 60.0F, -90.0F, 90.0F};
   private static final float[] aPPROACH_PITCHES = new float[]{-28.0F, -18.0F, -10.0F, -4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F, 21.0F, 27.0F, 34.0F};
   private static final float[] dIVE_PITCHES = new float[]{-4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F};
   private static final float[] fLARE_PITCHES = new float[]{-28.0F, -18.0F};
   private static final int[] fLARE_START_STEPS = new int[]{10, 25, 45};
   private final ElytraPhysics.FlightState scratchState = new ElytraPhysics.FlightState();
   private double requiredAltitude = Double.NEGATIVE_INFINITY;
   private int sampleCount;

   public double getRequiredAltitude() {
      return this.requiredAltitude;
   }

   public int simulateLanding() {
      return this.sampleCount;
   }

   public ElytraFlightPlanner.Trajectory plan(
      ElytraFlightPlanner.HeightSampler var1, ElytraPhysics.FlightState var2, int var3, boolean var4, ElytraFlightPlanner.Goal var5
   ) {
      this.sampleCount = 0;
      float var6 = ElytraPhysics.yawTo(var2.posX, var2.posZ, var5.targetX, var5.targetZ);
      float var7 = var2.yaw + Mth.wrapDegrees(var6 - var2.yaw);
      this.requiredAltitude = this.terrainAhead(var1, var2.posX, var2.posZ, var6, var5) + var5.clearance + 4.0;
      ElytraFlightPlanner.Trajectory var8 = this.searchCandidates(var1, var2, var3, var4, var5, var7, cRUISE_YAW_OFFSETS, false, null);
      if (needsWideSearch(var8, var5, var2)) {
         var8 = this.searchCandidates(var1, var2, var3, var4, var5, var7, wIDE_YAW_OFFSETS, true, var8);
      }

      return var8;
   }

   private ElytraFlightPlanner.Trajectory searchCandidates(
      ElytraFlightPlanner.HeightSampler var1,
      ElytraPhysics.FlightState var2,
      int var3,
      boolean var4,
      ElytraFlightPlanner.Goal var5,
      float var6,
      float[] var7,
      boolean var8,
      ElytraFlightPlanner.Trajectory var9
   ) {
      int var10 = var4 && var5.useRockets && var3 <= 0 ? 1 : 0;

      for (float var14 : var7) {
         for (float var18 : cRUISE_PITCHES) {
            for (int var19 = 0; var19 < (var10 != 0 ? 2 : 1); var19++) {
               ElytraFlightPlanner.Trajectory var20 = this.simulate(var1, var2, var3, var19 == 1, var5, var6 + var14, var18, var8);
               double var21 = var20.cost() + Math.abs(var14) * 0.08;
               if (var9 == null || var21 < var9.cost()) {
                  var9 = new ElytraFlightPlanner.Trajectory(
                     var20.yaw(), var20.pitch(), var20.rocket(), var21, var20.hitsGround(), var20.minClearance(), var20.endY(), var20.endSpeed(), var8
                  );
               }
            }
         }
      }

      return var9;
   }

   static boolean needsWideSearch(ElytraFlightPlanner.Trajectory var0, ElytraFlightPlanner.Goal var1, ElytraPhysics.FlightState var2) {
      double var3 = var1.descentSlack(var1.distanceTo(var2.posX, var2.posZ));
      return var0.hitsGround() || var0.minClearance() < var3 * 0.6 || var0.cost() > 400.0 || var0.endSpeed() < 0.45 && var0.minClearance() < 40.0;
   }

   ElytraFlightPlanner.Trajectory simulate(
      ElytraFlightPlanner.HeightSampler var1,
      ElytraPhysics.FlightState var2,
      int var3,
      boolean var4,
      ElytraFlightPlanner.Goal var5,
      float var6,
      float var7,
      boolean var8
   ) {
      ElytraPhysics.FlightState var9 = this.scratchState.copy(var2);
      int var10 = var4 ? ElytraPhysics.boostDurationTicks(var5.rocketCount) : Math.max(0, var3);
      double var11 = var5.distanceTo(var9.posX, var9.posZ);
      double var13 = Double.MAX_VALUE;
      double var15 = 0.0;
      double var17 = 0.0;
      double var19 = 0.0;
      double var21 = 0.0;
      double var23 = 0.0;
      double var25 = 0.0;
      int var27 = -1;

      for (int var28 = 0; var28 < 60; var28++) {
         var9.yaw = ElytraPhysics.stepYaw(var9.yaw, var6, 12.0F);
         var9.pitch = Mth.clamp(ElytraPhysics.stepYaw(var9.pitch, var7, 9.0F), -90.0F, 90.0F);
         ElytraPhysics.lookY(var9, var5.lookPitch);
         ElytraPhysics.advancePosition(var9);
         if (var10 > 0) {
            ElytraPhysics.applyFireworkBoost(var9);
            var10--;
         }

         double var29 = var5.distanceTo(var9.posX, var9.posZ);
         double var31 = var9.posY - this.groundHeight(var1, var9.posX, var9.posZ, var5);
         if (var31 < 0.05) {
            var27 = var28;
            break;
         }

         if (var31 < 24.0
            && var28 > 0
            && var9.posY - var9.velY * 0.5 - this.groundHeight(var1, var9.posX - var9.velX * 0.5, var9.posZ - var9.velZ * 0.5, var5) < 0.05) {
            var27 = var28;
            break;
         }

         var13 = Math.min(var13, var31);
         double var33 = Math.max(Math.max(var5.descentSlack(var29), var9.speed() * 5.0), -var9.velY * 25.0);
         if (var31 < var33) {
            double var35 = var33 - var31;
            var15 += var35 * var35 * 0.5 + var35 * 2.0;
         }

         if (var9.velY < -0.5) {
            var21 += (-var9.velY - 0.5) * 25.0;
         }

         if (var31 < 8.0 && var9.velY < -0.5) {
            var23 += (-var9.velY - 0.5) * 30.0;
         }

         if (var9.posY < var5.minEndY) {
            var25 += (var5.minEndY - var9.posY) * 4.0;
         }

         double var43 = var28 < 20 ? 0.4 : 1.0;
         var17 += Math.abs(this.altitudeError(var5, var9.posY, var29)) * var43;
         var19 += var43;
      }

      double var42 = var5.distanceTo(var9.posX, var9.posZ);
      double var30 = var9.speed();
      double var32 = var11 - var42;
      double var34;
      if (var27 >= 0) {
         var34 = 1000000 + (60 - var27) * 1000;
      } else {
         double var36 = this.altitudeError(var5, var9.posY, var42);
         var34 = (var36 < 0.0 ? -var36 * (var5.hasTargetY() ? 4.0 : 2.2) : var36 * (var5.hasTargetY() ? 1.6 : 0.6))
            + var17 / Math.max(1.0E-6, var19) * 0.4
            - var32 * (var5.hasTargetY() ? 0.35 : 1.0)
            - (
               var5.hasTargetY()
                  ? 0.0
                  : (this.cappedAltitude(var5, var9.posY, var42) - this.cappedAltitude(var5, var2.posY, var11) + speedEnergy(var30) - speedEnergy(var2.speed()))
                     * 9.0
            )
            + var15 / 60.0 * 10.0
            + var25 / 60.0
            + var23
            + var21 / 60.0 * 10.0
            + (var4 ? var5.rocketBonus : 0.0);
         if (var30 < 0.55) {
            var34 += (0.55 - var30) * 60.0;
         }

         if (var5.hasTargetY() && var42 < 120.0 && var30 > var5.maxEndSpeed) {
            var34 += (var30 - var5.maxEndSpeed) * 40.0;
         }

         double var38 = Math.atan2(-var9.velX, var9.velZ);
         double var40 = Math.atan2(-(var5.targetX - var9.posX), var5.targetZ - var9.posZ);
         if (var42 > 8.0) {
            var34 += Math.abs(Mth.wrapDegrees(Math.toDegrees(var38 - var40))) * 0.06;
         }
      }

      return new ElytraFlightPlanner.Trajectory(var6, var7, var4, var34, var27 >= 0, var27 >= 0 ? -1.0 : var13, var9.posY, var30, var8);
   }

   double minAltitude(ElytraFlightPlanner.Goal var1, double var2) {
      double var4 = var1.cruiseAltitude(var2);
      double var6 = var4 - (var1.hasTargetY() ? 6 : 45);
      if (!var1.hasTargetY() || var2 >= 67.5) {
         var6 = Math.max(var6, Math.min(this.requiredAltitude, var4 + 400.0));
      }

      return var6;
   }

   double maxAltitude(ElytraFlightPlanner.Goal var1, double var2) {
      return Math.max(var1.cruiseAltitude(var2) + (var1.hasTargetY() ? 6 : 15), this.minAltitude(var1, var2) + 10.0);
   }

   double altitudeError(ElytraFlightPlanner.Goal var1, double var2, double var4) {
      double var6 = this.minAltitude(var1, var4);
      double var8 = this.maxAltitude(var1, var4);
      return var2 < var6 ? var2 - var6 : (var2 > var8 ? var2 - var8 : 0.0);
   }

   private double cappedAltitude(ElytraFlightPlanner.Goal var1, double var2, double var4) {
      return Math.min(var2, this.maxAltitude(var1, var4));
   }

   static double speedEnergy(double var0) {
      return var0 * var0 / 0.16 * 0.8;
   }

   int groundHeight(ElytraFlightPlanner.HeightSampler var1, double var2, double var4, ElytraFlightPlanner.Goal var6) {
      int var7 = Integer.MIN_VALUE;
      byte var8 = 0;

      for (int var9 = Mth.floor(var2 - 0.3); var9 <= Mth.floor(var2 + 0.3); var9++) {
         for (int var10 = Mth.floor(var4 - 0.3); var10 <= Mth.floor(var4 + 0.3); var10++) {
            int var11 = var1.heightAt(var9, var10);
            this.sampleCount++;
            if (var11 == Integer.MIN_VALUE) {
               var8 = 1;
            } else {
               var7 = Math.max(var7, var11);
            }
         }
      }

      if (var8 != 0) {
         var7 = Math.max(var7, (int)Math.ceil(var6.flightCeiling - var6.clearance));
      }

      return var7 == Integer.MIN_VALUE ? -100000 : var7;
   }

   double terrainAhead(ElytraFlightPlanner.HeightSampler var1, double var2, double var4, float var6, ElytraFlightPlanner.Goal var7) {
      double var8 = var7.distanceTo(var2, var4);
      double var10 = Math.min(320.0, var7.hasTargetY() ? Math.max(0.0, var8 - 27.0) : var8 + 24.0);
      double var12 = ElytraPhysics.lookX(var6, 0.0F);
      double var14 = ElytraPhysics.lookZ(var6, 0.0F);
      double var16 = Double.NEGATIVE_INFINITY;

      for (double var18 = 0.0; var18 <= var10; var18 += 4.0) {
         for (byte var20 = -8; var20 <= 8; var20 += 8) {
            int var21 = var1.heightAt(Mth.floor(var2 + var12 * var18 - var14 * var20), Mth.floor(var4 + var14 * var18 + var12 * var20));
            this.sampleCount++;
            if (var21 == Integer.MIN_VALUE) {
               if (var18 <= 200.0) {
                  var16 = Math.max(var16, var7.flightCeiling - var7.clearance);
               }
            } else {
               var16 = Math.max(var16, var21);
            }
         }
      }

      return var16 == Double.NEGATIVE_INFINITY ? -100000.0 : var16;
   }

   public ElytraFlightPlanner.Aim planLanding(
      ElytraFlightPlanner.HeightSampler var1,
      ElytraFlightPlanner.SolidQuery var2,
      ElytraPhysics.FlightState var3,
      double var4,
      double var6,
      double var8,
      int var10,
      double var11
   ) {
      double var13 = Math.hypot(var4 - var3.posX, var8 - var3.posZ);
      float var15 = var13 > 2.5 ? var3.yaw + Mth.wrapDegrees(ElytraPhysics.yawTo(var3.posX, var3.posZ, var4, var8) - var3.yaw) : var3.yaw;
      double var16 = Double.MAX_VALUE;
      float var18 = var15;
      float var19 = 0.0F;
      byte var20 = 0;

      for (float var24 : aPPROACH_YAW_OFFSETS) {
         for (float var28 : aPPROACH_PITCHES) {
            double[] var29 = this.simulateLanding(var1, var2, var3, var15 + var24, var28, -1, var28, var4, var6, var8, var10, var11);
            double var30 = var29[0] + Math.abs(var24) * 0.15;
            if (var30 < var16) {
               var16 = var30;
               var18 = var15 + var24;
               var19 = var28;
               var20 = (byte)(var29[1] > 0.0 ? 1 : 0);
            }
         }
      }

      if (var13 > 8.0 && (var20 == 0 || var16 >= 40.0)) {
         for (float var43 : cRUISE_YAW_OFFSETS) {
            for (float var47 : dIVE_PITCHES) {
               for (int var32 : fLARE_START_STEPS) {
                  for (float var36 : fLARE_PITCHES) {
                     double[] var37 = this.simulateLanding(var1, var2, var3, var15 + var43 * 1.5F, var47, var32, var36, var4, var6, var8, var10, var11);
                     double var38 = var37[0] + Math.abs(var43 * 1.5F) * 0.15 + 2.0;
                     if (var38 < var16) {
                        var16 = var38;
                        var18 = var15 + var43 * 1.5F;
                        var19 = var47;
                        var20 = (byte)(var37[1] > 0.0 ? 1 : 0);
                     }
                  }
               }
            }
         }
      }

      return new ElytraFlightPlanner.Aim(var18, var19, var16, (boolean)var20);
   }

   double[] simulateLanding(
      ElytraFlightPlanner.HeightSampler var1,
      ElytraFlightPlanner.SolidQuery var2,
      ElytraPhysics.FlightState var3,
      float var4,
      float var5,
      int var6,
      float var7,
      double var8,
      double var10,
      double var12,
      int var14,
      double var15
   ) {
      ElytraPhysics.FlightState var17 = this.scratchState.copy(var3);
      int var18 = Math.max(0, var14);
      double var19 = 0.0;
      double var21 = var8 - var17.posX;
      double var23 = var12 - var17.posZ;
      double var25 = Math.hypot(var21, var23);
      double var27 = var25 > 0.001 ? var21 / var25 : 0.0;
      double var29 = var25 > 0.001 ? var23 / var25 : 0.0;

      for (int var31 = 0; var31 < 90; var31++) {
         var17.yaw = ElytraPhysics.stepYaw(var17.yaw, var4, 20.0F);
         var17.pitch = Mth.clamp(ElytraPhysics.stepYaw(var17.pitch, var6 >= 0 && var31 >= var6 ? var7 : var5, 13.0F), -90.0F, 90.0F);
         double var32 = var17.speed();
         ElytraPhysics.lookY(var17, var15);
         double var34 = var17.posX + var17.velX;
         double var36 = var17.posZ + var17.velZ;
         double var38 = var17.posY + var17.velY;
         int var40 = this.heightAt(var1, var34, var36);
         if (var40 != Integer.MIN_VALUE && var40 > var17.posY + 0.6) {
            double var41 = var32 * 10.0 - 3.0;
            if (var41 > 0.0) {
               return new double[]{1000.0 + var41 * 60.0, 0.0};
            }

            var19 += 12.0;
            var17.velX = var17.velZ = 0.0;
            var34 = var17.posX;
            var36 = var17.posZ;
         }

         int var54 = this.heightAt(var1, var34, var36);
         if (var54 != Integer.MIN_VALUE && var38 <= var54) {
            double var42 = var17.velY;
            var17.posX = var34;
            var17.posZ = var36;
            var17.posY = var54;
            if (var42 > 0.0) {
               return new double[]{600 + var31, 0.0};
            }

            int var44 = Mth.floor(var17.posX);
            int var45 = Mth.floor(var17.posZ);
            double var46 = Math.hypot(var17.posX - var8, var17.posZ - var12);
            if (var2 != null && !var2.isSolid(var44, var54, var45)) {
               return new double[]{850.0 + var46, 0.0};
            }

            int var48 = Math.abs(var17.posY - var10) < 1.2 && var46 < 7.0 ? 1 : 0;
            double var49 = this.wallClearance(var1, var17.posX, var17.posY, var17.posZ, var17.velX, var17.velZ);
            double var51 = (var48 != 0 ? 0 : 150) + var49 + var46 * 0.6 + var17.speed() * 7.0 + Math.max(0.0, -var42 - 0.5) * 45.0 + var19;
            return new double[]{var51, var48 != 0 && var49 == 0.0 ? 1.0 : 0.0};
         }

         var17.posX = var34;
         var17.posY = var38;
         var17.posZ = var36;
         if (var18 > 0) {
            ElytraPhysics.applyFireworkBoost(var17);
            var18--;
         }

         if (var17.posY < var10 - 6.0) {
            return new double[]{800.0, 0.0};
         }
      }

      double var53 = Math.hypot(var17.posX - var8, var17.posZ - var12);
      double var33 = var17.posY - var10;
      double var35 = Math.max(0.0, -((var8 - var17.posX) * var27 + (var12 - var17.posZ) * var29));
      return new double[]{
         Math.min(280.0, 120.0 + Math.abs(var33 - var53 * 0.22) * 4.0 + var53 * 0.4 + Math.max(0.0, var17.speed() - 1.15) * 25.0 + var35 * 1.5 + var19), 0.0
      };
   }

   private int heightAt(ElytraFlightPlanner.HeightSampler var1, double var2, double var4) {
      this.sampleCount++;
      return var1.heightAt(Mth.floor(var2), Mth.floor(var4));
   }

   double wallClearance(ElytraFlightPlanner.HeightSampler var1, double var2, double var4, double var6, double var8, double var10) {
      for (int var12 = 0; var12 < 14; var12++) {
         var2 += var8;
         var6 += var10;
         int var13 = this.heightAt(var1, var2, var6);
         if (var13 == Integer.MIN_VALUE || var4 - var13 > 1.2) {
            return 400.0;
         }

         if (var13 > var4 + 0.6) {
            return 0.0;
         }

         var8 *= 0.546;
         var10 *= 0.546;
         if (Math.hypot(var8, var10) < 0.02) {
            return 0.0;
         }
      }

      return 0.0;
   }

   public record Aim(float yaw, float pitch, double cost, boolean onTarget) {
   }

   public static final class Goal {
      public double targetX;
      public double targetZ;
      public double cruiseCeiling = 290.0;
      public double targetY = Double.NaN;
      public double clearance = 14.0;
      public double flightCeiling = 262.0;
      public double minEndY = Double.NEGATIVE_INFINITY;
      public double lookPitch = 0.08;
      public boolean useRockets = (boolean)1;
      public double rocketBonus = 70.0;
      public int rocketCount = 3;
      public double maxEndSpeed = 1.45;

      public boolean hasTargetY() {
         return !Double.isNaN(this.targetY);
      }

      public double cruiseAltitude(double var1) {
         return !this.hasTargetY() ? this.cruiseCeiling : Math.min(this.cruiseCeiling, this.targetY + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095);
      }

      public double descentSlack(double var1) {
         return !this.hasTargetY() ? this.clearance : Math.max(3.0, Math.min(this.clearance, (var1 - 45.0) * 0.25 + 4.0));
      }

      public double distanceTo(double var1, double var3) {
         return Math.hypot(this.targetX - var1, this.targetZ - var3);
      }
   }

   public interface HeightSampler {
      int nO_HEIGHT = Integer.MIN_VALUE;

      int heightAt(int var1, int var2);
   }

   public interface SolidQuery {
      boolean isSolid(int var1, int var2, int var3);
   }

   public record Trajectory(
      float yaw, float pitch, boolean rocket, double cost, boolean hitsGround, double minClearance, double endY, double endSpeed, boolean wide
   ) {
   }
}
