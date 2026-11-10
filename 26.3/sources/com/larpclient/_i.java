package com.larpclient;

public record _i(int zenith, int horizon) {
   public static _i of(String var0, int var1, float var2) {
      return switch (var0) {
         case "Theme" -> new _i(vv.Q(-16315882, c.y, 0.16), vv.Q(-15263703, c.t, 0.48));
         case "Sunset" -> new _i(-14609097, -3245717);
         case "Ocean" -> new _i(-16378327, -12349780);
         case "Purple" -> new _i(-15725786, -8955221);
         case "Red" -> new _i(-14678256, -5486497);
         case "Black" -> new _i(-16645368, -15460316);
         case "Rainbow" -> new _i(vv.b(var2, 0.65F, 0.13F), vv.b(var2, 0.42F, 0.58F));
         case "Custom" -> new _i(vv.Q(-16579055, var1 | 0xFF000000, 0.2), var1 | 0xFF000000);
         default -> new _i(-16446431, -12758401);
      };
   }
}
