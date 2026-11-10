package com.larpclient.module;

import java.util.Set;

public class AntiKnockbackModule extends Module {
   private final b Q = this.addSetting(new b("Horizontal", "Horizontal KB multiplier", 0.0, 0.0, 1.0, 0.05));
   private final b j = this.addSetting(new b("Vertical", "Vertical KB multiplier", 0.0, 0.0, 1.0, 0.05));

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   public AntiKnockbackModule() {
      super("AntiKnockback", "Reduce knockback from hits and explosions", Category.COMBAT, -1);
   }

   public double A() {
      return this.isEnabled() ? this.Q.getValue() : 1.0;
   }

   public double j() {
      return this.isEnabled() ? this.j.getValue() : 1.0;
   }
}
