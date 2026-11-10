package com.larpclient.module;

import net.minecraft.class_1309;
import net.minecraft.class_3532;

public interface z {
   l4 rotate(l4 var1, l4 var2, class_1309 var3, boolean var4, boolean var5);

   default int ticksToReach(l4 var1, l4 var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(a_.x(var1.j, var2.j));
         float var4 = Math.abs(var2.l - var1.l);
         float var5 = Math.max(1.0F, this.yawSpeed());
         float var6 = Math.max(1.0F, this.pitchSpeed());
         return class_3532.method_15340((int)Math.ceil(Math.max(var3 / var5, var4 / var6)), 0, 8);
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

   default void targetChanged(class_1309 var1) {
   }
}
