package com.larpclient.module.combat;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class HumanizedRotation implements RotationProvider {
   private final SecureRandom random = new SecureRandom();
   private int tickCounter;
   private int ticksSinceTargetChange;
   private float yawDrift;
   private float pitchDrift;

   @Override
   public Rotation rotate(Rotation var1, Rotation var2, LivingEntity var3, boolean var4, boolean var5) {
      this.tickCounter++;
      this.ticksSinceTargetChange++;
      float var6 = RotationMath.angleDelta(var1.yaw, var2.yaw);
      float var7 = var2.pitch - var1.pitch;
      if (this.tickCounter % 5 == 0) {
         float var8 = var5 ? this.randomRange(-1.0F, 1.0F) : this.randomRange(-3.0F, 3.0F);
         float var9 = var5 ? this.randomRange(-0.5F, 0.5F) : this.randomRange(-1.5F, 1.5F);
         if (Math.abs(var6) < 8.0F && Math.abs(var7) < 6.0F) {
            var8 *= 0.2F;
            var9 *= 0.2F;
         }

         float var10 = this.ticksSinceTargetChange > 20 ? 0.45F : 0.25F;
         this.yawDrift = Mth.lerp(var10, this.yawDrift, var8);
         this.pitchDrift = Mth.lerp(var10, this.pitchDrift, var9);
      }

      float var12 = var4 ? this.randomRange(80.0F, 130.0F) : this.randomRange(50.0F, 80.0F);
      float var13 = var4 ? this.randomRange(50.0F, 80.0F) : this.randomRange(30.0F, 50.0F);
      float var14 = Mth.clamp(var6 + this.yawDrift, -var12, var12);
      float var11 = Mth.clamp(var7 + this.pitchDrift, -var13, var13);
      return new Rotation(var1.yaw + var14, Mth.clamp(var1.pitch + var11, -90.0F, 90.0F));
   }

   private float randomRange(float var1, float var2) {
      return var1 + this.random.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.tickCounter = 0;
      this.ticksSinceTargetChange = 0;
      this.yawDrift = 0.0F;
      this.pitchDrift = 0.0F;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.ticksSinceTargetChange = 0;
   }
}
