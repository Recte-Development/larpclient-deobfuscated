package com.larpclient.module;

import com.larpclient.q8;

public final class ItemPhysicModule extends Module {
   private static ItemPhysicModule d;
   private final li n = this.addSetting(new li("Physics", "Dropped-item presentation", "Normal", "Normal", "2D"));

   public ItemPhysicModule() {
      super("Item Physic", "Rest dropped items on the ground, float them in water and tumble them in the air", Category.VISUALS, -1);
      d = this;
   }

   public static ItemPhysicModule o() {
      return d;
   }

   public boolean V() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean J() {
      return this.n.G("2D");
   }

   @Override
   public void onEnable() {
      q8.G("ItemPhysic", "Dropped-item render choreography enabled; native physics retained");
   }
}
