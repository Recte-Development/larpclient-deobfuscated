package com.larpclient;

import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_310;
import net.minecraft.class_2848.class_2849;

public final class bd {
   private static double j;
   private static double D;
   private static boolean g;
   private static boolean Q = (boolean)1;
   private static boolean B;
   private static float J;
   private static float b;
   private static boolean W;

   public static synchronized void I(class_2596<?> var0) {
      if (var0 instanceof class_2828 var1) {
         if (var1.method_36172()) {
            J = var1.method_12271(J);
            b = var1.method_12270(b);
            W = (boolean)1;
         }

         double var3 = var1.method_12268(j);
         if (var1.method_12273()) {
            D = 0.0;
         } else if (var1.method_36171() && g) {
            double var5 = j - var3;
            D = var5 < 0.0 ? 0.0 : D + var5;
         }

         if (var1.method_36171()) {
            j = var3;
            g = (boolean)1;
         }

         Q = var1.method_12273();
      } else if (var0 instanceof class_2848 var2) {
         if (var2.method_12365() == class_2849.field_12981) {
            B = (boolean)1;
         } else if (var2.method_12365() == class_2849.field_12985) {
            B = (boolean)0;
         }
      }
   }

   public static synchronized void l() {
      g = (boolean)0;
      D = 0.0;
      j = 0.0;
      Q = (boolean)1;
      B = (boolean)0;
      W = (boolean)0;
   }

   public static synchronized boolean t(boolean var0) {
      return B == var0;
   }

   public static synchronized class_243 a(class_243 var0) {
      return W ? class_243.method_1030(b, J) : var0;
   }

   public static boolean b(class_1657 var0, class_1297 var1) {
      if (!a(var0, var1)) {
         return false;
      }

      class_310 var2 = class_310.method_1551();
      return var2.method_1576() != null && com.larpclient.j.F("NoFall")
         ? false
         : t(var0.method_23318(), var0.method_18798().field_1351, var0.method_7261(0.0F));
   }

   public static boolean t(class_1657 var0, class_1297 var1) {
      return var0 != null
         && Y(
            var0.method_18798().field_1351,
            var0.method_7261(1.0F),
            var0.method_24828(),
            var0.method_6101(),
            var0.method_5799() || var0.method_5771(),
            var0.method_74025(),
            var0.method_5765(),
            var0.method_31549().field_7479 || var0.method_6128(),
            var1 instanceof class_1309
         );
   }

   public static boolean Y(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var0 < 0.0 && var2 >= 1.0F && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   public static synchronized boolean t(double var0, double var2, float var4) {
      return D(g, j, D, Q, B, var0, var2, var4);
   }

   public static boolean D(boolean var0, double var1, double var3, boolean var5, boolean var6, double var7, double var9, float var11) {
      return var0 && !var5 && !var6 && var3 > 0.0 && Math.abs(var7 - var1) <= 2.0E-4 && var9 < 0.0 && var11 >= 1.0F;
   }

   public static boolean a(class_1657 var0, class_1297 var1) {
      return var0 != null
         && F(
            var0.field_6017,
            var0.method_7261(0.5F),
            var0.method_24828(),
            var0.method_6101(),
            var0.method_5799(),
            var0.method_74025(),
            var0.method_5765(),
            var0.method_5624(),
            var1 instanceof class_1309
         );
   }

   public static boolean F(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var2 > 0.9F && var0 > 0.0 && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   private bd() {
   }
}
