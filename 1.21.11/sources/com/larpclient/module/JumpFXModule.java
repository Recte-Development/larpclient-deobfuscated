package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.e6;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.fc;
import com.larpclient.ft;
import com.larpclient.jg;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.ti;
import com.larpclient.uq;
import com.larpclient.yy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2394;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3726;
import net.minecraft.class_638;
import net.minecraft.class_703;
import net.minecraft.class_746;

public final class JumpFXModule extends Module {
   private static JumpFXModule w;
   private final li P = this.addSetting(
      new li(
         "Mode",
         "Terrain outlines, stars and Clarity jump effects",
         "Star of David",
         "Terrain",
         "Star of David",
         "Ripple",
         "Shockwave",
         "Rune",
         "Bloom",
         "Corona",
         "Vortex",
         "Spectrum",
         "Cloud",
         "End Rod",
         "Crit",
         "Flame",
         "Soul Flame",
         "Happy Villager"
      )
   );
   private final li D = this.addSetting(new li("Trigger", "Movement transition", "Land", "Jump", "Land", "Both"));
   private final lo E = this.addSetting(new lo("Maximum Radius", "Blocks", 1.0, 1.0, 8.0, 0.25));
   private final lo u = this.addSetting(new lo("Pulse Duration", "Seconds", 1.0, 0.3, 3.0, 0.05));
   private final lo m = this.addSetting(new lo("Wave Width", "Soft wave band in blocks", 0.9, 0.5, 3.0, 0.05));
   private final lo H = this.addSetting(new lo("Outline Thickness", "World-space stroke width", 0.04, 0.005, 0.08, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final li s = this.addSetting(new li("Color Theme", "Theme gradient, white or custom color", "Custom", "Theme", "White", "Custom"));
   private final l0 A = this.addSetting(new l0("Color", "Custom outline color", -15204097));
   private final lo R = this.addSetting(new lo("Opacity", "Outline opacity", 0.35, 0.05, 1.0, 0.05));
   private final lo x = this.addSetting(new lo("Glow Intensity", "Soft strokes and quality-controlled bloom", 2.0, 0.0, 2.0, 0.05));
   private final l0 t = this.addSetting(new l0("Circle Colour", "Primary circle color", -9452545));
   private final l0 a = this.addSetting(new l0("Circle Colour 2", "Secondary circle color", -3703297));
   private final lo h = this.addSetting(new lo("Circle Size", "Circle scale in blocks", 1.6, 0.5, 6.0, 0.1));
   private final lo S = this.addSetting(new lo("Circle Duration", "Seconds", 0.9, 0.2, 3.0, 0.1));
   private final l3 T = this.addSetting(new l3("Circle Through Walls", "Show circles through terrain", false));
   private final lo p = this.addSetting(new lo("Radius", "Particle ring radius in blocks", 0.6, 0.1, 3.0, 0.1));
   private final lo B = this.addSetting(new lo("Points", "Particles per jump or landing", 16.0, 4.0, 64.0, 1.0));
   private final l3 K = this.addSetting(new l3("Only Sprinting", "Emit Clarity effects only while sprinting", false));
   private final ft.j g = new ft.j();
   private final ArrayList<class_703> L = new ArrayList<>();
   private JumpFXModule.e j;
   private final fc l = new fc();
   private final uq C = new uq();
   private Object d;
   private Object W;

   public JumpFXModule() {
      super("JumpFX", "Show glowing effects on jumps and landings", Category.VISUALS, -1);
      w = this;
      this.A.O(() -> this.V() && this.s.G("Custom"));

      for (lb var2 : List.of(this.E, this.u, this.H, this.s, this.R)) {
         var2.O(this::V);
      }

      for (lb var5 : List.of(this.t, this.a, this.h, this.S, this.T)) {
         var5.O(() -> ft.U(this.P.getValue()) >= 0);
      }

      for (lo var6 : List.of(this.p, this.B)) {
         var6.O(() -> ft.b(this.P.getValue()));
      }

      this.K.O(() -> ft.F(this.P.getValue()));
      this.x.O(() -> this.V() || ft.U(this.P.getValue()) >= 0);
      this.m.O(() -> this.P.G("Terrain"));
      this.P.a(var1 -> {
         this.R();
         q8.G("JumpFX", "Mode changed: {}; active pulses cleared", var1);
      });
   }

   public lb<?> K() {
      return this.P;
   }

   public List<lb<?>> l() {
      return List.of(this.P, this.D, this.K);
   }

   public List<lb<?>> i() {
      return List.of(this.E, this.u, this.m, this.H, this.h, this.S, this.T, this.p, this.B);
   }

   public List<lb<?>> d() {
      return List.of(this.s, this.A, this.R, this.x, this.t, this.a);
   }

   public static JumpFXModule p() {
      return w;
   }

   @Override
   public void onEnable() {
      this.w();
      q8.G("JumpFX", "Enabled; mode={}, trigger={}, bounded world effects and native particles", this.P.getValue(), this.D.getValue());
   }

   @Override
   public void onDisable() {
      this.w();
   }

   private boolean V() {
      return this.P.G("Terrain") || this.P.G("Star of David");
   }

   private void R() {
      this.C.B();
      this.g.k();
      this.j = null;

      for (class_703 var2 : this.L) {
         var2.method_3085();
      }

      this.L.clear();
      jg.d();
   }

   public void w() {
      this.l.g();
      this.R();
      this.d = this.W = null;
   }

   public void I() {
      if (this.isEnabled()) {
         this.j = null;
         int var1 = ft.U(this.P.getValue());
         if (this.isRuntimeActive() && var1 >= 0 && !this.g.D().isEmpty()) {
            this.j = new JumpFXModule.e(
               var1, this.t.getValue(), this.h.getValue(), this.S.getValue(), this.T.getValue(), this.x.getValue().floatValue(), qo.x()
            );
            jg.E(this.a.getValue());
         }
      }
   }

   private boolean h() {
      class_746 var1 = this.getPlayer();
      Module var2 = LarpClient.b().G().j("Fly");
      Module var3 = LarpClient.b().G().j("Freecam");
      return var1 != null
         && var1.method_5805()
         && !var1.method_7325()
         && !var1.method_5799()
         && !var1.method_5771()
         && !var1.method_6101()
         && !var1.method_5765()
         && !var1.method_6128()
         && !var1.method_31549().field_7479
         && (var2 == null || !var2.isOperational())
         && (var3 == null || !var3.isOperational());
   }

   private fc.n Q() {
      class_746 var1 = this.getPlayer();
      if (this.d != getMc().field_1687 || this.W != var1) {
         this.w();
         this.d = getMc().field_1687;
         this.W = var1;
      }

      return new fc.n(this.d, var1, var1.method_73189(), var1.method_24828(), this.h());
   }

   public void i() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.K(this.l.M(this.Q()));
         }
      }
   }

   public void v() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.C.U(qo.K());
            this.g.Y(qo.K(), this.S.getValue());
            this.L.removeIf(var0 -> !var0.method_3086());
            this.K(this.l.r(this.Q()));
         } else {
            this.w();
         }
      }
   }

   private void K(fc.y var1) {
      if (fc.T(this.D.getValue(), var1.event())) {
         if (!this.V()) {
            if (!this.K.getValue() || this.getPlayer().method_5624()) {
               int var7 = ft.U(this.P.getValue());
               if (var7 >= 0) {
                  this.g.A(new ft.m(var1.source(), qo.K(), ThreadLocalRandom.current().nextFloat()));
               } else {
                  class_2394 var3 = ft.y(this.P.getValue());
                  if (var3 == null) {
                     return;
                  }

                  for (class_243 var5 : ft.C(var1.source(), this.p.getValue(), this.B.getValue().intValue())) {
                     if (this.L.size() == 384) {
                        this.L.removeFirst().method_3085();
                     }

                     class_703 var6 = getMc().field_1713.method_3056(var3, var5.field_1352, var5.field_1351, var5.field_1350, 0.0, 0.02, 0.0);
                     if (var6 != null) {
                        this.L.add(var6);
                     }
                  }
               }
            }
         } else {
            uq.f var2 = this.I(var1.source(), qo.K(), this.P.G("Terrain") ? this.N(var1.source().field_1351) : null);
            if (var2 != null) {
               this.C.s(var2);
            }
         }
      }
   }

   public uq.f I(class_243 var1, double var2, e6.o var4) {
      if (this.isEnabled()) {
         if (!this.V()) {
            return null;
         }

         boolean var5 = this.P.G("Star of David");
         List var6 = var5 ? yy.W : e6.E(var1, this.E.getValue(), var4);
         if (var6.isEmpty()) {
            return null;
         }

         int var7 = this.s.G("Theme") ? ez.a : (this.s.G("White") ? -1 : this.A.getValue());
         int var8 = this.s.G("Theme") ? ez.f : var7;
         return new uq.f(
            var1,
            var2,
            this.E.getValue(),
            this.u.getValue(),
            this.m.getValue(),
            this.H.getValue(),
            var7,
            var8,
            this.R.getValue(),
            this.x.getValue(),
            var6,
            var5 ? uq.o.Q : uq.o.R
         );
      } else {
         return null;
      }
   }

   private e6.o N(double var1) {
      class_638 var3 = getMc().field_1687;
      class_3726 var4 = class_3726.method_16195(this.getPlayer());
      return (var4x, var5) -> {
         ArrayList var6 = new ArrayList();
         int var7 = (int)Math.floor(var1);

         for (int var8 = var7 - 3; var8 <= var7 + 3; var8++) {
            class_2338 var9 = new class_2338(var4x, var8, var5);
            if (var3.method_22340(var9)) {
               class_2680 var10 = var3.method_8320(var9);
               if (var10.method_26227().method_15769()) {
                  for (class_238 var12 : var10.method_26194(var3, var9, var4).method_1090()) {
                     var6.add(var12.method_989(var4x, var8, var5));
                     if (var6.size() > 32) {
                        return var6;
                     }
                  }
               }
            }
         }

         return var6;
      };
   }

   public boolean n() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && (this.j != null && this.j.glow() > 0.0F || this.C.e().stream().anyMatch(var0 -> var0.glow() > 0.0));
   }

   public void Y(f1 var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.j != null) {
               JumpFXModule.e var3 = this.j;
               float var4 = var2 ? Math.clamp(var3.glow() * 0.2F, 0.0F, 1.0F) : 1.0F;
               if (var4 > 0.0F) {
                  for (ft.m var6 : this.g.D()) {
                     float var7 = (float)((var3.now() - var6.born()) / var3.duration());
                     if (var7 >= 0.0F && var7 < 1.0F) {
                        var1.f(var6.source(), var3.size(), var7, var6.seed(), var3.primary(), var3.style(), var3.walls(), var4);
                     }
                  }
               }
            }

            var1.blend(ti.d.l);
            double var31 = qo.x();

            for (uq.f var33 : this.C.e()) {
               if (!var2 || !(var33.glow() <= 0.0)) {
                  double var34 = Math.clamp((var31 - var33.born()) / var33.duration(), 0.0, 1.0);
                  int var9 = ei.W(var33.from(), var33.to(), var34);
                  int var10 = var33.style() == uq.o.Q ? 1 : 0;
                  double var11 = var10 != 0 ? uq.c(var33, var31) : 1.0;

                  for (e6.e var14 : var33.edges()) {
                     double var15 = (var10 != 0 ? uq.b(var33, var31) : uq.k(var33, var14.distance(), var31)) * var33.opacity();
                     if (!(var15 < 0.003)) {
                        class_243 var17 = var14.from();
                        class_243 var18 = var14.to();
                        double var19 = var17.field_1352;
                        double var21 = var17.field_1351;
                        double var23 = var17.field_1350;
                        double var25 = var18.field_1352;
                        double var27 = var18.field_1351;
                        double var29 = var18.field_1350;
                        if (var10 != 0) {
                           var19 = yy.A(var33.source().field_1352, var19, var11);
                           var23 = yy.A(var33.source().field_1350, var23, var11);
                           var25 = yy.A(var33.source().field_1352, var25, var11);
                           var29 = yy.A(var33.source().field_1350, var29, var11);
                           var21 = var27 = var33.source().field_1351 + 0.012;
                        }

                        w(var1, var33, var2, var9, var15, var19, var21, var23, var25, var27, var29);
                     }
                  }
               }
            }

            var1.blend(ti.d.G);
         }
      }
   }

   private static void w(
      f1 var0, uq.f var1, boolean var2, int var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16
   ) {
      if (var2) {
         int var18 = ei.R(var3, var4 * var1.glow() * 0.2);
         var0.g(var6, var8, var10, var12, var14, var16, var1.thickness() * 2.0, var1.thickness() * 2.0, var18, var18);
      } else {
         for (int var22 = 0; var22 < 3 && var1.glow() > 0.0; var22++) {
            double var19 = var1.thickness() * (2.0 + var22 * 1.5);
            int var21 = ei.R(var3, var4 * var1.glow() * 0.065 / (var22 + 1));
            var0.g(var6, var8, var10, var12, var14, var16, var19, var19, var21, var21);
         }

         int var23 = ei.R(ei.W(var3, var3 & 0xFF000000 | 16777215, 0.4), var4);
         var0.g(var6, var8, var10, var12, var14, var16, var1.thickness(), var1.thickness(), var23, var23);
      }
   }

   private record e(int style, int primary, double size, double duration, boolean walls, float glow, double now) {
   }
}
