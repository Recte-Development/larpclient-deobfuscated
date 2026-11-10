package com.larpclient.module;

import net.minecraft.class_2561;

public class AutoDisconnectModule extends Module {
   private final lo b = this.addSetting(new lo("Health", "Disconnect below health", 4.0, 1.0, 20.0, 0.5));

   public AutoDisconnectModule() {
      super("AutoDisconnect", "Disconnect at low health", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var6;
         int var10000 = (var6 = mc.field_1724.method_6032() - this.b.getValue()) == 0.0 ? 0 : (var6 < 0.0 ? -1 : 1);
         boolean var2 = false;
         int var1 = var10000;
         if (-Integer.signum(var1) >>> 31 == 0) {
            float var7;
            var10000 = (var7 = mc.field_1724.method_6032() - 0.0F) == 0.0F ? 0 : (var7 < 0.0F ? -1 : 1);
            boolean var4 = false;
            int var3 = var10000;
            if ((-Integer.signum(var3) >>> 31 ^ 1) == 0) {
               mc.field_1724.field_3944.method_48296().method_10747(class_2561.method_43470("LarpClient AutoDisconnect - Health too low!"));
               this.disable();
            }
         }
      }
   }
}
