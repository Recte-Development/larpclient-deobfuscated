package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.sg;
import java.util.List;
import java.util.Locale;

public final class FlameHandsModule extends Module {
   private static FlameHandsModule i;
   public final li r = this.addSetting(new li("Hands", "Hands that emit flames", "Both", "Both", "Main Only", "Off Only"));
   public final li T = this.addSetting(new li("Render Mode", "Reference particle texture", "Bloom", "Bloom", "Glow"));
   public final li A = this.addSetting(new li("Color Mode", "Particle color animation", "Static", "Static", "Gradient", "Rainbow"));
   public final l0 I = this.addSetting(new l0("Primary Color", "Color at emission", -45036));
   public final l0 m = this.addSetting(new l0("Secondary Color", "Gradient destination", -14336));
   public final lo E = this.addSetting(A("Size", "Particle diameter", 0.25, 0.05, 0.8, 0.01));
   public final lo P = this.addSetting(A("Density", "Particles emitted per second per hand", 100.0, 10.0, 300.0, 5.0));
   public final lo g = this.addSetting(A("Lifetime", "Particle life in seconds", 0.35, 0.1, 2.0, 0.05));
   public final lo L = this.addSetting(A("Buoyancy", "Upward acceleration", 0.5, 0.0, 2.0, 0.05));
   public final lo H = this.addSetting(A("Turbulence", "Random swirling motion", 0.3, 0.0, 1.0, 0.05));
   public final lo D = this.addSetting(A("Inertia Lag", "Response to camera and player movement", 0.5, 0.0, 1.0, 0.05));
   public final l3 C = this.addSetting(new l3("Only When Holding", "Emit only from hands holding an item", false));

   private static lo A(final String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new lo(var0, var1, var2, var4, var6, var8) {
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
      i = this;
      this.I.O(() -> !this.A.G("Rainbow"));
      this.m.O(() -> this.A.G("Gradient"));
      this.r.a(var0 -> sg.E());
      this.C.a(var0 -> sg.E());
   }

   public static FlameHandsModule L() {
      return i;
   }

   public List<lb<?>> F() {
      return List.of(this.r, this.C, this.T, this.E, this.P, this.g);
   }

   public List<lb<?>> n() {
      return List.of(this.A, this.I, this.m);
   }

   public List<lb<?>> R() {
      return List.of(this.L, this.H, this.D);
   }

   public boolean K(boolean var1, boolean var2) {
      return !this.isEnabled() ? false : (!this.C.getValue() || var2) && (!this.r.G("Main Only") || var1) && (!this.r.G("Off Only") || !var1);
   }

   public sg.w j() {
      return new sg.w(
         this.E.getValue().floatValue(),
         this.P.getValue().floatValue(),
         this.g.getValue().floatValue(),
         this.L.getValue().floatValue(),
         this.H.getValue().floatValue(),
         this.D.getValue().floatValue(),
         this.I.getValue(),
         this.m.getValue(),
         this.A.getValue()
      );
   }

   @Override
   public void onEnable() {
      sg.E();
      q8.v("FlameHands", "Activated hand-attached reference particles");
   }

   @Override
   public void onDisable() {
      sg.E();
      q8.v("FlameHands", "Cleared both hand particle histories");
   }

   @Override
   public void onTick() {
      sg.N();
   }
}
