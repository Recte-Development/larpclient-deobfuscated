package com.larpclient.module;

import com.larpclient.mc;
import com.larpclient.r4;

public final class MotionBlurModule extends Module {
   private static MotionBlurModule v;
   public final lp M = this.addSetting(new lp("Mode", "Smooth follows camera turns; Classic blends recent frames", "Smooth", "Smooth", "Classic"));
   public final lc J = this.addSetting(new lc("Strength", "Shutter length; higher leaves longer streaks", 0.5, 0.1, 1.0, 0.05));
   public final lp s = this.addSetting(new lp("Quality", "Samples along each streak", "Medium", "Low", "Medium", "High"));
   public final lc U = this.addSetting(new lc("Max Length", "Longest streak as a percent of the screen", 12.0, 2.0, 25.0, 1.0));

   public MotionBlurModule() {
      super("Motion Blur", "Smooth camera motion blur", Category.RENDER, -1);
      v = this;
      this.s.x(() -> this.M.getValue().equals("Smooth"));
      this.U.x(() -> this.M.getValue().equals("Smooth"));
   }

   public static MotionBlurModule S() {
      return v;
   }

   @Override
   public void onEnable() {
      r4.V("MotionBlur", "Enabled; mode={}; strength={}; quality={}", this.M.getValue(), this.J.getValue(), this.s.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.mc.J();
   }
}
