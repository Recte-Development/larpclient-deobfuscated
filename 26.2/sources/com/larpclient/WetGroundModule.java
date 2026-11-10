package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WetGroundModule extends Module {
   private static WetGroundModule d;
   public final com.larpclient.module.l q = this.addSetting(
      new com.larpclient.module.l("Style", "Patches of puddles or a reflective sheet across exposed ground", "Puddles", "Puddles", "Flooded")
   );
   public final com.larpclient.module.e B = this.addSetting(
      new com.larpclient.module.e("Puddles", "Coverage of exposed ground, percent", 45.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.e M = this.addSetting(
      new com.larpclient.module.e("Wetness", "Soaked look: darker soil, wider puddles and mirror-like reflections, percent", 50.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.e Q = this.addSetting(
      new com.larpclient.module.e("Shine", "Strength of sky, world and sun reflections, percent", 70.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.s W = this.addSetting(
      new com.larpclient.module.s("World reflections", "Reflect visible trees, buildings and entities; costs GPU time", true)
   );
   public final com.larpclient.module.s R = this.addSetting(new com.larpclient.module.s("Raindrops", "Expanding rings disturb puddle reflections", true));
   public final com.larpclient.module.s m = this.addSetting(new com.larpclient.module.s("Wetter in rain", "Rain increases puddle coverage and sheen", true));

   public WetGroundModule() {
      super("Wet Ground", "Damp soil, reflective puddles and rain rings on exposed terrain", Category.RENDER, -1);
      d = this;
      this.B.P(() -> this.q.O("Puddles"));
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && w6.B() ? "Material fallback" : "";
   }

   public static WetGroundModule h() {
      return d;
   }

   @Override
   public void onEnable() {
      w6.Q();
   }

   @Override
   public void onDisable() {
      w6.P();
   }
}
