package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_636;

public final class NoBreakDelayModule extends Module {
   public NoBreakDelayModule() {
      super("No Break Delay", "Remove the delay between breaking blocks", Category.PLAYER, -1);
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? "Ready immediately" : "";
   }

   @Override
   public void onEnable() {
      q8.v("NoBreakDelay", "Enabled; the native post-break delay is cleared each tick");
   }

   @Override
   public void onTick() {
      class_636 var2 = mc.field_1761;
      int var10000 = var2 instanceof q226fb0b431f37244;
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         q226fb0b431f37244 var1 = (q226fb0b431f37244)var2;
         var10000 = var1.larpDestroyDelay();
         boolean var6 = false;
         int var5 = var10000;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0) {
            var1.larpDestroyDelay(0);
         }
      }
   }
}
