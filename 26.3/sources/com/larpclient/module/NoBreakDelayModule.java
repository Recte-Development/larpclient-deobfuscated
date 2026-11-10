package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q9b8c1cd7fe41113a;
import com.larpclient._q;
import java.util.Set;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;

public final class NoBreakDelayModule extends Module {
   public NoBreakDelayModule() {
      super("No Break Delay", "Remove the delay between breaking blocks", Category.PLAYER, -1);
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? "Ready immediately" : "";
   }

   @Override
   public void onEnable() {
      _q.c("NoBreakDelay", "Enabled; the native post-break delay is cleared each tick");
   }

   @Override
   public void onTick() {
      MultiPlayerGameMode var2 = mc.gameMode;
      int var10000 = var2 instanceof q9b8c1cd7fe41113a;
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         q9b8c1cd7fe41113a var1 = (q9b8c1cd7fe41113a)var2;
         var10000 = var1.larpDestroyDelay();
         boolean var6 = false;
         int var5 = var10000;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0) {
            var1.larpDestroyDelay(0);
         }
      }
   }
}
