package com.larpclient;

public record lw(int zenith, int horizon) {
   public static lw of(String var0, int var1, float var2) {
      return switch (var0) {
         case "Theme" -> new lw(ei.W(-16315882, ez.a, 0.16), ei.W(-15263703, ez.f, 0.48));
         case "Sunset" -> new lw(-14609097, -3245717);
         case "Ocean" -> new lw(-16378327, -12349780);
         case "Purple" -> new lw(-15725786, -8955221);
         case "Red" -> new lw(-14678256, -5486497);
         case "Black" -> new lw(-16645368, -15460316);
         case "Rainbow" -> new lw(ei.X(var2, 0.65F, 0.13F), ei.X(var2, 0.42F, 0.58F));
         case "Custom" -> new lw(ei.W(-16579055, var1 | 0xFF000000, 0.2), var1 | 0xFF000000);
         default -> new lw(-16446431, -12758401);
      };
   }
}
