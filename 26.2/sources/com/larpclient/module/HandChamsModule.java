package com.larpclient.module;

import com.larpclient.be;
import com.larpclient.bl;

public final class HandChamsModule extends Module {
   private static HandChamsModule u;
   public final bl F = new bl(var1 -> this.addSetting(var1));

   public HandChamsModule() {
      super("Hand Chams", "Animated gradient outlines, fill and glow on your hands and held items", Category.RENDER, -1);
      u = this;
   }

   public static HandChamsModule Y() {
      return u;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("HandChams", "Echo gradient enabled for native hands and held items");
   }

   @Override
   public void onDisable() {
      be.o();
      this.F.v();
   }

   @Override
   public void onTick() {
      be.O();
   }
}
