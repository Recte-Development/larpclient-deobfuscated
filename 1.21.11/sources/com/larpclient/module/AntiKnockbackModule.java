package com.larpclient.module;

import java.util.Set;

public class AntiKnockbackModule extends Module {
   private final lo v = this.addSetting(new lo("Horizontal", "Horizontal KB multiplier", 0.0, 0.0, 1.0, 0.05));
   private final lo R = this.addSetting(new lo("Vertical", "Vertical KB multiplier", 0.0, 0.0, 1.0, 0.05));

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public AntiKnockbackModule() {
      super("AntiKnockback", "Reduce knockback from hits and explosions", Category.COMBAT, -1);
   }

   public double x() {
      return this.isEnabled() ? this.v.getValue() : 1.0;
   }

   public double f() {
      return this.isEnabled() ? this.R.getValue() : 1.0;
   }
}
