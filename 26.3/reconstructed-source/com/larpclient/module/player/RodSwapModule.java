package com.larpclient.module.player;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.MinecraftMixin_3;
import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.RodAttemptTracker;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.math.ProjectileTrajectory;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import java.util.Set;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class RodSwapModule extends Module {
   private static RodSwapModule instance;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode", "Cast Only returns once the bobber hits or misses; Hook and Reel pulls a hooked player once", "Cast Only", "Cast Only", "Hook and Reel"
      )
   );
   private final NumberSetting range = this.addSetting(
      new NumberSetting("Range", "Farthest player distance for a throw; Kill Aura tracks players this far while a rod is ready", 6.0, 3.0, 8.0, 0.25)
   );
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Milliseconds with your weapon back in hand before the next throw", 1000.0, 250.0, 5000.0, 50.0)
   );
   private final NumberSetting timeout = this.addSetting(
      new NumberSetting("Timeout", "Longest a throw keeps the rod out before returning to your weapon", 1000.0, 250.0, 2000.0, 50.0)
   );
   private static final int mAX_AIM_TICKS = 20;
   private static final long rETRY_BACKOFF_NANOS = 250000000L;
   private static final int mAX_TRAJECTORY_TICKS = 20;
   private static final double rEACH_MARGIN = 0.5;
   private final HotbarSwapSession swapSession = new HotbarSwapSession(this);
   private final RodAttemptTracker attemptTracker = new RodAttemptTracker();
   private final RodAttemptTracker.Sample throwCooldown = new RodAttemptTracker.Sample();
   private String lastWaitReason;
   private long lastWaitLogNanos;
   private LocalPlayer player;
   private ClientLevel level;
   private Player target;
   private Player aimTarget;
   private FishingHook bobber;
   private ItemStack weaponStack = ItemStack.EMPTY;
   private int weaponSlot = -1;
   private int rodSlot = -1;
   private int aimTicks;
   private String status = "Enable Kill Aura";

   public RodSwapModule() {
      super("Rod Swap", "Kill Aura throws a hotbar rod at its player target on a delay, then returns to its weapon", Category.COMBAT, -1);
      instance = this;
      this.mode.onChange(var1 -> this.endThrow("Mode changed", true));
   }

   @Override
   public String getDisplayName() {
      return "Auto Rod";
   }

   public List<Setting<?>> getModeSettings() {
      return List.of(this.mode);
   }

   public List<Setting<?>> hasManualSlotSelection() {
      return List.of(this.range, this.delay, this.timeout);
   }

   public static void onManualSlotSelection() {
      if (instance != null && instance.isRuntimeActive()) {
         instance.endThrow("Manual slot selection", false);
         instance.attemptTracker.startAttempt(System.nanoTime());
      }
   }

   public static boolean isThrowActive() {
      return instance != null && instance.isEnabled()
         ? instance != null && instance.isRuntimeActive() && instance.attemptTracker.isActive() && instance.swapSession.isActive()
         : false;
   }

   public static void onPlayerAttack() {
      if (isThrowActive()) {
         instance.endThrow("Your attack", true);
      }
   }

   public static double getAimRange() {
      RodSwapModule var0 = instance;
      return var0 != null && var0.isRuntimeActive() && var0.player != null && var0.findRodSlot() >= 0 ? var0.range.getValue() : 0.0;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   @Override
   public void onEnable() {
      this.player = getMc().player;
      this.level = minecraft.level;
      this.attemptTracker.isActive();
      this.throwCooldown.getSecond();
      this.resetAim();
      this.status = "Ready";
      this.lastWaitReason = null;
      LarpLog.info(
         "RodSwap",
         "Auto Rod enabled; mode={}, range={}, delay={}ms, timeout={}ms",
         this.mode.getValue(),
         this.range.getValue(),
         this.delay.getValue().longValue(),
         this.timeout.getValue().longValue()
      );
   }

   @Override
   public void onDisable() {
      this.endThrow("Disabled or disconnected", true);
      this.resetAim();
      this.player = null;
      this.level = null;
   }

   private boolean solveAim() {
      Minecraft var1 = getMc();
      return this.player != null
         && this.player == var1.player
         && this.level != null
         && this.level == var1.level
         && (CombatUtil.isActive(var1) || CombatUtil.isActiveFor(var1, ViewController.Owner.AURA))
         && !this.player.isSpectator()
         && this.player.isAlive();
   }

   private boolean hasManualSlotSelection() {
      for (KeyMapping var4 : minecraft.options.keyHotbarSlots) {
         if (CombatUtil.markPlayerTicked(var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean isWeaponSlotIntact() {
      if (this.player != null && this.weaponSlot >= 0 && this.weaponSlot < 9) {
         ItemStack var1 = this.player.getInventory().getItem(this.weaponSlot);
         return !var1.is(Items.FISHING_ROD) && ItemStack.isSameItemSameComponents(this.weaponStack, var1);
      } else {
         return false;
      }
   }

   private boolean throwRod(int var1) {
      if (this.player != null && this.level != null && var1 >= 0 && var1 < 9) {
         ItemStack var2 = this.player.getInventory().getItem(var1);
         return var2.is(Items.FISHING_ROD)
            && !var2.nextDamageWillBreak()
            && var2.isItemEnabled(this.level.enabledFeatures())
            && !this.player.getCooldowns().isOnCooldown(var2);
      } else {
         return false;
      }
   }

   private int findRodSlot() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.throwRod(var1)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean isValidTarget(Entity var1) {
      return var1 instanceof Player var2 && CombatUtil.isValidTarget(this.player, var2) && !this.player.isAlliedTo(var2) && var2.isPickable();
   }

   private static Vec3 getVelocity(Entity var0) {
      return var0.position().subtract(var0.xo, var0.yo, var0.zo);
   }

   private double distanceToTarget(Player var1) {
      return BoxRaycastUtil.distanceToBox(this.player.getEyePosition(), var1.getBoundingBox());
   }

   private Entity findAimedEntity() {
      ProjectileTrajectory.ProjectileState var1 = ProjectileTrajectory.initialState(this.player.getEyePosition(), this.player.getYRot(), this.player.getXRot());
      List var2 = this.level
         .getEntities(
            this.player,
            this.player.getBoundingBox().inflate(this.range.getValue() + 4.0),
            var1x -> var1x.isAlive() && !var1x.isSpectator() && var1x.canBeHitByProjectile() && var1x != this.player.getVehicle()
         );

      for (int var3 = 0; var3 < 20; var3++) {
         Vec3 var4 = var1.position();
         Vec3 var5 = var1.collisionEnd();
         BlockHitResult var6 = this.level.clip(new ClipContext(var4, var5, Block.COLLIDER, Fluid.ANY, this.player));
         double var7 = var6.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var4.distanceToSqr(var6.getLocation());
         Entity var9 = null;

         for (Entity var11 : var2) {
            AABB var12 = var11.getBoundingBox()
               .move(ProjectileTrajectory.scaleVelocity(getVelocity(var11), var3))
               .inflate(ProjectileTrajectory.chargeFraction(var3));
            double var13 = ProjectileTrajectory.rayDistanceSq(var4, var5, var12);
            if (var13 < var7) {
               var7 = var13;
               var9 = var11;
            }
         }

         if (var9 != null || var6.getType() != Type.MISS) {
            return var9;
         }

         var1 = var1.next();
      }

      return null;
   }

   private String getIgnoreReason(Player var1) {
      if (!var1.isSpectator() && !var1.isCreative()) {
         if (this.player.connection.getPlayerInfo(var1.getUUID()) == null) {
            return "no player-list entry";
         } else if (this.player.isAlliedTo(var1) || !this.player.canHarmPlayer(var1)) {
            return "teammate";
         } else {
            return var1.isPickable() && var1.isAttackable() ? "friend" : "not attackable";
         }
      } else {
         return "creative or spectator";
      }
   }

   private void logWaiting(String var1) {
      long var2 = System.nanoTime();
      if (!var1.equals(this.lastWaitReason) && (this.lastWaitReason == null || var2 - this.lastWaitLogNanos >= 1000000000L)) {
         this.lastWaitReason = var1;
         this.lastWaitLogNanos = var2;
         LarpLog.info("RodSwap", "Waiting: {}", var1);
      }
   }

   private String getBlockReason(long var1) {
      if (this.player.getMainHandItem().is(Items.FISHING_ROD)) {
         return "Hold your weapon, not the rod";
      } else if (this.player.getOffhandItem().is(Items.FISHING_ROD)) {
         return "Move the offhand rod to the hotbar";
      } else if (this.player.fishing != null) {
         return "Existing bobber";
      } else if (this.findRodSlot() < 0) {
         return "No usable rod in hotbar";
      } else if (!this.attemptTracker.isCooldownOver(var1) || this.hasManualSlotSelection()) {
         return "Manual slot selection";
      } else if (this.player.isInWater()) {
         return "In water";
      } else if (!CombatUtil.canAttack(minecraft, this.player) || CombatUtil.isAttackPressed(minecraft) || CombatUtil.isUsePressed(minecraft)) {
         return "Your hands are busy";
      } else if (!HotbarSwapSession.canSwap(this)) {
         return "Another module owns the hotbar";
      } else {
         return InteractionArbiter.resetTick() ? "Critical hit pending" : null;
      }
   }

   private boolean canThrow(long var1) {
      return this.isRuntimeActive()
         && this.solveAim()
         && !this.attemptTracker.isActive()
         && this.getBlockReason(var1) == null
         && RotationManager.isActive(RotationManager.Priority.AURA)
         && !CombatUtil.hasTickedThisTick(this.player)
         && ((MinecraftMixin_3)minecraft).larpUseDelay() <= 0;
   }

   public static Vec3 getAimPoint(LivingEntity var0) {
      if (instance != null && instance.isEnabled()) {
         RodSwapModule var1 = instance;
         return var1 == null ? null : var1.computeAimPoint(var0);
      } else {
         return null;
      }
   }

   private Vec3 computeAimPoint(LivingEntity var1) {
      if (this.isRuntimeActive() && !this.attemptTracker.isActive() && var1 instanceof Player var2 && this.solveAim()) {
         long var3 = System.nanoTime();
         String var5 = this.findAimedEntity(var2, var3);
         if (var5 != null) {
            this.resetAim();
            if (!var5.isEmpty()) {
               this.logWaiting(var5);
            }

            return null;
         } else {
            ProjectileTrajectory.AimSolution var6 = this.solveAim(var2);
            if (var6 == null) {
               this.resetAim();
               this.logWaiting("No rod arc reaches " + var2.getName().getString());
               return null;
            }

            if (this.aimTarget != var2) {
               this.aimTarget = var2;
               this.aimTicks = 0;
            }

            if (++this.aimTicks > 20) {
               LarpLog.info("RodSwap", "Throw at {} did not line up within {} ticks; retrying shortly", var2.getName().getString(), 20);
               this.throwCooldown.setValue(var3, 250000000L);
               this.resetAim();
               return null;
            } else {
               this.status = "Aiming";
               Vec3 var7 = this.player.getEyePosition();
               double var8 = Math.max(1.0, var7.distanceTo(var2.getBoundingBox().getCenter()));
               return var7.add(Vec3.directionFromRotation(var6.pitch(), var6.yaw()).scale(var8));
            }
         }
      } else {
         this.resetAim();
         return null;
      }
   }

   private String findAimedEntity(Player var1, long var2) {
      if (!this.throwCooldown.setValue(var2)) {
         this.status = "Delay";
         return "";
      }

      if (!this.isValidTarget(var1)) {
         return "Kill Aura target " + var1.getName().getString() + " ignored: " + this.getIgnoreReason(var1);
      }

      AuraModule var4 = AuraModule.getInstance();
      if (var4 == null) {
         return "";
      }

      double var5 = this.distanceToTarget(var1);
      if (var5 <= var4.getAttackRange() + 0.5) {
         this.status = "Inside sword reach";
         return "";
      }

      if (var5 > this.range.getValue()) {
         this.status = "Beyond Range";
         return "";
      }

      String var7 = this.getBlockReason(var2);
      if (var7 != null) {
         this.status = var7;
      }

      return var7;
   }

   private ProjectileTrajectory.AimSolution solveAim(Player var1) {
      Vec3 var2 = this.player.getEyePosition();
      Vec3 var3 = var1.getBoundingBox().getCenter();
      Vec3 var4 = getVelocity(var1);
      ProjectileTrajectory.AimSolution var5 = ProjectileTrajectory.solveAim(var2, var3, 20);

      for (int var6 = 0; var6 < 2 && var5 != null; var6++) {
         var5 = ProjectileTrajectory.solveAim(var2, var3.add(ProjectileTrajectory.scaleVelocity(var4, var5.ticks())), 20);
      }

      return var5;
   }

   private void resetAim() {
      this.aimTarget = null;
      this.aimTicks = 0;
   }

   public void onClientTick() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.player != getMc().player || this.level != minecraft.level) {
               this.endThrow("World changed", false);
               this.throwCooldown.getSecond();
               this.resetAim();
               this.player = minecraft.player;
               this.level = minecraft.level;
            }

            if (!this.solveAim()) {
               this.endThrow("Paused", true);
            } else if (this.hasManualSlotSelection()) {
               this.endThrow("Manual slot selection", false);
               this.attemptTracker.startAttempt(System.nanoTime());
            } else if (this.attemptTracker.isActive()) {
               if (!this.swapSession.isActive()) {
                  this.endThrow("Slot ownership changed", false);
                  this.attemptTracker.startAttempt(System.nanoTime());
               } else if (this.isWeaponSlotIntact()
                  && this.throwRod(this.rodSlot)
                  && HotbarSwapSession.canSwap(this)
                  && CombatUtil.canAttack(minecraft, this.player)
                  && !CombatUtil.isUsePressed(minecraft)
                  && !CombatUtil.isAttackPressed(minecraft)
                  && (!minecraft.options.keyAttack.isDown() || minecraft.hitResult == null || minecraft.hitResult.getType() != Type.BLOCK)) {
                  if (!this.isValidTarget(this.target)) {
                     this.endThrow("Target unavailable", true);
                  } else {
                     FishingHook var1 = this.player.fishing;
                     if (var1 == null || var1.getPlayerOwner() == this.player && var1.level() == this.level && !var1.isRemoved()) {
                        if (this.bobber != null && var1 != null && var1 != this.bobber) {
                           this.endThrow("Bobber replaced", true);
                        } else {
                           if (var1 != null && this.bobber == null) {
                              this.bobber = var1;
                              LarpLog.debug("RodSwap", "Owned bobber observed; entity={}", this.bobber.getId());
                           }

                           Entity var2 = var1 == null ? null : var1.getHookedIn();
                           int var3 = var1 != null
                                 && var1.tickCount > 2
                                 && var1.position().subtract(this.target.getBoundingBox().getCenter()).dot(var1.getDeltaMovement()) > 0.0
                                 && var1.distanceToSqr(this.target) > 2.25
                              ? 1
                              : 0;
                           RodAttemptTracker.Action var4 = this.attemptTracker
                              .update(
                                 System.nanoTime(),
                                 var1 != null,
                                 var2 == this.target,
                                 var2 != null && var2 != this.target,
                                 var1 != null && var1.onGround(),
                                 (boolean)var3
                              );
                           if (var4 == RodAttemptTracker.Action.ABORT) {
                              this.endThrow(var2 == this.target ? "Hit" : "Missed or timed out", true);
                           } else if (var4 == RodAttemptTracker.Action.REEL) {
                              this.status = "Player hooked";
                           }
                        }
                     } else {
                        this.endThrow("Bobber unavailable", true);
                     }
                  }
               } else {
                  this.endThrow("Your input takes priority", true);
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.solveAim();
      boolean var7 = false;
      byte var6 = (byte)var10000;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.endThrow("Paused", true);
      } else {
         int var17 = this.attemptTracker.isActive();
         boolean var11 = false;
         byte var10 = (byte)var17;
         var17 = Integer.signum(var10);
         if (((var17 | -var17) >>> 31 ^ 1) == 0) {
            if (this.attemptTracker.getPhase() == RodAttemptTracker.Phase.HOOKED) {
               InteractionArbiter.queueAction(this, this::endThrow, this::canReel);
            }
         } else {
            AuraModule var1 = AuraModule.getInstance();
            if (var1 != null && var1.isRuntimeActive()) {
               LivingEntity var3 = var1.applyMoveFix();
               int var19 = var3 instanceof Player;
               boolean var9 = false;
               byte var8 = (byte)var19;
               var19 = Integer.signum(var8);
               if (((var19 | -var19) >>> 31 ^ 1) == 0) {
                  Player var2 = (Player)var3;
                  if (this.aimTarget == var2) {
                     long var14 = System.nanoTime();
                     int var21 = this.canThrow(var14);
                     boolean var13 = false;
                     byte var12 = (byte)var21;
                     var21 = Integer.signum(var12);
                     if ((var21 | -var21) >>> 31 == 0) {
                        String var15 = this.getBlockReason(var14);
                        this.status = var15 == null ? "Aiming" : var15;
                     } else if (this.findAimedEntity() != var2) {
                        this.status = "Aiming";
                     } else {
                        int var5 = this.findRodSlot();
                        InteractionArbiter.queueAction(
                           this,
                           () -> this.throwRod(var2, var5),
                           () -> this.canThrow(System.nanoTime())
                              && this.throwRod(var5)
                              && this.isValidTarget(var2)
                              && this.aimTarget == var2
                              && var1.applyMoveFix() == var2
                              && this.findAimedEntity() == var2
                        );
                     }
                  }
               } else {
                  this.resetAim();
                  if (!this.status.equals("Delay")) {
                     this.status = "No player target";
                  }
               }
            } else {
               this.resetAim();
               this.status = "Enable Kill Aura";
            }
         }
      }
   }

   private void throwRod(Player var1, int var2) {
      this.weaponSlot = this.player.getInventory().getSelectedSlot();
      this.weaponStack = this.player.getMainHandItem().copy();
      this.rodSlot = var2;
      this.resetAim();
      if (!this.swapSession.swapTo(this.player, var2)) {
         this.throwCooldown.setValue(System.nanoTime(), 250000000L);
         this.endThrow("Hotbar busy", false);
      } else {
         this.target = var1;
         this.bobber = null;
         this.attemptTracker.startAttempt(System.nanoTime(), this.timeout.getValue().longValue() * 1000000L, this.mode.isMode("Hook and Reel"));
         boolean var3 = minecraft.gameMode.useItem(this.player, InteractionHand.MAIN_HAND).consumesAction();
         if (!var3) {
            this.endThrow("Throw rejected locally", true);
         } else {
            SwingHelper.swingInteract(this.player, InteractionHand.MAIN_HAND);
            ((MinecraftMixin_3)minecraft).larpUseDelay(4);
            this.status = "Thrown";
            this.lastWaitReason = null;
            LarpLog.info(
               "RodSwap",
               "Throw; mode={}, rodSlot={}, returnSlot={}, target={}, distance={}, yaw={}, pitch={}",
               this.mode.getValue(),
               var2 + 1,
               this.weaponSlot + 1,
               this.target.getName().getString(),
               String.format("%.2f", this.distanceToTarget(this.target)),
               String.format("%.1f", this.player.getYRot()),
               String.format("%.1f", this.player.getXRot())
            );
         }
      }
   }

   private boolean canReel() {
      return this.isRuntimeActive()
         && this.solveAim()
         && this.attemptTracker.getPhase() == RodAttemptTracker.Phase.HOOKED
         && this.swapSession.isActive()
         && this.isWeaponSlotIntact()
         && this.throwRod(this.rodSlot)
         && HotbarSwapSession.canSwap(this)
         && CombatUtil.canAttack(minecraft, this.player)
         && !CombatUtil.isUsePressed(minecraft)
         && !CombatUtil.isAttackPressed(minecraft)
         && !CombatUtil.hasTickedThisTick(this.player)
         && ((MinecraftMixin_3)minecraft).larpUseDelay() <= 0
         && this.bobber != null
         && this.player.fishing == this.bobber
         && this.bobber.getHookedIn() == this.target
         && this.isValidTarget(this.target);
   }

   private void endThrow() {
      if (this.canReel()) {
         this.attemptTracker.markReeled();
         if (!minecraft.gameMode.useItem(this.player, InteractionHand.MAIN_HAND).consumesAction()) {
            this.endThrow("Reel rejected locally", true);
         } else {
            SwingHelper.swingInteract(this.player, InteractionHand.MAIN_HAND);
            ((MinecraftMixin_3)minecraft).larpUseDelay(4);
            this.status = "Reeled";
            LarpLog.debug("RodSwap", "Reel requested for confirmed target {}; awaiting bobber removal", this.target.getId());
         }
      }
   }

   private void endThrow(String var1, boolean var2) {
      InteractionArbiter.findAttackBlocker(this);
      int var3 = !this.attemptTracker.isActive() && !this.swapSession.isSwapped() ? 0 : 1;
      RodAttemptTracker.Phase var4 = this.attemptTracker.getPhase();
      int var5 = this.player != null && this.player == getMc().player && this.level == minecraft.level && this.player.isAlive() ? 1 : 0;
      int var6 = var5 != 0 ? this.player.getInventory().getSelectedSlot() : -1;
      this.swapSession.restore(getMc(), var2 && var5 != 0 && this.isWeaponSlotIntact());
      if (var5 != 0 && minecraft.gameMode != null && var6 != this.player.getInventory().getSelectedSlot()) {
         ((MultiPlayerGameModeMixin_6)minecraft.gameMode).larpSyncCarriedItem();
      }

      if (var3 != 0) {
         this.throwCooldown.getFirst(System.nanoTime(), this.delay.getValue().longValue() * 1000000L);
      }

      this.attemptTracker.isActive();
      this.target = null;
      this.bobber = null;
      this.weaponStack = ItemStack.EMPTY;
      this.weaponSlot = -1;
      this.rodSlot = -1;
      this.status = var1;
      if (var3 != 0) {
         LarpLog.info(
            "RodSwap", "Throw ended: {} at {}; weapon slot restored={}", var1, var4, var5 != 0 && var6 != this.player.getInventory().getSelectedSlot()
         );
      }
   }
}
