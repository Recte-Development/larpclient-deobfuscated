package com.larpclient.module;

import com.larpclient.em;

public final class LARPModule extends Module {
   private static LARPModule J;
   public final ln U = this.addSetting(new ln("Share Appearance", "Retired sharing preference", false));
   public final ln t = this.addSetting(new ln("Receive Cosmetics", "Retired receive preference", true));

   public LARPModule() {
      super("LARP", "Copy a skin, cape and name locally", Category.PLAYER, -1);
      this.U.x(() -> false);
      this.t.x(() -> false);
      J = this;
   }

   public static LARPModule C() {
      return J;
   }

   @Override
   public void onEnable() {
      em.i();
   }

   @Override
   public void onDisable() {
      em.z();
   }

   @Override
   public String getStatus() {
      return (em.R.isEmpty() ? "" : em.R + " · ") + em.h;
   }
}
