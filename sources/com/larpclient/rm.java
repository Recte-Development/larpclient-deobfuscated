package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

public final class rm {
   private static AbstractContainerMenu c;
   private static Object s;
   private static Object W;
   private static List<rm.k> M = List.of();
   private static List<ItemStack> D = List.of();
   private static ItemStack l = ItemStack.EMPTY;
   private static ItemStack I = ItemStack.EMPTY;
   private static Runnable x;
   private static Runnable S;
   private static String H;
   private static int F;
   private static int C;
   private static boolean o;

   private rm() {
   }

   public static boolean L() {
      return c != null;
   }

   public static boolean h() {
      return o;
   }

   public static void L(Runnable var0) {
      if (!L() || o) {
         boolean var1 = o;
         o = (boolean)1;

         try {
            var0.run();
         } finally {
            o = var1;
         }
      }
   }

   public static int A(int var0) {
      return var0 == 40 ? 45 : (var0 < 9 ? var0 + 36 : var0);
   }

   public static List<rm.k> M(int var0, int var1, int var2, int var3) {
      if (var0 != var1 && var2 > 0 && var2 <= var3 && var3 <= 64) {
         ArrayList var4 = new ArrayList();
         var4.add(new rm.k(A(var0), 0, ContainerInput.PICKUP));
         if (var2 == var3) {
            var4.add(new rm.k(A(var1), 0, ContainerInput.PICKUP));
         } else {
            for (int var5 = 0; var5 < var2; var5++) {
               var4.add(new rm.k(A(var1), 1, ContainerInput.PICKUP));
            }

            var4.add(new rm.k(A(var0), 0, ContainerInput.PICKUP));
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Invalid backing transfer");
      }
   }

   public static boolean H(String var0, List<rm.k> var1, Runnable var2, Runnable var3) {
      Minecraft var4 = Minecraft.getInstance();
      return var4.player != null && W(var4.player.inventoryMenu, var0, var1, var2, var3);
   }

   public static boolean W(AbstractContainerMenu var0, String var1, List<rm.k> var2, Runnable var3, Runnable var4) {
      return N(var0, var1, var2, ItemStack.EMPTY, var3, var4);
   }

   public static boolean N(AbstractContainerMenu var0, String var1, List<rm.k> var2, ItemStack var3, Runnable var4, Runnable var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (!L()
         && !var2.isEmpty()
         && var2.size() <= 132
         && var6.player != null
         && var6.gameMode != null
         && var6.player.containerMenu == var0
         && var0.getCarried().isEmpty()) {
         c = var0;
         s = var6.player;
         W = var6.level;
         M = List.copyOf(var2);
         C = 0;
         F = 0;
         x = var4;
         S = var5;
         H = var1;
         I = var3.copy();
         m();
         r4.g("FakeSpawner", "Transaction started: {} clicks={} container={}", var1, var2.size(), c.containerId);
         return true;
      } else {
         return false;
      }
   }

   private static void m() {
      D = c.slots.stream().map(var0 -> var0.getItem().copy()).toList();
      l = c.getCarried().copy();
   }

   static boolean j(AbstractContainerMenu var0, List<ItemStack> var1, ItemStack var2) {
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

   public static void L() {
      if (L() && !j(c, D, l)) {
         U("server inventory correction");
      }

      mx.u();
   }

   public static void P() {
      if (L()) {
         Minecraft var0 = Minecraft.getInstance();
         if (var0.player == s && var0.level == W && var0.player.containerMenu == c && var0.player.isAlive() && lj.j().B()) {
            if (++C <= 200 && j(c, D, l)) {
               if (F == M.size()) {
                  if (!ItemStack.matches(c.getCarried(), I)) {
                     U("unexpected cursor at completion");
                  } else {
                     Runnable var6 = x;
                     r4.g("FakeSpawner", "Transaction predicted complete: {} clicks={}", H, F);
                     R();
                     if (var6 != null) {
                        var6.run();
                     }

                     mx.u();
                  }
               } else {
                  for (int var1 = 0; var1 < 4 && F < M.size(); var1++) {
                     rm.k var2 = M.get(F++);
                     o = (boolean)1;

                     try {
                        var0.gameMode.handleContainerInput(c.containerId, var2.slot, var2.button, var2.type, var0.player);
                     } finally {
                        o = (boolean)0;
                     }

                     m();
                     mx.u();
                  }
               }
            } else {
               U(C > 200 ? "timeout" : "inventory changed");
            }
         } else {
            U("owner or container changed");
         }
      }
   }

   public static void U(String var0) {
      if (L()) {
         Minecraft var1 = Minecraft.getInstance();
         Runnable var2 = S;
         int var3 = var1.player == s && var1.level == W && var1.player.containerMenu == c ? 1 : 0;
         r4.g("FakeSpawner", "Transaction cancelled: {} step={}/{} reason={}; native cursor recovery={}", H, F, M.size(), var0, Boolean.valueOf((boolean)var3));
         R();
         if (var3 != 0) {
            var1.player.closeContainer();
         }

         if (var2 != null) {
            var2.run();
         }

         mx.u();
      }
   }

   private static void R() {
      c = null;
      W = null;
      s = null;
      M = List.of();
      D = List.of();
      l = I = ItemStack.EMPTY;
      S = null;
      x = null;
      H = null;
      C = 0;
      F = 0;
   }

   public record k(int slot, int button, ContainerInput type) {
   }
}
