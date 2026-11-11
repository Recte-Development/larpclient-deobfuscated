package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import java.util.Set;

public final class CustomFOVModule extends Module {
   private static CustomFOVModule instance;
   private final NumberSetting fov = this.addSetting(new NumberSetting("FOV", "Field of view in degrees", 110.0, 30.0, 160.0, 1.0));

   public CustomFOVModule() {
      super("Custom FOV", "Changes your field of view.", Category.RENDER, -1);
      instance = this;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.iDLE);
   }

   public static float getFov(float var0) {
      if (instance != null && instance.isEnabled()) {
         CustomFOVModule var1 = instance;
         return var1 != null && var1.isRuntimeActive() ? var1.fov.getValue().floatValue() : var0;
      } else {
         return var0;
      }
   }
}
