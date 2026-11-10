package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public final class t9 {
   private static t9.v U;
   private static Module J;
   private static float F;
   private static float y;
   private static int G;
   private static long B;
   private static Object T;
   private static int z = Integer.MIN_VALUE;
   private static float n;
   private static float I;
   private static float m;
   private static float i;

   public static void N() {
      U = null;
      J = null;
   }

   public static boolean k(t9.v var0) {
      return U == var0;
   }

   public static boolean I() {
      return U != null;
   }

   public static boolean q(t9.v var0, t9.v var1) {
      return var1 == null || var0.ordinal() > var1.ordinal();
   }

   public static void X(t9.v var0, float var1, float var2, double var3) {
      class_746 var5 = class_310.method_1551().field_1724;
      if (var5 != null) {
         H(var0, var1, var2, var3, var5.method_36454(), var5.method_36455());
      }
   }

   public static void H(t9.v var0, float var1, float var2, double var3, float var5, float var6) {
      if (var0 == U || q(var0, U)) {
         U = var0;
         J = null;
         float[] var7 = e(var5, var6, var1, var2, var3);
         F = var7[0];
         y = var7[1];
      }
   }

   public static void O(Module var0, t9.v var1, float var2, float var3, double var4) {
      X(var1, var2, var3, var4);
      if (U == var1) {
         J = var0;
      }
   }

   public static float[] e(float var0, float var1, float var2, float var3, double var4) {
      double var6 = var4 <= 0.0 ? 1.0 : Math.clamp(var4, 0.0, 1.0);
      return new float[]{var0 + class_3532.method_15393(var2 - var0) * (float)var6, class_3532.method_15363(var1 + (var3 - var1) * (float)var6, -90.0F, 90.0F)};
   }

   public static void d() {
      class_310 var0 = class_310.method_1551();
      if (U != null && cr.o(J) && (J == null || J.isRuntimeActive())) {
         if (cr.o()) {
            l();
         } else {
            float var1 = var0.field_1724.method_36454();
            float var2 = var0.field_1724.method_36455();
            var0.field_1724.method_36456(F);
            var0.field_1724.method_36457(y);
            if (U == t9.v.e) {
               T = var0.field_1724;
               z = var0.field_1724.field_6012;
               n = var1;
               I = var2;
               m = F;
               i = y;
            }
         }
      }
   }

   public static float r(class_746 var0, float var1) {
      return !n(var0) ? Float.NaN : n + class_3532.method_15393(m - n) * class_3532.method_15363(var1, 0.0F, 1.0F);
   }

   public static float u(class_746 var0, float var1) {
      return !n(var0) ? Float.NaN : class_3532.method_16439(class_3532.method_15363(var1, 0.0F, 1.0F), I, i);
   }

   private static boolean n(class_746 var0) {
      return var0 == T && var0.field_6012 == z && !var0.method_5765() && var0.method_36454() == m && var0.method_36455() == i && !wp.d();
   }

   private static void l() {
      G++;
      long var0 = System.nanoTime();
      if (B == 0L || var0 - B >= 5000000000L) {
         q8.G("Rotation", "Held {} turn for your own right-click so the use and movement facing match ({} held since last note)", U, G);
         B = var0;
         G = 0;
      }
   }

   public static float[] X(class_243 var0, class_243 var1) {
      class_243 var2 = var1.method_1020(var0);
      return new float[]{
         (float)Math.toDegrees(Math.atan2(var2.field_1350, var2.field_1352)) - 90.0F,
         (float)(-Math.toDegrees(Math.atan2(var2.field_1351, Math.hypot(var2.field_1352, var2.field_1350))))
      };
   }

   private t9() {
   }

   public enum v {
      r,
      w,
      G,
      e,
      g,
      j,
      m,
      z,
      B,
      y,
      x,
      O,
      p;
   }
}
