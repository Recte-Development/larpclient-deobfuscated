package com.larpclient.module;

import com.larpclient.hn;
import com.larpclient.j;
import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_10255;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_4970.class_4971;

public final class BoatFlyModule extends Module {
   private static BoatFlyModule j;
   private final lo i = this.addSetting(new lo("Speed", "Horizontal flight speed", 20.0, 2.0, 80.0, 1.0)).m(" b/s");
   private final lo l = this.addSetting(new lo("Vertical Speed", "Climb speed while holding Jump and descent speed while holding Sprint", 10.0, 1.0, 40.0, 1.0))
      .m(" b/s");
   private final lo b = this.addSetting(new lo("Fall Speed", "Slow descent with no vertical input; 0 hovers in place", 0.0, 0.0, 10.0, 0.1)).m(" b/s");
   private final lo T = this.addSetting(new lo("Smoothing", "Ticks to reach full speed or come to a stop; 0 is instant", 3.0, 0.0, 20.0, 1.0));
   private final l3 V = this.addSetting(
      new l3("Anti Kick", "Dip slightly every 2 seconds in open air so a vanilla server does not kick you for floating", true)
   );
   private class_10255 X;
   private hn F;
   private double Y;
   private long m;
   private int k;
   private int Q;
   private String K = "Ride a boat";

   public BoatFlyModule() {
      super("Boat Fly", "Fly the boat you steer where you look; Jump climbs, Sprint descends", Category.MOVEMENT, -1);
      j = this;
   }

   public static BoatFlyModule U() {
      return j;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.K : "";
   }

   @Override
   public void onEnable() {
      this.A(null);
      q8.v(
         "BoatFly",
         "Enabled: speed {} b/s, vertical {} b/s, fall {} b/s, smoothing {}, anti kick {}",
         this.i.getValue(),
         this.l.getValue(),
         this.b.getValue(),
         this.T.getValue(),
         this.V.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.X != null) {
         if (!this.isInGame() || mc.field_1724.method_5854() != this.X || this.X.method_5642() != mc.field_1724) {
            this.A("Left the boat");
         }
      }
   }

   @Override
   public void onDisable() {
      this.A("Disabled");
   }

   public static boolean r(class_10255 var0) {
      if (j != null && j.isEnabled()) {
         BoatFlyModule var1 = j;
         if (var1 != null && var1.isRuntimeActive() && var0 != null) {
            try {
               class_310 var2 = getMc();
               if (var2.field_1724 != null && var2.field_1724.method_5805() && var0.method_73183() == var2.field_1687 && var0.method_5642() == var2.field_1724) {
                  var1.P(var0);
                  return true;
               } else {
                  return false;
               }
            } catch (RuntimeException var3) {
               q8.u("BoatFly", "Boat control failed; disabling and returning native boat steering", var3);
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

   public static void D() {
      BoatFlyModule var0 = j;
      if (var0 != null && var0.X != null && var0.F != null) {
         var0.F.W();
         var0.k++;
         var0.Q = 40;
         var0.K = "Server corrected the boat; try a lower speed";
         if (var0.k == 1 || var0.k % 20 == 0) {
            q8.L("BoatFly", "Server corrected the boat position ({} so far, speed {} b/s)", var0.k, var0.i.getValue());
         }
      }
   }

   private void P(class_10255 var1) {
      if (var1 != this.X) {
         this.A(this.X == null ? null : "Changed boats");
         this.X = var1;
         this.F = new hn();
         this.Y = var1.method_23318();
         q8.v("BoatFly", "Flying {} at {}", var1.method_5477().getString(), var1.method_24515().method_23854());
      }

      this.m++;
      this.F.D(var1.method_23318() - this.Y, var1.field_36331 || !this.k(var1));
      this.Y = var1.method_23318();
      class_315 var2 = mc.field_1690;
      int var3 = mc.field_1755 == null && com.larpclient.j.k() ? 1 : 0;
      class_243 var4 = var3 != 0 ? com.larpclient.j.i() : class_243.field_1353;
      int var5 = var3 == 0 ? 0 : (var2.field_1903.method_1434() ? 1 : 0) - (com.larpclient.j.W(var2.field_1867) ? 1 : 0);
      int var6 = this.F.X();
      class_243 var7 = this.F
         .J(var4, this.i.getValue() / 20.0, this.T.getValue().intValue(), var5, this.l.getValue() / 20.0, this.b.getValue() / 20.0, this.V.getValue());
      if (this.F.X() == 1 && var6 == 0) {
         q8.v("BoatFly", "Anti Kick dip after {} floating ticks; later dips are not logged", 40);
      }

      var1.method_36456(mc.field_1724.method_36454());
      var1.method_18799(var7);
      int var8 = this.F.N().method_1027() > 1.0E-4 ? 1 : 0;
      var1.method_64490((boolean)var8, (boolean)var8);
      if (this.Q > 0) {
         this.Q--;
      } else {
         if (this.F.e()) {
            this.K = "Anti Kick dip";
         } else if (var8 == 0 && var5 == 0) {
            this.K = this.b.getValue() > 0.0 ? "Gliding down" : "Hovering";
         } else {
            this.K = "Flying";
         }
      }
   }

   private boolean k(class_10255 var1) {
      return var1.method_73183().method_29546(var1.method_5829().method_1014(0.0625).method_1012(0.0, -0.55, 0.0)).allMatch(class_4971::method_26215);
   }

   private void A(String var1) {
      if (this.X != null && var1 != null) {
         q8.v("BoatFly", "{} after {} ticks: {} anti kick dips, {} server corrections", var1, this.m, this.F == null ? 0 : this.F.X(), this.k);
      }

      this.X = null;
      this.F = null;
      this.m = 0L;
      this.k = 0;
      this.Q = 0;
      this.K = "Ride a boat";
   }
}
