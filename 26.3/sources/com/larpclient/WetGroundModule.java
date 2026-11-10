package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WetGroundModule extends Module {
   private static WetGroundModule S;
   public final com.larpclient.module.o H = this.addSetting(
      new com.larpclient.module.o("Style", "Patches of puddles or a reflective sheet across exposed ground", "Puddles", "Puddles", "Flooded")
   );
   public final com.larpclient.module.b p = this.addSetting(
      new com.larpclient.module.b("Puddles", "Coverage of exposed ground, percent", 45.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.b q = this.addSetting(
      new com.larpclient.module.b("Wetness", "Soaked look: darker soil, wider puddles and mirror-like reflections, percent", 50.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.b h = this.addSetting(
      new com.larpclient.module.b("Shine", "Strength of sky, world and sun reflections, percent", 70.0, 0.0, 100.0, 5.0)
   );
   public final com.larpclient.module.m B = this.addSetting(
      new com.larpclient.module.m("World reflections", "Reflect visible trees, buildings and entities; costs GPU time", true)
   );
   public final com.larpclient.module.m y = this.addSetting(new com.larpclient.module.m("Raindrops", "Expanding rings disturb puddle reflections", true));
   public final com.larpclient.module.m C = this.addSetting(new com.larpclient.module.m("Wetter in rain", "Rain increases puddle coverage and sheen", true));

   public WetGroundModule() {
      super("Wet Ground", "Damp soil, reflective puddles and rain rings on exposed terrain", Category.RENDER, -1);
      S = this;
      this.p.a(() -> this.H.w("Puddles"));
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && at.r() ? "Material fallback" : "";
   }

   public static WetGroundModule C() {
      return S;
   }

   @Override
   public void onEnable() {
      at.E();
   }

   @Override
   public void onDisable() {
      at.r();
   }
}
