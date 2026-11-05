package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.dh;

public final class WetGroundModule extends Module {
   private static WetGroundModule d;
   public final com.larpclient.module.d6 S = this.addSetting(
      new com.larpclient.module.d6("Style", "Patches of puddles or a reflective sheet across exposed ground", "Puddles", "Puddles", "Flooded")
   );
   public final com.larpclient.module.d9 G = this.addSetting(
      new com.larpclient.module.d9("Puddles", "Coverage of exposed ground, percent", 45.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.d9 L = this.addSetting(
      new com.larpclient.module.d9("Wetness", "Soaked look: darker soil, wider puddles and mirror-like reflections, percent", 50.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.d9 g = this.addSetting(
      new com.larpclient.module.d9("Shine", "Strength of sky, world and sun reflections, percent", 70.0, 0.0, 100.0, 5.0)
   );
   public final dh M = this.addSetting(new dh("World reflections", "Reflect visible trees, buildings and entities; costs GPU time", true));
   public final dh O = this.addSetting(new dh("Raindrops", "Expanding rings disturb puddle reflections", true));
   public final dh q = this.addSetting(new dh("Wetter in rain", "Rain increases puddle coverage and sheen", true));

   public WetGroundModule() {
      super("Wet Ground", "Damp soil, reflective puddles and rain rings on exposed terrain", Category.RENDER, -1);
      d = this;
      this.G.H(() -> this.S.S("Puddles"));
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && k.g() ? "Material fallback" : "";
   }

   public static WetGroundModule n() {
      return d;
   }

   @Override
   public void onEnable() {
      k.g();
   }

   @Override
   public void onDisable() {
      k.w();
   }
}
