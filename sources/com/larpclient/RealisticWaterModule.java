package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class RealisticWaterModule extends Module {
   private static RealisticWaterModule j;
   public final com.larpclient.module.lc h = this.addSetting(
      new com.larpclient.module.lc("Intensity", "Strength of the water material, percent", 100.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.lc q = this.addSetting(
      new com.larpclient.module.lc("Waves", "Height of rolling surface waves, percent", 60.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.ln F = this.addSetting(
      new com.larpclient.module.ln("World reflections", "Reflect visible surroundings; costs GPU time", true)
   );
   public final com.larpclient.module.ln i = this.addSetting(new com.larpclient.module.ln("Foam", "Shoreline foam and waterfall streaks", true));
   public final com.larpclient.module.ln D = this.addSetting(new com.larpclient.module.ln("Caustics", "Sunlight patterns on the bed of shallow water", true));
   public final com.larpclient.module.ln u = this.addSetting(
      new com.larpclient.module.ln("Underwater", "Clear depth color, light shafts and caustics beneath water", true)
   );
   public final com.larpclient.module.ln B = this.addSetting(
      new com.larpclient.module.ln(
         "Volumetric", "Poured or newly released water becomes persistent droplets; real fluid still controls swimming and drowning", false
      )
   );

   public RealisticWaterModule() {
      super("Realistic Water", "Rolling waves, reflections, refraction, depth color and shoreline foam", Category.RENDER, -1);
      j = this;
      this.B.W(var0 -> {
         df.o();
         y3.z();
      });
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && y3.S() ? "Material fallback" : "";
   }

   public static RealisticWaterModule L() {
      return j;
   }

   @Override
   public void onEnable() {
      y3.z();
   }

   @Override
   public void onDisable() {
      df.o();
      y3.z();
      y3.J();
   }

   public static boolean t() {
      if (j != null && j.isEnabled()) {
         RealisticWaterModule var0 = j;
         return var0 != null && var0.isRuntimeActive() && var0.u.getValue() && y3.n();
      } else {
         return false;
      }
   }
}
