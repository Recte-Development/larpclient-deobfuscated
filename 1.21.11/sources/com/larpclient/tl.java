package com.larpclient;

public enum tl {
   t("nebula", "Larp Nebula"),
   W("golden-hour", "Golden Hour"),
   P("midnight", "Midnight");

   public final String X;
   public final String G;

   tl(String var3, String var4) {
      this.X = var3;
      this.G = var4;
   }

   public static tl S(String var0) {
      for (tl var4 : values()) {
         if (var4.X.equals(var0)) {
            return var4;
         }
      }

      return t;
   }
}
