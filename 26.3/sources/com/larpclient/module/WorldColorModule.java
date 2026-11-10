package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.br;

public final class WorldColorModule extends Module {
   private static WorldColorModule t;
   public final b E = this.addSetting(new b("Saturation", "Color intensity", 1.0, 0.0, 2.0, 0.05));
   public final b p = this.addSetting(new b("Contrast", "Mid-tone contrast", 1.0, 0.5, 1.5, 0.025));
   public final b Q = this.addSetting(new b("Exposure", "Exposure stops", 0.0, -2.0, 2.0, 0.05));
   public final b B = this.addSetting(new b("Gamma", "Display gamma adjustment", 1.0, 0.5, 2.0, 0.05));
   public final h i = this.addSetting(new h("Tint", "Overall multiplicative tint", -1));
   public final h T = this.addSetting(new h("Shadow Tint", "Dark-region tint", -1));
   public final h M = this.addSetting(new h("Highlight Tint", "Bright-region tint", -1));
   public final b I = this.addSetting(new b("Vignette", "Subtle edge darkening", 0.0, 0.0, 0.4, 0.01));
   public final h o = this.addSetting(new h("Ambient Color", "Flat tint of native world lighting", -5342721));
   public final b u = this.addSetting(new b("Ambient Strength", "Lighting tint strength; zero keeps native lighting", 0.0, 0.0, 1.0, 0.05));

   public WorldColorModule() {
      super("WorldColor", "Adjust world colors", Category.RENDER, -1);
      t = this;
      this.o.a(() -> this.u.getValue() > 0.0);
   }

   public static WorldColorModule x() {
      return t;
   }

   public boolean D() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   @Override
   public void onEnable() {
      _q.z(
         "WorldColor",
         "Native lighting tint enabled; color={}; strength={}; grading controls retained",
         Integer.toHexString(this.o.getValue()),
         this.u.getValue()
      );
   }

   @Override
   public void onDisable() {
      br.B();
   }
}
