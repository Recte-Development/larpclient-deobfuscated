package com.larpclient.module;

import com.larpclient.k4;
import com.larpclient.mt;
import com.larpclient.te;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.phys.Vec3;

public final class BoatFlyModule extends Module {
   private static BoatFlyModule R;
   private final d9 a = this.addSetting(new d9("Speed", "Horizontal flight speed", 20.0, 2.0, 80.0, 1.0)).T(" b/s");
   private final d9 S = this.addSetting(new d9("Vertical Speed", "Climb speed while holding Jump and descent speed while holding Sprint", 10.0, 1.0, 40.0, 1.0))
      .T(" b/s");
   private final d9 j = this.addSetting(new d9("Fall Speed", "Slow descent with no vertical input; 0 hovers in place", 0.0, 0.0, 10.0, 0.1)).T(" b/s");
   private final d9 X = this.addSetting(new d9("Smoothing", "Ticks to reach full speed or come to a stop; 0 is instant", 3.0, 0.0, 20.0, 1.0));
   private final dh L = this.addSetting(
      new dh("Anti Kick", "Dip slightly every 2 seconds in open air so a vanilla server does not kick you for floating", true)
   );
   private AbstractBoat W;
   private k4 A;
   private double B;
   private long c;
   private int O;
   private int t;
   private String E = "Ride a boat";

   public BoatFlyModule() {
      super("Boat Fly", "Fly the boat you steer where you look; Jump climbs, Sprint descends", Category.MOVEMENT, -1);
      R = this;
   }

   public static BoatFlyModule p() {
      return R;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.E : "";
   }

   @Override
   public void onEnable() {
      this.R(null);
      mt.U(
         "BoatFly",
         "Enabled: speed {} b/s, vertical {} b/s, fall {} b/s, smoothing {}, anti kick {}",
         this.a.P(),
         this.S.P(),
         this.j.P(),
         this.X.P(),
         this.L.P()
      );
   }

   @Override
   public void onTick() {
      if (this.W != null) {
         if (!this.isInGame() || mc.player.getVehicle() != this.W || this.W.getControllingPassenger() != mc.player) {
            this.R("Left the boat");
         }
      }
   }

   @Override
   public void onDisable() {
      this.R("Disabled");
   }

   public static boolean P(AbstractBoat var0) {
      if (R != null && R.isEnabled()) {
         BoatFlyModule var1 = R;
         if (var1 != null && var1.isRuntimeActive() && var0 != null) {
            try {
               Minecraft var2 = getMc();
               if (var2.player != null && var2.player.isAlive() && var0.level() == var2.level && var0.getControllingPassenger() == var2.player) {
                  var1.L(var0);
                  return true;
               } else {
                  return false;
               }
            } catch (RuntimeException var3) {
               mt.P("BoatFly", "Boat control failed; disabling and returning native boat steering", var3);
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
      BoatFlyModule var0 = R;
      if (var0 != null && var0.W != null && var0.A != null) {
         var0.A.i();
         var0.O++;
         var0.t = 40;
         var0.E = "Server corrected the boat; try a lower speed";
         if (var0.O == 1 || var0.O % 20 == 0) {
            mt.T("BoatFly", "Server corrected the boat position ({} so far, speed {} b/s)", var0.O, var0.a.P());
         }
      }
   }

   private void L(AbstractBoat var1) {
      if (var1 != this.W) {
         this.R(this.W == null ? null : "Changed boats");
         this.W = var1;
         this.A = new k4();
         this.B = var1.getY();
         mt.U("BoatFly", "Flying {} at {}", var1.getName().getString(), var1.blockPosition().toShortString());
      }

      this.c++;
      this.A.j(var1.getY() - this.B, var1.verticalCollisionBelow || !this.M(var1));
      this.B = var1.getY();
      Options var2 = mc.options;
      int var3 = mc.gui.screen() == null && te.i() ? 1 : 0;
      Vec3 var4 = var3 != 0 ? te.S() : Vec3.ZERO;
      int var5 = var3 == 0 ? 0 : (var2.keyJump.isDown() ? 1 : 0) - (te.e(var2.keySprint) ? 1 : 0);
      int var6 = this.A.s();
      Vec3 var7 = this.A.O(var4, this.a.P() / 20.0, this.X.P().intValue(), var5, this.S.P() / 20.0, this.j.P() / 20.0, this.L.P());
      if (this.A.s() == 1 && var6 == 0) {
         mt.U("BoatFly", "Anti Kick dip after {} floating ticks; later dips are not logged", 40);
      }

      var1.setYRot(mc.player.getYRot());
      var1.setDeltaMovement(var7);
      int var8 = this.A.m().lengthSqr() > 1.0E-4 ? 1 : 0;
      var1.setPaddleState((boolean)var8, (boolean)var8);
      if (this.t > 0) {
         this.t--;
      } else {
         if (this.A.O()) {
            this.E = "Anti Kick dip";
         } else if (var8 == 0 && var5 == 0) {
            this.E = this.j.P() > 0.0 ? "Gliding down" : "Hovering";
         } else {
            this.E = "Flying";
         }
      }
   }

   private boolean M(AbstractBoat var1) {
      return var1.level().getBlockStates(var1.getBoundingBox().inflate(0.0625).expandTowards(0.0, -0.55, 0.0)).allMatch(BlockStateBase::isAir);
   }

   private void R(String var1) {
      if (this.W != null && var1 != null) {
         mt.U("BoatFly", "{} after {} ticks: {} anti kick dips, {} server corrections", var1, this.c, this.A == null ? 0 : this.A.s(), this.O);
      }

      this.W = null;
      this.A = null;
      this.c = 0L;
      this.O = 0;
      this.t = 0;
      this.E = "Ride a boat";
   }
}
