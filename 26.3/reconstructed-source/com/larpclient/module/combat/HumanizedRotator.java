package com.larpclient.module.combat;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class HumanizedRotator implements RotationProvider {
   private final SecureRandom random = new SecureRandom();
   private float yawLimit = 0.0F;
   private float pitchLimit = 0.0F;

   @Override
   public Rotation rotate(Rotation var1, Rotation var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         float var6 = RotationMath.angleDelta(var1.yaw, var2.yaw);
         float var7 = var2.pitch - var1.pitch;
         float var8 = (float)Math.hypot(var6, var7);
         float var9;
         if (!var5) {
            var9 = this.randomBetween(45.0F, 65.0F);
         } else if (var4) {
            var9 = this.randomBetween(35.0F, 50.0F);
         } else {
            var9 = this.randomBetween(28.0F, 42.0F);
         }

         this.yawLimit = Mth.lerp(0.35F, this.yawLimit, var9);
         this.pitchLimit = Mth.lerp(0.3F, this.pitchLimit, var9 * 0.75F);
         float var10 = Mth.clamp(var6, -this.yawLimit, this.yawLimit);
         float var11 = Mth.clamp(var7, -this.pitchLimit, this.pitchLimit);
         if (var8 < 8.0F) {
            float var12 = Math.max(0.45F, var8 / 8.0F);
            var10 *= var12;
            var11 *= var12;
         }

         return new Rotation(var1.yaw + var10, Mth.clamp(var1.pitch + var11, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float randomBetween(float var1, float var2) {
      return var1 + this.random.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.yawLimit = 0.0F;
      this.pitchLimit = 0.0F;
   }
}
