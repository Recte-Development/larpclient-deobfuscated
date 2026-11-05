package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class lk {
   public static final lk.d x = new lk.d(
      5, 0.68F, 1.1F, 2, 5, 0.09F, 0.56F, 0.04F, 18.0F, 0.34, 0.78F, 0.12F, 0.48F, 0.19F, 0.23F, 0.1, 0.07F, 0.042F, 0.32F, 0.062F
   );
   public static final lk.d C = new lk.d(
      9, 0.74F, 1.14F, 1, 4, 0.125F, 0.66F, 0.048F, 24.0F, 0.28, 0.72F, 0.15F, 0.54F, 0.24F, 0.27F, 0.28, 0.055F, 0.055F, 0.35F, 0.078F
   );
   private final SplittableRandom g;
   private final lk.d p;
   private final lk.y M;
   private final lk.y i;
   private float N;
   private boolean k;
   private float y;
   private float Q;

   public lk(lk.d var1) {
      this(var1, new SplittableRandom());
   }

   public lk(lk.d var1, SplittableRandom var2) {
      this.g = var2;
      this.p = var1;
      this.M = new lk.y(var1.noiseSources());
      this.i = new lk.y(var1.noiseSources());
      this.c();
   }

   public void c() {
      this.y(this.M, 1.0F);
      this.y(this.i, 0.86F);
      this.N = this.m(-this.p.wristLimit(), this.p.wristLimit());
      this.k = (boolean)0;
      this.y = 0.0F;
      this.Q = 1.0F;
   }

   public float w(lk.o var1) {
      lk.y var2 = this.W(var1);
      this.m(var2);
      if (this.g.nextDouble() < this.p.additionalNoiseRefreshChance()) {
         this.m(var2);
      }

      float var3 = 0.0F;

      for (float var7 : var2.Z) {
         var3 += var7;
      }

      return Mth.clamp(var3 / var2.Z.length, -1.0F, 1.0F);
   }

   public float S(lk.o var1, boolean var2) {
      lk.y var3 = this.W(var1);
      if (var3.b <= 0) {
         float var4 = var1 == lk.o.Z ? 0.86F : 1.0F;
         var3.p = 1.0F + (this.m(this.p.minimumGain(), this.p.maximumGain()) - 1.0F) * var4;
         var3.b = this.g.nextInt(this.p.minimumGainDuration(), this.p.maximumGainDuration() + 1);
      }

      var3.b--;
      float var6 = this.p.gainAdjustmentPerTick();
      var3.X = var3.X + Mth.clamp(var3.p - var3.X, -var6, var6);
      if (!var2) {
         return var3.X;
      }

      float var5 = (var3.X - 1.0F) * this.p.precisionGainCompression();
      return 1.0F + Mth.clamp(var5, -this.p.maximumPrecisionGainDeviation(), this.p.maximumPrecisionGainDeviation());
   }

   public float I(float var1, boolean var2) {
      if (!var2 && Float.isFinite(var1)) {
         if (var1 > this.p.subMovementThreshold() && !this.k && this.g.nextDouble() < this.p.subMovementProbability()) {
            this.k = (boolean)1;
            this.y = 0.0F;
            this.Q = this.p.ballisticMinimum() + (float)this.g.nextDouble() * this.p.ballisticRange();
         }

         if (!this.k) {
            return 1.0F;
         }

         this.y = this.y + this.p.subMovementProgressPerTick();
         if (this.y < 1.0F) {
            return this.Q;
         }

         if (this.y < 1.75F) {
            return this.p.correctionMinimum() + (float)this.g.nextDouble() * this.p.correctionRange();
         }

         this.H();
         return 1.0F;
      } else {
         this.H();
         return 1.0F;
      }
   }

   public float D(float var1) {
      return var1 * this.p.wristPitchCoupling() * Mth.sin(this.N);
   }

   public void Q() {
      if (this.g.nextDouble() < this.p.wristUpdateChance()) {
         this.N = Mth.clamp(this.N + this.m(-this.p.wristAngleChange(), this.p.wristAngleChange()), -this.p.wristLimit(), this.p.wristLimit());
      }
   }

   private void y(lk.y var1, float var2) {
      for (int var3 = 0; var3 < var1.Z.length; var3++) {
         var1.Z[var3] = this.M();
      }

      var1.X = 1.0F;
      var1.p = 1.0F + (this.m(this.p.minimumGain(), this.p.maximumGain()) - 1.0F) * var2;
      var1.b = this.g.nextInt(this.p.minimumGainDuration(), this.p.maximumGainDuration() + 1);
   }

   private void m(lk.y var1) {
      var1.Z[this.g.nextInt(var1.Z.length)] = this.M();
   }

   private lk.y W(lk.o var1) {
      return var1 == lk.o.W ? this.M : this.i;
   }

   private void H() {
      this.k = (boolean)0;
      this.y = 0.0F;
      this.Q = 1.0F;
   }

   private float M() {
      return (float)(this.g.nextDouble() * 2.0 - 1.0);
   }

   private float m(float var1, float var2) {
      return var1 + (float)this.g.nextDouble() * (var2 - var1);
   }

   public record d(
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

   public enum o {
      W,
      Z;
   }

   private static final class y {
      final float[] Z;
      float X;
      float p;
      int b;

      y(int var1) {
         this.Z = new float[var1];
      }
   }
}
