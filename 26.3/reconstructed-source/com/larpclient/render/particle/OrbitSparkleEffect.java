package com.larpclient.render.particle;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.TrailRenderer;
import com.larpclient.util.math.Easing;
import com.larpclient.util.render.ColorUtil;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class OrbitSparkleEffect {
   private final ParticleSystem particles = new ParticleSystem();
   private final TrailRenderer trails = new TrailRenderer();
   private final Random random = new Random();
   private final Vec3[] cometPositions = new Vec3[32];
   private final Vec3[] cometDirections = new Vec3[32];
   private final Vec3[] cometAnchors = new Vec3[32];
   private final Vec3[] previousPositions = new Vec3[32];
   private final double[] cometAges = new double[32];
   private final double[] cometPhases = new double[32];
   private final double[] cometFades = new double[32];
   private final int[] cometTicks = new int[32];
   private final OrbitSparkleEffect.Sparkle[] glows = new OrbitSparkleEffect.Sparkle[32];
   private int glowCount;
   private boolean hasBloom;
   private final HitEffectConfig emitter = new HitEffectConfig();
   private Vec3 origin;
   private OrbitSparkleEffect.SparkleSettings settings;
   private double spawnAccumulator;
   private int tickCount;

   public OrbitSparkleEffect() {
      this.particles.maxParticles = 1536;
      this.trails.animated = (boolean)1;
      this.emitter.maxParticles = 1536;
      this.emitter.seed = 4;
      this.emitter.lifetime = this.emitter.fadeIn = this.emitter.glow = 0.0;
      this.emitter.colorScheme = Easing.FadeCurve.SOFT_ATTACK_SOFT_RELEASE;
      this.emitter.easing = Easing.SizeCurve.PULSE;
   }

   public void reset() {
      this.particles.clear();
      this.trails.clear();
      Arrays.fill(this.cometPositions, null);
      Arrays.fill(this.cometDirections, null);
      Arrays.fill(this.cometAnchors, null);
      Arrays.fill(this.previousPositions, null);
      Arrays.fill(this.cometAges, 0.0);
      Arrays.fill(this.cometPhases, 0.0);
      Arrays.fill(this.cometFades, 0.0);
      Arrays.fill(this.cometTicks, 0);
      Arrays.fill(this.glows, null);
      this.glowCount = 0;
      this.hasBloom = (boolean)0;
      this.spawnAccumulator = 0.0;
      this.tickCount = 0;
      this.origin = null;
      this.settings = null;
   }

   private Vec3 randomPoint(Vec3 var1, OrbitSparkleEffect.SparkleSettings var2) {
      return randomPointInCylinder(var1, var2.radius, var2.height, this.random);
   }

   public static Vec3 randomPointInCylinder(Vec3 var0, double var1, double var3, Random var5) {
      double var6 = var5.nextDouble() * Math.PI * 2.0;
      double var8 = Math.sqrt(0.04 + var5.nextDouble() * 0.96) * var1;
      return var0.add(Math.cos(var6) * var8, (var5.nextDouble() - 0.5) * var3, Math.sin(var6) * var8);
   }

   public void tick(Vec3 var1, OrbitSparkleEffect.SparkleSettings var2) {
      if (this.origin != null
         && (
            var1.distanceToSqr(this.origin) > 4096.0
               || this.settings.sparkles != var2.sparkles
               || this.settings.comets != var2.comets
               || this.settings.radius != var2.radius
               || this.settings.height != var2.height
               || this.settings.cometCount != var2.cometCount
               || this.settings.maximum != var2.maximum
               || this.settings.verticalTravel != var2.verticalTravel
               || this.settings.sidewaysTravel != var2.sidewaysTravel
               || this.settings.motionPeriod != var2.motionPeriod
         )) {
         this.reset();
      }

      int var3 = this.origin == null ? 1 : 0;
      this.origin = var1;
      this.settings = var2;
      this.tickCount++;
      int var4 = var2.comets ? Math.clamp(var2.cometCount, 0, this.cometPositions.length) : 0;
      this.particles.maxParticles = Math.clamp(var2.maximum, 128, 2048) - var4;
      this.particles.tickScaled(var2.speed);
      this.trails.tick();
      this.emitter.primaryColor = ColorUtil.withAlpha(var2.color, var2.opacity);
      this.emitter.secondaryColor = ColorUtil.withAlpha(var2.tailColor, var2.opacity * 0.35);
      this.emitter.enabled = var2.bloom;
      this.emitter.heightOverride = var2.radius * 2.0 + var2.height + 8.0;
      if (var2.sparkles) {
         int var5 = this.particles.maxParticles - var4 * 3;
         this.emitter.maxParticles = 2048;
         this.spawnAccumulator = this.spawnAccumulator + var2.rate * 0.05;
         int var6 = (int)this.spawnAccumulator + (var3 != 0 ? (int)Math.min(var5, var2.rate * var2.lifetime * 0.825) : 0);
         this.spawnAccumulator = this.spawnAccumulator - (int)this.spawnAccumulator;
         this.emitter.size = var2.lifetime * 0.65;
         this.emitter.speed = var2.lifetime;
         this.emitter.gravity = var2.size * 0.6;
         this.emitter.spread = var2.size;
         this.emitter.colorScheme = Easing.FadeCurve.SOFT_ATTACK_SOFT_RELEASE;
         this.emitter.easing = Easing.SizeCurve.PULSE;
         this.emitter.intensity = this.emitter.drag = 0.0;
         this.emitter.roll = var2.verticalTravel * 0.5;
         this.emitter.delay = var2.sidewaysTravel * 0.5;
         this.emitter.falloff = var2.motionPeriod;
         this.emitter.particleCount = 1;
         int var7 = Math.max(0, var5 - this.particles.getParticleCount());

         for (int var8 = 0; var8 < Math.min(var7, var6); var8++) {
            this.emitter.style = (this.tickCount + var8) % 3 == 0 ? ParticleSystem.Style.STAR : ParticleSystem.Style.GLOW;
            this.particles.spawn(this.randomPoint(var1, var2), this.emitter);
         }
      }

      for (int var11 = 0; var11 < this.cometPositions.length; var11++) {
         if (var11 >= var4) {
            this.cometPositions[var11] = this.cometAnchors[var11] = this.previousPositions[var11] = null;
            this.trails.removeSource(var11);
         } else {
            if (this.cometPositions[var11] != null
               && !(Math.hypot(this.cometAnchors[var11].x - var1.x, this.cometAnchors[var11].z - var1.z) > var2.radius * 1.4)
               && !(Math.abs(this.cometAnchors[var11].y - var1.y) > var2.height * 0.7)
               && !(this.cometAges[var11] > 12.0)) {
               this.previousPositions[var11] = this.cometPositions[var11];
            } else {
               this.trails.removeSource(var11);
               this.cometAnchors[var11] = this.randomPoint(var1, var2);
               double var12 = this.random.nextDouble() * Math.PI * 2.0;
               this.cometDirections[var11] = new Vec3(Math.cos(var12), (this.random.nextDouble() - 0.5) * 0.4, Math.sin(var12));
               this.cometAges[var11] = 0.0;
               this.cometTicks[var11] = 0;
               this.previousPositions[var11] = null;
               this.cometPhases[var11] = this.random.nextDouble() * Math.PI * 2.0;
            }

            this.cometTicks[var11]++;
            if (var2.speed > 0.0) {
               this.cometAges[var11] = this.cometAges[var11] + 0.05;
            }

            this.cometPhases[var11] = this.cometPhases[var11] + 0.05 * var2.speed * Math.PI * 2.0 / var2.motionPeriod;
            this.cometAnchors[var11] = this.cometAnchors[var11].add(this.cometDirections[var11].scale(var2.speed * 0.01));
            double var13 = this.cometPhases[var11];
            this.cometPositions[var11] = this.cometAnchors[var11]
               .add(
                  Math.sin(var13 * 0.83) * var2.sidewaysTravel * 0.5,
                  Math.sin(var13) * var2.verticalTravel * 0.5,
                  Math.cos(var13 * 0.67) * var2.sidewaysTravel * 0.5
               );
            double var14 = this.cometFades[var11] = cometFade(this.cometTicks[var11] * 0.05, this.cometAges[var11], this.cometAnchors[var11], var1, var2);
            TrailRenderer.RibbonStyle var10 = new TrailRenderer.RibbonStyle(
               var2.trailLength,
               var2.trailWidth,
               ColorUtil.withAlpha(var2.color, var2.opacity * var14),
               ColorUtil.withAlpha(var2.tailColor, var2.opacity * 0.3 * var14),
               var2.bloom,
               true,
               HitEffectConfig.BlendMode.ADDITIVE,
               TrailRenderer.WidthCurve.CUBIC,
               Easing.FadeCurve.LINEAR
            );
            this.trails.addStyledPoint(var11, this.cometPositions[var11], var10);
            this.emitter.style = ParticleSystem.Style.GLOW;
            this.emitter.maxParticles = 2048;
            this.emitter.particleCount = 1;
            this.emitter.size = this.emitter.speed = 0.15;
            this.emitter.gravity = this.emitter.spread = var2.size * 1.6;
            this.emitter.colorScheme = Easing.FadeCurve.LINEAR;
            this.emitter.easing = Easing.SizeCurve.SHRINK;
            this.emitter.primaryColor = ColorUtil.withAlpha(var2.color, var2.opacity * var14);
            this.emitter.secondaryColor = ColorUtil.withAlpha(var2.tailColor, var2.opacity * 0.35 * var14);
            this.emitter.intensity = this.emitter.drag = 0.0;
            this.emitter.roll = this.emitter.delay = 0.0;
            this.particles.spawn(this.cometPositions[var11], this.emitter);
         }
      }
   }

   public void prepareRender(Vec3 var1, float var2, Frustum var3) {
      this.particles.prepare(var1, var2, var3);
      this.trails.buildRibbons(var1, var2);
      this.glowCount = 0;
      this.hasBloom = (boolean)0;
      if (this.settings != null) {
         double var4 = this.settings.radius * 2.0 + this.settings.height + 8.0;

         for (int var6 = 0; var6 < this.cometPositions.length; var6++) {
            if (this.cometPositions[var6] != null) {
               Vec3 var7 = this.previousPositions[var6] == null
                  ? this.cometPositions[var6]
                  : this.previousPositions[var6].lerp(this.cometPositions[var6], var2);
               if (!(var7.distanceToSqr(var1) > var4 * var4)) {
                  double var8 = this.settings.size * 0.65;
                  if (var3 == null || var3.isVisible(new AABB(var7.x - var8, var7.y - var8, var7.z - var8, var7.x + var8, var7.y + var8, var7.z + var8))) {
                     int var10 = ColorUtil.withAlpha(this.settings.color, this.settings.opacity * this.cometFades[var6]);
                     var10 = ColorUtil.lerpColor(var10, var10 & 0xFF000000 | 16777215, 0.7);
                     if (var10 >>> 24 != 0) {
                        this.glows[this.glowCount++] = new OrbitSparkleEffect.Sparkle(var7, var8, var10);
                     }
                  }
               }
            }
         }

         this.hasBloom = (boolean)(this.glowCount > 0 && this.settings.bloom ? 1 : 0);
      }
   }

   public boolean randomPointInCylinder() {
      return this.particles.getVisibleCount() || this.trails.isGlowVisible() || this.hasBloom;
   }

   public boolean hasParticles() {
      return this.particles.hasDepthSortedVisible();
   }

   public int particleCount() {
      return this.particles.getParticleCount() + this.cometCount();
   }

   public int cometCount() {
      int var1 = 0;

      for (Vec3 var5 : this.cometPositions) {
         if (var5 != null) {
            var1++;
         }
      }

      return var1;
   }

   public int trailCount() {
      return this.trails.totalPointCount();
   }

   public Vec3 particleCount(int var1) {
      return this.cometPositions[var1];
   }

   public Vec3 glowPosition(int var1) {
      return this.glows[var1].position;
   }

   public int glowCount() {
      return this.glowCount;
   }

   public double reset(int var1) {
      return this.cometFades[var1];
   }

   public void render(EffectsRenderer var1, Vec3 var2, float var3, boolean var4) {
      this.particles.render(var1, var2, var3, var4);
      this.trails.render(var1, var3, var4);
      if (!var4 || this.hasBloom) {
         var1.blend(HitEffectConfig.BlendMode.ADDITIVE);

         for (int var5 = 0; var5 < this.glowCount; var5++) {
            OrbitSparkleEffect.Sparkle var6 = this.glows[var5];
            var1.drawSpriteWithType(var6.position.x, var6.position.y, var6.position.z, var6.size, 0.0, var6.color, ParticleSystem.Style.CIRCLE, 0.0, 0.0, 0.0);
         }

         var1.blend(HitEffectConfig.BlendMode.NORMAL);
      }
   }

   private static double cometFade(double var0, double var2, Vec3 var4, Vec3 var5, OrbitSparkleEffect.SparkleSettings var6) {
      double var7 = Math.hypot(var4.x - var5.x, var4.z - var5.z);
      double var9 = Math.abs(var4.y - var5.y);
      return Easing.smoothstep(var0 / 0.15)
         * Easing.smoothstep((12.0 - var2) / 0.35)
         * Easing.smoothstep((var6.radius * 1.4 - var7) / (var6.radius * 0.15))
         * Easing.smoothstep((var6.height * 0.7 - var9) / (var6.height * 0.1));
   }

   private record Sparkle(Vec3 position, double size, int color) {
   }

   public record SparkleSettings(
      boolean sparkles,
      boolean comets,
      double radius,
      double height,
      double rate,
      double lifetime,
      double speed,
      double size,
      int cometCount,
      double trailLength,
      double trailWidth,
      int color,
      int tailColor,
      double opacity,
      boolean bloom,
      double verticalTravel,
      double sidewaysTravel,
      double motionPeriod,
      int maximum
   ) {
   }
}
