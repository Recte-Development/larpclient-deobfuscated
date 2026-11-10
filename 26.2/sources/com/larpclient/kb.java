package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.FakeSpawnerModule;
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

public final class kb {
   private static final kb j = new kb();
   private static final String X = "https://api.donutsmp.net/v1/auction/list/";
   private static final long g = 10000L;
   private static final long r = 600L;
   private static final Pattern a = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
   private static final Pattern n = Pattern.compile("(?<![A-Za-z0-9])[A-Za-z0-9]{16,}(?![A-Za-z0-9])");
   private final HttpClient L = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private final Map<String, kb.n> Q = new HashMap<>();
   private final ArrayDeque<kb.j> m = new ArrayDeque<>();
   private long Z;
   private boolean d;

   private kb() {
   }

   public static kb h() {
      return j;
   }

   public void r(Component var1) {
      if (var1 != null) {
         String var2 = var1.getString();
         if (var2 != null && !var2.isBlank()) {
            String var3 = var2.toLowerCase(Locale.ROOT);
            if (var3.contains("api") && (var3.contains("key") || var3.contains("token"))) {
               String var4 = z(var2);
               if (var4 != null && !var4.equals(FakeSpawnerModule.M().S())) {
                  FakeSpawnerModule var5 = FakeSpawnerModule.M();
                  var5.F(var4);
                  var5.w();
                  m("DonutSMP API token captured", ChatFormatting.GREEN);
               }
            }
         }
      }
   }

   public void h() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = var1 != null && var1.player != null && var1.level != null ? 1 : 0;
      this.d = (boolean)var2;
      if (var2 != 0) {
         if (this.n()) {
            long var3 = System.currentTimeMillis();
            if (var3 - this.Z >= 600L && !this.m.isEmpty()) {
               kb.j var5 = this.m.poll();
               this.Z = var3;
               this.c(var1, var5, 0);
            }
         }
      }
   }

   public boolean n() {
      FakeSpawnerModule var1 = FakeSpawnerModule.M();
      return var1 != null && !var1.S().isBlank();
   }

   public void Z(String var1, Consumer<Long> var2) {
      String var3 = i(var1);
      if (!var3.isEmpty()) {
         kb.n var4 = this.Q.get(var3);
         if (var4 != null && System.currentTimeMillis() - var4.at() < 10000L) {
            var2.accept(var4.price());
         } else {
            for (kb.j var6 : this.m) {
               if (var6.term().equals(var3)) {
                  return;
               }
            }

            if (!FakeSpawnerModule.M().S().isBlank()) {
               this.f(new kb.j(var3, var2));
            }
         }
      }
   }

   public long P(String var1) {
      kb.n var2 = this.Q.get(i(var1));
      return var2 == null ? 0L : var2.price();
   }

   private void f(kb.j var1) {
      this.m.add(var1);
   }

   private void c(Minecraft var1, kb.j var2, int var3) {
      String var4 = FakeSpawnerModule.M().S();
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

         this.L.sendAsync(var5.build(), BodyHandlers.ofString()).whenComplete((var4x, var5x) -> var1.execute(() -> {
            if (var5x == null && var4x.statusCode() != 401) {
               if (var4x.statusCode() != 200) {
                  this.c(var1, var2, var3 + 1);
               } else {
                  long var6 = W((String)var4x.body(), var2.term());
                  this.Q.put(var2.term(), new kb.n(var6, System.currentTimeMillis()));
                  var2.onResult().accept(var6);
               }
            } else if (var4x != null && var4x.statusCode() == 401) {
               this.Q.put(var2.term(), new kb.n(0L, System.currentTimeMillis()));
               var2.onResult().accept(0L);
            } else {
               this.c(var1, var2, var3 + 1);
            }
         }));
      } else {
         this.Q.put(var2.term(), new kb.n(0L, System.currentTimeMillis()));
         var2.onResult().accept(0L);
      }
   }

   private static long W(String var0, String var1) {
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
                           if (Y(var14, var1)) {
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

   private static boolean Y(JsonObject var0, String var1) {
      String var2 = P(var1);
      String var3 = c(var0, "id");
      if (var3 != null) {
         return P(var3).equals(var2);
      }

      String var4 = c(var0, "display_name");
      return var4 != null && P(var4).equals(var2);
   }

   private static String P(String var0) {
      String var1 = i(var0).replace(' ', '_').replace('-', '_');
      int var2 = var1.indexOf(58);
      return var2 >= 0 ? var1.substring(var2 + 1) : var1;
   }

   private static String i(String var0) {
      return var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT);
   }

   private static String c(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && !var2.isJsonNull()) {
         String var3 = var2.getAsString();
         return var3 != null && !var3.isBlank() ? var3 : null;
      } else {
         return null;
      }
   }

   private static String z(String var0) {
      Matcher var1 = a.matcher(var0);
      if (var1.find()) {
         return var1.group();
      }

      Matcher var2 = n.matcher(var0);
      String var3 = null;

      while (var2.find()) {
         if (var3 == null || var2.group().length() > var3.length()) {
            var3 = var2.group();
         }
      }

      return var3;
   }

   private static void m(String var0, ChatFormatting var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         var2.execute(() -> {
            if (var2.player != null) {
               k4.f(Component.literal("[Fake Elytra] " + var0).withStyle(var1), false);
            }
         });
      }
   }

   private record j(String term, Consumer<Long> onResult) {
   }

   private record n(long price, long at) {
   }
}
