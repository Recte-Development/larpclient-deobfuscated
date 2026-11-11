package com.larpclient.module.combat;

import net.minecraft.util.Mth;

public final class RotationQuantizer {
   private float yawRemainder;
   private float pitchRemainder;
   private float lastStep = Float.NaN;

   public Rotation quantizeDefault(Rotation var1, Rotation var2) {
      return this.quantize(var1, var2, RotationMath.mouseStep());
   }

   public Rotation quantize(Rotation var1, Rotation var2, float var3) {
      if (var1 != null && var2 != null) {
         float var4 = RotationMath.unwrapAngle(var1.yaw, var2.yaw) - var1.yaw;
         float var5 = var2.pitch - var1.pitch;
         if (Float.isFinite(var3) && !(var3 <= 1.0E-5F)) {
            if (!Float.isFinite(this.lastStep) || Math.abs(this.lastStep - var3) > 1.0E-5F) {
               this.yawRemainder = 0.0F;
               this.pitchRemainder = 0.0F;
               this.lastStep = var3;
            }

            float var6 = this.quantizeYaw(var4, var3);
            float var7 = this.quantizePitch(var5, var3);
            float var8 = var1.pitch + var7;
            float var9 = Mth.clamp(var8, -90.0F, 90.0F);
            if (var9 != var8) {
               this.pitchRemainder = 0.0F;
            }

            return new Rotation(var1.yaw + var6, var9);
         } else {
            this.reset();
            return new Rotation(var1.yaw + finiteOrZero(var4), Mth.clamp(var1.pitch + finiteOrZero(var5), -90.0F, 90.0F));
         }
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private float quantizeYaw(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.yawRemainder = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.yawRemainder;
         float var4 = Math.round(var3 / var2) * var2;
         this.yawRemainder = var3 - var4;
         return var4;
      }
   }

   private float quantizePitch(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.pitchRemainder = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.pitchRemainder;
         float var4 = Math.round(var3 / var2) * var2;
         this.pitchRemainder = var3 - var4;
         return var4;
      }
   }

   private static float finiteOrZero(float var0) {
      return Float.isFinite(var0) ? var0 : 0.0F;
   }

   public void reset() {
      this.yawRemainder = 0.0F;
      this.pitchRemainder = 0.0F;
      this.lastStep = Float.NaN;
   }
}
