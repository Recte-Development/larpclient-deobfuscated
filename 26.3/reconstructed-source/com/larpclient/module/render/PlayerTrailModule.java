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
import com.larpclient.render.entity.BodyTrailEngine;
import com.larpclient.render.entity.GhostEcho;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.world.TrailRenderer;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public final class PlayerTrailModule extends Module {
   private static final int eMITTER_ID = 3;
   private final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Movement effect", "Ribbon", "Ribbon", "Glow", "Comet", "Ghost Echo", "Helix", "Stardust")
   );
   private final ModeSetting alphaCurve = this.addSetting(
      new ModeSetting("Alpha Curve", "Trail lifetime fade", "LINEAR", "LINEAR", "SMOOTH", "FADE_IN_OUT", "HOLD_FADE", "QUICK_FADE")
   );
   private final ModeSetting widthCurve = this.addSetting(
      new ModeSetting("Width Curve", "Strand taper", "TAPER_TAIL", "CONSTANT", "TAPER_TAIL", "TAPER_BOTH", "HEAD_HEAVY")
   );
   private final NumberSetting snapshotInterval = this.addSetting(new NumberSetting("Snapshot Interval", "Seconds between frozen poses", 1.2, 0.1, 4.0, 0.1));
   private final NumberSetting ghostLifetime = this.addSetting(new NumberSetting("Ghost Lifetime", "Seconds until fully faded", 2.4, 0.3, 8.0, 0.1));
   private final NumberSetting maxGhosts = this.addSetting(new NumberSetting("Maximum Ghosts", "Hard snapshot limit", 6.0, 1.0, 12.0, 1.0));
   private final NumberSetting ghostOpacity = this.addSetting(new NumberSetting("Ghost Opacity", "Starting opacity", 0.45, 0.05, 0.9, 0.05));
   private final ModeSetting ghostStyle = this.addSetting(
      new ModeSetting("Ghost Style", "Snapshot material", "Original", "Original", "Theme Tint", "Custom Tint", "Hologram", "Silhouette")
   );
   private final ModeSetting ghostFade = this.addSetting(new ModeSetting("Ghost Fade", "Lifetime opacity", "Smooth", "Smooth", "Linear"));
   private final BooleanSetting player = this.addSetting(new BooleanSetting("Player", "Trail behind yourself", true));
   private final BooleanSetting entities = this.addSetting(new BooleanSetting("Entities", "Nearby living entities", false));
   private final BooleanSetting projectiles = this.addSetting(new BooleanSetting("Projectiles", "Arrows and projectiles", true));
   private final BooleanSetting fullBody = this.addSetting(new BooleanSetting("Full Body", "Span the whole body instead of a low band at the feet", true));
   private final NumberSetting width = this.addSetting(new NumberSetting("Width", "Strand thickness", 0.12, 0.02, 0.6, 0.01));
   private final NumberSetting lifetime = this.addSetting(new NumberSetting("Lifetime", "Seconds", 1.2, 0.2, 5.0, 0.1));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Submit glow mask", true));
   private final BooleanSetting smoothing = this.addSetting(new BooleanSetting("Smoothing", "Smooth trail curves", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Head color", Theme.accent));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "Tail color", Theme.accentEnd));
   private final BooleanSetting trailParticles = this.addSetting(
      new BooleanSetting("Trail Particles", "Style particles shed from the whole body while moving", true)
   );
   private final NumberSetting particleDensity = this.addSetting(new NumberSetting("Particle Density", "Particles per block travelled", 1.0, 0.25, 3.0, 0.05));
   private final HitEffectConfig particleConfig = new HitEffectConfig();

   public PlayerTrailModule() {
      super("Player Trail", "Leave fading trails and player copies", Category.VISUALS, -1);

      for (Setting var4 : new Setting[]{this.snapshotInterval, this.ghostLifetime, this.maxGhosts, this.ghostOpacity, this.ghostStyle, this.ghostFade}) {
         var4.visibleWhen(() -> this.style.isMode("Ghost Echo"));
      }

      for (Setting var8 : new Setting[]{
         this.alphaCurve, this.player, this.entities, this.projectiles, this.fullBody, this.width, this.lifetime, this.smoothing, this.gradient
      }) {
         var8.visibleWhen(() -> !this.style.isMode("Ghost Echo"));
      }

      this.particleDensity.visibleWhen(this.trailParticles::getValue);
      this.color
         .visibleWhen(
            () -> !this.style.isMode("Ghost Echo")
               || this.ghostStyle.isMode("Custom Tint")
               || this.ghostStyle.isMode("Hologram")
               || this.ghostStyle.isMode("Silhouette")
         );
      this.widthCurve.visibleWhen(() -> !this.style.isMode("Ghost Echo") && !this.style.isMode("Comet") && !this.style.isMode("Stardust"));
      this.style.onChange(var0 -> {
         VisualsRenderer.particles.removeByEmitter(3);
         VisualsRenderer.trails.clear();
         VisualsRenderer.bodyTrails.clear();
         VisualsRenderer.ghostEcho.clear();
         LarpLog.info("Trails", "Style changed: {}", var0);
      });
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.style, this.player, this.entities, this.projectiles, this.fullBody, this.ghostStyle);
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.width, this.color, this.gradient, this.bloom, this.ghostOpacity);
   }

   public List<Setting<?>> getTimingSettings() {
      return List.of(
         this.lifetime,
         this.alphaCurve,
         this.widthCurve,
         this.smoothing,
         this.trailParticles,
         this.particleDensity,
         this.snapshotInterval,
         this.ghostLifetime,
         this.ghostFade
      );
   }

   public List<Setting<?>> getLimitSettings() {
      return List.of(this.maxGhosts);
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "Trails",
         "Style: {}; full body: {}; particles: {} at density {}",
         this.style.getValue(),
         this.fullBody.getValue(),
         this.trailParticles.getValue(),
         this.particleDensity.getValue()
      );
      LarpLog.info(
         "GhostEcho",
         "Snapshot interval: {}s; lifetime: {}s; limit: {}",
         this.snapshotInterval.getValue(),
         this.ghostLifetime.getValue(),
         this.maxGhosts.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.style.isMode("Ghost Echo")) {
            GhostEcho var16 = VisualsRenderer.ghostEcho;
            var16.spawnInterval = this.snapshotInterval.getValue();
            var16.lifetime = this.ghostLifetime.getValue();
            var16.maxEchoes = this.maxGhosts.getValue().intValue();
            var16.opacity = this.ghostOpacity.getValue();
            var16.style = this.ghostStyle.getValue();
            var16.fadeMode = this.ghostFade.getValue();
            var16.bloom = this.bloom.getValue();
            var16.color = this.color.getValue();
            var16.requestCapture();
            int var20 = this.trailParticles.getValue();
            boolean var11 = false;
            byte var10 = (byte)var20;
            var20 = Integer.signum(var10);
            if (((var20 | -var20) >>> 31 ^ 1) == 0) {
               this.spawnTrailParticles(minecraft.player, minecraft.player.getBbHeight(), 1.0);
            }
         } else {
            int var10000 = this.style.isMode("Comet");
            boolean var9 = false;
            byte var8 = (byte)var10000;
            var10000 = Integer.signum(var8);
            TrailRenderer.WidthCurve var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0
               ? TrailRenderer.WidthCurve.CUBIC
               : (this.style.isMode("Stardust") ? TrailRenderer.WidthCurve.QUADRATIC : TrailRenderer.WidthCurve.valueOf(this.widthCurve.getValue()));
            BodyTrailEngine var2 = VisualsRenderer.bodyTrails;
            var2.style = this.getBodyTrailStyle();
            var2.easing = Easing.FadeCurve.valueOf(this.alphaCurve.getValue());
            var2.fadeMode = var1;
            var2.width = this.width.getValue();
            var2.lifetimeSeconds = this.lifetime.getValue();
            var2.showThroughWalls = this.bloom.getValue();
            var2.smooth = this.smoothing.getValue();
            var2.startColor = this.color.getValue();
            var2.endColor = this.gradient.getValue();
            TrailRenderer var3 = VisualsRenderer.trails;
            var3.alphaCurve = var2.easing;
            int var10001 = this.style.isMode("Ribbon");
            boolean var13 = false;
            byte var12 = (byte)var10001;
            var10001 = Integer.signum(var12);
            var3.animated = (boolean)((var10001 | -var10001) >>> 31 == 0 ? 1 : 0);
            var3.widthCurve = var1;
            var3.blendMode = this.style.isMode("Ribbon") ? HitEffectConfig.BlendMode.NORMAL : HitEffectConfig.BlendMode.ADDITIVE;
            var3.width = this.width.getValue();
            var3.lifetime = this.lifetime.getValue();
            var3.glow = this.bloom.getValue();
            var3.smoothing = this.smoothing.getValue();
            var3.colorFrom = this.color.getValue();
            var3.colorTo = this.gradient.getValue();
            int var18 = this.player.getValue();
            boolean var15 = false;
            byte var14 = (byte)var18;
            var18 = Integer.signum(var14);
            if (((var18 | -var18) >>> 31 ^ 1) == 0) {
               this.addTrailPoint(minecraft.player, 1.0);
            }

            int var4 = 0;
            int var5 = 0;

            for (Entity var7 : minecraft.level.entitiesForRendering()) {
               if (var7 != minecraft.player && !(var7.distanceToSqr(minecraft.player) > 2304.0)) {
                  if (this.entities.getValue() && var7 instanceof LivingEntity && var4 < 15) {
                     this.addTrailPoint(var7, 0.35);
                     var4++;
                  } else if (this.projectiles.getValue() && var7 instanceof Projectile) {
                     var3.addPoint(var7.getId(), var7.position().add(0.0, 0.15, 0.0));
                     if (++var5 >= 31) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private BodyTrailEngine.Style getBodyTrailStyle() {
      return switch ((String)this.style.getValue()) {
         case "Glow" -> BodyTrailEngine.Style.RIBBON;
         case "Comet" -> BodyTrailEngine.Style.WINGS;
         case "Helix" -> BodyTrailEngine.Style.HELIX;
         case "Stardust" -> BodyTrailEngine.Style.SIMPLE;
         default -> BodyTrailEngine.Style.AURORA;
      };
   }

   private void addTrailPoint(Entity var1, double var2) {
      double var4 = this.fullBody.getValue() ? var1.getBbHeight() : 0.3;
      VisualsRenderer.bodyTrails.addPoint(var1.getId(), var1.position(), var4, var1.getBbWidth());
      if (this.trailParticles.getValue()) {
         this.spawnTrailParticles(var1, var4, var2);
      }
   }

   private void spawnTrailParticles(Entity var1, double var2, double var4) {
      Vec3 var6 = var1.position();
      Vec3 var7 = new Vec3(var1.xo, var1.yo, var1.zo);
      double var8 = var6.distanceTo(var7);
      if (var8 > 4.0) {
         var7 = var6;
         var8 = 0.0;
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();

      double var11 = switch ((String)this.style.getValue()) {
         case "Glow" -> 8.0;
         case "Comet" -> 9.0;
         case "Helix" -> 6.0;
         case "Stardust" -> 12.0;
         default -> 4.0;
      };
      double var25 = this.style.isMode("Stardust") ? 0.12 : 0.0;
      double var15 = (var11 * var8 + var25) * this.particleDensity.getValue() * var4;
      int var17 = (int)var15 + (var10.nextDouble() < var15 - (int)var15 ? 1 : 0);
      if (var17 != 0) {
         HitEffectConfig var18 = this.particleConfig;
         this.configureParticles(var18, var2);
         var18.scale = var18.opacity = var1.getBbWidth() * 1.15;
         double var19 = this.style.isMode("Comet") ? 7.0 : (this.style.isMode("Stardust") ? 1.2 : 3.0);
         var18.pitchOffset = (var7.x - var6.x) * var19;
         var18.spin = (var7.z - var6.z) * var19;
         double var21 = this.style.isMode("Comet") ? 0.55 : 0.5;

         for (int var23 = 0; var23 < Math.min(var17, 24); var23++) {
            var18.style = this.addTrailPoint(var10.nextDouble());
            Vec3 var24 = var7.lerp(var6, var10.nextDouble()).add(0.0, var2 * var21, 0.0);
            VisualsRenderer.particles.spawn(var24, var18);
         }
      }
   }

   private void configureParticles(HitEffectConfig var1, double var2) {
      int var4 = this.color.getValue();
      int var5 = this.gradient.getValue();
      var1.seed = 3;
      var1.particleCount = 1;
      var1.shape = HitEffectConfig.Shape.SPHERE;
      var1.stretch = var2;
      var1.blendMode = HitEffectConfig.BlendMode.ADDITIVE;
      var1.enabled = this.bloom.getValue();
      var1.maxParticles = (int)Math.round(140.0 + 260.0 * this.particleDensity.getValue());
      var1.primaryColor = ColorUtil.multiplyAlpha(var4, 0.9);
      var1.secondaryColor = ColorUtil.multiplyAlpha(var5, 0.25);
      var1.turbulence = 0.0;
      var1.brightness = 1.0;
      var1.yawOffset = 0.0;
      var1.rotation = 0.0;
      var1.radius = 3.0;
      switch ((String)this.style.getValue()) {
         case "Glow":
            var1.gravity = 0.025;
            var1.spread = 0.06;
            var1.size = 0.45;
            var1.speed = 0.95;
            var1.intensity = 0.18;
            var1.glow = 0.45;
            var1.lifetime = -0.35;
            var1.fadeIn = 1.6;
            var1.drag = 0.4;
            var1.colorScheme = Easing.FadeCurve.SOFT_ATTACK_SOFT_RELEASE;
            var1.easing = Easing.SizeCurve.SHRINK;
            break;
         case "Comet":
            var1.stretch = var2 * 0.5;
            var1.gravity = 0.018;
            var1.spread = 0.038;
            var1.size = 0.25;
            var1.speed = 0.55;
            var1.intensity = 0.9;
            var1.glow = 0.2;
            var1.lifetime = 0.7;
            var1.fadeIn = 1.8;
            var1.drag = 0.1;
            var1.colorScheme = Easing.FadeCurve.FLASH_CUBIC_DECAY;
            var1.easing = Easing.SizeCurve.SHRINK;
            var1.secondaryColor = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var4, var5, 0.6), 0.3);
            break;
         case "Helix":
            var1.gravity = 0.02;
            var1.spread = 0.045;
            var1.size = 0.5;
            var1.speed = 0.9;
            var1.intensity = 0.45;
            var1.glow = 0.05;
            var1.lifetime = -0.05;
            var1.fadeIn = 1.4;
            var1.drag = 0.15;
            var1.turbulence = 3.2;
            var1.brightness = 1.2;
            var1.colorScheme = Easing.FadeCurve.SOFT_ATTACK_SOFT_RELEASE;
            var1.easing = Easing.SizeCurve.POP;
            break;
         case "Stardust":
            var1.gravity = 0.016;
            var1.spread = 0.05;
            var1.size = 0.8;
            var1.speed = 1.7;
            var1.intensity = 0.06;
            var1.glow = 0.02;
            var1.lifetime = -0.03;
            var1.fadeIn = 2.5;
            var1.drag = 0.15;
            var1.radius = 1.2;
            var1.colorScheme = Easing.FadeCurve.SHARP_ATTACK_SMOOTH_RELEASE;
            var1.easing = Easing.SizeCurve.PULSE;
            var1.primaryColor = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var4, -1, 0.35), 0.95);
            var1.secondaryColor = ColorUtil.multiplyAlpha(var5, 0.35);
            break;
         default:
            var1.gravity = 0.02;
            var1.spread = 0.045;
            var1.size = 0.55;
            var1.speed = 1.0;
            var1.intensity = 0.06;
            var1.glow = 0.08;
            var1.lifetime = -0.04;
            var1.fadeIn = 2.0;
            var1.drag = 0.25;
            var1.colorScheme = Easing.FadeCurve.SOFT_ATTACK_SOFT_RELEASE;
            var1.easing = Easing.SizeCurve.SHRINK;
      }
   }

   private ParticleSystem.Style addTrailPoint(double var1) {
      return switch ((String)this.style.getValue()) {
         case "Glow" -> var1 < 0.72 ? ParticleSystem.Style.GLOW : ParticleSystem.Style.SPARK;
         case "Comet" -> var1 < 0.6 ? ParticleSystem.Style.STREAK : (var1 < 0.85 ? ParticleSystem.Style.SPARK : ParticleSystem.Style.STAR);
         case "Helix" -> var1 < 0.5 ? ParticleSystem.Style.TRIANGLE : ParticleSystem.Style.GLOW;
         case "Stardust" -> var1 < 0.5 ? ParticleSystem.Style.STAR : (var1 < 0.75 ? ParticleSystem.Style.TRIANGLE : ParticleSystem.Style.GLOW);
         case "Ghost Echo" -> var1 < 0.6 ? ParticleSystem.Style.TRIANGLE : ParticleSystem.Style.GLOW;
         default -> var1 < 0.7 ? ParticleSystem.Style.CIRCLE : ParticleSystem.Style.GLOW;
      };
   }

   @Override
   public void onDisable() {
      VisualsRenderer.particles.removeByEmitter(3);
      VisualsRenderer.trails.clear();
      VisualsRenderer.bodyTrails.clear();
      VisualsRenderer.ghostEcho.clear();
   }
}
