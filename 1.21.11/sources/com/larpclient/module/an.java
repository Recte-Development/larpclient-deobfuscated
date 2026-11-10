package com.larpclient.module;

import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.fh;
import com.larpclient.gd;
import com.larpclient.qo;
import com.larpclient.ti;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.class_12178;
import net.minecraft.class_12180;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3726;

public final class an {
   private static Object f;
   private static int h;
   private static long e = Long.MIN_VALUE;
   private static class_238 J;
   private static double a;
   private static double I;
   private static boolean Y;
   private static fh.x s;
   private static int Z;
   private static class_243 q;

   public static void k() {
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(an::u);
   }

   private static void u(WorldRenderContext var0) {
      AuraModule var1 = AuraModule.R();
      class_310 var2 = class_310.method_1551();
      if (var1 != null && var1.isOperational() && var2.field_1724 != null && var2.field_1755 == null && !var2.field_1690.field_1842) {
         float var3 = var2.method_61966().method_60637(false);
         if (var1.V()) {
            class_243 var4 = var2.field_1724.method_30950(var3).method_1031(0.0, 0.12, 0.0);
            double var5 = var2.field_1724.method_36454() - var1.B() / 2.0;
            int var7 = Math.max(8, (int)Math.ceil(var1.B() / 6.0));

            for (int var8 = 0; var8 < var7; var8++) {
               h(var1, X(var4, var5 + var1.B() * var8 / var7, var1.P()), X(var4, var5 + var1.B() * (var8 + 1) / var7, var1.P()), ei.W(ez.a, -1, 0.3), var1.Z());
            }

            if (var1.B() < 360.0) {
               h(var1, var4, X(var4, var5, var1.P()), ei.W(ez.a, -1, 0.3), var1.Z());
               h(var1, var4, X(var4, var5 + var1.B(), var1.P()), ei.W(ez.a, -1, 0.3), var1.Z());
            }
         }
      }
   }

   public static void h() {
      AuraModule var0 = AuraModule.R();
      class_310 var1 = class_310.method_1551();
      if (var0 != null && var0.Y() && var1.field_1687 != null && var1.field_1724 != null && var1.field_1755 == null && !var1.field_1690.field_1842) {
         o(var0, var1);
         String var2 = var0.n();
         int var3 = var2.equals("Valid attack ray")
            ? ei.W(ez.a, -8327246, 0.35)
            : (var2.equals("Outside reach") ? ei.W(ez.a, -1277295, 0.55) : ei.W(ez.a, -12672, 0.5));
         Z = Z == 0 ? var3 : ei.W(Z, var3, 1.0 - Math.exp(-qo.Q() * 9.0));
      } else {
         s = null;
         f = null;
         Z = 0;
      }
   }

   private static void o(AuraModule var0, class_310 var1) {
      class_1309 var2 = var0.B();
      class_238 var3 = var2.method_5829();
      double var4 = var0.P();
      double var6 = var1.field_1724.method_5751();
      long var8 = var1.field_1687.method_75260();
      if (s == null || f != var1.field_1687 || h != var2.method_5628() || e != var8 || !var3.equals(J) || a != var4 || I != var6 || Y != var0.q()) {
         HashMap var10 = new HashMap();
         double var11 = var2.method_23318();
         class_3726 var13 = class_3726.method_16195(var1.field_1724);
         fh.v var14 = (var5, var7) -> {
            int var9 = (int)Math.floor(var5);
            int var10x = (int)Math.floor(var7);
            long var11x = class_2338.method_10064(var9, 0, var10x);
            List var13x = var10.computeIfAbsent(var11x, var6x -> {
               ArrayList var7x = new ArrayList();

               for (int var8x = (int)Math.floor(var11 - 2.5); var8x <= Math.ceil(var11 + 3.1); var8x++) {
                  class_2338 var9x = new class_2338(var9, var8x, var10x);
                  if (var1.field_1687.method_22340(var9x)) {
                     for (class_238 var11xx : var1.field_1687.method_8320(var9x).method_26194(var1.field_1687, var9x, var13).method_1090()) {
                        var7x.add(var11xx.method_989(var9, var8x, var10x));
                     }
                  }
               }

               return var7x;
            });
            return fh.d(var13x, var5, var7, var11);
         };
         s = fh.I(var3, var4, var6, var14, var0.q());
         if (f != var1.field_1687 || h != var2.method_5628()) {
            Z = 0;
         }

         f = var1.field_1687;
         h = var2.method_5628();
         e = var8;
         J = var3;
         a = var4;
         I = var6;
         Y = var0.q();
      }

      q = var3.method_1005();
   }

   public static boolean v() {
      AuraModule var0 = AuraModule.R();
      return s != null && !s.outline().isEmpty() && var0 != null && var0.f();
   }

   public static void b(f1 var0, boolean var1) {
      AuraModule var2 = AuraModule.R();
      if (s != null && var2 != null && (!var1 || var2.f())) {
         double var3 = var2.w() * (var1 ? 0.65 : 1.0);
         double var5 = var2.Y() * 0.035;
         double var7 = var2.m() ? qo.x() : 0.0;
         var0.blend(ti.d.l);

         for (fh.t var10 : s.outline()) {
            class_243 var11 = var10.from();
            class_243 var12 = var10.to();
            int var13 = S(var11, var7, var3, var2.m());
            int var14 = S(var12, var7, var3, var2.m());
            var0.I(var11.field_1352, var11.field_1351, var11.field_1350, var12.field_1352, var12.field_1351, var12.field_1350, var5, var5, var13, var14);
         }

         if (var2.m()) {
            for (int var42 = 0; var42 < 3; var42++) {
               double var44 = var7 * 0.55 + var42 * Math.PI * 2.0 / 3.0;
               double var47 = Math.cos(var44);
               double var48 = Math.sin(var44);
               double var16 = Double.POSITIVE_INFINITY;
               class_243 var18 = null;

               for (fh.t var20 : s.outline()) {
                  class_243 var21 = var20.from();
                  class_243 var22 = var20.to();
                  double var23 = var21.field_1352 - q.field_1352;
                  double var25 = var21.field_1350 - q.field_1350;
                  double var27 = var22.field_1352 - var21.field_1352;
                  double var29 = var22.field_1350 - var21.field_1350;
                  double var31 = var27 * var48 - var29 * var47;
                  double var33 = Math.abs(var31) < 1.0E-8 ? 0.5 : Math.clamp((var25 * var47 - var23 * var48) / var31, 0.0, 1.0);
                  class_243 var35 = var21.method_35590(var22, var33);
                  double var36 = var35.field_1352 - q.field_1352;
                  double var38 = var35.field_1350 - q.field_1350;
                  if (!(var36 * var47 + var38 * var48 <= 0.0)) {
                     double var40 = Math.abs(var36 * var48 - var38 * var47);
                     if (var40 < var16) {
                        var16 = var40;
                        var18 = var35;
                     }
                  }
               }

               if (var18 != null && var16 < 0.08) {
                  int var49 = ei.R(ei.W(Z, -1, 0.65), var3);
                  var0.U(var18.field_1352, var18.field_1351 + 0.018, var18.field_1350, var5 * 1.6, var7 * 0.6, ei.R(var49, 0.45), gd.h.f, 0.0, 0.0, 0.0);
                  var0.V(var18.field_1352, var18.field_1351 + 0.018, var18.field_1350, var5 * 0.6, var7 * 0.6, var49);
               }
            }
         }

         var0.blend(ti.d.G);
         if (!var1) {
            int var43 = ei.R(Z, var3 * 0.065);

            for (fh.u var46 : s.fill()) {
               m(var0, var46.a(), var43);
               m(var0, var46.b(), var43);
               m(var0, var46.c(), var43);
               m(var0, var46.c(), var43);
            }
         }
      }
   }

   private static void m(f1 var0, class_243 var1, int var2) {
      var0.vertex(var1.field_1352, var1.field_1351, var1.field_1350, var2);
   }

   private static int S(class_243 var0, double var1, double var3, boolean var5) {
      double var6 = Math.atan2(var0.field_1350 - q.field_1350, var0.field_1352 - q.field_1352);
      double var8 = var5 ? Math.pow(0.5 + 0.5 * Math.cos(var6 * 3.0 - var1 * 1.65), 12.0) : 0.0;
      return ei.R(ei.W(Z, -1, 0.15 + var8 * 0.55), var3 * (0.65 + var8 * 0.35));
   }

   private static void h(AuraModule var0, class_243 var1, class_243 var2, int var3, float var4) {
      class_12178 var5 = class_12180.method_75546(var1, var2, var3 | 0xFF000000, var4);
      if (var0.T()) {
         var5.method_75533();
      }
   }

   private static class_243 X(class_243 var0, double var1, double var3) {
      double var5 = Math.toRadians(var1);
      return var0.method_1031(-Math.sin(var5) * var3, 0.0, Math.cos(var5) * var3);
   }

   private an() {
   }
}
