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

public final class ye {
   public static final long b = 60000L;
   public static final long M = 120000L;
   public static final int K = 32;
   private final ye.y n;
   private final Consumer<String> l;
   private final LinkedHashMap<String, JsonObject> D = new LinkedHashMap<>();
   private final Map<UUID, Long> d = new LinkedHashMap<>();
   private String p = "";
   private String J = "";
   private String F = "";
   private boolean W;
   private boolean x;
   private boolean z;
   private long A;
   private long U = -1L;
   private long Z;
   private long i;
   private long y;
   private long s;
   private int I;
   private long R;
   private ye.u Q;
   private String T;
   private String E;
   private JsonObject o;

   public ye(ye.y var1, Consumer<String> var2, JsonObject var3) {
      this.n = var1;
      this.l = var2;
      this.o = var3;
   }

   public synchronized void d(String var1, String var2, boolean var3, long var4, long var6) {
      if (!Objects.equals(this.p, var1)) {
         this.Y();
         this.p = var1;
         if (this.o != null && var1.equals(B(this.o, "scope"))) {
            try {
               this.J = B(this.o, "owner");
               JsonArray var8 = this.o.getAsJsonArray("pending");
               if (var8.size() > 32) {
                  throw new IllegalArgumentException();
               }

               for (JsonElement var10 : var8) {
                  JsonObject var11 = var10.getAsJsonObject();
                  a(var11, var6);
                  this.D.put(B(var11, "sessionId"), var11.deepCopy());
               }
            } catch (RuntimeException var14) {
               this.D.clear();
               this.J = "";
               this.l.accept("Invalid queued activity discarded");
            }
         }

         this.o = null;
         this.l.accept(var1.isEmpty() ? "Collection stopped: account unavailable" : "Checking dashboard consent");
      }

      if (var1.isEmpty()) {
         this.U = var4;
      } else {
         int var15 = this.W && var4 < this.i ? 1 : 0;
         String var16 = F(var2);
         if (!Objects.equals(this.T, var16)) {
            this.T = null;
            this.E = null;
         }

         long var17 = this.s + Math.max(0L, var4 - this.y);
         int var12 = this.Q != null && var17 / 86400000L != this.Q.Z / 86400000L ? 1 : 0;
         if (this.Q != null && var15 != 0 && this.Q.G.equals(var16) && var3 && this.z && this.U >= 0L) {
            this.Q.O = this.Q.O + Math.max(0L, Math.min(2000L, var4 - this.U));
         }

         if (this.Q != null && (var16 == null || !this.Q.G.equals(var16) || var15 == 0 || var12 != 0)) {
            int var13 = var15 != 0 && this.Q.G.equals(var16) ? 1 : 0;
            this.H(var13 == 0);
         }

         if (var15 != 0 && var16 != null && this.Q == null) {
            if (this.E == null) {
               this.E = UUID.randomUUID().toString();
               this.T = var16;
            }

            this.Q = new ye.u(var16, var17, this.E);
            if (var12 == 0) {
               this.d.clear();
            }

            this.z = (boolean)0;
         }

         this.z = var3;
         this.U = var4;
         if (var16 != null || !this.D.isEmpty()) {
            if (var4 >= this.Z && !this.x) {
               JsonObject var18 = var15 != 0 ? this.l() : null;
               if (var18 != null || var15 == 0 || var16 != null || var4 + 60000L >= this.i) {
                  this.C(var18, var4);
               }
            }
         }
      }
   }

   private JsonObject l() {
      if (!this.D.isEmpty()) {
         return this.D.values().iterator().next().deepCopy();
      } else {
         return this.Q != null ? this.Q.s() : null;
      }
   }

   private void C(JsonObject var1, long var2) {
      this.x = (boolean)1;
      this.Z = var2 + 60000L;
      long var4 = this.A;

      CompletableFuture var6;
      try {
         var6 = this.n.W(this.p, var1);
      } catch (RuntimeException var8) {
         var6 = CompletableFuture.failedFuture(var8);
      }

      var6.whenComplete((var6x, var7) -> {
         synchronized (this) {
            if (var4 == this.A) {
               this.x = (boolean)0;
               this.R++;
               long var9 = Math.max(var2, this.U);
               if (var7 != null) {
                  this.I = Math.min(5, this.I + 1);
                  this.Z = var9 + Math.min(900000L, 60000L << this.I - 1);
                  if (this.I == 1) {
                     this.l.accept("Activity sync unavailable; retaining bounded queue and backing off");
                  }

                  u6 var16 = ja.E(var7);
                  if (var16.M() == 401 || var16.M() == 403) {
                     this.W = (boolean)0;
                     this.j();
                     this.i = 0L;
                  } else if (var1 != null && (var16.M() == 400 || var16.M() == 409 || var16.M() == 413)) {
                     this.D.remove(B(var1, "sessionId"));
                     if (this.Q != null && this.Q.I.equals(B(var1, "sessionId"))) {
                        this.Q = null;
                     }

                     this.Z = var9 + 900000L;
                     this.l.accept("Activity snapshot rejected; collection paused before retrying consent");
                     this.i = 0L;
                  }
               } else {
                  try {
                     this.S(var6x, var9);
                     if (var1 != null && (var6x.get("accepted").getAsBoolean() || "STALE_SNAPSHOT".equals(B(var6x, "reason")))) {
                        String var11 = B(var1, "sessionId");
                        JsonObject var12 = this.D.get(var11);
                        if (var12 != null && var12.equals(var1)) {
                           this.D.remove(var11);
                        }
                     }

                     if (this.I > 0) {
                        this.l.accept("Activity sync recovered");
                     }

                     this.I = 0;
                  } catch (RuntimeException var14) {
                     this.i = 0L;
                     this.Z = var9 + 60000L;
                     this.l.accept("Invalid activity consent response; collection suspended");
                  }
               }
            }
         }
      });
   }

   private void S(JsonObject var1, long var2) {
      String var4 = B(var1, "ownerId");
      if (!var4.matches("[0-9]{17,20}")) {
         throw new IllegalArgumentException();
      }

      int var5 = var1.getAsJsonPrimitive("enabled").isBoolean() && var1.get("enabled").getAsBoolean() ? 1 : 0;
      long var6 = Instant.parse(B(var1, "serverTime")).toEpochMilli();
      String var8 = var5 != 0 ? B(var1, "collectionStartsAt") : "";
      long var9 = var5 != 0 ? Instant.parse(var8).toEpochMilli() : 0L;
      if (var5 != 0 && var9 > var6) {
         throw new IllegalArgumentException();
      }

      if (!this.J.isEmpty() && !this.J.equals(var4)) {
         this.D.clear();
         this.Q = null;
         this.d.clear();
         this.E = null;
         this.T = null;
      }

      this.J = var4;
      if (var5 == 0) {
         if (this.W || !this.D.isEmpty()) {
            this.l.accept("Dashboard tracking off; queued gameplay erased");
         }

         this.D.clear();
         this.Q = null;
         this.d.clear();
         this.E = null;
         this.T = null;
      } else if (!this.F.equals(var8)) {
         this.D.values().removeIf(var2x -> Instant.parse(B(var2x, "startedAt")).toEpochMilli() < var9);
         if (this.Q != null && this.Q.Z < var9) {
            this.Q = null;
            this.d.clear();
         }

         if (!this.W) {
            this.l.accept("Dashboard tracking on; collecting private gameplay activity");
         }
      }

      this.F = var8;
      this.W = (boolean)var5;
      this.i = var2 + 120000L;
      this.s = var6;
      this.y = var2;
   }

   public synchronized void W(UUID var1, boolean var2, long var3) {
      if (this.Q != null && this.W && var3 < this.i) {
         this.d.remove(var1);
         if (var2) {
            this.d.put(var1, var3);
            if (this.d.size() > 128) {
               this.d.remove(this.d.keySet().iterator().next());
            }
         }
      }
   }

   public synchronized void x(UUID var1, long var2) {
      Long var4 = this.d.remove(var1);
      if (this.Q != null && this.W && var2 < this.i && var4 != null && var2 >= var4 && var2 - var4 <= 15000L) {
         this.Q.z = Math.min(100000, this.Q.z + 1);
         this.l.accept("Server-attributed player kill queued for next activity snapshot");
      }
   }

   public synchronized void i() {
      this.d.clear();
   }

   public synchronized void G() {
      this.j();
      this.z = (boolean)0;
      this.E = null;
      this.T = null;
   }

   private void j() {
      this.H(true);
   }

   private void H(boolean var1) {
      if (this.Q != null && (this.Q.O >= 1000L || this.Q.z > 0)) {
         this.D.put(this.Q.I, this.Q.s());
         if (this.D.size() > 32) {
            this.D.remove(this.D.keySet().iterator().next());
            this.l.accept("Activity queue full; oldest unsent session discarded");
         }
      }

      this.Q = null;
      if (var1) {
         this.d.clear();
      }

      this.z = (boolean)0;
   }

   private void Y() {
      this.R++;
      this.A++;
      this.D.clear();
      this.Q = null;
      this.d.clear();
      this.J = "";
      this.F = "";
      this.W = (boolean)0;
      this.x = (boolean)0;
      this.i = 0L;
      this.Z = 0L;
      this.I = 0;
      this.z = (boolean)0;
      this.U = -1L;
      this.E = null;
      this.T = null;
   }

   public synchronized long R() {
      return this.R;
   }

   public synchronized JsonObject x() {
      if (this.o != null && this.p.isEmpty()) {
         return this.o.deepCopy();
      }

      JsonObject var1 = new JsonObject();
      var1.addProperty("format", 1);
      var1.addProperty("scope", this.p);
      var1.addProperty("owner", this.J);
      JsonArray var2 = new JsonArray();
      this.D.values().forEach(var1x -> var2.add(var1x.deepCopy()));
      if (this.Q != null && (this.Q.O >= 1000L || this.Q.z > 0)) {
         if (var2.size() == 32) {
            var2.remove(0);
         }

         var2.add(this.Q.s());
      }

      var1.add("pending", var2);
      return var1;
   }

   static String B(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : "";
   }

   public static String F(String var0) {
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

   static void a(JsonObject var0, long var1) {
      UUID.fromString(B(var0, "sessionId"));
      if (var0.has("connectionId")) {
         UUID.fromString(B(var0, "connectionId"));
      }

      if (F(B(var0, "server")) == null) {
         throw new IllegalArgumentException();
      }

      long var3 = Instant.parse(B(var0, "startedAt")).toEpochMilli();
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
      final String I = UUID.randomUUID().toString();
      final String G;
      final String i;
      final long Z;
      long O;
      int z;

      u(String var1, long var2, String var4) {
         this.G = var1;
         this.Z = var2;
         this.i = var4 == null ? this.I : var4;
      }

      JsonObject s() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("sessionId", this.I);
         var1.addProperty("server", this.G);
         var1.addProperty("connectionId", this.i);
         var1.addProperty("startedAt", Instant.ofEpochMilli(this.Z).toString());
         var1.addProperty("playedSeconds", this.O / 1000L);
         var1.addProperty("kills", this.z);
         return var1;
      }
   }

   public interface y {
      CompletableFuture<JsonObject> W(String var1, JsonObject var2);
   }
}
