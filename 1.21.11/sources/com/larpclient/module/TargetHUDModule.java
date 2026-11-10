package com.larpclient.module;

import com.larpclient.k;

public final class TargetHUDModule extends Module {
   public final li p = this.addSetting(new li("Equipment Pos", "Place the health and gear line above or below the health bar", "Above", "Above", "Below"));
   public final li J = this.addSetting(new li("Health Bar", "Place the health bar below or above the name", "Below", "Below", "Above"));
   public final li C = this.addSetting(new li("Hands items", "Place hand items left or right of the armor", "Right", "Right", "Left"));

   public TargetHUDModule() {
      super("TargetHUD", "Show information about your last target", Category.RENDER, -1);
   }

   @Override
   public void onDisable() {
      k.q();
   }
}
