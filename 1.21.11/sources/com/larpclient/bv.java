package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larpclient.module.Module;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.ToIntFunction;
import net.minecraft.class_1268;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_2350.class_2352;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class bv {
   private static s4 l;
   private static class_2338 m;
   private static Object p;
   private static Object B;
   private static int A = -1;
   private static int e = -1;
   private static int o;
   private static class_1799 j = class_1799.field_8037;
   private static boolean i;
   private static class_2338 y;
   private static class_2338 N;
   private static final ArrayDeque<bv.d> C = new ArrayDeque<>();
   private static int w;
   private static final class_2338 h = new class_2338(-1, -1, -1);
   private static int v;
   private static int Z;
   private static int V;
   static final int r = 10;
   static final int T = 200;
   static final double G = 0.03;
   static final double U = 0.3;
   public static final int z = 3;
   static final int S = 64;
   private static final Map<class_2338, Integer> b = new HashMap<>();

   private static void H(class_2338 var0, String var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null) {
         if (C.size() >= 80) {
            C.removeFirst();
         }

         C.addLast(new bv.d(var2.field_1724.field_6012, var0, var1));
      }
   }

   private static String J() {
      class_310 var0 = class_310.method_1551();
      q226fb0b431f37244 var1 = (q226fb0b431f37244)var0.field_1761;
      q586814c6d8f3640b var2 = (q586814c6d8f3640b)var0.field_1724;
      return String.format(
         Locale.ROOT,
         "%s %s p%.2f d%d slot%d/sent%d %s%s look %.1f/%.1f sent %.1f/%.1f",
         var1.larpIsDestroying() ? "digging" : "idle",
         var1.larpDestroyBlockPos() == null ? "-" : var1.larpDestroyBlockPos().method_23854(),
         var1.larpDestroyProgress(),
         var1.larpDestroyDelay(),
         var0.field_1724.method_31548().method_67532(),
         var1.larpCarriedIndex(),
         var0.field_1724.method_24828() ? "ground" : "air",
         var0.field_1724.method_5715() ? " sneak" : "",
         var0.field_1724.method_36454(),
         var0.field_1724.method_36455(),
         var2.larpYRotLast(),
         var2.larpXRotLast()
      );
   }

   public static void p() {
      class_310 var0 = class_310.method_1551();
      if (var0.field_1761 != null && var0.field_1724 != null) {
         q226fb0b431f37244 var1 = (q226fb0b431f37244)var0.field_1761;
         if (var1.larpIsDestroying() && var1.larpDestroyBlockPos() != null) {
            H(var1.larpDestroyBlockPos(), "vanilla abort (attack released) " + J());
         }
      }
   }

   public static void M(String var0, class_2338 var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null && w < 12) {
         w++;
         int var3 = var2.field_1724.field_6012;
         StringBuilder var4 = new StringBuilder();

         for (bv.d var6 : C) {
            if (var3 - var6.tick() <= 120) {
               var4.append(System.lineSeparator())
                  .append("  ")
                  .append(var6.tick() - var3)
                  .append(' ')
                  .append(var6.pos().equals(var1) ? "*" : " ")
                  .append(var6.pos().method_23854())
                  .append(' ')
                  .append(var6.text());
            }
         }

         q8.G(var0, "Dig trace before {} came back ({} of 12):{}", var1.method_23854(), w, var4);
      }
   }

   public static boolean P(class_2338 var0) {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1687 != null
         && var1.field_1724 != null
         && var1.field_1687.method_22340(var0)
         && !var1.field_1687.method_31606(var0)
         && var1.field_1687.method_8621().method_11952(var0)) {
         class_2680 var2 = var1.field_1687.method_8320(var0);
         return !var2.method_26215() && var2.method_26227().method_15769() && var2.method_26214(var1.field_1687, var0) >= 0.0F;
      } else {
         return false;
      }
   }

   public static class_3965 y(class_2338 var0, double var1) {
      return z(null, var0, var1);
   }

   public static class_3965 z(s4 var0, class_2338 var1, double var2) {
      return j(var0, var1, var2, 0);
   }

   static class_3965 j(s4 var0, class_2338 var1, double var2, int var4) {
      class_310 var5 = class_310.method_1551();
      if (!cr.o(var0)) {
         return null;
      }

      if (var4 == 0 && var0 != null && var0.aimsAtOpenFaces(var1)) {
         return n(var0, var1, var2);
      }

      if (var4 > 0) {
         ArrayList var15 = new ArrayList();

         for (class_243 var8 : K(var1)) {
            class_3965 var17 = J(var1, var8, var2);
            if (var17 != null
               && (var0 == null || var0.aimFaceAllowed(var1, var17.method_17780()))
               && var15.stream().noneMatch(var1x -> var1x.method_17784().method_1025(var17.method_17784()) < 0.01)) {
               var15.add(var17);
            }
         }

         return var15.isEmpty() ? null : (class_3965)var15.get(var4 % var15.size());
      } else {
         class_243 var6 = var5.field_1724.method_33571();
         double var7 = Math.min(var2, var5.field_1724.method_55754());
         class_265 var9 = var5.field_1687.method_8320(var1).method_26218(var5.field_1687, var1);
         ArrayList var10 = new ArrayList();
         if (!var9.method_1110()) {
            var10.add(var9.method_1107().method_1005().method_1031(var1.method_10263(), var1.method_10264(), var1.method_10260()));
         }

         for (class_2350 var14 : class_2350.values()) {
            var10.addAll(fk.n(var1, var14));
         }

         for (class_243 var19 : var10) {
            class_243 var20 = var19.method_1020(var6);
            if (!(var20.method_1027() > var7 * var7)) {
               class_3965 var21 = var5.field_1687
                  .method_17742(
                     new class_3959(
                        var6, var6.method_1019(var20.method_1029().method_1021(var7)), class_3960.field_17559, class_242.field_1348, var5.field_1724
                     )
                  );
               if (var21.method_17783() == class_240.field_1332 && var21.method_17777().equals(var1)) {
                  return var21;
               }
            }
         }

         return null;
      }
   }

   public static class_3965 v(class_2338 var0, double var1) {
      return n(null, var0, var1);
   }

   public static class_3965 n(s4 var0, class_2338 var1, double var2) {
      class_310 var4 = class_310.method_1551();
      double var5 = Math.min(var2, var4.field_1724.method_55754());
      if (var4.field_1724.method_33571().method_1025(class_243.method_24953(var1)) > var5 * var5) {
         return null;
      }

      List var7 = f(var4.field_1724.method_33571(), var1, var5);
      if (var0 != null) {
         EnumMap var8 = new EnumMap(class_2350.class);
         var7.removeIf(var3 -> !var8.computeIfAbsent(var3.hit().method_17780(), var2xx -> var0.aimFaceAllowed(var1, var2xx)));
         if (var0.aimsAtFaceCentre(var1)) {
            for (bv.s var10 : var7) {
               if (var10.index() % 9 == 0) {
                  return var10.hit();
               }
            }
         }
      }

      if (var7.isEmpty()) {
         return null;
      }

      Integer var18 = b.get(var1);
      if (var18 != null) {
         for (bv.s var21 : var7) {
            if (var21.index() == var18) {
               return var21.hit();
            }
         }
      }

      class_243 var20 = var4.field_1724.method_33571();
      class_243 var22 = var4.field_1724.method_5828(1.0F);
      bv.s var11 = null;
      double var12 = -2.0;

      for (bv.s var15 : var7) {
         double var16 = var15.hit().method_17784().method_1020(var20).method_1029().method_1026(var22);
         if (var16 > var12) {
            var12 = var16;
            var11 = var15;
         }
      }

      if (b.size() >= 64) {
         b.clear();
      }

      b.put(var1.method_10062(), var11.index());
      return var11.hit();
   }

   public static boolean j(class_243 var0, class_2338 var1, double var2) {
      return !f(var0, var1, var2).isEmpty();
   }

   private static List<bv.s> f(class_243 var0, class_2338 var1, double var2) {
      class_310 var4 = class_310.method_1551();
      ArrayList var5 = new ArrayList();
      class_243 var6 = class_243.method_24953(var1);

      for (class_2350 var10 : class_2350.values()) {
         class_243 var11 = class_243.method_24954(var10.method_62675());
         class_243 var12 = var6.method_1019(var11.method_1021(0.499));
         if (!(var0.method_1020(var12).method_1026(var11) <= 0.0)) {
            ArrayList var13 = new ArrayList(9);
            int var14 = 0;

            for (double var18 : new double[]{0.0, -0.3, 0.3}) {
               for (double var23 : new double[]{0.0, -0.3, 0.3}) {
                  int var25 = var10.ordinal() * 9 + var14++;

                  class_243 var26 = switch (var10.method_10166()) {
                     case field_11048 -> var12.method_1031(0.0, var18, var23);
                     case field_11052 -> var12.method_1031(var18, 0.0, var23);
                     case field_11051 -> var12.method_1031(var18, var23, 0.0);
                     default -> throw new MatchException(null, null);
                  };
                  class_243 var27 = var26.method_1020(var0);
                  if (!(var27.method_1027() > var2 * var2) && !(var27.method_1027() < 1.0E-8)) {
                     class_3965 var28 = var4.field_1687
                        .method_17742(
                           new class_3959(
                              var0, var0.method_1019(var27.method_1029().method_1021(var2)), class_3960.field_17559, class_242.field_1348, var4.field_1724
                           )
                        );
                     if (var28.method_17783() == class_240.field_1332 && var28.method_17777().equals(var1) && var28.method_17780() == var10) {
                        var13.add(new bv.s(var28, var25));
                     }
                  }
               }
            }

            if (var13.size() >= 3) {
               var5.addAll(var13);
            }
         }
      }

      return var5;
   }

   private static List<class_243> K(class_2338 var0) {
      class_310 var1 = class_310.method_1551();
      class_265 var2 = var1.field_1687.method_8320(var0).method_26218(var1.field_1687, var0);
      ArrayList var3 = new ArrayList();
      if (!var2.method_1110()) {
         var3.add(var2.method_1107().method_1005().method_1031(var0.method_10263(), var0.method_10264(), var0.method_10260()));
      }

      for (class_2350 var7 : class_2350.values()) {
         var3.addAll(fk.n(var0, var7));
      }

      return var3;
   }

   private static class_3965 J(class_2338 var0, class_243 var1, double var2) {
      class_310 var4 = class_310.method_1551();
      class_243 var5 = var4.field_1724.method_33571();
      class_243 var6 = var1.method_1020(var5);
      double var7 = Math.min(var2, var4.field_1724.method_55754());
      if (!(var6.method_1027() > var7 * var7) && !(var6.method_1027() < 1.0E-8)) {
         class_3965 var9 = var4.field_1687
            .method_17742(
               new class_3959(var5, var5.method_1019(var6.method_1029().method_1021(var7)), class_3960.field_17559, class_242.field_1348, var4.field_1724)
            );
         return var9.method_17783() == class_240.field_1332 && var9.method_17777().equals(var0) ? var9 : null;
      } else {
         return null;
      }
   }

   public static boolean f(class_2338 var0, class_2350 var1, double var2) {
      class_310 var4 = class_310.method_1551();
      class_746 var5 = var4.field_1724;
      if (var5 != null && var4.field_1687 != null) {
         q586814c6d8f3640b var6 = (q586814c6d8f3640b)var5;
         float var7 = var6.larpYRotLast();
         float var8 = var6.larpXRotLast();
         if (Float.isFinite(var7) && Float.isFinite(var8)) {
            class_243 var9 = class_243.method_1030(var8, var7);
            double var10 = Math.min(var2, var5.method_55754());
            class_238 var12 = new class_238(var0);
            class_243 var13 = new class_243(var6.larpXLast(), var6.larpYLast() + var5.method_5751(), var6.larpZLast());
            if (z(var12, var13, var9, var10) && z(var12, var5.method_33571(), var9, var10)) {
               class_265 var14 = var4.field_1687.method_8320(var0).method_26218(var4.field_1687, var0);
               return var1 == null || u(var14.method_1110() ? var12 : var14.method_1107().method_996(var0), var1, var13);
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

   public static boolean z(class_238 var0, class_243 var1, class_243 var2, double var3) {
      return var0.method_1014(0.03).method_1006(var1) || var0.method_992(var1, var1.method_1019(var2.method_1021(var3))).isPresent();
   }

   public static boolean u(class_238 var0, class_2350 var1, class_243 var2) {
      if (var0.method_1014(0.03).method_1006(var2)) {
         return true;
      }

      class_2351 var3 = var1.method_10166();
      double var4 = var3.method_10172(var2.field_1352, var2.field_1351, var2.field_1350);
      return var1.method_10171() == class_2352.field_11056 ? var4 + 0.03 >= var0.method_990(var3) : var4 - 0.03 <= var0.method_1001(var3);
   }

   public static boolean r(class_2338 var0, class_243 var1, double var2) {
      class_310 var4 = class_310.method_1551();
      class_243 var5 = var4.field_1724.method_33571();
      class_243 var6 = var1.method_1020(var5);
      double var7 = Math.min(var2, var4.field_1724.method_55754());
      if (!(var6.method_1027() > var7 * var7) && !(var6.method_1027() < 1.0E-8)) {
         class_3965 var9 = var4.field_1687
            .method_17742(
               new class_3959(var5, var5.method_1019(var6.method_1029().method_1021(var7)), class_3960.field_17559, class_242.field_1348, var4.field_1724)
            );
         return var9.method_17783() == class_240.field_1332 && var9.method_17777().equals(var0);
      } else {
         return false;
      }
   }

   public static class_3965 L(class_2338 var0, double var1) {
      class_310 var3 = class_310.method_1551();
      class_243 var4 = var3.field_1724.method_33571();
      class_3965 var5 = var3.field_1687
         .method_17742(
            new class_3959(
               var4,
               var4.method_1019(var3.field_1724.method_5720().method_1021(Math.min(var1, var3.field_1724.method_55754()))),
               class_3960.field_17559,
               class_242.field_1348,
               var3.field_1724
            )
         );
      return var5.method_17783() == class_240.field_1332 && var5.method_17777().equals(var0) ? var5 : null;
   }

   static boolean t(s4 var0, class_2338 var1, double var2, boolean var4, float var5, boolean var6, BooleanSupplier var7) {
      return q(var0, var1, var2, var4, var5, var6 ? bv::c : null, var7);
   }

   public static boolean q(s4 var0, class_2338 var1, double var2, boolean var4, float var5, ToIntFunction<class_2680> var6, BooleanSupplier var7) {
      int var8 = var6 != null ? 1 : 0;
      if (var0.r() && P(var1)) {
         class_310 var9 = class_310.method_1551();
         class_2338 var10 = var1.method_10062();
         class_638 var11 = var9.field_1687;
         class_746 var12 = var9.field_1724;
         int var13 = var9.field_1724.field_6012;
         class_2680 var14 = var9.field_1687.method_8320(var10);
         BooleanSupplier var15 = () -> var0.r()
            && var7.getAsBoolean()
            && fk.f(var11, var9.field_1687, var12, var9.field_1724, var13, var9.field_1724 == null ? -10 : var9.field_1724.field_6012)
            && P(var10)
            && var9.field_1687.method_8320(var10).equals(var14)
            && var9.field_1724.method_33571().method_1025(class_243.method_24953(var10)) <= var2 * var2;
         if (!var10.equals(y)) {
            y = var10;
            v = 0;
            Z = 0;
         } else if (L(var10, var2) != null && ++v > o(var14, var10)) {
            v = 0;
            Z++;
            q8.v(var0.getName(), "{} has not broken; aiming at another point of it ({})", var10.method_23854(), Z);
         }

         class_3965 var16 = var4 ? j(var0, var10, var2, Z) : null;
         if (var4 && var16 == null) {
            i(var0);
            return false;
         }

         if (var4 && cr.a(var0)) {
            var0.aimAt(var16.method_17784(), var5);
         }

         cr.y(
            var0,
            () -> {
               if (var15.getAsBoolean()) {
                  class_3965 var10x = var4 ? L(var10, var2) : null;
                  if (!var4 || var10x != null) {
                     if (l != null && l != var0) {
                        i(l);
                     }

                     if (var4 && !f(var10, var10x.method_17780(), var2)) {
                        H(var10, "hold: sent look misses " + J());
                        if (!var10.equals(N)) {
                           N = var10;
                           V = 0;
                        }

                        if (++V == 20) {
                           q8.G(var0.getName(), "Holding the break of {} for 20 ticks: the look the server has does not reach it", var10.method_23854());
                        }

                        if (l == var0) {
                           o = var9.field_1724.field_6012;
                        }
                     } else {
                        N = null;
                        if (l != null && var9.field_1724.method_31548().method_67532() != e) {
                           i(var0);
                        } else {
                           if (l == null) {
                              l = var0;
                              p = var9.field_1687;
                              B = var9.field_1724;
                              A = var9.field_1724.method_31548().method_67532();
                           }

                           if (!var10.equals(m)) {
                              if (m != null) {
                                 q8.v(var0.getName(), "Next block {}, {}, {}", var10.method_10263(), var10.method_10264(), var10.method_10260());
                              }

                              m = var10;
                              int var11x = var8 ? var6.applyAsInt(var14) : -1;
                              e = var11x >= 0 && var11x < 9 ? var11x : var9.field_1724.method_31548().method_67532();
                              j = var9.field_1724.method_31548().method_5438(e);
                           }

                           if (var8) {
                              var9.field_1724.method_31548().method_61496(e);
                           }

                           o = var9.field_1724.field_6012;
                           class_2350 var15x = var10x == null ? class_2350.field_11036 : var10x.method_17780();
                           q226fb0b431f37244 var12x = (q226fb0b431f37244)var9.field_1761;
                           if (!var12x.larpIsDestroying() && var10.equals(var12x.larpDestroyBlockPos()) && var12x.larpDestroyDelay() == 0) {
                              var12x.larpDestroyBlockPos(h);
                           }

                           String var13x = J();
                           boolean var14x = var9.field_1761.method_2902(var10, var15x);
                           if (var9.field_1687.method_8320(var10).method_26215()) {
                              var0.digFinished(var10);
                           }

                           H(
                              var10,
                              "dig "
                                 + var15x.method_15434()
                                 + (var14x ? " swing" : "")
                                 + (var9.field_1687.method_8320(var10).method_26215() ? " BROKEN" : "")
                                 + " | "
                                 + var13x
                                 + " -> "
                                 + J()
                           );
                           if (var14x) {
                              var9.field_1687.method_74254(var10, var15x);
                              w6.M(var9.field_1724, class_1268.field_5808);
                           }

                           var0.m("Mining " + var10.method_10263() + ", " + var10.method_10264() + ", " + var10.method_10260());
                        }
                     }
                  }
               }
            },
            () -> var15.getAsBoolean() && (!var4 || L(var10, var2) != null)
         );
         return true;
      } else {
         i(var0);
         return false;
      }
   }

   private static int o(class_2680 var0, class_2338 var1) {
      class_310 var2 = class_310.method_1551();
      float var3 = var0.method_26165(var2.field_1724, var2.field_1687, var1);
      int var4 = var3 <= 0.0F ? 200 : (int)Math.ceil(1.0F / var3);
      return Math.clamp(2L * var4, 10, 200);
   }

   public static int c(class_2680 var0) {
      class_1661 var1 = class_310.method_1551().field_1724.method_31548();
      int var2 = var1.method_67532();
      float var3 = var1.method_5438(var2).method_7924(var0) + (var1.method_5438(var2).method_7951(var0) ? 1000 : 0);

      for (int var4 = 0; var4 < 9; var4++) {
         class_1799 var5 = var1.method_5438(var4);
         float var6 = var5.method_7924(var0);
         if (var5.method_7951(var0)) {
            var6 += 1000.0F;
         }

         if (var6 > var3) {
            var3 = var6;
            var2 = var4;
         }
      }

      return var2;
   }

   public static boolean C() {
      class_310 var0 = class_310.method_1551();
      return !i
         && l != null
         && l.r()
         && var0.field_1687 == p
         && var0.field_1724 == B
         && var0.field_1724.field_6012 - o <= 1
         && P(m)
         && var0.field_1724.method_31548().method_67532() == e;
   }

   public static void i(Module var0) {
      if (l == var0) {
         class_310 var1 = class_310.method_1551();

         try {
            i = (boolean)1;
            if (var1.field_1687 == p && var1.field_1724 == B && var1.field_1761 != null) {
               if (!var1.field_1690.field_1886.method_1434()) {
                  if (m != null) {
                     H(m, "release (abort) " + J());
                  }

                  var1.field_1761.method_2925();
               }

               int var2 = var0 instanceof s4 var3 && var3.holdsToolBetweenBlocks() ? 1 : 0;
               if (var2 == 0
                  && !var1.field_1690.field_1886.method_1434()
                  && e != A
                  && var1.field_1724.method_31548().method_67532() == e
                  && var1.field_1724.method_31548().method_5438(e) == j) {
                  var1.field_1724.method_31548().method_61496(A);
               }
            }
         } finally {
            i = (boolean)0;
            l = null;
            m = null;
            p = null;
            B = null;
            N = null;
            y = null;
            V = 0;
            Z = 0;
            v = 0;
            A = -1;
            e = -1;
            j = class_1799.field_8037;
         }
      }
   }

   private bv() {
   }

   private record d(int tick, class_2338 pos, String text) {
   }

   private record s(class_3965 hit, int index) {
   }
}
