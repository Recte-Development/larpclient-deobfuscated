package com.larpclient.module.movement;

import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;

public final class ElytraHelperModule extends Module {
   static final int heldFireworkIntervalTicks = RocketBooster.clearActiveModule(180.0);
   static final int rELAUNCH_TIMEOUT_TICKS = 10;
   static final int tAKEOFF_TIMEOUT_TICKS = 3;
   private final KeybindSetting fireworkKey = this.addSetting(new KeybindSetting("Firework Key", "Hold this key to use hotbar fireworks while gliding", -1));
   private final BooleanSetting autoStart = this.addSetting(new BooleanSetting("Auto Start", "Start gliding when falling with an elytra equipped", true));
   private final BooleanSetting fastStart = this.addSetting(new BooleanSetting("Fast Start", "Use a firework on the first automatic takeoff", true));
   private final BooleanSetting combatRelaunch = this.addSetting(
      new BooleanSetting("Combat Relaunch", "Jump and relaunch when landing with an elytra equipped", true)
   );
   private final NumberSetting relaunchDelay = this.addSetting(
      new NumberSetting("Relaunch Delay", "Delay after the relaunch jump in milliseconds", 175.0, 0.0, 1000.0, 25.0)
   );
   private final BooleanSetting relaunchFirework = this.addSetting(new BooleanSetting("Relaunch Firework", "Use a firework after the relaunch", true));
   private final BooleanSetting autoFirework = this.addSetting(new BooleanSetting("Auto Firework", "Use hotbar fireworks when flight speed drops", false));
   private final NumberSetting fireworkDelay = this.addSetting(
      new NumberSetting("Delay", "Minimum time between fireworks in milliseconds", 650.0, 100.0, 2000.0, 25.0)
   );
   private final BooleanSetting onlyWithTarget = this.addSetting(
      new BooleanSetting("Only With Target", "Automatic fireworks require the current Kill Aura target", false)
   );
   private final BooleanSetting onlyWhileGliding = this.addSetting(new BooleanSetting("Only While Gliding", "Automatic fireworks require active gliding", true));
   private final RocketBooster rocketBooster = new RocketBooster(this);
   private LocalPlayer trackedPlayer;
   private ClientLevel trackedLevel;
   private int lastFireworkTick = -1073741824;
   private int relaunchStartTick;
   private int takeoffState;
   private int takeoffDeadlineTick;
   private boolean hasLaunched;
   private boolean relaunching;
   private boolean takeoffFirework;

   public ElytraHelperModule() {
      super("ElytraHelper", "Automate elytra takeoff and fireworks", Category.MOVEMENT, -1);
      this.fastStart.visibleWhen(this.autoStart::getValue);
      this.relaunchDelay.visibleWhen(this.combatRelaunch::getValue);
      this.relaunchFirework.visibleWhen(this.combatRelaunch::getValue);
      this.fireworkDelay.visibleWhen(this.autoFirework::getValue);
      this.onlyWithTarget.visibleWhen(this.autoFirework::getValue);
      this.onlyWhileGliding.visibleWhen(this.autoFirework::getValue);
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      this.reset();
      this.rocketBooster.reset();
      this.trackedPlayer = minecraft.player;
      this.trackedLevel = minecraft.level;
      this.lastFireworkTick = minecraft.player == null ? -1073741824 : minecraft.player.tickCount;
   }

   @Override
   public void onDisable() {
      InteractionArbiter.findAttackBlocker(this);
      this.reset();
      this.rocketBooster.reset();
      this.trackedPlayer = null;
      this.trackedLevel = null;
   }

   private void reset() {
      this.rocketBooster.restoreJump();
      this.takeoffState = 0;
      this.hasLaunched = (boolean)0;
      this.relaunching = (boolean)0;
      this.takeoffFirework = (boolean)0;
   }

   private boolean canRun() {
      return this.isInGame()
         && minecraft.player == this.trackedPlayer
         && minecraft.level == this.trackedLevel
         && PlayerInputUtil.isInWorld()
         && InteractionArbiter.canInteract()
         && !minecraft.player.getAbilities().flying
         && !minecraft.player.isPassenger()
         && !minecraft.player.isInWater()
         && !minecraft.player.isInLava()
         && minecraft.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && !this.tickTakeoff();
   }

   private boolean tickTakeoff() {
      AirStuckModule var1 = AirStuckModule.getInstance();
      ElytraMotionModule var2 = ElytraMotionModule.getInstance();
      return var1 != null && var1.isHolding() || var2 != null && var2.isHolding();
   }

   @Override
   public void onTick() {
      this.rocketBooster.releaseHotbar();
      if (this.canRun() && !RocketBooster.isHotbarClaimedByOther(this)) {
         int var1 = minecraft.player.tickCount;
         if (this.takeoffState != 0) {
            this.tickTakeoff(var1);
         } else if (minecraft.player.onGround()) {
            this.hasLaunched = (boolean)0;
            if (this.relaunching) {
               int var13 = var1 - this.relaunchStartTick;
               byte var10 = 10;
               int var9 = var13;
               if ((-Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
                  this.relaunching = (boolean)0;
               }
            }

            if (this.combatRelaunch.getValue() && !this.relaunching) {
               this.rocketBooster.hasRocket();
               this.relaunchStartTick = var1;
               this.relaunching = (boolean)1;
               LarpLog.debug(
                  "ElytraHelper", "Relaunch jump through the jump key; waiting {} ticks", RocketBooster.clearActiveModule(this.relaunchDelay.getValue())
               );
            }
         } else {
            if (this.relaunching && !this.combatRelaunch.getValue()) {
               this.relaunching = (boolean)0;
            }

            if (this.relaunching) {
               int var12 = var1 - this.relaunchStartTick;
               int var6 = RocketBooster.clearActiveModule(this.relaunchDelay.getValue());
               int var5 = var12;
               if (Integer.compare(var5, var6) >>> 31 == 0) {
                  this.startTakeoff(this.relaunchFirework.getValue());
                  this.relaunching = (boolean)0;
                  this.hasLaunched = (boolean)1;
               }
            } else if (this.autoStart.getValue() && !minecraft.player.isFallFlying() && minecraft.player.fallDistance > 0.0) {
               this.startTakeoff(this.fastStart.getValue() && !this.hasLaunched);
               this.hasLaunched = (boolean)1;
            } else {
               int var2 = minecraft.player.isFallFlying() && BindCodes.isPressed(minecraft.getWindow().handle(), this.fireworkKey.getValue()) ? 1 : 0;
               if (var2 != 0) {
                  int var10000 = var1 - this.lastFireworkTick;
                  int var8 = heldFireworkIntervalTicks;
                  int var7 = var10000;
                  if (Integer.compare(var7, var8) >>> 31 == 0) {
                     this.scheduleFirework(
                        () -> this.canRun()
                           && minecraft.player.isFallFlying()
                           && BindCodes.isPressed(minecraft.getWindow().handle(), this.fireworkKey.getValue())
                     );
                     return;
                  }
               }

               if (this.autoFirework.getValue() && this.needsFirework()) {
                  int var11 = var1 - this.lastFireworkTick;
                  int var4 = RocketBooster.clearActiveModule(this.fireworkDelay.getValue());
                  int var3 = var11;
                  if (Integer.compare(var3, var4) >>> 31 == 0) {
                     this.scheduleFirework(() -> this.canRun() && this.needsFirework());
                  }
               }
            }
         }
      } else {
         InteractionArbiter.findAttackBlocker(this);
         this.reset();
      }
   }

   private void tickTakeoff(int var1) {
      if (var1 > this.takeoffDeadlineTick) {
         InteractionArbiter.findAttackBlocker(this);
         this.reset();
         LarpLog.debug("ElytraHelper", "Takeoff timed out; released jump input");
      } else {
         if (this.takeoffState == 2) {
            if (minecraft.player.onGround()) {
               this.reset();
               return;
            }

            this.rocketBooster.hasRocket();
            this.takeoffState = 3;
         } else {
            boolean var2 = minecraft.player.isFallFlying();
            LarpLog.debug("ElytraHelper", "Takeoff {}; firework={}", var2 ? "started" : "did not start", this.takeoffFirework && var2);
            if (var2 && this.takeoffFirework) {
               this.scheduleFirework(() -> this.canRun() && minecraft.player.isFallFlying());
            }

            this.takeoffState = 0;
            this.takeoffFirework = (boolean)0;
         }
      }
   }

   private void scheduleFirework(BooleanSupplier var1) {
      this.rocketBooster.scheduleBoost(var1, 4.0, 0, () -> this.lastFireworkTick = minecraft.player.tickCount);
   }

   private void startTakeoff(boolean var1) {
      this.rocketBooster.setJump();
      this.takeoffFirework = var1;
      this.takeoffState = 2;
      this.takeoffDeadlineTick = minecraft.player.tickCount + 3;
   }

   private boolean needsFirework() {
      if (this.onlyWhileGliding.getValue() && !minecraft.player.isFallFlying()) {
         return false;
      }

      LivingEntity var1 = getAuraTarget();
      if (this.onlyWithTarget.getValue() && var1 == null) {
         return false;
      }

      double var2 = minecraft.player.getDeltaMovement().horizontalDistance() * 20.0;
      return var1 == null ? var2 <= 22.0 : !(minecraft.player.distanceTo(var1) < 5.0F) || !(var2 > var1.getDeltaMovement().horizontalDistance() * 20.0 * 0.9);
   }

   static LivingEntity getAuraTarget() {
      AuraModule var0 = AuraModule.getInstance();
      LivingEntity var1 = var0 == null ? null : var0.applyMoveFix();
      return var1 != null && var1.isAlive() && !var1.isRemoved() && var1.level() == getMc().level ? var1 : null;
   }

   @Override
   public String getStatus() {
      if (this.takeoffState != 0) {
         return "Takeoff";
      }

      if (this.relaunching) {
         return "Relaunch";
      }

      LocalPlayer var1 = minecraft == null ? null : minecraft.player;
      return var1 != null && var1 == this.trackedPlayer && this.autoFirework.getValue() && var1.isFallFlying() && !this.rocketBooster.hasRocket()
         ? "No rockets"
         : "";
   }
}
