package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

public final class q7 {
   private static AbstractContainerMenu U;
   private static Object y;
   private static Object A;
   private static List<q7.l> J = List.of();
   private static List<ItemStack> r = List.of();
   private static ItemStack w = ItemStack.EMPTY;
   private static ItemStack o = ItemStack.EMPTY;
   private static Runnable n;
   private static Runnable t;
   private static String D;
   private static int L;
   private static int O;
   private static boolean z;

   private q7() {
   }

   public static boolean M() {
      return U != null;
   }

   public static boolean F() {
      return z;
   }

   public static void K(Runnable var0) {
      if (!M() || z) {
         boolean var1 = z;
         z = (boolean)1;

         try {
            var0.run();
         } finally {
            z = var1;
         }
      }
   }

   public static int S(int var0) {
      return var0 == 40 ? 45 : (var0 < 9 ? var0 + 36 : var0);
   }

   public static List<q7.l> I(int var0, int var1, int var2, int var3) {
      if (var0 != var1 && var2 > 0 && var2 <= var3 && var3 <= 64) {
         ArrayList var4 = new ArrayList();
         var4.add(new q7.l(S(var0), 0, ContainerInput.PICKUP));
         if (var2 == var3) {
            var4.add(new q7.l(S(var1), 0, ContainerInput.PICKUP));
         } else {
            for (int var5 = 0; var5 < var2; var5++) {
               var4.add(new q7.l(S(var1), 1, ContainerInput.PICKUP));
            }

            var4.add(new q7.l(S(var0), 0, ContainerInput.PICKUP));
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Invalid backing transfer");
      }
   }

   public static boolean B(String var0, List<q7.l> var1, Runnable var2, Runnable var3) {
      Minecraft var4 = Minecraft.getInstance();
      return var4.player != null && I(var4.player.inventoryMenu, var0, var1, var2, var3);
   }

   public static boolean I(AbstractContainerMenu var0, String var1, List<q7.l> var2, Runnable var3, Runnable var4) {
      return h(var0, var1, var2, ItemStack.EMPTY, var3, var4);
   }

   public static boolean h(AbstractContainerMenu var0, String var1, List<q7.l> var2, ItemStack var3, Runnable var4, Runnable var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (!M()
         && !var2.isEmpty()
         && var2.size() <= 132
         && var6.player != null
         && var6.gameMode != null
         && var6.player.containerMenu == var0
         && var0.getCarried().isEmpty()) {
         U = var0;
         y = var6.player;
         A = var6.level;
         J = List.copyOf(var2);
         O = 0;
         L = 0;
         n = var4;
         t = var5;
         D = var1;
         o = var3.copy();
         f();
         _q.c("FakeSpawner", "Transaction started: {} clicks={} container={}", var1, var2.size(), U.containerId);
         return true;
      } else {
         return false;
      }
   }

   private static void f() {
      r = U.slots.stream().map(var0 -> var0.getItem().copy()).toList();
      w = U.getCarried().copy();
   }

   static boolean w(AbstractContainerMenu var0, List<ItemStack> var1, ItemStack var2) {
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

   public static void d() {
      if (M() && !w(U, r, w)) {
         i("server inventory correction");
      }

      _8.S();
   }

   public static void h() {
      if (M()) {
         Minecraft var0 = Minecraft.getInstance();
         if (var0.player == y && var0.level == A && var0.player.containerMenu == U && var0.player.isAlive() && jn.g().a()) {
            if (++O <= 200 && w(U, r, w)) {
               if (L == J.size()) {
                  if (!ItemStack.matches(U.getCarried(), o)) {
                     i("unexpected cursor at completion");
                  } else {
                     Runnable var6 = n;
                     _q.c("FakeSpawner", "Transaction predicted complete: {} clicks={}", D, L);
                     e();
                     if (var6 != null) {
                        var6.run();
                     }

                     _8.S();
                  }
               } else {
                  for (int var1 = 0; var1 < 4 && L < J.size(); var1++) {
                     q7.l var2 = J.get(L++);
                     z = (boolean)1;

                     try {
                        var0.gameMode.handleContainerInput(U.containerId, var2.slot, var2.button, var2.type, var0.player);
                     } finally {
                        z = (boolean)0;
                     }

                     f();
                     _8.S();
                  }
               }
            } else {
               i(O > 200 ? "timeout" : "inventory changed");
            }
         } else {
            i("owner or container changed");
         }
      }
   }

   public static void i(String var0) {
      if (M()) {
         Minecraft var1 = Minecraft.getInstance();
         Runnable var2 = t;
         int var3 = var1.player == y && var1.level == A && var1.player.containerMenu == U ? 1 : 0;
         _q.c("FakeSpawner", "Transaction cancelled: {} step={}/{} reason={}; native cursor recovery={}", D, L, J.size(), var0, Boolean.valueOf((boolean)var3));
         e();
         if (var3 != 0) {
            var1.player.closeContainer();
         }

         if (var2 != null) {
            var2.run();
         }

         _8.S();
      }
   }

   private static void e() {
      U = null;
      A = null;
      y = null;
      J = List.of();
      r = List.of();
      w = o = ItemStack.EMPTY;
      t = null;
      n = null;
      D = null;
      O = 0;
      L = 0;
   }

   public record l(int slot, int button, ContainerInput type) {
   }
}
