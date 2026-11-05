package com.larpclient.module;

public class NoWeatherModule extends Module {
   private static boolean r = (boolean)0;

   public NoWeatherModule() {
      super("NoWeather", "Hide rain and snow", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      r = (boolean)1;
   }

   @Override
   public void onDisable() {
      r = (boolean)0;
   }

   public static boolean b() {
      return r;
   }
}
