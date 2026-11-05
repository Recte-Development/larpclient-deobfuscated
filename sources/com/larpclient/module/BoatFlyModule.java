package com.larpclient.module;

import com.larpclient.mf;
import com.larpclient.r4;
import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.phys.Vec3;

public final class BoatFlyModule extends Module {
   private static BoatFlyModule K;
   private final lc b = this.addSetting(new lc("Speed", "Horizontal flight speed", 20.0, 2.0, 80.0, 1.0)).q(" b/s");
   private final lc F = this.addSetting(new lc("Vertical Speed", "Climb speed while holding Jump and descent speed while holding Sprint", 10.0, 1.0, 40.0, 1.0))
      .q(" b/s");
   private final lc j = this.addSetting(new lc("Fall Speed", "Slow descent with no vertical input; 0 hovers in place", 0.0, 0.0, 10.0, 0.1)).q(" b/s");
   private final lc R = this.addSetting(new lc("Smoothing", "Ticks to reach full speed or come to a stop; 0 is instant", 3.0, 0.0, 20.0, 1.0));
   private final ln n = this.addSetting(
      new ln("Anti Kick", "Dip slightly every 2 seconds in open air so a vanilla server does not kick you for floating", true)
   );
   private AbstractBoat f;
   private mf O;
   private double m;
   private long g;
   private int k;
   private int o;
   private String a = "Ride a boat";

   public BoatFlyModule() {
      super("Boat Fly", "Fly the boat you steer where you look; Jump climbs, Sprint descends", Category.MOVEMENT, -1);
      K = this;
   }

   public static BoatFlyModule p() {
      return K;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.a : "";
   }

   @Override
   public void onEnable() {
      this.i(null);
      r4.g(
         "BoatFly",
         "Enabled: speed {} b/s, vertical {} b/s, fall {} b/s, smoothing {}, anti kick {}",
         this.b.getValue(),
         this.F.getValue(),
         this.j.getValue(),
         this.R.getValue(),
         this.n.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.f != null) {
         if (!this.isInGame() || mc.player.getVehicle() != this.f || this.f.getControllingPassenger() != mc.player) {
            this.i("Left the boat");
         }
      }
   }

   @Override
   public void onDisable() {
      this.i("Disabled");
   }

   public static boolean k(AbstractBoat var0) {
      if (K != null && K.isEnabled()) {
         BoatFlyModule var1 = K;
         if (var1 != null && var1.isRuntimeActive() && var0 != null) {
            try {
               Minecraft var2 = getMc();
               if (var2.player != null && var2.player.isAlive() && var0.level() == var2.level && var0.getControllingPassenger() == var2.player) {
                  var1.Z(var0);
                  return true;
               } else {
                  return false;
               }
            } catch (RuntimeException var3) {
               r4.r("BoatFly", "Boat control failed; disabling and returning native boat steering", var3);
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

   public static void E() {
      BoatFlyModule var0 = K;
      if (var0 != null && var0.f != null && var0.O != null) {
         var0.O.o();
         var0.k++;
         var0.o = 40;
         var0.a = "Server corrected the boat; try a lower speed";
         if (var0.k == 1 || var0.k % 20 == 0) {
            r4.m("BoatFly", "Server corrected the boat position ({} so far, speed {} b/s)", var0.k, var0.b.getValue());
         }
      }
   }

   private void Z(AbstractBoat var1) {
      if (var1 != this.f) {
         this.i(this.f == null ? null : "Changed boats");
         this.f = var1;
         this.O = new mf();
         this.m = var1.getY();
         r4.g("BoatFly", "Flying {} at {}", var1.getName().getString(), var1.blockPosition().toShortString());
      }

      this.g++;
      this.O.H(var1.getY() - this.m, var1.verticalCollisionBelow || !this.p(var1));
      this.m = var1.getY();
      Options var2 = mc.options;
      int var3 = mc.gui.screen() == null && yu.r() ? 1 : 0;
      Vec3 var4 = var3 != 0 ? yu.q() : Vec3.ZERO;
      int var5 = var3 == 0 ? 0 : (var2.keyJump.isDown() ? 1 : 0) - (yu.X(var2.keySprint) ? 1 : 0);
      int var6 = this.O.H();
      Vec3 var7 = this.O
         .G(var4, this.b.getValue() / 20.0, this.R.getValue().intValue(), var5, this.F.getValue() / 20.0, this.j.getValue() / 20.0, this.n.getValue());
      if (this.O.H() == 1 && var6 == 0) {
         r4.g("BoatFly", "Anti Kick dip after {} floating ticks; later dips are not logged", 40);
      }

      var1.setYRot(mc.player.getYRot());
      var1.setDeltaMovement(var7);
      int var8 = this.O.P().lengthSqr() > 1.0E-4 ? 1 : 0;
      var1.setPaddleState((boolean)var8, (boolean)var8);
      if (this.o > 0) {
         this.o--;
      } else {
         if (this.O.V()) {
            this.a = "Anti Kick dip";
         } else if (var8 == 0 && var5 == 0) {
            this.a = this.j.getValue() > 0.0 ? "Gliding down" : "Hovering";
         } else {
            this.a = "Flying";
         }
      }
   }

   private boolean p(AbstractBoat var1) {
      return var1.level().getBlockStates(var1.getBoundingBox().inflate(0.0625).expandTowards(0.0, -0.55, 0.0)).allMatch(BlockStateBase::isAir);
   }

   private void i(String var1) {
      if (this.f != null && var1 != null) {
         r4.g("BoatFly", "{} after {} ticks: {} anti kick dips, {} server corrections", var1, this.g, this.O == null ? 0 : this.O.H(), this.k);
      }

      this.f = null;
      this.O = null;
      this.g = 0L;
      this.k = 0;
      this.o = 0;
      this.a = "Ride a boat";
   }
}
