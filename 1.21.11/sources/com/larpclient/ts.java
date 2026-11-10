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
import net.minecraft.class_1661;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_310;

public final class ts {
   private static Map<Integer, iq.k> j = new HashMap<>();
   private static String c;
   private static long V;
   private static Path n;
   private static final Map<String, ts.e> k = new HashMap<>();
   private static Map<Integer, class_1799> i = Map.of();
   private static Object S;
   private static boolean I;

   public static void j(class_1799 var0, int var1) {
      String var2 = jn.x(var0);
      ts.e var3 = k.get(var2);
      k.put(var2, new ts.e(Math.max(0, var1) + (var3 == null ? 0 : var3.count()), System.currentTimeMillis() + 10000L));
   }

   public static void v() {
      class_310 var0 = class_310.method_1551();
      if (var0.method_18854()) {
         if (!wu.e()) {
            if (var0.field_1724 != null && var0.field_1687 != null) {
               String var1 = var0.method_1558() == null ? "local" : var0.method_1558().field_3761;
               int var2 = !var1.equals(c) ? 1 : 0;
               if (var2 != 0) {
                  c = var1;
                  V = 0L;
                  j.clear();
                  n = FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-real-stacks-v2-" + jn.J(c) + ".txt");

                  try {
                     if (Files.exists(n)) {
                        for (String var4 : Files.readAllLines(n)) {
                           String[] var5 = var4.split("\t");
                           if (var5.length == 4) {
                              j.put(Integer.parseInt(var5[0]), new iq.k(var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3])));
                           }
                        }
                     }
                  } catch (IOException | NumberFormatException var18) {
                     q8.L("FakeSpawner", "Could not load auction stack exemptions", var18);
                  }
               }

               long var19 = System.currentTimeMillis();
               boolean var20 = t0.L();
               if (var20) {
                  V = var19 + 10000L;
               } else if (var0.field_1724.field_7512 != var0.field_1724.field_7498) {
                  V = 0L;
               }

               HashMap var6 = new HashMap();
               class_1661 var7 = var0.field_1724.method_31548();

               for (int var8 = 0; var8 < var7.method_5439(); var8++) {
                  B(var6, var8, var7.method_5438(var8));
               }

               B(var6, -1, var0.field_1724.field_7512.method_34255());
               if (!var20) {
                  for (class_1735 var9 : var0.field_1724.field_7512.field_7761) {
                     if (var9.field_7871 != var7) {
                        B(var6, 1000 + var9.field_7874, var9.method_7677());
                     }
                  }
               }

               k.values().removeIf(var2x -> var2x.until() < var19);
               int var22 = var2 == 0
                     && S == var0.field_1724.field_7512
                     && I == var20
                     && var6.size() == i.size()
                     && var6.entrySet()
                        .stream()
                        .allMatch(var0x -> class_1799.method_7973((class_1799)var0x.getValue(), i.getOrDefault(var0x.getKey(), class_1799.field_8037)))
                  ? 1
                  : 0;
               if (var22 == 0) {
                  i = new HashMap<>();
                  var6.forEach((var0x, var1x) -> i.put(var0x, var1x.method_7972()));
                  S = var0.field_1724.field_7512;
                  I = var20;
                  HashMap var23 = new HashMap();
                  var6.forEach((var1x, var2x) -> {
                     String var3 = jn.x(var2x);
                     iq.k var4x = j.get(var1x);
                     int var5x = jn.N(var2x) ? var2x.method_7947() : 0;
                     if (var5x > 0 && var4x != null && var4x.item().equals(var3)) {
                        var5x = Math.min(var5x, var4x.purchased());
                     }

                     var23.put(var1x, new iq.k(var3, var2x.method_7947(), var5x));
                  });
                  if (var2 != 0) {
                     j.entrySet()
                        .removeIf(
                           var1x -> !var23.containsKey(var1x.getKey())
                              || !((iq.k)var23.get(var1x.getKey())).item().equals(var1x.getValue().item())
                              || ((iq.k)var23.get(var1x.getKey())).count() != var1x.getValue().count()
                        );
                  }

                  Map var10 = iq.T(j, var23, var2 == 0 && var19 < V);
                  k.values().removeIf(var2x -> var2x.until() < var19);

                  for (Entry var12 : new TreeMap(var10).entrySet()) {
                     iq.k var13 = (iq.k)var12.getValue();
                     ts.e var14 = k.get(var13.item());
                     if (var14 != null) {
                        iq.k var15 = j.get(var12.getKey());
                        int var16 = var13.count() - (var15 != null && var15.item().equals(var13.item()) ? var15.count() : 0);
                        int var17 = Math.min(Math.max(0, var16), Math.min(var14.count(), var13.count() - var13.purchased()));
                        if (var17 > 0) {
                           var10.put((Integer)var12.getKey(), new iq.k(var13.item(), var13.count(), var13.purchased() + var17));
                           if (var17 == var14.count()) {
                              k.remove(var13.item());
                           } else {
                              k.put(var13.item(), new ts.e(var14.count() - var17, var14.until()));
                           }
                        }
                     }
                  }

                  var6.forEach((var1x, var2x) -> jn.Y(var2x, ((iq.k)var10.get(var1x)).purchased() == var2x.method_7947()));
                  int var24 = !var10.equals(j) ? 1 : 0;
                  j = var10;
                  boolean var25 = B(var0);
                  if (var24 != 0 || var25) {
                     J();
                  }
               }
            } else {
               c = null;
               j.clear();
               k.clear();
               V = 0L;
               i = Map.of();
               S = null;
            }
         }
      }
   }

   private static void J() {
      try {
         Files.createDirectories(n.getParent());
         List var0 = j.entrySet()
            .stream()
            .filter(var0x -> var0x.getKey() < 1000 && var0x.getValue().purchased() > 0)
            .map(var0x -> var0x.getKey() + "\t" + var0x.getValue().item() + "\t" + var0x.getValue().count() + "\t" + var0x.getValue().purchased())
            .sorted()
            .toList();
         qu.d(n, String.join("\n", var0));
      } catch (IOException var1) {
         q8.L("FakeSpawner", "Could not save auction stack exemptions", var1);
      }
   }

   private static boolean B(class_310 var0) {
      if (var0.field_1761 != null && var0.field_1724.field_7512 == var0.field_1724.field_7498 && var0.field_1724.field_7498.method_34255().method_7960()) {
         for (Entry var2 : new TreeMap<>(j).entrySet()) {
            int var3 = (Integer)var2.getKey();
            iq.k var4 = (iq.k)var2.getValue();
            if (var3 >= 0 && var3 < 36 && var4.purchased() > 0 && var4.purchased() < var4.count()) {
               int var5 = -1;

               for (int var6 = 0; var6 < 36; var6++) {
                  if (var0.field_1724.method_31548().method_5438(var6).method_7960()) {
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

               if (!cz.T().W(var3)) {
                  int var8 = var5;
                  return wu.D("separate purchased backing", wu.F(var3, var8, var4.purchased(), var4.count()), () -> {
                     jn.Y(var0.field_1724.method_31548().method_5438(var8), true);
                     j.put(var3, new iq.k(var4.item(), var4.count() - var4.purchased(), 0));
                     j.put(var8, new iq.k(var4.item(), var4.purchased(), var4.purchased()));
                     i = Map.of();
                     J();
                  }, () -> {
                     i = Map.of();
                     V = 0L;
                  });
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void B(Map<Integer, class_1799> var0, int var1, class_1799 var2) {
      if (!var2.method_7960()) {
         String var3 = var2.method_7909().method_40131().method_40237().method_29177().toString();
         if (cz.T().V(var2.method_7909()) != null) {
            var0.put(var1, var2);
         }
      }
   }

   private record e(int count, long until) {
   }
}
