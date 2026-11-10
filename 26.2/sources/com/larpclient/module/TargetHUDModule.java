package com.larpclient.module;

import com.larpclient.m4;

public final class TargetHUDModule extends Module {
   public final l i = this.addSetting(new l("Equipment Pos", "Place the health and gear line above or below the health bar", "Above", "Above", "Below"));
   public final l X = this.addSetting(new l("Health Bar", "Place the health bar below or above the name", "Below", "Below", "Above"));
   public final l F = this.addSetting(new l("Hands items", "Place hand items left or right of the armor", "Right", "Right", "Left"));

   public TargetHUDModule() {
      super("TargetHUD", "Show information about your last target", Category.RENDER, -1);
   }

   @Override
   public void onDisable() {
      m4.s();
   }
}
