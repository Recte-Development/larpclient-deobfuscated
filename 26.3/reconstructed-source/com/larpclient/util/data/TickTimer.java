package com.larpclient.util.data;

public final class TickTimer {
   private static long tickCount;
   private static double currentTime;
   private static double deltaTime;
   private static float partialTick;

   public static void tick() {
      tickCount++;
   }

   public static void updatePartialTick(float var0) {
      partialTick = Math.clamp(var0, 0.0F, 1.0F);
      double var1 = ((float)tickCount + partialTick) * 0.05;
      deltaTime = Math.clamp(var1 - currentTime, 0.0, 0.1);
      currentTime = var1;
   }

   public static double getTime() {
      return currentTime;
   }

   public static double getDeltaTime() {
      return deltaTime;
   }

   public static float getPartialTick() {
      return partialTick;
   }

   public static double getTickTime() {
      return tickCount * 0.05;
   }

   private TickTimer() {
   }
}
