package com.larpclient.util.data;

import com.google.gson.JsonObject;
import com.larpclient.core.PublicApiAccess;
import com.larpclient.network.ApiException;
import com.larpclient.network.ServiceStatusClient;
import com.larpclient.util.misc.LarpLog;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.player.Player;

public final class ActivityTracker2 {
   private static ActivityTracker2 instance;
   private final ActivityQueueStore store;
   private final CompletableFuture<JsonObject> loadFuture;
   private ActivityTracker tracker;
   private long nextTickAt;
   private long nextSaveAt;
   private long lastVersion = -1L;
   private Object lastLevel;
   private boolean closed;

   private ActivityTracker2() {
      Minecraft var1 = Minecraft.getInstance();
      this.store = new ActivityQueueStore(
         FabricLoader.getInstance().getConfigDir().resolve("larp-client/activity.json"),
         var1x -> var1.execute(() -> LarpLog.errorOnce("Activity", var1x, null))
      );
      this.loadFuture = this.store.load();
   }

   public static void init() {
      if (instance == null) {
         instance = new ActivityTracker2();
      }
   }

   private static long nowMillis() {
      return System.nanoTime() / 1000000L;
   }

   public static void tick(Minecraft var0) {
      if (instance != null) {
         instance.update(var0);
      }
   }

   private void update(Minecraft var1) {
      if (!this.closed && this.loadFuture.isDone()) {
         long var2 = nowMillis();
         if (this.tracker == null) {
            this.tracker = new ActivityTracker(
               (var1x, var2x) -> this.store
                  .<CompletableFuture<JsonObject>>submit(
                     () -> {
                        PublicApiAccess var2xx = PublicApiAccess.anonymous();
                        if (!var2xx.getToken().equals(var1x)) {
                           throw new ApiException(401, "UNAUTHENTICATED");
                        } else {
                           return ServiceStatusClient.iNSTANCE
                              .api
                              .requestWithAccess(
                                 var2x == null ? "GET" : "POST", var2x == null ? "/api/activity/preferences" : "/api/activity", var2x, true, null, var2xx
                              );
                        }
                     }
                  )
                  .thenCompose(var0 -> (CompletionStage<JsonObject>)var0),
               var0 -> LarpLog.debug("Activity", var0),
               this.loadFuture.join()
            );
         }

         if (var2 >= this.nextTickAt) {
            this.nextTickAt = var2 + 1000L;
            if (this.lastLevel != var1.level) {
               this.lastLevel = var1.level;
               this.tracker.clearHits();
            }

            ServerData var4 = var1.getCurrentServer();
            String var5 = var1.player != null && var1.level != null && !var1.isLocalServer() && var4 != null ? var4.ip : null;
            this.tracker.update("", var5, var5 != null && !var1.isPaused(), var2, System.currentTimeMillis());
            if (var2 >= this.nextSaveAt || this.lastVersion != this.tracker.getSyncCount()) {
               this.lastVersion = this.tracker.getSyncCount();
               this.nextSaveAt = var2 + 30000L;
               this.store.checkpoint(this.tracker.saveState());
            }
         }
      }
   }

   public static void onDamage(ClientboundDamageEventPacket var0) {
      ActivityTracker2 var1 = instance;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.tracker != null && var2.level != null && var2.player != null) {
         if (var1.lastLevel != var2.level) {
            var1.lastLevel = var2.level;
            var1.tracker.clearHits();
         }

         if (var2.level.getEntity(var0.entityId()) instanceof Player var4 && var4 != var2.player) {
            var1.tracker.trackHit(var4.getUUID(), var0.sourceCauseId() == var2.player.getId(), nowMillis());
         }
      }
   }

   public static void onEntityEvent(ClientboundEntityEventPacket var0) {
      ActivityTracker2 var1 = instance;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 != null && var1.tracker != null && var2.level != null && var0.getEventId() == 3) {
         if (var1.lastLevel != var2.level) {
            var1.lastLevel = var2.level;
            var1.tracker.clearHits();
         }

         if (var0.getEntity(var2.level) instanceof Player var3 && var3 != var2.player) {
            var1.tracker.recordKill(var3.getUUID(), nowMillis());
         }
      }
   }

   public static void flush() {
      ActivityTracker2 var0 = instance;
      if (var0 != null && var0.tracker != null) {
         var0.tracker.disconnect();
         var0.store.checkpoint(var0.tracker.saveState());
         var0.lastLevel = null;
      }
   }

   public static void shutdown() {
      ActivityTracker2 var0 = instance;
      if (var0 != null) {
         flush();
         var0.closed = (boolean)1;
         var0.store.close();
      }
   }
}
