package com.larpclient.module;

public class NoWeatherModule extends Module {
   private static boolean R = (boolean)0;

   public NoWeatherModule() {
      super("NoWeather", "Hide rain and snow", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      R = (boolean)1;
   }

   @Override
   public void onDisable() {
      R = (boolean)0;
   }

   public static boolean U() {
      return R;
   }
}
