package com.larpclient.module;

import com.larpclient.ma;
import com.larpclient.mt;
import java.util.List;
import java.util.Locale;

public final class FlameHandsModule extends Module {
   private static FlameHandsModule C;
   public final d6 R = this.addSetting(new d6("Hands", "Hands that emit flames", "Both", "Both", "Main Only", "Off Only"));
   public final d6 Z = this.addSetting(new d6("Render Mode", "Reference particle texture", "Bloom", "Bloom", "Glow"));
   public final d6 F = this.addSetting(new d6("Color Mode", "Particle color animation", "Static", "Static", "Gradient", "Rainbow"));
   public final dk I = this.addSetting(new dk("Primary Color", "Color at emission", -45036));
   public final dk p = this.addSetting(new dk("Secondary Color", "Gradient destination", -14336));
   public final d9 P = this.addSetting(R("Size", "Particle diameter", 0.25, 0.05, 0.8, 0.01));
   public final d9 Y = this.addSetting(R("Density", "Particles emitted per second per hand", 100.0, 10.0, 300.0, 5.0));
   public final d9 B = this.addSetting(R("Lifetime", "Particle life in seconds", 0.35, 0.1, 2.0, 0.05));
   public final d9 U = this.addSetting(R("Buoyancy", "Upward acceleration", 0.5, 0.0, 2.0, 0.05));
   public final d9 W = this.addSetting(R("Turbulence", "Random swirling motion", 0.3, 0.0, 1.0, 0.05));
   public final d9 e = this.addSetting(R("Inertia Lag", "Response to camera and player movement", 0.5, 0.0, 1.0, 0.05));
   public final dh T = this.addSetting(new dh("Only When Holding", "Emit only from hands holding an item", false));

   private static d9 R(final String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new d9(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getDisplayName() {
            return var0.equals("Inertia Lag") ? "Inertia/Lag" : var0;
         }

         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : "%.2f", this.P());
         }
      };
   }

   public FlameHandsModule() {
      super("Flame Hands", "Animated flames attached to first-person hands", Category.VISUALS, -1);
      C = this;
      this.I.H(() -> !this.F.S("Rainbow"));
      this.p.H(() -> this.F.S("Gradient"));
      this.R.h(var0 -> ma.B());
      this.T.h(var0 -> ma.B());
   }

   public static FlameHandsModule R() {
      return C;
   }

   public List<df<?>> f() {
      return List.of(this.R, this.T, this.Z, this.P, this.Y, this.B);
   }

   public List<df<?>> I() {
      return List.of(this.F, this.I, this.p);
   }

   public List<df<?>> O() {
      return List.of(this.U, this.W, this.e);
   }

   public boolean j(boolean var1, boolean var2) {
      return !this.isEnabled() ? false : (!this.T.P() || var2) && (!this.R.S("Main Only") || var1) && (!this.R.S("Off Only") || !var1);
   }

   public ma.d e() {
      return new ma.d(
         this.P.P().floatValue(),
         this.Y.P().floatValue(),
         this.B.P().floatValue(),
         this.U.P().floatValue(),
         this.W.P().floatValue(),
         this.e.P().floatValue(),
         this.I.P(),
         this.p.P(),
         this.F.P()
      );
   }

   @Override
   public void onEnable() {
      ma.B();
      mt.U("FlameHands", "Activated hand-attached reference particles");
   }

   @Override
   public void onDisable() {
      ma.B();
      mt.U("FlameHands", "Cleared both hand particle histories");
   }

   @Override
   public void onTick() {
      ma.a();
   }
}
