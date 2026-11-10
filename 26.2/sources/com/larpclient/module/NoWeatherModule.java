package com.larpclient.module;

public class NoWeatherModule extends Module {
   private static boolean z = (boolean)0;

   public NoWeatherModule() {
      super("NoWeather", "Hide rain and snow", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      z = (boolean)1;
   }

   @Override
   public void onDisable() {
      z = (boolean)0;
   }

   public static boolean R() {
      return z;
   }
}
