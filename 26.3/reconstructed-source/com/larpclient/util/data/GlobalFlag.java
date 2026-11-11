package com.larpclient.util.data;

public final class GlobalFlag {
   private static boolean active;

   private GlobalFlag() {
   }

   public static void setActive(boolean var0) {
      active = var0;
   }

   public static boolean isActive() {
      return active;
   }
}
