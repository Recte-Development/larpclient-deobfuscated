package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.core.ViewController;
import com.larpclient.hud.KeystrokesHud;
import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.movement.ElytraMotionModule;
import com.larpclient.module.movement.FlyModule;
import com.larpclient.module.world.AutoTunnelModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.MovementInputs;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public final class WTapModule extends Module {
   private static WTapModule instance;
   private final BooleanSetting playersOnly = this.addSetting(new BooleanSetting("Players Only", "Only release forward after attacking a player", true));
   private final BooleanSetting weaponsOnly = this.addSetting(new BooleanSetting("Weapons Only", "Require a held melee weapon", true));
   private final NumberSetting releaseDuration = this.addSetting(
      new NumberSetting("Release Duration", "Forward release in native input ticks", 1.0, 1.0, 3.0, 1.0)
   );
   private final MovementInputs.BurstCounter releaseBurst = new MovementInputs.BurstCounter();
   private final MovementInputs.PendingConfirm pendingAttack = new MovementInputs.PendingConfirm();
   private LocalPlayer player;
   private Object level;
   private String status = "Waiting for a sprint attack";

   public WTapModule() {
      super("W Tap", "Briefly release forward after a charged sprint attack", Category.COMBAT, -1);
      instance = this;
   }

   public static void onAttack(Player var0, Entity var1) {
      if (instance != null && instance.isEnabled()) {
         WTapModule var2 = instance;
         if (var2 != null && var0 == getMc().player) {
            var2.pendingAttack.clear();
            if (var2.isRuntimeActive() && var0 == getMc().player && var0 instanceof LocalPlayer var3) {
               int var4 = !InteractionArbiter.isAttacking()
                     && minecraft.options.keyAttack.isDown()
                     && minecraft.hitResult instanceof EntityHitResult var5
                     && var5.getEntity() == var1
                  ? 1
                  : 0;
               boolean var7 = var2.onAttack(var1);
               boolean var8 = MovementInputs.canSprint(
                  (boolean)var4,
                  var2.canAct(var3),
                  var3.isSprinting(),
                  minecraft.options.keyUp.isDown(),
                  minecraft.options.keyDown.isDown(),
                  minecraft.options.keyShift.isDown(),
                  var3.getAttackStrengthScale(0.5F),
                  var7
               );
               var2.pendingAttack.arm(var0, var1, var8);
               if (var8) {
                  var2.player = var3;
                  var2.level = minecraft.level;
               }
            }
         }
      }
   }

   public static void onAttackConfirmed(Player var0, Entity var1) {
      if (instance != null && instance.isEnabled()) {
         WTapModule var2 = instance;
         if (var2 != null) {
            var2.pendingAttack.confirm(var0, var1);
         }
      }
   }

   public static void onAttackExecuted(Player var0, Entity var1) {
      if (instance != null && instance.isEnabled()) {
         WTapModule var2 = instance;
         if (var2 != null && var0 == getMc().player) {
            if (var2.pendingAttack.consume(var0, var1) && var2.isRuntimeActive() && var0 == var2.player && var2.canAct(var2.player)) {
               var2.releaseBurst.arm(var2.releaseDuration.getValue().intValue());
               var2.status = "Forward release queued";
               LarpLog.debug("WTap", "Executed manual sprint attack; release queued for {} input ticks", var2.releaseDuration.getValue().intValue());
            }
         }
      }
   }

   public static Input modifyInput(LocalPlayer var0, Input var1) {
      if (instance != null && instance.isEnabled()) {
         WTapModule var2 = instance;
         if (var2 == null || !var2.isRuntimeActive()) {
            return var1;
         } else if (var2.player != var0 || var2.level != getMc().level || !var2.canAct(var0) || !var1.forward() || var1.backward() || var1.shift()) {
            var2.cancelRelease("Input or movement context changed");
            return var1;
         } else if (!var2.releaseBurst.update(var0.tickCount, true)) {
            var2.status = "Waiting for a sprint attack";
            return var1;
         } else {
            var2.status = "Releasing forward";
            KeystrokesHud.recordForwardSuppress();
            var0.setSprinting(false);
            Input var3 = MovementInputs.withoutForward(var1);
            var0.input.moveVector = MovementInputs.getMovementVector(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   public static boolean isReleasingForward() {
      if (instance != null && instance.isEnabled()) {
         WTapModule var0 = instance;
         Minecraft var1 = getMc();
         if (var0 == null || !var0.isRuntimeActive() || var1.player == null) {
            return false;
         }

         if (var0.player == var1.player && var0.level == var1.level && var0.canAct(var1.player)) {
            return var0.releaseBurst.isActiveFor(var1.player.tickCount);
         }

         var0.cancelRelease("Sprint ownership changed");
         return false;
      } else {
         return false;
      }
   }

   private boolean canAct(LocalPlayer var1) {
      if (var1 != null
         && var1 == getMc().player
         && PlayerInputUtil.isInWorld()
         && InteractionArbiter.getPriority()
         && minecraft.isWindowActive()
         && ViewController.update() == ViewController.Owner.NONE
         && !var1.isUsingItem()
         && !var1.isInWater()
         && !var1.isInLava()
         && !var1.onClimbable()
         && !var1.isPassenger()
         && !var1.getAbilities().flying
         && !var1.isFallFlying()
         && !var1.isSleeping()
         && !var1.isMobilityRestricted()
         && !var1.noPhysics
         && !var1.isCrouching()
         && !var1.isShiftKeyDown()
         && AutoTunnelModule.isActive() == null) {
         AirStuckModule var2 = AirStuckModule.getInstance();
         ElytraMotionModule var3 = ElytraMotionModule.getInstance();
         FlyModule var4 = FlyModule.getInstance();
         return (var2 == null || !var2.isHolding())
            && (var3 == null || !var3.isHolding())
            && (var4 == null || !var4.isFlightActive())
            && !AuraModule.isSprintResetActive();
      } else {
         return false;
      }
   }

   private boolean onAttack(Entity var1) {
      if (!(
         var1 instanceof LivingEntity var2
            && var1 != minecraft.player
            && var2.isAlive()
            && !var1.isRemoved()
            && !var1.isSpectator()
            && var1.level() == minecraft.level
            && !var1.isInvisible()
            && !minecraft.player.isAlliedTo(var1)
            && (!this.playersOnly.getValue() || var1 instanceof Player)
            && minecraft.player.hasLineOfSight(var1)
            && !(InteractionArbiter.claimManualInteraction(var1) > minecraft.player.entityInteractionRange() + 1.0E-7)
      )) {
         return false;
      } else {
         if (this.weaponsOnly.getValue() && !isMeleeWeapon(minecraft.player)) {
            return false;
         }

         LarpClient var3 = LarpClient.getInstance();
         MCFriendModule var4 = var3 != null && var3.getModuleManager() != null ? var3.getModuleManager().getModule(MCFriendModule.class) : null;
         return !(var1 instanceof Player) || var4 == null || !var4.isFriend(var1.getName().getString());
      }
   }

   public static boolean isMeleeWeapon(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return !var1.isEmpty() && (var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.MELEE_WEAPON_ENCHANTABLE));
   }

   private void cancelRelease(String var1) {
      if (this.releaseBurst.isPending()) {
         LarpLog.debug("WTap", "Release cancelled: {}; native input resumes", var1);
      }

      this.releaseBurst.reset();
      this.pendingAttack.clear();
      this.status = "Waiting for a sprint attack";
   }

   public static void onPositionCorrection(LocalPlayer var0) {
      WTapModule var1 = instance;
      if (var1 != null && var1.player == var0) {
         var1.cancelRelease("Position correction");
      }
   }

   @Override
   public void onEnable() {
      this.cancelRelease("Activated");
   }

   @Override
   public void onDisable() {
      this.cancelRelease("Disabled or disconnected");
      this.player = null;
      this.level = null;
   }

   public static void validateContext() {
      WTapModule var0 = instance;
      if (var0 != null) {
         if (!var0.isRuntimeActive()) {
            var0.cancelRelease("Runtime unavailable");
         } else {
            if (var0.player != null && (var0.player != getMc().player || var0.level != minecraft.level || !var0.canAct(minecraft.player))) {
               var0.cancelRelease("Context unavailable");
            }
         }
      }
   }

   @Override
   public void onTick() {
      validateContext();
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }

   @Override
   public String getStatus() {
      return this.status;
   }
}
