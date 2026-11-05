package com.larpclient.module;

public class NoWeatherModule extends Module {
   private static boolean g = (boolean)0;

   public NoWeatherModule() {
      super("NoWeather", "Hide rain and snow", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      g = (boolean)1;
   }

   @Override
   public void onDisable() {
      g = (boolean)0;
   }

   public static boolean F() {
      return g;
   }
}
