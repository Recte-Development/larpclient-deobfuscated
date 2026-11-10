package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.fs;
import com.larpclient.q8;
import com.larpclient.ug;
import com.larpclient.w;
import com.larpclient.w6;
import java.util.Set;
import java.util.function.ToIntFunction;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1661;
import net.minecraft.class_1684;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_746;

public final class AutoElytraSwapModule extends Module {
   static final double P = 1.2;
   static final double A = 1.5;
   private static final int Z = 40;
   private static final int x = 5;
   private static final int s = 10;
   private static final int R = 5;
   private static final class_1792[] L = new class_1792[]{
      class_1802.field_22028,
      class_1802.field_8058,
      class_1802.field_8523,
      class_1802.field_61344,
      class_1802.field_8678,
      class_1802.field_8873,
      class_1802.field_8577
   };
   private final lo n = this.addSetting(
      new lo("Swap Range", "How close an opponent must be, in clear view, to swap back to your chestplate", 3.0, 1.0, 6.0, 0.1)
   );
   private final l3 k = this.addSetting(new l3("Start Gliding", "Open the elytra right after equipping it", true));
   private final l3 O = this.addSetting(new l3("Swap On Landing", "Put your chestplate back when you land without reaching anyone", true));
   private final l3 r = this.addSetting(
      new l3("Hotbar Only", "Only swap pieces in your hotbar and never click inventory slots, which anticheats can flag while you move", false)
   );
   private final w S = new w(this);
   private final fs K = new fs(this);
   private class_746 G;
   private class_638 v;
   private class_1799 U;
   private class_243 B;
   private class_243 m = class_243.field_1353;
   private AutoElytraSwapModule.a b = AutoElytraSwapModule.a.l;
   private boolean z;
   private long y;
   private long p = -1L;
   private long W;
   private long E;
   private long I = -1L;
   private int D = -1;
   private String H = "";

   public AutoElytraSwapModule() {
      super("Auto Elytra Swap", "Wear the elytra after a pearl catch and swap back to your chestplate when you can hit someone", Category.MOVEMENT, -1);
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return !this.isEnabled() ? "" : (this.b == AutoElytraSwapModule.a.A ? "Elytra on" : (this.b == AutoElytraSwapModule.a.d ? "Equipping" : this.H));
   }

   @Override
   public void onEnable() {
      this.D();
      this.G = null;
      this.v = null;
      this.H = "";
      q8.G(
         "ElytraSwap",
         "Enabled; swap range={}, start gliding={}, swap on landing={}, hotbar only={}",
         this.n.getValue(),
         this.k.getValue(),
         this.O.getValue(),
         this.r.getValue()
      );
   }

   @Override
   public void onDisable() {
      cr.p(this);
      if (this.K.I()) {
         this.K.o(getMc(), true);
      }

      if (this.b != AutoElytraSwapModule.a.l) {
         q8.G("ElytraSwap", "Disabled during a swap; equipment left as worn");
      }

      this.D();
      this.G = null;
      this.v = null;
   }

   private void D() {
      this.b = AutoElytraSwapModule.a.l;
      this.D = -1;
      this.I = this.p = -1L;
      this.B = null;
      this.m = class_243.field_1353;
      this.z = (boolean)0;
      w.N(this);
      this.S.n();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         class_746 var1 = mc.field_1724;
         if (var1 != this.G || mc.field_1687 != this.v) {
            if (this.b != AutoElytraSwapModule.a.l) {
               q8.G("ElytraSwap", "World changed; swap state released");
            }

            this.D();
            this.G = var1;
            this.v = mc.field_1687;
         }

         this.y++;
         int var10000 = w.o(this);
         boolean var4 = false;
         byte var3 = (byte)var10000;
         var10000 = Integer.signum(var3);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            if (this.b != AutoElytraSwapModule.a.l) {
               q8.G("ElytraSwap", "Elytra Pilot took the flight; swap state released");
               this.D();
            }
         } else {
            byte var2 = this.h(var1);
            this.E(var1);
            boolean var6 = false;
            byte var5 = var2;
            var10000 = Integer.signum(var5);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               long var12;
               var10000 = (var12 = this.y - this.p) == 0L ? 0 : (var12 < 0L ? -1 : 1);
               boolean var8 = false;
               int var7 = var10000;
               if (-Integer.signum(var7) >>> 31 == 0) {
                  this.p = -1L;
                  this.G(var1);
               }
            }

            if (this.b == AutoElytraSwapModule.a.d) {
               this.b();
            } else if (this.b == AutoElytraSwapModule.a.A) {
               this.q(var1);
            }
         }
      }
   }

   private boolean h(class_746 var1) {
      class_243 var2 = var1.method_73189();
      int var3 = this.B != null && w(var2.method_1020(this.B), this.m) ? 1 : 0;
      this.B = var2;
      this.m = var1.method_18798();
      return (boolean)var3;
   }

   private void E(class_746 var1) {
      if (this.U == null) {
         this.U = new class_1799(class_1802.field_8634);
      }

      boolean var2 = var1.method_7357().method_7904(this.U);
      if (var2 && !this.z) {
         this.p = Math.max(this.p, this.y + 40L);
      }

      this.z = var2;

      for (class_1297 var4 : mc.field_1687.method_18112()) {
         if (var4 instanceof class_1684 var5 && !var5.method_31481() && var5.method_24921() == var1) {
            this.p = Math.max(this.p, this.y + 5L);
            return;
         }
      }
   }

   private void G(class_746 var1) {
      if (this.b == AutoElytraSwapModule.a.l) {
         if (var1.method_24828()
            || var1.method_5799()
            || var1.method_5771()
            || var1.method_5765()
            || !mc.field_1687.method_8587(var1, var1.method_5829().method_1012(0.0, -1.5, 0.0))) {
            q8.v("ElytraSwap", "Pearl teleport ended near the ground or in water; no swap");
         } else if (this.z(var1)) {
            q8.G("ElytraSwap", "Caught next to an opponent; keeping the chestplate on");
         } else {
            this.H = "";
            if (P(var1)) {
               this.D = -1;
               this.f();
               q8.G("ElytraSwap", "Caught with the elytra already on");
            } else {
               this.b = AutoElytraSwapModule.a.d;
               this.W = this.y + 10L;
               q8.G("ElytraSwap", "Pearl catch seen at y {}; equipping the elytra", Math.round(var1.method_23318()));
            }
         }
      }
   }

   private void b() {
      if (this.y > this.W) {
         this.m("Could not equip the elytra in time");
      } else {
         cr.y(this, this::d, () -> this.b == AutoElytraSwapModule.a.d && this.A());
      }
   }

   private void q(class_746 var1) {
      if (!P(var1)) {
         q8.G("ElytraSwap", "Elytra was taken off by something else; swap state released");
         this.C();
      } else {
         boolean var2 = this.z(var1);
         int var3 = this.O.getValue() && var1.method_24828() ? 1 : 0;
         if (!var2 && var3 == 0) {
            if (this.k.getValue() && this.y > this.E && this.y <= this.I && !var1.method_6128() && !var1.method_24828() && cr.f() && this.S.X()) {
               this.I = -1L;
            }
         } else if (this.q() < 0) {
            this.H = "No chestplate";
         } else {
            cr.y(this, () -> this.g(var2 ? "opponent in range" : "landed"), () -> this.b == AutoElytraSwapModule.a.A && this.A());
         }
      }
   }

   private void f() {
      this.b = AutoElytraSwapModule.a.A;
      this.E = this.y;
      this.I = this.y + 5L;
      w.h(this);
   }

   private void C() {
      this.b = AutoElytraSwapModule.a.l;
      this.D = -1;
      this.I = -1L;
      w.N(this);
   }

   private void m(String var1) {
      this.C();
      this.H = var1;
      q8.G("ElytraSwap", "Elytra not equipped: {}", var1);
   }

   private void d() {
      int var1 = this.s(AutoElytraSwapModule::d);
      if (var1 < 0) {
         this.m(this.r.getValue() ? "No elytra in hotbar" : "No elytra");
      } else {
         class_746 var2 = mc.field_1724;
         int var3 = l(var2.method_6118(class_1304.field_6174)) > 0 ? 1 : 0;
         if (this.v(var1) && P(var2)) {
            this.D = var3 != 0 ? var1 : -1;
            this.f();
            q8.G("ElytraSwap", "Elytra equipped from inventory slot {}; chestplate kept there: {}", var1, Boolean.valueOf((boolean)var3));
         }
      }
   }

   private void g(String var1) {
      int var2 = this.q();
      if (var2 >= 0) {
         class_746 var3 = mc.field_1724;
         if (this.v(var2) && !P(var3)) {
            this.C();
            q8.G("ElytraSwap", "Chestplate back on from inventory slot {}: {}", var2, var1);
         }
      }
   }

   private int q() {
      return this.D >= 0 && l(mc.field_1724.method_31548().method_5438(this.D)) > 0 ? this.D : this.s(AutoElytraSwapModule::l);
   }

   private int s(ToIntFunction<class_1799> var1) {
      int var2 = this.c(var1, 0, 9);
      return var2 < 0 && !this.r.getValue() ? this.c(var1, 9, 36) : var2;
   }

   private int c(ToIntFunction<class_1799> var1, int var2, int var3) {
      class_1661 var4 = mc.field_1724.method_31548();
      int var5 = -1;
      int var6 = 0;

      for (int var7 = var2; var7 < var3; var7++) {
         int var8 = var1.applyAsInt(var4.method_5438(var7));
         if (var8 > var6) {
            var6 = var8;
            var5 = var7;
         }
      }

      return var5;
   }

   private boolean v(int var1) {
      if (var1 < 9) {
         return this.g(var1);
      }

      if (!this.G()) {
         return false;
      }

      ElytraMotionModule.O(var1);
      return true;
   }

   private boolean g(int var1) {
      class_746 var2 = mc.field_1724;
      if (var1 != var2.method_31548().method_67532() && !this.K.m(var2, var1)) {
         return false;
      }

      try {
         boolean var3 = mc.field_1761.method_2919(var2, class_1268.field_5808).method_23665();
         if (var3) {
            w6.H(var2, class_1268.field_5808);
         }

         return var3;
      } finally {
         if (this.K.I()) {
            int var7 = var2.method_31548().method_67532();
            this.K.o(getMc(), true);
            if (var7 != var2.method_31548().method_67532()) {
               ((q226fb0b431f37244)mc.field_1761).larpSyncCarriedItem();
            }
         }
      }
   }

   private boolean A() {
      return this.isRuntimeActive() && this.isInGame() && mc.field_1724 == this.G && cr.f() && !mc.field_1724.method_6115() && !AuraModule.d();
   }

   private boolean G() {
      return mc.field_1724.field_7512 == mc.field_1724.field_7498 && mc.field_1724.field_7498.method_34255().method_7960();
   }

   private static boolean P(class_746 var0) {
      return var0.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833);
   }

   private boolean z(class_746 var1) {
      class_1309 var2 = ElytraHelperModule.d();
      if (var2 != null && this.B(var1, var2)) {
         return true;
      }

      for (class_742 var4 : mc.field_1687.method_18456()) {
         if (var4 != var1 && bn.K(var1, var4) && this.B(var1, var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean B(class_746 var1, class_1309 var2) {
      return R(var1.method_33571(), var1.method_18798(), var2.method_5829(), this.n.getValue()) && var1.method_6057(var2);
   }

   static boolean w(class_243 var0, class_243 var1) {
      return var0.method_1033() > 1.2 && var0.method_1020(var1).method_1033() > 1.2;
   }

   static boolean R(class_243 var0, class_243 var1, class_238 var2, double var3) {
      return Math.min(ug.L(var0, var2), ug.L(var0.method_1019(var1), var2)) <= var3;
   }

   static int l(class_1799 var0) {
      for (int var1 = 0; var1 < L.length; var1++) {
         if (var0.method_31574(L[var1])) {
            return L.length - var1;
         }
      }

      return 0;
   }

   static int d(class_1799 var0) {
      return var0.method_31574(class_1802.field_8833) && !var0.method_63692() ? var0.method_7936() - var0.method_7919() : 0;
   }

   private enum a {
      l,
      d,
      A;
   }
}
