package com.larpclient.module;

import com.larpclient.my;
import java.util.List;
import java.util.Locale;

public final class FlameHandsModule extends Module {
   private static FlameHandsModule R;
   public final l K = this.addSetting(new l("Hands", "Hands that emit flames", "Both", "Both", "Main Only", "Off Only"));
   public final l P = this.addSetting(new l("Render Mode", "Reference particle texture", "Bloom", "Bloom", "Glow"));
   public final l O = this.addSetting(new l("Color Mode", "Particle color animation", "Static", "Static", "Gradient", "Rainbow"));
   public final i E = this.addSetting(new i("Primary Color", "Color at emission", -45036));
   public final i j = this.addSetting(new i("Secondary Color", "Gradient destination", -14336));
   public final e F = this.addSetting(P("Size", "Particle diameter", 0.25, 0.05, 0.8, 0.01));
   public final e A = this.addSetting(P("Density", "Particles emitted per second per hand", 100.0, 10.0, 300.0, 5.0));
   public final e s = this.addSetting(P("Lifetime", "Particle life in seconds", 0.35, 0.1, 2.0, 0.05));
   public final e Z = this.addSetting(P("Buoyancy", "Upward acceleration", 0.5, 0.0, 2.0, 0.05));
   public final e n = this.addSetting(P("Turbulence", "Random swirling motion", 0.3, 0.0, 1.0, 0.05));
   public final e p = this.addSetting(P("Inertia Lag", "Response to camera and player movement", 0.5, 0.0, 1.0, 0.05));
   public final s k = this.addSetting(new s("Only When Holding", "Emit only from hands holding an item", false));

   private static e P(final String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new e(var0, var1, var2, var4, var6, var8) {
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
      R = this;
      this.E.P(() -> !this.O.O("Rainbow"));
      this.j.P(() -> this.O.O("Gradient"));
      this.K.o(var0 -> my.K());
      this.k.o(var0 -> my.K());
   }

   public static FlameHandsModule U() {
      return R;
   }

   public List<q<?>> S() {
      return List.of(this.K, this.k, this.P, this.F, this.A, this.s);
   }

   public List<q<?>> A() {
      return List.of(this.O, this.E, this.j);
   }

   public List<q<?>> M() {
      return List.of(this.Z, this.n, this.p);
   }

   public boolean g(boolean var1, boolean var2) {
      return !this.isEnabled() ? false : (!this.k.getValue() || var2) && (!this.K.O("Main Only") || var1) && (!this.K.O("Off Only") || !var1);
   }

   public my.j M() {
      return new my.j(
         this.F.getValue().floatValue(),
         this.A.getValue().floatValue(),
         this.s.getValue().floatValue(),
         this.Z.getValue().floatValue(),
         this.n.getValue().floatValue(),
         this.p.getValue().floatValue(),
         this.E.getValue(),
         this.j.getValue(),
         this.O.getValue()
      );
   }

   @Override
   public void onEnable() {
      my.K();
      com.larpclient.h.U("FlameHands", "Activated hand-attached reference particles");
   }

   @Override
   public void onDisable() {
      my.K();
      com.larpclient.h.U("FlameHands", "Cleared both hand particle histories");
   }

   @Override
   public void onTick() {
      my.A();
   }
}
