package com.larpclient;

public enum pz {
   Z("nebula", "Larp Nebula"),
   I("golden-hour", "Golden Hour"),
   R("midnight", "Midnight");

   public final String D;
   public final String J;

   pz(String var3, String var4) {
      this.D = var3;
      this.J = var4;
   }

   public static pz Q(String var0) {
      for (pz var4 : values()) {
         if (var4.D.equals(var0)) {
            return var4;
         }
      }

      return Z;
   }
}
