package com.larpclient.gui.misc;

import com.larpclient.gui.screen.HomeLayout;
import com.larpclient.gui.screen.MainMenuLayout;
import com.larpclient.render.model.StickFigureSimulation;

public final class PlayerPreviewAnimator {
   public static final double dROP_DELAY = 0.12;
   public static final double sETTLE_DURATION = 0.48;
   private static final double dROP_SPEED = 3.8;
   private static final double bOUNCE_DURATION = 0.14;
   private static final double sETTLE_BLEND_DURATION = 0.22;
   private final PlayerPreviewAnimator.Builder builder;
   private long lastFrameNanos = -1L;
   private boolean wasActive;
   private boolean dropStarted;
   private boolean interactive;
   private double elapsed;
   private double idleTime;
   private double dropDistance;
   private double lookX;
   private double lookY;
   private StickFigureSimulation toy;
   private PlayerPreviewAnimator.PreviewLayout layout;
   private PlayerPreviewAnimator.Stage stage = PlayerPreviewAnimator.Stage.HIDDEN;
   private float bodyYaw = -16.0F;
   static final double mODEL_HEIGHT = 2.101;
   static final double mODEL_EXTRA_HEIGHT = 0.15963829787234074;

   public PlayerPreviewAnimator() {
      this(new PlayerPreviewAnimator.Builder());
   }

   private PlayerPreviewAnimator(PlayerPreviewAnimator.Builder var1) {
      this.builder = var1;
   }

   public static PlayerPreviewAnimator.PreviewLayout computeLayout(MainMenuLayout var0) {
      return computeLayoutWithExtra(var0, 0.0);
   }

   public static PlayerPreviewAnimator.PreviewLayout computeLayoutWithExtra(MainMenuLayout var0, double var1) {
      int var3 = var0.width();
      int var4 = var0.height();
      int var5 = var0.navigation(0).left() + var0.menuWidth() + 18;
      int var6 = Math.max(0, var3 - 16 - var5);
      if (var6 >= 64 && var4 >= 180) {
         int var7 = Math.min(240, var6);
         int var8 = (int)Math.min(360.0, Math.min(var4 * 0.59, var7 * 1.5));
         int var9 = Math.clamp((int)(var3 * 0.738 - var7 / 2.0), var5, var3 - 16 - var7);
         int var10 = Math.min(var4 - 18, (var4 + var8) / 2);
         int var11 = Math.max(58, var10 - var8);
         var8 = var10 - var11;
         float var12 = 0.94F * var8 / 2.125F;
         if (var1 > 0.15963829787234074) {
            int var13 = (int)Math.ceil((2.101 + var1) * var12 - var8);
            int var14 = Math.min(var13, Math.max(0, var4 - 18 - var10));
            var10 += var14;
            var11 -= Math.min(var13 - var14, Math.max(0, var11 - 58));
            var8 = var10 - var11;
         }

         int var16 = Math.round(var10 - 0.101F * var12);
         return new PlayerPreviewAnimator.PreviewLayout(
            new HomeLayout.Rect(var9, var11, var7, var8), new HomeLayout.Rect(var9, var11 - 24, var7, 18), var12, var16, true
         );
      } else {
         return new PlayerPreviewAnimator.PreviewLayout(new HomeLayout.Rect(var3, 0, 0, 0), new HomeLayout.Rect(var3, 0, 0, 0), 0.0F, 0, false);
      }
   }

   public static int clipTop(PlayerPreviewAnimator.PreviewLayout var0, double var1) {
      HomeLayout.Rect var3 = var0.model();
      return Math.clamp(var3.top() + var3.height() - (int)Math.ceil((2.101 + var1) * var0.scale()), 0, var3.top());
   }

   public static double dropStartDistance(PlayerPreviewAnimator.PreviewLayout var0, int var1) {
      return var1 + var0.scale() + 8.0F - var0.centerX();
   }

   public static double dropDuration(double var0) {
      return var0 / 3.8 + 0.18;
   }

   public PlayerPreviewAnimator.PreviewFrame update(
      long var1, boolean var3, boolean var4, boolean var5, boolean var6, PlayerPreviewAnimator.PreviewLayout var7, int var8, int var9, int var10
   ) {
      PlayerPreviewAnimator.PreviewFrame var11 = this.computeFrame(var1, var3, var4, var5, var6, var7, var8, var9, var10);
      this.stage = var11.stage();
      this.layout = var7;
      this.interactive = var4;
      this.bodyYaw = var11.pose().bodyYaw();
      return var11;
   }

   private PlayerPreviewAnimator.PreviewFrame computeFrame(
      long var1, boolean var3, boolean var4, boolean var5, boolean var6, PlayerPreviewAnimator.PreviewLayout var7, int var8, int var9, int var10
   ) {
      double var11 = this.lastFrameNanos >= 0L && var1 >= this.lastFrameNanos && var1 - this.lastFrameNanos <= 500000000L
         ? (var1 - this.lastFrameNanos) / 1.0E9
         : 0.0;
      this.lastFrameNanos = var1;
      if (!var3 || !this.wasActive || !var6) {
         var11 = 0.0;
      }

      this.wasActive = (boolean)(var3 && var6 ? 1 : 0);
      if (!var6 || !var7.visible()) {
         this.toy = null;
      }

      if (var6 && var7.visible()) {
         if (!var4) {
            this.toy = null;
         }

         if (!var4 || !var5) {
            this.builder.dropFinished = (boolean)1;
         }

         if (!this.dropStarted && var3) {
            this.dropStarted = (boolean)1;
            this.dropDistance = dropStartDistance(var7, var8) / var7.scale();
            var11 = 0.0;
         }

         if (var4) {
            this.idleTime += var11;
            int var13 = var9 >= 0 && var10 >= 0 ? 1 : 0;
            double var14 = var13 != 0 ? Math.clamp((var9 - var7.centerX()) / Math.max(80.0, var7.model().height() * 0.8), -1.0, 1.0) : 0.0;
            double var16 = var13 != 0
               ? Math.clamp((var10 - var7.model().top() - var7.model().height() * 0.15) / Math.max(60.0, var7.model().height() * 0.7), -1.0, 1.0)
               : 0.0;
            double var18 = -Math.expm1(-var11 * 9.0);
            this.lookX = this.lookX + (var14 - this.lookX) * var18;
            this.lookY = this.lookY + (var16 - this.lookY) * var18;
         } else {
            this.idleTime = this.lookX = this.lookY = 0.0;
         }

         float var30 = (float)(-16.0 + this.lookX * 4.0);
         float var31 = var4 ? (float)(-this.lookX * 0.3) : 0.0F;
         float var15 = (float)(this.lookY * 0.2);
         if (this.builder.dropFinished && this.toy != null) {
            PlayerPreviewAnimator.PreviewFrame var32 = this.toy
               .update(var11, var7.scale(), var7.floorY(), var8, var7.centerX(), var30, var31, var15, (float)(this.idleTime * 20.0));
            if (var32 != null) {
               return var32;
            }

            this.toy = null;
         }

         if (this.builder.dropFinished) {
            return new PlayerPreviewAnimator.PreviewFrame(
               PlayerPreviewAnimator.Stage.IDLE, 0.0, 1.0, new PlayerPreviewAnimator.Pose(var30, var31, var15, 0.0F, 0.0F, (float)(this.idleTime * 20.0))
            );
         } else if (!this.dropStarted) {
            return new PlayerPreviewAnimator.PreviewFrame(
               PlayerPreviewAnimator.Stage.HIDDEN, 0.0, 0.0, new PlayerPreviewAnimator.Pose(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
            );
         } else {
            double var33 = 0.12 + dropDuration(this.dropDistance) + 0.48;
            this.elapsed = Math.min(this.elapsed + var11, var33);
            double var34 = this.elapsed - 0.12;
            if (var34 < 0.0) {
               return new PlayerPreviewAnimator.PreviewFrame(
                  PlayerPreviewAnimator.Stage.HIDDEN, 0.0, 0.0, new PlayerPreviewAnimator.Pose(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float)(this.idleTime * 20.0))
               );
            } else {
               PlayerPreviewAnimator.DropMotion var20 = dropMotion(var34, this.dropDistance);
               float var21 = (float)(var20.distance * 4.0);
               if (var34 < dropDuration(this.dropDistance)) {
                  return new PlayerPreviewAnimator.PreviewFrame(
                     PlayerPreviewAnimator.Stage.DROPPING,
                     dropStartDistance(var7, var8) * (1.0 - var20.distance / this.dropDistance),
                     0.0,
                     new PlayerPreviewAnimator.Pose(-90.0F, 0.0F, 0.0F, var21, (float)(var20.speed / 5.0), (float)(this.idleTime * 20.0))
                  );
               } else if (this.elapsed >= var33) {
                  this.builder.dropFinished = (boolean)1;
                  return new PlayerPreviewAnimator.PreviewFrame(
                     PlayerPreviewAnimator.Stage.IDLE, 0.0, 1.0, new PlayerPreviewAnimator.Pose(var30, var31, var15, 0.0F, 0.0F, (float)(this.idleTime * 20.0))
                  );
               } else {
                  double var22 = var34 - dropDuration(this.dropDistance);
                  double var24 = smootherStep(var22 / 0.48);
                  double var26 = smootherStep(var22 / 0.33599999999999997);
                  float var28 = (float)(-90.0 + (var30 + 90.0F) * var24);
                  float var29 = (float)(Math.clamp(Math.toRadians(var28 - var30) + var31, -0.75, 0.75) * var26);
                  return new PlayerPreviewAnimator.PreviewFrame(
                     PlayerPreviewAnimator.Stage.SETTLING,
                     0.0,
                     0.0,
                     new PlayerPreviewAnimator.Pose(var28, var29, (float)(var15 * var26), var21, 0.0F, (float)(this.idleTime * 20.0))
                  );
               }
            }
         }
      } else {
         return new PlayerPreviewAnimator.PreviewFrame(
            PlayerPreviewAnimator.Stage.HIDDEN, 0.0, 0.0, new PlayerPreviewAnimator.Pose(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
         );
      }
   }

   private static PlayerPreviewAnimator.DropMotion dropMotion(double var0, double var2) {
      double var4 = Math.max(0.0, var2 / 3.8 - 0.18);
      if (var0 >= dropDuration(var2)) {
         return new PlayerPreviewAnimator.DropMotion(var2, 0.0);
      }

      if (var0 < 0.14) {
         double var8 = Math.max(0.0, var0);
         return new PlayerPreviewAnimator.DropMotion(
            3.8 * (var8 / 2.0 - 0.02228169203286535 * Math.sin(Math.PI * var8 / 0.14)), 3.8 * (0.5 - 0.5 * Math.cos(Math.PI * var8 / 0.14))
         );
      }

      if (var0 < 0.14 + var4) {
         return new PlayerPreviewAnimator.DropMotion(3.8 * (0.07 + var0 - 0.14), 3.8);
      }

      double var6 = var0 - 0.14 - var4;
      return new PlayerPreviewAnimator.DropMotion(
         3.8 * (0.07 + var4 + var6 / 2.0 + 0.03501408748021698 * Math.sin(Math.PI * var6 / 0.22)), 3.8 * (0.5 + 0.5 * Math.cos(Math.PI * var6 / 0.22))
      );
   }

   private static double smootherStep(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   public boolean canGrab(double var1, double var3) {
      if (this.layout == null || !this.layout.visible() || !this.interactive || !this.builder.dropFinished) {
         return false;
      } else if (this.toy == null) {
         return this.stage != PlayerPreviewAnimator.Stage.IDLE
            ? false
            : StickFigureSimulation.containsPoint(
               (var1 - this.layout.centerX()) / this.layout.scale(), (var3 - this.layout.floorY()) / this.layout.scale() + 1.15, false
            );
      } else {
         return this.toy.isHeld() || this.toy.hitTest(var1, var3, this.layout.scale());
      }
   }

   public boolean startGrab(double var1, double var3) {
      if (this.canGrab(var1, var3) && (this.toy == null || !this.toy.isHeld())) {
         if (this.toy == null) {
            this.toy = new StickFigureSimulation(this.layout.centerX(), this.layout.floorY() - 1.15 * this.layout.scale(), this.bodyYaw);
         }

         this.toy.grab(var1, var3, this.layout.scale());
         return true;
      } else {
         return false;
      }
   }

   public void computeLayout(double var1, double var3) {
      if (this.toy != null) {
         this.toy.dragTo(var1, var3);
      }
   }

   public boolean isGrabbing() {
      return this.toy != null && this.toy.isHeld();
   }

   public boolean resetToy() {
      return this.toy != null && this.toy.release();
   }

   public double[][] toyPoints() {
      return this.toy != null && this.layout != null ? this.toy.integrate(this.layout.scale()) : null;
   }

   public void resetToy(boolean var1) {
      if (var1) {
         this.builder.dropFinished = (boolean)1;
      }

      this.toy = null;
      this.resetClock();
   }

   public void dropStartDistance() {
      this.builder.dropFinished = this.dropStarted = (boolean)0;
      this.elapsed = 0.0;
      this.toy = null;
      this.resetClock();
   }

   public void resetClock() {
      this.lastFrameNanos = -1L;
      this.wasActive = (boolean)0;
   }

   public static final class Builder {
      private boolean dropFinished;

      public PlayerPreviewAnimator build() {
         return new PlayerPreviewAnimator(this);
      }
   }

   private record DropMotion(double distance, double speed) {
   }

   public record LimbPose(
      float rightArmX,
      float rightArmZ,
      float leftArmX,
      float leftArmZ,
      float rightLegX,
      float rightLegZ,
      float leftLegX,
      float leftLegZ,
      float headRoll,
      float weight,
      boolean crouch
   ) {
   }

   public record Pose(float bodyYaw, float headYaw, float headPitch, float walkPosition, float walkSpeed, float ageInTicks) {
   }

   public record PreviewFrame(
      PlayerPreviewAnimator.Stage stage, double xOffset, double nameOpacity, PlayerPreviewAnimator.Pose pose, PlayerPreviewAnimator.ToyFrame toy
   ) {
      public PreviewFrame(PlayerPreviewAnimator.Stage var1, double var2, double var4, PlayerPreviewAnimator.Pose var6) {
         this(var1, var2, var4, var6, null);
      }
   }

   public record PreviewLayout(HomeLayout.Rect model, HomeLayout.Rect name, float scale, int floorY, boolean visible) {
      public float centerX() {
         return this.model.left() + this.model.width() / 2.0F;
      }

      public int renderWidth() {
         return Math.max(this.model.width(), (int)Math.ceil(this.scale * 2.3));
      }
   }

   public enum Stage {
      HIDDEN,
      DROPPING,
      SETTLING,
      IDLE,
      HELD,
      THROWN,
      LYING,
      GETTING_UP,
      LANDING,
      WALKING,
      STANDING;
   }

   public record ToyBounds(int left, int top, int side, float shiftX, float shiftY) {
      public static PlayerPreviewAnimator.ToyBounds fromToy(PlayerPreviewAnimator.ToyFrame var0, float var1, double var2) {
         int var4 = (int)Math.ceil(2.0 * var2 * var1) + 2;
         int var5 = (int)Math.floor(var0.x2() - var4 / 2.0);
         int var6 = (int)Math.floor(var0.y2() - var4 / 2.0);
         float var7 = -1.251F;
         return new PlayerPreviewAnimator.ToyBounds(
            var5, var6, var4, (float)((var0.x2() - var5 - var4 / 2.0) / var1), (float)((var0.y2() - var6 - var4) / var1) - var7
         );
      }
   }

   public record ToyFrame(
      double x, double y, float roll, double lift, PlayerPreviewAnimator.LimbPose limbs, float dizzy, float impact, double vx, double vy, float spin
   ) {
      private final double x2;
      private final double y2;
      private final double velX;
      private final double velY;

      public ToyFrame(
         double x, double y, float roll, double lift, PlayerPreviewAnimator.LimbPose limbs, float dizzy, float impact, double vx, double vy, float spin
      ) {
         this.x2 = x;
         this.y2 = y;
         this.roll = roll;
         this.lift = lift;
         this.limbs = limbs;
         this.dizzy = dizzy;
         this.impact = impact;
         this.velX = vx;
         this.velY = vy;
         this.spin = spin;
      }
   }
}
