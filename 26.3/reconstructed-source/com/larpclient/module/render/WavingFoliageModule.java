package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.world.TerrainMaterials;

public final class WavingFoliageModule extends Module {
   private static WavingFoliageModule instance;
   public final BooleanSetting leaves = this.addSetting(new BooleanSetting("Leaves", "Leaves and vines sway", true));
   public final BooleanSetting trees = this.addSetting(
      new BooleanSetting("Trees", "Bend natural trees from their planted base; log buildings stay still", true)
   );
   public final BooleanSetting plants = this.addSetting(new BooleanSetting("Plants", "Grass, flowers, crops and saplings bend from their base", true));
   public final NumberSetting strength = this.addSetting(new NumberSetting("Strength", "How far foliage sways", 1.0, 0.2, 3.0, 0.1));
   public final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Wind animation speed", 1.0, 0.2, 3.0, 0.1));
   public final NumberSetting gusts = this.addSetting(new NumberSetting("Gusts", "Variation in wind strength, percent", 60.0, 0.0, 100.0, 5.0));

   public WavingFoliageModule() {
      super("Waving Foliage", "Trees, grass, flowers and crops sway in wind and passing gusts", Category.RENDER, -1);
      instance = this;
      this.trees.visibleWhen(() -> this.leaves.getValue());
      this.leaves.onChange(var1 -> this.refreshChunks());
      this.trees.onChange(var1 -> this.refreshChunks());
      this.plants.onChange(var1 -> this.refreshChunks());
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && TerrainMaterials.isHookDisabled() ? "Material fallback" : "";
   }

   public static WavingFoliageModule getInstance() {
      return instance;
   }

   private void refreshChunks() {
      TerrainMaterials.refreshChunks();
   }

   @Override
   public void onEnable() {
      this.refreshChunks();
   }

   @Override
   public void onDisable() {
      this.refreshChunks();
      TerrainMaterials.isNativeHookUnavailable();
   }
}
