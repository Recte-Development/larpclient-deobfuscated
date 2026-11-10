package com.larpclient.module;

import java.util.Set;

public final class CustomFOVModule extends Module {
   private static CustomFOVModule h;
   private final b p = this.addSetting(new b("FOV", "Field of view in degrees", 110.0, 30.0, 160.0, 1.0));

   public CustomFOVModule() {
      super("Custom FOV", "Changes your field of view.", Category.RENDER, -1);
      h = this;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.E);
   }

   public static float A(float var0) {
      if (h != null && h.isEnabled()) {
         CustomFOVModule var1 = h;
         return var1 != null && var1.isRuntimeActive() ? var1.p.getValue().floatValue() : var0;
      } else {
         return var0;
      }
   }
}
