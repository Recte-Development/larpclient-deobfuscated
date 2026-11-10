package com.larpclient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public final class mm {
   private static final Map<Integer, ItemStack> L = new LinkedHashMap<>();
   private static final Map<Item, Integer> G = new LinkedHashMap<>();
   private static final Map<Item, Integer> u = new LinkedHashMap<>();
   private static Object X;
   private static Object p;
   private static BlockPos z;
   private static AbstractContainerMenu e;
   private static boolean l;
   private static boolean C;
   private static boolean h;
   private static boolean F;
   private static boolean a;
   private static boolean S;
   private static int H;
   private static int W;
   private static int A;
   private static int k;
   private static int n;
   private static int q;
   private static int y = -1;
   private static int d;
   private static Item N;
   private static boolean U;

   public static boolean o(int var0) {
      return L.containsKey(var0);
   }

   public static int N(Item var0) {
      int var1 = 0;

      for (ItemStack var3 : L.values()) {
         if (var3.is(var0)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static ItemStack l(int var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         return var1;
      }

      ItemStack var2 = L.get(var0);
      if (var2 == null) {
         return var1;
      }

      int var3 = u.getOrDefault(var2.getItem(), 0);

      for (Entry var5 : L.entrySet()) {
         if (((ItemStack)var5.getValue()).is(var2.getItem())) {
            if ((Integer)var5.getKey() == var0) {
               int var6 = var2.getCount() - Math.min(var3, var2.getCount());
               return var6 <= 0 ? var1 : gz.r(new ItemStack(var2.getItem(), var6));
            }

            var3 = Math.max(0, var3 - ((ItemStack)var5.getValue()).getCount());
         }
      }

      return var1;
   }

   public static int O(Item var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!l
         && var1 > 0
         && ll.r(var0)
         && var2.player != null
         && var2.player.containerMenu == var2.player.inventoryMenu
         && var2.player.inventoryMenu.getCarried().isEmpty()) {
         if (X != var2.player || p != var2.level) {
            y();
         }

         X = var2.player;
         p = var2.level;

         for (int var3 = 0; var3 < 36; var3++) {
            int var4 = ki.t(var3);
            if (var4 >= 0 && !b8.k().t(var4) && var2.player.getInventory().getItem(var4).isEmpty()) {
               ItemStack var5 = L.get(var4);
               if (var5 == null || var5.is(var0) && var5.getCount() < 64) {
                  int var6 = Math.min(var1, 64 - (var5 == null ? 0 : var5.getCount()));
                  L.put(var4, new ItemStack(var0, var6 + (var5 == null ? 0 : var5.getCount())));
                  ki.g(var3, var4);
                  return var6;
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void J() {
      d(null);
   }

   public static void d(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!L.isEmpty() && !l && var1.player != null) {
         if (var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
            G.clear();
            z = var0 == null ? null : var0.immutable();

            for (ItemStack var3 : L.values()) {
               G.merge(var3.getItem(), var3.getCount(), Integer::sum);
            }

            G.replaceAll((var0x, var1x) -> zk.Q(var1x));
            C = (boolean)1;
            l = (boolean)1;
            A = H + 120;
            W = H + 100;
            var1.player.connection.sendCommand("shop");
         } else {
            Y("Empty your cursor before withdrawing loot.");
         }
      }
   }

   public static boolean f(ClientboundOpenScreenPacket var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!var1.isSameThread() || !C || var1.player == null) {
         return false;
      }

      if (var1.gui.screen() == null && var1.player == X && var1.level == p && H <= W) {
         AbstractContainerMenu var2 = var0.getType().create(var0.getContainerId(), var1.player.getInventory());
         if (!(var2 instanceof ChestMenu)) {
            Y("Unsupported shop menu.");
            return false;
         } else {
            e = var2;
            var1.player.containerMenu = var2;
            C = (boolean)0;
            W = H + 100;
            return true;
         }
      } else {
         Y("Shop purchase stopped: another screen opened or the request expired.");
         return false;
      }
   }

   public static void o() {
      if (Minecraft.getInstance().isSameThread()) {
         k++;
         if (l && h && !S) {
            S = (boolean)1;
            t4.C(0L, () -> {
               S = (boolean)0;
               if (l && h) {
                  J(Minecraft.getInstance());
               }
            });
         }
      }
   }

   public static boolean G() {
      return Minecraft.getInstance().isSameThread() && H < A;
   }

   public static boolean A(Component var0, boolean var1) {
      if (!G()) {
         return false;
      }

      String var2 = var0.getString().toLowerCase(Locale.ROOT);
      int var3 = !var2.contains("not enough")
            && !var2.contains("insufficient")
            && !var2.contains("inventory full")
            && !var2.contains("inventory is full")
            && !var2.contains("cannot afford")
            && !var2.contains("can't afford")
         ? 0
         : 1;
      int var4 = var3 == 0
            && !var2.contains("bought")
            && !var2.contains("purchas")
            && !var2.contains("paid")
            && !var2.contains("shop")
            && !var2.contains("received")
            && !var2.contains("successfully")
            && !var2.contains("deducted")
         ? 0
         : 1;
      if (var3 != 0 && l) {
         Y("Shop purchase failed; remaining withdrawals cancelled.");
      }

      return var1 || var4 != 0;
   }

   public static void N(Minecraft var0) {
      H++;
      J(var0);
   }

   private static void J(Minecraft var0) {
      if (X == null || X == var0.player && p == var0.level && b8.k().J()) {
         if (!l) {
            if (!L.isEmpty() && !(var0.gui.screen() instanceof mn)) {
               y();
            }
         } else if (var0.player != null && var0.gameMode != null) {
            if (var0.gui.screen() instanceof InventoryScreen) {
               a = (boolean)1;
               var0.gui.setScreen(null);
            }

            if (var0.gui.screen() != null || var0.player.isDeadOrDying()) {
               Y("Shop purchase cancelled.");
            } else if (H > W) {
               Y(U ? "Shop purchase stopped: cannot read the server price (details in latest.log)." : "Shop purchase timed out; no retry was sent.");
            } else if (!C) {
               if (F) {
                  q(var0);
               } else if (e != null && var0.player.containerMenu == e) {
                  if (h) {
                     if (k <= n || t(N) < q + 64 || !e.getCarried().isEmpty()) {
                        return;
                     }

                     G.computeIfPresent(N, (var0x, var1) -> var1 - 1);
                     u.merge(N, 64, Integer::sum);
                     A = H + 20;
                     h = (boolean)0;
                     W = H + 100;
                  }

                  N = G.entrySet().stream().filter(var0x -> var0x.getValue() > 0).map(Entry::getKey).findFirst().orElse(null);
                  if (N == null) {
                     var0.player.closeContainer();
                     e = null;
                     F = (boolean)1;
                     W = H + 100;
                  } else {
                     for (Slot var2 : e.slots) {
                        if (var2.container != var0.player.getInventory() && var2.getItem().is(N)) {
                           ItemLore var3 = (ItemLore)var2.getItem().get(DataComponents.LORE);
                           List var4 = var3 == null ? List.of() : var3.lines().stream().map(Component::getString).toList();
                           zk.p var5 = zk.m(var4);
                           if (var5 == zk.p.m) {
                              if (e.getCarried().isEmpty() && Y(N) >= 64) {
                                 U = (boolean)0;
                                 q = t(N);
                                 n = k;
                                 h = (boolean)1;
                                 A = H + 120;
                                 W = H + 100;
                                 var0.gameMode.handleContainerInput(e.containerId, var2.index, 0, ContainerInput.PICKUP, var0.player);
                                 return;
                              }

                              Y("Shop purchase stopped: inventory or cursor is full.");
                              return;
                           }

                           if (var5 == zk.p.O && !U) {
                              com.larpclient.h.g("FakeSpawner", "Waiting for shop price for {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(N), var4);
                           }

                           if (var5 != zk.p.O) {
                              com.larpclient.h.g("FakeSpawner", "Purchase blocked for {}: {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(N), var5, var4);
                              Y("Shop purchase blocked: server listing is above $3M.");
                              return;
                           }

                           U = (boolean)1;
                        }
                     }
                  }
               } else {
                  Y("Shop closed before purchases completed.");
               }
            }
         } else {
            y();
         }
      } else {
         Y((String)null);
         A = 0;
      }
   }

   private static int t(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.getItem(var3).is(var0)) {
            var2 += var1.getItem(var3).getCount();
         }
      }

      return var2;
   }

   private static int Y(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (!b8.k().t(var3)) {
            ItemStack var4 = var1.getItem(var3);
            if (var4.isEmpty()) {
               var2 += 64;
            } else if (var4.is(var0)) {
               var2 += Math.max(0, var4.getMaxStackSize() - var4.getCount());
            }
         }
      }

      return var2;
   }

   private static int v(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   private static void q(Minecraft var0) {
      if (var0.player.containerMenu != var0.player.inventoryMenu) {
         Y("Inventory changed; purchased loot kept.");
      } else if (!m3.u()) {
         if (y >= 0) {
            if (var0.player.getInventory().getItem(y).getCount() < d || !var0.player.inventoryMenu.getCarried().isEmpty()) {
               return;
            }

            y = -1;
         }

         if (var0.player.inventoryMenu.getCarried().isEmpty()) {
            for (Entry var2 : L.entrySet()) {
               int var3 = (Integer)var2.getKey();
               ItemStack var4 = (ItemStack)var2.getValue();
               ItemStack var5 = var0.player.getInventory().getItem(var3);
               if (var5.isEmpty() || var5.is(var4.getItem())) {
                  int var6 = var4.getCount() - var5.getCount();
                  if (var6 > 0) {
                     for (int var7 = 0; var7 < 36; var7++) {
                        if (var7 != var3) {
                           ItemStack var8 = var0.player.getInventory().getItem(var7);
                           if (var8.is(var4.getItem())) {
                              int var9 = L.containsKey(var7) && L.get(var7).is(var8.getItem()) ? L.get(var7).getCount() : 0;
                              int var10 = Math.min(var6, var8.getCount() - var9);
                              if (var10 > 0) {
                                 y = var3;
                                 d = var5.getCount() + var10;
                                 n = k;
                                 W = H + 100;
                                 int var11 = var8.getCount();
                                 m3.j(
                                    "arrange purchased loot",
                                    m3.h(var7, var3, var10, var11),
                                    ki::n,
                                    () -> Y("Inventory corrected; purchased loot kept in its real slots.")
                                 );
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }

            Y((String)null);
         }
      }
   }

   private static void Y(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = a && var1.player == X ? 1 : 0;
      if (z != null && var1.player == X && var1.level == p) {
         LinkedHashMap var3 = new LinkedHashMap();

         for (ItemStack var5 : L.values()) {
            var3.merge(var5.getItem(), var5.getCount(), Integer::sum);
         }

         var3.forEach((var0x, var1x) -> b8.k().S(z, var0x, Math.min(var1x, u.getOrDefault(var0x, 0))));
      }

      if (var1.player == X && e != null && var1.player.containerMenu == e) {
         var1.player.closeContainer();
      }

      y();
      A = H + 20;
      if (var0 != null && var1.player != null) {
         k4.f(Component.literal(var0), false);
      }

      if (var2 != 0 && var1.player != null) {
         var1.gui.setScreen(new InventoryScreen(var1.player));
      }
   }

   public static void S() {
      Y((String)null);
      A = 0;
   }

   private static void y() {
      L.clear();
      G.clear();
      u.clear();
      e = null;
      z = null;
      p = null;
      X = null;
      S = (boolean)0;
      a = (boolean)0;
      U = (boolean)0;
      F = (boolean)0;
      h = (boolean)0;
      C = (boolean)0;
      l = (boolean)0;
      y = -1;
      ki.n();
   }
}
