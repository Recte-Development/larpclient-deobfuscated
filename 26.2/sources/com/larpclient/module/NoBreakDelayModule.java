package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q541491ff38fb1637;
import java.util.Set;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;

public final class NoBreakDelayModule extends Module {
   public NoBreakDelayModule() {
      super("No Break Delay", "Remove the delay between breaking blocks", Category.PLAYER, -1);
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? "Ready immediately" : "";
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U("NoBreakDelay", "Enabled; the native post-break delay is cleared each tick");
   }

   @Override
   public void onTick() {
      MultiPlayerGameMode var2 = mc.gameMode;
      int var10000 = var2 instanceof q541491ff38fb1637;
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         q541491ff38fb1637 var1 = (q541491ff38fb1637)var2;
         var10000 = var1.larpDestroyDelay();
         boolean var6 = false;
         int var5 = var10000;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0) {
            var1.larpDestroyDelay(0);
         }
      }
   }
}
