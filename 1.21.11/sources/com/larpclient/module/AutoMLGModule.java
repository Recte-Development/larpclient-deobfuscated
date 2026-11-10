package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larp.obf.q4031f93fe72647cb.qf65e76ba6afee893;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.fs;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.w6;
import com.larpclient.wp;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_12206;
import net.minecraft.class_1268;
import net.minecraft.class_1656;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class AutoMLGModule extends Module {
   public static final float I = 89.5F;
   public static final double D = -0.6;
   public static final int U = 3;
   public static final int X = 40;
   private final lo J = this.addSetting(new lo("Fall Distance", "Blocks fallen before the water bucket is readied", 8.0, 3.0, 40.0, 1.0));
   private final l3 E = this.addSetting(new l3("Pick Up", "Pick the water back up after landing", true));
   private final fs d = new fs(this);
   private AutoMLGModule.g r = AutoMLGModule.g.w;
   private class_746 T;
   private class_638 g;
   private float A;
   private int K;
   private String N = "Ready";

   public AutoMLGModule() {
      super("Auto MLG", "Place water below you before a long fall lands", Category.MOVEMENT, -1);
   }

   public static boolean W(boolean var0, boolean var1, double var2, double var4, double var6) {
      return !var0 && !var1 && var2 >= var6 && var4 < -0.6;
   }

   public static boolean I(boolean var0, boolean var1, int var2) {
      return (var0 || var1) && var2 >= 3;
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
      return this.isEnabled() ? this.N : "";
   }

   @Override
   public void onEnable() {
      this.u();
      this.T = mc.field_1724;
      this.g = mc.field_1687;
      this.N = "Ready";
      q8.G("AutoMLG", "Enabled; fall distance {} blocks, pick up={}", this.J.getValue().intValue(), this.E.getValue());
   }

   @Override
   public void onDisable() {
      cr.p(this);
      if (this.r != AutoMLGModule.g.w && this.T != null && this.T == getMc().field_1724) {
         this.d.o(mc, true);
         this.T.method_36457(this.A);
      }

      this.u();
      this.T = null;
      this.g = null;
   }

   private void u() {
      cr.p(this);
      this.r = AutoMLGModule.g.w;
      this.K = 0;
   }

   private boolean l() {
      wp.z var1 = wp.o();
      return !cr.f() || mc.field_1755 != null || var1 != wp.z.D;
   }

   @Override
   public void onTick() {
      if (this.T != mc.field_1724 || this.g != mc.field_1687) {
         this.d.o(getMc(), false);
         this.u();
         this.T = mc.field_1724;
         this.g = mc.field_1687;
      }

      int var10000 = this.l();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         if (this.r != AutoMLGModule.g.w) {
            this.N = "Paused";
         }
      } else {
         switch (this.r) {
            case w:
               this.l();
               break;
            case B:
               this.B();
               break;
            case X:
               this.M();
               break;
            case t:
               this.R();
         }
      }
   }

   private void l() {
      class_1656 var1 = this.T.method_31549();
      if (!var1.field_7479
         && !var1.field_7480
         && !this.T.method_6128()
         && !this.T.method_5765()
         && W(this.T.method_24828(), this.T.method_5799(), this.T.field_6017, this.T.method_18798().field_1351, this.J.getValue())) {
         if (fk.T(class_1802.field_8705, true) == -1) {
            this.N = "No water bucket in hotbar or hands";
         } else if ((Boolean)this.g.method_75728().method_75697(class_12206.field_63755, this.T.method_24515())) {
            this.N = "Water evaporates here";
         } else {
            this.A = this.T.method_36455();
            this.K = 0;
            this.r = AutoMLGModule.g.B;
            this.N = "Falling";
            this.O(class_1802.field_8705);
            q8.v(
               "AutoMLG",
               "Armed after {} blocks at {} blocks per tick",
               String.format(Locale.ROOT, "%.1f", this.T.field_6017),
               String.format(Locale.ROOT, "%.2f", this.T.method_18798().field_1351)
            );
            this.B();
         }
      }
   }

   private void B() {
      if (!this.T.method_24828() && !this.T.method_5799() && !(this.T.field_6017 <= 0.0)) {
         this.g();
         this.N = "Falling";
         if (this.N(this.T.method_36454(), 89.5F) != null) {
            cr.y(this, this::f, this::J);
         }
      } else {
         this.r = AutoMLGModule.g.t;
         this.R();
      }
   }

   private void M() {
      this.g();
      this.K++;
      if (this.K > 40) {
         this.N = "Pick up timed out";
         this.r = AutoMLGModule.g.t;
      } else if (I(this.T.method_24828(), this.T.method_5799(), this.K)) {
         cr.y(this, this::k, () -> this.r == AutoMLGModule.g.X && this.F(class_1802.field_8550));
      }
   }

   private void R() {
      int var1 = this.T.method_31548().method_67532();
      this.d.o(mc, true);
      if (mc.field_1761 != null && var1 != this.T.method_31548().method_67532()) {
         ((q226fb0b431f37244)mc.field_1761).larpSyncCarriedItem();
      }

      t9.O(this, t9.v.x, this.T.method_36454(), this.A, 1.0);
      this.u();
      this.N = "Ready";
   }

   private void g() {
      t9.O(this, t9.v.x, this.T.method_36454(), 89.5F, 1.0);
   }

   private class_243 N(float var1, float var2) {
      class_243 var3 = this.T.method_33571();
      class_243 var4 = var3.method_1019(class_243.method_1030(var2, var1).method_1021(this.T.method_55754()));
      class_3965 var5 = this.g.method_17742(new class_3959(var3, var4, class_3960.field_17559, class_242.field_1348, this.T));
      return var5.method_17783() == class_240.field_1332 ? var5.method_17784() : null;
   }

   private boolean J() {
      return this.r == AutoMLGModule.g.B
         && !this.T.method_24828()
         && !this.T.method_5799()
         && Math.abs(this.T.method_36455() - 89.5F) < 0.5F
         && !this.T.method_6115()
         && this.F(class_1802.field_8705)
         && this.N(this.T.method_36454(), this.T.method_36455()) != null;
   }

   private boolean F(class_1792 var1) {
      int var2 = fk.T(var1, true);
      return var2 == -2 || var2 >= 0 && (var2 == this.T.method_31548().method_67532() || fs.o(this));
   }

   private class_1268 O(class_1792 var1) {
      int var2 = fk.T(var1, true);
      if (var2 == -2) {
         return class_1268.field_5810;
      } else if (var2 < 0) {
         return null;
      } else {
         return var2 != this.T.method_31548().method_67532() && !this.d.m(this.T, var2) ? null : class_1268.field_5808;
      }
   }

   private boolean Y(class_1792 var1) {
      class_1268 var2 = this.O(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = mc.field_1761.method_2919(this.T, var2).method_23665();
      if (var3) {
         w6.H(this.T, var2);
         ((qf65e76ba6afee893)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void f() {
      if (this.J()) {
         if (!this.Y(class_1802.field_8705)) {
            this.N = "Water placement rejected";
         } else {
            this.r = this.E.getValue() ? AutoMLGModule.g.X : AutoMLGModule.g.t;
            this.K = 0;
            this.N = "Water placed";
            q8.v("AutoMLG", "Water placed after {} blocks", String.format(Locale.ROOT, "%.1f", this.T.field_6017));
         }
      }
   }

   private void k() {
      boolean var1 = this.Y(class_1802.field_8550);
      this.N = var1 ? "Water picked up" : "Pick up rejected";
      this.r = AutoMLGModule.g.t;
      q8.v("AutoMLG", "Pick up {}", var1 ? "requested" : "rejected locally");
   }

   public enum g {
      w,
      B,
      X,
      t;
   }
}
