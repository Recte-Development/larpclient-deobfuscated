package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q219e0ba2e314b7b5;
import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larpclient.bn;
import com.larpclient.ga;
import com.larpclient.q8;
import com.larpclient.tp;
import com.larpclient.wp;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_10185;
import net.minecraft.class_156;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2815;
import net.minecraft.class_310;
import net.minecraft.class_408;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_490;
import net.minecraft.class_746;

public final class AutoTotemModule extends Module {
   private static AutoTotemModule Z;
   private Object R;
   private Object i;
   private long h = Long.MIN_VALUE;
   private final li g = this.addSetting(new li("Mode", "When to protect your offhand", "Health", "Health", "Always"));
   private final lo v = this.addSetting(new lo("Health", "Equip at or below this health; an empty offhand is always protected", 10.0, 1.0, 20.0, 0.5));
   private final l3 a = this.addSetting(new l3("Include Absorption", "Count absorption hearts when comparing health", false));
   private final li K = this.addSetting(new li("Swap Mode", "Route from inventory to offhand", "Direct", "Direct", "Hotbar"));
   private final li d = this.addSetting(
      new li("Swap Style", "Legit stops and clicks like a player in the inventory; Instant clicks at once and gets flagged", "Legit", "Legit", "Instant")
   );
   private final lo B = this.addSetting(new lo("Click Delay", "Shortest pause in milliseconds before each legit inventory step", 100.0, 0.0, 1000.0, 10.0));
   private final lo J = this.addSetting(new lo("Click Delay Max", "Longest pause in milliseconds before each legit inventory step", 200.0, 0.0, 1000.0, 10.0));
   private final l3 W = this.addSetting(new l3("Offhand Key", "Equip hotbar totems with the swap hands key, which works while moving", true));
   private final l3 l = this.addSetting(new l3("Open Inventory", "Show the inventory during legit clicks, which also stops camera turns", false));
   private final lo I = this.addSetting(new lo("Delay", "Milliseconds before equipping", 0.0, 0.0, 1000.0, 10.0));
   private final lo c = this.addSetting(new lo("Random Delay", "Maximum additional milliseconds, sampled once per equip", 0.0, 0.0, 1000.0, 10.0));
   private final l3 w = this.addSetting(new l3("Hotbar Refill", "Keep spare totems in empty hotbar slots without replacing items", false));
   private final lo f = this.addSetting(new lo("Hotbar Reserves", "Desired number of hotbar slots holding spare totems", 1.0, 1.0, 9.0, 1.0));
   private final l3 o = this.addSetting(new l3("In Inventory", "Allow swaps in the survival inventory when the cursor is empty", true));
   private final ga.s N = new ga.s();
   private final ga.o L = new ga.o();
   private final ga.h V = new ga.h();
   private class_746 A;
   private Object m;
   private class_437 X;

   public AutoTotemModule() {
      super("AutoTotem", "Automatically equip offhand totems", Category.COMBAT, -1);
      Z = this;
      this.v.O(() -> this.g.G("Health"));
      this.a.O(() -> this.g.G("Health"));
      this.f.O(this.w::getValue);
      this.B.m(this.J).M("Click Delay");
      this.B.O(() -> this.d.G("Legit"));
      this.J.O(() -> this.d.G("Legit"));
      this.W.O(() -> this.d.G("Legit"));
      this.l.O(() -> this.d.G("Legit"));
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onEnable() {
      this.N.d();
      this.R = this.i = null;
      this.J();
   }

   @Override
   public void onDisable() {
      this.p("module disabled", true);
      this.N.d();
      this.R = this.i = null;
   }

   public static boolean s(class_746 var0) {
      if (Z != null && Z.isEnabled()) {
         AutoTotemModule var1 = Z;
         return var1 != null && var0 != null && var1.L.y() && var1.A == var0 && var1.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean h() {
      return q(false);
   }

   public static boolean A() {
      return q(true);
   }

   private static boolean q(boolean var0) {
      AutoTotemModule var1 = Z;
      if (var1 != null && var1.isRuntimeActive()) {
         class_310 var2 = getMc();
         class_746 var3 = var2.field_1724;
         if (var3 == null || var1.A != var3 || !var1.V.U() && (!var1.L.y() || var0)) {
            if (var3 != null
               && var2.field_1687 != null
               && var3.method_5805()
               && !var3.method_7325()
               && !wp.d()
               && var3.field_7512 == var3.field_7498
               && var3.field_7498.method_34255().method_7960()
               && var1.Y(var2.field_1755)) {
               if (var0 && !var1.K.G("Hotbar")) {
                  return false;
               }

               if (var1.R == var3 && var1.i == var2.field_1687 && class_156.method_658() - var1.h < 100L) {
                  return true;
               }

               boolean var4 = ga.k(
                  var3.method_6079().method_31574(class_1802.field_8288),
                  var3.method_6079().method_7960(),
                  var1.g.G("Always"),
                  var3.method_6032(),
                  var3.method_6067(),
                  var1.a.getValue(),
                  var1.v.getValue()
               );
               long var5 = 0L;
               int var7 = 0;

               for (int var8 = 0; var8 < 36; var8++) {
                  class_1799 var9 = var3.method_31548().method_5438(var8);
                  if (var9.method_31574(class_1802.field_8288)) {
                     var5 |= 1L << var8;
                  } else if (var8 < 9 && var9.method_7960()) {
                     var7 |= 1 << var8;
                  }
               }

               return ga.K(var5, var7, var4, var1.K.G("Hotbar"), var1.w.getValue(), var1.f.getValue().intValue()) != null;
            } else {
               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (this.A != null && (this.A != mc.field_1724 || this.m != mc.field_1687)) {
         q8.v("AutoTotem", "Player or world changed during a swap; state dropped without packets");
         this.J();
      }

      if (this.isInGame() && mc.field_1761 != null) {
         int var10000 = mc.field_1724.method_5805();
         boolean var7 = false;
         byte var6 = (byte)var10000;
         var10000 = Integer.signum(var6);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && !mc.field_1724.method_7325() && !wp.d() && mc.field_1724.field_7512 == mc.field_1724.field_7498) {
            int var16 = mc.field_1724.field_7498.method_34255().method_7960();
            boolean var9 = false;
            byte var8 = (byte)var16;
            var16 = Integer.signum(var8);
            if (((var16 | -var16) >>> 31 ^ 1) == 0 && this.Y(mc.field_1755)) {
               long var1 = class_156.method_658();
               if (this.V.U()) {
                  this.V(var1);
                  return;
               }

               if (this.L.y()) {
                  this.a(var1);
                  return;
               }

               if (this.N.v()) {
                  if (mc.field_1724.method_6079().method_31574(class_1802.field_8288)) {
                     this.N.B();
                     q8.v("AutoTotem", "Server confirmed the swap hands press");
                  } else {
                     if (this.N.p(var1)) {
                        return;
                     }

                     this.N.B();
                     q8.L("AutoTotem", "No totem {} ms after swap hands; the server dropped it or is lagging, retrying", 750L);
                  }
               }

               boolean var3 = this.F();
               if (!var3) {
                  this.N.p();
               }

               ga.r var4 = this.F(var3);
               if (var4 == null) {
                  this.N.p();
                  return;
               }

               if (var3) {
                  int var18 = this.d.G("Legit");
                  boolean var11 = false;
                  byte var10 = (byte)var18;
                  var18 = Integer.signum(var10);
                  if (((var18 | -var18) >>> 31 ^ 1) == 0 && this.W.getValue() && this.B()) {
                     var4 = ga.y(var4, this.L(), !this.N(), mc.field_1724.method_6115());
                  }
               }

               if (var3) {
                  int var20 = this.N.E();
                  boolean var13 = false;
                  byte var12 = (byte)var20;
                  var20 = Integer.signum(var12);
                  int var5 = (var20 | -var20) >>> 31 == 0 && this.c.getValue() > 0.0
                     ? ThreadLocalRandom.current().nextInt(this.c.getValue().intValue() + 1)
                     : 0;
                  if (!this.N.B(var1, this.I.getValue().intValue() + var5)) {
                     return;
                  }
               } else if (!this.N.h(var1)) {
                  return;
               }

               if (!this.U(var4)) {
                  this.N.p();
                  return;
               }

               if (this.d.G("Legit")) {
                  this.c(var1, var4, var3);
                  return;
               }

               class_1723 var14 = mc.field_1724.field_7498;
               mc.field_1761.method_2906(var14.field_7763, var4.menuSlot(), var4.button(), class_1713.field_7791, mc.field_1724);
               this.R = mc.field_1724;
               this.i = mc.field_1687;
               this.h = var1;
               this.N.h(var1, var4.equips());
               return;
            }
         }
      }

      this.p("context changed", true);
      this.N.p();
   }

   private void c(long var1, ga.r var3, boolean var4) {
      class_437 var5 = mc.field_1755;
      if (var3.equips() && var3.source() < 9 && this.W.getValue() && var5 == null) {
         this.A = mc.field_1724;
         this.m = mc.field_1687;
         int var8 = mc.field_1724.method_31548().method_67532();
         this.V.e(var1, var8, var3.source(), this.Z());
         q8.v("AutoTotem", "Swap hands route: hotbar {} to offhand, selected slot {}", var3.source() + 1, var8 + 1);
         this.V(var1);
      } else {
         boolean var6 = var5 instanceof class_490;
         if (var4 || var6 || this.N()) {
            this.A = mc.field_1724;
            this.m = mc.field_1687;
            this.L.F(var1, !var6);
            int var7 = var5 == null && this.l.getValue() ? 1 : 0;
            if (var7 != 0) {
               this.X = new class_490(mc.field_1724);
               mc.method_1507(this.X);
            }

            q8.v(
               "AutoTotem",
               "Inventory visit for {} from slot {}: {}, waiting for idle input and no sprint",
               var4 ? "equip" : "refill",
               var3.source(),
               var7 != 0 ? "opened the inventory" : (var6 ? "player inventory already open" : "silent visit")
            );
            this.a(var1);
         }
      }
   }

   private void a(long var1) {
      if (this.X != null && mc.field_1755 != this.X) {
         q8.v("AutoTotem", "Opened inventory was closed after {} clicks; visit ended", this.L.L());
         this.X = null;
         this.L.O();
         this.f();
      } else if (this.L.o(var1)) {
         q8.L("AutoTotem", "Inventory visit timed out in stage {}; closing and releasing input", this.L.T());
         this.p("timed out", true);
      } else {
         boolean var3 = this.N();
         switch (this.L.T()) {
            case w:
               if (this.L.v(var1, var3, this.Z())) {
                  q8.v("AutoTotem", "Server has idle input and no sprint; first click after a human pause");
               }
               break;
            case R:
               if (!this.L.c(var1, var3)) {
                  return;
               }

               ga.r var4 = this.F(this.F());
               if (var4 != null && this.U(var4)) {
                  class_1723 var5 = mc.field_1724.field_7498;
                  mc.field_1761.method_2906(var5.field_7763, var4.menuSlot(), var4.button(), class_1713.field_7791, mc.field_1724);
                  this.R = mc.field_1724;
                  this.i = mc.field_1687;
                  this.h = var1;
                  this.N.h(var1, var4.equips());
                  this.L.y(var1, this.Z());
                  q8.v(
                     "AutoTotem",
                     "Clicked slot {} to {} (state {})",
                     var4.menuSlot(),
                     var4.button() == 40 ? "offhand" : "hotbar " + (var4.button() + 1),
                     var5.method_37421()
                  );
                  break;
               }

               if (this.L.s(var1, this.Z())) {
                  q8.v("AutoTotem", "Clicks done ({}); closing the inventory after a pause", this.L.L());
               } else {
                  q8.v("AutoTotem", "Visit ended after {} clicks", this.L.L());
                  this.j();
               }

               return;
            case Y:
               if (!this.L.j(var1, var3)) {
                  return;
               }

               this.b();
               q8.v("AutoTotem", "Closed the inventory while still; input released");
               this.j();
               break;
            default:
               this.j();
         }
      }
   }

   private void V(long var1) {
      class_746 var3 = mc.field_1724;
      class_1661 var4 = var3.method_31548();
      if (this.V.X(var1)) {
         q8.L("AutoTotem", "Swap hands route timed out in stage {}", this.V.D());
         this.p("timed out", true);
      } else if (this.V.U(var1)) {
         int var5 = this.V.H();
         switch (this.V.D()) {
            case X:
               if (var4.method_5438(var5).method_31574(class_1802.field_8288) && !var3.method_6079().method_31574(class_1802.field_8288)) {
                  var4.method_61496(var5);
                  this.V.u(var1, this.Z());
                  q8.v("AutoTotem", "Selected hotbar {}; swap hands follows on a later tick", var5 + 1);
                  break;
               }

               q8.v("AutoTotem", "Hotbar totem moved before selection; route cancelled");
               this.V.z();
               this.f();
               return;
            case Y:
               if (var4.method_67532() != var5) {
                  q8.v("AutoTotem", "Player changed the selected slot; swap hands route cancelled");
                  this.V.z();
                  this.f();
                  return;
               }

               if (!var3.method_6079().method_31574(class_1802.field_8288) && var4.method_5438(var5).method_31574(class_1802.field_8288)) {
                  if (this.B() && !bn.M(mc.field_1690.field_1831)) {
                     ((q219e0ba2e314b7b5)mc.field_1690.field_1831).larpSetClickCount(1);
                     this.R = var3;
                     this.i = mc.field_1687;
                     this.h = var1;
                     this.N.A(var1);
                     this.V.u(var1, this.Z());
                     q8.v("AutoTotem", "Pressed swap hands for hotbar {}", var5 + 1);
                     break;
                  }

                  return;
               }

               q8.v("AutoTotem", "Offhand or hotbar changed before swap hands; restoring the slot");
               this.V.u(var1, this.Z());
               return;
            case g:
               if (this.V.k(var4.method_67532())) {
                  var4.method_61496(this.V.j());
                  q8.v("AutoTotem", "Restored hotbar {}", this.V.j() + 1);
               } else {
                  q8.v("AutoTotem", "Player chose another slot; restore skipped");
               }

               this.V.z();
               this.f();
               break;
            default:
               this.V.z();
               this.f();
         }
      }
   }

   private void p(String var1, boolean var2) {
      int var3 = this.A != null && this.A == mc.field_1724 && this.m == mc.field_1687 ? 1 : 0;
      if (this.L.y()) {
         if (var2 && var3 != 0 && this.L.R() && (this.L.L() > 0 || this.X != null)) {
            this.b();
         }

         q8.v("AutoTotem", "Inventory visit stopped ({}) after {} clicks", var1, this.L.L());
      }

      if (this.V.U() && var3 != 0 && this.V.D() != ga.h.c.X && this.V.k(this.A.method_31548().method_67532())) {
         this.A.method_31548().method_61496(this.V.j());
         q8.v("AutoTotem", "Swap hands route stopped ({}); restored hotbar {}", var1, this.V.j() + 1);
      }

      this.J();
   }

   private void b() {
      class_746 var1 = mc.field_1724;
      if (var1 != null && var1 == this.A && var1.field_7512 == var1.field_7498) {
         if (this.X != null && mc.field_1755 == this.X) {
            var1.method_7346();
         } else if (!(mc.field_1755 instanceof class_490)) {
            var1.field_3944.method_52787(new class_2815(var1.field_7498.field_7763));
         }

         this.X = null;
      }
   }

   private void j() {
      this.L.O();
      this.f();
   }

   private void f() {
      if (!this.L.y() && !this.V.U()) {
         this.J();
      }
   }

   private void J() {
      this.L.O();
      this.V.z();
      this.A = null;
      this.m = null;
      this.X = null;
   }

   private boolean Y(class_437 var1) {
      return var1 == null
         || this.X != null && var1 == this.X
         || this.o.getValue() && var1 instanceof class_490
         || var1 instanceof class_408
         || var1 instanceof class_433
         || var1 instanceof tp;
   }

   private boolean B() {
      return mc.field_1755 == null;
   }

   private int L() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (mc.field_1724.method_31548().method_5438(var2).method_31574(class_1802.field_8288)) {
            var1 |= 1 << var2;
         }
      }

      return var1;
   }

   private boolean N() {
      q586814c6d8f3640b var1 = (q586814c6d8f3640b)mc.field_1724;
      class_10185 var2 = var1.larpLastSentInput();
      return var2 != null
         && ga.L(var1.larpWasSprinting(), var2.comp_3159(), var2.comp_3160(), var2.comp_3161(), var2.comp_3162(), var2.comp_3163(), var2.comp_3164());
   }

   private int Z() {
      return ga.x(this.B.getValue().intValue(), this.J.getValue().intValue(), ThreadLocalRandom.current().nextDouble());
   }

   private boolean F() {
      class_1799 var1 = mc.field_1724.method_6079();
      return ga.k(
         var1.method_31574(class_1802.field_8288),
         var1.method_7960(),
         this.g.G("Always"),
         mc.field_1724.method_6032(),
         mc.field_1724.method_6067(),
         this.a.getValue(),
         this.v.getValue()
      );
   }

   private ga.r F(boolean var1) {
      long var2 = 0L;
      int var4 = 0;

      for (int var5 = 0; var5 < 36; var5++) {
         class_1799 var6 = mc.field_1724.method_31548().method_5438(var5);
         if (var6.method_31574(class_1802.field_8288)) {
            var2 |= 1L << var5;
         } else if (var5 < 9 && var6.method_7960()) {
            var4 |= 1 << var5;
         }
      }

      return ga.K(var2, var4, var1, this.K.G("Hotbar"), this.w.getValue(), this.f.getValue().intValue());
   }

   private boolean U(ga.r var1) {
      class_1735 var2 = mc.field_1724.field_7498.method_7611(var1.menuSlot());
      return var2.method_7677().method_31574(class_1802.field_8288)
         && var2.method_7674(mc.field_1724)
         && var2.method_7680(var1.equips() ? mc.field_1724.method_6079() : class_1799.field_8037)
         && (var1.equips() || mc.field_1724.method_31548().method_5438(var1.button()).method_7960());
   }
}
