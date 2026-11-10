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

public final class lc {
   public static final long i = 60000L;
   public static final long R = 120000L;
   public static final int U = 32;
   private final lc.j r;
   private final Consumer<String> e;
   private final LinkedHashMap<String, JsonObject> c = new LinkedHashMap<>();
   private final Map<UUID, Long> L = new LinkedHashMap<>();
   private String F = "";
   private String w = "";
   private String Q = "";
   private boolean o;
   private boolean P;
   private boolean C;
   private long N;
   private long g = -1L;
   private long X;
   private long H;
   private long t;
   private long v;
   private int q;
   private long k;
   private lc.b j;
   private String G;
   private String n;
   private JsonObject m;

   public lc(lc.j var1, Consumer<String> var2, JsonObject var3) {
      this.r = var1;
      this.e = var2;
      this.m = var3;
   }

   public synchronized void p(String var1, String var2, boolean var3, long var4, long var6) {
      if (!Objects.equals(this.F, var1)) {
         this.O();
         this.F = var1;
         if (this.m != null && var1.equals(V(this.m, "scope"))) {
            try {
               this.w = V(this.m, "owner");
               JsonArray var8 = this.m.getAsJsonArray("pending");
               if (var8.size() > 32) {
                  throw new IllegalArgumentException();
               }

               for (JsonElement var10 : var8) {
                  JsonObject var11 = var10.getAsJsonObject();
                  G(var11, var6);
                  this.c.put(V(var11, "sessionId"), var11.deepCopy());
               }
            } catch (RuntimeException var14) {
               this.c.clear();
               this.w = "";
               this.e.accept("Invalid queued activity discarded");
            }
         }

         this.m = null;
         this.e.accept(var1.isEmpty() ? "Collection stopped: account unavailable" : "Checking dashboard consent");
      }

      if (var1.isEmpty()) {
         this.g = var4;
      } else {
         int var15 = this.o && var4 < this.H ? 1 : 0;
         String var16 = M(var2);
         if (!Objects.equals(this.G, var16)) {
            this.G = null;
            this.n = null;
         }

         long var17 = this.v + Math.max(0L, var4 - this.t);
         int var12 = this.j != null && var17 / 86400000L != this.j.p / 86400000L ? 1 : 0;
         if (this.j != null && var15 != 0 && this.j.a.equals(var16) && var3 && this.C && this.g >= 0L) {
            this.j.S = this.j.S + Math.max(0L, Math.min(2000L, var4 - this.g));
         }

         if (this.j != null && (var16 == null || !this.j.a.equals(var16) || var15 == 0 || var12 != 0)) {
            int var13 = var15 != 0 && this.j.a.equals(var16) ? 1 : 0;
            this.I(var13 == 0);
         }

         if (var15 != 0 && var16 != null && this.j == null) {
            if (this.n == null) {
               this.n = UUID.randomUUID().toString();
               this.G = var16;
            }

            this.j = new lc.b(var16, var17, this.n);
            if (var12 == 0) {
               this.L.clear();
            }

            this.C = (boolean)0;
         }

         this.C = var3;
         this.g = var4;
         if (var16 != null || !this.c.isEmpty()) {
            if (var4 >= this.X && !this.P) {
               JsonObject var18 = var15 != 0 ? this.v() : null;
               if (var18 != null || var15 == 0 || var16 != null || var4 + 60000L >= this.H) {
                  this.h(var18, var4);
               }
            }
         }
      }
   }

   private JsonObject v() {
      if (!this.c.isEmpty()) {
         return this.c.values().iterator().next().deepCopy();
      } else {
         return this.j != null ? this.j.p() : null;
      }
   }

   private void h(JsonObject var1, long var2) {
      this.P = (boolean)1;
      this.X = var2 + 60000L;
      long var4 = this.N;

      CompletableFuture var6;
      try {
         var6 = this.r.T(this.F, var1);
      } catch (RuntimeException var8) {
         var6 = CompletableFuture.failedFuture(var8);
      }

      var6.whenComplete((var6x, var7) -> {
         synchronized (this) {
            if (var4 == this.N) {
               this.P = (boolean)0;
               this.k++;
               long var9 = Math.max(var2, this.g);
               if (var7 != null) {
                  this.q = Math.min(5, this.q + 1);
                  this.X = var9 + Math.min(900000L, 60000L << this.q - 1);
                  if (this.q == 1) {
                     this.e.accept("Activity sync unavailable; retaining bounded queue and backing off");
                  }

                  bh var16 = uq.q(var7);
                  if (var16.i() == 401 || var16.i() == 403) {
                     this.o = (boolean)0;
                     this.z();
                     this.H = 0L;
                  } else if (var1 != null && (var16.i() == 400 || var16.i() == 409 || var16.i() == 413)) {
                     this.c.remove(V(var1, "sessionId"));
                     if (this.j != null && this.j.x.equals(V(var1, "sessionId"))) {
                        this.j = null;
                     }

                     this.X = var9 + 900000L;
                     this.e.accept("Activity snapshot rejected; collection paused before retrying consent");
                     this.H = 0L;
                  }
               } else {
                  try {
                     this.w(var6x, var9);
                     if (var1 != null && (var6x.get("accepted").getAsBoolean() || "STALE_SNAPSHOT".equals(V(var6x, "reason")))) {
                        String var11 = V(var1, "sessionId");
                        JsonObject var12 = this.c.get(var11);
                        if (var12 != null && var12.equals(var1)) {
                           this.c.remove(var11);
                        }
                     }

                     if (this.q > 0) {
                        this.e.accept("Activity sync recovered");
                     }

                     this.q = 0;
                  } catch (RuntimeException var14) {
                     this.H = 0L;
                     this.X = var9 + 60000L;
                     this.e.accept("Invalid activity consent response; collection suspended");
                  }
               }
            }
         }
      });
   }

   private void w(JsonObject var1, long var2) {
      String var4 = V(var1, "ownerId");
      if (!var4.matches("[0-9]{17,20}")) {
         throw new IllegalArgumentException();
      }

      int var5 = var1.getAsJsonPrimitive("enabled").isBoolean() && var1.get("enabled").getAsBoolean() ? 1 : 0;
      long var6 = Instant.parse(V(var1, "serverTime")).toEpochMilli();
      String var8 = var5 != 0 ? V(var1, "collectionStartsAt") : "";
      long var9 = var5 != 0 ? Instant.parse(var8).toEpochMilli() : 0L;
      if (var5 != 0 && var9 > var6) {
         throw new IllegalArgumentException();
      }

      if (!this.w.isEmpty() && !this.w.equals(var4)) {
         this.c.clear();
         this.j = null;
         this.L.clear();
         this.n = null;
         this.G = null;
      }

      this.w = var4;
      if (var5 == 0) {
         if (this.o || !this.c.isEmpty()) {
            this.e.accept("Dashboard tracking off; queued gameplay erased");
         }

         this.c.clear();
         this.j = null;
         this.L.clear();
         this.n = null;
         this.G = null;
      } else if (!this.Q.equals(var8)) {
         this.c.values().removeIf(var2x -> Instant.parse(V(var2x, "startedAt")).toEpochMilli() < var9);
         if (this.j != null && this.j.p < var9) {
            this.j = null;
            this.L.clear();
         }

         if (!this.o) {
            this.e.accept("Dashboard tracking on; collecting private gameplay activity");
         }
      }

      this.Q = var8;
      this.o = (boolean)var5;
      this.H = var2 + 120000L;
      this.v = var6;
      this.t = var2;
   }

   public synchronized void V(UUID var1, boolean var2, long var3) {
      if (this.j != null && this.o && var3 < this.H) {
         this.L.remove(var1);
         if (var2) {
            this.L.put(var1, var3);
            if (this.L.size() > 128) {
               this.L.remove(this.L.keySet().iterator().next());
            }
         }
      }
   }

   public synchronized void d(UUID var1, long var2) {
      Long var4 = this.L.remove(var1);
      if (this.j != null && this.o && var2 < this.H && var4 != null && var2 >= var4 && var2 - var4 <= 15000L) {
         this.j.b = Math.min(100000, this.j.b + 1);
         this.e.accept("Server-attributed player kill queued for next activity snapshot");
      }
   }

   public synchronized void B() {
      this.L.clear();
   }

   public synchronized void b() {
      this.z();
      this.C = (boolean)0;
      this.n = null;
      this.G = null;
   }

   private void z() {
      this.I(true);
   }

   private void I(boolean var1) {
      if (this.j != null && (this.j.S >= 1000L || this.j.b > 0)) {
         this.c.put(this.j.x, this.j.p());
         if (this.c.size() > 32) {
            this.c.remove(this.c.keySet().iterator().next());
            this.e.accept("Activity queue full; oldest unsent session discarded");
         }
      }

      this.j = null;
      if (var1) {
         this.L.clear();
      }

      this.C = (boolean)0;
   }

   private void O() {
      this.k++;
      this.N++;
      this.c.clear();
      this.j = null;
      this.L.clear();
      this.w = "";
      this.Q = "";
      this.o = (boolean)0;
      this.P = (boolean)0;
      this.H = 0L;
      this.X = 0L;
      this.q = 0;
      this.C = (boolean)0;
      this.g = -1L;
      this.n = null;
      this.G = null;
   }

   public synchronized long p() {
      return this.k;
   }

   public synchronized JsonObject G() {
      if (this.m != null && this.F.isEmpty()) {
         return this.m.deepCopy();
      }

      JsonObject var1 = new JsonObject();
      var1.addProperty("format", 1);
      var1.addProperty("scope", this.F);
      var1.addProperty("owner", this.w);
      JsonArray var2 = new JsonArray();
      this.c.values().forEach(var1x -> var2.add(var1x.deepCopy()));
      if (this.j != null && (this.j.S >= 1000L || this.j.b > 0)) {
         if (var2.size() == 32) {
            var2.remove(0);
         }

         var2.add(this.j.p());
      }

      var1.add("pending", var2);
      return var1;
   }

   static String V(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : "";
   }

   public static String M(String var0) {
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

   static void G(JsonObject var0, long var1) {
      UUID.fromString(V(var0, "sessionId"));
      if (var0.has("connectionId")) {
         UUID.fromString(V(var0, "connectionId"));
      }

      if (M(V(var0, "server")) == null) {
         throw new IllegalArgumentException();
      }

      long var3 = Instant.parse(V(var0, "startedAt")).toEpochMilli();
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
      final String x = UUID.randomUUID().toString();
      final String a;
      final String o;
      final long p;
      long S;
      int b;

      b(String var1, long var2, String var4) {
         this.a = var1;
         this.p = var2;
         this.o = var4 == null ? this.x : var4;
      }

      JsonObject p() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("sessionId", this.x);
         var1.addProperty("server", this.a);
         var1.addProperty("connectionId", this.o);
         var1.addProperty("startedAt", Instant.ofEpochMilli(this.p).toString());
         var1.addProperty("playedSeconds", this.S / 1000L);
         var1.addProperty("kills", this.b);
         return var1;
      }
   }

   public interface j {
      CompletableFuture<JsonObject> T(String var1, JsonObject var2);
   }
}
