package com.larpclient.module;

import com.larpclient.yu;
import java.util.Set;
import net.minecraft.world.phys.Vec3;

public final class AirStuckModule extends Module {
   private static AirStuckModule t;

   public AirStuckModule() {
      super("AirStuck", "Hold your position", Category.MOVEMENT, -1);
      t = this;
   }

   public static AirStuckModule H() {
      return t;
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.isInGame() && yu.r() && !mc.player.isPassenger();
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.N;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public void onTick() {
      int var10000 = this.D();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         mc.player.setDeltaMovement(Vec3.ZERO);
      }
   }

   @Override
   public String getStatus() {
      return this.D() ? "Holding" : "";
   }
}
