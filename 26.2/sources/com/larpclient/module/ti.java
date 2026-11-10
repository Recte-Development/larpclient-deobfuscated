package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class ti {
   public static final ti.v J = new ti.v(
      5, 0.68F, 1.1F, 2, 5, 0.09F, 0.56F, 0.04F, 18.0F, 0.34, 0.78F, 0.12F, 0.48F, 0.19F, 0.23F, 0.1, 0.07F, 0.042F, 0.32F, 0.062F
   );
   public static final ti.v d = new ti.v(
      9, 0.74F, 1.14F, 1, 4, 0.125F, 0.66F, 0.048F, 24.0F, 0.28, 0.72F, 0.15F, 0.54F, 0.24F, 0.27F, 0.28, 0.055F, 0.055F, 0.35F, 0.078F
   );
   private final SplittableRandom W;
   private final ti.v H;
   private final ti.j o;
   private final ti.j e;
   private float O;
   private boolean m;
   private float D;
   private float p;

   public ti(ti.v var1) {
      this(var1, new SplittableRandom());
   }

   public ti(ti.v var1, SplittableRandom var2) {
      this.W = var2;
      this.H = var1;
      this.o = new ti.j(var1.noiseSources());
      this.e = new ti.j(var1.noiseSources());
      this.l();
   }

   public void l() {
      this.P(this.o, 1.0F);
      this.P(this.e, 0.86F);
      this.O = this.z(-this.H.wristLimit(), this.H.wristLimit());
      this.m = (boolean)0;
      this.D = 0.0F;
      this.p = 1.0F;
   }

   public float b(ti.y var1) {
      ti.j var2 = this.l(var1);
      this.t(var2);
      if (this.W.nextDouble() < this.H.additionalNoiseRefreshChance()) {
         this.t(var2);
      }

      float var3 = 0.0F;

      for (float var7 : var2.z) {
         var3 += var7;
      }

      return Mth.clamp(var3 / var2.z.length, -1.0F, 1.0F);
   }

   public float O(ti.y var1, boolean var2) {
      ti.j var3 = this.l(var1);
      if (var3.h <= 0) {
         float var4 = var1 == ti.y.G ? 0.86F : 1.0F;
         var3.p = 1.0F + (this.z(this.H.minimumGain(), this.H.maximumGain()) - 1.0F) * var4;
         var3.h = this.W.nextInt(this.H.minimumGainDuration(), this.H.maximumGainDuration() + 1);
      }

      var3.h--;
      float var6 = this.H.gainAdjustmentPerTick();
      var3.w = var3.w + Mth.clamp(var3.p - var3.w, -var6, var6);
      if (!var2) {
         return var3.w;
      }

      float var5 = (var3.w - 1.0F) * this.H.precisionGainCompression();
      return 1.0F + Mth.clamp(var5, -this.H.maximumPrecisionGainDeviation(), this.H.maximumPrecisionGainDeviation());
   }

   public float v(float var1, boolean var2) {
      if (!var2 && Float.isFinite(var1)) {
         if (var1 > this.H.subMovementThreshold() && !this.m && this.W.nextDouble() < this.H.subMovementProbability()) {
            this.m = (boolean)1;
            this.D = 0.0F;
            this.p = this.H.ballisticMinimum() + (float)this.W.nextDouble() * this.H.ballisticRange();
         }

         if (!this.m) {
            return 1.0F;
         }

         this.D = this.D + this.H.subMovementProgressPerTick();
         if (this.D < 1.0F) {
            return this.p;
         }

         if (this.D < 1.75F) {
            return this.H.correctionMinimum() + (float)this.W.nextDouble() * this.H.correctionRange();
         }

         this.n();
         return 1.0F;
      } else {
         this.n();
         return 1.0F;
      }
   }

   public float d(float var1) {
      return var1 * this.H.wristPitchCoupling() * Mth.sin(this.O);
   }

   public void x() {
      if (this.W.nextDouble() < this.H.wristUpdateChance()) {
         this.O = Mth.clamp(this.O + this.z(-this.H.wristAngleChange(), this.H.wristAngleChange()), -this.H.wristLimit(), this.H.wristLimit());
      }
   }

   private void P(ti.j var1, float var2) {
      for (int var3 = 0; var3 < var1.z.length; var3++) {
         var1.z[var3] = this.o();
      }

      var1.w = 1.0F;
      var1.p = 1.0F + (this.z(this.H.minimumGain(), this.H.maximumGain()) - 1.0F) * var2;
      var1.h = this.W.nextInt(this.H.minimumGainDuration(), this.H.maximumGainDuration() + 1);
   }

   private void t(ti.j var1) {
      var1.z[this.W.nextInt(var1.z.length)] = this.o();
   }

   private ti.j l(ti.y var1) {
      return var1 == ti.y.X ? this.o : this.e;
   }

   private void n() {
      this.m = (boolean)0;
      this.D = 0.0F;
      this.p = 1.0F;
   }

   private float o() {
      return (float)(this.W.nextDouble() * 2.0 - 1.0);
   }

   private float z(float var1, float var2) {
      return var1 + (float)this.W.nextDouble() * (var2 - var1);
   }

   private static final class j {
      final float[] z;
      float w;
      float p;
      int h;

      j(int var1) {
         this.z = new float[var1];
      }
   }

   public record v(
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

   public enum y {
      X,
      G;
   }
}
