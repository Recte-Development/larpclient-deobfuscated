package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_2350.class_2353;

public final class gw {
   private static final int f = 4;
   private static final int Z = 8;
   public static final double X = 0.35;
   private static final List<class_238> G = List.of(new class_238(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
   private static final float[] n = new float[]{0.0F, 10.0F, -10.0F, 20.0F, -20.0F, 30.0F, -30.0F, 40.0F, -40.0F};
   private static final int T = 6;

   public static int v(double var0) {
      return class_3532.method_15357(var0 + 1.0E-6) - 1;
   }

   public static List<Integer> W(int var0, int var1) {
      ArrayList var2 = new ArrayList();
      int var3 = Math.max(var1, var0 - 4 + 1);
      var2.add(var0);

      for (int var4 = var0 - 1; var4 >= var3; var4--) {
         var2.add(var4);
      }

      return List.copyOf(var2);
   }

   public static List<List<class_2338>> u(
      class_243 var0, class_238 var1, class_243 var2, List<Integer> var3, boolean var4, Predicate<class_2338> var5, Predicate<class_2338> var6
   ) {
      ArrayList var7 = new ArrayList();

      for (int var9 : var3) {
         List var10 = W(var0, var1, var2, var9);
         var7.add(var10);
         if (var4) {
            List var11 = a(var10, var5, var6);
            if (!var11.isEmpty()) {
               var7.add(var11);
            }
         }
      }

      return List.copyOf(var7);
   }

   public static List<class_2338> a(List<class_2338> var0, Predicate<class_2338> var1, Predicate<class_2338> var2) {
      LinkedHashSet var3 = new LinkedHashSet();

      for (class_2338 var5 : var0) {
         if (var1.test(var5) && !h(var5, var2)) {
            for (class_2350 var7 : class_2353.field_11062) {
               class_2338 var8 = var5.method_10093(var7);
               if (!var0.contains(var8) && var1.test(var8) && h(var8, var2)) {
                  var3.add(var8);
               }
            }
         }
      }

      return var3.stream().limit(8L).toList();
   }

   private static boolean h(class_2338 var0, Predicate<class_2338> var1) {
      for (class_2350 var5 : class_2350.values()) {
         if (var1.test(var0.method_10093(var5))) {
            return true;
         }
      }

      return false;
   }

   public static List<class_2338> W(class_243 var0, class_238 var1, class_243 var2, int var3) {
      class_243 var4 = new class_243(var2.field_1352, 0.0, var2.field_1350);
      if (var4.method_1027() > 0.36) {
         var4 = var4.method_1029().method_1021(0.6);
      }

      double var5 = Math.clamp(var1.method_17939() * 0.5 - 0.001, 0.0, 0.49);
      double var7 = Math.clamp(var1.method_17941() * 0.5 - 0.001, 0.0, 0.49);
      LinkedHashSet var9 = new LinkedHashSet();
      class_2338 var10 = new class_2338(class_3532.method_15357(var0.field_1352), var3, class_3532.method_15357(var0.field_1350));
      var9.add(var10);

      for (double var14 : new double[]{0.0, 0.5, 1.0}) {
         class_243 var16 = var0.method_1019(var4.method_1021(var14));

         for (double var20 : new double[]{-var5, 0.0, var5}) {
            for (double var25 : new double[]{-var7, 0.0, var7}) {
               var9.add(new class_2338(class_3532.method_15357(var16.field_1352 + var20), var3, class_3532.method_15357(var16.field_1350 + var25)));
            }
         }
      }

      ArrayList var27 = new ArrayList(var9);
      var27.remove(var10);
      var27.sort(Comparator.comparingDouble(var1x -> class_243.method_24953(var1x).method_18805(1.0, 0.0, 1.0).method_1025(var0.method_18805(1.0, 0.0, 1.0))));
      var27.addFirst(var10);
      return List.copyOf(var27.subList(0, Math.min(9, var27.size())));
   }

   public static int J(List<Boolean> var0, int var1) {
      int var2 = var0.indexOf(true);
      if (var2 < 0 && !var0.isEmpty()) {
         return var1 >= 0 && var1 < var0.size() ? var1 : 0;
      } else {
         return var2;
      }
   }

   public static List<class_243> Q(class_2338 var0) {
      class_243 var1 = class_243.method_24953(var0);
      ArrayList var2 = new ArrayList(27);

      for (double var6 : new double[]{0.3, 0.0, -0.3}) {
         for (double var11 : new double[]{0.0, -0.3, 0.3}) {
            for (double var16 : new double[]{0.0, -0.3, 0.3}) {
               var2.add(var1.method_1031(var11, var6, var16));
            }
         }
      }

      return var2;
   }

   public static class_243 G(List<class_243> var0, class_243 var1, float var2, float var3, Predicate<class_243> var4) {
      double[] var5 = new double[var0.size()];
      ArrayList var6 = new ArrayList(var0.size());

      for (int var7 = 0; var7 < var0.size(); var7++) {
         class_243 var8 = ((class_243)var0.get(var7)).method_1020(var1);
         double var9 = Math.hypot(var8.field_1352, var8.field_1350);
         float var11 = (float)Math.toDegrees(Math.atan2(var8.field_1350, var8.field_1352)) - 90.0F;
         float var12 = (float)(-Math.toDegrees(Math.atan2(var8.field_1351, var9)));
         var5[var7] = Math.abs(class_3532.method_15393(var11 - var2)) + Math.abs(var12 - var3) + (var9 < 0.35 ? 1000 : 0);
         var6.add(var7);
      }

      var6.sort(Comparator.comparingDouble(var1x -> var5[var1x]));

      for (int var14 : var6) {
         if (var4.test(((class_243)var0.get(var14)).method_1020(var1))) {
            return (class_243)var0.get(var14);
         }
      }

      return null;
   }

   public static float M(class_243 var0, float var1) {
      return var0.field_1352 * var0.field_1352 + var0.field_1350 * var0.field_1350 < 1.0E-8
         ? var1
         : (float)Math.toDegrees(Math.atan2(-var0.field_1350, -var0.field_1352)) - 90.0F;
   }

   public static float[] b(
      class_2338 var0, class_2350 var1, List<class_243> var2, class_243 var3, float var4, float var5, float var6, Predicate<class_243> var7
   ) {
      float[] var8 = B(var0, var1, var3, var4, var5, var7);
      if (var8 != null) {
         return var8;
      }

      class_243 var9 = G(var2, var3, var4, var6, var7);
      return var9 == null ? null : t9.X(var3, var9);
   }

   public static float[] B(class_2338 var0, class_2350 var1, class_243 var2, float var3, float var4, Predicate<class_243> var5) {
      ArrayList var6 = new ArrayList(n.length);

      for (float var10 : n) {
         var6.add(var3 + var10);
      }

      var6.sort(Comparator.comparingDouble(var1x -> Math.abs(class_3532.method_15393(var1x - var4))));

      for (float var12 : var6) {
         float var13 = g(var0, var1, var2, var12, var5);
         if (!Float.isNaN(var13)) {
            return new float[]{var12, var13};
         }
      }

      return null;
   }

   private static float g(class_2338 var0, class_2350 var1, class_243 var2, float var3, Predicate<class_243> var4) {
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 <= 180; var6++) {
         float var7 = var6 * 0.5F;
         class_3965 var8 = class_238.method_1010(G, var2, var2.method_1019(class_243.method_1030(var7, var3).method_1021(8.0)), var0);
         if (var8 != null && (var1 == null || var8.method_17780() == var1)) {
            var5.add(var7);
         }
      }

      if (var5.isEmpty()) {
         return Float.NaN;
      }

      ArrayList var12 = new ArrayList(6);
      double var13 = Math.max(1.0, var5.size() / 6.0);

      for (double var9 = var13 / 2.0; var9 < var5.size() && var12.size() < 6; var9 += var13) {
         var12.add((Float)var5.get((int)var9));
      }

      float var14 = (Float)var5.get(var5.size() / 2);
      var12.sort(Comparator.comparingDouble(var1x -> Math.abs(var1x - var14)));

      for (float var11 : var12) {
         if (var4.test(class_243.method_1030(var11, var3))) {
            return var11;
         }
      }

      return Float.NaN;
   }

   public static float[] n(class_2338 var0, class_243 var1, float var2, float var3, float var4, Predicate<class_243> var5, Predicate<class_243> var6) {
      List var7 = Q(var0);
      float[] var8 = b(var0, null, var7, var1, var2, var3, var4, var5);
      return var8 != null ? var8 : b(var0, null, var7, var1, var2, var3, var4, var6);
   }

   public static float[] r(
      class_2338 var0,
      class_2350 var1,
      List<class_243> var2,
      class_243 var3,
      float var4,
      float var5,
      float var6,
      Predicate<class_243> var7,
      Predicate<class_243> var8,
      boolean var9
   ) {
      float[] var10 = B(var0, var1, var3, var4, var5, var7);
      if (var10 == null) {
         var10 = B(var0, var1, var3, var4, var5, var8);
      }

      if (var10 == null && var9) {
         var10 = b(var0, var1, var2, var3, var4, var5, var6, var7);
         return var10 != null ? var10 : b(var0, var1, var2, var3, var4, var5, var6, var8);
      } else {
         return var10;
      }
   }

   public static boolean G(class_2338 var0, class_243 var1, class_243 var2, double var3) {
      return class_238.method_1010(G, var1, var1.method_1019(var2.method_1029().method_1021(var3)), var0) != null;
   }

   public static class_243 J(class_243 var0, class_243 var1, double var2) {
      class_243 var4 = new class_243(var1.field_1352, 0.0, var1.field_1350);
      if (var4.method_1027() > 1.0E-8) {
         var4 = var4.method_1029().method_1021(var2);
      }

      return new class_243(var0.field_1352, 0.0, var0.field_1350).method_1019(var4);
   }

   public static double K(double var0, boolean var2, boolean var3, double var4) {
      return var2 ? (var3 ? var4 : 0.0) : var0;
   }

   public static boolean Q(int var0) {
      return var0 <= 1;
   }

   public static boolean X(class_238 var0, class_243 var1, double var2, Predicate<class_238> var4) {
      return var4.test(
         new class_238(
            var0.field_1323 + 1.0E-7 + var1.field_1352,
            var0.field_1322 - var2 - 1.0E-7,
            var0.field_1321 + 1.0E-7 + var1.field_1350,
            var0.field_1320 - 1.0E-7 + var1.field_1352,
            var0.field_1322,
            var0.field_1324 - 1.0E-7 + var1.field_1350
         )
      );
   }

   public static class_2338 o(List<class_2338> var0, Predicate<class_2338> var1, Predicate<class_2338> var2, Predicate<class_2338> var3) {
      for (class_2338 var5 : var0) {
         if (var1.test(var5)) {
            for (class_2350 var9 : class_2350.values()) {
               if (var9 != class_2350.field_11036) {
                  class_2338 var10 = var5.method_10093(var9);
                  if (var2.test(var10) && var3.test(var10)) {
                     return var10;
                  }
               }
            }
         }
      }

      return null;
   }

   private gw() {
   }
}
