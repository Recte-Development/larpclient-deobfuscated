package com.larpclient.module.combat;

import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class AimNoiseGenerator {
   public static final AimNoiseGenerator.NoiseProfile pROFILE_STEADY = new AimNoiseGenerator.NoiseProfile(
      5, 0.68F, 1.1F, 2, 5, 0.09F, 0.56F, 0.04F, 18.0F, 0.34, 0.78F, 0.12F, 0.48F, 0.19F, 0.23F, 0.1, 0.07F, 0.042F, 0.32F, 0.062F
   );
   public static final AimNoiseGenerator.NoiseProfile pROFILE_JITTERY = new AimNoiseGenerator.NoiseProfile(
      9, 0.74F, 1.14F, 1, 4, 0.125F, 0.66F, 0.048F, 24.0F, 0.28, 0.72F, 0.15F, 0.54F, 0.24F, 0.27F, 0.28, 0.055F, 0.055F, 0.35F, 0.078F
   );
   private final SplittableRandom random;
   private final AimNoiseGenerator.NoiseProfile profile;
   private final AimNoiseGenerator.NoiseChannel yawChannel;
   private final AimNoiseGenerator.NoiseChannel pitchChannel;
   private float wristAngle;
   private boolean subMovementActive;
   private float subMovementProgress;
   private float ballisticScale;

   public AimNoiseGenerator(AimNoiseGenerator.NoiseProfile var1) {
      this(var1, new SplittableRandom());
   }

   public AimNoiseGenerator(AimNoiseGenerator.NoiseProfile var1, SplittableRandom var2) {
      this.random = var2;
      this.profile = var1;
      this.yawChannel = new AimNoiseGenerator.NoiseChannel(var1.noiseSources());
      this.pitchChannel = new AimNoiseGenerator.NoiseChannel(var1.noiseSources());
      this.reset();
   }

   public void reset() {
      this.seedChannel(this.yawChannel, 1.0F);
      this.seedChannel(this.pitchChannel, 0.86F);
      this.wristAngle = this.randomRange(-this.profile.wristLimit(), this.profile.wristLimit());
      this.subMovementActive = (boolean)0;
      this.subMovementProgress = 0.0F;
      this.ballisticScale = 1.0F;
   }

   public float sampleNoise(AimNoiseGenerator.Axis var1) {
      AimNoiseGenerator.NoiseChannel var2 = this.subMovementFactor(var1);
      this.refreshNoise(var2);
      if (this.random.nextDouble() < this.profile.additionalNoiseRefreshChance()) {
         this.refreshNoise(var2);
      }

      float var3 = 0.0F;

      for (float var7 : var2.noiseValues) {
         var3 += var7;
      }

      return Mth.clamp(var3 / var2.noiseValues.length, -1.0F, 1.0F);
   }

   public float nextGain(AimNoiseGenerator.Axis var1, boolean var2) {
      AimNoiseGenerator.NoiseChannel var3 = this.subMovementFactor(var1);
      if (var3.gainTicksRemaining <= 0) {
         float var4 = var1 == AimNoiseGenerator.Axis.PITCH ? 0.86F : 1.0F;
         var3.targetGain = 1.0F + (this.randomRange(this.profile.minimumGain(), this.profile.maximumGain()) - 1.0F) * var4;
         var3.gainTicksRemaining = this.random.nextInt(this.profile.minimumGainDuration(), this.profile.maximumGainDuration() + 1);
      }

      var3.gainTicksRemaining--;
      float var6 = this.profile.gainAdjustmentPerTick();
      var3.currentGain = var3.currentGain + Mth.clamp(var3.targetGain - var3.currentGain, -var6, var6);
      if (!var2) {
         return var3.currentGain;
      }

      float var5 = (var3.currentGain - 1.0F) * this.profile.precisionGainCompression();
      return 1.0F + Mth.clamp(var5, -this.profile.maximumPrecisionGainDeviation(), this.profile.maximumPrecisionGainDeviation());
   }

   public float subMovementFactor(float var1, boolean var2) {
      if (!var2 && Float.isFinite(var1)) {
         if (var1 > this.profile.subMovementThreshold() && !this.subMovementActive && this.random.nextDouble() < this.profile.subMovementProbability()) {
            this.subMovementActive = (boolean)1;
            this.subMovementProgress = 0.0F;
            this.ballisticScale = this.profile.ballisticMinimum() + (float)this.random.nextDouble() * this.profile.ballisticRange();
         }

         if (!this.subMovementActive) {
            return 1.0F;
         }

         this.subMovementProgress = this.subMovementProgress + this.profile.subMovementProgressPerTick();
         if (this.subMovementProgress < 1.0F) {
            return this.ballisticScale;
         }

         if (this.subMovementProgress < 1.75F) {
            return this.profile.correctionMinimum() + (float)this.random.nextDouble() * this.profile.correctionRange();
         }

         this.endSubMovement();
         return 1.0F;
      } else {
         this.endSubMovement();
         return 1.0F;
      }
   }

   public float wristPitchOffset(float var1) {
      return var1 * this.profile.wristPitchCoupling() * Mth.sin(this.wristAngle);
   }

   public void updateWrist() {
      if (this.random.nextDouble() < this.profile.wristUpdateChance()) {
         this.wristAngle = Mth.clamp(
            this.wristAngle + this.randomRange(-this.profile.wristAngleChange(), this.profile.wristAngleChange()),
            -this.profile.wristLimit(),
            this.profile.wristLimit()
         );
      }
   }

   private void seedChannel(AimNoiseGenerator.NoiseChannel var1, float var2) {
      for (int var3 = 0; var3 < var1.noiseValues.length; var3++) {
         var1.noiseValues[var3] = this.randomSigned();
      }

      var1.currentGain = 1.0F;
      var1.targetGain = 1.0F + (this.randomRange(this.profile.minimumGain(), this.profile.maximumGain()) - 1.0F) * var2;
      var1.gainTicksRemaining = this.random.nextInt(this.profile.minimumGainDuration(), this.profile.maximumGainDuration() + 1);
   }

   private void refreshNoise(AimNoiseGenerator.NoiseChannel var1) {
      var1.noiseValues[this.random.nextInt(var1.noiseValues.length)] = this.randomSigned();
   }

   private AimNoiseGenerator.NoiseChannel subMovementFactor(AimNoiseGenerator.Axis var1) {
      return var1 == AimNoiseGenerator.Axis.YAW ? this.yawChannel : this.pitchChannel;
   }

   private void endSubMovement() {
      this.subMovementActive = (boolean)0;
      this.subMovementProgress = 0.0F;
      this.ballisticScale = 1.0F;
   }

   private float randomSigned() {
      return (float)(this.random.nextDouble() * 2.0 - 1.0);
   }

   private float randomRange(float var1, float var2) {
      return var1 + (float)this.random.nextDouble() * (var2 - var1);
   }

   public enum Axis {
      YAW,
      PITCH;
   }

   private static final class NoiseChannel {
      final float[] noiseValues;
      float currentGain;
      float targetGain;
      int gainTicksRemaining;

      NoiseChannel(int var1) {
         this.noiseValues = new float[var1];
      }
   }

   public record NoiseProfile(
      int noiseSources,
      float minimumGain,
      float maximumGain,
      int minimumGainDuration,
      int maximumGainDuration,
      float gainAdjustmentPerTick,
      float precisionGainCompression,
      float maximumPrecisionGainDeviation,
      float subMovementThreshold,
      double subMovementProbability,
      float ballisticMinimum,
      float ballisticRange,
      float correctionMinimum,
      float correctionRange,
      float subMovementProgressPerTick,
      double additionalNoiseRefreshChance,
      float wristUpdateChance,
      float wristAngleChange,
      float wristLimit,
      float wristPitchCoupling
   ) {
   }
}
