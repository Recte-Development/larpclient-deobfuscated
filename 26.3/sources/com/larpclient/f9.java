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

public final class f9 {
   private static Map<Integer, ak.k> k = new HashMap<>();
   private static String Z;
   private static long o;
   private static Path y;
   private static final Map<String, f9.p> e = new HashMap<>();
   private static Map<Integer, ItemStack> s = Map.of();
   private static Object d;
   private static boolean U;

   public static void Y(ItemStack var0, int var1) {
      String var2 = nn.b(var0);
      f9.p var3 = e.get(var2);
      e.put(var2, new f9.p(Math.max(0, var1) + (var3 == null ? 0 : var3.count()), System.currentTimeMillis() + 10000L));
   }

   public static void Z() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.isSameThread()) {
         if (!q7.M()) {
            if (var0.player != null && var0.level != null) {
               String var1 = var0.getCurrentServer() == null ? "local" : var0.getCurrentServer().ip;
               int var2 = !var1.equals(Z) ? 1 : 0;
               if (var2 != 0) {
                  Z = var1;
                  o = 0L;
                  k.clear();
                  y = FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-real-stacks-v2-" + nn.K(Z) + ".txt");

                  try {
                     if (Files.exists(y)) {
                        for (String var4 : Files.readAllLines(y)) {
                           String[] var5 = var4.split("\t");
                           if (var5.length == 4) {
                              k.put(Integer.parseInt(var5[0]), new ak.k(var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3])));
                           }
                        }
                     }
                  } catch (IOException | NumberFormatException var18) {
                     _q.R("FakeSpawner", "Could not load auction stack exemptions", var18);
                  }
               }

               long var19 = System.currentTimeMillis();
               boolean var20 = nb.C();
               if (var20) {
                  o = var19 + 10000L;
               } else if (var0.player.containerMenu != var0.player.inventoryMenu) {
                  o = 0L;
               }

               HashMap var6 = new HashMap();
               Inventory var7 = var0.player.getInventory();

               for (int var8 = 0; var8 < var7.getContainerSize(); var8++) {
                  o(var6, var8, var7.getItem(var8));
               }

               o(var6, -1, var0.player.containerMenu.getCarried());
               if (!var20) {
                  for (Slot var9 : var0.player.containerMenu.slots) {
                     if (var9.container != var7) {
                        o(var6, 1000 + var9.index, var9.getItem());
                     }
                  }
               }

               e.values().removeIf(var2x -> var2x.until() < var19);
               int var22 = var2 == 0
                     && d == var0.player.containerMenu
                     && U == var20
                     && var6.size() == s.size()
                     && var6.entrySet()
                        .stream()
                        .allMatch(var0x -> ItemStack.matches((ItemStack)var0x.getValue(), s.getOrDefault(var0x.getKey(), ItemStack.EMPTY)))
                  ? 1
                  : 0;
               if (var22 == 0) {
                  s = new HashMap<>();
                  var6.forEach((var0x, var1x) -> s.put(var0x, var1x.copy()));
                  d = var0.player.containerMenu;
                  U = var20;
                  HashMap var23 = new HashMap();
                  var6.forEach((var1x, var2x) -> {
                     String var3 = nn.b(var2x);
                     ak.k var4x = k.get(var1x);
                     int var5x = nn.i(var2x) ? var2x.getCount() : 0;
                     if (var5x > 0 && var4x != null && var4x.item().equals(var3)) {
                        var5x = Math.min(var5x, var4x.purchased());
                     }

                     var23.put(var1x, new ak.k(var3, var2x.getCount(), var5x));
                  });
                  if (var2 != 0) {
                     k.entrySet()
                        .removeIf(
                           var1x -> !var23.containsKey(var1x.getKey())
                              || !((ak.k)var23.get(var1x.getKey())).item().equals(var1x.getValue().item())
                              || ((ak.k)var23.get(var1x.getKey())).count() != var1x.getValue().count()
                        );
                  }

                  Map var10 = ak.r(k, var23, var2 == 0 && var19 < o);
                  e.values().removeIf(var2x -> var2x.until() < var19);

                  for (Entry var12 : new TreeMap(var10).entrySet()) {
                     ak.k var13 = (ak.k)var12.getValue();
                     f9.p var14 = e.get(var13.item());
                     if (var14 != null) {
                        ak.k var15 = k.get(var12.getKey());
                        int var16 = var13.count() - (var15 != null && var15.item().equals(var13.item()) ? var15.count() : 0);
                        int var17 = Math.min(Math.max(0, var16), Math.min(var14.count(), var13.count() - var13.purchased()));
                        if (var17 > 0) {
                           var10.put((Integer)var12.getKey(), new ak.k(var13.item(), var13.count(), var13.purchased() + var17));
                           if (var17 == var14.count()) {
                              e.remove(var13.item());
                           } else {
                              e.put(var13.item(), new f9.p(var14.count() - var17, var14.until()));
                           }
                        }
                     }
                  }

                  var6.forEach((var1x, var2x) -> nn.U(var2x, ((ak.k)var10.get(var1x)).purchased() == var2x.getCount()));
                  int var24 = !var10.equals(k) ? 1 : 0;
                  k = var10;
                  boolean var25 = w(var0);
                  if (var24 != 0 || var25) {
                     a();
                  }
               }
            } else {
               Z = null;
               k.clear();
               e.clear();
               o = 0L;
               s = Map.of();
               d = null;
            }
         }
      }
   }

   private static void a() {
      try {
         Files.createDirectories(y.getParent());
         List var0 = k.entrySet()
            .stream()
            .filter(var0x -> var0x.getKey() < 1000 && var0x.getValue().purchased() > 0)
            .map(var0x -> var0x.getKey() + "\t" + var0x.getValue().item() + "\t" + var0x.getValue().count() + "\t" + var0x.getValue().purchased())
            .sorted()
            .toList();
         ds.q(y, String.join("\n", var0));
      } catch (IOException var1) {
         _q.R("FakeSpawner", "Could not save auction stack exemptions", var1);
      }
   }

   private static boolean w(Minecraft var0) {
      if (var0.gameMode != null && var0.player.containerMenu == var0.player.inventoryMenu && var0.player.inventoryMenu.getCarried().isEmpty()) {
         for (Entry var2 : new TreeMap<>(k).entrySet()) {
            int var3 = (Integer)var2.getKey();
            ak.k var4 = (ak.k)var2.getValue();
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

               if (!jn.g().G(var3)) {
                  int var8 = var5;
                  return q7.B("separate purchased backing", q7.I(var3, var8, var4.purchased(), var4.count()), () -> {
                     nn.U(var0.player.getInventory().getItem(var8), true);
                     k.put(var3, new ak.k(var4.item(), var4.count() - var4.purchased(), 0));
                     k.put(var8, new ak.k(var4.item(), var4.purchased(), var4.purchased()));
                     s = Map.of();
                     a();
                  }, () -> {
                     s = Map.of();
                     o = 0L;
                  });
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void o(Map<Integer, ItemStack> var0, int var1, ItemStack var2) {
      if (!var2.isEmpty()) {
         String var3 = var2.getItem().builtInRegistryHolder().key().identifier().toString();
         if (jn.g().B(var2.getItem()) != null) {
            var0.put(var1, var2);
         }
      }
   }

   private record p(int count, long until) {
   }
}
