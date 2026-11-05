package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.x6;

public final class MotionBlurModule extends Module {
   private static MotionBlurModule t;
   public final d6 n = this.addSetting(new d6("Mode", "Smooth follows camera turns; Classic blends recent frames", "Smooth", "Smooth", "Classic"));
   public final d9 K = this.addSetting(new d9("Strength", "Shutter length; higher leaves longer streaks", 0.5, 0.1, 1.0, 0.05));
   public final d6 v = this.addSetting(new d6("Quality", "Samples along each streak", "Medium", "Low", "Medium", "High"));
   public final d9 V = this.addSetting(new d9("Max Length", "Longest streak as a percent of the screen", 12.0, 2.0, 25.0, 1.0));

   public MotionBlurModule() {
      super("Motion Blur", "Smooth camera motion blur", Category.RENDER, -1);
      t = this;
      this.v.H(() -> this.n.P().equals("Smooth"));
      this.V.H(() -> this.n.P().equals("Smooth"));
   }

   public static MotionBlurModule U() {
      return t;
   }

   @Override
   public void onEnable() {
      mt.K("MotionBlur", "Enabled; mode={}; strength={}; quality={}", this.n.P(), this.K.P(), this.v.P());
   }

   @Override
   public void onDisable() {
      x6.o();
   }
}
