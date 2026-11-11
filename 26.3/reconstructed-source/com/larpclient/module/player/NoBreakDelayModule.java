package com.larpclient.module.player;

import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;

public final class NoBreakDelayModule extends Module {
   public NoBreakDelayModule() {
      super("No Break Delay", "Remove the delay between breaking blocks", Category.PLAYER, -1);
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? "Ready immediately" : "";
   }

   @Override
   public void onEnable() {
      LarpLog.debug("NoBreakDelay", "Enabled; the native post-break delay is cleared each tick");
   }

   @Override
   public void onTick() {
      MultiPlayerGameMode var2 = minecraft.gameMode;
      int var10000 = var2 instanceof MultiPlayerGameModeMixin_6;
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         MultiPlayerGameModeMixin_6 var1 = (MultiPlayerGameModeMixin_6)var2;
         var10000 = var1.larpDestroyDelay();
         boolean var6 = false;
         int var5 = var10000;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0) {
            var1.larpDestroyDelay(0);
         }
      }
   }
}
