package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.FlameHandsRenderer;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import java.util.Locale;

public final class FlameHandsModule extends Module {
   private static FlameHandsModule instance;
   public final ModeSetting hands = this.addSetting(new ModeSetting("Hands", "Hands that emit flames", "Both", "Both", "Main Only", "Off Only"));
   public final ModeSetting renderMode = this.addSetting(new ModeSetting("Render Mode", "Reference particle texture", "Bloom", "Bloom", "Glow"));
   public final ModeSetting colorMode = this.addSetting(new ModeSetting("Color Mode", "Particle color animation", "Static", "Static", "Gradient", "Rainbow"));
   public final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Color at emission", -45036));
   public final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Gradient destination", -14336));
   public final NumberSetting size = this.addSetting(createNumberSetting("Size", "Particle diameter", 0.25, 0.05, 0.8, 0.01));
   public final NumberSetting density = this.addSetting(createNumberSetting("Density", "Particles emitted per second per hand", 100.0, 10.0, 300.0, 5.0));
   public final NumberSetting lifetime = this.addSetting(createNumberSetting("Lifetime", "Particle life in seconds", 0.35, 0.1, 2.0, 0.05));
   public final NumberSetting buoyancy = this.addSetting(createNumberSetting("Buoyancy", "Upward acceleration", 0.5, 0.0, 2.0, 0.05));
   public final NumberSetting turbulence = this.addSetting(createNumberSetting("Turbulence", "Random swirling motion", 0.3, 0.0, 1.0, 0.05));
   public final NumberSetting inertiaLag = this.addSetting(createNumberSetting("Inertia Lag", "Response to camera and player movement", 0.5, 0.0, 1.0, 0.05));
   public final BooleanSetting onlyWhenHolding = this.addSetting(new BooleanSetting("Only When Holding", "Emit only from hands holding an item", false));

   private static NumberSetting createNumberSetting(final String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
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
      instance = this;
      this.primaryColor.visibleWhen(() -> !this.colorMode.isMode("Rainbow"));
      this.secondaryColor.visibleWhen(() -> this.colorMode.isMode("Gradient"));
      this.hands.onChange(var0 -> FlameHandsRenderer.reset());
      this.onlyWhenHolding.onChange(var0 -> FlameHandsRenderer.reset());
   }

   public static FlameHandsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getEmissionSettings() {
      return List.of(this.hands, this.onlyWhenHolding, this.renderMode, this.size, this.density, this.lifetime);
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.colorMode, this.primaryColor, this.secondaryColor);
   }

   public List<Setting<?>> getMotionSettings() {
      return List.of(this.buoyancy, this.turbulence, this.inertiaLag);
   }

   public boolean shouldEmit(boolean var1, boolean var2) {
      return !this.isEnabled()
         ? false
         : (!this.onlyWhenHolding.getValue() || var2) && (!this.hands.isMode("Main Only") || var1) && (!this.hands.isMode("Off Only") || !var1);
   }

   public FlameHandsRenderer.Settings createRendererSettings() {
      return new FlameHandsRenderer.Settings(
         this.size.getValue().floatValue(),
         this.density.getValue().floatValue(),
         this.lifetime.getValue().floatValue(),
         this.buoyancy.getValue().floatValue(),
         this.turbulence.getValue().floatValue(),
         this.inertiaLag.getValue().floatValue(),
         this.primaryColor.getValue(),
         this.secondaryColor.getValue(),
         this.colorMode.getValue()
      );
   }

   @Override
   public void onEnable() {
      FlameHandsRenderer.reset();
      LarpLog.debug("FlameHands", "Activated hand-attached reference particles");
   }

   @Override
   public void onDisable() {
      FlameHandsRenderer.reset();
      LarpLog.debug("FlameHands", "Cleared both hand particle histories");
   }

   @Override
   public void onTick() {
      FlameHandsRenderer.syncState();
   }
}
