package com.larpclient.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.social.misc.PlayerMessages;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class AuctionPriceClient {
   private static final AuctionPriceClient instance = new AuctionPriceClient();
   private static final String apiUrl = "https://api.donutsmp.net/v1/auction/list/";
   private static final long cacheTtlMs = 10000L;
   private static final long requestIntervalMs = 600L;
   private static final Pattern uuidPattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
   private static final Pattern tokenPattern = Pattern.compile("(?<![A-Za-z0-9])[A-Za-z0-9]{16,}(?![A-Za-z0-9])");
   private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private final Map<String, AuctionPriceClient.PriceEntry> priceCache = new HashMap<>();
   private final ArrayDeque<AuctionPriceClient.PriceRequest> requestQueue = new ArrayDeque<>();
   private long lastRequestTime;
   private boolean inWorld;

   private AuctionPriceClient() {
   }

   public static AuctionPriceClient getInstance() {
      return instance;
   }

   public void captureApiToken(Component var1) {
      if (var1 != null) {
         String var2 = var1.getString();
         if (var2 != null && !var2.isBlank()) {
            String var3 = var2.toLowerCase(Locale.ROOT);
            if (var3.contains("api") && (var3.contains("key") || var3.contains("token"))) {
               String var4 = extractToken(var2);
               if (var4 != null && !var4.equals(FakeSpawnerModule.getInstance().getPreviewCount())) {
                  FakeSpawnerModule var5 = FakeSpawnerModule.getInstance();
                  var5.setApiKey(var4);
                  var5.savePreferences();
                  captureApiToken("DonutSMP API token captured", ChatFormatting.GREEN);
               }
            }
         }
      }
   }

   public void tick() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = var1 != null && var1.player != null && var1.level != null ? 1 : 0;
      this.inWorld = (boolean)var2;
      if (var2 != 0) {
         if (this.requestPrice()) {
            long var3 = System.currentTimeMillis();
            if (var3 - this.lastRequestTime >= 600L && !this.requestQueue.isEmpty()) {
               AuctionPriceClient.PriceRequest var5 = this.requestQueue.poll();
               this.lastRequestTime = var3;
               this.sendRequest(var1, var5, 0);
            }
         }
      }
   }

   public boolean requestPrice() {
      FakeSpawnerModule var1 = FakeSpawnerModule.getInstance();
      return var1 != null && !var1.getPreviewCount().isBlank();
   }

   public void requestPrice(String var1, Consumer<Long> var2) {
      String var3 = normalizeTerm(var1);
      if (!var3.isEmpty()) {
         AuctionPriceClient.PriceEntry var4 = this.priceCache.get(var3);
         if (var4 != null && System.currentTimeMillis() - var4.timestamp() < 10000L) {
            var2.accept(var4.price());
         } else {
            for (AuctionPriceClient.PriceRequest var6 : this.requestQueue) {
               if (var6.term().equals(var3)) {
                  return;
               }
            }

            if (!FakeSpawnerModule.getInstance().getPreviewCount().isBlank()) {
               this.enqueue(new AuctionPriceClient.PriceRequest(var3, var2));
            }
         }
      }
   }

   public long tick(String var1) {
      AuctionPriceClient.PriceEntry var2 = this.priceCache.get(normalizeTerm(var1));
      return var2 == null ? 0L : var2.price();
   }

   private void enqueue(AuctionPriceClient.PriceRequest var1) {
      this.requestQueue.add(var1);
   }

   private void sendRequest(Minecraft var1, AuctionPriceClient.PriceRequest var2, int var3) {
      String var4 = FakeSpawnerModule.getInstance().getPreviewCount();
      if (var4 != null && !var4.isBlank() && var3 <= 1) {
         Builder var5 = HttpRequest.newBuilder(URI.create("https://api.donutsmp.net/v1/auction/list/1"))
            .timeout(Duration.ofSeconds(15L))
            .header("Authorization", "Bearer " + var4)
            .header("Accept", "application/json");
         if (var3 == 0) {
            var5.header("Content-Type", "application/json")
               .method("GET", BodyPublishers.ofString("{\"search\":\"" + var2.term() + "\",\"sort\":\"lowest_price\"}"));
         } else {
            var5.GET();
         }

         this.httpClient.sendAsync(var5.build(), BodyHandlers.ofString()).whenComplete((var4x, var5x) -> var1.execute(() -> {
            if (var5x == null && var4x.statusCode() != 401) {
               if (var4x.statusCode() != 200) {
                  this.sendRequest(var1, var2, var3 + 1);
               } else {
                  long var6 = getInstance((String)var4x.body(), var2.term());
                  this.priceCache.put(var2.term(), new AuctionPriceClient.PriceEntry(var6, System.currentTimeMillis()));
                  var2.onResult().accept(var6);
               }
            } else if (var4x != null && var4x.statusCode() == 401) {
               this.priceCache.put(var2.term(), new AuctionPriceClient.PriceEntry(0L, System.currentTimeMillis()));
               var2.onResult().accept(0L);
            } else {
               this.sendRequest(var1, var2, var3 + 1);
            }
         }));
      } else {
         this.priceCache.put(var2.term(), new AuctionPriceClient.PriceEntry(0L, System.currentTimeMillis()));
         var2.onResult().accept(0L);
      }
   }

   private static long getInstance(String var0, String var1) {
      try {
         JsonElement var2 = JsonParser.parseString(var0);
         if (!var2.isJsonObject()) {
            return 0L;
         }

         JsonObject var3 = var2.getAsJsonObject();
         JsonElement var4 = var3.get("result");
         if (var4 != null && var4.isJsonArray()) {
            long var5 = Long.MAX_VALUE;

            for (JsonElement var8 : var4.getAsJsonArray()) {
               if (var8.isJsonObject()) {
                  JsonObject var9 = var8.getAsJsonObject();
                  JsonElement var10 = var9.get("price");
                  if (var10 != null && !var10.isJsonNull()) {
                     double var11;
                     try {
                        var11 = var10.getAsDouble();
                     } catch (NumberFormatException var18) {
                        continue;
                     }

                     if (!(var11 <= 0.0)) {
                        JsonElement var13 = var9.get("item");
                        if (var13 != null && var13.isJsonObject()) {
                           JsonObject var14 = var13.getAsJsonObject();
                           if (matchesItem(var14, var1)) {
                              long var15 = 1L;
                              JsonElement var17 = var14.get("count");
                              if (var17 != null && !var17.isJsonNull()) {
                                 var15 = Math.max(1L, var17.getAsLong());
                              }

                              var5 = Math.min(var5, Math.max(1L, Math.round(var11 / var15)));
                           }
                        }
                     }
                  }
               }
            }

            return var5 == Long.MAX_VALUE ? 0L : var5;
         } else {
            return 0L;
         }
      } catch (Exception var19) {
         return 0L;
      }
   }

   private static boolean matchesItem(JsonObject var0, String var1) {
      String var2 = normalizeItemId(var1);
      String var3 = getString(var0, "id");
      if (var3 != null) {
         return normalizeItemId(var3).equals(var2);
      }

      String var4 = getString(var0, "display_name");
      return var4 != null && normalizeItemId(var4).equals(var2);
   }

   private static String normalizeItemId(String var0) {
      String var1 = normalizeTerm(var0).replace(' ', '_').replace('-', '_');
      int var2 = var1.indexOf(58);
      return var2 >= 0 ? var1.substring(var2 + 1) : var1;
   }

   private static String normalizeTerm(String var0) {
      return var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT);
   }

   private static String getString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && !var2.isJsonNull()) {
         String var3 = var2.getAsString();
         return var3 != null && !var3.isBlank() ? var3 : null;
      } else {
         return null;
      }
   }

   private static String extractToken(String var0) {
      Matcher var1 = uuidPattern.matcher(var0);
      if (var1.find()) {
         return var1.group();
      }

      Matcher var2 = tokenPattern.matcher(var0);
      String var3 = null;

      while (var2.find()) {
         if (var3 == null || var2.group().length() > var3.length()) {
            var3 = var2.group();
         }
      }

      return var3;
   }

   private static void captureApiToken(String var0, ChatFormatting var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         var2.execute(() -> {
            if (var2.player != null) {
               PlayerMessages.sendMessage(Component.literal("[Fake Elytra] " + var0).withStyle(var1), false);
            }
         });
      }
   }

   private record PriceEntry(long price, long at) {
      private final long timestamp;

      private PriceEntry(long price, long at) {
         this.price = price;
         this.timestamp = at;
      }
   }

   private record PriceRequest(String term, Consumer<Long> onResult) {
   }
}
