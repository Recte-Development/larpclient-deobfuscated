package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;

public class NoWeatherModule extends Module {
   private static boolean active = (boolean)0;

   public NoWeatherModule() {
      super("NoWeather", "Hide rain and snow", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      active = (boolean)1;
   }

   @Override
   public void onDisable() {
      active = (boolean)0;
   }

   public static boolean isActive() {
      return active;
   }
}
