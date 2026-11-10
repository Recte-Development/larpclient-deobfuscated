package com.larpclient.module;

import java.util.Set;

public class NoSlowdownModule extends Module {
   private final l3 B = this.addSetting(new l3("Items", "No item use slowdown", true));
   private final l3 R = this.addSetting(new l3("Webs", "No cobweb slowdown", true));
   private final l3 F = this.addSetting(new l3("Soul Sand", "No soul sand slowdown", true));
   private static NoSlowdownModule s;

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public NoSlowdownModule() {
      super("NoSlowdown", "Reduce item and block slowdown", Category.MOVEMENT, -1);
      s = this;
   }

   public static NoSlowdownModule k() {
      return s;
   }

   public boolean H() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.B.getValue();
   }

   public boolean a() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.R.getValue();
   }

   public boolean p() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.F.getValue();
   }
}
