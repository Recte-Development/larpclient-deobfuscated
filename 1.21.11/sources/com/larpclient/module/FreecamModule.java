package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q2d3674fef42ad81e;
import com.larp.obf.q4031f93fe72647cb.qe437be3a37000935;
import com.larpclient.ff;
import com.larpclient.hq;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.wp;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_1923;
import net.minecraft.class_241;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_631;
import net.minecraft.class_638;
import net.minecraft.class_746;

public class FreecamModule extends Module {
   private final lo M = this.addSetting(new lo("Speed", "Detached camera speed", 1.0, 0.1, 50.0, 0.1));
   private final lo Y = this.addSetting(new lo("Vertical", "Vertical speed multiplier", 1.0, 0.2, 5.0, 0.05) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final l3 E = this.addSetting(new l3("Smooth", "Use smooth half-speed camera travel", true));
   private final l3 v = this.addSetting(new l3("Body", "Show your body from the detached camera", true));
   private final l3 J = this.addSetting(new l3("Hands", "Show first-person hands while detached", true));
   private final l3 y = this.addSetting(new l3("Keys Move Body", "Also steer your body with movement keys", false));
   private final l3 S = this.addSetting(new l3("Keep Walking", "Carry movement held when Freecam starts", true));
   private final l3 k = this.addSetting(new l3("Scroll Speed", "Adjust camera speed with the mouse wheel", true));
   private final l3 A = this.addSetting(new l3("Reset Scroll Speed", "Keep wheel changes temporary until Freecam ends", true));
   private final lo F = this.addSetting(new lo("Chunks", "Client chunk-view radius, within server-provided terrain", 12.0, 2.0, 32.0, 1.0));
   private final li Q = this.addSetting(new li("Activation", "Toggle the module key or keep it held to use Freecam", "Toggle", "Toggle", "Hold"));
   private final hq l = new hq();
   private static FreecamModule Z;
   private class_746 a;
   private class_638 G;
   private hq.c b;
   private boolean P;
   private boolean O;
   private boolean x;
   private double L;

   @Override
   public Module.p getTickPhase() {
      return Module.p.g;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   public FreecamModule() {
      super("Freecam", "Explore with a detached camera", Category.PLAYER, -1);
      Z = this;
      this.A.O(this.k::getValue);
      this.M.a(var1 -> this.l.Q());
      this.S.a(var1 -> {
         if (!var1) {
            this.l.V();
         }
      });
      this.Q.a(var1 -> {
         if (this.D() || this.x) {
            this.setEnabled(false);
         }
      });
   }

   public boolean D() {
      return this.Q.G("Hold");
   }

   public void k(double var1) {
      this.L = Math.clamp(var1, 1.0, 40.0);
      q8.G("Freecam", "Guided camera speed {} blocks/s; Speed setting stays {}", this.L, this.M.getValue());
   }

   public void k() {
      this.L = 0.0;
   }

   public double R() {
      return this.L;
   }

   private boolean a() {
      return s5.m(getMc().method_22683().method_4490(), this.getKeyBind());
   }

   public boolean I() {
      if (this.isEnabled()) {
         if (this.D() && this.a()) {
            this.x = (boolean)1;
            this.enable();
            return this.isEnabled();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void enable() {
      if (!this.D() || this.x) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.x = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.D()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void H() {
      FreecamModule var0 = Z;
      if (var0 != null && var0.D() && var0.isEnabled()) {
         class_310 var1 = getMc();
         if (var1 == null
            || var1.field_1724 == null
            || var1.field_1687 == null
            || !var1.field_1724.method_5805()
            || var1.field_1755 != null
            || var1.method_18506() != null
            || !var1.method_1569()
            || var0.a != null && (var0.a != var1.field_1724 || var0.G != var1.field_1687)
            || !var0.a()) {
            var0.setEnabled(false);
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.D() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      class_310 var1 = getMc();
      this.a = var1.field_1724;
      this.G = var1.field_1687;
      boolean var2 = ff.C();
      wp.S(wp.z.w);
      this.l.g(var2 ? c() : hq.X(c(), this.a.field_3913.field_54155), this.x(), this.S.getValue());
      this.O(true, var2);
      this.z();
      this.W();
      q8.G(
         "Freecam",
         "Detached camera acquired; activation={}, speed={}, smooth={}, carry={}, body keys={}",
         this.Q.getValue(),
         this.M.getValue(),
         this.E.getValue(),
         this.S.getValue(),
         this.y.getValue()
      );
      q8.v("Freecam", "Terrain visibility starts from loaded columns in view, including across unloaded camera chunks");
   }

   @Override
   public void onDisable() {
      class_310 var1 = getMc();

      try {
         if (this.G != null && this.a != null) {
            class_1923 var2 = this.a.method_31476();
            this.G.method_2935().method_20317(var2.field_9181, var2.field_9180);
            this.G.method_2935().method_20180(this.I());
         }

         if (this.a == var1.field_1724 && this.G == var1.field_1687 && this.a != null && this.a.method_5805()) {
            if (this.P && this.a.method_31549().field_7478 && !this.a.method_31549().field_7479) {
               this.a.method_31549().field_7479 = this.O;
            }

            this.a.field_3913.field_54155 = class_10185.field_54098;
         }
      } finally {
         this.x = (boolean)0;
         this.L = 0.0;
         this.P = (boolean)0;
         this.a = null;
         this.G = null;
         this.b = null;
         this.l.W();
         wp.k(wp.z.w);
         if (var1.field_1687 != null) {
            ((q2d3674fef42ad81e)var1.field_1769).larpOcclusionGraph().method_52817();
         }

         q8.G("Freecam", "Released camera, body input and chunk view");
      }
   }

   @Override
   public void onTick() {
      H();
      if (this.isRuntimeActive()) {
         wp.S(wp.z.w);
         this.O(false, false);
         this.z();
         double var3;
         int var10000 = (var3 = this.L - 0.0) == 0.0 ? 0 : (var3 < 0.0 ? -1 : 1);
         boolean var2 = false;
         int var1 = var10000;
         if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
            wp.A(this.L / 20.0, 1.0, false);
         } else {
            wp.A(this.l.U(this.M.getValue(), this.A.getValue()), this.Y.getValue(), this.E.getValue());
         }

         this.W();
      }
   }

   public static boolean r() {
      return Z != null && Z.isEnabled() ? wp.d() : false;
   }

   public static boolean z() {
      return Z != null && Z.isEnabled() ? Z != null && Z.v.getValue() : false;
   }

   public static boolean q() {
      return Z != null && Z.isEnabled() ? Z != null && Z.J.getValue() : false;
   }

   public static class_10185 i(class_10185 var0) {
      if (Z != null && Z.isEnabled()) {
         FreecamModule var1 = Z;
         class_310 var2 = class_310.method_1551();
         return var1 != null && var2.field_1755 == null && var2.method_1569() ? var1.l.V(var0, var1.y.getValue()) : class_10185.field_54098;
      } else {
         return var0;
      }
   }

   public static class_10185 c() {
      class_315 var0 = class_310.method_1551().field_1690;
      return new class_10185(
         var0.field_1894.method_1434(),
         var0.field_1881.method_1434(),
         var0.field_1913.method_1434(),
         var0.field_1849.method_1434(),
         var0.field_1903.method_1434(),
         var0.field_1832.method_1434(),
         var0.field_1867.method_1434()
      );
   }

   public static class_10185 b() {
      FreecamModule var0 = Z;
      class_310 var1 = class_310.method_1551();
      return var0 != null && var1.field_1755 == null && var1.method_1569() ? var0.l.i(c(), var0.y.getValue()) : class_10185.field_54098;
   }

   private boolean x() {
      return this.a.method_5715() && !ScaffoldModule.Q(this.a);
   }

   private void O(boolean var1, boolean var2) {
      if (this.a != null) {
         float var3 = ScaffoldModule.f();
         int var4 = !var2 && Float.isNaN(var3) ? 0 : 1;
         this.l
            .Q(
               var4 != 0 ? c() : hq.X(c(), this.a.field_3913.field_54155),
               this.S.getValue(),
               this.y.getValue(),
               this.x(),
               var4 != 0 ? class_241.field_1340 : new class_241(this.a.field_6212, this.a.field_6250),
               this.a.method_18798(),
               Float.isNaN(var3) ? this.a.method_36454() : var3,
               var1
            );
      }
   }

   private void z() {
      if (this.a != null && this.a.method_31549().field_7478) {
         if (!this.P) {
            this.O = this.a.method_31549().field_7479;
            this.P = (boolean)1;
         }

         this.a.method_31549().field_7479 = (boolean)0;
      }
   }

   public static void b() {
      if (Z != null) {
         Z.P = (boolean)0;
      }
   }

   private void W() {
      if (this.G != null && this.a != null) {
         class_1923 var1 = this.a.method_31476();
         hq.c var2 = hq.B(wp.W(1.0F), var1.field_9181, var1.field_9180, this.F.getValue().intValue(), this.I());
         class_631 var3 = this.G.method_2935();
         var3.method_20317(var2.x(), var2.z());
         var3.method_20180(var2.radius());
         if (!var2.equals(this.b)) {
            ((q2d3674fef42ad81e)getMc().field_1769).larpOcclusionGraph().method_52817();
            this.b = var2;
         }
      }
   }

   private int I() {
      return this.a == null ? getMc().field_1690.method_38521() : ((qe437be3a37000935)this.a.field_3944).larpServerChunkRadius();
   }

   public static boolean f(double var0) {
      if (Z != null && Z.isEnabled()) {
         FreecamModule var2 = Z;
         class_310 var3 = class_310.method_1551();
         if (var2 == null || !wp.d() || !var2.k.getValue() && var2.L <= 0.0 || var3.field_1755 != null || var3.method_18506() != null || !var3.method_1569()) {
            return false;
         }

         if (var2.L > 0.0) {
            if (Double.isFinite(var0) && var0 != 0.0) {
               var2.L = hq.W(var2.L, var0);
               q8.v("Freecam", "Wheel guided camera speed={} blocks/s", var2.L);
               return true;
            } else {
               return false;
            }
         } else {
            if (!var2.l.m(var0, var2.M.getValue(), var2.A.getValue())) {
               return false;
            }

            if (!var2.A.getValue()) {
               var2.M.q(var2.M.getValue() * (var0 > 0.0 ? 1.2 : 0.8333333333333334));
            }

            q8.v("Freecam", "Wheel camera speed={}", var2.l.U(var2.M.getValue(), var2.A.getValue()));
            return true;
         }
      } else {
         return false;
      }
   }
}
