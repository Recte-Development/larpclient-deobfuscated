package com.larpclient.module;

public final class ItemPhysicModule extends Module {
   private static ItemPhysicModule c;
   private final l v = this.addSetting(new l("Physics", "Dropped-item presentation", "Normal", "Normal", "2D"));

   public ItemPhysicModule() {
      super("Item Physic", "Rest dropped items on the ground, float them in water and tumble them in the air", Category.VISUALS, -1);
      c = this;
   }

   public static ItemPhysicModule G() {
      return c;
   }

   public boolean y() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean R() {
      return this.v.O("2D");
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("ItemPhysic", "Dropped-item render choreography enabled; native physics retained");
   }
}
