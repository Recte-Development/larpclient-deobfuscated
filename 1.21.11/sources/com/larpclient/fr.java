package com.larpclient;

public final class fr {
   private fr() {
   }

   public static void V(String var0) {
      if (var0 != null && !var0.isBlank()) {
         q8.v("Rig/Swap", "{}", var0.replace('\r', ' ').replace('\n', ' '));
      }
   }
}
