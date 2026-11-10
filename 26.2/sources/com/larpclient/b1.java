package com.larpclient;

public record b1(int zenith, int horizon) {
   public static b1 of(String var0, int var1, float var2) {
      return switch (var0) {
         case "Theme" -> new b1(uh.q(-16315882, ho.J, 0.16), uh.q(-15263703, ho.ds, 0.48));
         case "Sunset" -> new b1(-14609097, -3245717);
         case "Ocean" -> new b1(-16378327, -12349780);
         case "Purple" -> new b1(-15725786, -8955221);
         case "Red" -> new b1(-14678256, -5486497);
         case "Black" -> new b1(-16645368, -15460316);
         case "Rainbow" -> new b1(uh.n(var2, 0.65F, 0.13F), uh.n(var2, 0.42F, 0.58F));
         case "Custom" -> new b1(uh.q(-16579055, var1 | 0xFF000000, 0.2), var1 | 0xFF000000);
         default -> new b1(-16446431, -12758401);
      };
   }
}
