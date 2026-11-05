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

public final class xa {
   public static final int s = 200;
   private static final DateTimeFormatter X = DateTimeFormatter.ofPattern("HH:mm:ss");
   private static final Object o = new Object();
   private static final ArrayDeque<String> W = new ArrayDeque<>();
   private static final Map<ItemEntity, Boolean> O = new WeakHashMap<>();
   private static long t;

   private xa() {
   }

   public static void K(int var0, int var1, int var2) {
      t("CARD", var0, var1, var2);
   }

   public static void t(String var0, int var1, int var2, int var3) {
      R(var0, var1, var2, var3, false);
   }

   public static void R(String var0, int var1, int var2, int var3, boolean var4) {
      if (var1 >= 1 && var2 >= 1 && var3 >= 0) {
         String var5 = z(var0);
         String var6 = U(y(var0, var1), y(var0, var2), true, var4);
         S("[" + LocalDateTime.now().format(X) + "] [" + var5 + "] " + var6 + "  (map " + var3 + ")");
      }
   }

   public static void q(String var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         S("[" + LocalDateTime.now().format(X) + "] [" + z(var0) + "] " + var1.replace('\r', ' ').replace('\n', ' '));
      }
   }

   public static String y(String var0, int var1) {
      return "BLACKJACK".equals(z(var0)) && var1 == 1 ? "Ace" : Integer.toString(var1);
   }

   public static String U(String var0, String var1, boolean var2, boolean var3) {
      if (!var2) {
         return "Unrigged - Real " + var0;
      } else if (var3 && !var0.equals(var1)) {
         return "[visual] " + (var0.equals("Ace") ? "A" : var0) + " -> " + (var1.equals("Ace") ? "A" : var1);
      } else {
         return var0.equals(var1) ? "Real " + var0 : "Real " + var0 + " -> " + (var3 ? "Spoofed " : "Rigged ") + var1;
      }
   }

   public static void J(Minecraft var0, ep.o var1, String var2, ItemEntity var3, String var4, String var5, boolean var6) {
      if (var0 != null && var0.level != null && var3.tickCount <= 20 && !var3.isRemoved()) {
         ep var7 = ep.H();
         if (!var7.O()) {
            BlockPos var8 = var7.n(var1);
            if (var8 != null) {
               BlockState var9 = var0.level.getBlockState(var8);
               if (var9.getBlock() instanceof DispenserBlock) {
                  Direction var10 = (Direction)var9.getValue(DispenserBlock.FACING);
                  Vec3 var11 = Vec3.atCenterOf(var8).add(var10.getStepX() * 0.7, var10.getStepY() * 0.7, var10.getStepZ() * 0.7);
                  if (!(var3.distanceToSqr(var11) > 2.25) && O.put(var3, true) == null) {
                     q(var2, U(var4, var5, var6, true));
                  }
               }
            }
         }
      }
   }

   public static void i(String var0) {
      if (var0 != null && !var0.isBlank()) {
         mt.U("Rig", "{}", var0);
      }
   }

   public static List<String> E() {
      synchronized (o) {
         return new ArrayList<>(W);
      }
   }

   public static long B() {
      synchronized (o) {
         return t;
      }
   }

   public static void b() {
      synchronized (o) {
         W.clear();
         t++;
      }
   }

   private static String z(String var0) {
      return var0 != null && !var0.isBlank() ? var0.trim().toUpperCase(Locale.ROOT).replace('_', ' ').replaceAll("[^A-Z0-9 /-]", "") : "GAME";
   }

   private static void S(String var0) {
      synchronized (o) {
         W.addLast(var0);

         while (W.size() > 200) {
            W.removeFirst();
         }

         t++;
      }

      mt.K("Rig", "{}", var0);
   }
}
