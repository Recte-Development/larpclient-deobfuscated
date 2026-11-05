package com.larpclient;

public record o(o.g state, xp account) {
   private static volatile o current = new o(o.g.N, null);

   public static o current() {
      return current;
   }

   static void publish(o.g var0, xp var1) {
      current = new o(var0, var0 == o.g.v ? var1 : null);
   }

   public enum g {
      N("Discord activity off", -8354674),
      z("Discord offline", -8354674),
      v("Online", -14441126);

      private final String C;
      private final int n;

      g(String var3, int var4) {
         this.C = var3;
         this.n = var4;
      }

      public String u() {
         return this.C;
      }

      public int x() {
         return this.n;
      }
   }
}
