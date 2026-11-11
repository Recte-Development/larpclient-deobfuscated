package com.larpclient.module.render;

import com.larpclient.hud.HudLayout;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;

public final class TargetHUDModule extends Module {
   public final ModeSetting equipmentPosition = this.addSetting(
      new ModeSetting("Equipment Pos", "Place the health and gear line above or below the health bar", "Above", "Above", "Below")
   );
   public final ModeSetting healthBarPosition = this.addSetting(
      new ModeSetting("Health Bar", "Place the health bar below or above the name", "Below", "Below", "Above")
   );
   public final ModeSetting handItemsSide = this.addSetting(
      new ModeSetting("Hands items", "Place hand items left or right of the armor", "Right", "Right", "Left")
   );

   public TargetHUDModule() {
      super("TargetHUD", "Show information about your last target", Category.RENDER, -1);
   }

   @Override
   public void onDisable() {
      HudLayout.addBounds();
   }
}
