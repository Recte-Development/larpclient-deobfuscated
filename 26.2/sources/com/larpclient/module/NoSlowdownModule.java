package com.larpclient.module;

import java.util.Set;

public class NoSlowdownModule extends Module {
   private final s Z = this.addSetting(new s("Items", "No item use slowdown", true));
   private final s K = this.addSetting(new s("Webs", "No cobweb slowdown", true));
   private final s j = this.addSetting(new s("Soul Sand", "No soul sand slowdown", true));
   private static NoSlowdownModule f;

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   public NoSlowdownModule() {
      super("NoSlowdown", "Reduce item and block slowdown", Category.MOVEMENT, -1);
      f = this;
   }

   public static NoSlowdownModule Q() {
      return f;
   }

   public boolean Q() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.Z.getValue();
   }

   public boolean Y() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.K.getValue();
   }

   public boolean H() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.j.getValue();
   }
}
