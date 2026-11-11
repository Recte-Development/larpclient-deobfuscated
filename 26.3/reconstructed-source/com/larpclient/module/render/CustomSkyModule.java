package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.sky.SkyGradient;
import com.larpclient.render.sky.SkyRenderer2;
import com.larpclient.util.data.TickTimer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public class CustomSkyModule extends Module {
   private static CustomSkyModule instance;
   private final ModeSetting skyStyle = this.addSetting(new ModeSetting("Sky Style", "World sky effect", "Nebula", "Nebula", "Ocean", "Israel Flag"));
   private final NumberSetting animationSpeed = this.addSetting(
      new NumberSetting("Animation Speed", "Effect motion speed; zero freezes it", 0.5, 0.0, 2.0, 0.1)
   );
   private final BooleanSetting stars = this.addSetting(new BooleanSetting("Stars", "Keep a star field visible in the custom sky", true));
   private final NumberSetting starBrightness = this.addSetting(new NumberSetting("Star Brightness", "Star field brightness", 0.65, 0.0, 1.0, 0.05));
   private final BooleanSetting clouds = this.addSetting(new BooleanSetting("Clouds", "Show vanilla clouds", false));
   private final BooleanSetting matchFog = this.addSetting(new BooleanSetting("Match Fog", "Blend atmospheric fog into the horizon", true));
   private final NumberSetting fogDistance = this.addSetting(
      new NumberSetting("Fog Distance", "Multiplier of vanilla atmospheric distance", 1.0, 0.1, 10.0, 0.1)
   );
   private final NumberSetting nebulaBrightness = this.addSetting(new NumberSetting("Nebula Brightness", "Overall nebula exposure", 1.0, 0.2, 2.0, 0.05));
   private final NumberSetting nebulaSaturation = this.addSetting(new NumberSetting("Nebula Saturation", "Nebula color richness", 0.9, 0.0, 1.5, 0.05));
   private final NumberSetting nebulaIntensity = this.addSetting(new NumberSetting("Nebula Intensity", "Cloud and highlight strength", 0.85, 0.0, 1.5, 0.05));
   private final NumberSetting nebulaAnimationSpeed = this.addSetting(
      new NumberSetting("Nebula Animation Speed", "Cloud motion multiplier; zero freezes cloud motion", 1.0, 0.0, 3.0, 0.1)
   );
   private final ModeSetting nebulaMotion = this.addSetting(
      new ModeSetting("Nebula Motion", "Flowing clouds or the original slow drift", "Flow", "Flow", "Classic")
   );
   private final NumberSetting nebulaFlowStrength = this.addNumberSetting("Nebula Flow Strength", "Cloud deformation", 0.35, 0.0, 1.0, 0.05);
   private final NumberSetting nebulaScale = this.addNumberSetting("Nebula Scale", "Cloud pattern scale", 1.0, 0.5, 2.0, 0.05);
   private final ColorSetting nebulaColor = this.addSetting(new ColorSetting("Nebula Color", "Cloud artwork tint", -1));
   private final NumberSetting starDensity = this.addSetting(new NumberSetting("Star Density", "Procedural star field density", 0.45, 0.0, 1.0, 0.05));
   private final NumberSetting nebulaRotationSpeed = this.addSetting(
      new NumberSetting("Nebula Rotation Speed", "Degrees per second; zero stops rotation", 0.02, 0.0, 0.15, 0.01)
   );
   private final BooleanSetting showVanillaSun = this.addSetting(new BooleanSetting("Show Vanilla Sun", "Show Minecraft's sun", false));
   private final BooleanSetting showVanillaMoon = this.addSetting(new BooleanSetting("Show Vanilla Moon", "Show Minecraft's moon", false));
   private final BooleanSetting horizonBlend = this.addSetting(new BooleanSetting("Horizon Blend", "Subtle fade into the fog palette", true));
   private final NumberSetting exposure = this.addNumberSetting("Exposure", "Sky brightness", 1.0, 0.25, 2.0, 0.05);
   private final NumberSetting rotationSpeed = this.addNumberSetting("Rotation Speed", "Degrees per second", 0.02, -0.2, 0.2, 0.01);
   private final ModeSetting skyQuality = this.addSetting(
      new ModeSetting("Sky Quality", "Procedural noise and caustic detail", "High", "Low", "Medium", "High", "Ultra")
   );
   private final NumberSetting causticScale = this.addNumberSetting("Caustic Scale", "Size of the fluid light cells", 5.0, 2.0, 10.0, 0.25);
   private final NumberSetting causticIntensity = this.addNumberSetting("Caustic Intensity", "Brightness of converging light", 0.65, 0.0, 1.5, 0.05);
   private final NumberSetting causticDistortion = this.addNumberSetting("Caustic Distortion", "Fluid warping strength", 0.8, 0.0, 2.0, 0.1);
   private final ColorSetting oceanBaseColor = this.addSetting(new ColorSetting("Ocean Base Color", "Deep water color", -16635057));
   private final ColorSetting oceanHighlightColor = this.addSetting(new ColorSetting("Ocean Highlight Color", "Caustic light color", -12133155));
   private SkyGradient gradient;

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public ModeSetting getSkyStyleSetting() {
      return this.skyStyle;
   }

   public List<Setting<?>> getActiveSettings() {
      return switch (this.getSkyStyle()) {
         case "Nebula" -> List.of(
            this.nebulaBrightness,
            this.nebulaSaturation,
            this.nebulaIntensity,
            this.nebulaMotion,
            this.nebulaAnimationSpeed,
            this.nebulaFlowStrength,
            this.nebulaScale,
            this.nebulaColor,
            this.nebulaRotationSpeed
         );
         case "Ocean" -> List.of(this.causticScale, this.causticIntensity, this.causticDistortion, this.oceanBaseColor, this.oceanHighlightColor);
         default -> List.of();
      };
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(
         this.nebulaBrightness,
         this.nebulaSaturation,
         this.nebulaIntensity,
         this.nebulaColor,
         this.causticIntensity,
         this.oceanBaseColor,
         this.oceanHighlightColor,
         this.exposure
      );
   }

   public List<Setting<?>> getAnimationSettings() {
      return List.of(this.animationSpeed, this.nebulaMotion, this.nebulaAnimationSpeed, this.nebulaRotationSpeed, this.rotationSpeed);
   }

   public List<Setting<?>> getEnvironmentSettings() {
      return List.of(
         this.stars,
         this.starBrightness,
         this.starDensity,
         this.clouds,
         this.showVanillaSun,
         this.showVanillaMoon,
         this.horizonBlend,
         this.matchFog,
         this.fogDistance
      );
   }

   public List<Setting<?>> getDetailSettings() {
      return List.of(this.nebulaFlowStrength, this.nebulaScale, this.causticScale, this.causticDistortion, this.skyQuality);
   }

   public String getSkyStyle() {
      return this.skyStyle.getValue();
   }

   public float getAnimationSpeed() {
      return this.animationSpeed.getValue().floatValue();
   }

   public float getRotationSpeed() {
      return this.rotationSpeed.getValue().floatValue();
   }

   public float getExposure() {
      return this.exposure.getValue().floatValue();
   }

   public boolean isHorizonBlend() {
      return this.horizonBlend.getValue();
   }

   public float getAnimationSettings() {
      return this.skyQuality.getModes().indexOf(this.skyQuality.getValue());
   }

   public float getCausticScale() {
      return this.causticScale.getValue().floatValue();
   }

   public float isHorizonBlend() {
      return this.causticIntensity.getValue().floatValue();
   }

   public float getInstance() {
      return this.causticDistortion.getValue().floatValue();
   }

   public int getOceanBaseColor() {
      return this.oceanBaseColor.getValue();
   }

   public int getRotationSpeed() {
      return this.oceanHighlightColor.getValue();
   }

   public boolean isShowVanillaSun() {
      return this.showVanillaSun.getValue();
   }

   public boolean isShowVanillaMoon() {
      return this.showVanillaMoon.getValue();
   }

   public boolean getNebulaFlowStrength() {
      return !this.skyStyle.isMode("Israel Flag");
   }

   public boolean isNebula() {
      return this.skyStyle.isMode("Nebula");
   }

   public float getNebulaBrightness() {
      return this.nebulaBrightness.getValue().floatValue();
   }

   public float getNebulaSaturation() {
      return this.nebulaSaturation.getValue().floatValue();
   }

   public float getGradient() {
      return this.nebulaIntensity.getValue().floatValue();
   }

   public float isShowVanillaMoon() {
      return this.starDensity.getValue().floatValue();
   }

   public float getNebulaAnimationSpeed() {
      return this.nebulaAnimationSpeed.getValue().floatValue();
   }

   public float getNebulaRotationSpeed() {
      return this.nebulaRotationSpeed.getValue().floatValue();
   }

   public boolean isFlowMotion() {
      return this.nebulaMotion.isMode("Flow");
   }

   public float getNebulaFlowStrength() {
      return this.nebulaFlowStrength.getValue().floatValue();
   }

   public float getEnvironmentSettings() {
      return this.nebulaScale.getValue().floatValue();
   }

   public int getNebulaColor() {
      return this.nebulaColor.getValue();
   }

   public CustomSkyModule() {
      super("CustomSky", "Use animated custom skies", Category.VISUALS, -1);
      instance = this;

      for (Setting var4 : new Setting[]{
         this.nebulaBrightness,
         this.nebulaSaturation,
         this.nebulaIntensity,
         this.nebulaAnimationSpeed,
         this.nebulaRotationSpeed,
         this.nebulaMotion,
         this.nebulaFlowStrength,
         this.nebulaScale,
         this.nebulaColor
      }) {
         var4.visibleWhen(this::isNebula);
      }

      this.nebulaFlowStrength.visibleWhen(() -> this.isNebula() && this.isFlowMotion());

      for (Setting var8 : new Setting[]{this.causticScale, this.causticIntensity, this.causticDistortion, this.oceanBaseColor, this.oceanHighlightColor}) {
         var8.visibleWhen(() -> this.skyStyle.isMode("Ocean"));
      }

      this.animationSpeed.visibleWhen(() -> this.skyStyle.isMode("Ocean"));
      this.rotationSpeed.visibleWhen(() -> !this.isNebula());
      this.exposure.visibleWhen(() -> !this.isNebula());
      this.horizonBlend.visibleWhen(() -> !this.isNebula());
      this.skyQuality.visibleWhen(() -> this.skyStyle.isMode("Ocean"));
      this.stars.visibleWhen(this::isNebula);
      this.starBrightness.visibleWhen(() -> this.stars.isVisible() && this.stars.getValue());
      this.starDensity.visibleWhen(this.starBrightness::isVisible);
   }

   @Override
   public void onEnable() {
      SkyRenderer2.requestReset();
   }

   public static CustomSkyModule getInstance() {
      return instance;
   }

   public static boolean isShowVanillaSun(Skybox var0) {
      return var0 == Skybox.OVERWORLD || var0 == Skybox.END;
   }

   public static boolean isActive() {
      if (instance != null && instance.isEnabled()) {
         Minecraft var0 = Minecraft.getInstance();
         return instance != null
            && instance.isOperational()
            && SkyRenderer2.isModeAvailable(instance.getSkyStyle())
            && var0.level != null
            && isShowVanillaSun(var0.level.dimensionType().skybox())
            && var0.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
      } else {
         return false;
      }
   }

   private SkyGradient getStarBrightness(int var1, int var2) {
      if (this.gradient == null || this.gradient.zenith() != var1 || this.gradient.horizon() != var2) {
         this.gradient = new SkyGradient(var1, var2);
      }

      return this.gradient;
   }

   public SkyGradient getGradient() {
      if (this.skyStyle.isMode("Ocean")) {
         return this.getStarBrightness(this.oceanBaseColor.getValue(), this.oceanBaseColor.getValue());
      } else {
         return this.skyStyle.isMode("Israel Flag")
            ? this.getStarBrightness(-1, -1)
            : this.getStarBrightness(this.adjustColor(-16251885), this.adjustColor(-15265755));
      }
   }

   private int adjustColor(int var1) {
      float var2 = var1 >> 16 & 0xFF;
      float var3 = var1 >> 8 & 0xFF;
      float var4 = var1 & 0xFF;
      float var5 = var2 * 0.2126F + var3 * 0.7152F + var4 * 0.0722F;
      float var6 = this.getNebulaSaturation();
      float var7 = this.getNebulaBrightness();
      int var8 = (int)Math.clamp((var5 + (var2 - var5) * var6) * var7, 0.0F, 255.0F);
      int var9 = (int)Math.clamp((var5 + (var3 - var5) * var6) * var7, 0.0F, 255.0F);
      int var10 = (int)Math.clamp((var5 + (var4 - var5) * var6) * var7, 0.0F, 255.0F);
      return 0xFF000000 | var8 << 16 | var9 << 8 | var10;
   }

   public float getAnimationTime() {
      return (float)(TickTimer.getTime() * this.animationSpeed.getValue());
   }

   public float getStarBrightness() {
      return !this.skyStyle.isMode("Israel Flag") && this.stars.getValue() ? this.starBrightness.getValue().floatValue() : 0.0F;
   }

   public boolean isCloudsEnabled() {
      return this.clouds.getValue();
   }

   public boolean getNebulaColor() {
      return this.matchFog.getValue();
   }

   public float getFogDistance() {
      return this.fogDistance.getValue().floatValue();
   }
}
