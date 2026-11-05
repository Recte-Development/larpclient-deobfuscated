package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.dh;

public final class RealisticWaterModule extends Module {
   private static RealisticWaterModule Z;
   public final com.larpclient.module.d9 J = this.addSetting(
      new com.larpclient.module.d9("Intensity", "Strength of the water material, percent", 100.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.d9 F = this.addSetting(
      new com.larpclient.module.d9("Waves", "Height of rolling surface waves, percent", 60.0, 0.0, 100.0, 5.0)
   );
   public final dh D = this.addSetting(new dh("World reflections", "Reflect visible surroundings; costs GPU time", true));
   public final dh E = this.addSetting(new dh("Foam", "Shoreline foam and waterfall streaks", true));
   public final dh H = this.addSetting(new dh("Caustics", "Sunlight patterns on the bed of shallow water", true));
   public final dh X = this.addSetting(new dh("Underwater", "Clear depth color, light shafts and caustics beneath water", true));
   public final dh V = this.addSetting(
      new dh("Volumetric", "Poured or newly released water becomes persistent droplets; real fluid still controls swimming and drowning", false)
   );

   public RealisticWaterModule() {
      super("Realistic Water", "Rolling waves, reflections, refraction, depth color and shoreline foam", Category.RENDER, -1);
      Z = this;
      this.V.h(var0 -> {
         hr.E();
         k.k();
      });
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && k.K() ? "Material fallback" : "";
   }

   public static RealisticWaterModule G() {
      return Z;
   }

   @Override
   public void onEnable() {
      k.k();
   }

   @Override
   public void onDisable() {
      hr.E();
      k.k();
      k.w();
   }

   public static boolean X() {
      if (Z != null && Z.isEnabled()) {
         RealisticWaterModule var0 = Z;
         return var0 != null && var0.isRuntimeActive() && var0.X.P() && k.h();
      } else {
         return false;
      }
   }
}
