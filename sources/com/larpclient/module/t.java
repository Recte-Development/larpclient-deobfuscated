package com.larpclient.module;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public interface t {
   d5 rotate(d5 var1, d5 var2, LivingEntity var3, boolean var4, boolean var5);

   default int ticksToReach(d5 var1, d5 var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(dj.h(var1.M, var2.M));
         float var4 = Math.abs(var2.G - var1.G);
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
