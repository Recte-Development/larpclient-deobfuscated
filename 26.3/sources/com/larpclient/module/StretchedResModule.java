package com.larpclient.module;

public final class StretchedResModule extends Module {
   private static StretchedResModule c;
   private final b u = this.addSetting(new b("Stretch", "Horizontal stretch multiplier; 1 is normal, 1.33 resembles 4:3 on 16:9", 1.33, 1.0, 2.0, 0.01));

   public StretchedResModule() {
      super("Stretched Res", "Widen the world view, keeping the HUD unchanged", Category.VISUALS, -1);
      c = this;
   }

   public static float m() {
      if (c != null && c.isEnabled()) {
         return c != null && c.isOperational() ? c.u.getValue().floatValue() : 1.0F;
      } else {
         return 1.0F;
      }
   }
}
