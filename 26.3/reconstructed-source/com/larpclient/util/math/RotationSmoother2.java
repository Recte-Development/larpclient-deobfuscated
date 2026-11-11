package com.larpclient.util.math;

import com.larpclient.module.combat.AimNoiseGenerator;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.combat.Rotation;
import com.larpclient.module.combat.RotationQuantizer;
import java.util.SplittableRandom;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class RotationSmoother2 {
   static final double dRAG = 0.546;
   static final double lATCH_MIN_SPEED = 0.4;
   static final double fORWARD_OFFSET = 1.5;
   private static final float nEAR_THRESHOLD = 6.0F;
   private static final int nO_OCTANT = Integer.MIN_VALUE;
   static final double sCALE = 0.85;
   static final float cLOSE_DISTANCE = 6.0F;
   static final float mAX_OVERSHOOT = 1.2F;
   static final float mAX_JITTER = 1.4F;
   static final float mIN_STEP_CAP = 0.35F;
   static final float mIN_SPEED = 0.3F;
   private final RotationSmoother2.Profile profile;
   private final AimNoiseGenerator noise = new AimNoiseGenerator(AimNoiseGenerator.pROFILE_STEADY);
   private final RotationQuantizer sensitivityFixer = new RotationQuantizer();
   private final SplittableRandom random = new SplittableRandom();
   private float yawVelocity;
   private float pitchVelocity;
   private float holdThreshold;
   private boolean holding;
   private boolean latched;
   private float currentSpeed;
   private float speedScale = 1.0F;
   private float curveStrength;
   private float overshootYaw;
   private float overshootPitch;
   private float startDistance;
   private float lastTargetYaw = Float.NaN;
   private float lastTargetPitch = Float.NaN;
   private float jitterYaw;
   private float jitterPitch;
   private float jitterTargetYaw;
   private float jitterTargetPitch;
   private int jitterTicks;
   private int pauseTicks;
   private boolean anchored;
   private boolean reduced;
   private float anchorYaw;
   private float anchorPitch;
   static final float mAX_ANCHOR = 4.0F;
   private int latchCountdown = -1;
   private int lastOctant = Integer.MIN_VALUE;

   public RotationSmoother2() {
      this(RotationSmoother2.Profile.STEADY);
   }

   public RotationSmoother2(RotationSmoother2.Profile var1) {
      this.profile = var1;
      this.reset();
   }

   public void reset() {
      this.yawVelocity = this.pitchVelocity = 0.0F;
      this.holding = this.latched = (boolean)0;
      this.latchCountdown = -1;
      this.lastOctant = Integer.MIN_VALUE;
      this.holdThreshold = this.randomHoldThreshold();
      this.noise.reset();
      this.sensitivityFixer.reset();
      this.currentSpeed = 0.0F;
      this.speedScale = 1.0F;
      this.curveStrength = this.overshootYaw = this.overshootPitch = this.startDistance = 0.0F;
      this.lastTargetYaw = this.lastTargetPitch = Float.NaN;
      this.jitterYaw = this.jitterPitch = this.jitterTargetYaw = this.jitterTargetPitch = 0.0F;
      this.jitterTicks = this.pauseTicks = 0;
      this.anchored = (boolean)0;
      this.anchorYaw = this.anchorPitch = 0.0F;
   }

   public float[] rotate(float var1, float var2, float var3, float var4, boolean var5) {
      return this.rotateWithMode(var1, var2, var3, var4, var5, false);
   }

   public float[] rotateWithMode(float var1, float var2, float var3, float var4, boolean var5, boolean var6) {
      this.reduced = var6;
      if (this.profile.glide()) {
         return this.glideRotate(var1, var2, var3, var4, var5);
      }

      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = Mth.clamp(var4, -90.0F, 90.0F) - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (!var5 || !(var9 <= this.holdThreshold) && (!this.holding || !(var9 <= 3.5F))) {
         this.holding = (boolean)0;
         int var10 = var9 < 6.0F ? 1 : 0;
         this.noise.updateWrist();
         float var11 = this.noise.subMovementFactor(var9, (boolean)var10);
         this.yawVelocity = approachAngle(
            var7,
            this.yawVelocity,
            12.0F * this.noise.nextGain(AimNoiseGenerator.Axis.YAW, (boolean)var10) * var11,
            3.0F,
            0.32F,
            this.noise.sampleNoise(AimNoiseGenerator.Axis.YAW) * 0.08F
         );
         this.pitchVelocity = approachAngle(
            var8,
            this.pitchVelocity,
            9.0F * this.noise.nextGain(AimNoiseGenerator.Axis.PITCH, (boolean)var10) * var11,
            2.25F,
            0.32F,
            this.noise.sampleNoise(AimNoiseGenerator.Axis.PITCH) * 0.06F
         );
         this.pitchVelocity = clampToward(this.pitchVelocity + this.noise.wristPitchOffset(this.yawVelocity), var8);
         Rotation var12 = this.sensitivityFixer.quantizeDefault(new Rotation(var1, var2), new Rotation(var1 + this.yawVelocity, var2 + this.pitchVelocity));
         return new float[]{var12.yaw, var12.pitch};
      }

      if (!this.holding) {
         this.holdThreshold = this.randomHoldThreshold();
      }

      this.holding = (boolean)1;
      this.yawVelocity = this.pitchVelocity = 0.0F;
      return new float[]{var1, var2};
   }

   private float[] glideRotate(float var1, float var2, float var3, float var4, boolean var5) {
      var4 = Mth.clamp(var4, -90.0F, 90.0F);
      if (Float.isNaN(this.lastTargetYaw) || Math.hypot(Mth.wrapDegrees(var3 - this.lastTargetYaw), var4 - this.lastTargetPitch) > 3.0) {
         this.beginSegment(var1, var2, var3, var4);
      }

      this.lastTargetYaw = var3;
      this.lastTargetPitch = var4;
      this.noise.updateWrist();
      float var6;
      float var7;
      if (var5) {
         if (!this.anchored) {
            this.anchored = (boolean)1;
            float var8 = this.reduced ? 0.0F : 4.0F;
            this.anchorYaw = Mth.clamp(Mth.wrapDegrees(var1 - var3), -var8, var8);
            this.anchorPitch = Mth.clamp(var2 - var4, -var8, var8);
         }

         var3 += this.anchorYaw;
         var4 = Mth.clamp(var4 + this.anchorPitch, -90.0F, 90.0F);
         if (--this.jitterTicks <= 0) {
            double var26 = this.random.nextDouble() * Math.PI * 2.0;
            double var10 = Math.sqrt(this.random.nextDouble()) * 1.4F * (this.reduced ? 0.3F : 1.0F);
            this.jitterTargetYaw = (float)(Math.cos(var26) * var10);
            this.jitterTargetPitch = (float)(Math.sin(var26) * var10 * 0.7);
            this.jitterTicks = 12 + this.random.nextInt(20);
         }

         this.jitterYaw = this.jitterYaw + (this.jitterTargetYaw - this.jitterYaw) * 0.12F;
         this.jitterPitch = this.jitterPitch + (this.jitterTargetPitch - this.jitterPitch) * 0.12F;
         float var27 = Mth.wrapDegrees(var3 + this.jitterYaw - var1);
         float var9 = Mth.clamp(var4 + this.jitterPitch, -90.0F, 90.0F) - var2;
         float var31 = (float)Math.hypot(var27, var9);
         this.currentSpeed *= 0.45F;
         float var11 = Math.max(this.currentSpeed, Math.min(0.35F, var31 * 0.15F));
         float var12 = Math.min(var11, var31);
         var6 = var31 < 1.0E-4F ? 0.0F : var27 / var31 * var12;
         var7 = var31 < 1.0E-4F ? 0.0F : var9 / var31 * var12;
      } else {
         this.anchored = (boolean)0;
         if (this.pauseTicks > 0) {
            this.pauseTicks--;
            return new float[]{var1, var2};
         }

         float var28 = Mth.wrapDegrees(var3 + this.overshootYaw - var1);
         float var30 = Mth.clamp(var4 + this.overshootPitch, -90.0F, 90.0F) - var2;
         float var32 = (float)Math.hypot(var28, var30);
         if (var32 < 1.5F && (this.overshootYaw != 0.0F || this.overshootPitch != 0.0F)) {
            this.overshootYaw = this.overshootPitch = 0.0F;
            var28 = Mth.wrapDegrees(var3 - var1);
            var30 = var4 - var2;
            var32 = (float)Math.hypot(var28, var30);
            if (this.profile == RotationSmoother2.Profile.QUICK && var32 > 1.5F && !this.reduced) {
               this.currentSpeed *= 0.35F;
               this.pauseTicks = this.random.nextInt(2);
               this.startDistance = var32;
            }
         }

         if (var32 < 1.0E-4F) {
            return new float[]{var1, var2};
         }

         float var33 = this.profile.speed() * this.speedScale * (0.9F + 0.1F * this.noise.nextGain(AimNoiseGenerator.Axis.YAW, var32 < 6.0F));
         float var34 = (float)Math.sqrt(2.0F * this.profile.braking() * var32) - this.profile.braking() * 0.5F;
         float var13 = Math.max(0.3F, Math.min(var33, Math.min(var34, var32 * this.profile.share())));
         this.currentSpeed = Math.min(
            var32, this.currentSpeed + Mth.clamp(var13 - this.currentSpeed, -this.profile.braking() * 1.5F, this.profile.acceleration())
         );
         float var14 = this.startDistance > 0.0F ? Mth.clamp(1.0F - var32 / this.startDistance, 0.0F, 1.0F) : 1.0F;
         float var15 = this.curveStrength * Mth.sin((float) Math.PI * var14);
         float var16 = var28 / var32;
         float var17 = var30 / var32;
         float var18 = var16 - var17 * var15;
         float var19 = var17 + var16 * var15;
         float var20 = (float)Math.hypot(var18, var19);
         var6 = var18 / var20 * this.currentSpeed + this.noise.sampleNoise(AimNoiseGenerator.Axis.YAW) * 0.04F * this.currentSpeed;
         var7 = var19 / var20 * this.currentSpeed
            + this.noise.sampleNoise(AimNoiseGenerator.Axis.PITCH) * 0.03F * this.currentSpeed
            + this.noise.wristPitchOffset(var18 / var20 * this.currentSpeed) * 0.5F;
         var6 = clampToward(var6, var28);
         var7 = clampToward(var7, var30);
      }

      Rotation var29 = this.sensitivityFixer.quantizeDefault(new Rotation(var1, var2), new Rotation(var1 + var6, var2 + var7));
      return new float[]{var29.yaw, var29.pitch};
   }

   private void beginSegment(float var1, float var2, float var3, float var4) {
      float var5 = Mth.wrapDegrees(var3 - var1);
      float var6 = var4 - var2;
      this.startDistance = (float)Math.hypot(var5, var6);
      int var7 = var6 > 0.0F && var6 > Math.abs(var5) * 0.5F ? 1 : 0;
      this.speedScale = (0.88F + (float)this.random.nextDouble() * 0.24F) * (var7 != 0 ? 0.92F : 1.0F);
      this.curveStrength = (float)(this.random.nextDouble() * 2.0 - 1.0) * 0.16F;
      if (this.profile == RotationSmoother2.Profile.QUICK && this.startDistance > 2.0F && !this.reduced) {
         double var14 = this.random.nextDouble() < 0.85 ? -(0.02 + this.random.nextDouble() * 0.1) * (var7 != 0 ? 1.3 : 1.0) : this.random.nextDouble() * 0.03;
         var14 = Mth.clamp(var14 * this.startDistance, -6.0, 1.2F);
         double var10 = (this.random.nextDouble() * 2.0 - 1.0) * Math.abs(var14) / 2.25;
         float var12 = var5 / this.startDistance;
         float var13 = var6 / this.startDistance;
         this.overshootYaw = (float)(var12 * var14 - var13 * var10);
         this.overshootPitch = (float)(var13 * var14 + var12 * var10);
      } else {
         float var8 = Math.min(1.2F, this.startDistance * 0.03F);
         this.overshootYaw = (float)(this.random.nextDouble() * 2.0 - 1.0) * var8;
         this.overshootPitch = (float)(this.random.nextDouble() * 2.0 - 1.0) * var8 * 0.7F;
      }

      this.jitterYaw = this.jitterPitch = 0.0F;
      this.jitterTicks = 0;
      this.anchored = (boolean)0;
      this.pauseTicks = this.startDistance > 8.0F ? this.random.nextInt(3) : 0;
   }

   private static float approachAngle(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = Mth.clamp(var0 * var4 * (1.0F + var5), -var2, var2);
      return clampToward(var1 + Mth.clamp(var6 - var1, -var3, var3), var0);
   }

   private static float clampToward(float var0, float var1) {
      return Math.signum(var0) != Math.signum(var1) ? 0.0F : Math.copySign(Math.min(Math.abs(var0), Math.abs(var1)), var1);
   }

   private float randomHoldThreshold() {
      return 0.8F + (float)this.random.nextDouble() * 1.2F;
   }

   public boolean updateLatch(double var1, double var3, boolean var5) {
      if (var5 && !(var1 <= 0.0)) {
         if (this.latched) {
            if (var1 <= slideDistance(var3) + 0.04) {
               this.latched = (boolean)0;
            }
         } else if (var1 > 0.4) {
            if (this.latchCountdown < 0) {
               this.latchCountdown = this.random.nextInt(1, 5);
            } else if (--this.latchCountdown <= 0) {
               this.latched = (boolean)1;
               this.latchCountdown = -1;
            }
         } else {
            this.latchCountdown = -1;
         }

         return this.latched;
      } else {
         this.latched = (boolean)0;
         this.latchCountdown = -1;
         return false;
      }
   }

   public boolean isLatched() {
      return this.latched;
   }

   public static double slideDistance(double var0) {
      return Math.max(0.0, var0) * 0.546 / 0.45399999999999996;
   }

   public Input directionToInput(float var1, Vec3 var2) {
      double var3 = Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0;
      double var5 = Mth.wrapDegrees(var3 - var1);
      int var7 = (int)Math.round(var5 / 45.0);
      if (var7 == -4) {
         var7 = 4;
      }

      if (this.lastOctant != Integer.MIN_VALUE && Math.abs(Mth.wrapDegrees(var5 - this.lastOctant * 45.0)) < 28.5) {
         var7 = this.lastOctant;
      }

      this.lastOctant = var7;
      float[] var8 = AuraModule.getMoveKeys(var1 + var7 * 45.0, var1);
      return new Input(var8[0] > 0.0F, var8[0] < 0.0F, var8[1] > 0.0F, var8[1] < 0.0F, false, false, false);
   }

   public static Vec2 inputToVector(Input var0) {
      float var1 = var0.forward() == var0.backward() ? 0.0F : (var0.forward() ? 1.0F : -1.0F);
      float var2 = var0.left() == var0.right() ? 0.0F : (var0.left() ? 1.0F : -1.0F);
      return new Vec2(var2, var1).normalized();
   }

   public static double forwardOffset(Vec3 var0, Vec3 var1, Direction var2) {
      return (var1.x - var0.x) * var2.getStepX() + (var1.z - var0.z) * var2.getStepZ();
   }

   public static double sideOffset(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      return (var1.x - var0.x) * var3.getStepX() + (var1.z - var0.z) * var3.getStepZ();
   }

   public static Vec3 targetOffset(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      double var4 = sideOffset(var0, var1, var2);
      return new Vec3(var2.getStepX() * 1.5 + var3.getStepX() * var4, 0.0, var2.getStepZ() * 1.5 + var3.getStepZ() * var4);
   }

   public record Profile(float speed, float acceleration, float braking, float share, boolean glide) {
      public static final RotationSmoother2.Profile STEADY = new RotationSmoother2.Profile(12.0F, 3.0F, 3.0F, 0.32F, false);
      public static final RotationSmoother2.Profile QUICK = new RotationSmoother2.Profile(18.0F, 4.5F, 4.0F, 0.45F, true);
   }
}
