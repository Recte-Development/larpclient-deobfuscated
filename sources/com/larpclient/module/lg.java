package com.larpclient.module;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public interface lg {
   lx rotate(lx var1, lx var2, LivingEntity var3, boolean var4, boolean var5);

   default int ticksToReach(lx var1, lx var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(le.P(var1.z, var2.z));
         float var4 = Math.abs(var2.I - var1.I);
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
