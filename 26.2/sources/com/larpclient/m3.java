package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

public final class m3 {
   private static AbstractContainerMenu f;
   private static Object O;
   private static Object u;
   private static List<m3.g> q = List.of();
   private static List<ItemStack> Y = List.of();
   private static ItemStack s = ItemStack.EMPTY;
   private static ItemStack X = ItemStack.EMPTY;
   private static Runnable y;
   private static Runnable b;
   private static String a;
   private static int I;
   private static int l;
   private static boolean c;

   private m3() {
   }

   public static boolean u() {
      return f != null;
   }

   public static boolean d() {
      return c;
   }

   public static void W(Runnable var0) {
      if (!u() || c) {
         boolean var1 = c;
         c = (boolean)1;

         try {
            var0.run();
         } finally {
            c = var1;
         }
      }
   }

   public static int t(int var0) {
      return var0 == 40 ? 45 : (var0 < 9 ? var0 + 36 : var0);
   }

   public static List<m3.g> h(int var0, int var1, int var2, int var3) {
      if (var0 != var1 && var2 > 0 && var2 <= var3 && var3 <= 64) {
         ArrayList var4 = new ArrayList();
         var4.add(new m3.g(t(var0), 0, ContainerInput.PICKUP));
         if (var2 == var3) {
            var4.add(new m3.g(t(var1), 0, ContainerInput.PICKUP));
         } else {
            for (int var5 = 0; var5 < var2; var5++) {
               var4.add(new m3.g(t(var1), 1, ContainerInput.PICKUP));
            }

            var4.add(new m3.g(t(var0), 0, ContainerInput.PICKUP));
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Invalid backing transfer");
      }
   }

   public static boolean j(String var0, List<m3.g> var1, Runnable var2, Runnable var3) {
      Minecraft var4 = Minecraft.getInstance();
      return var4.player != null && E(var4.player.inventoryMenu, var0, var1, var2, var3);
   }

   public static boolean E(AbstractContainerMenu var0, String var1, List<m3.g> var2, Runnable var3, Runnable var4) {
      return t(var0, var1, var2, ItemStack.EMPTY, var3, var4);
   }

   public static boolean t(AbstractContainerMenu var0, String var1, List<m3.g> var2, ItemStack var3, Runnable var4, Runnable var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (!u()
         && !var2.isEmpty()
         && var2.size() <= 132
         && var6.player != null
         && var6.gameMode != null
         && var6.player.containerMenu == var0
         && var0.getCarried().isEmpty()) {
         f = var0;
         O = var6.player;
         u = var6.level;
         q = List.copyOf(var2);
         l = 0;
         I = 0;
         y = var4;
         b = var5;
         a = var1;
         X = var3.copy();
         d();
         h.U("FakeSpawner", "Transaction started: {} clicks={} container={}", var1, var2.size(), f.containerId);
         return true;
      } else {
         return false;
      }
   }

   private static void d() {
      Y = f.slots.stream().map(var0 -> var0.getItem().copy()).toList();
      s = f.getCarried().copy();
   }

   static boolean r(AbstractContainerMenu var0, List<ItemStack> var1, ItemStack var2) {
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

   public static void m() {
      if (u() && !r(f, Y, s)) {
         J("server inventory correction");
      }

      ki.n();
   }

   public static void f() {
      if (u()) {
         Minecraft var0 = Minecraft.getInstance();
         if (var0.player == O && var0.level == u && var0.player.containerMenu == f && var0.player.isAlive() && b8.k().J()) {
            if (++l <= 200 && r(f, Y, s)) {
               if (I == q.size()) {
                  if (!ItemStack.matches(f.getCarried(), X)) {
                     J("unexpected cursor at completion");
                  } else {
                     Runnable var6 = y;
                     h.U("FakeSpawner", "Transaction predicted complete: {} clicks={}", a, I);
                     N();
                     if (var6 != null) {
                        var6.run();
                     }

                     ki.n();
                  }
               } else {
                  for (int var1 = 0; var1 < 4 && I < q.size(); var1++) {
                     m3.g var2 = q.get(I++);
                     c = (boolean)1;

                     try {
                        var0.gameMode.handleContainerInput(f.containerId, var2.slot, var2.button, var2.type, var0.player);
                     } finally {
                        c = (boolean)0;
                     }

                     d();
                     ki.n();
                  }
               }
            } else {
               J(l > 200 ? "timeout" : "inventory changed");
            }
         } else {
            J("owner or container changed");
         }
      }
   }

   public static void J(String var0) {
      if (u()) {
         Minecraft var1 = Minecraft.getInstance();
         Runnable var2 = b;
         int var3 = var1.player == O && var1.level == u && var1.player.containerMenu == f ? 1 : 0;
         h.U("FakeSpawner", "Transaction cancelled: {} step={}/{} reason={}; native cursor recovery={}", a, I, q.size(), var0, Boolean.valueOf((boolean)var3));
         N();
         if (var3 != 0) {
            var1.player.closeContainer();
         }

         if (var2 != null) {
            var2.run();
         }

         ki.n();
      }
   }

   private static void N() {
      f = null;
      u = null;
      O = null;
      q = List.of();
      Y = List.of();
      s = X = ItemStack.EMPTY;
      b = null;
      y = null;
      a = null;
      l = 0;
      I = 0;
   }

   public record g(int slot, int button, ContainerInput type) {
   }
}
