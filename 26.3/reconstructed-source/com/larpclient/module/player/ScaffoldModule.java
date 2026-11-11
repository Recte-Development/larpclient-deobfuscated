package com.larpclient.module.player;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.mixin.LivingEntityMixin;
import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.movement.ElytraMotionModule;
import com.larpclient.module.movement.FlyModule;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.render.world.PlacementHighlight;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.math.PlacementAim;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class ScaffoldModule extends Module implements NativePlacement.PlacementAimer, PlacementHighlight.PlacementHighlightHolder {
   private static ScaffoldModule instance;
   private final BooleanSetting autoSwitch = this.addSetting(
      new BooleanSetting("Auto Switch", "Use suitable hotbar blocks and restore your selected slot", true)
   );
   private final BooleanSetting safeWalk = this.addSetting(
      new BooleanSetting("Safe Walk", "Sneak at a ledge while grounded so you stop at the edge; jumping stays manual", true)
   );
   private final BooleanSetting keepHeight = this.addSetting(
      new BooleanSetting("Keep Height", "Build at the height captured when enabled or when this setting changes", false)
   );
   private final NumberSetting lookAhead = this.addSetting(
      new NumberSetting("Look Ahead", "Extra footprint prediction in your movement direction, in blocks", 0.35, 0.0, 0.6, 0.05)
   );
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1));
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Extra ticks between placement attempts; zero allows one per tick", 0.0, 0.0, 10.0, 1.0)
   );
   private final BooleanSetting pauseUsingItem = this.addSetting(new BooleanSetting("Pause Using Item", "Pause while eating or using an item", true));
   private final BooleanSetting pauseMining = this.addSetting(new BooleanSetting("Pause Mining", "Pause while the attack key is held", true));
   private final PlacementHighlight highlight = new PlacementHighlight(this, var1 -> this.addSetting(var1));
   private Object level;
   private Object player;
   private BlockPos target;
   private BlockPos supportTarget;
   private float[] aimAngles;
   private int turnTicks;
   private int layerY;
   private int nextPlaceTick;
   private int lastAimTick;
   private int groundLayerY;
   private boolean aiming;
   private String status = "Inactive";
   private String lastLoggedStatus = "";
   private long lastStatusLogNanos;
   private int placedBlocks;
   private int clickWaitTicks;
   private int ledgeSneaks;
   private int midAirPlacements;
   private int belowFeetPlacements;
   private boolean safeWalkSneaking;
   private long lastSneakLogNanos;
   private float walkYaw = Float.NaN;
   private float walkPitch;
   private boolean loggedWalkHeading;
   private String ledgeReason = "";
   private boolean inputTicked;
   private boolean wasOnGround;
   private boolean shiftOrJump;
   private double travelBound;
   private int unsneakedLedgeWalks;
   private long lastLedgeLogNanos;
   private static final double aIR_SPEED = 0.026;

   public ScaffoldModule() {
      super("Scaffold", "Bridge beneath your feet with visible player aim and free mouse look", Category.MOVEMENT, -1);
      instance = this;
      this.keepHeight.onChange(var1 -> {
         this.releasePlacement();
         if (this.isInGame()) {
            this.layerY = PlacementAim.feetBlockY(minecraft.player.getY());
         }
      });
      this.autoSwitch.onChange(var1 -> this.releasePlacement());
   }

   public static ScaffoldModule getInstance() {
      return instance;
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public PlacementHighlight placementHighlight() {
      return this.highlight;
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   @Override
   public boolean allowsDetachedCamera() {
      return true;
   }

   @Override
   public List<Setting<?>> getSettings() {
      return List.of(this.autoSwitch, this.safeWalk, this.keepHeight, this.lookAhead, this.range, this.delay, this.pauseUsingItem, this.pauseMining);
   }

   @Override
   public void onEnable() {
      getMc();
      this.releasePlacement();
      this.nextPlaceTick = 0;
      this.level = minecraft.level;
      this.player = minecraft.player;
      this.layerY = this.groundLayerY = PlacementAim.feetBlockY(minecraft.player.getY());
      this.status = "Ready";
      this.lastLoggedStatus = "";
      this.lastStatusLogNanos = 0L;
      this.placedBlocks = this.clickWaitTicks = this.ledgeSneaks = this.unsneakedLedgeWalks = this.midAirPlacements = this.belowFeetPlacements = 0;
      this.safeWalkSneaking = (boolean)0;
      this.lastSneakLogNanos = this.lastLedgeLogNanos = 0L;
      this.walkYaw = Float.NaN;
      this.loggedWalkHeading = (boolean)0;
      this.inputTicked = (boolean)0;
      this.ledgeReason = "";
      this.highlight.reset();
      LarpLog.info(
         this.getName(),
         "Enabled: real instant player aim, free camera (also behind Freelook and Freecam), native placement, safe walk (sneak at ledges)={}, keep height={}; clicks wait until the look already sent reaches the support; selected slot is restored",
         this.safeWalk.getValue(),
         this.keepHeight.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.releasePlacement();
      this.level = this.player = null;
      this.nextPlaceTick = 0;
      this.status = "Inactive";
      this.safeWalkSneaking = (boolean)0;
      this.inputTicked = (boolean)0;
      if (this.placedBlocks > 0 || this.clickWaitTicks > 0 || this.ledgeSneaks > 0 || this.unsneakedLedgeWalks > 0) {
         LarpLog.info(
            this.getName(),
            "Disabled after {} placed blocks ({} mid-air, {} of those below the feet's layer to support the jump); clicks waited {} ticks for the look already sent; Safe Walk sneaked at a ledge {} times; walked off a ledge unsneaked {} times",
            this.placedBlocks,
            this.midAirPlacements,
            this.belowFeetPlacements,
            this.clickWaitTicks,
            this.ledgeSneaks,
            this.unsneakedLedgeWalks
         );
      }

      LarpLog.debug(this.getName(), "Released placement, independent camera and ledge protection");
   }

   private void releasePlacement() {
      this.target = this.supportTarget = null;
      this.aimAngles = null;
      this.turnTicks = 0;
      this.aiming = (boolean)0;
      this.highlight.clear();
      NativePlacement.cancel(this);
      ViewController.release(ViewController.Owner.SCAFFOLD);
      this.restoreBodyRotation("placement paused or ended");
   }

   private boolean isSessionValid() {
      return this.isRuntimeActive()
         && minecraft != null
         && minecraft.player != null
         && minecraft.level != null
         && this.level == minecraft.level
         && this.player == minecraft.player
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && minecraft.gui.screen() == null
         && minecraft.isWindowActive();
   }

   private boolean getWalkSpeed() {
      LocalPlayer var1 = minecraft.player;
      return !var1.isPassenger()
         && !var1.isFallFlying()
         && !var1.getAbilities().flying
         && !var1.noPhysics
         && !var1.isSleeping()
         && !var1.isMobilityRestricted()
         && !var1.onClimbable()
         && !var1.isInWater()
         && !var1.isInLava();
   }

   private static boolean isMotionModuleActive() {
      FlyModule var0 = FlyModule.getInstance();
      AirStuckModule var1 = AirStuckModule.getInstance();
      ElytraMotionModule var2 = ElytraMotionModule.getInstance();
      return var0 != null && var0.isFlightActive() || var1 != null && var1.isHolding() || var2 != null && var2.isHolding() || TargetStrafeModule.restoreInput();
   }

   private boolean canBridge() {
      if (this.isSessionValid() && this.getWalkSpeed()) {
         LocalPlayer var1 = minecraft.player;
         return var1.fallDistance <= 3.0
            && (!this.pauseUsingItem.getValue() || !var1.isUsingItem())
            && (!this.pauseMining.getValue() || !minecraft.options.keyAttack.isDown());
      } else {
         return false;
      }
   }

   private static boolean isFreeCameraActive() {
      ViewController.Owner var0 = ViewController.update();
      return var0 == ViewController.Owner.FREELOOK || var0 == ViewController.Owner.FREECAM;
   }

   private boolean getPlaceableItems() {
      if (!this.canBridge() || !InteractionArbiter.canModuleInteract(this) || InteractionArbiter.resetTick()) {
         return false;
      } else {
         return !isFreeCameraActive() && !ViewController.canAcquire(ViewController.Owner.SCAFFOLD) ? false : !isMotionModuleActive();
      }
   }

   private String getSafeWalkBlocker(Player var1) {
      if (!this.safeWalk.getValue()) {
         return "Safe Walk is off";
      } else if (!this.isSessionValid()) {
         return "a screen, focus loss or session change";
      } else if (!this.getWalkSpeed()) {
         return "vehicle, flight, climbing or liquid";
      } else if (isMotionModuleActive()) {
         return "another module drives your motion";
      } else if (!var1.onGround()) {
         return "not on the ground";
      } else {
         return !ViewController.isFreecam() && minecraft.options.keyShift.isDown() ? "you hold Shift" : null;
      }
   }

   public static float getWalkYaw() {
      ScaffoldModule var0 = instance;
      Minecraft var1 = getMc();
      return var0 != null && !Float.isNaN(var0.walkYaw) && var1 != null && var1.player != null && var1.player == var0.player && var0.isRuntimeActive()
         ? var0.walkYaw
         : Float.NaN;
   }

   public static boolean isBridging(Player var0) {
      if (instance != null && instance.isEnabled()) {
         ScaffoldModule var1 = instance;
         return var1 != null && var1.safeWalkSneaking && var0 == getMc().player;
      } else {
         return false;
      }
   }

   public static void onCameraEnded() {
      if (instance != null) {
         instance.restoreBodyRotation("the outer camera ended");
      }
   }

   private void getWalkYaw() {
      this.walkYaw = minecraft.player.getYRot();
      this.walkPitch = minecraft.player.getXRot();
      String var1 = ViewController.update() == ViewController.Owner.FREECAM ? "Freecam" : "Freelook";
      if (!this.loggedWalkHeading) {
         this.loggedWalkHeading = (boolean)1;
         LarpLog.info(
            this.getName(),
            "Bridging behind {}: the body turns to place while your keys keep walking toward {} degrees; it faces that way again when bridging pauses or the camera returns",
            var1,
            Math.round(Mth.wrapDegrees(this.walkYaw))
         );
      } else {
         LarpLog.debug(this.getName(), "Holding walking heading {} behind {}", Math.round(Mth.wrapDegrees(this.walkYaw)), var1);
      }
   }

   private void restoreBodyRotation(String var1) {
      if (!Float.isNaN(this.walkYaw)) {
         float var2 = this.walkYaw;
         float var3 = this.walkPitch;
         this.walkYaw = Float.NaN;
         LocalPlayer var4 = minecraft == null ? null : minecraft.player;
         if (var4 != null && var4 == this.player && var4.isAlive()) {
            float var5 = var4.getYRot() + Mth.wrapDegrees(var2 - var4.getYRot());
            var4.setYRot(var5);
            var4.setXRot(var3);
            var4.setYHeadRot(var5);
            var4.yRotO = var5;
            var4.xRotO = var3;
            LarpLog.debug(this.getName(), "Body faces the walking heading again: {}", var1);
         }
      }
   }

   public boolean isAiming() {
      return !this.isEnabled()
         ? false
         : this.canBridge()
            && (this.target != null || this.aiming && minecraft.player.tickCount - this.lastAimTick >= 0 && minecraft.player.tickCount - this.lastAimTick <= 3);
   }

   private float yawToMovement() {
      return PlacementAim.yawToward(PlayerInputUtil.getMovementDirection(), minecraft.player.getYRot());
   }

   private Vec3 predictEyePosition() {
      LocalPlayer var1 = minecraft.player;
      Vec3 var2 = var1.getDeltaMovement();
      Vec3 var3 = PlacementAim.predictPosition(var2, PlayerInputUtil.getMovementDirection(), var1.onGround() ? getWalkSpeed(var1) : 0.026);
      double var4 = PlacementAim.findSupportBlocks(var2.y, var1.onGround(), var1.input.keyPresses.jump(), var1.getAttributeValue(Attributes.JUMP_STRENGTH));
      return var1.getEyePosition().add(var3.x, var4, var3.z);
   }

   private float[] aimAtBlock(BlockPos var1) {
      Vec3 var2 = this.predictEyePosition();
      double var3 = this.range.getValue();
      return PlacementAim.aimAtBlock(
         var1,
         minecraft.player.getEyePosition(),
         this.yawToMovement(),
         minecraft.player.getYRot(),
         minecraft.player.getXRot(),
         var5 -> NativePlacement.isReplaceable(this, var1, var5, var3) != null && NativePlacement.raycastFromOrigin(this, var2, var1, var5, var3) != null,
         var4 -> NativePlacement.isReplaceable(this, var1, var4, var3) != null
      );
   }

   private float[] isSessionValid(BlockPos var1, Direction var2, boolean var3) {
      BlockPos var4 = var1.relative(var2);
      Vec3 var5 = this.predictEyePosition();
      double var6 = this.range.getValue();
      Predicate var8 = var5x -> {
         BlockHitResult var6x = NativePlacement.isReplaceable(this, var1, var5x, var6);
         return var6x != null && var6x.getDirection() == var2;
      };
      return PlacementAim.aimAtFace(
         var1,
         var2,
         NativePlacement.facePoints(var1, var2),
         minecraft.player.getEyePosition(),
         this.yawToMovement(),
         minecraft.player.getYRot(),
         minecraft.player.getXRot(),
         var7 -> var8.test(var7)
            && (NativePlacement.raycastFromOrigin(this, var5, var1, var7, var6) != null || PlacementAim.computeRotation(var4, var5, var7, var6)),
         var8,
         var3
      );
   }

   private static double getWalkSpeed(Player var0) {
      double var1 = var0.getAttributeValue(Attributes.MOVEMENT_SPEED);
      return var0.isSprinting() ? var1 : var1 * 1.3;
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (this.getPlaceableItems() && InteractionArbiter.ownsInteraction(this)) {
            float[] var3 = this.supportTarget != null ? this.aimAtBlock(this.supportTarget) : this.aimAngles;
            if (var3 != null) {
               var1 = var3[0];
               var2 = var3[1];
            }

            boolean var4 = isFreeCameraActive();
            RotationManager.requestForModule(this, RotationManager.Priority.DEFENSE, var1, var2, 1.0);
            if (!RotationManager.isActive(RotationManager.Priority.DEFENSE)) {
               this.aiming = (boolean)0;
               if (!var4) {
                  ViewController.release(ViewController.Owner.SCAFFOLD);
               }
            } else {
               if (var4) {
                  if (Float.isNaN(this.walkYaw)) {
                     this.getWalkYaw();
                  }
               } else {
                  ViewController.acquire(ViewController.Owner.SCAFFOLD);
               }

               this.aiming = (boolean)1;
               this.lastAimTick = minecraft.player.tickCount;
            }
         }
      }
   }

   public float getBodyYaw(float var1) {
      if (!this.isEnabled()) {
         return var1;
      } else if (this.isRuntimeActive() && (ViewController.update() == ViewController.Owner.SCAFFOLD || !Float.isNaN(getWalkYaw()))) {
         minecraft.player.setYHeadRot(minecraft.player.getYRot());
         return minecraft.player.getYRot();
      } else {
         return var1;
      }
   }

   public static boolean aimAtBlock(Player var0) {
      if (instance != null && instance.isEnabled()) {
         ScaffoldModule var1 = instance;
         return var1 != null && var0 == getMc().player && var1.getSafeWalkBlocker(var0) == null;
      } else {
         return false;
      }
   }

   public static Input applySafeWalk(LocalPlayer var0, Input var1) {
      if (instance != null && instance.isEnabled()) {
         ScaffoldModule var2 = instance;
         if (var2 == null) {
            return var1;
         }

         int var3 = var1.jump() && PlacementAim.isGrounded(((LivingEntityMixin)var0).larpNoJumpDelay()) ? 1 : 0;
         if (var0 == getMc().player && var2.isRuntimeActive()) {
            var2.inputTicked = (boolean)1;
            var2.wasOnGround = var0.onGround();
            var2.shiftOrJump = (boolean)(!var1.shift() && var3 == 0 ? 0 : 1);
         }

         String var4 = aimAtBlock(var0) ? null : (var0 == getMc().player ? var2.getSafeWalkBlocker(var0) : "another player");
         if (!var1.shift() && var3 == 0 && var4 == null) {
            Vec2 var5 = var0.input.moveVector;
            float var6 = MovementCorrection.isRotationActive() && !MovementCorrection.isStrictMovementActive() ? ViewController.getPlayerYaw() : var0.getYRot();
            double var7 = Math.toRadians(var6);
            Vec3 var9 = new Vec3(var5.x * Math.cos(var7) - var5.y * Math.sin(var7), 0.0, var5.y * Math.cos(var7) + var5.x * Math.sin(var7));
            Vec3 var10 = PlacementAim.predictPosition(var0.getDeltaMovement(), var9, getWalkSpeed(var0));
            boolean var11 = PlacementAim.hasGroundBelow(var0.getBoundingBox(), var10, var0.maxUpStep(), var1x -> var0.level().noCollision(var0, var1x));
            var2.getWalkYaw(var11);
            var2.ledgeReason = var11 ? "sneaked" : "the step looked safe";
            var2.travelBound = var10.horizontalDistance();
            return var11 ? new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), true, var1.sprint()) : var1;
         } else {
            var2.safeWalkSneaking = (boolean)0;
            var2.ledgeReason = var1.shift() ? "the body already sneaks" : (var3 != 0 ? "a jump" : var4);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void getWalkYaw(boolean var1) {
      if (var1 && !this.safeWalkSneaking) {
         this.ledgeSneaks++;
         long var2 = System.nanoTime();
         if (var2 - this.lastSneakLogNanos >= 2000000000L) {
            this.lastSneakLogNanos = var2;
            LarpLog.info(
               this.getName(),
               "Safe Walk sneaked at a ledge ({} times since enable); vanilla sneak keeps you on the edge until a block is placed",
               this.ledgeSneaks
            );
         }
      }

      this.safeWalkSneaking = var1;
   }

   public static boolean isSuitableBlock(ItemStack var0, BlockGetter var1, BlockPos var2) {
      if (!var0.isEmpty() && var0.getItem() instanceof BlockItem var3) {
         Block var6 = var3.getBlock();
         BlockState var5 = var6.defaultBlockState();
         return !(var6 instanceof FallingBlock)
            && !(var6 instanceof TntBlock)
            && var6 != Blocks.MAGMA_BLOCK
            && var6 != Blocks.RESPAWN_ANCHOR
            && var6 != Blocks.CRAFTING_TABLE
            && var6 != Blocks.SMITHING_TABLE
            && var6 != Blocks.FLETCHING_TABLE
            && var6 != Blocks.NOTE_BLOCK
            && !var5.hasBlockEntity()
            && var5.isCollisionShapeFullBlock(var1, var2);
      } else {
         return false;
      }
   }

   private List<Item> getPlaceableItems() {
      LinkedHashSet var1 = new LinkedHashSet();
      Inventory var2 = minecraft.player.getInventory();
      ItemStack var3 = minecraft.player.getMainHandItem();
      ItemStack var4 = minecraft.player.getOffhandItem();
      if (isSuitableBlock(var3, minecraft.level, minecraft.player.blockPosition())) {
         var1.add(var3.getItem());
      }

      if (isSuitableBlock(var4, minecraft.level, minecraft.player.blockPosition())) {
         var1.add(var4.getItem());
      }

      if (this.autoSwitch.getValue()) {
         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = var2.getItem(var6);
            if (isSuitableBlock(var7, minecraft.level, minecraft.player.blockPosition())) {
               var5.add(var7);
            }
         }

         var5.sort(Comparator.comparingInt(ItemStack::getCount).reversed());
         var5.forEach(var1x -> var1.add(var1x.getItem()));
      }

      return List.copyOf(var1);
   }

   private List<List<BlockPos>> collectTargetLayers() {
      Vec3 var1 = PlayerInputUtil.getMovementDirection().scale(this.lookAhead.getValue());
      Vec3 var2 = minecraft.player.getDeltaMovement().multiply(1.0, 0.0, 1.0);
      int var3 = PlacementAim.feetBlockY(minecraft.player.getY());
      return PlacementAim.collectLayers(
         minecraft.player.position(),
         minecraft.player.getBoundingBox(),
         var1.add(var2),
         this.keepHeight.getValue() ? List.of(this.layerY) : PlacementAim.layerYs(var3, this.groundLayerY),
         !minecraft.player.onGround(),
         NativePlacement::isReplaceable,
         NativePlacement::getFailureReason
      );
   }

   private boolean applySafeWalk(BlockPos var1) {
      return this.collectTargetLayers().stream().anyMatch(var1x -> var1x.contains(var1));
   }

   private void isSuitableBlock(String var1) {
      this.status = var1;
      long var2 = System.nanoTime();
      if (!var1.equals(this.lastLoggedStatus) && var2 - this.lastStatusLogNanos >= 2000000000L) {
         this.lastLoggedStatus = var1;
         this.lastStatusLogNanos = var2;
         LarpLog.debug(this.getName(), "{}; will retry when the current placement conditions permit", var1);
      }
   }

   private void isSessionValid() {
      if (this.inputTicked) {
         this.inputTicked = (boolean)0;
         LocalPlayer var1 = minecraft.player;
         if (var1 != null
            && var1 == this.player
            && this.wasOnGround
            && !this.shiftOrJump
            && !var1.onGround()
            && !(var1.getDeltaMovement().y >= 0.0)
            && this.getWalkSpeed()
            && !this.ledgeReason.equals("Safe Walk is off")) {
            this.unsneakedLedgeWalks++;
            long var2 = System.nanoTime();
            if (var2 - this.lastLedgeLogNanos >= 2000000000L) {
               this.lastLedgeLogNanos = var2;
               LarpLog.info(
                  this.getName(),
                  "Walked off a ledge without the Safe Walk sneak ({} times since enable): {}{}; camera={}, status={}",
                  this.unsneakedLedgeWalks,
                  this.ledgeReason,
                  this.ledgeReason.equals("the step looked safe") ? String.format(Locale.ROOT, " (travel bound %.3f blocks)", this.travelBound) : "",
                  ViewController.update(),
                  this.status
               );
            }
         }
      }
   }

   @Override
   public void onTick() {
      this.isSessionValid();
      if (minecraft.player.onGround()) {
         this.groundLayerY = PlacementAim.feetBlockY(minecraft.player.getY());
      }

      if (!Float.isNaN(this.walkYaw) && !isFreeCameraActive()) {
         this.restoreBodyRotation("the outer camera ended");
      }

      BlockPos var1 = this.target;
      NativePlacement.cancel(this);
      this.target = this.supportTarget = null;
      this.aimAngles = null;
      if (!this.getPlaceableItems()) {
         this.releasePlacement();
         this.isSuitableBlock("Paused: screen, movement, item use or another camera owns control");
      } else {
         if (!this.isAiming()) {
            ViewController.release(ViewController.Owner.SCAFFOLD);
            this.restoreBodyRotation("no block to aim at");
         }

         int var18 = this.nextPlaceTick;
         int var17 = minecraft.player.tickCount;
         if ((Integer.compare(var17, var18) >>> 31 ^ 1) == 0) {
            this.highlight.setTarget(null);
            this.status = "Placement delay";
         } else {
            List var2 = this.getPlaceableItems();
            if (var2.isEmpty()) {
               this.releasePlacement();
               this.isSuitableBlock("No suitable blocks in held hands or hotbar");
            } else {
               List var3 = this.collectTargetLayers();
               ArrayList var4 = new ArrayList();

               label108:
               for (List var6 : var3) {
                  for (BlockPos var8 : var6) {
                     if (NativePlacement.isReplaceable(var8)) {
                        for (Item var10 : var2) {
                           BlockHitResult var11 = NativePlacement.findPlacementFor(this, var8, var10, this.range.getValue(), true, this.autoSwitch.getValue());
                           if (var11 != null) {
                              boolean var12 = NativePlacement.matchesItem(this, var11.getBlockPos(), this.range.getValue());
                              var4.add(new ScaffoldModule.PlacementCandidate(var8, var10, var11.getBlockPos(), var11.getDirection(), var12));
                              if (var12) {
                                 break label108;
                              }
                              break;
                           }
                        }
                     }
                  }

                  if (!var4.isEmpty()) {
                     break;
                  }
               }

               int var21 = PlacementAim.selectIndex(
                  var4.stream().map(ScaffoldModule.PlacementCandidate::sentAim).toList(),
                  var4.stream().map(ScaffoldModule.PlacementCandidate::pos).toList().indexOf(var1)
               );
               if (var21 >= 0) {
                  ScaffoldModule.PlacementCandidate var22 = (ScaffoldModule.PlacementCandidate)var4.get(var21);
                  BlockPos var24 = var22.pos();
                  float[] var10000;
                  if (var22.sentAim()) {
                     BlockPos var10001 = var22.support();
                     Direction var10002 = var22.face();
                     boolean var20 = false;
                     int var19 = this.turnTicks;
                     var10000 = this.isSessionValid(var10001, var10002, (-Integer.signum(var19) >>> 31 ^ 1) == 0);
                  } else {
                     var10000 = null;
                  }

                  float[] var26 = var10000;
                  int var27 = var26 != null ? 1 : 0;
                  this.target = var24;
                  boolean var28 = NativePlacement.pruneInactive(
                     this,
                     var24,
                     var22.item(),
                     this.range.getValue(),
                     true,
                     this.autoSwitch.getValue(),
                     () -> this.getPlaceableItems() && var24.equals(this.target) && this.applySafeWalk(var24),
                     () -> var27,
                     var2x -> {
                        this.nextPlaceTick = minecraft.player.tickCount + this.delay.getValue().intValue() + 1;
                        if (var2x) {
                           this.placedBlocks++;
                           if (!minecraft.player.onGround()) {
                              this.midAirPlacements++;
                              if (var24.getY() < PlacementAim.feetBlockY(minecraft.player.getY())) {
                                 this.belowFeetPlacements++;
                              }
                           }
                        }

                        this.status = var2x ? "Bridging" : "Placement rejected; retrying";
                     }
                  );
                  if (var28) {
                     this.highlight.setTarget(var24);
                     if (var27 != 0) {
                        this.aimAngles = var26;
                        this.turnTicks = 0;
                     } else {
                        byte var14 = 1;
                        int var13 = this.clickWaitTicks;
                        this.clickWaitTicks = var13 + var14;
                        this.supportTarget = var22.support();
                        if (var22.sentAim()) {
                           byte var16 = 1;
                           int var15 = this.turnTicks;
                           this.turnTicks = var15 + var16;
                        }
                     }

                     this.status = var27 != 0 ? "Aiming at bridge block" : "Turning before the click";
                     return;
                  }

                  this.target = null;
               }

               this.highlight.setTarget(null);
               BlockPos var23 = PlacementAim.yawToward(
                  var3.stream().flatMap(Collection::stream).toList(),
                  NativePlacement::isReplaceable,
                  NativePlacement::getFailureReason,
                  var1x -> NativePlacement.isReplaceable(this, var1x, Vec3.atCenterOf(var1x).subtract(minecraft.player.getEyePosition()), this.range.getValue())
                     != null
               );
               if (var23 != null) {
                  this.supportTarget = var23;
                  float[] var25 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), Vec3.atCenterOf(var23));
                  this.aimPlacement(var25[0], var25[1]);
                  this.status = "Looking at the next support";
               } else {
                  this.isSuitableBlock("Waiting for a reachable support face beneath your path");
               }
            }
         }
      }
   }

   private record PlacementCandidate(BlockPos pos, Item item, BlockPos support, Direction face, boolean sentAim) {
   }
}
