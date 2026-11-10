package com.larpclient.module;

import com.larpclient.nz;
import java.util.Set;
import net.minecraft.world.phys.Vec3;

public final class AirStuckModule extends Module {
   private static AirStuckModule B;

   public AirStuckModule() {
      super("AirStuck", "Hold your position", Category.MOVEMENT, -1);
      B = this;
   }

   public static AirStuckModule Z() {
      return B;
   }

   public boolean J() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.isInGame() && nz.v() && !mc.player.isPassenger();
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.N;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public void onTick() {
      int var10000 = this.J();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         mc.player.setDeltaMovement(Vec3.ZERO);
      }
   }

   @Override
   public String getStatus() {
      return this.J() ? "Holding" : "";
   }
}
