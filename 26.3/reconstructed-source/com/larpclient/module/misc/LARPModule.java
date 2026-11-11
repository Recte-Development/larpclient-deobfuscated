package com.larpclient.module.misc;

import com.larpclient.social.cosmetic.LocalAppearance;

public final class LARPModule extends Module {
   private static LARPModule instance;
   public final BooleanSetting shareAppearance = this.addSetting(new BooleanSetting("Share Appearance", "Retired sharing preference", false));
   public final BooleanSetting receiveCosmetics = this.addSetting(new BooleanSetting("Receive Cosmetics", "Retired receive preference", true));

   public LARPModule() {
      super("LARP", "Copy a skin, cape and name locally", Category.PLAYER, -1);
      this.shareAppearance.visibleWhen(() -> false);
      this.receiveCosmetics.visibleWhen(() -> false);
      instance = this;
   }

   public static LARPModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      LocalAppearance.enable();
   }

   @Override
   public void onDisable() {
      LocalAppearance.disable();
   }

   @Override
   public String getStatus() {
      return (LocalAppearance.statusMessage.isEmpty() ? "" : LocalAppearance.statusMessage + " · ") + LocalAppearance.mode;
   }
}
