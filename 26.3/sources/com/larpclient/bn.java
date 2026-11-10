package com.larpclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class bn {
   public static final int W = 200;
   private static final DateTimeFormatter R = DateTimeFormatter.ofPattern("HH:mm:ss");
   private static final Object c = new Object();
   private static final ArrayDeque<String> b = new ArrayDeque<>();
   private static final Map<ItemEntity, Boolean> Z = new WeakHashMap<>();
   private static long N;

   private bn() {
   }

   public static void N(int var0, int var1, int var2) {
      I("CARD", var0, var1, var2);
   }

   public static void I(String var0, int var1, int var2, int var3) {
      N(var0, var1, var2, var3, false);
   }

   public static void N(String var0, int var1, int var2, int var3, boolean var4) {
      if (var1 >= 1 && var2 >= 1 && var3 >= 0) {
         String var5 = E(var0);
         String var6 = W(I(var0, var1), I(var0, var2), true, var4);
         X("[" + LocalDateTime.now().format(R) + "] [" + var5 + "] " + var6 + "  (map " + var3 + ")");
      }
   }

   public static void j(String var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         X("[" + LocalDateTime.now().format(R) + "] [" + E(var0) + "] " + var1.replace('\r', ' ').replace('\n', ' '));
      }
   }

   public static String I(String var0, int var1) {
      return "BLACKJACK".equals(E(var0)) && var1 == 1 ? "Ace" : Integer.toString(var1);
   }

   public static String W(String var0, String var1, boolean var2, boolean var3) {
      if (!var2) {
         return "Unrigged - Real " + var0;
      } else if (var3 && !var0.equals(var1)) {
         return "[visual] " + (var0.equals("Ace") ? "A" : var0) + " -> " + (var1.equals("Ace") ? "A" : var1);
      } else {
         return var0.equals(var1) ? "Real " + var0 : "Real " + var0 + " -> " + (var3 ? "Spoofed " : "Rigged ") + var1;
      }
   }

   public static void y(Minecraft var0, hk.k var1, String var2, ItemEntity var3, String var4, String var5, boolean var6) {
      if (var0 != null && var0.level != null && var3.tickCount <= 20 && !var3.isRemoved()) {
         hk var7 = hk.e();
         if (!var7.q()) {
            BlockPos var8 = var7.f(var1);
            if (var8 != null) {
               BlockState var9 = var0.level.getBlockState(var8);
               if (var9.getBlock() instanceof DispenserBlock) {
                  Direction var10 = (Direction)var9.getValue(DispenserBlock.FACING);
                  Vec3 var11 = Vec3.atCenterOf(var8).add(var10.getStepX() * 0.7, var10.getStepY() * 0.7, var10.getStepZ() * 0.7);
                  if (!(var3.distanceToSqr(var11) > 2.25) && Z.put(var3, true) == null) {
                     j(var2, W(var4, var5, var6, true));
                  }
               }
            }
         }
      }
   }

   public static void b(String var0) {
      if (var0 != null && !var0.isBlank()) {
         _q.c("Rig", "{}", var0);
      }
   }

   public static List<String> p() {
      synchronized (c) {
         return new ArrayList<>(b);
      }
   }

   public static long h() {
      synchronized (c) {
         return N;
      }
   }

   public static void A() {
      synchronized (c) {
         b.clear();
         N++;
      }
   }

   private static String E(String var0) {
      return var0 != null && !var0.isBlank() ? var0.trim().toUpperCase(Locale.ROOT).replace('_', ' ').replaceAll("[^A-Z0-9 /-]", "") : "GAME";
   }

   private static void X(String var0) {
      synchronized (c) {
         b.addLast(var0);

         while (b.size() > 200) {
            b.removeFirst();
         }

         N++;
      }

      _q.z("Rig", "{}", var0);
   }
}
