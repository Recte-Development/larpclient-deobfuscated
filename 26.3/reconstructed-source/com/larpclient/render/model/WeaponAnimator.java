package com.larpclient.render.model;

public final class WeaponAnimator {
   private static final double mIN_SPEED = 0.5;
   private Object currentItem;
   private CustomItemModel profile;
   private WeaponAnimator.Phase phase = WeaponAnimator.Phase.IDLE;
   private double phaseStart;
   private double lastTime = Double.NaN;
   private double swingDuration;
   private final WeaponAnimator.Frame frame = new WeaponAnimator.Frame();

   public WeaponAnimator.Phase update() {
      return this.phase;
   }

   public void reset() {
      this.currentItem = null;
      this.profile = null;
      this.lastTime = Double.NaN;
      this.phase = WeaponAnimator.Phase.IDLE;
   }

   public void beginHold() {
      if (this.currentItem != null) {
         this.phase = WeaponAnimator.Phase.ATTACK;
      }
   }

   public boolean startSwing(double var1, double var3) {
      if (this.currentItem != null && this.phase == WeaponAnimator.Phase.IDLE) {
         this.phase = WeaponAnimator.Phase.INSPECT;
         this.phaseStart = var1;
         this.swingDuration = this.profile.length / (this.profile.thickness * Math.clamp(Double.isFinite(var3) ? var3 : 2.0, 0.5, 2.0));
         return true;
      } else {
         return false;
      }
   }

   public WeaponAnimator.Frame update(Object var1, CustomItemModel var2, double var3, float var5, boolean var6) {
      int var7 = this.frame.stage == 2 ? 1 : 0;
      this.frame.reset();
      if (var1 == null) {
         this.reset();
         return this.frame;
      }

      if (!Double.isFinite(var3)) {
         var3 = 0.0;
      }

      if (var1 != this.currentItem || var2 != this.profile) {
         var7 = 0;
         this.currentItem = var1;
         this.profile = var2;
         this.phase = WeaponAnimator.Phase.EQUIP;
         this.phaseStart = var3;
      } else if (Double.isFinite(this.lastTime) && (var3 < this.lastTime || var3 - this.lastTime > 0.500001)) {
         var7 = 0;
         this.phase = WeaponAnimator.Phase.IDLE;
      }

      this.lastTime = var3;
      var5 = Float.isFinite(var5) ? Math.clamp(var5, 0.0F, 1.0F) : 0.0F;
      this.frame.interruptedAttack = (boolean)(var7 == 0 || this.phase != WeaponAnimator.Phase.ATTACK && !(var5 > 0.0F) ? 0 : 1);
      if (var5 > 0.0F) {
         this.phase = WeaponAnimator.Phase.ATTACK;
      } else if (this.phase == WeaponAnimator.Phase.ATTACK) {
         this.phase = WeaponAnimator.Phase.IDLE;
      }

      if (this.phase == WeaponAnimator.Phase.EQUIP) {
         float var8 = (float)Math.clamp((var3 - this.phaseStart) / this.profile.scale, 0.0, 1.0);
         this.frame.stage = 1;
         this.frame.progress = var8;
         if (var8 >= 1.0F) {
            this.phase = WeaponAnimator.Phase.IDLE;
         }
      } else if (this.phase == WeaponAnimator.Phase.INSPECT) {
         float var10 = (float)Math.clamp((var3 - this.phaseStart) / this.swingDuration, 0.0, 1.0);
         this.frame.stage = 2;
         this.frame.progress = var10;
         if (var10 >= 1.0F) {
            this.phase = WeaponAnimator.Phase.IDLE;
            this.frame.reset();
         }
      } else if (this.phase == WeaponAnimator.Phase.ATTACK) {
         this.frame.stage = 3;
         this.frame.progress = var5;
      }

      if (var6 && this.phase == WeaponAnimator.Phase.IDLE) {
         this.frame.bob = this.frame.bob + 0.003F * (float)Math.sin(var3 * 1.8);
      }

      return this.frame;
   }

   public static final class Frame {
      public float bob;
      public float progress;
      public int stage;
      public boolean interruptedAttack;

      private void reset() {
         this.bob = this.progress = 0.0F;
         this.stage = 0;
         this.interruptedAttack = (boolean)0;
      }
   }

   public enum Phase {
      EQUIP,
      IDLE,
      INSPECT,
      ATTACK;
   }
}
