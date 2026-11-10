package com.larpclient.module;

import com.larpclient.bi;
import com.larpclient.q8;

public final class MotionBlurModule extends Module {
   private static MotionBlurModule j;
   public final li s = this.addSetting(new li("Mode", "Smooth follows camera turns; Classic blends recent frames", "Smooth", "Smooth", "Classic"));
   public final lo h = this.addSetting(new lo("Strength", "Shutter length; higher leaves longer streaks", 0.5, 0.1, 1.0, 0.05));
   public final li p = this.addSetting(new li("Quality", "Samples along each streak", "Medium", "Low", "Medium", "High"));
   public final lo N = this.addSetting(new lo("Max Length", "Longest streak as a percent of the screen", 12.0, 2.0, 25.0, 1.0));

   public MotionBlurModule() {
      super("Motion Blur", "Smooth camera motion blur", Category.RENDER, -1);
      j = this;
      this.p.O(() -> this.s.getValue().equals("Smooth"));
      this.N.O(() -> this.s.getValue().equals("Smooth"));
   }

   public static MotionBlurModule L() {
      return j;
   }

   @Override
   public void onEnable() {
      q8.G("MotionBlur", "Enabled; mode={}; strength={}; quality={}", this.s.getValue(), this.h.getValue(), this.p.getValue());
   }

   @Override
   public void onDisable() {
      bi.M();
   }
}
