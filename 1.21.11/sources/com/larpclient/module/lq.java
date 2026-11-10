package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.class_3532;

public final class lq {
   public static final lq.s u = new lq.s(
      5, 0.68F, 1.1F, 2, 5, 0.09F, 0.56F, 0.04F, 18.0F, 0.34, 0.78F, 0.12F, 0.48F, 0.19F, 0.23F, 0.1, 0.07F, 0.042F, 0.32F, 0.062F
   );
   public static final lq.s v = new lq.s(
      9, 0.74F, 1.14F, 1, 4, 0.125F, 0.66F, 0.048F, 24.0F, 0.28, 0.72F, 0.15F, 0.54F, 0.24F, 0.27F, 0.28, 0.055F, 0.055F, 0.35F, 0.078F
   );
   private final SplittableRandom Z;
   private final lq.s y;
   private final lq.u m;
   private final lq.u f;
   private float j;
   private boolean H;
   private float B;
   private float L;

   public lq(lq.s var1) {
      this(var1, new SplittableRandom());
   }

   public lq(lq.s var1, SplittableRandom var2) {
      this.Z = var2;
      this.y = var1;
      this.m = new lq.u(var1.noiseSources());
      this.f = new lq.u(var1.noiseSources());
      this.A();
   }

   public void A() {
      this.G(this.m, 1.0F);
      this.G(this.f, 0.86F);
      this.j = this.l(-this.y.wristLimit(), this.y.wristLimit());
      this.H = (boolean)0;
      this.B = 0.0F;
      this.L = 1.0F;
   }

   public float Y(lq.g var1) {
      lq.u var2 = this.I(var1);
      this.K(var2);
      if (this.Z.nextDouble() < this.y.additionalNoiseRefreshChance()) {
         this.K(var2);
      }

      float var3 = 0.0F;

      for (float var7 : var2.R) {
         var3 += var7;
      }

      return class_3532.method_15363(var3 / var2.R.length, -1.0F, 1.0F);
   }

   public float M(lq.g var1, boolean var2) {
      lq.u var3 = this.I(var1);
      if (var3.K <= 0) {
         float var4 = var1 == lq.g.u ? 0.86F : 1.0F;
         var3.D = 1.0F + (this.l(this.y.minimumGain(), this.y.maximumGain()) - 1.0F) * var4;
         var3.K = this.Z.nextInt(this.y.minimumGainDuration(), this.y.maximumGainDuration() + 1);
      }

      var3.K--;
      float var6 = this.y.gainAdjustmentPerTick();
      var3.J = var3.J + class_3532.method_15363(var3.D - var3.J, -var6, var6);
      if (!var2) {
         return var3.J;
      }

      float var5 = (var3.J - 1.0F) * this.y.precisionGainCompression();
      return 1.0F + class_3532.method_15363(var5, -this.y.maximumPrecisionGainDeviation(), this.y.maximumPrecisionGainDeviation());
   }

   public float v(float var1, boolean var2) {
      if (!var2 && Float.isFinite(var1)) {
         if (var1 > this.y.subMovementThreshold() && !this.H && this.Z.nextDouble() < this.y.subMovementProbability()) {
            this.H = (boolean)1;
            this.B = 0.0F;
            this.L = this.y.ballisticMinimum() + (float)this.Z.nextDouble() * this.y.ballisticRange();
         }

         if (!this.H) {
            return 1.0F;
         }

         this.B = this.B + this.y.subMovementProgressPerTick();
         if (this.B < 1.0F) {
            return this.L;
         }

         if (this.B < 1.75F) {
            return this.y.correctionMinimum() + (float)this.Z.nextDouble() * this.y.correctionRange();
         }

         this.O();
         return 1.0F;
      } else {
         this.O();
         return 1.0F;
      }
   }

   public float f(float var1) {
      return var1 * this.y.wristPitchCoupling() * class_3532.method_15374(this.j);
   }

   public void c() {
      if (this.Z.nextDouble() < this.y.wristUpdateChance()) {
         this.j = class_3532.method_15363(this.j + this.l(-this.y.wristAngleChange(), this.y.wristAngleChange()), -this.y.wristLimit(), this.y.wristLimit());
      }
   }

   private void G(lq.u var1, float var2) {
      for (int var3 = 0; var3 < var1.R.length; var3++) {
         var1.R[var3] = this.G();
      }

      var1.J = 1.0F;
      var1.D = 1.0F + (this.l(this.y.minimumGain(), this.y.maximumGain()) - 1.0F) * var2;
      var1.K = this.Z.nextInt(this.y.minimumGainDuration(), this.y.maximumGainDuration() + 1);
   }

   private void K(lq.u var1) {
      var1.R[this.Z.nextInt(var1.R.length)] = this.G();
   }

   private lq.u I(lq.g var1) {
      return var1 == lq.g.h ? this.m : this.f;
   }

   private void O() {
      this.H = (boolean)0;
      this.B = 0.0F;
      this.L = 1.0F;
   }

   private float G() {
      return (float)(this.Z.nextDouble() * 2.0 - 1.0);
   }

   private float l(float var1, float var2) {
      return var1 + (float)this.Z.nextDouble() * (var2 - var1);
   }

   public enum g {
      h,
      u;
   }

   public record s(
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

   private static final class u {
      final float[] R;
      float J;
      float D;
      int K;

      u(int var1) {
         this.R = new float[var1];
      }
   }
}
