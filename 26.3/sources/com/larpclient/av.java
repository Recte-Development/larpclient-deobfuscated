package com.larpclient;

import com.google.gson.JsonObject;

public record av(String id, String name, String avatar, int fallbackAvatar) {
   private static final int FALLBACKS = 6;
   private static final int LEGACY_FALLBACKS = 5;
   private static final int MAX_NAME = 40;
   private static final int MAX_ID = 24;
   private static final int MAX_HASH = 40;

   public String avatarUrl(int var1) {
      return this.avatar.isEmpty()
         ? "https://cdn.discordapp.com/embed/avatars/" + this.fallbackAvatar + ".png"
         : "https://cdn.discordapp.com/avatars/" + this.id + "/" + this.avatar + ".png?size=" + var1;
   }

   static av read(JsonObject var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = f2.R(var0, "id");
      if (!var1.isEmpty() && var1.length() <= 24 && var1.chars().allMatch(Character::isDigit)) {
         String var2 = clean(f2.R(var0, "global_name"));
         if (var2.isEmpty()) {
            var2 = clean(f2.R(var0, "username"));
         }

         if (var2.isEmpty()) {
            return null;
         }

         String var3 = f2.R(var0, "avatar");
         if (var3.length() > 40 || !var3.chars().allMatch(var0x -> var0x == 95 || var0x >= 48 && var0x <= 57 || var0x >= 97 && var0x <= 102)) {
            var3 = "";
         }

         return new av(var1, var2, var3, fallbackAvatar(var1, f2.R(var0, "discriminator")));
      } else {
         return null;
      }
   }

   private static String clean(String var0) {
      StringBuilder var1 = new StringBuilder(Math.min(var0.length(), 40));
      var0.codePoints().filter(var0x -> var0x == 32 || Character.getType(var0x) != 15).limit(40L).forEach(var1::appendCodePoint);
      return var1.toString().strip();
   }

   private static int fallbackAvatar(String var0, String var1) {
      try {
         return !var1.isEmpty() && !var1.equals("0") ? Integer.parseInt(var1) % 5 : (int)((Long.parseUnsignedLong(var0) >>> 22) % 6L);
      } catch (NumberFormatException var3) {
         return 0;
      }
   }
}
