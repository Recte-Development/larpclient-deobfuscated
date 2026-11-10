package com.larpclient.module;

import com.larpclient.wk;

public final class LARPModule extends Module {
   private static LARPModule X;
   public final l3 m = this.addSetting(new l3("Share Appearance", "Retired sharing preference", false));
   public final l3 p = this.addSetting(new l3("Receive Cosmetics", "Retired receive preference", true));

   public LARPModule() {
      super("LARP", "Copy a skin, cape and name locally", Category.PLAYER, -1);
      this.m.O(() -> false);
      this.p.O(() -> false);
      X = this;
   }

   public static LARPModule f() {
      return X;
   }

   @Override
   public void onEnable() {
      wk.b();
   }

   @Override
   public void onDisable() {
      wk.V();
   }

   @Override
   public String getStatus() {
      return (wk.C.isEmpty() ? "" : wk.C + " · ") + wk.H;
   }
}
