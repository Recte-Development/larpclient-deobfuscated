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

public final class _v {
   private static final Map<Integer, ItemStack> b = new LinkedHashMap<>();
   private static final Map<Item, Integer> R = new LinkedHashMap<>();
   private static final Map<Item, Integer> Z = new LinkedHashMap<>();
   private static Object W;
   private static Object Q;
   private static BlockPos S;
   private static AbstractContainerMenu k;
   private static boolean N;
   private static boolean C;
   private static boolean u;
   private static boolean v;
   private static boolean A;
   private static boolean B;
   private static int O;
   private static int j;
   private static int J;
   private static int D;
   private static int K;
   private static int a;
   private static int m = -1;
   private static int y;
   private static Item o;
   private static boolean l;

   public static boolean O(int var0) {
      return b.containsKey(var0);
   }

   public static int P(Item var0) {
      int var1 = 0;

      for (ItemStack var3 : b.values()) {
         if (var3.is(var0)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static ItemStack C(int var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         return var1;
      }

      ItemStack var2 = b.get(var0);
      if (var2 == null) {
         return var1;
      }

      int var3 = Z.getOrDefault(var2.getItem(), 0);

      for (Entry var5 : b.entrySet()) {
         if (((ItemStack)var5.getValue()).is(var2.getItem())) {
            if ((Integer)var5.getKey() == var0) {
               int var6 = var2.getCount() - Math.min(var3, var2.getCount());
               return var6 <= 0 ? var1 : nn.F(new ItemStack(var2.getItem(), var6));
            }

            var3 = Math.max(0, var3 - ((ItemStack)var5.getValue()).getCount());
         }
      }

      return var1;
   }

   public static int Y(Item var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!N
         && var1 > 0
         && ni.u(var0)
         && var2.player != null
         && var2.player.containerMenu == var2.player.inventoryMenu
         && var2.player.inventoryMenu.getCarried().isEmpty()) {
         if (W != var2.player || Q != var2.level) {
            H();
         }

         W = var2.player;
         Q = var2.level;

         for (int var3 = 0; var3 < 36; var3++) {
            int var4 = _8.t(var3);
            if (var4 >= 0 && !jn.g().G(var4) && var2.player.getInventory().getItem(var4).isEmpty()) {
               ItemStack var5 = b.get(var4);
               if (var5 == null || var5.is(var0) && var5.getCount() < 64) {
                  int var6 = Math.min(var1, 64 - (var5 == null ? 0 : var5.getCount()));
                  b.put(var4, new ItemStack(var0, var6 + (var5 == null ? 0 : var5.getCount())));
                  _8.k(var3, var4);
                  return var6;
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void U() {
      d(null);
   }

   public static void d(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!b.isEmpty() && !N && var1.player != null) {
         if (var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
            R.clear();
            S = var0 == null ? null : var0.immutable();

            for (ItemStack var3 : b.values()) {
               R.merge(var3.getItem(), var3.getCount(), Integer::sum);
            }

            R.replaceAll((var0x, var1x) -> pk.t(var1x));
            C = (boolean)1;
            N = (boolean)1;
            J = O + 120;
            j = O + 100;
            var1.player.connection.sendCommand("shop");
         } else {
            T("Empty your cursor before withdrawing loot.");
         }
      }
   }

   public static boolean P(ClientboundOpenScreenPacket var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!var1.isSameThread() || !C || var1.player == null) {
         return false;
      }

      if (var1.gui.screen() == null && var1.player == W && var1.level == Q && O <= j) {
         AbstractContainerMenu var2 = var0.getType().create(var0.getContainerId(), var1.player.getInventory());
         if (!(var2 instanceof ChestMenu)) {
            T("Unsupported shop menu.");
            return false;
         } else {
            k = var2;
            var1.player.containerMenu = var2;
            C = (boolean)0;
            j = O + 100;
            return true;
         }
      } else {
         T("Shop purchase stopped: another screen opened or the request expired.");
         return false;
      }
   }

   public static void g() {
      if (Minecraft.getInstance().isSameThread()) {
         D++;
         if (N && u && !B) {
            B = (boolean)1;
            qw.K(0L, () -> {
               B = (boolean)0;
               if (N && u) {
                  L(Minecraft.getInstance());
               }
            });
         }
      }
   }

   public static boolean k() {
      return Minecraft.getInstance().isSameThread() && O < J;
   }

   public static boolean i(Component var0, boolean var1) {
      if (!k()) {
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
      if (var3 != 0 && N) {
         T("Shop purchase failed; remaining withdrawals cancelled.");
      }

      return var1 || var4 != 0;
   }

   public static void h(Minecraft var0) {
      O++;
      L(var0);
   }

   private static void L(Minecraft var0) {
      if (W == null || W == var0.player && Q == var0.level && jn.g().a()) {
         if (!N) {
            if (!b.isEmpty() && !(var0.gui.screen() instanceof w2)) {
               H();
            }
         } else if (var0.player != null && var0.gameMode != null) {
            if (var0.gui.screen() instanceof InventoryScreen) {
               A = (boolean)1;
               var0.gui.setScreen(null);
            }

            if (var0.gui.screen() != null || var0.player.isDeadOrDying()) {
               T("Shop purchase cancelled.");
            } else if (O > j) {
               T(l ? "Shop purchase stopped: cannot read the server price (details in latest.log)." : "Shop purchase timed out; no retry was sent.");
            } else if (!C) {
               if (v) {
                  l(var0);
               } else if (k != null && var0.player.containerMenu == k) {
                  if (u) {
                     if (D <= K || h(o) < a + 64 || !k.getCarried().isEmpty()) {
                        return;
                     }

                     R.computeIfPresent(o, (var0x, var1) -> var1 - 1);
                     Z.merge(o, 64, Integer::sum);
                     J = O + 20;
                     u = (boolean)0;
                     j = O + 100;
                  }

                  o = R.entrySet().stream().filter(var0x -> var0x.getValue() > 0).map(Entry::getKey).findFirst().orElse(null);
                  if (o == null) {
                     var0.player.closeContainer();
                     k = null;
                     v = (boolean)1;
                     j = O + 100;
                  } else {
                     for (Slot var2 : k.slots) {
                        if (var2.container != var0.player.getInventory() && var2.getItem().is(o)) {
                           ItemLore var3 = (ItemLore)var2.getItem().get(DataComponents.LORE);
                           List var4 = var3 == null ? List.of() : var3.lines().stream().map(Component::getString).toList();
                           pk.l var5 = pk.T(var4);
                           if (var5 == pk.l.O) {
                              if (k.getCarried().isEmpty() && g(o) >= 64) {
                                 l = (boolean)0;
                                 a = h(o);
                                 K = D;
                                 u = (boolean)1;
                                 J = O + 120;
                                 j = O + 100;
                                 var0.gameMode.handleContainerInput(k.containerId, var2.index, 0, ContainerInput.PICKUP, var0.player);
                                 return;
                              }

                              T("Shop purchase stopped: inventory or cursor is full.");
                              return;
                           }

                           if (var5 == pk.l.B && !l) {
                              _q.R("FakeSpawner", "Waiting for shop price for {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(o), var4);
                           }

                           if (var5 != pk.l.B) {
                              _q.R("FakeSpawner", "Purchase blocked for {}: {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(o), var5, var4);
                              T("Shop purchase blocked: server listing is above $3M.");
                              return;
                           }

                           l = (boolean)1;
                        }
                     }
                  }
               } else {
                  T("Shop closed before purchases completed.");
               }
            }
         } else {
            H();
         }
      } else {
         T(null);
         J = 0;
      }
   }

   private static int h(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.getItem(var3).is(var0)) {
            var2 += var1.getItem(var3).getCount();
         }
      }

      return var2;
   }

   private static int g(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (!jn.g().G(var3)) {
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

   private static int j(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   private static void l(Minecraft var0) {
      if (var0.player.containerMenu != var0.player.inventoryMenu) {
         T("Inventory changed; purchased loot kept.");
      } else if (!q7.M()) {
         if (m >= 0) {
            if (var0.player.getInventory().getItem(m).getCount() < y || !var0.player.inventoryMenu.getCarried().isEmpty()) {
               return;
            }

            m = -1;
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
                                 m = var3;
                                 y = var5.getCount() + var10;
                                 K = D;
                                 j = O + 100;
                                 int var11 = var8.getCount();
                                 q7.B(
                                    "arrange purchased loot",
                                    q7.I(var7, var3, var10, var11),
                                    _8::S,
                                    () -> T("Inventory corrected; purchased loot kept in its real slots.")
                                 );
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }

            T(null);
         }
      }
   }

   private static void T(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = A && var1.player == W ? 1 : 0;
      if (S != null && var1.player == W && var1.level == Q) {
         LinkedHashMap var3 = new LinkedHashMap();

         for (ItemStack var5 : b.values()) {
            var3.merge(var5.getItem(), var5.getCount(), Integer::sum);
         }

         var3.forEach((var0x, var1x) -> jn.g().r(S, var0x, Math.min(var1x, Z.getOrDefault(var0x, 0))));
      }

      if (var1.player == W && k != null && var1.player.containerMenu == k) {
         var1.player.closeContainer();
      }

      H();
      J = O + 20;
      if (var0 != null && var1.player != null) {
         iw.V(Component.literal(var0), false);
      }

      if (var2 != 0 && var1.player != null) {
         var1.gui.setScreen(new InventoryScreen(var1.player));
      }
   }

   public static void T() {
      T(null);
      J = 0;
   }

   private static void H() {
      b.clear();
      R.clear();
      Z.clear();
      k = null;
      S = null;
      Q = null;
      W = null;
      B = (boolean)0;
      A = (boolean)0;
      l = (boolean)0;
      v = (boolean)0;
      u = (boolean)0;
      C = (boolean)0;
      N = (boolean)0;
      m = -1;
      _8.S();
   }
}
