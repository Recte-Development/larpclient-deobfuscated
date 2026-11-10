package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.w;
import java.util.Set;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_746;

public final class ElytraMotionModule extends Module {
   private static ElytraMotionModule s;
   private final lo B = this.addSetting(new lo("Distance", "Distance from your eyes to the Kill Aura target", 3.0, 0.1, 6.0, 0.1));
   private final l3 C = this.addSetting(new l3("Swap Chestplate", "Equip a chestplate while holding near the target", true));
   private final l3 M = this.addSetting(new l3("Use Firework", "Use a hotbar firework when returning to flight", true));
   private final l3 f = this.addSetting(new l3("Legit", "Retained reference preference; takeoff uses normal client actions", false));
   private class_746 J;
   private class_746 D;
   private class_638 o;
   private class_638 c;
   private int y = -1;
   private class_1799 X = class_1799.field_8037;
   private class_1799 E = class_1799.field_8037;
   private boolean I;
   private boolean l;
   private final w Y = new w(this);

   public ElytraMotionModule() {
      super("ElytraMotion", "Stay near your Kill Aura target with elytra", Category.MOVEMENT, -1);
      s = this;
      this.M.O(this.C::getValue);
      this.f.O(() -> false);
   }

   public static ElytraMotionModule N() {
      return s;
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.s;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   private boolean J() {
      class_1309 var1 = ElytraHelperModule.d();
      return var1 != null && mc.field_1724.method_33571().method_1022(var1.method_73189()) < this.B.getValue();
   }

   public boolean k() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && !w.o(this)
            && this.z()
            && j.k()
            && !mc.field_1724.method_5765()
            && this.J()
            && (this.y >= 0 && this.U() || mc.field_1724.method_6128());
   }

   @Override
   public void onEnable() {
      this.I = (boolean)0;
      this.l = (boolean)0;
      this.d();
      this.J = mc.field_1724;
      this.o = mc.field_1687;
   }

   private boolean z() {
      return this.isInGame() && mc.field_1724 == this.J && mc.field_1687 == this.o;
   }

   private boolean U() {
      return this.isInGame() && mc.field_1724 == this.D && mc.field_1687 == this.c;
   }

   @Override
   public void onDisable() {
      cr.p(this);
      if (this.E() && this.C()) {
         this.e();
      } else if (this.y >= 0) {
         q8.L("ElytraMotion", "Cleanup kept current equipment: owned swap no longer verifiable");
      }

      this.I = (boolean)0;
      this.l = (boolean)0;
      this.d();
      this.J = null;
      this.o = null;
   }

   @Override
   public void onTick() {
      if (!this.z()) {
         cr.p(this);
         this.I = (boolean)0;
         this.l = (boolean)0;
         this.d();
      } else if (j.k()) {
         boolean var5 = false;
         int var4 = this.y;
         if (Integer.signum(var4) >>> 31 == 0 && !this.E()) {
            this.d();
            this.I = (boolean)0;
            q8.v("ElytraMotion", "Equipment changed externally; released swap ownership");
         }

         boolean var1 = this.k();
         if (var1) {
            mc.field_1724.method_18799(class_243.field_1353);
            if (!this.I) {
               q8.v("ElytraMotion", "Holding near Aura target");
            }

            this.I = (boolean)1;
            if (this.C.getValue()) {
               boolean var7 = false;
               int var6 = this.y;
               if ((Integer.signum(var6) >>> 31 ^ 1) == 0 && mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833) && this.d()) {
                  cr.y(
                     this,
                     this::M,
                     () -> this.k() && this.C.getValue() && this.d() && mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)
                  );
               }
            }
         } else {
            if (this.I) {
               this.I = (boolean)0;
               this.l = (boolean)(this.y >= 0 ? 1 : 0);
               q8.v("ElytraMotion", "Target left hold range; restoring flight");
            }

            boolean var3 = false;
            int var2 = this.y;
            if (Integer.signum(var2) >>> 31 == 0 && this.d()) {
               cr.y(this, this::e, () -> !this.k() && this.E() && this.d());
            } else {
               boolean var9 = false;
               byte var8 = this.l;
               int var10000 = Integer.signum(var8);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.y < 0) {
                  if (!this.h()) {
                     this.l = (boolean)0;
                     return;
                  }

                  cr.y(this, () -> {
                     if (this.h()) {
                        this.Y.X();
                        if (this.M.getValue()) {
                           this.Y.Y(4.0, 0, () -> {});
                        }

                        this.l = (boolean)0;
                        q8.v("ElytraMotion", "Flight restored; firework={}", this.M.getValue());
                     }
                  }, this::h);
               }
            }
         }
      }
   }

   private boolean h() {
      AirStuckModule var1 = AirStuckModule.U();
      return this.z()
         && j.k()
         && cr.f()
         && !mc.field_1724.method_31549().field_7479
         && !mc.field_1724.method_24828()
         && !mc.field_1724.method_5799()
         && !mc.field_1724.method_5771()
         && !mc.field_1724.method_5765()
         && !this.J()
         && mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)
         && (var1 == null || !var1.E());
   }

   private boolean d() {
      return this.C() && cr.f();
   }

   private boolean C() {
      return this.z()
         && mc.field_1761 != null
         && mc.field_1724.method_5805()
         && !mc.field_1724.method_6115()
         && mc.field_1724.field_7512 == mc.field_1724.field_7498
         && mc.field_1724.field_7498.method_34255().method_7960();
   }

   private void M() {
      int var1 = this.R();
      if (var1 >= 0) {
         class_1799 var2 = mc.field_1724.method_6118(class_1304.field_6174).method_7972();
         class_1799 var3 = mc.field_1724.field_7498.method_7611(var1).method_7677().method_7972();
         O(var1);
         if (class_1799.method_31577(mc.field_1724.method_6118(class_1304.field_6174), var3)
            && class_1799.method_31577(mc.field_1724.field_7498.method_7611(var1).method_7677(), var2)) {
            this.y = var1;
            this.X = var2;
            this.E = var3;
            this.D = mc.field_1724;
            this.c = mc.field_1687;
            q8.v("ElytraMotion", "Equipped chestplate from slot {}", var1);
         } else {
            q8.L("ElytraMotion", "Chest swap prediction differed; kept current equipment");
         }
      }
   }

   private int R() {
      class_1792[] var1 = new class_1792[]{
         class_1802.field_22028, class_1802.field_8058, class_1802.field_8523, class_1802.field_8678, class_1802.field_8873, class_1802.field_8577
      };

      for (int var2 = 9; var2 < 45; var2++) {
         for (class_1792 var6 : var1) {
            if (mc.field_1724.field_7498.method_7611(var2).method_7677().method_31574(var6)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private boolean E() {
      return this.y >= 0
         && this.U()
         && c(mc.field_1724.method_6118(class_1304.field_6174), this.E)
         && c(mc.field_1724.field_7498.method_7611(this.y).method_7677(), this.X);
   }

   private static boolean c(class_1799 var0, class_1799 var1) {
      class_1799 var2 = var0.method_7972();
      var2.method_7974(var1.method_7919());
      return class_1799.method_7973(var2, var1);
   }

   private void e() {
      if (this.E() && this.C()) {
         O(this.y);
         if (mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)) {
            q8.v("ElytraMotion", "Restored owned elytra");
            this.d();
         } else {
            q8.L("ElytraMotion", "Elytra restore prediction differed; retaining ownership for retry");
         }
      }
   }

   static void O(int var0) {
      class_310 var1 = getMc();
      int var2 = var1.field_1724.field_7498.field_7763;
      var1.field_1761.method_2906(var2, var0, 0, class_1713.field_7790, var1.field_1724);
      var1.field_1761.method_2906(var2, 6, 0, class_1713.field_7790, var1.field_1724);
      var1.field_1761.method_2906(var2, var0, 0, class_1713.field_7790, var1.field_1724);
   }

   private void d() {
      this.y = -1;
      this.X = class_1799.field_8037;
      this.E = class_1799.field_8037;
      this.D = null;
      this.c = null;
   }

   @Override
   public String getStatus() {
      return this.I ? "Holding" : (this.l ? "Resume" : "");
   }
}
