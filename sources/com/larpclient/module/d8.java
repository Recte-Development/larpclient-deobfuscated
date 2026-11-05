package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class d8 {
   public static final d8.f D = new d8.f(
      5, 0.68F, 1.1F, 2, 5, 0.09F, 0.56F, 0.04F, 18.0F, 0.34, 0.78F, 0.12F, 0.48F, 0.19F, 0.23F, 0.1, 0.07F, 0.042F, 0.32F, 0.062F
   );
   public static final d8.f H = new d8.f(
      9, 0.74F, 1.14F, 1, 4, 0.125F, 0.66F, 0.048F, 24.0F, 0.28, 0.72F, 0.15F, 0.54F, 0.24F, 0.27F, 0.28, 0.055F, 0.055F, 0.35F, 0.078F
   );
   private final SplittableRandom v;
   private final d8.f E;
   private final d8.i Y;
   private final d8.i d;
   private float A;
   private boolean k;
   private float O;
   private float q;

   public d8(d8.f var1) {
      this(var1, new SplittableRandom());
   }

   public d8(d8.f var1, SplittableRandom var2) {
      this.v = var2;
      this.E = var1;
      this.Y = new d8.i(var1.noiseSources());
      this.d = new d8.i(var1.noiseSources());
      this.F();
   }

   public void F() {
      this.x(this.Y, 1.0F);
      this.x(this.d, 0.86F);
      this.A = this.I(-this.E.wristLimit(), this.E.wristLimit());
      this.k = (boolean)0;
      this.O = 0.0F;
      this.q = 1.0F;
   }

   public float c(d8.a var1) {
      d8.i var2 = this.A(var1);
      this.f(var2);
      if (this.v.nextDouble() < this.E.additionalNoiseRefreshChance()) {
         this.f(var2);
      }

      float var3 = 0.0F;

      for (float var7 : var2.D) {
         var3 += var7;
      }

      return Mth.clamp(var3 / var2.D.length, -1.0F, 1.0F);
   }

   public float O(d8.a var1, boolean var2) {
      d8.i var3 = this.A(var1);
      if (var3.s <= 0) {
         float var4 = var1 == d8.a.L ? 0.86F : 1.0F;
         var3.J = 1.0F + (this.I(this.E.minimumGain(), this.E.maximumGain()) - 1.0F) * var4;
         var3.s = this.v.nextInt(this.E.minimumGainDuration(), this.E.maximumGainDuration() + 1);
      }

      var3.s--;
      float var6 = this.E.gainAdjustmentPerTick();
      var3.z = var3.z + Mth.clamp(var3.J - var3.z, -var6, var6);
      if (!var2) {
         return var3.z;
      }

      float var5 = (var3.z - 1.0F) * this.E.precisionGainCompression();
      return 1.0F + Mth.clamp(var5, -this.E.maximumPrecisionGainDeviation(), this.E.maximumPrecisionGainDeviation());
   }

   public float g(float var1, boolean var2) {
      if (!var2 && Float.isFinite(var1)) {
         if (var1 > this.E.subMovementThreshold() && !this.k && this.v.nextDouble() < this.E.subMovementProbability()) {
            this.k = (boolean)1;
            this.O = 0.0F;
            this.q = this.E.ballisticMinimum() + (float)this.v.nextDouble() * this.E.ballisticRange();
         }

         if (!this.k) {
            return 1.0F;
         }

         this.O = this.O + this.E.subMovementProgressPerTick();
         if (this.O < 1.0F) {
            return this.q;
         }

         if (this.O < 1.75F) {
            return this.E.correctionMinimum() + (float)this.v.nextDouble() * this.E.correctionRange();
         }

         this.Z();
         return 1.0F;
      } else {
         this.Z();
         return 1.0F;
      }
   }

   public float Q(float var1) {
      return var1 * this.E.wristPitchCoupling() * Mth.sin(this.A);
   }

   public void q() {
      if (this.v.nextDouble() < this.E.wristUpdateChance()) {
         this.A = Mth.clamp(this.A + this.I(-this.E.wristAngleChange(), this.E.wristAngleChange()), -this.E.wristLimit(), this.E.wristLimit());
      }
   }

   private void x(d8.i var1, float var2) {
      for (int var3 = 0; var3 < var1.D.length; var3++) {
         var1.D[var3] = this.T();
      }

      var1.z = 1.0F;
      var1.J = 1.0F + (this.I(this.E.minimumGain(), this.E.maximumGain()) - 1.0F) * var2;
      var1.s = this.v.nextInt(this.E.minimumGainDuration(), this.E.maximumGainDuration() + 1);
   }

   private void f(d8.i var1) {
      var1.D[this.v.nextInt(var1.D.length)] = this.T();
   }

   private d8.i A(d8.a var1) {
      return var1 == d8.a.G ? this.Y : this.d;
   }

   private void Z() {
      this.k = (boolean)0;
      this.O = 0.0F;
      this.q = 1.0F;
   }

   private float T() {
      return (float)(this.v.nextDouble() * 2.0 - 1.0);
   }

   private float I(float var1, float var2) {
      return var1 + (float)this.v.nextDouble() * (var2 - var1);
   }

   public enum a {
      G,
      L;
   }

   public record f(
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

   private static final class i {
      final float[] D;
      float z;
      float J;
      int s;

      i(int var1) {
         this.D = new float[var1];
      }
   }
}
