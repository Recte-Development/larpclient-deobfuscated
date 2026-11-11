package com.larpclient.module.render;

import com.larpclient.gui.theme.BackgroundThemeColors;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.sky.SkyRenderContext;
import com.larpclient.render.sky.SkyShaderLayer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import net.minecraft.world.level.material.FogType;

public final class SkyShaderModule extends Module {
   private static SkyShaderModule instance;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode",
         "Larp sky shader",
         "Core Caustic",
         "Water",
         "Caustic",
         "Nebula",
         "Aurora",
         "Starfall",
         "BlackHole",
         "Plasma",
         "Voronoi",
         "Core Caustic",
         "Core Nebula",
         "Sunset",
         "Thunderstorm",
         "Volcano",
         "Sandstorm",
         "Christmas"
      )
   );
   private final NumberSetting speed = this.addNumberSetting("Speed", "Animation speed", 1.2, 0.1, 5.0, 0.1);
   private final NumberSetting scale = this.addNumberSetting("Scale", "Pattern scale", 5.0, 1.0, 20.0, 0.5);
   private final NumberSetting intensity = this.addNumberSetting("Intensity", "Pattern intensity", 0.01, 0.001, 0.05, 0.001);
   private final NumberSetting alpha = this.addNumberSetting("Alpha", "Sky opacity", 1.0, 0.3, 1.0, 0.05);
   private final BooleanSetting useThemeColor = this.addSetting(new BooleanSetting("Theme Color", "Use the client accent", true));
   private final ColorSetting customColor = this.addSetting(new ColorSetting("Custom Color", "Sky tint", -1));
   private final NumberSetting lightningRate = this.addNumberSetting("Lightning Rate", "How often lightning strikes", 1.0, 0.25, 3.0, 0.05);
   private final SkyRenderContext renderContext = new SkyRenderContext();
   private final HashMap<String, SkyShaderLayer> layers = new HashMap<>();
   private final HashSet<String> failedModes = new HashSet<>();
   private int reloadCount = -1;
   private Object lastLevel;
   private double lastRenderTime = Double.NaN;
   private String lastMode;
   private boolean customSkyActive;
   private boolean rendered;
   private String renderedMode;
   private String cachedMode;
   private int cachedColor;
   private float cachedIntensity;
   private int cachedThemeColor;

   public SkyShaderModule() {
      super("Sky Shader", "Show animated sky effects", Category.VISUALS, -1);
      instance = this;
      this.customColor.visibleWhen(() -> !this.useThemeColor.getValue());
      this.scale.visibleWhen(() -> !this.usesCoreShader() && !this.mode.isMode("BlackHole"));
      this.intensity.visibleWhen(() -> !this.usesCoreShader());
      this.alpha.visibleWhen(() -> !this.usesCoreShader());
      this.lightningRate.visibleWhen(() -> this.mode.isMode("Thunderstorm") || this.mode.isMode("Volcano"));
   }

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public static SkyShaderModule getInstance() {
      return instance;
   }

   public ModeSetting getModeSetting() {
      return this.mode;
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.useThemeColor, this.customColor, this.intensity, this.alpha);
   }

   public List<Setting<?>> getAnimationSettings() {
      return List.of(this.speed, this.scale, this.lightningRate);
   }

   private boolean usesCoreShader() {
      return this.mode.isMode("Core Caustic")
         || this.mode.isMode("Core Nebula")
         || this.mode.isMode("Sunset")
         || this.mode.isMode("Thunderstorm")
         || this.mode.isMode("Volcano")
         || this.mode.isMode("Sandstorm")
         || this.mode.isMode("Christmas");
   }

   public int getColor() {
      return this.useThemeColor.getValue() ? Theme.accent : this.customColor.getValue();
   }

   public float getSpeed() {
      return this.speed.getValue().floatValue();
   }

   public float getScale() {
      return this.scale.getValue().floatValue();
   }

   public float getIntensity() {
      return this.intensity.getValue().floatValue();
   }

   public float getLightningRate() {
      return this.lightningRate.getValue().floatValue();
   }

   public float getAlpha() {
      return this.usesCoreShader() ? 1.0F : this.alpha.getValue().floatValue();
   }

   public int getThemeColor() {
      String var1 = this.mode.getValue();
      int var2 = this.getColor();
      float var3 = this.getIntensity();
      if (!var1.equals(this.cachedMode) || var2 != this.cachedColor || var3 != this.cachedIntensity) {
         this.cachedMode = var1;
         this.cachedColor = var2;
         this.cachedIntensity = var3;
         this.cachedThemeColor = BackgroundThemeColors.themeColor(var1, var2, var3);
      }

      return this.cachedThemeColor;
   }

   public boolean shouldRender() {
      if (!this.isEnabled()) {
         return false;
      }

      CustomSkyModule var1 = CustomSkyModule.getInstance();
      return this.isRuntimeActive()
         && this.isInGame()
         && (var1 == null || !var1.isOperational())
         && CustomSkyModule.isShowVanillaSun(getMc().level.dimensionType().skybox())
         && getMc().gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE
         && (this.reloadCount != PostProcessing.reloadCount || !this.failedModes.contains(this.mode.getValue()));
   }

   public void usesCoreShader() {
      this.rendered = (boolean)0;
   }

   public boolean getAppearanceSettings() {
      return !this.isEnabled() ? false : this.rendered && this.shouldRender() && this.mode.getValue().equals(this.renderedMode);
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("SkyShader", "Enabled; mode={}; CustomSky takes precedence when enabled", this.mode.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   @Override
   public void onTick() {
      if (this.lastLevel != getMc().level) {
         this.reset();
         this.lastLevel = getMc().level;
      }

      CustomSkyModule var1 = CustomSkyModule.getInstance();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      boolean var4 = false;
      int var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.customSkyActive;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            LarpLog.info("SkyShader", "CustomSky is enabled; Larp sky is suspended without changing either preference");
         }
      }

      this.customSkyActive = (boolean)var2;
      boolean var8 = false;
      int var7 = var2;
      var10000 = Integer.signum(var7);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.closeLayers();
         this.lastRenderTime = Double.NaN;
      }
   }

   public boolean renderSky(RenderTarget var1) {
      if (this.isEnabled()) {
         this.rendered = (boolean)0;
         if (!this.shouldRender()) {
            return false;
         }

         String var2 = this.mode.getValue();

         try {
            if (this.reloadCount != PostProcessing.reloadCount) {
               this.reset();
               this.reloadCount = PostProcessing.reloadCount;
               this.lastLevel = getMc().level;
            }

            if (!var2.equals(this.lastMode)) {
               this.lastRenderTime = Double.NaN;
               this.lastMode = var2;
               LarpLog.info("SkyShader", "Rendering Larp shader {}; full-screen coverage, vanilla celestial/dark-disc/cloud suppression after success", var2);
            }
            String var3 = switch (var2) {
               case "Core Caustic" -> "core_caustic";
               case "Core Nebula" -> "core_nebula";
               default -> var2.toLowerCase(Locale.ROOT);
            };
            SkyShaderLayer var13 = this.layers.computeIfAbsent(var2, var2x -> new SkyShaderLayer(var3, this));
            double var14 = TickTimer.getTime();
            this.renderContext.partialTick = Double.isNaN(this.lastRenderTime) ? 0.0 : Math.clamp(var14 - this.lastRenderTime, 0.0, 0.1);
            this.lastRenderTime = var14;
            this.renderContext.target = var1;
            var13.render(this.renderContext);
            this.rendered = (boolean)1;
            this.renderedMode = var2;
            return true;
         } catch (RuntimeException | IOException var11) {
            this.failedModes.add(var2);
            this.closeLayers();
            LarpLog.error("SkyShader", var2 + " failed; vanilla sky until resource reload or disable/enable", var11);
            return false;
         } finally {
            this.renderContext.target = null;
         }
      } else {
         return false;
      }
   }

   private void closeLayers() {
      for (SkyShaderLayer var2 : this.layers.values()) {
         var2.close();
      }

      this.renderContext.state.close();
   }

   public void reset() {
      this.rendered = (boolean)0;
      this.renderedMode = null;
      this.closeLayers();
      this.layers.clear();
      this.failedModes.clear();
      this.reloadCount = -1;
      this.lastLevel = null;
      this.lastMode = null;
      this.lastRenderTime = Double.NaN;
      this.customSkyActive = (boolean)0;
   }
}
