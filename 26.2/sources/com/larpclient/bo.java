package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

public final class bo {
   private static Object F;
   private static final Map<BlockPos, Long> p = new HashMap<>();

   private static void U() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != F) {
         F = var0;
         p.clear();
      }

      long var1 = System.currentTimeMillis();
      p.values().removeIf(var2 -> var2 < var1);
   }

   public static void W(BlockPos var0) {
      U();
      p.put(var0.immutable(), System.currentTimeMillis() + 10000L);
   }

   public static void b() {
      U();
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null && !p.isEmpty()) {
         for (Entity var2 : var0.level.entitiesForRendering()) {
            if (var2 instanceof ItemEntity var3) {
               b(var3);
            }
         }
      }
   }

   public static void b(ItemEntity var0) {
      U();
      if (var0 != null && b8.k().K(var0.getItem().getItem()) != null) {
         for (BlockPos var2 : p.keySet()) {
            if (var0.distanceToSqr(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5) < 4.0) {
               gz.k(var0.getItem(), true);
               return;
            }
         }
      }
   }
}
