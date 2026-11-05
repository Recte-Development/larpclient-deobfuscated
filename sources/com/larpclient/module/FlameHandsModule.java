package com.larpclient.module;

import com.larpclient.m7;
import com.larpclient.r4;
import java.util.List;
import java.util.Locale;

public final class FlameHandsModule extends Module {
   private static FlameHandsModule j;
   public final lp n = this.addSetting(new lp("Hands", "Hands that emit flames", "Both", "Both", "Main Only", "Off Only"));
   public final lp z = this.addSetting(new lp("Render Mode", "Reference particle texture", "Bloom", "Bloom", "Glow"));
   public final lp t = this.addSetting(new lp("Color Mode", "Particle color animation", "Static", "Static", "Gradient", "Rainbow"));
   public final lv Z = this.addSetting(new lv("Primary Color", "Color at emission", -45036));
   public final lv g = this.addSetting(new lv("Secondary Color", "Gradient destination", -14336));
   public final lc C = this.addSetting(U("Size", "Particle diameter", 0.25, 0.05, 0.8, 0.01));
   public final lc W = this.addSetting(U("Density", "Particles emitted per second per hand", 100.0, 10.0, 300.0, 5.0));
   public final lc R = this.addSetting(U("Lifetime", "Particle life in seconds", 0.35, 0.1, 2.0, 0.05));
   public final lc O = this.addSetting(U("Buoyancy", "Upward acceleration", 0.5, 0.0, 2.0, 0.05));
   public final lc M = this.addSetting(U("Turbulence", "Random swirling motion", 0.3, 0.0, 1.0, 0.05));
   public final lc v = this.addSetting(U("Inertia Lag", "Response to camera and player movement", 0.5, 0.0, 1.0, 0.05));
   public final ln b = this.addSetting(new ln("Only When Holding", "Emit only from hands holding an item", false));

   private static lc U(final String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new lc(var0, var1, var2, var4, var6, var8) {
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
      j = this;
      this.Z.x(() -> !this.t.i("Rainbow"));
      this.g.x(() -> this.t.i("Gradient"));
      this.n.W(var0 -> m7.V());
      this.b.W(var0 -> m7.V());
   }

   public static FlameHandsModule Z() {
      return j;
   }

   public List<l6<?>> g() {
      return List.of(this.n, this.b, this.z, this.C, this.W, this.R);
   }

   public List<l6<?>> C() {
      return List.of(this.t, this.Z, this.g);
   }

   public List<l6<?>> j() {
      return List.of(this.O, this.M, this.v);
   }

   public boolean R(boolean var1, boolean var2) {
      return !this.isEnabled() ? false : (!this.b.getValue() || var2) && (!this.n.i("Main Only") || var1) && (!this.n.i("Off Only") || !var1);
   }

   public m7.n J() {
      return new m7.n(
         this.C.getValue().floatValue(),
         this.W.getValue().floatValue(),
         this.R.getValue().floatValue(),
         this.O.getValue().floatValue(),
         this.M.getValue().floatValue(),
         this.v.getValue().floatValue(),
         this.Z.getValue(),
         this.g.getValue(),
         this.t.getValue()
      );
   }

   @Override
   public void onEnable() {
      m7.V();
      r4.g("FlameHands", "Activated hand-attached reference particles");
   }

   @Override
   public void onDisable() {
      m7.V();
      r4.g("FlameHands", "Cleared both hand particle histories");
   }

   @Override
   public void onTick() {
      m7.g();
   }
}
