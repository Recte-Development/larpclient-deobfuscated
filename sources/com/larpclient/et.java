package com.larpclient;

public record et(int zenith, int horizon) {
   public static et of(String var0, int var1, float var2) {
      return switch (var0) {
         case "Theme" -> new et(_3.D(-16315882, f.C, 0.16), _3.D(-15263703, f.O, 0.48));
         case "Sunset" -> new et(-14609097, -3245717);
         case "Ocean" -> new et(-16378327, -12349780);
         case "Purple" -> new et(-15725786, -8955221);
         case "Red" -> new et(-14678256, -5486497);
         case "Black" -> new et(-16645368, -15460316);
         case "Rainbow" -> new et(_3.Z(var2, 0.65F, 0.13F), _3.Z(var2, 0.42F, 0.58F));
         case "Custom" -> new et(_3.D(-16579055, var1 | 0xFF000000, 0.2), var1 | 0xFF000000);
         default -> new et(-16446431, -12758401);
      };
   }
}
