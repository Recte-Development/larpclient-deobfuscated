package com.larpclient.module.combat;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public interface RotationProvider {
   Rotation rotate(Rotation var1, Rotation var2, LivingEntity var3, boolean var4, boolean var5);

   default int ticksToReach(Rotation var1, Rotation var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(RotationMath.angleDelta(var1.yaw, var2.yaw));
         float var4 = Math.abs(var2.pitch - var1.pitch);
         float var5 = Math.max(1.0F, this.yawSpeed());
         float var6 = Math.max(1.0F, this.pitchSpeed());
         return Mth.clamp((int)Math.ceil(Math.max(var3 / var5, var4 / var6)), 0, 8);
      } else {
         return 0;
      }
   }

   default float yawSpeed() {
      return 30.0F;
   }

   default float pitchSpeed() {
      return 18.0F;
   }

   default void reset() {
   }

   default void targetChanged(LivingEntity var1) {
   }
}
