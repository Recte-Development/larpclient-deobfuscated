package com.larpclient;

public final class l1 {
   public static l1.x H(int var0, boolean var1, boolean var2) {
      if (var1 && var0 >= 0 && var0 <= 20) {
         return var2 ? l1.x.v : l1.x.G;
      } else {
         return l1.x.x;
      }
   }

   private l1() {
   }

   public enum x {
      x,
      G,
      v;
   }
}
