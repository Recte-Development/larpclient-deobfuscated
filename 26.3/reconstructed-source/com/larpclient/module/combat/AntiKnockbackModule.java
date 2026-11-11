package com.larpclient.module.combat;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import java.util.Set;

public class AntiKnockbackModule extends Module {
   private final NumberSetting horizontal = this.addSetting(new NumberSetting("Horizontal", "Horizontal KB multiplier", 0.0, 0.0, 1.0, 0.05));
   private final NumberSetting vertical = this.addSetting(new NumberSetting("Vertical", "Vertical KB multiplier", 0.0, 0.0, 1.0, 0.05));

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   public AntiKnockbackModule() {
      super("AntiKnockback", "Reduce knockback from hits and explosions", Category.COMBAT, -1);
   }

   public double getHorizontalMultiplier() {
      return this.isEnabled() ? this.horizontal.getValue() : 1.0;
   }

   public double getVerticalMultiplier() {
      return this.isEnabled() ? this.vertical.getValue() : 1.0;
   }
}
