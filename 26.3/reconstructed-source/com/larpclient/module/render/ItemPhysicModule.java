package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.util.misc.LarpLog;

public final class ItemPhysicModule extends Module {
   private static ItemPhysicModule instance;
   private final ModeSetting physicsMode = this.addSetting(new ModeSetting("Physics", "Dropped-item presentation", "Normal", "Normal", "2D"));

   public ItemPhysicModule() {
      super("Item Physic", "Rest dropped items on the ground, float them in water and tumble them in the air", Category.VISUALS, -1);
      instance = this;
   }

   public static ItemPhysicModule getInstance() {
      return instance;
   }

   public boolean isActive() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean isFlatMode() {
      return this.physicsMode.isMode("2D");
   }

   @Override
   public void onEnable() {
      LarpLog.info("ItemPhysic", "Dropped-item render choreography enabled; native physics retained");
   }
}
