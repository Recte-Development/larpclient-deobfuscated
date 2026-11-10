package com.larpclient.module;

import com.larpclient._q;

public final class MotionBlurModule extends Module {
   private static MotionBlurModule Z;
   public final o W = this.addSetting(new o("Mode", "Smooth follows camera turns; Classic blends recent frames", "Smooth", "Smooth", "Classic"));
   public final b T = this.addSetting(new b("Strength", "Shutter length; higher leaves longer streaks", 0.5, 0.1, 1.0, 0.05));
   public final o u = this.addSetting(new o("Quality", "Samples along each streak", "Medium", "Low", "Medium", "High"));
   public final b V = this.addSetting(new b("Max Length", "Longest streak as a percent of the screen", 12.0, 2.0, 25.0, 1.0));

   public MotionBlurModule() {
      super("Motion Blur", "Smooth camera motion blur", Category.RENDER, -1);
      Z = this;
      this.u.a(() -> this.W.getValue().equals("Smooth"));
      this.V.a(() -> this.W.getValue().equals("Smooth"));
   }

   public static MotionBlurModule l() {
      return Z;
   }

   @Override
   public void onEnable() {
      _q.z("MotionBlur", "Enabled; mode={}; strength={}; quality={}", this.W.getValue(), this.T.getValue(), this.u.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.u.e();
   }
}
