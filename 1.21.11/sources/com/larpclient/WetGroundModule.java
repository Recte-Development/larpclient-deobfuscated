package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WetGroundModule extends Module {
   private static WetGroundModule p;
   public final com.larpclient.module.li b = this.addSetting(
      new com.larpclient.module.li("Style", "Patches of puddles or a reflective sheet across exposed ground", "Puddles", "Puddles", "Flooded")
   );
   public final com.larpclient.module.lo f = this.addSetting(
      new com.larpclient.module.lo("Puddles", "Coverage of exposed ground, percent", 45.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.lo r = this.addSetting(
      new com.larpclient.module.lo("Wetness", "Soaked look: darker soil, wider puddles and mirror-like reflections, percent", 50.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.lo q = this.addSetting(
      new com.larpclient.module.lo("Shine", "Strength of sky, world and sun reflections, percent", 70.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.l3 n = this.addSetting(
      new com.larpclient.module.l3("World reflections", "Reflect visible trees, buildings and entities; costs GPU time", true)
   );
   public final com.larpclient.module.l3 m = this.addSetting(new com.larpclient.module.l3("Raindrops", "Expanding rings disturb puddle reflections", true));
   public final com.larpclient.module.l3 A = this.addSetting(new com.larpclient.module.l3("Wetter in rain", "Rain increases puddle coverage and sheen", true));

   public WetGroundModule() {
      super("Wet Ground", "Damp soil, reflective puddles and rain rings on exposed terrain", Category.RENDER, -1);
      p = this;
      this.f.O(() -> this.b.G("Puddles"));
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && qs.M() ? "Material fallback" : "";
   }

   public static WetGroundModule M() {
      return p;
   }

   @Override
   public void onEnable() {
      qs.w();
   }

   @Override
   public void onDisable() {
      qs.B();
   }
}
