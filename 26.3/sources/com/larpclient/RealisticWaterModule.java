package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class RealisticWaterModule extends Module {
   private static RealisticWaterModule q;
   public final com.larpclient.module.b I = this.addSetting(
      new com.larpclient.module.b("Intensity", "Strength of the water material, percent", 100.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.b N = this.addSetting(
      new com.larpclient.module.b("Waves", "Height of rolling surface waves, percent", 60.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.m S = this.addSetting(
      new com.larpclient.module.m("World reflections", "Reflect visible surroundings; costs GPU time", true)
   );
   public final com.larpclient.module.m a = this.addSetting(new com.larpclient.module.m("Foam", "Shoreline foam and waterfall streaks", true));
   public final com.larpclient.module.m e = this.addSetting(new com.larpclient.module.m("Caustics", "Sunlight patterns on the bed of shallow water", true));
   public final com.larpclient.module.m W = this.addSetting(
      new com.larpclient.module.m("Underwater", "Clear depth color, light shafts and caustics beneath water", true)
   );
   public final com.larpclient.module.m A = this.addSetting(
      new com.larpclient.module.m(
         "Volumetric", "Poured or newly released water becomes persistent droplets; real fluid still controls swimming and drowning", false
      )
   );

   public RealisticWaterModule() {
      super("Realistic Water", "Rolling waves, reflections, refraction, depth color and shoreline foam", Category.RENDER, -1);
      q = this;
      this.A.w(var0 -> {
         v0.Q();
         at.U();
      });
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && at.o() ? "Material fallback" : "";
   }

   public static RealisticWaterModule g() {
      return q;
   }

   @Override
   public void onEnable() {
      at.U();
   }

   @Override
   public void onDisable() {
      v0.Q();
      at.U();
      at.r();
   }

   public static boolean Z() {
      if (q != null && q.isEnabled()) {
         RealisticWaterModule var0 = q;
         return var0 != null && var0.isRuntimeActive() && var0.W.getValue() && at.Y();
      } else {
         return false;
      }
   }
}
