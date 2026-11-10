package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.w;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1802;
import net.minecraft.class_638;
import net.minecraft.class_746;

public final class ElytraHelperModule extends Module {
   static final int P = com.larpclient.w.Y(180.0);
   static final int p = 10;
   static final int a = 3;
   private final lw q = this.addSetting(new lw("Firework Key", "Hold this key to use hotbar fireworks while gliding", -1));
   private final l3 J = this.addSetting(new l3("Auto Start", "Start gliding when falling with an elytra equipped", true));
   private final l3 I = this.addSetting(new l3("Fast Start", "Use a firework on the first automatic takeoff", true));
   private final l3 w = this.addSetting(new l3("Combat Relaunch", "Jump and relaunch when landing with an elytra equipped", true));
   private final lo n = this.addSetting(new lo("Relaunch Delay", "Delay after the relaunch jump in milliseconds", 175.0, 0.0, 1000.0, 25.0));
   private final l3 b = this.addSetting(new l3("Relaunch Firework", "Use a firework after the relaunch", true));
   private final l3 B = this.addSetting(new l3("Auto Firework", "Use hotbar fireworks when flight speed drops", false));
   private final lo h = this.addSetting(new lo("Delay", "Minimum time between fireworks in milliseconds", 650.0, 100.0, 2000.0, 25.0));
   private final l3 V = this.addSetting(new l3("Only With Target", "Automatic fireworks require the current Kill Aura target", false));
   private final l3 s = this.addSetting(new l3("Only While Gliding", "Automatic fireworks require active gliding", true));
   private final w x = new w(this);
   private class_746 T;
   private class_638 t;
   private int r = -1073741824;
   private int U;
   private int v;
   private int Z;
   private boolean C;
   private boolean c;
   private boolean m;

   public ElytraHelperModule() {
      super("ElytraHelper", "Automate elytra takeoff and fireworks", Category.MOVEMENT, -1);
      this.I.O(this.J::getValue);
      this.n.O(this.w::getValue);
      this.b.O(this.w::getValue);
      this.h.O(this.B::getValue);
      this.V.O(this.B::getValue);
      this.s.O(this.B::getValue);
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onEnable() {
      this.W();
      this.x.n();
      this.T = mc.field_1724;
      this.t = mc.field_1687;
      this.r = mc.field_1724 == null ? -1073741824 : mc.field_1724.field_6012;
   }

   @Override
   public void onDisable() {
      cr.p(this);
      this.W();
      this.x.n();
      this.T = null;
      this.t = null;
   }

   private void W() {
      this.x.L();
      this.v = 0;
      this.C = (boolean)0;
      this.c = (boolean)0;
      this.m = (boolean)0;
   }

   private boolean w() {
      return this.isInGame()
         && mc.field_1724 == this.T
         && mc.field_1687 == this.t
         && j.k()
         && cr.f()
         && !mc.field_1724.method_31549().field_7479
         && !mc.field_1724.method_5765()
         && !mc.field_1724.method_5799()
         && !mc.field_1724.method_5771()
         && mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)
         && !this.K();
   }

   private boolean K() {
      AirStuckModule var1 = AirStuckModule.U();
      ElytraMotionModule var2 = ElytraMotionModule.N();
      return var1 != null && var1.E() || var2 != null && var2.k();
   }

   @Override
   public void onTick() {
      this.x.H();
      if (this.w() && !com.larpclient.w.o(this)) {
         int var1 = mc.field_1724.field_6012;
         if (this.v != 0) {
            this.m(var1);
         } else if (mc.field_1724.method_24828()) {
            this.C = (boolean)0;
            if (this.c) {
               int var13 = var1 - this.U;
               byte var10 = 10;
               int var9 = var13;
               if ((-Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
                  this.c = (boolean)0;
               }
            }

            if (this.w.getValue() && !this.c) {
               this.x.x();
               this.U = var1;
               this.c = (boolean)1;
               q8.v("ElytraHelper", "Relaunch jump through the jump key; waiting {} ticks", com.larpclient.w.Y(this.n.getValue()));
            }
         } else {
            if (this.c && !this.w.getValue()) {
               this.c = (boolean)0;
            }

            if (this.c) {
               int var12 = var1 - this.U;
               int var8 = com.larpclient.w.Y(this.n.getValue());
               int var7 = var12;
               if (Integer.compare(var7, var8) >>> 31 == 0) {
                  this.m(this.b.getValue());
                  this.c = (boolean)0;
                  this.C = (boolean)1;
               }
            } else if (this.J.getValue() && !mc.field_1724.method_6128() && mc.field_1724.field_6017 > 0.0) {
               this.m(this.I.getValue() && !this.C);
               this.C = (boolean)1;
            } else {
               int var2 = mc.field_1724.method_6128() && s5.m(mc.method_22683().method_4490(), this.q.getValue()) ? 1 : 0;
               if (var2 != 0) {
                  int var10000 = var1 - this.r;
                  int var4 = P;
                  int var3 = var10000;
                  if (Integer.compare(var3, var4) >>> 31 == 0) {
                     this.w(() -> this.w() && mc.field_1724.method_6128() && s5.m(mc.method_22683().method_4490(), this.q.getValue()));
                     return;
                  }
               }

               if (this.B.getValue() && this.e()) {
                  int var11 = var1 - this.r;
                  int var6 = com.larpclient.w.Y(this.h.getValue());
                  int var5 = var11;
                  if (Integer.compare(var5, var6) >>> 31 == 0) {
                     this.w(() -> this.w() && this.e());
                  }
               }
            }
         }
      } else {
         cr.p(this);
         this.W();
      }
   }

   private void m(int var1) {
      if (var1 > this.Z) {
         cr.p(this);
         this.W();
         q8.v("ElytraHelper", "Takeoff timed out; released jump input");
      } else {
         if (this.v == 2) {
            if (mc.field_1724.method_24828()) {
               this.W();
               return;
            }

            this.x.x();
            this.v = 3;
         } else {
            boolean var2 = mc.field_1724.method_6128();
            q8.v("ElytraHelper", "Takeoff {}; firework={}", var2 ? "started" : "did not start", this.m && var2);
            if (var2 && this.m) {
               this.w(() -> this.w() && mc.field_1724.method_6128());
            }

            this.v = 0;
            this.m = (boolean)0;
         }
      }
   }

   private void w(BooleanSupplier var1) {
      this.x.q(var1, 4.0, 0, () -> this.r = mc.field_1724.field_6012);
   }

   private void m(boolean var1) {
      this.x.w();
      this.m = var1;
      this.v = 2;
      this.Z = mc.field_1724.field_6012 + 3;
   }

   private boolean e() {
      if (this.s.getValue() && !mc.field_1724.method_6128()) {
         return false;
      }

      class_1309 var1 = d();
      if (this.V.getValue() && var1 == null) {
         return false;
      }

      double var2 = mc.field_1724.method_18798().method_37267() * 20.0;
      return var1 == null ? var2 <= 22.0 : !(mc.field_1724.method_5739(var1) < 5.0F) || !(var2 > var1.method_18798().method_37267() * 20.0 * 0.9);
   }

   static class_1309 d() {
      AuraModule var0 = AuraModule.R();
      class_1309 var1 = var0 == null ? null : var0.B();
      return var1 != null && var1.method_5805() && !var1.method_31481() && var1.method_73183() == getMc().field_1687 ? var1 : null;
   }

   @Override
   public String getStatus() {
      if (this.v != 0) {
         return "Takeoff";
      }

      if (this.c) {
         return "Relaunch";
      }

      class_746 var1 = mc == null ? null : mc.field_1724;
      return var1 != null && var1 == this.T && this.B.getValue() && var1.method_6128() && !this.x.N() ? "No rockets" : "";
   }
}
