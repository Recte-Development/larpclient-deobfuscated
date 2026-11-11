package com.larpclient.module.render;

import com.larpclient.config.GradientOutlineSettings;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.render.entity.HandChamsRenderer;
import com.larpclient.util.misc.LarpLog;

public final class HandChamsModule extends Module {
   private static HandChamsModule instance;
   public final GradientOutlineSettings outlineSettings = new GradientOutlineSettings(var1 -> this.addSetting(var1));

   public HandChamsModule() {
      super("Hand Chams", "Animated gradient outlines, fill and glow on your hands and held items", Category.RENDER, -1);
      instance = this;
   }

   public static HandChamsModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      LarpLog.info("HandChams", "Echo gradient enabled for native hands and held items");
   }

   @Override
   public void onDisable() {
      HandChamsRenderer.reset();
      this.outlineSettings.resetPhase();
   }

   @Override
   public void onTick() {
      HandChamsRenderer.validate();
   }
}
