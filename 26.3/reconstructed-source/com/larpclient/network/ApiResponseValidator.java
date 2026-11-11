package com.larpclient.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.config.ProfileValidator;
import com.larpclient.core.BuildInfo;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class ApiResponseValidator {
   public static String readId(JsonObject var0, String var1) {
      String var2 = ProfileValidator.readString(var0, var1, 128);
      if (!var2.matches("[A-Za-z0-9_-]{1,128}")) {
         throw ApiException.invalidResponse();
      } else {
         return var2;
      }
   }

   public static String normalizeShareCode(String var0) {
      var0 = var0.trim().toUpperCase(Locale.ROOT);
      if (!var0.matches("(?:LARP-)?[A-F0-9]{16}")) {
         throw new IllegalArgumentException("Enter a LARP share code with 16 hexadecimal characters.");
      } else {
         return var0.startsWith("LARP-") ? var0 : "LARP-" + var0;
      }
   }

   public static String readShareCode(JsonObject var0) {
      String var1 = ProfileValidator.readString(var0, "shareCode", 21);
      if (!var1.matches("LARP-[A-F0-9]{16}")) {
         throw ApiException.invalidResponse();
      } else {
         return var1;
      }
   }

   public static String readConfigId(JsonObject var0) {
      String var1 = ProfileValidator.readString(var0, "id", 16);
      if (!var1.matches("LRP-[A-F0-9]{12}")) {
         throw ApiException.invalidResponse();
      } else {
         return var1;
      }
   }

   public static String marketplaceStateLabel(JsonObject var0) {
      return switch (ProfileValidator.normalize(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed")) {
         case "private" -> "Private (unlisted)";
         case "submitted" -> "Pending review";
         case "approved" -> "Approved";
         case "rejected" -> "Rejected";
         default -> "Removed";
      };
   }

   public static boolean isDiscordShared(JsonObject var0) {
      return var0.has("discordSharing") && ProfileValidator.readBoolean(var0.get("discordSharing"));
   }

   public static String discordStatusLabel(JsonObject var0) {
      if (!isDiscordShared(var0)) {
         return "";
      }

      String var1 = ProfileValidator.normalize(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed");
      return var1.equals("removed") ? "Taken off Discord by staff" : "Posted in Discord";
   }

   public static List<JsonObject> validateItems(JsonObject var0, boolean var1) {
      if (var0.has("items") && var0.get("items").isJsonArray() && var0.getAsJsonArray("items").size() <= 100) {
         ArrayList var2 = new ArrayList();

         for (JsonElement var4 : var0.getAsJsonArray("items")) {
            JsonObject var5 = ProfileValidator.asObject(var4);
            readId(var5, "id");
            readId(var5, "revision");
            ProfileValidator.validateName(ProfileValidator.readString(var5, "name", 32));
            ProfileValidator.readString(var5, "updatedAt", 40);
            if (!var1) {
               readShareCode(var5);
               marketplaceStateLabel(var5);
               ProfileValidator.readString(var5, "rejectionReason", 500);
               discordStatusLabel(var5);
            }

            var2.add(var5.deepCopy());
         }

         return List.copyOf(var2);
      } else {
         throw ApiException.invalidResponse();
      }
   }

   public static ApiResponseValidator.UpdateInfo parseUpdateInfo(JsonObject var0) {
      try {
         String var1 = ProfileValidator.readString(var0, "version", 40);
         if (!var1.matches("[A-Za-z0-9][A-Za-z0-9.+_-]*")) {
            throw ApiException.invalidResponse();
         }

         if (!var0.has("updateAvailable")) {
            throw ApiException.invalidResponse();
         }

         Boolean var2 = var0.get("updateAvailable").isJsonNull() ? null : ProfileValidator.readBoolean(var0.get("updateAvailable"));
         if (Boolean.TRUE.equals(var2) && var1.matches("\\d+(?:\\.\\d+){0,3}") && BuildInfo.larpVersion.matches("\\d+(?:\\.\\d+){0,3}")) {
            String[] var3 = var1.split("\\.");
            String[] var4 = BuildInfo.larpVersion.split("\\.");
            int var5 = 0;

            for (int var6 = 0; var6 < Math.max(var3.length, var4.length) && var5 == 0; var6++) {
               var5 = new BigInteger(var6 < var3.length ? var3[var6] : "0").compareTo(new BigInteger(var6 < var4.length ? var4[var6] : "0"));
            }

            if (var5 <= 0) {
               var2 = false;
            }
         }

         ArrayList var8 = new ArrayList();
         var8.add("Larp " + var1 + " — " + ProfileValidator.readString(var0, "title", 120));
         var8.add(
            ProfileValidator.readString(var0, "releaseDate", 10)
               + " / Minecraft "
               + ProfileValidator.readString(var0, "minecraftVersion", 40)
               + " / "
               + ProfileValidator.normalize(var0.get("status"), "stable", "beta", "experimental")
         );
         var8.add(ProfileValidator.readString(var0, "summary", 500));
         if (var0.has("notice") && !var0.get("notice").isJsonNull()) {
            var8.add(ProfileValidator.readString(var0, "notice", 1000));
         }

         if (var0.has("notes") && var0.get("notes").isJsonArray() && var0.getAsJsonArray("notes").size() <= 100) {
            for (JsonElement var10 : var0.getAsJsonArray("notes")) {
               JsonObject var11 = ProfileValidator.asObject(var10);
               var8.add(
                  ProfileValidator.normalize(var11.get("category"), "Added", "Changed", "Fixed", "Visuals", "Performance", "Notes")
                     + ": "
                     + ProfileValidator.readString(var11, "text", 2000)
               );
            }

            return new ApiResponseValidator.UpdateInfo(var1, var2, List.copyOf(var8));
         } else {
            throw ApiException.invalidResponse();
         }
      } catch (RuntimeException var7) {
         throw ApiException.invalidResponse();
      }
   }

   public static ApiResponseValidator.ServiceStatus readId(JsonObject var0) {
      try {
         String var1 = ProfileValidator.normalize(var0.get("state"), "Operational", "Degraded", "Unavailable");
         if (var0.has("services") && var0.get("services").isJsonArray() && var0.getAsJsonArray("services").size() <= 20) {
            byte var2 = 0;
            ArrayList var3 = new ArrayList();

            for (JsonElement var5 : var0.getAsJsonArray("services")) {
               JsonObject var6 = ProfileValidator.asObject(var5);
               String var7 = readId(var6, "id");
               String var8 = ProfileValidator.normalize(var6.get("state"), "Operational", "Degraded", "Unavailable");
               if (Set.of("authentication", "client-session").contains(var7) && !var8.equals("Operational")) {
                  var2 = 1;
               }

               var3.add(ProfileValidator.readString(var6, "name", 120) + ": " + var8);
            }

            return new ApiResponseValidator.ServiceStatus(var1, (boolean)var2, List.copyOf(var3));
         } else {
            throw ApiException.invalidResponse();
         }
      } catch (RuntimeException var9) {
         throw ApiException.invalidResponse();
      }
   }

   public record ServiceStatus(String state, boolean authIssue, List<String> lines) {
   }

   public record UpdateInfo(String version, Boolean updateAvailable, List<String> lines) {
   }
}
