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

public final class hh {
   public static final int i = 200;
   private static final DateTimeFormatter v = DateTimeFormatter.ofPattern("HH:mm:ss");
   private static final Object K = new Object();
   private static final ArrayDeque<String> G = new ArrayDeque<>();
   private static final Map<ItemEntity, Boolean> a = new WeakHashMap<>();
   private static long A;

   private hh() {
   }

   public static void d(int var0, int var1, int var2) {
      s("CARD", var0, var1, var2);
   }

   public static void s(String var0, int var1, int var2, int var3) {
      B(var0, var1, var2, var3, false);
   }

   public static void B(String var0, int var1, int var2, int var3, boolean var4) {
      if (var1 >= 1 && var2 >= 1 && var3 >= 0) {
         String var5 = C(var0);
         String var6 = e(M(var0, var1), M(var0, var2), true, var4);
         k("[" + LocalDateTime.now().format(v) + "] [" + var5 + "] " + var6 + "  (map " + var3 + ")");
      }
   }

   public static void K(String var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         k("[" + LocalDateTime.now().format(v) + "] [" + C(var0) + "] " + var1.replace('\r', ' ').replace('\n', ' '));
      }
   }

   public static String M(String var0, int var1) {
      return "BLACKJACK".equals(C(var0)) && var1 == 1 ? "Ace" : Integer.toString(var1);
   }

   public static String e(String var0, String var1, boolean var2, boolean var3) {
      if (!var2) {
         return "Unrigged - Real " + var0;
      } else if (var3 && !var0.equals(var1)) {
         return "[visual] " + (var0.equals("Ace") ? "A" : var0) + " -> " + (var1.equals("Ace") ? "A" : var1);
      } else {
         return var0.equals(var1) ? "Real " + var0 : "Real " + var0 + " -> " + (var3 ? "Spoofed " : "Rigged ") + var1;
      }
   }

   public static void j(Minecraft var0, f9.b var1, String var2, ItemEntity var3, String var4, String var5, boolean var6) {
      if (var0 != null && var0.level != null && var3.tickCount <= 20 && !var3.isRemoved()) {
         f9 var7 = f9.W();
         if (!var7.Y()) {
            BlockPos var8 = var7.R(var1);
            if (var8 != null) {
               BlockState var9 = var0.level.getBlockState(var8);
               if (var9.getBlock() instanceof DispenserBlock) {
                  Direction var10 = (Direction)var9.getValue(DispenserBlock.FACING);
                  Vec3 var11 = Vec3.atCenterOf(var8).add(var10.getStepX() * 0.7, var10.getStepY() * 0.7, var10.getStepZ() * 0.7);
                  if (!(var3.distanceToSqr(var11) > 2.25) && a.put(var3, true) == null) {
                     K(var2, e(var4, var5, var6, true));
                  }
               }
            }
         }
      }
   }

   public static void L(String var0) {
      if (var0 != null && !var0.isBlank()) {
         r4.g("Rig", "{}", var0);
      }
   }

   public static List<String> a() {
      synchronized (K) {
         return new ArrayList<>(G);
      }
   }

   public static long O() {
      synchronized (K) {
         return A;
      }
   }

   public static void g() {
      synchronized (K) {
         G.clear();
         A++;
      }
   }

   private static String C(String var0) {
      return var0 != null && !var0.isBlank() ? var0.trim().toUpperCase(Locale.ROOT).replace('_', ' ').replaceAll("[^A-Z0-9 /-]", "") : "GAME";
   }

   private static void k(String var0) {
      synchronized (K) {
         G.addLast(var0);

         while (G.size() > 200) {
            G.removeFirst();
         }

         A++;
      }

      r4.V("Rig", "{}", var0);
   }
}
