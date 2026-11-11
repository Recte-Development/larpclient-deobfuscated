package com.larpclient.render.particle;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.model.CrystalMesh;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.BlockRaycast;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ParticleSystem {
   private static final int mAX_PARTICLES = 2048;
   private final ParticleSystem.Particle[] particles = new ParticleSystem.Particle[2048];
   private final Random random = new Random();
   private Int2IntOpenHashMap emitterCounts;
   private int particleCount;
   private int overlayCount;
   private int visibleCount;
   private int visibleOverlayCount;
   private final ParticleSystem.RenderParticle[] renderPool = new ParticleSystem.RenderParticle[2048];
   private final ParticleSystem.RenderParticle[] visibleParticles = new ParticleSystem.RenderParticle[2048];
   private boolean hasDepthSortedVisible;
   private static final Comparator<ParticleSystem.RenderParticle> fAR_TO_NEAR = Comparator.<ParticleSystem.RenderParticle>comparingDouble(
         var0 -> var0.distanceSq
      )
      .reversed();
   public int maxParticles = 2048;
   public double defaultViewDistance = 64.0;

   public ParticleSystem() {
      for (int var1 = 0; var1 < 2048; var1++) {
         this.particles[var1] = new ParticleSystem.Particle();
         this.renderPool[var1] = new ParticleSystem.RenderParticle();
      }

      LarpLog.info("Particles", "Emitter engine initialized; capacity={}", 2048);
   }

   public int getParticleCount() {
      return this.particleCount;
   }

   public void clear() {
      this.particleCount = 0;
      if (this.emitterCounts != null) {
         this.emitterCounts.clear();
      }

      this.overlayCount = 0;
      this.visibleCount = this.visibleOverlayCount = 0;
      this.hasDepthSortedVisible = (boolean)0;
   }

   public void removeByEmitter(int var1) {
      for (int var2 = this.particleCount - 1; var2 >= 0; var2--) {
         if (this.particles[var2].emitterId == var1) {
            this.removeAt(var2);
         }
      }
   }

   private void removeAt(int var1) {
      ParticleSystem.Particle var2 = this.particles[var1];
      if (this.emitterCounts != null) {
         int var3 = this.emitterCounts.get(var2.emitterId) - 1;
         if (var3 == 0) {
            this.emitterCounts.remove(var2.emitterId);
         } else {
            this.emitterCounts.put(var2.emitterId, var3);
         }
      }

      if (var2.overlay) {
         this.overlayCount--;
      }

      this.particles[var1] = this.particles[--this.particleCount];
      this.particles[this.particleCount] = var2;
   }

   public boolean hasOverlayParticles() {
      return this.overlayCount > 0;
   }

   private double randomRange(double var1, double var3) {
      return var1 + this.random.nextDouble() * (var3 - var1);
   }

   public void spawn(Vec3 var1, HitEffectConfig var2) {
      int var3 = Math.clamp(this.maxParticles, 0, 2048);
      int var4 = var2.maxParticles < 2048 ? this.countForEmitter(var2.seed) : 0;
      int var5 = Math.clamp(var2.maxParticles, 0, 2048);
      int var6 = var2.mirrored ? this.countForEmitter(var2.seed) : 0;
      double var7 = Math.cos(var2.turbulence * 0.05);
      double var9 = Math.sin(var2.turbulence * 0.05);

      for (int var11 = 0; var11 < Math.clamp(var2.particleCount, 0, 2048) && this.particleCount < var3 && var4++ < var5; var11++) {
         ParticleSystem.Particle var12 = this.particles[this.particleCount++];
         var12.index = var6++;
         double var13 = this.randomRange(0.0, Math.PI * 2);
         double var15 = Math.sqrt(this.random.nextDouble());
         double var17 = this.randomRange(-0.5, 0.5);
         double var19 = 0.0;
         double var21 = 0.0;
         switch (var2.shape) {
            case RING:
               var19 = this.randomRange(-0.5, 0.5);
               var21 = this.randomRange(-0.5, 0.5);
               break;
            case STAR:
               double var23 = this.randomRange(-1.0, 1.0);
               double var25 = Math.cbrt(this.random.nextDouble()) * 0.5;
               double var27 = Math.sqrt(1.0 - var23 * var23) * var25;
               var19 = Math.cos(var13) * var27;
               var21 = Math.sin(var13) * var27;
               var17 = var23 * var25;
               break;
            case SPHERE:
               var19 = Math.cos(var13) * var15 * 0.5;
               var21 = Math.sin(var13) * var15 * 0.5;
               break;
            case CROSS:
               var19 = Math.cos(var13) * 0.5;
               var21 = Math.sin(var13) * 0.5;
               var17 = 0.0;
               break;
            case QUAD:
               var17 = 0.0;
         }

         var12.x2 = var12.prevX = var1.x + var19 * var2.scale;
         var12.y2 = var12.prevY = var1.y + var17 * var2.stretch;
         var12.z2 = var12.prevZ = var1.z + var21 * var2.opacity;
         var12.velX = this.randomRange(-1.0, 1.0) * var2.intensity * var2.brightness + var2.rotation + var2.pitchOffset;
         var12.velZ = this.randomRange(-1.0, 1.0) * var2.intensity * var2.brightness + var2.spin;
         var12.velY = this.randomRange(var2.mirrored ? -1.0 : -0.4, 1.0) * var2.intensity + var2.glow + var2.yawOffset;
         var12.age = -Math.max(0.0, var2.tilt);
         var12.maxAge = Math.max(0.05, this.randomRange(var2.size, var2.speed));
         var12.size = Math.max(0.001, this.randomRange(var2.gravity, var2.spread));
         if (var2.mode != HitEffectConfig.Mode.DEFAULT && Double.isFinite(var2.durationOverride)) {
            var12.y2 = var12.prevY = Math.max(var12.y2, var2.durationOverride + var12.size * 1.2 + 0.002);
         }

         var12.yaw = var13;
         var12.yawSpeed = this.randomRange(Double.isFinite(var2.maxDistance) ? var2.maxDistance : -var2.radius, var2.radius);
         var12.mirrored = var2.mirrored;
         var12.rainbow = var2.additive;
         var12.alphaPower = var2.globalScale;
         var12.spin = this.randomRange(0.0, Math.PI * 2);
         var12.orbitPhase = var12.spin;
         var12.orbitAmplitudeY = var2.roll;
         var12.orbitAmplitudeXZ = var2.delay;
         var12.orbitSpeed = (Math.PI * 2) / Math.max(0.05, var2.falloff);
         var12.orbitX = var12.orbitY = var12.orbitZ = 0.0;
         if (var12.orbitAmplitudeY != 0.0 || var12.orbitAmplitudeXZ != 0.0) {
            var12.orbitX = Math.sin(var12.orbitPhase * 0.83) * var12.orbitAmplitudeXZ;
            var12.orbitY = Math.sin(var12.orbitPhase) * var12.orbitAmplitudeY;
            var12.orbitZ = Math.cos(var12.orbitPhase * 0.67) * var12.orbitAmplitudeXZ;
            var12.x2 = var12.prevX = var12.prevX + var12.orbitX;
            var12.y2 = var12.prevY = var12.prevY + var12.orbitY;
            var12.z2 = var12.prevZ = var12.prevZ + var12.orbitZ;
         }

         var12.gravity = var2.lifetime;
         var12.damping = Math.exp(-Math.max(0.0, var2.fadeIn) * 0.05);
         var12.wobble = var2.drag;
         var12.turnRate = var2.turbulence;
         var12.cosTurn = var7;
         var12.sinTurn = var9;
         var12.colorScheme = var2.colorScheme;
         var12.easing = var2.easing;
         var12.blendMode = var2.blendMode;
         var12.color = var2.style == ParticleSystem.Style.BILL
            ? var2.primaryColor
            : ColorUtil.lerpColor(var2.primaryColor, var2.secondaryColor, this.random.nextDouble() * 0.35);
         var12.endColor = var2.secondaryColor;
         var12.overlay = var2.enabled;
         if (var12.overlay) {
            this.overlayCount++;
         }

         var12.style = var2.style;
         var12.mode = var2.mode;
         var12.landed = (boolean)0;
         var12.offsetX = var2.offset.x;
         var12.offsetY = var2.offset.y;
         var12.offsetZ = var2.offset.z;
         var12.pitch = var12.bank = var12.pitchSpeed = var12.bankSpeed = 0.0;
         if (var12.style == ParticleSystem.Style.BILL || var12.style == ParticleSystem.Style.CRYSTAL) {
            var12.pitch = this.randomRange(0.0, Math.PI * 2);
            var12.bank = this.randomRange(0.0, Math.PI * 2);
            var12.pitchSpeed = this.randomRange(-2.4, 2.4);
            var12.bankSpeed = this.randomRange(-1.8, 1.8);
         }

         var12.texture = var2.texture;
         var12.ringExtent = var2.density;
         var12.thickness = var2.jitter;
         var12.emitterId = var2.seed;
         if (this.emitterCounts != null) {
            this.emitterCounts.addTo(var12.emitterId, 1);
         }

         var12.maxViewDistance = Double.isFinite(var2.heightOverride) ? Math.max(0.0, var2.heightOverride) : Math.max(0.0, this.defaultViewDistance);
      }
   }

   public void spawnBurst(Vec3 var1, ParticleSystem.Style var2, int var3, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      HitEffectConfig var13 = new HitEffectConfig();
      var13.style = var2;
      var13.particleCount = var3;
      var13.gravity = var13.spread = var4;
      var13.size = var13.speed = var6;
      var13.primaryColor = var8;
      var13.secondaryColor = var9;
      var13.enabled = var10;
      var13.density = var4 * var11;
      this.spawn(var1, var13);
   }

   public void spawn() {
      this.tickScaled(1.0);
   }

   public void tickScaled(double var1) {
      this.tickWithCollision(var1, null);
   }

   public void tickWithCollision(double var1, BlockRaycast.RayHitTester var3) {
      if (Double.isFinite(var1) && !(var1 < 0.0)) {
         int var4 = 0;

         while (var4 < this.particleCount) {
            ParticleSystem.Particle var5 = this.particles[var4];
            var5.age += 0.05;
            if (var5.age >= var5.maxAge) {
               this.removeAt(var4);
            } else {
               var5.prevX = var5.x2;
               var5.prevY = var5.y2;
               var5.prevZ = var5.z2;
               if (var5.age >= 0.0 && var5.style != ParticleSystem.Style.RING && !var5.landed) {
                  if (var5.mirrored) {
                     var5.x2 = var5.x2 + var5.velX * 0.05 * var1;
                     var5.y2 = var5.y2 + var5.velY * 0.05 * var1;
                     var5.z2 = var5.z2 + var5.velZ * 0.05 * var1;
                     var5.velX = var5.velX * var5.damping;
                     var5.velZ = var5.velZ * var5.damping;
                     var5.velY = (var5.velY - var5.gravity * 0.05) * var5.damping;
                     var4++;
                     continue;
                  }

                  double var6 = var5.spin + var5.age * 3.0;
                  double var8 = var5.velX;
                  double var10 = var5.velZ;
                  var5.velX = (var8 * var5.cosTurn - var10 * var5.sinTurn) * var5.damping;
                  var5.velZ = (var8 * var5.sinTurn + var10 * var5.cosTurn) * var5.damping;
                  var5.velY = (var5.velY - var5.gravity * 0.05) * var5.damping;
                  if (var5.wobble != 0.0 || var5.velX != 0.0 || var5.velY != 0.0 || var5.velZ != 0.0) {
                     var5.x2 = var5.x2 + (var5.velX + Math.sin(var6) * var5.wobble) * 0.05 * var1;
                     var5.y2 = var5.y2 + (var5.velY + Math.cos(var6 * 1.3) * var5.wobble * 0.4) * 0.05 * var1;
                     var5.z2 = var5.z2 + (var5.velZ + Math.cos(var6 * 0.83) * var5.wobble) * 0.05 * var1;
                  }

                  if (var5.orbitAmplitudeY != 0.0 || var5.orbitAmplitudeXZ != 0.0) {
                     applyOrbit(var5, var1);
                  }

                  if (var3 != null && var5.mode != HitEffectConfig.Mode.DEFAULT) {
                     collideWithSurface(var5, var3);
                  }
               }

               var4++;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid motion speed");
      }
   }

   private static void collideWithSurface(ParticleSystem.Particle var0, BlockRaycast.RayHitTester var1) {
      Vec3 var2 = new Vec3(var0.prevX, var0.prevY, var0.prevZ);
      Vec3 var3 = new Vec3(var0.x2, var0.y2, var0.z2);
      if (!(var2.distanceToSqr(var3) < 1.0E-12)) {
         double var4 = var0.style == ParticleSystem.Style.CRYSTAL ? var0.size * 1.2 : 0.015;
         PerfProfiler.addCounter("Particle surface queries", 1L);
         BlockRaycast.RayHit var6 = var1.test(var2.add(0.0, -var4, 0.0), var3.add(0.0, -var4, 0.0));
         if (var6 != null) {
            Vec3 var7 = var6.normal();
            var0.x2 = var6.point().x + var7.x * 0.002;
            var0.y2 = var6.point().y + var4 + var7.y * 0.002;
            var0.z2 = var6.point().z + var7.z * 0.002;
            double var8 = var0.velX * var7.x + var0.velY * var7.y + var0.velZ * var7.z;
            if (!(var8 >= 0.0)) {
               int var10 = !(var7.y > 0.5) || var0.mode != HitEffectConfig.Mode.TRAIL && !(-var8 < 0.55) ? 0 : 1;
               if (var10 != 0) {
                  var0.landed = (boolean)1;
                  var0.velX = var0.velY = var0.velZ = 0.0;
                  var0.yaw = var0.yaw + var0.age * var0.yawSpeed;
                  var0.pitch = var0.pitch + var0.age * var0.pitchSpeed;
                  var0.bank = var0.bank + var0.age * var0.bankSpeed;
                  var0.yawSpeed = var0.pitchSpeed = var0.bankSpeed = 0.0;
                  if (var0.style == ParticleSystem.Style.CRYSTAL) {
                     var0.y2 = var6.point().y + CrystalMesh.minHeight(var0.size, var0.pitch, var0.bank) + 0.002;
                  }
               } else {
                  double var11 = var0.mode == HitEffectConfig.Mode.BURST ? 0.38 : 0.08;
                  var0.velX = (var0.velX - (1.0 + var11) * var8 * var7.x) * 0.72;
                  var0.velY = (var0.velY - (1.0 + var11) * var8 * var7.y) * 0.72;
                  var0.velZ = (var0.velZ - (1.0 + var11) * var8 * var7.z) * 0.72;
               }
            }
         }
      }
   }

   private static void applyOrbit(ParticleSystem.Particle var0, double var1) {
      var0.orbitPhase = var0.orbitPhase + 0.05 * var1 * var0.orbitSpeed;
      double var3 = Math.sin(var0.orbitPhase * 0.83) * var0.orbitAmplitudeXZ;
      double var5 = Math.sin(var0.orbitPhase) * var0.orbitAmplitudeY;
      double var7 = Math.cos(var0.orbitPhase * 0.67) * var0.orbitAmplitudeXZ;
      var0.x2 = var0.x2 + (var3 - var0.orbitX);
      var0.y2 = var0.y2 + (var5 - var0.orbitY);
      var0.z2 = var0.z2 + (var7 - var0.orbitZ);
      var0.orbitX = var3;
      var0.orbitY = var5;
      var0.orbitZ = var7;
   }

   public int countForEmitter(int var1) {
      if (this.emitterCounts == null) {
         this.rebuildEmitterCounts();
      }

      return this.emitterCounts.get(var1);
   }

   private void rebuildEmitterCounts() {
      this.emitterCounts = new Int2IntOpenHashMap();

      for (int var1 = 0; var1 < this.particleCount; var1++) {
         this.emitterCounts.addTo(this.particles[var1].emitterId, 1);
      }
   }

   public boolean getVisibleCount() {
      return this.visibleOverlayCount > 0;
   }

   public boolean hasDepthSortedVisible() {
      return this.hasDepthSortedVisible;
   }

   public int getVisibleCount() {
      return this.visibleCount;
   }

   public static int removeByEmitter(double var0, int var2) {
      long var3 = ((long)(var0 * 1000.0) / 12L + var2 * 14L) % 360L;
      return ColorUtil.hsbToRgb((float)var3 / 360.0F, 0.7F, 1.0F);
   }

   public void prepare(Vec3 var1, float var2, Frustum var3) {
      long var4 = PerfProfiler.startTimer();
      this.visibleCount = this.visibleOverlayCount = 0;
      this.hasDepthSortedVisible = (boolean)0;
      byte var6 = 0;

      for (int var7 = 0; var7 < this.particleCount; var7++) {
         ParticleSystem.Particle var8 = this.particles[var7];
         double var9 = var8.age + var2 * 0.05;
         if (!(var9 < 0.0)) {
            double var11 = var8.prevX + (var8.x2 - var8.prevX) * var2;
            double var13 = var8.prevY + (var8.y2 - var8.prevY) * var2;
            double var15 = var8.prevZ + (var8.z2 - var8.prevZ) * var2;
            double var17 = var11 - var1.x;
            double var19 = var13 - var1.y;
            double var21 = var15 - var1.z;
            if (!(var17 * var17 + var19 * var19 + var21 * var21 > var8.maxViewDistance * var8.maxViewDistance)) {
               double var23 = Math.clamp(var9 / var8.maxAge, 0.0, 1.0);
               double var25 = var8.size * var8.easing.scale(var23);
               int var27 = var8.rainbow ? removeByEmitter(TickTimer.getTime(), var8.index) : ColorUtil.lerpColor(var8.color, var8.endColor, var23);
               double var28 = var8.alphaPower == 1.0 ? var8.colorScheme.alpha(var23) : Math.pow(var8.colorScheme.alpha(var23), var8.alphaPower);
               int var30 = var8.mirrored
                  ? var27 & 16777215 | Math.clamp(Math.round((var27 >>> 24) * var28), 0, 255) << 24
                  : ColorUtil.multiplyAlpha(var27, var28);
               if (var30 >>> 24 != 0) {
                  double var31 = var8.style == ParticleSystem.Style.RING ? var8.ringExtent * (1.0 - Math.pow(1.0 - var23, 3.0)) : var25;
                  double var33 = var8.style == ParticleSystem.Style.RING ? var31 + Math.abs(var8.thickness) : Math.abs(var25) * 5.0;
                  if (var3 == null || var3.isVisible(new AABB(var11 - var33, var13 - var33, var15 - var33, var11 + var33, var13 + var33, var15 + var33))) {
                     ParticleSystem.RenderParticle var35 = this.renderPool[this.visibleCount];
                     var35.x = var11;
                     var35.y2 = var13;
                     var35.z2 = var15;
                     var35.size = var25;
                     var35.extent = var31;
                     var35.yaw = var8.yaw + var9 * var8.yawSpeed;
                     var35.pitch = var8.pitch + var9 * var8.pitchSpeed;
                     var35.bank = var8.bank + var9 * var8.bankSpeed;
                     var35.spin = var8.spin + var9 * 4.0;
                     var35.velX = var8.velX;
                     var35.velY = var8.velY;
                     var35.velZ = var8.velZ;
                     var35.thickness = var8.thickness;
                     var35.offsetX = var8.offsetX;
                     var35.offsetY = var8.offsetY;
                     var35.offsetZ = var8.offsetZ;
                     var35.color = var30;
                     var35.overlay = var8.overlay;
                     var35.style = var8.style;
                     var35.blendMode = var8.blendMode;
                     var35.texture = var8.texture;
                     var35.depthSorted = (boolean)(var8.style != ParticleSystem.Style.SOFT_GLOW && var8.style != ParticleSystem.Style.GLOW ? 0 : 1);
                     var35.distanceSq = var17 * var17 + var19 * var19 + var21 * var21;
                     var6 |= var35.blendMode != HitEffectConfig.BlendMode.ADDITIVE ? 1 : 0;
                     this.visibleParticles[this.visibleCount++] = var35;
                     if (var35.overlay) {
                        this.visibleOverlayCount++;
                     }

                     this.hasDepthSortedVisible = this.hasDepthSortedVisible | var35.depthSorted;
                  }
               }
            }
         }
      }

      if (var6 != 0) {
         Arrays.sort(this.visibleParticles, 0, this.visibleCount, fAR_TO_NEAR);
      }

      PerfProfiler.addCounter("Particles prepared", this.visibleCount);
      PerfProfiler.endTimer("Particles prepare", var4);
   }

   public void render(EffectsRenderer var1, Vec3 var2, float var3, boolean var4) {
      long var5 = PerfProfiler.startTimer();
      ParticleSystem.RenderParticle var7 = null;

      for (int var8 = 0; var8 < this.visibleCount; var8++) {
         ParticleSystem.RenderParticle var9 = this.visibleParticles[var8];
         if (!var4 || var9.overlay) {
            if (var7 != null
               && (var9.blendMode != HitEffectConfig.BlendMode.ADDITIVE || var7.blendMode != HitEffectConfig.BlendMode.ADDITIVE)
               && !canBatch(var7, var9)) {
               var1.flush();
            }

            var1.blend(var9.blendMode);
            var1.setSoftParticles(var9.depthSorted);
            if (var9.style == ParticleSystem.Style.RING) {
               var1.drawRing(var9.x, var9.y2, var9.z2, var9.extent, var9.thickness, var9.color, var9.offsetX, var9.offsetY, var9.offsetZ);
            } else if (var9.style == ParticleSystem.Style.BILL) {
               var1.setSoftParticles(var9.x, var9.y2, var9.z2, var9.size, var9.yaw, var9.pitch, var9.bank, var9.spin, var9.color);
            } else if (var9.style == ParticleSystem.Style.CRYSTAL) {
               var1.drawCone(var9.x, var9.y2, var9.z2, var9.size, var9.yaw, var9.pitch, var9.bank, var9.color);
            } else {
               var1.emitParticleQuad(var9.x, var9.y2, var9.z2, var9.size, var9.yaw, var9.color, var9.style, var9.velX, var9.velY, var9.velZ, var9.texture);
            }

            var7 = var9;
         }
      }

      var1.setSoftParticles(false);
      var1.blend(HitEffectConfig.BlendMode.NORMAL);
      PerfProfiler.endTimer("Particles", var5);
   }

   private static boolean canBatch(ParticleSystem.RenderParticle var0, ParticleSystem.RenderParticle var1) {
      return var0.blendMode == var1.blendMode
         && var0.depthSorted == var1.depthSorted
         && var0.texture == var1.texture
         && (var0.style == ParticleSystem.Style.BILL ? 1 : 0) == (var1.style == ParticleSystem.Style.BILL ? 1 : 0)
         && (var0.style == ParticleSystem.Style.CRYSTAL ? 1 : 0) == (var1.style == ParticleSystem.Style.CRYSTAL ? 1 : 0)
         && (var0.style == ParticleSystem.Style.RING ? 1 : 0) == (var1.style == ParticleSystem.Style.RING ? 1 : 0);
   }

   private static final class Particle {
      double x2;
      double y2;
      double z2;
      double prevX;
      double prevY;
      double prevZ;
      double velX;
      double velY;
      double velZ;
      double age;
      double maxAge;
      double size;
      double yaw;
      double yawSpeed;
      double spin;
      double pitch;
      double bank;
      double pitchSpeed;
      double bankSpeed;
      double gravity;
      double damping;
      double wobble;
      double turnRate;
      double cosTurn;
      double sinTurn;
      double ringExtent;
      double thickness;
      double maxViewDistance;
      double orbitAmplitudeY;
      double orbitAmplitudeXZ;
      double orbitPhase;
      double orbitSpeed;
      double orbitX;
      double orbitY;
      double orbitZ;
      double offsetX;
      double offsetY;
      double offsetZ;
      HitEffectConfig.Mode mode;
      boolean landed;
      int color;
      int endColor;
      int emitterId;
      int index;
      boolean overlay;
      boolean mirrored;
      boolean rainbow;
      double alphaPower;
      ParticleSystem.Style style;
      HitEffectConfig.Texture texture;
      Easing.FadeCurve colorScheme;
      Easing.SizeCurve easing;
      HitEffectConfig.BlendMode blendMode;
   }

   private static final class RenderParticle {
      double x;
      double y2;
      double z2;
      double size;
      double extent;
      double thickness;
      double yaw;
      double pitch;
      double bank;
      double spin;
      double velX;
      double velY;
      double velZ;
      double distanceSq;
      double offsetX;
      double offsetY;
      double offsetZ;
      int color;
      boolean overlay;
      boolean depthSorted;
      ParticleSystem.Style style;
      HitEffectConfig.Texture texture;
      HitEffectConfig.BlendMode blendMode;
   }

   public enum Style {
      SPARK,
      GLOW,
      CIRCLE,
      STAR,
      SHARD,
      STREAK,
      FOUR_POINT_STAR,
      RING,
      TRIANGLE,
      CRYSTAL,
      PETAL,
      SOFT_GLOW,
      BILL;
   }
}
