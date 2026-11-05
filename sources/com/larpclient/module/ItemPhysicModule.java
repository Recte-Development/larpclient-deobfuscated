package com.larpclient.module;

import com.larpclient.mt;

public final class ItemPhysicModule extends Module {
   private static ItemPhysicModule z;
   private final d6 N = this.addSetting(new d6("Physics", "Dropped-item presentation", "Normal", "Normal", "2D"));

   public ItemPhysicModule() {
      super("Item Physic", "Rest dropped items on the ground, float them in water and tumble them in the air", Category.VISUALS, -1);
      z = this;
   }

   public static ItemPhysicModule z() {
      return z;
   }

   public boolean v() {
      return this.N.S("2D");
   }

   @Override
   public void onEnable() {
      mt.K("ItemPhysic", "Dropped-item render choreography enabled; native physics retained");
   }
}
