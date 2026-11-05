package com.larpclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class jq {
   private static Map<Integer, _w.z> k = new HashMap<>();
   private static String n;
   private static long r;
   private static Path X;
   private static final Map<String, jq.b> R = new HashMap<>();
   private static Map<Integer, ItemStack> I = Map.of();
   private static Object M;
   private static boolean F;

   public static void l(ItemStack var0, int var1) {
      String var2 = ja.n(var0);
      jq.b var3 = R.get(var2);
      R.put(var2, new jq.b(Math.max(0, var1) + (var3 == null ? 0 : var3.count()), System.currentTimeMillis() + 10000L));
   }

   public static void X() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.isSameThread()) {
         if (!rm.L()) {
            if (var0.player != null && var0.level != null) {
               String var1 = var0.getCurrentServer() == null ? "local" : var0.getCurrentServer().ip;
               int var2 = !var1.equals(n) ? 1 : 0;
               if (var2 != 0) {
                  n = var1;
                  r = 0L;
                  k.clear();
                  X = FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-real-stacks-v2-" + ja.k(n) + ".txt");

                  try {
                     if (Files.exists(X)) {
                        for (String var4 : Files.readAllLines(X)) {
                           String[] var5 = var4.split("\t");
                           if (var5.length == 4) {
                              k.put(Integer.parseInt(var5[0]), new _w.z(var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3])));
                           }
                        }
                     }
                  } catch (IOException | NumberFormatException var18) {
                     r4.m("FakeSpawner", "Could not load auction stack exemptions", var18);
                  }
               }

               long var19 = System.currentTimeMillis();
               boolean var20 = hr.f();
               if (var20) {
                  r = var19 + 10000L;
               } else if (var0.player.containerMenu != var0.player.inventoryMenu) {
                  r = 0L;
               }

               HashMap var6 = new HashMap();
               Inventory var7 = var0.player.getInventory();

               for (int var8 = 0; var8 < var7.getContainerSize(); var8++) {
                  t(var6, var8, var7.getItem(var8));
               }

               t(var6, -1, var0.player.containerMenu.getCarried());
               if (!var20) {
                  for (Slot var9 : var0.player.containerMenu.slots) {
                     if (var9.container != var7) {
                        t(var6, 1000 + var9.index, var9.getItem());
                     }
                  }
               }

               R.values().removeIf(var2x -> var2x.until() < var19);
               int var22 = var2 == 0
                     && M == var0.player.containerMenu
                     && F == var20
                     && var6.size() == I.size()
                     && var6.entrySet()
                        .stream()
                        .allMatch(var0x -> ItemStack.matches((ItemStack)var0x.getValue(), I.getOrDefault(var0x.getKey(), ItemStack.EMPTY)))
                  ? 1
                  : 0;
               if (var22 == 0) {
                  I = new HashMap<>();
                  var6.forEach((var0x, var1x) -> I.put(var0x, var1x.copy()));
                  M = var0.player.containerMenu;
                  F = var20;
                  HashMap var23 = new HashMap();
                  var6.forEach((var1x, var2x) -> {
                     String var3 = ja.n(var2x);
                     _w.z var4x = k.get(var1x);
                     int var5x = ja.C(var2x) ? var2x.getCount() : 0;
                     if (var5x > 0 && var4x != null && var4x.item().equals(var3)) {
                        var5x = Math.min(var5x, var4x.purchased());
                     }

                     var23.put(var1x, new _w.z(var3, var2x.getCount(), var5x));
                  });
                  if (var2 != 0) {
                     k.entrySet()
                        .removeIf(
                           var1x -> !var23.containsKey(var1x.getKey())
                              || !((_w.z)var23.get(var1x.getKey())).item().equals(var1x.getValue().item())
                              || ((_w.z)var23.get(var1x.getKey())).count() != var1x.getValue().count()
                        );
                  }

                  Map var10 = _w.R(k, var23, var2 == 0 && var19 < r);
                  R.values().removeIf(var2x -> var2x.until() < var19);

                  for (Entry var12 : new TreeMap(var10).entrySet()) {
                     _w.z var13 = (_w.z)var12.getValue();
                     jq.b var14 = R.get(var13.item());
                     if (var14 != null) {
                        _w.z var15 = k.get(var12.getKey());
                        int var16 = var13.count() - (var15 != null && var15.item().equals(var13.item()) ? var15.count() : 0);
                        int var17 = Math.min(Math.max(0, var16), Math.min(var14.count(), var13.count() - var13.purchased()));
                        if (var17 > 0) {
                           var10.put((Integer)var12.getKey(), new _w.z(var13.item(), var13.count(), var13.purchased() + var17));
                           if (var17 == var14.count()) {
                              R.remove(var13.item());
                           } else {
                              R.put(var13.item(), new jq.b(var14.count() - var17, var14.until()));
                           }
                        }
                     }
                  }

                  var6.forEach((var1x, var2x) -> ja.x(var2x, ((_w.z)var10.get(var1x)).purchased() == var2x.getCount()));
                  int var24 = !var10.equals(k) ? 1 : 0;
                  k = var10;
                  boolean var25 = U(var0);
                  if (var24 != 0 || var25) {
                     U();
                  }
               }
            } else {
               n = null;
               k.clear();
               R.clear();
               r = 0L;
               I = Map.of();
               M = null;
            }
         }
      }
   }

   private static void U() {
      try {
         Files.createDirectories(X.getParent());
         List var0 = k.entrySet()
            .stream()
            .filter(var0x -> var0x.getKey() < 1000 && var0x.getValue().purchased() > 0)
            .map(var0x -> var0x.getKey() + "\t" + var0x.getValue().item() + "\t" + var0x.getValue().count() + "\t" + var0x.getValue().purchased())
            .sorted()
            .toList();
         pi.U(X, String.join("\n", var0));
      } catch (IOException var1) {
         r4.m("FakeSpawner", "Could not save auction stack exemptions", var1);
      }
   }

   private static boolean U(Minecraft var0) {
      if (var0.gameMode != null && var0.player.containerMenu == var0.player.inventoryMenu && var0.player.inventoryMenu.getCarried().isEmpty()) {
         for (Entry var2 : new TreeMap<>(k).entrySet()) {
            int var3 = (Integer)var2.getKey();
            _w.z var4 = (_w.z)var2.getValue();
            if (var3 >= 0 && var3 < 36 && var4.purchased() > 0 && var4.purchased() < var4.count()) {
               int var5 = -1;

               for (int var6 = 0; var6 < 36; var6++) {
                  if (var0.player.getInventory().getItem(var6).isEmpty()) {
                     var5 = var6;
                     break;
                  }
               }

               if (var5 < 0) {
                  return false;
               }

               if (var3 < 9) {
                  int var10000 = var3 + 36;
               }

               if (var5 < 9) {
                  int var9 = var5 + 36;
               }

               if (!lj.j().U(var3)) {
                  int var8 = var5;
                  return rm.H("separate purchased backing", rm.M(var3, var8, var4.purchased(), var4.count()), () -> {
                     ja.x(var0.player.getInventory().getItem(var8), true);
                     k.put(var3, new _w.z(var4.item(), var4.count() - var4.purchased(), 0));
                     k.put(var8, new _w.z(var4.item(), var4.purchased(), var4.purchased()));
                     I = Map.of();
                     U();
                  }, () -> {
                     I = Map.of();
                     r = 0L;
                  });
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void t(Map<Integer, ItemStack> var0, int var1, ItemStack var2) {
      if (!var2.isEmpty()) {
         String var3 = var2.getItem().builtInRegistryHolder().key().identifier().toString();
         if (lj.j().u(var2.getItem()) != null) {
            var0.put(var1, var2);
         }
      }
   }

   private record b(int count, long until) {
   }
}
