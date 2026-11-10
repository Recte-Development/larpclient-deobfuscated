package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class RealisticWaterModule extends Module {
   private static RealisticWaterModule g;
   public final com.larpclient.module.lo W = this.addSetting(
      new com.larpclient.module.lo("Intensity", "Strength of the water material, percent", 100.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.lo Y = this.addSetting(
      new com.larpclient.module.lo("Waves", "Height of rolling surface waves, percent", 60.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.l3 G = this.addSetting(
      new com.larpclient.module.l3("World reflections", "Reflect visible surroundings; costs GPU time", true)
   );
   public final com.larpclient.module.l3 E = this.addSetting(new com.larpclient.module.l3("Foam", "Shoreline foam and waterfall streaks", true));
   public final com.larpclient.module.l3 V = this.addSetting(new com.larpclient.module.l3("Caustics", "Sunlight patterns on the bed of shallow water", true));
   public final com.larpclient.module.l3 f = this.addSetting(
      new com.larpclient.module.l3("Underwater", "Clear depth color, light shafts and caustics beneath water", true)
   );
   public final com.larpclient.module.l3 M = this.addSetting(
      new com.larpclient.module.l3(
         "Volumetric", "Poured or newly released water becomes persistent droplets; real fluid still controls swimming and drowning", false
      )
   );

   public RealisticWaterModule() {
      super("Realistic Water", "Rolling waves, reflections, refraction, depth color and shoreline foam", Category.RENDER, -1);
      g = this;
      this.M.a(var0 -> {
         ex.v();
         qs.O();
      });
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && qs.k() ? "Material fallback" : "";
   }

   public static RealisticWaterModule C() {
      return g;
   }

   @Override
   public void onEnable() {
      qs.O();
   }

   @Override
   public void onDisable() {
      ex.v();
      qs.O();
      qs.B();
   }

   public static boolean d() {
      if (g != null && g.isEnabled()) {
         RealisticWaterModule var0 = g;
         return var0 != null && var0.isRuntimeActive() && var0.f.getValue() && qs.S();
      } else {
         return false;
      }
   }
}
