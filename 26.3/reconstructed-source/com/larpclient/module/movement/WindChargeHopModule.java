package com.larpclient.module.movement;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.MinecraftMixin_3;
import com.larpclient.mixin.MultiPlayerGameModeMixin_6;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.network.ServerBrandCheck;
import com.larpclient.util.entity.RotationController;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.entity.WindChargeBoost;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class WindChargeHopModule extends Module {
   private static final int tRACK_TICKS = 40;
   private static final int mAX_TURN_TICKS = 30;
   private static WindChargeHopModule instance;
   private final KeybindSetting hopBind = this.addSetting(new KeybindSetting("Hop Bind", "Press for one wind charge hop; separate from the module toggle", -1));
   private final ModeSetting direction = this.addSetting(
      new ModeSetting(
         "Direction", "Up throws straight down for height; Forward throws behind you so the burst pushes you where you look", "Up", "Up", "Forward"
      )
   );
   private final ModeSetting jumpMode = this.addSetting(
      new ModeSetting(
         "Jump",
         "With Throw jumps as you throw, so the push adds to the jump; Timed jumps just before the push arrives, from your ping and measured hops; Off never jumps",
         "With Throw",
         "With Throw",
         "Timed",
         "Off"
      )
   );
   private final BooleanSetting swapBack = this.addSetting(
      new BooleanSetting("Swap Back", "Return to your previous hotbar slot the tick after the throw", true)
   );
   private final NumberSetting turnSpeed = this.addSetting(
      new NumberSetting(
         "Turn Speed",
         "Most the player turns per tick for the throw; your camera stays on your look. 180 with Humanize off turns at once",
         45.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final NumberSetting returnSpeed = this.addSetting(
      new NumberSetting("Return Speed", "Most the player turns per tick back to your look after the throw. 180 returns at once", 35.0, 10.0, 180.0, 5.0)
   );
   private final BooleanSetting humanize = this.addSetting(
      new BooleanSetting(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final RotationController rotationController = new RotationController();
   private final HotbarSwapSession hotbarSwap = new HotbarSwapSession(this);
   private static final Map<String, Integer> measuredPushDelays = new HashMap<>();
   private WindChargeHopModule.State state = WindChargeHopModule.State.IDLE;
   private boolean hopRequested;
   private LocalPlayer player;
   private ClientLevel level;
   private WindChargeBoost.AimSolution aim;
   private long tickCounter;
   private long throwTick = Long.MIN_VALUE;
   private int jumpDelayTicks;
   private int trackTicksRemaining;
   private double startY;
   private double maxY;
   private double lastVelocityY;
   private boolean pushSeen;
   private float yawJitter;
   private float pitchJitter;
   private final Set<Integer> existingChargeIds = new HashSet<>();
   private int chargeSpawnTick;
   private String status = "Set a Hop Bind";
   private int turnTicks;

   public WindChargeHopModule() {
      super("Wind Charge Hop", "Throw a wind charge at the ground for a timed boost, with a silent turn", Category.MOVEMENT, -1);
      instance = this;
      this.direction.onChange(var1 -> this.cancelHop("Direction changed"));
      this.hopBind.onChange(var1 -> this.cancelHop("Bind changed"));
   }

   public static WindChargeHopModule getInstance() {
      return instance;
   }

   public RotationController getRotationController() {
      return this.rotationController;
   }

   public double onKeyPress() {
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

   public boolean isThrowing() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.player != null
            && this.player == minecraft.player
            && this.level == minecraft.level
            && (this.state == WindChargeHopModule.State.THROWING || this.tickCounter == this.throwTick);
   }

   public static void onKeyPress(int var0) {
      if (instance != null && instance.isEnabled()) {
         WindChargeHopModule var1 = instance;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.hopBind.getValue()) {
            var1.hopRequested = (boolean)1;
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.cancelHop("Waiting for a world");
      } else {
         if (this.player != minecraft.player || this.level != minecraft.level) {
            this.cancelHop("World changed");
            this.player = minecraft.player;
            this.level = minecraft.level;
            this.trackTicksRemaining = 0;
         }

         this.tickCounter++;
         boolean var9 = false;
         int var8 = this.trackTicksRemaining;
         if ((-Integer.signum(var8) >>> 31 ^ 1) == 0) {
            this.trackHop();
         }

         if (this.state != WindChargeHopModule.State.THROWING && this.hotbarSwap.isSwapped() && this.tickCounter > this.throwTick) {
            this.restoreSlot();
         }

         byte var1 = this.hopRequested;
         this.hopRequested = (boolean)0;
         if (this.state == WindChargeHopModule.State.IDLE) {
            boolean var11 = false;
            byte var10 = var1;
            int var12 = Integer.signum(var10);
            if (((var12 | -var12) >>> 31 ^ 1) == 0) {
               this.startHop();
            } else if (this.hopBind.getValue() < 0) {
               this.status = "Set a Hop Bind";
            }
         } else if (this.state == WindChargeHopModule.State.WAITING_JUMP) {
            this.tickWaitingJump();
         } else {
            String var2 = this.startHop();
            if (var2 != null) {
               this.cancelHop(var2);
            } else {
               WindChargeBoost.AimSolution var3 = WindChargeBoost.findBestAim(
                  this.player.position(),
                  this.player.getEyeHeight(),
                  this.player.getDeltaMovement(),
                  this.player.onGround(),
                  this.shouldJumpWithThrow(),
                  this.getLookAngles()[0],
                  this.isForwardMode(),
                  this::raycast
               );
               if (var3 == null) {
                  this.cancelHop("No ground close enough for a boost");
               } else {
                  this.aim = var3;
                  this.updateRotation();
                  InteractionArbiter.queueAction(
                     this,
                     this::executeThrow,
                     () -> this.state == WindChargeHopModule.State.THROWING && this.startHop() == null && this.canThrow() && this.getRotationController()
                  );
                  int var10000 = ++this.turnTicks;
                  byte var5 = 30;
                  int var4 = var10000;
                  if ((-Integer.compare(var4, var5) >>> 31 ^ 1) == 0) {
                     int var10001 = this.getRotationController();
                     boolean var7 = false;
                     byte var6 = (byte)var10001;
                     var10001 = Integer.signum(var6);
                     this.cancelHop(((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "The throw could not go out" : "The turn did not reach a throw angle in time");
                  }
               }
            }
         }
      }
   }

   private void startHop() {
      String var1 = this.startHop();
      if (var1 == null) {
         var1 = this.cancelHop();
      }

      if (var1 != null) {
         this.status = var1;
         LarpLog.info("WindHop", "Hop refused: {}", var1);
      } else {
         LocalPlayer var2 = this.player;
         float var3 = this.getLookAngles()[0];
         int var4 = !this.jumpMode.isMode("Off") && var2.onGround() ? 1 : 0;
         this.aim = WindChargeBoost.findBestAim(
            var2.position(),
            var2.getEyeHeight(),
            var2.getDeltaMovement(),
            var2.onGround(),
            this.shouldJumpWithThrow(),
            var3,
            this.isForwardMode(),
            this::raycast
         );
         if (this.aim == null) {
            this.status = "No ground close enough for a boost";
            LarpLog.info("WindHop", "Hop refused: no burst within {} blocks of you", 2.4);
         } else {
            this.state = WindChargeHopModule.State.THROWING;
            this.turnTicks = 0;
            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            this.yawJitter = this.isForwardMode() ? (float)(var5.nextDouble() * 6.0 - 3.0) : 0.0F;
            this.pitchJitter = this.isForwardMode() ? (float)(var5.nextDouble() * 4.0 - 2.0) : (float)(-(var5.nextDouble() * 3.0));
            this.rotationController.clampStep();
            this.shouldJumpWithThrow();
            LarpLog.info(
               "WindHop",
               "Hop: {}, best throw yaw {} pitch {}, burst after {} ticks at {}, predicted push {} up {} along ({} blocks/tick), jump {}; turn {}/tick, return {}/tick, humanize {}",
               this.direction.getValue(),
               formatNumber(this.aim.yaw()),
               formatNumber(this.aim.pitch()),
               this.aim.ticks(),
               formatVec(this.aim.burst()),
               formatNumber(this.aim.push().y),
               formatNumber(Math.hypot(this.aim.push().x, this.aim.push().z)),
               formatNumber(this.aim.push().length()),
               var4 != 0 ? this.jumpMode.getValue() : "none",
               this.turnSpeed.getValue().intValue(),
               this.returnSpeed.getValue().intValue(),
               this.humanize.getValue()
            );
            this.updateRotation();
            InteractionArbiter.queueAction(
               this,
               this::executeThrow,
               () -> this.state == WindChargeHopModule.State.THROWING && this.startHop() == null && this.canThrow() && this.getRotationController()
            );
         }
      }
   }

   private boolean isForwardMode() {
      return this.direction.isMode("Forward");
   }

   private boolean shouldJumpWithThrow() {
      return this.jumpMode.isMode("With Throw") && this.player.onGround();
   }

   private WindChargeBoost.AimSolution executeThrow() {
      return WindChargeBoost.simulateThrow(
         this.player.position(),
         this.player.getEyeHeight(),
         this.player.getDeltaMovement(),
         this.player.onGround(),
         this.shouldJumpWithThrow(),
         this.player.getYRot(),
         this.player.getXRot(),
         this::raycast
      );
   }

   private boolean getRotationController() {
      return WindChargeBoost.isBetterAim(this.aim, this.executeThrow(), this.getLookAngles()[0], this.isForwardMode());
   }

   private float[] getInstance() {
      return !this.isForwardMode()
         ? new float[]{this.player.getYRot(), Math.min(90.0F, this.aim.pitch() + this.pitchJitter)}
         : new float[]{this.aim.yaw() + this.yawJitter, Mth.clamp(this.aim.pitch() + this.pitchJitter, -90.0F, 90.0F)};
   }

   private void shouldJumpWithThrow() {
      int var1 = NativePlacement.findItemSlot(Items.WIND_CHARGE, true);
      if (var1 >= 0 && var1 != this.player.getInventory().getSelectedSlot() && this.hotbarSwap.swapTo(this.player, var1)) {
         if (minecraft.gameMode != null) {
            ((MultiPlayerGameModeMixin_6)minecraft.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private WindChargeBoost.RayHit raycast(Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = this.level.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, this.player));
      return var3.getType() == Type.MISS ? null : new WindChargeBoost.RayHit(var3.getLocation(), Vec3.atLowerCornerOf(var3.getDirection().getUnitVec3i()));
   }

   private void updateRotation() {
      if (InteractionArbiter.ownsInteraction(this)) {
         ViewController.acquire(ViewController.Owner.WIND_HOP);
         if (ViewController.update() == ViewController.Owner.WIND_HOP) {
            float[] var1 = this.getInstance();
            float[] var2 = this.rotationController
               .rotateToward(this.player.getYRot(), this.player.getXRot(), var1[0], var1[1], this.turnSpeed.getValue(), this.humanize.getValue());
            RotationManager.requestForModule(this, RotationManager.Priority.WIND_CHARGE, var2[0], var2[1], 1.0);
         }
      }
   }

   private void executeThrow() {
      LocalPlayer var1 = this.player;
      WindChargeBoost.AimSolution var2 = this.executeThrow();
      if (this.state == WindChargeHopModule.State.THROWING && WindChargeBoost.isBetterAim(this.aim, var2, this.getLookAngles()[0], this.isForwardMode())) {
         int var3 = !this.jumpMode.isMode("Off") && var1.onGround() ? 1 : 0;
         if (this.jumpMode.isMode("With Throw") && var3 != 0) {
            var1.jumpFromGround();
         }

         this.existingChargeIds.clear();

         for (Entity var5 : minecraft.level.entitiesForRendering()) {
            if (var5 instanceof WindCharge var6 && var6.getOwner() == var1) {
               this.existingChargeIds.add(var6.getId());
            }
         }

         if (!this.useWindCharge()) {
            this.cancelHop("The wind charge throw was rejected");
         } else {
            this.throwTick = this.tickCounter;
            this.chargeSpawnTick = -1;
            this.startY = this.maxY = var1.getY();
            this.lastVelocityY = var1.getDeltaMovement().y;
            this.pushSeen = (boolean)0;
            this.trackTicksRemaining = 40;
            String var8 = this.getRotationController();
            int var9 = measuredPushDelays.getOrDefault(var8, 0);
            this.jumpDelayTicks = WindChargeBoost.traceCharge(var2.ticks(), this.trackHop(), var9);
            if (this.jumpMode.isMode("Timed") && var3 != 0 && this.jumpDelayTicks > 0) {
               this.state = WindChargeHopModule.State.WAITING_JUMP;
               this.status = "Wind charge thrown; jump in " + this.jumpDelayTicks + " ticks";
            } else {
               if (this.jumpMode.isMode("Timed") && var3 != 0) {
                  var1.jumpFromGround();
               }

               this.state = WindChargeHopModule.State.IDLE;
               this.status = "Wind charge thrown";
            }

            double var10 = WindChargeBoost.scorePush(this.aim, this.getLookAngles()[0], this.isForwardMode());
            LarpLog.info(
               "WindHop",
               "Thrown at yaw {} pitch {} ({} of the best push) after a turn of {}; jump {} ({} ticks from {}), round trip {} ticks",
               formatNumber(var1.getYRot()),
               formatNumber(var1.getXRot()),
               var10 > 0.0
                  ? String.format(Locale.ROOT, "%.0f%%", 100.0 * WindChargeBoost.scorePush(var2, this.getLookAngles()[0], this.isForwardMode()) / var10)
                  : "?",
               this.rotationController.summarize(),
               var3 != 0 ? this.jumpMode.getValue() : "none",
               this.jumpDelayTicks,
               var9 > 0 ? "measured hops" : "ping",
               this.trackHop()
            );
         }
      }
   }

   private void tickWaitingJump() {
      if (this.tickCounter - this.throwTick >= this.jumpDelayTicks) {
         this.state = WindChargeHopModule.State.IDLE;
         if (this.player.onGround() && !this.pushSeen) {
            this.player.jumpFromGround();
            this.status = "Jumped for the burst";
         } else {
            this.status = this.pushSeen ? "The push came before the jump" : "Left the ground before the jump";
         }
      }
   }

   private void trackHop() {
      LocalPlayer var1 = this.player;
      if (this.chargeSpawnTick < 0) {
         for (Entity var4 : minecraft.level.entitiesForRendering()) {
            if (var4 instanceof WindCharge var2 && var2.getOwner() == var1 && !this.existingChargeIds.contains(var2.getId())) {
               this.chargeSpawnTick = (int)(this.tickCounter - this.throwTick);
               break;
            }
         }
      }

      double var7 = var1.getDeltaMovement().y;
      if (!this.pushSeen && var7 - this.lastVelocityY > 0.5) {
         this.pushSeen = (boolean)1;
         int var8 = (int)(this.tickCounter - this.throwTick);
         measuredPushDelays.put(this.getRotationController(), var8);
         LarpLog.info("WindHop", "Push arrived {} ticks after the throw: vertical speed {} to {}", var8, formatNumber(this.lastVelocityY), formatNumber(var7));
      }

      this.lastVelocityY = var7;
      this.maxY = Math.max(this.maxY, var1.getY());
      if (--this.trackTicksRemaining <= 0) {
         String var9 = this.chargeSpawnTick >= 0
            ? "your wind charge appeared " + this.chargeSpawnTick + " ticks after the throw"
            : (
               this.pushSeen
                  ? "the burst pushed you, so the server spawned the throw"
                  : "no wind charge from you was seen and nothing pushed you: the server may not have spawned the throw (an anticheat or plugin may have cancelled it), or it burst between two ticks and missed you"
            );
         String var5 = !this.pushSeen && this.chargeSpawnTick >= 0
            ? "; no push seen, so the burst missed you or this server changes wind charge knockback"
            : "";
         String var6 = ServerBrandCheck.getServerBrand();
         LarpLog.info(
            "WindHop", "Hop result: rose {} blocks; {}{}; server {}", formatNumber(this.maxY - this.startY), var9, var5, var6 == null ? "singleplayer" : var6
         );
         if (this.state == WindChargeHopModule.State.IDLE) {
            this.status = String.format(Locale.ROOT, "Hopped %.1f blocks", this.maxY - this.startY);
         }
      }
   }

   private String startHop() {
      if (!InteractionArbiter.canInteract() || minecraft.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!minecraft.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (ViewController.update() != ViewController.Owner.WIND_HOP && !ViewController.canAcquire(ViewController.Owner.WIND_HOP)) {
         return "Paused: Freecam, Freelook or a pearl catch holds the view";
      } else if (this.player.isPassenger()) {
         return "Not while riding";
      } else if (this.player.isInWater() || this.player.isEyeInFluid(FluidTags.WATER) || this.player.isInLava()) {
         return "Not in water or lava";
      } else {
         return this.player.isFallFlying() ? "Not while gliding" : null;
      }
   }

   private String cancelHop() {
      if (this.player.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.isShieldUnpressActive()) {
         return "Paused while Kill Aura lowers your shield";
      } else {
         int var1 = NativePlacement.findItemSlot(Items.WIND_CHARGE, true);
         if (var1 == -1) {
            return "No wind charges in your hotbar or hands";
         } else {
            ItemStack var2 = this.getStack(var1);
            if (!var2.isItemEnabled(this.level.enabledFeatures())) {
               return "Wind charges are disabled here";
            } else {
               float var3 = this.player.getCooldowns().getCooldownPercent(var2, 0.0F);
               if (var3 > 0.0F) {
                  UseCooldown var4 = (UseCooldown)var2.get(DataComponents.USE_COOLDOWN);
                  return String.format(Locale.ROOT, "Wind charge cooling down (%.2f s)", var3 * (var4 == null ? 10 : var4.ticks()) / 20.0);
               } else {
                  return var1 >= 0 && var1 != this.player.getInventory().getSelectedSlot() && !HotbarSwapSession.canSwap(this) ? "Hotbar busy" : null;
               }
            }
         }
      }
   }

   private ItemStack getStack(int var1) {
      return var1 == -2 ? this.player.getOffhandItem() : this.player.getInventory().getItem(var1);
   }

   private boolean canThrow() {
      int var1 = NativePlacement.findItemSlot(Items.WIND_CHARGE, true);
      return var1 != -1
         && !this.player.getCooldowns().isOnCooldown(this.getStack(var1))
         && !AuraModule.isShieldUnpressActive()
         && (var1 == -2 || var1 == this.player.getInventory().getSelectedSlot() || HotbarSwapSession.canSwap(this));
   }

   private boolean useWindCharge() {
      int var1 = NativePlacement.findItemSlot(Items.WIND_CHARGE, true);
      if (var1 == -1) {
         return false;
      }

      InteractionHand var2 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var1 >= 0 && var1 != this.player.getInventory().getSelectedSlot() && !this.hotbarSwap.swapTo(this.player, var1)) {
         return false;
      }

      boolean var3 = minecraft.gameMode.useItem(this.player, var2).consumesAction();
      if (var3) {
         SwingHelper.swingInteract(this.player, var2);
         ((MinecraftMixin_3)minecraft).larpUseDelay(4);
      }

      return var3;
   }

   private void restoreSlot() {
      if (this.hotbarSwap.isSwapped()) {
         int var1 = this.player == null ? -1 : this.player.getInventory().getSelectedSlot();
         this.hotbarSwap.restore(getMc(), this.swapBack.getValue());
         if (this.player != null && this.player == minecraft.player && minecraft.gameMode != null && var1 != this.player.getInventory().getSelectedSlot()) {
            ((MultiPlayerGameModeMixin_6)minecraft.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] getLookAngles() {
      return !ViewController.isActive()
         ? new float[]{this.player.getYRot(), this.player.getXRot()}
         : ViewController.toPlayerRotation(ViewController.getRawYaw(), ViewController.getPosition(), minecraft.options.getCameraType().isMirrored());
   }

   private int trackHop() {
      ClientPacketListener var1 = minecraft.getConnection();
      PlayerInfo var2 = var1 != null && this.player != null ? var1.getPlayerInfo(this.player.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private String getRotationController() {
      ServerData var1 = minecraft.getCurrentServer();
      return var1 != null ? var1.ip : (minecraft.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void cancelHop(String var1) {
      int var2 = this.state != WindChargeHopModule.State.IDLE ? 1 : 0;
      InteractionArbiter.findAttackBlocker(this);
      this.restoreSlot();
      this.state = WindChargeHopModule.State.IDLE;
      ViewController.requestReturn(ViewController.Owner.WIND_HOP, this.rotationController, this.returnSpeed.getValue(), this.humanize.getValue());
      if (var2 != 0) {
         this.status = var1;
         LarpLog.info("WindHop", "Hop cancelled: {}", var1);
      }
   }

   private static String formatNumber(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String formatVec(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   @Override
   public void onEnable() {
      this.player = null;
      this.level = null;
      this.state = WindChargeHopModule.State.IDLE;
      this.throwTick = Long.MIN_VALUE;
      this.trackTicksRemaining = 0;
      this.status = this.hopBind.getValue() < 0 ? "Set a Hop Bind" : "Ready";
      LarpLog.info(
         "WindHop",
         "Enabled; direction={}, jump={}, swap back={}, turn speed={}, return speed={}, humanize={}",
         this.direction.getValue(),
         this.jumpMode.getValue(),
         this.swapBack.getValue(),
         this.turnSpeed.getValue().intValue(),
         this.returnSpeed.getValue().intValue(),
         this.humanize.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.cancelHop("Disabled");
      this.player = null;
      this.level = null;
      LarpLog.info("WindHop", "Disabled; pending throw and view released");
   }

   private enum State {
      IDLE,
      THROWING,
      WAITING_JUMP;
   }
}
