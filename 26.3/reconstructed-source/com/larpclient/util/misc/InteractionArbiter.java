package com.larpclient.util.misc;

import com.larpclient.core.ViewController;
import com.larpclient.module.combat.AnchorMacroModule;
import com.larpclient.module.combat.AnchorTapModule;
import com.larpclient.module.combat.AutoCrystalModule;
import com.larpclient.module.combat.CriticalsModule;
import com.larpclient.module.combat.CrystalTapModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.player.RodSwapModule;
import com.larpclient.util.entity.AimPoints;
import com.larpclient.util.entity.CombatState;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.math.ValueRangeCheck;
import com.larpclient.util.world.NativePlacement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Rot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class InteractionArbiter {
   private static final List<InteractionArbiter.PendingAction> pendingActions = new ArrayList<>();
   private static boolean claimed;
   private static boolean runningAction;
   private static boolean manualClaimed;
   private static final List<InteractionArbiter.PendingAttack> pendingAttacks = new ArrayList<>();
   private static InteractionArbiter.PendingAttack delayedAttack;
   private static int delayStartTick;
   private static long lastGameTime = Long.MIN_VALUE;
   private static final Set<Integer> attackedEntityIds = new HashSet<>();
   private static String owner;
   private static boolean attackInProgress;

   public static void queueAction(Module var0, Runnable var1, BooleanSupplier var2) {
      pendingActions.add(new InteractionArbiter.PendingAction(var0, var1, var2));
   }

   public static boolean ownsInteraction(Module var0) {
      syncTick();
      return canModuleInteract(var0) && (owner == null || owner.equals(var0.getName()));
   }

   public static boolean tryRunAction(Module var0, Runnable var1, BooleanSupplier var2) {
      syncTick();
      if (var0 != null
         && var0.isRuntimeActive()
         && canInteract()
         && var2.getAsBoolean()
         && delayedAttack == null
         && !claimed
         && attackedEntityIds.isEmpty()
         && (owner == null || owner.equals(var0.getName()))) {
         owner = var0.getName();
         claimed = (boolean)1;
         runningAction = (boolean)1;

         try {
            var1.run();
         } finally {
            runningAction = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean tryRunEating(Runnable var0) {
      syncTick();
      if (claimTarget() && !ViewController.isFreecam() && delayedAttack == null && !claimed && attackedEntityIds.isEmpty() && owner == null) {
         owner = "Eating";
         claimed = (boolean)1;
         runningAction = (boolean)1;

         try {
            var0.run();
         } finally {
            runningAction = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean findAttackBlocker(Module var0, Entity var1, double var2) {
      syncTick();
      if (var0 != null
         && var1 != null
         && var0.isRuntimeActive()
         && delayedAttack == null
         && !claimed
         && isTargetAvailable(var0.getName(), var1)
         && isLookingAt(var1, var2)) {
         Minecraft var4 = Minecraft.getInstance();
         InteractionArbiter.PendingAttack var5 = new InteractionArbiter.PendingAttack(var0, var1, var2, false, () -> true, 1);
         performAttack(var5);
         return CombatUtil.hasTickedThisTick(var4.player);
      } else {
         return false;
      }
   }

   public static boolean runAttackClaim(Module var0, Entity var1, Runnable var2) {
      syncTick();
      if (var0 != null
         && var1 != null
         && var0.isRuntimeActive()
         && canModuleInteract(var0)
         && delayedAttack == null
         && !claimed
         && claimTarget(var0.getName(), var1)) {
         attackInProgress = (boolean)1;

         try {
            var2.run();
         } finally {
            attackInProgress = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean runAttackFollowup(Module var0, Entity var1, Runnable var2) {
      syncTick();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && canModuleInteract(var0) && attackedEntityIds.contains(var1.getId())) {
         attackInProgress = (boolean)1;

         try {
            var2.run();
         } finally {
            attackInProgress = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isModuleInteracting() {
      return attackInProgress || runningAction;
   }

   public static boolean claimManualInteraction() {
      if (runningAction) {
         return true;
      } else if (!canInteract()) {
         return false;
      } else {
         syncTick();
         if (AnchorTapModule.isHandlingUse() || AnchorMacroModule.isActive()) {
            return false;
         } else if (CrystalTapModule.onUseBlock()) {
            return false;
         } else if (delayedAttack == null && (owner == null || owner.equals("Manual"))) {
            claimed = (boolean)1;
            owner = "Manual";
            manualClaimed = (boolean)1;
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean wasManualClaimed() {
      syncTick();
      return manualClaimed;
   }

   public static void findAttackBlocker() {
      manualClaimed = (boolean)0;
   }

   public static boolean getPriority() {
      return claimTarget() && Minecraft.getInstance().gui.screen() == null;
   }

   private static boolean claimTarget() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && var0.level != null && var0.gameMode != null && var0.player.isAlive() && !var0.player.isSpectator();
   }

   public static boolean canInteract() {
      return getPriority() && !ViewController.isFreecam();
   }

   public static boolean canModuleInteract(Module var0) {
      return getPriority(var0) && (!ViewController.isFreecam() || allowsDetachedCamera(var0));
   }

   public static boolean allowsDetachedCamera(Module var0) {
      return var0 != null && var0.isRuntimeActive() && var0.allowsDetachedCamera();
   }

   public static boolean getPriority(Module var0) {
      return claimTarget() && allowsScreenInteraction(Minecraft.getInstance().gui.screen() != null, var0);
   }

   public static boolean allowsScreenInteraction(boolean var0, Module var1) {
      return !var0 || var1 != null && var1.isRuntimeActive() && var1.allowsScreenInteractions();
   }

   public static boolean tryRunAction() {
      return !pendingAttacks.isEmpty() || !pendingActions.isEmpty() || delayedAttack != null;
   }

   public static void clearQueues() {
      pendingAttacks.clear();
      pendingActions.clear();
      syncTick();
   }

   private static void syncTick() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != null) {
         resetTick(var0.getGameTime());
      }
   }

   private static void resetTick(long var0) {
      if (lastGameTime != var0) {
         lastGameTime = var0;
         attackedEntityIds.clear();
         owner = null;
         claimed = (boolean)0;
         manualClaimed = (boolean)0;
      }
   }

   public static void reset() {
      CombatState.reset();
      NativePlacement.clear();
      pendingAttacks.clear();
      pendingActions.clear();
      claimed = (boolean)0;
      manualClaimed = (boolean)0;
      runningAction = (boolean)0;
      delayedAttack = null;
      attackedEntityIds.clear();
      owner = null;
      lastGameTime = Long.MIN_VALUE;
      attackInProgress = (boolean)0;
   }

   public static void findAttackBlocker(Module var0) {
      pendingAttacks.removeIf(var1 -> var1.source == var0);
      pendingActions.removeIf(var1 -> var1.source == var0);
      if (delayedAttack != null && delayedAttack.source == var0) {
         delayedAttack = null;
      }
   }

   public static void claimManualInteraction(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5) {
      resetTick(var0, var1, var2, var4, var5, 1);
   }

   public static void resetTick(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6) {
      pendingAttacks.add(new InteractionArbiter.PendingAttack(var0, var1, var2, var4, var5, var6));
   }

   public static void performAttack(Module var0, Entity var1, double var2, BooleanSupplier var4, Supplier<float[]> var5, Runnable var6) {
      pendingAttacks.add(new InteractionArbiter.PendingAttack(var0, var1, var2, false, var4, 1, var5, var6, var0x -> {}));
   }

   public static double claimManualInteraction(Entity var0) {
      return BoxRaycastUtil.distanceToBox(Minecraft.getInstance().player.getEyePosition(), var0.getBoundingBox());
   }

   public static boolean isLookingAt(Entity var0, double var1) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      return var3 != null && canHit(var0, var1, var3.getLookAngle());
   }

   public static void queueAttackWithCallbacks(
      Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6, Runnable var7, Consumer<String> var8
   ) {
      pendingAttacks.add(new InteractionArbiter.PendingAttack(var0, var1, var2, var4, var5, var6, null, var7, var8));
   }

   public static boolean processQueue() {
      return Minecraft.getInstance().player != null && Minecraft.getInstance().player.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   public static double clampRange(double var0) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 == null ? 0.0 : Math.max(0.0, Math.min(var0, var2.entityInteractionRange()));
   }

   public static boolean canHit(Entity var0, double var1, Vec3 var3) {
      return findAttackBlocker(var0, var1, var3) == null;
   }

   public static String findAttackBlocker(Entity var0, double var1, Vec3 var3) {
      if (!canInteract() || var0 == null) {
         return "interaction ownership";
      }

      if (RodSwapModule.isThrowActive()) {
         return "temporary rod selection";
      }

      Minecraft var4 = Minecraft.getInstance();
      if (var0 != var4.player && var0.level() == var4.level && var0.isAlive() && !var0.isRemoved() && !var0.isSpectator() && var0.isPickable()) {
         double var5 = clampRange(var1);
         Vec3 var7 = var4.player.getEyePosition();
         if (claimManualInteraction(var0) > var5 + 1.0E-7) {
            return "range";
         } else {
            Optional var8 = BoxRaycastUtil.raycastBox(var7, var3, var0.getBoundingBox().inflate(var0.getPickRadius()), var5);
            if (var8.isEmpty()) {
               return "alignment";
            } else {
               return claimTarget(var0, var7, (Vec3)var8.get(), clampRange(var0, var7)) ? null : "obstruction";
            }
         }
      } else {
         return "target invalid";
      }
   }

   private static List<Entity> clampRange(Entity var0, Vec3 var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2.level.getEntities(var2.player, var0.getBoundingBox().minmax(new AABB(var1, var1)).inflate(1.0));
   }

   private static boolean claimTarget(Entity var0, Vec3 var1, Vec3 var2, List<Entity> var3) {
      Minecraft var4 = Minecraft.getInstance();
      BlockHitResult var5 = var4.level.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, var4.player));
      if (var5.getType() != Type.MISS && var1.distanceToSqr(var5.getLocation()) + 1.0E-7 < var1.distanceToSqr(var2)) {
         return false;
      }

      for (Entity var7 : var3) {
         if (var7 != var0 && var7.isPickable() && !var7.isSpectator() && BoxRaycastUtil.isBoxBlocking(var1, var2, var7.getBoundingBox())) {
            return false;
         }
      }

      return true;
   }

   public static BlockHitResult wasManualClaimed(BlockPos var0, double var1) {
      return !canInteract() ? null : reset(var0, var1, Minecraft.getInstance().player.getLookAngle());
   }

   public static BlockHitResult reset(BlockPos var0, double var1, Vec3 var3) {
      if (!canInteract()) {
         return null;
      }

      Minecraft var4 = Minecraft.getInstance();
      double var5 = Math.max(0.0, Math.min(var1, var4.player.blockInteractionRange()));
      Vec3 var7 = var4.player.getEyePosition();
      Vec3 var8 = var7.add(var3.normalize().scale(var5));
      BlockHitResult var9 = var4.level.clip(new ClipContext(var7, var8, Block.OUTLINE, Fluid.NONE, var4.player));
      if (var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0)) {
         for (Entity var11 : var4.level.getEntities(var4.player, new AABB(var7, var8).inflate(1.0))) {
            if (var11.isPickable() && !var11.isSpectator() && BoxRaycastUtil.isBoxBlocking(var7, var9.getLocation(), var11.getBoundingBox())) {
               return null;
            }
         }

         return var9;
      } else {
         return null;
      }
   }

   public static Optional<Vec3> findVisibleAimPoint(Entity var0, double var1, Vec3 var3, Vec3 var4) {
      if (canInteract() && var0 != null && !(claimManualInteraction(var0) > var1 + 1.0E-7)) {
         Vec3 var5 = Minecraft.getInstance().player.getEyePosition();
         List var6 = clampRange(var0, var5);
         return AimPoints.rotationToBox(var0.getBoundingBox(), var5, var3, var4, var5x -> {
            Optional var6x = BoxRaycastUtil.raycastBox(var5, var5x.subtract(var5), var0.getBoundingBox(), var1);
            return var6x.isPresent() && claimTarget(var0, var5, (Vec3)var6x.get(), var6);
         });
      } else {
         return Optional.empty();
      }
   }

   public static int getPriority(String var0) {
      return switch (var0) {
         case "Aura" -> 4;
         case "AutoCart" -> 5;
         case "AutoCrystal" -> 6;
         case "Auto MLG" -> 7;
         case "Auto Pearl Catch" -> 8;
         case "Wind Charge Hop" -> 8;
         case "TriggerBot" -> 2;
         case "AutoClicker" -> 1;
         case "Scaffold" -> 1;
         case "Auto Drain" -> 1;
         case "Auto Area Miner" -> 1;
         case "Auto Elytra Pilot" -> 1;
         case "Anchor Hunter" -> 1;
         case "Auto Builder" -> 1;
         case "Rod Swap" -> 0;
         default -> 5;
      };
   }

   private static Vec3 getAimDirection(InteractionArbiter.PendingAttack var0) {
      if (var0.aim != null) {
         float[] var1 = var0.aim.get();
         return Vec3.directionFromRotation(var1[1], var1[0]);
      } else {
         return Minecraft.getInstance().player.getLookAngle();
      }
   }

   public static void allowsScreenInteraction() {
      syncTick();
      if (claimTarget()) {
         AutoCrystalModule var0 = AutoCrystalModule.placeCrystal();
         if (getPriority() && var0 != null && !ViewController.isFreecam()) {
            var0.updateAim();
         }

         NativePlacement.tick();
      }
   }

   private static boolean claimTarget(InteractionArbiter.PendingAttack var0) {
      Object var1 = null;
      if ((var0.source == null || var0.source.isRuntimeActive()) && var0.valid.getAsBoolean()) {
         if (var0.cooldown && !processQueue()) {
            var1 = "cooldown";
         } else {
            var1 = findAttackBlocker(var0.target, var0.range, getAimDirection(var0));
         }
      } else {
         var1 = "intent expired / item use";
      }

      if (var1 != null) {
         var0.rejected.accept((String)var1);
      }

      return var1 == null;
   }

   public static void processQueue() {
      syncTick();
      if (claimTarget() && (!ViewController.isFreecam() || !pendingActions.stream().noneMatch(var0x -> allowsDetachedCamera(var0x.source)))) {
         if (ViewController.isFreecam()) {
            pendingAttacks.clear();
            delayedAttack = null;
            pendingActions.removeIf(var0x -> !allowsDetachedCamera(var0x.source));
            NativePlacement.pruneUnscheduled();
         }

         if (!getPriority()) {
            pendingAttacks.clear();
            delayedAttack = null;
            NativePlacement.pruneInactive();
         }

         Minecraft var0 = Minecraft.getInstance();
         pendingAttacks.sort(Comparator.<InteractionArbiter.PendingAttack>comparingInt(var0x -> getPriority(var0x.source.getName())).reversed());
         if (delayedAttack != null) {
            int var1 = !pendingActions.stream()
                     .anyMatch(
                        var0x -> var0x.source.isRuntimeActive()
                           && var0x.valid.getAsBoolean()
                           && getPriority(var0x.source.getName()) > getPriority(getPriority(delayedAttack))
                     )
                  && claimTarget(delayedAttack)
                  && CriticalsModule.canAssist()
                  && (var0.player.tickCount == delayStartTick || !var0.player.onGround())
                  && pendingAttacks.stream()
                     .noneMatch(var0x -> claimTarget(var0x) && getPriority(getPriority(var0x)) > getPriority(getPriority(delayedAttack)))
               ? 1
               : 0;
            switch (ValueRangeCheck.classify(var0.player.tickCount - delayStartTick, (boolean)var1, CriticalsModule.isFallingCritical())) {
               case CANCEL:
                  delayedAttack.rejected.accept("critical delay cancelled");
                  delayedAttack = null;
                  break;
               case ATTACK:
                  performAttack(delayedAttack);
                  delayedAttack = null;
                  break;
               case CLEAR_ALL:
                  pendingAttacks.clear();
                  pendingActions.clear();
                  return;
            }
         }

         pendingActions.sort(Comparator.<InteractionArbiter.PendingAction>comparingInt(var0x -> getPriority(var0x.source.getName())).reversed());

         for (InteractionArbiter.PendingAttack var2 : List.copyOf(pendingAttacks)) {
            if (pendingActions.stream()
               .anyMatch(
                  var1x -> var1x.source.isRuntimeActive() && var1x.valid.getAsBoolean() && getPriority(var1x.source.getName()) > getPriority(getPriority(var2))
               )) {
               var2.rejected.accept("interaction ownership");
            } else if (isTargetAvailable(getPriority(var2), var2.target) && attackedEntityIds.size() < var2.limit) {
               if (claimTarget(var2)) {
                  if (startCriticalDelay(var2)) {
                     break;
                  }

                  performAttack(var2);
               }
            } else {
               var2.rejected.accept("interaction ownership");
            }
         }

         pendingAttacks.clear();
         if (delayedAttack == null && !claimed) {
            for (InteractionArbiter.PendingAction var8 : List.copyOf(pendingActions)) {
               if (canModuleInteract(var8.source)
                  && var8.source.isRuntimeActive()
                  && var8.valid.getAsBoolean()
                  && (owner == null || owner.equals(var8.source.getName()))) {
                  owner = var8.source.getName();
                  claimed = (boolean)1;
                  runningAction = (boolean)1;

                  try {
                     var8.action.run();
                     break;
                  } finally {
                     runningAction = (boolean)0;
                  }
               }
            }
         }

         pendingActions.clear();
      } else {
         reset();
      }
   }

   private static String getPriority(InteractionArbiter.PendingAttack var0) {
      return var0.source == null ? "Manual" : var0.source.getName();
   }

   private static boolean startCriticalDelay(InteractionArbiter.PendingAttack var0) {
      if (var0.target instanceof LivingEntity && CriticalsModule.shouldAttackNow()) {
         var0.rejected.accept("critical delay");
         delayedAttack = var0;
         delayStartTick = Minecraft.getInstance().player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private static void performAttack(InteractionArbiter.PendingAttack var0) {
      if (claimTarget(var0)) {
         if (attackedEntityIds.size() < var0.limit && claimTarget(getPriority(var0), var0.target)) {
            Minecraft var1 = Minecraft.getInstance();
            int var2 = var0.aim != null ? 1 : 0;
            attackInProgress = (boolean)1;

            try {
               if (var2 != 0) {
                  float[] var3 = var0.aim.get();
                  var1.player.connection.send(new Rot(var3[0], var3[1], var1.player.onGround(), var1.player.horizontalCollision));
               }

               var1.gameMode.attack(var1.player, var0.target);
               SwingHelper.swingAttack(var1.player, InteractionHand.MAIN_HAND);
               var0.completed.run();
            } finally {
               attackInProgress = (boolean)0;
               if (var2 != 0) {
                  var1.player.connection.send(new Rot(var1.player.getYRot(), var1.player.getXRot(), var1.player.onGround(), var1.player.horizontalCollision));
               }
            }
         }
      }
   }

   private static boolean isTargetAvailable(String var0, Entity var1) {
      return (owner == null || owner.equals(var0)) && !attackedEntityIds.contains(var1.getId());
   }

   private static boolean claimTarget(String var0, Entity var1) {
      if (!isTargetAvailable(var0, var1)) {
         return false;
      }

      owner = var0;
      attackedEntityIds.add(var1.getId());
      return true;
   }

   public static boolean tryManualAttack(Entity var0) {
      if (!attackInProgress) {
         RodSwapModule.onPlayerAttack();
      }

      if (attackInProgress) {
         return true;
      }

      if (!isLookingAt(var0, Double.MAX_VALUE)) {
         return false;
      }

      syncTick();
      if (delayedAttack != null) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      InteractionArbiter.PendingAttack var2 = new InteractionArbiter.PendingAttack(
         null,
         var0,
         var1.player.entityInteractionRange(),
         true,
         () -> var1.options.keyAttack.isDown() && var1.hitResult instanceof EntityHitResult var2x && var2x.getEntity() == var0,
         1
      );
      return isTargetAvailable("Manual", var0) && !startCriticalDelay(var2) ? claimTarget("Manual", var0) : false;
   }

   public static boolean resetTick() {
      return delayedAttack != null;
   }

   public static boolean isAttacking() {
      return attackInProgress;
   }

   private InteractionArbiter() {
   }

   private record PendingAction(Module source, Runnable action, BooleanSupplier valid) {
   }

   private record PendingAttack(
      Module source,
      Entity target,
      double range,
      boolean cooldown,
      BooleanSupplier valid,
      int limit,
      Supplier<float[]> aim,
      Runnable completed,
      Consumer<String> rejected
   ) {
      PendingAttack(Module var1, Entity var2, double var3, boolean var5, BooleanSupplier var6, int var7) {
         this(var1, var2, var3, var5, var6, var7, null, () -> {}, var0 -> {});
      }
   }
}
