package com.larpclient;

public record hm(hm.u state, c9 account) {
   private static volatile hm current = new hm(hm.u.f, null);

   public static hm current() {
      return current;
   }

   static void publish(hm.u var0, c9 var1) {
      current = new hm(var0, var0 == hm.u.D ? var1 : null);
   }

   public enum u {
      f("Discord activity off", -8354674),
      L("Discord offline", -8354674),
      D("Online", -14441126);

      private final String h;
      private final int Z;

      u(String var3, int var4) {
         this.h = var3;
         this.Z = var4;
      }

      public String H() {
         return this.h;
      }

      public int o() {
         return this.Z;
      }
   }
}
