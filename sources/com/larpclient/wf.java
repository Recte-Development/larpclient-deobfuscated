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

public final class wf {
   private static Map<Integer, hg.t> e = new HashMap<>();
   private static String D;
   private static long Y;
   private static Path i;
   private static final Map<String, wf.d> w = new HashMap<>();
   private static Map<Integer, ItemStack> I = Map.of();
   private static Object a;
   private static boolean o;

   public static void I(ItemStack var0, int var1) {
      String var2 = h2.m(var0);
      wf.d var3 = w.get(var2);
      w.put(var2, new wf.d(Math.max(0, var1) + (var3 == null ? 0 : var3.count()), System.currentTimeMillis() + 10000L));
   }

   public static void g() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.isSameThread()) {
         if (!wt.v()) {
            if (var0.player != null && var0.level != null) {
               String var1 = var0.getCurrentServer() == null ? "local" : var0.getCurrentServer().ip;
               int var2 = !var1.equals(D) ? 1 : 0;
               if (var2 != 0) {
                  D = var1;
                  Y = 0L;
                  e.clear();
                  i = FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-real-stacks-v2-" + h2.Z(D) + ".txt");

                  try {
                     if (Files.exists(i)) {
                        for (String var4 : Files.readAllLines(i)) {
                           String[] var5 = var4.split("\t");
                           if (var5.length == 4) {
                              e.put(Integer.parseInt(var5[0]), new hg.t(var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3])));
                           }
                        }
                     }
                  } catch (IOException | NumberFormatException var18) {
                     mt.T("FakeSpawner", "Could not load auction stack exemptions", var18);
                  }
               }

               long var19 = System.currentTimeMillis();
               boolean var20 = d1.v();
               if (var20) {
                  Y = var19 + 10000L;
               } else if (var0.player.containerMenu != var0.player.inventoryMenu) {
                  Y = 0L;
               }

               HashMap var6 = new HashMap();
               Inventory var7 = var0.player.getInventory();

               for (int var8 = 0; var8 < var7.getContainerSize(); var8++) {
                  m(var6, var8, var7.getItem(var8));
               }

               m(var6, -1, var0.player.containerMenu.getCarried());
               if (!var20) {
                  for (Slot var9 : var0.player.containerMenu.slots) {
                     if (var9.container != var7) {
                        m(var6, 1000 + var9.index, var9.getItem());
                     }
                  }
               }

               w.values().removeIf(var2x -> var2x.until() < var19);
               int var22 = var2 == 0
                     && a == var0.player.containerMenu
                     && o == var20
                     && var6.size() == I.size()
                     && var6.entrySet()
                        .stream()
                        .allMatch(var0x -> ItemStack.matches((ItemStack)var0x.getValue(), I.getOrDefault(var0x.getKey(), ItemStack.EMPTY)))
                  ? 1
                  : 0;
               if (var22 == 0) {
                  I = new HashMap<>();
                  var6.forEach((var0x, var1x) -> I.put(var0x, var1x.copy()));
                  a = var0.player.containerMenu;
                  o = var20;
                  HashMap var23 = new HashMap();
                  var6.forEach((var1x, var2x) -> {
                     String var3 = h2.m(var2x);
                     hg.t var4x = e.get(var1x);
                     int var5x = h2.M(var2x) ? var2x.getCount() : 0;
                     if (var5x > 0 && var4x != null && var4x.item().equals(var3)) {
                        var5x = Math.min(var5x, var4x.purchased());
                     }

                     var23.put(var1x, new hg.t(var3, var2x.getCount(), var5x));
                  });
                  if (var2 != 0) {
                     e.entrySet()
                        .removeIf(
                           var1x -> !var23.containsKey(var1x.getKey())
                              || !((hg.t)var23.get(var1x.getKey())).item().equals(var1x.getValue().item())
                              || ((hg.t)var23.get(var1x.getKey())).count() != var1x.getValue().count()
                        );
                  }

                  Map var10 = hg.E(e, var23, var2 == 0 && var19 < Y);
                  w.values().removeIf(var2x -> var2x.until() < var19);

                  for (Entry var12 : new TreeMap(var10).entrySet()) {
                     hg.t var13 = (hg.t)var12.getValue();
                     wf.d var14 = w.get(var13.item());
                     if (var14 != null) {
                        hg.t var15 = e.get(var12.getKey());
                        int var16 = var13.count() - (var15 != null && var15.item().equals(var13.item()) ? var15.count() : 0);
                        int var17 = Math.min(Math.max(0, var16), Math.min(var14.count(), var13.count() - var13.purchased()));
                        if (var17 > 0) {
                           var10.put((Integer)var12.getKey(), new hg.t(var13.item(), var13.count(), var13.purchased() + var17));
                           if (var17 == var14.count()) {
                              w.remove(var13.item());
                           } else {
                              w.put(var13.item(), new wf.d(var14.count() - var17, var14.until()));
                           }
                        }
                     }
                  }

                  var6.forEach((var1x, var2x) -> h2.f(var2x, ((hg.t)var10.get(var1x)).purchased() == var2x.getCount()));
                  int var24 = !var10.equals(e) ? 1 : 0;
                  e = var10;
                  boolean var25 = X(var0);
                  if (var24 != 0 || var25) {
                     A();
                  }
               }
            } else {
               D = null;
               e.clear();
               w.clear();
               Y = 0L;
               I = Map.of();
               a = null;
            }
         }
      }
   }

   private static void A() {
      try {
         Files.createDirectories(i.getParent());
         List var0 = e.entrySet()
            .stream()
            .filter(var0x -> var0x.getKey() < 1000 && var0x.getValue().purchased() > 0)
            .map(var0x -> var0x.getKey() + "\t" + var0x.getValue().item() + "\t" + var0x.getValue().count() + "\t" + var0x.getValue().purchased())
            .sorted()
            .toList();
         eg.o(i, String.join("\n", var0));
      } catch (IOException var1) {
         mt.T("FakeSpawner", "Could not save auction stack exemptions", var1);
      }
   }

   private static boolean X(Minecraft var0) {
      if (var0.gameMode != null && var0.player.containerMenu == var0.player.inventoryMenu && var0.player.inventoryMenu.getCarried().isEmpty()) {
         for (Entry var2 : new TreeMap<>(e).entrySet()) {
            int var3 = (Integer)var2.getKey();
            hg.t var4 = (hg.t)var2.getValue();
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

               if (!dr.z().t(var3)) {
                  int var8 = var5;
                  return wt.S("separate purchased backing", wt.G(var3, var8, var4.purchased(), var4.count()), () -> {
                     h2.f(var0.player.getInventory().getItem(var8), true);
                     e.put(var3, new hg.t(var4.item(), var4.count() - var4.purchased(), 0));
                     e.put(var8, new hg.t(var4.item(), var4.purchased(), var4.purchased()));
                     I = Map.of();
                     A();
                  }, () -> {
                     I = Map.of();
                     Y = 0L;
                  });
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void m(Map<Integer, ItemStack> var0, int var1, ItemStack var2) {
      if (!var2.isEmpty()) {
         String var3 = var2.getItem().builtInRegistryHolder().key().identifier().toString();
         if (dr.z().x(var2.getItem()) != null) {
            var0.put(var1, var2);
         }
      }
   }

   private record d(int count, long until) {
   }
}
