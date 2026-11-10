package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.cr;
import com.larpclient.el;
import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.wp;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.minecraft.class_1297;
import net.minecraft.class_239;
import net.minecraft.class_3966;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;

public final class AutoClickerModule extends Module {
   private static AutoClickerModule b;
   private final li t = this.addSetting(new li("Activation", "Hold each attack/use bind, or click while the module is enabled", "Hold", "Hold", "Automatic"));
   private final l3 n = this.addSetting(new l3("Left Click", "Repeat attacks", true));
   private final l3 Y = this.addSetting(new l3("Right Click", "Repeat item use and placement", false));
   private final lo d = this.addSetting(new lo("CPS", "Maximum left clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final lo e = this.addSetting(new lo("Right CPS", "Maximum right clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final l3 I = this.addSetting(new l3("Randomize CPS", "Choose a rate within each button's range after a click", false));
   private final lo L = this.addSetting(new lo("Min CPS", "Minimum left rate, capped by CPS", 8.0, 1.0, 20.0, 1.0));
   private final lo A = this.addSetting(new lo("Min Right CPS", "Minimum right rate, capped by Right CPS", 8.0, 1.0, 20.0, 1.0));
   private final l3 D = this.addSetting(new l3("Only Weapon", "Only click while holding a weapon", false));
   private final l3 R = this.addSetting(new l3("Wait Cooldown", "Wait for a fully charged attack", false));
   private final l3 T = this.addSetting(new l3("Only Entities", "Left clicks require an entity under your actual aim", false));
   private final l3 G = this.addSetting(new l3("Break Blocks", "Keep native continuous mining while aiming at blocks", true));
   private final el s = new el();
   private final el u = new el();
   private boolean k;
   private boolean a;
   private String U = "Waiting for input";
   private long B;
   private int C;
   private int z;

   public AutoClickerModule() {
      super("AutoClicker", "Customizable attack and use clicking", Category.COMBAT, -1);
      b = this;
      this.d.O(this.n::getValue);
      this.e.O(this.Y::getValue);
      this.L.O(() -> this.n.getValue() && this.I.getValue());
      this.A.O(() -> this.Y.getValue() && this.I.getValue());
      this.R.O(this.n::getValue);
      this.T.O(this.n::getValue);
      this.G.O(() -> this.n.getValue() && !this.T.getValue());

      for (lb var2 : this.getSettings()) {
         var2.a(var1 -> this.G());
      }
   }

   public static AutoClickerModule s() {
      return b;
   }

   public static void p(JsonObject var0) {
      if (!var0.has("Right CPS") && var0.has("CPS")) {
         var0.add("Right CPS", var0.get("CPS").deepCopy());
      }
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   public List<lb<?>> O() {
      return List.of(this.t, this.n, this.Y, this.D);
   }

   public List<lb<?>> v() {
      return List.of(this.d, this.e, this.I, this.L, this.A, this.R);
   }

   public List<lb<?>> L() {
      return List.of(this.T, this.G);
   }

   private boolean Q() {
      return this.isRuntimeActive()
         && this.isInGame()
         && mc.field_1761 != null
         && mc.field_1724.method_5805()
         && !mc.field_1724.method_7325()
         && mc.method_1569()
         && mc.field_1755 == null
         && mc.method_18506() == null
         && (!this.D.getValue() || ReachModule.W());
   }

   public boolean X() {
      return !this.isEnabled() ? false : this.Q() && this.n.getValue() && !mc.field_1724.method_6047().method_57826(class_9334.field_63631);
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.Q() && this.Y.getValue();
   }

   public boolean T() {
      return this.k;
   }

   private boolean C(boolean var1) {
      return this.t.G("Automatic") || (var1 ? mc.field_1690.field_1904 : mc.field_1690.field_1886).method_1434();
   }

   private boolean s() {
      return j.F("Aura") || wp.o() != wp.z.D;
   }

   private boolean q(boolean var1) {
      return (var1 ? this.h() : this.X())
         && this.C(var1)
         && cr.f()
         && !this.s()
         && !mc.field_1724.method_6115()
         && !mc.field_1724.method_3144()
         && (var1 || !mc.field_1690.field_1904.method_1434());
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.h() && this.t.G("Automatic") && !this.s() && cr.f() && mc.field_1724.method_6115();
   }

   @Override
   public void onTick() {
      if (this.Q()) {
         int var10000 = cr.f();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && !this.s()) {
            long var1 = System.nanoTime();
            this.U = mc.field_1724.method_6115() ? "Using held item" : "Waiting for input";
            if (!this.q(false)) {
               this.s.B();
            }

            int var17 = this.q(true);
            boolean var13 = false;
            byte var12 = (byte)var17;
            var17 = Integer.signum(var12);
            if ((var17 | -var17) >>> 31 == 0) {
               this.u.B();
            }

            int var3 = this.q(true) && this.u.E(var1) ? 1 : 0;
            if (var3 != 0) {
               boolean var11 = false;
               byte var10 = this.a;
               var17 = Integer.signum(var10);
               if (((var17 | -var17) >>> 31 ^ 1) == 0) {
                  this.b();
                  return;
               }
            }

            byte var4 = 0;
            if (this.q(false) && this.s.E(var1)) {
               class_239 var5 = this.G();
               label50:
               if (var5 instanceof class_3966 var6) {
                  if (this.R.getValue()) {
                     int var20 = cr.A();
                     boolean var15 = false;
                     byte var14 = (byte)var20;
                     var20 = Integer.signum(var14);
                     if (((var20 | -var20) >>> 31 ^ 1) != 0) {
                        this.U = "Waiting for attack cooldown";
                        break label50;
                     }
                  }

                  class_1297 var7 = var6.method_17782();
                  cr.U(
                     this,
                     var7,
                     mc.field_1724.method_55755(),
                     this.R.getValue(),
                     () -> this.q(false) && this.G() instanceof class_3966 var2 && var2.method_17782() == var7,
                     1,
                     () -> this.U(false),
                     var1x -> this.U = "Waiting: " + var1x
                  );
                  var4 = 1;
               } else if (var5.method_17783() == class_240.field_1333 && !this.T.getValue()) {
                  cr.y(this, () -> this.Q(() -> {
                     mc.method_1536();
                     this.U(false);
                  }), () -> this.q(false) && !this.T.getValue() && this.G().method_17783() == class_240.field_1333 && (!this.R.getValue() || cr.A()));
                  var4 = 1;
               }
            }

            if (var3 != 0 && var4 == 0) {
               this.b();
            }

            return;
         }
      }

      this.G();
      this.U = "Paused: camera or input unavailable";
   }

   private void b() {
      cr.y(this, () -> this.Q(() -> {
         mc.method_1583();
         this.U(true);
      }), () -> this.q(true));
   }

   public void f(Consumer<Boolean> var1, boolean var2) {
      if (!this.X()) {
         var1.accept(var2);
      } else {
         this.Q(() -> {
            int var2x = this.q(false) && this.G.getValue() && !this.T.getValue() && mc.field_1765.method_17783() == class_240.field_1332 ? 1 : 0;
            var1.accept(Boolean.valueOf((boolean)var2x));
            if (var2x != 0) {
               this.U = "Mining";
            }
         });
      }
   }

   private void Q(Runnable var1) {
      class_239 var2 = mc.field_1765;
      boolean var3 = this.k;
      mc.field_1765 = this.G();
      this.k = (boolean)1;

      try {
         var1.run();
      } finally {
         mc.field_1765 = var2;
         this.k = var3;
      }
   }

   private class_239 G() {
      return mc.field_1724.method_76762(1.0F, mc.field_1724);
   }

   private void U(boolean var1) {
      long var2 = System.nanoTime();
      double var4 = (var1 ? this.e : this.d).getValue();
      double var6 = this.I.getValue() ? (var1 ? this.A : this.L).getValue() : var4;
      (var1 ? this.u : this.s).i(var2, var6, var4, ThreadLocalRandom.current().nextDouble());
      if (var1) {
         this.z++;
      } else {
         this.C++;
      }

      this.a = (boolean)(!var1 ? 1 : 0);
      this.U = var1 ? "Using" : "Clicking";
      if (var2 - this.B >= 1000000000L) {
         this.B = var2;
         q8.v("AutoClicker", "Native clicks: left={}, right={}; activation={}", this.C, this.z, this.t.getValue());
      }
   }

   private void G() {
      this.s.B();
      this.u.B();
      cr.p(this);
   }

   @Override
   public void onEnable() {
      this.G();
      this.C = this.z = 0;
      this.B = 0L;
      this.a = (boolean)0;
      this.U = "Waiting for input";
      q8.v(
         "AutoClicker",
         "Enabled: activation={}, left={} CPS, right={} CPS, cooldown={}",
         this.t.getValue(),
         this.d.getValue(),
         this.e.getValue(),
         this.R.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.G();
      this.U = "Waiting for input";
      q8.v("AutoClicker", "Released pending clicks and button timers");
   }

   @Override
   public String getStatus() {
      return this.U;
   }
}
