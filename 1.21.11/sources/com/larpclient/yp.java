package com.larpclient;

import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class yp {
   private static final long B = 5000L;
   private static final long A = 2000L;
   private static final long Q = 1500L;
   private static long c;
   private static long J;
   private static long g;
   private static long i;
   private static long R;
   private static class_2561 X;

   private yp() {
   }

   public static void K(long var0) {
      X = class_2561.method_43470("$ ")
         .method_27692(class_124.field_1060)
         .method_10852(class_2561.method_43470(" " + t0.e(var0)).method_27692(class_124.field_1068));
      long var2 = System.currentTimeMillis();
      long var4 = ie.f();
      J = var2 + 2000L;
      c = var2 + 5000L + var4;
      i = var2 + var4;
      R = 0L;
      ie.w(var4, () -> {
         if (X != null && System.currentTimeMillis() < c) {
            h();
            class_746 var0x = class_310.method_1551().field_1724;
            if (var0x != null) {
               b1.q(X, false);
            }
         }
      });
   }

   public static void z() {
      X = null;
      g = 0L;
      J = 0L;
      c = 0L;
   }

   public static void g() {
      if (X == null || System.currentTimeMillis() >= c) {
         X = null;
      } else if (System.currentTimeMillis() >= i) {
         if (System.currentTimeMillis() - R >= 1500L) {
            h();
         }
      }
   }

   public static void Y(long var0) {
      g = System.currentTimeMillis() + var0;
   }

   public static boolean p(class_2561 var0, boolean var1) {
      return X != null && System.currentTimeMillis() < J || System.currentTimeMillis() < g;
   }

   private static void h() {
      class_746 var0 = class_310.method_1551().field_1724;
      if (var0 != null && X != null) {
         b1.q(X, true);
         R = System.currentTimeMillis();
      }
   }
}
