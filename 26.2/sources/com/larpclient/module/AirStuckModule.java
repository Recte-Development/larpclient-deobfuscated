package com.larpclient.module;

import com.larpclient.c5;
import java.util.Set;
import net.minecraft.world.phys.Vec3;

public final class AirStuckModule extends Module {
   private static AirStuckModule p;

   public AirStuckModule() {
      super("AirStuck", "Hold your position", Category.MOVEMENT, -1);
      p = this;
   }

   public static AirStuckModule T() {
      return p;
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.isInGame() && c5.J() && !mc.player.isPassenger();
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.j;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onTick() {
      int var10000 = this.I();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         mc.player.setDeltaMovement(Vec3.ZERO);
      }
   }

   @Override
   public String getStatus() {
      return this.I() ? "Holding" : "";
   }
}
