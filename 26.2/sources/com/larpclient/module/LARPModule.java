package com.larpclient.module;

import com.larpclient.k9;

public final class LARPModule extends Module {
   private static LARPModule w;
   public final s g = this.addSetting(new s("Share Appearance", "Retired sharing preference", false));
   public final s F = this.addSetting(new s("Receive Cosmetics", "Retired receive preference", true));

   public LARPModule() {
      super("LARP", "Copy a skin, cape and name locally", Category.PLAYER, -1);
      this.g.P(() -> false);
      this.F.P(() -> false);
      w = this;
   }

   public static LARPModule D() {
      return w;
   }

   @Override
   public void onEnable() {
      k9.f();
   }

   @Override
   public void onDisable() {
      k9.t();
   }

   @Override
   public String getStatus() {
      return (k9.R.isEmpty() ? "" : k9.R + " · ") + k9.L;
   }
}
