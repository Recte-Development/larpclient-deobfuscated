package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.j4;
import com.larpclient.nz;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.phys.Vec3;

public final class BoatFlyModule extends Module {
   private static BoatFlyModule h;
   private final b N = this.addSetting(new b("Speed", "Horizontal flight speed", 20.0, 2.0, 80.0, 1.0)).A(" b/s");
   private final b K = this.addSetting(new b("Vertical Speed", "Climb speed while holding Jump and descent speed while holding Sprint", 10.0, 1.0, 40.0, 1.0))
      .A(" b/s");
   private final b c = this.addSetting(new b("Fall Speed", "Slow descent with no vertical input; 0 hovers in place", 0.0, 0.0, 10.0, 0.1)).A(" b/s");
   private final b Q = this.addSetting(new b("Smoothing", "Ticks to reach full speed or come to a stop; 0 is instant", 3.0, 0.0, 20.0, 1.0));
   private final m W = this.addSetting(new m("Anti Kick", "Dip slightly every 2 seconds in open air so a vanilla server does not kick you for floating", true));
   private AbstractBoat Y;
   private j4 V;
   private double s;
   private long F;
   private int R;
   private int k;
   private String A = "Ride a boat";

   public BoatFlyModule() {
      super("Boat Fly", "Fly the boat you steer where you look; Jump climbs, Sprint descends", Category.MOVEMENT, -1);
      h = this;
   }

   public static BoatFlyModule d() {
      return h;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.A : "";
   }

   @Override
   public void onEnable() {
      this.l(null);
      _q.c(
         "BoatFly",
         "Enabled: speed {} b/s, vertical {} b/s, fall {} b/s, smoothing {}, anti kick {}",
         this.N.getValue(),
         this.K.getValue(),
         this.c.getValue(),
         this.Q.getValue(),
         this.W.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.Y != null) {
         if (!this.isInGame() || mc.player.getVehicle() != this.Y || this.Y.getControllingPassenger() != mc.player) {
            this.l("Left the boat");
         }
      }
   }

   @Override
   public void onDisable() {
      this.l("Disabled");
   }

   public static boolean z(AbstractBoat var0) {
      if (h != null && h.isEnabled()) {
         BoatFlyModule var1 = h;
         if (var1 != null && var1.isRuntimeActive() && var0 != null) {
            try {
               Minecraft var2 = getMc();
               if (var2.player != null && var2.player.isAlive() && var0.level() == var2.level && var0.getControllingPassenger() == var2.player) {
                  var1.E(var0);
                  return true;
               } else {
                  return false;
               }
            } catch (RuntimeException var3) {
               _q.o("BoatFly", "Boat control failed; disabling and returning native boat steering", var3);
               var1.disable();
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void y() {
      BoatFlyModule var0 = h;
      if (var0 != null && var0.Y != null && var0.V != null) {
         var0.V.c();
         var0.R++;
         var0.k = 40;
         var0.A = "Server corrected the boat; try a lower speed";
         if (var0.R == 1 || var0.R % 20 == 0) {
            _q.R("BoatFly", "Server corrected the boat position ({} so far, speed {} b/s)", var0.R, var0.N.getValue());
         }
      }
   }

   private void E(AbstractBoat var1) {
      if (var1 != this.Y) {
         this.l(this.Y == null ? null : "Changed boats");
         this.Y = var1;
         this.V = new j4();
         this.s = var1.getY();
         _q.c("BoatFly", "Flying {} at {}", var1.getName().getString(), var1.blockPosition().toShortString());
      }

      this.F++;
      this.V.z(var1.getY() - this.s, var1.verticalCollisionBelow || !this.d(var1));
      this.s = var1.getY();
      Options var2 = mc.options;
      int var3 = mc.gui.screen() == null && nz.v() ? 1 : 0;
      Vec3 var4 = var3 != 0 ? nz.b() : Vec3.ZERO;
      int var5 = var3 == 0 ? 0 : (var2.keyJump.isDown() ? 1 : 0) - (nz.K(var2.keySprint) ? 1 : 0);
      int var6 = this.V.q();
      Vec3 var7 = this.V
         .t(var4, this.N.getValue() / 20.0, this.Q.getValue().intValue(), var5, this.K.getValue() / 20.0, this.c.getValue() / 20.0, this.W.getValue());
      if (this.V.q() == 1 && var6 == 0) {
         _q.c("BoatFly", "Anti Kick dip after {} floating ticks; later dips are not logged", 40);
      }

      var1.setYRot(mc.player.getYRot());
      var1.setDeltaMovement(var7);
      int var8 = this.V.b().lengthSqr() > 1.0E-4 ? 1 : 0;
      var1.setPaddleState((boolean)var8, (boolean)var8);
      if (this.k > 0) {
         this.k--;
      } else {
         if (this.V.U()) {
            this.A = "Anti Kick dip";
         } else if (var8 == 0 && var5 == 0) {
            this.A = this.c.getValue() > 0.0 ? "Gliding down" : "Hovering";
         } else {
            this.A = "Flying";
         }
      }
   }

   private boolean d(AbstractBoat var1) {
      return var1.level().getBlockStates(var1.getBoundingBox().inflate(0.0625).expandTowards(0.0, -0.55, 0.0)).allMatch(BlockStateBase::isAir);
   }

   private void l(String var1) {
      if (this.Y != null && var1 != null) {
         _q.c("BoatFly", "{} after {} ticks: {} anti kick dips, {} server corrections", var1, this.F, this.V == null ? 0 : this.V.q(), this.R);
      }

      this.Y = null;
      this.V = null;
      this.F = 0L;
      this.R = 0;
      this.k = 0;
      this.A = "Ride a boat";
   }
}
