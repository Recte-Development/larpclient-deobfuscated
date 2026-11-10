package com.larpclient.module;

import com.larpclient.ba;
import com.larpclient.c5;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.phys.Vec3;

public final class BoatFlyModule extends Module {
   private static BoatFlyModule o;
   private final e p = this.addSetting(new e("Speed", "Horizontal flight speed", 20.0, 2.0, 80.0, 1.0)).V(" b/s");
   private final e a = this.addSetting(new e("Vertical Speed", "Climb speed while holding Jump and descent speed while holding Sprint", 10.0, 1.0, 40.0, 1.0))
      .V(" b/s");
   private final e L = this.addSetting(new e("Fall Speed", "Slow descent with no vertical input; 0 hovers in place", 0.0, 0.0, 10.0, 0.1)).V(" b/s");
   private final e G = this.addSetting(new e("Smoothing", "Ticks to reach full speed or come to a stop; 0 is instant", 3.0, 0.0, 20.0, 1.0));
   private final s j = this.addSetting(new s("Anti Kick", "Dip slightly every 2 seconds in open air so a vanilla server does not kick you for floating", true));
   private AbstractBoat n;
   private ba i;
   private double u;
   private long m;
   private int D;
   private int A;
   private String S = "Ride a boat";

   public BoatFlyModule() {
      super("Boat Fly", "Fly the boat you steer where you look; Jump climbs, Sprint descends", Category.MOVEMENT, -1);
      o = this;
   }

   public static BoatFlyModule T() {
      return o;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.S : "";
   }

   @Override
   public void onEnable() {
      this.o(null);
      com.larpclient.h.U(
         "BoatFly",
         "Enabled: speed {} b/s, vertical {} b/s, fall {} b/s, smoothing {}, anti kick {}",
         this.p.getValue(),
         this.a.getValue(),
         this.L.getValue(),
         this.G.getValue(),
         this.j.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.n != null) {
         if (!this.isInGame() || mc.player.getVehicle() != this.n || this.n.getControllingPassenger() != mc.player) {
            this.o("Left the boat");
         }
      }
   }

   @Override
   public void onDisable() {
      this.o("Disabled");
   }

   public static boolean Q(AbstractBoat var0) {
      if (o != null && o.isEnabled()) {
         BoatFlyModule var1 = o;
         if (var1 != null && var1.isRuntimeActive() && var0 != null) {
            try {
               Minecraft var2 = getMc();
               if (var2.player != null && var2.player.isAlive() && var0.level() == var2.level && var0.getControllingPassenger() == var2.player) {
                  var1.P(var0);
                  return true;
               } else {
                  return false;
               }
            } catch (RuntimeException var3) {
               com.larpclient.h.m("BoatFly", "Boat control failed; disabling and returning native boat steering", var3);
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

   public static void q() {
      BoatFlyModule var0 = o;
      if (var0 != null && var0.n != null && var0.i != null) {
         var0.i.J();
         var0.D++;
         var0.A = 40;
         var0.S = "Server corrected the boat; try a lower speed";
         if (var0.D == 1 || var0.D % 20 == 0) {
            com.larpclient.h.g("BoatFly", "Server corrected the boat position ({} so far, speed {} b/s)", var0.D, var0.p.getValue());
         }
      }
   }

   private void P(AbstractBoat var1) {
      if (var1 != this.n) {
         this.o(this.n == null ? null : "Changed boats");
         this.n = var1;
         this.i = new ba();
         this.u = var1.getY();
         com.larpclient.h.U("BoatFly", "Flying {} at {}", var1.getName().getString(), var1.blockPosition().toShortString());
      }

      this.m++;
      this.i.p(var1.getY() - this.u, var1.verticalCollisionBelow || !this.a(var1));
      this.u = var1.getY();
      Options var2 = mc.options;
      int var3 = mc.gui.screen() == null && c5.J() ? 1 : 0;
      Vec3 var4 = var3 != 0 ? c5.D() : Vec3.ZERO;
      int var5 = var3 == 0 ? 0 : (var2.keyJump.isDown() ? 1 : 0) - (c5.J(var2.keySprint) ? 1 : 0);
      int var6 = this.i.h();
      Vec3 var7 = this.i
         .G(var4, this.p.getValue() / 20.0, this.G.getValue().intValue(), var5, this.a.getValue() / 20.0, this.L.getValue() / 20.0, this.j.getValue());
      if (this.i.h() == 1 && var6 == 0) {
         com.larpclient.h.U("BoatFly", "Anti Kick dip after {} floating ticks; later dips are not logged", 40);
      }

      var1.setYRot(mc.player.getYRot());
      var1.setDeltaMovement(var7);
      int var8 = this.i.J().lengthSqr() > 1.0E-4 ? 1 : 0;
      var1.setPaddleState((boolean)var8, (boolean)var8);
      if (this.A > 0) {
         this.A--;
      } else {
         if (this.i.q()) {
            this.S = "Anti Kick dip";
         } else if (var8 == 0 && var5 == 0) {
            this.S = this.L.getValue() > 0.0 ? "Gliding down" : "Hovering";
         } else {
            this.S = "Flying";
         }
      }
   }

   private boolean a(AbstractBoat var1) {
      return var1.level().getBlockStates(var1.getBoundingBox().inflate(0.0625).expandTowards(0.0, -0.55, 0.0)).allMatch(BlockStateBase::isAir);
   }

   private void o(String var1) {
      if (this.n != null && var1 != null) {
         com.larpclient.h.U("BoatFly", "{} after {} ticks: {} anti kick dips, {} server corrections", var1, this.m, this.i == null ? 0 : this.i.h(), this.D);
      }

      this.n = null;
      this.i = null;
      this.m = 0L;
      this.D = 0;
      this.A = 0;
      this.S = "Ride a boat";
   }
}
