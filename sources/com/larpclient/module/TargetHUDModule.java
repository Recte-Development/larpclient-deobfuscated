package com.larpclient.module;

import com.larpclient.o8;

public final class TargetHUDModule extends Module {
   public final lp L = this.addSetting(new lp("Equipment Pos", "Place the health and gear line above or below the health bar", "Above", "Above", "Below"));
   public final lp P = this.addSetting(new lp("Health Bar", "Place the health bar below or above the name", "Below", "Below", "Above"));
   public final lp I = this.addSetting(new lp("Hands items", "Place hand items left or right of the armor", "Right", "Right", "Left"));

   public TargetHUDModule() {
      super("TargetHUD", "Show information about your last target", Category.RENDER, -1);
   }

   @Override
   public void onDisable() {
      o8.A();
   }
}
