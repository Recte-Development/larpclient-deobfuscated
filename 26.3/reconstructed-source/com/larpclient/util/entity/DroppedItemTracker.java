package com.larpclient.util.entity;

import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.util.world.FakeSpawnerController;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

public final class DroppedItemTracker {
   private static Object trackedLevel;
   private static final Map<BlockPos, Long> expiryByPos = new HashMap<>();

   private static void pruneExpired() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != trackedLevel) {
         trackedLevel = var0;
         expiryByPos.clear();
      }

      long var1 = System.currentTimeMillis();
      expiryByPos.values().removeIf(var2 -> var2 < var1);
   }

   public static void trackPosition(BlockPos var0) {
      pruneExpired();
      expiryByPos.put(var0.immutable(), System.currentTimeMillis() + 10000L);
   }

   public static void scanItems() {
      pruneExpired();
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null && !expiryByPos.isEmpty()) {
         for (Entity var2 : var0.level.entitiesForRendering()) {
            if (var2 instanceof ItemEntity var3) {
               checkItem(var3);
            }
         }
      }
   }

   public static void checkItem(ItemEntity var0) {
      pruneExpired();
      if (var0 != null && FakeSpawnerController.getInstance().writePilesTo(var0.getItem().getItem()) != null) {
         for (BlockPos var2 : expiryByPos.keySet()) {
            if (var0.distanceToSqr(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5) < 4.0) {
               ItemStackMarkers.setRealPurchase(var0.getItem(), true);
               return;
            }
         }
      }
   }
}
