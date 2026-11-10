package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larp.obf.q4031f93fe72647cb.qf65e76ba6afee893;
import com.larpclient.cr;
import com.larpclient.ep;
import com.larpclient.fk;
import com.larpclient.fs;
import com.larpclient.gn;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.uz;
import com.larpclient.w6;
import com.larpclient.wp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_10130;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_640;
import net.minecraft.class_642;
import net.minecraft.class_746;
import net.minecraft.class_8956;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class WindChargeHopModule extends Module {
   private static final int l = 40;
   private static final int w = 30;
   private static WindChargeHopModule I;
   private final lw v = this.addSetting(new lw("Hop Bind", "Press for one wind charge hop; separate from the module toggle", -1));
   private final li P = this.addSetting(
      new li("Direction", "Up throws straight down for height; Forward throws behind you so the burst pushes you where you look", "Up", "Up", "Forward")
   );
   private final li b = this.addSetting(
      new li(
         "Jump",
         "With Throw jumps as you throw, so the push adds to the jump; Timed jumps just before the push arrives, from your ping and measured hops; Off never jumps",
         "With Throw",
         "With Throw",
         "Timed",
         "Off"
      )
   );
   private final l3 O = this.addSetting(new l3("Swap Back", "Return to your previous hotbar slot the tick after the throw", true));
   private final lo C = this.addSetting(
      new lo(
         "Turn Speed",
         "Most the player turns per tick for the throw; your camera stays on your look. 180 with Humanize off turns at once",
         45.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final lo e = this.addSetting(
      new lo("Return Speed", "Most the player turns per tick back to your look after the throw. 180 returns at once", 35.0, 10.0, 180.0, 5.0)
   );
   private final l3 y = this.addSetting(
      new l3(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final gn V = new gn();
   private final fs q = new fs(this);
   private static final Map<String, Integer> d = new HashMap<>();
   private WindChargeHopModule.g p = WindChargeHopModule.g.R;
   private boolean H;
   private class_746 h;
   private class_638 i;
   private ep.o J;
   private long c;
   private long t = Long.MIN_VALUE;
   private int W;
   private int N;
   private double j;
   private double K;
   private double S;
   private boolean R;
   private float f;
   private float B;
   private final Set<Integer> A = new HashSet<>();
   private int s;
   private String X = "Set a Hop Bind";
   private int o;

   public WindChargeHopModule() {
      super("Wind Charge Hop", "Throw a wind charge at the ground for a timed boost, with a silent turn", Category.MOVEMENT, -1);
      I = this;
      this.P.a(var1 -> this.h("Direction changed"));
      this.v.a(var1 -> this.h("Bind changed"));
   }

   public static WindChargeHopModule T() {
      return I;
   }

   public gn H() {
      return this.V;
   }

   public double t() {
      return this.e.getValue();
   }

   public boolean J() {
      return this.y.getValue();
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.X : "";
   }

   public boolean u() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.h != null
            && this.h == mc.field_1724
            && this.i == mc.field_1687
            && (this.p == WindChargeHopModule.g.q || this.c == this.t);
   }

   public static void J(int var0) {
      if (I != null && I.isEnabled()) {
         WindChargeHopModule var1 = I;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.v.getValue()) {
            var1.H = (boolean)1;
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.h("Waiting for a world");
      } else {
         if (this.h != mc.field_1724 || this.i != mc.field_1687) {
            this.h("World changed");
            this.h = mc.field_1724;
            this.i = mc.field_1687;
            this.N = 0;
         }

         this.c++;
         boolean var5 = false;
         int var4 = this.N;
         if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
            this.W();
         }

         if (this.p != WindChargeHopModule.g.q && this.q.I() && this.c > this.t) {
            this.p();
         }

         boolean var1 = this.H;
         this.H = (boolean)0;
         if (this.p == WindChargeHopModule.g.R) {
            if (var1) {
               this.X();
            } else {
               int var12 = this.v.getValue();
               boolean var7 = false;
               int var6 = var12;
               if ((Integer.signum(var6) >>> 31 ^ 1) == 0) {
                  this.X = "Set a Hop Bind";
               }
            }
         } else if (this.p == WindChargeHopModule.g.P) {
            this.Y();
         } else {
            String var2 = this.q();
            if (var2 != null) {
               this.h(var2);
            } else {
               ep.o var3 = ep.e(
                  this.h.method_73189(), this.h.method_5751(), this.h.method_18798(), this.h.method_24828(), this.Z(), this.C()[0], this.x(), this::y
               );
               if (var3 == null) {
                  this.h("No ground close enough for a boost");
               } else {
                  this.J = var3;
                  this.Q();
                  cr.y(this, this::H, () -> this.p == WindChargeHopModule.g.q && this.q() == null && this.Y() && this.N());
                  int var10000 = ++this.o;
                  byte var9 = 30;
                  int var8 = var10000;
                  if ((-Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
                     int var10001 = this.N();
                     boolean var11 = false;
                     byte var10 = (byte)var10001;
                     var10001 = Integer.signum(var10);
                     this.h(((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "The throw could not go out" : "The turn did not reach a throw angle in time");
                  }
               }
            }
         }
      }
   }

   private void X() {
      String var1 = this.q();
      if (var1 == null) {
         var1 = this.A();
      }

      if (var1 != null) {
         this.X = var1;
         q8.G("WindHop", "Hop refused: {}", var1);
      } else {
         class_746 var2 = this.h;
         float var3 = this.C()[0];
         int var4 = !this.b.G("Off") && var2.method_24828() ? 1 : 0;
         this.J = ep.e(var2.method_73189(), var2.method_5751(), var2.method_18798(), var2.method_24828(), this.Z(), var3, this.x(), this::y);
         if (this.J == null) {
            this.X = "No ground close enough for a boost";
            q8.G("WindHop", "Hop refused: no burst within {} blocks of you", 2.4);
         } else {
            this.p = WindChargeHopModule.g.q;
            this.o = 0;
            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            this.f = this.x() ? (float)(var5.nextDouble() * 6.0 - 3.0) : 0.0F;
            this.B = this.x() ? (float)(var5.nextDouble() * 4.0 - 2.0) : (float)(-(var5.nextDouble() * 3.0));
            this.V.z();
            this.t();
            q8.G(
               "WindHop",
               "Hop: {}, best throw yaw {} pitch {}, burst after {} ticks at {}, predicted push {} up {} along ({} blocks/tick), jump {}; turn {}/tick, return {}/tick, humanize {}",
               this.P.getValue(),
               Y(this.J.yaw()),
               Y(this.J.pitch()),
               this.J.ticks(),
               o(this.J.burst()),
               Y(this.J.push().field_1351),
               Y(Math.hypot(this.J.push().field_1352, this.J.push().field_1350)),
               Y(this.J.push().method_1033()),
               var4 != 0 ? this.b.getValue() : "none",
               this.C.getValue().intValue(),
               this.e.getValue().intValue(),
               this.y.getValue()
            );
            this.Q();
            cr.y(this, this::H, () -> this.p == WindChargeHopModule.g.q && this.q() == null && this.Y() && this.N());
         }
      }
   }

   private boolean x() {
      return this.P.G("Forward");
   }

   private boolean Z() {
      return this.b.G("With Throw") && this.h.method_24828();
   }

   private ep.o n() {
      return ep.O(
         this.h.method_73189(),
         this.h.method_5751(),
         this.h.method_18798(),
         this.h.method_24828(),
         this.Z(),
         this.h.method_36454(),
         this.h.method_36455(),
         this::y
      );
   }

   private boolean N() {
      return ep.R(this.J, this.n(), this.C()[0], this.x());
   }

   private float[] o() {
      return !this.x()
         ? new float[]{this.h.method_36454(), Math.min(90.0F, this.J.pitch() + this.B)}
         : new float[]{this.J.yaw() + this.f, class_3532.method_15363(this.J.pitch() + this.B, -90.0F, 90.0F)};
   }

   private void t() {
      int var1 = fk.T(class_1802.field_49098, true);
      if (var1 >= 0 && var1 != this.h.method_31548().method_67532() && this.q.m(this.h, var1)) {
         if (mc.field_1761 != null) {
            ((q226fb0b431f37244)mc.field_1761).larpSyncCarriedItem();
         }
      }
   }

   private ep.i y(class_243 var1, class_243 var2) {
      class_3965 var3 = this.i.method_17742(new class_3959(var1, var2, class_3960.field_17558, class_242.field_1348, this.h));
      return var3.method_17783() == class_240.field_1333 ? null : new ep.i(var3.method_17784(), class_243.method_24954(var3.method_17780().method_62675()));
   }

   private void Q() {
      if (cr.a(this)) {
         wp.S(wp.z.g);
         if (wp.o() == wp.z.g) {
            float[] var1 = this.o();
            float[] var2 = this.V.j(this.h.method_36454(), this.h.method_36455(), var1[0], var1[1], this.C.getValue(), this.y.getValue());
            t9.O(this, t9.v.O, var2[0], var2[1], 1.0);
         }
      }
   }

   private void H() {
      class_746 var1 = this.h;
      ep.o var2 = this.n();
      if (this.p == WindChargeHopModule.g.q && ep.R(this.J, var2, this.C()[0], this.x())) {
         int var3 = !this.b.G("Off") && var1.method_24828() ? 1 : 0;
         if (this.b.G("With Throw") && var3 != 0) {
            var1.method_6043();
         }

         this.A.clear();

         for (class_1297 var5 : mc.field_1687.method_18112()) {
            if (var5 instanceof class_8956 var6 && var6.method_24921() == var1) {
               this.A.add(var6.method_5628());
            }
         }

         if (!this.B()) {
            this.h("The wind charge throw was rejected");
         } else {
            this.t = this.c;
            this.s = -1;
            this.j = this.K = var1.method_23318();
            this.S = var1.method_18798().field_1351;
            this.R = (boolean)0;
            this.N = 40;
            String var8 = this.h();
            int var9 = d.getOrDefault(var8, 0);
            this.W = ep.s(var2.ticks(), this.W(), var9);
            if (this.b.G("Timed") && var3 != 0 && this.W > 0) {
               this.p = WindChargeHopModule.g.P;
               this.X = "Wind charge thrown; jump in " + this.W + " ticks";
            } else {
               if (this.b.G("Timed") && var3 != 0) {
                  var1.method_6043();
               }

               this.p = WindChargeHopModule.g.R;
               this.X = "Wind charge thrown";
            }

            double var10 = ep.T(this.J, this.C()[0], this.x());
            q8.G(
               "WindHop",
               "Thrown at yaw {} pitch {} ({} of the best push) after a turn of {}; jump {} ({} ticks from {}), round trip {} ticks",
               Y(var1.method_36454()),
               Y(var1.method_36455()),
               var10 > 0.0 ? String.format(Locale.ROOT, "%.0f%%", 100.0 * ep.T(var2, this.C()[0], this.x()) / var10) : "?",
               this.V.E(),
               var3 != 0 ? this.b.getValue() : "none",
               this.W,
               var9 > 0 ? "measured hops" : "ping",
               this.W()
            );
         }
      }
   }

   private void Y() {
      if (this.c - this.t >= this.W) {
         this.p = WindChargeHopModule.g.R;
         if (this.h.method_24828() && !this.R) {
            this.h.method_6043();
            this.X = "Jumped for the burst";
         } else {
            this.X = this.R ? "The push came before the jump" : "Left the ground before the jump";
         }
      }
   }

   private void W() {
      class_746 var1 = this.h;
      if (this.s < 0) {
         for (class_1297 var4 : mc.field_1687.method_18112()) {
            if (var4 instanceof class_8956 var2 && var2.method_24921() == var1 && !this.A.contains(var2.method_5628())) {
               this.s = (int)(this.c - this.t);
               break;
            }
         }
      }

      double var7 = var1.method_18798().field_1351;
      if (!this.R && var7 - this.S > 0.5) {
         this.R = (boolean)1;
         int var8 = (int)(this.c - this.t);
         d.put(this.h(), var8);
         q8.G("WindHop", "Push arrived {} ticks after the throw: vertical speed {} to {}", var8, Y(this.S), Y(var7));
      }

      this.S = var7;
      this.K = Math.max(this.K, var1.method_23318());
      if (--this.N <= 0) {
         String var9 = this.s >= 0
            ? "your wind charge appeared " + this.s + " ticks after the throw"
            : (
               this.R
                  ? "the burst pushed you, so the server spawned the throw"
                  : "no wind charge from you was seen and nothing pushed you: the server may not have spawned the throw (an anticheat or plugin may have cancelled it), or it burst between two ticks and missed you"
            );
         String var5 = !this.R && this.s >= 0 ? "; no push seen, so the burst missed you or this server changes wind charge knockback" : "";
         String var6 = uz.i();
         q8.G("WindHop", "Hop result: rose {} blocks; {}{}; server {}", Y(this.K - this.j), var9, var5, var6 == null ? "singleplayer" : var6);
         if (this.p == WindChargeHopModule.g.R) {
            this.X = String.format(Locale.ROOT, "Hopped %.1f blocks", this.K - this.j);
         }
      }
   }

   private String q() {
      if (!cr.f() || mc.field_1755 != null) {
         return "Paused: a screen is open";
      } else if (!mc.method_1569()) {
         return "Paused: the window lost focus";
      } else if (wp.o() != wp.z.g && !wp.h(wp.z.g)) {
         return "Paused: Freecam, Freelook or a pearl catch holds the view";
      } else if (this.h.method_5765()) {
         return "Not while riding";
      } else if (this.h.method_5799() || this.h.method_5777(class_3486.field_15517) || this.h.method_5771()) {
         return "Not in water or lava";
      } else {
         return this.h.method_6128() ? "Not while gliding" : null;
      }
   }

   private String A() {
      if (this.h.method_6115()) {
         return "Stop using your item first";
      } else if (AuraModule.d()) {
         return "Paused while Kill Aura lowers your shield";
      } else {
         int var1 = fk.T(class_1802.field_49098, true);
         if (var1 == -1) {
            return "No wind charges in your hotbar or hands";
         } else {
            class_1799 var2 = this.D(var1);
            if (!var2.method_45435(this.i.method_45162())) {
               return "Wind charges are disabled here";
            } else {
               float var3 = this.h.method_7357().method_7905(var2, 0.0F);
               if (var3 > 0.0F) {
                  class_10130 var4 = (class_10130)var2.method_58694(class_9334.field_53966);
                  return String.format(Locale.ROOT, "Wind charge cooling down (%.2f s)", var3 * (var4 == null ? 10 : var4.method_62860()) / 20.0);
               } else {
                  return var1 >= 0 && var1 != this.h.method_31548().method_67532() && !fs.o(this) ? "Hotbar busy" : null;
               }
            }
         }
      }
   }

   private class_1799 D(int var1) {
      return var1 == -2 ? this.h.method_6079() : this.h.method_31548().method_5438(var1);
   }

   private boolean Y() {
      int var1 = fk.T(class_1802.field_49098, true);
      return var1 != -1
         && !this.h.method_7357().method_7904(this.D(var1))
         && !AuraModule.d()
         && (var1 == -2 || var1 == this.h.method_31548().method_67532() || fs.o(this));
   }

   private boolean B() {
      int var1 = fk.T(class_1802.field_49098, true);
      if (var1 == -1) {
         return false;
      }

      class_1268 var2 = var1 == -2 ? class_1268.field_5810 : class_1268.field_5808;
      if (var1 >= 0 && var1 != this.h.method_31548().method_67532() && !this.q.m(this.h, var1)) {
         return false;
      }

      boolean var3 = mc.field_1761.method_2919(this.h, var2).method_23665();
      if (var3) {
         w6.H(this.h, var2);
         ((qf65e76ba6afee893)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void p() {
      if (this.q.I()) {
         int var1 = this.h == null ? -1 : this.h.method_31548().method_67532();
         this.q.o(getMc(), this.O.getValue());
         if (this.h != null && this.h == mc.field_1724 && mc.field_1761 != null && var1 != this.h.method_31548().method_67532()) {
            ((q226fb0b431f37244)mc.field_1761).larpSyncCarriedItem();
         }
      }
   }

   private float[] C() {
      return !wp.Y() ? new float[]{this.h.method_36454(), this.h.method_36455()} : wp.N(wp.g(), wp.J(), mc.field_1690.method_31044().method_31035());
   }

   private int W() {
      class_634 var1 = mc.method_1562();
      class_640 var2 = var1 != null && this.h != null ? var1.method_2871(this.h.method_5667()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.method_2959() / 50.0F));
   }

   private String h() {
      class_642 var1 = mc.method_1558();
      return var1 != null ? var1.field_3761 : (mc.method_1496() ? "singleplayer" : "this server");
   }

   private void h(String var1) {
      int var2 = this.p != WindChargeHopModule.g.R ? 1 : 0;
      cr.p(this);
      this.p();
      this.p = WindChargeHopModule.g.R;
      wp.g(wp.z.g, this.V, this.e.getValue(), this.y.getValue());
      if (var2 != 0) {
         this.X = var1;
         q8.G("WindHop", "Hop cancelled: {}", var1);
      }
   }

   private static String Y(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String o(class_243 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.field_1352, var0.field_1351, var0.field_1350);
   }

   @Override
   public void onEnable() {
      this.h = null;
      this.i = null;
      this.p = WindChargeHopModule.g.R;
      this.t = Long.MIN_VALUE;
      this.N = 0;
      this.X = this.v.getValue() < 0 ? "Set a Hop Bind" : "Ready";
      q8.G(
         "WindHop",
         "Enabled; direction={}, jump={}, swap back={}, turn speed={}, return speed={}, humanize={}",
         this.P.getValue(),
         this.b.getValue(),
         this.O.getValue(),
         this.C.getValue().intValue(),
         this.e.getValue().intValue(),
         this.y.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.h("Disabled");
      this.h = null;
      this.i = null;
      q8.G("WindHop", "Disabled; pending throw and view released");
   }

   private enum g {
      R,
      q,
      P;
   }
}
