package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class st {
   public static String f(JsonObject var0, String var1) {
      String var2 = _4.m(var0, var1, 128);
      if (!var2.matches("[A-Za-z0-9_-]{1,128}")) {
         throw wr.O();
      } else {
         return var2;
      }
   }

   public static String N(String var0) {
      var0 = var0.trim().toUpperCase(Locale.ROOT);
      if (!var0.matches("(?:LARP-)?[A-F0-9]{16}")) {
         throw new IllegalArgumentException("Enter a LARP share code with 16 hexadecimal characters.");
      } else {
         return var0.startsWith("LARP-") ? var0 : "LARP-" + var0;
      }
   }

   public static String W(JsonObject var0) {
      String var1 = _4.m(var0, "shareCode", 21);
      if (!var1.matches("LARP-[A-F0-9]{16}")) {
         throw wr.O();
      } else {
         return var1;
      }
   }

   public static String B(JsonObject var0) {
      String var1 = _4.m(var0, "id", 16);
      if (!var1.matches("LRP-[A-F0-9]{12}")) {
         throw wr.O();
      } else {
         return var1;
      }
   }

   public static String o(JsonObject var0) {
      return switch (_4.Z(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed")) {
         case "private" -> "Private (unlisted)";
         case "submitted" -> "Pending review";
         case "approved" -> "Approved";
         case "rejected" -> "Rejected";
         default -> "Removed";
      };
   }

   public static boolean b(JsonObject var0) {
      return var0.has("discordSharing") && _4.Y(var0.get("discordSharing"));
   }

   public static String l(JsonObject var0) {
      if (!b(var0)) {
         return "";
      }

      String var1 = _4.Z(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed");
      return var1.equals("removed") ? "Taken off Discord by staff" : "Posted in Discord";
   }

   public static List<JsonObject> G(JsonObject var0, boolean var1) {
      if (var0.has("items") && var0.get("items").isJsonArray() && var0.getAsJsonArray("items").size() <= 100) {
         ArrayList var2 = new ArrayList();

         for (JsonElement var4 : var0.getAsJsonArray("items")) {
            JsonObject var5 = _4.J(var4);
            f(var5, "id");
            f(var5, "revision");
            _4.p(_4.m(var5, "name", 32));
            _4.m(var5, "updatedAt", 40);
            if (!var1) {
               W(var5);
               o(var5);
               _4.m(var5, "rejectionReason", 500);
               l(var5);
            }

            var2.add(var5.deepCopy());
         }

         return List.copyOf(var2);
      } else {
         throw wr.O();
      }
   }

   public static st.v A(JsonObject var0) {
      try {
         String var1 = _4.m(var0, "version", 40);
         if (!var1.matches("[A-Za-z0-9][A-Za-z0-9.+_-]*")) {
            throw wr.O();
         }

         if (!var0.has("updateAvailable")) {
            throw wr.O();
         }

         Boolean var2 = var0.get("updateAvailable").isJsonNull() ? null : _4.Y(var0.get("updateAvailable"));
         if (Boolean.TRUE.equals(var2) && var1.matches("\\d+(?:\\.\\d+){0,3}") && a0.r.matches("\\d+(?:\\.\\d+){0,3}")) {
            String[] var3 = var1.split("\\.");
            String[] var4 = a0.r.split("\\.");
            int var5 = 0;

            for (int var6 = 0; var6 < Math.max(var3.length, var4.length) && var5 == 0; var6++) {
               var5 = new BigInteger(var6 < var3.length ? var3[var6] : "0").compareTo(new BigInteger(var6 < var4.length ? var4[var6] : "0"));
            }

            if (var5 <= 0) {
               var2 = false;
            }
         }

         ArrayList var8 = new ArrayList();
         var8.add("Larp " + var1 + " — " + _4.m(var0, "title", 120));
         var8.add(
            _4.m(var0, "releaseDate", 10)
               + " / Minecraft "
               + _4.m(var0, "minecraftVersion", 40)
               + " / "
               + _4.Z(var0.get("status"), "stable", "beta", "experimental")
         );
         var8.add(_4.m(var0, "summary", 500));
         if (var0.has("notice") && !var0.get("notice").isJsonNull()) {
            var8.add(_4.m(var0, "notice", 1000));
         }

         if (var0.has("notes") && var0.get("notes").isJsonArray() && var0.getAsJsonArray("notes").size() <= 100) {
            for (JsonElement var10 : var0.getAsJsonArray("notes")) {
               JsonObject var11 = _4.J(var10);
               var8.add(_4.Z(var11.get("category"), "Added", "Changed", "Fixed", "Visuals", "Performance", "Notes") + ": " + _4.m(var11, "text", 2000));
            }

            return new st.v(var1, var2, List.copyOf(var8));
         } else {
            throw wr.O();
         }
      } catch (RuntimeException var7) {
         throw wr.O();
      }
   }

   public static st.l f(JsonObject var0) {
      try {
         String var1 = _4.Z(var0.get("state"), "Operational", "Degraded", "Unavailable");
         if (var0.has("services") && var0.get("services").isJsonArray() && var0.getAsJsonArray("services").size() <= 20) {
            byte var2 = 0;
            ArrayList var3 = new ArrayList();

            for (JsonElement var5 : var0.getAsJsonArray("services")) {
               JsonObject var6 = _4.J(var5);
               String var7 = f(var6, "id");
               String var8 = _4.Z(var6.get("state"), "Operational", "Degraded", "Unavailable");
               if (Set.of("authentication", "client-session").contains(var7) && !var8.equals("Operational")) {
                  var2 = 1;
               }

               var3.add(_4.m(var6, "name", 120) + ": " + var8);
            }

            return new st.l(var1, (boolean)var2, List.copyOf(var3));
         } else {
            throw wr.O();
         }
      } catch (RuntimeException var9) {
         throw wr.O();
      }
   }

   public record l(String state, boolean authIssue, List<String> lines) {
   }

   public record v(String version, Boolean updateAvailable, List<String> lines) {
   }
}
