package com.larpclient;

public enum t2 {
   K("nebula", "Larp Nebula"),
   G("golden-hour", "Golden Hour"),
   i("midnight", "Midnight");

   public final String B;
   public final String z;

   t2(String var3, String var4) {
      this.B = var3;
      this.z = var4;
   }

   public static t2 k(String var0) {
      for (t2 var4 : values()) {
         if (var4.B.equals(var0)) {
            return var4;
         }
      }

      return K;
   }
}
