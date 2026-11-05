package com.larpclient.module;

import com.larpclient.dy;
import com.larpclient.r4;
import com.larpclient.rg;

public final class HandChamsModule extends Module {
   private static HandChamsModule g;
   public final rg S = new rg(var1 -> this.addSetting(var1));

   public HandChamsModule() {
      super("Hand Chams", "Animated gradient outlines, fill and glow on your hands and held items", Category.RENDER, -1);
      g = this;
   }

   public static HandChamsModule w() {
      return g;
   }

   @Override
   public void onEnable() {
      r4.V("HandChams", "Echo gradient enabled for native hands and held items");
   }

   @Override
   public void onDisable() {
      dy.z();
      this.S.C();
   }

   @Override
   public void onTick() {
      dy.K();
   }
}
