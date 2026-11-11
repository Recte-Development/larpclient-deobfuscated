package com.larpclient.util.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.network.ApiException;
import com.larpclient.network.ServiceStatusClient;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class ActivityTracker {
   public static final long sYNC_INTERVAL_MS = 60000L;
   public static final long cONSENT_TTL_MS = 120000L;
   public static final int mAX_PENDING = 32;
   private final ActivityTracker.ApiClient client;
   private final Consumer<String> logger;
   private final LinkedHashMap<String, JsonObject> pending = new LinkedHashMap<>();
   private final Map<UUID, Long> recentHits = new LinkedHashMap<>();
   private String scope = "";
   private String ownerId = "";
   private String collectionStartsAt = "";
   private boolean trackingEnabled;
   private boolean requestInFlight;
   private boolean wasActive;
   private long generation;
   private long lastTickTime = -1L;
   private long nextSyncAt;
   private long consentExpiresAt;
   private long consentLocalTime;
   private long consentServerTime;
   private int failureCount;
   private long syncCount;
   private ActivityTracker.Session currentSession;
   private String currentServer;
   private String connectionId;
   private JsonObject restoredState;

   public ActivityTracker(ActivityTracker.ApiClient var1, Consumer<String> var2, JsonObject var3) {
      this.client = var1;
      this.logger = var2;
      this.restoredState = var3;
   }

   public synchronized void update(String var1, String var2, boolean var3, long var4, long var6) {
      if (!Objects.equals(this.scope, var1)) {
         this.sendSnapshot();
         this.scope = var1;
         if (this.restoredState != null && var1.equals(getString(this.restoredState, "scope"))) {
            try {
               this.ownerId = getString(this.restoredState, "owner");
               JsonArray var8 = this.restoredState.getAsJsonArray("pending");
               if (var8.size() > 32) {
                  throw new IllegalArgumentException();
               }

               for (JsonElement var10 : var8) {
                  JsonObject var11 = var10.getAsJsonObject();
                  validateSnapshot(var11, var6);
                  this.pending.put(getString(var11, "sessionId"), var11.deepCopy());
               }
            } catch (RuntimeException var14) {
               this.pending.clear();
               this.ownerId = "";
               this.logger.accept("Invalid queued activity discarded");
            }
         }

         this.restoredState = null;
         this.logger.accept(var1.isEmpty() ? "Collection stopped: account unavailable" : "Checking dashboard consent");
      }

      if (var1.isEmpty()) {
         this.lastTickTime = var4;
      } else {
         int var15 = this.trackingEnabled && var4 < this.consentExpiresAt ? 1 : 0;
         String var16 = normalizeServer(var2);
         if (!Objects.equals(this.currentServer, var16)) {
            this.currentServer = null;
            this.connectionId = null;
         }

         long var17 = this.consentServerTime + Math.max(0L, var4 - this.consentLocalTime);
         int var12 = this.currentSession != null && var17 / 86400000L != this.currentSession.startedAt / 86400000L ? 1 : 0;
         if (this.currentSession != null && var15 != 0 && this.currentSession.server.equals(var16) && var3 && this.wasActive && this.lastTickTime >= 0L) {
            this.currentSession.playedMillis = this.currentSession.playedMillis + Math.max(0L, Math.min(2000L, var4 - this.lastTickTime));
         }

         if (this.currentSession != null && (var16 == null || !this.currentSession.server.equals(var16) || var15 == 0 || var12 != 0)) {
            int var13 = var15 != 0 && this.currentSession.server.equals(var16) ? 1 : 0;
            this.applyConsent(var13 == 0);
         }

         if (var15 != 0 && var16 != null && this.currentSession == null) {
            if (this.connectionId == null) {
               this.connectionId = UUID.randomUUID().toString();
               this.currentServer = var16;
            }

            this.currentSession = new ActivityTracker.Session(var16, var17, this.connectionId);
            if (var12 == 0) {
               this.recentHits.clear();
            }

            this.wasActive = (boolean)0;
         }

         this.wasActive = var3;
         this.lastTickTime = var4;
         if (var16 != null || !this.pending.isEmpty()) {
            if (var4 >= this.nextSyncAt && !this.requestInFlight) {
               JsonObject var18 = var15 != 0 ? this.nextSnapshot() : null;
               if (var18 != null || var15 == 0 || var16 != null || var4 + 60000L >= this.consentExpiresAt) {
                  this.sendSnapshot(var18, var4);
               }
            }
         }
      }
   }

   private JsonObject nextSnapshot() {
      if (!this.pending.isEmpty()) {
         return this.pending.values().iterator().next().deepCopy();
      } else {
         return this.currentSession != null ? this.currentSession.toJson() : null;
      }
   }

   private void sendSnapshot(JsonObject var1, long var2) {
      this.requestInFlight = (boolean)1;
      this.nextSyncAt = var2 + 60000L;
      long var4 = this.generation;

      CompletableFuture var6;
      try {
         var6 = this.client.post(this.scope, var1);
      } catch (RuntimeException var8) {
         var6 = CompletableFuture.failedFuture(var8);
      }

      var6.whenComplete((var6x, var7) -> {
         synchronized (this) {
            if (var4 == this.generation) {
               this.requestInFlight = (boolean)0;
               this.syncCount++;
               long var9 = Math.max(var2, this.lastTickTime);
               if (var7 != null) {
                  this.failureCount = Math.min(5, this.failureCount + 1);
                  this.nextSyncAt = var9 + Math.min(900000L, 60000L << this.failureCount - 1);
                  if (this.failureCount == 1) {
                     this.logger.accept("Activity sync unavailable; retaining bounded queue and backing off");
                  }

                  ApiException var16 = ServiceStatusClient.unwrapError(var7);
                  if (var16.getStatusCode() == 401 || var16.getStatusCode() == 403) {
                     this.trackingEnabled = (boolean)0;
                     this.endSession();
                     this.consentExpiresAt = 0L;
                  } else if (var1 != null && (var16.getStatusCode() == 400 || var16.getStatusCode() == 409 || var16.getStatusCode() == 413)) {
                     this.pending.remove(getString(var1, "sessionId"));
                     if (this.currentSession != null && this.currentSession.sessionId.equals(getString(var1, "sessionId"))) {
                        this.currentSession = null;
                     }

                     this.nextSyncAt = var9 + 900000L;
                     this.logger.accept("Activity snapshot rejected; collection paused before retrying consent");
                     this.consentExpiresAt = 0L;
                  }
               } else {
                  try {
                     this.applyConsent(var6x, var9);
                     if (var1 != null && (var6x.get("accepted").getAsBoolean() || "STALE_SNAPSHOT".equals(getString(var6x, "reason")))) {
                        String var11 = getString(var1, "sessionId");
                        JsonObject var12 = this.pending.get(var11);
                        if (var12 != null && var12.equals(var1)) {
                           this.pending.remove(var11);
                        }
                     }

                     if (this.failureCount > 0) {
                        this.logger.accept("Activity sync recovered");
                     }

                     this.failureCount = 0;
                  } catch (RuntimeException var14) {
                     this.consentExpiresAt = 0L;
                     this.nextSyncAt = var9 + 60000L;
                     this.logger.accept("Invalid activity consent response; collection suspended");
                  }
               }
            }
         }
      });
   }

   private void applyConsent(JsonObject var1, long var2) {
      String var4 = getString(var1, "ownerId");
      if (!var4.matches("[0-9]{17,20}")) {
         throw new IllegalArgumentException();
      }

      int var5 = var1.getAsJsonPrimitive("enabled").isBoolean() && var1.get("enabled").getAsBoolean() ? 1 : 0;
      long var6 = Instant.parse(getString(var1, "serverTime")).toEpochMilli();
      String var8 = var5 != 0 ? getString(var1, "collectionStartsAt") : "";
      long var9 = var5 != 0 ? Instant.parse(var8).toEpochMilli() : 0L;
      if (var5 != 0 && var9 > var6) {
         throw new IllegalArgumentException();
      }

      if (!this.ownerId.isEmpty() && !this.ownerId.equals(var4)) {
         this.pending.clear();
         this.currentSession = null;
         this.recentHits.clear();
         this.connectionId = null;
         this.currentServer = null;
      }

      this.ownerId = var4;
      if (var5 == 0) {
         if (this.trackingEnabled || !this.pending.isEmpty()) {
            this.logger.accept("Dashboard tracking off; queued gameplay erased");
         }

         this.pending.clear();
         this.currentSession = null;
         this.recentHits.clear();
         this.connectionId = null;
         this.currentServer = null;
      } else if (!this.collectionStartsAt.equals(var8)) {
         this.pending.values().removeIf(var2x -> Instant.parse(getString(var2x, "startedAt")).toEpochMilli() < var9);
         if (this.currentSession != null && this.currentSession.startedAt < var9) {
            this.currentSession = null;
            this.recentHits.clear();
         }

         if (!this.trackingEnabled) {
            this.logger.accept("Dashboard tracking on; collecting private gameplay activity");
         }
      }

      this.collectionStartsAt = var8;
      this.trackingEnabled = (boolean)var5;
      this.consentExpiresAt = var2 + 120000L;
      this.consentServerTime = var6;
      this.consentLocalTime = var2;
   }

   public synchronized void trackHit(UUID var1, boolean var2, long var3) {
      if (this.currentSession != null && this.trackingEnabled && var3 < this.consentExpiresAt) {
         this.recentHits.remove(var1);
         if (var2) {
            this.recentHits.put(var1, var3);
            if (this.recentHits.size() > 128) {
               this.recentHits.remove(this.recentHits.keySet().iterator().next());
            }
         }
      }
   }

   public synchronized void recordKill(UUID var1, long var2) {
      Long var4 = this.recentHits.remove(var1);
      if (this.currentSession != null && this.trackingEnabled && var2 < this.consentExpiresAt && var4 != null && var2 >= var4 && var2 - var4 <= 15000L) {
         this.currentSession.kills = Math.min(100000, this.currentSession.kills + 1);
         this.logger.accept("Server-attributed player kill queued for next activity snapshot");
      }
   }

   public synchronized void clearHits() {
      this.recentHits.clear();
   }

   public synchronized void disconnect() {
      this.endSession();
      this.wasActive = (boolean)0;
      this.connectionId = null;
      this.currentServer = null;
   }

   private void endSession() {
      this.applyConsent(true);
   }

   private void applyConsent(boolean var1) {
      if (this.currentSession != null && (this.currentSession.playedMillis >= 1000L || this.currentSession.kills > 0)) {
         this.pending.put(this.currentSession.sessionId, this.currentSession.toJson());
         if (this.pending.size() > 32) {
            this.pending.remove(this.pending.keySet().iterator().next());
            this.logger.accept("Activity queue full; oldest unsent session discarded");
         }
      }

      this.currentSession = null;
      if (var1) {
         this.recentHits.clear();
      }

      this.wasActive = (boolean)0;
   }

   private void sendSnapshot() {
      this.syncCount++;
      this.generation++;
      this.pending.clear();
      this.currentSession = null;
      this.recentHits.clear();
      this.ownerId = "";
      this.collectionStartsAt = "";
      this.trackingEnabled = (boolean)0;
      this.requestInFlight = (boolean)0;
      this.consentExpiresAt = 0L;
      this.nextSyncAt = 0L;
      this.failureCount = 0;
      this.wasActive = (boolean)0;
      this.lastTickTime = -1L;
      this.connectionId = null;
      this.currentServer = null;
   }

   public synchronized long getSyncCount() {
      return this.syncCount;
   }

   public synchronized JsonObject saveState() {
      if (this.restoredState != null && this.scope.isEmpty()) {
         return this.restoredState.deepCopy();
      }

      JsonObject var1 = new JsonObject();
      var1.addProperty("format", 1);
      var1.addProperty("scope", this.scope);
      var1.addProperty("owner", this.ownerId);
      JsonArray var2 = new JsonArray();
      this.pending.values().forEach(var1x -> var2.add(var1x.deepCopy()));
      if (this.currentSession != null && (this.currentSession.playedMillis >= 1000L || this.currentSession.kills > 0)) {
         if (var2.size() == 32) {
            var2.remove(0);
         }

         var2.add(this.currentSession.toJson());
      }

      var1.add("pending", var2);
      return var1;
   }

   static String getString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : "";
   }

   public static String normalizeServer(String var0) {
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

   static void validateSnapshot(JsonObject var0, long var1) {
      UUID.fromString(getString(var0, "sessionId"));
      if (var0.has("connectionId")) {
         UUID.fromString(getString(var0, "connectionId"));
      }

      if (normalizeServer(getString(var0, "server")) == null) {
         throw new IllegalArgumentException();
      }

      long var3 = Instant.parse(getString(var0, "startedAt")).toEpochMilli();
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

   public interface ApiClient {
      CompletableFuture<JsonObject> post(String var1, JsonObject var2);
   }

   private static final class Session {
      final String sessionId = UUID.randomUUID().toString();
      final String server;
      final String connectionId;
      final long startedAt;
      long playedMillis;
      int kills;

      Session(String var1, long var2, String var4) {
         this.server = var1;
         this.startedAt = var2;
         this.connectionId = var4 == null ? this.sessionId : var4;
      }

      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("sessionId", this.sessionId);
         var1.addProperty("server", this.server);
         var1.addProperty("connectionId", this.connectionId);
         var1.addProperty("startedAt", Instant.ofEpochMilli(this.startedAt).toString());
         var1.addProperty("playedSeconds", this.playedMillis / 1000L);
         var1.addProperty("kills", this.kills);
         return var1;
      }
   }
}
