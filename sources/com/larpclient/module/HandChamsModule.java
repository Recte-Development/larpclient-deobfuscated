package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.p3;
import com.larpclient.xw;

public final class HandChamsModule extends Module {
   private static HandChamsModule o;
   public final p3 i = new p3(var1 -> this.addSetting(var1));

   public HandChamsModule() {
      super("Hand Chams", "Animated gradient outlines, fill and glow on your hands and held items", Category.RENDER, -1);
      o = this;
   }

   public static HandChamsModule e() {
      return o;
   }

   @Override
   public void onEnable() {
      mt.K("HandChams", "Echo gradient enabled for native hands and held items");
   }

   @Override
   public void onDisable() {
      xw.F();
      this.i.i();
   }

   @Override
   public void onTick() {
      xw.j();
   }
}
