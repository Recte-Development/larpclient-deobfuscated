package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494.qca336a9a679bb27a;
import com.larpclient.module.Module;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1750;
import net.minecraft.class_1799;
import net.minecraft.class_1827;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2269;
import net.minecraft.class_2272;
import net.minecraft.class_2286;
import net.minecraft.class_2304;
import net.minecraft.class_2309;
import net.minecraft.class_2312;
import net.minecraft.class_2315;
import net.minecraft.class_2323;
import net.minecraft.class_2338;
import net.minecraft.class_2346;
import net.minecraft.class_2349;
import net.minecraft.class_2350;
import net.minecraft.class_2401;
import net.minecraft.class_2404;
import net.minecraft.class_2415;
import net.minecraft.class_2426;
import net.minecraft.class_2428;
import net.minecraft.class_243;
import net.minecraft.class_2457;
import net.minecraft.class_2462;
import net.minecraft.class_2470;
import net.minecraft.class_2533;
import net.minecraft.class_2665;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2741;
import net.minecraft.class_2742;
import net.minecraft.class_2756;
import net.minecraft.class_2758;
import net.minecraft.class_2769;
import net.minecraft.class_2771;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_4969;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class uo {
   static final List<class_2769<?>> v = List.of(
      class_2741.field_12525,
      class_2741.field_12481,
      class_2741.field_12545,
      class_2741.field_12496,
      class_2741.field_12529,
      class_2741.field_12518,
      class_2741.field_12485,
      class_2741.field_12555,
      class_2741.field_28062,
      class_2741.field_23333,
      class_2741.field_12520
   );
   static final List<class_2758> j = List.of(
      class_2741.field_27220, class_2741.field_12543, class_2741.field_12509, class_2741.field_12536, class_2741.field_42835, class_2741.field_55829
   );
   static final float B = 3.0F;
   static final float Y = 9.0F;
   static final float C = 10.0F;
   static final class_2350[] m = new class_2350[]{
      class_2350.field_11033, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034, class_2350.field_11036
   };
   static final double E = 0.03;

   private uo() {
   }

   public static class_2680 B(String var0) {
      class_2960 var1 = class_2960.method_12829(n.s(var0));
      if (var1 == null) {
         return null;
      }

      Optional var2 = class_7923.field_41175.method_17966(var1);
      if (var2.isEmpty()) {
         return null;
      }

      class_2680 var3 = ((class_2248)var2.get()).method_9564();
      class_2689 var4 = ((class_2248)var2.get()).method_9595();

      for (Entry var6 : n.F(var0).entrySet()) {
         class_2769 var7 = var4.method_11663((String)var6.getKey());
         if (var7 != null) {
            var3 = I(var3, var7, (String)var6.getValue());
         }
      }

      return var3;
   }

   private static <T extends Comparable<T>> class_2680 I(class_2680 var0, class_2769<T> var1, String var2) {
      return var1.method_11900(var2).map(var2x -> (class_2680)var0.method_11657(var1, var2x)).orElse(var0);
   }

   public static class_2680 q(class_2680 var0, n.v var1) {
      class_2680 var2 = switch (var1.mirror()) {
         case I -> var0.method_26185(class_2415.field_11301);
         case J -> var0.method_26185(class_2415.field_11300);
         case M -> var0;
      };
      return var2.method_26186(Y(var1.rotation()));
   }

   public static class_2470 Y(int var0) {
      return switch (Math.floorMod(var0, 360)) {
         case 90 -> class_2470.field_11463;
         case 180 -> class_2470.field_11464;
         case 270 -> class_2470.field_11465;
         default -> class_2470.field_11467;
      };
   }

   public static class_1747 w(class_2680 var0) {
      return !(var0.method_26204().method_8389() instanceof class_1747 var1 && (var1.method_7711() == var0.method_26204() || var1 instanceof class_1827))
         ? null
         : var1;
   }

   public static boolean X(class_2680 var0) {
      return var0.method_28498(class_2741.field_12533) && var0.method_11654(class_2741.field_12533) == class_2756.field_12609
         || var0.method_28498(class_2741.field_12483) && var0.method_11654(class_2741.field_12483) == class_2742.field_12560;
   }

   public static class_2338 E(class_2680 var0, class_2338 var1) {
      if (var0.method_28498(class_2741.field_12533)) {
         return var0.method_11654(class_2741.field_12533) == class_2756.field_12607 ? var1.method_10084() : var1.method_10074();
      } else if (var0.method_28498(class_2741.field_12483) && var0.method_28498(class_2741.field_12481)) {
         class_2350 var2 = (class_2350)var0.method_11654(class_2741.field_12481);
         return var0.method_11654(class_2741.field_12483) == class_2742.field_12557 ? var1.method_10093(var2) : var1.method_10093(var2.method_10153());
      } else {
         return null;
      }
   }

   public static boolean v(class_2680 var0) {
      return var0.method_26204() instanceof class_2346;
   }

   public static String g(class_2680 var0) {
      if (var0.method_26204() instanceof class_2404) {
         return "fluids are not placed";
      } else {
         return w(var0) == null ? "no item places it directly" : null;
      }
   }

   public static boolean b(class_2680 var0) {
      if (var0.method_28498(class_2741.field_12485)) {
         return var0.method_11654(class_2741.field_12485) == class_2771.field_12682;
      }

      for (class_2758 var2 : j) {
         if (var0.method_28498(var2)) {
            return (Integer)var0.method_11654(var2) > 1;
         }
      }

      return false;
   }

   public static int R(class_2680 var0, class_2680 var1) {
      int var2 = var1 != null && var1.method_26204() == var0.method_26204() ? 1 : 0;
      if (var0.method_28498(class_2741.field_12485) && var0.method_11654(class_2741.field_12485) == class_2771.field_12682) {
         return var2 != 0 ? (var1.method_11654(class_2741.field_12485) == class_2771.field_12682 ? 0 : 1) : 2;
      }

      for (class_2758 var4 : j) {
         if (var0.method_28498(var4)) {
            return Math.max(0, (Integer)var0.method_11654(var4) - (var2 != 0 ? (Integer)var1.method_11654(var4) : 0));
         }
      }

      return var2 != 0 ? 0 : 1;
   }

   public static List<class_2769<?>> Y(class_2680 var0) {
      class_2248 var1 = var0.method_26204();
      if (var1 instanceof class_2462) {
         return List.of(class_2741.field_12494);
      } else if (var1 instanceof class_2286) {
         return List.of(class_2741.field_12534);
      } else if (var1 instanceof class_2428) {
         return List.of(class_2741.field_12524);
      } else if (var1 instanceof class_2309) {
         return List.of(class_2741.field_12501);
      } else if (var1 instanceof class_2401) {
         return List.of(class_2741.field_12484);
      } else {
         return !(var1 instanceof class_2323) && !(var1 instanceof class_2533) && !(var1 instanceof class_2349)
               || var1 == class_2246.field_9973
               || var1 == class_2246.field_10453
               || var0.method_28498(class_2741.field_12484) && var0.method_11654(class_2741.field_12484)
            ? List.of()
            : List.of(class_2741.field_12537);
      }
   }

   public static int d(class_2680 var0, class_2680 var1) {
      if (var1 != null && var1.method_26204() == var0.method_26204() && y(var0, var1) && !n(var0, var1)) {
         for (class_2758 var3 : j) {
            if (var0.method_28498(var3) && !((Integer)var0.method_11654(var3)).equals(var1.method_11654(var3))) {
               return -1;
            }
         }

         if (var0.method_28498(class_2741.field_12485) && var0.method_11654(class_2741.field_12485) != var1.method_11654(class_2741.field_12485)) {
            return -1;
         }

         int var5 = 0;

         for (class_2769 var4 : Y(var0)) {
            var5 += G(var4, var1, var0);
         }

         return var5;
      } else {
         return -1;
      }
   }

   private static <T extends Comparable<T>> int G(class_2769<T> var0, class_2680 var1, class_2680 var2) {
      List var3 = List.copyOf(var0.method_11898());
      int var4 = var3.indexOf(var1.method_11654(var0));
      int var5 = var3.indexOf(var2.method_11654(var0));
      return var4 >= 0 && var5 >= 0 ? Math.floorMod(var5 - var4, var3.size()) : 0;
   }

   private static boolean y(class_2680 var0, class_2680 var1) {
      for (class_2769 var3 : v) {
         if (var0.method_28498(var3)
            && var1.method_28498(var3)
            && (var3 != class_2741.field_12485 || var0.method_11654(class_2741.field_12485) != class_2771.field_12682)
            && !var0.method_11654(var3).equals(var1.method_11654(var3))) {
            return false;
         }
      }

      return true;
   }

   private static boolean n(class_2680 var0, class_2680 var1) {
      if (var0.method_28498(class_2741.field_12485) && var0.method_11654(class_2741.field_12485) == class_2771.field_12682) {
         return var1.method_11654(class_2741.field_12485) != class_2771.field_12682;
      }

      for (class_2758 var3 : j) {
         if (var0.method_28498(var3)) {
            return (Integer)var1.method_11654(var3) < (Integer)var0.method_11654(var3);
         }
      }

      return false;
   }

   public static boolean X(class_2680 var0, class_2680 var1) {
      if (var1 != null && var1.method_26204() == var0.method_26204()) {
         for (class_2769 var3 : v) {
            if (var0.method_28498(var3) && var1.method_28498(var3) && !var0.method_11654(var3).equals(var1.method_11654(var3))) {
               return false;
            }
         }

         for (class_2758 var6 : j) {
            if (var0.method_28498(var6) && !((Integer)var0.method_11654(var6)).equals(var1.method_11654(var6))) {
               return false;
            }
         }

         for (class_2769 var7 : Y(var0)) {
            if (!var0.method_11654(var7).equals(var1.method_11654(var7))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static uo.t q(class_2680 var0, class_2680 var1) {
      if (var1 == null) {
         return uo.t.v;
      }

      if (var1.method_26204() == var0.method_26204()) {
         if (X(var0, var1)) {
            return uo.t.V;
         } else {
            return y(var0, var1) && n(var0, var1) ? uo.t.L : uo.t.m;
         }
      } else {
         return !var1.method_26215() && !var1.method_45474() ? uo.t.D : uo.t.L;
      }
   }

   public static boolean i(class_2680 var0, class_2680 var1, class_2680 var2) {
      if (var2 != null && var2.method_26204() == var0.method_26204() && y(var0, var2)) {
         int var3 = var1 != null && var1.method_26204() == var0.method_26204() ? 1 : 0;
         if (var0.method_28498(class_2741.field_12485) && var0.method_11654(class_2741.field_12485) == class_2771.field_12682) {
            return (var2.method_11654(class_2741.field_12485) == class_2771.field_12682 ? 1 : 0) == var3;
         }

         for (class_2758 var5 : j) {
            if (var0.method_28498(var5)) {
               return (Integer)var2.method_11654(var5) == (var3 != 0 ? (Integer)var1.method_11654(var5) : 0) + 1
                  && (Integer)var2.method_11654(var5) <= (Integer)var0.method_11654(var5);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean d(class_2680 var0) {
      for (class_2769 var2 : v) {
         if (var0.method_28498(var2)) {
            return true;
         }
      }

      return false;
   }

   public static class_2680 w(class_1657 var0, class_1799 var1, class_3965 var2, float var3, float var4) {
      if (var1.method_7909() instanceof class_1747 var5) {
         uo.l var8 = new uo.l(var0, var1, var2, var3, var4);
         if (!var8.method_7716()) {
            return null;
         }

         if (var5.method_16356(var8) != var8) {
            return null;
         }

         class_2680 var7 = var5 instanceof class_1827 ? ((qca336a9a679bb27a)var5).larp$placementState(var8) : var5.method_7711().method_9605(var8);
         return var7 != null && var7.method_26184(var0.method_73183(), var8.method_8037()) ? var7 : null;
      } else {
         return null;
      }
   }

   public static boolean b(class_1657 var0, class_1799 var1, class_3965 var2, float var3, float var4, class_2680 var5, class_2680 var6, class_2338 var7) {
      if (!new uo.l(var0, var1, var2, var3, var4).method_8037().equals(var7)) {
         return false;
      }

      boolean var8 = d(var5);
      float var9 = z(var5) ? 9.0F : 3.0F;
      float[][] var10 = var8 ? new float[][]{{0.0F, 0.0F}, {var9, 0.0F}, {-var9, 0.0F}, {0.0F, w(var5)}, {0.0F, -w(var5)}} : new float[][]{{0.0F, 0.0F}};

      for (float[] var14 : var10) {
         class_2680 var15 = w(var0, var1, var2, var3 + var14[0], class_3532.method_15363(var4 + var14[1], -90.0F, 90.0F));
         if (!i(var5, var6, var15)) {
            return false;
         }
      }

      return true;
   }

   private static float w(class_2680 var0) {
      if (var0.method_28498(class_2741.field_12525) && ((class_2350)var0.method_11654(class_2741.field_12525)).method_10166().method_10178()) {
         return !z(var0) && !(var0.method_26204() instanceof class_2426) ? 3.0F : 10.0F;
      } else {
         return 3.0F;
      }
   }

   public static boolean z(class_2680 var0) {
      return var0.method_26204() instanceof class_2315 || var0.method_26204() instanceof class_2665;
   }

   public static boolean C(class_2680 var0) {
      if (!var0.method_28498(class_2741.field_12525)) {
         return false;
      }

      class_2350 var1 = (class_2350)var0.method_11654(class_2741.field_12525);
      return var0.method_26204() instanceof class_2426 ? var1 == class_2350.field_11036 : z(var0) && var1 == class_2350.field_11033;
   }

   public static String K(Module var0, class_1657 var1, class_1799 var2, class_2338 var3, class_2680 var4, class_243 var5, double var6) {
      class_2680 var8 = var1.method_73183().method_8320(var3);
      byte var9 = 0;
      byte var10 = 0;
      byte var11 = 0;
      byte var12 = 0;

      for (class_2350 var16 : m) {
         class_2338 var17 = var3.method_10093(var16);
         if (t(var1, var17)) {
            var9 = 1;
            class_2350 var18 = var16.method_10153();
            class_243 var19 = new class_243(var18.method_10148(), var18.method_10164(), var18.method_10165());

            for (class_243 var21 : fk.n(var17, var18)) {
               if (!(var5.method_1020(var21).method_1026(var19) <= 0.02)) {
                  var10 = 1;
                  if (!(var5.method_1025(var21) > var6 * var6)) {
                     var11 = 1;
                     class_3965 var22 = fk.E(var0, var5, var17, var21.method_1020(var5), var6);
                     if (var22 != null && var22.method_17780() == var18) {
                        var12 = 1;
                        float[] var23 = t9.X(var5, var22.method_17784());
                        if (b(var1, var2, var22, var23[0], var23[1], var4, var8, var3)) {
                           return null;
                        }
                     }
                  }
               }
            }
         }
      }

      return var9 == 0 ? "no support" : (var10 == 0 ? "face turned away" : (var11 == 0 ? "out of reach" : (var12 == 0 ? "view blocked" : "wrong direction")));
   }

   public static class_3965 z(Module var0, class_1657 var1, class_1799 var2, class_2338 var3, class_2680 var4, class_243 var5, double var6, boolean var8) {
      return X(var0, var1, var2, var3, var4, var5, var6, var8, false);
   }

   public static class_3965 X(
      Module var0, class_1657 var1, class_1799 var2, class_2338 var3, class_2680 var4, class_243 var5, double var6, boolean var8, boolean var9
   ) {
      class_2680 var10 = var1.method_73183().method_8320(var3);
      int var11 = var10.method_26204() == var4.method_26204() ? 1 : 0;

      for (int var12 = 0; var12 < 2; var12++) {
         for (int var13 = 0; var13 < m.length * (var11 != 0 ? 2 : 1); var13++) {
            int var14 = var13 >= m.length ? 1 : 0;
            class_2350 var15 = m[var13 % m.length];
            class_2338 var16 = var14 != 0 ? var3 : var3.method_10093(var15);
            if ((var14 != 0 ? var12 != 1 : O(var1.method_73183().method_8320(var16)) == (var12 == 1 ? 1 : 0))
               && (var14 != 0 ? !var10.method_26218(var1.method_73183(), var3).method_1110() && !var10.method_31709() : t(var1, var16))) {
               class_2350 var17 = var14 != 0 ? var15 : var15.method_10153();
               class_243 var18 = new class_243(var17.method_10148(), var17.method_10164(), var17.method_10165());
               List var19 = fk.n(var16, var17);

               for (int var20 = 0; var20 < var19.size(); var20++) {
                  class_243 var21 = (class_243)var19.get(var20);
                  if (!(var5.method_1020(var21).method_1026(var18) <= 0.02) && !(var5.method_1025(var21) > var6 * var6)) {
                     class_3965 var22;
                     if (var8) {
                        var22 = fk.E(var0, var5, var16, var21.method_1020(var5), var6);
                        if (var22 == null || var22.method_17780() != var17) {
                           continue;
                        }
                     } else {
                        var22 = new class_3965(var21, var17, var16, false);
                     }

                     float[] var23 = t9.X(var5, var22.method_17784());
                     if (b(var1, var2, var22, var23[0], var23[1], var4, var10, var3)) {
                        return var22;
                     }

                     if ((!var8 || var9) && !d(var4)) {
                        break;
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   public static boolean w(class_2680 var0) {
      class_2248 var1 = var0.method_26204();
      return var1 instanceof class_2323
         || var1 instanceof class_2533
         || var1 instanceof class_2349
         || var1 instanceof class_2401
         || var1 instanceof class_2269
         || var1 instanceof class_2312
         || var1 instanceof class_2428
         || var1 instanceof class_2309
         || var1 instanceof class_2244
         || var1 instanceof class_2272
         || var1 instanceof class_2457;
   }

   static boolean t(class_1657 var0, class_2338 var1) {
      class_2680 var2 = var0.method_73183().method_8320(var1);
      if (!O(var2)) {
         return !fk.V(var1) ? false : !var2.method_26220(var0.method_73183(), var1).method_1110();
      } else {
         return var0.method_73183().method_22340(var1) && !var2.method_45474() && !var2.method_26220(var0.method_73183(), var1).method_1110();
      }
   }

   public static boolean O(class_2680 var0) {
      return var0.method_31709() || w(var0) || var0.method_26204() instanceof class_4969 || var0.method_26204() instanceof class_2304;
   }

   public static boolean B(class_2350 var0, double var1, double var3, double var5, double var7) {
      return (boolean)(switch (var0) {
         case field_11036 -> var5 > var1 + 0.03 ? 1 : 0;
         case field_11033 -> var5 < var1 - 0.03 ? 1 : 0;
         case field_11034 -> var3 > var1 + 0.03 ? 1 : 0;
         case field_11039 -> var3 < var1 - 0.03 ? 1 : 0;
         case field_11035 -> var7 > var1 + 0.03 ? 1 : 0;
         case field_11043 -> var7 < var1 - 0.03 ? 1 : 0;
         default -> throw new MatchException(null, null);
      });
   }

   public static boolean u(class_1657 var0, class_3965 var1) {
      class_2350 var2 = var1.method_17780();
      class_243 var3 = var1.method_17784();

      double var4 = switch (var2.method_10166()) {
         case field_11048 -> var3.field_1352;
         case field_11052 -> var3.field_1351;
         case field_11051 -> var3.field_1350;
         default -> throw new MatchException(null, null);
      };
      q586814c6d8f3640b var6 = (q586814c6d8f3640b)var0;
      double var7 = var0.method_5751();
      return B(var2, var4, var0.method_23317(), var0.method_23318() + var7, var0.method_23321())
         && B(var2, var4, var6.larpXLast(), var6.larpYLast() + var7, var6.larpZLast());
   }

   public static boolean I(class_2338 var0) {
      class_746 var1 = class_310.method_1551().field_1724;

      for (class_2350 var5 : class_2350.values()) {
         if (t(var1, var0.method_10093(var5))) {
            return true;
         }
      }

      return false;
   }

   public static class_3965 l(Module var0, class_2338 var1, class_243 var2, double var3, class_2680 var5) {
      for (class_2350 var9 : m) {
         class_2350 var10 = var9.method_10153();
         class_243 var11 = new class_243(var10.method_10148(), var10.method_10164(), var10.method_10165());

         for (class_243 var13 : fk.n(var1, var10)) {
            if (!(var2.method_1020(var13).method_1026(var11) <= 0.02) && !(var2.method_1025(var13) > var3 * var3)) {
               class_3965 var14 = fk.E(var0, var2, var1, var13.method_1020(var2), var3);
               if (var14 != null && var14.method_17780() == var10 && z(var5, t9.X(var2, var14.method_17784())[0])) {
                  return var14;
               }
            }
         }
      }

      return null;
   }

   public static boolean z(class_2680 var0, float var1) {
      return var0 != null && var0.method_26204() instanceof class_2349 && var0.method_11654(class_2741.field_12537)
         ? class_2350.method_10150(var1) != ((class_2350)var0.method_11654(class_2741.field_12481)).method_10153()
         : true;
   }

   static class_2350[] B(float var0, float var1) {
      float var2 = var1 * (float) (Math.PI / 180.0);
      float var3 = -var0 * (float) (Math.PI / 180.0);
      float var4 = class_3532.method_15374(var2);
      float var5 = class_3532.method_15362(var2);
      float var6 = class_3532.method_15374(var3);
      float var7 = class_3532.method_15362(var3);
      int var8 = var6 > 0.0F ? 1 : 0;
      int var9 = var4 < 0.0F ? 1 : 0;
      int var10 = var7 > 0.0F ? 1 : 0;
      float var11 = var8 != 0 ? var6 : -var6;
      float var12 = var9 != 0 ? -var4 : var4;
      float var13 = var10 != 0 ? var7 : -var7;
      float var14 = var11 * var5;
      float var15 = var13 * var5;
      class_2350 var16 = var8 != 0 ? class_2350.field_11034 : class_2350.field_11039;
      class_2350 var17 = var9 != 0 ? class_2350.field_11036 : class_2350.field_11033;
      class_2350 var18 = var10 != 0 ? class_2350.field_11035 : class_2350.field_11043;
      if (var11 > var13) {
         if (var12 > var14) {
            return R(var17, var16, var18);
         } else {
            return var15 > var12 ? R(var16, var18, var17) : R(var16, var17, var18);
         }
      } else if (var12 > var15) {
         return R(var17, var18, var16);
      } else {
         return var14 > var12 ? R(var18, var16, var17) : R(var18, var17, var16);
      }
   }

   private static class_2350[] R(class_2350 var0, class_2350 var1, class_2350 var2) {
      return new class_2350[]{var0, var1, var2, var2.method_10153(), var1.method_10153(), var0.method_10153()};
   }

   static final class l extends class_1750 {
      private final float y;
      private final float o;

      l(class_1657 var1, class_1799 var2, class_3965 var3, float var4, float var5) {
         super(var1, class_1268.field_5808, var2, var3);
         this.y = var4;
         this.o = var5;
      }

      public class_2350 method_8042() {
         return class_2350.method_10150(this.y);
      }

      public float method_8044() {
         return this.y;
      }

      public class_2350 method_7715() {
         return uo.B(this.y, this.o)[0];
      }

      public class_2350 method_32760() {
         return this.o < 0.0F ? class_2350.field_11036 : class_2350.field_11033;
      }

      public class_2350[] method_7718() {
         class_2350[] var1 = uo.B(this.y, this.o);
         if (this.field_7904) {
            return var1;
         }

         class_2350 var2 = this.method_8038();
         int var3 = 0;

         while (var3 < var1.length && var1[var3] != var2.method_10153()) {
            var3++;
         }

         if (var3 > 0 && var3 < var1.length) {
            System.arraycopy(var1, 0, var1, 1, var3);
            var1[0] = var2.method_10153();
         }

         return var1;
      }
   }

   public enum t {
      L,
      D,
      m,
      V,
      v;
   }
}
