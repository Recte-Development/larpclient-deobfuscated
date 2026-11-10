package com.larpclient.module;

import com.larpclient.sa;

public final class MotionBlurModule extends Module {
   private static MotionBlurModule C;
   public final l g = this.addSetting(new l("Mode", "Smooth follows camera turns; Classic blends recent frames", "Smooth", "Smooth", "Classic"));
   public final e N = this.addSetting(new e("Strength", "Shutter length; higher leaves longer streaks", 0.5, 0.1, 1.0, 0.05));
   public final l i = this.addSetting(new l("Quality", "Samples along each streak", "Medium", "Low", "Medium", "High"));
   public final e q = this.addSetting(new e("Max Length", "Longest streak as a percent of the screen", 12.0, 2.0, 25.0, 1.0));

   public MotionBlurModule() {
      super("Motion Blur", "Smooth camera motion blur", Category.RENDER, -1);
      C = this;
      this.i.P(() -> this.g.getValue().equals("Smooth"));
      this.q.P(() -> this.g.getValue().equals("Smooth"));
   }

   public static MotionBlurModule E() {
      return C;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("MotionBlur", "Enabled; mode={}; strength={}; quality={}", this.g.getValue(), this.N.getValue(), this.i.getValue());
   }

   @Override
   public void onDisable() {
      sa.u();
   }
}
