package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.overlay.MotionBlurRenderer;
import com.larpclient.util.misc.LarpLog;

public final class MotionBlurModule extends Module {
   private static MotionBlurModule instance;
   public final ModeSetting modeSetting = this.addSetting(
      new ModeSetting("Mode", "Smooth follows camera turns; Classic blends recent frames", "Smooth", "Smooth", "Classic")
   );
   public final NumberSetting strengthSetting = this.addSetting(
      new NumberSetting("Strength", "Shutter length; higher leaves longer streaks", 0.5, 0.1, 1.0, 0.05)
   );
   public final ModeSetting qualitySetting = this.addSetting(new ModeSetting("Quality", "Samples along each streak", "Medium", "Low", "Medium", "High"));
   public final NumberSetting maxLengthSetting = this.addSetting(
      new NumberSetting("Max Length", "Longest streak as a percent of the screen", 12.0, 2.0, 25.0, 1.0)
   );

   public MotionBlurModule() {
      super("Motion Blur", "Smooth camera motion blur", Category.RENDER, -1);
      instance = this;
      this.qualitySetting.visibleWhen(() -> this.modeSetting.getValue().equals("Smooth"));
      this.maxLengthSetting.visibleWhen(() -> this.modeSetting.getValue().equals("Smooth"));
   }

   public static MotionBlurModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "MotionBlur",
         "Enabled; mode={}; strength={}; quality={}",
         this.modeSetting.getValue(),
         this.strengthSetting.getValue(),
         this.qualitySetting.getValue()
      );
   }

   @Override
   public void onDisable() {
      MotionBlurRenderer.releaseResources();
   }
}
