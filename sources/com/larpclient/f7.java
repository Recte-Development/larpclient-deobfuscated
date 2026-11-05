package com.larpclient;

public enum f7 {
   o("nebula", "Larp Nebula"),
   p("golden-hour", "Golden Hour"),
   F("midnight", "Midnight");

   public final String e;
   public final String f;

   f7(String var3, String var4) {
      this.e = var3;
      this.f = var4;
   }

   public static f7 x(String var0) {
      for (f7 var4 : values()) {
         if (var4.e.equals(var0)) {
            return var4;
         }
      }

      return o;
   }
}
