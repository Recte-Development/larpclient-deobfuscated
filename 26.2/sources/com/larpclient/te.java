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

public final class te {
   public static final int X = 200;
   private static final DateTimeFormatter I = DateTimeFormatter.ofPattern("HH:mm:ss");
   private static final Object G = new Object();
   private static final ArrayDeque<String> m = new ArrayDeque<>();
   private static final Map<ItemEntity, Boolean> U = new WeakHashMap<>();
   private static long S;

   private te() {
   }

   public static void h(int var0, int var1, int var2) {
      H("CARD", var0, var1, var2);
   }

   public static void H(String var0, int var1, int var2, int var3) {
      j(var0, var1, var2, var3, false);
   }

   public static void j(String var0, int var1, int var2, int var3, boolean var4) {
      if (var1 >= 1 && var2 >= 1 && var3 >= 0) {
         String var5 = G(var0);
         String var6 = I(K(var0, var1), K(var0, var2), true, var4);
         T("[" + LocalDateTime.now().format(I) + "] [" + var5 + "] " + var6 + "  (map " + var3 + ")");
      }
   }

   public static void Z(String var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         T("[" + LocalDateTime.now().format(I) + "] [" + G(var0) + "] " + var1.replace('\r', ' ').replace('\n', ' '));
      }
   }

   public static String K(String var0, int var1) {
      return "BLACKJACK".equals(G(var0)) && var1 == 1 ? "Ace" : Integer.toString(var1);
   }

   public static String I(String var0, String var1, boolean var2, boolean var3) {
      if (!var2) {
         return "Unrigged - Real " + var0;
      } else if (var3 && !var0.equals(var1)) {
         return "[visual] " + (var0.equals("Ace") ? "A" : var0) + " -> " + (var1.equals("Ace") ? "A" : var1);
      } else {
         return var0.equals(var1) ? "Real " + var0 : "Real " + var0 + " -> " + (var3 ? "Spoofed " : "Rigged ") + var1;
      }
   }

   public static void M(Minecraft var0, gj.d var1, String var2, ItemEntity var3, String var4, String var5, boolean var6) {
      if (var0 != null && var0.level != null && var3.tickCount <= 20 && !var3.isRemoved()) {
         gj var7 = gj.c();
         if (!var7.F()) {
            BlockPos var8 = var7.q(var1);
            if (var8 != null) {
               BlockState var9 = var0.level.getBlockState(var8);
               if (var9.getBlock() instanceof DispenserBlock) {
                  Direction var10 = (Direction)var9.getValue(DispenserBlock.FACING);
                  Vec3 var11 = Vec3.atCenterOf(var8).add(var10.getStepX() * 0.7, var10.getStepY() * 0.7, var10.getStepZ() * 0.7);
                  if (!(var3.distanceToSqr(var11) > 2.25) && U.put(var3, true) == null) {
                     Z(var2, I(var4, var5, var6, true));
                  }
               }
            }
         }
      }
   }

   public static void R(String var0) {
      if (var0 != null && !var0.isBlank()) {
         h.U("Rig", "{}", var0);
      }
   }

   public static List<String> h() {
      synchronized (G) {
         return new ArrayList<>(m);
      }
   }

   public static long W() {
      synchronized (G) {
         return S;
      }
   }

   public static void x() {
      synchronized (G) {
         m.clear();
         S++;
      }
   }

   private static String G(String var0) {
      return var0 != null && !var0.isBlank() ? var0.trim().toUpperCase(Locale.ROOT).replace('_', ' ').replaceAll("[^A-Z0-9 /-]", "") : "GAME";
   }

   private static void T(String var0) {
      synchronized (G) {
         m.addLast(var0);

         while (m.size() > 200) {
            m.removeFirst();
         }

         S++;
      }

      h.A("Rig", "{}", var0);
   }
}
