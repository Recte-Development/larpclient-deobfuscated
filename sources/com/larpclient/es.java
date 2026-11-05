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

public final class es {
   public static final long X = 60000L;
   public static final long l = 120000L;
   public static final int z = 32;
   private final es.v g;
   private final Consumer<String> F;
   private final LinkedHashMap<String, JsonObject> O = new LinkedHashMap<>();
   private final Map<UUID, Long> I = new LinkedHashMap<>();
   private String q = "";
   private String v = "";
   private String U = "";
   private boolean N;
   private boolean J;
   private boolean s;
   private long W;
   private long Z = -1L;
   private long n;
   private long o;
   private long V;
   private long D;
   private int k;
   private long S;
   private es.b G;
   private String d;
   private String m;
   private JsonObject Q;

   public es(es.v var1, Consumer<String> var2, JsonObject var3) {
      this.g = var1;
      this.F = var2;
      this.Q = var3;
   }

   public synchronized void I(String var1, String var2, boolean var3, long var4, long var6) {
      if (!Objects.equals(this.q, var1)) {
         this.l();
         this.q = var1;
         if (this.Q != null && var1.equals(c(this.Q, "scope"))) {
            try {
               this.v = c(this.Q, "owner");
               JsonArray var8 = this.Q.getAsJsonArray("pending");
               if (var8.size() > 32) {
                  throw new IllegalArgumentException();
               }

               for (JsonElement var10 : var8) {
                  JsonObject var11 = var10.getAsJsonObject();
                  x(var11, var6);
                  this.O.put(c(var11, "sessionId"), var11.deepCopy());
               }
            } catch (RuntimeException var14) {
               this.O.clear();
               this.v = "";
               this.F.accept("Invalid queued activity discarded");
            }
         }

         this.Q = null;
         this.F.accept(var1.isEmpty() ? "Collection stopped: account unavailable" : "Checking dashboard consent");
      }

      if (var1.isEmpty()) {
         this.Z = var4;
      } else {
         int var15 = this.N && var4 < this.o ? 1 : 0;
         String var16 = a(var2);
         if (!Objects.equals(this.d, var16)) {
            this.d = null;
            this.m = null;
         }

         long var17 = this.D + Math.max(0L, var4 - this.V);
         int var12 = this.G != null && var17 / 86400000L != this.G.l / 86400000L ? 1 : 0;
         if (this.G != null && var15 != 0 && this.G.f.equals(var16) && var3 && this.s && this.Z >= 0L) {
            this.G.N = this.G.N + Math.max(0L, Math.min(2000L, var4 - this.Z));
         }

         if (this.G != null && (var16 == null || !this.G.f.equals(var16) || var15 == 0 || var12 != 0)) {
            int var13 = var15 != 0 && this.G.f.equals(var16) ? 1 : 0;
            this.y(var13 == 0);
         }

         if (var15 != 0 && var16 != null && this.G == null) {
            if (this.m == null) {
               this.m = UUID.randomUUID().toString();
               this.d = var16;
            }

            this.G = new es.b(var16, var17, this.m);
            if (var12 == 0) {
               this.I.clear();
            }

            this.s = (boolean)0;
         }

         this.s = var3;
         this.Z = var4;
         if (var16 != null || !this.O.isEmpty()) {
            if (var4 >= this.n && !this.J) {
               JsonObject var18 = var15 != 0 ? this.f() : null;
               if (var18 != null || var15 == 0 || var16 != null || var4 + 60000L >= this.o) {
                  this.k(var18, var4);
               }
            }
         }
      }
   }

   private JsonObject f() {
      if (!this.O.isEmpty()) {
         return this.O.values().iterator().next().deepCopy();
      } else {
         return this.G != null ? this.G.t() : null;
      }
   }

   private void k(JsonObject var1, long var2) {
      this.J = (boolean)1;
      this.n = var2 + 60000L;
      long var4 = this.W;

      CompletableFuture var6;
      try {
         var6 = this.g.X(this.q, var1);
      } catch (RuntimeException var8) {
         var6 = CompletableFuture.failedFuture(var8);
      }

      var6.whenComplete((var6x, var7) -> {
         synchronized (this) {
            if (var4 == this.W) {
               this.J = (boolean)0;
               this.S++;
               long var9 = Math.max(var2, this.Z);
               if (var7 != null) {
                  this.k = Math.min(5, this.k + 1);
                  this.n = var9 + Math.min(900000L, 60000L << this.k - 1);
                  if (this.k == 1) {
                     this.F.accept("Activity sync unavailable; retaining bounded queue and backing off");
                  }

                  oj var16 = hl.D(var7);
                  if (var16.M() == 401 || var16.M() == 403) {
                     this.N = (boolean)0;
                     this.H();
                     this.o = 0L;
                  } else if (var1 != null && (var16.M() == 400 || var16.M() == 409 || var16.M() == 413)) {
                     this.O.remove(c(var1, "sessionId"));
                     if (this.G != null && this.G.n.equals(c(var1, "sessionId"))) {
                        this.G = null;
                     }

                     this.n = var9 + 900000L;
                     this.F.accept("Activity snapshot rejected; collection paused before retrying consent");
                     this.o = 0L;
                  }
               } else {
                  try {
                     this.Y(var6x, var9);
                     if (var1 != null && (var6x.get("accepted").getAsBoolean() || "STALE_SNAPSHOT".equals(c(var6x, "reason")))) {
                        String var11 = c(var1, "sessionId");
                        JsonObject var12 = this.O.get(var11);
                        if (var12 != null && var12.equals(var1)) {
                           this.O.remove(var11);
                        }
                     }

                     if (this.k > 0) {
                        this.F.accept("Activity sync recovered");
                     }

                     this.k = 0;
                  } catch (RuntimeException var14) {
                     this.o = 0L;
                     this.n = var9 + 60000L;
                     this.F.accept("Invalid activity consent response; collection suspended");
                  }
               }
            }
         }
      });
   }

   private void Y(JsonObject var1, long var2) {
      String var4 = c(var1, "ownerId");
      if (!var4.matches("[0-9]{17,20}")) {
         throw new IllegalArgumentException();
      }

      int var5 = var1.getAsJsonPrimitive("enabled").isBoolean() && var1.get("enabled").getAsBoolean() ? 1 : 0;
      long var6 = Instant.parse(c(var1, "serverTime")).toEpochMilli();
      String var8 = var5 != 0 ? c(var1, "collectionStartsAt") : "";
      long var9 = var5 != 0 ? Instant.parse(var8).toEpochMilli() : 0L;
      if (var5 != 0 && var9 > var6) {
         throw new IllegalArgumentException();
      }

      if (!this.v.isEmpty() && !this.v.equals(var4)) {
         this.O.clear();
         this.G = null;
         this.I.clear();
         this.m = null;
         this.d = null;
      }

      this.v = var4;
      if (var5 == 0) {
         if (this.N || !this.O.isEmpty()) {
            this.F.accept("Dashboard tracking off; queued gameplay erased");
         }

         this.O.clear();
         this.G = null;
         this.I.clear();
         this.m = null;
         this.d = null;
      } else if (!this.U.equals(var8)) {
         this.O.values().removeIf(var2x -> Instant.parse(c(var2x, "startedAt")).toEpochMilli() < var9);
         if (this.G != null && this.G.l < var9) {
            this.G = null;
            this.I.clear();
         }

         if (!this.N) {
            this.F.accept("Dashboard tracking on; collecting private gameplay activity");
         }
      }

      this.U = var8;
      this.N = (boolean)var5;
      this.o = var2 + 120000L;
      this.D = var6;
      this.V = var2;
   }

   public synchronized void w(UUID var1, boolean var2, long var3) {
      if (this.G != null && this.N && var3 < this.o) {
         this.I.remove(var1);
         if (var2) {
            this.I.put(var1, var3);
            if (this.I.size() > 128) {
               this.I.remove(this.I.keySet().iterator().next());
            }
         }
      }
   }

   public synchronized void K(UUID var1, long var2) {
      Long var4 = this.I.remove(var1);
      if (this.G != null && this.N && var2 < this.o && var4 != null && var2 >= var4 && var2 - var4 <= 15000L) {
         this.G.t = Math.min(100000, this.G.t + 1);
         this.F.accept("Server-attributed player kill queued for next activity snapshot");
      }
   }

   public synchronized void k() {
      this.I.clear();
   }

   public synchronized void i() {
      this.H();
      this.s = (boolean)0;
      this.m = null;
      this.d = null;
   }

   private void H() {
      this.y(true);
   }

   private void y(boolean var1) {
      if (this.G != null && (this.G.N >= 1000L || this.G.t > 0)) {
         this.O.put(this.G.n, this.G.t());
         if (this.O.size() > 32) {
            this.O.remove(this.O.keySet().iterator().next());
            this.F.accept("Activity queue full; oldest unsent session discarded");
         }
      }

      this.G = null;
      if (var1) {
         this.I.clear();
      }

      this.s = (boolean)0;
   }

   private void l() {
      this.S++;
      this.W++;
      this.O.clear();
      this.G = null;
      this.I.clear();
      this.v = "";
      this.U = "";
      this.N = (boolean)0;
      this.J = (boolean)0;
      this.o = 0L;
      this.n = 0L;
      this.k = 0;
      this.s = (boolean)0;
      this.Z = -1L;
      this.m = null;
      this.d = null;
   }

   public synchronized long m() {
      return this.S;
   }

   public synchronized JsonObject I() {
      if (this.Q != null && this.q.isEmpty()) {
         return this.Q.deepCopy();
      }

      JsonObject var1 = new JsonObject();
      var1.addProperty("format", 1);
      var1.addProperty("scope", this.q);
      var1.addProperty("owner", this.v);
      JsonArray var2 = new JsonArray();
      this.O.values().forEach(var1x -> var2.add(var1x.deepCopy()));
      if (this.G != null && (this.G.N >= 1000L || this.G.t > 0)) {
         if (var2.size() == 32) {
            var2.remove(0);
         }

         var2.add(this.G.t());
      }

      var1.add("pending", var2);
      return var1;
   }

   static String c(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : "";
   }

   public static String a(String var0) {
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

   static void x(JsonObject var0, long var1) {
      UUID.fromString(c(var0, "sessionId"));
      if (var0.has("connectionId")) {
         UUID.fromString(c(var0, "connectionId"));
      }

      if (a(c(var0, "server")) == null) {
         throw new IllegalArgumentException();
      }

      long var3 = Instant.parse(c(var0, "startedAt")).toEpochMilli();
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

   private static final class b {
      final String n = UUID.randomUUID().toString();
      final String f;
      final String b;
      final long l;
      long N;
      int t;

      b(String var1, long var2, String var4) {
         this.f = var1;
         this.l = var2;
         this.b = var4 == null ? this.n : var4;
      }

      JsonObject t() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("sessionId", this.n);
         var1.addProperty("server", this.f);
         var1.addProperty("connectionId", this.b);
         var1.addProperty("startedAt", Instant.ofEpochMilli(this.l).toString());
         var1.addProperty("playedSeconds", this.N / 1000L);
         var1.addProperty("kills", this.t);
         return var1;
      }
   }

   public interface v {
      CompletableFuture<JsonObject> X(String var1, JsonObject var2);
   }
}
