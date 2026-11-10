package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.class_1268;
import net.minecraft.class_1533;
import net.minecraft.class_1542;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_2775;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_638;

public final class i {
   private static class_638 X;
   private static final Map<UUID, class_1799> U = new HashMap<>();
   private static final List<class_1542> g = new ArrayList<>();
   private static final Map<Integer, class_1799> S = new HashMap<>();
   private static class_1799 m = class_1799.field_8037;
   private static class_1703 B;
   private static int a = -1900000000;

   private static void x(class_310 var0) {
      if (X != var0.field_1687) {
         if (var0.field_1724 != null) {
            for (Entry var2 : S.entrySet()) {
               if (var0.field_1724.method_31548().method_5438((Integer)var2.getKey()) == var2.getValue()) {
                  var0.field_1724.method_31548().method_5447((Integer)var2.getKey(), class_1799.field_8037);
               }
            }
         }

         if (B != null && B.method_34255() == m) {
            B.method_34254(class_1799.field_8037);
         }

         for (class_1542 var4 : g) {
            var4.method_31472();
         }

         U.clear();
         g.clear();
         S.clear();
         m = class_1799.field_8037;
         B = null;
         X = var0.field_1687;
      }
   }

   public static class_1799 t(class_1533 var0) {
      class_310 var1 = class_310.method_1551();
      x(var1);
      return var0.method_73183() == X ? U.get(var0.method_5667()) : null;
   }

   public static void O(class_1533 var0) {
      U.remove(var0.method_5667());
   }

   public static void S(class_310 var0, class_1533 var1) {
      x(var0);
      class_1799 var2 = var1.method_6940().method_7972();
      U.put(var1.method_5667(), class_1799.field_8037);
      if (!var2.method_7960()) {
         l(var0, var2.method_46651(1), new class_243(var1.method_23317(), var1.method_23318(), var1.method_23321()).method_1031(0.0, 0.15, 0.0));
      }
   }

   private static void l(class_310 var0, class_1799 var1, class_243 var2) {
      class_1542 var3 = new class_1542(var0.field_1687, var2.field_1352, var2.field_1351, var2.field_1350, var1.method_7972());
      var3.method_5838(a--);
      var3.method_6988();
      var3.method_35190();
      var0.field_1687.method_53875(var3);
      g.add(var3);
   }

   private static int C(class_310 var0, int var1, int var2) {
      for (int var3 = var1; var3 < var2; var3++) {
         if (var0.field_1724.method_31548().method_5438(var3).method_7960() && !S.containsKey(var3)) {
            return var3;
         }
      }

      return -1;
   }

   private static void n(class_310 var0, int var1, class_1799 var2) {
      S.put(var1, var2);
      var0.field_1724.method_31548().method_5447(var1, var2);
   }

   private static int D(class_1799 var0, class_1799 var1, int var2) {
      if (!class_1799.method_31577(var0, var1)) {
         return 0;
      }

      int var3 = Math.min(var2, Math.min(var1.method_7947(), Math.max(0, var0.method_7914() - var0.method_7947())));
      var0.method_7933(var3);
      var1.method_7934(var3);
      return var3;
   }

   private static void D(class_310 var0, class_1799 var1, int var2, int var3) {
      class_1799 var4 = S.get(40);
      if (var4 != null && var4 != var1 && var0.field_1724.method_31548().method_5438(40) == var4) {
         D(var4, var1, var1.method_7947());
      }

      for (int var5 = var2; var5 < var3 && !var1.method_7960(); var5++) {
         class_1799 var6 = S.get(var5);
         if (var6 != null && var0.field_1724.method_31548().method_5438(var5) == var6) {
            D(var6, var1, var1.method_7947());
         }
      }

      while (!var1.method_7960()) {
         int var7 = C(var0, var2, var3);
         if (var7 < 0) {
            return;
         }

         int var8 = Math.min(var1.method_7947(), var1.method_7914());
         n(var0, var7, var1.method_46651(var8));
         var1.method_7934(var8);
      }
   }

   private static void j(class_310 var0, int var1, class_1799 var2) {
      if (var2.method_7960()) {
         S.remove(var1);
         if (var0.field_1724.method_31548().method_5438(var1) == var2) {
            var0.field_1724.method_31548().method_5447(var1, class_1799.field_8037);
         }
      }
   }

   public static boolean R(class_310 var0, class_1268 var1) {
      return var0.field_1724 != null && S.containsKey(var1 == class_1268.field_5810 ? 40 : var0.field_1724.method_31548().method_67532());
   }

   public static void r(class_310 var0) {
      x(var0);
      if (var0.field_1687 != null && var0.field_1724 != null) {
         if (!m.method_7960() && B != var0.field_1724.field_7512) {
            e(var0);
         }

         for (Entry var2 : new ArrayList<>(S.entrySet())) {
            int var3 = (Integer)var2.getKey();
            class_1799 var4 = (class_1799)var2.getValue();
            class_1799 var5 = var0.field_1724.method_31548().method_5438(var3);
            if (var5 != var4) {
               if (var5.method_7960()) {
                  var0.field_1724.method_31548().method_5447(var3, var4);
               } else {
                  S.remove(var3);
                  D(var0, var4, 0, 36);
                  if (!var4.method_7960()) {
                     l(var0, var4, new class_243(var0.field_1724.method_23317(), var0.field_1724.method_23318(), var0.field_1724.method_23321()));
                  }
               }
            }
         }

         if (!m.method_7960() && B == var0.field_1724.field_7512) {
            if (B.method_34255().method_7960()) {
               B.method_34254(m);
            } else if (B.method_34255() != m) {
               e(var0);
            }
         }

         Iterator var6 = g.iterator();

         while (var6.hasNext()) {
            class_1542 var7 = (class_1542)var6.next();
            if (var7.method_31481()) {
               var6.remove();
            } else if (!var7.method_6977() && var7.method_5829().method_994(var0.field_1724.method_5829().method_1009(1.0, 0.5, 1.0))) {
               class_1799 var8 = var7.method_6983().method_7972();
               int var9 = var8.method_7947();
               D(var0, var8, 0, 36);
               int var10 = var9 - var8.method_7947();
               if (var10 != 0) {
                  var0.method_1562().method_11150(new class_2775(var7.method_5628(), var0.field_1724.method_5628(), var10));
                  if (var7.method_31481() || var7.method_6983().method_7960()) {
                     var7.method_31472();
                     var6.remove();
                  }
               }
            }
         }
      }
   }

   public static boolean r(class_310 var0, class_1533 var1, class_1268 var2) {
      x(var0);
      if (!var1.method_6940().method_7960()) {
         if (!U.containsKey(var1.method_5667()) && !R(var0, var2)) {
            return false;
         }

         var1.method_6939((var1.method_6934() + 1) % 8);
         var1.method_5783(var1.method_34244(), 1.0F, 1.0F);
         return true;
      } else {
         if (!R(var0, var2)) {
            return U.containsKey(var1.method_5667());
         }

         int var3 = var2 == class_1268.field_5810 ? 40 : var0.field_1724.method_31548().method_67532();
         class_1799 var4 = S.get(var3);
         U.put(var1.method_5667(), var4.method_46651(1));
         if (var4.method_7947() == 1) {
            S.remove(var3);
            if (var0.field_1724.method_31548().method_5438(var3) == var4) {
               var0.field_1724.method_31548().method_5447(var3, class_1799.field_8037);
            }
         } else {
            var4.method_7934(1);
         }

         var1.method_5783(class_3417.field_14667, 1.0F, 1.0F);
         return true;
      }
   }

   public static boolean B(class_310 var0, class_1533 var1) {
      x(var0);
      U.remove(var1.method_5667());
      return false;
   }

   public static boolean H(class_310 var0) {
      if (var0.field_1724 == null) {
         return false;
      }

      int var1 = var0.field_1724.method_31548().method_67532();
      class_1799 var2 = S.get(var1);
      class_1799 var3 = S.get(40);
      if (var2 == null && var3 == null) {
         return false;
      }

      if ((var2 != null || var0.field_1724.method_6047().method_7960()) && (var3 != null || var0.field_1724.method_6079().method_7960())) {
         S.remove(var1);
         S.remove(40);
         var0.field_1724.method_31548().method_5447(var1, class_1799.field_8037);
         var0.field_1724.method_31548().method_5447(40, class_1799.field_8037);
         if (var2 != null) {
            n(var0, 40, var2);
         }

         if (var3 != null) {
            n(var0, var1, var3);
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean E(class_310 var0, boolean var1) {
      if (!R(var0, class_1268.field_5808)) {
         return false;
      }

      int var2 = var0.field_1724.method_31548().method_67532();
      class_1799 var3 = S.get(var2);
      class_1799 var4 = var3.method_7971(var1 ? var3.method_7947() : 1);
      j(var0, var2, var3);
      l(var0, var4, var0.field_1724.method_33571().method_1019(var0.field_1724.method_5828(1.0F).method_1021(0.3)));
      return true;
   }

   public static void e(class_310 var0) {
      if (!m.method_7960() && var0.field_1724 != null) {
         if (B != null && B.method_34255() == m) {
            B.method_34254(class_1799.field_8037);
         }

         D(var0, m, 0, 36);
         if (!m.method_7960() && var0.field_1687 != null) {
            l(var0, m, new class_243(var0.field_1724.method_23317(), var0.field_1724.method_23318(), var0.field_1724.method_23321()));
         }

         m = class_1799.field_8037;
         B = null;
      }
   }

   public static boolean J(class_310 var0, int var1, int var2, class_1713 var3) {
      if (var0.field_1724 == null) {
         return false;
      }

      class_1703 var4 = var0.field_1724.field_7512;
      class_1735 var5 = var1 >= 0 && var1 < var4.field_7761.size() ? var4.method_7611(var1) : null;
      int var6 = var5 != null && var5.field_7871 == var0.field_1724.method_31548() ? var5.method_34266() : -1;
      class_1799 var7 = S.get(var6);
      if (var7 != null && var5.method_7677() != var7) {
         r(var0);
         return true;
      }

      int var8 = var3 == class_1713.field_7791 && S.containsKey(var2) ? 1 : 0;
      if (var7 == null && m.method_7960() && var8 == 0) {
         class_1799 var14 = var3 == class_1713.field_7793 ? var4.method_34255() : (var5 == null ? class_1799.field_8037 : var5.method_7677());
         if (var3 == class_1713.field_7793 || var3 == class_1713.field_7794) {
            for (class_1799 var11 : S.values()) {
               if (class_1799.method_31577(var14, var11)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         if (m.method_7960() && !var4.method_34255().method_7960()) {
            return true;
         }

         if (var3 == class_1713.field_7790) {
            if (m.method_7960()) {
               if (var7 != null && var4.method_34255().method_7960()) {
                  m = var7.method_7971(var2 == 1 ? (var7.method_7947() + 1) / 2 : var7.method_7947());
                  j(var0, var6, var7);
               }
            } else if (var6 >= 0 && (var6 < 36 || var6 == 40) && (var5.method_7677().method_7960() || var7 != null)) {
               if (var7 == null) {
                  n(var0, var6, m.method_7971(var2 == 1 ? 1 : m.method_7947()));
               } else if (class_1799.method_31577(var7, m)) {
                  D(var7, m, var2 == 1 ? 1 : m.method_7947());
               } else {
                  class_1799 var9 = var7;
                  n(var0, var6, m);
                  m = var9;
               }
            } else if (var1 == -999) {
               l(var0, m.method_7971(var2 == 1 ? 1 : m.method_7947()), var0.field_1724.method_33571());
            }

            if (m.method_7960()) {
               m = class_1799.field_8037;
            }

            var4.method_34254(m);
            B = m.method_7960() ? null : var4;
         } else if (var3 == class_1713.field_7794 && var7 != null) {
            D(var0, var7, var6 < 9 ? 9 : 0, var6 >= 9 && var6 != 40 ? 9 : 36);
            j(var0, var6, var7);
         } else if (var3 == class_1713.field_7791 && var6 >= 0 && (var6 < 36 || var6 == 40) && var2 >= 0 && (var2 < 9 || var2 == 40)) {
            class_1799 var13 = S.get(var2);
            if ((var7 != null || var5.method_7677().method_7960()) && (var13 != null || var0.field_1724.method_31548().method_5438(var2).method_7960())) {
               S.remove(var6);
               S.remove(var2);
               var0.field_1724.method_31548().method_5447(var6, class_1799.field_8037);
               var0.field_1724.method_31548().method_5447(var2, class_1799.field_8037);
               if (var7 != null) {
                  n(var0, var2, var7);
               }

               if (var13 != null) {
                  n(var0, var6, var13);
               }
            }
         } else if (var3 == class_1713.field_7795 && var7 != null) {
            class_1799 var12 = var7.method_7971(var2 == 0 ? 1 : var7.method_7947());
            j(var0, var6, var7);
            l(var0, var12, var0.field_1724.method_33571());
         }

         return true;
      }
   }
}
