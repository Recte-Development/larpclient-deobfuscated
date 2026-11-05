package com.larpclient.module;

import java.util.Set;

public class NoSlowdownModule extends Module {
   private final ln c = this.addSetting(new ln("Items", "No item use slowdown", true));
   private final ln K = this.addSetting(new ln("Webs", "No cobweb slowdown", true));
   private final ln Y = this.addSetting(new ln("Soul Sand", "No soul sand slowdown", true));
   private static NoSlowdownModule y;

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   public NoSlowdownModule() {
      super("NoSlowdown", "Reduce item and block slowdown", Category.MOVEMENT, -1);
      y = this;
   }

   public static NoSlowdownModule v() {
      return y;
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.c.getValue();
   }

   public boolean p() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.K.getValue();
   }

   public boolean q() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.Y.getValue();
   }
}
