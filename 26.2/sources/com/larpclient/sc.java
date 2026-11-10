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

public final class sc {
   private static Map<Integer, js.g> y = new HashMap<>();
   private static String l;
   private static long e;
   private static Path z;
   private static final Map<String, sc.a> W = new HashMap<>();
   private static Map<Integer, ItemStack> v = Map.of();
   private static Object d;
   private static boolean I;

   public static void H(ItemStack var0, int var1) {
      String var2 = gz.M(var0);
      sc.a var3 = W.get(var2);
      W.put(var2, new sc.a(Math.max(0, var1) + (var3 == null ? 0 : var3.count()), System.currentTimeMillis() + 10000L));
   }

   public static void r() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.isSameThread()) {
         if (!m3.u()) {
            if (var0.player != null && var0.level != null) {
               String var1 = var0.getCurrentServer() == null ? "local" : var0.getCurrentServer().ip;
               int var2 = !var1.equals(l) ? 1 : 0;
               if (var2 != 0) {
                  l = var1;
                  e = 0L;
                  y.clear();
                  z = FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-real-stacks-v2-" + gz.Z(l) + ".txt");

                  try {
                     if (Files.exists(z)) {
                        for (String var4 : Files.readAllLines(z)) {
                           String[] var5 = var4.split("\t");
                           if (var5.length == 4) {
                              y.put(Integer.parseInt(var5[0]), new js.g(var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3])));
                           }
                        }
                     }
                  } catch (IOException | NumberFormatException var18) {
                     h.g("FakeSpawner", "Could not load auction stack exemptions", var18);
                  }
               }

               long var19 = System.currentTimeMillis();
               boolean var20 = px.A();
               if (var20) {
                  e = var19 + 10000L;
               } else if (var0.player.containerMenu != var0.player.inventoryMenu) {
                  e = 0L;
               }

               HashMap var6 = new HashMap();
               Inventory var7 = var0.player.getInventory();

               for (int var8 = 0; var8 < var7.getContainerSize(); var8++) {
                  Z(var6, var8, var7.getItem(var8));
               }

               Z(var6, -1, var0.player.containerMenu.getCarried());
               if (!var20) {
                  for (Slot var9 : var0.player.containerMenu.slots) {
                     if (var9.container != var7) {
                        Z(var6, 1000 + var9.index, var9.getItem());
                     }
                  }
               }

               W.values().removeIf(var2x -> var2x.until() < var19);
               int var22 = var2 == 0
                     && d == var0.player.containerMenu
                     && I == var20
                     && var6.size() == v.size()
                     && var6.entrySet()
                        .stream()
                        .allMatch(var0x -> ItemStack.matches((ItemStack)var0x.getValue(), v.getOrDefault(var0x.getKey(), ItemStack.EMPTY)))
                  ? 1
                  : 0;
               if (var22 == 0) {
                  v = new HashMap<>();
                  var6.forEach((var0x, var1x) -> v.put(var0x, var1x.copy()));
                  d = var0.player.containerMenu;
                  I = var20;
                  HashMap var23 = new HashMap();
                  var6.forEach((var1x, var2x) -> {
                     String var3 = gz.M(var2x);
                     js.g var4x = y.get(var1x);
                     int var5x = gz.W(var2x) ? var2x.getCount() : 0;
                     if (var5x > 0 && var4x != null && var4x.item().equals(var3)) {
                        var5x = Math.min(var5x, var4x.purchased());
                     }

                     var23.put(var1x, new js.g(var3, var2x.getCount(), var5x));
                  });
                  if (var2 != 0) {
                     y.entrySet()
                        .removeIf(
                           var1x -> !var23.containsKey(var1x.getKey())
                              || !((js.g)var23.get(var1x.getKey())).item().equals(var1x.getValue().item())
                              || ((js.g)var23.get(var1x.getKey())).count() != var1x.getValue().count()
                        );
                  }

                  Map var10 = js.c(y, var23, var2 == 0 && var19 < e);
                  W.values().removeIf(var2x -> var2x.until() < var19);

                  for (Entry var12 : new TreeMap(var10).entrySet()) {
                     js.g var13 = (js.g)var12.getValue();
                     sc.a var14 = W.get(var13.item());
                     if (var14 != null) {
                        js.g var15 = y.get(var12.getKey());
                        int var16 = var13.count() - (var15 != null && var15.item().equals(var13.item()) ? var15.count() : 0);
                        int var17 = Math.min(Math.max(0, var16), Math.min(var14.count(), var13.count() - var13.purchased()));
                        if (var17 > 0) {
                           var10.put((Integer)var12.getKey(), new js.g(var13.item(), var13.count(), var13.purchased() + var17));
                           if (var17 == var14.count()) {
                              W.remove(var13.item());
                           } else {
                              W.put(var13.item(), new sc.a(var14.count() - var17, var14.until()));
                           }
                        }
                     }
                  }

                  var6.forEach((var1x, var2x) -> gz.k(var2x, ((js.g)var10.get(var1x)).purchased() == var2x.getCount()));
                  int var24 = !var10.equals(y) ? 1 : 0;
                  y = var10;
                  boolean var25 = L(var0);
                  if (var24 != 0 || var25) {
                     Z();
                  }
               }
            } else {
               l = null;
               y.clear();
               W.clear();
               e = 0L;
               v = Map.of();
               d = null;
            }
         }
      }
   }

   private static void Z() {
      try {
         Files.createDirectories(z.getParent());
         List var0 = y.entrySet()
            .stream()
            .filter(var0x -> var0x.getKey() < 1000 && var0x.getValue().purchased() > 0)
            .map(var0x -> var0x.getKey() + "\t" + var0x.getValue().item() + "\t" + var0x.getValue().count() + "\t" + var0x.getValue().purchased())
            .sorted()
            .toList();
         l7.O(z, String.join("\n", var0));
      } catch (IOException var1) {
         h.g("FakeSpawner", "Could not save auction stack exemptions", var1);
      }
   }

   private static boolean L(Minecraft var0) {
      if (var0.gameMode != null && var0.player.containerMenu == var0.player.inventoryMenu && var0.player.inventoryMenu.getCarried().isEmpty()) {
         for (Entry var2 : new TreeMap<>(y).entrySet()) {
            int var3 = (Integer)var2.getKey();
            js.g var4 = (js.g)var2.getValue();
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

               if (!b8.k().t(var3)) {
                  int var8 = var5;
                  return m3.j("separate purchased backing", m3.h(var3, var8, var4.purchased(), var4.count()), () -> {
                     gz.k(var0.player.getInventory().getItem(var8), true);
                     y.put(var3, new js.g(var4.item(), var4.count() - var4.purchased(), 0));
                     y.put(var8, new js.g(var4.item(), var4.purchased(), var4.purchased()));
                     v = Map.of();
                     Z();
                  }, () -> {
                     v = Map.of();
                     e = 0L;
                  });
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void Z(Map<Integer, ItemStack> var0, int var1, ItemStack var2) {
      if (!var2.isEmpty()) {
         String var3 = var2.getItem().builtInRegistryHolder().key().identifier().toString();
         if (b8.k().K(var2.getItem()) != null) {
            var0.put(var1, var2);
         }
      }
   }

   private record a(int count, long until) {
   }
}
