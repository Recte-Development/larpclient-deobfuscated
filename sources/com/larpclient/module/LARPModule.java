package com.larpclient.module;

import com.larpclient.du;

public final class LARPModule extends Module {
   private static LARPModule y;
   public final dh c = this.addSetting(new dh("Share Appearance", "Retired sharing preference", false));
   public final dh M = this.addSetting(new dh("Receive Cosmetics", "Retired receive preference", true));

   public LARPModule() {
      super("LARP", "Copy a skin, cape and name locally", Category.PLAYER, -1);
      this.c.H(() -> false);
      this.M.H(() -> false);
      y = this;
   }

   public static LARPModule d() {
      return y;
   }

   @Override
   public void onEnable() {
      du.s();
   }

   @Override
   public void onDisable() {
      du.u();
   }

   @Override
   public String getStatus() {
      return (du.F.isEmpty() ? "" : du.F + " · ") + du.w;
   }
}
