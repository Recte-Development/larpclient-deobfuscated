package com.larpclient.module.movement;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.ScanPhase;
import java.util.Set;

public class NoSlowdownModule extends Module {
   private final BooleanSetting items = this.addSetting(new BooleanSetting("Items", "No item use slowdown", true));
   private final BooleanSetting webs = this.addSetting(new BooleanSetting("Webs", "No cobweb slowdown", true));
   private final BooleanSetting soulSand = this.addSetting(new BooleanSetting("Soul Sand", "No soul sand slowdown", true));
   private static NoSlowdownModule instance;

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   public NoSlowdownModule() {
      super("NoSlowdown", "Reduce item and block slowdown", Category.MOVEMENT, -1);
      instance = this;
   }

   public static NoSlowdownModule getInstance() {
      return instance;
   }

   public boolean shouldIgnoreItemSlowdown() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.items.getValue();
   }

   public boolean shouldIgnoreWebs() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.webs.getValue();
   }

   public boolean shouldIgnoreSoulSand() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.soulSand.getValue();
   }
}
