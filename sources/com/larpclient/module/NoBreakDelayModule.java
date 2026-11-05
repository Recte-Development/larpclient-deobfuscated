package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q143ef5d8ac4bd5b8;
import com.larpclient.r4;
import java.util.Set;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;

public final class NoBreakDelayModule extends Module {
   public NoBreakDelayModule() {
      super("No Break Delay", "Remove the delay between breaking blocks", Category.PLAYER, -1);
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? "Ready immediately" : "";
   }

   @Override
   public void onEnable() {
      r4.g("NoBreakDelay", "Enabled; the native post-break delay is cleared each tick");
   }

   @Override
   public void onTick() {
      MultiPlayerGameMode var2 = mc.gameMode;
      int var10000 = var2 instanceof q143ef5d8ac4bd5b8;
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         q143ef5d8ac4bd5b8 var1 = (q143ef5d8ac4bd5b8)var2;
         var10000 = var1.larpDestroyDelay();
         boolean var6 = false;
         int var5 = var10000;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0) {
            var1.larpDestroyDelay(0);
         }
      }
   }
}
