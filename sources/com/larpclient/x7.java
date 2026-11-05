package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

public final class x7 {
   private static Object Z;
   private static final Map<BlockPos, Long> J = new HashMap<>();

   private static void x() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != Z) {
         Z = var0;
         J.clear();
      }

      long var1 = System.currentTimeMillis();
      J.values().removeIf(var2 -> var2 < var1);
   }

   public static void W(BlockPos var0) {
      x();
      J.put(var0.immutable(), System.currentTimeMillis() + 10000L);
   }

   public static void t() {
      x();
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null && !J.isEmpty()) {
         for (Entity var2 : var0.level.entitiesForRendering()) {
            if (var2 instanceof ItemEntity var3) {
               P(var3);
            }
         }
      }
   }

   public static void P(ItemEntity var0) {
      x();
      if (var0 != null && dr.z().x(var0.getItem().getItem()) != null) {
         for (BlockPos var2 : J.keySet()) {
            if (var0.distanceToSqr(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5) < 4.0) {
               h2.f(var0.getItem(), true);
               return;
            }
         }
      }
   }
}
