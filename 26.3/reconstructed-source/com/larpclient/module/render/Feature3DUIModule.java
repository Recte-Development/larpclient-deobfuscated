package com.larpclient.module.render;

import com.larpclient.core.ViewController;
import com.larpclient.mixin.AbstractContainerScreenMixin_3;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.overlay.ScreenWarp;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import java.util.Locale;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class Feature3DUIModule extends Module {
   private static Feature3DUIModule instance;
   private final ModeSetting screens = this.addSetting(new ModeSetting("Screens", "Which menus open in 3D", "Inventory", "Inventory", "All Containers"));
   private final ModeSetting side = this.addSetting(
      new ModeSetting("Side", "Screen side for the menu; the camera moves over the opposite shoulder", "Right", "Right", "Left")
   );
   private final NumberSetting menuAngle = this.addSetting(
      new NumberSetting("Menu Angle", "Degrees the menu turns toward the center of the view", 14.0, 0.0, 45.0, 1.0)
   );
   private final NumberSetting menuScreenHeight = this.addSetting(
      new NumberSetting("Menu Screen Height", "On-screen menu height, tabs included, as a percent of the screen", 76.0, 30.0, 90.0, 1.0)
   );
   private final NumberSetting cameraDistance = this.addSetting(
      new NumberSetting("Camera Distance", "Camera distance behind the player in blocks", 1.1, 0.6, 4.0, 0.1)
   );
   private final NumberSetting cameraOffset = this.addSetting(new NumberSetting("Camera Offset", "Sideways camera offset in blocks", 0.5, 0.0, 1.5, 0.05));
   private final NumberSetting cameraHeight = this.addSetting(
      new NumberSetting("Camera Height", "Camera height relative to the torso in blocks", 0.1, -0.6, 0.6, 0.05)
   );
   private final NumberSetting cameraPitch = this.addSetting(
      new NumberSetting("Camera Pitch", "Camera pitch in degrees; positive looks down", 3.0, -15.0, 40.0, 1.0)
   );
   private final NumberSetting transitionMs = this.addSetting(
      new NumberSetting("Transition", "Camera and menu transition in milliseconds", 320.0, 0.0, 800.0, 10.0)
   );
   private final BooleanSetting dimBackground = this.addSetting(
      new BooleanSetting("Dim Background", "Keep the vanilla darkened backdrop behind the menu", false)
   );
   private final BooleanSetting showHud = this.addSetting(new BooleanSetting("Show HUD", "Keep the hotbar and HUD visible while a 3D menu is open", true));
   private final ModeSetting playerFacing = this.addSetting(
      new ModeSetting(
         "Player Facing",
         "Where your player turns while a 3D menu is open: its own way, around to look at you, or by Player Turn. Only you see it",
         "Forward",
         "Forward",
         "Camera",
         "Custom"
      )
   );
   private final NumberSetting playerTurn = this.addSetting(
      new NumberSetting("Player Turn", "Degrees your player turns from its own facing; 180 faces the camera", 180.0, -180.0, 180.0, 5.0)
   );
   private final Animation transition = new Animation(0.0);
   private CameraType previousCameraType;
   private boolean menuSettled;
   private static final double pERSPECTIVE_DEPTH = 3.5;

   public Feature3DUIModule() {
      super("3D UI", "Third person shoulder view with your inventory in 3D beside you", Category.RENDER, -1);
      instance = this;
      this.playerTurn.visibleWhen(() -> this.playerFacing.isMode("Custom"));
   }

   public static Feature3DUIModule getInstance() {
      return instance;
   }

   private boolean isViewAvailable() {
      Minecraft var1 = getMc();
      LocalPlayer var2 = var1.player;
      return this.isRuntimeActive()
         && var2 != null
         && var1.level != null
         && var2.isAlive()
         && !var2.isSleeping()
         && !var2.isSpectator()
         && !ViewController.isFreecam()
         && !FreelookModule.isFreelookActive();
   }

   public boolean appliesTo(Screen var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return var1 instanceof AbstractContainerScreen && this.isViewAvailable()
            ? this.screens.isMode("All Containers") || var1 instanceof InventoryScreen || var1 instanceof CreativeModeInventoryScreen
            : false;
      }
   }

   private double getProgress() {
      boolean var1 = this.appliesTo(getMc().gui.screen());
      double var2 = this.transition.animateToWithEasing(var1 ? 1.0 : 0.0, this.transitionMs.getValue().intValue(), Animation.Easing.EASE_OUT_CUBIC);
      return Math.clamp(var2, 0.0, 1.0);
   }

   public static void tickCamera() {
      Feature3DUIModule var0 = instance;
      if (var0 != null) {
         var0.updateCamera();
      }
   }

   private void updateCamera() {
      if (!this.isViewAvailable()) {
         this.releaseView("view unavailable");
         this.transition.snapTo(0.0);
      } else {
         double var1 = this.getProgress();
         if (var1 <= 0.001 && !this.appliesTo(getMc().gui.screen())) {
            this.releaseView("closed");
         } else {
            Options var3 = getMc().options;
            if (this.previousCameraType == null) {
               this.previousCameraType = var3.getCameraType();
               LarpLog.debug("Camera", "3D UI view engaged from {}", this.previousCameraType);
            } else if (var3.getCameraType() != CameraType.THIRD_PERSON_BACK) {
               LarpLog.debug("Camera", "3D UI view yielded to perspective {}", var3.getCameraType());
               this.previousCameraType = null;
               this.transition.snapTo(0.0);
               return;
            }

            if (var3.getCameraType() != CameraType.THIRD_PERSON_BACK) {
               var3.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
         }
      }
   }

   private void releaseView(String var1) {
      if (this.previousCameraType != null) {
         Options var2 = getMc().options;
         if (var2.getCameraType() == CameraType.THIRD_PERSON_BACK) {
            var2.setCameraType(this.previousCameraType);
         }

         LarpLog.debug("Camera", "3D UI view released ({}), perspective {}", var1, var2.getCameraType());
         this.previousCameraType = null;
      }
   }

   public Feature3DUIModule.MenuPose computeMenuPose(Vec3 var1, float var2, float var3, Entity var4, float var5, float var6) {
      if (this.isEnabled()) {
         if (this.previousCameraType != null && var4 != null) {
            double var7 = this.getProgress();
            if (var7 <= 0.001) {
               return null;
            }

            Vec3 var9 = var4.getPosition(var5);
            Vec3 var10 = this.previousCameraType.isFirstPerson() ? var9.add(0.0, var6, 0.0) : var1;
            float var11 = var4 instanceof LivingEntity var12 ? var12.getScale() : 1.0F;
            float var28 = var4.getViewYRot(var5);
            float var13 = this.cameraPitch.getValue().floatValue();
            double var14 = Math.toRadians(var28);
            double var16 = Math.toRadians(var13);
            Vec3 var18 = new Vec3(-Math.sin(var14) * Math.cos(var16), -Math.sin(var16), Math.cos(var14) * Math.cos(var16));
            Vec3 var19 = new Vec3(-Math.cos(var14), 0.0, -Math.sin(var14));
            Vec3 var20 = var9.add(0.0, var4.getBbHeight() * 0.6 + this.cameraHeight.getValue() * var11, 0.0);
            double var21 = (this.side.isMode("Right") ? 1 : -1) * this.cameraOffset.getValue() * var11;
            Vec3 var23 = var20.subtract(var18.scale(this.cameraDistance.getValue() * var11)).add(var19.scale(var21));
            BlockHitResult var24 = var4.level().clip(new ClipContext(var20, var23, Block.VISUAL, Fluid.NONE, var4));
            if (var24.getType() != Type.MISS) {
               Vec3 var25 = var24.getLocation().subtract(var20);
               double var26 = var25.length();
               var23 = var26 < 1.0E-4 ? var20 : var20.add(var25.scale(Math.max(0.0, var26 - 0.2) / var26));
            }

            float var29 = (float)var7;
            Vec3 var30 = var10.lerp(var23, var29);
            return new Feature3DUIModule.MenuPose(var30.x, var30.y, var30.z, var2 + Mth.wrapDegrees(var28 - var2) * var29, Mth.lerp(var29, var3, var13));
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private Feature3DUIModule.LookTarget computeLookTarget(Entity var1, float var2) {
      if (this.previousCameraType != null && !this.playerFacing.isMode("Forward") && var1 != null && var1 == getMc().player) {
         float var3 = (float)this.getProgress();
         if (var3 <= 0.001) {
            return null;
         }

         if (this.playerFacing.isMode("Custom")) {
            return new Feature3DUIModule.LookTarget(var1.getViewYRot(var2) + this.playerTurn.getValue().floatValue(), Float.NaN, var3);
         }

         Vec3 var4 = getMc().gameRenderer.mainCamera().position();
         Vec3 var5 = var1.getEyePosition(var2);
         double var6 = var4.x - var5.x;
         double var8 = var4.y - var5.y;
         double var10 = var4.z - var5.z;
         return new Feature3DUIModule.LookTarget(
            (float)Math.toDegrees(Math.atan2(-var6, var10)), (float)(-Math.toDegrees(Math.atan2(var8, Math.hypot(var6, var10)))), var3
         );
      } else {
         return null;
      }
   }

   public static void applyLookTarget(Entity var0, LivingEntityRenderState var1, float var2) {
      if (instance != null && instance.isEnabled()) {
         Feature3DUIModule var3 = instance;
         Feature3DUIModule.LookTarget var4 = var3 == null ? null : var3.computeLookTarget(var0, var2);
         if (var4 != null) {
            float var5 = var1.bodyRot;
            float var6 = var5 + var1.yRot;
            float var7 = var4.weight();
            float var8 = var5 + Mth.wrapDegrees(var4.yaw() - var5) * var7;
            float var9 = var6 + Mth.wrapDegrees(var4.yaw() - var6) * var7;
            var1.bodyRot = var8;
            var1.yRot = Mth.wrapDegrees(var9 - var8);
            if (!Float.isNaN(var4.pitch())) {
               var1.xRot = Mth.lerp(var7, var1.xRot, var4.pitch());
            }
         }
      }
   }

   public static float getBodyYaw(Entity var0, float var1, float var2) {
      if (instance != null && instance.isEnabled()) {
         Feature3DUIModule var3 = instance;
         Feature3DUIModule.LookTarget var4 = var3 == null ? null : var3.computeLookTarget(var0, var2);
         return var4 == null ? var1 : var1 + Mth.wrapDegrees(var4.yaw() - var1) * var4.weight();
      } else {
         return var1;
      }
   }

   public static ScreenWarp.PerspectiveWarp updateWarp(Screen var0) {
      Feature3DUIModule var1 = instance;
      ScreenWarp.PerspectiveWarp var2 = var1 == null ? null : var1.computeWarp(var0);
      ScreenWarp.setWarp(var0, var2);
      return var2;
   }

   private ScreenWarp.PerspectiveWarp computeWarp(Screen var1) {
      if (this.previousCameraType != null && this.appliesTo(var1)) {
         double var2 = this.getProgress();
         if (var2 <= 0.001) {
            return null;
         }

         double[] var4 = ScreenWarp.setViewTransform(var1);
         if (var4 == null) {
            AbstractContainerScreenMixin_3 var5 = (AbstractContainerScreenMixin_3)var1;
            var4 = new double[]{var5.larpLeftPos(), var5.larpTopPos(), var5.larpLeftPos() + var5.larpImageWidth(), var5.larpTopPos() + var5.larpImageHeight()};
         }

         double var41 = var4[0];
         double var7 = var4[1];
         double var9 = var4[2];
         double var11 = var4[3];
         double var13 = (var41 + var9) / 2.0;
         double var15 = (var7 + var11) / 2.0;
         int var17 = this.side.isMode("Right") ? 1 : -1;
         double var18 = -var17 * Math.toRadians(this.menuAngle.getValue());
         double var20 = 3.5 * Math.max(1.0, (var9 - var41) / 2.0);
         double[] var22 = ScreenWarp.PerspectiveWarp.fromAngle(var13, var15, 0.0, 0.0, 1.0, var18, var20).bounds(var41, var7, var9, var11);
         double var23 = 8.0;
         double var25 = var1.width;
         double var27 = var1.height;
         double var29 = Math.min(
            Math.min(this.menuScreenHeight.getValue() / 100.0 * var27, var27 - 2.0 * var23) / (var22[3] - var22[1]),
            (var25 * 0.5 - var23) / (var22[2] - var22[0])
         );
         double var31 = var17 > 0 ? var25 * 0.71 : var25 * 0.29;
         double var33 = var31 - var29 * (var22[0] + var22[2]) / 2.0;
         var33 = var17 > 0 ? Math.min(var33, var25 - var23 - var29 * var22[2]) : Math.max(var33, var23 - var29 * var22[0]);
         double var35 = var27 / 2.0 - var29 * (var22[1] + var22[3]) / 2.0;
         if (var2 >= 0.999 && !this.menuSettled) {
            this.menuSettled = (boolean)1;
            LarpLog.debug(
               "GUI",
               "3D UI menu settled: {} content {}..{} x {}..{} (measured={}), scale {}, target {} {} in {}x{}",
               var1.getClass().getSimpleName(),
               Math.round(var41),
               Math.round(var9),
               Math.round(var7),
               Math.round(var11),
               ScreenWarp.setViewTransform(var1) != null,
               String.format(Locale.ROOT, "%.2f", var29),
               Math.round(var33),
               Math.round(var35),
               var1.width,
               var1.height
            );
         } else if (var2 < 0.5) {
            this.menuSettled = (boolean)0;
         }

         double var37 = var2;
         double var39 = Mth.lerp(var37, 1.0, var29);
         return ScreenWarp.PerspectiveWarp.fromAngle(
            var13, var15, Mth.lerp(var37, var13, var33), Mth.lerp(var37, var15, var35), var39, var18 * var37, var20 * var39
         );
      } else {
         return null;
      }
   }

   public static void renderBackdrop(GuiGraphicsExtractor var0, int var1, int var2) {
      Feature3DUIModule var3 = instance;
      if (var3 != null) {
         float var4 = var3.dimBackground.getValue() ? 1.0F : (float)(1.0 - var3.getProgress());
         if (!(var4 <= 0.01F)) {
            var0.fillGradient(0, 0, var1, var2, withAlpha(-1072689136, var4), withAlpha(-804253680, var4));
         }
      }
   }

   private static int withAlpha(int var0, float var1) {
      return Math.round((var0 >>> 24) * var1) << 24 | var0 & 16777215;
   }

   public static boolean hidesHud() {
      if (instance != null && instance.isEnabled()) {
         Feature3DUIModule var0 = instance;
         return var0 != null && !var0.showHud.getValue() && var0.previousCameraType != null && var0.appliesTo(getMc().gui.screen());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      LarpLog.debug(
         "Camera",
         "3D UI active: screens={}, side={}, player facing={}{}",
         this.screens.getValue(),
         this.side.getValue(),
         this.playerFacing.getValue(),
         this.playerFacing.isMode("Custom") ? " " + this.playerTurn.getValue().intValue() + " degrees" : ""
      );
   }

   @Override
   public void onDisable() {
      this.releaseView("module inactive");
      this.transition.snapTo(0.0);
      ScreenWarp.setWarp(null, null);
   }

   private record LookTarget(float yaw, float pitch, float weight) {
   }

   public record MenuPose(double x, double y, double z, float yaw, float pitch) {
      private final double posX;
      private final double posY;
      private final double posZ;

      public MenuPose(double x, double y, double z, float yaw, float pitch) {
         this.posX = x;
         this.posY = y;
         this.posZ = z;
         this.yaw = yaw;
         this.pitch = pitch;
      }

      public double getX() {
         return this.posX;
      }

      public double getY() {
         return this.posY;
      }

      public double getZ() {
         return this.posZ;
      }
   }
}
