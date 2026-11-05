package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WetGroundModule extends Module {
   private static WetGroundModule K;
   public final com.larpclient.module.lp S = this.addSetting(
      new com.larpclient.module.lp("Style", "Patches of puddles or a reflective sheet across exposed ground", "Puddles", "Puddles", "Flooded")
   );
   public final com.larpclient.module.lc M = this.addSetting(
      new com.larpclient.module.lc("Puddles", "Coverage of exposed ground, percent", 45.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.lc D = this.addSetting(
      new com.larpclient.module.lc("Wetness", "Soaked look: darker soil, wider puddles and mirror-like reflections, percent", 50.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.lc Q = this.addSetting(
      new com.larpclient.module.lc("Shine", "Strength of sky, world and sun reflections, percent", 70.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.ln B = this.addSetting(
      new com.larpclient.module.ln("World reflections", "Reflect visible trees, buildings and entities; costs GPU time", true)
   );
   public final com.larpclient.module.ln C = this.addSetting(new com.larpclient.module.ln("Raindrops", "Expanding rings disturb puddle reflections", true));
   public final com.larpclient.module.ln N = this.addSetting(new com.larpclient.module.ln("Wetter in rain", "Rain increases puddle coverage and sheen", true));

   public WetGroundModule() {
      super("Wet Ground", "Damp soil, reflective puddles and rain rings on exposed terrain", Category.RENDER, -1);
      K = this;
      this.M.x(() -> this.S.i("Puddles"));
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && y3.i() ? "Material fallback" : "";
   }

   public static WetGroundModule Y() {
      return K;
   }

   @Override
   public void onEnable() {
      y3.G();
   }

   @Override
   public void onDisable() {
      y3.J();
   }
}
