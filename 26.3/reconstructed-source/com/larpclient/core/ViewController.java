package com.larpclient.core;

import com.larpclient.mixin.KeyMappingMixin;
import com.larpclient.mixin.MinecraftMixin_3;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.combat.AutoAnchorModule;
import com.larpclient.module.combat.AutoCrystalModule;
import com.larpclient.module.combat.AutoPearlCatchModule;
import com.larpclient.module.combat.AutoTrap;
import com.larpclient.module.combat.AutoWebModule;
import com.larpclient.module.combat.MaceModule;
import com.larpclient.module.combat.RotationMath;
import com.larpclient.module.movement.FreecamInput;
import com.larpclient.module.movement.WindChargeHopModule;
import com.larpclient.module.player.ScaffoldModule;
import com.larpclient.module.render.FreecamModule;
import com.larpclient.module.world.AutoBuilderModule;
import com.larpclient.util.entity.RotationController;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

public final class ViewController {
   private static final ViewController.ViewState state = new ViewController.ViewState();
   private static CameraType savedCameraType;
   private static CameraType lastCameraType;
   private static float yaw;
   private static float pitch;
   private static Vec3 position = Vec3.ZERO;
   private static Vec3 previousPosition = Vec3.ZERO;
   public static final int rETURN_TICK_LIMIT = 40;
   private static RotationController returnTurn;
   private static double returnSpeed;
   private static boolean returnFlag;
   private static ViewController.Owner returnOwner = ViewController.Owner.NONE;
   private static int returnTicks;

   public static boolean canAcquire(ViewController.Owner var0) {
      update();
      return state.canAcquire(var0);
   }

   public static void acquire(ViewController.Owner var0) {
      Minecraft var1 = Minecraft.getInstance();
      update();
      if (state.canAcquire(var0)) {
         if (state.owner == var0) {
            if (!logReturn(var0)) {
               applyCameraType(var0);
            }
         } else if (state.owner == ViewController.Owner.RETURN && logReturn(var0)) {
            logReturn("taken over by " + var0);
            clearReturn();
            state.isAimOwner(var0, var1.player, var1.level);
            applyCameraType("acquired");
         } else {
            if (logReturn(state.owner)) {
               release(state.owner);
            }

            if (state.owner == ViewController.Owner.NONE && !logReturn(var0)) {
               savedCameraType = var1.options.getCameraType();
            }

            if (state.isAimOwner(var0, var1.player, var1.level)) {
               yaw = var1.player.getYRot();
               pitch = var1.player.getXRot();
               lastCameraType = var1.options.getCameraType();
               if (logReturn(var0) && lastCameraType.isMirrored()) {
                  yaw += 180.0F;
                  pitch = -pitch;
               }

               previousPosition = position = var0 == ViewController.Owner.FREECAM
                  ? var1.player.position().add(0.0, var1.player.getEyeHeight(Pose.STANDING), 0.0)
                  : var1.player.getEyePosition();
               if (!logReturn(var0)) {
                  applyCameraType(var0);
               } else {
                  applyCameraType("acquired");
               }
            }
         }
      }
   }

   public static void release(ViewController.Owner var0) {
      if (state.owner == var0) {
         Minecraft var1 = Minecraft.getInstance();
         if (logReturn(var0) && state.isBoundTo(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
            syncMirroredView();
            float[] var2 = toPlayerRotation(yaw, pitch, var1.options.getCameraType().isMirrored());
            var2[0] = var1.player.getYRot() + Mth.wrapDegrees(var2[0] - var1.player.getYRot());
            var1.player.setYRot(var2[0]);
            var1.player.setXRot(var2[1]);
            if (var0 == ViewController.Owner.SCAFFOLD) {
               var1.player.setYHeadRot(var2[0]);
            }

            var1.player.yRotO = var2[0];
            var1.player.xRotO = var2[1];
         }

         reset();
         if (var0 == ViewController.Owner.FREELOOK || var0 == ViewController.Owner.FREECAM) {
            ScaffoldModule.onCameraEnded();
         }
      }
   }

   public static void requestReturn(ViewController.Owner var0, RotationController var1, double var2, boolean var4) {
      if (state.owner == var0 && var0 != ViewController.Owner.RETURN) {
         if (var1 != null && !(var2 >= 180.0) && logReturn(var0) && canControl()) {
            Minecraft var5 = Minecraft.getInstance();
            syncMirroredView();
            float[] var6 = toPlayerRotation(yaw, pitch, var5.options.getCameraType().isMirrored());
            if (RotationController.isOnTarget(var5.player.getYRot(), var5.player.getXRot(), var6[0], var6[1], RotationMath.mouseStep())) {
               reset();
            } else {
               state.beginReturn(var0);
               returnTurn = var1;
               returnSpeed = var2;
               returnFlag = var4;
               returnOwner = var0;
               returnTicks = 0;
               var1.clampStep();
            }
         } else {
            release(var0);
         }
      }
   }

   public static void tickReturn() {
      if (update() == ViewController.Owner.RETURN) {
         Minecraft var0 = Minecraft.getInstance();
         LocalPlayer var1 = var0.player;
         float[] var2 = toPlayerRotation(yaw, pitch, var0.options.getCameraType().isMirrored());
         float var3 = RotationMath.mouseStep();
         if (RotationController.isOnTarget(var1.getYRot(), var1.getXRot(), var2[0], var2[1], var3)) {
            logReturn("done, " + returnTurn.summarize());
            reset();
         } else if (++returnTicks > 40) {
            logReturn("not on your look after 40 ticks, so handed back at once; " + returnTurn.summarize());
            release(ViewController.Owner.RETURN);
         } else {
            float[] var4 = returnTurn.rotateTowardSmoothed(var1.getYRot(), var1.getXRot(), var2[0], var2[1], returnSpeed, returnFlag, var3);
            RotationManager.requestRotation(RotationManager.Priority.VIEW, var4[0], var4[1], 1.0, var1.getYRot(), var1.getXRot());
         }
      }
   }

   public static void checkReturnInterrupted() {
      if (state.owner == ViewController.Owner.RETURN && RotationManager.hasRequest() && !RotationManager.isActive(RotationManager.Priority.VIEW)) {
         logReturn("ended by another module's turn; " + returnTurn.summarize());
         reset();
      }
   }

   public static void checkClickInterrupt() {
      if (state.owner == ViewController.Owner.RETURN) {
         Minecraft var0 = Minecraft.getInstance();
         if (isKeyActive(var0.options.keyAttack) || isKeyActive(var0.options.keyUse)) {
            finishReturn("your click");
            if (var0.player != null && var0.level != null) {
               ((MinecraftMixin_3)var0).larpPick(1.0F);
            }
         }
      }
   }

   public static void finishReturn(String var0) {
      if (state.owner == ViewController.Owner.RETURN) {
         logReturn("finished at once for " + var0 + "; " + returnTurn.summarize());
         release(ViewController.Owner.RETURN);
      }
   }

   private static boolean isKeyActive(KeyMapping var0) {
      return var0.isDown() || ((KeyMappingMixin)var0).larpClickCount() > 0;
   }

   public static boolean isReturning() {
      return state.owner == ViewController.Owner.RETURN;
   }

   private static void clearReturn() {
      returnTurn = null;
      returnOwner = ViewController.Owner.NONE;
      returnTicks = 0;
   }

   private static void logReturn(String var0) {
      String var1 = switch (returnOwner) {
         case AUTO_BUILDER -> "Auto Builder";
         default -> "Camera";
         case MACE -> "Mace";
         case WIND_HOP -> "WindHop";
         case PEARL_CATCH -> "PearlCatch";
      };
      LarpLog.info(var1, "Return to your look {}", var0);
   }

   private static boolean canControl() {
      Minecraft var0 = Minecraft.getInstance();
      return state.isBoundTo(var0.player, var0.level, var0.player != null && var0.player.isAlive())
         && var0.gui.screen() == null
         && var0.isWindowActive()
         && !var0.player.isSpectator();
   }

   public static void reset() {
      if (logReturn(state.owner)) {
         applyCameraType("released");
         fixHandSway();
      }

      if (savedCameraType != null) {
         Minecraft.getInstance().options.setCameraType(savedCameraType);
      }

      savedCameraType = null;
      lastCameraType = null;
      state.release(state.owner);
      clearReturn();
      previousPosition = position = Vec3.ZERO;
      pitch = 0.0F;
      yaw = 0.0F;
   }

   public static ViewController.Owner update() {
      if (state.owner == ViewController.Owner.SCAFFOLD) {
         ScaffoldModule var0 = ScaffoldModule.getInstance();
         if (!canControl() || var0 == null || !var0.isAiming()) {
            release(ViewController.Owner.SCAFFOLD);
         }
      }

      if (state.owner == ViewController.Owner.AUTO_TRAP) {
         AutoTrap var1 = AutoTrap.getInstance();
         if (!canControl() || var1 == null || !var1.isAimActive()) {
            release(ViewController.Owner.AUTO_TRAP);
         }
      }

      if (state.owner == ViewController.Owner.AUTO_WEB) {
         AutoWebModule var2 = AutoWebModule.getInstance();
         if (!canControl() || var2 == null || !var2.isRotationVisible()) {
            release(ViewController.Owner.AUTO_WEB);
         }
      }

      if (state.owner == ViewController.Owner.AUTO_ANCHOR) {
         AutoAnchorModule var3 = AutoAnchorModule.getInstance();
         if (!canControl() || var3 == null || !var3.isRotationActive()) {
            release(ViewController.Owner.AUTO_ANCHOR);
         }
      }

      if (state.owner == ViewController.Owner.AURA) {
         AuraModule var4 = AuraModule.getInstance();
         if (!canControl() || var4 == null || !var4.isTargeting()) {
            release(ViewController.Owner.AURA);
         }
      }

      if (state.owner == ViewController.Owner.MACE) {
         MaceModule var5 = MaceModule.getInstance();
         if (!canControl() || var5 == null) {
            release(ViewController.Owner.MACE);
         } else if (!var5.isSilentAimActive()) {
            requestReturn(ViewController.Owner.MACE, var5.getRotationController(), var5.shouldDelaySmash(), true);
         }
      }

      if (state.owner == ViewController.Owner.AUTO_CRYSTAL) {
         AutoCrystalModule var6 = AutoCrystalModule.placeCrystal();
         if (!canControl() || var6 == null || !var6.isRotating()) {
            release(ViewController.Owner.AUTO_CRYSTAL);
         }
      }

      if (state.owner == ViewController.Owner.PEARL_CATCH) {
         AutoPearlCatchModule var7 = AutoPearlCatchModule.getInstance();
         if (!canControl() || var7 == null) {
            release(ViewController.Owner.PEARL_CATCH);
         } else if (!var7.isActive()) {
            requestReturn(ViewController.Owner.PEARL_CATCH, var7.getRotationController(), var7.getReturnSpeed(), var7.isHumanize());
         }
      }

      if (state.owner == ViewController.Owner.WIND_HOP) {
         WindChargeHopModule var8 = WindChargeHopModule.getInstance();
         if (!canControl() || var8 == null) {
            release(ViewController.Owner.WIND_HOP);
         } else if (!var8.isThrowing()) {
            requestReturn(ViewController.Owner.WIND_HOP, var8.getRotationController(), var8.onKeyPress(), var8.isHumanize());
         }
      }

      if (state.owner == ViewController.Owner.AUTO_BUILDER) {
         AutoBuilderModule var9 = AutoBuilderModule.getInstance();
         if (!canControl() || var9 == null) {
            release(ViewController.Owner.AUTO_BUILDER);
         } else if (!var9.isAggressiveActive()) {
            requestReturn(ViewController.Owner.AUTO_BUILDER, var9.getRotationController(), var9.getMaterialNeeds(), true);
         }
      }

      if (state.owner == ViewController.Owner.RETURN && !canControl()) {
         release(ViewController.Owner.RETURN);
      }

      Minecraft var10 = Minecraft.getInstance();
      if (state.owner != ViewController.Owner.NONE && !state.isBoundTo(var10.player, var10.level, var10.player != null && var10.player.isAlive())) {
         reset();
      }

      if (logReturn(state.owner)) {
         syncMirroredView();
      }

      return state.owner;
   }

   private static boolean logReturn(ViewController.Owner var0) {
      return var0 == ViewController.Owner.AURA
         || var0 == ViewController.Owner.MACE
         || var0 == ViewController.Owner.AUTO_TRAP
         || var0 == ViewController.Owner.AUTO_WEB
         || var0 == ViewController.Owner.AUTO_ANCHOR
         || var0 == ViewController.Owner.AUTO_CRYSTAL
         || var0 == ViewController.Owner.SCAFFOLD
         || var0 == ViewController.Owner.PEARL_CATCH
         || var0 == ViewController.Owner.WIND_HOP
         || var0 == ViewController.Owner.RETURN
         || var0 == ViewController.Owner.AUTO_BUILDER;
   }

   public static boolean isActive() {
      return logReturn(update());
   }

   private static void syncMirroredView() {
      CameraType var0 = Minecraft.getInstance().options.getCameraType();
      if (lastCameraType != null && lastCameraType.isMirrored() != var0.isMirrored()) {
         yaw = yaw + (var0.isMirrored() ? 180.0F : -180.0F);
         pitch = -pitch;
      }

      lastCameraType = var0;
   }

   public static boolean isFreecam() {
      return update() == ViewController.Owner.FREECAM;
   }

   public static float getYaw(float var0) {
      return !isActive() ? Float.NaN : unwrapYaw(toPlayerRotation(yaw, pitch, Minecraft.getInstance().options.getCameraType().isMirrored())[0], var0);
   }

   public static float getPitch() {
      return !isActive() ? Float.NaN : toPlayerRotation(yaw, pitch, Minecraft.getInstance().options.getCameraType().isMirrored())[1];
   }

   public static float unwrapYaw(float var0, float var1) {
      return var1 + Mth.wrapDegrees(var0 - var1);
   }

   public static float finishReturn(float var0, float var1) {
      return Math.round((var1 - var0) / 360.0F) * 360.0F;
   }

   private static void fixHandSway() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null) {
         float var1 = finishReturn(var0.yBob, var0.getYRot());
         if (var1 != 0.0F) {
            var0.yBob += var1;
            var0.yBobO += var1;
            LarpLog.debug("Camera", "Hand sway moved {} degrees back onto the player's rotation", var1);
         }
      }
   }

   public static void rotate(double var0, double var2) {
      yaw += (float)var0 * 0.15F;
      float var4 = isFreecam() ? 90.0F : 89.0F;
      pitch = Mth.clamp(pitch + (float)var2 * 0.15F, -var4, var4);
   }

   public static float getRawYaw() {
      return yaw;
   }

   public static float getPlayerYaw() {
      return Minecraft.getInstance().options.getCameraType().isMirrored() ? yaw - 180.0F : yaw;
   }

   public static float getPosition() {
      return pitch;
   }

   public static Vec3 getPosition(float var0) {
      return previousPosition.lerp(position, var0);
   }

   public static void isFreecam(double var0, double var2, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      previousPosition = position;
      if (isFreecam() && var5.gui.screen() == null && var5.isWindowActive()) {
         position = position.add(FreecamInput.computeVelocity(yaw, FreecamModule.readKeyboardInput(), var0, var2, var4));
      }
   }

   private static void applyCameraType(ViewController.Owner var0) {
      Minecraft var1 = Minecraft.getInstance();
      CameraType var2 = savedCameraType == null ? var1.options.getCameraType() : savedCameraType;
      CameraType var3 = var0 == ViewController.Owner.FREECAM
         ? (FreecamModule.shouldShowBody() && var2.isFirstPerson() ? CameraType.THIRD_PERSON_BACK : var2)
         : CameraType.THIRD_PERSON_BACK;
      if (var1.options.getCameraType() != var3) {
         var1.options.setCameraType(var3);
      }
   }

   public static float[] toPlayerRotation(float var0, float var1, boolean var2) {
      return new float[]{var2 ? var0 - 180.0F : var0, var2 ? -var1 : var1};
   }

   private static void applyCameraType(String var0) {
      if (state.owner != ViewController.Owner.RETURN) {
         if (state.owner == ViewController.Owner.SCAFFOLD) {
            LarpLog.debug("Scaffold", "Independent view {}", var0);
         } else if (state.owner == ViewController.Owner.AUTO_TRAP) {
            LarpLog.info("AutoTrap", "Independent view {}", var0);
         } else if (state.owner == ViewController.Owner.AUTO_WEB) {
            LarpLog.info("AutoWeb", "Independent view {}", var0);
         } else if (state.owner == ViewController.Owner.AUTO_ANCHOR) {
            LarpLog.info("AutoAnchor", "Independent view {}", var0);
         } else if (state.owner == ViewController.Owner.AUTO_CRYSTAL) {
            LarpLog.debug("AutoCrystal", "Independent view {}", var0);
         } else if (state.owner == ViewController.Owner.PEARL_CATCH) {
            LarpLog.info("PearlCatch", "Silent view {}", var0);
         } else if (state.owner == ViewController.Owner.WIND_HOP) {
            LarpLog.info("WindHop", "Silent view {}", var0);
         } else {
            if (state.owner == ViewController.Owner.AURA) {
               LarpLog.debug("Aura", "Independent view {}", var0);
            }

            if (state.owner == ViewController.Owner.MACE) {
               LarpLog.debug("Mace", "Independent view {}", var0);
            }
         }
      }
   }

   private ViewController() {
   }

   public enum Owner {
      NONE,
      RETURN,
      AUTO_BUILDER,
      SCAFFOLD,
      AUTO_TRAP,
      AUTO_WEB,
      AUTO_ANCHOR,
      AURA,
      MACE,
      AUTO_CRYSTAL,
      WIND_HOP,
      PEARL_CATCH,
      FREELOOK,
      FREECAM;
   }

   public static final class ViewState {
      private ViewController.Owner owner = ViewController.Owner.NONE;
      private Object player;
      private Object level;

      public ViewController.Owner getOwner() {
         return this.owner;
      }

      public boolean canAcquire(ViewController.Owner var1) {
         if (var1 == ViewController.Owner.RETURN) {
            return false;
         } else if (this.owner == ViewController.Owner.RETURN) {
            return true;
         } else if (var1 == ViewController.Owner.AUTO_BUILDER) {
            return this.owner == ViewController.Owner.NONE || this.owner == var1;
         } else if (var1 == ViewController.Owner.SCAFFOLD) {
            return this.owner == ViewController.Owner.NONE || this.owner == ViewController.Owner.AUTO_BUILDER || this.owner == var1;
         } else if (isAimOwner(var1)) {
            return this.owner == ViewController.Owner.NONE
               || this.owner == ViewController.Owner.SCAFFOLD
               || this.owner == ViewController.Owner.AUTO_BUILDER
               || isAimOwner(this.owner);
         } else if (var1 == ViewController.Owner.AURA) {
            return this.owner == ViewController.Owner.NONE
               || this.owner == ViewController.Owner.SCAFFOLD
               || this.owner == ViewController.Owner.AUTO_BUILDER
               || isAimOwner(this.owner)
               || this.owner == var1;
         } else if (var1 == ViewController.Owner.MACE) {
            return this.owner == ViewController.Owner.NONE
               || this.owner == ViewController.Owner.SCAFFOLD
               || this.owner == ViewController.Owner.AUTO_BUILDER
               || isAimOwner(this.owner)
               || this.owner == ViewController.Owner.AURA
               || this.owner == var1;
         } else if (var1 == ViewController.Owner.AUTO_CRYSTAL) {
            return this.owner == ViewController.Owner.NONE
               || this.owner == ViewController.Owner.SCAFFOLD
               || this.owner == ViewController.Owner.AUTO_BUILDER
               || isAimOwner(this.owner)
               || this.owner == ViewController.Owner.AURA
               || this.owner == var1;
         } else if (var1 == ViewController.Owner.WIND_HOP) {
            return this.owner != ViewController.Owner.FREELOOK && this.owner != ViewController.Owner.FREECAM && this.owner != ViewController.Owner.PEARL_CATCH;
         } else if (var1 != ViewController.Owner.PEARL_CATCH) {
            return var1 == ViewController.Owner.FREELOOK ? this.owner != ViewController.Owner.FREECAM : true;
         } else {
            return this.owner != ViewController.Owner.FREELOOK && this.owner != ViewController.Owner.FREECAM;
         }
      }

      private static boolean isAimOwner(ViewController.Owner var0) {
         return var0 == ViewController.Owner.AUTO_TRAP || var0 == ViewController.Owner.AUTO_WEB || var0 == ViewController.Owner.AUTO_ANCHOR;
      }

      public boolean isAimOwner(ViewController.Owner var1, Object var2, Object var3) {
         if (!this.canAcquire(var1)) {
            return false;
         }

         this.owner = var1;
         this.player = var2;
         this.level = var3;
         return true;
      }

      public boolean beginReturn(ViewController.Owner var1) {
         if (this.owner == var1 && var1 != ViewController.Owner.NONE && var1 != ViewController.Owner.RETURN) {
            this.owner = ViewController.Owner.RETURN;
            return true;
         } else {
            return false;
         }
      }

      public boolean isBoundTo(Object var1, Object var2, boolean var3) {
         return var1 != null && var3 && this.player == var1 && this.level == var2;
      }

      public boolean release(ViewController.Owner var1) {
         if (this.owner != var1) {
            return false;
         }

         this.owner = ViewController.Owner.NONE;
         this.player = this.level = null;
         return true;
      }
   }
}
