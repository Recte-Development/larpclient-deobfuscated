package com.larpclient.module;

import com.larpclient.mu;
import com.larpclient.r4;

public final class WorldColorModule extends Module {
   private static WorldColorModule Q;
   public final lc H = this.addSetting(new lc("Saturation", "Color intensity", 1.0, 0.0, 2.0, 0.05));
   public final lc S = this.addSetting(new lc("Contrast", "Mid-tone contrast", 1.0, 0.5, 1.5, 0.025));
   public final lc A = this.addSetting(new lc("Exposure", "Exposure stops", 0.0, -2.0, 2.0, 0.05));
   public final lc j = this.addSetting(new lc("Gamma", "Display gamma adjustment", 1.0, 0.5, 2.0, 0.05));
   public final lv R = this.addSetting(new lv("Tint", "Overall multiplicative tint", -1));
   public final lv q = this.addSetting(new lv("Shadow Tint", "Dark-region tint", -1));
   public final lv v = this.addSetting(new lv("Highlight Tint", "Bright-region tint", -1));
   public final lc a = this.addSetting(new lc("Vignette", "Subtle edge darkening", 0.0, 0.0, 0.4, 0.01));
   public final lv z = this.addSetting(new lv("Ambient Color", "Flat tint of native world lighting", -5342721));
   public final lc g = this.addSetting(new lc("Ambient Strength", "Lighting tint strength; zero keeps native lighting", 0.0, 0.0, 1.0, 0.05));

   public WorldColorModule() {
      super("WorldColor", "Adjust world colors", Category.RENDER, -1);
      Q = this;
      this.z.x(() -> this.g.getValue() > 0.0);
   }

   public static WorldColorModule S() {
      return Q;
   }

   @Override
   public void onEnable() {
      r4.V(
         "WorldColor",
         "Native lighting tint enabled; color={}; strength={}; grading controls retained",
         Integer.toHexString(this.z.getValue()),
         this.g.getValue()
      );
   }

   @Override
   public void onDisable() {
      mu.s();
   }
}
