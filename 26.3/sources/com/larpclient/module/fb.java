package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.bn;
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

public final class fb {
   private static final long u = 30000000000L;
   private static final long B = 1200000000L;
   private static final long m = 1500000000L;
   private static final int l = -13180806;
   private static final int C = -2074034;
   private static fb.m o;
   private static long M;
   private static long P;
   private static BlockPos J;
   private static long k;
   private static AbstractContainerMenu t;
   private static int f = -1;
   private static long D;
   private static boolean R;

   private fb() {
   }

   public static void f(String var0, Predicate<BlockState> var1, String var2, Function<BlockPos, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         g();
         o = new fb.d(var0, var1, var2, var3);
         G(var4);
         var4.gui.setScreen(null);
      } else {
         _q.z("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static void u(String var0, Predicate<ItemStack> var1, String var2, Function<ItemStack, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         g();
         o = new fb.t(var0, var1, var2, var3);
         G(var4);
         var4.gui.setScreen(new InventoryScreen(var4.player));
      } else {
         _q.z("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static boolean H() {
      return o != null;
   }

   private static void G(Minecraft var0) {
      long var1 = System.nanoTime();
      M = var1 + 30000000000L;
      P = var1;
      G(var0, o.prompt());
      _q.z("Rig", "Selection started: {}", o.prompt());
   }

   public static void k(String var0) {
      if (o != null) {
         o = null;
         G(Minecraft.getInstance(), "Selection " + var0);
         _q.z("Rig", "Selection {}", var0);
      }
   }

   private static void G(Minecraft var0, String var1) {
      if (var0.player != null) {
         var0.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private static void g() {
      if (!R) {
         R = (boolean)1;
         ClientTickEvents.END_CLIENT_TICK.register(fb::u);
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> a());
      }
   }

   private static void u(Minecraft var0) {
      if (o != null) {
         if (var0.player != null && var0.level != null) {
            long var1 = System.nanoTime();
            if (var1 > M) {
               k("timed out");
            } else if (o instanceof fb.d && var0.gui.screen() != null) {
               k("cancelled");
            } else if (o instanceof fb.t && !(var0.gui.screen() instanceof AbstractContainerScreen)) {
               k("cancelled");
            } else {
               if (o instanceof fb.d && var1 - P > 1500000000L) {
                  P = var1;
                  G(var0, o.prompt());
               }
            }
         } else {
            o = null;
         }
      }
   }

   private static AABB K(BlockPos var0) {
      return new AABB(var0.getX() - 0.002, var0.getY() - 0.002, var0.getZ() - 0.002, var0.getX() + 1.002, var0.getY() + 1.002, var0.getZ() + 1.002);
   }

   private static void a() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null) {
         if (o instanceof fb.d var1 && var0.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            boolean var6 = var1.valid().test(var0.level.getBlockState(var2.getBlockPos()));
            Gizmos.cuboid(K(var2.getBlockPos()), GizmoStyle.stroke(var6 ? -13180806 : -2074034, 2.5F)).setAlwaysOnTop();
         }

         long var4 = System.nanoTime();
         if (J != null && var4 < k) {
            int var7 = (int)(110L * (k - var4) / 1.2E9);
            Gizmos.cuboid(K(J), GizmoStyle.strokeAndFill(-13180806, 2.5F, var7 << 24 | 3596410)).setAlwaysOnTop();
         } else {
            J = null;
         }
      }
   }

   public static boolean v(Minecraft var0) {
      if (!(o instanceof fb.d var1 && var0.level != null)) {
         return false;
      } else if (var0.hitResult instanceof BlockHitResult var5 && var5.getType() == Type.BLOCK) {
         BlockPos var6 = var5.getBlockPos().immutable();
         if (!var1.valid().test(var0.level.getBlockState(var6))) {
            G(var0, var1.invalid());
            return true;
         } else {
            o = null;
            String var4 = var1.apply().apply(var6);
            J = var6;
            k = System.nanoTime() + 1200000000L;
            G(var0, var4);
            bn.j("Casino", var4);
            return true;
         }
      } else {
         G(var0, var1.prompt());
         return true;
      }
   }

   public static boolean n(Slot var0, AbstractContainerMenu var1) {
      if (o instanceof fb.t var2 && var0 != null) {
         Minecraft var6 = Minecraft.getInstance();
         ItemStack var4 = var0.getItem();
         if (!var4.isEmpty() && var2.valid().test(var4)) {
            o = null;
            String var5 = var2.apply().apply(var4.copy());
            t = var1;
            f = var0.index;
            D = System.nanoTime() + 1200000000L;
            G(var6, var5);
            bn.j("Casino", var5);
            return true;
         } else {
            G(var6, var2.invalid());
            return true;
         }
      } else {
         return false;
      }
   }

   public static int i(Slot var0, boolean var1, AbstractContainerMenu var2) {
      long var3 = System.nanoTime();
      if (f >= 0 && var3 >= D) {
         f = -1;
         t = null;
      }

      if (f >= 0 && var2 == t && var0.index == f) {
         int var7 = (int)(150L * (D - var3) / 1.2E9);
         return var7 << 24 | 3596410;
      }

      if (o instanceof fb.t var5 && !var0.getItem().isEmpty()) {
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

   private record d(String prompt, Predicate<BlockState> valid, String invalid, Function<BlockPos, String> apply) implements fb.m {
   }

   private sealed interface m permits fb.d, fb.t {
      String prompt();
   }

   private record t(String prompt, Predicate<ItemStack> valid, String invalid, Function<ItemStack, String> apply) implements fb.m {
   }
}
