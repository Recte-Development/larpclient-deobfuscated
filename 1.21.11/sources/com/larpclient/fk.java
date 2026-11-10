package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.Category;
import com.larpclient.module.IgnoreEntityModule;
import com.larpclient.module.MCFriendModule;
import com.larpclient.module.Module;
import com.larpclient.module.ScaffoldModule;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1750;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2304;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3726;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4969;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class fk {
   private static final Map<Module, fk.a> T = new LinkedHashMap<>();
   private static final Map<Module, String> r = new HashMap<>();
   private static final Map<Module, Long> U = new HashMap<>();
   private static final class_2350[] s = new class_2350[]{
      class_2350.field_11036, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034, class_2350.field_11033
   };

   public static boolean f(Object var0, Object var1, Object var2, Object var3, int var4, int var5) {
      int var6 = var5 - var4;
      return var0 != null && var0 == var1 && var2 != null && var2 == var3 && var6 >= 0 && var6 <= 1;
   }

   public static String c(Module var0) {
      return r.getOrDefault(var0, "No supported face within native reach");
   }

   private static void L(Module var0, String var1) {
      r.put(var0, var1);
   }

   private static void G(Module var0, String var1) {
      L(var0, var1);
      long var2 = System.nanoTime();
      long var4 = U.getOrDefault(var0, Long.MIN_VALUE);
      if (var4 == Long.MIN_VALUE || var2 - var4 >= 2000000000L) {
         U.put(var0, var2);
         q8.G(var0.getName(), "Placement paused: {}", var1);
      }
   }

   public static void N(Module var0) {
      T.remove(var0);
      cr.p(var0);
   }

   public static void M() {
      T.clear();
      r.clear();
      U.clear();
   }

   public static void n() {
      T.values().removeIf(var0 -> !var0.source.isRuntimeActive() || !var0.source.allowsScreenInteractions());
   }

   public static void U() {
      T.values().removeIf(var0 -> !cr.Y(var0.source));
   }

   private static boolean M(Module var0) {
      return var0 == null ? cr.f() : cr.o(var0);
   }

   public static boolean b(Module var0) {
      fk.a var1 = T.get(var0);
      class_310 var2 = class_310.method_1551();
      return var1 != null && var2.field_1724 != null && f(var1.world, var2.field_1687, var1.player, var2.field_1724, var1.tick, var2.field_1724.field_6012);
   }

   public static int T(class_1792 var0, boolean var1) {
      class_746 var2 = class_310.method_1551().field_1724;
      if (var2 == null) {
         return -1;
      }

      int var3 = var2.method_31548().method_67532();
      if (Z(var2.method_6047(), var0)) {
         return var3;
      }

      if (Z(var2.method_6079(), var0)) {
         return -2;
      }

      if (var1) {
         for (int var4 = 0; var4 < 9; var4++) {
            if (Z(var2.method_31548().method_5438(var4), var0)) {
               return var4;
            }
         }
      }

      return -1;
   }

   private static boolean Z(class_1799 var0, class_1792 var1) {
      return var1 == class_1802.field_8162 ? var0.method_7960() : var0.method_31574(var1);
   }

   public static boolean v(class_2338 var0) {
      class_310 var1 = class_310.method_1551();
      return var1.field_1687 != null
         && var1.field_1687.method_22340(var0)
         && !var1.field_1687.method_31606(var0)
         && var1.field_1687.method_8621().method_11952(var0)
         && var1.field_1687.method_8320(var0).method_45474();
   }

   public static boolean j(class_2338 var0, class_1792 var1) {
      if (v(var0)) {
         return true;
      }

      class_310 var2 = class_310.method_1551();
      return var1 instanceof class_1747 var3
         && var2.field_1687 != null
         && var2.field_1687.method_22340(var0)
         && !var2.field_1687.method_31606(var0)
         && var2.field_1687.method_8621().method_11952(var0)
         && var2.field_1687.method_8320(var0).method_27852(var3.method_7711());
   }

   private static class_3965 d(Module var0, class_2338 var1, class_243 var2, double var3, boolean var5) {
      return !M(var0) ? null : p(var0, class_310.method_1551().field_1724.method_33571(), var1, var2, var3, var5);
   }

   private static class_3965 p(Module var0, class_243 var1, class_2338 var2, class_243 var3, double var4, boolean var6) {
      if (!M(var0)) {
         return null;
      }

      class_310 var7 = class_310.method_1551();
      double var8 = Math.min(var4, var7.field_1724.method_55754());
      class_243 var10 = var1.method_1019(var3.method_1029().method_1021(var8));
      class_3965 var11 = var7.field_1687.method_17742(new class_3959(var1, var10, class_3960.field_17559, class_242.field_1348, var7.field_1724));
      if (var11.method_17783() == class_240.field_1332 && var11.method_17777().equals(var2) && !AntiTrapModule.J(var7.field_1687.method_8320(var2))) {
         if (var6) {
            return var11;
         }

         for (class_1297 var13 : var7.field_1687.method_8335(var7.field_1724, new class_238(var1, var11.method_17784()).method_1014(1.0))) {
            if (var13.method_5863()
               && !var13.method_7325()
               && !AntiTrapModule.C(var13)
               && !IgnoreEntityModule.U(var13)
               && ug.U(var1, var11.method_17784(), var13.method_5829())) {
               return null;
            }
         }

         return var11;
      } else {
         return null;
      }
   }

   public static class_3965 x(class_2338 var0, class_243 var1, class_243 var2, double var3, boolean var5, fk.e var6) {
      if (!var6.replaceable(var0)) {
         return null;
      }

      for (class_2350 var10 : class_2350.values()) {
         class_2338 var11 = var0.method_10093(var10);
         if (var6.usableSupport(var11)) {
            class_2350 var12 = var10.method_10153();

            for (class_243 var14 : n(var11, var12)) {
               class_3965 var15 = var6.ray(var11, var5 ? var14.method_1020(var1) : var2, var3);
               if (var15 != null && var15.method_17780() == var12 && var6.contextMatches(var15, var0)) {
                  return var15;
               }

               if (!var5) {
                  break;
               }
            }
         }
      }

      return null;
   }

   public static List<class_243> n(class_2338 var0, class_2350 var1) {
      class_243 var2 = class_243.method_24953(var0)
         .method_1019(new class_243(var1.method_10148(), var1.method_10164(), var1.method_10165()).method_1021(0.499));
      ArrayList var3 = new ArrayList(9);

      for (double var7 : new double[]{0.0, -0.42, 0.42}) {
         for (double var12 : new double[]{0.0, -0.42, 0.42}) {
            var3.add(switch (var1.method_10166()) {
               case field_11048 -> var2.method_1031(0.0, var7, var12);
               case field_11052 -> var2.method_1031(var7, 0.0, var12);
               case field_11051 -> var2.method_1031(var7, var12, 0.0);
               default -> throw new MatchException(null, null);
            });
         }
      }

      return var3;
   }

   private static boolean X(Module var0, class_2338 var1) {
      return !(var0 instanceof fk.h var2 && !var2.trustsSupport(var1));
   }

   public static boolean V(class_2338 var0) {
      class_310 var1 = class_310.method_1551();
      if (!var1.field_1687.method_22340(var0)) {
         return false;
      }

      class_2680 var2 = var1.field_1687.method_8320(var0);
      return !var2.method_45474()
         && (
            var1.field_1724.method_5715()
               || !var2.method_31709() && !(var2.method_26204() instanceof class_4969) && !(var2.method_26204() instanceof class_2304)
         );
   }

   public static class_3965 U(class_2338 var0, class_1792 var1, double var2, boolean var4) {
      return M(var0, var1, var2, var4, true);
   }

   public static class_3965 M(class_2338 var0, class_1792 var1, double var2, boolean var4, boolean var5) {
      return c(null, var0, var1, var2, var4, var5);
   }

   public static class_3965 c(final Module var0, class_2338 var1, final class_1792 var2, double var3, boolean var5, final boolean var6) {
      if (var2 instanceof class_1747 && M(var0)) {
         class_310 var7 = class_310.method_1551();
         return x(var1, var7.field_1724.method_33571(), var7.field_1724.method_5720(), Math.min(var3, var7.field_1724.method_55754()), var5, new fk.e() {
            @Override
            public boolean replaceable(class_2338 var1) {
               return fk.v(var1);
            }

            @Override
            public boolean usableSupport(class_2338 var1) {
               return fk.V(var1) && fk.X(var0, var1);
            }

            @Override
            public class_3965 ray(class_2338 var1, class_243 var2x, double var3x) {
               return fk.d(var0, var1, var2x, var3x, true);
            }

            @Override
            public boolean contextMatches(class_3965 var1, class_2338 var2x) {
               return fk.V(var1, var2x, var2, var6);
            }
         });
      } else {
         return null;
      }
   }

   private static boolean V(class_3965 var0, class_2338 var1, class_1792 var2, boolean var3) {
      return K(var0, var2, var3, var1) != null;
   }

   public static fk.p A(class_3965 var0, class_1792 var1, boolean var2) {
      return K(var0, var1, var2, null);
   }

   private static fk.p K(class_3965 var0, class_1792 var1, boolean var2, class_2338 var3) {
      class_310 var4 = class_310.method_1551();
      int var5 = T(var1, var2);
      if (var5 != -1 && var1 instanceof class_1747 var6) {
         class_1799 var7 = var5 == -2 ? var4.field_1724.method_6079() : var4.field_1724.method_31548().method_5438(var5);
         class_1268 var8 = var5 == -2 ? class_1268.field_5810 : class_1268.field_5808;
         class_1750 var9 = new class_1750(var4.field_1724, var8, var7, var0);
         if (!var9.method_7716()) {
            return null;
         }

         class_2338 var10 = var9.method_8037();
         if (var3 != null && !var10.equals(var3)) {
            return null;
         }

         class_2680 var11 = var6.method_7711().method_9605(var9);
         return var11 != null
               && var11.method_26184(var4.field_1687, var10)
               && var4.field_1687.method_8628(var11, var10, class_3726.method_67715(var4.field_1724))
            ? new fk.p(var10, var11)
            : null;
      } else {
         return null;
      }
   }

   public static class_3965 q(class_2338 var0, class_243 var1, double var2) {
      return D(null, var0, var1, var2);
   }

   public static class_3965 D(Module var0, class_2338 var1, class_243 var2, double var3) {
      return d(var0, var1, var2, var3, true);
   }

   public static class_3965 a(class_243 var0, class_2338 var1, class_243 var2, double var3) {
      return E(null, var0, var1, var2, var3);
   }

   public static class_3965 E(Module var0, class_243 var1, class_2338 var2, class_243 var3, double var4) {
      return p(var0, var1, var2, var3, var4, true);
   }

   public static boolean p(class_2338 var0, double var1) {
      return N(null, var0, var1);
   }

   public static boolean N(Module var0, class_2338 var1, double var2) {
      class_746 var4 = class_310.method_1551().field_1724;
      if (var4 == null) {
         return false;
      }

      q586814c6d8f3640b var5 = (q586814c6d8f3640b)var4;
      float var6 = var5.larpYRotLast();
      float var7 = var5.larpXRotLast();
      return Float.isFinite(var6) && Float.isFinite(var7) && d(var0, var1, class_243.method_1030(var7, var6), var2, true) != null;
   }

   public static boolean a(Module var0, class_2338 var1, class_1792 var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      return m(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   public static boolean m(
      Module var0,
      class_2338 var1,
      class_1792 var2,
      double var3,
      boolean var5,
      boolean var6,
      BooleanSupplier var7,
      BooleanSupplier var8,
      Consumer<Boolean> var9
   ) {
      if (T(var2, var6) == -1) {
         L(var0, "Required item missing from held hands or hotbar");
         return false;
      } else {
         class_3965 var10 = c(var0, var1, var2, var3, var5, var6);
         if (var10 == null) {
            L(var0, "No valid supported face, collision-free position or aim within native reach");
            return false;
         } else {
            return L(var0, var1, var10, var2, var3, true, var5, var6, var7, var8, var9);
         }
      }
   }

   public static boolean j(
      Module var0,
      class_2338 var1,
      class_3965 var2,
      class_1792 var3,
      double var4,
      boolean var6,
      BooleanSupplier var7,
      BooleanSupplier var8,
      Consumer<Boolean> var9
   ) {
      if (T(var3, var6) == -1) {
         L(var0, "Required item missing from held hands or hotbar");
         return false;
      }

      if (var2 != null && var3 instanceof class_1747 && M(var0) && j(var1, var3) && V(var2, var1, var3, var6)) {
         return L(var0, var1, var2, var3, var4, true, true, var6, var7, var8, var9);
      }

      L(var0, "The chosen support face no longer accepts this block");
      return false;
   }

   public static boolean E(
      Module var0,
      class_2338 var1,
      class_1792 var2,
      double var3,
      boolean var5,
      boolean var6,
      BooleanSupplier var7,
      BooleanSupplier var8,
      Consumer<Boolean> var9
   ) {
      if (M(var0) && T(var2, var6) != -1) {
         class_310 var10 = class_310.method_1551();
         class_243 var11 = var5 ? class_243.method_24953(var1).method_1020(var10.field_1724.method_33571()) : var10.field_1724.method_5720();
         class_3965 var12 = d(var0, var1, var11, var3, false);
         if (var12 == null && var5) {
            var12 = y(var0, var1, var10.field_1724.method_33571(), var3);
         }

         return var12 == null ? false : L(var0, var1, var12, var2, var3, false, var5, var6, var7, var8, var9);
      } else {
         return false;
      }
   }

   public static boolean C(Module var0, class_2338 var1, class_1792 var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      return E(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   private static class_3965 y(Module var0, class_2338 var1, class_243 var2, double var3) {
      class_243 var5 = class_243.method_24953(var1);

      for (class_2350 var9 : s) {
         class_243 var10 = new class_243(var9.method_10148(), var9.method_10164(), var9.method_10165());
         if (!(var2.method_1020(var5.method_1019(var10.method_1021(0.5))).method_1026(var10) <= 0.0)) {
            for (class_243 var12 : n(var1, var9)) {
               class_3965 var13 = d(var0, var1, var12.method_1020(var2), var3, false);
               if (var13 != null && var13.method_17780() == var9) {
                  return var13;
               }
            }
         }
      }

      return null;
   }

   private static boolean L(
      Module var0,
      class_2338 var1,
      class_3965 var2,
      class_1792 var3,
      double var4,
      boolean var6,
      boolean var7,
      boolean var8,
      BooleanSupplier var9,
      BooleanSupplier var10,
      Consumer<Boolean> var11
   ) {
      class_310 var12 = class_310.method_1551();
      fk.a var13 = new fk.a(
         var0, var1.method_10062(), var2, var3, var4, var6, var7, var9, var10, var11, var12.field_1687, var12.field_1724, var12.field_1724.field_6012
      );
      T.put(var0, var13);
      cr.y(var0, () -> k(var13, var8), () -> V(var13) && var13.actionReady.getAsBoolean() && T(var3, var8) != -1 && B(var13, var8) != null);
      return true;
   }

   private static boolean V(fk.a var0) {
      class_310 var1 = class_310.method_1551();
      return T.get(var0.source) == var0
         && f(var0.world, var1.field_1687, var0.player, var1.field_1724, var0.tick, var1.field_1724 == null ? 0 : var1.field_1724.field_6012)
         && var0.source.isRuntimeActive()
         && cr.o(var0.source)
         && (var1.method_1569() || var0.source.allowsScreenInteractions())
         && var0.valid.getAsBoolean();
   }

   private static class_3965 B(fk.a var0, boolean var1) {
      if (!V(var0)) {
         return null;
      }

      class_310 var2 = class_310.method_1551();
      class_3965 var3 = d(var0.source, var0.hit.method_17777(), var2.field_1724.method_5720(), var0.range, var0.place);
      if (var3 != null && var3.method_17780() == var0.hit.method_17780()) {
         if (!var0.place || V(var3.method_17777()) && X(var0.source, var3.method_17777())) {
            if (var0.place && var0.source instanceof fk.h var4 && var4.waitsForSentLook()) {
               q586814c6d8f3640b var8 = (q586814c6d8f3640b)var2.field_1724;
               class_3965 var6 = d(var0.source, var0.hit.method_17777(), class_243.method_1030(var8.larpXRotLast(), var8.larpYRotLast()), var0.range, true);
               if (var6 == null || var6.method_17780() != var0.hit.method_17780()) {
                  G(
                     var0.source,
                     "The look the server has does not reach the support face "
                        + var0.hit.method_17780().method_15434()
                        + " of "
                        + var0.hit.method_17777().method_23854()
                        + " yet; waiting a tick"
                  );
                  return null;
               }
            }

            if (var0.place && !var2.field_1724.method_5715()) {
               class_2680 var7 = var2.field_1687.method_8320(var3.method_17777());
               if (var7.method_31709() || var7.method_26204() instanceof class_4969 || var7.method_26204() instanceof class_2304) {
                  return null;
               }
            }

            if (!var0.place || j(var0.target, var0.item) && V(var3, var0.target, var0.item, var1)) {
               return var3;
            }

            G(var0.source, "Native placement context rejects the current block position, support or entity collision");
            return null;
         } else {
            G(var0.source, "The support " + var3.method_17777().method_23854() + " is gone or not trusted yet; waiting instead of an air place");
            return null;
         }
      } else {
         G(
            var0.source,
            "Current look does not reach the planned support face "
               + var0.hit.method_17780().method_15434()
               + " of "
               + var0.hit.method_17777().method_23854()
               + (var3 == null ? " (looking at no block of it)" : " (looking at its " + var3.method_17780().method_15434() + " face)")
               + "; check competing rotations or Crosshair aim"
         );
         return null;
      }
   }

   public static void x() {
      T.values().removeIf(var0x -> {
         if (V(var0x)) {
            return false;
         }

         G(var0x.source, "Intent expired or its target, player, world, focus or pause condition changed");
         return true;
      });
      Optional var0 = T.values()
         .stream()
         .filter(var0x -> var0x.visible && V(var0x) && cr.a(var0x.source))
         .max(Comparator.comparingInt(var0x -> cr.o(var0x.source.getName())));
      if (var0.isEmpty() || !(((fk.a)var0.get()).source instanceof com.larpclient.module.g)) {
         wp.k(wp.z.v);
      }

      if (var0.isEmpty() || !(((fk.a)var0.get()).source instanceof com.larpclient.module.b)) {
         wp.k(wp.z.r);
      }

      if (var0.isEmpty() || !(((fk.a)var0.get()).source instanceof AutoAnchorModule)) {
         wp.k(wp.z.J);
      }

      if (var0.isPresent() && !(((fk.a)var0.get()).source instanceof ScaffoldModule)) {
         wp.k(wp.z.e);
      }

      if (!var0.isEmpty()) {
         fk.a var1 = (fk.a)var0.get();
         class_310 var2 = class_310.method_1551();
         float[] var3 = t9.X(var2.field_1724.method_33571(), var1.hit.method_17784());
         if (var1.source instanceof fk.h var4) {
            var4.aimPlacement(var3[0], var3[1]);
         } else {
            t9.X(t9.v.j, var3[0], var3[1], 1.0);
         }

         t9.d();
      }
   }

   private static void k(fk.a var0, boolean var1) {
      if (var0.actionReady.getAsBoolean()) {
         class_3965 var2 = B(var0, var1);
         int var3 = T(var0.item, var1);
         if (var2 != null && var3 != -1) {
            class_310 var4 = class_310.method_1551();
            int var5 = var4.field_1724.method_31548().method_67532();

            try {
               if (var3 >= 0) {
                  var4.field_1724.method_31548().method_61496(var3);
               }

               class_1268 var6 = var3 == -2 ? class_1268.field_5810 : class_1268.field_5808;
               boolean var7 = var4.field_1761.method_2896(var4.field_1724, var6, var2).method_23665();
               if (var7) {
                  w6.H(var4.field_1724, var6);
               }

               var0.completed.accept(var7);
            } finally {
               var4.field_1724.method_31548().method_61496(var5);
               T.remove(var0.source, var0);
            }
         } else {
            G(var0.source, "Execution lost its valid support ray or inventory item");
         }
      }
   }

   private fk() {
   }

   private record a(
      Module source,
      class_2338 target,
      class_3965 hit,
      class_1792 item,
      double range,
      boolean place,
      boolean visible,
      BooleanSupplier valid,
      BooleanSupplier actionReady,
      Consumer<Boolean> completed,
      Object world,
      Object player,
      int tick
   ) {
   }

   public interface e {
      boolean replaceable(class_2338 var1);

      boolean usableSupport(class_2338 var1);

      class_3965 ray(class_2338 var1, class_243 var2, double var3);

      boolean contextMatches(class_3965 var1, class_2338 var2);
   }

   public interface h {
      void aimPlacement(float var1, float var2);

      default boolean trustsSupport(class_2338 var1) {
         return true;
      }

      default boolean waitsForSentLook() {
         return false;
      }
   }

   public record p(class_2338 target, class_2680 state) {
   }

   public abstract static class y extends Module implements fk.h {
      protected final com.larpclient.module.lo P = this.X("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1);
      protected final com.larpclient.module.lo r = this.X("Target Range", "Search distance for survival opponents", 6.0, 1.0, 12.0, 0.5);
      protected final com.larpclient.module.lo q;
      protected final com.larpclient.module.li m;
      protected final com.larpclient.module.l3 y;
      protected final com.larpclient.module.l3 C;
      protected final com.larpclient.module.l3 R;
      private long E;
      private String W = "Waiting for a target";

      protected y(String var1, String var2) {
         this(var1, var2, 0);
      }

      protected y(String var1, String var2, int var3) {
         super(var1, var2, Category.COMBAT, -1);
         this.q = this.X("Delay Per Action", "Ticks between native placement attempts", var3, 0.0, 20.0, 1.0);
         this.m = this.addSetting(
            new com.larpclient.module.li("Rotation", "Visible turns the player; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
         );
         this.y = this.O("Auto Switch", "Use hotbar blocks and restore the selected slot", true);
         this.C = this.O("Pause Using Item", "Pause while eating or using an item", true);
         this.R = this.O("Pause Mining", "Pause while the attack key is held", true);
      }

      protected void aim(float var1, float var2) {
         if (this.isEnabled()) {
            t9.X(t9.v.j, var1, var2, 1.0);
         }
      }

      @Override
      public final void aimPlacement(float var1, float var2) {
         if (this.isEnabled()) {
            this.aim(var1, var2);
         }
      }

      protected final void V(boolean var1) {
         this.E = mc.field_1687.method_75260() + Math.max(1, this.q.getValue().intValue());
         this.W(var1 ? "Waiting for world update" : "Placement rejected; retrying");
      }

      protected final com.larpclient.module.lo X(String var1, String var2, double var3, double var5, double var7, double var9) {
         return this.addSetting(new com.larpclient.module.lo(var1, var2, var3, var5, var7, var9));
      }

      protected final com.larpclient.module.l3 O(String var1, String var2, boolean var3) {
         return this.addSetting(new com.larpclient.module.l3(var1, var2, var3));
      }

      @Override
      public Module.p getTickPhase() {
         return Module.p.P;
      }

      @Override
      public Set<com.larpclient.module.l5> getNetworkImpacts() {
         return Set.of(com.larpclient.module.l5.F);
      }

      protected final void W(String var1) {
         if (!var1.equals(this.W) && !var1.startsWith("Preparing") && !var1.equals("Waiting for world update")) {
            fk.G(this, var1);
         }

         this.W = var1;
      }

      @Override
      public String getStatus() {
         return this.W;
      }

      protected final boolean e() {
         return !this.isEnabled() ? false : this.g() && mc.field_1687.method_75260() >= this.E;
      }

      protected final boolean g() {
         return !this.isEnabled()
            ? false
            : cr.f()
               && mc.method_1569()
               && (!this.C.getValue() || !mc.field_1724.method_6115())
               && (!this.R.getValue() || !mc.field_1690.field_1886.method_1434());
      }

      protected final boolean K(class_2338 var1, class_1792 var2) {
         return !this.isEnabled()
            ? false
            : fk.T(var2, this.y.getValue()) != -1 && fk.M(var1, var2, this.P.getValue(), this.m.G("Visible"), this.y.getValue()) != null;
      }

      protected final boolean I(class_1657 var1) {
         if (var1 != null
            && var1 != mc.field_1724
            && var1.method_73183() == mc.field_1687
            && var1.method_5805()
            && !var1.method_7325()
            && !var1.method_68878()
            && !var1.method_31481()
            && !(mc.field_1724.method_5858(var1) > this.r.getValue() * this.r.getValue())) {
            MCFriendModule var2 = LarpClient.b().G().b(MCFriendModule.class);
            return var2 == null || !var2.M(var1.method_5477().getString());
         } else {
            return false;
         }
      }

      protected final class_1657 U() {
         return mc.field_1687.method_18456().stream().filter(this::I).min(Comparator.comparingDouble(mc.field_1724::method_5858)).orElse(null);
      }

      protected final boolean u(class_2338 var1, class_1792 var2, class_1657 var3) {
         return this.isEnabled()
            ? fk.a(this, var1, var2, this.P.getValue(), this.m.G("Visible"), this.y.getValue(), () -> this.e() && this.I(var3), this::V)
            : false;
      }

      @Override
      public void onEnable() {
         this.E = 0L;
         fk.U.remove(this);
         fk.r.remove(this);
         q8.G(this.getName(), "Native placement enabled; rotation={}, range={}, autoSwitch={}", this.m.getValue(), this.P.getValue(), this.y.getValue());
      }

      @Override
      public void onDisable() {
         fk.N(this);
         this.E = 0L;
         this.W = "Waiting for a target";
         q8.v(this.getName(), "Placement intents cleared");
      }
   }
}
