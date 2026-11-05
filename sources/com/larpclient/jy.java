package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

public final class jy {
   private static Object g;
   private static final Map<BlockPos, Long> a = new HashMap<>();

   private static void W() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != g) {
         g = var0;
         a.clear();
      }

      long var1 = System.currentTimeMillis();
      a.values().removeIf(var2 -> var2 < var1);
   }

   public static void M(BlockPos var0) {
      W();
      a.put(var0.immutable(), System.currentTimeMillis() + 10000L);
   }

   public static void G() {
      W();
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null && !a.isEmpty()) {
         for (Entity var2 : var0.level.entitiesForRendering()) {
            if (var2 instanceof ItemEntity var3) {
               w(var3);
            }
         }
      }
   }

   public static void w(ItemEntity var0) {
      W();
      if (var0 != null && lj.j().u(var0.getItem().getItem()) != null) {
         for (BlockPos var2 : a.keySet()) {
            if (var0.distanceToSqr(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5) < 4.0) {
               ja.x(var0.getItem(), true);
               return;
            }
         }
      }
   }
}
