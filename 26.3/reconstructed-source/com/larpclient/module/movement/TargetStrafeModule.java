package com.larpclient.module.movement;

import com.larpclient.core.ViewController;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.AutoTunnelModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.LarpLog;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class TargetStrafeModule extends Module {
   private static final int maxRecoveryTicks = 3;
   private static final int reverseCooldownTicks = 10;
   private static final float maxYawDrift = 25.0F;
   private static final double orbitCorrectionGain = Math.PI / 3;
   private static final double groundSpeedScale = 0.7;
   private static final double airSpeedScale = 1.0;
   private static final float[] noKeys = new float[]{0.0F, 0.0F};
   private static TargetStrafeModule instance;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode",
         "Native presses real movement keys at normal speed for anti-cheat servers; Fast overrides your speed and will flag on them",
         "Native",
         "Native",
         "Fast"
      )
   );
   private final NumberSetting distance = this.addSetting(
      new NumberSetting("Distance", "Distance to hold from the target center, capped inside actual attack reach", 2.4, 1.5, 3.0, 0.1)
   );
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Horizontal orbit speed in blocks per tick", 1.0, 0.1, 1.2, 0.1));
   private final NumberSetting radius = this.addSetting(
      new NumberSetting("Radius", "Distance from the target center, capped inside actual attack reach", 1.0, 1.0, 3.0, 0.1)
   );
   private final ModeSetting direction = this.addSetting(
      new ModeSetting("Direction", "Starting orbit direction; reverse when the path is blocked", "Clockwise", "Clockwise", "Counterclockwise")
   );
   private final BooleanSetting autoJump = this.addSetting(new BooleanSetting("Auto Jump", "Jump while orbiting when grounded with clear headroom", false));
   private final BooleanSetting avoidEdges = this.addSetting(
      new BooleanSetting("Avoid Edges", "Require ground below the orbit path; sneak pauses automatic movement", true)
   );
   private LocalPlayer ownerPlayer;
   private LocalPlayer inputPlayer;
   private ClientLevel level;
   private LivingEntity target;
   private long gameTime;
   private boolean movementHandled;
   private int orbitSign = 1;
   private int recoveryTicks;
   private int keyCacheTicks;
   private int reverseCooldown;
   private float[] cachedKeys;
   private float cachedYaw;
   private long lastLogNanos;
   private String lastDirection = "";
   private String status = "Waiting for Kill Aura";
   private Input originalInput;
   private Input overrideInput;
   private Vec2 originalMoveVector;
   private Vec2 overrideMoveVector;

   public TargetStrafeModule() {
      super("Target Strafe", "Automatically circle Kill Aura's target", Category.MOVEMENT, -1);
      instance = this;
      this.distance.visibleWhen(() -> !this.isFastMode());
      this.speed.visibleWhen(this::isFastMode);
      this.radius.visibleWhen(this::isFastMode);
   }

   private boolean isFastMode() {
      return this.mode.isMode("Fast");
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
      return this.isRuntimeActive() ? this.status : "Inactive";
   }

   @Override
   public void onEnable() {
      this.reset();
      this.recoveryTicks = 0;
      this.setStatus("Waiting for Kill Aura");
      LarpLog.debug(
         "TargetStrafe",
         "Enabled: mode={}, distance={}, fast speed={} blocks/tick, fast radius={}, auto jump={}, edge checks={}",
         this.mode.getValue(),
         this.distance.getValue(),
         this.speed.getValue(),
         this.radius.getValue(),
         this.autoJump.getValue(),
         this.avoidEdges.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.reset();
      this.recoveryTicks = 0;
      this.setStatus("Inactive");
   }

   private void reset() {
      this.restoreInput();
      this.ownerPlayer = null;
      this.level = null;
      this.target = null;
      this.movementHandled = (boolean)0;
      this.lastDirection = "";
      this.cachedKeys = null;
      this.keyCacheTicks = this.reverseCooldown = 0;
   }

   private void setStatus(String var1) {
      if (!this.status.equals(var1)) {
         this.logThrottled(var1);
      }

      this.status = var1;
   }

   private void logThrottled(String var1) {
      long var2 = System.nanoTime();
      if (var2 - this.lastLogNanos >= 1000000000L) {
         LarpLog.debug("TargetStrafe", "{}", var1);
         this.lastLogNanos = var2;
      }
   }

   @Override
   public void onTick() {
      this.restoreInput();
      this.movementHandled = (boolean)0;
      boolean var3 = false;
      int var2 = this.recoveryTicks;
      if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
         byte var5 = 1;
         int var4 = this.recoveryTicks;
         this.recoveryTicks = var4 - var5;
         this.reset();
         this.setStatus("Recovering after position correction");
      } else {
         LivingEntity var1 = this.findTarget();
         if (var1 == null) {
            this.reset();
         } else {
            label28: {
               if (var1 == this.target) {
                  int var10000 = this.direction.getValue().equals(this.lastDirection);
                  boolean var7 = false;
                  byte var6 = (byte)var10000;
                  var10000 = Integer.signum(var6);
                  if ((var10000 | -var10000) >>> 31 != 0) {
                     break label28;
                  }
               }

               int var10001 = this.direction.isMode("Clockwise");
               boolean var9 = false;
               byte var8 = (byte)var10001;
               var10001 = Integer.signum(var8);
               this.orbitSign = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? 1 : -1;
               this.lastDirection = this.direction.getValue();
               this.cachedKeys = null;
               this.reverseCooldown = 0;
               this.logThrottled("Orbit target acquired: entity=" + var1.getId());
            }

            this.ownerPlayer = minecraft.player;
            this.level = minecraft.level;
            this.gameTime = minecraft.level.getGameTime();
            this.target = var1;
         }
      }
   }

   private LivingEntity findTarget() {
      if (this.isInGame() && PlayerInputUtil.isInWorld() && minecraft.isWindowActive() && minecraft.gui.screen() == null && !minecraft.isPaused()) {
         LocalPlayer var1 = minecraft.player;
         FlyModule var2 = FlyModule.getInstance();
         AirStuckModule var3 = AirStuckModule.getInstance();
         ElytraMotionModule var4 = ElytraMotionModule.getInstance();
         if ((var2 == null || !var2.isFlightActive())
            && (var3 == null || !var3.isHolding())
            && (var4 == null || !var4.isHolding())
            && AutoTunnelModule.isActive() == null
            && ViewController.update() != ViewController.Owner.AUTO_TRAP
            && ViewController.update() != ViewController.Owner.AUTO_WEB
            && ViewController.update() != ViewController.Owner.AUTO_ANCHOR) {
            if (var1.isPassenger()
               || var1.isSpectator()
               || var1.getAbilities().flying
               || var1.isFallFlying()
               || var1.isInWater()
               || var1.isInLava()
               || var1.onClimbable()
               || var1.isSleeping()
               || var1.isMobilityRestricted()
               || var1.noPhysics
               || var1.isUsingItem()
               || var1.isCrouching()
               || var1.isShiftKeyDown()
               || minecraft.options.keyShift.isDown()) {
               this.setStatus("Paused for player movement or item use");
               return null;
            } else if (this.isFastMode() && var1.hurtTime > 0) {
               this.setStatus("Paused for knockback");
               return null;
            } else {
               AuraModule var5 = AuraModule.getInstance();
               LivingEntity var6 = var5 == null ? null : var5.applyMoveFix();
               if (var6 == null) {
                  this.setStatus("Waiting for Kill Aura target");
                  return null;
               } else if (horizontalReach(var1.getEyePosition(), var6.getBoundingBox(), this.getReach(), var5.getClampedReach()) <= 0.0) {
                  this.setStatus("Target is outside vertical attack reach");
                  return null;
               } else {
                  return var6;
               }
            }
         } else {
            this.setStatus("Paused for another movement controller");
            return null;
         }
      } else {
         this.setStatus("Waiting for gameplay");
         return null;
      }
   }

   private boolean isOwner(LocalPlayer var1) {
      return this.isRuntimeActive()
         && this.recoveryTicks == 0
         && var1 != null
         && var1 == this.ownerPlayer
         && var1 == getMc().player
         && this.level == minecraft.level
         && this.gameTime == minecraft.level.getGameTime()
         && this.target != null
         && this.findTarget() == this.target;
   }

   private double getReach() {
      return this.isFastMode() ? this.radius.getValue() : this.distance.getValue();
   }

   public static boolean shouldAutoJump() {
      if (instance != null && instance.isEnabled()) {
         TargetStrafeModule var0 = instance;
         if (var0 == null) {
            return false;
         }

         try {
            return var0.isOwner(getMc().player);
         } catch (RuntimeException var2) {
            var0.onError("movement ownership", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean findTarget() {
      if (instance != null && instance.isEnabled()) {
         TargetStrafeModule var0 = instance;
         return var0 != null && var0.isFastMode() && shouldAutoJump();
      } else {
         return false;
      }
   }

   public static Input modifyInput(LocalPlayer var0, Input var1) {
      if (instance != null && instance.isEnabled()) {
         TargetStrafeModule var2 = instance;
         if (var2 == null) {
            return var1;
         }

         try {
            if (!var2.isOwner(var0)) {
               return var1;
            }

            if (var1.shift()) {
               var2.reset();
               var2.setStatus("Paused for sneak input");
               return var1;
            }

            if (!var2.isFastMode()) {
               return var2.nativeInput(var0, var1);
            }

            Vec3 var3 = var2.getFastVelocity();
            Vec2 var4 = onPositionCorrection(var3, var0.getYRot());
            return var2.applyInput(
               var0,
               var1,
               var4,
               new Input(
                  var4.y > 0.1F,
                  var4.y < -0.1F,
                  var4.x > 0.1F,
                  var4.x < -0.1F,
                  var1.jump() || var2.shouldAutoJump(var0, var3.lengthSqr() > 0.0),
                  var1.shift(),
                  var1.sprint()
               )
            );
         } catch (RuntimeException var5) {
            var2.onError("input", var5);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private Input nativeInput(LocalPlayer var1, Input var2) {
      float[] var3 = this.computeNativeKeys();
      int var4 = var3[0] > 0.0F ? 1 : 0;
      int var5 = var3[0] == 0.0F && var3[1] == 0.0F ? 0 : 1;
      return this.applyInput(
         var1,
         var2,
         new Vec2(var3[1], var3[0]).normalized(),
         new Input(
            (boolean)var4,
            var3[0] < 0.0F,
            var3[1] > 0.0F,
            var3[1] < 0.0F,
            var2.jump() || this.shouldAutoJump(var1, (boolean)var5),
            var2.shift(),
            var4 != 0 && !var1.horizontalCollision
         )
      );
   }

   private boolean shouldAutoJump(LocalPlayer var1, boolean var2) {
      return var2
         && this.autoJump.getValue()
         && var1.onGround()
         && var1.getDeltaMovement().y <= 0.0
         && !var1.getAbilities().mayfly
         && minecraft.level.noCollision(var1, var1.getBoundingBox().expandTowards(0.0, 1.3, 0.0));
   }

   private Input applyInput(LocalPlayer var1, Input var2, Vec2 var3, Input var4) {
      this.inputPlayer = var1;
      this.originalInput = var2;
      this.originalMoveVector = var1.input.moveVector;
      this.overrideMoveVector = var3;
      this.overrideInput = var4;
      var1.input.moveVector = var3;
      return var4;
   }

   public static boolean restoreInput() {
      if (instance != null && instance.isEnabled()) {
         TargetStrafeModule var0 = instance;
         return var0 != null
            && var0.inputPlayer != null
            && var0.inputPlayer == getMc().player
            && var0.level == minecraft.level
            && var0.gameTime == minecraft.level.getGameTime()
            && var0.inputPlayer.input.moveVector == var0.overrideMoveVector;
      } else {
         return false;
      }
   }

   private void restoreInput() {
      if (this.inputPlayer != null && this.inputPlayer == getMc().player && this.level == minecraft.level && this.gameTime == minecraft.level.getGameTime()) {
         if (this.inputPlayer.input.keyPresses == this.overrideInput) {
            this.inputPlayer.input.keyPresses = this.originalInput;
         }

         if (this.inputPlayer.input.moveVector == this.overrideMoveVector) {
            this.inputPlayer.input.moveVector = this.originalMoveVector;
         }
      }

      this.inputPlayer = null;
      this.originalInput = this.overrideInput = null;
      this.originalMoveVector = this.overrideMoveVector = null;
   }

   public static Vec3 modifyMovement(LocalPlayer var0, Vec3 var1) {
      if (instance != null && instance.isEnabled()) {
         TargetStrafeModule var2 = instance;
         if (var2 != null && !var2.movementHandled) {
            try {
               if (!var2.isOwner(var0)) {
                  var2.restoreInput();
                  return var1;
               }

               var2.movementHandled = (boolean)1;
               if (!var2.isFastMode()) {
                  return var1;
               }

               Vec3 var3 = var2.getFastVelocity();
               Vec3 var4 = new Vec3(var3.x, var1.y, var3.z);
               var0.setDeltaMovement(var4);
               return var4;
            } catch (RuntimeException var5) {
               var2.onError("movement", var5);
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void onError(String var1, RuntimeException var2) {
      LarpLog.error("TargetStrafe", "Failed during " + var1 + "; disabling and releasing orbit input", var2);
      this.disable();
   }

   public static void onPositionCorrection(LocalPlayer var0) {
      TargetStrafeModule var1 = instance;
      if (var1 != null && var1.isRuntimeActive() && var0 == getMc().player) {
         if (var1.recoveryTicks == 0) {
            LarpLog.info(
               "TargetStrafe", "Position correction while active: mode=" + var1.mode.getValue() + ", status=" + var1.status + "; pausing for 20 ticks"
            );
         }

         var1.reset();
         var1.recoveryTicks = 20;
         var1.setStatus("Recovering after position correction");
      }
   }

   private float[] computeNativeKeys() {
      LocalPlayer var1 = minecraft.player;
      float var2 = var1.getYRot();
      if (this.reverseCooldown > 0) {
         this.reverseCooldown--;
      }

      double var3 = horizontalReach(var1.getEyePosition(), this.target.getBoundingBox(), this.distance.getValue(), AuraModule.getInstance().getClampedReach());
      if (var3 <= 0.0) {
         this.cachedKeys = null;
         return noKeys;
      }

      if (this.cachedKeys != null
         && this.keyCacheTicks-- > 0
         && Math.abs(Mth.wrapDegrees(var2 - this.cachedYaw)) <= 25.0F
         && this.isKeyPathClear(this.cachedKeys, var2)) {
         return this.cachedKeys;
      }

      Vec3 var5 = this.target.getBoundingBox().getCenter();
      float[] var6 = orbitKeys(var1.position(), var5, var3, this.orbitSign, var2);
      if (!this.isKeyPathClear(var6, var2)) {
         float[] var7 = this.reverseCooldown == 0 ? orbitKeys(var1.position(), var5, var3, -this.orbitSign, var2) : null;
         if (var7 == null || !this.isKeyPathClear(var7, var2)) {
            this.cachedKeys = null;
            this.setStatus("Orbit blocked by terrain or edge");
            return noKeys;
         }

         this.orbitSign = -this.orbitSign;
         this.reverseCooldown = 10;
         var6 = var7;
         this.logThrottled("Reversed orbit at blocked path or edge");
      }

      if (!Arrays.equals(var6, this.cachedKeys)) {
         this.logThrottled(
            String.format(
               Locale.ROOT,
               "Keys forward=%.0f strafe=%.0f distance=%.2f ring=%.2f",
               var6[0],
               var6[1],
               var1.position().subtract(var5).horizontalDistance(),
               var3
            )
         );
      }

      this.cachedKeys = var6;
      this.keyCacheTicks = 2;
      this.cachedYaw = var2;
      this.setStatus("Orbiting");
      return var6;
   }

   private boolean isKeyPathClear(float[] var1, float var2) {
      Vec3 var3 = GrimSpeedModule.getInputDirection(var1[1], var1[0], var2);
      return this.isPathSafe(var3.scale(minecraft.player.onGround() ? 0.7 : 1.0));
   }

   private Vec3 getFastVelocity() {
      LocalPlayer var1 = minecraft.player;
      double var2 = horizontalReach(var1.getEyePosition(), this.target.getBoundingBox(), this.radius.getValue(), AuraModule.getInstance().getClampedReach());
      if (var2 <= 0.0) {
         return Vec3.ZERO;
      } else {
         Vec3 var4 = this.target.getBoundingBox().getCenter();
         Vec3 var5 = nativeInput(var1.position(), var4, var2, this.speed.getValue(), this.orbitSign, var1.getYRot());
         if (this.isPathSafe(var5)) {
            this.setStatus("Orbiting");
            return var5;
         } else {
            Vec3 var6 = nativeInput(var1.position(), var4, var2, this.speed.getValue(), -this.orbitSign, var1.getYRot());
            if (this.isPathSafe(var6)) {
               this.orbitSign = -this.orbitSign;
               this.logThrottled("Reversed orbit at blocked path or edge");
               this.setStatus("Orbiting");
               return var6;
            } else {
               this.setStatus("Orbit blocked by terrain or edge");
               return Vec3.ZERO;
            }
         }
      }
   }

   private boolean isPathSafe(Vec3 var1) {
      LocalPlayer var2 = minecraft.player;
      AABB var3 = var2.getBoundingBox().deflate(0.001);
      if (!minecraft.level.noCollision(var2, var3.expandTowards(var1.x, Math.max(0.0, var2.getDeltaMovement().y), var1.z))) {
         return false;
      }

      int var4 = Math.max(1, (int)Math.ceil(var1.horizontalDistance() / 0.25));
      double var5 = var2.onGround() ? 0.6 : 2.0;

      for (int var7 = 1; var7 <= var4; var7++) {
         double var8 = var2.getX() + var1.x * var7 / var4;
         double var10 = var2.getZ() + var1.z * var7 / var4;
         if (!minecraft.level.hasChunkAt(BlockPos.containing(var8, var2.getY(), var10))) {
            return false;
         }

         if (this.avoidEdges.getValue()
            && minecraft.level.noBlockCollision(var2, new AABB(var8 - 0.1, var3.minY - var5, var10 - 0.1, var8 + 0.1, var3.minY, var10 + 0.1))) {
            return false;
         }
      }

      return true;
   }

   public static float[] orbitKeys(Vec3 var0, Vec3 var1, double var2, int var4, float var5) {
      return Double.isFinite(var2)
            && !(var2 <= 0.0)
            && Float.isFinite(var5)
            && Double.isFinite(var0.x)
            && Double.isFinite(var0.z)
            && Double.isFinite(var1.x)
            && Double.isFinite(var1.z)
         ? AuraModule.getMoveKeys(shouldAutoJump(var0, var1, var2, var4, var5), var5)
         : noKeys;
   }

   public static double shouldAutoJump(Vec3 var0, Vec3 var1, double var2, int var4, float var5) {
      double var6 = var0.x - var1.x;
      double var8 = var0.z - var1.z;
      double var10 = Math.hypot(var6, var8);
      if (var10 < 1.0E-4) {
         return var5;
      }

      double var12 = var6 / var10;
      double var14 = var8 / var10;
      double var16 = var4 >= 0 ? 1.0 : -1.0;
      double var18 = Math.clamp((var10 - var2) * (Math.PI / 3), -Math.PI / 2, Math.PI / 2);
      double var20 = -var14 * var16 * Math.cos(var18) - var12 * Math.sin(var18);
      double var22 = var12 * var16 * Math.cos(var18) - var14 * Math.sin(var18);
      return Mth.wrapDegrees(Math.toDegrees(Math.atan2(var22, var20)) - 90.0);
   }

   public static Vec3 nativeInput(Vec3 var0, Vec3 var1, double var2, double var4, int var6, float var7) {
      if (Double.isFinite(var2)
         && Double.isFinite(var4)
         && !(var2 <= 0.0)
         && !(var4 <= 0.0)
         && Double.isFinite(var0.x)
         && Double.isFinite(var0.z)
         && Double.isFinite(var1.x)
         && Double.isFinite(var1.z)
         && Float.isFinite(var7)) {
         double var8 = var0.x - var1.x;
         double var10 = var0.z - var1.z;
         double var12 = var8 * var8 + var10 * var10 < 1.0E-8 ? Math.toRadians(var7) : Math.atan2(var10, var8);
         var12 += (var6 >= 0 ? 1 : -1) * 2 * Math.asin(Math.min(1.0, var4 / (2.0 * var2)));
         Vec3 var14 = new Vec3(var1.x + Math.cos(var12) * var2 - var0.x, 0.0, var1.z + Math.sin(var12) * var2 - var0.z);
         double var15 = var14.length();
         return var15 > var4 ? var14.scale(var4 / var15) : var14;
      } else {
         return Vec3.ZERO;
      }
   }

   public static double horizontalReach(Vec3 var0, AABB var1, double var2, double var4) {
      double var6 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var8 = Math.max(0.0, var4 - 0.25);
      double var10 = var8 * var8 - var6 * var6;
      return Double.isFinite(var10) && !(var10 <= 0.0)
         ? Math.max(0.0, Math.min(var2, Math.sqrt(var10) + Math.min(var1.getXsize(), var1.getZsize()) / 2.0))
         : 0.0;
   }

   public static Vec2 onPositionCorrection(Vec3 var0, float var1) {
      if (var0.horizontalDistanceSqr() < 1.0E-8) {
         return Vec2.ZERO;
      }

      Vec3 var2 = var0.normalize();
      double var3 = Math.toRadians(var1);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2((float)(var5 * var2.x + var7 * var2.z), (float)(-var7 * var2.x + var5 * var2.z));
   }
}
