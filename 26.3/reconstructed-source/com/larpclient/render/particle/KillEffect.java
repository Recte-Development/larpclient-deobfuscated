package com.larpclient.render.particle;

import com.larpclient.gui.screen.RugPullScene;
import com.larpclient.render.entity.ItemSwingAnimator;
import com.larpclient.render.model.ModelAssemblyAnimation;
import com.larpclient.render.overlay.ShatterEffect;
import com.larpclient.social.emote.EmoteDanceAnimator;
import com.larpclient.util.math.Easing;
import com.larpclient.util.world.BlockRaycast;
import com.larpclient.util.world.GroundProbeGrid;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

public final class KillEffect {
   public static final int pART_COUNT = 6;
   public static final double sETTLE_TIME = 1.25;
   public static final double dEFAULT_DURATION = 4.6;
   public static final double mODEL_SCALE = 0.9375;
   public static final double[][] pART_OFFSETS = new double[][]{
      {0.0, -4.0, 0.0}, {0.0, 6.0, 0.0}, {-6.0, 6.0, 0.0}, {6.0, 6.0, 0.0}, {-1.9, 18.0, 0.0}, {1.9, 18.0, 0.0}
   };
   private final Vec3[] positions = new Vec3[6];
   private final Vec3[] prevPositions = new Vec3[6];
   private final Vec3[] velocities = new Vec3[6];
   private final double[] spins = new double[6];
   private final double[] prevSpins = new double[6];
   private final boolean[] landed = new boolean[6];
   public final KillEffect.Style style;
   public final Vec3 origin;
   public final double yaw;
   public final double startTime;
   public final long seed;
   public final boolean slimArms;
   private final GroundProbeGrid trail;
   private final YinYangRitualEffect shatterEffect;
   private final ShatterEffect spiderEffect;
   private final RugPullScene jellyEffect;
   private double lastTime;
   private boolean initialized;

   public KillEffect(KillEffect.Style var1, Vec3 var2, double var3, double var5, long var7) {
      this(var1, var2, var3, var5, var7, false);
   }

   public KillEffect(KillEffect.Style var1, Vec3 var2, double var3, double var5, long var7, boolean var9) {
      this.style = var1;
      this.origin = var2;
      this.yaw = var3;
      this.startTime = var5;
      this.seed = var7;
      this.slimArms = var9;
      this.lastTime = var5;
      this.trail = var1 != KillEffect.Style.YIN_YANG && var1 != KillEffect.Style.SPIDER ? null : new GroundProbeGrid(var2);
      this.shatterEffect = var1 == KillEffect.Style.YIN_YANG ? new YinYangRitualEffect(this, this.trail) : null;
      this.spiderEffect = var1 == KillEffect.Style.SPIDER ? new ShatterEffect(this, this.trail) : null;
      this.jellyEffect = var1 == KillEffect.Style.RUG_PULL ? new RugPullScene(this) : null;
   }

   public GroundProbeGrid getTrail() {
      return this.trail;
   }

   public YinYangRitualEffect getShatterEffect() {
      return this.shatterEffect;
   }

   public ShatterEffect partOffset() {
      return this.spiderEffect;
   }

   public RugPullScene isFinished() {
      return this.jellyEffect;
   }

   public double physicsStart() {
      return this.style == KillEffect.Style.SCATTER ? 1.25 : (this.style == KillEffect.Style.SPIDER ? 1.32 : Double.POSITIVE_INFINITY);
   }

   public static double ease(double var0) {
      return Easing.smoothstep(var0);
   }

   public static double physicsStart(double var0, double var2, double var4) {
      return !(var0 < var2) && !(var0 >= var4) ? ease((var0 - var2) / 0.09) * ease((var4 - var0) / 0.22) : 0.0;
   }

   public Vec3 partOffset(int var1) {
      double var2 = getTrail(var1, this.slimArms) / 16.0 * 0.9375;
      return new Vec3(var2 * Math.cos(this.yaw), (24.0 - pART_OFFSETS[var1][1]) / 16.0 * 0.9375, var2 * Math.sin(this.yaw));
   }

   public static double getTrail(int var0, boolean var1) {
      return var1 && (var0 == 2 || var0 == 3) ? pART_OFFSETS[var0][0] + (var0 == 2 ? 0.5 : -0.5) : pART_OFFSETS[var0][0];
   }

   public static double partRadius(int var0) {
      return var0 == 0 ? 0.46 : (var0 == 1 ? 0.47 : 0.42);
   }

   public void update(double var1, BlockRaycast.RayHitTester var3) {
      double var4 = this.lastTime - this.startTime;
      double var6 = var1 - this.startTime;
      this.lastTime = var1;
      if (this.trail != null) {
         this.trail.processQueue(var3, 48);
      }

      double var8 = this.physicsStart();
      if (Double.isFinite(var8) && !(var6 < var8) && !(var1 < this.startTime)) {
         if (!this.initialized) {
            this.initialized = (boolean)1;

            for (int var10 = 0; var10 < 6; var10++) {
               if (this.style == KillEffect.Style.SPIDER) {
                  this.positions[var10] = this.prevPositions[var10] = ShatterEffect.partPose(this, var10, var8).position();
                  this.velocities[var10] = ShatterEffect.randomDirection(this, var10);
               } else {
                  this.positions[var10] = this.prevPositions[var10] = this.origin.add(this.partOffset(var10)).add(0.0, 1.7, 0.0);
                  double var11 = var10 * Math.PI * 2.0 / 6.0 + this.randomFor(var10) * 0.6;
                  this.velocities[var10] = new Vec3(
                     Math.cos(var11) * (1.1 + this.randomFor(var10 + 8)),
                     0.65 + this.randomFor(var10 + 15),
                     Math.sin(var11) * (1.1 + this.randomFor(var10 + 8))
                  );
               }
            }

            var4 = var8;
         }

         double var25 = Math.clamp(var6 - Math.max(var4, var8), 0.0, 0.05);

         for (int var12 = 0; var12 < 6; var12++) {
            this.prevPositions[var12] = this.positions[var12];
            this.prevSpins[var12] = this.spins[var12];
            if (!this.landed[var12] && var25 != 0.0) {
               this.velocities[var12] = this.velocities[var12].add(0.0, -8.8 * var25, 0.0);
               Vec3 var13 = this.positions[var12].add(this.velocities[var12].scale(var25));
               double var14 = partRadius(var12);
               Vec3 var16 = new Vec3(0.0, -var14, 0.0);
               BlockRaycast.RayHit var17 = var3 == null ? null : var3.test(this.positions[var12].add(var16), var13.add(var16));
               Vec3 var18 = var13.subtract(this.positions[var12]);

               for (int var19 = 0; var19 < 2 && var3 != null; var19++) {
                  double var20 = var19 == 0 ? var18.x : var18.z;
                  if (!(Math.abs(var20) < 1.0E-8)) {
                     Vec3 var22 = var19 == 0 ? new Vec3(Math.copySign(var14, var20), 0.0, 0.0) : new Vec3(0.0, 0.0, Math.copySign(var14, var20));
                     Vec3 var23 = this.positions[var12].add(var22);
                     BlockRaycast.RayHit var24 = var3.test(var23, var13.add(var22));
                     if (var24 != null && (var17 == null || var23.distanceToSqr(var24.point()) < this.positions[var12].add(var16).distanceToSqr(var17.point()))
                        )
                      {
                        var17 = var24;
                        var16 = var22;
                     }
                  }
               }

               if (var17 != null) {
                  this.positions[var12] = var17.point().subtract(var16).add(var17.normal().scale(var14 + 0.025 + var16.dot(var17.normal())));
                  if (var17.normal().y > 0.5) {
                     this.landed[var12] = (boolean)1;
                     this.velocities[var12] = Vec3.ZERO;
                     this.positions[var12] = new Vec3(this.positions[var12].x, var17.point().y + (var12 == 0 ? 0.27 : 0.15), this.positions[var12].z);
                  } else {
                     this.velocities[var12] = new Vec3(0.0, this.velocities[var12].y * 0.85, 0.0);
                  }
               } else {
                  this.positions[var12] = var13;
               }

               if (!this.landed[var12]) {
                  this.spins[var12] = this.spins[var12] + var25 * (2.8 + this.randomFor(var12 + 22) * 2.0);
               }
            }
         }
      }
   }

   public KillEffect.PartState getPartState(int var1, double var2, double var4) {
      return this.computePartState(var1, var2, var4, ModelAssemblyAnimation.dEFAULT_BASIS);
   }

   public KillEffect.PartState computePartState(int var1, double var2, double var4, ModelAssemblyAnimation.Basis var6) {
      if (this.style == KillEffect.Style.MODEL_ASSEMBLY) {
         return ModelAssemblyAnimation.computePartPose(this, var1, var2, var6);
      }

      if (this.style == KillEffect.Style.ITEM_SWING) {
         return ItemSwingAnimator.heldItemPose(this, var1, var2);
      }

      if (this.style == KillEffect.Style.EMOTE_DANCE) {
         return EmoteDanceAnimator.risePose(this, var1, var2);
      }

      if (this.style == KillEffect.Style.YIN_YANG) {
         return YinYangRitualEffect.faceTransform(this, var1, var2);
      }

      if (this.style == KillEffect.Style.RUG_PULL) {
         return RugPullScene.rugOffset(this, var1, var2);
      }

      Vec3 var7 = this.partOffset(var1);
      double var8 = ease((4.6 - var2) / 0.75);
      if (this.style == KillEffect.Style.SPIDER) {
         if (!(var2 < 1.32) && this.initialized) {
            double var23 = this.prevSpins[var1] + (this.spins[var1] - this.prevSpins[var1]) * Math.clamp(var4, 0.0, 1.0);
            double var25 = ease((var2 - 2.7 - this.randomFor(var1 + 40) * 0.3) / 1.2);
            return new KillEffect.PartState(
               this.prevPositions[var1].lerp(this.positions[var1], Math.clamp(var4, 0.0, 1.0)),
               this.landed[var1] ? Math.PI / 2 : var23 * 0.75 + ShatterEffect.maxTilt(),
               this.landed[var1] ? 0.0 : var23 * 0.45,
               this.landed[var1] ? var23 * 0.45 : var23 + ShatterEffect.partSwayFor(var1),
               1.0,
               var8,
               var25,
               this.landed[var1]
            );
         } else {
            return ShatterEffect.partPose(this, var1, var2);
         }
      } else if (this.style == KillEffect.Style.SCATTER) {
         if (!(var2 < 1.25) && this.initialized) {
            double var22 = this.prevSpins[var1] + (this.spins[var1] - this.prevSpins[var1]) * Math.clamp(var4, 0.0, 1.0);
            return new KillEffect.PartState(
               this.prevPositions[var1].lerp(this.positions[var1], Math.clamp(var4, 0.0, 1.0)),
               this.landed[var1] ? Math.PI / 2 : var22 * 0.75 + (var1 < 2 ? -0.07 : 0.0),
               this.landed[var1] ? 0.0 : var22 * 0.45,
               this.landed[var1] ? var22 * 0.45 : var22 + (var1 == 2 ? -0.32 : (var1 == 3 ? 0.32 : 0.0)),
               1.0,
               var8,
               0.0,
               this.landed[var1]
            );
         } else {
            double var21 = 1.7 * ease(var2 / 0.85);
            double var24 = ease(var2 / 0.8);
            return new KillEffect.PartState(
               this.origin.add(var7).add(0.0, var21, 0.0),
               var1 < 2 ? -0.07 * var24 : 0.0,
               0.0,
               var1 == 2 ? -0.32 * var24 : (var1 == 3 ? 0.32 * var24 : 0.0),
               1.0,
               var8,
               0.0,
               false
            );
         }
      } else if (this.style == KillEffect.Style.VORTEX) {
         double var20 = ease((var2 - 0.45) / 1.45);
         double var12 = var20 * 8.5;
         Vec3 var14 = var7.subtract(0.0, 1.05, 0.0).scale(1.0 - var20);
         double var15 = Math.cos(var12);
         double var17 = Math.sin(var12);
         Vec3 var19 = new Vec3(var14.x * var15 - var14.z * var17, var14.y, var14.x * var17 + var14.z * var15);
         return new KillEffect.PartState(
            this.origin.add(0.0, 1.05 + 0.35 * Math.sin(var20 * Math.PI), 0.0).add(var19),
            var20 * 2.0,
            var12,
            var20 * 1.2,
            Math.max(0.0, 1.0 - var20),
            1.0 - var20,
            0.0,
            false
         );
      } else {
         double var10 = ease((var2 - 0.35 - (1.8 - var7.y) * 0.3) / 1.2);
         return new KillEffect.PartState(
            this.origin.add(var7).add(Math.sin(var2 * 2.0 + var1) * var10 * 0.04, var10 * 0.38, 0.0),
            var10 * 0.1,
            0.0,
            0.0,
            1.0,
            1.0 - var10 * 0.2,
            var10,
            false
         );
      }
   }

   public double duration() {
      return this.style == KillEffect.Style.EMOTE_DANCE
         ? 7.5
         : (this.style == KillEffect.Style.YIN_YANG ? 8.0 : (this.style == KillEffect.Style.RUG_PULL ? 6.4 : 4.6));
   }

   public boolean isFinished(double var1) {
      return var1 - this.startTime >= this.duration();
   }

   public boolean update(int var1) {
      return this.landed[var1];
   }

   public double randomFor(int var1) {
      long var2 = this.seed + var1 * -7046029254386353131L;
      var2 = (var2 ^ var2 >>> 30) * -4658895280553007687L;
      var2 = (var2 ^ var2 >>> 27) * -7723592293110705685L;
      return ((var2 ^ var2 >>> 31) >>> 11) * 1.110223E-16F;
   }

   public record PartState(
      Vec3 position, double rx, double ry, double rz, double scale, double alpha, double dissolve, boolean landed, Quaternionfc orientation, Vec3 stretch
   ) {
      private final double rotX;
      private final double rotY;
      private final double rotZ;

      public PartState(Vec3 var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
         this(var1, var2, var4, var6, var8, var10, var12, var14, null, null);
      }

      public PartState(
         Vec3 position, double rx, double ry, double rz, double scale, double alpha, double dissolve, boolean landed, Quaternionfc orientation, Vec3 stretch
      ) {
         this.position = position;
         this.rotX = rx;
         this.rotY = ry;
         this.rotZ = rz;
         this.scale = scale;
         this.alpha = alpha;
         this.dissolve = dissolve;
         this.landed = landed;
         this.orientation = orientation;
         this.stretch = stretch;
      }
   }

   public enum Style {
      SCATTER,
      VORTEX,
      ASCEND,
      MODEL_ASSEMBLY,
      ITEM_SWING,
      EMOTE_DANCE,
      YIN_YANG,
      SPIDER,
      RUG_PULL;
   }
}
