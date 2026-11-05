package com.larpclient.module;

import java.util.Set;

public class NoSlowdownModule extends Module {
   private final dh A = this.addSetting(new dh("Items", "No item use slowdown", true));
   private final dh w = this.addSetting(new dh("Webs", "No cobweb slowdown", true));
   private final dh K = this.addSetting(new dh("Soul Sand", "No soul sand slowdown", true));
   private static NoSlowdownModule R;

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   public NoSlowdownModule() {
      super("NoSlowdown", "Reduce item and block slowdown", Category.MOVEMENT, -1);
      R = this;
   }

   public static NoSlowdownModule n() {
      return R;
   }

   public boolean s() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.A.P();
   }

   public boolean m() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.w.P();
   }

   public boolean u() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.K.P();
   }
}
