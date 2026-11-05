package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.xa;
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

public final class d {
   private static final long k = 30000000000L;
   private static final long d = 1200000000L;
   private static final long U = 1500000000L;
   private static final int L = -13180806;
   private static final int R = -2074034;
   private static d.j O;
   private static long h;
   private static long I;
   private static BlockPos S;
   private static long X;
   private static AbstractContainerMenu t;
   private static int G = -1;
   private static long E;
   private static boolean e;

   private d() {
   }

   public static void X(String var0, Predicate<BlockState> var1, String var2, Function<BlockPos, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         l();
         O = new d.s(var0, var1, var2, var3);
         m(var4);
         var4.gui.setScreen(null);
      } else {
         mt.K("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static void l(String var0, Predicate<ItemStack> var1, String var2, Function<ItemStack, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         l();
         O = new d.f(var0, var1, var2, var3);
         m(var4);
         var4.gui.setScreen(new InventoryScreen(var4.player));
      } else {
         mt.K("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static boolean B() {
      return O != null;
   }

   private static void m(Minecraft var0) {
      long var1 = System.nanoTime();
      h = var1 + 30000000000L;
      I = var1;
      S(var0, O.prompt());
      mt.K("Rig", "Selection started: {}", O.prompt());
   }

   public static void i(String var0) {
      if (O != null) {
         O = null;
         S(Minecraft.getInstance(), "Selection " + var0);
         mt.K("Rig", "Selection {}", var0);
      }
   }

   private static void S(Minecraft var0, String var1) {
      if (var0.player != null) {
         var0.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private static void l() {
      if (!e) {
         e = (boolean)1;
         ClientTickEvents.END_CLIENT_TICK.register(d::J);
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> C());
      }
   }

   private static void J(Minecraft var0) {
      if (O != null) {
         if (var0.player != null && var0.level != null) {
            long var1 = System.nanoTime();
            if (var1 > h) {
               i("timed out");
            } else if (O instanceof d.s && var0.gui.screen() != null) {
               i("cancelled");
            } else if (O instanceof d.f && !(var0.gui.screen() instanceof AbstractContainerScreen)) {
               i("cancelled");
            } else {
               if (O instanceof d.s && var1 - I > 1500000000L) {
                  I = var1;
                  S(var0, O.prompt());
               }
            }
         } else {
            O = null;
         }
      }
   }

   private static AABB s(BlockPos var0) {
      return new AABB(var0.getX() - 0.002, var0.getY() - 0.002, var0.getZ() - 0.002, var0.getX() + 1.002, var0.getY() + 1.002, var0.getZ() + 1.002);
   }

   private static void C() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null) {
         if (O instanceof d.s var1 && var0.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            boolean var6 = var1.valid().test(var0.level.getBlockState(var2.getBlockPos()));
            Gizmos.cuboid(s(var2.getBlockPos()), GizmoStyle.stroke(var6 ? -13180806 : -2074034, 2.5F)).setAlwaysOnTop();
         }

         long var4 = System.nanoTime();
         if (S != null && var4 < X) {
            int var7 = (int)(110L * (X - var4) / 1.2E9);
            Gizmos.cuboid(s(S), GizmoStyle.strokeAndFill(-13180806, 2.5F, var7 << 24 | 3596410)).setAlwaysOnTop();
         } else {
            S = null;
         }
      }
   }

   public static boolean V(Minecraft var0) {
      if (!(O instanceof d.s var1 && var0.level != null)) {
         return false;
      } else if (var0.hitResult instanceof BlockHitResult var5 && var5.getType() == Type.BLOCK) {
         BlockPos var6 = var5.getBlockPos().immutable();
         if (!var1.valid().test(var0.level.getBlockState(var6))) {
            S(var0, var1.invalid());
            return true;
         } else {
            O = null;
            String var4 = var1.apply().apply(var6);
            S = var6;
            X = System.nanoTime() + 1200000000L;
            S(var0, var4);
            xa.q("Casino", var4);
            return true;
         }
      } else {
         S(var0, var1.prompt());
         return true;
      }
   }

   public static boolean e(Slot var0, AbstractContainerMenu var1) {
      if (O instanceof d.f var2 && var0 != null) {
         Minecraft var6 = Minecraft.getInstance();
         ItemStack var4 = var0.getItem();
         if (!var4.isEmpty() && var2.valid().test(var4)) {
            O = null;
            String var5 = var2.apply().apply(var4.copy());
            t = var1;
            G = var0.index;
            E = System.nanoTime() + 1200000000L;
            S(var6, var5);
            xa.q("Casino", var5);
            return true;
         } else {
            S(var6, var2.invalid());
            return true;
         }
      } else {
         return false;
      }
   }

   public static int f(Slot var0, boolean var1, AbstractContainerMenu var2) {
      long var3 = System.nanoTime();
      if (G >= 0 && var3 >= E) {
         G = -1;
         t = null;
      }

      if (G >= 0 && var2 == t && var0.index == G) {
         int var7 = (int)(150L * (E - var3) / 1.2E9);
         return var7 << 24 | 3596410;
      }

      if (O instanceof d.f var5 && !var0.getItem().isEmpty()) {
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

   private record f(String prompt, Predicate<ItemStack> valid, String invalid, Function<ItemStack, String> apply) implements d.j {
   }

   private sealed interface j permits d.s, d.f {
      String prompt();
   }

   private record s(String prompt, Predicate<BlockState> valid, String invalid, Function<BlockPos, String> apply) implements d.j {
   }
}
