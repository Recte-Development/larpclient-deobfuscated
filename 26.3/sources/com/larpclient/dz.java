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

public final class dz {
   public static final long y = 60000L;
   public static final long A = 120000L;
   public static final int Q = 32;
   private final dz.z P;
   private final Consumer<String> j;
   private final LinkedHashMap<String, JsonObject> t = new LinkedHashMap<>();
   private final Map<UUID, Long> R = new LinkedHashMap<>();
   private String M = "";
   private String Z = "";
   private String E = "";
   private boolean W;
   private boolean x;
   private boolean u;
   private long q;
   private long U = -1L;
   private long a;
   private long p;
   private long J;
   private long k;
   private int n;
   private long H;
   private dz.u C;
   private String g;
   private String v;
   private JsonObject K;

   public dz(dz.z var1, Consumer<String> var2, JsonObject var3) {
      this.P = var1;
      this.j = var2;
      this.K = var3;
   }

   public synchronized void B(String var1, String var2, boolean var3, long var4, long var6) {
      if (!Objects.equals(this.M, var1)) {
         this.A();
         this.M = var1;
         if (this.K != null && var1.equals(o(this.K, "scope"))) {
            try {
               this.Z = o(this.K, "owner");
               JsonArray var8 = this.K.getAsJsonArray("pending");
               if (var8.size() > 32) {
                  throw new IllegalArgumentException();
               }

               for (JsonElement var10 : var8) {
                  JsonObject var11 = var10.getAsJsonObject();
                  l(var11, var6);
                  this.t.put(o(var11, "sessionId"), var11.deepCopy());
               }
            } catch (RuntimeException var14) {
               this.t.clear();
               this.Z = "";
               this.j.accept("Invalid queued activity discarded");
            }
         }

         this.K = null;
         this.j.accept(var1.isEmpty() ? "Collection stopped: account unavailable" : "Checking dashboard consent");
      }

      if (var1.isEmpty()) {
         this.U = var4;
      } else {
         int var15 = this.W && var4 < this.p ? 1 : 0;
         String var16 = D(var2);
         if (!Objects.equals(this.g, var16)) {
            this.g = null;
            this.v = null;
         }

         long var17 = this.k + Math.max(0L, var4 - this.J);
         int var12 = this.C != null && var17 / 86400000L != this.C.o / 86400000L ? 1 : 0;
         if (this.C != null && var15 != 0 && this.C.i.equals(var16) && var3 && this.u && this.U >= 0L) {
            this.C.c = this.C.c + Math.max(0L, Math.min(2000L, var4 - this.U));
         }

         if (this.C != null && (var16 == null || !this.C.i.equals(var16) || var15 == 0 || var12 != 0)) {
            int var13 = var15 != 0 && this.C.i.equals(var16) ? 1 : 0;
            this.u(var13 == 0);
         }

         if (var15 != 0 && var16 != null && this.C == null) {
            if (this.v == null) {
               this.v = UUID.randomUUID().toString();
               this.g = var16;
            }

            this.C = new dz.u(var16, var17, this.v);
            if (var12 == 0) {
               this.R.clear();
            }

            this.u = (boolean)0;
         }

         this.u = var3;
         this.U = var4;
         if (var16 != null || !this.t.isEmpty()) {
            if (var4 >= this.a && !this.x) {
               JsonObject var18 = var15 != 0 ? this.G() : null;
               if (var18 != null || var15 == 0 || var16 != null || var4 + 60000L >= this.p) {
                  this.A(var18, var4);
               }
            }
         }
      }
   }

   private JsonObject G() {
      if (!this.t.isEmpty()) {
         return this.t.values().iterator().next().deepCopy();
      } else {
         return this.C != null ? this.C.u() : null;
      }
   }

   private void A(JsonObject var1, long var2) {
      this.x = (boolean)1;
      this.a = var2 + 60000L;
      long var4 = this.q;

      CompletableFuture var6;
      try {
         var6 = this.P.n(this.M, var1);
      } catch (RuntimeException var8) {
         var6 = CompletableFuture.failedFuture(var8);
      }

      var6.whenComplete((var6x, var7) -> {
         synchronized (this) {
            if (var4 == this.q) {
               this.x = (boolean)0;
               this.H++;
               long var9 = Math.max(var2, this.U);
               if (var7 != null) {
                  this.n = Math.min(5, this.n + 1);
                  this.a = var9 + Math.min(900000L, 60000L << this.n - 1);
                  if (this.n == 1) {
                     this.j.accept("Activity sync unavailable; retaining bounded queue and backing off");
                  }

                  wr var16 = bl.i(var7);
                  if (var16.A() == 401 || var16.A() == 403) {
                     this.W = (boolean)0;
                     this.W();
                     this.p = 0L;
                  } else if (var1 != null && (var16.A() == 400 || var16.A() == 409 || var16.A() == 413)) {
                     this.t.remove(o(var1, "sessionId"));
                     if (this.C != null && this.C.b.equals(o(var1, "sessionId"))) {
                        this.C = null;
                     }

                     this.a = var9 + 900000L;
                     this.j.accept("Activity snapshot rejected; collection paused before retrying consent");
                     this.p = 0L;
                  }
               } else {
                  try {
                     this.u(var6x, var9);
                     if (var1 != null && (var6x.get("accepted").getAsBoolean() || "STALE_SNAPSHOT".equals(o(var6x, "reason")))) {
                        String var11 = o(var1, "sessionId");
                        JsonObject var12 = this.t.get(var11);
                        if (var12 != null && var12.equals(var1)) {
                           this.t.remove(var11);
                        }
                     }

                     if (this.n > 0) {
                        this.j.accept("Activity sync recovered");
                     }

                     this.n = 0;
                  } catch (RuntimeException var14) {
                     this.p = 0L;
                     this.a = var9 + 60000L;
                     this.j.accept("Invalid activity consent response; collection suspended");
                  }
               }
            }
         }
      });
   }

   private void u(JsonObject var1, long var2) {
      String var4 = o(var1, "ownerId");
      if (!var4.matches("[0-9]{17,20}")) {
         throw new IllegalArgumentException();
      }

      int var5 = var1.getAsJsonPrimitive("enabled").isBoolean() && var1.get("enabled").getAsBoolean() ? 1 : 0;
      long var6 = Instant.parse(o(var1, "serverTime")).toEpochMilli();
      String var8 = var5 != 0 ? o(var1, "collectionStartsAt") : "";
      long var9 = var5 != 0 ? Instant.parse(var8).toEpochMilli() : 0L;
      if (var5 != 0 && var9 > var6) {
         throw new IllegalArgumentException();
      }

      if (!this.Z.isEmpty() && !this.Z.equals(var4)) {
         this.t.clear();
         this.C = null;
         this.R.clear();
         this.v = null;
         this.g = null;
      }

      this.Z = var4;
      if (var5 == 0) {
         if (this.W || !this.t.isEmpty()) {
            this.j.accept("Dashboard tracking off; queued gameplay erased");
         }

         this.t.clear();
         this.C = null;
         this.R.clear();
         this.v = null;
         this.g = null;
      } else if (!this.E.equals(var8)) {
         this.t.values().removeIf(var2x -> Instant.parse(o(var2x, "startedAt")).toEpochMilli() < var9);
         if (this.C != null && this.C.o < var9) {
            this.C = null;
            this.R.clear();
         }

         if (!this.W) {
            this.j.accept("Dashboard tracking on; collecting private gameplay activity");
         }
      }

      this.E = var8;
      this.W = (boolean)var5;
      this.p = var2 + 120000L;
      this.k = var6;
      this.J = var2;
   }

   public synchronized void S(UUID var1, boolean var2, long var3) {
      if (this.C != null && this.W && var3 < this.p) {
         this.R.remove(var1);
         if (var2) {
            this.R.put(var1, var3);
            if (this.R.size() > 128) {
               this.R.remove(this.R.keySet().iterator().next());
            }
         }
      }
   }

   public synchronized void a(UUID var1, long var2) {
      Long var4 = this.R.remove(var1);
      if (this.C != null && this.W && var2 < this.p && var4 != null && var2 >= var4 && var2 - var4 <= 15000L) {
         this.C.R = Math.min(100000, this.C.R + 1);
         this.j.accept("Server-attributed player kill queued for next activity snapshot");
      }
   }

   public synchronized void j() {
      this.R.clear();
   }

   public synchronized void h() {
      this.W();
      this.u = (boolean)0;
      this.v = null;
      this.g = null;
   }

   private void W() {
      this.u(true);
   }

   private void u(boolean var1) {
      if (this.C != null && (this.C.c >= 1000L || this.C.R > 0)) {
         this.t.put(this.C.b, this.C.u());
         if (this.t.size() > 32) {
            this.t.remove(this.t.keySet().iterator().next());
            this.j.accept("Activity queue full; oldest unsent session discarded");
         }
      }

      this.C = null;
      if (var1) {
         this.R.clear();
      }

      this.u = (boolean)0;
   }

   private void A() {
      this.H++;
      this.q++;
      this.t.clear();
      this.C = null;
      this.R.clear();
      this.Z = "";
      this.E = "";
      this.W = (boolean)0;
      this.x = (boolean)0;
      this.p = 0L;
      this.a = 0L;
      this.n = 0;
      this.u = (boolean)0;
      this.U = -1L;
      this.v = null;
      this.g = null;
   }

   public synchronized long w() {
      return this.H;
   }

   public synchronized JsonObject H() {
      if (this.K != null && this.M.isEmpty()) {
         return this.K.deepCopy();
      }

      JsonObject var1 = new JsonObject();
      var1.addProperty("format", 1);
      var1.addProperty("scope", this.M);
      var1.addProperty("owner", this.Z);
      JsonArray var2 = new JsonArray();
      this.t.values().forEach(var1x -> var2.add(var1x.deepCopy()));
      if (this.C != null && (this.C.c >= 1000L || this.C.R > 0)) {
         if (var2.size() == 32) {
            var2.remove(0);
         }

         var2.add(this.C.u());
      }

      var1.add("pending", var2);
      return var1;
   }

   static String o(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : "";
   }

   public static String D(String var0) {
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

   static void l(JsonObject var0, long var1) {
      UUID.fromString(o(var0, "sessionId"));
      if (var0.has("connectionId")) {
         UUID.fromString(o(var0, "connectionId"));
      }

      if (D(o(var0, "server")) == null) {
         throw new IllegalArgumentException();
      }

      long var3 = Instant.parse(o(var0, "startedAt")).toEpochMilli();
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

   private static final class u {
      final String b = UUID.randomUUID().toString();
      final String i;
      final String B;
      final long o;
      long c;
      int R;

      u(String var1, long var2, String var4) {
         this.i = var1;
         this.o = var2;
         this.B = var4 == null ? this.b : var4;
      }

      JsonObject u() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("sessionId", this.b);
         var1.addProperty("server", this.i);
         var1.addProperty("connectionId", this.B);
         var1.addProperty("startedAt", Instant.ofEpochMilli(this.o).toString());
         var1.addProperty("playedSeconds", this.c / 1000L);
         var1.addProperty("kills", this.R);
         return var1;
      }
   }

   public interface z {
      CompletableFuture<JsonObject> n(String var1, JsonObject var2);
   }
}
