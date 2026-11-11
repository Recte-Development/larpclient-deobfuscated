package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.world.TerrainMaterials;

public final class WetGroundModule extends Module {
   private static WetGroundModule instance;
   public final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Patches of puddles or a reflective sheet across exposed ground", "Puddles", "Puddles", "Flooded")
   );
   public final NumberSetting puddles = this.addSetting(new NumberSetting("Puddles", "Coverage of exposed ground, percent", 45.0, 0.0, 100.0, 5.0));
   public final NumberSetting wetness = this.addSetting(
      new NumberSetting("Wetness", "Soaked look: darker soil, wider puddles and mirror-like reflections, percent", 50.0, 0.0, 100.0, 5.0)
   );
   public final NumberSetting shine = this.addSetting(new NumberSetting("Shine", "Strength of sky, world and sun reflections, percent", 70.0, 0.0, 100.0, 5.0));
   public final BooleanSetting worldReflections = this.addSetting(
      new BooleanSetting("World reflections", "Reflect visible trees, buildings and entities; costs GPU time", true)
   );
   public final BooleanSetting raindrops = this.addSetting(new BooleanSetting("Raindrops", "Expanding rings disturb puddle reflections", true));
   public final BooleanSetting wetterInRain = this.addSetting(new BooleanSetting("Wetter in rain", "Rain increases puddle coverage and sheen", true));

   public WetGroundModule() {
      super("Wet Ground", "Damp soil, reflective puddles and rain rings on exposed terrain", Category.RENDER, -1);
      instance = this;
      this.puddles.visibleWhen(() -> this.style.isMode("Puddles"));
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && TerrainMaterials.isNativeHookUnavailable() ? "Material fallback" : "";
   }

   public static WetGroundModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      TerrainMaterials.onNativeActivated();
   }

   @Override
   public void onDisable() {
      TerrainMaterials.isNativeHookUnavailable();
   }
}
