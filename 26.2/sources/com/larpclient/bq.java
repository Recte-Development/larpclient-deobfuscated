package com.larpclient;

public record bq(bq.b state, ui account) {
   private static volatile bq current = new bq(bq.b.v, null);

   public static bq current() {
      return current;
   }

   static void publish(bq.b var0, ui var1) {
      current = new bq(var0, var0 == bq.b.P ? var1 : null);
   }

   public enum b {
      v("Discord activity off", -8354674),
      R("Discord offline", -8354674),
      P("Online", -14441126);

      private final String G;
      private final int u;

      b(String var3, int var4) {
         this.G = var3;
         this.u = var4;
      }

      public String Z() {
         return this.G;
      }

      public int C() {
         return this.u;
      }
   }
}
