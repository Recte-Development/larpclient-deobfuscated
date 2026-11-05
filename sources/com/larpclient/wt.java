package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

public final class wt {
   private static AbstractContainerMenu W;
   private static Object E;
   private static Object L;
   private static List<wt.a> b = List.of();
   private static List<ItemStack> l = List.of();
   private static ItemStack k = ItemStack.EMPTY;
   private static ItemStack B = ItemStack.EMPTY;
   private static Runnable w;
   private static Runnable R;
   private static String N;
   private static int g;
   private static int A;
   private static boolean r;

   private wt() {
   }

   public static boolean v() {
      return W != null;
   }

   public static boolean V() {
      return r;
   }

   public static void Y(Runnable var0) {
      if (!v() || r) {
         boolean var1 = r;
         r = (boolean)1;

         try {
            var0.run();
         } finally {
            r = var1;
         }
      }
   }

   public static int i(int var0) {
      return var0 == 40 ? 45 : (var0 < 9 ? var0 + 36 : var0);
   }

   public static List<wt.a> G(int var0, int var1, int var2, int var3) {
      if (var0 != var1 && var2 > 0 && var2 <= var3 && var3 <= 64) {
         ArrayList var4 = new ArrayList();
         var4.add(new wt.a(i(var0), 0, ContainerInput.PICKUP));
         if (var2 == var3) {
            var4.add(new wt.a(i(var1), 0, ContainerInput.PICKUP));
         } else {
            for (int var5 = 0; var5 < var2; var5++) {
               var4.add(new wt.a(i(var1), 1, ContainerInput.PICKUP));
            }

            var4.add(new wt.a(i(var0), 0, ContainerInput.PICKUP));
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Invalid backing transfer");
      }
   }

   public static boolean S(String var0, List<wt.a> var1, Runnable var2, Runnable var3) {
      Minecraft var4 = Minecraft.getInstance();
      return var4.player != null && b(var4.player.inventoryMenu, var0, var1, var2, var3);
   }

   public static boolean b(AbstractContainerMenu var0, String var1, List<wt.a> var2, Runnable var3, Runnable var4) {
      return p(var0, var1, var2, ItemStack.EMPTY, var3, var4);
   }

   public static boolean p(AbstractContainerMenu var0, String var1, List<wt.a> var2, ItemStack var3, Runnable var4, Runnable var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (!v()
         && !var2.isEmpty()
         && var2.size() <= 132
         && var6.player != null
         && var6.gameMode != null
         && var6.player.containerMenu == var0
         && var0.getCarried().isEmpty()) {
         W = var0;
         E = var6.player;
         L = var6.level;
         b = List.copyOf(var2);
         A = 0;
         g = 0;
         w = var4;
         R = var5;
         N = var1;
         B = var3.copy();
         b();
         mt.U("FakeSpawner", "Transaction started: {} clicks={} container={}", var1, var2.size(), W.containerId);
         return true;
      } else {
         return false;
      }
   }

   private static void b() {
      l = W.slots.stream().map(var0 -> var0.getItem().copy()).toList();
      k = W.getCarried().copy();
   }

   static boolean k(AbstractContainerMenu var0, List<ItemStack> var1, ItemStack var2) {
      if (var0.slots.size() == var1.size() && ItemStack.matches(var0.getCarried(), var2)) {
         for (int var3 = 0; var3 < var1.size(); var3++) {
            if (!ItemStack.matches(var0.getSlot(var3).getItem(), (ItemStack)var1.get(var3))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static void g() {
      if (v() && !k(W, l, k)) {
         L("server inventory correction");
      }

      u0.V();
   }

   public static void C() {
      if (v()) {
         Minecraft var0 = Minecraft.getInstance();
         if (var0.player == E && var0.level == L && var0.player.containerMenu == W && var0.player.isAlive() && dr.z().H()) {
            if (++A <= 200 && k(W, l, k)) {
               if (g == b.size()) {
                  if (!ItemStack.matches(W.getCarried(), B)) {
                     L("unexpected cursor at completion");
                  } else {
                     Runnable var6 = w;
                     mt.U("FakeSpawner", "Transaction predicted complete: {} clicks={}", N, g);
                     L();
                     if (var6 != null) {
                        var6.run();
                     }

                     u0.V();
                  }
               } else {
                  for (int var1 = 0; var1 < 4 && g < b.size(); var1++) {
                     wt.a var2 = b.get(g++);
                     r = (boolean)1;

                     try {
                        var0.gameMode.handleContainerInput(W.containerId, var2.slot, var2.button, var2.type, var0.player);
                     } finally {
                        r = (boolean)0;
                     }

                     b();
                     u0.V();
                  }
               }
            } else {
               L(A > 200 ? "timeout" : "inventory changed");
            }
         } else {
            L("owner or container changed");
         }
      }
   }

   public static void L(String var0) {
      if (v()) {
         Minecraft var1 = Minecraft.getInstance();
         Runnable var2 = R;
         int var3 = var1.player == E && var1.level == L && var1.player.containerMenu == W ? 1 : 0;
         mt.U("FakeSpawner", "Transaction cancelled: {} step={}/{} reason={}; native cursor recovery={}", N, g, b.size(), var0, Boolean.valueOf((boolean)var3));
         L();
         if (var3 != 0) {
            var1.player.closeContainer();
         }

         if (var2 != null) {
            var2.run();
         }

         u0.V();
      }
   }

   private static void L() {
      W = null;
      L = null;
      E = null;
      b = List.of();
      l = List.of();
      k = B = ItemStack.EMPTY;
      R = null;
      w = null;
      N = null;
      A = 0;
      g = 0;
   }

   public record a(int slot, int button, ContainerInput type) {
   }
}
