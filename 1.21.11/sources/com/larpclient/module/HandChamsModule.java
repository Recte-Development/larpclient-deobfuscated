package com.larpclient.module;

import com.larpclient.ba;
import com.larpclient.hi;
import com.larpclient.q8;

public final class HandChamsModule extends Module {
   private static HandChamsModule E;
   public final ba C = new ba(var1 -> this.addSetting(var1));

   public HandChamsModule() {
      super("Hand Chams", "Animated gradient outlines, fill and glow on your hands and held items", Category.RENDER, -1);
      E = this;
   }

   public static HandChamsModule Z() {
      return E;
   }

   @Override
   public void onEnable() {
      q8.G("HandChams", "Echo gradient enabled for native hands and held items");
   }

   @Override
   public void onDisable() {
      hi.c();
      this.C.B();
   }

   @Override
   public void onTick() {
      hi.P();
   }
}
