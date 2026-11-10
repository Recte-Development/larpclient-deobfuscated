package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.q8;

public final class WorldColorModule extends Module {
   private static WorldColorModule p;
   public final lo t = this.addSetting(new lo("Saturation", "Color intensity", 1.0, 0.0, 2.0, 0.05));
   public final lo y = this.addSetting(new lo("Contrast", "Mid-tone contrast", 1.0, 0.5, 1.5, 0.025));
   public final lo S = this.addSetting(new lo("Exposure", "Exposure stops", 0.0, -2.0, 2.0, 0.05));
   public final lo w = this.addSetting(new lo("Gamma", "Display gamma adjustment", 1.0, 0.5, 2.0, 0.05));
   public final l0 g = this.addSetting(new l0("Tint", "Overall multiplicative tint", -1));
   public final l0 T = this.addSetting(new l0("Shadow Tint", "Dark-region tint", -1));
   public final l0 Y = this.addSetting(new l0("Highlight Tint", "Bright-region tint", -1));
   public final lo j = this.addSetting(new lo("Vignette", "Subtle edge darkening", 0.0, 0.0, 0.4, 0.01));
   public final l0 k = this.addSetting(new l0("Ambient Color", "Flat tint of native world lighting", -5342721));
   public final lo P = this.addSetting(new lo("Ambient Strength", "Lighting tint strength; zero keeps native lighting", 0.0, 0.0, 1.0, 0.05));

   public WorldColorModule() {
      super("WorldColor", "Adjust world colors", Category.RENDER, -1);
      p = this;
      this.k.O(() -> this.P.getValue() > 0.0);
   }

   public static WorldColorModule n() {
      return p;
   }

   public boolean O() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   @Override
   public void onEnable() {
      q8.G(
         "WorldColor",
         "Native lighting tint enabled; color={}; strength={}; grading controls retained",
         Integer.toHexString(this.k.getValue()),
         this.P.getValue()
      );
   }

   @Override
   public void onDisable() {
      cf.P();
   }
}
