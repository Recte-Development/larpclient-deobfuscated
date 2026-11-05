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

public final class l7 {
   private static final l7 W = new l7();
   private static final String b = "https://api.donutsmp.net/v1/auction/list/";
   private static final long f = 10000L;
   private static final long G = 600L;
   private static final Pattern o = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
   private static final Pattern t = Pattern.compile("(?<![A-Za-z0-9])[A-Za-z0-9]{16,}(?![A-Za-z0-9])");
   private final HttpClient U = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private final Map<String, l7.b> v = new HashMap<>();
   private final ArrayDeque<l7.q> c = new ArrayDeque<>();
   private long a;
   private boolean i;
   private static final String s = "https://api.donutsmp.net/v1/lookup/";
   private static final long K = 600L;
   private static final long Q = 1800000L;
   private static final long g = 300000L;
   private static final long A = 60000L;
   private static final Pattern H = Pattern.compile("\\.?[A-Za-z0-9_]{1,16}");
   private final Map<String, l7.a> z = new HashMap<>();
   private final ArrayDeque<String> l = new ArrayDeque<>();
   private long d;
   private long n;
   private String e = "";
   private int p;

   private l7() {
   }

   public static l7 q() {
      return W;
   }

   public void e(Component var1) {
      if (var1 != null) {
         String var2 = var1.getString();
         if (var2 != null && !var2.isBlank()) {
            String var3 = var2.toLowerCase(Locale.ROOT);
            if (var3.contains("api") && (var3.contains("key") || var3.contains("token"))) {
               String var4 = g(var2);
               if (var4 != null && !var4.equals(FakeSpawnerModule.w().v())) {
                  FakeSpawnerModule var5 = FakeSpawnerModule.w();
                  var5.z(var4);
                  var5.o();
                  n("DonutSMP API token captured", ChatFormatting.GREEN);
               }
            }
         }
      }
   }

   public void n() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = var1 != null && var1.player != null && var1.level != null ? 1 : 0;
      this.i = (boolean)var2;
      if (var2 != 0) {
         if (this.b()) {
            long var3 = System.currentTimeMillis();
            if (var3 - this.a >= 600L && !this.c.isEmpty()) {
               l7.q var5 = this.c.poll();
               this.a = var3;
               this.a(var1, var5, 0);
            }

            if (var3 - this.d >= 600L && var3 >= this.n && this.p < 2 && !this.l.isEmpty() && !this.U()) {
               this.d = var3;
               this.b(var1, this.l.poll());
            }
         }
      }
   }

   public boolean b() {
      FakeSpawnerModule var1 = FakeSpawnerModule.w();
      return var1 != null && !var1.v().isBlank();
   }

   public boolean U() {
      FakeSpawnerModule var1 = FakeSpawnerModule.w();
      return var1 != null && !this.e.isEmpty() && this.e.equals(var1.v());
   }

   public String h(String var1) {
      if (var1 != null && H.matcher(var1).matches()) {
         String var2 = var1.toLowerCase(Locale.ROOT);
         l7.a var3 = this.z.get(var2);
         long var4 = System.currentTimeMillis();
         if (var3 != null && var3.fresh(var4)) {
            return var3.rank();
         }

         if (this.b() && !this.U() && !this.l.contains(var2)) {
            this.l.add(var2);
         }

         return var3 == null ? null : var3.rank();
      } else {
         return "";
      }
   }

   private void b(Minecraft var1, String var2) {
      String var3 = FakeSpawnerModule.w().v();
      HttpRequest var4 = HttpRequest.newBuilder(URI.create("https://api.donutsmp.net/v1/lookup/" + var2))
         .timeout(Duration.ofSeconds(15L))
         .header("Authorization", "Bearer " + var3)
         .header("Accept", "application/json")
         .GET()
         .build();
      this.p++;
      this.U.sendAsync(var4, BodyHandlers.ofString()).whenComplete((var4x, var5) -> var1.execute(() -> {
         this.p--;
         long var5x = System.currentTimeMillis();
         if (var5 != null) {
            r4.g("StaffList", "DonutSMP rank lookup for {} failed: {}", var2, var5.toString());
         } else {
            int var7 = var4x.statusCode();
            if (var7 != 401 && var7 != 403) {
               if (var7 == 429) {
                  this.n = var5x + 60000L;
                  this.l.addFirst(var2);
                  r4.V("StaffList", "DonutSMP API rate limit reached; rank lookups pause for 60 seconds");
               } else {
                  String var8 = var7 == 200 ? V((String)var4x.body()) : null;
                  if (var7 != 200) {
                     r4.g("StaffList", "DonutSMP rank lookup for {} returned HTTP {}", var2, var7);
                  }

                  this.z.put(var2, new l7.a(var8 == null ? "" : var8, var7 == 200, var5x));
                  if (this.z.size() > 4096) {
                     this.z.values().removeIf(var2xxx -> !var2xxx.fresh(var5x));
                  }
               }
            } else {
               if (!var3.equals(this.e)) {
                  r4.m("StaffList", "DonutSMP API refused the saved key (HTTP {}); run /api again for a new one", var7);
               }

               this.e = var3;
               this.l.clear();
            }
         }
      }));
   }

   public static String V(String var0) {
      try {
         JsonElement var1 = JsonParser.parseString(var0);
         if (!var1.isJsonObject()) {
            return null;
         }

         JsonElement var2 = var1.getAsJsonObject().get("result");
         return var2 != null && var2.isJsonObject() ? n(var2.getAsJsonObject(), "rank") : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public void m(String var1, Consumer<Long> var2) {
      String var3 = y(var1);
      if (!var3.isEmpty()) {
         l7.b var4 = this.v.get(var3);
         if (var4 != null && System.currentTimeMillis() - var4.at() < 10000L) {
            var2.accept(var4.price());
         } else {
            for (l7.q var6 : this.c) {
               if (var6.term().equals(var3)) {
                  return;
               }
            }

            if (!FakeSpawnerModule.w().v().isBlank()) {
               this.u(new l7.q(var3, var2));
            }
         }
      }
   }

   public long x(String var1) {
      l7.b var2 = this.v.get(y(var1));
      return var2 == null ? 0L : var2.price();
   }

   private void u(l7.q var1) {
      this.c.add(var1);
   }

   private void a(Minecraft var1, l7.q var2, int var3) {
      String var4 = FakeSpawnerModule.w().v();
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

         this.U.sendAsync(var5.build(), BodyHandlers.ofString()).whenComplete((var4x, var5x) -> var1.execute(() -> {
            if (var5x == null && var4x.statusCode() != 401) {
               if (var4x.statusCode() != 200) {
                  this.a(var1, var2, var3 + 1);
               } else {
                  long var6 = U((String)var4x.body(), var2.term());
                  this.v.put(var2.term(), new l7.b(var6, System.currentTimeMillis()));
                  var2.onResult().accept(var6);
               }
            } else if (var4x != null && var4x.statusCode() == 401) {
               this.v.put(var2.term(), new l7.b(0L, System.currentTimeMillis()));
               var2.onResult().accept(0L);
            } else {
               this.a(var1, var2, var3 + 1);
            }
         }));
      } else {
         this.v.put(var2.term(), new l7.b(0L, System.currentTimeMillis()));
         var2.onResult().accept(0L);
      }
   }

   private static long U(String var0, String var1) {
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
                           if (q(var14, var1)) {
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

   private static boolean q(JsonObject var0, String var1) {
      String var2 = I(var1);
      String var3 = n(var0, "id");
      if (var3 != null) {
         return I(var3).equals(var2);
      }

      String var4 = n(var0, "display_name");
      return var4 != null && I(var4).equals(var2);
   }

   private static String I(String var0) {
      String var1 = y(var0).replace(' ', '_').replace('-', '_');
      int var2 = var1.indexOf(58);
      return var2 >= 0 ? var1.substring(var2 + 1) : var1;
   }

   private static String y(String var0) {
      return var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT);
   }

   private static String n(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && !var2.isJsonNull()) {
         String var3 = var2.getAsString();
         return var3 != null && !var3.isBlank() ? var3 : null;
      } else {
         return null;
      }
   }

   private static String g(String var0) {
      Matcher var1 = o.matcher(var0);
      if (var1.find()) {
         return var1.group();
      }

      Matcher var2 = t.matcher(var0);
      String var3 = null;

      while (var2.find()) {
         if (var3 == null || var2.group().length() > var3.length()) {
            var3 = var2.group();
         }
      }

      return var3;
   }

   private static void n(String var0, ChatFormatting var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         var2.execute(() -> {
            if (var2.player != null) {
               jb.v(Component.literal("[Fake Elytra] " + var0).withStyle(var1), false);
            }
         });
      }
   }

   private record a(String rank, boolean found, long at) {
      boolean fresh(long var1) {
         return var1 - this.at < (this.found ? 1800000L : 300000L);
      }
   }

   private record b(long price, long at) {
   }

   private record q(String term, Consumer<Long> onResult) {
   }
}
