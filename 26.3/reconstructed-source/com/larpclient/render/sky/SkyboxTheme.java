package com.larpclient.render.sky;

public enum SkyboxTheme {
   nEBULA("nebula", "Larp Nebula"),
   gOLDEN_HOUR("golden-hour", "Golden Hour"),
   mIDNIGHT("midnight", "Midnight");

   public final String id;
   public final String displayName;

   SkyboxTheme(String var3, String var4) {
      this.id = var3;
      this.displayName = var4;
   }

   public static SkyboxTheme fromId(String var0) {
      for (SkyboxTheme var4 : values()) {
         if (var4.id.equals(var0)) {
            return var4;
         }
      }

      return nEBULA;
   }
}
