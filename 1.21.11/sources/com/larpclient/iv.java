package com.larpclient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_3944;
import net.minecraft.class_490;
import net.minecraft.class_7923;
import net.minecraft.class_9290;
import net.minecraft.class_9334;

public final class iv {
   private static final Map<Integer, class_1799> o = new LinkedHashMap<>();
   private static final Map<class_1792, Integer> Q = new LinkedHashMap<>();
   private static final Map<class_1792, Integer> M = new LinkedHashMap<>();
   private static Object r;
   private static Object a;
   private static class_2338 d;
   private static class_1703 z;
   private static boolean E;
   private static boolean F;
   private static boolean y;
   private static boolean t;
   private static boolean R;
   private static boolean J;
   private static int Y;
   private static int B;
   private static int n;
   private static int A;
   private static int x;
   private static int O;
   private static int s = -1;
   private static int q;
   private static class_1792 m;
   private static boolean Z;

   public static boolean m(int var0) {
      return o.containsKey(var0);
   }

   public static int I(class_1792 var0) {
      int var1 = 0;

      for (class_1799 var3 : o.values()) {
         if (var3.method_31574(var0)) {
            var1 += var3.method_7947();
         }
      }

      return var1;
   }

   public static class_1799 y(int var0, class_1799 var1) {
      if (!var1.method_7960()) {
         return var1;
      }

      class_1799 var2 = o.get(var0);
      if (var2 == null) {
         return var1;
      }

      int var3 = M.getOrDefault(var2.method_7909(), 0);

      for (Entry var5 : o.entrySet()) {
         if (((class_1799)var5.getValue()).method_31574(var2.method_7909())) {
            if ((Integer)var5.getKey() == var0) {
               int var6 = var2.method_7947() - Math.min(var3, var2.method_7947());
               return var6 <= 0 ? var1 : jn.Q(new class_1799(var2.method_7909(), var6));
            }

            var3 = Math.max(0, var3 - ((class_1799)var5.getValue()).method_7947());
         }
      }

      return var1;
   }

   public static int T(class_1792 var0, int var1) {
      class_310 var2 = class_310.method_1551();
      if (!E
         && var1 > 0
         && qh.I(var0)
         && var2.field_1724 != null
         && var2.field_1724.field_7512 == var2.field_1724.field_7498
         && var2.field_1724.field_7498.method_34255().method_7960()) {
         if (r != var2.field_1724 || a != var2.field_1687) {
            B();
         }

         r = var2.field_1724;
         a = var2.field_1687;

         for (int var3 = 0; var3 < 36; var3++) {
            int var4 = iy.E(var3);
            if (var4 >= 0 && !cz.T().W(var4) && var2.field_1724.method_31548().method_5438(var4).method_7960()) {
               class_1799 var5 = o.get(var4);
               if (var5 == null || var5.method_31574(var0) && var5.method_7947() < 64) {
                  int var6 = Math.min(var1, 64 - (var5 == null ? 0 : var5.method_7947()));
                  o.put(var4, new class_1799(var0, var6 + (var5 == null ? 0 : var5.method_7947())));
                  iy.e(var3, var4);
                  return var6;
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void k() {
      h(null);
   }

   public static void h(class_2338 var0) {
      class_310 var1 = class_310.method_1551();
      if (!o.isEmpty() && !E && var1.field_1724 != null) {
         if (var1.field_1724.field_7512 == var1.field_1724.field_7498 && var1.field_1724.field_7498.method_34255().method_7960()) {
            Q.clear();
            d = var0 == null ? null : var0.method_10062();

            for (class_1799 var3 : o.values()) {
               Q.merge(var3.method_7909(), var3.method_7947(), Integer::sum);
            }

            Q.replaceAll((var0x, var1x) -> y_.o(var1x));
            F = (boolean)1;
            E = (boolean)1;
            n = Y + 120;
            B = Y + 100;
            var1.field_1724.field_3944.method_45730("shop");
         } else {
            t("Empty your cursor before withdrawing loot.");
         }
      }
   }

   public static boolean z(class_3944 var0) {
      class_310 var1 = class_310.method_1551();
      if (!var1.method_18854() || !F || var1.field_1724 == null) {
         return false;
      }

      if (var1.field_1755 == null && var1.field_1724 == r && var1.field_1687 == a && Y <= B) {
         class_1703 var2 = var0.method_17593().method_17434(var0.method_17592(), var1.field_1724.method_31548());
         if (!(var2 instanceof class_1707)) {
            t("Unsupported shop menu.");
            return false;
         } else {
            z = var2;
            var1.field_1724.field_7512 = var2;
            F = (boolean)0;
            B = Y + 100;
            return true;
         }
      } else {
         t("Shop purchase stopped: another screen opened or the request expired.");
         return false;
      }
   }

   public static void c() {
      if (class_310.method_1551().method_18854()) {
         A++;
         if (E && y && !J) {
            J = (boolean)1;
            ie.w(0L, () -> {
               J = (boolean)0;
               if (E && y) {
                  X(class_310.method_1551());
               }
            });
         }
      }
   }

   public static boolean G() {
      return class_310.method_1551().method_18854() && Y < n;
   }

   public static boolean c(class_2561 var0, boolean var1) {
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
      if (var3 != 0 && E) {
         t("Shop purchase failed; remaining withdrawals cancelled.");
      }

      return var1 || var4 != 0;
   }

   public static void S(class_310 var0) {
      Y++;
      X(var0);
   }

   private static void X(class_310 var0) {
      if (r == null || r == var0.field_1724 && a == var0.field_1687 && cz.T().J()) {
         if (!E) {
            if (!o.isEmpty() && !(var0.field_1755 instanceof h_)) {
               B();
            }
         } else if (var0.field_1724 != null && var0.field_1761 != null) {
            if (var0.field_1755 instanceof class_490) {
               R = (boolean)1;
               var0.method_1507(null);
            }

            if (var0.field_1755 != null || var0.field_1724.method_29504()) {
               t("Shop purchase cancelled.");
            } else if (Y > B) {
               t(Z ? "Shop purchase stopped: cannot read the server price (details in latest.log)." : "Shop purchase timed out; no retry was sent.");
            } else if (!F) {
               if (t) {
                  z(var0);
               } else if (z != null && var0.field_1724.field_7512 == z) {
                  if (y) {
                     if (A <= x || w(m) < O + 64 || !z.method_34255().method_7960()) {
                        return;
                     }

                     Q.computeIfPresent(m, (var0x, var1) -> var1 - 1);
                     M.merge(m, 64, Integer::sum);
                     n = Y + 20;
                     y = (boolean)0;
                     B = Y + 100;
                  }

                  m = Q.entrySet().stream().filter(var0x -> var0x.getValue() > 0).map(Entry::getKey).findFirst().orElse(null);
                  if (m == null) {
                     var0.field_1724.method_7346();
                     z = null;
                     t = (boolean)1;
                     B = Y + 100;
                  } else {
                     for (class_1735 var2 : z.field_7761) {
                        if (var2.field_7871 != var0.field_1724.method_31548() && var2.method_7677().method_31574(m)) {
                           class_9290 var3 = (class_9290)var2.method_7677().method_58694(class_9334.field_49632);
                           List var4 = var3 == null ? List.of() : var3.comp_2400().stream().map(class_2561::getString).toList();
                           y_.z var5 = y_.u(var4);
                           if (var5 == y_.z.y) {
                              if (z.method_34255().method_7960() && J(m) >= 64) {
                                 Z = (boolean)0;
                                 O = w(m);
                                 x = A;
                                 y = (boolean)1;
                                 n = Y + 120;
                                 B = Y + 100;
                                 var0.field_1761.method_2906(z.field_7763, var2.field_7874, 0, class_1713.field_7790, var0.field_1724);
                                 return;
                              }

                              t("Shop purchase stopped: inventory or cursor is full.");
                              return;
                           }

                           if (var5 == y_.z.x && !Z) {
                              q8.L("FakeSpawner", "Waiting for shop price for {}. Server lore: {}", class_7923.field_41178.method_10221(m), var4);
                           }

                           if (var5 != y_.z.x) {
                              q8.L("FakeSpawner", "Purchase blocked for {}: {}. Server lore: {}", class_7923.field_41178.method_10221(m), var5, var4);
                              t("Shop purchase blocked: server listing is above $3M.");
                              return;
                           }

                           Z = (boolean)1;
                        }
                     }
                  }
               } else {
                  t("Shop closed before purchases completed.");
               }
            }
         } else {
            B();
         }
      } else {
         t(null);
         n = 0;
      }
   }

   private static int w(class_1792 var0) {
      class_1661 var1 = class_310.method_1551().field_1724.method_31548();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.method_5438(var3).method_31574(var0)) {
            var2 += var1.method_5438(var3).method_7947();
         }
      }

      return var2;
   }

   private static int J(class_1792 var0) {
      class_1661 var1 = class_310.method_1551().field_1724.method_31548();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (!cz.T().W(var3)) {
            class_1799 var4 = var1.method_5438(var3);
            if (var4.method_7960()) {
               var2 += 64;
            } else if (var4.method_31574(var0)) {
               var2 += Math.max(0, var4.method_7914() - var4.method_7947());
            }
         }
      }

      return var2;
   }

   private static int v(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   private static void z(class_310 var0) {
      if (var0.field_1724.field_7512 != var0.field_1724.field_7498) {
         t("Inventory changed; purchased loot kept.");
      } else if (!wu.e()) {
         if (s >= 0) {
            if (var0.field_1724.method_31548().method_5438(s).method_7947() < q || !var0.field_1724.field_7498.method_34255().method_7960()) {
               return;
            }

            s = -1;
         }

         if (var0.field_1724.field_7498.method_34255().method_7960()) {
            for (Entry var2 : o.entrySet()) {
               int var3 = (Integer)var2.getKey();
               class_1799 var4 = (class_1799)var2.getValue();
               class_1799 var5 = var0.field_1724.method_31548().method_5438(var3);
               if (var5.method_7960() || var5.method_31574(var4.method_7909())) {
                  int var6 = var4.method_7947() - var5.method_7947();
                  if (var6 > 0) {
                     for (int var7 = 0; var7 < 36; var7++) {
                        if (var7 != var3) {
                           class_1799 var8 = var0.field_1724.method_31548().method_5438(var7);
                           if (var8.method_31574(var4.method_7909())) {
                              int var9 = o.containsKey(var7) && o.get(var7).method_31574(var8.method_7909()) ? o.get(var7).method_7947() : 0;
                              int var10 = Math.min(var6, var8.method_7947() - var9);
                              if (var10 > 0) {
                                 s = var3;
                                 q = var5.method_7947() + var10;
                                 x = A;
                                 B = Y + 100;
                                 int var11 = var8.method_7947();
                                 wu.D(
                                    "arrange purchased loot",
                                    wu.F(var7, var3, var10, var11),
                                    iy::V,
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
      class_310 var1 = class_310.method_1551();
      int var2 = R && var1.field_1724 == r ? 1 : 0;
      if (d != null && var1.field_1724 == r && var1.field_1687 == a) {
         LinkedHashMap var3 = new LinkedHashMap();

         for (class_1799 var5 : o.values()) {
            var3.merge(var5.method_7909(), var5.method_7947(), Integer::sum);
         }

         var3.forEach((var0x, var1x) -> cz.T().R(d, var0x, Math.min(var1x, M.getOrDefault(var0x, 0))));
      }

      if (var1.field_1724 == r && z != null && var1.field_1724.field_7512 == z) {
         var1.field_1724.method_7346();
      }

      B();
      n = Y + 20;
      if (var0 != null && var1.field_1724 != null) {
         b1.q(class_2561.method_43470(var0), false);
      }

      if (var2 != 0 && var1.field_1724 != null) {
         var1.method_1507(new class_490(var1.field_1724));
      }
   }

   public static void b() {
      t(null);
      n = 0;
   }

   private static void B() {
      o.clear();
      Q.clear();
      M.clear();
      z = null;
      d = null;
      a = null;
      r = null;
      J = (boolean)0;
      R = (boolean)0;
      Z = (boolean)0;
      t = (boolean)0;
      y = (boolean)0;
      F = (boolean)0;
      E = (boolean)0;
      s = -1;
      iy.V();
   }
}
