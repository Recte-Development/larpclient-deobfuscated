package com.larpclient.module;

import com.larpclient.mq;

public final class TargetHUDModule extends Module {
   public final d6 E = this.addSetting(new d6("Equipment Pos", "Place the health and gear line above or below the health bar", "Above", "Above", "Below"));
   public final d6 m = this.addSetting(new d6("Health Bar", "Place the health bar below or above the name", "Below", "Below", "Above"));
   public final d6 L = this.addSetting(new d6("Hands items", "Place hand items left or right of the armor", "Right", "Right", "Left"));

   public TargetHUDModule() {
      super("TargetHUD", "Show information about your last target", Category.RENDER, -1);
   }

   @Override
   public void onDisable() {
      mq.M();
   }
}
