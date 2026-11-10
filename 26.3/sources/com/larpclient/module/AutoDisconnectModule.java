package com.larpclient.module;

import net.minecraft.network.chat.Component;

public class AutoDisconnectModule extends Module {
   private final b X = this.addSetting(new b("Health", "Disconnect below health", 4.0, 1.0, 20.0, 0.5));

   public AutoDisconnectModule() {
      super("AutoDisconnect", "Disconnect at low health", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var6;
         int var10000 = (var6 = mc.player.getHealth() - this.X.getValue()) == 0.0 ? 0 : (var6 < 0.0 ? -1 : 1);
         boolean var2 = false;
         int var1 = var10000;
         if (-Integer.signum(var1) >>> 31 == 0) {
            float var7;
            var10000 = (var7 = mc.player.getHealth() - 0.0F) == 0.0F ? 0 : (var7 < 0.0F ? -1 : 1);
            boolean var4 = false;
            int var3 = var10000;
            if ((-Integer.signum(var3) >>> 31 ^ 1) == 0) {
               mc.player.connection.getConnection().disconnect(Component.literal("LarpClient AutoDisconnect - Health too low!"));
               this.disable();
            }
         }
      }
   }
}
