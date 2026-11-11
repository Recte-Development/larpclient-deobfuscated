package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.world.WorldColorRenderer;
import com.larpclient.util.misc.LarpLog;

public final class WorldColorModule extends Module {
   private static WorldColorModule instance;
   public final NumberSetting saturation = this.addSetting(new NumberSetting("Saturation", "Color intensity", 1.0, 0.0, 2.0, 0.05));
   public final NumberSetting contrast = this.addSetting(new NumberSetting("Contrast", "Mid-tone contrast", 1.0, 0.5, 1.5, 0.025));
   public final NumberSetting exposure = this.addSetting(new NumberSetting("Exposure", "Exposure stops", 0.0, -2.0, 2.0, 0.05));
   public final NumberSetting gamma = this.addSetting(new NumberSetting("Gamma", "Display gamma adjustment", 1.0, 0.5, 2.0, 0.05));
   public final ColorSetting tint = this.addSetting(new ColorSetting("Tint", "Overall multiplicative tint", -1));
   public final ColorSetting shadowTint = this.addSetting(new ColorSetting("Shadow Tint", "Dark-region tint", -1));
   public final ColorSetting highlightTint = this.addSetting(new ColorSetting("Highlight Tint", "Bright-region tint", -1));
   public final NumberSetting vignette = this.addSetting(new NumberSetting("Vignette", "Subtle edge darkening", 0.0, 0.0, 0.4, 0.01));
   public final ColorSetting ambientColor = this.addSetting(new ColorSetting("Ambient Color", "Flat tint of native world lighting", -5342721));
   public final NumberSetting ambientStrength = this.addSetting(
      new NumberSetting("Ambient Strength", "Lighting tint strength; zero keeps native lighting", 0.0, 0.0, 1.0, 0.05)
   );

   public WorldColorModule() {
      super("WorldColor", "Adjust world colors", Category.RENDER, -1);
      instance = this;
      this.ambientColor.visibleWhen(() -> this.ambientStrength.getValue() > 0.0);
   }

   public static WorldColorModule getInstance() {
      return instance;
   }

   public boolean isActive() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "WorldColor",
         "Native lighting tint enabled; color={}; strength={}; grading controls retained",
         Integer.toHexString(this.ambientColor.getValue()),
         this.ambientStrength.getValue()
      );
   }

   @Override
   public void onDisable() {
      WorldColorRenderer.release();
   }
}
