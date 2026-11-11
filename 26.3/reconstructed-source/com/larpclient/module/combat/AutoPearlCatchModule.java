package com.larpclient.module.combat;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.MinecraftMixin_3;
import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.render.world.PearlTrajectory;
import com.larpclient.util.entity.PearlCatchState;
import com.larpclient.util.entity.RotationController;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import com.larpclient.util.world.WorldRaycaster;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.phys.Vec3;

public final class AutoPearlCatchModule extends Module {
   private static final int cATCH_DEADLINE_TICKS = 20;
   private static final int mAX_SOLVE_DELAY = 20;
   private static final int oUTCOME_GRACE_TICKS = 20;
   private static final int cATCH_TOLERANCE = 3;
   private static final int mAX_PENDING_THROWS = 4;
   private static final double mIN_RELIABILITY = 0.6;
   private static final double pEARL_HIT_DAMAGE = 5.0;
   private static final float aIM_BOOST = 0.05F;
   public static final float aIM_TOLERANCE_DEGREES = 2.5F;
   private static AutoPearlCatchModule instance;
   private final KeybindSetting catchBind = this.addSetting(
      new KeybindSetting("Catch Bind", "Press for one pearl catch; pressing again while it throws cancels it. Separate from the module toggle", -1)
   );
   private final ModeSetting direction = this.addSetting(
      new ModeSetting("Direction", "Up throws straight up for height; Look throws where you aim for distance", "Up", "Up", "Look")
   );
   private final ModeSetting timing = this.addSetting(
      new ModeSetting(
         "Timing",
         "Instant throws both in one tick for a catch a few blocks out; Solved aims the wind charge a tick or more later for a farther catch; Fixed Delay throws it along the same look after Delay",
         "Instant",
         "Instant",
         "Solved",
         "Fixed Delay"
      )
   );
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Ticks from the pearl to the wind charge; same-look catches work near straight up", 2.0, 1.0, 10.0, 1.0)
   );
   private final NumberSetting turnSpeed = this.addSetting(
      new NumberSetting(
         "Turn Speed", "Maximum player turn in degrees per tick; your camera stays on your look. 180 with Humanize off turns at once", 90.0, 10.0, 180.0, 5.0
      )
   );
   private final BooleanSetting swapBack = this.addSetting(new BooleanSetting("Swap Back", "Return to your previous hotbar slot after each throw", true));
   private final NumberSetting returnSpeed = this.addSetting(
      new NumberSetting("Return Speed", "Most the player turns per tick back to your look after the last throw. 180 returns at once", 40.0, 10.0, 180.0, 5.0)
   );
   private final BooleanSetting humanize = this.addSetting(
      new BooleanSetting(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final RotationController rotationController = new RotationController();
   private final HotbarSwapSession hotbarSwap = new HotbarSwapSession(this);
   private final PearlCatchState catchState = new PearlCatchState();
   private final Set<Integer> knownPearlIds = new HashSet<>();
   private final List<AutoPearlCatchModule.PendingThrow> pendingThrows = new ArrayList<>();
   private LocalPlayer player;
   private ClientLevel level;
   private long tickCounter;
   private long lastThrowTick = Long.MIN_VALUE;
   private long simulationTick = -1L;
   private float[] savedLook;
   private float previousPearlCooldown;
   private float previousWindChargeCooldown;
   private List<PearlTrajectory.Simulation> cachedSimulations;
   private Vec3 lastPosition;
   private Vec3 velocity = Vec3.ZERO;
   private Vec3 landingPoint;
   private Vec3 throwOrigin = Vec3.ZERO;
   private Vec3 pearlThrowEye = Vec3.ZERO;
   private Vec3 pearlThrowDirection = Vec3.ZERO;
   private Vec3 pearlThrowVelocity = Vec3.ZERO;
   private float targetYaw;
   private float targetPitch;
   private ThrownEnderpearl trackedPearl;
   private PearlTrajectory.Plan plan;
   private PearlTrajectory.Plan windChargePlan;
   private String status = "Set a Catch Bind";

   public AutoPearlCatchModule() {
      super("Auto Pearl Catch", "Throw a pearl, then a wind charge it hits in flight, to teleport mid-air", Category.MOVEMENT, -1);
      instance = this;
      this.delay.visibleWhen(this::isFixedDelayTiming);
      this.direction.onChange(var1 -> this.cancel("Direction changed"));
      this.timing.onChange(var1 -> this.cancel("Timing changed"));
      this.catchBind.onChange(var1 -> this.cancel("Bind changed"));
   }

   public static AutoPearlCatchModule getInstance() {
      return instance;
   }

   public List<Setting<?>> aimToward() {
      return List.of(this.catchBind, this.direction, this.timing, this.delay);
   }

   public List<Setting<?>> getTurnSettings() {
      return List.of(this.turnSpeed, this.returnSpeed, this.humanize, this.swapBack);
   }

   public RotationController getRotationController() {
      return this.rotationController;
   }

   public double getReturnSpeed() {
      return this.returnSpeed.getValue();
   }

   public boolean isHumanize() {
      return this.humanize.getValue();
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   public boolean isActive() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.player != null
            && this.player == minecraft.player
            && this.level == minecraft.level
            && (this.catchState.isActive() || this.tickCounter == this.lastThrowTick);
   }

   public static void onCatchKey(int var0) {
      if (instance != null && instance.isEnabled()) {
         AutoPearlCatchModule var1 = instance;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.catchBind.getValue()) {
            if (!var1.catchState.requestStart()) {
               var1.cancel("Cancelled with Catch Bind");
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.cancel("Waiting for a world");
         this.pendingThrows.clear();
      } else {
         if (this.player != minecraft.player || this.level != minecraft.level) {
            float[] var15;
            label69: {
               if (!this.catchState.isActive()) {
                  long var21;
                  int var10000 = (var21 = this.tickCounter - this.lastThrowTick) == 0L ? 0 : (var21 < 0L ? -1 : 1);
                  boolean var12 = false;
                  int var11 = var10000;
                  int var14 = Integer.signum(var11);
                  if ((var14 | -var14) >>> 31 != 0) {
                     var15 = null;
                     break label69;
                  }
               }

               var15 = this.savedLook;
            }

            float[] var1 = var15;
            this.cancel("World changed");
            this.pendingThrows.clear();
            this.player = minecraft.player;
            this.level = minecraft.level;
            this.lastPosition = null;
            if (var1 != null) {
               this.player.setYRot(var1[0]);
               this.player.setXRot(var1[1]);
               this.player.yRotO = var1[0];
               this.player.xRotO = var1[1];
               LarpLog.info("PearlCatch", "World changed during a catch; your look (yaw {} pitch {}) restored", aimToward(var1[0]), aimToward(var1[1]));
            }
         }

         this.tickCounter++;
         if (ViewController.update() == ViewController.Owner.PEARL_CATCH) {
            this.savedLook = ViewController.toPlayerRotation(
               ViewController.getRawYaw(), ViewController.getPosition(), minecraft.options.getCameraType().isMirrored()
            );
         }

         Vec3 var13 = this.player.position();
         this.velocity = this.lastPosition != null && !(var13.distanceToSqr(this.lastPosition) > 16.0) ? var13.subtract(this.lastPosition) : Vec3.ZERO;
         this.lastPosition = var13;
         this.updatePendingThrows();
         byte var2 = this.catchState.consumeStartRequest();
         PearlCatchState.Phase var3 = this.catchState.getPhase();
         if (var3 == PearlCatchState.Phase.IDLE) {
            boolean var6 = false;
            byte var5 = var2;
            int var16 = Integer.signum(var5);
            if (((var16 | -var16) >>> 31 ^ 1) == 0) {
               int var17 = this.catchState.isWaiting();
               boolean var8 = false;
               byte var7 = (byte)var17;
               var17 = Integer.signum(var7);
               if ((var17 | -var17) >>> 31 == 0) {
                  this.previousPearlCooldown = this.previousWindChargeCooldown = 0.0F;
                  this.catchState.beginWait(this.tickCounter);
               }
            }

            int var19 = this.catchState.isWaiting();
            boolean var10 = false;
            byte var9 = (byte)var19;
            var19 = Integer.signum(var9);
            if (((var19 | -var19) >>> 31 ^ 1) == 0) {
               this.tickWaiting();
            }
         } else {
            String var4 = this.getPauseReason();
            if (var4 != null) {
               this.cancel(var4);
            } else if (this.catchState.isDeadlineReached(this.tickCounter)) {
               this.cancel(
                  var3 == PearlCatchState.Phase.PEARL_THROWN
                     ? "The view did not reach the throw angle in time"
                     : (this.isFixedDelayTiming() ? "The wind charge could not be thrown on its tick" : "No reliable catch before the pearl came down")
               );
            } else {
               if (var3 == PearlCatchState.Phase.PEARL_THROWN) {
                  this.tickPearlThrown();
               } else {
                  this.tickWindCharge();
               }
            }
         }
      }
   }

   private void tickWaiting() {
      String var1 = this.getPauseReason();
      if (var1 == null) {
         var1 = this.getInventoryBlocker();
      }

      if (var1 != null) {
         this.catchState.clearWait();
         this.refuse(var1);
      } else {
         int var2 = this.getCooldownTicks();
         if (var2 > 0) {
            if (var2 <= 30 && !this.catchState.hasWaitTimedOut(this.tickCounter)) {
               this.status = String.format(Locale.ROOT, "Catch in %.2f s (cooldown)", var2 / 20.0);
            } else {
               this.catchState.clearWait();
               this.refuse(String.format(Locale.ROOT, "Ender pearl or wind charge is cooling down (%.1f s left)", var2 / 20.0));
            }
         } else {
            this.catchState.clearWait();
            this.startCatch();
         }
      }
   }

   private int getCooldownTicks() {
      float var1 = this.getCooldownPercent(Items.ENDER_PEARL);
      float var2 = this.getCooldownPercent(Items.WIND_CHARGE);
      int var3 = Math.max(
         cooldownTicksRemaining(var1, this.previousPearlCooldown, Items.ENDER_PEARL),
         cooldownTicksRemaining(var2, this.previousWindChargeCooldown, Items.WIND_CHARGE)
      );
      this.previousPearlCooldown = var1;
      this.previousWindChargeCooldown = var2;
      return var3;
   }

   private float getCooldownPercent(Item var1) {
      int var2 = NativePlacement.findItemSlot(var1, true);
      if (var2 == -1) {
         return 0.0F;
      }

      ItemStack var3 = var2 == -2 ? this.player.getOffhandItem() : this.player.getInventory().getItem(var2);
      return this.player.getCooldowns().getCooldownPercent(var3, 0.0F);
   }

   private static int cooldownTicksRemaining(float var0, float var1, Item var2) {
      if (var0 <= 0.0F) {
         return 0;
      }

      float var3 = var1 - var0;
      if (var3 > 0.0F) {
         return (int)Math.ceil(var0 / var3);
      }

      UseCooldown var4 = (UseCooldown)new ItemStack(var2).get(DataComponents.USE_COOLDOWN);
      return (int)Math.ceil(var0 * (var4 == null ? 20 : var4.ticks()));
   }

   private void startCatch() {
      String var1 = this.getPauseReason();
      if (var1 == null) {
         var1 = this.getInventoryBlocker();
      }

      if (var1 != null) {
         this.refuse(var1);
      } else {
         float[] var2 = this.getPlayerLook();
         float var3 = var2[0];
         float var4 = var2[1];
         float var5 = this.isUpDirection() ? -90.0F : var4;
         Vec3 var6 = this.player.getEyePosition();
         PearlTrajectory.Plan var7;
         if (this.isInstantTiming()) {
            PearlTrajectory.Launch var8 = PearlTrajectory.findLaunch(var6, var3, var5, this.getVelocity(), this.createWorld(), 0.6);
            if (var8 == null) {
               this.refuse(this.isUpDirection() ? "No instant catch: clear the space above you" : "No instant catch along this look; clear the space ahead");
               return;
            }

            var5 = var8.pitch();
            var7 = var8.plan();
         } else {
            Vec3 var11 = PearlTrajectory.lookVector(var3, var5);
            var7 = this.isFixedDelayTiming()
               ? PearlTrajectory.buildPlan(
                  this.simulateAfter(var6, var11, this.getVelocity(), this.delay.getValue().intValue()), var6, this.getVelocity(), var11, this.createWorld()
               )
               : this.getInventoryBlocker(var6, var11, this.getVelocity());
            if (var7 == null || var7.reliability() < 0.6) {
               this.refuse(this.getReturnSpeed(var7));
               return;
            }
         }

         this.targetYaw = var3;
         this.targetPitch = var5;
         this.plan = var7;
         this.knownPearlIds.clear();

         for (Entity var9 : minecraft.level.entitiesForRendering()) {
            if (var9 instanceof ThrownEnderpearl var10 && var10.getOwner() == this.player) {
               this.knownPearlIds.add(var10.getId());
            }
         }

         this.catchState.beginPearlThrow(this.tickCounter, 20);
         this.rotationController.clampStep();
         this.status = this.isActive(var3, var5) ? "Throwing" : "Turning to throw";
         LarpLog.info(
            "PearlCatch",
            "Catch started; direction={}, timing={}, pitch {}{}, forecast catch on step {} at {} ({}/{} samples)",
            this.direction.getValue(),
            this.timing.getValue(),
            aimToward(var5),
            !this.isUpDirection() && var5 < var4 - 0.5F ? " raised from " + aimToward(var4) : "",
            var7.nominal().tick(),
            formatPoint(var7.nominal().point()),
            var7.score(),
            var7.samples()
         );
         this.tickPearlThrown();
      }
   }

   private PearlTrajectory.Plan getInventoryBlocker(Vec3 var1, Vec3 var2, Vec3 var3) {
      PearlTrajectory.Plan var4 = null;

      for (int var5 = 1; var5 <= 20; var5++) {
         PearlTrajectory.Plan var6 = PearlTrajectory.solveDirection(this.simulateAfter(var1, var2, var3, var5), var1, var3, this.createWorld());
         if (var6 != null && var6.reliability() >= 0.6) {
            return var6;
         }

         if (var6 != null && (var4 == null || var6.score() > var4.score())) {
            var4 = var6;
         }
      }

      return var4;
   }

   private List<PearlTrajectory.Simulation> simulateAfter(Vec3 var1, Vec3 var2, Vec3 var3, int var4) {
      return PearlTrajectory.simulateJitteredAfter(var1, var2, var3, var4, this.createWorld());
   }

   private String getReturnSpeed(PearlTrajectory.Plan var1) {
      if (this.isFixedDelayTiming() && !this.isUpDirection()) {
         return "Fixed Delay only catches near straight up; use Instant or Solved for Look";
      } else if (var1 == null) {
         return this.isUpDirection()
            ? "No catch: the pearl would come down first, so clear the space above you"
            : "No catch at this angle; aim higher or use Instant";
      } else {
         return String.format(
            Locale.ROOT,
            "Only %d of %d samples catch here; %s",
            var1.score(),
            var1.samples(),
            this.isFixedDelayTiming() ? "try another Delay or Instant" : (this.isUpDirection() ? "clear the space above you" : "aim higher or use Instant")
         );
      }
   }

   private void tickPearlThrown() {
      if (InteractionArbiter.ownsInteraction(this)) {
         this.aimToward(this.targetYaw, this.targetPitch);
      }

      InteractionArbiter.queueAction(this, this.isInstantTiming() ? this::throwInstantPair : this::isFixedDelayTiming, this::getInstance);
   }

   private boolean getInstance() {
      if (!this.isRuntimeActive()
         || this.catchState.getPhase() != PearlCatchState.Phase.PEARL_THROWN
         || this.getPauseReason() != null
         || (this.isInstantTiming() ? !this.getRotationController(this.targetYaw, this.targetPitch, 2.5F) : !this.isActive(this.targetYaw, this.targetPitch))
         || !this.isActive(Items.ENDER_PEARL)
         || !this.isActive(Items.WIND_CHARGE)
         || !this.cooldownTicksRemaining(Items.ENDER_PEARL)
         || AuraModule.isShieldUnpressActive()) {
         return false;
      } else if (!this.isInstantTiming()) {
         return true;
      } else if (!this.cooldownTicksRemaining(Items.WIND_CHARGE)) {
         return false;
      } else {
         Vec3 var1 = this.player.getEyePosition();
         Vec3 var2 = PearlTrajectory.lookVector(this.player.getYRot(), this.player.getXRot());
         PearlTrajectory.Plan var3 = PearlTrajectory.buildPlan(
            PearlTrajectory.simulateJittered(var1, var2, this.getVelocity(), this.createWorld()), var1, this.getVelocity(), var2, this.createWorld()
         );
         if (var3 != null && !(var3.reliability() < 0.6)) {
            this.windChargePlan = var3;
            return true;
         } else {
            return false;
         }
      }
   }

   private void throwInstantPair() {
      PearlTrajectory.Plan var1 = this.windChargePlan;
      this.throwOrigin = this.player.position();
      byte var2 = 0;
      byte var3 = 0;

      try {
         var7 = this.useItem(Items.ENDER_PEARL);
         if (var7) {
            var3 = this.useItem(Items.WIND_CHARGE);
         }
      } finally {
         this.restoreHotbar();
      }

      if (!var7) {
         this.cancel("The pearl throw was rejected");
      } else if (var3 == 0) {
         this.cancel("The wind charge was rejected after the pearl, so the pearl flies on");
      } else {
         this.plan = var1;
         this.lastThrowTick = this.tickCounter;
         this.catchState.beginWindCharge(this.tickCounter, 1);
         this.catchState.hasWaitTimedOut();
         this.recordPendingThrow(var1);
         this.status = "Pearl and wind charge thrown together";
         LarpLog.info(
            "PearlCatch",
            "Instant pair thrown at yaw {} pitch {} from {} after a turn of {}; expected catch on step {} at {} ({}/{} samples)",
            aimToward(this.player.getYRot()),
            aimToward(this.player.getXRot()),
            formatPoint(this.player.getEyePosition()),
            this.rotationController.summarize(),
            var1 == null ? "?" : var1.nominal().tick(),
            var1 == null ? "?" : formatPoint(var1.nominal().point()),
            var1 == null ? 0 : var1.score(),
            var1 == null ? 0 : var1.samples()
         );
      }
   }

   private void isFixedDelayTiming() {
      this.throwOrigin = this.player.position();
      Vec3 var1 = this.player.getEyePosition();
      Vec3 var2 = this.getVelocity();
      if (!this.useItemAndRestore(Items.ENDER_PEARL)) {
         this.cancel("The pearl throw was rejected");
      } else {
         this.targetYaw = this.player.getYRot();
         this.targetPitch = this.player.getXRot();
         this.pearlThrowEye = var1;
         this.pearlThrowDirection = PearlTrajectory.lookVector(this.targetYaw, this.targetPitch);
         this.pearlThrowVelocity = var2;
         this.catchState.beginWindCharge(this.tickCounter, this.isFixedDelayTiming() ? this.delay.getValue().intValue() + 1 : 21);
         this.status = this.isFixedDelayTiming()
            ? "Pearl thrown; wind charge in " + this.delay.getValue().intValue() + " ticks"
            : "Pearl thrown; aiming the wind charge";
         LarpLog.info(
            "PearlCatch",
            "Pearl thrown at yaw {} pitch {} from {} after a turn of {}",
            aimToward(this.targetYaw),
            aimToward(this.targetPitch),
            formatPoint(var1),
            this.rotationController.summarize()
         );
         this.rotationController.clampStep();
      }
   }

   private void tickWindCharge() {
      this.refuse();
      if (this.trackedPearl != null && this.trackedPearl.isRemoved()) {
         this.cancel("The pearl came down before the wind charge");
      } else if (this.isFixedDelayTiming()) {
         if (InteractionArbiter.ownsInteraction(this)) {
            this.aimToward(this.targetYaw, this.targetPitch);
         }

         if (this.catchState.isActive(this.tickCounter) >= this.delay.getValue().intValue()) {
            InteractionArbiter.queueAction(this, this::throwWindCharge, this::restoreHotbar);
         }
      } else {
         List var1 = this.simulateAfter();
         if (this.landingPoint == null) {
            this.landingPoint = ((PearlTrajectory.Simulation)var1.get(0)).impact();
         }

         PearlTrajectory.Plan var2 = PearlTrajectory.solveDirection(var1, this.player.getEyePosition(), this.getVelocity(), this.createWorld());
         if (var2 != null && !(var2.reliability() < 0.6)) {
            this.plan = var2;
            float[] var3 = RotationManager.calculateAngles(Vec3.ZERO, var2.direction());
            this.targetYaw = var3[0];
            this.targetPitch = Mth.clamp(var3[1], -90.0F, 90.0F);
            if (InteractionArbiter.ownsInteraction(this)) {
               this.aimToward(this.targetYaw, this.targetPitch);
            }

            InteractionArbiter.queueAction(this, this::throwWindCharge, this::restoreHotbar);
            this.status = "Aiming wind charge";
         } else {
            this.status = "Waiting for a reliable catch line";
         }
      }
   }

   private boolean restoreHotbar() {
      if (this.isRuntimeActive()
         && this.catchState.getPhase() == PearlCatchState.Phase.WIND_CHARGE
         && this.getPauseReason() == null
         && this.isActive(Items.WIND_CHARGE)
         && this.cooldownTicksRemaining(Items.WIND_CHARGE)
         && !AuraModule.isShieldUnpressActive()) {
         Vec3 var1 = PearlTrajectory.lookVector(this.player.getYRot(), this.player.getXRot());
         PearlTrajectory.Plan var2 = PearlTrajectory.buildPlan(this.simulateAfter(), this.player.getEyePosition(), this.getVelocity(), var1, this.createWorld());
         if (this.isFixedDelayTiming()) {
            this.windChargePlan = var2;
            return this.catchState.isActive(this.tickCounter) == this.delay.getValue().intValue() && this.isActive(this.targetYaw, this.targetPitch);
         } else if (var2 != null && !(var2.reliability() < 0.6)) {
            this.windChargePlan = var2;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void throwWindCharge() {
      PearlTrajectory.Plan var1 = this.windChargePlan;
      long var2 = this.catchState.isActive(this.tickCounter);
      if (!this.useItemAndRestore(Items.WIND_CHARGE)) {
         this.cancel("The wind charge throw was rejected");
      } else {
         this.plan = var1;
         this.lastThrowTick = this.tickCounter;
         this.catchState.hasWaitTimedOut();
         this.recordPendingThrow(var1);
         this.status = "Wind charge thrown";
         if (var1 == null) {
            LarpLog.info("PearlCatch", "Wind charge thrown {} ticks after the pearl; no catch expected along this look", var2);
         } else {
            LarpLog.info(
               "PearlCatch",
               "Wind charge thrown {} ticks after the pearl at yaw {} pitch {}; expected catch on step {} at {} ({}/{} samples){}",
               var2,
               aimToward(this.player.getYRot()),
               aimToward(this.player.getXRot()),
               var1.nominal().tick(),
               formatPoint(var1.nominal().point()),
               var1.score(),
               var1.samples(),
               var1.nominal().burst() ? ", charge bursts on you" : ""
            );
         }
      }
   }

   private void recordPendingThrow(PearlTrajectory.Plan var1) {
      if (this.pendingThrows.size() >= 4) {
         this.pendingThrows.remove(0);
      }

      int var2 = (var1 == null ? 60 : var1.nominal().tick()) + this.refuse() + 20;
      AutoPearlCatchModule.PendingThrow var3 = new AutoPearlCatchModule.PendingThrow(
         new HashSet<>(this.knownPearlIds), this.throwOrigin, var1, this.landingPoint, this.tickCounter, this.tickCounter + var2
      );
      var3.pearl = this.trackedPearl;
      this.pendingThrows.add(var3);
      this.trackedPearl = null;
      this.knownPearlIds.clear();
      this.landingPoint = null;
   }

   private void updatePendingThrows() {
      Iterator var1 = this.pendingThrows.iterator();

      while (var1.hasNext()) {
         AutoPearlCatchModule.PendingThrow var2 = (AutoPearlCatchModule.PendingThrow)var1.next();
         if (var2.pearl == null) {
            var2.pearl = this.findNewPearl(var2.knownPearlIds);
         }

         ThrownEnderpearl var3 = var2.pearl;
         if (var2.removedTick < 0L && var3 != null && (var3.isRemoved() || minecraft.level.getEntity(var3.getId()) != var3)) {
            var2.removedTick = this.tickCounter;
         }

         if (var2.landingPoint == null && !var2.landingSimulated && var2.removedTick < 0L && var3 != null) {
            var2.landingPoint = PearlTrajectory.simulate(
                  new PearlTrajectory.PearlState(var3.position(), var3.getDeltaMovement(), 1 + var3.tickCount), this.createWorld()
               )
               .impact();
            var2.landingSimulated = (boolean)1;
         }

         if (var2.removedTick >= 0L && this.tickCounter > var2.removedTick) {
            this.onCatchKey(var2);
            var1.remove();
         } else if (this.tickCounter - var2.deadlineTick >= 0L) {
            String var4 = var3 == null ? "Pearl not seen; the outcome is unknown" : "No catch observed in time";
            LarpLog.info("PearlCatch", "{}, {} ticks after the wind charge", var4, this.tickCounter - var2.throwTick);
            if (!this.catchState.isActive()) {
               this.status = var4;
            }

            var1.remove();
         }
      }
   }

   private ThrownEnderpearl findNewPearl(Set<Integer> var1) {
      for (Entity var3 : minecraft.level.entitiesForRendering()) {
         if (var3 instanceof ThrownEnderpearl var4
            && !var4.isRemoved()
            && var4.getOwner() == this.player
            && !var1.contains(var4.getId())
            && this.pendingThrows.stream().noneMatch(var1x -> var1x.pearl == var4)) {
            return var4;
         }
      }

      return null;
   }

   private void onCatchKey(AutoPearlCatchModule.PendingThrow var1) {
      Vec3 var2 = this.player.position();
      Vec3 var3 = var1.plan == null ? null : var1.plan.nominal().point();
      double var4 = var3 == null ? Double.NaN : var2.distanceTo(var3);
      int var6 = var2.distanceTo(var1.throwOrigin) > 1.0 ? 1 : 0;
      String var7;
      if (var1.landingPoint != null && var2.distanceTo(var1.landingPoint) <= 1.0) {
         var7 = "Missed: the pearl landed";
      } else if (var6 == 0 || var3 != null && !(var4 <= 3.0)) {
         if (var6 != 0) {
            var7 = String.format(
               Locale.ROOT,
               "Caught %.1f blocks out, %.1f up, %.1f blocks from the predicted point (a tick of server timing)",
               var2.distanceTo(var1.throwOrigin),
               var2.y - var1.throwOrigin.y,
               var4
            );
         } else {
            var7 = "No teleport: the pearl ended near where you threw it";
         }
      } else {
         var7 = String.format(Locale.ROOT, "Caught %.1f blocks out, %.1f up", var2.distanceTo(var1.throwOrigin), var2.y - var1.throwOrigin.y);
      }

      LarpLog.info(
         "PearlCatch",
         "{}; now at {}, expected {} ({} blocks off), {} ticks after the wind charge",
         var7,
         formatPoint(var2),
         var3 == null ? "unknown" : formatPoint(var3),
         Double.isNaN(var4) ? "?" : aimToward(var4),
         this.tickCounter - var1.throwTick
      );
      if (!this.catchState.isActive()) {
         this.status = var7;
      }
   }

   private List<PearlTrajectory.Simulation> simulateAfter() {
      if (this.simulationTick != this.tickCounter || this.cachedSimulations == null) {
         this.cachedSimulations = this.simulateAfter(
            this.pearlThrowEye, this.pearlThrowDirection, this.pearlThrowVelocity, (int)Math.max(1L, this.catchState.isActive(this.tickCounter))
         );
         this.simulationTick = this.tickCounter;
      }

      return this.cachedSimulations;
   }

   private void refuse() {
      if (this.trackedPearl == null) {
         for (Entity var2 : minecraft.level.entitiesForRendering()) {
            if (var2 instanceof ThrownEnderpearl var3 && !var3.isRemoved() && var3.getOwner() == this.player && !this.knownPearlIds.contains(var3.getId())) {
               this.trackedPearl = var3;
               LarpLog.debug(
                  "PearlCatch", "Own pearl seen {} ticks after the throw, {} client ticks old", this.catchState.isActive(this.tickCounter), var3.tickCount
               );
               return;
            }
         }
      }
   }

   private String getPauseReason() {
      if (!InteractionArbiter.canInteract() || minecraft.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!minecraft.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (ViewController.update() != ViewController.Owner.PEARL_CATCH && !ViewController.canAcquire(ViewController.Owner.PEARL_CATCH)) {
         return "Paused: Freecam or Freelook holds the view";
      } else if (this.player.isPassenger()) {
         return "Not while riding";
      } else {
         return !this.player.isEyeInFluid(FluidTags.WATER) && !this.player.isInLava() ? null : "Not with your head in water or lava";
      }
   }

   private String getInventoryBlocker() {
      if (this.player.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.isShieldUnpressActive()) {
         return "Paused while Kill Aura lowers your shield";
      } else if (NativePlacement.findItemSlot(Items.ENDER_PEARL, true) == -1) {
         return "No ender pearls in your hotbar or hands";
      } else if (NativePlacement.findItemSlot(Items.WIND_CHARGE, true) == -1) {
         return "No wind charges in your hotbar or hands";
      } else {
         return !this.player.getAbilities().invulnerable && this.player.getHealth() + this.player.getAbsorptionAmount() <= 5.0
            ? "The 5 HP pearl hit would be lethal"
            : null;
      }
   }

   private boolean isActive(Item var1) {
      int var2 = NativePlacement.findItemSlot(var1, true);
      if (var2 == -1) {
         return false;
      }

      ItemStack var3 = var2 == -2 ? this.player.getOffhandItem() : this.player.getInventory().getItem(var2);
      return var3.isItemEnabled(this.level.enabledFeatures()) && !this.player.getCooldowns().isOnCooldown(var3);
   }

   private boolean cooldownTicksRemaining(Item var1) {
      int var2 = NativePlacement.findItemSlot(var1, true);
      return var2 == -2 || var2 == this.player.getInventory().getSelectedSlot() || var2 >= 0 && HotbarSwapSession.canSwap(this);
   }

   private boolean useItemAndRestore(Item var1) {
      try {
         return this.useItem(var1);
      } finally {
         this.restoreHotbar();
      }
   }

   private boolean useItem(Item var1) {
      int var2 = NativePlacement.findItemSlot(var1, true);
      if (var2 == -1) {
         return false;
      }

      InteractionHand var3 = var2 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var2 >= 0 && var2 != this.player.getInventory().getSelectedSlot() && !this.hotbarSwap.swapTo(this.player, var2)) {
         return false;
      }

      boolean var4 = minecraft.gameMode.useItem(this.player, var3).consumesAction();
      if (var4) {
         SwingHelper.swingInteract(this.player, var3);
         ((MinecraftMixin_3)minecraft).larpUseDelay(4);
      }

      return var4;
   }

   private void restoreHotbar() {
      if (this.hotbarSwap.isSwapped()) {
         int var1 = this.player == null ? -1 : this.player.getInventory().getSelectedSlot();
         this.hotbarSwap.restore(getMc(), this.swapBack.getValue());
         if (this.player != null && this.player == minecraft.player && minecraft.gameMode != null && var1 != this.player.getInventory().getSelectedSlot()) {
            ((MultiPlayerGameModeMixin_6)minecraft.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] getPlayerLook() {
      return !ViewController.isActive()
         ? new float[]{this.player.getYRot(), this.player.getXRot()}
         : ViewController.toPlayerRotation(ViewController.getRawYaw(), ViewController.getPosition(), minecraft.options.getCameraType().isMirrored());
   }

   private void aimToward(float var1, float var2) {
      ViewController.acquire(ViewController.Owner.PEARL_CATCH);
      if (ViewController.update() == ViewController.Owner.PEARL_CATCH) {
         float[] var3 = this.rotationController
            .rotateToward(this.player.getYRot(), this.player.getXRot(), var1, var2, this.turnSpeed.getValue(), this.humanize.getValue());
         RotationManager.requestForModule(this, RotationManager.Priority.PEARL, var3[0], var3[1], 1.0);
      }
   }

   private boolean isActive(float var1, float var2) {
      return this.getRotationController(var1, var2, RotationController.boostSpeed(this.humanize.getValue(), 0.05F, RotationMath.mouseStep()));
   }

   private boolean getRotationController(float var1, float var2, float var3) {
      return Math.abs(Mth.wrapDegrees(this.player.getYRot() - var1)) <= var3 && Math.abs(this.player.getXRot() - var2) <= var3;
   }

   private Vec3 getVelocity() {
      return this.player.onGround() ? new Vec3(this.velocity.x, 0.0, this.velocity.z) : this.velocity;
   }

   private int refuse() {
      ClientPacketListener var1 = minecraft.getConnection();
      PlayerInfo var2 = var1 != null && this.player != null ? var1.getPlayerInfo(this.player.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private PearlTrajectory.World createWorld() {
      return new WorldRaycaster(this.level, this.player);
   }

   private boolean isUpDirection() {
      return this.direction.isMode("Up");
   }

   private boolean isFixedDelayTiming() {
      return this.timing.isMode("Fixed Delay");
   }

   private boolean isInstantTiming() {
      return this.timing.isMode("Instant");
   }

   private static String aimToward(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String formatPoint(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   private void refuse(String var1) {
      this.status = var1;
      LarpLog.info("PearlCatch", "Catch refused: {}", var1);
   }

   private void cancel(String var1) {
      boolean var2 = this.catchState.isActive();
      InteractionArbiter.findAttackBlocker(this);
      this.restoreHotbar();
      this.resetState();
      if (var2) {
         this.status = var1;
         LarpLog.info("PearlCatch", "Catch cancelled: {}", var1);
      }
   }

   private void resetState() {
      this.catchState.reset();
      ViewController.requestReturn(ViewController.Owner.PEARL_CATCH, this.rotationController, this.returnSpeed.getValue(), this.humanize.getValue());
      this.knownPearlIds.clear();
      this.trackedPearl = null;
      this.plan = this.windChargePlan = null;
      this.cachedSimulations = null;
      this.simulationTick = -1L;
      this.landingPoint = null;
   }

   @Override
   public void onEnable() {
      this.player = null;
      this.level = null;
      this.lastPosition = null;
      this.pendingThrows.clear();
      this.lastThrowTick = Long.MIN_VALUE;
      this.resetState();
      this.status = this.catchBind.getValue() < 0 ? "Set a Catch Bind" : "Ready";
      LarpLog.info(
         "PearlCatch",
         "Enabled; direction={}, timing={}, delay={}, turn speed={}, return speed={}, humanize={}",
         this.direction.getValue(),
         this.timing.getValue(),
         this.delay.getValue().intValue(),
         this.turnSpeed.getValue().intValue(),
         this.returnSpeed.getValue().intValue(),
         this.humanize.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.cancel("Disabled");
      this.pendingThrows.clear();
      this.player = null;
      this.level = null;
      LarpLog.info("PearlCatch", "Disabled; pending throws and aim released");
   }

   private static final class PendingThrow {
      final Set<Integer> knownPearlIds;
      final Vec3 throwOrigin;
      final PearlTrajectory.Plan plan;
      final long throwTick;
      final long deadlineTick;
      ThrownEnderpearl pearl;
      Vec3 landingPoint;
      boolean landingSimulated;
      long removedTick = -1L;

      PendingThrow(Set<Integer> var1, Vec3 var2, PearlTrajectory.Plan var3, Vec3 var4, long var5, long var7) {
         this.knownPearlIds = var1;
         this.throwOrigin = var2;
         this.plan = var3;
         this.landingPoint = var4;
         this.throwTick = var5;
         this.deadlineTick = var7;
      }
   }
}
