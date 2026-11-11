package com.larpclient.module.combat;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class HumanizedRotation2 implements RotationProvider {
   private final SecureRandom random = new SecureRandom();
   private float yawPhase;
   private float pitchPhase;
   private float yawStepLimit = 0.0F;
   private float pitchStepLimit = 0.0F;

   @Override
   public Rotation rotate(Rotation var1, Rotation var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         this.yawPhase += 0.065F;
         this.pitchPhase += 0.045F;
         float var6 = (float)(Math.sin(this.yawPhase) * 0.8F + Math.cos(this.yawPhase * 0.5) * 0.4F);
         float var7 = (float)(Math.cos(this.pitchPhase) * 0.5);
         float var8 = RotationMath.unwrapAngle(var1.yaw, var2.yaw + var6);
         float var9 = Mth.clamp(var2.pitch + var7, -90.0F, 90.0F);
         float var10 = var8 - var1.yaw;
         float var11 = var9 - var1.pitch;
         float var12 = (float)Math.hypot(var10, var11);
         float var13 = var5 ? this.randomRange(40.0F, 60.0F) : this.randomRange(55.0F, 85.0F);
         float var14 = var5 ? this.randomRange(25.0F, 40.0F) : this.randomRange(35.0F, 55.0F);
         this.yawStepLimit = Mth.lerp(0.3F, this.yawStepLimit, var13);
         this.pitchStepLimit = Mth.lerp(0.3F, this.pitchStepLimit, var14);
         float var15 = Mth.clamp(var10, -this.yawStepLimit, this.yawStepLimit);
         float var16 = Mth.clamp(var11, -this.pitchStepLimit, this.pitchStepLimit);
         if (var12 < 6.0F) {
            float var17 = Math.max(0.4F, var12 / 6.0F);
            var15 *= var17;
            var16 *= var17;
         }

         return new Rotation(var1.yaw + var15, Mth.clamp(var1.pitch + var16, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float randomRange(float var1, float var2) {
      return var1 + this.random.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.yawPhase = this.pitchPhase = this.yawStepLimit = this.pitchStepLimit = 0.0F;
   }
}
