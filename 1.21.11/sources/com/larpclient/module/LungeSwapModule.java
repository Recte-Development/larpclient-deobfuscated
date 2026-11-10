package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q4aba1ac527aae1c6;
import com.larpclient.LarpClient;
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.fs;
import com.larpclient.ga;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.th;
import com.larpclient.uz;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_12125;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1320;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_243;
import net.minecraft.class_5134;
import net.minecraft.class_638;
import net.minecraft.class_642;
import net.minecraft.class_746;
import net.minecraft.class_7924;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;

public final class LungeSwapModule extends Module {
   private static LungeSwapModule I;
   public static final int A = 10;
   public static final int R = 12;
   public static final double t = 0.4;
   private final li o = this.addSetting(
      new li("Mode", "Legit waits for full charges like vanilla; Blatant jabs as soon as the server accepts it", "Legit", "Legit", "Blatant")
   );
   private final lw v = this.addSetting(new lw("Activation Key", "Press to swap to your Lunge spear and jab; separate from the module toggle", 86));
   private final l3 j = this.addSetting(new l3("Swap Back", "Return after the jab; Blatant always returns", true));
   private final l3 F = this.addSetting(new l3("Swap Back To Slot", "Return to a fixed hotbar slot instead of your previous one", false));
   private final lo a = this.addSetting(new lo("Swap Back Slot", "Hotbar slot to return to", 1.0, 1.0, 9.0, 1.0));
   private final l3 k = this.addSetting(new l3("Hold To Lunge", "Keep jabbing while the activation key is held", false));
   private final l3 f = this.addSetting(
      new l3(
         "Charge After Swap", "On a server that resets charge on a swap, hold the spear until its own charge is ready. Off always jabs in the swap tick", true
      )
   );
   private final li x = this.addSetting(
      new li(
         "Swap Rule",
         "How the server treats charge across a swap. Detect jabs in the swap tick and waits for the spear's charge on a server where that did not lunge",
         "Detect",
         "Detect",
         "Kept",
         "Reset"
      )
   );
   private final lo q = this.addSetting(
      new lo(
         "Jab Delay",
         "Fewest ticks from your press to the swap and jab; your held item keeps charging meanwhile. 0 jabs in the press tick",
         0.0,
         0.0,
         10.0,
         1.0
      )
   );
   private final lo Y = this.addSetting(new lo("Jab Delay Max", "Most ticks from your press to the swap and jab", 0.0, 0.0, 10.0, 1.0));
   private final lo X = this.addSetting(
      new lo("Swap Back Delay", "Fewest ticks after the jab before you return to your slot. 1 is the next tick", 1.0, 1.0, 20.0, 1.0)
   );
   private final lo J = this.addSetting(new lo("Swap Back Delay Max", "Most ticks after the jab before you return to your slot", 1.0, 1.0, 20.0, 1.0));
   private final lo Z = this.addSetting(new lo("Hold Interval", "Fewest extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private final lo c = this.addSetting(new lo("Hold Interval Max", "Most extra ticks between lunges while Hold To Lunge is held", 0.0, 0.0, 40.0, 1.0));
   private static final Map<String, Boolean> p = new HashMap<>();
   private LungeSwapModule.h P = LungeSwapModule.h.e;
   private boolean N;
   private int l;
   private int r;
   private int M;
   private int y = -1;
   private int D = -1;
   private int b;
   private int O;
   private int m;
   private int K;
   private int i;
   private th.j n = th.j.A;
   private String u = "";
   private String Q = "";
   private float C;
   private class_746 V;
   private class_638 e;
   private String s = "Ready";
   private String L = "";
   private int g;
   private int T;
   private double d;
   private boolean S;
   private boolean E;
   private class_243 z = class_243.field_1353;

   public LungeSwapModule() {
      super("Lunge Swap", "Swap to your Lunge spear and jab with an activation key", Category.COMBAT, -1);
      I = this;
      this.j.O(this::A);
      this.F.O(this::t);
      this.a.O(() -> this.t() && this.F.getValue());
      this.x.O(this.f::getValue);
      this.q.m(this.Y).M("Jab Delay");
      this.X.m(this.J).M("Swap Back Delay");
      this.Z.m(this.c).M("Hold Interval");
      this.X.O(this::t);
      this.J.O(this::t);
      this.Z.O(this.k::getValue);
      this.c.O(this.k::getValue);
      this.o.a(var1 -> this.M(true));
      this.v.a(var1 -> {
         this.M(true);
         this.g = 0;
      });
      this.x.a(var1 -> this.M(true));
   }

   private boolean A() {
      return this.o.G("Legit");
   }

   private boolean f() {
      return this.o.G("Blatant");
   }

   private boolean t() {
      return this.f() || this.j.getValue();
   }

   public static int v(boolean var0) {
      return th.S(var0);
   }

   public static int F(double var0, double var2, double var4) {
      return ga.x((int)var0, (int)var2, var4);
   }

   private int A(lo var1, lo var2) {
      return F(var1.getValue(), var2.getValue(), ThreadLocalRandom.current().nextDouble());
   }

   public static int J(boolean var0, boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 ? var2 : var3;
      return var0 && var4 == var5 && var6 >= 0 && var6 < 9 && var6 != var4 ? var6 : -1;
   }

   public static th.j A(boolean var0, String var1, Boolean var2, boolean var3) {
      if (!var0 || "Kept".equals(var1)) {
         return th.j.A;
      } else if ("Reset".equals(var1)) {
         return th.j.E;
      } else if (var2 != null) {
         return var2 ? th.j.E : th.j.A;
      } else {
         return var3 ? th.j.E : th.j.A;
      }
   }

   public static void T(int var0) {
      if (I != null && I.isEnabled()) {
         LungeSwapModule var1 = I;
         if (var1 != null && var1.isRuntimeActive() && s5.A(var0) && var0 == var1.v.getValue()) {
            var1.N = (boolean)1;
         }
      }
   }

   public boolean k() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.P != LungeSwapModule.h.e && this.V != null && this.V == getMc().field_1724;
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
      if (!this.isEnabled()) {
         return "";
      } else {
         return this.P == LungeSwapModule.h.e && !this.L.isEmpty() ? s5.S(this.v.getValue()) + " also triggers " + this.L : this.s;
      }
   }

   @Override
   public void onEnable() {
      this.o();
      this.V = mc.field_1724;
      this.e = mc.field_1687;
      this.s = "Ready";
      this.L = "";
      this.g = 0;
      q8.G(
         "LungeSwap",
         "Enabled; mode={}, activation key={}, hold={}, charge after swap={}, swap rule={}, jab delay {}-{}, swap back delay {}-{}, hold interval {}-{} ticks",
         this.o.getValue(),
         this.v.getValue(),
         this.k.getValue(),
         this.f.getValue(),
         this.x.getValue(),
         this.q.getValue().intValue(),
         this.Y.getValue().intValue(),
         this.X.getValue().intValue(),
         this.J.getValue().intValue(),
         this.Z.getValue().intValue(),
         this.c.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.M(true);
      this.V = null;
      this.e = null;
   }

   private boolean o() {
      return this.isInGame()
         && this.V == mc.field_1724
         && this.e == mc.field_1687
         && mc.field_1724.method_5805()
         && !mc.field_1724.method_7325()
         && mc.field_1755 == null
         && (bn.q(mc) || wp.o() && bn.x(mc, wp.z.u));
   }

   @Override
   public void onTick() {
      boolean var1 = this.N;
      this.N = (boolean)0;
      if (--this.g <= 0) {
         this.g = 20;
         this.q();
      }

      if (this.V != mc.field_1724 || this.e != mc.field_1687) {
         this.M(false);
         this.V = mc.field_1724;
         this.e = mc.field_1687;
         this.T = 0;
      }

      if (this.T > 0) {
         this.x();
      }

      if (!this.o()) {
         this.M(true);
         this.s = "Paused";
      } else {
         boolean var2 = s5.m(mc.method_22683().method_4490(), this.v.getValue());
         boolean var6 = false;
         int var5 = this.M;
         if ((-Integer.signum(var5) >>> 31 ^ 1) == 0 && (!var2 || var1)) {
            this.M = 0;
         }

         label60:
         if (this.P == LungeSwapModule.h.e) {
            if (!var1) {
               if (!this.k.getValue() || !var2) {
                  break label60;
               }

               int var10000 = this.M;
               byte var8 = 1;
               int var7 = this.M;
               this.M = var7 - var8;
               boolean var10 = false;
               int var9 = var10000;
               if (-Integer.signum(var9) >>> 31 != 0) {
                  break label60;
               }
            }

            this.K(var1);
         }

         if (this.P == LungeSwapModule.h.a) {
            this.T();
         } else if (this.P == LungeSwapModule.h.B) {
            this.K();
         } else {
            if (this.P == LungeSwapModule.h.X) {
               int var11 = --this.r;
               boolean var4 = false;
               int var3 = var11;
               if (-Integer.signum(var3) >>> 31 == 0) {
                  this.R();
                  if (this.k.getValue() && var2) {
                     this.M = this.A(this.Z, this.c);
                  }

                  this.s = "Ready";
                  return;
               }
            }

            if (this.P == LungeSwapModule.h.e && !s5.A(this.v.getValue())) {
               this.s = "Set an Activation Key";
            }
         }
      }
   }

   public static String j(Module var0, int var1, Iterable<Module> var2) {
      if (!s5.A(var1)) {
         return "";
      }

      if (var0.getKeyBind() == var1) {
         return "the Lunge Swap toggle";
      }

      for (Module var4 : var2) {
         if (var4 != var0) {
            if (var4.getKeyBind() == var1) {
               return var4.getName();
            }

            for (lb var6 : var4.getSettings()) {
               if (var6 instanceof lw var7 && var7.getValue() == var1) {
                  return var4.getName();
               }
            }
         }
      }

      return "";
   }

   private void q() {
      LarpClient var1 = LarpClient.b();
      int var2 = this.v.getValue();
      String var3 = var1 != null && var1.G() != null ? j(this, var2, var1.G().M()) : "";
      if (!var3.isEmpty() && !var3.equals(this.L)) {
         q8.L(
            "LungeSwap",
            "Activation Key {} is also bound to {}; one press triggers both, and a movement module such as Fly cancels the lunge. Rebind one of them",
            s5.S(var2),
            var3
         );
      }

      this.L = var3;
   }

   private void x() {
      class_746 var1 = mc.field_1724;
      this.d = Math.max(this.d, var1.method_18798().method_37267());
      if (--this.T <= 0) {
         int var2 = this.d >= 0.4 ? 1 : 0;
         q8.G(
            "LungeSwap",
            "Jab result: peak horizontal speed {} blocks/tick, moved {} blocks in {} ticks ({})",
            String.format(Locale.ROOT, "%.3f", this.d),
            String.format(Locale.ROOT, "%.2f", var1.method_73189().method_1020(this.z).method_37267()),
            12,
            var2 != 0 ? "lunge push arrived" : "no lunge push; the server refused it or another movement owner replaced it"
         );
         if (this.S && this.f.getValue() && this.x.G("Detect")) {
            String var3 = this.S();
            Boolean var4 = p.get(var3);
            p.put(var3, var2 == 0);
            if (var2 == 0 && !Boolean.TRUE.equals(var4)) {
               q8.G("LungeSwap", "A swap-tick jab did not lunge on {}; later presses there wait for the spear's own charge", var3);
            } else if (var2 != 0 && var4 == null) {
               q8.G("LungeSwap", "{} keeps the charge across a swap; presses there jab in the swap tick", var3);
            }
         }
      }
   }

   private String S() {
      class_642 var1 = mc.method_1558();
      return var1 != null ? var1.field_3761 : (mc.method_1496() ? "singleplayer" : "this server");
   }

   private void K(boolean var1) {
      String var2 = this.T();
      if (var2 != null) {
         this.s = var2;
         if (var1) {
            q8.G("LungeSwap", "Press refused: {}", var2);
         }
      } else if (this.I() == null) {
         this.s = "No Lunge spear in hotbar";
         if (var1) {
            q8.G("LungeSwap", "Press ignored: no Lunge spear in the hotbar");
         }
      } else {
         this.y = mc.field_1724.method_31548().method_67532();
         this.u = mc.field_1724.method_6047().method_7964().getString();
         this.C = mc.field_1724.method_7261(0.0F);
         this.n = A(this.f.getValue(), this.x.getValue(), p.get(this.S()), uz.l());
         this.b = this.O = 0;
         this.m = -1;
         this.Q = "";
         this.l = this.A(this.q, this.Y);
         this.P = LungeSwapModule.h.a;
      }
   }

   private String T() {
      class_746 var1 = mc.field_1724;
      return th.P(var1.method_7344().method_7586(), var1.method_31549().field_7477, var1.method_5799(), var1.method_5765(), var1.method_6128());
   }

   private th.o I() {
      class_746 var1 = mc.field_1724;
      class_1799 var2 = var1.method_6047();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.h(var4);
         if (var5 > 0) {
            class_1799 var6 = var1.method_31548().method_5438(var4);
            if (var6.method_45435(this.e.method_45162())) {
               var3.add(
                  new th.b(
                     var4,
                     X(var1, var6),
                     (Float)var6.method_58695(class_9334.field_63635, 0.0F),
                     var5,
                     var6.method_7963() ? var6.method_7936() - var6.method_7919() : Integer.MAX_VALUE,
                     var6.method_63692(),
                     class_1799.method_7984(var6, var2)
                  )
               );
            }
         }
      }

      return th.T(var3, var1.method_31548().method_67532(), this.n, ((q4aba1ac527aae1c6)var1).larpAttackStrengthTicker(), var1.method_7279(), this.f());
   }

   private static float X(class_746 var0, class_1799 var1) {
      class_1324 var2 = var0.method_5996(class_5134.field_23723);
      if (var2 == null) {
         return var0.method_7279();
      }

      ArrayList var3 = new ArrayList();

      for (class_1322 var5 : var2.method_6195()) {
         if (!var5.comp_2447().equals(class_1792.field_8001)) {
            var3.add(var5);
         }
      }

      var1.method_57354(class_1304.field_6173, (var1x, var2x) -> {
         if (var1x.equals(class_5134.field_23723)) {
            var3.add(var2x);
         }
      });
      double var10 = var2.method_6201();

      for (class_1322 var9 : var3) {
         if (var9.comp_2450() == class_1323.field_6328) {
            var10 += var9.comp_2449();
         }
      }

      double var6 = var10;

      for (class_1322 var13 : var3) {
         if (var13.comp_2450() == class_1323.field_6330) {
            var6 += var10 * var13.comp_2449();
         }
      }

      for (class_1322 var14 : var3) {
         if (var14.comp_2450() == class_1323.field_6331) {
            var6 *= 1.0 + var14.comp_2449();
         }
      }

      return (float)(1.0 / ((class_1320)var2.method_6198().comp_349()).method_6165(var6) * 20.0);
   }

   private void T() {
      th.o var1 = this.I();
      if (var1 == null) {
         this.o();
         this.s = "No Lunge spear in hotbar";
      } else {
         if (this.m < 0) {
            this.m = var1.ticks();
            q8.G(
               "LungeSwap",
               "Press: {} rule, slot {} (Lunge {}) from slot {} ({}, charge {}); {}",
               this.n,
               var1.slot() + 1,
               var1.lunge(),
               this.y + 1,
               this.u,
               j(this.C),
               var1.ticks() == 0 ? (var1.swap() ? "swap and jab now" : "jab now") : "server needs " + var1.ticks() + " more ticks"
            );
         }

         this.D = var1.slot();
         if (this.l > 0) {
            this.l--;
            this.s = "Lunge in " + th.L(Math.max(this.l + 1, var1.ticks()));
            this.B("Jab Delay");
         } else if (var1.ticks() > 0 && !var1.resets()) {
            this.b++;
            this.s = "Lunge in " + th.L(var1.ticks());
            this.B("charge");
            if (this.b > this.m + 10 + 2) {
               this.h("the charge never passed");
            }
         } else if (!this.w()) {
            String var2 = this.X();
            if (var2 != null) {
               this.B(var2);
               this.s = "Waiting for " + var2;
               if (++this.O > 10) {
                  this.h(var2);
               }
            } else {
               this.s = "Lunging";
               cr.y(this, this::N, () -> this.X() == null && this.P == LungeSwapModule.h.a);
            }
         }
      }
   }

   private boolean w() {
      if (!wp.o()) {
         return false;
      }

      wp.B("a Lunge Swap jab");
      this.B("your look");
      this.s = "Lunging";
      return true;
   }

   private String X() {
      if (!this.isRuntimeActive() || !this.o()) {
         return "the game";
      } else if (!fs.o(this)) {
         return "the hotbar";
      } else if (!bn.R(mc, mc.field_1724)) {
         return "free hands";
      } else if (this.A() && bn.v(mc) > 0) {
         return "the miss delay";
      } else if (this.T() != null) {
         return "the Lunge conditions";
      } else {
         return this.D >= 0 && this.A(this.D) ? null : "the spear";
      }
   }

   private void B(String var1) {
      if (!var1.equals(this.Q)) {
         this.Q = var1;
         q8.G("LungeSwap", "Jab held by {}", var1);
      }
   }

   private void h(String var1) {
      q8.G("LungeSwap", "Press lapsed after {} ticks, held by {}", this.b + this.O, var1);
      this.M(true);
      this.s = "Lunge blocked by " + var1;
   }

   private void N() {
      th.o var1 = this.I();
      if (this.P == LungeSwapModule.h.a && var1 != null && (var1.ticks() <= 0 || var1.resets()) && this.X() == null) {
         class_746 var2 = mc.field_1724;
         this.D = var1.slot();
         boolean var3 = var1.swap();
         if (var1.resets()) {
            this.i = th.U(this.x(this.D), false, this.n, 0, 0.0F, this.f());
            var2.method_31548().method_61496(this.D);
            this.P = LungeSwapModule.h.B;
            this.K = 0;
            this.s = "Charging spear " + th.L(this.i);
            q8.G("LungeSwap", "Selected spear slot {}; the server restarts its charge, so the jab waits {} ticks", this.D + 1, this.i);
         } else {
            this.E = (boolean)(var3 && !class_1799.method_7984(var2.method_6047(), var2.method_31548().method_5438(this.D)) ? 1 : 0);
            var2.method_31548().method_61496(this.D);
            this.p(var3);
         }
      }
   }

   private th.b x(int var1) {
      class_746 var2 = mc.field_1724;
      class_1799 var3 = var2.method_31548().method_5438(var1);
      return new th.b(var1, X(var2, var3), (Float)var3.method_58695(class_9334.field_63635, 0.0F), this.h(var1), 0, false, false);
   }

   private void K() {
      class_746 var1 = mc.field_1724;
      this.K++;
      if (var1.method_31548().method_67532() == this.D && this.A(this.D)) {
         int var2 = this.i - this.K;
         if (var2 > 0) {
            this.s = "Charging spear " + th.L(var2);
         } else if (!this.w()) {
            String var3 = this.X();
            if (var3 != null) {
               this.B(var3);
               this.s = "Waiting for " + var3;
               if (++this.O > 10) {
                  q8.G("LungeSwap", "Charged jab lapsed, held by {}", var3);
                  cr.p(this);
                  this.R();
                  this.s = "Lunge blocked by " + var3;
               }
            } else {
               cr.y(this, () -> {
                  if (this.P == LungeSwapModule.h.B && this.X() == null) {
                     this.p(true);
                  }
               }, () -> this.P == LungeSwapModule.h.B && this.X() == null);
            }
         }
      } else {
         q8.G("LungeSwap", "Charge interrupted after {} ticks: slot {} selected", this.K, var1.method_31548().method_67532() + 1);
         cr.p(this);
         this.o();
         this.s = "Swap interrupted";
      }
   }

   private void p(boolean var1) {
      class_746 var2 = mc.field_1724;
      class_1799 var3 = var2.method_31548().method_5438(this.D);
      int var4 = var1 && this.P == LungeSwapModule.h.a ? 1 : 0;
      if (var3.method_45435(this.e.method_45162())) {
         mc.field_1761.method_75407((class_12125)var3.method_58694(class_9334.field_63631));
         var2.method_6104(class_1268.field_5808);
         this.s = "Lunged";
         this.T = 12;
         this.d = 0.0;
         this.z = var2.method_73189();
         this.S = (boolean)(var4 != 0 && this.E ? 1 : 0);
      } else {
         this.s = "Spear not enabled here";
      }

      int var5 = this.b + this.O + this.K;
      this.P = LungeSwapModule.h.X;
      this.r = this.A(this.X, this.J);
      q8.G(
         "LungeSwap",
         "{}: {} {} rule, slot {} ({}, charge {}) to spear slot {} (Lunge {}) {} ticks after the press{}; food {}, ground {}, sprint {}",
         this.s,
         this.o.getValue(),
         this.n,
         this.y + 1,
         this.u,
         j(this.C),
         this.D + 1,
         this.h(this.D),
         var5,
         var4 != 0 ? ", swapped in the jab tick" : (var1 ? ", spear charged after the swap" : ", spear already held"),
         var2.method_7344().method_7586(),
         var2.method_24828(),
         var2.method_5624()
      );
      if (this.r > 1) {
         q8.G("LungeSwap", "Swap back in {} ticks", this.r);
      }
   }

   private void R() {
      class_746 var1 = getMc() == null ? null : mc.field_1724;
      if (var1 != null && var1 == this.V && var1.method_73183() == this.e) {
         int var2 = J(this.t(), this.F.getValue(), this.a.getValue().intValue() - 1, this.y, var1.method_31548().method_67532(), this.D);
         if (var2 >= 0) {
            var1.method_31548().method_61496(var2);
         }
      }

      this.o();
   }

   private void M(boolean var1) {
      cr.p(this);
      if (this.P != LungeSwapModule.h.e) {
         q8.G("LungeSwap", "Cancelled at {}; restore={}, status {}", this.P, var1, this.s);
      }

      if (!var1 || this.P != LungeSwapModule.h.X && this.P != LungeSwapModule.h.B) {
         this.o();
      } else {
         this.R();
      }
   }

   private void o() {
      this.P = LungeSwapModule.h.e;
      this.E = (boolean)0;
      this.y = this.D = -1;
      this.l = this.r = 0;
      this.b = this.O = this.K = this.i = 0;
      this.m = -1;
      this.N = (boolean)0;
      this.Q = "";
   }

   private boolean A(int var1) {
      return this.h(var1) > 0;
   }

   private int h(int var1) {
      if (var1 >= 0 && var1 < 9 && mc.field_1724 != null && mc.field_1687 != null) {
         class_1799 var2 = mc.field_1724.method_31548().method_5438(var1);
         if (!var2.method_57826(class_9334.field_63631)) {
            return 0;
         }

         Optional var3 = mc.field_1687.method_30349().method_30530(class_7924.field_41265).method_46746(class_1893.field_63420);
         return var3.<Integer>map(var1x -> class_1890.method_8225(var1x, var2)).orElse(0);
      } else {
         return 0;
      }
   }

   private static String j(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public enum h {
      e,
      a,
      B,
      X;
   }
}
