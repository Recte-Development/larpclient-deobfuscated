package com.larpclient.module;

import com.larpclient.cd;

public final class WorldColorModule extends Module {
   private static WorldColorModule n;
   public final e I = this.addSetting(new e("Saturation", "Color intensity", 1.0, 0.0, 2.0, 0.05));
   public final e e = this.addSetting(new e("Contrast", "Mid-tone contrast", 1.0, 0.5, 1.5, 0.025));
   public final e l = this.addSetting(new e("Exposure", "Exposure stops", 0.0, -2.0, 2.0, 0.05));
   public final e u = this.addSetting(new e("Gamma", "Display gamma adjustment", 1.0, 0.5, 2.0, 0.05));
   public final i J = this.addSetting(new i("Tint", "Overall multiplicative tint", -1));
   public final i C = this.addSetting(new i("Shadow Tint", "Dark-region tint", -1));
   public final i c = this.addSetting(new i("Highlight Tint", "Bright-region tint", -1));
   public final e V = this.addSetting(new e("Vignette", "Subtle edge darkening", 0.0, 0.0, 0.4, 0.01));
   public final i T = this.addSetting(new i("Ambient Color", "Flat tint of native world lighting", -5342721));
   public final e N = this.addSetting(new e("Ambient Strength", "Lighting tint strength; zero keeps native lighting", 0.0, 0.0, 1.0, 0.05));

   public WorldColorModule() {
      super("WorldColor", "Adjust world colors", Category.RENDER, -1);
      n = this;
      this.T.P(() -> this.N.getValue() > 0.0);
   }

   public static WorldColorModule p() {
      return n;
   }

   public boolean i() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A(
         "WorldColor",
         "Native lighting tint enabled; color={}; strength={}; grading controls retained",
         Integer.toHexString(this.T.getValue()),
         this.N.getValue()
      );
   }

   @Override
   public void onDisable() {
      cd.U();
   }
}
