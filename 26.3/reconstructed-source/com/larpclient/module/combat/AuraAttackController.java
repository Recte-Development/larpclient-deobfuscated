package com.larpclient.module.combat;

import com.larpclient.module.misc.CombatUtil;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.TpsTracker;
import com.larpclient.util.entity.CombatState;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

public final class AuraAttackController {
   private final Module module;
   private final HotbarSwapSession shieldSwapper;
   private int attackCount;
   private boolean sprintReportDeferred;
   private long nextAttackNanos;
   private long sprintResetUntilMillis;
   private long cooldownStartNanos;
   private long cooldownDelayNanos;
   private boolean freshTarget = (boolean)1;

   public AuraAttackController(Module var1) {
      this.module = var1;
      this.shieldSwapper = new HotbarSwapSession(var1);
   }

   public void scheduleNextAttack(Minecraft var1) {
      this.shieldSwapper.tick(var1, true);
   }

   public void restoreSlot(Minecraft var1) {
      if (this.shieldSwapper.isSwapped()) {
         this.shieldSwapper.restore(var1, true);
      }
   }

   public void clearSprintReset(LocalPlayer var1) {
      this.sprintResetUntilMillis = 0L;
      AuraModule.sprintResetActive = (boolean)0;
   }

   public boolean isSprintResetActive(LocalPlayer var1) {
      if (!AuraModule.sprintResetActive) {
         return false;
      }

      if (var1 != null && !var1.onGround()) {
         if (System.currentTimeMillis() <= this.sprintResetUntilMillis) {
            return true;
         }

         this.clearSprintReset(var1);
         return false;
      } else {
         this.clearSprintReset(var1);
         return false;
      }
   }

   public boolean isLocalSprintResetActive() {
      return this.isSprintResetActive(Minecraft.getInstance().player);
   }

   public boolean tryAttack(LocalPlayer var1, AuraAttackController.AttackRequest var2) {
      LivingEntity var3 = var2.target();
      if (var1 != null && var3 != null && var3.isAlive()) {
         this.updateSprintReset(var1, var2.crit(), var2.sprint());
         if (var2.pauseWhileEating() && CombatUtil.isConsuming()) {
            this.clearSprintReset(var1);
            return false;
         }

         AABB var4 = var2.box() != null ? var2.box().inflate(var3.getPickRadius()) : var3.getBoundingBox().inflate(var3.getPickRadius());
         if (CombatUtil.distanceToBox(var1, var4) > var2.range()) {
            this.clearSprintReset(var1);
            return false;
         }

         boolean var5 = this.canCrit(var1, var2.crit(), var2.predictCrits(), var2.blatant());
         if (var2.sprint() != AuraAttackController.SprintMode.NONE && var5 && !this.isSprintResetActive(var1)) {
            int var6 = !var1.isSprinting() && !markTargetChanged() ? 0 : 1;
            this.startSprintReset(var1);
            if (var6 != 0 && var2.sprint() == AuraAttackController.SprintMode.SPRINT_RESET) {
               return false;
            }
         }

         if (this.isCooldownReady(var1, var2.legacy(), var2.tpsSync(), var2.randomize(), var2.blatant()) && var2.entityHit()) {
            int var8 = var2.ignoreWalls() && !var2.raycastWalls() ? 1 : 0;
            if (!CombatUtil.isLookingAtBox(var1, var4, var2.yaw(), var2.pitch(), var2.range(), (boolean)var8)) {
               if (!var5 && !this.isSprintResetActive(var1)) {
                  this.clearSprintReset(var1);
               }

               return false;
            } else {
               if (var2.autoJump() && var2.crit() != AuraAttackController.CritMode.NONE && canAutoJump(var1)) {
                  var1.jumpFromGround();
                  return false;
               }

               if (!scheduleNextAttack(var1, var2.crit())) {
                  if (!var5 && !this.isSprintResetActive(var1)) {
                     this.clearSprintReset(var1);
                  }

                  return false;
               } else if (!CombatState.isSprinting(var1.isSprinting())) {
                  if (!this.sprintReportDeferred) {
                     LarpLog.debug("Aura", "Attack deferred: waiting for vanilla sprint report; localSprint=" + var1.isSprinting());
                  }

                  this.sprintReportDeferred = (boolean)1;
                  return false;
               } else {
                  this.sprintReportDeferred = (boolean)0;
                  boolean var7 = InteractionArbiter.runAttackClaim(this.module, var3, () -> {
                     if (var2.beforeAttack() != null) {
                        var2.beforeAttack().run();
                     }

                     this.attack(var1, var3, var2.shieldBreaker());
                  });
                  if (!var7) {
                     return false;
                  }

                  this.cooldownStartNanos = 0L;
                  if (var2.legacy()) {
                     this.scheduleNextAttack(var2.minCps(), var2.maxCps(), var2.tpsSync(), var2.blatant());
                  }

                  AuraModule.sprintResetActive = (boolean)0;
                  this.sprintResetUntilMillis = 0L;
                  return true;
               }
            }
         } else {
            if (!var5 && !this.isSprintResetActive(var1)) {
               this.clearSprintReset(var1);
            }

            return false;
         }
      } else {
         this.clearSprintReset(var1);
         return false;
      }
   }

   private void attack(LocalPlayer var1, LivingEntity var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var3
         && !this.shieldSwapper.isSwapped()
         && var2 instanceof Player var5
         && com.larpclient.util.entity.CombatUtil.isBlockingAttack(var5, var1)
         && !com.larpclient.util.entity.CombatUtil.isRecentTarget(var5)) {
         int var6 = com.larpclient.util.entity.CombatUtil.findShieldBreakerSlot(var1);
         if (var6 >= 0 && this.shieldSwapper.swapTo(var1, var6)) {
            com.larpclient.util.entity.CombatUtil.markRecentTarget(var5);
            LarpLog.debug("Aura", "Shield Breaker: hit " + var5.getId() + " with hotbar slot " + (var6 + 1));
         }
      }

      AuraModule.getCombatSettings(var1, "attack-before");
      var4.gameMode.attack(var1, var2);
      AuraModule.getCombatSettings(var1, "attack-after");
      SwingHelper.swingAttack(var1, InteractionHand.MAIN_HAND);
      this.freshTarget = (boolean)0;
      this.attackCount++;
   }

   public void markTargetChanged() {
      this.freshTarget = (boolean)1;
      this.cooldownStartNanos = 0L;
   }

   public boolean isCooldownReady(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var1 == null) {
         return false;
      }

      if (var2) {
         return System.nanoTime() >= this.nextAttackNanos;
      }

      boolean var6 = CombatUtil.isInCobweb();
      int var7 = !CombatUtil.isFalling(var1) && (!var6 || var1.onGround()) ? 0 : 1;
      float var8 = !var5 && var7 == 0 ? getCooldownThreshold(var3, var4, var5) : 0.9F;
      if (var1.getAttackStrengthScale(0.5F) < var8) {
         this.cooldownStartNanos = 0L;
         return false;
      }

      if (!var5 && !this.freshTarget && var7 == 0) {
         if (this.cooldownStartNanos == 0L) {
            this.cooldownStartNanos = System.nanoTime();
            this.cooldownDelayNanos = (long)((5.0 + Math.random() * 15.0) * 1000000.0);
         }

         return System.nanoTime() - this.cooldownStartNanos >= this.cooldownDelayNanos;
      } else {
         this.cooldownStartNanos = 0L;
         return true;
      }
   }

   public float ticksUntilReady(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var2) {
         return Math.max(0.0F, (float)(this.nextAttackNanos - System.nanoTime()) / 5.0E7F);
      }

      if (var1 == null) {
         return Float.POSITIVE_INFINITY;
      }

      double var6 = var1.getAttributeValue(Attributes.ATTACK_SPEED);
      if (var6 <= 0.0) {
         return Float.POSITIVE_INFINITY;
      }

      float var8 = (float)(20.0 / var6);
      return Math.max(0.0F, (getCooldownThreshold(var3, var4, var5) - var1.getAttackStrengthScale(0.5F)) * var8);
   }

   private void startSprintReset(LocalPlayer var1) {
      this.sprintResetUntilMillis = System.currentTimeMillis() + 1200L;
      AuraModule.sprintResetActive = (boolean)1;
      stopSprinting(var1);
   }

   private void updateSprintReset(LocalPlayer var1, AuraAttackController.CritMode var2, AuraAttackController.SprintMode var3) {
      if (var3 != AuraAttackController.SprintMode.NONE && var2 != AuraAttackController.CritMode.NONE && !var1.getMainHandItem().is(Items.MACE)) {
         if (this.isSprintResetActive(var1)) {
            stopSprinting(var1);
         }
      } else {
         this.clearSprintReset(var1);
      }
   }

   private static void stopSprinting(LocalPlayer var0) {
      if (var0 != null) {
         var0.setSprinting(false);
      }
   }

   private static boolean markTargetChanged() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.options != null && var0.options.keySprint.isDown();
   }

   private static float canCrit() {
      return Mth.clamp(20.0F / TpsTracker.getTps(), 1.0F, 3.0F);
   }

   private static float getCooldownThreshold(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return 1.0F;
      }

      float var3 = 1.0F;
      if (var1) {
         var3 = 0.97F + (float)(Math.random() * 0.03F);
      }

      return Math.min(var3, 1.0F);
   }

   private void scheduleNextAttack(float var1, float var2, boolean var3, boolean var4) {
      float var5 = Math.max(1.0F, Math.min(var1, var2));
      float var6 = Math.max(var5, Math.max(var1, var2));
      float var7 = var4 ? var6 : var5 + (float)(Math.random() * (var6 - var5));
      long var8 = (long)(1.0E9F / var7);
      if (var3) {
         var8 = Math.max(50000000L, (long)((float)var8 * canCrit()));
      }

      this.nextAttackNanos = System.nanoTime() + var8;
   }

   private static boolean scheduleNextAttack(LocalPlayer var0, AuraAttackController.CritMode var1) {
      if (!var0.getMainHandItem().is(Items.MACE) && var1 != AuraAttackController.CritMode.NONE && CombatUtil.canAttackState(var0)) {
         boolean var2 = CombatUtil.isFalling(var0);
         if (var1 == AuraAttackController.CritMode.ALWAYS) {
            return var2;
         }

         if (var2) {
            return true;
         }

         if (!var0.onGround()) {
            return false;
         }

         Minecraft var3 = Minecraft.getInstance();
         return var3.options == null || !var3.options.keyJump.isDown();
      } else {
         return true;
      }
   }

   private boolean canCrit(LocalPlayer var1, AuraAttackController.CritMode var2, boolean var3, boolean var4) {
      if (var2 == AuraAttackController.CritMode.NONE || var1.getMainHandItem().is(Items.MACE)) {
         return false;
      }

      if (isCritBlocked(var1)) {
         return false;
      }

      if (AuraModule.sprintResetActive && !var1.onGround()) {
         return true;
      }

      if (!var1.onGround() && var1.fallDistance > 0.0) {
         return true;
      }

      if (!var3) {
         return !var1.onGround() && var1.getDeltaMovement().y < 0.0;
      }

      int var5 = markTargetChanged(var1);
      return var5 >= 0 && var5 <= (var4 ? 1 : 2);
   }

   private static int markTargetChanged(LocalPlayer var0) {
      if (CombatUtil.isFalling(var0)) {
         return 0;
      }

      double var1 = var0.getDeltaMovement().y;
      if (var0.onGround()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3.options == null || !var3.options.keyJump.isDown()) {
            return -1;
         }

         var1 = 0.42;
      }

      double var6 = var0.fallDistance;

      for (int var5 = 1; var5 <= 8; var5++) {
         var1 = (var1 - 0.08) * 0.98;
         if (var1 < 0.0) {
            var6 += -var1;
            if (var6 > 0.0) {
               return var5;
            }
         }
      }

      return -1;
   }

   private static boolean isCritBlocked(LocalPlayer var0) {
      return var0.onClimbable()
         || var0.isInWater()
         || var0.isInLava()
         || var0.isPassenger()
         || var0.getAbilities().flying
         || var0.isFallFlying()
         || var0.hasEffect(MobEffects.BLINDNESS)
         || var0.hasEffect(MobEffects.LEVITATION)
         || var0.hasEffect(MobEffects.SLOW_FALLING);
   }

   private static boolean canAutoJump(LocalPlayer var0) {
      return var0.onGround() && !var0.onClimbable() && !var0.isInWater() && !var0.isInLava() && !var0.isPassenger() && !var0.getAbilities().flying;
   }

   public int scheduleNextAttack() {
      return this.attackCount;
   }

   public void reset() {
      this.attackCount = 0;
      this.freshTarget = (boolean)1;
      this.nextAttackNanos = 0L;
      this.sprintReportDeferred = (boolean)0;
      this.sprintResetUntilMillis = 0L;
      AuraModule.sprintResetActive = (boolean)0;
   }

   public record AttackRequest(
      LivingEntity target,
      AABB box,
      float yaw,
      float pitch,
      double range,
      boolean ignoreWalls,
      AuraAttackController.CritMode crit,
      AuraAttackController.SprintMode sprint,
      boolean shieldBreaker,
      boolean pauseWhileEating,
      boolean raycastWalls,
      boolean entityHit,
      boolean autoJump,
      boolean predictCrits,
      Runnable beforeAttack,
      boolean legacy,
      boolean tpsSync,
      boolean randomize,
      boolean blatant,
      float minCps,
      float maxCps
   ) {
   }

   public enum CritMode {
      NONE,
      SMART,
      ALWAYS;
   }

   public enum SprintMode {
      SPRINT_RESET,
      KEEP_SPRINT,
      NONE;
   }
}
