package com.larpclient.module;

import com.larpclient.r4;

public final class ItemPhysicModule extends Module {
   private static ItemPhysicModule j;
   private final lp C = this.addSetting(new lp("Physics", "Dropped-item presentation", "Normal", "Normal", "2D"));

   public ItemPhysicModule() {
      super("Item Physic", "Rest dropped items on the ground, float them in water and tumble them in the air", Category.VISUALS, -1);
      j = this;
   }

   public static ItemPhysicModule S() {
      return j;
   }

   public boolean D() {
      return this.C.i("2D");
   }

   @Override
   public void onEnable() {
      r4.V("ItemPhysic", "Dropped-item render choreography enabled; native physics retained");
   }
}
