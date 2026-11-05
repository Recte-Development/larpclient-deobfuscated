package com.larpclient;

public enum _w {
   N("nebula", "Larp Nebula"),
   s("golden-hour", "Golden Hour"),
   V("midnight", "Midnight");

   public final String G;
   public final String S;

   _w(String var3, String var4) {
      this.G = var3;
      this.S = var4;
   }

   public static _w b(String var0) {
      for (_w var4 : values()) {
         if (var4.G.equals(var0)) {
            return var4;
         }
      }

      return N;
   }
}
