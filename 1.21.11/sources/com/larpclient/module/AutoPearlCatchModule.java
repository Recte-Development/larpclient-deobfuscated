package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larp.obf.q4031f93fe72647cb.qf65e76ba6afee893;
import com.larpclient.cc;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.fs;
import com.larpclient.gn;
import com.larpclient.q8;
import com.larpclient.sf;
import com.larpclient.t9;
import com.larpclient.w6;
import com.larpclient.wp;
import com.larpclient.ym;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_10130;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1684;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_640;
import net.minecraft.class_746;
import net.minecraft.class_9334;

public final class AutoPearlCatchModule extends Module {
   private static final int R = 20;
   private static final int c = 20;
   private static final int V = 20;
   private static final int i = 3;
   private static final int x = 4;
   private static final double h = 0.6;
   private static final double l = 5.0;
   private static final float Q = 0.05F;
   public static final float g = 2.5F;
   private static AutoPearlCatchModule k;
   private final lw B = this.addSetting(
      new lw("Catch Bind", "Press for one pearl catch; pressing again while it throws cancels it. Separate from the module toggle", -1)
   );
   private final li A = this.addSetting(new li("Direction", "Up throws straight up for height; Look throws where you aim for distance", "Up", "Up", "Look"));
   private final li j = this.addSetting(
      new li(
         "Timing",
         "Instant throws both in one tick for a catch a few blocks out; Solved aims the wind charge a tick or more later for a farther catch; Fixed Delay throws it along the same look after Delay",
         "Instant",
         "Instant",
         "Solved",
         "Fixed Delay"
      )
   );
   private final lo q = this.addSetting(
      new lo("Delay", "Ticks from the pearl to the wind charge; same-look catches work near straight up", 2.0, 1.0, 10.0, 1.0)
   );
   private final lo e = this.addSetting(
      new lo(
         "Turn Speed", "Maximum player turn in degrees per tick; your camera stays on your look. 180 with Humanize off turns at once", 90.0, 10.0, 180.0, 5.0
      )
   );
   private final l3 G = this.addSetting(new l3("Swap Back", "Return to your previous hotbar slot after each throw", true));
   private final lo F = this.addSetting(
      new lo("Return Speed", "Most the player turns per tick back to your look after the last throw. 180 returns at once", 40.0, 10.0, 180.0, 5.0)
   );
   private final l3 J = this.addSetting(
      new l3(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final gn d = new gn();
   private final fs L = new fs(this);
   private final cc S = new cc();
   private final Set<Integer> p = new HashSet<>();
   private final List<AutoPearlCatchModule.m> z = new ArrayList<>();
   private class_746 m;
   private class_638 M;
   private long N;
   private long v = Long.MIN_VALUE;
   private long f = -1L;
   private float[] Y;
   private float E;
   private float t;
   private List<sf.c> C;
   private class_243 r;
   private class_243 Z = class_243.field_1353;
   private class_243 H;
   private class_243 O = class_243.field_1353;
   private class_243 X = class_243.field_1353;
   private class_243 o = class_243.field_1353;
   private class_243 w = class_243.field_1353;
   private float D;
   private float u;
   private class_1684 n;
   private sf.p s;
   private sf.p b;
   private String I = "Set a Catch Bind";

   public AutoPearlCatchModule() {
      super("Auto Pearl Catch", "Throw a pearl, then a wind charge it hits in flight, to teleport mid-air", Category.MOVEMENT, -1);
      k = this;
      this.q.O(this::w);
      this.A.a(var1 -> this.e("Direction changed"));
      this.j.a(var1 -> this.e("Timing changed"));
      this.B.a(var1 -> this.e("Bind changed"));
   }

   public static AutoPearlCatchModule M() {
      return k;
   }

   public List<lb<?>> t() {
      return List.of(this.B, this.A, this.j, this.q);
   }

   public List<lb<?>> x() {
      return List.of(this.e, this.F, this.J, this.G);
   }

   public gn k() {
      return this.d;
   }

   public double x() {
      return this.F.getValue();
   }

   public boolean X() {
      return this.J.getValue();
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
      return this.isEnabled() ? this.I : "";
   }

   public boolean l() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.m != null && this.m == mc.field_1724 && this.M == mc.field_1687 && (this.S.E() || this.N == this.v);
   }

   public static void v(int var0) {
      if (k != null && k.isEnabled()) {
         AutoPearlCatchModule var1 = k;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.B.getValue()) {
            if (!var1.S.u()) {
               var1.e("Cancelled with Catch Bind");
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.e("Waiting for a world");
         this.z.clear();
      } else {
         if (this.m != mc.field_1724 || this.M != mc.field_1687) {
            int var10000 = this.S.E();
            boolean var12 = false;
            byte var11 = (byte)var10000;
            var10000 = Integer.signum(var11);
            float[] var1 = (var10000 | -var10000) >>> 31 == 0 && this.N != this.v ? null : this.Y;
            this.e("World changed");
            this.z.clear();
            this.m = mc.field_1724;
            this.M = mc.field_1687;
            this.r = null;
            if (var1 != null) {
               this.m.method_36456(var1[0]);
               this.m.method_36457(var1[1]);
               this.m.field_5982 = var1[0];
               this.m.field_6004 = var1[1];
               q8.G("PearlCatch", "World changed during a catch; your look (yaw {} pitch {}) restored", V(var1[0]), V(var1[1]));
            }
         }

         this.N++;
         if (wp.o() == wp.z.V) {
            this.Y = wp.N(wp.g(), wp.J(), mc.field_1690.method_31044().method_31035());
         }

         class_243 var13 = this.m.method_73189();
         this.Z = this.r != null && !(var13.method_1025(this.r) > 16.0) ? var13.method_1020(this.r) : class_243.field_1353;
         this.r = var13;
         this.W();
         byte var2 = this.S.L();
         cc.u var3 = this.S.a();
         if (var3 == cc.u.x) {
            boolean var8 = false;
            byte var7 = var2;
            int var15 = Integer.signum(var7);
            if (((var15 | -var15) >>> 31 ^ 1) == 0) {
               int var16 = this.S.C();
               boolean var10 = false;
               byte var9 = (byte)var16;
               var16 = Integer.signum(var9);
               if ((var16 | -var16) >>> 31 == 0) {
                  this.E = this.t = 0.0F;
                  this.S.x(this.N);
               }
            }

            if (this.S.C()) {
               this.M();
            }
         } else {
            String var4 = this.d();
            if (var4 != null) {
               this.e(var4);
            } else if (this.S.y(this.N)) {
               String var10001;
               if (var3 == cc.u.E) {
                  var10001 = "The view did not reach the throw angle in time";
               } else {
                  byte var18 = this.w();
                  boolean var6 = false;
                  byte var5 = var18;
                  int var19 = Integer.signum(var5);
                  var10001 = ((var19 | -var19) >>> 31 ^ 1) == 0
                     ? "The wind charge could not be thrown on its tick"
                     : "No reliable catch before the pearl came down";
               }

               this.e(var10001);
            } else {
               if (var3 == cc.u.E) {
                  this.r();
               } else {
                  this.N();
               }
            }
         }
      }
   }

   private void M() {
      String var1 = this.d();
      if (var1 == null) {
         var1 = this.L();
      }

      if (var1 != null) {
         this.S.Z();
         this.C(var1);
      } else {
         int var2 = this.V();
         if (var2 > 0) {
            if (var2 <= 30 && !this.S.A(this.N)) {
               this.I = String.format(Locale.ROOT, "Catch in %.2f s (cooldown)", var2 / 20.0);
            } else {
               this.S.Z();
               this.C(String.format(Locale.ROOT, "Ender pearl or wind charge is cooling down (%.1f s left)", var2 / 20.0));
            }
         } else {
            this.S.Z();
            this.x();
         }
      }
   }

   private int V() {
      float var1 = this.j(class_1802.field_8634);
      float var2 = this.j(class_1802.field_49098);
      int var3 = Math.max(h(var1, this.E, class_1802.field_8634), h(var2, this.t, class_1802.field_49098));
      this.E = var1;
      this.t = var2;
      return var3;
   }

   private float j(class_1792 var1) {
      int var2 = fk.T(var1, true);
      if (var2 == -1) {
         return 0.0F;
      }

      class_1799 var3 = var2 == -2 ? this.m.method_6079() : this.m.method_31548().method_5438(var2);
      return this.m.method_7357().method_7905(var3, 0.0F);
   }

   private static int h(float var0, float var1, class_1792 var2) {
      if (var0 <= 0.0F) {
         return 0;
      }

      float var3 = var1 - var0;
      if (var3 > 0.0F) {
         return (int)Math.ceil(var0 / var3);
      }

      class_10130 var4 = (class_10130)new class_1799(var2).method_58694(class_9334.field_53966);
      return (int)Math.ceil(var0 * (var4 == null ? 20 : var4.method_62860()));
   }

   private void x() {
      String var1 = this.d();
      if (var1 == null) {
         var1 = this.L();
      }

      if (var1 != null) {
         this.C(var1);
      } else {
         float[] var2 = this.F();
         float var3 = var2[0];
         float var4 = var2[1];
         float var5 = this.J() ? -90.0F : var4;
         class_243 var6 = this.m.method_33571();
         sf.p var7;
         if (this.U()) {
            sf.j var8 = sf.S(var6, var3, var5, this.G(), this.i(), 0.6);
            if (var8 == null) {
               this.C(this.J() ? "No instant catch: clear the space above you" : "No instant catch along this look; clear the space ahead");
               return;
            }

            var5 = var8.pitch();
            var7 = var8.plan();
         } else {
            class_243 var11 = sf.x(var3, var5);
            var7 = this.w()
               ? sf.n(this.E(var6, var11, this.G(), this.q.getValue().intValue()), var6, this.G(), var11, this.i())
               : this.o(var6, var11, this.G());
            if (var7 == null || var7.reliability() < 0.6) {
               this.C(this.u(var7));
               return;
            }
         }

         this.D = var3;
         this.u = var5;
         this.s = var7;
         this.p.clear();

         for (class_1297 var9 : mc.field_1687.method_18112()) {
            if (var9 instanceof class_1684 var10 && var10.method_24921() == this.m) {
               this.p.add(var10.method_5628());
            }
         }

         this.S.k(this.N, 20);
         this.d.z();
         this.I = this.b(var3, var5) ? "Throwing" : "Turning to throw";
         q8.G(
            "PearlCatch",
            "Catch started; direction={}, timing={}, pitch {}{}, forecast catch on step {} at {} ({}/{} samples)",
            this.A.getValue(),
            this.j.getValue(),
            V(var5),
            !this.J() && var5 < var4 - 0.5F ? " raised from " + V(var4) : "",
            var7.nominal().tick(),
            s(var7.nominal().point()),
            var7.score(),
            var7.samples()
         );
         this.r();
      }
   }

   private sf.p o(class_243 var1, class_243 var2, class_243 var3) {
      sf.p var4 = null;

      for (int var5 = 1; var5 <= 20; var5++) {
         sf.p var6 = sf.p(this.E(var1, var2, var3, var5), var1, var3, this.i());
         if (var6 != null && var6.reliability() >= 0.6) {
            return var6;
         }

         if (var6 != null && (var4 == null || var6.score() > var4.score())) {
            var4 = var6;
         }
      }

      return var4;
   }

   private List<sf.c> E(class_243 var1, class_243 var2, class_243 var3, int var4) {
      return sf.T(var1, var2, var3, var4, this.i());
   }

   private String u(sf.p var1) {
      if (this.w() && !this.J()) {
         return "Fixed Delay only catches near straight up; use Instant or Solved for Look";
      } else if (var1 == null) {
         return this.J() ? "No catch: the pearl would come down first, so clear the space above you" : "No catch at this angle; aim higher or use Instant";
      } else {
         return String.format(
            Locale.ROOT,
            "Only %d of %d samples catch here; %s",
            var1.score(),
            var1.samples(),
            this.w() ? "try another Delay or Instant" : (this.J() ? "clear the space above you" : "aim higher or use Instant")
         );
      }
   }

   private void r() {
      if (cr.a(this)) {
         this.g(this.D, this.u);
      }

      cr.y(this, this.U() ? this::f : this::n, this::F);
   }

   private boolean F() {
      if (!this.isRuntimeActive()
         || this.S.a() != cc.u.E
         || this.d() != null
         || (this.U() ? !this.w(this.D, this.u, 2.5F) : !this.b(this.D, this.u))
         || !this.u(class_1802.field_8634)
         || !this.u(class_1802.field_49098)
         || !this.C(class_1802.field_8634)
         || AuraModule.d()) {
         return false;
      } else if (!this.U()) {
         return true;
      } else if (!this.C(class_1802.field_49098)) {
         return false;
      } else {
         class_243 var1 = this.m.method_33571();
         class_243 var2 = sf.x(this.m.method_36454(), this.m.method_36455());
         sf.p var3 = sf.n(sf.I(var1, var2, this.G(), this.i()), var1, this.G(), var2, this.i());
         if (var3 != null && !(var3.reliability() < 0.6)) {
            this.b = var3;
            return true;
         } else {
            return false;
         }
      }
   }

   private void f() {
      sf.p var1 = this.b;
      this.O = this.m.method_73189();
      byte var2 = 0;
      byte var3 = 0;

      try {
         var7 = this.L(class_1802.field_8634);
         if (var7) {
            var3 = this.L(class_1802.field_49098);
         }
      } finally {
         this.h();
      }

      if (!var7) {
         this.e("The pearl throw was rejected");
      } else if (var3 == 0) {
         this.e("The wind charge was rejected after the pearl, so the pearl flies on");
      } else {
         this.s = var1;
         this.v = this.N;
         this.S.L(this.N, 1);
         this.S.m();
         this.H(var1);
         this.I = "Pearl and wind charge thrown together";
         q8.G(
            "PearlCatch",
            "Instant pair thrown at yaw {} pitch {} from {} after a turn of {}; expected catch on step {} at {} ({}/{} samples)",
            V(this.m.method_36454()),
            V(this.m.method_36455()),
            s(this.m.method_33571()),
            this.d.E(),
            var1 == null ? "?" : var1.nominal().tick(),
            var1 == null ? "?" : s(var1.nominal().point()),
            var1 == null ? 0 : var1.score(),
            var1 == null ? 0 : var1.samples()
         );
      }
   }

   private void n() {
      this.O = this.m.method_73189();
      class_243 var1 = this.m.method_33571();
      class_243 var2 = this.G();
      if (!this.X(class_1802.field_8634)) {
         this.e("The pearl throw was rejected");
      } else {
         this.D = this.m.method_36454();
         this.u = this.m.method_36455();
         this.X = var1;
         this.o = sf.x(this.D, this.u);
         this.w = var2;
         this.S.L(this.N, this.w() ? this.q.getValue().intValue() + 1 : 21);
         this.I = this.w() ? "Pearl thrown; wind charge in " + this.q.getValue().intValue() + " ticks" : "Pearl thrown; aiming the wind charge";
         q8.G("PearlCatch", "Pearl thrown at yaw {} pitch {} from {} after a turn of {}", V(this.D), V(this.u), s(var1), this.d.E());
         this.d.z();
      }
   }

   private void N() {
      this.m();
      if (this.n != null && this.n.method_31481()) {
         this.e("The pearl came down before the wind charge");
      } else if (this.w()) {
         if (cr.a(this)) {
            this.g(this.D, this.u);
         }

         if (this.S.z(this.N) >= this.q.getValue().intValue()) {
            cr.y(this, this::w, this::Q);
         }
      } else {
         List var1 = this.V();
         if (this.H == null) {
            this.H = ((sf.c)var1.get(0)).N();
         }

         sf.p var2 = sf.p(var1, this.m.method_33571(), this.G(), this.i());
         if (var2 != null && !(var2.reliability() < 0.6)) {
            this.s = var2;
            float[] var3 = t9.X(class_243.field_1353, var2.direction());
            this.D = var3[0];
            this.u = class_3532.method_15363(var3[1], -90.0F, 90.0F);
            if (cr.a(this)) {
               this.g(this.D, this.u);
            }

            cr.y(this, this::w, this::Q);
            this.I = "Aiming wind charge";
         } else {
            this.I = "Waiting for a reliable catch line";
         }
      }
   }

   private boolean Q() {
      if (this.isRuntimeActive()
         && this.S.a() == cc.u.v
         && this.d() == null
         && this.u(class_1802.field_49098)
         && this.C(class_1802.field_49098)
         && !AuraModule.d()) {
         class_243 var1 = sf.x(this.m.method_36454(), this.m.method_36455());
         sf.p var2 = sf.n(this.V(), this.m.method_33571(), this.G(), var1, this.i());
         if (this.w()) {
            this.b = var2;
            return this.S.z(this.N) == this.q.getValue().intValue() && this.b(this.D, this.u);
         } else if (var2 != null && !(var2.reliability() < 0.6)) {
            this.b = var2;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void w() {
      sf.p var1 = this.b;
      long var2 = this.S.z(this.N);
      if (!this.X(class_1802.field_49098)) {
         this.e("The wind charge throw was rejected");
      } else {
         this.s = var1;
         this.v = this.N;
         this.S.m();
         this.H(var1);
         this.I = "Wind charge thrown";
         if (var1 == null) {
            q8.G("PearlCatch", "Wind charge thrown {} ticks after the pearl; no catch expected along this look", var2);
         } else {
            q8.G(
               "PearlCatch",
               "Wind charge thrown {} ticks after the pearl at yaw {} pitch {}; expected catch on step {} at {} ({}/{} samples){}",
               var2,
               V(this.m.method_36454()),
               V(this.m.method_36455()),
               var1.nominal().tick(),
               s(var1.nominal().point()),
               var1.score(),
               var1.samples(),
               var1.nominal().burst() ? ", charge bursts on you" : ""
            );
         }
      }
   }

   private void H(sf.p var1) {
      if (this.z.size() >= 4) {
         this.z.remove(0);
      }

      int var2 = (var1 == null ? 60 : var1.nominal().tick()) + this.A() + 20;
      AutoPearlCatchModule.m var3 = new AutoPearlCatchModule.m(new HashSet<>(this.p), this.O, var1, this.H, this.N, this.N + var2);
      var3.N = this.n;
      this.z.add(var3);
      this.n = null;
      this.p.clear();
      this.H = null;
   }

   private void W() {
      Iterator var1 = this.z.iterator();

      while (var1.hasNext()) {
         AutoPearlCatchModule.m var2 = (AutoPearlCatchModule.m)var1.next();
         if (var2.N == null) {
            var2.N = this.v(var2.K);
         }

         class_1684 var3 = var2.N;
         if (var2.u < 0L && var3 != null && (var3.method_31481() || mc.field_1687.method_8469(var3.method_5628()) != var3)) {
            var2.u = this.N;
         }

         if (var2.c == null && !var2.t && var2.u < 0L && var3 != null) {
            var2.c = sf.H(new sf.y(var3.method_73189(), var3.method_18798(), 1 + var3.field_6012), this.i()).N();
            var2.t = (boolean)1;
         }

         if (var2.u >= 0L && this.N > var2.u) {
            this.h(var2);
            var1.remove();
         } else if (this.N - var2.V >= 0L) {
            String var4 = var3 == null ? "Pearl not seen; the outcome is unknown" : "No catch observed in time";
            q8.G("PearlCatch", "{}, {} ticks after the wind charge", var4, this.N - var2.p);
            if (!this.S.E()) {
               this.I = var4;
            }

            var1.remove();
         }
      }
   }

   private class_1684 v(Set<Integer> var1) {
      for (class_1297 var3 : mc.field_1687.method_18112()) {
         if (var3 instanceof class_1684 var4
            && !var4.method_31481()
            && var4.method_24921() == this.m
            && !var1.contains(var4.method_5628())
            && this.z.stream().noneMatch(var1x -> var1x.N == var4)) {
            return var4;
         }
      }

      return null;
   }

   private void h(AutoPearlCatchModule.m var1) {
      class_243 var2 = this.m.method_73189();
      class_243 var3 = var1.q == null ? null : var1.q.nominal().point();
      double var4 = var3 == null ? Double.NaN : var2.method_1022(var3);
      int var6 = var2.method_1022(var1.S) > 1.0 ? 1 : 0;
      String var7;
      if (var1.c != null && var2.method_1022(var1.c) <= 1.0) {
         var7 = "Missed: the pearl landed";
      } else if (var6 == 0 || var3 != null && !(var4 <= 3.0)) {
         if (var6 != 0) {
            var7 = String.format(
               Locale.ROOT,
               "Caught %.1f blocks out, %.1f up, %.1f blocks from the predicted point (a tick of server timing)",
               var2.method_1022(var1.S),
               var2.field_1351 - var1.S.field_1351,
               var4
            );
         } else {
            var7 = "No teleport: the pearl ended near where you threw it";
         }
      } else {
         var7 = String.format(Locale.ROOT, "Caught %.1f blocks out, %.1f up", var2.method_1022(var1.S), var2.field_1351 - var1.S.field_1351);
      }

      q8.G(
         "PearlCatch",
         "{}; now at {}, expected {} ({} blocks off), {} ticks after the wind charge",
         var7,
         s(var2),
         var3 == null ? "unknown" : s(var3),
         Double.isNaN(var4) ? "?" : V(var4),
         this.N - var1.p
      );
      if (!this.S.E()) {
         this.I = var7;
      }
   }

   private List<sf.c> V() {
      if (this.f != this.N || this.C == null) {
         this.C = this.E(this.X, this.o, this.w, (int)Math.max(1L, this.S.z(this.N)));
         this.f = this.N;
      }

      return this.C;
   }

   private void m() {
      if (this.n == null) {
         for (class_1297 var2 : mc.field_1687.method_18112()) {
            if (var2 instanceof class_1684 var3 && !var3.method_31481() && var3.method_24921() == this.m && !this.p.contains(var3.method_5628())) {
               this.n = var3;
               q8.v("PearlCatch", "Own pearl seen {} ticks after the throw, {} client ticks old", this.S.z(this.N), var3.field_6012);
               return;
            }
         }
      }
   }

   private String d() {
      if (!cr.f() || mc.field_1755 != null) {
         return "Paused: a screen is open";
      } else if (!mc.method_1569()) {
         return "Paused: the window lost focus";
      } else if (wp.o() != wp.z.V && !wp.h(wp.z.V)) {
         return "Paused: Freecam or Freelook holds the view";
      } else if (this.m.method_5765()) {
         return "Not while riding";
      } else {
         return !this.m.method_5777(class_3486.field_15517) && !this.m.method_5771() ? null : "Not with your head in water or lava";
      }
   }

   private String L() {
      if (this.m.method_6115()) {
         return "Stop using your item first";
      } else if (AuraModule.d()) {
         return "Paused while Kill Aura lowers your shield";
      } else if (fk.T(class_1802.field_8634, true) == -1) {
         return "No ender pearls in your hotbar or hands";
      } else if (fk.T(class_1802.field_49098, true) == -1) {
         return "No wind charges in your hotbar or hands";
      } else {
         return !this.m.method_31549().field_7480 && this.m.method_6032() + this.m.method_6067() <= 5.0 ? "The 5 HP pearl hit would be lethal" : null;
      }
   }

   private boolean u(class_1792 var1) {
      int var2 = fk.T(var1, true);
      if (var2 == -1) {
         return false;
      }

      class_1799 var3 = var2 == -2 ? this.m.method_6079() : this.m.method_31548().method_5438(var2);
      return var3.method_45435(this.M.method_45162()) && !this.m.method_7357().method_7904(var3);
   }

   private boolean C(class_1792 var1) {
      int var2 = fk.T(var1, true);
      return var2 == -2 || var2 == this.m.method_31548().method_67532() || var2 >= 0 && fs.o(this);
   }

   private boolean X(class_1792 var1) {
      try {
         return this.L(var1);
      } finally {
         this.h();
      }
   }

   private boolean L(class_1792 var1) {
      int var2 = fk.T(var1, true);
      if (var2 == -1) {
         return false;
      }

      class_1268 var3 = var2 == -2 ? class_1268.field_5810 : class_1268.field_5808;
      if (var2 >= 0 && var2 != this.m.method_31548().method_67532() && !this.L.m(this.m, var2)) {
         return false;
      }

      boolean var4 = mc.field_1761.method_2919(this.m, var3).method_23665();
      if (var4) {
         w6.H(this.m, var3);
         ((qf65e76ba6afee893)mc).larpUseDelay(4);
      }

      return var4;
   }

   private void h() {
      if (this.L.I()) {
         int var1 = this.m == null ? -1 : this.m.method_31548().method_67532();
         this.L.o(getMc(), this.G.getValue());
         if (this.m != null && this.m == mc.field_1724 && mc.field_1761 != null && var1 != this.m.method_31548().method_67532()) {
            ((q226fb0b431f37244)mc.field_1761).larpSyncCarriedItem();
         }
      }
   }

   private float[] F() {
      return !wp.Y() ? new float[]{this.m.method_36454(), this.m.method_36455()} : wp.N(wp.g(), wp.J(), mc.field_1690.method_31044().method_31035());
   }

   private void g(float var1, float var2) {
      wp.S(wp.z.V);
      if (wp.o() == wp.z.V) {
         float[] var3 = this.d.j(this.m.method_36454(), this.m.method_36455(), var1, var2, this.e.getValue(), this.J.getValue());
         t9.O(this, t9.v.p, var3[0], var3[1], 1.0);
      }
   }

   private boolean b(float var1, float var2) {
      return this.w(var1, var2, gn.l(this.J.getValue(), 0.05F, a_.i()));
   }

   private boolean w(float var1, float var2, float var3) {
      return Math.abs(class_3532.method_15393(this.m.method_36454() - var1)) <= var3 && Math.abs(this.m.method_36455() - var2) <= var3;
   }

   private class_243 G() {
      return this.m.method_24828() ? new class_243(this.Z.field_1352, 0.0, this.Z.field_1350) : this.Z;
   }

   private int A() {
      class_634 var1 = mc.method_1562();
      class_640 var2 = var1 != null && this.m != null ? var1.method_2871(this.m.method_5667()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.method_2959() / 50.0F));
   }

   private sf.k i() {
      return new ym(this.M, this.m);
   }

   private boolean J() {
      return this.A.G("Up");
   }

   private boolean w() {
      return this.j.G("Fixed Delay");
   }

   private boolean U() {
      return this.j.G("Instant");
   }

   private static String V(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String s(class_243 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.field_1352, var0.field_1351, var0.field_1350);
   }

   private void C(String var1) {
      this.I = var1;
      q8.G("PearlCatch", "Catch refused: {}", var1);
   }

   private void e(String var1) {
      boolean var2 = this.S.E();
      cr.p(this);
      this.h();
      this.H();
      if (var2) {
         this.I = var1;
         q8.G("PearlCatch", "Catch cancelled: {}", var1);
      }
   }

   private void H() {
      this.S.v();
      wp.g(wp.z.V, this.d, this.F.getValue(), this.J.getValue());
      this.p.clear();
      this.n = null;
      this.s = this.b = null;
      this.C = null;
      this.f = -1L;
      this.H = null;
   }

   @Override
   public void onEnable() {
      this.m = null;
      this.M = null;
      this.r = null;
      this.z.clear();
      this.v = Long.MIN_VALUE;
      this.H();
      this.I = this.B.getValue() < 0 ? "Set a Catch Bind" : "Ready";
      q8.G(
         "PearlCatch",
         "Enabled; direction={}, timing={}, delay={}, turn speed={}, return speed={}, humanize={}",
         this.A.getValue(),
         this.j.getValue(),
         this.q.getValue().intValue(),
         this.e.getValue().intValue(),
         this.F.getValue().intValue(),
         this.J.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.e("Disabled");
      this.z.clear();
      this.m = null;
      this.M = null;
      q8.G("PearlCatch", "Disabled; pending throws and aim released");
   }

   private static final class m {
      final Set<Integer> K;
      final class_243 S;
      final sf.p q;
      final long p;
      final long V;
      class_1684 N;
      class_243 c;
      boolean t;
      long u = -1L;

      m(Set<Integer> var1, class_243 var2, sf.p var3, class_243 var4, long var5, long var7) {
         this.K = var1;
         this.S = var2;
         this.q = var3;
         this.c = var4;
         this.p = var5;
         this.V = var7;
      }
   }
}
