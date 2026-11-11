package com.larpclient.util.math;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class AimAssistMath {
   public static final long sTICKY_TARGET_MAX_MS = 1500L;
   public static final long iNPUT_TIMEOUT_MS = 150L;
   public static final double rANGE_LEEWAY = 0.5;
   public static final double aNGLE_LEEWAY_FACTOR = 1.3;
   public static final double mAX_DELTA_TIME = 0.05;

   public static boolean isWithinLimits(double var0, double var2, double var4, double var6) {
      return Double.isFinite(var0) && Double.isFinite(var2) && var0 >= 0.0 && var2 >= 0.0 && var0 <= var4 && var2 <= var6;
   }

   public static <T> T selectTarget(List<AimAssistMath.Candidate<T>> var0, T var1, long var2, T var4, double var5, double var7) {
      double var9 = var7 / 2.0;

      for (AimAssistMath.Candidate var12 : var0) {
         if (var12.entity == var1 && var2 >= 0L && var2 <= 1500L && isWithinLimits(var12.distance, var12.angle, var5, var9)) {
            return (T)var1;
         }
      }

      for (AimAssistMath.Candidate var18 : var0) {
         if (var18.entity == var4 && isWithinLimits(var18.distance, var18.angle, var5 + 0.5, var9 * 1.3)) {
            return (T)var4;
         }
      }

      Object var17 = null;
      double var19 = var9;

      for (AimAssistMath.Candidate var15 : var0) {
         if (isWithinLimits(var15.distance, var15.angle, var5, var19)) {
            var17 = var15.entity;
            var19 = var15.angle;
         }
      }

      return (T)var17;
   }

   public static AABB shrinkBox(AABB var0) {
      return var0.deflate(var0.getXsize() * 0.25, var0.getYsize() * 0.1, var0.getXsize() * 0.25);
   }

   public static Vec3 clampToBox(Vec3 var0, AABB var1) {
      return new Vec3(Math.clamp(var0.x, var1.minX, var1.maxX), Math.clamp(var0.y, var1.minY, var1.maxY), Math.clamp(var0.z, var1.minZ, var1.maxZ));
   }

   public static float[] applyMouseDelta(float var0, float var1, double var2, double var4) {
      return new float[]{var0 + (float)var2 * 0.15F, Math.clamp(var1 + (float)var4 * 0.15F, -90.0F, 90.0F)};
   }

   public static Vec3 findAimPoint(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      if (var2.contains(var0)) {
         return var0;
      }

      Optional var5 = var2.clip(var0, var0.add(var1.scale(var3 + 1.0)));
      return var5.orElseGet(() -> clampToBox(var0.add(var1.scale(var0.distanceTo(var2.getCenter()))), var2));
   }

   public static float[] computeRotationToBox(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      if (!var2.contains(var0) && !var2.clip(var0, var0.add(var1.scale(var3 + 1.0))).isPresent()) {
         Vec3 var5 = findAimPoint(var0, var1, var2, var3).subtract(var0);
         return new float[]{
            (float)AimMath.wrapDegrees(Math.toDegrees(Math.atan2(var5.z, var5.x)) - 90.0),
            (float)Math.clamp(-Math.toDegrees(Math.atan2(var5.y, Math.hypot(var5.x, var5.z))), -90.0, 90.0)
         };
      } else {
         return null;
      }
   }

   public static double rotationDistance(float var0, float var1, float[] var2) {
      return var2 == null ? 0.0 : Math.hypot(AimMath.wrapDegrees(var2[0] - var0), var2[1] - var1);
   }

   public static double calculateGcdStep(double var0, boolean var2, boolean var3) {
      double var4 = var0 * 0.6F + 0.2F;
      return var4 * var4 * var4 * (var2 && var3 ? 1 : 8);
   }

   private AimAssistMath() {
   }

   public record Candidate<T>(T entity, double distance, double angle) {
   }

   public record MouseDelta(double yaw, double pitch) {
      public static final AimAssistMath.MouseDelta STILL = new AimAssistMath.MouseDelta(0.0, 0.0);
   }

   public static final class MouseStepper {
      private double yawRemainder;
      private double pitchRemainder;
      private double yawSign;
      private double pitchSign;
      private long lastInputMs;
      private long lastYawInputMs;
      private long lastPitchInputMs;
      private boolean hasInput;

      public AimAssistMath.MouseDelta step(
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
               this.lastInputMs = var19;
               this.hasInput = (boolean)1;
            }

            if (var1 != 0.0) {
               this.yawSign = Math.signum(var1);
               this.lastYawInputMs = var19;
            } else if (this.yawSign != 0.0 && (var19 < this.lastYawInputMs || var19 - this.lastYawInputMs > 150L)) {
               this.yawSign = 0.0;
               this.yawRemainder = 0.0;
            }

            if (var3 != 0.0) {
               this.pitchSign = Math.signum(var3);
               this.lastPitchInputMs = var19;
            } else if (this.pitchSign != 0.0 && (var19 < this.lastPitchInputMs || var19 - this.lastPitchInputMs > 150L)) {
               this.pitchSign = 0.0;
               this.pitchRemainder = 0.0;
            }

            if (var9 && this.hasInput && var19 >= this.lastInputMs && var19 - this.lastInputMs <= 150L) {
               double var21 = Math.min(var11, 0.05);
               double var23 = var13 * var15 * var21;
               double var25 = -Math.expm1(-12.0 * var21);
               double var27 = (float)var17 * 0.15F;
               double var29 = AimMath.wrapDegrees(var5);
               if (this.yawSign * var29 < 0.0) {
                  var29 = 0.0;
               }

               double var31 = var10 && this.pitchSign * var7 >= 0.0 ? var7 : 0.0;
               if (var29 == 0.0 || this.yawRemainder * var29 < 0.0) {
                  this.yawRemainder = 0.0;
               }

               if (var31 == 0.0 || this.pitchRemainder * var31 < 0.0) {
                  this.pitchRemainder = 0.0;
               }

               this.yawRemainder = this.yawRemainder + Math.clamp(var29 * var25, -var23, var23) / var27;
               this.pitchRemainder = this.pitchRemainder + Math.clamp(var31 * var25 * 0.6, -var23, var23) / var27;
               long var33 = (long)this.yawRemainder;
               long var35 = (long)this.pitchRemainder;
               this.yawRemainder -= var33;
               this.pitchRemainder -= var35;
               return new AimAssistMath.MouseDelta(var33 * var17, var35 * var17);
            } else {
               this.clearRemainders();
               this.yawSign = this.pitchSign = 0.0;
               return AimAssistMath.MouseDelta.STILL;
            }
         } else {
            this.reset();
            return AimAssistMath.MouseDelta.STILL;
         }
      }

      public void clearRemainders() {
         this.yawRemainder = this.pitchRemainder = 0.0;
      }

      public void reset() {
         this.clearRemainders();
         this.yawSign = this.pitchSign = 0.0;
         this.lastInputMs = 0L;
         this.lastYawInputMs = this.lastPitchInputMs = 0L;
         this.hasInput = (boolean)0;
      }

      public double isInputRecent() {
         return this.yawRemainder;
      }

      public double getPitchRemainder() {
         return this.pitchRemainder;
      }

      public boolean isInputRecent(long var1) {
         return this.hasInput && var1 >= this.lastInputMs && var1 - this.lastInputMs <= 150L;
      }

      public boolean opposesInput(double var1, double var3, boolean var5) {
         int var6 = var1 != 0.0 ? 1 : 0;
         int var7 = var5 && var3 != 0.0 ? 1 : 0;
         return (var6 != 0 || var7 != 0) && (var6 == 0 || !(this.yawSign * var1 >= 0.0)) && (var7 == 0 || !(this.pitchSign * var3 >= 0.0));
      }
   }
}
