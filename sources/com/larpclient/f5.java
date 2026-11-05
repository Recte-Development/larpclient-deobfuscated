package com.larpclient;

public record f5(int zenith, int horizon) {
   public static f5 of(String var0, int var1, float var2) {
      return switch (var0) {
         case "Theme" -> new f5(oh.j(-16315882, rv.e, 0.16), oh.j(-15263703, rv.GQ, 0.48));
         case "Sunset" -> new f5(-14609097, -3245717);
         case "Ocean" -> new f5(-16378327, -12349780);
         case "Purple" -> new f5(-15725786, -8955221);
         case "Red" -> new f5(-14678256, -5486497);
         case "Black" -> new f5(-16645368, -15460316);
         case "Rainbow" -> new f5(oh.O(var2, 0.65F, 0.13F), oh.O(var2, 0.42F, 0.58F));
         case "Custom" -> new f5(oh.j(-16579055, var1 | 0xFF000000, 0.2), var1 | 0xFF000000);
         default -> new f5(-16446431, -12758401);
      };
   }
}
