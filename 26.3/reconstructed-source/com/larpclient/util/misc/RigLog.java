package com.larpclient.util.misc;

import com.larpclient.util.world.DispenserRegistry;
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

public final class RigLog {
   public static final int maxEntries = 200;
   private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
   private static final Object lock = new Object();
   private static final ArrayDeque<String> entries = new ArrayDeque<>();
   private static final Map<ItemEntity, Boolean> loggedDrops = new WeakHashMap<>();
   private static long version;

   private RigLog() {
   }

   public static void logCard(int var0, int var1, int var2) {
      logRig("CARD", var0, var1, var2);
   }

   public static void logRig(String var0, int var1, int var2, int var3) {
      logCard(var0, var1, var2, var3, false);
   }

   public static void logCard(String var0, int var1, int var2, int var3, boolean var4) {
      if (var1 >= 1 && var2 >= 1 && var3 >= 0) {
         String var5 = normalizeGame(var0);
         String var6 = describeRig(logRig(var0, var1), logRig(var0, var2), true, var4);
         append("[" + LocalDateTime.now().format(timeFormat) + "] [" + var5 + "] " + var6 + "  (map " + var3 + ")");
      }
   }

   public static void logMessage(String var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         append("[" + LocalDateTime.now().format(timeFormat) + "] [" + normalizeGame(var0) + "] " + var1.replace('\r', ' ').replace('\n', ' '));
      }
   }

   public static String logRig(String var0, int var1) {
      return "BLACKJACK".equals(normalizeGame(var0)) && var1 == 1 ? "Ace" : Integer.toString(var1);
   }

   public static String describeRig(String var0, String var1, boolean var2, boolean var3) {
      if (!var2) {
         return "Unrigged - Real " + var0;
      } else if (var3 && !var0.equals(var1)) {
         return "[visual] " + (var0.equals("Ace") ? "A" : var0) + " -> " + (var1.equals("Ace") ? "A" : var1);
      } else {
         return var0.equals(var1) ? "Real " + var0 : "Real " + var0 + " -> " + (var3 ? "Spoofed " : "Rigged ") + var1;
      }
   }

   public static void logDispenserDrop(Minecraft var0, DispenserRegistry.GameType var1, String var2, ItemEntity var3, String var4, String var5, boolean var6) {
      if (var0 != null && var0.level != null && var3.tickCount <= 20 && !var3.isRemoved()) {
         DispenserRegistry var7 = DispenserRegistry.getInstance();
         if (!var7.isLookingAtUnassigned()) {
            BlockPos var8 = var7.onDispenserUpdate(var1);
            if (var8 != null) {
               BlockState var9 = var0.level.getBlockState(var8);
               if (var9.getBlock() instanceof DispenserBlock) {
                  Direction var10 = (Direction)var9.getValue(DispenserBlock.FACING);
                  Vec3 var11 = Vec3.atCenterOf(var8).add(var10.getStepX() * 0.7, var10.getStepY() * 0.7, var10.getStepZ() * 0.7);
                  if (!(var3.distanceToSqr(var11) > 2.25) && loggedDrops.put(var3, true) == null) {
                     logMessage(var2, describeRig(var4, var5, var6, true));
                  }
               }
            }
         }
      }
   }

   public static void logDebug(String var0) {
      if (var0 != null && !var0.isBlank()) {
         LarpLog.debug("Rig", "{}", var0);
      }
   }

   public static List<String> getEntries() {
      synchronized (lock) {
         return new ArrayList<>(entries);
      }
   }

   public static long getVersion() {
      synchronized (lock) {
         return version;
      }
   }

   public static void clear() {
      synchronized (lock) {
         entries.clear();
         version++;
      }
   }

   private static String normalizeGame(String var0) {
      return var0 != null && !var0.isBlank() ? var0.trim().toUpperCase(Locale.ROOT).replace('_', ' ').replaceAll("[^A-Z0-9 /-]", "") : "GAME";
   }

   private static void append(String var0) {
      synchronized (lock) {
         entries.addLast(var0);

         while (entries.size() > 200) {
            entries.removeFirst();
         }

         version++;
      }

      LarpLog.info("Rig", "{}", var0);
   }
}
