package com.larpclient.module;

public final class StretchedResModule extends Module {
   private static StretchedResModule l;
   private final d9 S = this.addSetting(new d9("Stretch", "Horizontal stretch multiplier; 1 is normal, 1.33 resembles 4:3 on 16:9", 1.33, 1.0, 2.0, 0.01));

   public StretchedResModule() {
      super("Stretched Res", "Widen the world view, keeping the HUD unchanged", Category.VISUALS, -1);
      l = this;
   }

   public static float F() {
      if (l != null && l.isEnabled()) {
         return l != null && l.isOperational() ? l.S.P().floatValue() : 1.0F;
      } else {
         return 1.0F;
      }
   }
}
