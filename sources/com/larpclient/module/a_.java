package com.larpclient.module;

import com.larpclient.hh;
import com.larpclient.r4;
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

public final class a_ {
   private static final long F = 30000000000L;
   private static final long X = 1200000000L;
   private static final long Y = 1500000000L;
   private static final int y = -13180806;
   private static final int E = -2074034;
   private static a_.m v;
   private static long B;
   private static long W;
   private static BlockPos i;
   private static long D;
   private static AbstractContainerMenu u;
   private static int z = -1;
   private static long k;
   private static boolean a;

   private a_() {
   }

   public static void B(String var0, Predicate<BlockState> var1, String var2, Function<BlockPos, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         x();
         v = new a_.a(var0, var1, var2, var3);
         P(var4);
         var4.gui.setScreen(null);
      } else {
         r4.V("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static void L(String var0, Predicate<ItemStack> var1, String var2, Function<ItemStack, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         x();
         v = new a_.w(var0, var1, var2, var3);
         P(var4);
         var4.gui.setScreen(new InventoryScreen(var4.player));
      } else {
         r4.V("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static boolean Q() {
      return v != null;
   }

   private static void P(Minecraft var0) {
      long var1 = System.nanoTime();
      B = var1 + 30000000000L;
      W = var1;
      q(var0, v.prompt());
      r4.V("Rig", "Selection started: {}", v.prompt());
   }

   public static void d(String var0) {
      if (v != null) {
         v = null;
         q(Minecraft.getInstance(), "Selection " + var0);
         r4.V("Rig", "Selection {}", var0);
      }
   }

   private static void q(Minecraft var0, String var1) {
      if (var0.player != null) {
         var0.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private static void x() {
      if (!a) {
         a = (boolean)1;
         ClientTickEvents.END_CLIENT_TICK.register(a_::I);
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> a());
      }
   }

   private static void I(Minecraft var0) {
      if (v != null) {
         if (var0.player != null && var0.level != null) {
            long var1 = System.nanoTime();
            if (var1 > B) {
               d("timed out");
            } else if (v instanceof a_.a && var0.gui.screen() != null) {
               d("cancelled");
            } else if (v instanceof a_.w && !(var0.gui.screen() instanceof AbstractContainerScreen)) {
               d("cancelled");
            } else {
               if (v instanceof a_.a && var1 - W > 1500000000L) {
                  W = var1;
                  q(var0, v.prompt());
               }
            }
         } else {
            v = null;
         }
      }
   }

   private static AABB r(BlockPos var0) {
      return new AABB(var0.getX() - 0.002, var0.getY() - 0.002, var0.getZ() - 0.002, var0.getX() + 1.002, var0.getY() + 1.002, var0.getZ() + 1.002);
   }

   private static void a() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null) {
         if (v instanceof a_.a var1 && var0.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            boolean var6 = var1.valid().test(var0.level.getBlockState(var2.getBlockPos()));
            Gizmos.cuboid(r(var2.getBlockPos()), GizmoStyle.stroke(var6 ? -13180806 : -2074034, 2.5F)).setAlwaysOnTop();
         }

         long var4 = System.nanoTime();
         if (i != null && var4 < D) {
            int var7 = (int)(110L * (D - var4) / 1.2E9);
            Gizmos.cuboid(r(i), GizmoStyle.strokeAndFill(-13180806, 2.5F, var7 << 24 | 3596410)).setAlwaysOnTop();
         } else {
            i = null;
         }
      }
   }

   public static boolean N(Minecraft var0) {
      if (!(v instanceof a_.a var1 && var0.level != null)) {
         return false;
      } else if (var0.hitResult instanceof BlockHitResult var5 && var5.getType() == Type.BLOCK) {
         BlockPos var6 = var5.getBlockPos().immutable();
         if (!var1.valid().test(var0.level.getBlockState(var6))) {
            q(var0, var1.invalid());
            return true;
         } else {
            v = null;
            String var4 = var1.apply().apply(var6);
            i = var6;
            D = System.nanoTime() + 1200000000L;
            q(var0, var4);
            hh.K("Casino", var4);
            return true;
         }
      } else {
         q(var0, var1.prompt());
         return true;
      }
   }

   public static boolean r(Slot var0, AbstractContainerMenu var1) {
      if (v instanceof a_.w var2 && var0 != null) {
         Minecraft var6 = Minecraft.getInstance();
         ItemStack var4 = var0.getItem();
         if (!var4.isEmpty() && var2.valid().test(var4)) {
            v = null;
            String var5 = var2.apply().apply(var4.copy());
            u = var1;
            z = var0.index;
            k = System.nanoTime() + 1200000000L;
            q(var6, var5);
            hh.K("Casino", var5);
            return true;
         } else {
            q(var6, var2.invalid());
            return true;
         }
      } else {
         return false;
      }
   }

   public static int o(Slot var0, boolean var1, AbstractContainerMenu var2) {
      long var3 = System.nanoTime();
      if (z >= 0 && var3 >= k) {
         z = -1;
         u = null;
      }

      if (z >= 0 && var2 == u && var0.index == z) {
         int var7 = (int)(150L * (k - var3) / 1.2E9);
         return var7 << 24 | 3596410;
      }

      if (v instanceof a_.w var5 && !var0.getItem().isEmpty()) {
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

   private record a(String prompt, Predicate<BlockState> valid, String invalid, Function<BlockPos, String> apply) implements a_.m {
   }

   private sealed interface m permits a_.a, a_.w {
      String prompt();
   }

   private record w(String prompt, Predicate<ItemStack> valid, String invalid, Function<ItemStack, String> apply) implements a_.m {
   }
}
