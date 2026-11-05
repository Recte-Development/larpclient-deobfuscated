package com.larpclient.module;

public final class StretchedResModule extends Module {
   private static StretchedResModule f;
   private final lc d = this.addSetting(new lc("Stretch", "Horizontal stretch multiplier; 1 is normal, 1.33 resembles 4:3 on 16:9", 1.33, 1.0, 2.0, 0.01));

   public StretchedResModule() {
      super("Stretched Res", "Widen the world view, keeping the HUD unchanged", Category.VISUALS, -1);
      f = this;
   }

   public static float n() {
      if (f != null && f.isEnabled()) {
         return f != null && f.isOperational() ? f.d.getValue().floatValue() : 1.0F;
      } else {
         return 1.0F;
      }
   }
}
