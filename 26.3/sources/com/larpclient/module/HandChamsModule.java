package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.q6;

public final class HandChamsModule extends Module {
   private static HandChamsModule I;
   public final q6 T = new q6(var1 -> this.addSetting(var1));

   public HandChamsModule() {
      super("Hand Chams", "Animated gradient outlines, fill and glow on your hands and held items", Category.RENDER, -1);
      I = this;
   }

   public static HandChamsModule q() {
      return I;
   }

   @Override
   public void onEnable() {
      _q.z("HandChams", "Echo gradient enabled for native hands and held items");
   }

   @Override
   public void onDisable() {
      com.larpclient.v.H();
      this.T.A();
   }

   @Override
   public void onTick() {
      com.larpclient.v.n();
   }
}
