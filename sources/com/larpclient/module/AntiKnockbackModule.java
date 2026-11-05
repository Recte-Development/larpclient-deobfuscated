package com.larpclient.module;

import java.util.Set;

public class AntiKnockbackModule extends Module {
   private final lc k = this.addSetting(new lc("Horizontal", "Horizontal KB multiplier", 0.0, 0.0, 1.0, 0.05));
   private final lc F = this.addSetting(new lc("Vertical", "Vertical KB multiplier", 0.0, 0.0, 1.0, 0.05));

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   public AntiKnockbackModule() {
      super("AntiKnockback", "Reduce knockback from hits and explosions", Category.COMBAT, -1);
   }

   public double F() {
      return this.isEnabled() ? this.k.getValue() : 1.0;
   }

   public double b() {
      return this.isEnabled() ? this.F.getValue() : 1.0;
   }
}
