package com.larpclient;

public record b3(b3.p state, av account) {
   private static volatile b3 current = new b3(b3.p.T, null);

   public static b3 current() {
      return current;
   }

   static void publish(b3.p var0, av var1) {
      current = new b3(var0, var0 == b3.p.c ? var1 : null);
   }

   public enum p {
      T("Discord activity off", -8354674),
      P("Discord offline", -8354674),
      c("Online", -14441126);

      private final String f;
      private final int B;

      p(String var3, int var4) {
         this.f = var3;
         this.B = var4;
      }

      public String R() {
         return this.f;
      }

      public int G() {
         return this.B;
      }
   }
}
