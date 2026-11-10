package com.larpclient.account;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.util.UndashedUuid;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public final class TokenLogin {
   static final URI PROFILE = URI.create("https://api.minecraftservices.com/minecraft/profile");
   static final String KIND = "larpToken";
   public static final int MAX_LENGTH = 8192;

   private TokenLogin() {
   }

   public static String normalize(String var0) {
      String var1 = var0 == null ? "" : var0.strip();
      if (var1.regionMatches(true, 0, "Authorization:", 0, 14)) {
         var1 = var1.substring(14).strip();
      }

      if (var1.regionMatches(true, 0, "Bearer ", 0, 7)) {
         var1 = var1.substring(7).strip();
      }

      if (var1.length() >= 2 && var1.startsWith("\"") && var1.endsWith("\"")) {
         var1 = var1.substring(1, var1.length() - 1).strip();
      }

      if (var1.isEmpty()) {
         throw new IllegalArgumentException("Paste a Minecraft access token first.");
      } else if (var1.length() >= 32 && var1.length() <= 8192 && var1.matches("[A-Za-z0-9._~+/=-]+")) {
         return var1;
      } else {
         throw new IllegalArgumentException("That does not look like a Minecraft access token.");
      }
   }

   public static Instant expiry(String var0) {
      String[] var1 = var0.split("\\.", -1);
      if (var1.length != 3) {
         return null;
      }

      try {
         JsonObject var2 = JsonParser.parseString(new String(Base64.getUrlDecoder().decode(var1[1]), StandardCharsets.UTF_8)).getAsJsonObject();
         return var2.has("exp") && var2.get("exp").isJsonPrimitive() ? Instant.ofEpochSecond(var2.get("exp").getAsLong()) : null;
      } catch (RuntimeException var3) {
         return null;
      }
   }

   static JsonObject state(String var0, Instant var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("accessToken", var0);
      if (var1 != null) {
         var2.addProperty("expiresAt", var1.getEpochSecond());
      }

      JsonObject var3 = new JsonObject();
      var3.add("larpToken", var2);
      return var3;
   }

   public static boolean saved(JsonObject var0) {
      return var0.has("larpToken") && var0.get("larpToken").isJsonObject();
   }

   static String token(JsonObject var0) {
      return var0.getAsJsonObject("larpToken").get("accessToken").getAsString();
   }

   public static Instant expiresAt(JsonObject var0) {
      if (!saved(var0)) {
         return null;
      }

      JsonObject var1 = var0.getAsJsonObject("larpToken");
      return var1.has("expiresAt") ? Instant.ofEpochSecond(var1.get("expiresAt").getAsLong()) : null;
   }

   static TokenLogin.Profile profile(String var0, Proxy var1) throws IOException {
      HttpURLConnection var2 = (HttpURLConnection)PROFILE.toURL().openConnection(var1);
      var2.setConnectTimeout(10000);
      var2.setReadTimeout(15000);
      var2.setInstanceFollowRedirects(false);
      var2.setUseCaches(false);
      var2.setRequestProperty("Authorization", "Bearer " + var0);
      var2.setRequestProperty("Accept", "application/json");

      try {
         int var3 = var2.getResponseCode();
         if (var3 == 401 || var3 == 403) {
            throw new AccountVault.Failure("Minecraft rejected this token. It may have expired; get a new one and try again.");
         }

         if (var3 == 404) {
            throw new AccountVault.Failure("This token's account does not own Minecraft: Java Edition or has no profile yet.");
         }

         if (var3 == 429) {
            throw new AccountVault.Failure("Minecraft is limiting token checks. Wait a minute and retry.");
         }

         if (var3 != 200) {
            throw new AccountVault.Failure("Minecraft could not check this token right now (HTTP " + var3 + "). Retry later.");
         }

         byte[] var4;
         try (InputStream var5 = var2.getInputStream()) {
            var4 = var5.readNBytes(262144);
         }

         JsonObject var20 = JsonParser.parseString(new String(var4, StandardCharsets.UTF_8)).getAsJsonObject();
         UUID var6 = UndashedUuid.fromStringLenient(var20.get("id").getAsString());
         String var7 = var20.get("name").getAsString();
         if (!var7.matches("[A-Za-z0-9_]{1,16}")) {
            throw new AccountVault.Failure("Minecraft returned an unexpected account name for this token.");
         } else {
            return new TokenLogin.Profile(var6, var7);
         }
      } catch (AccountVault.Failure var17) {
         throw var17;
      } catch (IOException | RuntimeException var18) {
         throw new AccountVault.Failure("Could not reach Minecraft to check the token. Check your connection and retry.");
      } finally {
         var2.disconnect();
      }
   }

   public record Profile(UUID id, String name) {
   }
}
