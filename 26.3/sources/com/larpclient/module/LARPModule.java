package com.larpclient.module;

import com.larpclient.be;

public final class LARPModule extends Module {
   private static LARPModule X;
   public final m q = this.addSetting(new m("Share Appearance", "Retired sharing preference", false));
   public final m P = this.addSetting(new m("Receive Cosmetics", "Retired receive preference", true));

   public LARPModule() {
      super("LARP", "Copy a skin, cape and name locally", Category.PLAYER, -1);
      this.q.a(() -> false);
      this.P.a(() -> false);
      X = this;
   }

   public static LARPModule I() {
      return X;
   }

   @Override
   public void onEnable() {
      be.P();
   }

   @Override
   public void onDisable() {
      be.S();
   }

   @Override
   public String getStatus() {
      return (be.k.isEmpty() ? "" : be.k + " · ") + be.A;
   }
}
