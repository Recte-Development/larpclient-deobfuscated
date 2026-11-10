package com.larpclient.module;

public final class StretchedResModule extends Module {
   private static StretchedResModule Z;
   private final lo r = this.addSetting(new lo("Stretch", "Horizontal stretch multiplier; 1 is normal, 1.33 resembles 4:3 on 16:9", 1.33, 1.0, 2.0, 0.01));

   public StretchedResModule() {
      super("Stretched Res", "Widen the world view, keeping the HUD unchanged", Category.VISUALS, -1);
      Z = this;
   }

   public static float u() {
      if (Z != null && Z.isEnabled()) {
         return Z != null && Z.isOperational() ? Z.r.getValue().floatValue() : 1.0F;
      } else {
         return 1.0F;
      }
   }
}
