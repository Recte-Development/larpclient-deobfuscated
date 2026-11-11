package com.larpclient.render.particle;

import com.larpclient.render.entity.SwayAnimator;
import com.larpclient.render.world.FloatingWord;
import com.larpclient.setting.RangeSettings;
import com.larpclient.social.misc.RandomMessageGenerator;
import com.larpclient.util.data.PositionSampler;
import java.util.Random;

public final class ParticleSimulation {
   public static final int mAX_PARTICLES = 96;
   private static final double rESET_DISTANCE = 48.0;
   private final FloatingWord[] particles = new FloatingWord[96];
   private final Random random;
   private final PositionSampler spawner;
   private RandomMessageGenerator textSource;
   private boolean initialized;
   private double lastX;
   private double lastY;
   private double lastZ;
   private double elapsed;
   private int generation;
   public double smoothedX;
   public double smoothedY;
   public double smoothedZ;
   public double smoothedYaw;

   public ParticleSimulation(long var1, RandomMessageGenerator var3) {
      this.random = new Random(var1);
      this.spawner = new PositionSampler(this.random);
      this.textSource = var3;

      for (int var4 = 0; var4 < 96; var4++) {
         this.particles[var4] = new FloatingWord();
      }
   }

   public FloatingWord[] getParticles() {
      return this.particles;
   }

   public double getElapsed() {
      return this.elapsed;
   }

   public void setTextSource(RandomMessageGenerator var1) {
      this.textSource = var1;
      this.invalidate();
   }

   public void invalidate() {
      this.generation++;
   }

   public void reset() {
      this.initialized = (boolean)0;
      this.elapsed = 0.0;
      this.smoothedYaw = 0.0;

      for (FloatingWord var4 : this.particles) {
         var4.active = (boolean)0;
         var4.state = FloatingWord.State.IDLE;
         var4.alpha = var4.spawnCooldown = 0.0;
         var4.text = "";
      }
   }

   public static double smoothingFactor(double var0, double var2) {
      return -Math.expm1(-var0 * Math.max(0.0, var2));
   }

   public static double wrapAngle(double var0) {
      return Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public boolean update(RangeSettings var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      double var18 = var2 - this.lastX;
      double var20 = var4 - this.lastY;
      double var22 = var6 - this.lastZ;
      int var24 = this.initialized && var18 * var18 + var20 * var20 + var22 * var22 > 2304.0 ? 1 : 0;
      if (!this.initialized || var24 != 0) {
         this.reset();
         this.smoothedX = var2;
         this.smoothedY = var4;
         this.smoothedZ = var6;
         this.initialized = (boolean)1;
         this.smoothedYaw = var1.mode == RangeSettings.YawMode.FOLLOW_PLAYER ? var8 : 0.0;
      }

      this.lastX = var2;
      this.lastY = var4;
      this.lastZ = var6;
      double var25 = smoothingFactor(var1.range, var16);
      this.smoothedX = this.smoothedX + (var2 - this.smoothedX) * var25;
      this.smoothedY = this.smoothedY + (var4 - this.smoothedY) * var25;
      this.smoothedZ = this.smoothedZ + (var6 - this.smoothedZ) * var25;
      double var27 = var1.mode == RangeSettings.YawMode.FIXED ? 0.0 : var8;
      this.smoothedYaw = wrapAngle(this.smoothedYaw + wrapAngle(var27 - this.smoothedYaw) * smoothingFactor(2.5, var16));
      this.elapsed += var16;
      int var29 = var12 * var12 + var14 * var14 > 1.0E-4 ? 1 : 0;
      int var30 = var1.style != RangeSettings.Heading.LOOK && (var1.style != RangeSettings.Heading.VELOCITY || var29 == 0) ? 0 : 1;
      double var31 = (var1.style == RangeSettings.Heading.VELOCITY ? Math.atan2(var12, var14) : var10) - this.smoothedYaw;
      int var33 = Math.clamp(var1.maxTargets, 0, 96);

      for (int var34 = 0; var34 < 96; var34++) {
         FloatingWord var35 = this.particles[var34];
         if (var34 >= var33 || var35.generation != this.generation) {
            var35.beginFadeOut();
         }

         if (var35.state != FloatingWord.State.IDLE) {
            SwayAnimator.update(var35, var1, var16);
         }

         if (var35.state == FloatingWord.State.IDLE && var34 < var33) {
            var35.spawnCooldown -= var16;
            if (!(var35.spawnCooldown > 0.0)) {
               String var36 = this.textSource.next(this.elapsed, this.random);
               if (var36 == null || var36.isBlank()) {
                  var35.spawnCooldown = 0.1;
               } else if (!this.spawner.placeEntity(var35, this.particles, var1, this.random, var31, (boolean)var30)) {
                  var35.spawnCooldown = 0.75 + this.random.nextDouble() * 0.5;
               } else {
                  var35.text = var36;
                  var35.jitter = this.random.nextDouble() * 2.0 - 1.0;
                  var35.phase = this.random.nextDouble() * Math.PI * 2.0;
                  var35.speed = 0.5 + this.random.nextDouble() * 0.7;
                  var35.driftPhase = this.random.nextDouble() * Math.PI * 2.0;
                  var35.driftX = (this.random.nextDouble() - 0.5) * 0.3;
                  var35.driftY = (this.random.nextDouble() - 0.5) * 0.16;
                  var35.age = var35.stateTime = var35.alpha = 0.0;
                  var35.lifetime = var1.maxAngle * (0.65 + this.random.nextDouble() * 0.7);
                  var35.generation = this.generation;
                  var35.active = (boolean)1;
                  var35.state = FloatingWord.State.FADE_IN;
                  SwayAnimator.update(var35, var1, 0.0);
               }
            }
         }
      }

      return (boolean)var24;
   }
}
