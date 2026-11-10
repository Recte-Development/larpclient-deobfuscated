package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_10185;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1542;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

public final class il {
   private static final sk S = new sk();
   private static final Map<String, Map<Integer, Integer>> P = new HashMap<>();
   private static final Map<Integer, class_1799> v = new HashMap<>();
   private static final Map<sk.f, class_1297> s = new HashMap<>();
   private static final Map<sk.x, il.s> u = new IdentityHashMap<>();
   private static class_638 Y;
   private static long H = Long.MIN_VALUE;
   private static Map<Integer, u0.i> d = Map.of();
   private static final Map<Integer, class_1799> C = new HashMap<>();
   private static boolean J;
   private static int B = -1;
   private static int c = -1;
   private static boolean k;

   private static Map<Integer, u0.i> W() {
      class_310 var0 = class_310.method_1551();
      if (var0.field_1724 != null && !S.O.isEmpty()) {
         int[] var1 = new int[36];
         int[] var2 = new int[36];

         for (int var3 = 0; var3 < 36; var3++) {
            class_1799 var4 = var0.field_1724.method_31548().method_5438(var3);
            var1[var3] = Z(var4);
            var2[var3] = var4.method_7947();
         }

         ArrayList var6 = new ArrayList();

         for (sk.x var5 : S.O) {
            if (var5.R != null && var5.R.kind().equals("inventory") && var5.R.slot() < 36) {
               var6.add(new u0.y(var5.R.slot(), var5.o, var5.z, 1));
            }
         }

         Map var8 = u0.U(var1, var2, var6, d);
         if (!var8.equals(d)) {
            d = var8;
            C.clear();
            var8.forEach((var1x, var2x) -> {
               if (var2x.count() == 0) {
                  C.put(var1x, class_1799.field_8037);
               } else {
                  class_1799 var3x = var0.field_1724.method_31548().method_5438(var2x.portions().getFirst().source()).method_46651(var2x.count());
                  var3x.method_57379(class_9334.field_49646, new class_9209(var2x.shown()));
                  C.put(var1x, var3x);
               }
            });
         }

         return d;
      } else {
         d = Map.of();
         C.clear();
         return d;
      }
   }

   public static int L(int var0) {
      if (k) {
         return var0;
      }

      B = var0;
      c = var0;
      u0.i var1 = W().get(var0);
      if (var1 != null && !var1.portions().isEmpty()) {
         u0.y var2 = var1.portions().getFirst();
         if (var2.source() >= 9) {
            return var0;
         }

         c = var2.source();
         S.a(j(var2.source()), var1.shown());
         return var2.source();
      } else {
         return var0;
      }
   }

   public static int I() {
      class_310 var0 = class_310.method_1551();
      if (var0.field_1724 == null) {
         return -1;
      }

      int var1 = var0.field_1724.method_31548().method_67532();
      if (!q7.L().isSwapActive() && !bk.W().E()) {
         if (B < 0 || B >= 9 || var1 != c) {
            B = var1;
            c = var1;
         }

         u0.i var2 = W().get(B);
         int var3 = var2 != null && !var2.portions().isEmpty() ? var2.portions().getFirst().source() : B;
         if (var3 < 9 && var3 != var1) {
            k = (boolean)1;

            try {
               var0.field_1724.method_31548().method_61496(var3);
               c = var3;
            } finally {
               k = (boolean)0;
            }
         }

         return B;
      } else {
         return var1;
      }
   }

   public static boolean p() {
      u0.i var0 = W().get(I());
      return var0 == null || var0.count() > 0;
   }

   public static boolean X(boolean var0) {
      if (!p()) {
         return false;
      }

      u0.i var1 = W().get(I());
      class_310 var2 = class_310.method_1551();
      return !var0 || var1 == null || var1.portions().size() == 1 && var1.count() == var2.field_1724.method_6047().method_7947();
   }

   public static boolean Q(int var0) {
      u0.i var1 = W().get(var0);
      return var1 != null && var1.portions().size() > 1;
   }

   public static void w() {
      class_310 var0 = class_310.method_1551();
      if (var0.field_1724 != null) {
         u0.i var1 = W().get(I());
         if (var1 != null) {
            S.a(j(var0.field_1724.method_31548().method_67532()), var1.shown());
         }
      }
   }

   private static int m(int var0) {
      class_310 var1 = class_310.method_1551();

      for (class_1735 var3 : var1.field_1724.field_7512.field_7761) {
         if (var3.field_7871 == var1.field_1724.method_31548() && var3.method_34266() == var0) {
            return var3.field_7874;
         }
      }

      return -1;
   }

   private static void h(int var0, int var1, class_1713 var2) {
      class_310 var3 = class_310.method_1551();
      var3.field_1761.method_2906(var3.field_1724.field_7512.field_7763, var0, var1, var2, var3.field_1724);
      s();
   }

   private static void r(u0.y var0, int var1) {
      class_310 var2 = class_310.method_1551();
      int var3 = m(var0.source());
      int var4 = var2.field_1724.method_31548().method_5438(var0.source()).method_7947();
      S.a(j(var0.source()), var0.shown());
      h(var3, 0, class_1713.field_7790);

      for (int var5 = var1; var5 < var4; var5++) {
         int var6 = Z(var2.field_1724.field_7512.method_34255());
         long var7 = S.O.stream().filter(var0x -> R().equals(var0x.R)).count();
         int var9 = var2.field_1724.field_7512.method_34255().method_7947() > var7 ? var6 : var0.shown();
         if (var9 == var0.shown()) {
            for (sk.x var11 : S.O) {
               if (R().equals(var11.R) && var11.z != var0.shown()) {
                  var9 = var11.z;
                  break;
               }
            }
         }

         S.a(R(), var9);
         h(var3, 1, class_1713.field_7790);
      }
   }

   public static void C(class_310 var0) {
      if (var0.field_1724 != null
         && var0.field_1761 != null
         && !S.O.isEmpty()
         && !J
         && !q7.L().isSwapActive()
         && !q7.L().blocksUserInteraction()
         && !ju.b().S()
         && !tw.s().x()
         && !q7.L().isSwapFreezingPassThrough()
         && var0.field_1724.field_7512.method_34255().method_7960()) {
         s();

         for (sk.x var2 : S.O) {
            if (var2.R != null && var2.R.kind().equals("inventory") && var2.R.slot() < 36) {
               int var3 = var2.R.slot();
               int var4 = var2.o;
               int var5 = var2.z;
               class_1799 var6 = var0.field_1724.method_31548().method_5438(var3);
               if (Z(var6) == var4) {
                  int var7 = (int)S.O.stream().filter(var3x -> j(var3).equals(var3x.R) && var3x.o == var4 && var3x.z == var5).count();
                  if (var7 < var6.method_7947()) {
                     int var8 = -1;

                     for (int var9 = 0; var9 < 36; var9++) {
                        if (var0.field_1724.method_31548().method_5438(var9).method_7960() && m(var9) >= 0) {
                           var8 = var9;
                           break;
                        }
                     }

                     if (var8 >= 0 && m(var3) >= 0) {
                        J = (boolean)1;

                        try {
                           h(m(var3), 0, class_1713.field_7790);

                           for (int var13 = 0; var13 < var7; var13++) {
                              S.a(R(), var5);
                              h(m(var8), 1, class_1713.field_7790);
                           }

                           if (!var0.field_1724.field_7512.method_34255().method_7960()) {
                              h(m(var3), 0, class_1713.field_7790);
                           }
                        } finally {
                           J = (boolean)0;
                        }

                        return;
                     }

                     return;
                  }
               }
            }
         }
      }
   }

   public static boolean U(int var0, int var1, int var2, class_1713 var3) {
      class_310 var4 = class_310.method_1551();
      if (!J && var4.field_1724 != null && !S.O.isEmpty() && !q7.L().isSwapActive() && !ju.b().S() && !tw.s().x()) {
         class_1703 var5 = var4.field_1724.field_7512;
         if (var5.field_7763 == var0 && var1 >= 0 && var1 < var5.field_7761.size()) {
            class_1735 var6 = var5.method_7611(var1);
            if (var6.field_7871 == var4.field_1724.method_31548() && var6.method_34266() >= 0 && var6.method_34266() < 36) {
               s();
               u0.i var7 = W().get(var6.method_34266());
               if (var7 == null) {
                  return false;
               }

               if (var3 != class_1713.field_7790 && var3 != class_1713.field_7794 && var3 != class_1713.field_7795) {
                  return true;
               }

               J = (boolean)1;

               try {
                  if (!var5.method_34255().method_7960()) {
                     if (var3 != class_1713.field_7790) {
                        return true;
                     }

                     class_1799 var19 = Y(var5.method_34255());
                     int var21 = -1;
                     if (var7.shown() == Z(var19)) {
                        for (u0.y var25 : var7.portions()) {
                           if (class_1799.method_31577(var4.field_1724.method_31548().method_5438(var25.source()), var5.method_34255())
                              && var4.field_1724.method_31548().method_5438(var25.source()).method_7947() < var5.method_34255().method_7914()) {
                              var21 = m(var25.source());
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && (var7.count() == 0 || var7.shown() == Z(var19))) {
                        for (int var23 = 0; var23 < 36; var23++) {
                           if (var4.field_1724.method_31548().method_5438(var23).method_7960()) {
                              var21 = m(var23);
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && var7.shown() != Z(var19) && !var7.portions().isEmpty()) {
                        u0.y var24 = var7.portions().getFirst();
                        class_1799 var26 = var4.field_1724.method_31548().method_5438(var24.source());
                        if (class_1799.method_31577(var26, var5.method_34255())) {
                           return true;
                        }

                        if (var24.count() == var26.method_7947()) {
                           var21 = m(var24.source());
                        }
                     }

                     if (var21 >= 0) {
                        h(var21, var2, var3);
                     }

                     return true;
                  } else {
                     if (var7.portions().isEmpty()) {
                        return true;
                     }

                     if (var3 == class_1713.field_7794) {
                        for (u0.y var9 : var7.portions()) {
                           int var10 = var4.field_1724.method_31548().method_5438(var9.source()).method_7947();
                           if (var10 == var9.count()) {
                              S.a(j(var9.source()), var9.shown());
                              h(m(var9.source()), 0, var3);
                           } else if (var5 instanceof class_1716) {
                              int var11 = -1;
                              class_1799 var12 = var4.field_1724.method_31548().method_5438(var9.source());

                              for (int var13 = 0; var13 < 9; var13++) {
                                 class_1799 var14 = var5.method_7611(var13).method_7677();
                                 if (!var14.method_7960() && class_1799.method_31577(var14, var12) && var14.method_7947() + var9.count() <= var14.method_7914()
                                    )
                                  {
                                    var11 = var13;
                                    break;
                                 }
                              }

                              if (var11 < 0) {
                                 for (int var27 = 0; var27 < 9; var27++) {
                                    if (var5.method_7611(var27).method_7677().method_7960()) {
                                       var11 = var27;
                                       break;
                                    }
                                 }
                              }

                              if (var11 >= 0) {
                                 r(var9, var9.count());
                                 h(var11, 0, class_1713.field_7790);
                                 if (!var5.method_34255().method_7960()) {
                                    h(m(var9.source()), 0, class_1713.field_7790);
                                 }
                              }
                           }
                        }
                     } else {
                        u0.y var18 = var7.portions().getFirst();
                        int var20 = var3 == class_1713.field_7795
                           ? (var2 == 0 ? 1 : var18.count())
                           : Math.min(var18.count(), var2 == 1 ? (var7.count() + 1) / 2 : var18.count());
                        r(var18, var20);
                        if (var3 == class_1713.field_7795) {
                           h(-999, 0, class_1713.field_7790);
                        }
                     }

                     return true;
                  }
               } finally {
                  J = (boolean)0;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static sk.f j(int var0) {
      return new sk.f("inventory", "", var0);
   }

   private static sk.f R() {
      return new sk.f("cursor", "", 0);
   }

   private static sk.f N(class_1297 var0) {
      return new sk.f("entity", var0.method_5845(), 0);
   }

   private static int Z(class_1799 var0) {
      class_9209 var1 = var0.method_31574(class_1802.field_8204) ? (class_9209)var0.method_58694(class_9334.field_49646) : null;
      return var1 == null ? -1 : var1.comp_2315();
   }

   private static void i(Map<sk.f, sk.t> var0, sk.f var1, class_1799 var2) {
      var0.put(var1, new sk.t(Z(var2), var2.method_7947()));
      if (Z(var2) >= 0) {
         v.put(Z(var2), var2.method_46651(1));
      }
   }

   private static String z(class_310 var0) {
      hu var1 = hu.f();
      return var0.field_1724 != null && var0.field_1724.field_7512 instanceof class_1716 var2 && var1.z() == var2.field_7763 ? var1.W() : null;
   }

   private static Map<sk.f, sk.t> g(class_310 var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (var0.field_1724 != null && var0.field_1687 != null) {
         for (int var2 = 0; var2 < var0.field_1724.method_31548().method_5439(); var2++) {
            i(var1, j(var2), var0.field_1724.method_31548().method_5438(var2));
         }

         i(var1, R(), var0.field_1724.field_7512.method_34255());
         String var7 = z(var0);
         if (var7 != null) {
            Map var3 = P.computeIfAbsent(var7, var0x -> new HashMap<>());

            for (int var4 = 0; var4 < 9; var4++) {
               class_1799 var5 = var0.field_1724.field_7512.method_7611(var4).method_7677();
               i(var1, new sk.f("stock", var7, var4), var5);
               if (Z(var5) >= 0) {
                  var3.put(Z(var5), var4);
               }
            }
         }

         for (class_1297 var10 : var0.field_1687.method_18112()) {
            if (var10 instanceof class_1533 || var10 instanceof class_1542) {
               sk.f var12 = N(var10);
               s.put(var12, var10);
               i(var1, var12, var10 instanceof class_1533 var6 ? var6.method_6940() : ((class_1542)var10).method_6983());
            }
         }

         for (Entry var11 : s.entrySet()) {
            if (((class_1297)var11.getValue()).method_31481()) {
               i(var1, (sk.f)var11.getKey(), class_1799.field_8037);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void A() {
      S.P();
      P.clear();
      v.clear();
      s.clear();
      u.clear();
      H = Long.MIN_VALUE;
      d = Map.of();
      C.clear();
      B = -1;
      c = -1;
   }

   public static void D(class_310 var0) {
      if (Y != var0.field_1687) {
         A();
         Y = var0.field_1687;
      }

      if (var0.field_1724 != null && var0.field_1687 != null && var0.field_1687.method_75260() != H) {
         H = var0.field_1687.method_75260();
         if (!S.O.isEmpty()) {
            S.n(g(var0));
         }

         u.keySet().retainAll(S.O);
         I();
      }
   }

   public static void s() {
      class_310 var0 = class_310.method_1551();
      D(var0);
      if (var0.field_1724 != null && var0.field_1687 != null && !S.O.isEmpty()) {
         S.n(g(var0));
      }
   }

   public static boolean K(class_310 var0) {
      class_10185 var1 = var0.field_1724.field_3913.field_54155;
      return !var0.field_1724.method_24828()
         || var0.field_1724.method_18798().method_37268() > 1.0E-6
         || var1.comp_3159()
         || var1.comp_3160()
         || var1.comp_3161()
         || var1.comp_3162()
         || var1.comp_3163()
         || var0.field_1690.field_1894.method_1434()
         || var0.field_1690.field_1881.method_1434()
         || var0.field_1690.field_1913.method_1434()
         || var0.field_1690.field_1849.method_1434()
         || var0.field_1690.field_1903.method_1434();
   }

   public static boolean S(q7 var0, int var1, int var2) {
      class_310 var3 = class_310.method_1551();
      D(var3);
      String var4 = var0.O();
      if (var4 != null && var1 >= 0 && var3.field_1724 != null) {
         class_1799 var5 = var3.field_1724.method_31548().method_5438(var1);
         int var6 = Z(var5);
         int var7 = var0.getTemplateIdForValue(var2);
         if (var6 >= 0 && var7 >= 0 && var6 != var7) {
            Map var8 = P.computeIfAbsent(var4, var0x -> new HashMap<>());

            for (int var9 = 1; var9 <= 10; var9++) {
               int var10 = var0.getTemplateIdForValue(var9);
               int var11 = var0.f(var9);
               if (var11 < 0) {
                  var11 = var0.containerIndexForValue(var9);
               }

               if (var10 >= 0 && var11 >= 0 && var11 < 9) {
                  var8.putIfAbsent(var10, var11);
               }
            }

            class_1799 var12 = var5.method_46651(1);
            var12.method_57379(class_9334.field_49646, new class_9209(var7));
            v.putIfAbsent(var7, var12);
            S.S(var4, var6, var7, j(var1), g(var3));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static class_1799 x(sk.f var0, class_1799 var1) {
      sk.x var2 = S.l(var0, Z(var1));
      if (var2 == null) {
         return null;
      }

      il.s var3 = u.get(var2);
      if (var3 != null && class_1799.method_7973(var3.actual(), var1)) {
         return var3.visual();
      }

      class_1799 var4 = var1.method_7972();
      var4.method_57379(class_9334.field_49646, new class_9209(var2.z));
      u.put(var2, new il.s(var1.method_7972(), var4));
      return var4;
   }

   public static class_1799 W(int var0) {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var0 >= 0 && var0 < var1.field_1724.method_31548().method_5439() && !S.O.isEmpty()) {
         D(var1);
         if (var0 < 36) {
            W();
            return C.get(var0);
         } else {
            return x(j(var0), var1.field_1724.method_31548().method_5438(var0));
         }
      } else {
         return null;
      }
   }

   public static class_1799 k(String var0, class_1799 var1) {
      D(class_310.method_1551());

      for (sk.x var3 : S.O) {
         if (var3.R != null && !var3.l() && var3.o == Z(var1)) {
            class_1297 var4 = s.get(var3.R);
            if (var3.R.kind().equals("inventory") && var0.equals("inventory[" + var3.R.slot() + "]")
               || var4 instanceof class_1533 && var0.equals("item_frame[" + var4.method_5628() + "]")) {
               return x(var3.R, var1);
            }
         }
      }

      return var1;
   }

   public static class_1799 Y(class_1799 var0) {
      D(class_310.method_1551());
      class_1799 var1 = x(R(), var0);
      return var1 == null ? var0 : var1;
   }

   public static class_1799 o(class_1297 var0, class_1799 var1) {
      if (S.O.isEmpty()) {
         return var1;
      }

      D(class_310.method_1551());
      class_1799 var2 = x(N(var0), var1);
      if (var2 == null && Z(var1) >= 0) {
         s();
         var2 = x(N(var0), var1);
      }

      return var2 == null ? var1 : var2;
   }

   public static class_1799 K(class_1735 var0) {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var0 != null) {
         if (var0.field_7871 == var1.field_1724.method_31548()) {
            return W(var0.method_34266());
         }

         String var2 = z(var1);
         if (var2 != null && var0.field_7874 >= 0 && var0.field_7874 < 9 && !S.O.isEmpty()) {
            D(var1);
            Map var3 = P.getOrDefault(var2, Map.of());
            class_1799 var4 = var0.method_7677();
            int var5 = Z(var4);
            if (var5 < 0) {
               for (Entry var7 : var3.entrySet()) {
                  if ((Integer)var7.getValue() == var0.field_7874 && S.Z(var2, (Integer)var7.getKey()) > 0) {
                     var5 = (Integer)var7.getKey();
                     break;
                  }
               }
            }

            int var9 = var3.getOrDefault(var5, -1) == var0.field_7874 ? S.Z(var2, var5) : 0;
            if (var9 == 0) {
               return null;
            }

            class_1799 var10 = var4.method_7960() ? v.get(var5) : var4;
            if (var10 == null) {
               return null;
            }

            int var8 = Math.max(0, var4.method_7947() + var9);
            return var8 == 0 ? class_1799.field_8037 : var10.method_46651(var8);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void g(int var0, int var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null && var2.field_1724.field_7512.field_7763 == var0 && var1 >= 0 && var1 < 9) {
         String var3 = z(var2);
         if (var3 != null) {
            s();
            S.f(new sk.f("stock", var3, var1));
         }
      }
   }

   public static void z(int var0) {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var1.field_1724.field_7512.field_7763 == var0 && z(var1) != null) {
         s();
         S.b();
      }
   }

   private record s(class_1799 actual, class_1799 visual) {
   }
}
