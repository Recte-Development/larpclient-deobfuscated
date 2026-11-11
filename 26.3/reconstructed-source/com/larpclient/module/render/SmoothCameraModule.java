package com.larpclient.module.render;

import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.misc.LarpLog;
import java.util.Locale;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SmoothCameraModule extends Module {
   private static SmoothCameraModule instance;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "How the camera follows movement", "Default", "Default", "Smooth"));
   private final BooleanSetting firstPov = this.addSetting(new BooleanSetting("Enable First POV", "Also smooth camera movement in first person", false));
   private final BooleanSetting resetOnPerspective = this.addSetting(
      new BooleanSetting("Reset On Perspective", "Reset smoothing when switching perspective", true)
   );
   private final NumberSetting horizontalFactor = this.createFactorSetting("Horizontal Factor", "Higher values add more horizontal lag", 0.9);
   private final NumberSetting verticalFactor = this.createFactorSetting("Vertical Factor", "Higher values add more vertical lag", 0.93);
   private final NumberSetting smoothHorizontalSpeed = this.addSetting(
      new NumberSetting("Smooth Horizontal Speed", "Horizontal follow speed in blocks per second", 2.2, 0.1, 10.0, 0.1)
   );
   private final NumberSetting smoothVerticalSpeed = this.addSetting(
      new NumberSetting("Smooth Vertical Speed", "Higher values make vertical movement catch up faster", 1.4, 0.1, 10.0, 0.1)
   );
   private final SmoothCameraModule.PositionSmoother smoother = new SmoothCameraModule.PositionSmoother();
   private Object lastLevel;
   private Object lastEntity;
   private Object lastCameraType;
   private Vec3 lastPosition;
   private long lastUpdateNanos;

   public SmoothCameraModule() {
      super("SmoothCamera", "Smooth camera movement as you move", Category.PLAYER, -1);
      instance = this;
      this.horizontalFactor.visibleWhen(() -> this.mode.isMode("Default"));
      this.verticalFactor.visibleWhen(() -> this.mode.isMode("Default"));
      this.smoothHorizontalSpeed.visibleWhen(() -> this.mode.isMode("Smooth"));
      this.smoothVerticalSpeed.visibleWhen(() -> this.mode.isMode("Smooth"));

      for (Setting var2 : this.getSettings()) {
         var2.onChange(var1 -> this.reset());
      }
   }

   public static SmoothCameraModule getInstance() {
      return instance;
   }

   public static boolean isSmoothableOwner(ViewController.Owner var0) {
      return var0 == ViewController.Owner.NONE
         || var0 == ViewController.Owner.AURA
         || var0 == ViewController.Owner.AUTO_TRAP
         || var0 == ViewController.Owner.AUTO_WEB
         || var0 == ViewController.Owner.AUTO_ANCHOR
         || var0 == ViewController.Owner.MACE
         || var0 == ViewController.Owner.AUTO_CRYSTAL
         || var0 == ViewController.Owner.PEARL_CATCH
         || var0 == ViewController.Owner.WIND_HOP
         || var0 == ViewController.Owner.RETURN;
   }

   private NumberSetting createFactorSetting(String var1, String var2, double var3) {
      return this.addSetting(new NumberSetting(var1, var2, var3, 0.0, 1.0, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      });
   }

   public Vec3 smoothPosition(Camera var1, Vec3 var2) {
      if (!this.isEnabled()) {
         return var2;
      }

      Minecraft var3 = getMc();
      if (this.isRuntimeActive()
         && var3 != null
         && var3.level != null
         && var3.player != null
         && var3.player.isAlive()
         && var1.entity() != null
         && !(var1.entity() instanceof LivingEntity var4 && var4.isSleeping())
         && !var1.isPanoramicMode()
         && isSmoothableOwner(ViewController.update())
         && (!var3.options.getCameraType().isFirstPerson() || this.firstPov.getValue())) {
         long var10 = System.nanoTime();
         CameraType var6 = var3.options.getCameraType();
         double var7 = (var10 - this.lastUpdateNanos) * 1.0E-9;
         int var9 = this.lastLevel == var3.level
               && this.lastEntity == var1.entity()
               && (!this.resetOnPerspective.getValue() || this.lastCameraType == var6)
               && this.lastPosition != null
               && !(this.lastPosition.distanceToSqr(var2) > 256.0)
               && !(var7 > 0.25)
            ? 0
            : 1;
         this.lastLevel = var3.level;
         this.lastEntity = var1.entity();
         this.lastCameraType = var6;
         this.lastPosition = var2;
         this.lastUpdateNanos = var10;
         if (var9 != 0) {
            this.smoother.reset();
         }

         return this.smoother
            .smooth(
               var2,
               var7,
               this.mode.isMode("Smooth"),
               this.horizontalFactor.getValue(),
               this.verticalFactor.getValue(),
               this.smoothHorizontalSpeed.getValue(),
               this.smoothVerticalSpeed.getValue()
            );
      } else {
         this.reset();
         return var2;
      }
   }

   private void reset() {
      this.smoother.reset();
      this.lastLevel = this.lastEntity = this.lastCameraType = null;
      this.lastPosition = null;
      this.lastUpdateNanos = 0L;
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.debug("SmoothCamera", "Position smoothing enabled; mode={}, first POV={}", this.mode.getValue(), this.firstPov.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   public static final class PositionSmoother {
      public static final double mAX_LAG_DISTANCE = 1.0;
      private Vec3 smoothedPos;
      private double verticalVelocity;

      public void reset() {
         this.smoothedPos = null;
         this.verticalVelocity = 0.0;
      }

      public Vec3 smooth(Vec3 var1, double var2, boolean var4, double var5, double var7, double var9, double var11) {
         if (!Double.isFinite(var1.x) || !Double.isFinite(var1.y) || !Double.isFinite(var1.z)) {
            this.reset();
            return var1;
         }

         if (this.smoothedPos == null) {
            this.smoothedPos = var1;
            return var1;
         }

         double var13 = Math.clamp(var2, 0.0, 0.05);
         if (var13 <= 0.0) {
            return this.smoothedPos;
         }

         if (!var4) {
            double var39 = Math.pow(var5, var13 * 60.0);
            double var40 = Math.pow(var7, var13 * 60.0);
            this.smoothedPos = new Vec3(
               var1.x + (this.smoothedPos.x - var1.x) * var39, var1.y + (this.smoothedPos.y - var1.y) * var40, var1.z + (this.smoothedPos.z - var1.z) * var39
            );
            return this.smoothedPos;
         }

         double var15 = var1.x - this.smoothedPos.x;
         double var17 = var1.z - this.smoothedPos.z;
         double var19 = Math.hypot(var15, var17);
         double var21 = Math.max(var9 * var13, var19 - 1.0);
         double var23 = !(var19 <= var21) && !(var19 < 1.0E-5) ? var21 / var19 : 1.0;
         double var25 = this.smoothedPos.y;
         if (Math.abs(var1.y - var25) > 6.0) {
            double var27 = var11 * var13 * 4.0;
            var25 += Math.clamp(var1.y - var25, -var27, var27);
            this.verticalVelocity = 0.0;
         } else {
            double var42 = 2.0 / Math.max(0.06, 0.42 / Math.max(0.1, var11));
            double var29 = var42 * var13;
            double var31 = 1.0 / (1.0 + var29 + 0.48 * var29 * var29 + 0.235 * var29 * var29 * var29);
            double var33 = var25 - var1.y;
            double var35 = (this.verticalVelocity + var42 * var33) * var13;
            this.verticalVelocity = (this.verticalVelocity - var42 * var35) * var31;
            double var37 = var1.y + (var33 + var35) * var31;
            if ((var1.y - var25 > 0.0 ? 1 : 0) == (var37 > var1.y ? 1 : 0)) {
               var37 = var1.y;
               this.verticalVelocity = 0.0;
            }

            var25 = var37;
         }

         this.smoothedPos = new Vec3(this.smoothedPos.x + var15 * var23, var25, this.smoothedPos.z + var17 * var23);
         return this.smoothedPos;
      }
   }
}
