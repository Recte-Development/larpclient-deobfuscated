package com.larpclient.util.entity;

import com.larpclient.module.combat.AimNoiseGenerator;
import com.larpclient.module.combat.Rotation;
import com.larpclient.module.combat.RotationMath;
import com.larpclient.module.combat.RotationQuantizer;
import com.larpclient.util.math.RotationLimiter;
import java.util.Locale;
import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class RotationController {
   public static final double maxSpeed = 180.0;
   public static final float speedRampFraction = 0.55F;
   public static final float distanceFraction = 0.8F;
   public static final float defaultSmoothing = 1.0F;
   public static final float overshootDistance = 30.0F;
   public static final float minStep = 0.5F;
   public static final float minTolerance = 0.15F;
   private static final double overshootChance = 0.3;
   private static final float closeRange = 6.0F;
   private static final int maxLoggedTurns = 16;
   private final SplittableRandom random;
   private final AimNoiseGenerator humanizer;
   private final RotationQuantizer smoother = new RotationQuantizer();
   private float lastStep;
   private float overshootYaw;
   private float overshootPitch;
   private boolean overshooting;
   private final StringBuilder turnLog = new StringBuilder();
   private int turnTicks;
   private float totalDegrees;
   private float fastestStep;

   public RotationController() {
      this(new SplittableRandom());
   }

   public RotationController(SplittableRandom var1) {
      this.random = var1;
      this.humanizer = new AimNoiseGenerator(AimNoiseGenerator.pROFILE_STEADY, var1.split());
   }

   public float[] rotateToward(float var1, float var2, float var3, float var4, double var5, boolean var7) {
      return this.rotateTowardSmoothed(var1, var2, var3, var4, var5, var7, RotationMath.mouseStep());
   }

   public float[] rotateTowardSmoothed(float var1, float var2, float var3, float var4, double var5, boolean var7, float var8) {
      float var9 = Mth.clamp(var4, -90.0F, 90.0F);
      float[] var10 = var7
         ? this.rotateHumanized(var1, var2, var3, var9, (float)Math.clamp(var5, 1.0, 180.0), var8)
         : rotateLinear(var1, var2, var3, var9, var5);
      this.recordTurn(var1, var2, var10);
      return var10;
   }

   public static float[] rotateLinear(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.clamp(var3, -90.0F, 90.0F);
      return var4 >= 180.0 ? new float[]{var0 + Mth.wrapDegrees(var2 - var0), var6} : RotationLimiter.stepToward(var0, var1, var2, var6, var4);
   }

   private float[] rotateHumanized(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (var9 < 1.0E-4F) {
         this.lastStep = 0.0F;
         this.overshooting = (boolean)0;
         return new float[]{var1, var2};
      }

      if (this.lastStep == 0.0F && !this.overshooting && var9 > 30.0F && this.random.nextDouble() < 0.3) {
         float var10 = (float)(0.02 + this.random.nextDouble() * 0.04);
         this.overshootYaw = var7 * var10;
         this.overshootPitch = var8 * var10;
         this.overshooting = (boolean)1;
      }

      if (this.overshooting) {
         float var18 = var7 + this.overshootYaw;
         float var11 = Mth.clamp(var4 + this.overshootPitch, -90.0F, 90.0F) - var2;
         float var12 = (float)Math.hypot(var18, var11);
         if (var12 <= 1.5F) {
            this.overshooting = (boolean)0;
         } else {
            var7 = var18;
            var8 = var11;
            var9 = var12;
         }
      }

      int var19 = var9 < 6.0F ? 1 : 0;
      this.humanizer.updateWrist();
      float var20 = this.humanizer.subMovementFactor(var9, (boolean)var19);
      float var21 = this.humanizer.nextGain(AimNoiseGenerator.Axis.YAW, (boolean)var19) + this.humanizer.sampleNoise(AimNoiseGenerator.Axis.YAW) * 0.05F;
      float var13 = this.humanizer.nextGain(AimNoiseGenerator.Axis.PITCH, (boolean)var19) + this.humanizer.sampleNoise(AimNoiseGenerator.Axis.PITCH) * 0.05F;
      float var14;
      float var15;
      if (var9 <= 1.0F) {
         var14 = var7;
         var15 = var8;
      } else {
         float var16 = Math.min(var5, this.lastStep + var5 * 0.55F);
         var16 = Math.min(var16, Math.max(1.0F, var9 * 0.8F)) * var20;
         var14 = var7 / var9 * var16 * var21;
         var15 = var8 / var9 * var16 * var13;
         if (var19 == 0) {
            var15 += this.humanizer.wristPitchOffset(var14);
         }

         var14 = clampStep(var14, var7);
         var15 = clampStep(var15, var8);
         float var17 = (float)Math.hypot(var14, var15);
         if (var17 > var5) {
            var14 *= var5 / var17;
            var15 *= var5 / var17;
         }
      }

      Rotation var25 = this.smoother.quantize(new Rotation(var1, var2), new Rotation(var1 + var14, Mth.clamp(var2 + var15, -90.0F, 90.0F)), var6);
      this.lastStep = (float)Math.hypot(var25.yaw - var1, var25.pitch - var2);
      return new float[]{var25.yaw, var25.pitch};
   }

   private static float clampStep(float var0, float var1) {
      return var1 >= 0.0F ? Mth.clamp(var0, -0.5F, var1) : Mth.clamp(var0, var1, 0.5F);
   }

   public static boolean isOnTarget(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Math.max(0.15F, var4);
      return Math.abs(Mth.wrapDegrees(var2 - var0)) <= var5 && Math.abs(var3 - var1) <= var5;
   }

   public static float boostSpeed(boolean var0, float var1, float var2) {
      return var0 ? Math.max(var1, var2 * 0.6F) : var1;
   }

   public void reset() {
      this.humanizer.reset();
      this.smoother.reset();
      this.lastStep = this.overshootYaw = this.overshootPitch = 0.0F;
      this.overshooting = (boolean)0;
   }

   public void clampStep() {
      this.turnLog.setLength(0);
      this.turnTicks = 0;
      this.totalDegrees = this.fastestStep = 0.0F;
   }

   public int getTurnTicks() {
      return this.turnTicks;
   }

   public String summarize() {
      return String.format(
         Locale.ROOT,
         "%d ticks, %.1f degrees, fastest %.1f/tick: %s",
         this.turnTicks,
         this.totalDegrees,
         this.fastestStep,
         this.turnLog.length() == 0 ? "no turn" : this.turnLog
      );
   }

   private void recordTurn(float var1, float var2, float[] var3) {
      float var4 = (float)Math.hypot(Mth.wrapDegrees(var3[0] - var1), var3[1] - var2);
      if (!(var4 <= 0.0F)) {
         this.turnTicks++;
         this.totalDegrees += var4;
         this.fastestStep = Math.max(this.fastestStep, var4);
         if (this.turnTicks <= 16) {
            if (this.turnLog.length() > 0) {
               this.turnLog.append(" > ");
            }

            this.turnLog.append(String.format(Locale.ROOT, "%.1f/%.1f", Mth.wrapDegrees(var3[0]), var3[1]));
         } else if (this.turnTicks == 17) {
            this.turnLog.append(" > ...");
         }
      }
   }
}
