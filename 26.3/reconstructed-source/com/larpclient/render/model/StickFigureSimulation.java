package com.larpclient.render.model;

import com.larpclient.gui.misc.PlayerPreviewAnimator;
import java.util.Arrays;

final class StickFigureSimulation {
   static final double fEET_OFFSET = 1.15;
   static final double hEAD_OFFSET = 0.85;
   static final float gRAB_RADIUS = 0.35F;
   private static final double[][] bODY_POINTS = new double[][]{
      {-0.25, -0.85}, {0.25, -0.85}, {-0.5, -0.33}, {0.5, -0.33}, {-0.5, 0.38}, {0.5, 0.38}, {-0.25, 1.15}, {0.25, 1.15}
   };
   static final double fIXED_STEP = 0.004166666666666667;
   private static final double mAX_FALL_SPEED = 30.0;
   private static final double gRAVITY_FACTOR = 0.6;
   private static final double mAX_SPEED = 34.0;
   private static final double mAX_ANGULAR_SPEED = 22.0;
   private static final double iNERTIA_FACTOR = 4.2;
   private static final double sPRING_DAMPING = 0.75;
   private static final double bALANCE_DAMPING = 1.8;
   private static final double mAX_SPRING_FORCE = 60.0;
   private static final double sWAY_AMOUNT = 0.8;
   private static final double tIP_TORQUE = 6.0;
   private static final double bOUNCE_GROUND = 0.32;
   private static final double bOUNCE_WALL = 0.45;
   private static final double rECOVER_WINDOW = 0.6;
   private static final double iMPACT_THRESHOLD = 3.0;
   private static final double mAX_WALK_SPEED = 5.6;
   private static final double wALK_ACCELERATION = 20.0;
   private static final double wALK_DECELERATION = 16.0;
   private static final double sTAND_DURATION = 0.22;
   private static final double mIN_WALK_SPEED = 0.5;
   private static final double gET_UP_DURATION = 0.55;
   private static final double gET_UP_DURATION_SHORT = 0.62;
   private static final double gET_UP_DURATION_LONG = 0.7;
   private static final double fLIP_ANGLE_THRESHOLD = 2.2;
   private PlayerPreviewAnimator.Stage state;
   private double posX;
   private double posY;
   private double velX;
   private double velY;
   private double angle;
   private double angularVelocity;
   private double grabLocalX;
   private double grabLocalY;
   private double pointerX;
   private double pointerY;
   private double prevPointerX;
   private double prevPointerY;
   private double pointerVelX;
   private double pointerVelY;
   private double stepAccumulator;
   private double stateTime;
   private double turnTime;
   private double elapsedTime;
   private double restTime;
   private double peakImpact;
   private double dangleTime;
   private double stridePhase;
   private double strideAmount;
   private double walkSpeed;
   private double uprightness;
   private double yaw;
   private double headRoll;
   private double headPitch;
   private double turnStartYaw;
   private double recoverAngle;
   private double recoverStartX;
   private double recoverStartY;
   private double lieDuration;
   private double walkDirection;
   private boolean onGround;
   private boolean landedUpright;
   private boolean dazed;
   private boolean landingPuff;
   private float impactStrength;
   private final double[] poseCurrent = new double[10];
   private final double[] poseTarget = new double[10];

   StickFigureSimulation(double var1, double var3, double var5) {
      this.posX = var1;
      this.posY = var3;
      this.yaw = var5;
   }

   PlayerPreviewAnimator.Stage getState() {
      return this.state;
   }

   boolean isHeld() {
      return this.state == PlayerPreviewAnimator.Stage.HELD;
   }

   void grab(double var1, double var3, double var5) {
      double var7 = (var1 - this.posX) / var5;
      double var9 = (var3 - this.posY) / var5;
      double var11 = Math.cos(this.angle);
      double var13 = Math.sin(this.angle);
      this.grabLocalX = var7 * var11 + var9 * var13;
      this.grabLocalY = -var7 * var13 + var9 * var11;
      this.pointerX = this.prevPointerX = var1;
      this.pointerY = this.prevPointerY = var3;
      this.pointerVelX = this.pointerVelY = 0.0;
      if (this.state != PlayerPreviewAnimator.Stage.THROWN && this.state != PlayerPreviewAnimator.Stage.WALKING) {
         this.velX = this.velY = this.angularVelocity = 0.0;
      }

      if (this.state == PlayerPreviewAnimator.Stage.WALKING) {
         this.velY = this.angularVelocity = 0.0;
      }

      this.peakImpact = this.restTime = 0.0;
      this.setState(PlayerPreviewAnimator.Stage.HELD);
   }

   void dragTo(double var1, double var3) {
      this.pointerX = var1;
      this.pointerY = var3;
   }

   boolean release() {
      if (this.state != PlayerPreviewAnimator.Stage.HELD) {
         return false;
      }

      this.setState(PlayerPreviewAnimator.Stage.THROWN);
      return true;
   }

   boolean hitTest(double var1, double var3, double var5) {
      double var7 = (var1 - this.posX) / var5;
      double var9 = (var3 - this.posY) / var5;
      double var11 = Math.cos(this.angle);
      double var13 = Math.sin(this.angle);
      return containsPoint(var7 * var11 + var9 * var13, -var7 * var13 + var9 * var11, Math.abs(this.yaw) > 45.0);
   }

   static boolean containsPoint(double var0, double var2, boolean var4) {
      double var5 = 0.06;
      if (!(var2 < -0.85 - var5) && !(var2 > 1.15 + var5)) {
         double var7 = !(var2 < -0.35) && !(var2 > 0.4) ? (var4 ? 0.3 : 0.5) : 0.27;
         return Math.abs(var0) <= var7 + var5;
      } else {
         return false;
      }
   }

   PlayerPreviewAnimator.PreviewFrame update(
      double var1, double var3, double var5, double var7, double var9, float var11, float var12, float var13, float var14
   ) {
      var1 = Math.min(var1, 0.05);
      this.impactStrength = 0.0F;
      if (var1 > 0.0) {
         double var15 = -Math.expm1(-var1 / 0.035);
         this.pointerVelX = this.pointerVelX + ((this.pointerX - this.prevPointerX) / var1 - this.pointerVelX) * var15;
         this.pointerVelY = this.pointerVelY + ((this.pointerY - this.prevPointerY) / var1 - this.pointerVelY) * var15;
      }

      double var33 = this.prevPointerX;
      double var17 = this.prevPointerY;
      this.prevPointerX = this.pointerX;
      this.prevPointerY = this.pointerY;
      this.stepAccumulator = Math.min(this.stepAccumulator + var1, 0.05);
      int var19 = (int)(this.stepAccumulator / 0.004166666666666667);
      this.stepAccumulator -= var19 * 0.004166666666666667;

      for (int var20 = 0; var20 < var19 && this.state != null; var20++) {
         double var21 = (var20 + 1.0) / var19;
         this.stepSimulation(var3, var5, var7, var9, var33 + (this.pointerX - var33) * var21, var17 + (this.pointerY - var17) * var21, var11);
      }

      if (this.state == null) {
         return null;
      }

      float var34 = (float)this.yaw;
      float var35 = (float)this.headRoll;
      float var22 = (float)this.headPitch;
      if (this.state == PlayerPreviewAnimator.Stage.STANDING) {
         double var23 = stepSimulation(this.stateTime / 0.48);
         double var25 = stepSimulation(this.stateTime / 0.33599999999999997);
         var34 = (float)(this.turnStartYaw + (var11 - this.turnStartYaw) * var23);
         var35 = (float)(Math.clamp(Math.toRadians(var34 - var11) + var12, -0.75, 0.75) * var25);
         var22 = (float)(var13 * var25);
      }

      double var36 = 1.0 - Math.clamp(Math.hypot(this.pointerVelX, this.pointerVelY) / (4.0 * var3), 0.0, 1.0);
      double var37 = this.state == PlayerPreviewAnimator.Stage.HELD ? 0.014 * Math.sin(this.elapsedTime * 53.0) * this.uprightness * var36 : 0.0;
      double var27 = !this.dazed
         ? 0.0
         : (
            this.state == PlayerPreviewAnimator.Stage.LYING
               ? 1.0
               : (this.state == PlayerPreviewAnimator.Stage.GETTING_UP ? 1.0 - stepSimulation(this.stateTime / 0.45) : 0.0)
         );
      double var29 = (var5 - (this.posY + release(this.angle) * var3)) / var3;
      PlayerPreviewAnimator.LimbPose var31 = new PlayerPreviewAnimator.LimbPose(
         (float)this.poseCurrent[0],
         (float)this.poseCurrent[1],
         (float)this.poseCurrent[2],
         (float)this.poseCurrent[3],
         (float)this.poseCurrent[4],
         (float)this.poseCurrent[5],
         (float)this.poseCurrent[6],
         (float)this.poseCurrent[7],
         (float)this.poseCurrent[8],
         (float)Math.clamp(this.poseCurrent[9], 0.0, 1.0),
         this.landingPuff
      );
      return new PlayerPreviewAnimator.PreviewFrame(
         this.state,
         this.posX - var9,
         0.0,
         new PlayerPreviewAnimator.Pose(var34, var35, var22, (float)this.stridePhase, (float)this.strideAmount, var14),
         new PlayerPreviewAnimator.ToyFrame(
            this.posX,
            this.posY,
            (float)(this.angle + var37),
            Math.max(0.0, var29),
            var31,
            (float)var27,
            this.impactStrength,
            this.velX / var3,
            this.velY / var3,
            (float)this.angularVelocity
         )
      );
   }

   private void stepSimulation(double var1, double var3, double var5, double var7, double var9, double var11, float var13) {
      double var14 = 0.004166666666666667;
      this.stateTime += var14;
      this.elapsedTime += var14;
      switch (this.state) {
         case HELD:
         case THROWN:
            this.integrate(var14, var1, var3, var5, var9, var11);
            break;
         case LYING:
            if (this.stateTime >= this.lieDuration) {
               this.recoverAngle = setPoseTargets(this.angle);
               this.recoverStartX = this.posX;
               this.recoverStartY = this.posY;
               this.setState(PlayerPreviewAnimator.Stage.GETTING_UP);
            }
            break;
         case GETTING_UP:
            this.updateGetUp(var1, var3, var5);
            break;
         case LANDING:
            this.updateLanding(var1, var3, var7);
            break;
         case WALKING:
            this.updateWalk(var14, var1, var3, var7);
            break;
         case STANDING:
            if (this.stateTime >= 0.48) {
               this.state = null;
               return;
            }
      }

      this.resolveCollisions(var14, var1);
   }

   private void integrate(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 30.0 * var3;
      double var15 = 0.36 * var3 * var3;
      double var17 = 0.0;
      double var19 = var13;
      double var21 = 0.0;
      if (this.state == PlayerPreviewAnimator.Stage.HELD) {
         double var23 = Math.cos(this.angle);
         double var25 = Math.sin(this.angle);
         double var27 = (this.grabLocalX * var23 - this.grabLocalY * var25) * var3;
         double var29 = (this.grabLocalX * var25 + this.grabLocalY * var23) * var3;
         double var31 = this.velX - this.angularVelocity * var29;
         double var33 = this.velY + this.angularVelocity * var27;
         double var35 = 26.389378290154262;
         double var37 = var35 * var35 * (var9 - this.posX - var27) + 1.5 * var35 * (this.pointerVelX - var31);
         double var39 = var35 * var35 * (var11 - this.posY - var29) + 1.5 * var35 * (this.pointerVelY - var33);
         double var41 = Math.hypot(var37, var39);
         double var43 = 60.0 * var13;
         if (var41 > var43) {
            var37 *= var43 / var41;
            var39 *= var43 / var41;
         }

         var17 += var37;
         var19 += var39;
         var21 += var27 * var39 - var29 * var37;
         double var45 = this.angularVelocity * Math.exp(-1.8 * var1) - this.angularVelocity;
         this.angularVelocity += var45;
         this.velX += var45 * var29;
         this.velY -= var45 * var27;
         var21 += this.uprightness * 0.8 * Math.sin(this.dangleTime * 0.5) * var15;
         if (var29 > 0.2 * var3) {
            var21 += this.uprightness * 6.0 * (var27 > 0.0 ? -1 : 1) * var15;
         }
      }

      this.velX += var17 * var1;
      this.velY += var19 * var1;
      this.angularVelocity += var21 / var15 * var1;
      this.velX = this.velX * Math.exp(-0.12 * var1);
      this.velY = this.velY * Math.exp(-0.12 * var1);
      this.angularVelocity = this.angularVelocity * Math.exp(-0.35 * var1);
      if (this.onGround) {
         this.velX = this.velX * Math.exp(-2.5 * var1);
         this.angularVelocity = this.angularVelocity * Math.exp(-4.0 * var1);
      }

      double var48 = Math.hypot(this.velX, this.velY);
      double var49 = 34.0 * var3;
      if (var48 > var49) {
         this.velX *= var49 / var48;
         this.velY *= var49 / var48;
      }

      this.angularVelocity = Math.clamp(this.angularVelocity, -22.0, 22.0);
      this.posX = this.posX + this.velX * var1;
      this.posY = this.posY + this.velY * var1;
      this.angle = setPoseTargets(this.angle + this.angularVelocity * var1);
      this.resolveCollisions(var3, var5, var7, var15);
      double var50 = (var5 - (this.posY + release(this.angle) * var3)) / var3;
      double var51 = this.state == PlayerPreviewAnimator.Stage.THROWN ? (this.onGround ? 0 : 1) : stepSimulation((var50 - 0.04) / 0.46);
      this.uprightness = this.uprightness + (var51 - this.uprightness) * -Math.expm1(-var1 * 10.0);
      if (this.state == PlayerPreviewAnimator.Stage.THROWN) {
         int var52 = this.onGround && Math.hypot(this.velX, this.velY) < 0.8 * var3 && Math.abs(this.angularVelocity) < 1.2 ? 1 : 0;
         this.restTime = var52 != 0 ? this.restTime + var1 : 0.0;
         if (this.restTime > 0.16 || this.stateTime > 8.0) {
            this.stepSimulation();
         }
      }
   }

   private void resolveCollisions(double var1, double var3, double var5, double var7) {
      this.onGround = (boolean)0;
      double var9 = Math.cos(this.angle);
      double var11 = Math.sin(this.angle);

      for (int var13 = 0; var13 < 4; var13++) {
         if (var13 == 0 || this.state != PlayerPreviewAnimator.Stage.HELD) {
            double var14 = var13 == 2 ? 1.0 : (var13 == 3 ? -1.0 : 0.0);
            double var16 = var13 == 0 ? -1.0 : (var13 == 1 ? 1.0 : 0.0);
            double var18 = 0.0;

            for (double[] var23 : bODY_POINTS) {
               var18 = Math.max(var18, this.cornerPenetration(var13, var23, var9, var11, var1, var3, var5));
            }

            if (!(var18 <= 0.0)) {
               double var45 = 0.0;
               double var47 = 0.0;
               int var24 = 0;

               for (double[] var28 : bODY_POINTS) {
                  if (!(this.cornerPenetration(var13, var28, var9, var11, var1, var3, var5) < var18 - 0.015 * var1)) {
                     var45 += (var28[0] * var9 - var28[1] * var11) * var1;
                     var47 += (var28[0] * var11 + var28[1] * var9) * var1;
                     var24++;
                  }
               }

               var45 /= var24;
               var47 /= var24;
               if (var13 == 0) {
                  this.posY -= var18;
                  this.onGround = (boolean)1;
               }

               double var49 = this.velX - this.angularVelocity * var47;
               double var51 = this.velY + this.angularVelocity * var45;
               double var29 = var49 * var14 + var51 * var16;
               if (!(var29 >= 0.0)) {
                  double var31 = var13 != 0 ? 0.45 : (-var29 > 3.0 * var1 ? 0.32 : 0.0);
                  double var33 = var45 * var16 - var47 * var14;
                  double var35 = -(1.0 + var31) * var29 / (1.0 + var33 * var33 / var7);
                  this.velX += var35 * var14;
                  this.velY += var35 * var16;
                  this.angularVelocity += var33 * var35 / var7;
                  double var37 = -var16;
                  double var39 = var14;
                  var49 = this.velX - this.angularVelocity * var47;
                  var51 = this.velY + this.angularVelocity * var45;
                  double var41 = var45 * var39 - var47 * var37;
                  double var43 = Math.clamp(-(var49 * var37 + var51 * var39) / (1.0 + var41 * var41 / var7), -0.6 * var35, 0.6 * var35);
                  this.velX += var43 * var37;
                  this.velY += var43 * var39;
                  this.angularVelocity += var41 * var43 / var7;
                  this.impactStrength = (float)Math.max(this.impactStrength, -var29 / var1);
                  this.peakImpact = Math.max(this.peakImpact, -var29 / var1);
               }
            }
         }
      }
   }

   private double cornerPenetration(int var1, double[] var2, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.posX + (var2[0] * var3 - var2[1] * var5) * var7;
      double var15 = this.posY + (var2[0] * var5 + var2[1] * var3) * var7;

      return switch (var1) {
         case 0 -> var15 - var9;
         case 1 -> -var15;
         case 2 -> -var13;
         default -> var13 - var11;
      };
   }

   private void stepSimulation() {
      this.angle = setPoseTargets(this.angle);
      this.velX = this.velY = this.angularVelocity = 0.0;
      this.landedUpright = (boolean)(Math.abs(this.angle) < 0.5 ? 1 : 0);
      this.dazed = (boolean)(!this.landedUpright && this.peakImpact > 9.0 ? 1 : 0);
      this.recoverAngle = this.angle;
      this.recoverStartX = this.posX;
      this.recoverStartY = this.posY;
      if (this.landedUpright) {
         this.setState(PlayerPreviewAnimator.Stage.LANDING);
      } else {
         this.lieDuration = 0.5 + Math.min(0.5, this.peakImpact / 30.0);
         this.setState(PlayerPreviewAnimator.Stage.LYING);
      }
   }

   private void updateGetUp(double var1, double var3, double var5) {
      int var7 = Math.abs(this.recoverAngle) > 2.2 ? 1 : 0;
      double var8 = Math.min(1.0, this.stateTime / (var7 != 0 ? 0.7 : 0.62));
      double var10 = var7 != 0 ? stepSimulation(var8) : stepSimulation((var8 - 0.14) / 0.86);
      this.angle = this.recoverAngle * (1.0 - var10);
      double var12 = (this.recoverStartY - (var3 - release(this.recoverAngle) * var1)) * (1.0 - var10);
      double var14 = var3 - release(this.angle) * var1 + var12;
      if (var7 != 0) {
         this.posX = this.recoverStartX;
         this.posY = var14 - Math.sin(Math.PI * var10) * 0.9 * var1;
      } else {
         this.posX = this.recoverStartX - 1.15 * var1 * (Math.sin(this.recoverAngle) - Math.sin(this.angle));
         this.posY = var14;
      }

      this.posX = clampX(this.posX, this.angle, var1, var5);
      if (var8 >= 1.0) {
         this.angle = this.recoverAngle = 0.0;
         this.posY = var3 - 1.15 * var1;
         this.recoverStartX = this.posX;
         this.setState(PlayerPreviewAnimator.Stage.LANDING);
      }
   }

   private void updateLanding(double var1, double var3, double var5) {
      double var7 = this.stateTime;
      double var9 = this.recoverAngle * (1.0 - stepSimulation(var7 / 0.22)) + (this.landedUpright ? 0.07 : 0.1) * Math.sin(var7 * 13.0) * Math.exp(-var7 * 5.5);
      double var11 = this.recoverStartX - 1.15 * var1 * Math.sin(this.recoverAngle);
      this.angle = var9;
      this.posX = var11 + 1.15 * var1 * Math.sin(var9);
      this.posY = var3 - release(var9) * var1;
      this.landingPuff = (boolean)(this.landedUpright && this.peakImpact > 7.0 && var7 < 0.16 ? 1 : 0);
      if (!(var7 < 0.55)) {
         this.angle = 0.0;
         this.posX = var11;
         this.posY = var3 - 1.15 * var1;
         this.landingPuff = (boolean)0;
         this.turnStartYaw = this.yaw;
         this.walkSpeed = 0.0;
         this.walkDirection = Math.signum(var5 - this.posX);
         this.setState(Math.abs(var5 - this.posX) > 0.12 * var1 ? PlayerPreviewAnimator.Stage.WALKING : PlayerPreviewAnimator.Stage.STANDING);
      }
   }

   private void updateWalk(double var1, double var3, double var5, double var7) {
      double var9 = var7 - this.posX;
      double var11 = Math.abs(var9);
      double var13 = Math.signum(var9);
      if (var13 != 0.0 && var13 != this.walkDirection) {
         this.walkDirection = var13;
         this.turnStartYaw = this.yaw;
         this.turnTime = 0.0;
      }

      double var15 = 16.0 * var3;
      if (var11 <= this.walkSpeed * this.walkSpeed / (2.0 * var15)) {
         this.walkSpeed = Math.max(0.5 * var3, this.walkSpeed - var15 * var1);
      } else {
         this.walkSpeed = Math.min(5.6 * var3, this.walkSpeed + 20.0 * var3 * var1);
      }

      double var17 = Math.min(var11, this.walkSpeed * var1);
      this.posX += var13 * var17;
      this.posY = var5 - 1.15 * var3;
      this.angle = 0.0;
      this.velX = var13 * this.walkSpeed;
      this.stridePhase += var17 / var3 * 4.0;
      this.strideAmount = Math.min(1.0, this.walkSpeed / var3 / 5.0);
      if (!(var11 - var17 > 1.0E-6)) {
         this.posX = var7;
         this.velX = this.walkSpeed = this.strideAmount = 0.0;
         this.turnStartYaw = this.yaw;
         this.setState(PlayerPreviewAnimator.Stage.STANDING);
      }
   }

   private void resolveCollisions(double var1, double var3) {
      double var5 = this.stateTime;
      double var7 = Math.hypot(this.velX, this.velY) / var3;
      double var9 = this.yaw;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      Arrays.fill(this.poseTarget, 0, 9, 0.0);
      switch (this.state) {
         case HELD:
            double var29 = Math.clamp(Math.hypot(this.pointerVelX, this.pointerVelY) / var3 / 5.0, 0.0, 1.0);
            this.dangleTime = this.dangleTime + var1 * (9.0 + 9.0 * var29) * (0.4 + 0.6 * this.uprightness);
            var9 = -10.0 - Math.clamp(this.velX / var3 * 4.0, -28.0, 28.0);
            var11 = this.uprightness * (0.42 * Math.sin(this.elapsedTime * 2.2) + 0.14 * Math.sin(this.elapsedTime * 5.7));
            var13 = 0.2 + 0.35 * this.uprightness;
            double var31 = -0.9 - 1.65 * this.uprightness;
            double var33 = 0.25 + 0.35 * this.uprightness;
            double var35 = 0.15 + 0.65 * this.uprightness;
            this.setPoseTargets(
               var31 + var33 * Math.sin(this.dangleTime * 1.3),
               0.25 + 0.35 * this.uprightness + 0.25 * Math.sin(this.dangleTime * 0.9 + 1.0),
               var31 + var33 * Math.sin(this.dangleTime * 1.3 + 2.7),
               -(0.25 + 0.35 * this.uprightness + 0.25 * Math.sin(this.dangleTime * 0.9 + 2.4)),
               var35 * Math.sin(this.dangleTime),
               0.06 + 0.1 * this.uprightness,
               -var35 * Math.sin(this.dangleTime + 0.3),
               -(0.06 + 0.1 * this.uprightness)
            );
            this.poseTarget[8] = Math.clamp(-setPoseTargets(this.angle) * 0.45, -0.6, 0.6) * this.uprightness;
            var15 = 1.0;
            break;
         case THROWN:
            this.dangleTime = this.dangleTime + var1 * (this.onGround ? 8 : 18);
            var9 = -6.0 - Math.clamp(this.velX / var3 * 2.5, -35.0, 35.0);
            var11 = 0.2 * Math.sin(this.elapsedTime * 3.0);
            var13 = 0.45 * this.uprightness;
            double var28 = 0.4 + 0.6 * this.uprightness;
            this.setPoseTargets(
               -1.5 + 1.25 * var28 * Math.sin(this.dangleTime),
               0.55 + 0.4 * Math.cos(this.dangleTime),
               -1.5 + 1.25 * var28 * Math.sin(this.dangleTime + Math.PI),
               -(0.55 + 0.4 * Math.cos(this.dangleTime + Math.PI)),
               0.9 * var28 * Math.sin(this.dangleTime * 0.85),
               0.12,
               -0.9 * var28 * Math.sin(this.dangleTime * 0.85),
               -0.12
            );
            var15 = 1.0;
            break;
         case LYING:
            var9 = 0.0;
            var11 = this.dazed ? 0.4 * Math.sin(this.elapsedTime * 3.2) : 0.12;
            var13 = -0.1;
            double var27 = 0.25 * Math.sin(var5 * 30.0) * Math.exp(-var5 * 7.0);
            double var19 = Math.clamp(Math.sin(this.angle) * 1.4, -1.0, 1.0);
            double var21 = 1.0 - Math.abs(var19);
            double var23 = Math.max(0.0, var19);
            double var25 = Math.max(0.0, -var19);
            this.setPoseTargets(
               0.15 + var27,
               0.65 * var21 - 1.25 * var23 + 0.1 * var25,
               0.15 - var27,
               -0.65 * var21 + 1.25 * var25 - 0.1 * var23,
               var27,
               0.25 * var21 - 0.3 * var23 + 0.08 * var25,
               -var27,
               -0.25 * var21 + 0.3 * var25 - 0.08 * var23
            );
            this.poseTarget[8] = this.dazed ? 0.2 * Math.sin(this.elapsedTime * 3.2 + 1.0) : 0.0;
            var15 = 1.0;
            break;
         case GETTING_UP:
            var9 = 0.0;
            var13 = 0.1;
            this.setPoseTargets(-0.5, 0.3, -0.5, -0.3, -0.2, 0.12, 0.2, -0.12);
            var15 = 1.0 - stepSimulation(this.stateTime / (Math.abs(this.recoverAngle) > 2.2 ? 0.7 : 0.62));
            break;
         case LANDING:
            var9 = -12.0;
            var11 = 0.55 * Math.sin(var5 * 17.0) * Math.exp(-var5 * 4.5);
            var13 = 0.1 * (1.0 - stepSimulation(var5 / 0.55));
            double var17 = 1.2 + 0.35 * Math.sin(var5 * 11.0) * Math.exp(-var5 * 2.5);
            this.setPoseTargets(-0.1, var17, -0.1, -var17, 0.0, 0.12, 0.0, -0.12);
            var15 = 1.0 - stepSimulation((var5 - 0.2) / 0.3);
            break;
         case WALKING:
            this.turnTime += var1;
            this.yaw = this.turnStartYaw + ((this.walkDirection < 0.0 ? -90 : 90) - this.turnStartYaw) * stepSimulation(this.turnTime / 0.22);
            var9 = this.yaw;
      }

      this.poseTarget[9] = var15;
      if (this.state != PlayerPreviewAnimator.Stage.WALKING && this.state != PlayerPreviewAnimator.Stage.STANDING) {
         this.yaw = this.yaw + (var9 - this.yaw) * -Math.expm1(-var1 * 9.0);
      }

      double var30 = -Math.expm1(-var1 * 14.0);
      this.headRoll = this.headRoll + (var11 - this.headRoll) * var30;
      this.headPitch = this.headPitch + (var13 - this.headPitch) * var30;
      double var32 = -Math.expm1(-var1 * 22.0);

      for (int var34 = 0; var34 < this.poseCurrent.length; var34++) {
         this.poseCurrent[var34] = this.poseCurrent[var34] + (this.poseTarget[var34] - this.poseCurrent[var34]) * var32;
      }

      if (this.state == PlayerPreviewAnimator.Stage.HELD || this.state == PlayerPreviewAnimator.Stage.THROWN) {
         this.strideAmount = this.strideAmount + (Math.min(1.0, var7 / 6.0) - this.strideAmount) * var30;
         this.stridePhase += var7 * var1 * 2.0;
      } else if (this.state != PlayerPreviewAnimator.Stage.WALKING) {
         this.strideAmount = this.strideAmount + (0.0 - this.strideAmount) * var30;
      }
   }

   private void setPoseTargets(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.poseTarget[0] = var1;
      this.poseTarget[1] = var3;
      this.poseTarget[2] = var5;
      this.poseTarget[3] = var7;
      this.poseTarget[4] = var9;
      this.poseTarget[5] = var11;
      this.poseTarget[6] = var13;
      this.poseTarget[7] = var15;
   }

   private void setState(PlayerPreviewAnimator.Stage var1) {
      this.state = var1;
      this.stateTime = this.turnTime = 0.0;
      this.restTime = 0.0;
   }

   static double release(double var0) {
      double var2 = Math.cos(var0);
      double var4 = Math.sin(var0);
      double var6 = -Double.MAX_VALUE;

      for (double[] var11 : bODY_POINTS) {
         var6 = Math.max(var6, var11[0] * var4 + var11[1] * var2);
      }

      return var6;
   }

   private static double clampX(double var0, double var2, double var4, double var6) {
      double var8 = Math.cos(var2);
      double var10 = Math.sin(var2);
      double var12 = Double.MAX_VALUE;
      double var14 = -Double.MAX_VALUE;

      for (double[] var19 : bODY_POINTS) {
         double var20 = (var19[0] * var8 - var19[1] * var10) * var4;
         var12 = Math.min(var12, var20);
         var14 = Math.max(var14, var20);
      }

      return Math.clamp(var0, -var12, Math.max(-var12, var6 - var14));
   }

   double[][] integrate(double var1) {
      double var3 = Math.cos(this.angle);
      double var5 = Math.sin(this.angle);
      double[][] var7 = new double[bODY_POINTS.length][];

      for (int var8 = 0; var8 < bODY_POINTS.length; var8++) {
         var7[var8] = new double[]{
            this.posX + (bODY_POINTS[var8][0] * var3 - bODY_POINTS[var8][1] * var5) * var1,
            this.posY + (bODY_POINTS[var8][0] * var5 + bODY_POINTS[var8][1] * var3) * var1
         };
      }

      return var7;
   }

   private static double setPoseTargets(double var0) {
      return Math.IEEEremainder(var0, Math.PI * 2);
   }

   private static double stepSimulation(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }
}
