package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class f0 {
   public static final f0.m Y = new f0.m(
      5, 0.68F, 1.1F, 2, 5, 0.09F, 0.56F, 0.04F, 18.0F, 0.34, 0.78F, 0.12F, 0.48F, 0.19F, 0.23F, 0.1, 0.07F, 0.042F, 0.32F, 0.062F
   );
   public static final f0.m L = new f0.m(
      9, 0.74F, 1.14F, 1, 4, 0.125F, 0.66F, 0.048F, 24.0F, 0.28, 0.72F, 0.15F, 0.54F, 0.24F, 0.27F, 0.28, 0.055F, 0.055F, 0.35F, 0.078F
   );
   private final SplittableRandom c;
   private final f0.m G;
   private final f0.r r;
   private final f0.r b;
   private float I;
   private boolean z;
   private float f;
   private float e;

   public f0(f0.m var1) {
      this(var1, new SplittableRandom());
   }

   public f0(f0.m var1, SplittableRandom var2) {
      this.c = var2;
      this.G = var1;
      this.r = new f0.r(var1.noiseSources());
      this.b = new f0.r(var1.noiseSources());
      this.G();
   }

   public void G() {
      this.q(this.r, 1.0F);
      this.q(this.b, 0.86F);
      this.I = this.I(-this.G.wristLimit(), this.G.wristLimit());
      this.z = (boolean)0;
      this.f = 0.0F;
      this.e = 1.0F;
   }

   public float h(f0.p var1) {
      f0.r var2 = this.c(var1);
      this.T(var2);
      if (this.c.nextDouble() < this.G.additionalNoiseRefreshChance()) {
         this.T(var2);
      }

      float var3 = 0.0F;

      for (float var7 : var2.E) {
         var3 += var7;
      }

      return Mth.clamp(var3 / var2.E.length, -1.0F, 1.0F);
   }

   public float E(f0.p var1, boolean var2) {
      f0.r var3 = this.c(var1);
      if (var3.U <= 0) {
         float var4 = var1 == f0.p.K ? 0.86F : 1.0F;
         var3.p = 1.0F + (this.I(this.G.minimumGain(), this.G.maximumGain()) - 1.0F) * var4;
         var3.U = this.c.nextInt(this.G.minimumGainDuration(), this.G.maximumGainDuration() + 1);
      }

      var3.U--;
      float var6 = this.G.gainAdjustmentPerTick();
      var3.H = var3.H + Mth.clamp(var3.p - var3.H, -var6, var6);
      if (!var2) {
         return var3.H;
      }

      float var5 = (var3.H - 1.0F) * this.G.precisionGainCompression();
      return 1.0F + Mth.clamp(var5, -this.G.maximumPrecisionGainDeviation(), this.G.maximumPrecisionGainDeviation());
   }

   public float c(float var1, boolean var2) {
      if (!var2 && Float.isFinite(var1)) {
         if (var1 > this.G.subMovementThreshold() && !this.z && this.c.nextDouble() < this.G.subMovementProbability()) {
            this.z = (boolean)1;
            this.f = 0.0F;
            this.e = this.G.ballisticMinimum() + (float)this.c.nextDouble() * this.G.ballisticRange();
         }

         if (!this.z) {
            return 1.0F;
         }

         this.f = this.f + this.G.subMovementProgressPerTick();
         if (this.f < 1.0F) {
            return this.e;
         }

         if (this.f < 1.75F) {
            return this.G.correctionMinimum() + (float)this.c.nextDouble() * this.G.correctionRange();
         }

         this.B();
         return 1.0F;
      } else {
         this.B();
         return 1.0F;
      }
   }

   public float a(float var1) {
      return var1 * this.G.wristPitchCoupling() * Mth.sin(this.I);
   }

   public void U() {
      if (this.c.nextDouble() < this.G.wristUpdateChance()) {
         this.I = Mth.clamp(this.I + this.I(-this.G.wristAngleChange(), this.G.wristAngleChange()), -this.G.wristLimit(), this.G.wristLimit());
      }
   }

   private void q(f0.r var1, float var2) {
      for (int var3 = 0; var3 < var1.E.length; var3++) {
         var1.E[var3] = this.d();
      }

      var1.H = 1.0F;
      var1.p = 1.0F + (this.I(this.G.minimumGain(), this.G.maximumGain()) - 1.0F) * var2;
      var1.U = this.c.nextInt(this.G.minimumGainDuration(), this.G.maximumGainDuration() + 1);
   }

   private void T(f0.r var1) {
      var1.E[this.c.nextInt(var1.E.length)] = this.d();
   }

   private f0.r c(f0.p var1) {
      return var1 == f0.p.g ? this.r : this.b;
   }

   private void B() {
      this.z = (boolean)0;
      this.f = 0.0F;
      this.e = 1.0F;
   }

   private float d() {
      return (float)(this.c.nextDouble() * 2.0 - 1.0);
   }

   private float I(float var1, float var2) {
      return var1 + (float)this.c.nextDouble() * (var2 - var1);
   }

   public record m(
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

   public enum p {
      g,
      K;
   }

   private static final class r {
      final float[] E;
      float H;
      float p;
      int U;

      r(int var1) {
         this.E = new float[var1];
      }
   }
}
