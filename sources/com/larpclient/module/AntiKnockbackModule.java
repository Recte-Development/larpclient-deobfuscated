package com.larpclient.module;

import java.util.Set;

public class AntiKnockbackModule extends Module {
   private final d9 q = this.addSetting(new d9("Horizontal", "Horizontal KB multiplier", 0.0, 0.0, 1.0, 0.05));
   private final d9 s = this.addSetting(new d9("Vertical", "Vertical KB multiplier", 0.0, 0.0, 1.0, 0.05));

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   public AntiKnockbackModule() {
      super("AntiKnockback", "Reduce knockback from hits and explosions", Category.COMBAT, -1);
   }

   public double A() {
      return this.isEnabled() ? this.q.P() : 1.0;
   }

   public double V() {
      return this.isEnabled() ? this.s.P() : 1.0;
   }
}
