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
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;

public final class s3 {
   private static final s3 j = new s3();
   private static final String K = "https://api.donutsmp.net/v1/auction/list/";
   private static final long l = 10000L;
   private static final long U = 600L;
   private static final Pattern B = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
   private static final Pattern T = Pattern.compile("(?<![A-Za-z0-9])[A-Za-z0-9]{16,}(?![A-Za-z0-9])");
   private final HttpClient S = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private final Map<String, s3.c> d = new HashMap<>();
   private final ArrayDeque<s3.d> y = new ArrayDeque<>();
   private long f;
   private boolean A;

   private s3() {
   }

   public static s3 T() {
      return j;
   }

   public void W(class_2561 var1) {
      if (var1 != null) {
         String var2 = var1.getString();
         if (var2 != null && !var2.isBlank()) {
            String var3 = var2.toLowerCase(Locale.ROOT);
            if (var3.contains("api") && (var3.contains("key") || var3.contains("token"))) {
               String var4 = V(var2);
               if (var4 != null && !var4.equals(FakeSpawnerModule.i().w())) {
                  FakeSpawnerModule var5 = FakeSpawnerModule.i();
                  var5.e(var4);
                  var5.b();
                  t("DonutSMP API token captured", class_124.field_1060);
               }
            }
         }
      }
   }

   public void L() {
      class_310 var1 = class_310.method_1551();
      int var2 = var1 != null && var1.field_1724 != null && var1.field_1687 != null ? 1 : 0;
      this.A = (boolean)var2;
      if (var2 != 0) {
         if (this.C()) {
            long var3 = System.currentTimeMillis();
            if (var3 - this.f >= 600L && !this.y.isEmpty()) {
               s3.d var5 = this.y.poll();
               this.f = var3;
               this.G(var1, var5, 0);
            }
         }
      }
   }

   public boolean C() {
      FakeSpawnerModule var1 = FakeSpawnerModule.i();
      return var1 != null && !var1.w().isBlank();
   }

   public void w(String var1, Consumer<Long> var2) {
      String var3 = I(var1);
      if (!var3.isEmpty()) {
         s3.c var4 = this.d.get(var3);
         if (var4 != null && System.currentTimeMillis() - var4.at() < 10000L) {
            var2.accept(var4.price());
         } else {
            for (s3.d var6 : this.y) {
               if (var6.term().equals(var3)) {
                  return;
               }
            }

            if (!FakeSpawnerModule.i().w().isBlank()) {
               this.X(new s3.d(var3, var2));
            }
         }
      }
   }

   public long a(String var1) {
      s3.c var2 = this.d.get(I(var1));
      return var2 == null ? 0L : var2.price();
   }

   private void X(s3.d var1) {
      this.y.add(var1);
   }

   private void G(class_310 var1, s3.d var2, int var3) {
      String var4 = FakeSpawnerModule.i().w();
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

         this.S.sendAsync(var5.build(), BodyHandlers.ofString()).whenComplete((var4x, var5x) -> var1.execute(() -> {
            if (var5x == null && var4x.statusCode() != 401) {
               if (var4x.statusCode() != 200) {
                  this.G(var1, var2, var3 + 1);
               } else {
                  long var6 = C((String)var4x.body(), var2.term());
                  this.d.put(var2.term(), new s3.c(var6, System.currentTimeMillis()));
                  var2.onResult().accept(var6);
               }
            } else if (var4x != null && var4x.statusCode() == 401) {
               this.d.put(var2.term(), new s3.c(0L, System.currentTimeMillis()));
               var2.onResult().accept(0L);
            } else {
               this.G(var1, var2, var3 + 1);
            }
         }));
      } else {
         this.d.put(var2.term(), new s3.c(0L, System.currentTimeMillis()));
         var2.onResult().accept(0L);
      }
   }

   private static long C(String var0, String var1) {
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
                           if (e(var14, var1)) {
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

   private static boolean e(JsonObject var0, String var1) {
      String var2 = e(var1);
      String var3 = r(var0, "id");
      if (var3 != null) {
         return e(var3).equals(var2);
      }

      String var4 = r(var0, "display_name");
      return var4 != null && e(var4).equals(var2);
   }

   private static String e(String var0) {
      String var1 = I(var0).replace(' ', '_').replace('-', '_');
      int var2 = var1.indexOf(58);
      return var2 >= 0 ? var1.substring(var2 + 1) : var1;
   }

   private static String I(String var0) {
      return var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT);
   }

   private static String r(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && !var2.isJsonNull()) {
         String var3 = var2.getAsString();
         return var3 != null && !var3.isBlank() ? var3 : null;
      } else {
         return null;
      }
   }

   private static String V(String var0) {
      Matcher var1 = B.matcher(var0);
      if (var1.find()) {
         return var1.group();
      }

      Matcher var2 = T.matcher(var0);
      String var3 = null;

      while (var2.find()) {
         if (var3 == null || var2.group().length() > var3.length()) {
            var3 = var2.group();
         }
      }

      return var3;
   }

   private static void t(String var0, class_124 var1) {
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1724 != null) {
         var2.execute(() -> {
            if (var2.field_1724 != null) {
               b1.q(class_2561.method_43470("[Fake Elytra] " + var0).method_27692(var1), false);
            }
         });
      }
   }

   private record c(long price, long at) {
   }

   private record d(String term, Consumer<Long> onResult) {
   }
}
