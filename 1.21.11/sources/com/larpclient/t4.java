package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class t4 {
   public static String a(JsonObject var0, String var1) {
      String var2 = h.f(var0, var1, 128);
      if (!var2.matches("[A-Za-z0-9_-]{1,128}")) {
         throw u6.V();
      } else {
         return var2;
      }
   }

   public static String c(String var0) {
      var0 = var0.trim().toUpperCase(Locale.ROOT);
      if (!var0.matches("(?:LARP-)?[A-F0-9]{16}")) {
         throw new IllegalArgumentException("Enter a LARP share code with 16 hexadecimal characters.");
      } else {
         return var0.startsWith("LARP-") ? var0 : "LARP-" + var0;
      }
   }

   public static String F(JsonObject var0) {
      String var1 = h.f(var0, "shareCode", 21);
      if (!var1.matches("LARP-[A-F0-9]{16}")) {
         throw u6.V();
      } else {
         return var1;
      }
   }

   public static String n(JsonObject var0) {
      String var1 = h.f(var0, "id", 16);
      if (!var1.matches("LRP-[A-F0-9]{12}")) {
         throw u6.V();
      } else {
         return var1;
      }
   }

   public static String V(JsonObject var0) {
      return switch (h.v(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed")) {
         case "private" -> "Private (unlisted)";
         case "submitted" -> "Pending review";
         case "approved" -> "Approved";
         case "rejected" -> "Rejected";
         default -> "Removed";
      };
   }

   public static boolean S(JsonObject var0) {
      return var0.has("discordSharing") && h.o(var0.get("discordSharing"));
   }

   public static String u(JsonObject var0) {
      if (!S(var0)) {
         return "";
      }

      String var1 = h.v(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed");
      return var1.equals("removed") ? "Taken off Discord by staff" : "Posted in Discord";
   }

   public static List<JsonObject> J(JsonObject var0, boolean var1) {
      if (var0.has("items") && var0.get("items").isJsonArray() && var0.getAsJsonArray("items").size() <= 100) {
         ArrayList var2 = new ArrayList();

         for (JsonElement var4 : var0.getAsJsonArray("items")) {
            JsonObject var5 = h.Z(var4);
            a(var5, "id");
            a(var5, "revision");
            h.Y(h.f(var5, "name", 32));
            h.f(var5, "updatedAt", 40);
            if (!var1) {
               F(var5);
               V(var5);
               h.f(var5, "rejectionReason", 500);
               u(var5);
            }

            var2.add(var5.deepCopy());
         }

         return List.copyOf(var2);
      } else {
         throw u6.V();
      }
   }

   public static t4.e g(JsonObject var0) {
      try {
         String var1 = h.f(var0, "version", 40);
         if (!var1.matches("[A-Za-z0-9][A-Za-z0-9.+_-]*")) {
            throw u6.V();
         }

         if (!var0.has("updateAvailable")) {
            throw u6.V();
         }

         Boolean var2 = var0.get("updateAvailable").isJsonNull() ? null : h.o(var0.get("updateAvailable"));
         if (Boolean.TRUE.equals(var2) && var1.matches("\\d+(?:\\.\\d+){0,3}") && sc.v.matches("\\d+(?:\\.\\d+){0,3}")) {
            String[] var3 = var1.split("\\.");
            String[] var4 = sc.v.split("\\.");
            int var5 = 0;

            for (int var6 = 0; var6 < Math.max(var3.length, var4.length) && var5 == 0; var6++) {
               var5 = new BigInteger(var6 < var3.length ? var3[var6] : "0").compareTo(new BigInteger(var6 < var4.length ? var4[var6] : "0"));
            }

            if (var5 <= 0) {
               var2 = false;
            }
         }

         ArrayList var8 = new ArrayList();
         var8.add("Larp " + var1 + " — " + h.f(var0, "title", 120));
         var8.add(
            h.f(var0, "releaseDate", 10)
               + " / Minecraft "
               + h.f(var0, "minecraftVersion", 40)
               + " / "
               + h.v(var0.get("status"), "stable", "beta", "experimental")
         );
         var8.add(h.f(var0, "summary", 500));
         if (var0.has("notice") && !var0.get("notice").isJsonNull()) {
            var8.add(h.f(var0, "notice", 1000));
         }

         if (var0.has("notes") && var0.get("notes").isJsonArray() && var0.getAsJsonArray("notes").size() <= 100) {
            for (JsonElement var10 : var0.getAsJsonArray("notes")) {
               JsonObject var11 = h.Z(var10);
               var8.add(h.v(var11.get("category"), "Added", "Changed", "Fixed", "Visuals", "Performance", "Notes") + ": " + h.f(var11, "text", 2000));
            }

            return new t4.e(var1, var2, List.copyOf(var8));
         } else {
            throw u6.V();
         }
      } catch (RuntimeException var7) {
         throw u6.V();
      }
   }

   public static t4.u L(JsonObject var0) {
      try {
         String var1 = h.v(var0.get("state"), "Operational", "Degraded", "Unavailable");
         if (var0.has("services") && var0.get("services").isJsonArray() && var0.getAsJsonArray("services").size() <= 20) {
            byte var2 = 0;
            ArrayList var3 = new ArrayList();

            for (JsonElement var5 : var0.getAsJsonArray("services")) {
               JsonObject var6 = h.Z(var5);
               String var7 = a(var6, "id");
               String var8 = h.v(var6.get("state"), "Operational", "Degraded", "Unavailable");
               if (Set.of("authentication", "client-session").contains(var7) && !var8.equals("Operational")) {
                  var2 = 1;
               }

               var3.add(h.f(var6, "name", 120) + ": " + var8);
            }

            return new t4.u(var1, (boolean)var2, List.copyOf(var3));
         } else {
            throw u6.V();
         }
      } catch (RuntimeException var9) {
         throw u6.V();
      }
   }

   public record e(String version, Boolean updateAvailable, List<String> lines) {
   }

   public record u(String state, boolean authIssue, List<String> lines) {
   }
}
