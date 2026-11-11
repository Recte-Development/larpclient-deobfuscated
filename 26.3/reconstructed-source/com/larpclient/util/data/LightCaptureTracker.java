package com.larpclient.util.data;

import com.larpclient.util.misc.LarpLog;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents.AfterClientLevelChange;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacketData;
import net.minecraft.world.level.ChunkPos;

public final class LightCaptureTracker {
   private static final LightLevelCache tracker = new LightLevelCache();
   private static ClientLevel level;
   private static boolean registered;

   public static void register() {
      if (!registered) {
         registered = (boolean)1;
         ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((AfterClientLevelChange)(var0, var1) -> getTracker(var1));
         ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var0, var1) -> {
            if (var0 == level) {
               tracker.removeChunk(var1.getPos().pack());
            }
         });
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> {
            tracker.clear();
            level = null;
         });
      }
   }

   public static LightLevelCache getTracker(ClientLevel var0) {
      if (var0 != level) {
         tracker.clear();
         level = var0;
      }

      return tracker;
   }

   public static void onLightUpdate(ClientLevel var0, int var1, int var2, ClientboundLightUpdatePacketData var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var0 != null && var4.level == var0 && var4.player != null && var4.isSameThread()) {
         try {
            ChunkPos var5 = var4.player.chunkPosition();
            LightLevelCache var6 = getTracker(var0);
            var6.retainChunks(
               var2x -> {
                  ChunkPos var4x = ChunkPos.unpack(var2x);
                  return Math.abs((long)var4x.x() - var5.x()) <= 12L
                     && Math.abs((long)var4x.z() - var5.z()) <= 12L
                     && var0.getChunkSource().getChunkNow(var4x.x(), var4x.z()) != null;
               }
            );
            if (Math.abs((long)var1 - var5.x()) > 12L || Math.abs((long)var2 - var5.z()) > 12L || var0.getChunkSource().getChunkNow(var1, var2) == null) {
               return;
            }

            var6.update(var1, var2, (var0.getMinY() >> 4) - 1, var3.blockYMask(), var3.emptyBlockYMask(), var3.blockUpdates());
         } catch (RuntimeException var7) {
            LarpLog.errorOnce("BaseFinding", "Received light capture failed; ordinary block scans remain available", var7);
         }
      }
   }

   private LightCaptureTracker() {
   }
}
