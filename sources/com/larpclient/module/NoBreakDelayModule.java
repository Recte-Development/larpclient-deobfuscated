package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q099a584e4baa1bb9;
import com.larpclient.mt;
import java.util.Set;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;

public final class NoBreakDelayModule extends Module {
   public NoBreakDelayModule() {
      super("No Break Delay", "Remove the delay between breaking blocks", Category.PLAYER, -1);
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? "Ready immediately" : "";
   }

   @Override
   public void onEnable() {
      mt.U("NoBreakDelay", "Enabled; the native post-break delay is cleared each tick");
   }

   @Override
   public void onTick() {
      MultiPlayerGameMode var2 = mc.gameMode;
      int var10000 = var2 instanceof q099a584e4baa1bb9;
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         q099a584e4baa1bb9 var1 = (q099a584e4baa1bb9)var2;
         var10000 = var1.larpDestroyDelay();
         boolean var6 = false;
         int var5 = var10000;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0) {
            var1.larpDestroyDelay(0);
         }
      }
   }
}
