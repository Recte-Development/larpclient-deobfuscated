package com.larpclient.module.render;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.world.WorldColorRenderer;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.BlockRaycast;
import com.larpclient.util.world.SurfaceSample;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class HitFXModule extends Module {
   private static HitFXModule instance;
   private final ModeSetting preset = this.addSetting(
      new ModeSetting(
         "Preset", "Choose a complete impact style; its behavior is explained below", "Clean", "Clean", "Energy", "Spark", "Petal", "Heavy", "Cash", "Bloom"
      )
   );
   private final BooleanSetting screenImpact = this.addSetting(new BooleanSetting("Screen Impact", "Brief subtle exposure and edge pulse", false));
   private final NumberSetting ringThickness = this.addSetting(new NumberSetting("Ring Thickness", "Shockwave width", 0.045, 0.01, 0.2, 0.005));
   private Vec3 lastNormal = Vec3.ZERO;
   private final HitEffectConfig effectConfig = new HitEffectConfig();
   private final ModeSetting particleType = this.addSetting(
      new ModeSetting(
         "Particle Type",
         "Accent shape for Clean; the other presets choose their own shapes",
         "Star",
         "Spark",
         "Glow",
         "Circle",
         "Star",
         "Triangle",
         "Streak",
         "Textured"
      )
   );
   private final NumberSetting amount = this.addSetting(
      new NumberSetting("Amount", "Burst density: higher values emit more particles in every layer", 18.0, 1.0, 100.0, 1.0)
   );
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "World-space particle size", 0.08, 0.02, 0.3, 0.01));
   private final NumberSetting lifetime = this.addSetting(new NumberSetting("Lifetime", "Seconds", 0.8, 0.2, 4.0, 0.1));
   private final NumberSetting maxParticles = this.addSetting(
      new NumberSetting("Maximum", "HitFX particle limit; raise only if repeated bursts reach the limit", 1000.0, 100.0, 2000.0, 100.0)
   );
   private final NumberSetting distance = this.addSetting(new NumberSetting("Distance", "Render distance", 64.0, 8.0, 128.0, 8.0));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Start color", Theme.accent));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "End color", Theme.accentEnd));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Submit glow mask", true));
   private final BooleanSetting particles = this.addSetting(new BooleanSetting("Particles", "Emit particles", true));
   private final BooleanSetting hitRings = this.addSetting(new BooleanSetting("Hit Rings", "Expanding surface or airborne impact rings", true));
   private final NumberSetting ringSize = this.addSetting(new NumberSetting("Ring Size", "Final radius", 0.8, 0.2, 2.0, 0.1));
   private final NumberSetting ringSpeed = this.addSetting(new NumberSetting("Ring Speed", "Expansion multiplier", 1.0, 0.5, 3.0, 0.1));
   private final BooleanSetting entityHitColor = this.addSetting(new BooleanSetting("Entity Hit Color", "Tint hurt entity models", false));
   private final BooleanSetting hitTrails = this.addSetting(new BooleanSetting("Hit Trails", "Additional streaks on impact", true));
   private final NumberSetting ringFade = this.addSetting(new NumberSetting("Ring Fade", "Ring lifetime in seconds", 0.7, 0.2, 3.0, 0.1));
   private final NumberSetting bloomCount = this.addSetting(getInstance("Bloom Count", "Soft sprites per impact", 15.0, 3.0, 30.0, 1.0));
   private final NumberSetting bloomSize = this.addSetting(getInstance("Bloom Size", "Full sprite width in blocks", 0.25, 0.05, 0.5, 0.01));
   private final NumberSetting bloomLifetime = this.addSetting(getInstance("Bloom Lifetime", "Seconds before the sprites expire", 1.5, 0.5, 3.0, 0.1));
   private final NumberSetting bloomSpread = this.addSetting(getInstance("Bloom Spread", "Initial travel in blocks per tick", 0.08, 0.02, 0.15, 0.01));
   private final NumberSetting bloomGravity = this.addSetting(getInstance("Bloom Gravity", "Downward acceleration per tick", 0.015, 0.005, 0.03, 0.001));
   private final NumberSetting bloomFadeSpeed = this.addSetting(getInstance("Bloom Fade Speed", "Higher values fade faster", 1.0, 0.3, 2.0, 0.1));
   private final BooleanSetting bloomColorAnimation = this.addSetting(
      new BooleanSetting("Bloom Color Animation", "Cycle each sprite through rainbow colors", false)
   );
   private Object lastPlayer;
   private Object lastLevel;
   private int lastHurtTick = -1;

   @Override
   public boolean isOperational() {
      return this.isEnabled() ? this.isOperational() : false;
   }

   @Override
   public boolean isRuntimeActive() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean isEntityHitColorEnabled() {
      return !this.isEnabled() ? false : this.isOperational() && this.entityHitColor.getValue();
   }

   public int getPrimaryColor() {
      return this.themeColors.getValue() ? Theme.accent : this.color.getValue();
   }

   private int getSecondaryColor() {
      return this.themeColors.getValue() ? Theme.accentEnd : this.gradient.getValue();
   }

   private static NumberSetting getInstance(String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : (var8 >= 0.1 ? "%.1f" : (var8 >= 0.01 ? "%.2f" : "%.3f")), this.getValue());
         }
      };
   }

   public HitFXModule() {
      super("HitFX", "Add particles and rings to hits", Category.VISUALS, -1);
      instance = this;

      for (Setting var4 : new Setting[]{this.ringSize, this.ringSpeed, this.ringFade, this.ringThickness}) {
         var4.visibleWhen(() -> this.hitRings.getValue());
      }

      this.particleType.visibleWhen(() -> this.particles.getValue() && this.preset.isMode("Clean"));

      for (Setting var11 : new Setting[]{this.amount, this.size, this.lifetime, this.hitTrails}) {
         var11.visibleWhen(() -> this.particles.getValue() && !this.preset.isMode("Bloom"));
      }

      for (Setting var12 : new Setting[]{
         this.bloomCount, this.bloomSize, this.bloomLifetime, this.bloomSpread, this.bloomGravity, this.bloomFadeSpeed, this.bloomColorAnimation
      }) {
         var12.visibleWhen(() -> this.particles.getValue() && this.preset.isMode("Bloom"));
      }

      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.gradient.visibleWhen(() -> !this.themeColors.getValue() && (this.hitRings.getValue() || this.particles.getValue() && !this.preset.isMode("Bloom")));
      this.bloomColorAnimation.visibleWhen(() -> !this.themeColors.getValue() && this.particles.getValue() && this.preset.isMode("Bloom"));
      this.themeColors.onChange(var0 -> LarpLog.info("HitFX", "Theme colors: {}", var0));
      this.preset.onChange(var0 -> LarpLog.info("HitFX", "Preset changed: {}", var0));
   }

   public Setting<?> describePreset() {
      return this.particles;
   }

   public static HitFXModule getInstance() {
      return instance;
   }

   public Setting<?> getPresetSetting() {
      return this.preset;
   }

   public List<Setting<?>> getParticleSettings() {
      return List.of(
         this.preset,
         this.particles,
         this.particleType,
         this.amount,
         this.size,
         this.lifetime,
         this.hitTrails,
         this.bloomCount,
         this.bloomSize,
         this.bloomLifetime
      );
   }

   public List<Setting<?>> getRingToggleSettings() {
      return List.of(this.hitRings, this.ringSize, this.ringSpeed);
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.themeColors, this.color, this.gradient, this.bloom, this.bloomColorAnimation);
   }

   public List<Setting<?>> getAdvancedSettings() {
      return List.of(
         this.ringThickness,
         this.ringFade,
         this.bloomSpread,
         this.bloomGravity,
         this.bloomFadeSpeed,
         this.screenImpact,
         this.entityHitColor,
         this.maxParticles,
         this.distance
      );
   }

   public String describePreset() {
      return switch ((String)this.preset.getValue()) {
         case "Bloom" -> "Bloom: soft glowing sprites with tumbling drift, gravity and a smooth fade. Choose a fixed color or animated rainbow.";
         case "Energy" -> "Energy: bright energy flares, drifting light and a delayed second ring.";
         case "Spark" -> "Spark: a dense spray of narrow sparks and streaks that slow and fade around the hit.";
         case "Petal" -> "Petal: a cloud of gently turning petals with light sparks and a soft glow.";
         case "Cash" -> "Cash: tumbling $100 bills with printed fronts and backs, curved paper and a gentle falling flutter.";
         case "Heavy" -> "Heavy: chunky tumbling shards, stronger outward motion and a delayed second ring.";
         default -> "Clean: a full, balanced burst of colorful accents with light sparks. Particle Type changes its accent shape.";
      };
   }

   @Override
   public void onEnable() {
      this.lastPlayer = this.lastLevel = null;
      this.lastHurtTick = -1;
      LarpLog.info(
         "HitFX", "Impact style={}; contact-local bursts, surface/airborne rings and tumbling Heavy shards; particle counts retained", this.preset.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.preset.isMode("Bloom");
         boolean var3 = false;
         byte var2 = (byte)var10000;
         var10000 = Integer.signum(var2);
         if ((var10000 | -var10000) >>> 31 != 0) {
            LocalPlayer var1 = this.getPlayer();
            if (this.lastPlayer != var1 || this.lastLevel != getMc().level) {
               this.lastPlayer = var1;
               this.lastLevel = getMc().level;
               this.lastHurtTick = -1;
            }

            byte var5 = 10;
            int var4 = var1.hurtTime;
            var10000 = Integer.compare(var4, var5);
            if ((var10000 | -var10000) >>> 31 == 0) {
               int var7 = this.lastHurtTick;
               int var6 = var1.tickCount;
               var10000 = Integer.compare(var6, var7);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.lastHurtTick = var1.tickCount;
                  this.spawnAtEntity(var1);
               }
            }

            return;
         }
      }

      this.lastPlayer = this.lastLevel = null;
      this.lastHurtTick = -1;
   }

   public void spawnAtEntity(LivingEntity var1) {
      if (this.isEnabled()) {
         LocalPlayer var2 = this.getPlayer();
         Vec3 var3 = var2 != null && var2 != var1 ? var2.getEyePosition() : var1.getBoundingBox().getCenter();
         Vec3 var4 = var2 != null && var2 != var1 ? var2.getViewVector(1.0F) : new Vec3(0.0, 0.0, 1.0);
         SurfaceSample var5 = SurfaceSample.sample(var1.getBoundingBox(), var3, var4, (var0, var1x) -> BlockRaycast.raycastBlock(getMc().level, var0, var1x));
         this.spawnBurst(VisualsRenderer.particles, var1.position(), var1.getBbWidth(), var1.getBbHeight(), var5);
      }
   }

   public void spawnAtPoint(ParticleSystem var1, Vec3 var2, double var3, double var5, Vec3 var7) {
      if (this.isEnabled()) {
         this.spawnBurst(var1, var2, var3, var5, new SurfaceSample(var2.add(0.0, var5 * 0.5, 0.0), var7, var2.add(0.0, 0.035, 0.0), new Vec3(0.0, 1.0, 0.0)));
      }
   }

   public void spawnBurst(ParticleSystem var1, Vec3 var2, double var3, double var5, SurfaceSample var7) {
      if (this.isEnabled()) {
         this.lastNormal = var7.normal();
         this.effectConfig.maxParticles = this.maxParticles.getValue().intValue();
         this.effectConfig.heightOverride = this.distance.getValue();
         Vec3 var8 = var7.point();
         this.effectConfig.seed = 1;
         this.effectConfig.shape = HitEffectConfig.Shape.RING;
         this.effectConfig.scale = this.effectConfig.opacity = Math.max(0.08, var3 * 0.45);
         this.effectConfig.stretch = Math.min(0.4, var5 * 0.22);
         this.effectConfig.primaryColor = this.getPrimaryColor();
         this.effectConfig.secondaryColor = this.getSecondaryColor();
         this.effectConfig.enabled = this.bloom.getValue();
         this.effectConfig.tilt = 0.0;
         this.effectConfig.mode = HitEffectConfig.Mode.DEFAULT;
         this.effectConfig.durationOverride = var7.groundY();
         this.effectConfig.offset = var7.ringNormal();
         this.effectConfig.mirrored = this.effectConfig.additive = (boolean)0;
         this.effectConfig.globalScale = 1.0;
         this.effectConfig.maxDistance = Double.NaN;
         double var9 = this.amount.getValue() / 18.0;
         double var11 = this.size.getValue() / 0.08;
         double var13 = this.lifetime.getValue() / 0.8;
         boolean var15 = this.preset.isMode("Heavy");
         boolean var16 = this.preset.isMode("Petal");
         boolean var17 = this.preset.isMode("Energy");
         boolean var18 = this.preset.isMode("Spark");
         this.effectConfig.texture = var17 ? HitEffectConfig.Texture.HITFX : HitEffectConfig.Texture.EFFECTS;
         if (this.particles.getValue() && this.preset.isMode("Bloom")) {
            this.effectConfig.texture = HitEffectConfig.Texture.BLOOM;
            this.effectConfig.shape = HitEffectConfig.Shape.QUAD;
            this.effectConfig.style = ParticleSystem.Style.GLOW;
            this.effectConfig.particleCount = this.bloomCount.getValue().intValue();
            this.effectConfig.gravity = this.effectConfig.spread = this.bloomSize.getValue() * 0.5;
            this.effectConfig.size = this.effectConfig.speed = this.bloomLifetime.getValue();
            this.effectConfig.primaryColor = this.effectConfig.secondaryColor = this.getPrimaryColor();
            this.effectConfig.enabled = this.bloom.getValue();
            this.effectConfig.blendMode = HitEffectConfig.BlendMode.ADDITIVE;
            this.effectConfig.intensity = this.bloomSpread.getValue() * 20.0;
            this.effectConfig.brightness = 1.0;
            this.effectConfig.glow = 0.0;
            this.effectConfig.lifetime = this.bloomGravity.getValue() * 400.0;
            this.effectConfig.fadeIn = -Math.log(0.98) * 20.0;
            this.effectConfig.pitchOffset = this.effectConfig.yawOffset = this.effectConfig.spin = 0.0;
            this.effectConfig.drag = this.effectConfig.rotation = this.effectConfig.turbulence = 0.0;
            this.effectConfig.mirrored = (boolean)1;
            this.effectConfig.additive = (boolean)(!this.themeColors.getValue() && this.bloomColorAnimation.getValue() ? 1 : 0);
            this.effectConfig.colorScheme = Easing.FadeCurve.LINEAR;
            this.effectConfig.globalScale = this.bloomFadeSpeed.getValue();
            this.effectConfig.easing = Easing.SizeCurve.CONSTANT;
            this.effectConfig.maxDistance = Math.toRadians(20.0);
            this.effectConfig.radius = Math.toRadians(80.0);
            var1.spawn(var2.add(0.0, var5 * 0.5 + 0.1, 0.0), this.effectConfig);
         } else if (this.particles.getValue() && this.preset.isMode("Cash")) {
            this.spawnLayer(
               var1,
               var8,
               ParticleSystem.Style.BILL,
               18.0 * var9,
               0.17 * var11,
               0.25 * var11,
               0.9 * var13,
               1.5 * var13,
               1.25,
               1.15,
               0.22,
               HitEffectConfig.BlendMode.NORMAL
            );
         } else if (this.particles.getValue()) {
            this.spawnLayer(
               var1,
               var8,
               ParticleSystem.Style.GLOW,
               (var17 ? 10 : (!var18 && !var16 ? 6 : 4)) * var9,
               0.065 * var11,
               0.13 * var11,
               0.3 * var13,
               0.6 * var13,
               0.35,
               0.05,
               0.35,
               HitEffectConfig.BlendMode.ADDITIVE
            );
            this.spawnLayer(
               var1,
               var8,
               ParticleSystem.Style.SPARK,
               (var16 ? 6 : (var18 ? 36 : (var15 ? 18 : (var17 ? 12 : 8)))) * var9,
               0.018 * var11,
               0.04 * var11,
               0.28 * var13,
               0.55 * var13,
               var15 ? 4.2 : 3.2,
               0.8,
               0.08,
               HitEffectConfig.BlendMode.ADDITIVE
            );
            this.spawnLayer(
               var1,
               var8,
               var16
                  ? ParticleSystem.Style.PETAL
                  : (
                     var15
                        ? ParticleSystem.Style.CRYSTAL
                        : (
                           var17
                              ? ParticleSystem.Style.TRIANGLE
                              : (var18 ? ParticleSystem.Style.STREAK : ParticleSystem.Style.valueOf(this.particleType.getValue()))
                        )
                  ),
               (var16 ? 52 : (var15 ? 38 : (var17 ? 34 : (var18 ? 14 : 36)))) * var9,
               (var18 ? 0.025 : (var15 ? 0.07 : 0.045)) * var11,
               (var18 ? 0.065 : (var15 ? 0.14 : 0.09)) * var11,
               (var18 ? 0.35 : 0.65) * var13,
               (var18 ? 0.7 : 1.2) * var13,
               var16 ? 0.65 : (var15 ? 1.8 : 1.1),
               var16 ? 0.15 : (var15 ? 1.5 : 0.45),
               var16 ? 0.35 : (var17 ? 0.4 : 0.1),
               HitEffectConfig.BlendMode.NORMAL
            );
         }

         if (this.particles.getValue() && this.hitTrails.getValue() && !this.preset.isMode("Bloom")) {
            this.spawnLayer(
               var1,
               var8,
               ParticleSystem.Style.STREAK,
               6.0 * var9,
               0.025 * var11,
               0.055 * var11,
               0.25 * var13,
               0.5 * var13,
               3.0,
               0.1,
               0.0,
               HitEffectConfig.BlendMode.ADDITIVE
            );
         }

         if (this.hitRings.getValue()) {
            this.effectConfig.mirrored = this.effectConfig.additive = (boolean)0;
            this.effectConfig.mode = HitEffectConfig.Mode.DEFAULT;
            this.effectConfig.tilt = 0.0;
            this.effectConfig.globalScale = 1.0;
            this.effectConfig.maxDistance = Double.NaN;
            this.effectConfig.texture = var17 ? HitEffectConfig.Texture.HITFX : HitEffectConfig.Texture.EFFECTS;
            this.effectConfig.primaryColor = this.getPrimaryColor();
            this.effectConfig.secondaryColor = this.getSecondaryColor();
            this.effectConfig.enabled = this.bloom.getValue();
            this.effectConfig.shape = HitEffectConfig.Shape.QUAD;
            this.effectConfig.style = ParticleSystem.Style.RING;
            this.effectConfig.particleCount = 1;
            this.effectConfig.size = this.effectConfig.speed = this.ringFade.getValue();
            this.effectConfig.density = this.ringSize.getValue() * this.ringSpeed.getValue();
            this.effectConfig.jitter = this.ringThickness.getValue();
            this.effectConfig.blendMode = HitEffectConfig.BlendMode.ADDITIVE;
            this.effectConfig.colorScheme = Easing.FadeCurve.FLASH_CUBIC_DECAY;
            var1.spawn(var7.ring(), this.effectConfig);
            if (var15 || var17) {
               this.effectConfig.tilt = 0.075;
               this.effectConfig.density *= 1.25;
               var1.spawn(var7.ring().add(var7.ringNormal().scale(0.005)), this.effectConfig);
            }
         }

         if (this.screenImpact.getValue()) {
            WorldColorRenderer.triggerImpact(this.getPrimaryColor());
         }
      }
   }

   private void spawnLayer(
      ParticleSystem var1,
      Vec3 var2,
      ParticleSystem.Style var3,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      HitEffectConfig.BlendMode var20
   ) {
      int var21 = var3 != ParticleSystem.Style.SPARK && var3 != ParticleSystem.Style.STREAK ? 0 : 1;
      int var22 = var3 == ParticleSystem.Style.GLOW ? 1 : 0;
      this.effectConfig.mode = var3 == ParticleSystem.Style.CRYSTAL ? HitEffectConfig.Mode.BURST : HitEffectConfig.Mode.DEFAULT;
      this.effectConfig.tilt = var22 == 0 && var21 == 0 ? 0.025 : 0.0;
      this.effectConfig.shape = var22 != 0 ? HitEffectConfig.Shape.RING : HitEffectConfig.Shape.STAR;
      this.effectConfig.glow = var3 == ParticleSystem.Style.BILL ? 0.65 : (var3 == ParticleSystem.Style.PETAL ? 0.35 : 0.15);
      this.effectConfig.primaryColor = var21 != 0
         ? ColorUtil.lerpColor(this.getPrimaryColor(), -1, 0.65)
         : ColorUtil.withAlpha(this.getPrimaryColor(), var22 != 0 ? 0.52 : 0.92);
      this.effectConfig.secondaryColor = ColorUtil.withAlpha(this.getSecondaryColor(), var22 != 0 ? 0.16 : 0.65);
      if (var3 == ParticleSystem.Style.BILL) {
         this.effectConfig.primaryColor = this.effectConfig.secondaryColor = -1;
      }

      this.effectConfig.enabled = (boolean)(this.bloom.getValue() && var20 == HitEffectConfig.BlendMode.ADDITIVE ? 1 : 0);
      this.effectConfig.brightness = var21 != 0 ? 0.65 : 1.0;
      double var23 = var3 != ParticleSystem.Style.SPARK && var3 != ParticleSystem.Style.STREAK ? 0.0 : var14 * 0.95;
      this.effectConfig.pitchOffset = this.lastNormal.x * var23;
      this.effectConfig.yawOffset = this.lastNormal.y * var23;
      this.effectConfig.spin = this.lastNormal.z * var23;
      this.effectConfig.style = var3;
      this.effectConfig.particleCount = (int)Math.round(var4);
      this.effectConfig.gravity = var6;
      this.effectConfig.spread = var8;
      this.effectConfig.size = var10;
      this.effectConfig.speed = var12;
      this.effectConfig.intensity = var21 != 0 ? var14 * 0.3 : var14;
      this.effectConfig.lifetime = var16;
      this.effectConfig.drag = var18;
      this.effectConfig.blendMode = var20;
      this.effectConfig.fadeIn = var21 != 0 ? 2.4 : (var3 == ParticleSystem.Style.PETAL ? 0.8 : 1.4);
      this.effectConfig.radius = var3 == ParticleSystem.Style.BILL
         ? 3.2
         : (var3 == ParticleSystem.Style.PETAL ? 2.1 : (var3 == ParticleSystem.Style.CRYSTAL ? 5.0 : 3.0));
      this.effectConfig.colorScheme = var22 != 0 ? Easing.FadeCurve.FLASH_CUBIC_DECAY : Easing.FadeCurve.SHARP_ATTACK_SMOOTH_RELEASE;
      this.effectConfig.easing = var22 != 0 ? Easing.SizeCurve.POP : (var21 != 0 ? Easing.SizeCurve.SHRINK : Easing.SizeCurve.CONSTANT);
      var1.spawn(var2, this.effectConfig);
   }

   @Override
   public void onDisable() {
      this.lastPlayer = this.lastLevel = null;
      this.lastHurtTick = -1;
      VisualsRenderer.particles.removeByEmitter(1);
   }
}
