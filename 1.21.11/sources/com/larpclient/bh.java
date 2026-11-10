package com.larpclient;

public record bh(bh.d state, ui account) {
   private static volatile bh current = new bh(bh.d.k, null);

   public static bh current() {
      return current;
   }

   static void publish(bh.d var0, ui var1) {
      current = new bh(var0, var0 == bh.d.f ? var1 : null);
   }

   public enum d {
      k("Discord activity off", -8354674),
      q("Discord offline", -8354674),
      f("Online", -14441126);

      private final String X;
      private final int c;

      d(String var3, int var4) {
         this.X = var3;
         this.c = var4;
      }

      public String V() {
         return this.X;
      }

      public int Y() {
         return this.c;
      }
   }
}
