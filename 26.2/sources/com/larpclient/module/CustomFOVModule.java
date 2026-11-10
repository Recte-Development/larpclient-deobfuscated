package com.larpclient.module;

import java.util.Set;

public final class CustomFOVModule extends Module {
   private static CustomFOVModule x;
   private final e W = this.addSetting(new e("FOV", "Field of view in degrees", 110.0, 30.0, 160.0, 1.0));

   public CustomFOVModule() {
      super("Custom FOV", "Changes your field of view.", Category.RENDER, -1);
      x = this;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.k);
   }

   public static float z(float var0) {
      if (x != null && x.isEnabled()) {
         CustomFOVModule var1 = x;
         return var1 != null && var1.isRuntimeActive() ? var1.W.getValue().floatValue() : var0;
      } else {
         return var0;
      }
   }
}
