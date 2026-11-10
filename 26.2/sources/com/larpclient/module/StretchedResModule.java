package com.larpclient.module;

public final class StretchedResModule extends Module {
   private static StretchedResModule t;
   private final e S = this.addSetting(new e("Stretch", "Horizontal stretch multiplier; 1 is normal, 1.33 resembles 4:3 on 16:9", 1.33, 1.0, 2.0, 0.01));

   public StretchedResModule() {
      super("Stretched Res", "Widen the world view, keeping the HUD unchanged", Category.VISUALS, -1);
      t = this;
   }

   public static float T() {
      if (t != null && t.isEnabled()) {
         return t != null && t.isOperational() ? t.S.getValue().floatValue() : 1.0F;
      } else {
         return 1.0F;
      }
   }
}
