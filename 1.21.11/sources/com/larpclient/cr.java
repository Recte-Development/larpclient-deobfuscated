package com.larpclient;

import com.larpclient.module.AnchorMacroModule;
import com.larpclient.module.AnchorTapModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.CriticalsModule;
import com.larpclient.module.CrystalTapModule;
import com.larpclient.module.Module;
import com.larpclient.module.RodSwapModule;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class cr {
   private static final List<cr.m> Y = new ArrayList<>();
   private static boolean F;
   private static boolean K;
   private static boolean E;
   private static final List<cr.j> e = new ArrayList<>();
   private static cr.j r;
   private static int o;
   private static long g = Long.MIN_VALUE;
   private static final Set<Integer> v = new HashSet<>();
   private static String k;
   private static boolean D;

   public static void y(Module var0, Runnable var1, BooleanSupplier var2) {
      Y.add(new cr.m(var0, var1, var2));
   }

   public static boolean a(Module var0) {
      I();
      return o(var0) && (k == null || k.equals(var0.getName()));
   }

   public static boolean P(Module var0, Runnable var1, BooleanSupplier var2) {
      I();
      if (var0 != null && var0.isRuntimeActive() && f() && var2.getAsBoolean() && r == null && !F && v.isEmpty() && (k == null || k.equals(var0.getName()))) {
         k = var0.getName();
         F = (boolean)1;
         K = (boolean)1;

         try {
            var1.run();
         } finally {
            K = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean l(Runnable var0) {
      I();
      if (H() && !wp.d() && r == null && !F && v.isEmpty() && k == null) {
         k = "Eating";
         F = (boolean)1;
         K = (boolean)1;

         try {
            var0.run();
         } finally {
            K = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean Z(Module var0, class_1297 var1, double var2) {
      I();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && r == null && !F && j(var0.getName(), var1) && B(var1, var2)) {
         class_310 var4 = class_310.method_1551();
         cr.j var5 = new cr.j(var0, var1, var2, false, () -> true, 1);
         f(var5);
         return bn.w(var4.field_1724);
      } else {
         return false;
      }
   }

   public static boolean J(Module var0, class_1297 var1, Runnable var2) {
      I();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && o(var0) && r == null && !F && Y(var0.getName(), var1)) {
         D = (boolean)1;

         try {
            var2.run();
         } finally {
            D = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean y(Module var0, class_1297 var1, Runnable var2) {
      I();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && o(var0) && v.contains(var1.method_5628())) {
         D = (boolean)1;

         try {
            var2.run();
         } finally {
            D = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean j() {
      return D || K;
   }

   public static boolean l() {
      if (K) {
         return true;
      } else if (!f()) {
         return false;
      } else {
         I();
         if (AnchorTapModule.N() || AnchorMacroModule.R()) {
            return false;
         } else if (CrystalTapModule.k()) {
            return false;
         } else if (r == null && (k == null || k.equals("Manual"))) {
            F = (boolean)1;
            k = "Manual";
            E = (boolean)1;
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean o() {
      I();
      return E;
   }

   public static void E() {
      E = (boolean)0;
   }

   public static boolean u() {
      return H() && class_310.method_1551().field_1755 == null;
   }

   private static boolean H() {
      class_310 var0 = class_310.method_1551();
      return var0.field_1724 != null && var0.field_1687 != null && var0.field_1761 != null && var0.field_1724.method_5805() && !var0.field_1724.method_7325();
   }

   public static boolean f() {
      return u() && !wp.d();
   }

   public static boolean o(Module var0) {
      return W(var0) && (!wp.d() || Y(var0));
   }

   public static boolean Y(Module var0) {
      return var0 != null && var0.isRuntimeActive() && var0.allowsDetachedCamera();
   }

   public static boolean W(Module var0) {
      return H() && l(class_310.method_1551().field_1755 != null, var0);
   }

   public static boolean l(boolean var0, Module var1) {
      return !var0 || var1 != null && var1.isRuntimeActive() && var1.allowsScreenInteractions();
   }

   public static boolean t() {
      return !e.isEmpty() || !Y.isEmpty() || r != null;
   }

   public static void m() {
      e.clear();
      Y.clear();
      I();
   }

   private static void I() {
      class_638 var0 = class_310.method_1551().field_1687;
      if (var0 != null) {
         k(var0.method_75260());
      }
   }

   private static void k(long var0) {
      if (g != var0) {
         g = var0;
         v.clear();
         k = null;
         F = (boolean)0;
         E = (boolean)0;
      }
   }

   public static void H() {
      bd.l();
      fk.M();
      e.clear();
      Y.clear();
      F = (boolean)0;
      E = (boolean)0;
      K = (boolean)0;
      r = null;
      v.clear();
      k = null;
      g = Long.MIN_VALUE;
      D = (boolean)0;
   }

   public static void p(Module var0) {
      e.removeIf(var1 -> var1.source == var0);
      Y.removeIf(var1 -> var1.source == var0);
      if (r != null && r.source == var0) {
         r = null;
      }
   }

   public static void F(Module var0, class_1297 var1, double var2, boolean var4, BooleanSupplier var5) {
      z(var0, var1, var2, var4, var5, 1);
   }

   public static void z(Module var0, class_1297 var1, double var2, boolean var4, BooleanSupplier var5, int var6) {
      e.add(new cr.j(var0, var1, var2, var4, var5, var6));
   }

   public static void I(Module var0, class_1297 var1, double var2, BooleanSupplier var4, Supplier<float[]> var5, Runnable var6) {
      e.add(new cr.j(var0, var1, var2, false, var4, 1, var5, var6, var0x -> {}));
   }

   public static double M(class_1297 var0) {
      return ug.L(class_310.method_1551().field_1724.method_33571(), var0.method_5829());
   }

   public static boolean B(class_1297 var0, double var1) {
      class_746 var3 = class_310.method_1551().field_1724;
      return var3 != null && e(var0, var1, var3.method_5720());
   }

   public static void U(Module var0, class_1297 var1, double var2, boolean var4, BooleanSupplier var5, int var6, Runnable var7, Consumer<String> var8) {
      e.add(new cr.j(var0, var1, var2, var4, var5, var6, null, var7, var8));
   }

   public static boolean A() {
      return class_310.method_1551().field_1724 != null && class_310.method_1551().field_1724.method_7261(0.0F) >= 1.0F;
   }

   public static double R(double var0) {
      class_746 var2 = class_310.method_1551().field_1724;
      return var2 == null ? 0.0 : Math.max(0.0, Math.min(var0, var2.method_55755()));
   }

   public static boolean e(class_1297 var0, double var1, class_243 var3) {
      return l(var0, var1, var3) == null;
   }

   public static String l(class_1297 var0, double var1, class_243 var3) {
      if (!f() || var0 == null) {
         return "interaction ownership";
      }

      if (RodSwapModule.S()) {
         return "temporary rod selection";
      }

      class_310 var4 = class_310.method_1551();
      if (var0 != var4.field_1724
         && var0.method_73183() == var4.field_1687
         && var0.method_5805()
         && !var0.method_31481()
         && !var0.method_7325()
         && var0.method_5863()) {
         double var5 = R(var1);
         class_243 var7 = var4.field_1724.method_33571();
         if (M(var0) > var5 + 1.0E-7) {
            return "range";
         } else {
            Optional var8 = ug.p(var7, var3, var0.method_5829().method_1014(var0.method_5871()), var5);
            if (var8.isEmpty()) {
               return "alignment";
            } else {
               return q(var0, var7, (class_243)var8.get(), C(var0, var7)) ? null : "obstruction";
            }
         }
      } else {
         return "target invalid";
      }
   }

   private static List<class_1297> C(class_1297 var0, class_243 var1) {
      class_310 var2 = class_310.method_1551();
      return var2.field_1687.method_8335(var2.field_1724, var0.method_5829().method_991(new class_238(var1, var1)).method_1014(1.0));
   }

   private static boolean q(class_1297 var0, class_243 var1, class_243 var2, List<class_1297> var3) {
      class_310 var4 = class_310.method_1551();
      class_3965 var5 = var4.field_1687.method_17742(new class_3959(var1, var2, class_3960.field_17558, class_242.field_1348, var4.field_1724));
      if (var5.method_17783() != class_240.field_1333 && var1.method_1025(var5.method_17784()) + 1.0E-7 < var1.method_1025(var2)) {
         return false;
      }

      for (class_1297 var7 : var3) {
         if (var7 != var0 && var7.method_5863() && !var7.method_7325() && ug.U(var1, var2, var7.method_5829())) {
            return false;
         }
      }

      return true;
   }

   public static class_3965 Y(class_2338 var0, double var1) {
      return !f() ? null : S(var0, var1, class_310.method_1551().field_1724.method_5720());
   }

   public static class_3965 S(class_2338 var0, double var1, class_243 var3) {
      if (!f()) {
         return null;
      }

      class_310 var4 = class_310.method_1551();
      double var5 = Math.max(0.0, Math.min(var1, var4.field_1724.method_55754()));
      class_243 var7 = var4.field_1724.method_33571();
      class_243 var8 = var7.method_1019(var3.method_1029().method_1021(var5));
      class_3965 var9 = var4.field_1687.method_17742(new class_3959(var7, var8, class_3960.field_17559, class_242.field_1348, var4.field_1724));
      if (var9.method_17783() == class_240.field_1332 && var9.method_17777().equals(var0)) {
         for (class_1297 var11 : var4.field_1687.method_8335(var4.field_1724, new class_238(var7, var8).method_1014(1.0))) {
            if (var11.method_5863() && !var11.method_7325() && ug.U(var7, var9.method_17784(), var11.method_5829())) {
               return null;
            }
         }

         return var9;
      } else {
         return null;
      }
   }

   public static Optional<class_243> D(class_1297 var0, double var1, class_243 var3, class_243 var4) {
      if (f() && var0 != null && !(M(var0) > var1 + 1.0E-7)) {
         class_243 var5 = class_310.method_1551().field_1724.method_33571();
         List var6 = C(var0, var5);
         return s8.l(var0.method_5829(), var5, var3, var4, var5x -> {
            Optional var6x = ug.p(var5, var5x.method_1020(var5), var0.method_5829(), var1);
            return var6x.isPresent() && q(var0, var5, (class_243)var6x.get(), var6);
         });
      } else {
         return Optional.empty();
      }
   }

   public static int o(String var0) {
      return switch (var0) {
         case "Aura" -> 4;
         case "AutoCart" -> 5;
         case "AutoCrystal" -> 6;
         case "Auto MLG" -> 7;
         case "Auto Pearl Catch" -> 8;
         case "Wind Charge Hop" -> 8;
         case "TriggerBot" -> 2;
         case "AutoClicker" -> 1;
         case "Scaffold" -> 1;
         case "Auto Drain" -> 1;
         case "Auto Area Miner" -> 1;
         case "Auto Elytra Pilot" -> 1;
         case "Anchor Hunter" -> 1;
         case "Auto Builder" -> 1;
         case "Rod Swap" -> 0;
         default -> 5;
      };
   }

   private static class_243 Y(cr.j var0) {
      if (var0.aim != null) {
         float[] var1 = var0.aim.get();
         return class_243.method_1030(var1[1], var1[0]);
      } else {
         return class_310.method_1551().field_1724.method_5720();
      }
   }

   public static void Q() {
      I();
      if (H()) {
         AutoCrystalModule var0 = AutoCrystalModule.l();
         if (u() && var0 != null && !wp.d()) {
            var0.K();
         }

         fk.x();
      }
   }

   private static boolean I(cr.j var0) {
      Object var1 = null;
      if ((var0.source == null || var0.source.isRuntimeActive()) && var0.valid.getAsBoolean()) {
         if (var0.cooldown && !A()) {
            var1 = "cooldown";
         } else {
            var1 = l(var0.target, var0.range, Y(var0));
         }
      } else {
         var1 = "intent expired / item use";
      }

      if (var1 != null) {
         var0.rejected.accept((String)var1);
      }

      return var1 == null;
   }

   public static void p() {
      I();
      if (H() && (!wp.d() || !Y.stream().noneMatch(var0x -> Y(var0x.source)))) {
         if (wp.d()) {
            e.clear();
            r = null;
            Y.removeIf(var0x -> !Y(var0x.source));
            fk.U();
         }

         if (!u()) {
            e.clear();
            r = null;
            fk.n();
         }

         class_310 var0 = class_310.method_1551();
         e.sort(Comparator.<cr.j>comparingInt(var0x -> o(var0x.source.getName())).reversed());
         if (r != null) {
            int var1 = !Y.stream().anyMatch(var0x -> var0x.source.isRuntimeActive() && var0x.valid.getAsBoolean() && o(var0x.source.getName()) > o(Z(r)))
                  && I(r)
                  && CriticalsModule.g()
                  && (var0.field_1724.field_6012 == o || !var0.field_1724.method_24828())
                  && e.stream().noneMatch(var0x -> I(var0x) && o(Z(var0x)) > o(Z(r)))
               ? 1
               : 0;
            switch (qn.c(var0.field_1724.field_6012 - o, (boolean)var1, CriticalsModule.o())) {
               case M:
                  r.rejected.accept("critical delay cancelled");
                  r = null;
                  break;
               case i:
                  f(r);
                  r = null;
                  break;
               case j:
                  e.clear();
                  Y.clear();
                  return;
            }
         }

         Y.sort(Comparator.<cr.m>comparingInt(var0x -> o(var0x.source.getName())).reversed());

         for (cr.j var2 : List.copyOf(e)) {
            if (Y.stream().anyMatch(var1x -> var1x.source.isRuntimeActive() && var1x.valid.getAsBoolean() && o(var1x.source.getName()) > o(Z(var2)))) {
               var2.rejected.accept("interaction ownership");
            } else if (j(Z(var2), var2.target) && v.size() < var2.limit) {
               if (I(var2)) {
                  if (a(var2)) {
                     break;
                  }

                  f(var2);
               }
            } else {
               var2.rejected.accept("interaction ownership");
            }
         }

         e.clear();
         if (r == null && !F) {
            for (cr.m var8 : List.copyOf(Y)) {
               if (o(var8.source) && var8.source.isRuntimeActive() && var8.valid.getAsBoolean() && (k == null || k.equals(var8.source.getName()))) {
                  k = var8.source.getName();
                  F = (boolean)1;
                  K = (boolean)1;

                  try {
                     var8.action.run();
                     break;
                  } finally {
                     K = (boolean)0;
                  }
               }
            }
         }

         Y.clear();
      } else {
         H();
      }
   }

   private static String Z(cr.j var0) {
      return var0.source == null ? "Manual" : var0.source.getName();
   }

   private static boolean a(cr.j var0) {
      if (var0.target instanceof class_1309 && CriticalsModule.w()) {
         var0.rejected.accept("critical delay");
         r = var0;
         o = class_310.method_1551().field_1724.field_6012;
         return true;
      } else {
         return false;
      }
   }

   private static void f(cr.j var0) {
      if (I(var0)) {
         if (v.size() < var0.limit && Y(Z(var0), var0.target)) {
            class_310 var1 = class_310.method_1551();
            int var2 = var0.aim != null ? 1 : 0;
            D = (boolean)1;

            try {
               if (var2 != 0) {
                  float[] var3 = var0.aim.get();
                  var1.field_1724.field_3944.method_52787(new class_2831(var3[0], var3[1], var1.field_1724.method_24828(), var1.field_1724.field_5976));
               }

               var1.field_1761.method_2918(var1.field_1724, var0.target);
               w6.M(var1.field_1724, class_1268.field_5808);
               var0.completed.run();
            } finally {
               D = (boolean)0;
               if (var2 != 0) {
                  var1.field_1724
                     .field_3944
                     .method_52787(
                        new class_2831(
                           var1.field_1724.method_36454(), var1.field_1724.method_36455(), var1.field_1724.method_24828(), var1.field_1724.field_5976
                        )
                     );
               }
            }
         }
      }
   }

   private static boolean j(String var0, class_1297 var1) {
      return (k == null || k.equals(var0)) && !v.contains(var1.method_5628());
   }

   private static boolean Y(String var0, class_1297 var1) {
      if (!j(var0, var1)) {
         return false;
      }

      k = var0;
      v.add(var1.method_5628());
      return true;
   }

   public static boolean G(class_1297 var0) {
      if (!D) {
         RodSwapModule.H();
      }

      if (D) {
         return true;
      }

      if (!B(var0, Double.MAX_VALUE)) {
         return false;
      }

      I();
      if (r != null) {
         return false;
      }

      class_310 var1 = class_310.method_1551();
      cr.j var2 = new cr.j(
         null,
         var0,
         var1.field_1724.method_55755(),
         true,
         () -> var1.field_1690.field_1886.method_1434() && var1.field_1765 instanceof class_3966 var2x && var2x.method_17782() == var0,
         1
      );
      return j("Manual", var0) && !a(var2) ? Y("Manual", var0) : false;
   }

   public static boolean V() {
      return r != null;
   }

   public static boolean C() {
      return D;
   }

   private cr() {
   }

   private record j(
      Module source,
      class_1297 target,
      double range,
      boolean cooldown,
      BooleanSupplier valid,
      int limit,
      Supplier<float[]> aim,
      Runnable completed,
      Consumer<String> rejected
   ) {
      j(Module var1, class_1297 var2, double var3, boolean var5, BooleanSupplier var6, int var7) {
         this(var1, var2, var3, var5, var6, var7, null, () -> {}, var0 -> {});
      }
   }

   private record m(Module source, Runnable action, BooleanSupplier valid) {
   }
}
