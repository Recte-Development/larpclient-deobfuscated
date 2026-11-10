package com.larpclient.module;

import java.util.Set;

public class AntiKnockbackModule extends Module {
   private final e q = this.addSetting(new e("Horizontal", "Horizontal KB multiplier", 0.0, 0.0, 1.0, 0.05));
   private final e y = this.addSetting(new e("Vertical", "Vertical KB multiplier", 0.0, 0.0, 1.0, 0.05));

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   public AntiKnockbackModule() {
      super("AntiKnockback", "Reduce knockback from hits and explosions", Category.COMBAT, -1);
   }

   public double Y() {
      return this.isEnabled() ? this.q.getValue() : 1.0;
   }

   public double z() {
      return this.isEnabled() ? this.y.getValue() : 1.0;
   }
}
