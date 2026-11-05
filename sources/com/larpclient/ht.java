package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class ht {
   public static final long d = 60000L;
   public static final long f = 120000L;
   public static final int o = 32;
   private final ht.j i;
   private final Consumer<String> D;
   private final LinkedHashMap<String, JsonObject> p = new LinkedHashMap<>();
   private final Map<UUID, Long> l = new LinkedHashMap<>();
   private String z = "";
   private String a = "";
   private String w = "";
   private boolean S;
   private boolean Y;
   private boolean J;
   private long t;
   private long Q = -1L;
   private long e;
   private long y;
   private long H;
   private long T;
   private int O;
   private long W;
   private ht.e m;
   private String g;
   private String b;
   private JsonObject P;

   public ht(ht.j var1, Consumer<String> var2, JsonObject var3) {
      this.i = var1;
      this.D = var2;
      this.P = var3;
   }

   public synchronized void e(String var1, String var2, boolean var3, long var4, long var6) {
      if (!Objects.equals(this.z, var1)) {
         this.A();
         this.z = var1;
         if (this.P != null && var1.equals(M(this.P, "scope"))) {
            try {
               this.a = M(this.P, "owner");
               JsonArray var8 = this.P.getAsJsonArray("pending");
               if (var8.size() > 32) {
                  throw new IllegalArgumentException();
               }

               for (JsonElement var10 : var8) {
                  JsonObject var11 = var10.getAsJsonObject();
                  D(var11, var6);
                  this.p.put(M(var11, "sessionId"), var11.deepCopy());
               }
            } catch (RuntimeException var14) {
               this.p.clear();
               this.a = "";
               this.D.accept("Invalid queued activity discarded");
            }
         }

         this.P = null;
         this.D.accept(var1.isEmpty() ? "Collection stopped: account unavailable" : "Checking dashboard consent");
      }

      if (var1.isEmpty()) {
         this.Q = var4;
      } else {
         int var15 = this.S && var4 < this.y ? 1 : 0;
         String var16 = c(var2);
         if (!Objects.equals(this.g, var16)) {
            this.g = null;
            this.b = null;
         }

         long var17 = this.T + Math.max(0L, var4 - this.H);
         int var12 = this.m != null && var17 / 86400000L != this.m.g / 86400000L ? 1 : 0;
         if (this.m != null && var15 != 0 && this.m.x.equals(var16) && var3 && this.J && this.Q >= 0L) {
            this.m.a = this.m.a + Math.max(0L, Math.min(2000L, var4 - this.Q));
         }

         if (this.m != null && (var16 == null || !this.m.x.equals(var16) || var15 == 0 || var12 != 0)) {
            int var13 = var15 != 0 && this.m.x.equals(var16) ? 1 : 0;
            this.l(var13 == 0);
         }

         if (var15 != 0 && var16 != null && this.m == null) {
            if (this.b == null) {
               this.b = UUID.randomUUID().toString();
               this.g = var16;
            }

            this.m = new ht.e(var16, var17, this.b);
            if (var12 == 0) {
               this.l.clear();
            }

            this.J = (boolean)0;
         }

         this.J = var3;
         this.Q = var4;
         if (var16 != null || !this.p.isEmpty()) {
            if (var4 >= this.e && !this.Y) {
               JsonObject var18 = var15 != 0 ? this.z() : null;
               if (var18 != null || var15 == 0 || var16 != null || var4 + 60000L >= this.y) {
                  this.n(var18, var4);
               }
            }
         }
      }
   }

   private JsonObject z() {
      if (!this.p.isEmpty()) {
         return this.p.values().iterator().next().deepCopy();
      } else {
         return this.m != null ? this.m.d() : null;
      }
   }

   private void n(JsonObject var1, long var2) {
      this.Y = (boolean)1;
      this.e = var2 + 60000L;
      long var4 = this.t;

      CompletableFuture var6;
      try {
         var6 = this.i.A(this.z, var1);
      } catch (RuntimeException var8) {
         var6 = CompletableFuture.failedFuture(var8);
      }

      var6.whenComplete((var6x, var7) -> {
         synchronized (this) {
            if (var4 == this.t) {
               this.Y = (boolean)0;
               this.W++;
               long var9 = Math.max(var2, this.Q);
               if (var7 != null) {
                  this.O = Math.min(5, this.O + 1);
                  this.e = var9 + Math.min(900000L, 60000L << this.O - 1);
                  if (this.O == 1) {
                     this.D.accept("Activity sync unavailable; retaining bounded queue and backing off");
                  }

                  pe var16 = cm.G(var7);
                  if (var16.F() == 401 || var16.F() == 403) {
                     this.S = (boolean)0;
                     this.p();
                     this.y = 0L;
                  } else if (var1 != null && (var16.F() == 400 || var16.F() == 409 || var16.F() == 413)) {
                     this.p.remove(M(var1, "sessionId"));
                     if (this.m != null && this.m.h.equals(M(var1, "sessionId"))) {
                        this.m = null;
                     }

                     this.e = var9 + 900000L;
                     this.D.accept("Activity snapshot rejected; collection paused before retrying consent");
                     this.y = 0L;
                  }
               } else {
                  try {
                     this.q(var6x, var9);
                     if (var1 != null && (var6x.get("accepted").getAsBoolean() || "STALE_SNAPSHOT".equals(M(var6x, "reason")))) {
                        String var11 = M(var1, "sessionId");
                        JsonObject var12 = this.p.get(var11);
                        if (var12 != null && var12.equals(var1)) {
                           this.p.remove(var11);
                        }
                     }

                     if (this.O > 0) {
                        this.D.accept("Activity sync recovered");
                     }

                     this.O = 0;
                  } catch (RuntimeException var14) {
                     this.y = 0L;
                     this.e = var9 + 60000L;
                     this.D.accept("Invalid activity consent response; collection suspended");
                  }
               }
            }
         }
      });
   }

   private void q(JsonObject var1, long var2) {
      String var4 = M(var1, "ownerId");
      if (!var4.matches("[0-9]{17,20}")) {
         throw new IllegalArgumentException();
      }

      int var5 = var1.getAsJsonPrimitive("enabled").isBoolean() && var1.get("enabled").getAsBoolean() ? 1 : 0;
      long var6 = Instant.parse(M(var1, "serverTime")).toEpochMilli();
      String var8 = var5 != 0 ? M(var1, "collectionStartsAt") : "";
      long var9 = var5 != 0 ? Instant.parse(var8).toEpochMilli() : 0L;
      if (var5 != 0 && var9 > var6) {
         throw new IllegalArgumentException();
      }

      if (!this.a.isEmpty() && !this.a.equals(var4)) {
         this.p.clear();
         this.m = null;
         this.l.clear();
         this.b = null;
         this.g = null;
      }

      this.a = var4;
      if (var5 == 0) {
         if (this.S || !this.p.isEmpty()) {
            this.D.accept("Dashboard tracking off; queued gameplay erased");
         }

         this.p.clear();
         this.m = null;
         this.l.clear();
         this.b = null;
         this.g = null;
      } else if (!this.w.equals(var8)) {
         this.p.values().removeIf(var2x -> Instant.parse(M(var2x, "startedAt")).toEpochMilli() < var9);
         if (this.m != null && this.m.g < var9) {
            this.m = null;
            this.l.clear();
         }

         if (!this.S) {
            this.D.accept("Dashboard tracking on; collecting private gameplay activity");
         }
      }

      this.w = var8;
      this.S = (boolean)var5;
      this.y = var2 + 120000L;
      this.T = var6;
      this.H = var2;
   }

   public synchronized void O(UUID var1, boolean var2, long var3) {
      if (this.m != null && this.S && var3 < this.y) {
         this.l.remove(var1);
         if (var2) {
            this.l.put(var1, var3);
            if (this.l.size() > 128) {
               this.l.remove(this.l.keySet().iterator().next());
            }
         }
      }
   }

   public synchronized void H(UUID var1, long var2) {
      Long var4 = this.l.remove(var1);
      if (this.m != null && this.S && var2 < this.y && var4 != null && var2 >= var4 && var2 - var4 <= 15000L) {
         this.m.b = Math.min(100000, this.m.b + 1);
         this.D.accept("Server-attributed player kill queued for next activity snapshot");
      }
   }

   public synchronized void S() {
      this.l.clear();
   }

   public synchronized void n() {
      this.p();
      this.J = (boolean)0;
      this.b = null;
      this.g = null;
   }

   private void p() {
      this.l(true);
   }

   private void l(boolean var1) {
      if (this.m != null && (this.m.a >= 1000L || this.m.b > 0)) {
         this.p.put(this.m.h, this.m.d());
         if (this.p.size() > 32) {
            this.p.remove(this.p.keySet().iterator().next());
            this.D.accept("Activity queue full; oldest unsent session discarded");
         }
      }

      this.m = null;
      if (var1) {
         this.l.clear();
      }

      this.J = (boolean)0;
   }

   private void A() {
      this.W++;
      this.t++;
      this.p.clear();
      this.m = null;
      this.l.clear();
      this.a = "";
      this.w = "";
      this.S = (boolean)0;
      this.Y = (boolean)0;
      this.y = 0L;
      this.e = 0L;
      this.O = 0;
      this.J = (boolean)0;
      this.Q = -1L;
      this.b = null;
      this.g = null;
   }

   public synchronized long d() {
      return this.W;
   }

   public synchronized JsonObject n() {
      if (this.P != null && this.z.isEmpty()) {
         return this.P.deepCopy();
      }

      JsonObject var1 = new JsonObject();
      var1.addProperty("format", 1);
      var1.addProperty("scope", this.z);
      var1.addProperty("owner", this.a);
      JsonArray var2 = new JsonArray();
      this.p.values().forEach(var1x -> var2.add(var1x.deepCopy()));
      if (this.m != null && (this.m.a >= 1000L || this.m.b > 0)) {
         if (var2.size() == 32) {
            var2.remove(0);
         }

         var2.add(this.m.d());
      }

      var1.add("pending", var2);
      return var1;
   }

   static String M(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : "";
   }

   public static String c(String var0) {
      if (var0 != null && var0.length() <= 253) {
         String var1 = var0.trim().toLowerCase(Locale.ROOT);
         if (!var1.matches("(?:[a-z0-9](?:[a-z0-9.-]*[a-z0-9])?|\\[[0-9a-f:]+\\])(?::[0-9]{1,5})?")) {
            return null;
         }

         int var2 = var1.startsWith("[") ? var1.indexOf(58, var1.indexOf(93)) : var1.indexOf(58);
         if (var2 >= 0) {
            int var3 = Integer.parseInt(var1.substring(var2 + 1));
            if (var3 < 1 || var3 > 65535) {
               return null;
            }

            if (var3 == 25565) {
               var1 = var1.substring(0, var2);
            }
         }

         return var1;
      } else {
         return null;
      }
   }

   static void D(JsonObject var0, long var1) {
      UUID.fromString(M(var0, "sessionId"));
      if (var0.has("connectionId")) {
         UUID.fromString(M(var0, "connectionId"));
      }

      if (c(M(var0, "server")) == null) {
         throw new IllegalArgumentException();
      }

      long var3 = Instant.parse(M(var0, "startedAt")).toEpochMilli();
      if (var3 >= var1 - 7776000000L && var3 <= var1 + 86400000L) {
         int var5 = var0.get("playedSeconds").getAsInt();
         int var6 = var0.get("kills").getAsInt();
         if (var5 < 0 || var5 > 86400 || var6 < 0 || var6 > 100000) {
            throw new IllegalArgumentException();
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static final class e {
      final String h = UUID.randomUUID().toString();
      final String x;
      final String B;
      final long g;
      long a;
      int b;

      e(String var1, long var2, String var4) {
         this.x = var1;
         this.g = var2;
         this.B = var4 == null ? this.h : var4;
      }

      JsonObject d() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("sessionId", this.h);
         var1.addProperty("server", this.x);
         var1.addProperty("connectionId", this.B);
         var1.addProperty("startedAt", Instant.ofEpochMilli(this.g).toString());
         var1.addProperty("playedSeconds", this.a / 1000L);
         var1.addProperty("kills", this.b);
         return var1;
      }
   }

   public interface j {
      CompletableFuture<JsonObject> A(String var1, JsonObject var2);
   }
}
