package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.sky.DistanceFogRenderer;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.DimensionType.Skybox;

public final class FogModule extends Module {
   private static FogModule instance;
   public final NumberSetting startDistance = this.addNumberSetting("Start Distance", "Clear range before fog begins, in blocks", 0.0, 0.0, 256.0, 4.0);
   public final NumberSetting endDistance = this.addNumberSetting("End Distance", "Distance where fog reaches full density, in blocks", 48.0, 16.0, 1024.0, 8.0);
   public final NumberSetting height = this.addNumberSetting("Height", "Fog thins above sea level over this many blocks", 256.0, 8.0, 256.0, 4.0);
   public final NumberSetting sunGlow = this.addNumberSetting("Sun Glow", "Warm light scattered through the fog toward the sun", 0.45, 0.0, 1.0, 0.05);
   public final ModeSetting colorMode = this.addSetting(new ModeSetting("Color", "Sky follows the native and sky module fog color", "Sky", "Sky", "Custom"));
   public final ColorSetting customColor = this.addSetting(new ColorSetting("Custom Color", "Fog color when Color is Custom", -4601895));
   public final NumberSetting skyHeight = this.addNumberSetting(
      "Sky Height", "Degrees above the horizon the fog covers the sky; zero keeps the sky clear", 30.0, 0.0, 30.0, 1.0
   );
   public final BooleanSetting clearSky = this.addSetting(new BooleanSetting("Clear Sky", "Keep vanilla fog from climbing high up the sky", true));
   public final BooleanSetting fadeUnderground = this.addSetting(
      new BooleanSetting("Fade Underground", "Fade the fog where the camera cannot see the sky", true)
   );
   public final ModeSetting dimensionFilter = this.addSetting(
      new ModeSetting("Dimension Filter", "Choose where the fog appears", "Overworld", "Overworld", "All")
   );
   public static final float maxDistance = 512.0F;
   public static final float minDensity = 1.0F;
   private double skyVisibility = 1.0;
   private double previousSkyVisibility = 1.0;
   private int tickCounter;

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public FogModule() {
      super("Fog", "Soft distance fog that keeps the sky clear", Category.VISUALS, -1);
      instance = this;
      this.customColor.visibleWhen(() -> this.colorMode.isMode("Custom"));
   }

   public static FogModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getDistanceSettings() {
      return List.of(this.startDistance, this.endDistance);
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.height, this.sunGlow, this.colorMode, this.customColor);
   }

   public List<Setting<?>> getSkySettings() {
      return List.of(this.skyHeight, this.clearSky);
   }

   public List<Setting<?>> getFilterSettings() {
      return List.of(this.fadeUnderground, this.dimensionFilter);
   }

   public boolean isFogActive() {
      if (!this.isEnabled()) {
         return false;
      } else {
         return this.isRuntimeActive() && minecraft != null && minecraft.level != null
            ? this.dimensionFilter.isMode("All") || minecraft.level.dimensionType().skybox() == Skybox.OVERWORLD
            : false;
      }
   }

   public static boolean getEffectiveEndDistance() {
      return instance != null && instance.isEnabled() ? instance != null && instance.clearSky.getValue() && instance.isFogActive() : false;
   }

   public float getEffectiveEndDistance() {
      return (float)Math.max(this.endDistance.getValue(), this.startDistance.getValue() + 8.0);
   }

   public float getSkyVisibility(float var1) {
      return (float)(this.previousSkyVisibility + (this.skyVisibility - this.previousSkyVisibility) * Math.clamp(var1, 0.0F, 1.0F));
   }

   @Override
   public void onTick() {
      this.previousSkyVisibility = this.skyVisibility;
      if (this.isInGame()) {
         double var1 = 1.0;
         int var10000 = this.fadeUnderground.getValue();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            BlockPos var3 = BlockPos.containing(minecraft.gameRenderer.mainCamera().position());
            double var14;
            if (minecraft.level.hasChunkAt(var3)) {
               var10000 = minecraft.level.getBrightness(LightLayer.SKY, var3);
               byte var11 = 3;
               int var10 = var10000;
               var14 = Math.clamp((var10 - var11) / 7.0, 0.0, 1.0);
            } else {
               var14 = this.skyVisibility;
            }

            var1 = var14;
         }

         this.skyVisibility = this.skyVisibility + Math.clamp(var1 - this.skyVisibility, -0.04, 0.04);
         var10000 = this.tickCounter;
         byte var5 = 1;
         int var4 = this.tickCounter;
         this.tickCounter = var4 + var5;
         if (var10000 % 100 == 0) {
            double var17;
            var10000 = (var17 = Math.abs(var1 - this.skyVisibility) - 0.5) == 0.0 ? 0 : (var17 < 0.0 ? -1 : 1);
            boolean var7 = false;
            int var6 = var10000;
            if ((-Integer.signum(var6) >>> 31 ^ 1) == 0) {
               LarpLog.debug("Fog", "Underground fade toward {}", var1);
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.skyVisibility = this.previousSkyVisibility = 1.0;
      this.tickCounter = 0;
      LarpLog.info(
         "Fog",
         "Enabled; start={}; end={}; height={}; sky height={} degrees; clear sky={}",
         this.startDistance.getValue(),
         this.getEffectiveEndDistance(),
         this.height.getValue(),
         this.skyHeight.getValue(),
         this.clearSky.getValue()
      );
   }

   @Override
   public void onDisable() {
      DistanceFogRenderer.releaseResources();
   }
}
