package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;

public final class StretchedResModule extends Module {
   private static StretchedResModule instance;
   private final NumberSetting stretch = this.addSetting(
      new NumberSetting("Stretch", "Horizontal stretch multiplier; 1 is normal, 1.33 resembles 4:3 on 16:9", 1.33, 1.0, 2.0, 0.01)
   );

   public StretchedResModule() {
      super("Stretched Res", "Widen the world view, keeping the HUD unchanged", Category.VISUALS, -1);
      instance = this;
   }

   public static float getStretch() {
      if (instance != null && instance.isEnabled()) {
         return instance != null && instance.isOperational() ? instance.stretch.getValue().floatValue() : 1.0F;
      } else {
         return 1.0F;
      }
   }
}
