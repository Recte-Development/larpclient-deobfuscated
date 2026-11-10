package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class RealisticWaterModule extends Module {
   private static RealisticWaterModule n;
   public final com.larpclient.module.e t = this.addSetting(
      new com.larpclient.module.e("Intensity", "Strength of the water material, percent", 100.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.e U = this.addSetting(
      new com.larpclient.module.e("Waves", "Height of rolling surface waves, percent", 60.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.s y = this.addSetting(
      new com.larpclient.module.s("World reflections", "Reflect visible surroundings; costs GPU time", true)
   );
   public final com.larpclient.module.s g = this.addSetting(new com.larpclient.module.s("Foam", "Shoreline foam and waterfall streaks", true));
   public final com.larpclient.module.s M = this.addSetting(new com.larpclient.module.s("Caustics", "Sunlight patterns on the bed of shallow water", true));
   public final com.larpclient.module.s S = this.addSetting(
      new com.larpclient.module.s("Underwater", "Clear depth color, light shafts and caustics beneath water", true)
   );
   public final com.larpclient.module.s k = this.addSetting(
      new com.larpclient.module.s(
         "Volumetric", "Poured or newly released water becomes persistent droplets; real fluid still controls swimming and drowning", false
      )
   );

   public RealisticWaterModule() {
      super("Realistic Water", "Rolling waves, reflections, refraction, depth color and shoreline foam", Category.RENDER, -1);
      n = this;
      this.k.o(var0 -> {
         b9.b();
         w6.N();
      });
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && w6.C() ? "Material fallback" : "";
   }

   public static RealisticWaterModule K() {
      return n;
   }

   @Override
   public void onEnable() {
      w6.N();
   }

   @Override
   public void onDisable() {
      b9.b();
      w6.N();
      w6.P();
   }

   public static boolean c() {
      if (n != null && n.isEnabled()) {
         RealisticWaterModule var0 = n;
         return var0 != null && var0.isRuntimeActive() && var0.S.getValue() && w6.c();
      } else {
         return false;
      }
   }
}
