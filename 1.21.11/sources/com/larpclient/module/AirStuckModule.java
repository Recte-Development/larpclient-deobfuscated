package com.larpclient.module;

import com.larpclient.j;
import java.util.Set;
import net.minecraft.class_243;

public final class AirStuckModule extends Module {
   private static AirStuckModule E;

   public AirStuckModule() {
      super("AirStuck", "Hold your position", Category.MOVEMENT, -1);
      E = this;
   }

   public static AirStuckModule U() {
      return E;
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.isInGame() && j.k() && !mc.field_1724.method_5765();
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.s;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onTick() {
      int var10000 = this.E();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         mc.field_1724.method_18799(class_243.field_1353);
      }
   }

   @Override
   public String getStatus() {
      return this.E() ? "Holding" : "";
   }
}
