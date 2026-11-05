package com.larpclient.module;

import com.larpclient.te;
import java.util.Set;
import net.minecraft.world.phys.Vec3;

public final class AirStuckModule extends Module {
   private static AirStuckModule H;

   public AirStuckModule() {
      super("AirStuck", "Hold your position", Category.MOVEMENT, -1);
      H = this;
   }

   public static AirStuckModule X() {
      return H;
   }

   public boolean U() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.isInGame() && te.i() && !mc.player.isPassenger();
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.b;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public void onTick() {
      int var10000 = this.U();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         mc.player.setDeltaMovement(Vec3.ZERO);
      }
   }

   @Override
   public String getStatus() {
      return this.U() ? "Holding" : "";
   }
}
