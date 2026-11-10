package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class e_ {
   private static final y3 b = new y3();
   private static final qd.x[] e = new qd.x[3];
   private static final class_243[] n = new class_243[3];
   private static Object j;
   private static Object t;
   private static class_243 x;
   private static class_243 Z;
   private static double a;
   private static double h;
   private static double T;
   private static double X;
   private static double Y;
   private static double f;
   private static int L;
   private static int N;
   private static boolean U;
   private static boolean v;

   public static void Y() {
      b.I();
      t = null;
      j = null;
      Z = null;
      x = null;
      U = (boolean)0;
   }

   public static void i(float var0) {
      class_310 var1 = class_310.method_1551();
      SelfVisualsModule var2 = SelfVisualsModule.o();
      class_746 var3 = var1.field_1724;
      if (var2 != null && var2.Q() && var2.Q.G("Comets") && var3 != null && var3.method_5805() && !var3.method_5767() && !var3.method_7325()) {
         class_243 var4 = var3.method_73189();
         int var5 = j == var3 && t == var1.field_1687 && (x == null || !(x.method_1025(var4) > 64.0)) ? 0 : 1;
         if (var5 != 0) {
            Y();
         }

         j = var3;
         t = var1.field_1687;
         x = var4;
         Z = var3.method_30950(var0).method_1025(var4) > 64.0 ? var4 : var3.method_30950(var0);
         h = var3.method_17682();
         T = y3.H(var2.E.getValue(), h);
         X = var2.u.getValue();
         Y = var2.g.getValue();
         v = var2.l.getValue();
         L = var2.U.getValue() ? ez.a : var2.V.getValue();
         N = var2.U.getValue() ? ez.f : var2.J.getValue();
         a = qo.x();
         double var6 = Math.max(var2.s.getValue(), var3.method_17681() * 0.71 + T);
         b.v(a, var2.W.getValue(), var6, X);
         f = b.J(a);
         class_243 var8 = var1.field_1773.method_19418().method_71156();

         for (int var9 = 0; var9 < 3; var9++) {
            qd.x var10 = e[var9];
            var10.T();
            class_243 var11 = n[var9] = y3.u(b.D(var9), Z, h, T);

            for (int var12 = 0; var12 <= b.r(); var12++) {
               class_243 var13 = var12 == b.r() ? var11 : y3.u(b.J(var9, var12), Z, h, T);
               double var14 = var12 == b.r() ? 1.0 : Math.clamp(1.0 - (a - b.y(var12)) / X, 0.0, 1.0);
               var10.x(var13, T * 1.4 * var14 * var14, ei.R(ei.W(N, L, var14), var14 * var14 * Y * f));
            }

            var10.V();
            var10.k(var8);
         }

         U = (boolean)1;
      } else {
         Y();
      }
   }

   public static boolean C() {
      return U && v;
   }

   public static void p(f1 var0, boolean var1) {
      if (U && (!var1 || v)) {
         var0.blend(ti.d.l);

         for (int var2 = 0; var2 < 3; var2++) {
            class_243 var3 = n[var2];
            var0.G(e[var2], true);
            var0.U(var3.field_1352, var3.field_1351, var3.field_1350, T * 2.2, 0.0, ei.R(L, 0.35 * Y * f), gd.h.f, 0.0, 0.0, 0.0);
            var0.U(var3.field_1352, var3.field_1351, var3.field_1350, T, 0.0, ei.R(L, 0.9 * f), gd.h.x, 0.0, 0.0, 0.0);
            var0.U(var3.field_1352, var3.field_1351, var3.field_1350, T * 0.55, 0.0, ei.R(ei.W(L, L & 0xFF000000 | 16777215, 0.8), f), gd.h.f, 0.0, 0.0, 0.0);
         }

         var0.blend(ti.d.G);
      }
   }

   private e_() {
   }

   static {
      for (int var0 = 0; var0 < e.length; var0++) {
         e[var0] = new qd.x(257);
      }
   }
}
