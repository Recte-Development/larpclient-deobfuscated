package com.larpclient;

public final class hc {
   public static hc.w B(int var0, boolean var1, boolean var2) {
      if (var1 && var0 >= 0 && var0 <= 20) {
         return var2 ? hc.w.B : hc.w.m;
      } else {
         return hc.w.M;
      }
   }

   private hc() {
   }

   public enum w {
      M,
      m,
      B;
   }
}
