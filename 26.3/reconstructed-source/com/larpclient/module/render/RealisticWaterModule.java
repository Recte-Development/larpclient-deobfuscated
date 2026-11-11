package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.world.RealisticWater;
import com.larpclient.render.world.TerrainMaterials;

public final class RealisticWaterModule extends Module {
   private static RealisticWaterModule instance;
   public final NumberSetting intensity = this.addSetting(new NumberSetting("Intensity", "Strength of the water material, percent", 100.0, 0.0, 100.0, 5.0));
   public final NumberSetting waves = this.addSetting(new NumberSetting("Waves", "Height of rolling surface waves, percent", 60.0, 0.0, 100.0, 5.0));
   public final BooleanSetting worldReflections = this.addSetting(new BooleanSetting("World reflections", "Reflect visible surroundings; costs GPU time", true));
   public final BooleanSetting foam = this.addSetting(new BooleanSetting("Foam", "Shoreline foam and waterfall streaks", true));
   public final BooleanSetting caustics = this.addSetting(new BooleanSetting("Caustics", "Sunlight patterns on the bed of shallow water", true));
   public final BooleanSetting underwater = this.addSetting(
      new BooleanSetting("Underwater", "Clear depth color, light shafts and caustics beneath water", true)
   );
   public final BooleanSetting volumetric = this.addSetting(
      new BooleanSetting("Volumetric", "Poured or newly released water becomes persistent droplets; real fluid still controls swimming and drowning", false)
   );

   public RealisticWaterModule() {
      super("Realistic Water", "Rolling waves, reflections, refraction, depth color and shoreline foam", Category.RENDER, -1);
      instance = this;
      this.volumetric.onChange(var0 -> {
         RealisticWater.reset();
         TerrainMaterials.refreshChunks();
      });
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && TerrainMaterials.isHookDisabled() ? "Material fallback" : "";
   }

   public static RealisticWaterModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      TerrainMaterials.refreshChunks();
   }

   @Override
   public void onDisable() {
      RealisticWater.reset();
      TerrainMaterials.refreshChunks();
      TerrainMaterials.isNativeHookUnavailable();
   }

   public static boolean isUnderwaterActive() {
      if (instance != null && instance.isEnabled()) {
         RealisticWaterModule var0 = instance;
         return var0 != null && var0.isRuntimeActive() && var0.underwater.getValue() && TerrainMaterials.isMaterialPassActive();
      } else {
         return false;
      }
   }
}
