package com.larpclient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_1263;
import net.minecraft.class_1277;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_481.class_483;
import net.minecraft.class_481.class_484;

public final class iy {
   private static fz E = new fz();
   private static Object f;
   private static int[] c;
   private static int o;
   private static int A = -1;
   private static boolean j;
   private static class_1703 K;
   private static class_1703 h;
   private static final Map<class_1735, iy.n> v = new IdentityHashMap<>();
   private static final Map<class_1735, class_484> D = new IdentityHashMap<>();
   private static final class_1277 b = new class_1277(1);
   private static boolean Y = (boolean)1;
   private static Object X;
   private static long u;
   private static int C = -1;

   public static boolean m() {
      return wu.e();
   }

   public static void V() {
      Y = (boolean)1;
   }

   public static long g() {
      return u;
   }

   public static void j() {
      X = null;
      f = null;
      c = null;
      h = null;
      K = null;
      v.clear();
      D.clear();
      C = -1;
      A = -1;
      j = (boolean)0;
      E = new fz();
      Y = (boolean)1;
   }

   public static boolean n() {
      class_310 var0 = class_310.method_1551();
      return var0.field_1724 != null && cz.T().J();
   }

   public static void d() {
      Y = (boolean)1;
      u();
   }

   private static void u() {
      class_310 var0 = class_310.method_1551();
      if (!n()) {
         f = null;
         c = null;
      } else {
         if (var0.field_1724.field_7512.method_34255().method_7960()) {
            C = -1;
         }

         if (f != var0.field_1724) {
            f = var0.field_1724;
            E = new fz();
            c = null;
            A = var0.field_1724.method_31548().method_67532();
            o = A;
            j = (boolean)0;
            Y = (boolean)1;
            q8.v("FakeSpawner", "Visual inventory initialized; ordinary slot positions retained");
         }

         if (X != var0.field_1724.field_7512) {
            X = var0.field_1724.field_7512;
            Y = (boolean)1;
         }

         if (Y || c == null) {
            if (c == null || !m()) {
               long var1 = 0L;
               long var3 = 0L;

               for (int var5 = 0; var5 < 36; var5++) {
                  if (!var0.field_1724.method_31548().method_5438(var5).method_7960() || iv.m(var5) || cz.T().V(var5)) {
                     var1 |= 1L << var5;
                  }

                  if (cz.T().W(var5) && cz.T().m(var5, var0.field_1724.method_31548().method_5438(var5)) == 0) {
                     var3 |= 1L << var5;
                  }
               }

               Y = (boolean)0;
               int[] var6 = E.g(var1, var3);
               if (var6 != c) {
                  c = var6;
                  u++;
                  q8.v("FakeSpawner", "Mapping snapshot {} occupancy={} reserved={}", u, Long.toHexString(var1), Long.toHexString(var3));
               }
            }
         }
      }
   }

   public static int E(int var0) {
      u();
      return c != null && var0 >= 0 && var0 < 36 ? c[var0] : var0;
   }

   public static class_1799 p(int var0) {
      int var1 = E(var0);
      return var1 < 0 ? class_1799.field_8037 : I(var1, class_310.method_1551().field_1724.method_31548().method_5438(var1));
   }

   public static class_1799 I(int var0, class_1799 var1) {
      return cz.T().W(var0) ? cz.T().T(var0, var1) : cz.T().x(var0, iv.y(var0, var1));
   }

   public static int p(int var0) {
      u();
      if (!n()) {
         return var0;
      }

      if (var0 != A) {
         o = var0;
         j = (boolean)0;
      }

      A = var0;
      if (j && c != null && o >= 0 && o < 9 && c[o] < 0) {
         return o;
      }

      j = (boolean)0;
      if (c != null) {
         for (int var1 = 0; var1 < 9; var1++) {
            if (c[var1] == var0) {
               o = var1;
               break;
            }
         }
      }

      return o;
   }

   public static int f() {
      u();

      for (int var0 = 0; var0 < 36; var0++) {
         int var1 = c == null ? var0 : c[var0];
         if (var1 < 0 || class_310.method_1551().field_1724.method_31548().method_5438(var1).method_7960() && !cz.T().V(var1) && !iv.m(var1)) {
            return var0;
         }
      }

      return -1;
   }

   public static void i(int var0, class_1799 var1, int var2) {
      if (var2 >= 0) {
         E.s(var2, var0);
      }

      d();
   }

   public static void t(int var0, class_1799 var1) {
      i(var0, var1, f());
   }

   public static class_1735 x(class_1703 var0, class_1735 var1) {
      class_310 var2 = class_310.method_1551();
      if (var0 instanceof class_483) {
         if (n() && var1 instanceof class_484 var7 && !(var7.field_2898 instanceof iy.n)) {
            z(var0);
            return q(var1, x(var2.field_1724.field_7498, var7.field_2898));
         } else {
            return var1;
         }
      } else if (n() && !(var1 instanceof iy.n) && var1.field_7871 == var2.field_1724.method_31548() && var1.method_34266() >= 0 && var1.method_34266() < 36) {
         int var3 = E(var1.method_34266());
         class_1735 var4 = null;
         if (var3 >= 0) {
            for (class_1735 var6 : var0.field_7761) {
               if (var6.field_7871 == var1.field_7871 && var6.method_34266() == var3) {
                  var4 = var6;
                  break;
               }
            }
         }

         if (K != var0) {
            v.clear();
            D.clear();
            K = var0;
         }

         iy.n var8 = v.get(var1);
         if (var8 == null || var8.n != var4) {
            var8 = new iy.n(var1, var4);
            v.put(var1, var8);
         }

         return var8;
      } else {
         return var1;
      }
   }

   private static class_1735 q(class_1735 var0, class_1735 var1) {
      class_484 var2 = D.get(var0);
      if (var2 == null || var2.field_2898 != var1) {
         var2 = new class_484(var1, var0.method_34266(), var0.field_7873, var0.field_7872);
         var2.field_7874 = var0.field_7874;
         D.put(var0, var2);
      }

      return var2;
   }

   private static void z(class_1703 var0) {
      if (h != var0) {
         D.clear();
         h = var0;
      }
   }

   public static class_1735 a(class_1735 var0) {
      return var0 instanceof class_484 var1 ? var1.field_2898 : var0;
   }

   public static class_1735 i(class_1703 var0, class_1735 var1, Set<class_1735> var2) {
      if (n() && var0 instanceof class_483 && var1 instanceof class_484 var16) {
         z(var0);
         class_1723 var17 = class_310.method_1551().field_1724.field_7498;
         HashSet var18 = new HashSet();

         for (class_1735 var21 : var2) {
            var18.add(a(var21));
         }

         class_1735 var20 = i(var17, var16.field_2898, var18);
         if (!var2.isEmpty()) {
            var2.clear();

            for (class_1735 var23 : var18) {
               for (class_1735 var25 : var0.field_7761) {
                  class_1735 var26 = x(var0, var25);
                  if (a(var26) == var23) {
                     var2.add(var26);
                     break;
                  }
               }
            }
         }

         return q(var1, var20);
      } else {
         class_1735 var3 = x(var0, var1);
         if (var3 instanceof iy.n var4 && var4.n == null && !m() && var0.field_7763 >= 0 && !var0.method_34255().method_7960() && !jn.Z(var0.method_34255())) {
            class_1661 var5 = class_310.method_1551().field_1724.method_31548();

            for (int var6 = 0; var6 < 2; var6++) {
               for (class_1735 var8 : var0.field_7761) {
                  int var9 = var8.method_34266();
                  if (var8.field_7871 == var5
                     && var9 >= 0
                     && var9 < 36
                     && !var8.method_7681()
                     && (var6 != 0 || var9 != C)
                     && (var6 != 1 || var9 == C)
                     && !U(var9, var8.method_7677())
                     && var8.method_7680(var0.method_34255())
                     && var8.method_7682()
                     && var8.method_7676(var0.method_34255()) >= 1
                     && !var2.stream().anyMatch(var2x -> var2x.field_7871 == var5 && var2x.method_34266() == var9)) {
                     E.s(var4.r, var9);
                     V();
                     u();
                     if (!var2.isEmpty()) {
                        ArrayList var10 = new ArrayList(var2.size());

                        for (class_1735 var12 : var2) {
                           class_1735 var13 = var12;
                           if (var12.field_7871 == var5) {
                              for (class_1735 var15 : var0.field_7761) {
                                 if (var15.field_7871 == var5
                                    && var15.method_34266() >= 0
                                    && var15.method_34266() < 36
                                    && E(var15.method_34266()) == var12.method_34266()) {
                                    var13 = x(var0, var15);
                                    break;
                                 }
                              }
                           }

                           var10.add(var13);
                        }

                        var2.clear();
                        var2.addAll(var10);
                     }

                     q8.v("FakeSpawner", "Visual gap bound logical={} physical={} menu={}", var4.r, var9, var0.field_7763);
                     return x(var0, var1);
                  }
               }
            }

            return var3;
         } else {
            return var3;
         }
      }
   }

   public static class_1735 Q(class_1703 var0, class_1735 var1) {
      if (var1 instanceof iy.n var2 && var2.n != null) {
         for (class_1735 var4 : var0.field_7761) {
            if (var4.field_7871 == var1.field_7871 && var4.method_34266() == var1.method_34266()) {
               return new iy.n(new class_1735(var1.field_7871, var2.r, var1.field_7873, var1.field_7872), var4);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void U(class_1735 var0) {
      if (!m()) {
         if (var0 instanceof iy.n var1 && var1.n != null) {
            if (class_310.method_1551().field_1724.field_7512.method_34255().method_7960() && var1.method_7681()) {
               C = var1.n.method_34266();
            }

            E.s(var1.r, var1.n.method_34266());
            V();
         }
      }
   }

   private static boolean U(int var0, class_1799 var1) {
      return var0 < 0 || cz.T().W(var0) || iv.m(var0) || cz.T().V(var0);
   }

   public static boolean l(class_1735 var0) {
      return var0 instanceof iy.n var1 && (var1.n == null || U(var1.n.method_34266(), var1.n.method_7677()));
   }

   public static void e(int var0, int var1) {
      E.s(var0, var1);
      d();
   }

   public static void i(class_1703 var0, class_1735 var1, int var2) {
      if (!m()) {
         class_310 var3 = class_310.method_1551();
         if (var1 != null && !l(var1) && var3.field_1761 != null && var0.method_34255().method_7960()) {
            int var4 = E(var2);
            class_1735 var5 = null;

            for (class_1735 var7 : var0.field_7761) {
               if (var7.field_7871 == var3.field_1724.method_31548() && var7.method_34266() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.field_7874 != var1.field_7874 && !U(var4, var5.method_7677())) {
               if (var1.method_7674(var3.field_1724)
                  && var5.method_7674(var3.field_1724)
                  && (
                     var5.method_7677().method_7960()
                        || var1.method_7680(var5.method_7677()) && var5.method_7677().method_7947() <= var1.method_7676(var5.method_7677())
                  )
                  && (var1.method_7677().method_7960() || var5.method_7680(var1.method_7677()))) {
                  if (var4 < 9) {
                     if (!cz.T().R(var0, List.of(new wu.l(var1.field_7874, var4, class_1713.field_7791)))) {
                        var3.field_1761.method_2906(var0.field_7763, var1.field_7874, var4, class_1713.field_7791, var3.field_1724);
                     }
                  } else {
                     int var10 = 0;

                     while (var1.field_7871 == var3.field_1724.method_31548() && var1.method_34266() == var10) {
                        var10++;
                     }

                     int var11 = var10;
                     class_1735 var8 = var5;
                     if (cz.T().W(var11)) {
                        return;
                     }

                     List var9 = List.of(
                        new wu.l(var1.field_7874, var11, class_1713.field_7791),
                        new wu.l(var8.field_7874, var11, class_1713.field_7791),
                        new wu.l(var1.field_7874, var11, class_1713.field_7791)
                     );
                     if (!cz.T().R(var0, var9)) {
                        wu.o(var0, "visual number-key swap", var9, iy::V, iy::V);
                     }
                  }

                  E.s(var2, var4);
                  if (var1 instanceof iy.n) {
                     U(var1);
                  } else if (var1.field_7871 == var3.field_1724.method_31548()) {
                     E.s(var1.method_34266(), var1.method_34266());
                  }

                  d();
               }
            }
         }
      }
   }

   public static void S(int var0) {
      if (!m()) {
         class_310 var1 = class_310.method_1551();
         if (!n()) {
            var1.field_1724.method_31548().method_61496(var0);
         } else {
            int var2 = E(var0);
            if (var2 >= 0) {
               j = (boolean)0;
               if (var2 < 9) {
                  var1.field_1724.method_31548().method_61496(var2);
                  A = var2;
                  o = var0;
               } else if (var1.field_1761 != null
                  && var1.field_1724.field_7512 == var1.field_1724.field_7498
                  && var1.field_1724.field_7498.method_34255().method_7960()) {
                  for (int var6 = 0; var6 < 9; var6++) {
                     if (!cz.T().W(var6)) {
                        cz var8 = cz.T();
                        if (var1.field_1724.method_31548().method_5438(var2).method_31574(class_1802.field_8606)) {
                           var8.e(var6);
                        }

                        if (var1.field_1724.method_31548().method_5438(var6).method_31574(class_1802.field_8606)) {
                           var8.e(var2);
                        }

                        var1.field_1761.method_2906(0, var2, var6, class_1713.field_7791, var1.field_1724);
                        E.f(var2, var6);
                        V();
                        var1.field_1724.method_31548().method_61496(var6);
                        A = var6;
                        o = var0;
                        return;
                     }
                  }

                  if (cz.T().q(var2, var0)) {
                     E.f(var2, var0);
                     E.s(var0, var0);
                     var1.field_1724.method_31548().method_61496(var0);
                     A = var0;
                     o = var0;
                     d();
                  }
               }
            } else {
               int var3 = 0;

               int var4;
               while (true) {
                  if (var3 >= 36) {
                     var3 = var0;

                     for (int var7 = 0; var7 < 9; var7++) {
                        if (cz.T().W(var7)) {
                           var3 = var7;
                           break;
                        }
                     }

                     var1.field_1724.method_31548().method_61496(var3);
                     A = var3;
                     o = var0;
                     j = (boolean)1;
                     return;
                  }

                  if (!U(var3, var1.field_1724.method_31548().method_5438(var3)) && var1.field_1724.method_31548().method_5438(var3).method_7960()) {
                     var4 = var3;
                     if (var3 < 9) {
                        break;
                     }

                     var4 = var0;
                     if (cz.T().q(var3, var4)) {
                        E.f(var3, var4);
                        E.s(var0, var4);
                        V();
                        break;
                     }
                  }

                  var3++;
               }

               var1.field_1724.method_31548().method_61496(var4);
               A = var4;
               o = var0;
               j = (boolean)1;
            }
         }
      }
   }

   public static final class n extends class_1735 {
      private final class_1735 n;
      private final int r;

      n(class_1735 var1, class_1735 var2) {
         super((class_1263)(var2 == null ? iy.b : var2.field_7871), var2 == null ? 0 : var2.method_34266(), var1.field_7873, var1.field_7872);
         this.n = var2;
         this.r = var1.method_34266();
         this.field_7874 = var2 == null ? -1 : var2.field_7874;
      }

      public class_1799 method_7677() {
         return this.n == null ? class_1799.field_8037 : iy.I(this.n.method_34266(), this.n.method_7677());
      }

      public boolean method_7680(class_1799 var1) {
         return this.n != null && !iy.l(this) && this.n.method_7680(var1);
      }

      public boolean method_7674(class_1657 var1) {
         return this.n != null && (!iy.l(this) || cz.T().m(this.n.method_34266(), this.n.method_7677()) > 0) && this.n.method_7674(var1);
      }

      public int method_7675() {
         return this.n == null ? 0 : this.n.method_7675();
      }

      public int method_7676(class_1799 var1) {
         return this.n == null ? 0 : this.n.method_7676(var1);
      }

      public boolean method_7682() {
         return this.n != null && this.n.method_7682();
      }

      public class_2960 method_7679() {
         return this.n == null ? null : this.n.method_7679();
      }

      public boolean method_55059() {
         return this.n != null && this.n.method_55059();
      }
   }
}
