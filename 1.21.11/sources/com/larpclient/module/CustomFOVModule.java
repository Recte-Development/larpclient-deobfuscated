package com.larpclient.module;

import java.util.Set;

public final class CustomFOVModule extends Module {
   private static CustomFOVModule W;
   private final lo t = this.addSetting(new lo("FOV", "Field of view in degrees", 110.0, 30.0, 160.0, 1.0));

   public CustomFOVModule() {
      super("Custom FOV", "Changes your field of view.", Category.RENDER, -1);
      W = this;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E);
   }

   public static float W(float var0) {
      if (W != null && W.isEnabled()) {
         CustomFOVModule var1 = W;
         return var1 != null && var1.isRuntimeActive() ? var1.t.getValue().floatValue() : var0;
      } else {
         return var0;
      }
   }
}
