package com.larpclient.render.sky;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.particle.OrbitSparkleEffect;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.BlockRaycast;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CelestialWeatherSystem {
   public static final int mAX_FLIGHTS = 256;
   public static final int mAX_METEORS = 32;
   public static final int tRAIL_LENGTH = 12;
   public static final int mAX_SPRITES = 1024;
   public static final int mAX_TERRAIN_QUERIES = 4;
   private static final double tICK_SECONDS = 0.05;
   private final Random random;
   private final ParticleSystem sprites = new ParticleSystem();
   private final HitEffectConfig spriteSpec = new HitEffectConfig();
   private final CelestialWeatherSystem.Flight[] flights = new CelestialWeatherSystem.Flight[256];
   private final CelestialWeatherSystem.Ribbon[] ribbons = new CelestialWeatherSystem.Ribbon[256];
   private int flightCount;
   private int ribbonCount;
   private int impactCursor;
   private int terrainQueries;
   private int impactCount;
   private int burstCount;
   private int roofedSnowSkipped;
   private boolean snowBurstDone;
   private double spawnAccumulator;
   private double intensity;
   private double previousIntensity;
   private Vec3 origin;
   private Vec3 cameraPos = Vec3.ZERO;
   private CelestialWeatherSystem.Settings settings;
   private boolean bloomActive;
   private float partialTick;

   public CelestialWeatherSystem() {
      this(new Random());
   }

   public CelestialWeatherSystem(Random var1) {
      this.random = var1;
      this.sprites.maxParticles = 1024;
      this.spriteSpec.maxParticles = 2048;
      this.spriteSpec.seed = 7;

      for (int var2 = 0; var2 < this.flights.length; var2++) {
         this.flights[var2] = new CelestialWeatherSystem.Flight();
         this.ribbons[var2] = new CelestialWeatherSystem.Ribbon();
      }
   }

   public void reset() {
      for (CelestialWeatherSystem.Flight var4 : this.flights) {
         Arrays.fill(var4.trail, null);
         var4.position = var4.previousPosition = var4.velocity = null;
         var4.impactHit = null;
      }

      for (CelestialWeatherSystem.Ribbon var8 : this.ribbons) {
         Arrays.fill(var8.points, null);
         var8.pointCount = 0;
      }

      this.sprites.clear();
      this.flightCount = this.ribbonCount = this.impactCursor = this.terrainQueries = this.impactCount = this.burstCount = 0;
      this.spawnAccumulator = this.intensity = this.previousIntensity = 0.0;
      this.origin = null;
      this.settings = null;
      this.bloomActive = this.snowBurstDone = (boolean)0;
      this.roofedSnowSkipped = 0;
   }

   public void tick(Vec3 var1, CelestialWeatherSystem.Settings var2, boolean var3, BlockRaycast.RayHitTester var4) {
      this.tickWithTerrain(var1, var2, var3, var4, null);
   }

   public void tickWithTerrain(
      Vec3 var1, CelestialWeatherSystem.Settings var2, boolean var3, BlockRaycast.RayHitTester var4, CelestialWeatherSystem.TerrainHeights var5
   ) {
      if (this.settings != null && (this.settings.style != var2.style || this.origin.distanceToSqr(var1) > 4096.0)) {
         this.reset();
      }

      if (this.settings != null && (this.settings.speed != var2.speed || this.settings.impacts != var2.impacts)) {
         for (int var6 = 0; var6 < this.flightCount; var6++) {
            this.flights[var6].impactHit = null;
            this.flights[var6].queriedUntil = 0.0;
         }
      }

      this.origin = var1;
      this.settings = var2;
      this.terrainQueries = this.roofedSnowSkipped = 0;
      this.previousIntensity = this.intensity;
      this.intensity = this.intensity + Math.clamp((var3 ? 1 : 0) - this.intensity, -0.075, 0.075);
      this.sprites.tickScaled(var2.speed);
      if (var2.impacts && var4 != null && var2.speed > 0.0) {
         int var14 = 0;

         while (var14++ < this.flightCount && this.terrainQueries < 4 && this.flightCount > 0) {
            if (this.impactCursor >= this.flightCount) {
               this.impactCursor = 0;
            }

            CelestialWeatherSystem.Flight var7 = this.flights[this.impactCursor++];
            if (var7.willImpact && var7.impactHit == null && !(var7.queriedUntil > var7.age + 0.15)) {
               double var8 = var7.velocity.length() * var2.speed;
               double var10 = Math.min(var7.lifetime, var7.age + 7.5 / Math.max(0.01, var8));
               Vec3 var12 = var7.position;
               Vec3 var13 = var7.position.add(var7.velocity.scale((var10 - var7.age) * var2.speed));
               this.terrainQueries++;
               var7.impactHit = var4.test(var12, var13);
               var7.queriedUntil = var10;
               if (var7.impactHit != null) {
                  var7.impactAge = var7.age + var12.distanceTo(var7.impactHit.point()) / var8;
               }
            }
         }
      }

      int var15 = 0;

      while (var15 < this.flightCount) {
         CelestialWeatherSystem.Flight var17 = this.flights[var15];
         var17.previousPosition = var17.position;
         var17.age += 0.05;
         var17.position = var17.position.add(var17.velocity.scale(0.05 * var2.speed));
         if (var17.impactHit != null && var17.age >= var17.impactAge) {
            this.spawnImpact(var17.impactHit, var17.size);
            this.removeFlight(var15);
         } else {
            if (var2.style == CelestialWeatherSystem.Style.METEOR && !var17.burstDone && var17.age >= var17.lifetime * 0.62) {
               var17.burstDone = (boolean)1;
               if (var17.seed > 0.65) {
                  this.spawnTrailBurst(var17);
                  var17.lifetime = Math.min(var17.lifetime, var17.age + 0.28);
               }
            }

            if (!(var17.age >= var17.lifetime) && !(var17.position.distanceToSqr(this.origin) > Math.pow(var2.distance * 1.3, 2.0))) {
               if (var2.style == CelestialWeatherSystem.Style.METEOR) {
                  System.arraycopy(var17.trail, 0, var17.trail, 1, 11);
                  var17.trail[0] = var17.previousPosition;
                  var17.trailCount = Math.min(12, var17.trailCount + 1);
               }

               var15++;
            } else {
               this.removeFlight(var15);
            }
         }
      }

      var15 = var2.style == CelestialWeatherSystem.Style.METEOR ? Math.min(32, Math.max(1, var2.maximum * 32 / 256)) : Math.min(var2.maximum, 256);

      double var18 = var2.density * switch (var2.style) {
         case METEOR -> 2.5;
         case MOTES -> 42.0;
         case RAIN -> 62.0;
         case SNOW -> 48.0;
      };
      this.spawnAccumulator = Math.min(8.0, this.spawnAccumulator + var18 * 0.05 * this.intensity);
      int var9 = (int)this.spawnAccumulator;
      this.spawnAccumulator -= var9;
      int var19 = Math.min(928, Math.max(32, var2.maximum * 3));
      if (var2.style == CelestialWeatherSystem.Style.MOTES) {
         for (int var11 = 0; var11 < var9 && this.sprites.getParticleCount() < var19; var11++) {
            this.spawnMote();
         }
      } else if (var2.style == CelestialWeatherSystem.Style.SNOW) {
         if (!this.snowBurstDone && this.intensity >= 1.0) {
            this.snowBurstDone = (boolean)1;
            var9 += (int)Math.min(var19 * 0.6, var18 * 3.0);
         }

         for (int var20 = 0; var20 < var9 && this.sprites.getParticleCount() < var19; var20++) {
            this.removeFlight(var5);
         }
      } else {
         for (int var21 = 0; var21 < var9 && this.flightCount < var15; var21++) {
            this.spawnFlight();
         }
      }

      PerfProfiler.addCounter("Celestial active flights", this.flightCount);
      PerfProfiler.addCounter("Celestial sprites", this.sprites.getParticleCount());
      PerfProfiler.addCounter("Celestial terrain queries", this.terrainQueries);
      if (var2.style == CelestialWeatherSystem.Style.SNOW) {
         PerfProfiler.addCounter("Celestial roofed snow columns", this.roofedSnowSkipped);
      }
   }

   private void removeFlight(int var1) {
      CelestialWeatherSystem.Flight var2 = this.flights[var1];
      this.flights[var1] = this.flights[--this.flightCount];
      this.flights[this.flightCount] = var2;
      var2.impactHit = null;
      Arrays.fill(var2.trail, null);
      var2.trailCount = 0;
   }

   private void spawnFlight() {
      CelestialWeatherSystem.Flight var1 = this.flights[this.flightCount++];
      int var2 = this.settings.style == CelestialWeatherSystem.Style.METEOR ? 1 : 0;
      var1.willImpact = (boolean)(this.settings.impacts && this.random.nextDouble() < (var2 != 0 ? 0.23 : 0.16) ? 1 : 0);
      double var3 = 0.65 + (this.random.nextDouble() - 0.5) * (var2 != 0 ? 0.17 : 0.08);
      double var5 = this.settings.wind;
      var1.velocity = var2 != 0
         ? new Vec3(
               Math.cos(var3) * (18.0 + this.random.nextDouble() * 7.0),
               -8.0 - this.random.nextDouble() * 4.0,
               Math.sin(var3) * (18.0 + this.random.nextDouble() * 7.0)
            )
            .add(var5, 0.0, var5 * 0.4)
         : new Vec3(var5 * 0.75, -10.0 - this.random.nextDouble() * 5.0, var5 * 0.3);
      Vec3 var7 = this.origin;
      double var8;
      if (var2 != 0) {
         var7 = this.origin.add(-var1.velocity.x * 0.65, var1.willImpact ? 8.0 : Math.min(32.0, this.settings.radius * 0.7), -var1.velocity.z * 0.65);
         var8 = var1.willImpact ? 8.0 : 18.0;
      } else {
         var7 = this.origin.add(0.0, 9.0, 0.0);
         var8 = 16.0;
      }

      var1.position = var1.previousPosition = OrbitSparkleEffect.randomPointInCylinder(
         var7, var1.willImpact ? Math.min(12.0, this.settings.radius) : this.settings.radius, var8, this.random
      );
      var1.age = 0.0;
      var1.lifetime = var2 != 0 ? 1.6 + this.random.nextDouble() * 1.8 : 2.3 + this.random.nextDouble() * 1.3;
      var1.size = this.settings.size * (var2 != 0 ? 0.055 : 0.014) * (0.65 + this.random.nextDouble() * 0.7);
      var1.seed = this.random.nextDouble();
      var1.burstDone = (boolean)0;
      var1.impactHit = null;
      var1.queriedUntil = 0.0;
      var1.impactAge = Double.POSITIVE_INFINITY;
      var1.trailCount = 0;
   }

   private void resetSpriteSpec() {
      this.spriteSpec.particleCount = 1;
      this.spriteSpec.primaryColor = this.settings.primary;
      this.spriteSpec.secondaryColor = ColorUtil.multiplyAlpha(this.settings.secondary, 0.55);
      this.spriteSpec.enabled = this.settings.bloom;
      this.spriteSpec.heightOverride = this.settings.distance;
      this.spriteSpec.pitchOffset = this.spriteSpec.yawOffset = this.spriteSpec.spin = this.spriteSpec.intensity = this.spriteSpec.glow = 0.0;
      this.spriteSpec.lifetime = this.spriteSpec.fadeIn = this.spriteSpec.drag = this.spriteSpec.turbulence = this.spriteSpec.radius = 0.0;
      this.spriteSpec.roll = this.spriteSpec.delay = 0.0;
      this.spriteSpec.colorScheme = Easing.FadeCurve.SOFT_ATTACK_SOFT_RELEASE;
      this.spriteSpec.easing = Easing.SizeCurve.PULSE;
      this.spriteSpec.style = ParticleSystem.Style.GLOW;
   }

   private void spawnMote() {
      this.resetSpriteSpec();
      this.spriteSpec.style = this.random.nextDouble() < 0.2 ? ParticleSystem.Style.STAR : ParticleSystem.Style.GLOW;
      this.spriteSpec.size = 3.0;
      this.spriteSpec.speed = 6.0;
      this.spriteSpec.gravity = this.settings.size * 0.022;
      this.spriteSpec.spread = this.settings.size * 0.07;
      this.spriteSpec.pitchOffset = this.settings.wind * 0.12;
      this.spriteSpec.spin = this.settings.wind * 0.05;
      this.spriteSpec.roll = 0.35 + this.random.nextDouble() * 0.5;
      this.spriteSpec.delay = 0.4 + this.random.nextDouble();
      this.spriteSpec.falloff = 4.0 + this.random.nextDouble() * 4.0;
      this.spriteSpec.primaryColor = ColorUtil.multiplyAlpha(
         ColorUtil.lerpColor(this.settings.primary, this.settings.secondary, this.random.nextDouble()), 0.4 + this.random.nextDouble() * 0.5
      );
      int var1 = this.random.nextDouble() < 0.22 ? 1 : 0;
      this.sprites
         .spawn(
            OrbitSparkleEffect.randomPointInCylinder(
               this.origin,
               var1 != 0 ? Math.min(3.0, this.settings.radius * 0.16) : this.settings.radius,
               var1 != 0 ? 4.0 : Math.min(20.0, this.settings.radius),
               this.random
            ),
            this.spriteSpec
         );
   }

   private void removeFlight(CelestialWeatherSystem.TerrainHeights var1) {
      this.resetSpriteSpec();
      double var2 = this.random.nextDouble();
      int var4 = var2 < 0.06 ? 1 : 0;
      int var5 = var2 < 0.32 ? 1 : 0;
      double var6 = var4 != 0 ? Math.min(4.0, this.settings.radius * 0.2) : (var5 != 0 ? Math.min(9.0, this.settings.radius * 0.4) : this.settings.radius);
      Vec3 var8 = OrbitSparkleEffect.randomPointInCylinder(
         this.origin.add(0.0, var4 != 0 ? 1.0 : (var5 != 0 ? 3.0 : 7.0), 0.0), var6, var4 != 0 ? 4.0 : (var5 != 0 ? 12.0 : 22.0), this.random
      );
      double var9 = 0.55 + this.random.nextDouble() * 0.55;
      double var11 = this.settings.wind * 0.35;
      double var13 = this.settings.wind * 0.15;
      double var15 = 6.0 + this.random.nextDouble() * 6.0;
      double var17 = var9 * this.settings.speed;
      if (var1 != null) {
         double var19 = var1.heightAt((int)Math.floor(var8.x), (int)Math.floor(var8.z));
         if (Double.isFinite(var19) && var8.y < var19 + 0.25) {
            this.roofedSnowSkipped++;
            return;
         }

         if (var17 > 0.05) {
            double var21 = var8.x + var11 * this.settings.speed * var15;
            double var23 = var8.z + var13 * this.settings.speed * var15;
            double var25 = var1.heightAt((int)Math.floor(var21), (int)Math.floor(var23));
            if (Double.isFinite(var25)) {
               var15 = Math.min(var15, Math.max(0.4, (var8.y - var25) / var17));
            }
         }
      }

      this.spriteSpec.size = this.spriteSpec.speed = var15;
      this.spriteSpec.pitchOffset = var11;
      this.spriteSpec.yawOffset = -var9;
      this.spriteSpec.spin = var13;
      this.spriteSpec.drag = 0.12;
      this.spriteSpec.delay = 0.2 + this.random.nextDouble() * 0.45;
      this.spriteSpec.roll = 0.06;
      this.spriteSpec.falloff = 3.0 + this.random.nextDouble() * 3.5;
      this.spriteSpec.colorScheme = Easing.FadeCurve.SHARP_ATTACK_SMOOTH_RELEASE;
      int var27 = var4 == 0 && this.random.nextDouble() < 0.12 ? 1 : 0;
      this.spriteSpec.style = var27 != 0 ? ParticleSystem.Style.STAR : ParticleSystem.Style.GLOW;
      this.spriteSpec.easing = var27 != 0 ? Easing.SizeCurve.PULSE : Easing.SizeCurve.CONSTANT;
      double var20 = this.settings.size * (0.02 + this.random.nextDouble() * 0.032) * (var4 != 0 ? 2.6 : (var5 != 0 ? 1.2 : 1.0));
      this.spriteSpec.gravity = this.spriteSpec.spread = var20;
      int var22 = ColorUtil.lerpColor(this.settings.primary, this.settings.primary & 0xFF000000 | 16777215, 0.35 + this.random.nextDouble() * 0.3);
      this.spriteSpec.primaryColor = ColorUtil.multiplyAlpha(var22, var4 != 0 ? 0.28 : 0.55 + this.random.nextDouble() * 0.4);
      this.spriteSpec.secondaryColor = ColorUtil.multiplyAlpha(this.settings.secondary, var4 != 0 ? 0.12 : 0.35);
      this.sprites.spawn(var8, this.spriteSpec);
   }

   private void spawnTrailBurst(CelestialWeatherSystem.Flight var1) {
      this.resetSpriteSpec();
      this.spriteSpec.style = ParticleSystem.Style.SPARK;
      this.spriteSpec.particleCount = 3;
      this.spriteSpec.size = 0.25;
      this.spriteSpec.speed = 0.55;
      this.spriteSpec.gravity = var1.size * 0.65;
      this.spriteSpec.spread = var1.size;
      this.spriteSpec.intensity = 1.4;
      this.spriteSpec.lifetime = 2.0;
      this.spriteSpec.fadeIn = 0.4;
      this.spriteSpec.pitchOffset = var1.velocity.x * 0.65;
      this.spriteSpec.yawOffset = var1.velocity.y * 0.65;
      this.spriteSpec.spin = var1.velocity.z * 0.65;
      this.spriteSpec.colorScheme = Easing.FadeCurve.LINEAR;
      this.spriteSpec.easing = Easing.SizeCurve.SHRINK;
      this.sprites.spawn(var1.position, this.spriteSpec);
      this.burstCount++;
   }

   private void spawnImpact(BlockRaycast.RayHit var1, double var2) {
      this.resetSpriteSpec();
      this.spriteSpec.style = ParticleSystem.Style.RING;
      this.spriteSpec.offset = var1.normal();
      this.spriteSpec.size = this.spriteSpec.speed = 0.42;
      this.spriteSpec.density = Math.clamp(var2 * 7.0, 0.12, 0.45);
      this.spriteSpec.jitter = 0.015;
      this.spriteSpec.colorScheme = Easing.FadeCurve.SHARP_ATTACK_SMOOTH_RELEASE;
      this.spriteSpec.easing = Easing.SizeCurve.CONSTANT;
      this.spriteSpec.primaryColor = ColorUtil.multiplyAlpha(this.settings.primary, 0.5);
      this.spriteSpec.secondaryColor = ColorUtil.multiplyAlpha(this.settings.secondary, 0.1);
      Vec3 var4 = var1.point().add(var1.normal().scale(0.012));
      this.sprites.spawn(var4, this.spriteSpec);
      this.spriteSpec.style = ParticleSystem.Style.GLOW;
      this.spriteSpec.particleCount = 2;
      this.spriteSpec.size = 0.18;
      this.spriteSpec.speed = 0.32;
      this.spriteSpec.gravity = 0.025;
      this.spriteSpec.spread = 0.045;
      this.spriteSpec.density = 1.0;
      this.spriteSpec.yawOffset = 0.2;
      this.spriteSpec.intensity = 0.35;
      this.spriteSpec.easing = Easing.SizeCurve.SHRINK;
      this.sprites.spawn(var4, this.spriteSpec);
      this.impactCount++;
   }

   public void prepareRender(Vec3 var1, float var2, Frustum var3) {
      long var4 = PerfProfiler.startTimer();
      this.cameraPos = var1;
      this.partialTick = Math.clamp(var2, 0.0F, 1.0F);
      this.ribbonCount = 0;
      this.bloomActive = (boolean)0;
      this.sprites.prepare(var1, this.partialTick, var3);
      if (this.settings != null) {
         double var6 = this.previousIntensity + (this.intensity - this.previousIntensity) * this.partialTick;

         for (int var8 = 0; var8 < this.flightCount; var8++) {
            CelestialWeatherSystem.Flight var9 = this.flights[var8];
            Vec3 var10 = var9.previousPosition.lerp(var9.position, this.partialTick);
            double var11 = Math.max(0.0, var9.age - 0.05 + this.partialTick * 0.05);
            double var13 = var10.distanceTo(var1);
            double var15 = fadeEnvelope(var11, var9.lifetime)
               * Easing.smoothstep((this.settings.distance - var13) / Math.max(2.0, this.settings.distance * 0.18))
               * var6;
            if (!(var15 < 0.005)) {
               int var17 = this.settings.style == CelestialWeatherSystem.Style.METEOR ? 1 : 0;
               Vec3 var18 = var17 != 0 && var9.trailCount > 0
                  ? var9.trail[var9.trailCount - 1]
                  : var10.subtract(var9.velocity.scale(0.045 + var9.seed * 0.025));
               double var19 = Math.max(0.1, var9.size * 3.0);
               if (var3 == null || var3.isVisible(new AABB(var10, var18).inflate(var19))) {
                  CelestialWeatherSystem.Ribbon var21 = this.ribbons[this.ribbonCount++];
                  var21.width = var9.size;
                  var21.isMeteor = (boolean)var17;
                  var21.primaryColor = ColorUtil.multiplyAlpha(
                     ColorUtil.lerpColor(this.settings.primary, this.settings.primary & 0xFF000000 | 16777215, var17 != 0 ? 0.55 : 0.18),
                     var15 * (var17 != 0 ? 0.95 : 0.6)
                  );
                  var21.secondaryColor = ColorUtil.multiplyAlpha(this.settings.secondary, var15 * (var17 != 0 ? 0.6 : 0.3));
                  if (var21.primaryColor >>> 24 == 0 && var21.secondaryColor >>> 24 == 0) {
                     this.ribbonCount--;
                  } else {
                     var21.points[0] = var10;
                     var21.pointCount = 1;
                     if (var17 != 0) {
                        for (int var22 = 0; var22 < var9.trailCount; var22++) {
                           var21.points[var21.pointCount++] = var9.trail[var22];
                        }
                     } else {
                        var21.points[var21.pointCount++] = var18;
                     }
                  }
               }
            }
         }

         this.bloomActive = (boolean)(!this.settings.bloom || this.ribbonCount <= 0 && !this.sprites.getVisibleCount() ? 0 : 1);
         PerfProfiler.addCounter("Celestial prepared flights", this.ribbonCount);
         PerfProfiler.addCounter("Celestial ribbon segments", this.getRibbonSegmentCount());
         PerfProfiler.addCounter("Celestial culled flights", this.flightCount - this.ribbonCount);
         PerfProfiler.endTimer("Celestial prepare", var4);
      }
   }

   public static double fadeEnvelope(double var0, double var2) {
      return Easing.smoothstep(var0 / 0.22) * Easing.smoothstep((var2 - var0) / 0.45);
   }

   public boolean spawnMote() {
      return this.bloomActive;
   }

   public boolean hasSprites() {
      return this.sprites.hasDepthSortedVisible();
   }

   public void render(EffectsRenderer var1, boolean var2) {
      if (!var2 || this.bloomActive) {
         this.sprites.render(var1, this.cameraPos, this.partialTick, var2);
         var1.blend(HitEffectConfig.BlendMode.ADDITIVE);

         for (int var3 = 0; var3 < this.ribbonCount; var3++) {
            CelestialWeatherSystem.Ribbon var4 = this.ribbons[var3];

            for (int var5 = 1; var5 < var4.pointCount; var5++) {
               Vec3 var6 = var4.points[var5 - 1];
               Vec3 var7 = var4.points[var5];
               double var8 = 1.0 - (var5 - 1.0) / (var4.pointCount - 1);
               double var10 = 1.0 - (double)var5 / (var4.pointCount - 1);
               double var12 = var4.width * (var4.isMeteor ? 0.08 + 0.92 * var8 : 1.0);
               double var14 = var4.width * (var4.isMeteor ? 0.08 + 0.92 * var10 : 0.2);
               int var16 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var4.secondaryColor, var4.primaryColor, var8), var8);
               int var17 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var4.secondaryColor, var4.primaryColor, var10), var10);
               var1.drawRibbon(
                  var6.x,
                  var6.y,
                  var6.z,
                  var7.x,
                  var7.y,
                  var7.z,
                  var12 * 3.0,
                  var14 * 3.0,
                  ColorUtil.multiplyAlpha(var16, 0.15),
                  ColorUtil.multiplyAlpha(var17, 0.15)
               );
               var1.drawRibbon(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12, var14, var16, var17);
               if (var4.isMeteor) {
                  var1.drawRibbon(
                     var6.x,
                     var6.y,
                     var6.z,
                     var7.x,
                     var7.y,
                     var7.z,
                     var12 * 0.26,
                     var14 * 0.26,
                     ColorUtil.lerpColor(var16, var16 & 0xFF000000 | 16777215, 0.75),
                     ColorUtil.lerpColor(var17, var17 & 0xFF000000 | 16777215, 0.75)
                  );
               }
            }

            if (var4.isMeteor) {
               Vec3 var18 = var4.points[0];
               var1.drawSpriteWithType(var18.x, var18.y, var18.z, var4.width * 1.5, 0.0, var4.primaryColor, ParticleSystem.Style.GLOW, 0.0, 0.0, 0.0);
            }
         }

         var1.blend(HitEffectConfig.BlendMode.NORMAL);
      }
   }

   public int getFlightCount() {
      return this.flightCount;
   }

   public int getSpriteCount() {
      return this.sprites.getParticleCount();
   }

   public int getTrailPointCount() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.flightCount; var2++) {
         var1 += this.flights[var2].trailCount;
      }

      return var1;
   }

   public int spawnTrailBurst() {
      return this.ribbonCount;
   }

   public int getRibbonSegmentCount() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.ribbonCount; var2++) {
         var1 += Math.max(0, this.ribbons[var2].pointCount - 1);
      }

      return var1;
   }

   public int getTerrainQueries() {
      return this.terrainQueries;
   }

   public int getRoofedSnowSkipped() {
      return this.roofedSnowSkipped;
   }

   public int removeFlight() {
      return this.impactCount;
   }

   public int getBurstCount() {
      return this.burstCount;
   }

   public Vec3 getFlightPosition(int var1) {
      return this.flights[var1].position;
   }

   public Vec3 reset(int var1) {
      return this.flights[var1].velocity;
   }

   private static final class Flight {
      Vec3 position;
      Vec3 previousPosition;
      Vec3 velocity;
      final Vec3[] trail = new Vec3[12];
      int trailCount;
      double age;
      double lifetime;
      double size;
      double seed;
      double queriedUntil;
      double impactAge;
      boolean willImpact;
      boolean burstDone;
      BlockRaycast.RayHit impactHit;
   }

   private static final class Ribbon {
      final Vec3[] points = new Vec3[13];
      int pointCount;
      int primaryColor;
      int secondaryColor;
      double width;
      boolean isMeteor;
   }

   public record Settings(
      CelestialWeatherSystem.Style style,
      double density,
      double radius,
      double speed,
      double size,
      double wind,
      double distance,
      int primary,
      int secondary,
      boolean bloom,
      boolean impacts,
      int maximum
   ) {
   }

   public enum Style {
      METEOR,
      MOTES,
      RAIN,
      SNOW;
   }

   @FunctionalInterface
   public interface TerrainHeights {
      double heightAt(int var1, int var2);
   }
}
