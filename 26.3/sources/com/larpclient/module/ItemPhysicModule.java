package com.larpclient.module;

import com.larpclient._q;

public final class ItemPhysicModule extends Module {
   private static ItemPhysicModule X;
   private final o U = this.addSetting(new o("Physics", "Dropped-item presentation", "Normal", "Normal", "2D"));

   public ItemPhysicModule() {
      super("Item Physic", "Rest dropped items on the ground, float them in water and tumble them in the air", Category.VISUALS, -1);
      X = this;
   }

   public static ItemPhysicModule f() {
      return X;
   }

   public boolean M() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean G() {
      return this.U.w("2D");
   }

   @Override
   public void onEnable() {
      _q.z("ItemPhysic", "Dropped-item render choreography enabled; native physics retained");
   }
}
