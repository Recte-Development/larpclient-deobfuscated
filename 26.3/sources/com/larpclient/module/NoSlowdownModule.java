package com.larpclient.module;

import java.util.Set;

public class NoSlowdownModule extends Module {
   private final m q = this.addSetting(new m("Items", "No item use slowdown", true));
   private final m x = this.addSetting(new m("Webs", "No cobweb slowdown", true));
   private final m a = this.addSetting(new m("Soul Sand", "No soul sand slowdown", true));
   private static NoSlowdownModule Q;

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   public NoSlowdownModule() {
      super("NoSlowdown", "Reduce item and block slowdown", Category.MOVEMENT, -1);
      Q = this;
   }

   public static NoSlowdownModule J() {
      return Q;
   }

   public boolean V() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.q.getValue();
   }

   public boolean Y() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.x.getValue();
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.a.getValue();
   }
}
