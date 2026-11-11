package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.particle.OrbitSparkleEffect;
import com.larpclient.util.misc.LarpLog;

public final class AmbientParticlesModule extends Module {
   private static AmbientParticlesModule instance;
   public final ModeSetting style = this.addSetting(new ModeSetting("Style", "Flying world ambience", "Both", "Both", "Sparkles", "Comets"));
   public final NumberSetting radius = this.addNumberSetting("Radius", "Spawn region in blocks", 12.0, 4.0, 32.0, 1.0);
   public final NumberSetting height = this.addNumberSetting("Height", "Spawn region height in blocks", 10.0, 2.0, 24.0, 1.0);
   public final NumberSetting density = this.addNumberSetting("Density", "Sparkles per second; fills the region on enable", 160.0, 0.0, 512.0, 1.0);
   public final NumberSetting lifetime = this.addNumberSetting("Lifetime", "Sparkle lifetime in seconds", 5.0, 1.0, 8.0, 0.25);
   public final NumberSetting speed = this.addNumberSetting("Speed", "Motion multiplier; zero freezes flight", 1.5, 0.0, 6.0, 0.1);
   public final NumberSetting verticalTravel = this.addNumberSetting("Vertical Travel", "Peak-to-peak rise and fall in blocks", 3.0, 0.0, 8.0, 0.25);
   public final NumberSetting sidewaysTravel = this.addNumberSetting("Sideways Travel", "Peak-to-peak sideways motion in blocks", 2.5, 0.0, 8.0, 0.25);
   public final NumberSetting motionPeriod = this.addNumberSetting("Motion Period", "Seconds per rise and fall at Speed 1", 5.0, 1.0, 12.0, 0.25);
   public final NumberSetting maxParticles = this.addNumberSetting(
      "Maximum Particles", "Hard ambient sprite limit, independent of combat", 1536.0, 128.0, 2048.0, 64.0
   );
   public final NumberSetting size = this.addNumberSetting("Size", "Sparkle radius in blocks", 0.05, 0.015, 0.15, 0.005);
   public final NumberSetting cometCount = this.addNumberSetting("Comet Count", "Maximum simultaneous flying comets", 12.0, 0.0, 32.0, 1.0);
   public final NumberSetting trailLength = this.addNumberSetting("Trail Length", "Comet history in seconds", 0.8, 0.2, 2.0, 0.1);
   public final NumberSetting trailWidth = this.addNumberSetting("Trail Width", "Comet ribbon width in blocks", 0.025, 0.01, 0.08, 0.005);
   public final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   public final ColorSetting color = this.addSetting(new ColorSetting("Color", "Sparkles and comet heads", -1517057));
   public final ColorSetting tailColor = this.addSetting(new ColorSetting("Tail Color", "Custom comet tails and sparkle fade", -6794006));
   public final NumberSetting opacity = this.addNumberSetting("Opacity", "Ambient effect strength", 0.7, 0.1, 1.0, 0.05);
   public final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Shared world glow", true));
   private final OrbitSparkleEffect effect = new OrbitSparkleEffect();

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public AmbientParticlesModule() {
      super("Ambient Particles", "Add sparkles and comets around you", Category.VISUALS, -1);
      instance = this;
      this.density.visibleWhen(() -> !this.style.isMode("Comets"));
      this.lifetime.visibleWhen(() -> !this.style.isMode("Comets"));

      for (Setting var4 : new Setting[]{this.cometCount, this.trailLength, this.trailWidth}) {
         var4.visibleWhen(() -> !this.style.isMode("Sparkles"));
      }

      this.motionPeriod.visibleWhen(() -> this.verticalTravel.getValue() > 0.0 || this.sidewaysTravel.getValue() > 0.0);
      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.tailColor.visibleWhen(() -> !this.themeColors.getValue());
      this.themeColors.onChange(var0 -> LarpLog.info("Ambient Particles", "Theme colors: {}", var0));
      this.style.onChange(var1 -> this.resetEffect());
   }

   public OrbitSparkleEffect.SparkleSettings buildSparkleSettings() {
      return new OrbitSparkleEffect.SparkleSettings(
         !this.style.isMode("Comets"),
         !this.style.isMode("Sparkles"),
         this.radius.getValue(),
         this.height.getValue(),
         this.density.getValue(),
         this.lifetime.getValue(),
         this.speed.getValue(),
         this.size.getValue(),
         this.cometCount.getValue().intValue(),
         this.trailLength.getValue(),
         this.trailWidth.getValue(),
         this.themeColors.getValue() ? Theme.accent : this.color.getValue(),
         this.themeColors.getValue() ? Theme.accentEnd : this.tailColor.getValue(),
         this.opacity.getValue(),
         this.bloom.getValue(),
         this.verticalTravel.getValue(),
         this.sidewaysTravel.getValue(),
         this.motionPeriod.getValue(),
         this.maxParticles.getValue().intValue()
      );
   }

   public static AmbientParticlesModule getInstance() {
      return instance;
   }

   public OrbitSparkleEffect getEffect() {
      return this.effect;
   }

   public void resetEffect() {
      this.effect.reset();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = minecraft.isPaused();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.effect.tick(minecraft.gameRenderer.mainCamera().position(), this.buildSparkleSettings());
         }
      }
   }

   @Override
   public void onEnable() {
      this.resetEffect();
      LarpLog.info(
         "Ambient Particles",
         "Enabled {}; density={}/s; sprite limit={}; comets={}; vertical travel={} blocks",
         this.style.getValue(),
         this.density.getValue(),
         this.maxParticles.getValue(),
         this.cometCount.getValue(),
         this.verticalTravel.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.resetEffect();
   }
}
