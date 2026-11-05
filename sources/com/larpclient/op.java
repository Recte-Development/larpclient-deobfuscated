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

public final class op {
   private static final op g = new op();
   private static final String a = "https://api.donutsmp.net/v1/auction/list/";
   private static final long t = 10000L;
   private static final long o = 600L;
   private static final Pattern e = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
   private static final Pattern Q = Pattern.compile("(?<![A-Za-z0-9])[A-Za-z0-9]{16,}(?![A-Za-z0-9])");
   private final HttpClient B = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private final Map<String, op.y> n = new HashMap<>();
   private final ArrayDeque<op.r> H = new ArrayDeque<>();
   private long D;
   private boolean N;
   private static final String u = "https://api.donutsmp.net/v1/lookup/";
   private static final long c = 600L;
   private static final long U = 1800000L;
   private static final long q = 300000L;
   private static final long k = 60000L;
   private static final Pattern m = Pattern.compile("\\.?[A-Za-z0-9_]{1,16}");
   private final Map<String, op.a> G = new HashMap<>();
   private final ArrayDeque<String> Y = new ArrayDeque<>();
   private long R;
   private long K;
   private String W = "";
   private int F;

   private op() {
   }

   public static op a() {
      return g;
   }

   public void Z(Component var1) {
      if (var1 != null) {
         String var2 = var1.getString();
         if (var2 != null && !var2.isBlank()) {
            String var3 = var2.toLowerCase(Locale.ROOT);
            if (var3.contains("api") && (var3.contains("key") || var3.contains("token"))) {
               String var4 = o(var2);
               if (var4 != null && !var4.equals(FakeSpawnerModule.T().j())) {
                  FakeSpawnerModule var5 = FakeSpawnerModule.T();
                  var5.H(var4);
                  var5.A();
                  q("DonutSMP API token captured", ChatFormatting.GREEN);
               }
            }
         }
      }
   }

   public void O() {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = var1 != null && var1.player != null && var1.level != null ? 1 : 0;
      this.N = (boolean)var2;
      if (var2 != 0) {
         if (this.J()) {
            long var3 = System.currentTimeMillis();
            if (var3 - this.D >= 600L && !this.H.isEmpty()) {
               op.r var5 = this.H.poll();
               this.D = var3;
               this.g(var1, var5, 0);
            }

            if (var3 - this.R >= 600L && var3 >= this.K && this.F < 2 && !this.Y.isEmpty() && !this.c()) {
               this.R = var3;
               this.C(var1, this.Y.poll());
            }
         }
      }
   }

   public boolean J() {
      FakeSpawnerModule var1 = FakeSpawnerModule.T();
      return var1 != null && !var1.j().isBlank();
   }

   public boolean c() {
      FakeSpawnerModule var1 = FakeSpawnerModule.T();
      return var1 != null && !this.W.isEmpty() && this.W.equals(var1.j());
   }

   public String I(String var1) {
      if (var1 != null && m.matcher(var1).matches()) {
         String var2 = var1.toLowerCase(Locale.ROOT);
         op.a var3 = this.G.get(var2);
         long var4 = System.currentTimeMillis();
         if (var3 != null && var3.fresh(var4)) {
            return var3.rank();
         }

         if (this.J() && !this.c() && !this.Y.contains(var2)) {
            this.Y.add(var2);
         }

         return var3 == null ? null : var3.rank();
      } else {
         return "";
      }
   }

   private void C(Minecraft var1, String var2) {
      String var3 = FakeSpawnerModule.T().j();
      HttpRequest var4 = HttpRequest.newBuilder(URI.create("https://api.donutsmp.net/v1/lookup/" + var2))
         .timeout(Duration.ofSeconds(15L))
         .header("Authorization", "Bearer " + var3)
         .header("Accept", "application/json")
         .GET()
         .build();
      this.F++;
      this.B.sendAsync(var4, BodyHandlers.ofString()).whenComplete((var4x, var5) -> var1.execute(() -> {
         this.F--;
         long var5x = System.currentTimeMillis();
         if (var5 != null) {
            mt.U("StaffList", "DonutSMP rank lookup for {} failed: {}", var2, var5.toString());
         } else {
            int var7 = var4x.statusCode();
            if (var7 != 401 && var7 != 403) {
               if (var7 == 429) {
                  this.K = var5x + 60000L;
                  this.Y.addFirst(var2);
                  mt.K("StaffList", "DonutSMP API rate limit reached; rank lookups pause for 60 seconds");
               } else {
                  String var8 = var7 == 200 ? Q((String)var4x.body()) : null;
                  if (var7 != 200) {
                     mt.U("StaffList", "DonutSMP rank lookup for {} returned HTTP {}", var2, var7);
                  }

                  this.G.put(var2, new op.a(var8 == null ? "" : var8, var7 == 200, var5x));
                  if (this.G.size() > 4096) {
                     this.G.values().removeIf(var2xxx -> !var2xxx.fresh(var5x));
                  }
               }
            } else {
               if (!var3.equals(this.W)) {
                  mt.T("StaffList", "DonutSMP API refused the saved key (HTTP {}); run /api again for a new one", var7);
               }

               this.W = var3;
               this.Y.clear();
            }
         }
      }));
   }

   public static String Q(String var0) {
      try {
         JsonElement var1 = JsonParser.parseString(var0);
         if (!var1.isJsonObject()) {
            return null;
         }

         JsonElement var2 = var1.getAsJsonObject().get("result");
         return var2 != null && var2.isJsonObject() ? v(var2.getAsJsonObject(), "rank") : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public void l(String var1, Consumer<Long> var2) {
      String var3 = K(var1);
      if (!var3.isEmpty()) {
         op.y var4 = this.n.get(var3);
         if (var4 != null && System.currentTimeMillis() - var4.at() < 10000L) {
            var2.accept(var4.price());
         } else {
            for (op.r var6 : this.H) {
               if (var6.term().equals(var3)) {
                  return;
               }
            }

            if (!FakeSpawnerModule.T().j().isBlank()) {
               this.l(new op.r(var3, var2));
            }
         }
      }
   }

   public long X(String var1) {
      op.y var2 = this.n.get(K(var1));
      return var2 == null ? 0L : var2.price();
   }

   private void l(op.r var1) {
      this.H.add(var1);
   }

   private void g(Minecraft var1, op.r var2, int var3) {
      String var4 = FakeSpawnerModule.T().j();
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

         this.B.sendAsync(var5.build(), BodyHandlers.ofString()).whenComplete((var4x, var5x) -> var1.execute(() -> {
            if (var5x == null && var4x.statusCode() != 401) {
               if (var4x.statusCode() != 200) {
                  this.g(var1, var2, var3 + 1);
               } else {
                  long var6 = h((String)var4x.body(), var2.term());
                  this.n.put(var2.term(), new op.y(var6, System.currentTimeMillis()));
                  var2.onResult().accept(var6);
               }
            } else if (var4x != null && var4x.statusCode() == 401) {
               this.n.put(var2.term(), new op.y(0L, System.currentTimeMillis()));
               var2.onResult().accept(0L);
            } else {
               this.g(var1, var2, var3 + 1);
            }
         }));
      } else {
         this.n.put(var2.term(), new op.y(0L, System.currentTimeMillis()));
         var2.onResult().accept(0L);
      }
   }

   private static long h(String var0, String var1) {
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
                           if (w(var14, var1)) {
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

   private static boolean w(JsonObject var0, String var1) {
      String var2 = G(var1);
      String var3 = v(var0, "id");
      if (var3 != null) {
         return G(var3).equals(var2);
      }

      String var4 = v(var0, "display_name");
      return var4 != null && G(var4).equals(var2);
   }

   private static String G(String var0) {
      String var1 = K(var0).replace(' ', '_').replace('-', '_');
      int var2 = var1.indexOf(58);
      return var2 >= 0 ? var1.substring(var2 + 1) : var1;
   }

   private static String K(String var0) {
      return var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT);
   }

   private static String v(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && !var2.isJsonNull()) {
         String var3 = var2.getAsString();
         return var3 != null && !var3.isBlank() ? var3 : null;
      } else {
         return null;
      }
   }

   private static String o(String var0) {
      Matcher var1 = e.matcher(var0);
      if (var1.find()) {
         return var1.group();
      }

      Matcher var2 = Q.matcher(var0);
      String var3 = null;

      while (var2.find()) {
         if (var3 == null || var2.group().length() > var3.length()) {
            var3 = var2.group();
         }
      }

      return var3;
   }

   private static void q(String var0, ChatFormatting var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         var2.execute(() -> {
            if (var2.player != null) {
               ws.c(Component.literal("[Fake Elytra] " + var0).withStyle(var1), false);
            }
         });
      }
   }

   private record a(String rank, boolean found, long at) {
      boolean fresh(long var1) {
         return var1 - this.at < (this.found ? 1800000L : 300000L);
      }
   }

   private record r(String term, Consumer<Long> onResult) {
   }

   private record y(long price, long at) {
   }
}
