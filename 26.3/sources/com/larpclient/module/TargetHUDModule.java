package com.larpclient.module;

import com.larpclient.dk;

public final class TargetHUDModule extends Module {
   public final o Q = this.addSetting(new o("Equipment Pos", "Place the health and gear line above or below the health bar", "Above", "Above", "Below"));
   public final o R = this.addSetting(new o("Health Bar", "Place the health bar below or above the name", "Below", "Below", "Above"));
   public final o q = this.addSetting(new o("Hands items", "Place hand items left or right of the armor", "Right", "Right", "Left"));

   public TargetHUDModule() {
      super("TargetHUD", "Show information about your last target", Category.RENDER, -1);
   }

   @Override
   public void onDisable() {
      dk.X();
   }
}
