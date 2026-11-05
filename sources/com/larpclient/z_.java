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

public final class z_ {
   private static final Map<Integer, ItemStack> u = new LinkedHashMap<>();
   private static final Map<Item, Integer> I = new LinkedHashMap<>();
   private static final Map<Item, Integer> g = new LinkedHashMap<>();
   private static Object b;
   private static Object O;
   private static BlockPos R;
   private static AbstractContainerMenu T;
   private static boolean L;
   private static boolean m;
   private static boolean Q;
   private static boolean C;
   private static boolean B;
   private static boolean W;
   private static int t;
   private static int H;
   private static int M;
   private static int X;
   private static int N;
   private static int J;
   private static int l = -1;
   private static int E;
   private static Item w;
   private static boolean e;

   public static boolean w(int var0) {
      return u.containsKey(var0);
   }

   public static int K(Item var0) {
      int var1 = 0;

      for (ItemStack var3 : u.values()) {
         if (var3.is(var0)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static ItemStack X(int var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         return var1;
      }

      ItemStack var2 = u.get(var0);
      if (var2 == null) {
         return var1;
      }

      int var3 = g.getOrDefault(var2.getItem(), 0);

      for (Entry var5 : u.entrySet()) {
         if (((ItemStack)var5.getValue()).is(var2.getItem())) {
            if ((Integer)var5.getKey() == var0) {
               int var6 = var2.getCount() - Math.min(var3, var2.getCount());
               return var6 <= 0 ? var1 : h2.H(new ItemStack(var2.getItem(), var6));
            }

            var3 = Math.max(0, var3 - ((ItemStack)var5.getValue()).getCount());
         }
      }

      return var1;
   }

   public static int p(Item var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!L
         && var1 > 0
         && cl.b(var0)
         && var2.player != null
         && var2.player.containerMenu == var2.player.inventoryMenu
         && var2.player.inventoryMenu.getCarried().isEmpty()) {
         if (b != var2.player || O != var2.level) {
            P();
         }

         b = var2.player;
         O = var2.level;

         for (int var3 = 0; var3 < 36; var3++) {
            int var4 = u0.f(var3);
            if (var4 >= 0 && !dr.z().t(var4) && var2.player.getInventory().getItem(var4).isEmpty()) {
               ItemStack var5 = u.get(var4);
               if (var5 == null || var5.is(var0) && var5.getCount() < 64) {
                  int var6 = Math.min(var1, 64 - (var5 == null ? 0 : var5.getCount()));
                  u.put(var4, new ItemStack(var0, var6 + (var5 == null ? 0 : var5.getCount())));
                  u0.w(var3, var4);
                  return var6;
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void G() {
      r(null);
   }

   public static void r(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!u.isEmpty() && !L && var1.player != null) {
         if (var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
            I.clear();
            R = var0 == null ? null : var0.immutable();

            for (ItemStack var3 : u.values()) {
               I.merge(var3.getItem(), var3.getCount(), Integer::sum);
            }

            I.replaceAll((var0x, var1x) -> w2.y(var1x));
            m = (boolean)1;
            L = (boolean)1;
            M = t + 120;
            H = t + 100;
            var1.player.connection.sendCommand("shop");
         } else {
            t("Empty your cursor before withdrawing loot.");
         }
      }
   }

   public static boolean Z(ClientboundOpenScreenPacket var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!var1.isSameThread() || !m || var1.player == null) {
         return false;
      }

      if (var1.gui.screen() == null && var1.player == b && var1.level == O && t <= H) {
         AbstractContainerMenu var2 = var0.getType().create(var0.getContainerId(), var1.player.getInventory());
         if (!(var2 instanceof ChestMenu)) {
            t("Unsupported shop menu.");
            return false;
         } else {
            T = var2;
            var1.player.containerMenu = var2;
            m = (boolean)0;
            H = t + 100;
            return true;
         }
      } else {
         t("Shop purchase stopped: another screen opened or the request expired.");
         return false;
      }
   }

   public static void a() {
      if (Minecraft.getInstance().isSameThread()) {
         X++;
         if (L && Q && !W) {
            W = (boolean)1;
            t1.O(0L, () -> {
               W = (boolean)0;
               if (L && Q) {
                  F(Minecraft.getInstance());
               }
            });
         }
      }
   }

   public static boolean K() {
      return Minecraft.getInstance().isSameThread() && t < M;
   }

   public static boolean F(Component var0, boolean var1) {
      if (!K()) {
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
      if (var3 != 0 && L) {
         t("Shop purchase failed; remaining withdrawals cancelled.");
      }

      return var1 || var4 != 0;
   }

   public static void Z(Minecraft var0) {
      t++;
      F(var0);
   }

   private static void F(Minecraft var0) {
      if (b == null || b == var0.player && O == var0.level && dr.z().H()) {
         if (!L) {
            if (!u.isEmpty() && !(var0.gui.screen() instanceof eu)) {
               P();
            }
         } else if (var0.player != null && var0.gameMode != null) {
            if (var0.gui.screen() instanceof InventoryScreen) {
               B = (boolean)1;
               var0.gui.setScreen(null);
            }

            if (var0.gui.screen() != null || var0.player.isDeadOrDying()) {
               t("Shop purchase cancelled.");
            } else if (t > H) {
               t(e ? "Shop purchase stopped: cannot read the server price (details in latest.log)." : "Shop purchase timed out; no retry was sent.");
            } else if (!m) {
               if (C) {
                  g(var0);
               } else if (T != null && var0.player.containerMenu == T) {
                  if (Q) {
                     if (X <= N || u(w) < J + 64 || !T.getCarried().isEmpty()) {
                        return;
                     }

                     I.computeIfPresent(w, (var0x, var1) -> var1 - 1);
                     g.merge(w, 64, Integer::sum);
                     M = t + 20;
                     Q = (boolean)0;
                     H = t + 100;
                  }

                  w = I.entrySet().stream().filter(var0x -> var0x.getValue() > 0).map(Entry::getKey).findFirst().orElse(null);
                  if (w == null) {
                     var0.player.closeContainer();
                     T = null;
                     C = (boolean)1;
                     H = t + 100;
                  } else {
                     for (Slot var2 : T.slots) {
                        if (var2.container != var0.player.getInventory() && var2.getItem().is(w)) {
                           ItemLore var3 = (ItemLore)var2.getItem().get(DataComponents.LORE);
                           List var4 = var3 == null ? List.of() : var3.lines().stream().map(Component::getString).toList();
                           w2.w var5 = w2.V(var4);
                           if (var5 == w2.w.q) {
                              if (T.getCarried().isEmpty() && c(w) >= 64) {
                                 e = (boolean)0;
                                 J = u(w);
                                 N = X;
                                 Q = (boolean)1;
                                 M = t + 120;
                                 H = t + 100;
                                 var0.gameMode.handleContainerInput(T.containerId, var2.index, 0, ContainerInput.PICKUP, var0.player);
                                 return;
                              }

                              t("Shop purchase stopped: inventory or cursor is full.");
                              return;
                           }

                           if (var5 == w2.w.K && !e) {
                              mt.T("FakeSpawner", "Waiting for shop price for {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(w), var4);
                           }

                           if (var5 != w2.w.K) {
                              mt.T("FakeSpawner", "Purchase blocked for {}: {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(w), var5, var4);
                              t("Shop purchase blocked: server listing is above $3M.");
                              return;
                           }

                           e = (boolean)1;
                        }
                     }
                  }
               } else {
                  t("Shop closed before purchases completed.");
               }
            }
         } else {
            P();
         }
      } else {
         t(null);
         M = 0;
      }
   }

   private static int u(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.getItem(var3).is(var0)) {
            var2 += var1.getItem(var3).getCount();
         }
      }

      return var2;
   }

   private static int c(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (!dr.z().t(var3)) {
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

   private static int U(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   private static void g(Minecraft var0) {
      if (var0.player.containerMenu != var0.player.inventoryMenu) {
         t("Inventory changed; purchased loot kept.");
      } else if (!wt.v()) {
         if (l >= 0) {
            if (var0.player.getInventory().getItem(l).getCount() < E || !var0.player.inventoryMenu.getCarried().isEmpty()) {
               return;
            }

            l = -1;
         }

         if (var0.player.inventoryMenu.getCarried().isEmpty()) {
            for (Entry var2 : u.entrySet()) {
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
                              int var9 = u.containsKey(var7) && u.get(var7).is(var8.getItem()) ? u.get(var7).getCount() : 0;
                              int var10 = Math.min(var6, var8.getCount() - var9);
                              if (var10 > 0) {
                                 l = var3;
                                 E = var5.getCount() + var10;
                                 N = X;
                                 H = t + 100;
                                 int var11 = var8.getCount();
                                 wt.S(
                                    "arrange purchased loot",
                                    wt.G(var7, var3, var10, var11),
                                    u0::V,
                                    () -> t("Inventory corrected; purchased loot kept in its real slots.")
                                 );
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }

            t(null);
         }
      }
   }

   private static void t(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = B && var1.player == b ? 1 : 0;
      if (R != null && var1.player == b && var1.level == O) {
         LinkedHashMap var3 = new LinkedHashMap();

         for (ItemStack var5 : u.values()) {
            var3.merge(var5.getItem(), var5.getCount(), Integer::sum);
         }

         var3.forEach((var0x, var1x) -> dr.z().f(R, var0x, Math.min(var1x, g.getOrDefault(var0x, 0))));
      }

      if (var1.player == b && T != null && var1.player.containerMenu == T) {
         var1.player.closeContainer();
      }

      P();
      M = t + 20;
      if (var0 != null && var1.player != null) {
         ws.c(Component.literal(var0), false);
      }

      if (var2 != 0 && var1.player != null) {
         var1.gui.setScreen(new InventoryScreen(var1.player));
      }
   }

   public static void f() {
      t(null);
      M = 0;
   }

   private static void P() {
      u.clear();
      I.clear();
      g.clear();
      T = null;
      R = null;
      O = null;
      b = null;
      W = (boolean)0;
      B = (boolean)0;
      e = (boolean)0;
      C = (boolean)0;
      Q = (boolean)0;
      m = (boolean)0;
      L = (boolean)0;
      l = -1;
      u0.V();
   }
}
