package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.math.AimAssistMath;
import com.larpclient.util.math.AimMath;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AimAssistModule extends Module {
   private static AimAssistModule instance;
   private final NumberSetting range = this.addSetting(new NumberSetting("Anubis Range", "Maximum eye-to-hitbox acquisition distance", 4.0, 3.0, 6.0, 0.1) {
      @Override
      public String getDisplayName() {
         return "Range";
      }
   });
   private final NumberSetting fov = this.addSetting(new NumberSetting("Anubis FOV", "Total acquisition width around the crosshair", 60.0, 10.0, 180.0, 5.0) {
      @Override
      public String getDisplayName() {
         return "FOV";
      }
   });
   private final NumberSetting speed = this.addSetting(
      new NumberSetting("Anubis Speed", "Maximum correction in degrees per second, varied slightly for each target", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "Speed";
         }
      }
   );
   private final BooleanSetting weaponsOnly = this.addSetting(new BooleanSetting("Anubis Weapons Only", "Require a sword, axe, spear, mace or trident", true) {
      @Override
      public String getDisplayName() {
         return "Weapons Only";
      }
   });
   private final BooleanSetting vertical = this.addSetting(new BooleanSetting("Anubis Vertical", "Also ease pitch toward the target's core", true) {
      @Override
      public String getDisplayName() {
         return "Vertical";
      }
   });
   private final AimAssistMath.MouseStepper mouseStepper = new AimAssistMath.MouseStepper();
   private Player target;
   private Player lastAttacked;
   private long lastAttackTimeMs;
   private long lastLogTimeMs;
   private double speedJitter = 1.0;
   private LocalPlayer trackedPlayer;
   private Object trackedLevel;
   private String status = "Waiting for input";

   public AimAssistModule() {
      super("Aim Assist", "Smooth visible aim correction while you actively aim", Category.COMBAT, -1);
      instance = this;

      for (Setting var2 : this.getSettings()) {
         var2.onChange(var1 -> this.resetState("Settings changed"));
      }

      this.registerRetiredSettings();
   }

   private void registerRetiredSettings() {
      for (Setting var2 : List.of(
         new BooleanSetting("Require Attack", "Retired profile value", true),
         new BooleanSetting("Weapons Only", "Retired profile value", true),
         new BooleanSetting("Players Only", "Retired profile value", true),
         new BooleanSetting("Vertical Assistance", "Retired profile value", false),
         new NumberSetting("Range", "Retired profile value", 3.0, 0.5, 6.0, 0.25),
         new NumberSetting("Aim Cone", "Retired profile value", 60.0, 10.0, 180.0, 5.0),
         new NumberSetting("Correction Speed", "Retired profile value", 60.0, 5.0, 180.0, 5.0)
      )) {
         this.addSetting(var2).visibleWhen(() -> false);
      }
   }

   private String getPauseReason() {
      Minecraft var1 = getMc();
      if (var1.player == null || var1.level == null || var1.gameMode == null) {
         return "Waiting for world";
      }

      if (!var1.player.isAlive() || var1.player.isSpectator() || var1.player.isSleeping()) {
         return "Paused: player unavailable";
      }

      if (var1.gui.screen() != null || var1.gui.overlay() != null) {
         return "Paused: menu open";
      }

      if (var1.isWindowActive() && var1.mouseHandler.isMouseGrabbed()) {
         if (var1.getCameraEntity() != var1.player || ViewController.update() != ViewController.Owner.NONE || RotationManager.hasRequest()) {
            return "Paused: rotation owned";
         } else if (var1.player.isUsingItem() || var1.options.keyUse.isDown()) {
            return "Paused: using an item";
         } else {
            return this.weaponsOnly.getValue() && !isHoldingMeleeWeapon(var1.player) ? "Waiting for a melee weapon" : null;
         }
      } else {
         return "Paused: mouse not captured";
      }
   }

   private static boolean isHoldingMeleeWeapon(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.SPEARS) || var1.is(Items.MACE) || var1.is(Items.TRIDENT);
   }

   public static void onAttack(Player var0, Entity var1) {
      if (instance != null && instance.isEnabled()) {
         AimAssistModule var2 = instance;
         Minecraft var3 = getMc();
         if (var2 != null && var0 == var3.player && var1 instanceof Player var4 && var4.isAlive() && !var4.isRemoved() && var4.level() == var3.level) {
            var2.lastAttacked = var4;
            var2.lastAttackTimeMs = System.nanoTime() / 1000000L;
         }
      }
   }

   public static AimAssistMath.MouseDelta applyMouseCorrection(LocalPlayer var0, double var1, double var3, double var5) {
      if (instance != null && instance.isEnabled()) {
         AimAssistModule var7 = instance;
         if (var7 != null && var7.isRuntimeActive()) {
            try {
               return var7.computeMouseDelta(var0, var1, var3, var5);
            } catch (RuntimeException var9) {
               var7.resetState("Paused: aim correction failed");
               LarpLog.error("AimAssist", "Mouse correction failed; disabling and retaining native input", var9);
               var7.disable();
               return AimAssistMath.MouseDelta.STILL;
            }
         } else {
            return AimAssistMath.MouseDelta.STILL;
         }
      } else {
         return AimAssistMath.MouseDelta.STILL;
      }
   }

   private AimAssistMath.MouseDelta computeMouseDelta(LocalPlayer var1, double var2, double var4, double var6) {
      String var8 = this.getPauseReason();
      if (var8 == null && var1 == minecraft.player) {
         this.findTarget(var1);
         long var9 = System.nanoTime() / 1000000L;
         int var11 = var2 == 0.0 && var4 == 0.0 && !this.mouseStepper.isInputRecent(var9) ? 0 : 1;
         if (var11 == 0) {
            this.mouseStepper.reset();
            this.setStatus("Waiting for mouse movement");
            return AimAssistMath.MouseDelta.STILL;
         }

         float[] var12 = AimAssistMath.applyMouseDelta(var1.getYRot(), var1.getXRot(), var2, var4);
         Vec3 var13 = Vec3.directionFromRotation(var12[1], var12[0]);
         this.setTarget(this.findTarget(var1, var9, var12[0], var12[1], var13));
         float[] var14 = this.target == null
            ? null
            : AimAssistMath.computeRotationToBox(var1.getEyePosition(), var13, AimAssistMath.shrinkBox(this.target.getBoundingBox()), this.range.getValue());
         double var15 = var14 == null ? 0.0 : AimMath.wrapDegrees(var14[0] - var12[0]);
         double var17 = var14 == null ? 0.0 : var14[1] - var12[1];
         double var19 = AimAssistMath.calculateGcdStep(
            (Double)minecraft.options.sensitivity().get(), minecraft.options.getCameraType().isFirstPerson(), var1.isScoping()
         );
         AimAssistMath.MouseDelta var21 = this.mouseStepper
            .step(var2, var4, var15, var17, var14 != null, this.vertical.getValue(), var6, this.speed.getValue(), this.speedJitter, var19, var9);
         if (this.target == null) {
            this.setStatus("No eligible target");
         } else if (var14 == null) {
            this.setStatus("On target");
         } else if (this.mouseStepper.opposesInput(var15, var17, this.vertical.getValue())) {
            this.setStatus("Following your mouse direction");
         } else {
            this.setStatus("Assisting " + this.target.getName().getString());
         }

         return var21;
      } else {
         this.resetState(var8 == null ? "Waiting for input" : var8);
         return AimAssistMath.MouseDelta.STILL;
      }
   }

   private void findTarget(LocalPlayer var1) {
      if (this.trackedPlayer != var1 || this.trackedLevel != minecraft.level) {
         if (this.trackedPlayer != null || this.lastAttacked != null && this.lastAttacked.level() != minecraft.level) {
            this.lastAttacked = null;
         }

         this.resetState("Waiting for input");
         this.trackedPlayer = var1;
         this.trackedLevel = minecraft.level;
      }
   }

   private Player findTarget(LocalPlayer var1, long var2, float var4, float var5, Vec3 var6) {
      ArrayList var7 = new ArrayList(2);
      long var8 = var2 - this.lastAttackTimeMs;
      if (var8 >= 0L && var8 <= 1500L) {
         AimAssistMath.Candidate var10 = this.evaluateCandidate(this.lastAttacked, var4, var5, var6);
         if (var10 != null) {
            var7.add(var10);
         }
      }

      AimAssistMath.Candidate var16 = this.evaluateCandidate(this.target, var4, var5, var6);
      if (var16 != null) {
         var7.add(var16);
      }

      Player var11 = AimAssistMath.selectTarget(var7, this.lastAttacked, var8, this.target, this.range.getValue(), this.fov.getValue());
      if (var11 != null) {
         return var11;
      }

      ArrayList var12 = new ArrayList();

      for (Player var14 : minecraft.level.getEntitiesOfClass(Player.class, var1.getBoundingBox().inflate(this.range.getValue() + 0.5))) {
         AimAssistMath.Candidate var15 = this.evaluateCandidate(var14, var4, var5, var6);
         if (var15 != null) {
            var12.add(var15);
         }
      }

      return AimAssistMath.selectTarget(var12, this.lastAttacked, var2 - this.lastAttackTimeMs, this.target, this.range.getValue(), this.fov.getValue());
   }

   private void setTarget(Player var1) {
      if (this.target != var1) {
         this.target = var1;
         this.speedJitter = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
         this.mouseStepper.clearRemainders();
      }
   }

   private AimAssistMath.Candidate<Player> evaluateCandidate(Player var1, float var2, float var3, Vec3 var4) {
      if (var1 != null
         && var1 != minecraft.player
         && var1.isAlive()
         && !var1.isRemoved()
         && var1.level() == minecraft.level
         && !var1.isSpectator()
         && !var1.isCreative()
         && var1.isAttackable()
         && !var1.isInvisible()
         && minecraft.player.getVehicle() != var1
         && minecraft.player.canHarmPlayer(var1)
         && minecraft.player.connection.getPlayerInfo(var1.getUUID()) != null) {
         LarpClient var5 = LarpClient.getInstance();
         MCFriendModule var6 = var5 != null && var5.getModuleManager() != null ? var5.getModuleManager().getModule(MCFriendModule.class) : null;
         if (var6 != null && var6.isFriend(var1.getName().getString())) {
            return null;
         }

         Vec3 var7 = minecraft.player.getEyePosition();
         double var8 = var7.distanceTo(AimAssistMath.clampToBox(var7, var1.getBoundingBox()));
         if (var8 > this.range.getValue() + 0.5) {
            return null;
         }

         AABB var10 = AimAssistMath.shrinkBox(var1.getBoundingBox());
         float[] var11 = AimAssistMath.computeRotationToBox(var7, var4, var10, this.range.getValue());
         double var12 = AimAssistMath.rotationDistance(var2, var3, var11);
         if (var12 > this.fov.getValue() / 2.0 * 1.3) {
            return null;
         }

         Vec3 var14 = AimAssistMath.findAimPoint(var7, var4, var10, this.range.getValue());
         BlockHitResult var15 = minecraft.level.clip(new ClipContext(var7, var14, Block.COLLIDER, Fluid.NONE, minecraft.player));
         return var15.getType() != Type.MISS && var15.getLocation().distanceToSqr(var7) < var14.distanceToSqr(var7) - 0.001
            ? null
            : new AimAssistMath.Candidate<>(var1, var8, var12);
      } else {
         return null;
      }
   }

   private void setStatus(String var1) {
      if (!this.status.equals(var1)) {
         this.status = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.lastLogTimeMs >= 1000L) {
            this.lastLogTimeMs = var2;
            LarpLog.debug("AimAssist", "{}", var1);
         }
      }
   }

   private void resetState(String var1) {
      this.mouseStepper.reset();
      this.target = null;
      this.setStatus(var1);
   }

   public static void resetFor(LocalPlayer var0) {
      AimAssistModule var1 = instance;
      if (var1 != null && var1.trackedPlayer == var0) {
         var1.resetState("Waiting for input");
      }
   }

   public static void updateState() {
      AimAssistModule var0 = instance;
      if (var0 != null) {
         Minecraft var1 = getMc();
         if (var0.lastAttacked != null
            && (
               !var0.lastAttacked.isAlive()
                  || var0.lastAttacked.isRemoved()
                  || var0.lastAttacked.level() != var1.level
                  || System.nanoTime() / 1000000L - var0.lastAttackTimeMs > 30000L
            )) {
            var0.lastAttacked = null;
         }

         if (!var0.isRuntimeActive()) {
            var0.resetState("Waiting for input");
         } else {
            String var2 = var0.getPauseReason();
            if (var2 != null || var0.trackedPlayer != var1.player || var0.trackedLevel != var1.level) {
               var0.resetState(var2 == null ? "Waiting for input" : var2);
            }
         }
      }
   }

   @Override
   public void onTick() {
      updateState();
   }

   @Override
   public void onEnable() {
      this.resetState("Waiting for input");
      LarpLog.debug(
         "AimAssist",
         "Aim assistance active; range={}, FOV={}, speed={}, weapons={}, vertical={}",
         this.range.getValue(),
         this.fov.getValue(),
         this.speed.getValue(),
         this.weaponsOnly.getValue(),
         this.vertical.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.resetState("Waiting for input");
      this.trackedPlayer = null;
      this.trackedLevel = null;
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
