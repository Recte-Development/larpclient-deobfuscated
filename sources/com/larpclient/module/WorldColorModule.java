package com.larpclient.module;

import com.larpclient.eb;
import com.larpclient.mt;

public final class WorldColorModule extends Module {
   private static WorldColorModule L;
   public final d9 w = this.addSetting(new d9("Saturation", "Color intensity", 1.0, 0.0, 2.0, 0.05));
   public final d9 A = this.addSetting(new d9("Contrast", "Mid-tone contrast", 1.0, 0.5, 1.5, 0.025));
   public final d9 Q = this.addSetting(new d9("Exposure", "Exposure stops", 0.0, -2.0, 2.0, 0.05));
   public final d9 K = this.addSetting(new d9("Gamma", "Display gamma adjustment", 1.0, 0.5, 2.0, 0.05));
   public final dk S = this.addSetting(new dk("Tint", "Overall multiplicative tint", -1));
   public final dk G = this.addSetting(new dk("Shadow Tint", "Dark-region tint", -1));
   public final dk i = this.addSetting(new dk("Highlight Tint", "Bright-region tint", -1));
   public final d9 D = this.addSetting(new d9("Vignette", "Subtle edge darkening", 0.0, 0.0, 0.4, 0.01));
   public final dk T = this.addSetting(new dk("Ambient Color", "Flat tint of native world lighting", -5342721));
   public final d9 B = this.addSetting(new d9("Ambient Strength", "Lighting tint strength; zero keeps native lighting", 0.0, 0.0, 1.0, 0.05));

   public WorldColorModule() {
      super("WorldColor", "Adjust world colors", Category.RENDER, -1);
      L = this;
      this.T.H(() -> this.B.P() > 0.0);
   }

   public static WorldColorModule N() {
      return L;
   }

   @Override
   public void onEnable() {
      mt.K("WorldColor", "Native lighting tint enabled; color={}; strength={}; grading controls retained", Integer.toHexString(this.T.P()), this.B.P());
   }

   @Override
   public void onDisable() {
      eb.f();
   }
}
