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

public final class fv {
   private static final Map<Integer, ItemStack> b = new LinkedHashMap<>();
   private static final Map<Item, Integer> R = new LinkedHashMap<>();
   private static final Map<Item, Integer> F = new LinkedHashMap<>();
   private static Object Q;
   private static Object x;
   private static BlockPos e;
   private static AbstractContainerMenu J;
   private static boolean t;
   private static boolean d;
   private static boolean v;
   private static boolean G;
   private static boolean O;
   private static boolean Y;
   private static int V;
   private static int K;
   private static int E;
   private static int X;
   private static int r;
   private static int y;
   private static int W = -1;
   private static int L;
   private static Item s;
   private static boolean n;

   public static boolean l(int var0) {
      return b.containsKey(var0);
   }

   public static int Z(Item var0) {
      int var1 = 0;

      for (ItemStack var3 : b.values()) {
         if (var3.is(var0)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static ItemStack W(int var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         return var1;
      }

      ItemStack var2 = b.get(var0);
      if (var2 == null) {
         return var1;
      }

      int var3 = F.getOrDefault(var2.getItem(), 0);

      for (Entry var5 : b.entrySet()) {
         if (((ItemStack)var5.getValue()).is(var2.getItem())) {
            if ((Integer)var5.getKey() == var0) {
               int var6 = var2.getCount() - Math.min(var3, var2.getCount());
               return var6 <= 0 ? var1 : ja.T(new ItemStack(var2.getItem(), var6));
            }

            var3 = Math.max(0, var3 - ((ItemStack)var5.getValue()).getCount());
         }
      }

      return var1;
   }

   public static int n(Item var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!t
         && var1 > 0
         && mb.j(var0)
         && var2.player != null
         && var2.player.containerMenu == var2.player.inventoryMenu
         && var2.player.inventoryMenu.getCarried().isEmpty()) {
         if (Q != var2.player || x != var2.level) {
            b();
         }

         Q = var2.player;
         x = var2.level;

         for (int var3 = 0; var3 < 36; var3++) {
            int var4 = mx.V(var3);
            if (var4 >= 0 && !lj.j().U(var4) && var2.player.getInventory().getItem(var4).isEmpty()) {
               ItemStack var5 = b.get(var4);
               if (var5 == null || var5.is(var0) && var5.getCount() < 64) {
                  int var6 = Math.min(var1, 64 - (var5 == null ? 0 : var5.getCount()));
                  b.put(var4, new ItemStack(var0, var6 + (var5 == null ? 0 : var5.getCount())));
                  mx.z(var3, var4);
                  return var6;
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void W() {
      P(null);
   }

   public static void P(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!b.isEmpty() && !t && var1.player != null) {
         if (var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
            R.clear();
            e = var0 == null ? null : var0.immutable();

            for (ItemStack var3 : b.values()) {
               R.merge(var3.getItem(), var3.getCount(), Integer::sum);
            }

            R.replaceAll((var0x, var1x) -> x7.H(var1x));
            d = (boolean)1;
            t = (boolean)1;
            E = V + 120;
            K = V + 100;
            var1.player.connection.sendCommand("shop");
         } else {
            q("Empty your cursor before withdrawing loot.");
         }
      }
   }

   public static boolean b(ClientboundOpenScreenPacket var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!var1.isSameThread() || !d || var1.player == null) {
         return false;
      }

      if (var1.gui.screen() == null && var1.player == Q && var1.level == x && V <= K) {
         AbstractContainerMenu var2 = var0.getType().create(var0.getContainerId(), var1.player.getInventory());
         if (!(var2 instanceof ChestMenu)) {
            q("Unsupported shop menu.");
            return false;
         } else {
            J = var2;
            var1.player.containerMenu = var2;
            d = (boolean)0;
            K = V + 100;
            return true;
         }
      } else {
         q("Shop purchase stopped: another screen opened or the request expired.");
         return false;
      }
   }

   public static void m() {
      if (Minecraft.getInstance().isSameThread()) {
         X++;
         if (t && v && !Y) {
            Y = (boolean)1;
            ff.r(0L, () -> {
               Y = (boolean)0;
               if (t && v) {
                  z(Minecraft.getInstance());
               }
            });
         }
      }
   }

   public static boolean B() {
      return Minecraft.getInstance().isSameThread() && V < E;
   }

   public static boolean h(Component var0, boolean var1) {
      if (!B()) {
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
      if (var3 != 0 && t) {
         q("Shop purchase failed; remaining withdrawals cancelled.");
      }

      return var1 || var4 != 0;
   }

   public static void b(Minecraft var0) {
      V++;
      z(var0);
   }

   private static void z(Minecraft var0) {
      if (Q == null || Q == var0.player && x == var0.level && lj.j().B()) {
         if (!t) {
            if (!b.isEmpty() && !(var0.gui.screen() instanceof oy)) {
               b();
            }
         } else if (var0.player != null && var0.gameMode != null) {
            if (var0.gui.screen() instanceof InventoryScreen) {
               O = (boolean)1;
               var0.gui.setScreen(null);
            }

            if (var0.gui.screen() != null || var0.player.isDeadOrDying()) {
               q("Shop purchase cancelled.");
            } else if (V > K) {
               q(n ? "Shop purchase stopped: cannot read the server price (details in latest.log)." : "Shop purchase timed out; no retry was sent.");
            } else if (!d) {
               if (G) {
                  y(var0);
               } else if (J != null && var0.player.containerMenu == J) {
                  if (v) {
                     if (X <= r || o(s) < y + 64 || !J.getCarried().isEmpty()) {
                        return;
                     }

                     R.computeIfPresent(s, (var0x, var1) -> var1 - 1);
                     F.merge(s, 64, Integer::sum);
                     E = V + 20;
                     v = (boolean)0;
                     K = V + 100;
                  }

                  s = R.entrySet().stream().filter(var0x -> var0x.getValue() > 0).map(Entry::getKey).findFirst().orElse(null);
                  if (s == null) {
                     var0.player.closeContainer();
                     J = null;
                     G = (boolean)1;
                     K = V + 100;
                  } else {
                     for (Slot var2 : J.slots) {
                        if (var2.container != var0.player.getInventory() && var2.getItem().is(s)) {
                           ItemLore var3 = (ItemLore)var2.getItem().get(DataComponents.LORE);
                           List var4 = var3 == null ? List.of() : var3.lines().stream().map(Component::getString).toList();
                           x7.y var5 = x7.A(var4);
                           if (var5 == x7.y.N) {
                              if (J.getCarried().isEmpty() && V(s) >= 64) {
                                 n = (boolean)0;
                                 y = o(s);
                                 r = X;
                                 v = (boolean)1;
                                 E = V + 120;
                                 K = V + 100;
                                 var0.gameMode.handleContainerInput(J.containerId, var2.index, 0, ContainerInput.PICKUP, var0.player);
                                 return;
                              }

                              q("Shop purchase stopped: inventory or cursor is full.");
                              return;
                           }

                           if (var5 == x7.y.S && !n) {
                              r4.m("FakeSpawner", "Waiting for shop price for {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(s), var4);
                           }

                           if (var5 != x7.y.S) {
                              r4.m("FakeSpawner", "Purchase blocked for {}: {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(s), var5, var4);
                              q("Shop purchase blocked: server listing is above $3M.");
                              return;
                           }

                           n = (boolean)1;
                        }
                     }
                  }
               } else {
                  q("Shop closed before purchases completed.");
               }
            }
         } else {
            b();
         }
      } else {
         q(null);
         E = 0;
      }
   }

   private static int o(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.getItem(var3).is(var0)) {
            var2 += var1.getItem(var3).getCount();
         }
      }

      return var2;
   }

   private static int V(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (!lj.j().U(var3)) {
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

   private static int C(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   private static void y(Minecraft var0) {
      if (var0.player.containerMenu != var0.player.inventoryMenu) {
         q("Inventory changed; purchased loot kept.");
      } else if (!rm.L()) {
         if (W >= 0) {
            if (var0.player.getInventory().getItem(W).getCount() < L || !var0.player.inventoryMenu.getCarried().isEmpty()) {
               return;
            }

            W = -1;
         }

         if (var0.player.inventoryMenu.getCarried().isEmpty()) {
            for (Entry var2 : b.entrySet()) {
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
                              int var9 = b.containsKey(var7) && b.get(var7).is(var8.getItem()) ? b.get(var7).getCount() : 0;
                              int var10 = Math.min(var6, var8.getCount() - var9);
                              if (var10 > 0) {
                                 W = var3;
                                 L = var5.getCount() + var10;
                                 r = X;
                                 K = V + 100;
                                 int var11 = var8.getCount();
                                 rm.H(
                                    "arrange purchased loot",
                                    rm.M(var7, var3, var10, var11),
                                    mx::u,
                                    () -> q("Inventory corrected; purchased loot kept in its real slots.")
                                 );
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }

            q(null);
         }
      }
   }

   private static void q(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = O && var1.player == Q ? 1 : 0;
      if (e != null && var1.player == Q && var1.level == x) {
         LinkedHashMap var3 = new LinkedHashMap();

         for (ItemStack var5 : b.values()) {
            var3.merge(var5.getItem(), var5.getCount(), Integer::sum);
         }

         var3.forEach((var0x, var1x) -> lj.j().B(e, var0x, Math.min(var1x, F.getOrDefault(var0x, 0))));
      }

      if (var1.player == Q && J != null && var1.player.containerMenu == J) {
         var1.player.closeContainer();
      }

      b();
      E = V + 20;
      if (var0 != null && var1.player != null) {
         jb.v(Component.literal(var0), false);
      }

      if (var2 != 0 && var1.player != null) {
         var1.gui.setScreen(new InventoryScreen(var1.player));
      }
   }

   public static void H() {
      q(null);
      E = 0;
   }

   private static void b() {
      b.clear();
      R.clear();
      F.clear();
      J = null;
      e = null;
      x = null;
      Q = null;
      Y = (boolean)0;
      O = (boolean)0;
      n = (boolean)0;
      G = (boolean)0;
      v = (boolean)0;
      d = (boolean)0;
      t = (boolean)0;
      W = -1;
      mx.u();
   }
}
