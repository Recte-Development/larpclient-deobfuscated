package com.larpclient.module;

import com.larpclient.te;
import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class w {
   private static final long h = 30000000000L;
   private static final long o = 1200000000L;
   private static final long X = 1500000000L;
   private static final int e = -13180806;
   private static final int W = -2074034;
   private static w.v O;
   private static long c;
   private static long x;
   private static BlockPos s;
   private static long w;
   private static AbstractContainerMenu i;
   private static int v = -1;
   private static long Y;
   private static boolean F;

   private w() {
   }

   public static void r(String var0, Predicate<BlockState> var1, String var2, Function<BlockPos, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         V();
         O = new w.c(var0, var1, var2, var3);
         y(var4);
         var4.gui.setScreen(null);
      } else {
         com.larpclient.h.A("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static void q(String var0, Predicate<ItemStack> var1, String var2, Function<ItemStack, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         V();
         O = new w.u(var0, var1, var2, var3);
         y(var4);
         var4.gui.setScreen(new InventoryScreen(var4.player));
      } else {
         com.larpclient.h.A("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static boolean s() {
      return O != null;
   }

   private static void y(Minecraft var0) {
      long var1 = System.nanoTime();
      c = var1 + 30000000000L;
      x = var1;
      w(var0, O.prompt());
      com.larpclient.h.A("Rig", "Selection started: {}", O.prompt());
   }

   public static void V(String var0) {
      if (O != null) {
         O = null;
         w(Minecraft.getInstance(), "Selection " + var0);
         com.larpclient.h.A("Rig", "Selection {}", var0);
      }
   }

   private static void w(Minecraft var0, String var1) {
      if (var0.player != null) {
         var0.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private static void V() {
      if (!F) {
         F = (boolean)1;
         ClientTickEvents.END_CLIENT_TICK.register(w::z);
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> p());
      }
   }

   private static void z(Minecraft var0) {
      if (O != null) {
         if (var0.player != null && var0.level != null) {
            long var1 = System.nanoTime();
            if (var1 > c) {
               V("timed out");
            } else if (O instanceof w.c && var0.gui.screen() != null) {
               V("cancelled");
            } else if (O instanceof w.u && !(var0.gui.screen() instanceof AbstractContainerScreen)) {
               V("cancelled");
            } else {
               if (O instanceof w.c && var1 - x > 1500000000L) {
                  x = var1;
                  w(var0, O.prompt());
               }
            }
         } else {
            O = null;
         }
      }
   }

   private static AABB X(BlockPos var0) {
      return new AABB(var0.getX() - 0.002, var0.getY() - 0.002, var0.getZ() - 0.002, var0.getX() + 1.002, var0.getY() + 1.002, var0.getZ() + 1.002);
   }

   private static void p() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null) {
         if (O instanceof w.c var1 && var0.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            boolean var6 = var1.valid().test(var0.level.getBlockState(var2.getBlockPos()));
            Gizmos.cuboid(X(var2.getBlockPos()), GizmoStyle.stroke(var6 ? -13180806 : -2074034, 2.5F)).setAlwaysOnTop();
         }

         long var4 = System.nanoTime();
         if (s != null && var4 < w) {
            int var7 = (int)(110L * (w - var4) / 1.2E9);
            Gizmos.cuboid(X(s), GizmoStyle.strokeAndFill(-13180806, 2.5F, var7 << 24 | 3596410)).setAlwaysOnTop();
         } else {
            s = null;
         }
      }
   }

   public static boolean d(Minecraft var0) {
      if (!(O instanceof w.c var1 && var0.level != null)) {
         return false;
      } else if (var0.hitResult instanceof BlockHitResult var5 && var5.getType() == Type.BLOCK) {
         BlockPos var6 = var5.getBlockPos().immutable();
         if (!var1.valid().test(var0.level.getBlockState(var6))) {
            w(var0, var1.invalid());
            return true;
         } else {
            O = null;
            String var4 = var1.apply().apply(var6);
            s = var6;
            w = System.nanoTime() + 1200000000L;
            w(var0, var4);
            te.Z("Casino", var4);
            return true;
         }
      } else {
         w(var0, var1.prompt());
         return true;
      }
   }

   public static boolean a(Slot var0, AbstractContainerMenu var1) {
      if (O instanceof w.u var2 && var0 != null) {
         Minecraft var6 = Minecraft.getInstance();
         ItemStack var4 = var0.getItem();
         if (!var4.isEmpty() && var2.valid().test(var4)) {
            O = null;
            String var5 = var2.apply().apply(var4.copy());
            i = var1;
            v = var0.index;
            Y = System.nanoTime() + 1200000000L;
            w(var6, var5);
            te.Z("Casino", var5);
            return true;
         } else {
            w(var6, var2.invalid());
            return true;
         }
      } else {
         return false;
      }
   }

   public static int O(Slot var0, boolean var1, AbstractContainerMenu var2) {
      long var3 = System.nanoTime();
      if (v >= 0 && var3 >= Y) {
         v = -1;
         i = null;
      }

      if (v >= 0 && var2 == i && var0.index == v) {
         int var7 = (int)(150L * (Y - var3) / 1.2E9);
         return var7 << 24 | 3596410;
      }

      if (O instanceof w.u var5 && !var0.getItem().isEmpty()) {
         boolean var8 = var5.valid().test(var0.getItem());
         if (var1) {
            return var8 ? -2143887238 : -2132780466;
         } else {
            return var8 ? 808902778 : 0;
         }
      } else {
         return 0;
      }
   }

   private record c(String prompt, Predicate<BlockState> valid, String invalid, Function<BlockPos, String> apply) implements w.v {
   }

   private record u(String prompt, Predicate<ItemStack> valid, String invalid, Function<ItemStack, String> apply) implements w.v {
   }

   private sealed interface v permits w.c, w.u {
      String prompt();
   }
}
