package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.oj;
import java.util.List;
import java.util.Locale;

public final class FlameHandsModule extends Module {
   private static FlameHandsModule z;
   public final o u = this.addSetting(new o("Hands", "Hands that emit flames", "Both", "Both", "Main Only", "Off Only"));
   public final o a = this.addSetting(new o("Render Mode", "Reference particle texture", "Bloom", "Bloom", "Glow"));
   public final o Z = this.addSetting(new o("Color Mode", "Particle color animation", "Static", "Static", "Gradient", "Rainbow"));
   public final h F = this.addSetting(new h("Primary Color", "Color at emission", -45036));
   public final h B = this.addSetting(new h("Secondary Color", "Gradient destination", -14336));
   public final b T = this.addSetting(a("Size", "Particle diameter", 0.25, 0.05, 0.8, 0.01));
   public final b o = this.addSetting(a("Density", "Particles emitted per second per hand", 100.0, 10.0, 300.0, 5.0));
   public final b d = this.addSetting(a("Lifetime", "Particle life in seconds", 0.35, 0.1, 2.0, 0.05));
   public final b w = this.addSetting(a("Buoyancy", "Upward acceleration", 0.5, 0.0, 2.0, 0.05));
   public final b m = this.addSetting(a("Turbulence", "Random swirling motion", 0.3, 0.0, 1.0, 0.05));
   public final b s = this.addSetting(a("Inertia Lag", "Response to camera and player movement", 0.5, 0.0, 1.0, 0.05));
   public final m C = this.addSetting(new m("Only When Holding", "Emit only from hands holding an item", false));

   private static b a(final String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new b(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getDisplayName() {
            return var0.equals("Inertia Lag") ? "Inertia/Lag" : var0;
         }

         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : "%.2f", this.getValue());
         }
      };
   }

   public FlameHandsModule() {
      super("Flame Hands", "Animated flames attached to first-person hands", Category.VISUALS, -1);
      z = this;
      this.F.a(() -> !this.Z.w("Rainbow"));
      this.B.a(() -> this.Z.w("Gradient"));
      this.u.w(var0 -> oj.G());
      this.C.w(var0 -> oj.G());
   }

   public static FlameHandsModule w() {
      return z;
   }

   public List<r<?>> u() {
      return List.of(this.u, this.C, this.a, this.T, this.o, this.d);
   }

   public List<r<?>> i() {
      return List.of(this.Z, this.F, this.B);
   }

   public List<r<?>> X() {
      return List.of(this.w, this.m, this.s);
   }

   public boolean L(boolean var1, boolean var2) {
      return !this.isEnabled() ? false : (!this.C.getValue() || var2) && (!this.u.w("Main Only") || var1) && (!this.u.w("Off Only") || !var1);
   }

   public oj.q b() {
      return new oj.q(
         this.T.getValue().floatValue(),
         this.o.getValue().floatValue(),
         this.d.getValue().floatValue(),
         this.w.getValue().floatValue(),
         this.m.getValue().floatValue(),
         this.s.getValue().floatValue(),
         this.F.getValue(),
         this.B.getValue(),
         this.Z.getValue()
      );
   }

   @Override
   public void onEnable() {
      oj.G();
      _q.c("FlameHands", "Activated hand-attached reference particles");
   }

   @Override
   public void onDisable() {
      oj.G();
      _q.c("FlameHands", "Cleared both hand particle histories");
   }

   @Override
   public void onTick() {
      oj.B();
   }
}
